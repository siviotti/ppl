package br.net.buzu.sample.enums;

import br.net.buzu.pplspec.model.PplSerializable;

public enum Gender implements PplSerializable {

	MALE('M', "Male"), FEMALE('F', "Female");

	private final char code;
	private final String description;

	Gender(char code, String descriptio) {
		this.code = code;
		this.description = descriptio;
	}

	@Override
	public String asPplSerial() {
		return "" + code;
	}

	public char getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
