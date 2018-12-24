package br.net.buzu.sample.ppl;

import br.net.buzu.annotation.PplMetadata;
import br.net.buzu.annotation.PplUse;
import br.net.buzu.model.Subtype;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Plain annotation sample.
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class Human implements Serializable{
	
	private static final long serialVersionUID = 1L;
	static final String IGNORED_FIELD = "ignore";
	
	@PplUse
	@PplMetadata(name="fixed-field", size=5, subtype=Subtype.CHAR)
	private static final String FIXED_FIELD = "fix";

	@PplMetadata(name="fullName", size=10, subtype=Subtype.CHAR)
	private String name;
	
	@PplMetadata(index=3, name="birthDay", subtype=Subtype.ISO_DATE)
	private LocalDate birth;
	
	@PplMetadata(index= 2, size=5)
	private double weight;
	
	public Human(String name, LocalDate birth, double weight) {
		super();
		this.name = name;
		this.birth = birth;
		this.weight = weight;
	}

	public Human(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public static String getFIXED_FIELD() {
		return FIXED_FIELD;
	}

	public static void setFIXED_FIELD(String fIXED_FIELD) {
	}

}
