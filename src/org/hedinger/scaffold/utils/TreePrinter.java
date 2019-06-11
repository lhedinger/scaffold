package org.hedinger.scaffold.utils;

import java.util.List;

import org.hedinger.scaffold.markers.StringNode;
import org.hedinger.scaffold.template.TemplateBranchNode;
import org.hedinger.scaffold.template.TemplateNode;

public class TreePrinter {

	public static String print(TemplateNode root) {
		StringBuilder sb = new StringBuilder();

		printTreeHelper(root, 0, sb);

		return sb.toString();
	}

	public static String print(StringNode root) {
		StringBuilder sb = new StringBuilder();

		printTreeHelper(root, 0, sb);

		return sb.toString();
	}

	private static void printTreeHelper(TemplateNode node, int tabs, StringBuilder sb) {
		sb.append('\n');

		for (int i = 0; i < tabs; i++) {
			sb.append('\t');
		}

		sb.append(node.toString());

		if (node instanceof TemplateBranchNode) {
			TemplateBranchNode branch = (TemplateBranchNode) node;

			TemplateNode n = branch.getFirst();

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
