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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ComuneExtendedDTO;
import it.csi.risca.riscabesrv.dto.utils.ValidatorDto;
import it.csi.risca.riscabesrv.util.ErrorMessages;

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
	private List<IndirizzoSpedizioneDTO>  indirizziSpedizione;
	
	private transient String _risca_id_recapito;
	
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
	
//	public String getAmbito() {
//		return ambito;
//	}
//
//	public void setAmbito(String ambito) {
//		this.ambito = ambito;
//	}

	public String get_risca_id_recapito() {
		return _risca_id_recapito;
	}

	public void set_risca_id_recapito(String _risca_id_recapito) {
		this._risca_id_recapito = _risca_id_recapito;
	}
	
	
	public void validate(String fruitore) throws DatiInputErratiException {
	
	        boolean isItaliaRecap = (getIdNazioneRecapito() != null && getIdNazioneRecapito() == 1/*IT*/) 
	        		|| (getNazioneRecapito() != null && getNazioneRecapito().getIdNazione() == 1/*IT*/);
	        
	        HashMap<String, String> fieldsMap =  new HashMap<String, String>();
	        //Deve avere id Fonte 1 (RISCA) per fare il controllo sul tipo di invio
	        if(getFonte().getIdFonte()!= null && (getFonte().getIdFonte() == 1 ||  getFonte().getIdFonte() == 3)) {
				Long tipoInvio = getTipoInvio() == null ? getIdTipoInvio() : getTipoInvio().getIdTipoInvio();
				fieldsMap = ValidatorDto.getInvalidMandatoryFields("",
					"id_tipo_invio", tipoInvio != null,
					"id_tipo_sede", getTipoSede() == null ? true : getTipoSede().getIdTipoSede(),
					"id_nazione_recapito", getNazioneRecapito() == null ? true : getNazioneRecapito().getIdNazione(),
					"id_comune_recapito", getComuneRecapito() == null ? true : getComuneRecapito().getIdComune(),
					"citta_estera_recapito", isItaliaRecap ? true : getCittaEsteraRecapito(),
					"indirizzo", fruitore !=null  ? true : getIndirizzo(),					
					"cap_recapito", fruitore !=null  ? true :getCapRecapito(),
					"email", tipoInvio != 2/*E - eMail*/? true : getEmail(),
					"pec", tipoInvio != 1/*P - Pec*/? true : getPec()
				);
	        }
	        
	        for (Map.Entry<String, String> field: fieldsMap.entrySet()) {			
				System.err.print("[RecapitiExtendedDTO::validate] Campo = " + field.getKey() + " errore = " + field.getValue());
			}
						
		if(!fieldsMap.isEmpty()) {	
			for (Map.Entry<String, String> field: fieldsMap.entrySet()) {
			
				ErrorObjectDTO error = new ErrorObjectDTO();
				error.setCode(ErrorMessages.CODE_E001_ERRORE_VALIDAZIONE_DATI_INGRESSO);
				error.setTitle(ErrorMessages.MESSAGE_E001_ERRORE_VALIDAZIONE_DATI_INGRESSO+" Campo: "+field.getKey()+" errore: "+field.getValue());
				throw new DatiInputErratiException(error);
			}
		
			
		}	
	}

 
}
