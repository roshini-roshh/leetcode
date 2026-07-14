class Solution {
    public int lengthOfLongestSubstring(String s) {
    int n = s.length();
    int maxLen = 0;
    // Map to store character and its last seen index
    int[] charMap = new int[128]; 
    java.util.Arrays.fill(charMap, -1);
    
    for (int left = 0, right = 0; right < n; right++) {
        char currentChar = s.charAt(right);
        
        if (charMap[currentChar] >= left) {
            left = charMap[currentChar] + 1;
        }
        
        charMap[currentChar] = right;
        maxLen = Math.max(maxLen, right - left + 1);
    }
    
    return maxLen;
}

}
