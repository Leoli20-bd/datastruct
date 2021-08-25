package OOO0.Leetcode;

import java.util.Arrays;

/**
 * @ClassName: AboutArray
 * @Author: haleli
 * @Date: 21:51
 * @ProjectName: DataStruct
 * @Description: 原地修改数组
 * @Version: 1.0
 **/
public class AboutArray {

    /**
     * https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array/
     * 给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。
     * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
     *
     * @param nums
     * @return
     */

    /**
     * 考虑到是原地删除，所以我们只能交换数据，不能移动数据，这样的话会想到用快慢指针
     * 1. 快慢指针都从0开始，遍历数组，每次快指针都往前走一步
     * 2. 如果快指针与慢指针下标值相同，慢指针往前走一步，快指针往前一步
     * 3. 如果值不同，将快指针的值赋给慢指针，快指针往前一步
     * 4. 当快指针走完，0 到 slow的数据就是去重后的数据
     * 5. 所以数组的长度就是slow +1
     *
     * @param nums
     * @return
     */
    public int removeDuplicates(int[] nums) {
        int slow = 0;
        int fast = 0;
        while (fast < nums.length) {
            if (nums[slow] != nums[fast]) {
                slow++;
                //注意是先slow+1后，然后再赋值
                nums[slow] = nums[fast];
            }
            fast++;
        }
        return slow + 1;
    }

    /**
     * https://leetcode-cn.com/problems/remove-element/
     * 给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于val的元素，并返回移除后数组的新长度。
     * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。
     * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
     * <p>
     * 使用快慢指针
     * 1. 快慢指针的初始值都是0
     * 2. 遍历数组，如果快指针下标数据不等于val，那么将快指针值赋给慢指针，同时慢指针加1，慢指针加1
     * 3. 如果快指针值等于慢指针，快指针加1
     * 4. 当快指针到终点时，0 到 slow的数据是有序的
     * 5. 因为slow是先赋值，后加1的，所以，数组长度为slow
     *
     * @param nums
     * @param val
     * @return
     */

    public int removeElement(int[] nums, int val) {
        int slow = 0;
        int fast = 0;
        while (fast < nums.length) {
            if (nums[fast] != val) {
                nums[slow] = nums[fast];
                slow++;
            }
            fast++;
        }
        return slow;
    }


    /**
     * https://leetcode-cn.com/problems/move-zeroes/
     * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
     * <p>
     * 示例:
     * <p>
     * 输入: [0,1,0,3,12]
     * 输出: [1,3,12,0,0]
     * 使用快慢指针
     * 1. 首先快指针往前移动，找到不为0的数，然后将慢指针与0对比，如果慢指针等于0则将，快慢指针的数交换
     * 2. 如果慢指针不等于0，慢指针加1
     * 3. 当快指针遍历完，0的数也替换到末尾了
     *
     *
     * @param nums
     */

    public void moveZeroes(int[] nums) {
        int slow = 0;
        int fast = 0;
        while (fast < nums.length) {
            if (nums[fast] != 0) {
                if (nums[slow] == 0) {
                    int tmp = nums[slow];
                    nums[slow] = nums[fast];
                    nums[fast] = tmp;
                }
                slow++;
            }
            fast++;
        }
    }


//    /**
//     * https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list/
//     * 存在一个按升序排列的链表，给你这个链表的头节点 head ，请你删除所有重复的元素，使每个元素 只出现一次 。
//     *
//     * 返回同样按升序排列的结果链表。
//     * @param head
//     * @return
//     */
//    public ListNode deleteDuplicates(ListNode head) {
//
//    }


    public static void main(String[] args) {
        int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        AboutArray aboutArray = new AboutArray();
       /* int i = aboutArray.removeDuplicates(nums);
        System.out.println(i);*/

        /*int i1 = aboutArray.removeElement(nums, 2);
        System.out.println(i1);
        System.out.println(Arrays.toString(nums));*/

        aboutArray.moveZeroes(nums);
        System.out.println(Arrays.toString(nums));

    }
}
