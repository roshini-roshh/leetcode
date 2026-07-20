#include <stdio.h>
#include <string.h>

void binary_to_ascii(const char *binary, char *output_text)
{
    int i, bit;
    int text_idx = 0;

    // Process the binary string safely in blocks of 8 bits
    for (i = 0; binary[i] != '\0'; i += 8)
    {
        if (strlen(&binary[i]) < 8) break;

        char ch = 0;
        for (bit = 0; bit < 8; bit++)
        {
            ch <<= 1;
            if (binary[i + bit] == '1')
            {
                ch |= 1;
            }
        }
        output_text[text_idx++] = ch;
    }
    output_text[text_idx] = '\0';
}

int main()
{
    char flag[] = "01111110";
    char text[50];
    char data[400];
    char stuffed[500];
    char transmitted[600];
    char destuffed[400];
    char recovered_text[50];

    int i, j = 0, count = 0;
    int bin_idx = 0;
    int bit, error_choice;

    // Get input string from user
    printf("Enter String: ");
    scanf(" %[^\n]", text);

    printf("\n====BIT STUFFING====\n");
    printf("The entered input is: %s\n", text);

    // 1. Convert ASCII to Binary String
    for(i = 0; text[i] != '\0'; i++)
    {
        char ch = text[i];
        for(bit = 7; bit >= 0; bit--)
        {
            if((ch >> bit) & 1)
                data[bin_idx++] = '1';
            else
                data[bin_idx++] = '0';
        }
    }
    data[bin_idx] = '\0';
    printf("The binary conversion of the string is: %s\n", data);

    printf("\n====SENDER SIDE====\n");
    printf("Before stuffing binary of the string: %s%s%s\n", flag, data, flag);

    // 2. Bit Stuffing Process
    for(i = 0; data[i] != '\0'; i++)
    {
        stuffed[j++] = data[i];

        if(data[i] == '1')
        {
            count++;
            if(count == 5)
            {
                stuffed[j++] = '0';
                count = 0;
            }
        }
        else
        {
            count = 0;
        }
    }
    stuffed[j] = '\0';

    // Construct transmitted frame
    strcpy(transmitted, flag);
    strcat(transmitted, stuffed);
    strcat(transmitted, flag);

    printf("After stuffing......\n");
    printf("Sender Output (Transmitted Frame): %s\n", transmitted);

    // 3. Channel Simulation (Single Bit Error Choice)
    printf("\n================ CHANNEL SIMULATION ================\n");
    printf("Choose an environment state:\n");
    printf(" 0. Clean/Perfect Transmission\n");
    printf(" 1. Inject Single-Bit Error\n");
    printf("Enter Choice: ");
    scanf("%d", &error_choice);

    int was_error_injected = 0;
    if (error_choice == 1)
    {
        // Altering the bit to an illegal character 'X' to corrupt it
        int flip_pos = 12;
        transmitted[flip_pos] = 'X';
        was_error_injected = 1;
        printf(">>> Simulated channel error: Character at payload index %d changed to 'X' <<<\n", flip_pos);
    }
    else if(error_choice == 0)
    {
        printf(">>> Clean frame transmission over network preserved <<<\n");
    }
    else {
        printf("Invalid choice. Proceeding with clean transmission.\n");
    }

    // 4. Receiver Side (Extract Payload & Verification Check)
    printf("\n=================== RECEIVER OUTPUT =================\n");
    printf("Received bits over line:        %s\n", transmitted);

    int error_detected = 0;
    int trans_len = strlen(transmitted);
    char received_stuffed[500];

    // Verify framing rules are intact (Check header and trailer flags)
    if (strncmp(transmitted, flag, 8) != 0 || strncmp(&transmitted[trans_len - 8], flag, 8) != 0) {
        error_detected = 1;
    }

    // Safely extract the payload while monitoring for corrupted invalid characters
    for(i = 8; i < trans_len - 8; i++)
    {
        if (transmitted[i] == 'X') {
            error_detected = 1;
        }
        received_stuffed[i - 8] = transmitted[i];
    }
    received_stuffed[trans_len - 16] = '\0';

    // Discard logic if error flag is tripped
    if (error_detected)
    {
        printf("\n[ERROR DETECTED] Frame corruption found during transit!\n");
        printf("Receiver Actions: Discarding the corrupted message entirely.\n");
    }
    else
    {
        // Run Destuffing Loop only if frame is clean
        count = 0;
        j = 0;
        for(i = 0; received_stuffed[i] != '\0'; i++)
        {
            destuffed[j++] = received_stuffed[i];

            if(received_stuffed[i] == '1')
            {
                count++;
                if(count == 5)
                {
                    i++; // Advance pointer to skip the stuffed '0' bit
                    count = 0;
                }
            }
            else
            {
                count = 0;
            }
        }
        destuffed[j] = '\0';

        // Convert Restored Binary back to ASCII
        binary_to_ascii(destuffed, recovered_text);

        printf("Destuffed successfully.\n");
        printf("Receiver Output (Binary Data):  %s\n", destuffed);
        printf("Recovered word:                 %s\n", recovered_text);
    }

    return 0;
}
