#include <stdio.h>

int findComplement(int num) {
    int mask = 1;

    // Create a mask with all bits as 1
    while (mask < num) {
        mask = (mask << 1) | 1;
    }

    return mask ^ num;
}
int main() {
    int num;

    printf("Enter a number: ");
    scanf("%d", &num);

    printf("Complement = %d\n", findComplement(num));

    return 0;
}
