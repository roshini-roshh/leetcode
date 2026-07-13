#include <stdio.h>
#include <string.h>

int main()
{
    char data[100], frame[200];
    int i, j = 0, count = 0;

    printf("Enter bit stream: ");
    scanf("%s", data);

    // Starting Flag
    strcpy(frame, "01111110");
    j = 8;

    // Bit Stuffing
    for (i = 0; data[i] != '\0'; i++)
    {
        frame[j++] = data[i];

        if (data[i] == '1')
        {
            count++;
            if (count == 5)
            {
                frame[j++] = '0';
                count = 0;
            }
        }
        else
        {
            count = 0;
        }
    }

    // Ending Flag
    strcpy(&frame[j], "01111110");

    printf("\nHDLC Frame:\n%s\n", frame);

    return 0;
}
