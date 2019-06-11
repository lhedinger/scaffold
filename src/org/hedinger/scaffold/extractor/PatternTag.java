package org.hedinger.scaffold.extractor;

import static java.util.regex.Pattern.compile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum PatternTag {

	LOWERCASE("[a-z]+"),
	UPPERCASE("[A-Z]+"),
	NUMBER("[0-9]+"),
	WORD("[a-zA-Z_-]+"),;

	Pattern pattern;

	private PatternTag(String patternStr) {
		pattern = compile(patternStr);
	}

	public boolean matches(String candidate) {
		Matcher matcher = pattern.matcher(candidate);
		return matcher.matches();
	}

	public static List<PatternTag> detect(String candidate) {
		List<PatternTag> found = new ArrayList<>();
		for(PatternTag tag : PatternTag.values()) {
			if(tag.matches(candidate)){
				found.add(tag);
			}
		}
		return found;
	}

	// TODO https://chaseonsoftware.com/most-common-programming-case-types/

}
