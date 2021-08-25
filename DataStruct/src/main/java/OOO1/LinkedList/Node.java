package OOO1.LinkedList;

/**
 * @ClassName: Node
 * @Author: haleli
 * @Date: 11:37
 * @ProjectName: DataStruct
 * @Description: 单链表的一个节点
 * @Version: 1.0.0
 **/
public class Node {
    //数据信息
    String data;

    //指针、引用
    Node next;

    public Node() {
    }

    public Node(String data, Node next) {
        this.data = data;
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data='" + data + '\'' +
                ", next=" + next +
                '}';
    }
}
