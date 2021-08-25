package OOO1.LinkedList;

import com.sun.deploy.util.LinkMouseListener;

import javax.lang.model.element.NestingKind;

/**
 * @ClassName: MyLinkedList
 * @Author: haleli
 * @Date: 11:40
 * @ProjectName: DataStruct
 * @Description: 链表的操作，添加数据，反转，删除倒数第n位，环检测，中间位
 * @Version: 1.0.0
 **/
public class MyLinkedList {
    //哨兵节点，头节点
    Node header;

    //链表长度
    long length;

    //初始化链表
    public MyLinkedList() {
        this.header = null;
        this.length = 0;
    }

    //添加数据到链表
    public void add(String item) {
        Node newnode = new Node(item, null);
        //判断头节点
        if (header == null) {
            header = newnode;
        } else {
            Node tmp = header;
            //找到尾部节点，将数据添加
            while (tmp.next != null) {
                tmp = tmp.next;
            }
            tmp.next = newnode;
        }
        length++;
    }

    //获取链表的长度
    public long getLength() {
        long len = 0;
        Node tmp = header;
        while (tmp != null) {
            len++;
            tmp = tmp.next;
        }

        return len;
    }

    //toString 方法
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Node tmp = header;
        while (tmp != null) {
            sb.append(tmp.data).append(",");
            tmp = tmp.next;
        }

        return sb.toString();
    }


    /**
     * 链表反转
     * 1. 当前节点.next  = 前节点
     * 2. 将当前节点赋值给前节点
     * 3. 当前节点后移
     */


    public void reverse() {
        //记录前一个节点
        Node pre_node = null;
        //记录当前节点
        Node curr = header;

        while (curr != null) {
            //记录当前节点的下一个节点
            Node tmp = curr.next;

            curr.next = pre_node;
            pre_node = curr;
            curr = tmp;
        }
        header = pre_node;

    }

    //链表成环
    public void loop() throws Exception {
        if (header == null) {
            throw new Exception();
        }

        Node tmp = header;
        while (tmp != null) {
            tmp = tmp.next;
        }
        tmp.next = header;
    }

    /**
     * 链表环检测
     * 1. 初始化两个指针，快指针和慢指针
     * 2. 快指针每次前进一位，慢指针每次前进二位
     * 3. 如果有环，慢指针跑完一圈，慢指针跑完两圈，即又回到同一起点
     */

    public boolean checkLoop() {
        Node low = header;
        Node fast = header;

        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            low = low.next;
            if (low == fast) {
                return true;
            }
        }
        return false;
    }


    /**
     * 查找中间节点「链表不为环」
     * 1. 初始化两个指针快指针和慢指针
     * 2. 快指针每次走一步，慢指针每次走两步
     * 3. 如果快指针走到终点，那么慢指针刚好在中间
     */

    public Node getMid() throws Exception {
        if (header == null) {
            throw new Exception();
        }

        Node low = header;
        Node fast = header;
        while (fast != null && fast.next != null) {

            fast = fast.next.next;
            low = low.next;

        }
        return low;
    }


    /**
     * 删除倒数第n位
     * 1. 此处涉及两个指针，前指针先走n步，后指针从0的位置开始走
     * 2. 当前指针走到终点时，后指针就是倒数n的位置
     *
     * @param n
     */
    public void deleteN(int n) {
        //pre_node 需要删除节点的前一个节点
        Node node = new Node();
        node.next =header;
        Node pre_node =node;
        Node curr_node = node;


        for (int i = 0; i <n; i++) {
            curr_node = curr_node.next;
        }

        while (curr_node.next != null) {
            curr_node = curr_node.next;
            pre_node = pre_node.next;
        }

        //删除倒数第n的节点
        pre_node.next = pre_node.next.next;
        //头节点也可能被删除，
      //  header = node.next;
        length--;
    }


    public static void main(String[] args) {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.add("a");
        myLinkedList.add("b");
        myLinkedList.add("c");

        System.out.println(myLinkedList);

        myLinkedList.reverse();
        System.out.println(myLinkedList);

        myLinkedList.deleteN(3);
        System.out.println(myLinkedList);

    }


}
