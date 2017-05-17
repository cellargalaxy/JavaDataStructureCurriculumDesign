package huffman;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class HuffmanTree<T> {
	private TreeNode<T> root;
	
	
	
	protected HuffmanTree(Link<TreeNode> link) {
		root = createTree(link);
		createCodings(root);
	}
	
	protected HuffmanTree(String[] codings,Iterator<T> iterator){
		root=new TreeNode<T>(null,-1);
		for (String coding : codings) {
			if (coding!=null) {
				String[] bits=coding.split("");
				TreeNode<T> node=root;
				for (String bit : bits) {
					if (bit.length()==0) {
						continue;
					}
					if (bit.equals("0")) {
						if (node.getLeft()==null) {
							node.setLeft(new TreeNode<T>(null,-1));
						}
						node=node.getLeft();
					}else if (bit.equals("1")){
						if (node.getRight()==null) {
							node.setRight(new TreeNode<T>(null,-1));
						}
						node=node.getRight();
					}else{
						throw new RuntimeException("哈夫曼树构建失败");
					}
				}
				node.setT(iterator.next());
			}
		}
	}
	
	private TreeNode createTree(Link<TreeNode> link) {
		while (link.getLen() > 1) {
			LinkNode<TreeNode> linkNode1 = link.pollNode();
			LinkNode<TreeNode> linkNode2 = link.pollNode();
			link.addNode(createNewLinkNode(linkNode1, linkNode2));
		}
		return link.pollNode().getT();
	}
	
	private LinkNode<TreeNode> createNewLinkNode(LinkNode<TreeNode> linkNode1, LinkNode<TreeNode> linkNode2) {
		TreeNode<T> treeNode1 = linkNode1.getT();
		TreeNode<T> treeNode2 = linkNode2.getT();
		TreeNode<T> newTreeNode = new TreeNode<T>(null, treeNode1.getCount() + treeNode2.getCount());
		newTreeNode.setLeft(treeNode1);
		newTreeNode.setRight(treeNode2);
		treeNode1.setParent(newTreeNode);
		treeNode2.setParent(newTreeNode);
		return new LinkNode<TreeNode>(newTreeNode, newTreeNode.getCount());
	}
	
	private void createCodings(TreeNode root) {
		if (root.getLeft() != null) {
			root.getLeft().setCoding(root.getCoding() + "0");
			createCodings(root.getLeft());
		}
		if (root.getRight() != null) {
			root.getRight().setCoding(root.getCoding() + "1");
			createCodings(root.getRight());
		}
	}
	
	public TreeNode<T> getRoot() {
		return root;
	}
	
	public void print(){
		print(root);
	}
	
	private void print(TreeNode<T> node){
		System.out.print("节点："+node.getCoding()+":"+node.getT());
		if (node.getLeft()!=null) {
			System.out.print("；有左孩子："+node.getLeft().getCoding()+":"+node.getLeft().getT());
		}
		if (node.getRight()!=null) {
			System.out.print("；有右孩子："+node.getRight().getCoding()+":"+node.getRight().getT());
		}
		if (node.getLeft()==null&&node.getRight()==null) {
			System.out.print("；是叶子节点");
		}
		System.out.println();
		if (node.getLeft()!=null) {
			print(node.getLeft());
		}
		if (node.getRight()!=null) {
			print(node.getRight());
		}
	}
}
