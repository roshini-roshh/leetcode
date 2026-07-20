#include <stdio.h>
#include <string.h>
#include <stdlib.h> // Required for rand() and srand()
#include <time.h>   // Required for time() to seed random engine

// Helper function to print a character array in 8-bit binary format
void print_string_as_binary(const char *str, int len) {
    for (int i = 0; i < len; i++) {
        for (int b = 7; b >= 0; b--) {
            printf("%d", (str[i] >> b) & 1);
        }
        printf(" ");
    }
    printf("\n");
}

int main()
{
    char flag = 'F';   // Frame delimiter marker (ASCII 70 -> 01000110)
    char esc = 'E';    // Escape marker byte      (ASCII 69 -> 01000101)

    char text[100];
    char stuffed[200];
    char transmitted[200];
    char received_stuffed[200];
    char destuffed[100];

    int i, j = 0, error_choice;

    // Seed the random number generator using system time
    srand(time(NULL));

    // Get input string from user
    printf("\nEnter String: ");
    scanf(" %[^\n]", text);

    printf("\n==== BYTE STUFFING ====\n");
    printf("The entered input is: %s\n", text);
    printf("Original Text in Binary: ");
    print_string_as_binary(text, strlen(text));

    printf("\nFlag byte: '%c' (Binary: ", flag);
    print_string_as_binary(&flag, 1);
    printf("Escape byte: '%c' (Binary: ", esc);
    print_string_as_binary(&esc, 1);

    printf("\n==== SENDER SIDE ====\n");

    // 1. Byte Stuffing Process
    for (i = 0; text[i] != '\0'; i++)
    {
        // Rule: If current char is a flag or an escape, stuff an escape byte first
        if (text[i] == flag || text[i] == esc)
        {
            stuffed[j++] = esc; // Stuffing escape byte
        }
        stuffed[j++] = text[i]; // Copying actual character
    }
    stuffed[j] = '\0';
    int stuffed_len = j;

    // Construct the fully transmitted frame safely to avoid strcat vulnerabilities
    int t = 0;
    transmitted[t++] = flag; // Starting flag
    for (int k = 0; k < stuffed_len; k++) {
        transmitted[t++] = stuffed[k]; // Payload
    }
    transmitted[t++] = flag; // Ending flag
    transmitted[t] = '\0';
    int trans_len = t;

    printf("After stuffing (Stuffed Payload Text): %s\n", stuffed);
    printf("After stuffing (Stuffed Payload Binary): ");
    print_string_as_binary(stuffed, stuffed_len);

    printf("\nSender Output (Transmitted Frame Text): %s\n", transmitted);
    printf("Sender Output (Transmitted Frame Binary): ");
    print_string_as_binary(transmitted, trans_len);


    // 2. Channel Simulation (Random Single Character Error Choice)
    printf("\n================ CHANNEL SIMULATION ================\n");
    printf("Choose an environment state:\n");
    printf(" 0. Clean/Perfect Transmission\n");
    printf(" 1. Inject Random Single-Character Error\n");
    printf("Enter Choice: ");
    scanf("%d", &error_choice);

    if (error_choice == 1)
    {
        if (stuffed_len > 0)
        {
            // Pick a random payload position (shifts by 1 to skip the front 'F' flag)
            int random_pos = 1 + (rand() % stuffed_len);

            transmitted[random_pos] = 'X'; // Injecting error character
            printf(">>> Simulated channel error: Character at random payload index %d changed to 'X' <<<\n", random_pos - 1);
        }
    }
    else
    {
        printf(">>> Clean frame transmission over network preserved <<<\n");
    }


    // 3. Receiver Side (De-framing, Error Detection, and De-stuffing)
    printf("\n=================== RECEIVER OUTPUT =================\n");
    printf("Received characters over line (Text): %s\n", transmitted);
    printf("Received bits over line (Binary): ");
    print_string_as_binary(transmitted, trans_len);

    int error_detected = 0;

    // Verify framing rules are intact (Check flags)
    if (transmitted[0] != flag || transmitted[trans_len - 1] != flag) {
        error_detected = 1;
    }

    // Extract the payload while scanning for corrupted 'X' characters
    int rx_stuffed_len = 0;
    for (i = 1; i < trans_len - 1; i++)
    {
        if (transmitted[i] == 'X') {
            error_detected = 1; // Catching injected data corruption
        }
        received_stuffed[rx_stuffed_len++] = transmitted[i];
    }
    received_stuffed[rx_stuffed_len] = '\0';

    // Discard logic if error flag is triggered
    if (error_detected) {
        printf("\n[ERROR DETECTED] Frame corruption found during transit!\n");
        printf("Receiver Actions: DISCARDING the corrupted message entirely.\n");
    }
    else {
        // Run Destuffing Loop only if frame is completely clean
        j = 0;
        for (i = 0; received_stuffed[i] != '\0'; i++)
        {
            // Rule: If an escape byte is encountered, skip it and take the next character directly
            if (received_stuffed[i] == esc)
            {
                i++; // Advance pointer to skip the stuffed escape byte
            }
            destuffed[j++] = received_stuffed[i];
        }
        destuffed[j] = '\0';

        printf("\nDestuffed successfully.\n");
        printf("Receiver Output (Decoded Text): %s\n", destuffed);
        printf("Receiver Output (Decoded Binary): ");
        print_string_as_binary(destuffed, j);
    }

    return 0;
}
