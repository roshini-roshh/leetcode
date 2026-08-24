#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include <math.h>

#define MAX_SIZE 100
#define MAX_BITS (MAX_SIZE * 8)

int main() {
    int choice;
    char str_input[MAX_SIZE];
    int data_bits[MAX_BITS] = {0};
    int m = 0;

    printf("=== DYNAMIC HAMMING CODE SYSTEM ===\n");
    printf("1. Straight String Input\n");
    printf("2. Direct Binary Stream Input (e.g., Test (7,4) input '1011' or (12,8) '10110010')\n");
    printf("3. Alphanumeric Input\n");
    printf("Enter choice (1, 2, or 3): ");
    if (scanf("%d", &choice) != 1) return 1;

    while (getchar() != '\n');

    switch (choice) {
        case 1:
            printf("\nEnter straight text string: ");
            if (fgets(str_input, sizeof(str_input), stdin) == NULL) return 1;
            str_input[strcspn(str_input, "\n")] = '\0';
            int len = strlen(str_input);
            for (int i = 0; i < len; i++) {
                char ch = str_input[i];
                for (int b = 7; b >= 0; b--) {
                    data_bits[m++] = (ch >> b) & 1;
                }
            }
            break;

        case 2:
            printf("\nEnter raw binary stream: ");
            if (fgets(str_input, sizeof(str_input), stdin) == NULL) return 1;
            str_input[strcspn(str_input, "\n")] = '\0';
            m = strlen(str_input);
            for (int i = 0; i < m; i++) {
                if (str_input[i] != '1' && str_input[i] != '0') {
                    printf("Error: Non-binary character.\n");
                    return 1;
                }
                data_bits[i] = str_input[i] - '0';
            }
            break;

        case 3:
            printf("\nEnter alphanumeric string: ");
            if (fgets(str_input, sizeof(str_input), stdin) == NULL) return 1;
            str_input[strcspn(str_input, "\n")] = '\0';
            int alen = strlen(str_input);
            for (int i = 0; i < alen; i++) {
                if (!isalnum((unsigned char)str_input[i])) return 1;
                char ch = str_input[i];
                for (int b = 7; b >= 0; b--) {
                    data_bits[m++] = (ch >> b) & 1;
                }
            }
            break;

        default:
            return 1;
    }

    printf("\nTotal Data Bits Count (m) = %d\n", m);

    // --- TRIAL AND ERROR LOOP ---
    int p = 1;
    printf("\n--- Running Trial & Error Execution Loop ---\n");
    while (1) {
        int left_side = (int)pow(2, p);
        int right_side = m + p + 1;
        printf("  Testing p = %d: 2^%d (%d) >= %d + %d + 1 (%d) ? ", p, p, left_side, m, p, right_side);

        if (left_side >= right_side) {
            printf("PASSED!\n");
            break;
        } else {
            printf("FAILED\n");
            p++;
        }
    }

    int total_bits = m + p;
    printf("Optimal Parity Bit Count found: p = %d\n", p);
    printf("Resulting Code Word Format Structure: (%d,%d)\n", total_bits, m);

    // --- CONSTRUCT SENDER HAMMING FRAME (RIGHT-TO-LEFT) ---
    int tx_frame[MAX_BITS] = {0};
    int data_ptr = m - 1;

    // Map out positions 1-indexed from the right side moving left
    for (int i = 1; i <= total_bits; i++) {
        if ((i & (i - 1)) == 0) {
            tx_frame[i] = -1; // Flag placeholder value for empty parity slots
        } else {
            tx_frame[i] = data_bits[data_ptr--];
        }
    }

    // Print the Unfilled frame layout style
    printf("\nUnfilled frame structure (Parity bits empty): ");
    for (int i = total_bits; i >= 1; i--) {
        if (tx_frame[i] == -1) {
            printf("_");
        } else {
            printf("%d", tx_frame[i]);
        }
    }
    printf("\n");

    // 🌐 STEP-BY-STEP PARITY ANALYSIS ENGINE 🌐
    printf("\n--- Step-by-Step Parity Bit Calculation ---\n");
    for (int i = 0; i < p; i++) {
        int parity_pos = (int)pow(2, i);
        int ones_count = 0;

        printf("  P%d (Position %d from right) checks indices: ", parity_pos, parity_pos);

        // Print the positions checking mapping layout first
        int printed_first = 0;
        for (int j = 1; j <= total_bits; j++) {
            if ((j & parity_pos) != 0 && j != parity_pos) {
                if (printed_first) printf(", ");
                printf("%d", j);
                printed_first = 1;
            }
        }

        // Print values corresponding to those indices
        printf("\n    -> Corresponding values: ");
        printed_first = 0;
        for (int j = 1; j <= total_bits; j++) {
            if ((j & parity_pos) != 0 && j != parity_pos) {
                if (printed_first) printf(", ");
                printf("Bit(Index %d)=%d", j, tx_frame[j]);
                if (tx_frame[j] == 1) ones_count++;
                printed_first = 1;
            }
        }

        // Calculate the even parity logic output
        int calculated_parity = ones_count % 2;
        tx_frame[parity_pos] = calculated_parity;
        printf("\n    -> Total 1s count = %d | Even Parity Bit P%d becomes = %d\n\n",
               ones_count, parity_pos, calculated_parity);
    }

    // Print the Filled frame layout style
    printf("Final transmiting frame:                  ");
    for (int i = total_bits; i >= 1; i--) printf("%d", tx_frame[i]);
    printf("\n");


    // --- AUTOMATED RECEIVER SIMULATION & CORRUPTION MATRIX ---
    printf("\n===============================================\n");
    printf("--- RECEIVER SIDE SIMULATION AND EVALUATION ---\n");
    printf("===============================================\n");

    int rx_frame[MAX_BITS];
    for (int i = 1; i <= total_bits; i++) rx_frame[i] = tx_frame[i];

    int errors_to_inject = 0;
    printf("How many bit errors do you want to auto-inject? (0, 1, 2, etc.): ");
    if (scanf("%d", &errors_to_inject) != 1) return 1;

    for (int e = 0; e < errors_to_inject; e++) {
        int target_position;
        printf("  Enter bit index position to flip (Choose 1 to %d counting from RIGHT): ", total_bits);
        if (scanf("%d", &target_position) == 1 && target_position >= 1 && target_position <= total_bits) {
            rx_frame[target_position] ^= 1;
            printf("  [Bit at index %d from right flipped successfully]\n", target_position);
        }
    }

    // --- SYNDROME ERROR CALCULATION (RIGHT-TO-LEFT) ---
    int error_position = 0;
    for (int i = 0; i < p; i++) {
        int parity_pos = (int)pow(2, i);
        int check_sum = 0;

        for (int j = 1; j <= total_bits; j++) {
            if ((j & parity_pos) != 0) {
                if (rx_frame[j] == 1) check_sum++;
            }
        }
        if (check_sum % 2 != 0) {
            error_position += parity_pos;
        }
    }

    printf("\n--- Final Integrity Audit Matrix ---\n");
    printf("Transmitted Frame Stream : ");
    for (int i = total_bits; i >= 1; i--) printf("%d", tx_frame[i]);
    printf("\nReceived Frame Stream    : ");
    for (int i = total_bits; i >= 1; i--) printf("%d", rx_frame[i]);
    printf("\nCalculated Error Syndrome Position = %d (from right)\n", error_position);

    if (error_position == 0) {
        printf("\nResult: STATUS OK (Clean transmission frame confirmed).\n");

        printf("\n--- Receiver Data Decryption ---\n");
        printf("Received Decoded Bits: ");
        for (int i = total_bits; i >= 1; i--) {
            if ((i & (i - 1)) != 0) printf("%d", rx_frame[i]);
        }
        printf("\n>>>> MESSAGE ACCEPTED <<<<\n");
    } else {
        printf("\nResult: ERROR DETECTED! (Syndrome evaluation caught data modification at position %d from right).\n", error_position);
        printf(">>>> MESSAGE DISCARDED <<<<\n");
    }

    return 0;
}
