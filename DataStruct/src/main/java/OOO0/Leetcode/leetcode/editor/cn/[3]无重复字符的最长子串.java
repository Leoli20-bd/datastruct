package OOO0.Leetcode.leetcode.editor.cn;//给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
//
// 
//
// 示例 1: 
//
// 
//输入: s = "abcabcbb"
//输出: 3 
//解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
// 
//
// 示例 2: 
//
// 
//输入: s = "bbbbb"
//输出: 1
//解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
// 
//
// 示例 3: 
//
// 
//输入: s = "pwwkew"
//输出: 3
//解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
//     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
// 
//
// 示例 4: 
//
// 
//输入: s = ""
//输出: 0
// 
//
// 
//
// 提示： 
//
// 
// 0 <= s.length <= 5 * 10⁴ 
// s 由英文字母、数字、符号和空格组成 
// 
// Related Topics 哈希表 字符串 滑动窗口 👍 5986 👎 0


import java.util.HashMap;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution3 {
    public int lengthOfLongestSubstring(String s) {
        //使用滑动窗口，使用map记录窗口中数据及出现的次数
        HashMap<Character, Integer> windows = new HashMap<>();
        int left = 0;
        int right = 0;
        int length = 0;

        while (right < s.length()) {
            //获取字符
            char cr = s.charAt(right);
            right++;
            //先将数据添加到windows窗口中
            if (windows.containsKey(cr)) {
                windows.put(cr, windows.get(cr) + 1);
            } else {
                windows.put(cr, 1);
            }

            //如果windows中获取字符出现次数大于1说明有重复字符出现
            while (windows.get(cr) > 1) {
                char cl = s.charAt(left);
                left++;
                windows.put(cl, windows.get(cl) - 1);
            }

            if (length < right - left) {
                length = right - left;
            }

        }
        return length;

    }

    public static void main(String[] args) {
        String s = "abcabcbb";
        Solution3 solution3 = new Solution3();
        int length = solution3.lengthOfLongestSubstring(s);
        System.out.println(length);
    }
}
//leetcode submit region end(Prohibit modification and deletion)
