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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Ambito dto.
 *
 * @author CSI PIEMONTE
 */

public class ClassificazioniDTO {

	@JsonProperty("id_classificazione")
    private String idClassificazione;
	
	@JsonProperty("db_key_classificazione")
    private String dbKeyClassificazione;
	
	@JsonProperty("entrata_uscita")
	private String entrataUscita;
	
	@JsonProperty("protocollo_regionale")
	private String protocolloRegionale;
	
	@JsonProperty("protocollo_mittente")
	private String protocolloMittente;
	
	@JsonProperty("descrizione")
	private String descrizione;

	@JsonProperty("visibilita")
	private String visibilita;
	

	@JsonProperty("data_inserimento")
	private String  dataInserimento;
	
	@JsonProperty("rappresentazione_digitale")
    private boolean rappresentazioneDigitale;
	
	@JsonProperty("doc_con_allegati")
    private boolean docConAllegati;
	
	@JsonProperty("valido")
    private boolean valido;
        
    
	public String getIdClassificazione() {
		return idClassificazione;
	}
	public void setIdClassificazione(String idClassificazione) {
		this.idClassificazione = idClassificazione;
	}
	public String getDbKeyClassificazione() {
		return dbKeyClassificazione;
	}
	public void setDbKeyClassificazione(String dbKeyClassificazione) {
		this.dbKeyClassificazione = dbKeyClassificazione;
	}
	public String getEntrataUscita() {
		return entrataUscita;
	}
	public void setEntrataUscita(String entrataUscita) {
		this.entrataUscita = entrataUscita;
	}
	public String getProtocolloRegionale() {
		return protocolloRegionale;
	}
	public void setProtocolloRegionale(String protocolloRegionale) {
		this.protocolloRegionale = protocolloRegionale;
	}
	public String getProtocolloMittente() {
		return protocolloMittente;
	}
	public void setProtocolloMittente(String protocolloMittente) {
		this.protocolloMittente = protocolloMittente;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getVisibilita() {
		return visibilita;
	}
	public void setVisibilita(String visibilita) {
		this.visibilita = visibilita;
	}
	public String getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}
	public boolean isValido() {
		return valido;
	}
	public void setValido(boolean valido) {
		this.valido = valido;
	}
	public boolean isDocConAllegati() {
		return docConAllegati;
	}
	public void setDocConAllegati(boolean docConAllegati) {
		this.docConAllegati = docConAllegati;
	}
	public boolean isRappresentazioneDigitale() {
		return rappresentazioneDigitale;
	}
	public void setRappresentazioneDigitale(boolean rappresentazioneDigitale) {
		this.rappresentazioneDigitale = rappresentazioneDigitale;
	}
      

}
