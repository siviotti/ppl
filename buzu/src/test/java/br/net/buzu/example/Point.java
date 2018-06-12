package br.net.buzu.example;

import br.net.buzu.sample.pojo.Person;

public class Point {
	
	private int x;
	private int y;
	private Person person;
	
	public Point() {
		// TODO Auto-generated constructor stub
	}
	
	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	@Override
	public String toString() {
		return "(x:" + x + " y:" + y + ")";
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}

}
