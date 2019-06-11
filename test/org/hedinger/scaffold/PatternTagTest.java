package org.hedinger.scaffold;

import static org.hedinger.scaffold.extractor.PatternTag.LOWERCASE;
import static org.hedinger.scaffold.extractor.PatternTag.NUMBER;
import static org.hedinger.scaffold.extractor.PatternTag.UPPERCASE;
import static org.hedinger.scaffold.extractor.PatternTag.WORD;

import org.hedinger.scaffold.extractor.PatternTag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PatternTagTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDetectTags_findsTags() throws Exception {
		Assert.assertTrue(PatternTag.detect("hello").contains(WORD));
		Assert.assertTrue(PatternTag.detect("hello").contains(LOWERCASE));
		Assert.assertFalse(PatternTag.detect("hello").contains(UPPERCASE));
		Assert.assertFalse(PatternTag.detect("hello").contains(NUMBER));
	}

	@Test
	public void testDetectTags_findsNothing() throws Exception {
		Assert.assertTrue(PatternTag.detect("!!!").isEmpty());
	}

	@Test
	public void testMatches_lowercase() throws Exception {
		Assert.assertTrue(LOWERCASE.matches("abc"));
		Assert.assertFalse(LOWERCASE.matches("Abc"));
		Assert.assertFalse(LOWERCASE.matches("abC"));
	}

	@Test
	public void testMatches_uppercase() throws Exception {
		Assert.assertTrue(UPPERCASE.matches("ABC"));
		Assert.assertFalse(UPPERCASE.matches("aBC"));
		Assert.assertFalse(UPPERCASE.matches("ABc"));
	}

	@Test
	public void testMatches_number() throws Exception {
		Assert.assertTrue(NUMBER.matches("123"));
		Assert.assertFalse(NUMBER.matches("12.5"));
		Assert.assertFalse(NUMBER.matches("12a"));
	}

	@Test
	public void testMatches_word() throws Exception {
		Assert.assertTrue(WORD.matches("hello"));
		Assert.assertTrue(WORD.matches("some-thing"));
		Assert.assertTrue(WORD.matches("some_thing"));
		Assert.assertFalse(WORD.matches("hi there"));
		Assert.assertFalse(WORD.matches("hello1"));
	}

}
