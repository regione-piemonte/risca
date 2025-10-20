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
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TassiDiInteresseDTO extends GestAttoreDTO {
	
    @Min(value = 1, message = "L' id_ambito_interesse deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_ambito_interesse supera il limite massimo consentito per Integer")
	@JsonProperty("id_ambito_interesse")
	private Long idAmbitoInteresse;
	
    @Min(value = 1, message = "L' id_ambito deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_ambito supera il limite massimo consentito per Integer")
	@JsonProperty("id_ambito")
	private Long idAmbito;
	
    @Pattern(regexp = "L|M", message = "Il tipo_interesse deve avere valori 'L' o 'M'")
	@JsonProperty("tipo_interesse")
	private String flgTipoInteresse;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_inizio")
	private Date DataInizio;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_fine")
	private Date DataFine;
	
	@Digits(integer = 3, fraction = 4, message = "La percentuale deve avere al massimo 3 cifre intere e 4 decimali")
	@Min(value = 0, message = "La percentuale deve essere maggiore o uguale a 0")
	@JsonProperty("percentuale")
	private BigDecimal Percentuale;
	
	
	@Digits(integer = 3, fraction = 0, message = "I giorni_legali devono avere al massimo 3 cifre intere")
	@Min(value = 0, message = "Il campo giorni_legali deve essere maggiore o uguale a 0")
	@JsonProperty("giorni_legali")
	private Integer GiorniLegali;
	
	@JsonProperty("flg_cancellazione")
	private Boolean flgCancellazione;
	
	public Long getIdAmbitoInteresse() {
		return idAmbitoInteresse;
	}

	public void setIdAmbitoInteresse(Long idAmbitoInteresse) {
		this.idAmbitoInteresse = idAmbitoInteresse;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getFlgTipoInteresse() {
		return flgTipoInteresse;
	}

	public Boolean isFlgCancellazione() {
		return flgCancellazione;
	}

	public void setFlgCancellazione(Boolean flgCancellazione) {
		this.flgCancellazione = flgCancellazione;
	}

	public void setFlgTipoInteresse(String flgTipoInteresse) {
		this.flgTipoInteresse = flgTipoInteresse;
	}

	public Date getDataInizio() {
		return DataInizio;
	}
	
	public void setDataInizio(Date DataInizio) {
		this.DataInizio = DataInizio;
	}

	public Date getDataFine() {
		return DataFine;
	}

	public void setDataFine(Date DataFine) {
		this.DataFine = DataFine;
	}

	public BigDecimal getPercentuale() {
		return Percentuale;
	}

	public void setPercentuale(BigDecimal Percentuale) {
		this.Percentuale = Percentuale;
	}
	
	public Integer getGiorniLegali() {
		return GiorniLegali;
	}

	public void setGiorniLegali(Integer GiorniLegali) {
		this.GiorniLegali = GiorniLegali;
	}
	
}
