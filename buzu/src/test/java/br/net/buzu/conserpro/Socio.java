package br.net.buzu.conserpro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Socio {
	
	private String cpf;
	
	private String nome;
	
	private double percentual;
	
	private List<Telefone> telefones = new ArrayList<Telefone>();
	
	private List<String> emails = new ArrayList<String>();
	
	public Socio() {
		
	}

	public Socio(String cpf, String nome, double percentual, List<Telefone> telefones, String... emails) {
		super();
		this.cpf = cpf;
		this.nome = nome;
		this.percentual = percentual;
		this.telefones = telefones;
		if (emails != null) {
			this.emails = Arrays.asList(emails);		
		}
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getPercentual() {
		return percentual;
	}

	public void setPercentual(double percentual) {
		this.percentual = percentual;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	@Override
	public String toString() {
		return cpf + " " + nome + " (" +telefones.size() + "tels)";
	}
	
	

}
