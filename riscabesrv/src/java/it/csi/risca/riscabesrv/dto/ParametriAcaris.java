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

public class ParametriAcaris {
	
    private long idAmbitoDTO;
    private String repositoryName;
    private long idAoo;
    private long idStruttura;
    private long idNodo;
    private String codiceFiscale;
    private String appKey;
    private String paroleChiaveSF;

    public ParametriAcaris() {
    }
    

    
    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public long getIdAoo() {
        return idAoo;
    }

    public void setIdAoo(long idAoo) {
        this.idAoo = idAoo;
    }

    public long getIdStruttura() {
        return idStruttura;
    }

    public void setIdStruttura(long idStruttura) {
        this.idStruttura = idStruttura;
    }

    public long getIdNodo() {
        return idNodo;
    }

    public void setIdNodo(long idNodo) {
        this.idNodo = idNodo;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    
	public long getIdAmbitoDTO() {
		return idAmbitoDTO;
	}

	public void setIdAmbitoDTO(long idAmbitoDTO) {
		this.idAmbitoDTO = idAmbitoDTO;
	}



	public String getParoleChiaveSF() {
		return paroleChiaveSF;
	}



	public void setParoleChiaveSF(String paroleChiaveSF) {
		this.paroleChiaveSF = paroleChiaveSF;
	}

    
    
}
