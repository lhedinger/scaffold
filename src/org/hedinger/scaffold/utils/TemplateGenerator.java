package org.hedinger.scaffold.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.node.Template;

public class TemplateGenerator {

	private TreeSet<Template> templates = new TreeSet<Template>(new TreeComparator());

	private List<String> samples = new ArrayList<String>();

	private Map<Integer, String> tokens = new HashMap<Integer, String>();

	public TemplateGenerator() {

	}

	public void addSample(String sample) {
		samples.add(sample);
	}

	public AbstractNode generateTemplate() {
		return null;
	}

	public void tokenize() {

	}

	private static class Sample {

		private String sample;

	}

	private static class TreeComparator implements Comparator<Template> {

		@Override
		public int compare(Template o1, Template o2) {
			if (o1.getScore() < 0) {
				return 1; // move to end
			}
			if (o2.getScore() < 0) {
				return -1; // move to end
			}
			if (o1.getScore() > o2.getScore()) {
				return -1;
			}
			if (o1.getScore() < o2.getScore()) {
				return 1;
			}
			return 1;
		}

	}

}
