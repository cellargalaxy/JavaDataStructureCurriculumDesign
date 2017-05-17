package huffman;


import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class HuffmanCoding {
	private HuffmanTree<Byte> tree;
	private String[] codings;
	private StringBuilder codingHead;
	private String fileNmae;
	
	
	
	protected HuffmanCoding(String codingHead) {
		this.codingHead=new StringBuilder(codingHead);
		String[] strings = codingHead.split(";");
		fileNmae = strings[0].trim();
		strings = strings[1].trim().split(":");
		codings = new String[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
		LinkedList<Byte> bytes=new LinkedList<Byte>();
		for (int i = 0; i < strings.length; i++) {
			strings[i]=strings[i].trim();
			if (strings[i].length() > 0) {
				codings[i] = strings[i];
				bytes.add((byte)(i+Byte.MIN_VALUE));
			}
		}
		tree=new HuffmanTree<Byte>(codings,bytes.iterator());
	}
	
	protected HuffmanCoding(long[] counts, String fileNmae) {
		this.fileNmae = fileNmae;
		Link<TreeNode> link = createLink(counts);
		tree = new HuffmanTree<Byte>(link);
		codings = new String[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
		createCodings(tree.getRoot());
		codingHead = new StringBuilder();
	}
	
	
	
	private void createCodings(TreeNode<Byte> root) {
		if (root.getLeft() != null) {
			createCodings(root.getLeft());
		}
		if (root.getRight() != null) {
			createCodings(root.getRight());
		}
		if (root.getLeft() == null && root.getRight() == null) {
			codings[root.getT() - Byte.MIN_VALUE] = root.getCoding();
		}
	}
	
	private static Link<TreeNode> createLink(long[] counts) {
		Link<TreeNode> link = new Link<TreeNode>();
		byte b = Byte.MIN_VALUE;
		for (long count : counts) {
			if (count > 0) {
				TreeNode<Byte> treeNode = new TreeNode<Byte>(b, count);
				link.addNode(new LinkNode<TreeNode>(treeNode, treeNode.getCount()));
			}
			b++;
		}
		return link;
	}
	
	public String byteToCoding(byte b) {
		return codings[b - Byte.MIN_VALUE];
	}
	
	public Byte codingToByte(HuffmanDecodingInputStream decodingInputStream) {
		TreeNode<Byte> node=tree.getRoot();
		do {
			String s=decodingInputStream.pollBit();
			if (s.equals("0")) {
				node=node.getLeft();
			}else if (s.equals("1")){
				node=node.getRight();
			}else {
				throw new RuntimeException("编码异常");
			}
			if (node.getLeft()==null&&node.getRight()==null) {
				return node.getT();
			}
		}while (true);
	}
	
	public void printCoding() {
		byte b = Byte.MIN_VALUE;
		for (String coding : codings) {
			if (coding != null) {
				System.out.println((char) (b) + ":" + b + "：" + coding);
			}
			b++;
		}
	}
	
	public StringBuilder getCodingHead() {
		if (codingHead.length() > 0) {
			return codingHead;
		} else {
			codingHead.append(fileNmae + ";");
			for (String coding : codings) {
				if (coding != null) {
					codingHead.append(coding);
				}
				codingHead.append(" :");
			}
			return codingHead;
		}
	}
	
	public String getFileNmae() {
		return fileNmae;
	}
	
	public void printTree(){
		tree.print();
	}
}
