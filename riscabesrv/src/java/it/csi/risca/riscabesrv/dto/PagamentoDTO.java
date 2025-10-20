/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PagamentoDTO
 *
 * @author CSI PIEMONTE
 */
public class PagamentoDTO extends GestAttoreDTO {
	
    @Min(value = 1, message = "L' id_pagamento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_pagamento supera il limite massimo consentito per Integer")
	@JsonProperty("id_pagamento")
	private Long idPagamento;
	
    @Min(value = 1, message = "L' id_ambito deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_ambito supera il limite massimo consentito per Integer")
	@JsonProperty("id_ambito")
	private Long idAmbito;
	
    @Min(value = 1, message = "L' id_tipologia_pag deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipologia_pag supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipologia_pag")
	private Long idTipologiaPag;
	
    @Min(value = 1, message = "L' id_tipo_modalita_pag deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_modalita_pag supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_modalita_pag")
	private Long idTipoModalitaPag;
	
    @Min(value = 1, message = "L' id_file_poste deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_file_poste supera il limite massimo consentito per Integer")
	@JsonProperty("id_file_poste")
	private Long idFilePoste;

    @Min(value = 1, message = "L' id_immagine deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_immagine supera il limite massimo consentito per Integer")
	@JsonProperty("id_immagine")
	private Long idImmagine;
	
    @Size(max = 1000, min = 0, message = "causale deve essere compreso tra 0 e 1000 caratteri.")
	@JsonProperty("causale")
	private String causale;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_op_val")
	private Date dataOpVal;
	
    @Digits(integer = 13, fraction = 2, message = "importo_versato deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("importo_versato")
	private BigDecimal importoVersato;
	
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_download")
	private Date dataDownload;
	
    @Size(max = 16, min = 0, message = " quinto campo deve essere compreso tra 0 e 16 caratteri.")
	@JsonProperty("quinto_campo")
	private String quintoCampo;

    @Size(max = 16, min = 0, message = "cro deve essere compreso tra 0 e 16 caratteri. ")
	@JsonProperty("cro")
	private String cro;
	
    @Size(max = 500, min = 0, message = "note deve essere compreso tra 0 e 500 caratteri. ")
	@JsonProperty("note")
	private String note;

    @Digits(integer = 6, fraction = 0, message = "numero_pagamento deve avere al massimo 6 cifre intere ")
	@JsonProperty("numero_pagamento")
	private Long numeroPagamento;

    @Size(max = 200, min = 0, message = " soggetto versamento deve essere compreso tra 0 e 200 caratteri.")
	@JsonProperty("soggetto_versamento")
	private String soggettoVersamento;

    @Size(max = 80, min = 0, message = " indirizzo versamento deve essere compreso tra 0 e 80 caratteri.")
	@JsonProperty("indirizzo_versamento")
	private String indirizzoVersamento;

    @Size(max = 10, min = 0, message = " civico versamento deve essere compreso tra 0 e 10 caratteri.")
	@JsonProperty("civico_versamento")
	private String civicoVersamento;
	
    @Size(max = 50, min = 0, message = "frazione versamento deve essere compreso tra 0 e 50 caratteri. ")
	@JsonProperty("frazione_versamento")
	private String frazioneVersamento;
	
    @Size(max = 40, min = 0, message = "comune versamento deve essere compreso tra 0 e 40 caratteri. ")
	@JsonProperty("comune_versamento")
	private String comuneVersamento;
	
    @Size(max = 5, min = 0, message = "cap versamento deve essere compreso tra 0 e 5 caratteri. ")
	@JsonProperty("cap_versamento")
	private String capVersamento;
	
    @Size(max = 20, min = 0, message = "prov versamento deve essere compreso tra 0 e 20 caratteri. ")
	@JsonProperty("prov_versamento")
	private String provVersamento;
	
    @Min(value = 0, message = " flg_rimborsato deve essere 0 o 1 ")
    @Max(value = 1, message = " flg_rimborsato deve essere 0 o 1 ")
	@JsonProperty("flg_rimborsato")
	private Integer flgRimborsato;
	
    @Digits(integer = 13, fraction = 2, message = "imp_da_assegnare deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("imp_da_assegnare")
	private BigDecimal impDaAssegnare;

    @Size(max = 35, min = 0, message = "codice avviso deve essere compreso tra 0 e 35 caratteri. ")
	@JsonProperty("codice_avviso")
	private String codiceAvviso;
	
    @Min(value = 1, message = "L' id_file_soris deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_file_soris supera il limite massimo consentito per Integer")
	@JsonProperty("id_file_soris")
	private Long idFileSoris;

    
	public Long getIdFileSoris() {
		return idFileSoris;
	}

	public void setIdFileSoris(Long idFileSoris) {
		this.idFileSoris = idFileSoris;
	}

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public Long getIdTipologiaPag() {
		return idTipologiaPag;
	}

	public void setIdTipologiaPag(Long idTipologiaPag) {
		this.idTipologiaPag = idTipologiaPag;
	}

	public Long getIdTipoModalitaPag() {
		return idTipoModalitaPag;
	}

	public void setIdTipoModalitaPag(Long idTipoModalitaPag) {
		this.idTipoModalitaPag = idTipoModalitaPag;
	}

	public Long getIdFilePoste() {
		return idFilePoste;
	}

	public void setIdFilePoste(Long idFilePoste) {
		this.idFilePoste = idFilePoste;
	}

	public Long getIdImmagine() {
		return idImmagine;
	}

	public void setIdImmagine(Long idImmagine) {
		this.idImmagine = idImmagine;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public Date getDataOpVal() {
		return dataOpVal;
	}

	public void setDataOpVal(Date dataOpVal) {
		this.dataOpVal = dataOpVal;
	}



	public BigDecimal getImportoVersato() {
		return importoVersato;
	}

	public void setImportoVersato(BigDecimal importoVersato) {
		this.importoVersato = importoVersato;
	}

	public Date getDataDownload() {
		return dataDownload;
	}

	public void setDataDownload(Date dataDownload) {
		this.dataDownload = dataDownload;
	}

	public String getQuintoCampo() {
		return quintoCampo;
	}

	public void setQuintoCampo(String quintoCampo) {
		this.quintoCampo = quintoCampo;
	}

	public String getCro() {
		return cro;
	}

	public void setCro(String cro) {
		this.cro = cro;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getNumeroPagamento() {
		return numeroPagamento;
	}

	public void setNumeroPagamento(Long numeroPagamento) {
		this.numeroPagamento = numeroPagamento;
	}

	public String getSoggettoVersamento() {
		return soggettoVersamento;
	}

	public void setSoggettoVersamento(String soggettoVersamento) {
		this.soggettoVersamento = soggettoVersamento;
	}

	public String getIndirizzoVersamento() {
		return indirizzoVersamento;
	}

	public void setIndirizzoVersamento(String indirizzoVersamento) {
		this.indirizzoVersamento = indirizzoVersamento;
	}

	public String getCivicoVersamento() {
		return civicoVersamento;
	}

	public void setCivicoVersamento(String civicoVersamento) {
		this.civicoVersamento = civicoVersamento;
	}

	public String getFrazioneVersamento() {
		return frazioneVersamento;
	}

	public void setFrazioneVersamento(String frazioneVersamento) {
		this.frazioneVersamento = frazioneVersamento;
	}

	public String getComuneVersamento() {
		return comuneVersamento;
	}

	public void setComuneVersamento(String comuneVersamento) {
		this.comuneVersamento = comuneVersamento;
	}

	public String getCapVersamento() {
		return capVersamento;
	}

	public void setCapVersamento(String capVersamento) {
		this.capVersamento = capVersamento;
	}

	public String getProvVersamento() {
		return provVersamento;
	}

	public void setProvVersamento(String provVersamento) {
		this.provVersamento = provVersamento;
	}

	public Integer getFlgRimborsato() {
		return flgRimborsato;
	}

	public void setFlgRimborsato(Integer flgRimborsato) {
		this.flgRimborsato = flgRimborsato;
	}

	public BigDecimal getImpDaAssegnare() {
		return impDaAssegnare;
	}

	public void setImpDaAssegnare(BigDecimal impDaAssegnare) {
		this.impDaAssegnare = impDaAssegnare;
	}

	public String getCodiceAvviso() {
		return codiceAvviso;
	}

	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}
	
	

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

}
