package br.net.buzu.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer {
	
	private String name;
	private int age;
	private double weight;
	private Point point;
	private List<Address> addresses = new ArrayList<Address>();
	private List<String> phones = new ArrayList<String>();
	private boolean active;
	private Boolean vip;
	private Date birth;
	
	public Customer() {
		
	}
	
	public Customer(String name, List<Address> adresses, int age, String... phones) {
		super();
		this.name = name;
		this.addresses = adresses;
		this.age = age;
		for (String phone: phones){
			this.phones.add(phone);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> adresses) {
		this.addresses = adresses;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Boolean getVip() {
		return vip;
	}

	public void setVip(Boolean vip) {
		this.vip = vip;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	@Override
	public String toString() {
		return  name + "(" + age + ")";
	}

	
}
