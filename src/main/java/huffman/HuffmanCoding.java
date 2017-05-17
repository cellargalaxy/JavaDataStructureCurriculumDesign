package huffman;


/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class HuffmanCoding {
	private HuffmanTree<Byte> tree;
	private String[] codings;
	private StringBuilder codingHead;
	
	
	
	public HuffmanCoding(String[] codings) {
		this.codings = codings;
	}
	
	public HuffmanCoding(long[] counts) {
		Link<TreeNode> link=createLink(counts);
		tree=new HuffmanTree<Byte>(link);
		codings=new String[Byte.MAX_VALUE-Byte.MIN_VALUE+1];
		createCodings(tree.getRoot());
		codingHead=new StringBuilder();
	}
	private void createCodings(TreeNode<Byte> root){
		if (root.getLeft()!=null) {
			createCodings(root.getLeft());
		}
		if (root.getRight()!=null) {
			createCodings(root.getRight());
		}
		if (root.getLeft()==null&&root.getRight()==null) {
			codings[root.getT()-Byte.MIN_VALUE]=root.getCoding();
		}
	}
	
	private static Link<TreeNode> createLink(long[] counts){
		Link<TreeNode> link=new Link<TreeNode>();
		byte b=Byte.MIN_VALUE;
		for (long count : counts) {
			if (count>0) {
				TreeNode<Byte> treeNode=new TreeNode<Byte>(b,count);
				link.addNode(new LinkNode<TreeNode>(treeNode,treeNode.getCount()));
			}
			b++;
		}
		return link;
	}
	
	public String byteToCoding(byte b){
		return codings[b-Byte.MIN_VALUE];
	}
	
	public Byte codingToByte(String coding){
		byte b=Byte.MIN_VALUE;
		for (String s : codings) {
			if (s!=null&&s.equals(coding)) {
				return b;
			}
			b++;
		}
		return null;
	}
	
	public void printCoding(){
		System.out.println("字典：");
		byte b=Byte.MIN_VALUE;
		for (String coding : codings) {
			if (coding!=null) {
				System.out.println((char)(b)+":"+b+"："+coding);
			}
			b++;
		}
	}
	
	public StringBuilder getCodingHead() {
		if (codingHead.length()>0) {
			return codingHead;
		}else {
			for (String coding : codings) {
				if (coding!=null) {
					codingHead.append(coding);
				}
				codingHead.append(":");
			}
			codingHead.delete(codingHead.length()-1,codingHead.length());
			while (codingHead.length() < 3072) {
				codingHead.append(" ");
			}
			return codingHead;
		}
	}
	
	
}
