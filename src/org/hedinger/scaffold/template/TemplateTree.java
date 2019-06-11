package org.hedinger.scaffold.template;

import org.hedinger.scaffold.utils.TreePrinter;

public class TemplateTree {
	private TemplateNode root;

	public TemplateTree() {
	}

	public TemplateNode getRoot() {
		return root;
	}

	public void setRoot(TemplateNode root) {
		this.root = root;
	}

	public int getScore() {
		return 0;
	}

	@Override
	public String toString() {
		return TreePrinter.print(root);
	}

}
