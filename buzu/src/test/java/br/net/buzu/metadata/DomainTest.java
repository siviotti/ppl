package br.net.buzu.metadata;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.net.buzu.pplspec.model.Domain;
import br.net.buzu.pplspec.model.DomainItem;

/**
 * Unit Test of StringDomain
 * 
 * @author Douglas Siviotti
 * @since 1.0
 */
public class DomainTest {
	
	private DomainItem item1 = new DomainItem("01", "One");
	private DomainItem item2 = new DomainItem("02", "Two");
	private DomainItem item3 = new DomainItem("03", "Three");
	
	

	@Test
	public void test() {
		List<DomainItem> items = new ArrayList<>();
		items.add(item1);
		items.add(item2);
		items.add(item3);
		Domain domain = Domain.create("testDomain", items);
		assertEquals(3, domain.items().size());
	}

}

