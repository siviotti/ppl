package br.net.buzu.conserpro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um item de nota fiscal.
 *
 * @author Clecio Lopes (080.685.887-79) #21 3509-7145
 * @author Alexander Martins (075.305.427-29) #21 3509-7299
 * @since 12/09/2016 - Construção Inicial da classe
 *
 */
public class ItemNotaFiscal implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private BigDecimal numeroItem; // NR ITEM NUMBER(3) SIM
	private String descricao; // TX DESCRICAO VARCHAR2(256) SIM
	private Integer ncm; // CD NCM NUMBER(8) SIM
	private Integer cfop; // CD CFOP NUMBER(4) SIM
	private String codigoProduto; // CD PRODUTO VARCHAR2(60) SIM
	private String unidadeComercial; // NM UN COMERCIAL VARCHAR2(6) NÃO
	private BigDecimal qtComercial; // QT UN COMERCIAL NUMBER(15,4) NÃO
	private BigDecimal valorUnitario; // VL UNITARIO NUMBER(15,2) SIM
	private BigDecimal qtEstatistica; // QT ESTATISTICA NUMBER(15,4) SIM
	private BigDecimal valorTotalBruto; // VL TOTAL NUMBER(15,2) SIM
	private Integer gtin; // CD EAN NUMBER(14) NÃO
	private Integer gtinTributavel; // CD CEAN TRIB NUMBER(14) NÃO
	private String unidadeTributavel; // CD UTRIB VARCHAR2(6) NÃO
	private BigDecimal qtTributacao; // QT QTRIB NUMBER(11,4) NÃO
	private BigDecimal valorFrete; // VL VFRETE NUMBER(15,2) NÃO
	private BigDecimal valorSeguro; // VL VSEG NUMBER(15,2) NÃO
	private BigDecimal valorDesconto; // VL VDESC NUMBER(15,2) NÃO
	private BigDecimal valorOutros; // VL VOUTRO NUMBER(15,2) NÃO
	private Integer numeroAtoDrawback; // NR NDRAW NUMBER(11) NÃO
	private String numeroPedido; // NR XPED VARCHAR2(15) NÃO

	// Detalhamento de Item
	private List<DetalhamentoItem> detalhesItem = new ArrayList<DetalhamentoItem>();
	// DetalhamentoItemNotaFiscalDTO
	//

	// ********* get - set *****************

	public BigDecimal getNumeroItem() {
		return numeroItem;
	}

	public void setNumeroItem(BigDecimal numeroItem) {
		this.numeroItem = numeroItem;
	}

	public Integer getCfop() {
		return cfop;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getGtin() {
		return gtin;
	}

	public Integer getNcm() {
		return ncm;
	}

	public Integer getNumeroAtoDrawback() {
		return numeroAtoDrawback;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public BigDecimal getQtComercial() {
		return qtComercial;
	}

	public BigDecimal getQtEstatistica() {
		return qtEstatistica;
	}

	public BigDecimal getQtTributacao() {
		return qtTributacao;
	}

	public String getUnidadeComercial() {
		return unidadeComercial;
	}

	public String getUnidadeTributavel() {
		return unidadeTributavel;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public BigDecimal getValorOutros() {
		return valorOutros;
	}

	public BigDecimal getValorSeguro() {
		return valorSeguro;
	}

	public BigDecimal getValorTotalBruto() {
		return valorTotalBruto;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setCfop(Integer cfop) {
		this.cfop = cfop;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setGtin(Integer gtin) {
		this.gtin = gtin;
	}

	public void setNcm(Integer ncm) {
		this.ncm = ncm;
	}

	public void setNumeroAtoDrawback(Integer numeroAtoDrawback) {
		this.numeroAtoDrawback = numeroAtoDrawback;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public void setQtComercial(BigDecimal qtComercial) {
		this.qtComercial = qtComercial;
	}

	public void setQtEstatistica(BigDecimal qtEstatistica) {
		this.qtEstatistica = qtEstatistica;
	}

	public void setQtTributacao(BigDecimal qtTributacao) {
		this.qtTributacao = qtTributacao;
	}

	public void setUnidadeComercial(String unidadeComercial) {
		this.unidadeComercial = unidadeComercial;
	}

	public void setUnidadeTributavel(String unidadeTributavel) {
		this.unidadeTributavel = unidadeTributavel;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public void setValorOutros(BigDecimal valorOutros) {
		this.valorOutros = valorOutros;
	}

	public void setValorSeguro(BigDecimal valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	public void setValorTotalBruto(BigDecimal valorTotalBruto) {
		this.valorTotalBruto = valorTotalBruto;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Integer getGtinTributavel() {
		return gtinTributavel;
	}

	public void setGtinTributavel(Integer gtinTributavel) {
		this.gtinTributavel = gtinTributavel;
	}

	/**
	 * @return the detalhesItem
	 */
	public List<DetalhamentoItem> getDetalhesItem() {
		return detalhesItem;
	}

	/**
	 * @param detalhesItem
	 *            the detalhesItem to set
	 */
	public void setDetalhesItem(List<DetalhamentoItem> detalhesItem) {
		this.detalhesItem = detalhesItem;
	}

}
