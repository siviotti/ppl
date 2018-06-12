package br.net.buzu.sample.enums;

public class Card {

	private Suit suit;
	private int number;

	public Card() {

	}

	public Card(Suit suit, int number) {
		super();
		this.suit = suit;
		this.number = number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (number != other.number)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "" + number + " of " + suit;
	}

	public Suit getSuit() {
		return suit;
	}

	public int getNumber() {
		return number;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
