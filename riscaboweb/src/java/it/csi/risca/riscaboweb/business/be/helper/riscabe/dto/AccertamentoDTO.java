/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AccertamentoDTO
 *
 * @author CSI PIEMONTE
 */

public class AccertamentoDTO {
	
	@JsonProperty("id_accertamento")
    private Long idAccertamento;

    @JsonProperty("id_stato_debitorio")
    private Long idStatoDebitorio;
    
    @JsonProperty("tipo_accertamento")
    private TipiAccertamentoDTO tipoAccertamento;
    
	@JsonProperty("id_file_450")
    private Long idFile450;

    @JsonProperty("num_protocollo")
    private String numProtocollo;
    
    @JsonProperty("data_protocollo")
    private Date dataProtocollo;
    
	@JsonProperty("data_scadenza")
    private Date dataScadenza;

    @JsonProperty("flg_restituito")
    private int flgRestituito;
    
    @JsonProperty("flg_annullato")
    private int flgAnnullato;
    
	@JsonProperty("data_notifica")
    private Date dataNotifica;
	
    @JsonProperty("nota")
    private String nota;


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

}
