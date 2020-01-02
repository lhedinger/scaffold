package org.hedinger.scaffold;

import java.io.File;
import java.util.Set;

import org.hedinger.scaffold.extractor.PatternExtractor;
import org.hedinger.scaffold.template.TemplateTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PatternExtractorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetCount_getTotal() throws Exception {
		PatternExtractor extractor = new PatternExtractor();
		extractor.run(new File("test/pattern1"));

		Assert.assertEquals(120, extractor.getTotal());
	}

	@Test
	public void testGetCount_singleCharacter() throws Exception {
		PatternExtractor extractor = new PatternExtractor();
		extractor.run(new File("test/pattern1"));

		Assert.assertEquals(18, extractor.getCount("\n"));
		Assert.assertEquals(18, extractor.getCount("%"));
		Assert.assertEquals(9, extractor.getCount("["));
		Assert.assertEquals(9, extractor.getCount("]"));
	}

	@Test
	public void testGetCount_Words() throws Exception {
		PatternExtractor extractor = new PatternExtractor();
		extractor.run(new File("test/pattern1"));

		Assert.assertEquals(18, extractor.getCount("\r\n"));
		Assert.assertEquals(9, extractor.getCount("]%"));
		Assert.assertEquals(5, extractor.getCount("[\r"));
		Assert.assertEquals(6, extractor.getCount("\r\n%"));
	}

	@Test
	public void testGetFrequents() throws Exception {
		PatternExtractor extractor = new PatternExtractor();
		extractor.run(new File("test/pattern1"));

		Assert.assertTrue(extractor.getFrequents().contains("\r"));
		Assert.assertTrue(extractor.getFrequents().contains("\r\n"));
		Assert.assertTrue(extractor.getFrequents().contains("\r\n%"));
		Assert.assertFalse(extractor.getFrequents().contains("t"));
		Assert.assertFalse(extractor.getFrequents().contains("tw"));
		Assert.assertFalse(extractor.getFrequents().contains("two"));
	}

	@Test
	public void testBuildSimpleTemplateTree() throws Exception {
		PatternExtractor extractor = new PatternExtractor();
		extractor.run(new File("test/pattern1"));

		TemplateTree out = extractor.buildSimpleTemplateTree(new File("test/pattern1"));

		Assert.assertNotNull(out);
	}

	@Test
	public void testPattern2() throws Exception {
		PatternExtractor extractor = new PatternExtractor();
		extractor.run(new File("test/pattern2"));

		Set<String> frequents = extractor.getFrequents();
		for (String str : frequents) {
			printStrEscaped(str);
		}
	}

	public static void printStrEscaped(String str) {
		System.out.println(str.replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t"));
	}

}
