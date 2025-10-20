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

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpedizioneActaDTO extends GestAttoreDTO {

	@JsonProperty("id_spedizione_acta")
	private Long idSpedizioneActa;

	@JsonProperty("id_spedizione")
	private Long idSpedizione;

	@JsonProperty("id_tipo_spedizione")
	private Long idTipoSpedizione;

	@JsonProperty("cod_tipo_spedizione")
	private String codTipoSpedizione;

	@JsonProperty("id_elabora")
	private Long idElabora;

	@JsonProperty("flg_archiviata_acta")
	private int flgArchiviataActa;

	@JsonProperty("nome_dirigente_protempore")
	private String nomeDirigenteProtempore;

	public Long getIdSpedizioneActa() {
		return idSpedizioneActa;
	}

	public void setIdSpedizioneActa(Long idSpedizioneActa) {
		this.idSpedizioneActa = idSpedizioneActa;
	}

	public Long getIdSpedizione() {
		return idSpedizione;
	}

	public void setIdSpedizione(Long idSpedizione) {
		this.idSpedizione = idSpedizione;
	}

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public int getFlgArchiviataActa() {
		return flgArchiviataActa;
	}

	public void setFlgArchiviataActa(int flgArchiviataActa) {
		this.flgArchiviataActa = flgArchiviataActa;
	}

	public String getNomeDirigenteProtempore() {
		return nomeDirigenteProtempore;
	}

	public void setNomeDirigenteProtempore(String nomeDirigenteProtempore) {
		this.nomeDirigenteProtempore = nomeDirigenteProtempore;
	}

	public Long getIdTipoSpedizione() {
		return idTipoSpedizione;
	}

	public void setIdTipoSpedizione(Long idTipoSpedizione) {
		this.idTipoSpedizione = idTipoSpedizione;
	}

	public String getCodTipoSpedizione() {
		return codTipoSpedizione;
	}

	public void setCodTipoSpedizione(String codTipoSpedizione) {
		this.codTipoSpedizione = codTipoSpedizione;
	}

	@Override
	public String toString() {
		return "SpedizioneActaDTO [idSpedizioneActa=" + idSpedizioneActa + ", idSpedizione=" + idSpedizione
				+ ", idElabora=" + idElabora + ", flgArchiviataActa=" + flgArchiviataActa + ", nomeDirigenteProtempore="
				+ nomeDirigenteProtempore + ", getGestAttoreIns()=" + getGestAttoreIns() + ", getGestDataIns()="
				+ getGestDataIns() + ", getGestAttoreUpd()=" + getGestAttoreUpd() + ", getGestDataUpd()="
				+ getGestDataUpd() + ", getGestUid()=" + getGestUid() + "]";
	}

}
