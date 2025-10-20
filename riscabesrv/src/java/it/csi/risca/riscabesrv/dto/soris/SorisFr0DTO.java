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

import java.util.Date;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type SorisFr0 dto.
 *
 * @author CSI PIEMONTE
 */
public class SorisFr0DTO {



    @Size(max = 3, min = 0, message = "tipo_record deve essere compreso tra 0 e 3 caratteri. ")
	@JsonProperty("tipo_record")
	private String tipoRecord;
    
	@JsonProperty("progr_record")
    private Integer progrRecord;
    
    @Size(max = 3, min = 0, message = "cod_ambito deve essere compreso tra 0 e 3 caratteri. ")
	@JsonProperty("cod_ambito")
    private String codAmbito;
    
    @Size(max = 5, min = 0, message = "mittente deve essere compreso tra 0 e 5 caratteri. ")
   	@JsonProperty("mittente")
    private String mittente;
    
    @JsonProperty("data_riferimento")
    private Date dataRiferimento;
    
    @Size(max = 20, min = 0, message = "identif_file deve essere compreso tra 0 e 20 caratteri. ")
   	@JsonProperty("identif_File")
    private String identifFile;

    @Size(max = 1, min = 0, message = "tipo_file deve essere compreso tra 0 e 1 caratteri. ")
   	@JsonProperty("tipo_file")
	private String tipoFile;
	
    
    @Size(max = 20, min = 0, message = "identif_file_orig deve essere compreso tra 0 e 20 caratteri. ")
   	@JsonProperty("identif_file_orig")
	private String identifFileOrig;
    
    @JsonProperty("data_creazione_file")
    private Date dataCreazioneFile;
    
    @Size(max = 3, min = 0, message = "release deve essere compreso tra 0 e 3 caratteri. ")
   	@JsonProperty("release")
	private String release;
    
    @Size(max = 322, min = 0, message = "filler3 deve essere compreso tra 0 e 322 caratteri. ")
   	@JsonProperty("filler3")
	private String filler3;

	public String getTipoRecord() {
		return tipoRecord;
	}





	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}






	public int getProgrRecord() {
		return progrRecord;
	}





	public void setProgrRecord(int progrRecord) {
		this.progrRecord = progrRecord;
	}





	public String getCodAmbito() {
		return codAmbito;
	}





	public void setCodAmbito(String codAmbito) {
		this.codAmbito = codAmbito;
	}





	public String getMittente() {
		return mittente;
	}





	public void setMittente(String mittente) {
		this.mittente = mittente;
	}





	public Date getDataRiferimento() {
		return dataRiferimento;
	}





	public void setDataRiferimento(Date dataRiferimento) {
		this.dataRiferimento = dataRiferimento;
	}





	public String getIdentifFile() {
		return identifFile;
	}





	public void setIdentifFile(String identifFile) {
		this.identifFile = identifFile;
	}





	public String getTipoFile() {
		return tipoFile;
	}





	public void setTipoFile(String tipoFile) {
		this.tipoFile = tipoFile;
	}





	public String getIdentifFileOrig() {
		return identifFileOrig;
	}





	public void setIdentifFileOrig(String identifFileOrig) {
		this.identifFileOrig = identifFileOrig;
	}





	public Date getDataCreazioneFile() {
		return dataCreazioneFile;
	}





	public void setDataCreazioneFile(Date dataCreazioneFile) {
		this.dataCreazioneFile = dataCreazioneFile;
	}





	public String getRelease() {
		return release;
	}





	public void setRelease(String release) {
		this.release = release;
	}





	public String getFiller3() {
		return filler3;
	}





	public void setFiller3(String filler3) {
		this.filler3 = filler3;
	}





	@Override
	public String toString() {
		return "SorisFr0DTO [tipoRecord=" + tipoRecord + ", progrRecord=" + progrRecord + ", codAmbito="
				+ codAmbito + ", mittente=" + mittente + ", dataRiferimento=" + dataRiferimento
				+  ", tipoFile=" + tipoFile + ", identifFileOrig=" + identifFileOrig
				+  ", dataCreazioneFile=" + dataCreazioneFile + ", release=" + release
				+ ", identifFile=" + identifFile + ", filler3=" + filler3  + "]";

	}

}