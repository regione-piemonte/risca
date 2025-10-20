package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DettaglioPagSearchResultDTO
 *
 * @author CSI PIEMONTE
 */

public class DettaglioPagSearchResultDTO {
	
	@JsonProperty("dettaglio_pag")
    private DettaglioPagExtendedDTO dettaglioPag;	
	
	@JsonIgnore
	private Long idDettaglioPag;
	 
    @JsonProperty("cod_riscossione")
    private String codRiscossione;   
    
    @JsonProperty("anno")
    private int anno;	
    
    @JsonProperty("canone_dovuto")
    private BigDecimal canonDovuto;	
	
    @JsonProperty("importo_versato")
    private BigDecimal importoVersato;	
	
    @JsonProperty("data_scadenza_pagamento")
    private String dataScadenzaPagamento;
    
	public Long getIdDettaglioPag() {
		return idDettaglioPag;
	}

	public void setIdDettaglioPag(Long idDettaglioPag) {
		this.idDettaglioPag = idDettaglioPag;
	}

	public DettaglioPagExtendedDTO getDettaglioPag() {
		return dettaglioPag;
	}

	public void setDettaglioPag(DettaglioPagExtendedDTO dettaglioPag) {
		this.dettaglioPag = dettaglioPag;
	}

	public String getCodRiscossione() {
		return codRiscossione;
	}

	public void setCodRiscossione(String codRiscossione) {
		this.codRiscossione = codRiscossione;
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public BigDecimal getCanonDovuto() {
		return canonDovuto;
	}

	public void setCanonDovuto(BigDecimal canonDovuto) {
		this.canonDovuto = canonDovuto;
	}

	public BigDecimal getImportoVersato() {
		return importoVersato;
	}

	public void setImportoVersato(BigDecimal importoVersato) {
		this.importoVersato = importoVersato;
	}

	public String getDataScadenzaPagamento() {
		return dataScadenzaPagamento;
	}

	public void setDataScadenzaPagamento(String dataScadenzaPagamento) {
		this.dataScadenzaPagamento = dataScadenzaPagamento;
	}

}
