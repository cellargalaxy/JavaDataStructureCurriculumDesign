package huffman;


import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class HuffmanCoding {
	private HuffmanTree<Byte> tree;
	private String[] codings;
	private StringBuilder codingHead;
	private String fileName;
	
	/**
	 * 通过压缩文件的文件头保存的哈夫曼编码构建哈夫曼树
	 *
	 * @param codingHead 压缩文件的文件头
	 */
	protected HuffmanCoding(String codingHead) {
		this.codingHead = new StringBuilder(codingHead);
		String[] strings = codingHead.split(";");
		fileName = strings[0].trim();
		strings = strings[1].trim().split(":");
		codings = new String[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
		LinkedList<Byte> bytes = new LinkedList<Byte>();
		for (int i = 0; i < strings.length; i++) {
			strings[i] = strings[i].trim();
			if (strings[i].length() > 0) {
				codings[i] = strings[i];
				bytes.add((byte) (i + Byte.MIN_VALUE));
			}
		}
		tree = new HuffmanTree<Byte>(codings, bytes.iterator());
	}
	
	/**
	 * @param countInputStream 统计完比特结果的HuffmanCountInputStream对象
	 * @param fileName         源文件名
	 */
	public HuffmanCoding(HuffmanCountInputStream countInputStream, String fileName) {
		this.fileName = fileName;
		Link<TreeNode> link = createLink(countInputStream.getCounts());
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
		TreeNode<Byte> node = tree.getRoot();
		do {
			String s = decodingInputStream.pollBit();
			if (s.equals("0")) {
				node = node.getLeft();
			} else if (s.equals("1")) {
				node = node.getRight();
			} else {
				throw new RuntimeException("编码异常");
			}
			if (node.getLeft() == null && node.getRight() == null) {
				return node.getT();
			}
		} while (true);
	}
	
	public StringBuilder getCodingHead() {
		if (codingHead.length() > 0) {
			return codingHead;
		} else {
			codingHead.append(fileName + ";");
			for (String coding : codings) {
				if (coding != null) {
					codingHead.append(coding);
				}
				codingHead.append(" :");
			}
			return codingHead;
		}
	}
	
	public HuffmanTree<Byte> getTree() {
		return tree;
	}
	
	public void setTree(HuffmanTree<Byte> tree) {
		this.tree = tree;
	}
	
	public String[] getCodings() {
		return codings;
	}
	
	public void setCodings(String[] codings) {
		this.codings = codings;
	}
	
	public void setCodingHead(StringBuilder codingHead) {
		this.codingHead = codingHead;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
