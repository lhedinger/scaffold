package org.hedinger.scaffold.utils;

import java.util.List;

import org.hedinger.scaffold.markers.StringNode;
import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.node.BranchNode;

public class TreePrinter {

	public static String print(AbstractNode root) {
		StringBuilder sb = new StringBuilder();

		printTreeHelper(root, 0, sb);

		return sb.toString();
	}

	public static String print(StringNode root) {
		StringBuilder sb = new StringBuilder();

		printTreeHelper(root, 0, sb);

		return sb.toString();
	}

	private static void printTreeHelper(AbstractNode node, int tabs, StringBuilder sb) {
		sb.append('\n');

		for (int i = 0; i < tabs; i++) {
			sb.append('\t');
		}

		sb.append(node.toString());

		if (node instanceof BranchNode) {
			BranchNode branch = (BranchNode) node;

			AbstractNode n = branch.getFirst();

			while (n != null) {
				printTreeHelper(n, tabs + 1, sb);

				n = branch.getNext();
			}
		}
	}

	private static void printTreeHelper(StringNode node, int tabs, StringBuilder sb) {
		sb.append('\n');

		for (int i = 0; i < tabs; i++) {
			sb.append('\t');
		}

		sb.append(node.toString());

		List<StringNode> children = node.getChildren();

		if (children != null) {
			for (StringNode child : children) {
				printTreeHelper(child, tabs + 1, sb);
			}
		}
	}
}
