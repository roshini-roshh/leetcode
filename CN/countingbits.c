#include <stdio.h>
#include <stdlib.h>

int* countBits(int n, int* returnSize) {
    *returnSize = n + 1;

    int *ans = (int*)malloc((n + 1) * sizeof(int));

    ans[0] = 0;

    for (int i = 1; i <= n; i++) {
        ans[i] = ans[i / 2] + (i % 2);
    }

    return ans;
}

int main() {
    int n;
    printf("Enter n: ");
    scanf("%d", &n);

    int returnSize;
    int *result = countBits(n, &returnSize);

    printf("[");
    for (int i = 0; i < returnSize; i++) {
        printf("%d", result[i]);
        if (i != returnSize - 1)
            printf(",");
    }
    printf("]\n");

    free(result);

    return 0;
}
