package br.net.buzu.conserpro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe que representa uma nota fiscal.
 *
 * @author Clecio Lopes (080.685.887-79) #21 3509-7145
 * 
 * @author DouglasSiviotti (073.116.317-69) #21 3509-7585 [30/06/2017-1.2]
 * 
 * @since 12/09/2016 - Construção Inicial da classe
 *
 */
public class NotaFiscal implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * CHAVE OID. Sequence do Banco.
	 */
	private Integer id; // NR SQ NUMBER(9) SIM

	/**
	 * CHAVE NATURAL. Chave da Nota Fiscal composta por outros campos nesta
	 * ordem:<BR>
	 * codigo UF(02) - AAMM da emissão(04) - CNPJ do emitente (14) -
	 */
	private String chaveAcesso; // NR CHAVE VARCHAR2(44) NÃO

	// ******************** Campos que compõe a chave ********************

	private String ufEmissor;

	private String anoMes; // DT AAMM VARCHAR2(4) SIM
	private String cnpjEmitente;
	private String modelo; // CD MODELO VARCHAR2(2) SIM
	private Integer serie; // NR SERIE NUMBER(3) SIM
	private Integer numeroDocumento; // NR DOC FISCAL NUMBER(9) SIM
	private Integer cnf; // CD CNF NUMBER(8) NÃO
	private Integer dv; // CD DV NUMBER(1) NÃO

	// ******************** ??? ********************

	private String cancelada; 
	private String localDestinoOperacao; 
	private String identificacaoEstrangeiro; 
	private String situacaoNFe; 
	private Date dataEmissao; 
	private String nomeImportador; 
	private String enderecoImportador; 
	private String finalidade; 
	private String cpfEmitente;
	private String cnpjDestinatario;
	private String cpfDestinatario;
	private Integer pais;
	private List<ItemNotaFiscal> items = new ArrayList<ItemNotaFiscal>();

	private String tipoEmissao;

	// ********* get - set *************

	public String getCancelada() {
		return cancelada;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public Integer getCnf() {
		return cnf;
	}

	public String getCnpjDestinatario() {
		return cnpjDestinatario;
	}

	public String getCnpjEmitente() {
		return cnpjEmitente;
	}

	public String getCpfDestinatario() {
		return cpfDestinatario;
	}

	public String getCpfEmitente() {
		return cpfEmitente;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public Integer getDv() {
		return dv;
	}

	public String getEnderecoImportador() {
		return enderecoImportador;
	}

	public String getFinalidade() {
		return finalidade;
	}

	public Integer getId() {
		return id;
	}

	public String getIdentificacaoEstrangeiro() {
		return identificacaoEstrangeiro;
	}

	public List<ItemNotaFiscal> getItems() {
		return items;
	}

	public String getLocalDestinoOperacao() {
		return localDestinoOperacao;
	}

	public String getModelo() {
		return modelo;
	}

	public String getNomeImportador() {
		return nomeImportador;
	}


	public Integer getNumeroDocumento() {
		return numeroDocumento;
	}

	public Integer getPais() {
		return pais;
	}

	public Integer getSerie() {
		return serie;
	}

	public String getSituacaoNFe() {
		return situacaoNFe;
	}

	public String getUfEmissor() {
		return ufEmissor;
	}

	public void setCancelada(String cancelada) {
		this.cancelada = cancelada;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	public void setCnf(Integer cnf) {
		this.cnf = cnf;
	}

	public void setCnpjDestinatario(String cnpjDestinatario) {
		this.cnpjDestinatario = cnpjDestinatario;
	}

	public void setCnpjEmitente(String cnpjEmitente) {
		this.cnpjEmitente = cnpjEmitente;
	}

	public void setCpfDestinatario(String cpfDestinatario) {
		this.cpfDestinatario = cpfDestinatario;
	}

	public void setCpfEmitente(String cpfEmitente) {
		this.cpfEmitente = cpfEmitente;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public void setDv(Integer dv) {
		this.dv = dv;
	}

	public void setEnderecoImportador(String enderecoImportador) {
		this.enderecoImportador = enderecoImportador;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIdentificacaoEstrangeiro(String identificacaoEstrangeiro) {
		this.identificacaoEstrangeiro = identificacaoEstrangeiro;
	}

	public void setItems(List<ItemNotaFiscal> items) {
		this.items = items;
	}

	public void setLocalDestinoOperacao(String localDestinoOperacao) {
		this.localDestinoOperacao = localDestinoOperacao;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public void setNomeImportador(String nomeImportador) {
		this.nomeImportador = nomeImportador;
	}

	public void setNumeroDocumento(Integer numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public void setPais(Integer pais) {
		this.pais = pais;
	}

	public void setSerie(Integer serie) {
		this.serie = serie;
	}

	public void setSituacaoNFe(String situacaoNFe) {
		this.situacaoNFe = situacaoNFe;
	}

	public void setUfEmissor(String ufEmissor) {
		this.ufEmissor = ufEmissor;
	}

	public String getAnoMes() {
		return anoMes;
	}

	public void setAnoMes(String anoMes) {
		this.anoMes = anoMes;
	}

	/**
	 * @return the tipoEmissao
	 */
	public String getTipoEmissao() {
		return tipoEmissao;
	}

	/**
	 * @param tipoEmissao
	 *            the tipoEmissao to set
	 */
	public void setTipoEmissao(String tipoEmissao) {
		this.tipoEmissao = tipoEmissao;
	}

}
