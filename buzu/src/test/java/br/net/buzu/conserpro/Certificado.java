package br.net.buzu.conserpro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe responsável por armazenar informações dos certificados que devem ser
 * associados quando o pais for Mercosul.
 *
 * @author Rodrigo Adur (099.163.947-25) #21 3509-7369
 * @since 26 de jun de 2016 Construção DUE.
 *
 */
public class Certificado implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Long id;


	private String codigoCertificado;

	private BigDecimal qtdUnidadeEstatistica;

	private Date dataRegistro;
	
	public Certificado(Long id, String codigoCertificado) {
		super();
		this.id = id;
		this.codigoCertificado = codigoCertificado;
		qtdUnidadeEstatistica = new BigDecimal("56353");
		dataRegistro = new Date();
	}

	public Certificado() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getQtdUnidadeEstatistica() {
		return qtdUnidadeEstatistica;
	}

	public void setQtdUnidadeEstatistica(BigDecimal qtdUnidadeEstatistica) {
		this.qtdUnidadeEstatistica = qtdUnidadeEstatistica;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public String getCodigoCertificado() {
		return codigoCertificado;
	}

	public void setCodigoCertificado(String codigoCertificado) {
		this.codigoCertificado = codigoCertificado;
	}

}
