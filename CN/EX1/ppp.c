#include <stdio.h>
#include <string.h>

#define FLAG 'F'
#define ESC 'E'

int main()
{
    char data[100], frame[200];
    int i, j = 0;

    printf("Enter data: ");
    scanf("%s", data);

    // Start Flag
    frame[j++] = FLAG;

    // Byte Stuffing
    for (i = 0; data[i] != '\0'; i++)
    {
        if (data[i] == FLAG || data[i] == ESC)
        {
            frame[j++] = ESC;
        }

        frame[j++] = data[i];
    }

    // End Flag
    frame[j++] = FLAG;
    frame[j] = '\0';

    printf("\nPPP Frame:\n%s\n", frame);

    return 0;
}
