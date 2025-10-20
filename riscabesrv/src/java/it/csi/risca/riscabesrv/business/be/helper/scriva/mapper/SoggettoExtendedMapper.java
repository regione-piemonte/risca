/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.scriva.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.NazioneDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.SoggettoExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.TipoNaturaGiuridicaDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.TipoSoggettoDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class SoggettoExtendedMapper {

	protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	
	private SoggettoExtendedDTO soggettoScriva;
	private SoggettiExtendedDTO soggettoRisca;


	
	
	public SoggettoExtendedMapper(SoggettoExtendedDTO soggettoScriva, SoggettiExtendedDTO soggettoRisca) {
		super();
		this.soggettoScriva = soggettoScriva;
		this.soggettoRisca = soggettoRisca;
	}


	public SoggettoExtendedDTO mapSoggettoRiscaToSoggettoScriva() throws ParseException {
	    LOGGER.debug("[SoggettoExtendedMapper::mapSoggettoRiscaToSoggettoScriva] BEGIN");
        SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
		if(soggettoScriva == null) {
			this.soggettoScriva = new SoggettoExtendedDTO();
		}
		
			RecapitiExtendedDTO recapitoPrincipale = soggettoRisca.getRecapiti().stream()
					.filter(r -> r.getTipoRecapito().getCodTipoRecapito().equals("P")).collect(Collectors.toList()).get(0);
			
			 soggettoScriva.setCittaEsteraNascita(soggettoRisca.getCittaEsteraNascita());
			 soggettoScriva.setCfSoggetto(soggettoRisca.getCfSoggetto());
			 soggettoScriva.setCognome(soggettoRisca.getCognome());
			 

	         Calendar cal = Calendar.getInstance();
	         Date now = cal.getTime();
			 soggettoScriva.setDataAggiornamento(now);

			 if(soggettoRisca.getDataCancellazione() != null)
			     soggettoScriva.setDataCessazioneSoggetto(formatter.parse(soggettoRisca.getDataCancellazione()));
			 if(StringUtils.isNotBlank(soggettoRisca.getDataNascitaSoggetto())) 
			     soggettoScriva.setDataNascitaSoggetto(formatter.parse(soggettoRisca.getDataNascitaSoggetto()));
			 
			 soggettoScriva.setDenAnnoCciaa(null);

			 soggettoScriva.setDenNumeroCciaa(null);
			 soggettoScriva.setDenProvinciaCciaa(null);
			 soggettoScriva.setDenSoggetto(soggettoRisca.getDenSoggetto());
			 soggettoScriva.setDesEmail(recapitoPrincipale.getEmail());
			 soggettoScriva.setDesLocalita(recapitoPrincipale.getDesLocalita());
			 soggettoScriva.setDesPec(recapitoPrincipale.getPec());
			 soggettoScriva.setGestAttoreIns(soggettoRisca.getGestAttoreIns());
			 soggettoScriva.setGestAttoreUpd(soggettoRisca.getGestAttoreUpd());
			 soggettoScriva.setGestUID(soggettoRisca.getGestUid());
			 if(soggettoRisca.getComuneNascita() != null)
				 if(soggettoRisca.getComuneNascita().getIdComune() > 0) {
					 soggettoScriva.setComuneNascita(soggettoRisca.getComuneNascita());
				     soggettoScriva.setIdComuneNascita(soggettoRisca.getComuneNascita().getIdComune()); 
				 }
			 soggettoScriva.setIdMasterdata(1L);
			 soggettoScriva.setIdMasterdataOrigine(1L);


			 soggettoScriva.setIdTipoNaturaGiuridica(soggettoRisca.getIdTipoNaturaGiuridica());
			 
			 soggettoScriva.setIdTipoSoggetto(soggettoRisca.getTipoSoggetto().getIdTipoSoggetto());
			 soggettoScriva.setIndirizzoSoggetto(recapitoPrincipale.getIndirizzo());
			
			 soggettoScriva.setNome(soggettoRisca.getNome());
			 soggettoScriva.setNumCellulare(recapitoPrincipale.getCellulare());
			 soggettoScriva.setNumCivicoIndirizzo(recapitoPrincipale.getNumCivico());
			 soggettoScriva.setNumTelefono(recapitoPrincipale.getTelefono());
			 soggettoScriva.setPartitaIvaSoggetto(soggettoRisca.getPartitaIvaSoggetto());
			 
			 TipoSoggettoDTO tipoSoggetto = new TipoSoggettoDTO();
			 tipoSoggetto.setIdTipoSoggetto(soggettoRisca.getTipoSoggetto().getIdTipoSoggetto());
			 tipoSoggetto.setCodTipoSoggetto(soggettoRisca.getTipoSoggetto().getCodTipoSoggetto());
			 tipoSoggetto.setDesTipoSoggetto(soggettoRisca.getTipoSoggetto().getDesTipoSoggetto());
			 soggettoScriva.setTipoSoggetto(tipoSoggetto);
			 
			 TipoNaturaGiuridicaDTO tipoNaturaGiuridicaDTO = new TipoNaturaGiuridicaDTO();
			 if(soggettoRisca.getTipiNaturaGiuridica() != null) {
				 tipoNaturaGiuridicaDTO.setCodTipoNaturaGiuridica(soggettoRisca.getTipiNaturaGiuridica().getCodTipoNaturaGiuridica());
				 tipoNaturaGiuridicaDTO.setDesTipoNaturaGiuridica(soggettoRisca.getTipiNaturaGiuridica().getDesTipoNaturaGiuridica());
				 tipoNaturaGiuridicaDTO.setFlgPubblico(soggettoRisca.getTipiNaturaGiuridica().getFlgPubblico().equals("0") ? true:false);
				 tipoNaturaGiuridicaDTO.setIdTipoNaturaGiuridica(soggettoRisca.getTipiNaturaGiuridica().getIdTipoNaturaGiuridica());
				 tipoNaturaGiuridicaDTO.setOrdinamentoTipoNaturaGiu(soggettoRisca.getTipiNaturaGiuridica().getOrdinaNaturaGiuridica());
				 tipoNaturaGiuridicaDTO.setSiglaTipoNaturaGiuridica(soggettoRisca.getTipiNaturaGiuridica().getSiglaTipoNaturaGiuridica());
				 soggettoScriva.setTipoNaturaGiuridica(tipoNaturaGiuridicaDTO);
			 }


			 
			 NazioneDTO nazioneSoggettoRisca = soggettoRisca.getNazioneNascita();
			 NazioneDTO nazioneNascita = null;
			 if(nazioneSoggettoRisca != null)
				 if(nazioneSoggettoRisca.getIdNazione() > 0) {
					 nazioneNascita = new NazioneDTO();
					 nazioneNascita.setIdNazione(nazioneSoggettoRisca.getIdNazione());
					 nazioneNascita.setIdOrigine(nazioneSoggettoRisca.getIdOrigine());

					 nazioneNascita.codBelfioreNazione(nazioneSoggettoRisca.getCodBelfioreNazione());
					 nazioneNascita.codIso2(nazioneSoggettoRisca.getCodIso2());
					 nazioneNascita.codIstatNazione(nazioneSoggettoRisca.getCodIstatNazione());
					 nazioneNascita.dataFineValidita(nazioneSoggettoRisca.getDataFineValidita());
					 nazioneNascita.dataInizioValidita(nazioneSoggettoRisca.getDataFineValidita());
					 nazioneNascita.denomNazione(nazioneSoggettoRisca.getDenomNazione());
					 nazioneNascita.dtIdStato(nazioneSoggettoRisca.getDtIdStato());
					 nazioneNascita.dtIdStatoNext(nazioneSoggettoRisca.getDtIdStatoNext());
					 nazioneNascita.dtIdStatoPrev(nazioneSoggettoRisca.getDtIdStatoPrev());
					 soggettoScriva.setNazioneNascita(nazioneNascita);
					 soggettoScriva.setIdNazioneNascita(soggettoRisca.getNazioneNascita().getIdNazione());
				 }

			 if(soggettoRisca.getTipoSoggetto().getCodTipoSoggetto().equals("PF")) {
				 soggettoScriva.setCapResidenza(recapitoPrincipale.getCapRecapito());
				 soggettoScriva.setCittaEsteraResidenza(recapitoPrincipale.getCittaEsteraRecapito());
				 soggettoScriva.setComuneResidenza(recapitoPrincipale.getComuneRecapito());
				 soggettoScriva.setIdComuneResidenza(recapitoPrincipale.getIdComuneRecapito());
				 soggettoScriva.setIdNazioneResidenza(recapitoPrincipale.getIdNazioneRecapito());
				 soggettoScriva.setNazioneResidenza(nazioneNascita);


			}else {
				 soggettoScriva.setCapSedeLegale(recapitoPrincipale.getCapRecapito());
				 soggettoScriva.setCittaEsteraSedeLegale(recapitoPrincipale.getCittaEsteraRecapito());
				 soggettoScriva.setCittaEsteraSedeLegale(recapitoPrincipale.getCittaEsteraRecapito());
				 soggettoScriva.setComuneSedeLegale(recapitoPrincipale.getComuneRecapito());
				 soggettoScriva.setIdComuneSedeLegale(recapitoPrincipale.getIdComuneRecapito());
				 soggettoScriva.setIdNazioneResidenza(recapitoPrincipale.getIdNazioneRecapito());
				 soggettoScriva.setNazioneSedeLegale(nazioneNascita);
			}
			
		

			    LOGGER.debug("[SoggettoExtendedMapper::mapSoggettoRiscaToSoggettoScriva] END");
		return soggettoScriva;
		
	}


	public SoggettoExtendedDTO getSoggettoScriva() {
		return soggettoScriva;
	}


	public void setSoggettoScriva(SoggettoExtendedDTO soggettoScriva) {
		this.soggettoScriva = soggettoScriva;
	}


	public SoggettiExtendedDTO getSoggettoRisca() {
		return soggettoRisca;
	}


	public void setSoggettoRisca(SoggettiExtendedDTO soggettoRisca) {
		this.soggettoRisca = soggettoRisca;
	}


	
	
}
