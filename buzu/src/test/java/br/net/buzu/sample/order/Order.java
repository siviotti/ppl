package br.net.buzu.sample.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Sample complex object
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class Order {

	private String number;
	private Customer customer;
	private LocalDate date;
	private List<Product> products = new ArrayList<>();
	private Status status;
	private boolean canceled;
	
	public Order(){
		
	}
	
	public Order(String number, Customer customer, LocalDate date, List<Product> products, Status status,
			boolean canceled) {
		super();
		this.number = number;
		this.customer = customer;
		this.date = date;
		this.products = products;
		this.status = status;
		this.canceled = canceled;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (canceled ? 1231 : 1237);
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((products == null) ? 0 : products.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Order other = (Order) obj;
		if (canceled != other.canceled)
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (products == null) {
			if (other.products != null)
				return false;
		} else if (!products.equals(other.products))
			return false;
        return status == other.status;
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("number=").append(number).append("\n");
		sb.append("customer\n").append(customer).append("\n");
		sb.append("date=").append(date).append("\n");
		sb.append("products=").append(products).append("\n");
		sb.append("status=").append(status).append("\n");
		sb.append("canceled=").append(canceled);
		return sb.toString();
	}
	
	
}
