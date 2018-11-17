package org.hedinger.scaffold.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.node.BranchNode;
import org.hedinger.scaffold.node.NewlineLeaf;
import org.hedinger.scaffold.node.StaticLeaf;
import org.hedinger.scaffold.node.Template;

public class TemplateParser {
	public static final String NODE_ROOT = "root";
	public static final String NODE_NEWLINE = "NEWLINE";

	private StringBuilder logger = new StringBuilder();
	private HashMap<String, AbstractNode> slotMap;
	private HashMap<String, ArrayList<String>> childMap;
	private int anonymousCount;

	public TemplateParser() {
		slotMap = new HashMap<String, AbstractNode>();
		childMap = new HashMap<String, ArrayList<String>>();
		anonymousCount = 0;
	}

	public String getLog() {
		return logger.toString();
	}

	public Template getRoot() {
		Template template = new Template();
		template.setRoot(slotMap.get(NODE_ROOT));
		return template;
	}

	public void parseTemplate(File file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		parseTemplate(br);
	}

	public void parseTemplate(BufferedReader br) throws Exception {

		try {
			parseTemplateHelper(br);
		} catch (Exception e) {
			throw e;
		}

		br.close();

		slotMap.put(NODE_NEWLINE, new NewlineLeaf());

		for (String parentId : childMap.keySet()) {
			ArrayList<String> children = childMap.get(parentId);

			AbstractNode parent = slotMap.get(parentId);

			if (parent instanceof BranchNode) {
				for (String childId : children) {
					AbstractNode child = slotMap.get(childId);

					if (child == null) {
						throw new Exception("Node " + childId + " not found");
					}

					((BranchNode) parent).addNode(child);
				}
			} else {
				throw new Exception("Node " + parentId + " undefined");
			}
		}
	}

	private void parseTemplateHelper(BufferedReader br) throws Exception {
		int c_int = 0;
		int state = -1;
		String varName = "", varType = "";

		while ((c_int = br.read()) != -1) {
			char c = (char) c_int;

			// System.out.print(c);

			switch (state) {
			case -1: {
				if (c == '%') {
					state = 0;
				}
				break;
			}
			case 0: {
				if (c == ':') {
					state = 1;
				} else if (c == '[') {
					throw new Exception("Empty Variable Name");
				} else {
					varType += c;
				}
				break;
			}
			case 1: {
				if (c == '[') {
					generateSlot(varType.trim(), varName, null);
					parseTemplateBody(br, varType, varName);
					varType = "";
					varName = "";
					state = -1;
				} else {
					varName += c;
				}
				break;
			}
			}
		}
	}

	private void parseTemplateBody(BufferedReader br, String parentVarType, String parentVarName) throws Exception {
		int c_int = 0;
		int state = -1;
		String body = "", varName = "", varType = "";

		state = -1;

		if (parentVarType.equals("V")) {
			throw new Exception();
		}

		while ((c_int = br.read()) != -1) {
			char c = (char) c_int;

			// System.out.print(c);

			switch (state) {
			case -1: {
				if (c == '%') {
					generateStatic(body.trim(), parentVarName);
					body = "";
					state = 0;
				} else if (c == ']') {
					state = 3;
				} else {
					body += c;
				}
				break;
			}
			case 0: {
				if (c == '[') {
					if (varType.isEmpty()) {
						state = 1;
					} else {
						varName = generateAnonymous(varType.trim(), parentVarName);
						parseTemplateBody(br, varType, varName);
						varName = "";
						varType = "";
						body = "";
						state = -1;
					}

				} else {
					varType += c;
				}
				break;
			}
			case 1: // anon var
			{
				if (c == ']') {
					linkChildHelper(body.trim(), parentVarName);
					body = "";
					state = 2;
				} else {
					body += c;
				}
				break;
			}
			case 2: {
				if (c == '%') {
					varName = "";
					varType = "";
					body = "";
					state = -1;
				} else {
					return;
				}
				break;
			}
			case 3: {
				if (c == '%') {
					generateStatic(body.trim(), parentVarName);
					varType = "";
					body = "";
					state = -1;
					return;
				} else {
					body += ']';
					body += c;
					state = -1;
				}
			}
			}
		}
	}

	private String generateAnonymous(String type, String parent) throws Exception {
		anonymousCount++;

		String name = "anon-" + anonymousCount;

		generateSlotHelper(type, name, parent);
		return name;
	}

	private void generateStatic(String body, String parent) throws Exception {
		if (body.trim().isEmpty()) {
			return;
		}

		anonymousCount++;
		String name = "static=" + anonymousCount;

		slotMap.put(name, new StaticLeaf(body.trim()));

		linkChildHelper(name, parent);
	}

	private void generateSlot(String type, String name, String parent) throws Exception {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		if (name.trim().isEmpty()) {
			throw new IllegalArgumentException();
		}

		if (!name.matches("[a-zA-Z0-9]*")) {
			throw new Exception("Invalid name " + name);
		}

		generateSlotHelper(type, name, parent);
	}

	private void generateSlotHelper(String type, String name, String parent) throws Exception {
		AbstractNode node = null;

		if (type == null) {
			throw new IllegalArgumentException();
		}

		if (type.equals("N")) {
			node = new BranchNode(name, 1, 1);
		}
		if (type.equals("M")) {
			node = new BranchNode(name, 1);
		}
		if (type.equals("O")) {
			node = new BranchNode(name, 0, 1);
		}

		if (node == null) {
			throw new Exception("Invalid type: " + type);
		}

		slotMap.put(name, node);

		linkChildHelper(name, parent);
	}

	private void linkChildHelper(String name, String parent) throws Exception {
		if (parent == null) {
			return;
		}

		ArrayList<String> list = childMap.get(parent);

		if (list == null) {
			list = new ArrayList<String>();
		}

		list.add(name);

		childMap.put(parent, list);
	}

}
