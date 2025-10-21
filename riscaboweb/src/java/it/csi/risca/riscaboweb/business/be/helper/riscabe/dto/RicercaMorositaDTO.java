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

import java.io.Serializable;
import java.util.List;
/**
 * The type Ricerca Morosita DTO.
 *
 * @author CSI PIEMONTE
 */
public class RicercaMorositaDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String tipoRicercaMorosita;
	
	private Integer anno;
	
	private Integer flgRest;
	
	private Integer flgAnn;
	
	private String lim;
	
	private List<Long> listIdStatoDebitorio;

	public RicercaMorositaDTO(String tipoRicercaMorosita, Integer anno, Integer flgRest, Integer flgAnn, String lim) {
		super();
		this.tipoRicercaMorosita = tipoRicercaMorosita;
		this.anno = anno;
		this.flgRest = flgRest;
		this.flgAnn = flgAnn;
		this.lim = lim;
	}
	
	public RicercaMorositaDTO(String tipoRicercaMorosita, Integer anno, Integer flgRest, Integer flgAnn, String lim, List<Long> listIdStatoDebitorio) {
		super();
		this.tipoRicercaMorosita = tipoRicercaMorosita;
		this.anno = anno;
		this.flgRest = flgRest;
		this.flgAnn = flgAnn;
		this.lim = lim;
		this.listIdStatoDebitorio = listIdStatoDebitorio;
		
	}

	public String getTipoRicercaMorosita() {
		return tipoRicercaMorosita;
	}

	public void setTipoRicercaMorosita(String tipoRicercaMorosita) {
		this.tipoRicercaMorosita = tipoRicercaMorosita;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public Integer getFlgRest() {
		return flgRest;
	}

	public void setFlgRest(Integer flgRest) {
		this.flgRest = flgRest;
	}

	public Integer getFlgAnn() {
		return flgAnn;
	}

	public void setFlgAnn(Integer flgAnn) {
		this.flgAnn = flgAnn;
	}

	public String getLim() {
		return lim;
	}
	

	public void setLim(String lim) {
		this.lim = lim;
	}

	public List<Long> getListIdStatoDebitorio() {
		return listIdStatoDebitorio;
	}

	public void setListIdStatoDebitorio(List<Long> listIdStatoDebitorio) {
		this.listIdStatoDebitorio = listIdStatoDebitorio;
	}

	

}
