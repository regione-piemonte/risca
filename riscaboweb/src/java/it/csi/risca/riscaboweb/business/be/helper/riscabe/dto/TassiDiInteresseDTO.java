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
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TassiDiInteresseDTO {
	@JsonProperty("id_ambito_interesse")
	private Long idAmbitoInteresse;
	
	@JsonProperty("id_ambito")
	private Long idAmbito;
	
	@JsonProperty("tipo_interesse")
	private String flgTipoInteresse;
	
	@JsonProperty("data_inizio")
	private Date DataInizio;
	
	@JsonProperty("data_fine")
	private Date DataFine;
	
	@JsonProperty("percentuale")
	private BigDecimal Percentuale;
	
	@JsonProperty("giorni_legali")
	private Integer GiorniLegali;
	
	@JsonProperty("flg_cancellazione")
	private Boolean flgCancellazione;
	
	
	public Boolean getFlgCancellazione() {
		return flgCancellazione;
	}

	public void setFlgCancellazione(Boolean flgCancellazione) {
		this.flgCancellazione = flgCancellazione;
	}
	
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

	public BigDecimal Percentuale() {
		return Percentuale;
	}

	public void setPercentuale(BigDecimal Percentuale) {
		this.Percentuale = Percentuale;
	}
	
	public Integer GiorniLegali() {
		return GiorniLegali;
	}

	public void setGiorniLegali(Integer GiorniLegali) {
		this.GiorniLegali = GiorniLegali;
	}
}