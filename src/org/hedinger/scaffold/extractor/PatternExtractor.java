package org.hedinger.scaffold.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PatternExtractor {

	private Map<String, Integer> counters = new HashMap<>();
	private Set<String> frequents = new HashSet<>();
	private long total = 0;

	public PatternExtractor() {

	}

	public void run(File file) throws Exception {
		counters = new HashMap<>();
		frequents = new HashSet<>();
		total = 0;
		parseText(file);
	}

	public int getCount(String sequence) {
		if (counters.containsKey(sequence)) {
			return counters.get(sequence);
		}
		return 0;
	}

	public long getTotal() {
		return total;
	}

	public Set<String> getFrequents() {
		return frequents;
	}

	public void parseText(File file) throws Exception {

		// first pass

		BufferedReader br = new BufferedReader(new FileReader(file));
		int c_int = 0;
		while ((c_int = br.read()) != -1) {
			total++;
			char c = (char) c_int;
			register(String.valueOf(c));
		}
		br.close();

		int min = 7;

		// analysis

		for (Entry<String, Integer> entry : counters.entrySet()) {
			if (entry.getValue() > min) {
				// frequents.add(entry.getKey());
			}
		}

		// second pass

		br = new BufferedReader(new FileReader(file));
		char previous = (char) br.read();
		while ((c_int = br.read()) != -1) {
			char current = (char) c_int;
			// if (frequents.contains(String.valueOf(previous))) {
			register(previous + "" + current);
			// }
			previous = current;
		}
		br.close();

		// analysis

		for (Entry<String, Integer> entry : counters.entrySet()) {
			String key = entry.getKey();
			if (key.length() == 2) {
				int a0 = counters.get(String.valueOf(key.charAt(0)));
				int a1 = counters.get(String.valueOf(key.charAt(1)));
				int count = entry.getValue();
				int sum = a0 + a1;
				if (count >= 3 && sum >= Math.max(a0, a1) * 1.99) {
					frequents.add(entry.getKey());
				}
			}
		}

		// third pass

		br = new BufferedReader(new FileReader(file));
		char previous1 = (char) br.read();
		char previous2 = (char) br.read();
		while ((c_int = br.read()) != -1) {
			char current = (char) c_int;
			// if (frequents.contains(String.valueOf(previous))) {
			register(previous1 + "" + previous2 + "" + current);
			// }
			previous1 = previous2;
			previous2 = current;
		}

		// analysis

		for (Entry<String, Integer> entry : counters.entrySet()) {
			String key = entry.getKey();
			if (key.length() == 3) {
				int a01 = getCount(String.valueOf(key.charAt(0) + "" + String.valueOf(key.charAt(1))));
				int a12 = getCount(String.valueOf(key.charAt(1) + "" + String.valueOf(key.charAt(2))));
				int count = entry.getValue();
				int sum = a01 + a12;
				if (count >= 3 && sum >= Math.max(a01, a12) * 1.99) {
					frequents.add(entry.getKey());
				}
			}
		}

		// TODO have frequents be non-boolean in nature (ratio of frequency)
		// TODO determine relationship between features
		// DONE tagging/aliasing of values (ex: uppercase character, number)

	}

	public void register(String cStr) {
		if (counters.containsKey(cStr)) {
			int count = counters.get(cStr);
			counters.put(cStr, count + 1);
		} else {
			counters.put(cStr, 1);
		}

	}



}
