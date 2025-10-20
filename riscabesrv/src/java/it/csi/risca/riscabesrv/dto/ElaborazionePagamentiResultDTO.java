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

import java.util.List;

public class ElaborazionePagamentiResultDTO {
	int numPagamScart;
	int numPagamLetti;
	int numPagamCaric;
	
	List<StatoDebitorioExtendedDTO> sdList;

	public int getNumPagamScart() {
		return numPagamScart;
	}

	public void setNumPagamScart(int numPagamScart) {
		this.numPagamScart = numPagamScart;
	}

	public int getNumPagamLetti() {
		return numPagamLetti;
	}

	public void setNumPagamLetti(int numPagamLetti) {
		this.numPagamLetti = numPagamLetti;
	}

	public int getNumPagamCaric() {
		return numPagamCaric;
	}

	public void setNumPagamCaric(int numPagamCaric) {
		this.numPagamCaric = numPagamCaric;
	}

	public List<StatoDebitorioExtendedDTO> getSdList() {
		return sdList;
	}

	public void setSdList(List<StatoDebitorioExtendedDTO> sdList) {
		this.sdList = sdList;
	}

}
