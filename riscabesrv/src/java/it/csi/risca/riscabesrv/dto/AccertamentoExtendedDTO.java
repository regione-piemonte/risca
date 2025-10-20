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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AccertamentoExtendedDTO
 *
 * @author CSI PIEMONTE
 */

public class AccertamentoExtendedDTO extends AccertamentoDTO {
	
	@JsonProperty("importo_dovuto")
    private BigDecimal importoDovuto;
    
	@JsonProperty("data_scadenza_pag")
    private String dataScadenzaPag;

    @JsonProperty("interessi_dovuti")
    private BigDecimal interessiDovuti;
    
    @JsonProperty("importo_versato")
    private BigDecimal importoVersato;
    
	@JsonProperty("importo_di_canone_mancante")
    private BigDecimal importoDiCanoneMancante;
	
    @JsonProperty("interessi_mancanti")
    private BigDecimal interessiMancanti;

	@JsonProperty("interessi_versati")
	private BigDecimal interessiVersati;

	@JsonProperty("importo_rimborsato")
	private BigDecimal importoRimborsato;
	
	@JsonProperty("file_450")
    private File450DTO file450;

	public BigDecimal getImportoDovuto() {
		return importoDovuto;
	}

	public void setImportoDovuto(BigDecimal importoDovuto) {
		this.importoDovuto = importoDovuto;
	}

	public String getDataScadenzaPag() {
		return dataScadenzaPag;
	}

	public void setDataScadenzaPag(String dataScadenzaPag) {
		this.dataScadenzaPag = dataScadenzaPag;
	}

	public BigDecimal getInteressiDovuti() {
		return interessiDovuti;
	}

	public void setInteressiDovuti(BigDecimal interessiDovuti) {
		this.interessiDovuti = interessiDovuti;
	}

	public BigDecimal getImportoVersato() {
		return importoVersato;
	}

	public void setImportoVersato(BigDecimal importoVersato) {
		this.importoVersato = importoVersato;
	}

	public BigDecimal getImportoDiCanoneMancante() {
		return importoDiCanoneMancante;
	}

	public void setImportoDiCanoneMancante(BigDecimal importoDiCanoneMancante) {
		this.importoDiCanoneMancante = importoDiCanoneMancante;
	}

	public BigDecimal getInteressiMancanti() {
		return interessiMancanti;
	}

	public void setInteressiMancanti(BigDecimal interessiMancanti) {
		this.interessiMancanti = interessiMancanti;
	}

	public BigDecimal getInteressiVersati() {
		return interessiVersati;
	}

	public void setInteressiVersati(BigDecimal interessiVersati) {
		this.interessiVersati = interessiVersati;
	}

	public BigDecimal getImportoRimborsato() {
		return importoRimborsato;
	}

	public void setImportoRimborsato(BigDecimal importoRimborsato) {
		this.importoRimborsato = importoRimborsato;
	}
	
	public File450DTO getFile450() {
		return file450;
	}

	public void setFile450(File450DTO file450) {
		this.file450 = file450;
	}
	
}
