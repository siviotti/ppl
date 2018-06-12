package br.net.buzu.conserpro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Rodrigo Adur (099.163.947-25) #21 3509-7369
 * @author Douglas Siviotti (073.116.317-69) #21 3509-7585 (refactoring)
 * @since 5 de jun de 2016 - Classe reposnsável por representar um item de Due.
 *
 */
public class ItemDue implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Date dataRegistro;

	private Integer identificadorItem;

	private String niExportador;

	private String ncm;

	private String unidadeComercializada;

	private BigDecimal qtdUnidadeComercializada;

	private BigDecimal valorUnitario;

	private BigDecimal qtdUnidadeEstatistica;

	private String descricaoMercadoria;

	private BigDecimal pesoLiquidoTotal;

	private String condicaoVenda;

	private Date dataConversao;

	private BigDecimal valorCondicaoVenda;

	private BigDecimal valorLocalEmbarque;

	private String descricaoComplementar;

	private int motivoPrioridade;

	private String licencaExportacao;

	private String numeroProcesso;

	private int tipoDeclaracaoImportacao;

	private String diEletronicaReexp;

	private String dsiEletronicaReexp;

	private String dsiFormularioReexp;

	private String edbvReexp;

	private String bemOuAdicao;

	private String deWebExpTemp;

	private String dueExpTemp;

	private Integer prazoExportacaoTemporaria;

	// Dados dos itens de DUE sem nota fiscal
	private String nomeExportador;

	private String numeroDocumentoIdentificacao;

	private String nacionalidade;

	private String nomeImportador;

	private String enderecoImportador;

	private Date dataBloqueio;

	private Date datadesbloqueio;

	// Listas


	private List<ExportacaoItemPaisDestino> exportacaoPaisDestino = new ArrayList<ExportacaoItemPaisDestino>();

	private BigDecimal percentualDeComissaoDoAgente;


	// ******************** GET/SET ********************


	public String getBemOuAdicao() {
		return bemOuAdicao;
	}

	public String getCondicaoVenda() {
		return condicaoVenda;
	}

	public Date getDataBloqueio() {
		return dataBloqueio;
	}

	public Date getDataConversao() {
		return dataConversao;
	}

	public Date getDatadesbloqueio() {
		return datadesbloqueio;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public String getDescricaoComplementar() {
		return descricaoComplementar;
	}

	public String getDescricaoMercadoria() {
		return descricaoMercadoria;
	}

	public String getDeWebExpTemp() {
		return deWebExpTemp;
	}

	public String getDiEletronicaReexp() {
		return diEletronicaReexp;
	}

	public String getDsiEletronicaReexp() {
		return dsiEletronicaReexp;
	}

	public String getDsiFormularioReexp() {
		return dsiFormularioReexp;
	}

	public String getDueExpTemp() {
		return dueExpTemp;
	}

	public String getEdbvReexp() {
		return edbvReexp;
	}

	public String getEnderecoImportador() {
		return enderecoImportador;
	}

	public List<ExportacaoItemPaisDestino> getExportacaoPaisDestino() {
		return exportacaoPaisDestino;
	}

	public Long getId() {
		return id;
	}

	public Integer getIdentificadorItem() {
		return identificadorItem;
	}

	public String getLicencaExportacao() {
		return licencaExportacao;
	}


	public int getMotivoPrioridade() {
		return motivoPrioridade;
	}

	public String getNacionalidade() {
		return nacionalidade;
	}

	public String getNcm() {
		return ncm;
	}

	public String getNiExportador() {
		return niExportador;
	}

	public String getNomeExportador() {
		return nomeExportador;
	}

	public String getNomeImportador() {
		return nomeImportador;
	}

	public String getNumeroDocumentoIdentificacao() {
		return numeroDocumentoIdentificacao;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public BigDecimal getPercentualDeComissaoDoAgente() {
		return percentualDeComissaoDoAgente;
	}

	public BigDecimal getPesoLiquidoTotal() {
		return pesoLiquidoTotal;
	}

	public Integer getPrazoExportacaoTemporaria() {
		return prazoExportacaoTemporaria;
	}

	public BigDecimal getQtdUnidadeComercializada() {
		return qtdUnidadeComercializada;
	}

	public BigDecimal getQtdUnidadeEstatistica() {
		return qtdUnidadeEstatistica;
	}

	public int getTipoDeclaracaoImportacao() {
		return tipoDeclaracaoImportacao;
	}

	public String getUnidadeComercializada() {
		return unidadeComercializada;
	}

	public BigDecimal getValorCondicaoVenda() {
		return valorCondicaoVenda;
	}

	public BigDecimal getValorLocalEmbarque() {
		return valorLocalEmbarque;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setBemOuAdicao(String bemOuAdicao) {
		this.bemOuAdicao = bemOuAdicao;
	}

	public void setCondicaoVenda(String condicaoVenda) {
		this.condicaoVenda = condicaoVenda;
	}

	public void setDataBloqueio(Date dataBloqueio) {
		this.dataBloqueio = dataBloqueio;
	}

	public void setDataConversao(Date dataConversao) {
		this.dataConversao = dataConversao;
	}

	public void setDatadesbloqueio(Date datadesbloqueio) {
		this.datadesbloqueio = datadesbloqueio;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public void setDescricaoComplementar(String descricaoComplementar) {
		this.descricaoComplementar = descricaoComplementar;
	}

	public void setDescricaoMercadoria(String descricaoMercadoria) {
		this.descricaoMercadoria = descricaoMercadoria;
	}

	public void setDeWebExpTemp(String deWebExpTemp) {
		this.deWebExpTemp = deWebExpTemp;
	}

	public void setDiEletronicaReexp(String diEletronicaReexp) {
		this.diEletronicaReexp = diEletronicaReexp;
	}

	public void setDsiEletronicaReexp(String dsiEletronicaReexp) {
		this.dsiEletronicaReexp = dsiEletronicaReexp;
	}

	public void setDsiFormularioReexp(String dsiFormularioReexp) {
		this.dsiFormularioReexp = dsiFormularioReexp;
	}

	public void setDueExpTemp(String dueExpTemp) {
		this.dueExpTemp = dueExpTemp;
	}

	public void setEdbvReexp(String edbvReexp) {
		this.edbvReexp = edbvReexp;
	}

	public void setEnderecoImportador(String enderecoImportador) {
		this.enderecoImportador = enderecoImportador;
	}


	public void setExportacaoPaisDestino(List<ExportacaoItemPaisDestino> exportacaoPaisDestino) {
		this.exportacaoPaisDestino = exportacaoPaisDestino;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIdentificadorItem(Integer identificadorItem) {
		this.identificadorItem = identificadorItem;
	}

	public void setLicencaExportacao(String licencaExportacao) {
		this.licencaExportacao = licencaExportacao;
	}


	public void setMotivoPrioridade(int motivoPrioridade) {
		this.motivoPrioridade = motivoPrioridade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public void setNcm(String ncm) {
		this.ncm = ncm;
	}

	public void setNiExportador(String niExportador) {
		this.niExportador = niExportador;
	}

	public void setNomeExportador(String nomeExportador) {
		this.nomeExportador = nomeExportador;
	}

	public void setNomeImportador(String nomeImportador) {
		this.nomeImportador = nomeImportador;
	}

	public void setNumeroDocumentoIdentificacao(String numeroDocumentoIdentificacao) {
		this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public void setPercentualDeComissaoDoAgente(BigDecimal percentualDeComissaoDoAgente) {
		this.percentualDeComissaoDoAgente = percentualDeComissaoDoAgente;
	}

	public void setPesoLiquidoTotal(BigDecimal pesoLiquidoTotal) {
		this.pesoLiquidoTotal = pesoLiquidoTotal;
	}

	public void setPrazoExportacaoTemporaria(Integer prazoExportacaoTemporaria) {
		this.prazoExportacaoTemporaria = prazoExportacaoTemporaria;
	}

	public void setQtdUnidadeComercializada(BigDecimal qtdUnidadeComercializada) {
		this.qtdUnidadeComercializada = qtdUnidadeComercializada;
	}

	public void setQtdUnidadeEstatistica(BigDecimal qtdUnidadeEstatistica) {
		this.qtdUnidadeEstatistica = qtdUnidadeEstatistica;
	}

	public void setTipoDeclaracaoImportacao(int tipoDeclaracaoImportacao) {
		this.tipoDeclaracaoImportacao = tipoDeclaracaoImportacao;
	}

	public void setUnidadeComercializada(String unidadeComercializada) {
		this.unidadeComercializada = unidadeComercializada;
	}

	public void setValorCondicaoVenda(BigDecimal valorCondicaoVenda) {
		this.valorCondicaoVenda = valorCondicaoVenda;
	}

	public void setValorLocalEmbarque(BigDecimal valorLocalEmbarque) {
		this.valorLocalEmbarque = valorLocalEmbarque;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

}
