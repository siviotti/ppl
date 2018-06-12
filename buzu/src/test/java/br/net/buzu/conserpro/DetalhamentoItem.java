package br.net.buzu.conserpro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe que representa o Detalhamento de um item de  nota fiscal. <p>
 * Cada item possui um ou mais Detalhamentos.<p>
 * O detalhamento existe para os seguintes tipos de item:<p>
 * - Combustiveis, Veículos, Automóveis e Medicamentos <p>
 *
 * @author Alexander Martins (075.305.427-29) #21 3509-7299
 * @since 10/10/2016 - Construção Inicial da classe
 *
 */
public class DetalhamentoItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Tipo de Detalhamento: 1.Veículo // 2.Medicamento // 3.Arma  // 4.Combustível 
	 */
	private String tipoDetalhamento;
	
	// ===============================================================================	
	// Veículos
	// ===============================================================================	
	private String chassi; 					// NR CHASSI NUMBER NÃO
	private String numeroSerie; 			// NR NSERIE VARCHAR2(15) NÃO
	private Integer tipoVeiculo; 			// CD TPVEIC NUMBER(2) NÃO
	private Integer especieVeiculo; 		// CD ESPVEIC NUMBER(1) NÃO
	private String condicaoVeiculo; 		// CD CONDVEIC NUMBER(1) NÃO 
	private Integer marcaModeloVeiculo; 	// CD CMOD NUMBER(6) NÃO
	private String restricao;				// CD TPREST NUMBER(1) NÃO 
	
	// ===============================================================================	  	  	  	  
	// Medicamentos
	// ===============================================================================
	private String lote; 			// NR NLOTE VARCHAR2(20) NÃO
	private BigDecimal qtLote; 		// QT QLOTE NUMBER(11,3) NÃO
	private Date dataFabricacao; 	// DT DFAB DATE NÃO
	private Date dataValidade; 		// DT DVAL DATE NÃO
	
	// ===============================================================================	
	// Armas
	// ===============================================================================
	private String tipoArma; 				// DITX_CD_TPARMA NUMBER(1)
	private String armaNumeroSerie;			// DITX_NR_NSERIE VARCHAR2(15)
	private String armaNumeroCano;			// DITX_NR_NCANO  VARCHAR2(15)
	private String armaDescricao; 			// TX DESCRICAO VARCHAR2(256) 
	
	// ===============================================================================	  	  	  
	// Combustíveis
	// ===============================================================================
	private Integer codProdutoANP; 			// "DITX_CD_CPRODANP	NUMBER(9)


	public String getTipoDetalhamento() {
		return tipoDetalhamento;
	}

	public void setTipoDetalhamento(String tipoDetalhamento) {
		this.tipoDetalhamento = tipoDetalhamento;
	}

	public String getChassi() {
		return chassi;
	}

	public void setChassi(String chassi) {
		this.chassi = chassi;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public Integer getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(Integer tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

	public Integer getEspecieVeiculo() {
		return especieVeiculo;
	}

	public void setEspecieVeiculo(Integer especieVeiculo) {
		this.especieVeiculo = especieVeiculo;
	}

	public String getCondicaoVeiculo() {
		return condicaoVeiculo;
	}

	public void setCondicaoVeiculo(String condicaoVeiculo) {
		this.condicaoVeiculo = condicaoVeiculo;
	}

	public Integer getMarcaModeloVeiculo() {
		return marcaModeloVeiculo;
	}

	public void setMarcaModeloVeiculo(Integer marcaModeloVeiculo) {
		this.marcaModeloVeiculo = marcaModeloVeiculo;
	}

	public String getRestricao() {
		return restricao;
	}

	public void setRestricao(String restricao) {
		this.restricao = restricao;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public BigDecimal getQtLote() {
		return qtLote;
	}

	public void setQtLote(BigDecimal qtLote) {
		this.qtLote = qtLote;
	}

	public Date getDataFabricacao() {
		return dataFabricacao;
	}

	public void setDataFabricacao(Date dataFabricacao) {
		this.dataFabricacao = dataFabricacao;
	}

	public Date getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}

	public String getTipoArma() {
		return tipoArma;
	}

	public void setTipoArma(String tipoArma) {
		this.tipoArma = tipoArma;
	}

	public String getArmaNumeroSerie() {
		return armaNumeroSerie;
	}

	public void setArmaNumeroSerie(String armaNumeroSerie) {
		this.armaNumeroSerie = armaNumeroSerie;
	}

	public String getArmaNumeroCano() {
		return armaNumeroCano;
	}

	public void setArmaNumeroCano(String armaNumeroCano) {
		this.armaNumeroCano = armaNumeroCano;
	}

	public String getArmaDescricao() {
		return armaDescricao;
	}

	public void setArmaDescricao(String armaDescricao) {
		this.armaDescricao = armaDescricao;
	}

	public Integer getCodProdutoANP() {
		return codProdutoANP;
	}

	public void setCodProdutoANP(Integer codProdutoANP) {
		this.codProdutoANP = codProdutoANP;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((armaDescricao == null) ? 0 : armaDescricao.hashCode());
		result = prime * result + ((armaNumeroCano == null) ? 0 : armaNumeroCano.hashCode());
		result = prime * result + ((armaNumeroSerie == null) ? 0 : armaNumeroSerie.hashCode());
		result = prime * result + ((chassi == null) ? 0 : chassi.hashCode());
		result = prime * result + ((codProdutoANP == null) ? 0 : codProdutoANP.hashCode());
		result = prime * result + ((condicaoVeiculo == null) ? 0 : condicaoVeiculo.hashCode());
		result = prime * result + ((dataFabricacao == null) ? 0 : dataFabricacao.hashCode());
		result = prime * result + ((dataValidade == null) ? 0 : dataValidade.hashCode());
		result = prime * result + ((especieVeiculo == null) ? 0 : especieVeiculo.hashCode());
		result = prime * result + ((lote == null) ? 0 : lote.hashCode());
		result = prime * result + ((marcaModeloVeiculo == null) ? 0 : marcaModeloVeiculo.hashCode());
		result = prime * result + ((numeroSerie == null) ? 0 : numeroSerie.hashCode());
		result = prime * result + ((qtLote == null) ? 0 : qtLote.hashCode());
		result = prime * result + ((restricao == null) ? 0 : restricao.hashCode());
		result = prime * result + ((tipoArma == null) ? 0 : tipoArma.hashCode());
		result = prime * result + ((tipoDetalhamento == null) ? 0 : tipoDetalhamento.hashCode());
		result = prime * result + ((tipoVeiculo == null) ? 0 : tipoVeiculo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalhamentoItem other = (DetalhamentoItem) obj;
		if (armaDescricao == null) {
			if (other.armaDescricao != null)
				return false;
		} else if (!armaDescricao.equals(other.armaDescricao))
			return false;
		if (armaNumeroCano == null) {
			if (other.armaNumeroCano != null)
				return false;
		} else if (!armaNumeroCano.equals(other.armaNumeroCano))
			return false;
		if (armaNumeroSerie == null) {
			if (other.armaNumeroSerie != null)
				return false;
		} else if (!armaNumeroSerie.equals(other.armaNumeroSerie))
			return false;
		if (chassi == null) {
			if (other.chassi != null)
				return false;
		} else if (!chassi.equals(other.chassi))
			return false;
		if (codProdutoANP == null) {
			if (other.codProdutoANP != null)
				return false;
		} else if (!codProdutoANP.equals(other.codProdutoANP))
			return false;
		if (condicaoVeiculo == null) {
			if (other.condicaoVeiculo != null)
				return false;
		} else if (!condicaoVeiculo.equals(other.condicaoVeiculo))
			return false;
		if (dataFabricacao == null) {
			if (other.dataFabricacao != null)
				return false;
		} else if (!dataFabricacao.equals(other.dataFabricacao))
			return false;
		if (dataValidade == null) {
			if (other.dataValidade != null)
				return false;
		} else if (!dataValidade.equals(other.dataValidade))
			return false;
		if (especieVeiculo == null) {
			if (other.especieVeiculo != null)
				return false;
		} else if (!especieVeiculo.equals(other.especieVeiculo))
			return false;
		if (lote == null) {
			if (other.lote != null)
				return false;
		} else if (!lote.equals(other.lote))
			return false;
		if (marcaModeloVeiculo == null) {
			if (other.marcaModeloVeiculo != null)
				return false;
		} else if (!marcaModeloVeiculo.equals(other.marcaModeloVeiculo))
			return false;
		if (numeroSerie == null) {
			if (other.numeroSerie != null)
				return false;
		} else if (!numeroSerie.equals(other.numeroSerie))
			return false;
		if (qtLote == null) {
			if (other.qtLote != null)
				return false;
		} else if (!qtLote.equals(other.qtLote))
			return false;
		if (restricao == null) {
			if (other.restricao != null)
				return false;
		} else if (!restricao.equals(other.restricao))
			return false;
		if (tipoArma == null) {
			if (other.tipoArma != null)
				return false;
		} else if (!tipoArma.equals(other.tipoArma))
			return false;
		if (tipoDetalhamento == null) {
			if (other.tipoDetalhamento != null)
				return false;
		} else if (!tipoDetalhamento.equals(other.tipoDetalhamento))
			return false;
		if (tipoVeiculo == null) {
			if (other.tipoVeiculo != null)
				return false;
		} else if (!tipoVeiculo.equals(other.tipoVeiculo))
			return false;
		return true;
	}
		
}
