package org.hedinger.scaffold.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.hedinger.scaffold.template.TemplateBranchNode;
import org.hedinger.scaffold.template.TemplateNewlineLeaf;
import org.hedinger.scaffold.template.TemplateNode;
import org.hedinger.scaffold.template.TemplateRegexLeaf;
import org.hedinger.scaffold.template.TemplateStaticLeaf;
import org.hedinger.scaffold.template.TemplateTree;
import org.hedinger.scaffold.template.TemplateNode.Flag;

public class TemplateParser {
	public static final String NODE_ROOT = "root";
	public static final String NODE_NEWLINE = "NEWLINE";

	private StringBuilder logger = new StringBuilder();
	private HashMap<String, TemplateNode> slotMap;
	private HashMap<String, ArrayList<String>> childMap;
	private int anonymousCount;

	public TemplateParser() {
		slotMap = new HashMap<String, TemplateNode>();
		childMap = new HashMap<String, ArrayList<String>>();
		anonymousCount = 0;
	}

	public String getLog() {
		return logger.toString();
	}

	public TemplateTree getRoot() {
		TemplateTree template = new TemplateTree();
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

		slotMap.put(NODE_NEWLINE, new TemplateNewlineLeaf());

		for (String parentId : childMap.keySet()) {
			ArrayList<String> children = childMap.get(parentId);

			TemplateNode parent = slotMap.get(parentId);

			if (parent instanceof TemplateBranchNode) {
				for (String childId : children) {
					TemplateNode child = slotMap.get(childId);

					if (child == null) {
						throw new Exception("Node " + childId + " not found");
					}

					child.setParent(parent);
					((TemplateBranchNode) parent).addNode(child);
				}
			} else {
				throw new Exception("Node " + parentId + " undefined");
			}
		}
	}

	private void parseTemplateHelper(BufferedReader br) throws Exception {
		int c_int = 0;
		int state = -1;
		String varDef = "", varType = "";

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
					String varName = generateSlot(varType.trim(), varDef.trim(), null);
					parseTemplateBody(br, varType, varName);
					varType = "";
					varDef = "";
					state = -1;
				} else {
					varDef += c;
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
					generateStatic(body.trim(), parentVarName, parentVarType);
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
					generateStatic(body.trim(), parentVarName, parentVarType);
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

		generateSlotHelper(type, name, parent, null);
		return name;
	}

	private void generateStatic(String body, String parent, String parentType) throws Exception {
		if (body.trim().isEmpty()) {
			return;
		}

		anonymousCount++;
		String name = "static=" + anonymousCount;

		if (parentType.equals("R")) {
			slotMap.put(name, new TemplateRegexLeaf(body.trim()));
		} else {
			slotMap.put(name, new TemplateStaticLeaf(body.trim()));
		}

		linkChildHelper(name, parent);
	}

	private String generateSlot(String type, String definition, String parent) throws Exception {
		if (definition == null || definition.isEmpty()) {
			throw new IllegalArgumentException();
		}

		String[] arr = definition.split(":", 2);

		String name = arr[0];
		String[] flags = {};

		if (arr.length > 1) {
			flags = arr[1].split(",");
		}

		if (!name.matches("[a-zA-Z0-9]*")) {
			throw new Exception("Invalid name " + name);
		}

		generateSlotHelper(type, name, parent, flags);
		return name;
	}

	private void generateSlotHelper(String type, String name, String parent, String[] flagsArr) throws Exception {
		TemplateNode node = null;

		if (type == null) {
			throw new IllegalArgumentException();
		}

		Set<Flag> flags = new HashSet<>();

		if (flagsArr != null) {
			for (String flag : flagsArr) {
				flags.add(Flag.valueOf(flag.toUpperCase()));
			}
		}

		if (type.equals("N")) {
			node = new TemplateBranchNode(name, 1, 1, flags);
		}
		if (type.equals("M")) {
			node = new TemplateBranchNode(name, 1, flags);
		}
		if (type.equals("O")) {
			node = new TemplateBranchNode(name, 0, 1, flags);
		}
		if (type.equals("R")) {
			node = new TemplateBranchNode(name, 1, 1, flags);
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
