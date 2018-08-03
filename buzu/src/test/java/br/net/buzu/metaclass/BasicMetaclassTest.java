package br.net.buzu.metaclass;

import static org.junit.Assert.*;

import org.junit.Test;

import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.sample.order.Order;

/**
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BasicMetaclassTest {

	@Test
	public void test() {
		Metaclass mc1 = new BasicMetaclassReader().read(Order.class);
		Metaclass mc2 = new BasicMetaclassReader().read(Order.class);

		assertTrue(mc1.equals(mc2));
		assertTrue(mc2.equals(mc1));
		assertTrue(mc2.hashCode() == mc1.hashCode());
		assertTrue(mc2.toString().equals(mc1.toString()));
		assertTrue(mc2.toTree(0).equals(mc1.toTree(0)));
		assertTrue(mc1.equals(mc1));
		assertFalse(mc1.equals(null));
		Object o = new String("abc");
		assertFalse(mc1.equals(o));
	}

}
