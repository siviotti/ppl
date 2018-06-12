package br.net.buzu.conserpro;

public class NaturezaJuridica {
	
	private int codigo;
	
	private String descricao;

	public NaturezaJuridica() {
		
	}
	public NaturezaJuridica(int codigo, String descricao) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	@Override
	public String toString() {
		return codigo + "-" + descricao ;
	}

}
