#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

#define MAX_SIZE 100
#define MAX_BITS (MAX_SIZE * 8)
#define MAX_POLY 32

// Function to perform Binary Division (XOR division) used by CRC
void perform_crc_division(const char *input_frame, const char *poly, char *remainder, int poly_degree) {
    int frame_len = strlen(input_frame);
    int poly_len = strlen(poly);

    char temp_frame[MAX_BITS + MAX_POLY + 1];
    strcpy(temp_frame, input_frame);

    for (int i = 0; i <= frame_len - poly_len; i++) {
        if (temp_frame[i] == '1') {
            for (int j = 0; j < poly_len; j++) {
                if (temp_frame[i + j] == poly[j]) {
                    temp_frame[i + j] = '0';
                } else {
                    temp_frame[i + j] = '1';
                }
            }
        }
    }

    strncpy(remainder, &temp_frame[frame_len - poly_degree], poly_degree);
    remainder[poly_degree] = '\0';
}

// Function to evaluate if a polynomial is a "Good Generator"
int evaluate_polynomial_quality(const char *poly) {
    int poly_len = strlen(poly);
    int term_count = 0;

    printf("\n--- Polynomial Integrity Audit ---\n");

    // Rule 3: x^0 coefficient is 1 (Last bit must be '1')
    int rule3_passed = (poly[poly_len - 1] == '1');
    printf("Rule 3: x^0 coefficient is 1? -> %s\n", rule3_passed ? "PASSED" : "FAILED");

    // Rule 2: At least two terms (Count number of '1's)
    for (int i = 0; i < poly_len; i++) {
        if (poly[i] == '1') term_count++;
    }
    int rule2_passed = (term_count >= 2);
    printf("Rule 2: At least two terms? -> %s (Found %d terms)\n", rule2_passed ? "PASSED" : "FAILED", term_count);

    // Rule 1: x + 1 is contained in the entered polynomial (Requires an even number of terms)
    int rule1_passed = (term_count % 2 == 0);
    printf("Rule 1: (x + 1) is a factor?   -> %s\n", rule1_passed ? "PASSED" : "FAILED");

    if (rule1_passed && rule2_passed && rule3_passed) {
        printf("\n>>>> VERDICT: GOOD POLYNOMIAL GENERATOR <<<<\n");
        return 1;
    } else {
        printf("\n>>>> VERDICT: POOR POLYNOMIAL GENERATOR <<<<\n");
        return 0;
    }
}

int main() {
    int choice;
    char str_input[MAX_SIZE];
    char polynomial[MAX_POLY];
    char tx_bit_stream[MAX_BITS] = "";
    char tx_frame_appended[MAX_BITS + MAX_POLY + 1] = "";
    char rx_frame_stream[MAX_BITS + MAX_POLY + 1] = "";
    int len = 0;

    printf("=== DYNAMIC CRC ANALYSIS SYSTEM ===\n");
    printf("1. Straight String Input (e.g., 'Hi')\n");
    printf("2. Direct Binary Stream Input (e.g., '10110010')\n");
    printf("3. Alphanumeric Input (Strictly Letters/Numbers, e.g., 'A1')\n");
    printf("Enter choice (1, 2, or 3): ");
    if (scanf("%d", &choice) != 1) {
        printf("Invalid choice input.\n");
        return 1;
    }

    while (getchar() != '\n');

    // --- STEP 1: INPUT GATHERING SWITCH ---
    switch (choice) {
        case 1:
            printf("\nEnter straight text string: ");
            if (fgets(str_input, sizeof(str_input), stdin) == NULL) return 1;
            str_input[strcspn(str_input, "\n")] = '\0';
            len = strlen(str_input);
            if (len == 0) { printf("Error: Input is empty.\n"); return 1; }

            printf("\n--- Character to ASCII & Binary Breakdown ---\n");
            for (int i = 0; i < len; i++) {
                char ch = str_input[i];
                printf("Character: '%c' | ASCII: %3d | Binary: ", ch, ch);
                for (int b = 7; b >= 0; b--) {
                    char bit = ((ch >> b) & 1) ? '1' : '0';
                    printf("%c", bit);
                    strncat(tx_bit_stream, &bit, 1);
                }
                printf("\n");
            }
            break;

        case 2:
            printf("\nEnter raw binary stream (e.g., 10110010): ");
            if (fgets(str_input, sizeof(str_input), stdin) == NULL) return 1;
            str_input[strcspn(str_input, "\n")] = '\0';
            int bit_count = strlen(str_input);
            if (bit_count == 0) { printf("Error: Input is empty.\n"); return 1; }

            for (int i = 0; i < bit_count; i++) {
                if (str_input[i] != '1' && str_input[i] != '0') {
                    printf("Error: Non-binary character detected.\n");
                    return 1;
                }
            }
            strcpy(tx_bit_stream, str_input);
            break;

        case 3:
            printf("\nEnter alphanumeric string: ");
            if (fgets(str_input, sizeof(str_input), stdin) == NULL) return 1;
            str_input[strcspn(str_input, "\n")] = '\0';
            len = strlen(str_input);
            if (len == 0) { printf("Error: Input is empty.\n"); return 1; }

            for (int i = 0; i < len; i++) {
                if (!isalnum((unsigned char)str_input[i])) {
                    printf("Error: Non-alphanumeric character '%c' detected.\n", str_input[i]);
                    return 1;
                }
            }

            printf("\n--- Alphanumeric to ASCII & Binary Breakdown ---\n");
            for (int i = 0; i < len; i++) {
                char ch = str_input[i];
                printf("Alphanumeric: '%c' | ASCII: %3d | Binary: ", ch, ch);
                for (int b = 7; b >= 0; b--) {
                    char bit = ((ch >> b) & 1) ? '1' : '0';
                    printf("%c", bit);
                    strncat(tx_bit_stream, &bit, 1);
                }
                printf("\n");
            }
            break;

        default:
            printf("Invalid selection option.\n");
            return 1;
    }

    // --- STEP 2: USER POLYNOMIAL INPUT & ANALYSIS ---
    printf("\nEnter custom Generator Polynomial in binary (e.g., CRC-4 is '10011'): ");
    scanf("%31s", polynomial);

    int poly_len = strlen(polynomial);
    for (int i = 0; i < poly_len; i++) {
        if (polynomial[i] != '0' && polynomial[i] != '1') {
            printf("Error: Polynomial must be strictly in binary.\n");
            return 1;
        }
    }

    int poly_degree = poly_len - 1;
    if (poly_degree <= 0) {
        printf("Error: Polynomial degree must be greater than 0.\n");
        return 1;
    }

    evaluate_polynomial_quality(polynomial);

    // --- STEP 3: TRANSMITTER SIDE CRC GENERATION ---
    int msg_bit_len = strlen(tx_bit_stream);

    strcpy(tx_frame_appended, tx_bit_stream);
    for (int i = 0; i < poly_degree; i++) {
        strcat(tx_frame_appended, "0");
    }

    char generated_checksum[MAX_POLY];
    perform_crc_division(tx_frame_appended, polynomial, generated_checksum, poly_degree);

    strcpy(tx_frame_appended, tx_bit_stream);
    strcat(tx_frame_appended, generated_checksum);

    printf("\nFinal transmiting frame: %s%s\n", tx_bit_stream, generated_checksum);

    // --- STEP 4: AUTOMATED RECEIVER SIMULATION & ERROR INJECTION ---
    printf("\n===============================================\n");
    printf("--- RECEIVER SIDE SIMULATION AND EVALUATION ---\n");
    printf("===============================================\n");

    strcpy(rx_frame_stream, tx_frame_appended);
    int total_frame_bits = strlen(rx_frame_stream);

    int errors_to_inject = 0;
    printf("How many bit errors do you want to auto-inject? (0, 1, 2, 3, etc.): ");
    if (scanf("%d", &errors_to_inject) != 1) return 1;

    for (int e = 0; e < errors_to_inject; e++) {
        int target_index;
        printf("  Enter bit index position to flip (Choose 0 to %d): ", total_frame_bits - 1);
        if (scanf("%d", &target_index) == 1 && target_index >= 0 && target_index < total_frame_bits) {
            rx_frame_stream[target_index] = (rx_frame_stream[target_index] == '0') ? '1' : '0';
            printf("  [Bit at position %d flipped successfully]\n", target_index);
        } else {
            printf("  Invalid index position. Error injection skipped.\n");
        }
    }

    int error_count = 0;
    for (int i = 0; i < total_frame_bits; i++) {
        if (tx_frame_appended[i] != rx_frame_stream[i]) {
            error_count++;
        }
    }

    char receiver_remainder[MAX_POLY];
    perform_crc_division(rx_frame_stream, polynomial, receiver_remainder, poly_degree);

    int balance_check_zero = 1;
    for (int i = 0; i < poly_degree; i++) {
        if (receiver_remainder[i] != '0') {
            balance_check_zero = 0;
            break;
        }
    }

    printf("\n--- Final Integrity Audit Matrix ---\n");
    printf("Transmitted Frame Stream : %s\n", tx_frame_appended);
    printf("Received Frame Stream    : %s\n", rx_frame_stream);
    printf("Receiver Division Synergy: Remainder = %s\n", receiver_remainder);
    printf("Total Corrupted Bit Switches Detected: %d\n", error_count);

    // --- STEP 5: CRITICAL INTEGRITY TESTING & DECISION MATRIX ---
    if (balance_check_zero == 1) {
        if (error_count == 0) {
            printf("\nResult: STATUS OK\n");
        } else {
            printf("\nResult: UNCAUGHT BURST EXCEPTION! STATUS PASSED (SYSTEM FOOLED).\n");
        }

        // DECODING ONLY HAPPENS IF PASSED
        printf("\n--- Receiver Data Decryption ---\n");
        if (msg_bit_len % 8 != 0) {
            printf("Received Payload (Raw bits): ");
            for (int i = 0; i < msg_bit_len; i++) printf("%c", rx_frame_stream[i]);
            printf("\n");
        } else {
            printf("Received Decoded String: \"");
            for (int c = 0; c < msg_bit_len / 8; c++) {
                int ascii_val = 0;
                for (int b = 0; b < 8; b++) {
                    ascii_val = (ascii_val << 1) | (rx_frame_stream[c * 8 + b] - '0');
                }
                printf("%c", (ascii_val >= 32 && ascii_val <= 126) ? (char)ascii_val : '?');
            }
            printf("\"\n");
        }
        printf(">>>> MESSAGE ACCEPTED <<<<\n");

    } else {

        printf("\nResult: ERROR DETECTED! (The remainder division is non-zero).\n");
        if (error_count == 1)
        {
        printf("Proof: Verified! CRC successfully detected a Single Bit Error.\n");
        }
        else if (error_count % 2 != 0)
        {
        printf("Proof: Verified! CRC successfully caught an ODD Numbered Error [%d errors].\n", error_count);
        }
        else
        {       printf("Proof: CRC successfully caught an Even Multi-Bit Burst Error [%d errors].\n", error_count);
        }
        printf(">>>> MESSAGE DISCARDED <<<<\n");
}
return 0;
}
