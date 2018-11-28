package br.net.buzu.metadata.code;

import br.net.buzu.metadata.BasicMetadata;
import br.net.buzu.metadata.ComplexMetadata;
import br.net.buzu.metadata.ComplexMetadataTest;
import br.net.buzu.metadata.SimpleMetadataTest;
import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.model.Dialect;
import br.net.buzu.pplspec.model.MetaInfoTest;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.pplspec.model.Subtype;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class MetadataCoderTest {

	private static final BasicMetadata COLOR = SimpleMetadataTest.createSample("color", Subtype.STRING, 10, 0, 0, 1,
			MetaInfoTest.domain("black", "white", "red"), "red", "KIX");
	private static final BasicMetadata SIZE = SimpleMetadataTest.createSample("size", Subtype.INTEGER, 10, 0, 1, 1,
			null, null, null);
	private static final List<Metadata> CHILDREN = new ArrayList<>();

	static {
		CHILDREN.add(COLOR);
		CHILDREN.add(SIZE);
	}
	private static final ComplexMetadata BOOK = ComplexMetadataTest.createSample("book", Subtype.OBJ, 0, 0, 0, 0, null, null, null, CHILDREN);
	
	@Test
	public void testVerbose() {
		VerboseMetadataCoder coder = new VerboseMetadataCoder();
		assertEquals(Dialect.VERBOSE, coder.dialect());
		assertEquals(VerboseMetadataCoder.SPACE, coder.afterName());
		assertEquals(VerboseMetadataCoder.SPACE, coder.afterType());
		assertEquals(VerboseMetadataCoder.SPACE, coder.afterSize());
		assertEquals(VerboseMetadataCoder.TAB, coder.indentationElement());
		assertEquals(VerboseMetadataCoder.ENTER, coder.afterMetadataEnd());
		assertEquals(VerboseMetadataCoder.ENTER, coder.afterMetadataEnd());
		
		String code = coder.code(BOOK);
		
		assertTrue(code.contains(Syntax.Companion.getDEFAULT_OCCURS()));
		assertTrue(code.contains(""+Syntax.ENTER));
		assertTrue(code.contains(""+Subtype.STRING.getId()));
		
	}

	@Test
	public void testNatural() {
		NaturalMetadataCoder coder = new NaturalMetadataCoder();
		assertEquals(Dialect.NATURAL, coder.dialect());
		assertEquals(VerboseMetadataCoder.SPACE, coder.afterName());
		assertEquals(VerboseMetadataCoder.SPACE, coder.afterType());
		assertEquals(VerboseMetadataCoder.SPACE, coder.afterSize());
		assertEquals(VerboseMetadataCoder.TAB, coder.indentationElement());
		assertEquals(VerboseMetadataCoder.ENTER, coder.afterMetadataEnd());
		
		String code = coder.code(BOOK);
		
		assertFalse(code.contains(Syntax.Companion.getDEFAULT_OCCURS()));
		assertTrue(code.contains(""+Syntax.ENTER));
		assertTrue(code.contains(""+Subtype.STRING.getId()));
	}

	@Test
	public void testShort() {
		ShortMetadataCoder coder = new ShortMetadataCoder();
		assertEquals(Dialect.SHORT, coder.dialect());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterName());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterType());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterSize());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.indentationElement());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterMetadataEnd());
		
		String code = coder.code(BOOK);
		
		assertFalse(code.contains(Syntax.Companion.getDEFAULT_OCCURS()));
		assertFalse(code.contains(""+Syntax.ENTER));
		assertTrue(code.contains(""+Subtype.STRING.getId()));
	}

	@Test
	public void testCompact() {
		CompactMetadataCoder coder = new CompactMetadataCoder();
		assertEquals(Dialect.COMPACT, coder.dialect());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterName());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterType());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterSize());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.indentationElement());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterMetadataEnd());
		
		String code = coder.code(BOOK);
		
		assertFalse(code.contains(Syntax.Companion.getDEFAULT_OCCURS()));
		assertFalse(code.contains(""+Syntax.ENTER));
		assertFalse(code.contains(""+Subtype.STRING.getId()));
	}

	@Test
	public void testStructural() {
		StructuralMetadataCoder coder = new StructuralMetadataCoder();
		assertEquals(Dialect.STRUCTURAL, coder.dialect());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterName());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterType());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterSize());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.indentationElement());
		assertEquals(VerboseMetadataCoder.EMPTY, coder.afterMetadataEnd());
		
		String code = coder.code(BOOK);
		
		assertFalse(code.contains(Syntax.Companion.getDEFAULT_OCCURS()));
		assertFalse(code.contains(""+Syntax.ENTER));
	}

}
