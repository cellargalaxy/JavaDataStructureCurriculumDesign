package huffman;


/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class Link<T> {
	private int len;
	private LinkNode<T> first;
	private LinkNode<T> last;
	
	protected Link() {
		len = 0;
	}
	
	/**
	 * 添加一个节点，并且根据节点的权重进行排序
	 *
	 * @param linkNode
	 */
	public void addNode(LinkNode<T> linkNode) {
		if (len == 0) {
			first = linkNode;
			last = linkNode;
			first.setNext(last);
			first.setPre(null);
			last.setPre(first);
			last.setNext(null);
			len++;
		} else if (len == 1) {
			if (first.getCount() > linkNode.getCount()) {
				first = linkNode;
			} else {
				last = linkNode;
			}
			first.setNext(last);
			first.setPre(null);
			last.setPre(first);
			last.setNext(null);
			len++;
		} else {
			if (first.getCount() >= linkNode.getCount()) {
				linkNode.setNext(first);
				linkNode.getNext().setPre(linkNode);
				first = linkNode;
				first.setPre(null);
				len++;
				return;
			}
			LinkNode node = first;
			do {
				node = node.getNext();
				if (node.getCount() >= linkNode.getCount()) {
					linkNode.setNext(node);
					linkNode.setPre(node.getPre());
					linkNode.getPre().setNext(linkNode);
					linkNode.getNext().setPre(linkNode);
					len++;
					return;
				}
			} while (node.getNext() != null);
			linkNode.setPre(last);
			linkNode.getPre().setNext(linkNode);
			last = linkNode;
			last.setNext(null);
			len++;
		}
	}
	
	/**
	 * @return 弹出权重最小的一个节点，若链表为空则返回null
	 */
	public LinkNode<T> pollNode() {
		if (len == 0) {
			return null;
		} else if (len == 1) {
			LinkNode linkNode = first;
			first = null;
			last = null;
			len--;
			return linkNode;
		} else {
			LinkNode linkNode = first;
			first = first.getNext();
			len--;
			return linkNode;
		}
	}
	
	public int getLen() {
		return len;
	}
	
	public void print() {
		if (len == 0) {
			System.out.println("Link{}");
		} else if (len == 1) {
			System.out.println("Link{" + first.getT() + "}");
		} else {
			LinkNode node = first;
			System.out.println("Link{");
			do {
				System.out.println(node.getT() + ",");
				node = node.getNext();
			} while (node != null);
			System.out.println("}");
		}
	}
}
