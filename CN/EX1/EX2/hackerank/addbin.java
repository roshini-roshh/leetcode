class Solution {
    public String addBinary(String a, String b) {
        StringBuilder result = new StringBuilder();
        int carry = 0;
        int i = a.length() - 1;
        int j = b.length() - 1;

        // Loop as long as there are characters to process or a carry remains
        while (i >= 0 || j >= 0 || carry > 0) {
            int total = carry;
            
            if (i >= 0) {
                total += a.charAt(i) - '0'; // Convert char to int
                i--;
            }
            if (j >= 0) {
                total += b.charAt(j) - '0'; // Convert char to int
                j--;
            }

            result.append(total % 2); // Append the current binary digit
            carry = total / 2;        // Calculate the next carry
        }

        // The digits were added right-to-left, so reverse for the final answer
        return result.reverse().toString();
    }
}
