package br.net.buzu.sample.enums;

public class Animal {
	
	private String name;
	private Species species;
	private Gender gender;
	
	public Animal(){
		
	}
	
	public Animal(String name, Species species, Gender gender) {
		super();
		this.name = name;
		this.species = species;
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Species getSpecies() {
		return species;
	}

	public void setSpecies(Species species) {
		this.species = species;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((species == null) ? 0 : species.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Animal)) {
			return false;
		}
		Animal other = (Animal) obj;
		if (gender != other.gender) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
        return species == other.species;
    }

	@Override
	public String toString() {
		return species + "(" + gender.getCode() + ") " + name  ;
	}

}
