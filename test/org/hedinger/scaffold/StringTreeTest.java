package org.hedinger.scaffold;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileReader;

import org.hedinger.scaffold.markers.StringForest;
import org.hedinger.scaffold.template.TemplateNode;
import org.hedinger.scaffold.template.TemplateTree;
import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.TemplateGenerator;
import org.hedinger.scaffold.utils.TemplateParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringTreeTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testTree_basic() throws Exception {
		StringForest tree = new StringForest(generateTemplate("test/templ1"), new SmartBuffer("xAxBCZx"));
		tree.run();

		Assert.assertEquals("ABCZ", tree.getBest().generateOutput());
		Assert.assertEquals(4, tree.getBest().getCoverage());
	}

	@Test
	public void testTree_optionals() throws Exception {
		StringForest tree = new StringForest(generateTemplate("test/templ1"), new SmartBuffer("AxBCZABxBCBZZCZx"));
		tree.run();
	}

	@Test
	public void testTree_repeating() throws Exception {
		StringForest tree = new StringForest(generateTemplate("test/templ1"), new SmartBuffer("AxBCZABxBCBZZCZx"));
		tree.run();
	}

	@Test
	public void testTree_noise() throws Exception {
		StringForest tree = new StringForest(generateTemplate("test/templ1"), new SmartBuffer("AxBCZABxBCBZZCZx"));
		tree.run();

		Assert.assertEquals("ABCZABBCBCZ", tree.getBest().generateOutput());
		Assert.assertEquals(11, tree.getBest().getCoverage());
	}

	@Test
	public void testTree_file() throws Exception {
		StringForest tree = new StringForest(generateTemplate("test/templ1"),
				new SmartBuffer(new FileReader("test/input1")));
		tree.run();

		Assert.assertEquals("ABCZABBCBCZ", tree.getBest().generateOutput());
		Assert.assertEquals(11, tree.getBest().getCoverage());
	}

	@Test
	public void testTree_newlines() throws Exception {
		StringForest tree = new StringForest(generateTemplate("test/templ6"), new SmartBuffer("AB534ZAB96Z"));
		tree.run();

		Assert.assertEquals("AB534ZAB96Z", tree.getBest().generateOutput());
		Assert.assertEquals(11, tree.getBest().getCoverage());
	}

	@Test
	public void testForest_Basic() throws Exception {
		StringForest tree = new StringForest(generateTemplate("test/templ1"), new SmartBuffer("AxBCZABxBCBZZCZx"));
		tree.run();

		Assert.assertEquals("ABCZABBCBCZ", tree.getBest().generateOutput());
		Assert.assertEquals(11, tree.getBest().getCoverage());
	}

	@Test
	public void testForest_BasicStrict() throws Exception {
		StringForest tree = new StringForest(generateTemplate("test/templ1s"), new SmartBuffer("xABCZxAZ"));
		tree.run();

		Assert.assertEquals("ABCZAZ", tree.getBest().generateOutput());
		Assert.assertEquals(6, tree.getBest().getCoverage());
	}

	@Test
	public void test_scoring() throws Exception {

		String sample = "ABCZABBZ";

		TemplateTree az = generateTemplate("test/templ2");
		TemplateTree ab = generateTemplate("test/templ3");
		TemplateTree bc = generateTemplate("test/templ4");

		StringForest aztree = new StringForest(az, new SmartBuffer(sample));
		StringForest abtree = new StringForest(ab, new SmartBuffer(sample));
		StringForest bctree = new StringForest(bc, new SmartBuffer(sample));

		aztree.run();
		abtree.run();
		bctree.run();

		Assert.assertEquals("AZAZ", aztree.getBest().generateOutput());
		Assert.assertEquals("ABAB", abtree.getBest().generateOutput());
		Assert.assertEquals("BC", bctree.getBest().generateOutput());
		Assert.assertEquals(4, aztree.getBest().getCoverage());
		Assert.assertEquals(4, abtree.getBest().getCoverage());
		Assert.assertEquals(2, bctree.getBest().getCoverage());

	}

	@Test
	public void test_findRoot() throws Exception {

		String sample = "ABXCZABBZ";
		StringForest alltree = new StringForest(generateTemplate("test/templ1"), new SmartBuffer(sample));
		StringForest aztree = new StringForest(generateTemplate("test/templ2"), new SmartBuffer(sample));
		StringForest abtree = new StringForest(generateTemplate("test/templ3"), new SmartBuffer(sample));
		StringForest bctree = new StringForest(generateTemplate("test/templ4"), new SmartBuffer(sample));
		alltree.run();
		aztree.run();
		abtree.run();
		bctree.run();

		String query = "root";
		Assert.assertEquals(asList("ABCZ", "ABBZ"), alltree.getBest().find(query));
		Assert.assertEquals(asList("AXZ", "AZ"), aztree.getBest().find(query));
		Assert.assertEquals(asList("AB", "AB"), abtree.getBest().find(query));
		Assert.assertEquals(asList("BXC"), bctree.getBest().find(query));
	}

	@Test
	public void test_findZero() throws Exception {

		String sample = "ABXCZABBZ";
		StringForest alltree = new StringForest(generateTemplate("test/templ1"), new SmartBuffer(sample));
		StringForest aztree = new StringForest(generateTemplate("test/templ2"), new SmartBuffer(sample));
		StringForest abtree = new StringForest(generateTemplate("test/templ3"), new SmartBuffer(sample));
		StringForest bctree = new StringForest(generateTemplate("test/templ4"), new SmartBuffer(sample));
		alltree.run();
		aztree.run();
		abtree.run();
		bctree.run();

		String query = "root.zero";
		Assert.assertEquals(asList("A", "BC", "Z", "A", "BB", "Z"), alltree.getBest().find(query));
		Assert.assertEquals(asList("A", "X", "Z", "A", "", "Z"), aztree.getBest().find(query));
		Assert.assertEquals(asList("A", "", "B", "A", "", "B"), abtree.getBest().find(query));
		Assert.assertEquals(asList("B", "X", "C"), bctree.getBest().find(query));
	}

	@Test
	public void test_findOne() throws Exception {

		String sample = "ABXCZABBZ";
		StringForest alltree = new StringForest(generateTemplate("test/templ1"), new SmartBuffer(sample));
		StringForest aztree = new StringForest(generateTemplate("test/templ2"), new SmartBuffer(sample));
		StringForest abtree = new StringForest(generateTemplate("test/templ3"), new SmartBuffer(sample));
		StringForest bctree = new StringForest(generateTemplate("test/templ4"), new SmartBuffer(sample));
		alltree.run();
		aztree.run();
		abtree.run();
		bctree.run();

		String query = "root.zero.one";
		Assert.assertEquals(asList("BC", "BB"), alltree.getBest().find(query));
		Assert.assertEquals(asList(), aztree.getBest().find(query));
		Assert.assertEquals(asList(), abtree.getBest().find(query));
		Assert.assertEquals(asList(), bctree.getBest().find(query));
	}

	@Test
	public void test_findX() throws Exception {

		String sample = "ABXCZABBZ";
		StringForest alltree = new StringForest(generateTemplate("test/templ1"), new SmartBuffer(sample));
		StringForest aztree = new StringForest(generateTemplate("test/templ2"), new SmartBuffer(sample));
		StringForest abtree = new StringForest(generateTemplate("test/templ3"), new SmartBuffer(sample));
		StringForest bctree = new StringForest(generateTemplate("test/templ4"), new SmartBuffer(sample));
		alltree.run();
		aztree.run();
		abtree.run();
		bctree.run();

		String query = "root.zero.x";
		Assert.assertEquals(asList(), alltree.getBest().find(query));
		Assert.assertEquals(asList("X"), aztree.getBest().find(query));
		Assert.assertEquals(asList(), abtree.getBest().find(query));
		Assert.assertEquals(asList("X"), bctree.getBest().find(query));
	}

	// @Test
	public void testTemplateGen_Basic() throws Exception {
		TemplateGenerator generator = new TemplateGenerator();
		generator.addSample("ABCZ");
		generator.addSample("ABCZABBZ");
		generator.addSample("ABZAZABCZ");

		TemplateNode generatedTemplate = generator.generateTemplate();

		System.out.println(generatedTemplate.toString());
	}

	private static void print(String output, float coverage) {
		System.out.println(output + "  " + String.valueOf(coverage * 100) + "%");
	}

	private static TemplateTree generateTemplate(String filePath) throws Exception {
		TemplateParser parser = new TemplateParser();
		parser.parseTemplate(new File(filePath));
		return parser.getRoot();
	}

}
