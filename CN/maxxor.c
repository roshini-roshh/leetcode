#include <stdio.h>
#include <stdlib.h>

typedef struct TrieNode {
    struct TrieNode *child[2];
} TrieNode;

// Create a new Trie node
TrieNode* createNode() {
    TrieNode *node = (TrieNode*)malloc(sizeof(TrieNode));
    node->child[0] = NULL;
    node->child[1] = NULL;
    return node;
}

// Insert a number into Trie
void insert(TrieNode *root, int num) {
    TrieNode *curr = root;

    for (int i = 31; i >= 0; i--) {
        int bit = (num >> i) & 1;

        if (curr->child[bit] == NULL)
            curr->child[bit] = createNode();

        curr = curr->child[bit];
    }
}

// Find maximum XOR for one number
int findMaxXOR(TrieNode *root, int num) {
    TrieNode *curr = root;
    int maxXor = 0;

    for (int i = 31; i >= 0; i--) {
        int bit = (num >> i) & 1;

        // Prefer opposite bit
        if (curr->child[1 - bit] != NULL) {
            maxXor |= (1 << i);
            curr = curr->child[1 - bit];
        } else {
            curr = curr->child[bit];
        }
    }

    return maxXor;
}

int findMaximumXOR(int* nums, int numsSize) {
    TrieNode *root = createNode();

    for (int i = 0; i < numsSize; i++)
        insert(root, nums[i]);

    int ans = 0;

    for (int i = 0; i < numsSize; i++) {
        int value = findMaxXOR(root, nums[i]);
        if (value > ans)
            ans = value;
    }

    return ans;
}

int main() {
    int nums[] = {3,10,5,25,2,8};
    int n = sizeof(nums) / sizeof(nums[0]);

    printf("Maximum XOR = %d\n", findMaximumXOR(nums, n));

    return 0;
}
