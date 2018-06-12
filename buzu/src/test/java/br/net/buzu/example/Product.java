package br.net.buzu.example;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Product {

	private long code;
	private char category;
	private String nameProd;
	private Set<Price> prices = new HashSet<Price>();
	
	public Product() {
		// TODO Auto-generated constructor stub
	}

	public Product(long code, char category, String name) {
		super();
		this.code = code;
		this.category = category;
		this.nameProd = name;
		for (int i = 0; i < name.length(); i++) {
			prices.add(new Price("" + category, new BigDecimal("" + i + ".5")));
		}
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public char getCategory() {
		return category;
	}

	public void setCategory(char category) {
		this.category = category;
	}

	public String getNameProd() {
		return nameProd;
	}

	public void setNameProd(String name) {
		this.nameProd = name;
	}

	public Set<Price> getPrices() {
		return prices;
	}

	public void setPrices(Set<Price> prices) {
		this.prices = prices;
	}

	@Override
	public String toString() {
		return ""+ category + code + ":" + nameProd + prices;
	}

}
