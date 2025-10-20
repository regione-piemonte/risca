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

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AccertamentoDTO
 *
 * @author CSI PIEMONTE
 */

public class AccertamentoDTO extends GestAttoreDTO {
	
    @Min(value = 1, message = "L' id_accertamento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_accertamento supera il limite massimo consentito per Integer")
	@JsonProperty("id_accertamento")
    private Long idAccertamento;

    @Min(value = 1, message = "L' id_stato_debitorio deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_stato_debitorio supera il limite massimo consentito per Integer")
    @JsonProperty("id_stato_debitorio")
    private Long idStatoDebitorio;
    
    @JsonProperty("tipo_accertamento")
    private TipiAccertamentoDTO tipoAccertamento;
    
    @Min(value = 1, message = "L' id_file_450 deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_file_450 supera il limite massimo consentito per Integer")
	@JsonProperty("id_file_450")
    private Long idFile450;
	
    @Min(value = 1, message = "L' id_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
    private Long idElabora;

    @Size(max = 30, min = 0, message = "Il numero di protocollo deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("num_protocollo")
    private String numProtocollo;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("data_protocollo")
    private Date dataProtocollo;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_scadenza")
    private Date dataScadenza;
	
    @Min(value = 0, message = "Il flg_restituito deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_restituito deve essere al massimo 1.")
    @JsonProperty("flg_restituito")
    private int flgRestituito;
    
    @Min(value = 0, message = "Il flg_annullato deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_annullato deve essere al massimo 1.")
    @JsonProperty("flg_annullato")
    private int flgAnnullato;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_notifica")
    private Date dataNotifica;

    @Size(max = 250, min = 0, message = "La nota deve essere compreso tra 0 e 250 caratteri.")
    @JsonProperty("nota")
    private String nota;
    
	@JsonProperty("id_spedizione")
    private Long idSpedizione;



	public Long getIdAccertamento() {
		return idAccertamento;
	}

	public void setIdAccertamento(Long idAccertamento) {
		this.idAccertamento = idAccertamento;
	}

	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}

	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
	}

	public TipiAccertamentoDTO getTipoAccertamento() {
		return tipoAccertamento;
	}

	public void setTipoAccertamento(TipiAccertamentoDTO tipoAccertamento) {
		this.tipoAccertamento = tipoAccertamento;
	}

	public Long getIdFile450() {
		return idFile450;
	}

	public void setIdFile450(Long idFile450) {
		this.idFile450 = idFile450;
	}

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public String getNumProtocollo() {
		return numProtocollo;
	}

	public void setNumProtocollo(String numProtocollo) {
		this.numProtocollo = numProtocollo;
	}

	public Date getDataProtocollo() {
		return dataProtocollo;
	}

	public void setDataProtocollo(Date dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public int getFlgRestituito() {
		return flgRestituito;
	}

	public void setFlgRestituito(int flgRestituito) {
		this.flgRestituito = flgRestituito;
	}

	public int getFlgAnnullato() {
		return flgAnnullato;
	}

	public void setFlgAnnullato(int flgAnnullato) {
		this.flgAnnullato = flgAnnullato;
	}

	public Date getDataNotifica() {
		return dataNotifica;
	}

	public void setDataNotifica(Date dataNotifica) {
		this.dataNotifica = dataNotifica;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}
	
	public Long getIdSpedizione() {
		return idSpedizione;
	}

	public void setIdSpedizione(Long idSpedizione) {
		this.idSpedizione = idSpedizione;
	}
	
}
