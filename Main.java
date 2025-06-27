import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println(new Solution().solve(input));
    }
}

class Solution {
    public String solve(String input) {
        StringBuilder sb = new StringBuilder();
        char[] s = input.toCharArray();
        int i = 0;
        
        while (i < s.length) {
            char curr = s[i];
            int count = 1;
        
            while (i + 1 < s.length && s[i + 1] == curr) {
                i++;
                count++;
            }
        
            sb.append(curr);
            if (count > 1) {
                sb.append(count);
            }
        
            i++;
        }
        
        return sb.toString();
    }
}
