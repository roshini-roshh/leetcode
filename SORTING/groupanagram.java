import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        if (strs == null || strs.length == 0) {
            return new ArrayList<>();
        }
        
        Map<String, List<String>> map = new HashMap<>();
        
        for (String s : strs) {
            // Count frequency of each character (a-z)
            int[] count = new int[26];
            for (char c : s.toCharArray()) {
                count[c - 'a']++;
            }
            
            // Build a unique string identifier based on counts
            // Example: "abbc" becomes "#1#2#1#0#0..."
            StringBuilder sb = new StringBuilder();
            for (int val : count) {
                sb.append('#');
                sb.append(val);
            }
            
            String key = sb.toString();
            
            // Group strings by their unique identifier
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(s);
        }
        
        return new ArrayList<>(map.values());
    }
}
