package br.net.buzu.example;

import java.math.BigDecimal;

public class Price {
	
	private String currency = "$";
	private BigDecimal value;
	
	public Price() {
		
	}
	
	public Price(String currency, BigDecimal value) {
		super();
		this.currency = currency;
		this.value = value;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return currency + value;
	}

}
