package br.net.buzu.conserpro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO que representa a Declaração Única do Comércio Exterior.
 *
 * @author Tiago Tosta Peres (094.455.377-00) #21 3509-7355
 * @author Douglas Siviotti (073.116.317-69) #21 3509-7585 (refactoring)
 * @since 29 de jun de 2016 Construção DUE.
 *
 */
public class Due implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Integer versaoDue;

	private String numero;

	private String ruc;

	private int situacaoDue;

	private int paisImportador;

	private Date dataRegistro;

	private Date dataCriacao;

	private String niDeclarante;

	private int formaExportacao;

	private int situacoesEspeciais;

	private int moeda;

	private String unidadeLocalDespacho;

	private String recintoAduaneiroDespacho;

	private String estabelecimentoLocalDespacho;

	private String enderecoEstabelecimentoLocalDespacho;

	private String localizacaoGeograficaLocalDespacho;

	private String referenciaEnderecoLocalDespacho;

	private String unidadeLocalEmbarque;

	private String recintoAduaneiroEmbarque;

	private String enderecoEstabelecimentoLocalEmbarque;

	private String latitudeDoLocalDeEmbarque;

	private String longitudeDoLocalDeEmbarque;

	private String referenciaEnderecoLocalEmbarque;

	private int viaTransporteEspecial;

	private String informacoesComplementares;

	private int tipoItemDue;

	private int motivoDispensaNotaFiscal;

	private boolean indicadorBloqueioEmbarqueExportador;

	private String numeroComprotCancelamentoAduana;

	private String ipUsuarioLogado;

	private String cpfUsuarioLogado;

	// Listas

	private List<ItemDue> listaItensDue = new ArrayList<ItemDue>();

	private List<NotaFiscal> notasFiscais = new ArrayList<NotaFiscal>();

	// ******************** GET/SET ********************

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public int getSituacaoDue() {
		return situacaoDue;
	}

	public void setSituacaoDue(int situacaoDue) {
		this.situacaoDue = situacaoDue;
	}

	public int getPaisImportador() {
		return paisImportador;
	}

	public void setPaisImportador(int paisImportador) {
		this.paisImportador = paisImportador;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getNiDeclarante() {
		return niDeclarante;
	}

	public void setNiDeclarante(String niDeclarante) {
		this.niDeclarante = niDeclarante;
	}

	public int getFormaExportacao() {
		return formaExportacao;
	}

	public void setFormaExportacao(int formaExportacao) {
		this.formaExportacao = formaExportacao;
	}

	public int getSituacoesEspeciais() {
		return situacoesEspeciais;
	}

	public void setSituacoesEspeciais(int situacoesEspeciais) {
		this.situacoesEspeciais = situacoesEspeciais;
	}

	public int getMoeda() {
		return moeda;
	}

	public void setMoeda(int moeda) {
		this.moeda = moeda;
	}

	public String getUnidadeLocalDespacho() {
		return unidadeLocalDespacho;
	}

	public void setUnidadeLocalDespacho(String unidadeLocalDespacho) {
		this.unidadeLocalDespacho = unidadeLocalDespacho;
	}

	public String getRecintoAduaneiroDespacho() {
		return recintoAduaneiroDespacho;
	}

	public void setRecintoAduaneiroDespacho(String recintoAduaneiroDespacho) {
		this.recintoAduaneiroDespacho = recintoAduaneiroDespacho;
	}

	public String getEstabelecimentoLocalDespacho() {
		return estabelecimentoLocalDespacho;
	}

	public void setEstabelecimentoLocalDespacho(String estabelecimentoLocalDespacho) {
		this.estabelecimentoLocalDespacho = estabelecimentoLocalDespacho;
	}

	public String getEnderecoEstabelecimentoLocalDespacho() {
		return enderecoEstabelecimentoLocalDespacho;
	}

	public void setEnderecoEstabelecimentoLocalDespacho(String enderecoEstabelecimentoLocalDespacho) {
		this.enderecoEstabelecimentoLocalDespacho = enderecoEstabelecimentoLocalDespacho;
	}

	public String getLocalizacaoGeograficaLocalDespacho() {
		return localizacaoGeograficaLocalDespacho;
	}

	public void setLocalizacaoGeograficaLocalDespacho(String localizacaoGeograficaLocalDespacho) {
		this.localizacaoGeograficaLocalDespacho = localizacaoGeograficaLocalDespacho;
	}

	public String getReferenciaEnderecoLocalDespacho() {
		return referenciaEnderecoLocalDespacho;
	}

	public void setReferenciaEnderecoLocalDespacho(String referenciaEnderecoLocalDespacho) {
		this.referenciaEnderecoLocalDespacho = referenciaEnderecoLocalDespacho;
	}

	public String getUnidadeLocalEmbarque() {
		return unidadeLocalEmbarque;
	}

	public void setUnidadeLocalEmbarque(String unidadeLocalEmbarque) {
		this.unidadeLocalEmbarque = unidadeLocalEmbarque;
	}

	public String getRecintoAduaneiroEmbarque() {
		return recintoAduaneiroEmbarque;
	}

	public void setRecintoAduaneiroEmbarque(String recintoAduaneiroEmbarque) {
		this.recintoAduaneiroEmbarque = recintoAduaneiroEmbarque;
	}

	public String getEnderecoEstabelecimentoLocalEmbarque() {
		return enderecoEstabelecimentoLocalEmbarque;
	}

	public void setEnderecoEstabelecimentoLocalEmbarque(String enderecoEstabelecimentoLocalEmbarque) {
		this.enderecoEstabelecimentoLocalEmbarque = enderecoEstabelecimentoLocalEmbarque;
	}

	public String getLatitudeDoLocalDeEmbarque() {
		return latitudeDoLocalDeEmbarque;
	}

	public void setLatitudeDoLocalDeEmbarque(String latitudeDoLocalDeEmbarque) {
		this.latitudeDoLocalDeEmbarque = latitudeDoLocalDeEmbarque;
	}

	public String getLongitudeDoLocalDeEmbarque() {
		return longitudeDoLocalDeEmbarque;
	}

	public void setLongitudeDoLocalDeEmbarque(String longitudeDoLocalDeEmbarque) {
		this.longitudeDoLocalDeEmbarque = longitudeDoLocalDeEmbarque;
	}

	public String getReferenciaEnderecoLocalEmbarque() {
		return referenciaEnderecoLocalEmbarque;
	}

	public void setReferenciaEnderecoLocalEmbarque(String referenciaEnderecoLocalEmbarque) {
		this.referenciaEnderecoLocalEmbarque = referenciaEnderecoLocalEmbarque;
	}

	public int getViaTransporteEspecial() {
		return viaTransporteEspecial;
	}

	public void setViaTransporteEspecial(int viaTransporteEspecial) {
		this.viaTransporteEspecial = viaTransporteEspecial;
	}

	public String getInformacoesComplementares() {
		return informacoesComplementares;
	}

	public void setInformacoesComplementares(String informacoesComplementares) {
		this.informacoesComplementares = informacoesComplementares;
	}

	public int getTipoItemDue() {
		return tipoItemDue;
	}

	public void setTipoItemDue(int tipoItemDue) {
		this.tipoItemDue = tipoItemDue;
	}

	public int getMotivoDispensaNotaFiscal() {
		return motivoDispensaNotaFiscal;
	}

	public void setMotivoDispensaNotaFiscal(int motivoDispensaNotaFiscal) {
		this.motivoDispensaNotaFiscal = motivoDispensaNotaFiscal;
	}

	public boolean isIndicadorBloqueioEmbarqueExportador() {
		return indicadorBloqueioEmbarqueExportador;
	}

	public void setIndicadorBloqueioEmbarqueExportador(boolean indicadorBloqueioEmbarqueExportador) {
		this.indicadorBloqueioEmbarqueExportador = indicadorBloqueioEmbarqueExportador;
	}

	public String getNumeroComprotCancelamentoAduana() {
		return numeroComprotCancelamentoAduana;
	}

	public void setNumeroComprotCancelamentoAduana(String numeroComprotCancelamentoAduana) {
		this.numeroComprotCancelamentoAduana = numeroComprotCancelamentoAduana;
	}

	public String getIpUsuarioLogado() {
		return ipUsuarioLogado;
	}

	public void setIpUsuarioLogado(String ipUsuarioLogado) {
		this.ipUsuarioLogado = ipUsuarioLogado;
	}

	public String getCpfUsuarioLogado() {
		return cpfUsuarioLogado;
	}

	public void setCpfUsuarioLogado(String cpfUsuarioLogado) {
		this.cpfUsuarioLogado = cpfUsuarioLogado;
	}

	public List<ItemDue> getListaItensDue() {
		return listaItensDue;
	}

	public void setListaItensDue(List<ItemDue> listaItensDue) {
		this.listaItensDue = listaItensDue;
	}

	public List<NotaFiscal> getNotasFiscais() {
		return notasFiscais;
	}

	public void setNotasFiscais(List<NotaFiscal> notasFiscais) {
		this.notasFiscais = notasFiscais;
	}


	public Integer getVersaoDue() {
		return versaoDue;
	}

	public void setVersaoDue(Integer versaoDue) {
		this.versaoDue = versaoDue;
	}

}
