class Solution {
    public int maxArea(int[] height) {
        int left=0;
        int right=height.length-1;
        int maxw=0;
        while(left<right)
        {
            int width=right-left;
            int cHeight=Math.min(height[left],height[right]);
            int currwater=width*cHeight;
            if(currwater>maxw){
                maxw=currwater;
            }
        if(height[left]<height[right])
        {
            left++;
        }
        else
        {
            right--;
        }
        }
        return maxw;

        
    }
    }
public class Main {
    public static void main(String[] args) {
        
        Solution solver = new Solution();

        
        int[] height1 = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int result1 = solver.maxArea(height1);
        System.out.println("Output for Example 1: " + result1);

        
        int[] height2 = {1, 1};
        int result2 = solver.maxArea(height2);
        System.out.println("Output for Example 2: " + result2); 
    }
}
