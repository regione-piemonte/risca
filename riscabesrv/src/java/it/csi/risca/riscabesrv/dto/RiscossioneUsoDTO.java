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

import java.util.Date;

public class RiscossioneUsoDTO {


	private Long idRiscossioneUso;
	
	private Long  idRiscossione;
	
	private Long  idTipoUso;
	
    private Date gestDataIns;
	
    private String gestAttoreIns;
	
    private Date gestDataUpd;
	
    private String gestAttoreUpd;

    private String gestUid;

	public Long getIdRiscossioneUso() {
		return idRiscossioneUso;
	}

	public void setIdRiscossioneUso(Long idRiscossioneUso) {
		this.idRiscossioneUso = idRiscossioneUso;
	}

	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}

	public Long getIdTipoUso() {
		return idTipoUso;
	}

	public void setIdTipoUso(Long idTipoUso) {
		this.idTipoUso = idTipoUso;
	}

	public Date getGestDataIns() {
		return gestDataIns;
	}

	public void setGestDataIns(Date gestDataIns) {
		this.gestDataIns = gestDataIns;
	}

	public String getGestAttoreIns() {
		return gestAttoreIns;
	}

	public void setGestAttoreIns(String gestAttoreIns) {
		this.gestAttoreIns = gestAttoreIns;
	}

	public Date getGestDataUpd() {
		return gestDataUpd;
	}

	public void setGestDataUpd(Date gestDataUpd) {
		this.gestDataUpd = gestDataUpd;
	}

	public String getGestAttoreUpd() {
		return gestAttoreUpd;
	}

	public void setGestAttoreUpd(String gestAttoreUpd) {
		this.gestAttoreUpd = gestAttoreUpd;
	}

	public String getGestUid() {
		return gestUid;
	}

	public void setGestUid(String gestUid) {
		this.gestUid = gestUid;
	}
    
    
}
