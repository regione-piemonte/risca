/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto.ambiente;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UsoDatoTecnicoDTO
 *
 * @author CSI PIEMONTE
 */
@JsonInclude(Include.NON_NULL)
public class TipoUsoDatoTecnicoDTO {
	
	@JsonProperty("id_tipo_uso_legge")
    private Long idTipoUsoLegge;
	
	@JsonProperty("uso_di_legge")
    private String tipoUsoLegge;
	
	@JsonProperty("uso_specifico")
    private String[] tipoUsoSpecifico;
	
	@JsonProperty("unita_di_misura")
    private String unitaDiMisura;
	
	@JsonProperty("qta_acqua")
    private BigDecimal quantita;
	
	@JsonProperty("qta_acqua_da")
    private BigDecimal quantitaDa;
	
	@JsonProperty("qta_acqua_a")
    private BigDecimal quantitaA;

	@JsonProperty("qta_falda_profonda")
    private BigDecimal quantitaFaldaProfonda;
	
	@JsonProperty("perc_falda_profonda")
    private BigDecimal percFaldaProfonda;
	
	@JsonProperty("qta_falda_superficie")
    private BigDecimal quantitaFaldaSuperficiale;
	
	@JsonProperty("perc_falda_superficie")
    private BigDecimal percQuantitaFaldaSuperficiale;

	@JsonProperty("riduzione")
    private List<RiduzioneDTDTO> riduzione;
	
	@JsonProperty("aumento")
    private List<AumentoDTDTO> aumento;
	
	@JsonProperty("data_scadenza_emas_iso")
	private String dataScadenzaEmasIso;
	
	@JsonProperty("data_scadenza_emas_iso_da")
	private String dataScadenzaEmasIsoDa;
	
	@JsonProperty("data_scadenza_emas_iso_a")
	private String dataScadenzaEmasIsoA;
	
	@JsonProperty("perc_quota_var")
    private Integer percQuotaVar;
	
	@JsonProperty("coeff_energ_grat")
    private BigDecimal coeffEnergGrat;
	
	@JsonProperty("pnm_per_energ_grat")
    private BigDecimal pnmPerEnergGrat;
	
	@JsonProperty("extra_profitti")
    private BigDecimal extraProfitti;
	
	@JsonProperty("tot_energ_prod")
    private BigDecimal toTEnergProd;
	
	@JsonProperty("tot_ricavi_anno")
    private BigDecimal totRicaviAnno;
	
	@JsonProperty("prezzo_med_ora_pond")
    private BigDecimal prezzoMedOraPond;
	
	public Long getIdTipoUsoLegge() {
		return idTipoUsoLegge;
	}

	public void setIdTipoUsoLegge(Long idTipoUsoLegge) {
		this.idTipoUsoLegge = idTipoUsoLegge;
	}

	public String getTipoUsoLegge() {
		return tipoUsoLegge;
	}

	public void setTipoUsoLegge(String tipoUsoLegge) {
		this.tipoUsoLegge = tipoUsoLegge;
	}

	public String[] getTipoUsoSpecifico() {
		return tipoUsoSpecifico;
	}

	public void setTipoUsoSpecifico(String[] tipoUsoSpecifico) {
		this.tipoUsoSpecifico = tipoUsoSpecifico;
	}

	public String getUnitaDiMisura() {
		return unitaDiMisura;
	}

	public void setUnitaDiMisura(String unitaDiMisura) {
		this.unitaDiMisura = unitaDiMisura;
	}

	public BigDecimal getQuantita() {
		return quantita;
	}

	public void setQuantita(BigDecimal quantita) {
		this.quantita = quantita;
	}

	public BigDecimal getQuantitaFaldaProfonda() {
		return quantitaFaldaProfonda;
	}

	public void setQuantitaFaldaProfonda(BigDecimal quantitaFaldaProfonda) {
		this.quantitaFaldaProfonda = quantitaFaldaProfonda;
	}

	public BigDecimal getPercFaldaProfonda() {
		return percFaldaProfonda;
	}

	public void setPercFaldaProfonda(BigDecimal percFaldaProfonda) {
		this.percFaldaProfonda = percFaldaProfonda;
	}

	public BigDecimal getQuantitaFaldaSuperficiale() {
		return quantitaFaldaSuperficiale;
	}

	public void setQuantitaFaldaSuperficiale(BigDecimal quantitaFaldaSuperficiale) {
		this.quantitaFaldaSuperficiale = quantitaFaldaSuperficiale;
	}

	public BigDecimal getPercQuantitaFaldaSuperficiale() {
		return percQuantitaFaldaSuperficiale;
	}

	public void setPercQuantitaFaldaSuperficiale(BigDecimal percQuantitaFaldaSuperficiale) {
		this.percQuantitaFaldaSuperficiale = percQuantitaFaldaSuperficiale;
	}
	
	public List<RiduzioneDTDTO> getRiduzione() {
		return riduzione;
	}

	public void setRiduzione(List<RiduzioneDTDTO> riduzione) {
		this.riduzione = riduzione;
	}

	public List<AumentoDTDTO> getAumento() {
		return aumento;
	}

	public void setAumento(List<AumentoDTDTO> aumento) {
		this.aumento = aumento;
	}

	public String getDataScadenzaEmasIso() {
		return dataScadenzaEmasIso;
	}

	public void setDataScadenzaEmasIso(String dataScadenzaEmasIso) {
		this.dataScadenzaEmasIso = dataScadenzaEmasIso;
	}

	public BigDecimal getQuantitaDa() {
		return quantitaDa;
	}

	public void setQuantitaDa(BigDecimal quantitaDa) {
		this.quantitaDa = quantitaDa;
	}

	public BigDecimal getQuantitaA() {
		return quantitaA;
	}

	public void setQuantitaA(BigDecimal quantitaA) {
		this.quantitaA = quantitaA;
	}

	public String getDataScadenzaEmasIsoDa() {
		return dataScadenzaEmasIsoDa;
	}

	public void setDataScadenzaEmasIsoDa(String dataScadenzaEmasIsoDa) {
		this.dataScadenzaEmasIsoDa = dataScadenzaEmasIsoDa;
	}

	public String getDataScadenzaEmasIsoA() {
		return dataScadenzaEmasIsoA;
	}

	public void setDataScadenzaEmasIsoA(String dataScadenzaEmasIsoA) {
		this.dataScadenzaEmasIsoA = dataScadenzaEmasIsoA;
	}

	public Integer getPercQuotaVar() {
		return percQuotaVar;
	}

	public void setPercQuotaVar(Integer percQuotaVar) {
		this.percQuotaVar = percQuotaVar;
	}

	public BigDecimal getCoeffEnergGrat() {
		return coeffEnergGrat;
	}

	public void setCoeffEnergGrat(BigDecimal coeffEnergGrat) {
		this.coeffEnergGrat = coeffEnergGrat;
	}

	public BigDecimal getPnmPerEnergGrat() {
		return pnmPerEnergGrat;
	}

	public void setPnmPerEnergGrat(BigDecimal pnmPerEnergGrat) {
		this.pnmPerEnergGrat = pnmPerEnergGrat;
	}

	public BigDecimal getToTEnergProd() {
		return toTEnergProd;
	}

	public void setToTEnergProd(BigDecimal toTEnergProd) {
		this.toTEnergProd = toTEnergProd;
	}



	public BigDecimal getTotRicaviAnno() {
		return totRicaviAnno;
	}

	public void setTotRicaviAnno(BigDecimal totRicaviAnno) {
		this.totRicaviAnno = totRicaviAnno;
	}

	public BigDecimal getPrezzoMedOraPond() {
		return prezzoMedOraPond;
	}

	public void setPrezzoMedOraPond(BigDecimal prezzoMedOraPond) {
		this.prezzoMedOraPond = prezzoMedOraPond;
	}

	public BigDecimal getExtraProfitti() {
		return extraProfitti;
	}

	public void setExtraProfitti(BigDecimal extraProfitti) {
		this.extraProfitti = extraProfitti;
	}


}
