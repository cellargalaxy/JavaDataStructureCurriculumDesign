package huffman;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class TreeNode<T> {
	private TreeNode parent;
	private TreeNode left;
	private TreeNode right;
	
	private T t;
	private long count;
	private String coding;
	
	protected TreeNode(T t, long count) {
		this.t = t;
		this.count = count;
		coding = "";
	}
	
	public TreeNode getParent() {
		return parent;
	}
	
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	
	public TreeNode getLeft() {
		return left;
	}
	
	public void setLeft(TreeNode left) {
		this.left = left;
	}
	
	public TreeNode getRight() {
		return right;
	}
	
	public void setRight(TreeNode right) {
		this.right = right;
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
	
	public String getCoding() {
		return coding;
	}
	
	public void setCoding(String coding) {
		this.coding = coding;
	}
	
	@Override
	public String toString() {
		return "TreeNode{" +
				"t=" + t +
				", count=" + count +
				", coding='" + coding + '\'' +
				'}';
	}
}
