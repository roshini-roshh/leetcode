class Solution {
    public boolean hasAlternatingBits(int n) {
        int lastBit = n & 1;
        n >>>= 1; // Unsigned right shift
        
        while (n > 0) {
            int currentBit = n & 1;
            // If adjacent bits are the same, return false
            if (currentBit == lastBit) {
                return false;
            }
            lastBit = currentBit;
            n >>>= 1;
        }
        
        return true;
    }
}
