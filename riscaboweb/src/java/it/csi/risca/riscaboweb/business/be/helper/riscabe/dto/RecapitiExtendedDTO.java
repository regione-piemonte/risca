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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * RecapitiDTO
 *
 * @author CSI PIEMONTE
 */

public class RecapitiExtendedDTO extends RecapitiDTO {

	@JsonProperty("tipo_invio")
    private TipiInvioDTO tipoInvio;
	
	@JsonProperty("tipo_recapito")
    private TipiRecapitoDTO tipoRecapito;
	
	@JsonProperty("fonte")
    private FonteDTO Fonte;
	
	@JsonProperty("comune_recapito")
    private ComuneExtendedDTO comuneRecapito;
	
	@JsonProperty("nazione_recapito")
    private NazioniDTO nazioneRecapito;

	@JsonProperty("tipo_sede")
    private TipiSedeDTO tipoSede;

	@JsonProperty("indirizzi_spedizione")
	private List<IndirizzoSpedizioneDTO> indirizziSpedizione;
	
	@JsonProperty("_risca_id_recapito")
	private String _risca_id_recapito;
	
    public TipiInvioDTO getTipoInvio() {
		return tipoInvio;
	}

	public void setTipoInvio(TipiInvioDTO tipoInvio) {
		this.tipoInvio = tipoInvio;
	}

	
	public TipiRecapitoDTO getTipoRecapito() {
		return tipoRecapito;
	}

	public void setTipoRecapito(TipiRecapitoDTO tipoRecapito) {
		this.tipoRecapito = tipoRecapito;
	}

	public FonteDTO getFonte() {
		return Fonte;
	}

	public void setFonte(FonteDTO fonte) {
		Fonte = fonte;
	}

	public ComuneExtendedDTO getComuneRecapito() {
		return comuneRecapito;
	}

	public void setComuneRecapito(ComuneExtendedDTO comuneRecapito) {
		this.comuneRecapito = comuneRecapito;
	}

	public NazioniDTO getNazioneRecapito() {
		return nazioneRecapito;
	}

	public void setNazioneRecapito(NazioniDTO nazioneRecapito) {
		this.nazioneRecapito = nazioneRecapito;
	}
 
	public TipiSedeDTO getTipoSede() {
		return tipoSede;
	}

	public void setTipoSede(TipiSedeDTO tipoSede) {
		this.tipoSede = tipoSede;
	}
	
	public List<IndirizzoSpedizioneDTO> getIndirizziSpedizione() {
		return indirizziSpedizione;
	}

	public void setIndirizziSpedizione(List<IndirizzoSpedizioneDTO> indirizziSpedizione) {
		this.indirizziSpedizione = indirizziSpedizione;
	}

	public String get_risca_id_recapito() {
		return _risca_id_recapito;
	}

	public void set_risca_id_recapito(String _risca_id_recapito) {
		this._risca_id_recapito = _risca_id_recapito;
	}
	
}
