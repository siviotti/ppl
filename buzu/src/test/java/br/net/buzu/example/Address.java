package br.net.buzu.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Address {
	
	private String street;
	private String city;
	//private Product[] products;
	private List<Product> productList = new ArrayList<Product>();
	private String zip;
	
	public Address() {
		
	}
	
	public Address(String street, String city, String zip, Product ...products) {
		super();
		this.street = street;
		this.city = city;
		this.zip = zip;
		productList = Arrays.asList(products);
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	@Override
	public String toString() {
		return zip;
	}

	
	


}
