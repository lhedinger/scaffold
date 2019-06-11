package org.hedinger.scaffold.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.hedinger.scaffold.template.TemplateNode;
import org.hedinger.scaffold.template.TemplateTree;

public class TemplateGenerator {

	private TreeSet<TemplateTree> templates = new TreeSet<TemplateTree>(new TreeComparator());

	private List<String> samples = new ArrayList<String>();

	private Map<Integer, String> tokens = new HashMap<Integer, String>();

	public TemplateGenerator() {

	}

	public void addSample(String sample) {
		samples.add(sample);
	}

	public TemplateNode generateTemplate() {
		return null;
	}

	public void tokenize() {

	}

	private static class Sample {

		private String sample;

	}

	private static class TreeComparator implements Comparator<TemplateTree> {

		@Override
		public int compare(TemplateTree o1, TemplateTree o2) {
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
