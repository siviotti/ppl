package br.net.buzu.sample.ppl;

import br.net.buzu.java.annotation.PplMetadata;
import br.net.buzu.java.model.Subtype;

/**
 *
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
@PplMetadata(maxOccurs=1)
public class StaticPerson {

	@PplMetadata(name="personName", size=10, maxOccurs=1, subtype=Subtype.CHAR)
	private String name;
	@PplMetadata(name="personAge", size=10, maxOccurs=1, subtype=Subtype.INTEGER)
	private Integer age;
	@PplMetadata(name="personCity", size=10, maxOccurs=1, subtype=Subtype.CHAR)
	private String city;

	public StaticPerson() {

	}

	public StaticPerson(String name, int age, String city) {
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
