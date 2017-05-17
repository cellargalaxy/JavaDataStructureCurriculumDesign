package huffman;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class HuffmanTree<T> {
	private TreeNode<T> root;
	private int size;
	
	public HuffmanTree(Link<TreeNode> link) {
		size=link.getLen();
		root=createTree(link);
		createCodings(root);
	}
	
	
	
	private TreeNode createTree(Link<TreeNode> link){
		while (link.getLen() > 1) {
			LinkNode<TreeNode> linkNode1=link.pollNode();
			LinkNode<TreeNode> linkNode2=link.pollNode();
			link.addNode(createNewLinkNode(linkNode1,linkNode2));
		}
		return link.pollNode().getT();
	}
	
	private LinkNode<TreeNode> createNewLinkNode(LinkNode<TreeNode> linkNode1,LinkNode<TreeNode> linkNode2){
		TreeNode<T> treeNode1=linkNode1.getT();
		TreeNode<T> treeNode2=linkNode2.getT();
		TreeNode<T> newTreeNode=new TreeNode<T>(null,treeNode1.getCount()+treeNode2.getCount());
		newTreeNode.setLeft(treeNode1);
		newTreeNode.setRight(treeNode2);
		treeNode1.setParent(newTreeNode);
		treeNode2.setParent(newTreeNode);
		return new LinkNode<TreeNode>(newTreeNode,newTreeNode.getCount());
	}
	
	private void createCodings(TreeNode root){
		if (root.getLeft()!=null) {
			root.getLeft().setCoding(root.getCoding()+"0");
			createCodings(root.getLeft());
		}
		if (root.getRight()!=null) {
			root.getRight().setCoding(root.getCoding()+"1");
			createCodings(root.getRight());
		}
	}
	
	public TreeNode<T> getRoot() {
		return root;
	}
	
	public void setRoot(TreeNode<T> root) {
		this.root = root;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
}
