import java.util.HashSet;
import java.util.Set;

class Solution {
    public int subarrayBitwiseORs(int[] arr) {
        Set<Integer> ans = new HashSet<>();
        Set<Integer> cur = new HashSet<>();
        
        for (int x : arr) {
            Set<Integer> nextSet = new HashSet<>();
            nextSet.add(x);
            
            // Generate all OR combinations ending at the current element
            for (int y : cur) {
                nextSet.add(x | y);
            }
            
            // Move current combinations to the global unique results set
            cur = nextSet;
            ans.addAll(cur);
        }
        
        return ans.size();
    }
}
