package br.net.buzu.parsing.simple;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.net.buzu.Buzu;
import br.net.buzu.sample.enums.Animal;
import br.net.buzu.sample.enums.Card;
import br.net.buzu.sample.enums.Gender;
import br.net.buzu.sample.enums.Species;
import br.net.buzu.sample.enums.Suit;

/**
 * Unit Test for EnumNameParser.
 * 
 * @author Douglas Siviotti (073.116.317-69) #21 3509-7585
 * @since 1.0
 *
 */
public class EnumNameParserTest {
	
	private Buzu buzu;
	
	@Before
	public void before(){
		buzu = new Buzu();
	}

	@Test
	public void testSimpleEnum() {
		Card card = new Card(Suit.Clubs, 7);
		String cardStr = buzu.toPpl(card);
		Card card2 = buzu.fromPpl(cardStr, Card.class);
		assertEquals(card, card2);
	}

	@Test
	public void testEnumParserInterface() {
		Animal leo = new Animal("Leo", Species.LION, Gender.MALE);
		String str = buzu.toPpl(leo);
		assertTrue(str.endsWith("M"));
		Animal leo2 = buzu.fromPpl(str, Animal.class);
		assertEquals(leo,  leo2);
	}
}
