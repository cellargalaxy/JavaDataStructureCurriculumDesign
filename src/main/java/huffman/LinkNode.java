package huffman;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class LinkNode<T> {
	private LinkNode pre;
	private LinkNode next;
	
	private T t;
	private long count;
	
	protected LinkNode(T t, long count) {
		this.t = t;
		this.count = count;
	}
	
	public LinkNode getPre() {
		return pre;
	}
	
	public void setPre(LinkNode pre) {
		this.pre = pre;
	}
	
	public LinkNode getNext() {
		return next;
	}
	
	public void setNext(LinkNode next) {
		this.next = next;
	}
	
	public long getCount() {
		return count;
	}
	
	public void setCount(long count) {
		this.count = count;
	}
	
	public T getT() {
		return t;
	}
	
	public void setT(T t) {
		this.t = t;
	}
}
