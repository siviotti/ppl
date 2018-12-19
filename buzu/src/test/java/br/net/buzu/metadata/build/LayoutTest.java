package br.net.buzu.metadata.build;

import br.net.buzu.java.lang.Syntax;
import br.net.buzu.java.model.Domain;
import br.net.buzu.java.model.MetaInfo;
import br.net.buzu.java.model.MetaInfoTest;
import br.net.buzu.java.model.Subtype;
import org.junit.Test;

import java.util.List;

import static br.net.buzu.metadata.build.Layout.NO_PRECISION;
import static br.net.buzu.metadata.build.Layout.NO_SIZE;
import static br.net.buzu.java.model.MetaInfoTest.domain;
import static org.junit.Assert.*;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class LayoutTest {
	
	static int ORDER_CHILDREN_COUNT = 6;


	private static void assertMetainfo(MetaInfo metaInfo, String name, Subtype subtype, int size, int precision, int minOccurs, int maxOccurs){
		MetaInfoTest.assertMetainfo(metaInfo, name, subtype, size, precision, minOccurs, maxOccurs);
	}
	
	public static Layout createPersonLayout(){
		Layout layout = new Layout()
				.field("fullName", Subtype.STRING, 10, 0, 1)
				.field("age", Subtype.INTEGER, 3, 0, 1)
				.field("city",Subtype.STRING, 10, 0, 1);
		return layout;
	}
	
	public static Layout createOrderLayout(){
		Layout layout = new Layout("order")
				.atomic("number", Subtype.STRING, 10, true)
				.record("customer", true)
					.atomic("name", Subtype.STRING, 20)
					.table("addresses", 1, 2)
						.atomic("street", Subtype.STRING, 30, true)
						.atomic("city", Subtype.STRING, 20, true)
						.atomic("zip", Subtype.CHAR, 8, true)
						.atomic("elementType", Subtype.STRING, 8, false, "", domain("BILLING", "DELIVERY"), "")
					.end()
					.array("phones", Subtype.CHAR, 9, 1, 3)
				.end()
				.atomic("date", Subtype.DATE, true)
				.table("products", 1, 5)
					.atomic("description", Subtype.STRING, 10, true)
					.atomic("price", Subtype.NUMBER, 8,2,  true)
				.end()
				.atomic("status", Subtype.INTEGER, 1, true, "0", domain("0","1", "2"), "")
				.atomic("canceled", Subtype.BOOLEAN)
				;
		return layout;
	}
	
	@Test
	public void testCreate() {
		Layout l1 = new Layout("test", Subtype.STRING, 10, 0, 1, 1, "abc", Domain.Companion.getEMPTY(), "XYZ");
		assertEquals("abc", l1.metaInfo().getDefaultValue());
		Layout l2 = new Layout("test", Subtype.STRING, 10, 0, 1, 1); // no extension
		assertEquals(Syntax.EMPTY, l2.metaInfo().getDefaultValue());
		Layout l3 = new Layout("test", Subtype.STRING, 10, 1, 1); // no scale
		assertEquals(0, l3.metaInfo().getScale());
		Layout l4 = new Layout("test", 10, 1, 1); // no subtype (OBJ)
		assertEquals(Subtype.OBJ, l4.metaInfo().getSubtype());
		Layout l5 = new Layout("test", 10); // no subtype (OBJ) #0-1
		assertEquals(0, l5.metaInfo().getMinOccurs());
		assertEquals(1, l5.metaInfo().getMaxOccurs());
		Layout l6 = new Layout("test", Subtype.STRING); // size -1  #0-1
		assertEquals(Layout.NO_SIZE, l6.metaInfo().getSize());
		Layout l7 = new Layout("test"); // size -1 (OBJ) #0-1
		assertEquals(Layout.NO_SIZE, l7.metaInfo().getSize());
		Layout l8 = new Layout(); // "root" size -1 (OBJ) #0-1
		assertEquals(Layout.ROOT, l8.metaInfo().getName());
		
	}
	
	@Test
	public void testBasic() {
		Layout l1 = new Layout("test", Subtype.STRING, 10, 0, 1, 1, "abc", Domain.Companion.getEMPTY(), "XYZ");
		assertEquals("abc", l1.metaInfo().getDefaultValue());
		try {
			l1.end(); // no parent
			fail();
		} catch (IllegalStateException e) {
			assertTrue(e.getMessage().equals(Layout.NONE_OPENED_LAYOUT));
		}

		l1.atomic("field1");
		l1.atomic("field2", 10);
		assertEquals(2, l1.getChildren().size());
		Layout child1 = l1.getChildren().get(0);
		assertEquals(Subtype.Companion.getDEFAULT_SINGLE(), child1.metaInfo().getSubtype());
		assertEquals(Layout.NO_SIZE, child1.metaInfo().getSize());
		
		Layout l2 = new Layout("test", Subtype.STRING, 10, 0, 1, 1, "abc", Domain.Companion.getEMPTY(), "XYZ");
		l2.atomic("field1");
		l2.atomic("field2", 10);
		assertEquals(l1.toTree(0), l2.toTree(0));
	}


	@Test
	public void testSimple() {
		Layout layout = createPersonLayout();
		assertEquals(3, layout.childrenSize());
		List<Layout> children = layout.getChildren();
		assertMetainfo(children.get(0).metaInfo(), "fullName", Subtype.STRING, 10,NO_PRECISION, 0, 1);
		assertMetainfo(children.get(1).metaInfo(), "age", Subtype.INTEGER, 3, NO_PRECISION,0, 1);
		assertMetainfo(children.get(2).metaInfo(), "city", Subtype.STRING, 10, NO_PRECISION,0, 1);
		
		assertEquals(layout, children.get(0).parent());
	}

	@Test
	public void testComplex() {
		Layout layout = createOrderLayout();
		
		assertEquals(ORDER_CHILDREN_COUNT, layout.childrenSize());
		List<Layout> children = layout.getChildren();
		
		// 1 - number
		Layout number = children.get(0);
		assertMetainfo(number.metaInfo(), "number", Subtype.STRING, 10, NO_PRECISION, 1, 1);
		
		// 2 - customer
		Layout customer = children.get(1);
		assertMetainfo(customer.metaInfo(), "customer", Subtype.OBJ, NO_SIZE,NO_PRECISION,  1, 1);
		List<Layout> customerChildren = customer.getChildren();
		
		Layout name = customerChildren.get(0);
		assertMetainfo(name.metaInfo(), "name", Subtype.STRING, 20, NO_PRECISION, 0, 1);
		
		Layout adress = customerChildren.get(1);
		assertMetainfo(adress.metaInfo(), "addresses", Subtype.OBJ, NO_SIZE, NO_PRECISION, 1, 2);
		List<Layout> adressChildren = adress.getChildren();
		Layout street = adressChildren.get(0);
		assertMetainfo(street.metaInfo(), "street", Subtype.STRING, 30,NO_PRECISION,  1, 1);
		Layout city = adressChildren.get(1);
		assertMetainfo(city.metaInfo(), "city", Subtype.STRING, 20, NO_PRECISION, 1, 1);
		Layout zip = adressChildren.get(2);
		assertMetainfo(zip.metaInfo(), "zip", Subtype.CHAR, 8,NO_PRECISION,  1, 1);
		Layout type = adressChildren.get(3);
		assertMetainfo(type.metaInfo(), "elementType", Subtype.STRING, 8,NO_PRECISION,  0, 1);
		
		Layout creditCard = customerChildren.get(2);
		assertMetainfo(creditCard.metaInfo(), "phones", Subtype.CHAR, 9,NO_PRECISION,  1, 3);
		
		
		// 3 - date
		Layout date = children.get(2);
		assertMetainfo(date.metaInfo(), "date", Subtype.DATE, 8, NO_PRECISION, 1, 1);
		
		// 4 - products
		Layout products = children.get(3);
		assertMetainfo(products.metaInfo(), "products", Subtype.OBJ, NO_SIZE, NO_PRECISION, 1, 5);
		List<Layout> productsChildren = products.getChildren();
		Layout description = productsChildren.get(0);
		assertMetainfo(description.metaInfo(), "description", Subtype.STRING, 10,NO_PRECISION,  1, 1);
		Layout price = productsChildren.get(1);
		assertMetainfo(price.metaInfo(), "price", Subtype.NUMBER, 8, 2, 1, 1);
		
		// 5 - status
		Layout open = children.get(4);
		assertMetainfo(open.metaInfo(), "status", Subtype.INTEGER, 1, NO_PRECISION, 1, 1);
		
		// 6 - canceled		
		Layout delivered = children.get(5);
		assertMetainfo(delivered.metaInfo(), "canceled", Subtype.BOOLEAN, 5, NO_PRECISION, 0, 1);
		
	}


}

