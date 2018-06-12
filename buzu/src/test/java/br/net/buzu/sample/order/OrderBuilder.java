package br.net.buzu.sample.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test Data Builder for Order.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class OrderBuilder {

	private String number;
	private Customer customer;
	private LocalDate date;
	private List<Product> products = new ArrayList<>();
	private Status status;
	private boolean canceled;

	public Order build() {
		return new Order(number, customer, date, products, status, canceled);
	}

	public String getNumber() {
		return number;
	}

	public OrderBuilder number(String number) {
		this.number = number;
		return this;
	}

	public Customer getCustomer() {
		return customer;
	}

	public OrderBuilder customer(Customer customer) {
		this.customer = customer;
		return this;
	}

	public OrderBuilder customer(String name, String... phones) {
		this.customer = new Customer(name, null, Arrays.asList(phones));
		return this;
	}

	public OrderBuilder billing(String street, String city, String zip) {
		if (customer == null){
			throw new IllegalArgumentException("customer is null. call 'customer' method");
		}
		this.customer.addAddress(new Address(street, city, zip, AddressType.BILLING));
		return this;
	}

	public OrderBuilder delivery(String street, String city, String zip) {
		if (customer == null){
			throw new IllegalArgumentException("customer is null. call 'customer' method");
		}
		this.customer.addAddress(new Address(street, city, zip, AddressType.DELIVERY));
		return this;
	}

	public LocalDate getDate() {
		return date;
	}

	public OrderBuilder date(LocalDate date) {
		this.date = date;
		return this;
	}

	public List<Product> getProducts() {
		return products;
	}

	public OrderBuilder products(List<Product> products) {
		this.products.addAll(products);
		return this;
	}

	public OrderBuilder product(String description, double price) {
		products.add(new Product(description, price));
		return this;
	}

	public Status getStatus() {
		return status;
	}

	public OrderBuilder status(Status status) {
		this.status = status;
		return this;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public OrderBuilder canceled(boolean canceled) {
		this.canceled = canceled;
		return this;
	}
}
