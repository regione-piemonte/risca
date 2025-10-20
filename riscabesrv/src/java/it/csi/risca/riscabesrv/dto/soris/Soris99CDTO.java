/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto.soris;


import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Soris99C dto.
 *
 * @author CSI PIEMONTE
 */
public class Soris99CDTO {
	
	
	
    @Size(max = 3, min = 0, message = "tipo_record deve essere compreso tra 0 e 3 caratteri. ")
	@JsonProperty("tipo_record")
	private String tipoRecord;
    
    
    @Size(max = 2, min = 0, message = "filler1 deve essere compreso tra 0 e 2 caratteri. ")
	@JsonProperty("filler1")
    private String filler1;
    
    @Size(max = 3, min = 0, message = "cod_dest_mitt deve essere compreso tra 0 e 3 caratteri. ")
   	@JsonProperty("cod_dest_mitt")
    private String codDestMitt;
    

    @Size(max = 4, min = 0, message = "filler2 deve essere compreso tra 0 e 4 caratteri. ")
   	@JsonProperty("filler2")
    private String filler2;
	   
    @Size(max = 8, min = 0, message = "totale_record_invio deve essere compreso tra 0 e 8 caratteri. ")
   	@JsonProperty("totale_record_invio")
    private String totaleRecordInvio;
	
    
    @Size(max = 8, min = 0, message = "cod_tracciato deve essere compreso tra 0 e 8 caratteri. ")
   	@JsonProperty("cod_tracciato")
    private String codTracciato;
	
    @Size(max = 379, min = 0, message = "filler3 deve essere compreso tra 0 e 379 caratteri. ")
   	@JsonProperty("filler3")
    private String filler3;
    
    
    
	public String getTipoRecord() {
		return tipoRecord;
	}




	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}



	public String getFiller1() {
		return filler1;
	}




	public void setFiller1(String filler1) {
		this.filler1 = filler1;
	}




	public String getCodDestMitt() {
		return codDestMitt;
	}




	public void setCodDestMitt(String codDestMitt) {
		this.codDestMitt = codDestMitt;
	}




	public String getFiller2() {
		return filler2;
	}




	public void setFiller2(String filler2) {
		this.filler2 = filler2;
	}




	public String getTotaleRecordInvio() {
		return totaleRecordInvio;
	}




	public void setTotaleRecordInvio(String totaleRecordInvio) {
		this.totaleRecordInvio = totaleRecordInvio;
	}




	public String getCodTracciato() {
		return codTracciato;
	}




	public void setCodTracciato(String codTracciato) {
		this.codTracciato = codTracciato;
	}




	public String getFiller3() {
		return filler3;
	}




	public void setFiller3(String filler3) {
		this.filler3 = filler3;
	}



	@Override
	public String toString() {
		return "SorisFr1DTO [tipoRecord=" + tipoRecord + ", filler1=" + filler1 + ", cod_dest_mitt="
				+ codDestMitt + ", filler2=" + filler2 + ", totale_record_invio=" + totaleRecordInvio
				+  ", cod_tracciato=" + codTracciato + ", filler3=" + filler3+ "]";
		
			
	}

}