package br.net.buzu.conserpro;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class Cnae {
	
	private int codigo;
	
	private String descricao;

	public Cnae() {
		
	}
	public Cnae(int codigo, String descricao) {
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
