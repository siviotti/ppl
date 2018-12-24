package br.net.buzu.sample.ppl;

import br.net.buzu.annotation.PplMetadata;

import java.time.LocalDate;

/**
 * Extension sample.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class Xmen extends Human{

	private static final long serialVersionUID = 1L;
	
	public static final Xmen WOLVERINE;
	
	static{
		WOLVERINE = new Xmen();
		WOLVERINE.setName("Wolverine");
		WOLVERINE.setBirth(LocalDate.of(1882, 04, 5));
		WOLVERINE.setWeight(99);
		WOLVERINE.setSkill("healing");
	}
	
	@PplMetadata(index=4, name="power", size=20)
	private String skill;

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

}
