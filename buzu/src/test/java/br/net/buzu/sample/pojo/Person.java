package br.net.buzu.sample.pojo;

/**
 *
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class Person {

	private String name;
	private Integer age;
	private String city;

	public Person() {

	}

	public Person(String name, int age, String city) {
		super();
		this.name = name;
		this.age = age;
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", city=" + city + "]";
	}


}
