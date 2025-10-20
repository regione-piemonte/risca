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
import java.util.Map;

public class ArchiviazioneData {
	private ArchiviazioneActaDTO archiviazioneActa;
	private List<String> listaCodiciUtenza;
	private Long idElabora;
	private Map<String, String> configurazioniStardas;
	private String filePath;
	private int multiclassificazione;

	public ArchiviazioneActaDTO getArchiviazioneActa() {
		return archiviazioneActa;
	}

	public void setArchiviazioneActa(ArchiviazioneActaDTO archiviazioneActa) {
		this.archiviazioneActa = archiviazioneActa;
	}

	public List<String> getListaCodiciUtenza() {
		return listaCodiciUtenza;
	}

	public void setListaCodiciUtenza(List<String> listaCodiciUtenza) {
		this.listaCodiciUtenza = listaCodiciUtenza;
	}

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public Map<String, String> getConfigurazioniStardas() {
		return configurazioniStardas;
	}

	public void setConfigurazioniStardas(Map<String, String> configurazioniStardas) {
		this.configurazioniStardas = configurazioniStardas;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getMulticlassificazione() {
		return multiclassificazione;
	}

	public void setMulticlassificazione(int multiclassificazione) {
		this.multiclassificazione = multiclassificazione;
	}

}
