/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.IndirizziSpedizioneApi;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.IndirizziSpedizioneDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Indirizzi Spedizione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class IndirizziSpedizioneApiServiceImpl extends BaseApiServiceImpl  implements IndirizziSpedizioneApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private IndirizziSpedizioneDAO indSpedDAO;

	@Autowired
	public BusinessLogic businessLogic;
	
    @Autowired
    private TracciamentoManager tracciamentoManager;
	
	@Override
	public Response saveIndirizziSpedizione(IndirizzoSpedizioneDTO indSped,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[IndirizziSpedizioneApiServiceImpl::saveIndirizziSpedizione] BEGIN");
		IndirizzoSpedizioneDTO indSpedSaved;
		try {
			setGestAttoreInsUpd(indSped, null, httpRequest, httpHeaders);
			businessLogic.validatorDTO(indSped, null, null);
			indSpedSaved = indSpedDAO.saveIndirizziSpedizione(indSped);
			
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		}  catch (Exception e1) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		
		
    	try {
           	LOGGER.debug("[IndirizziSpedizioneApiServiceImpl::saveIndirizziSpedizione] BEGIN save tracciamento");			

			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), null);


			tracciamentoManager.saveTracciamento(null, indSpedSaved, identita, null, "JSON  INDIRIZZO SPEDIZIONE",
					indSpedSaved.getIdRecapitoPostel() != null ? indSpedSaved.getIdRecapitoPostel().toString() : null, "RISCA_R_RECAPITO_POSTEL",
					Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, true, httpRequest);		

           	LOGGER.debug("[IndirizziSpedizioneApiServiceImpl::saveIndirizziSpedizione] END save tracciamento");

		} catch (Exception e) {
            LOGGER.error("[IndirizziSpedizioneApiServiceImpl::saveIndirizziSpedizione:: operazione insertLogAudit]: "+e);
            ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}   
		
		LOGGER.debug("[IndirizziSpedizioneApiServiceImpl::saveIndirizziSpedizione] END");
		return Response.ok(indSpedSaved).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}


	@Override
	public Response updateIndirizziSpedizione(IndirizzoSpedizioneDTO indSped,String fruitore, Long modVerifica,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericExceptionList {
		LOGGER.debug("[IndirizziSpedizioneApiServiceImpl::updateIndirizziSpedizione] BEGIN");
		IndirizzoSpedizioneDTO indSpedUpdated;
		try {
			setGestAttoreInsUpd(indSped, fruitore, httpRequest, httpHeaders);
			businessLogic.validatorDTO(indSped, null, null);
			indSpedUpdated = indSpedDAO.updateIndirizziSpedizione(indSped, modVerifica);
			LOGGER.debug("[IndirizziSpedizioneApiServiceImpl::updateIndirizziSpedizione] END");
           	LOGGER.debug("[IndirizziSpedizioneApiServiceImpl::updateIndirizziSpedizione] BEGIN save tracciamento");			

			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), null);
			 if(modVerifica == null || modVerifica == 0) {//se 1 non deve tracciare
				tracciamentoManager.saveTracciamento(null, indSpedUpdated, identita, null, "JSON  INDIRIZZO SPEDIZIONE",
				    indSpedUpdated.getIdRecapitoPostel() != null ? indSpedUpdated.getIdRecapitoPostel().toString() : null, "RISCA_R_RECAPITO_POSTEL",
				    Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, true, httpRequest);								
			}
           	LOGGER.debug("[IndirizziSpedizioneApiServiceImpl::updateIndirizziSpedizione] END save tracciamento");

		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
	        List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
	        errorsList.add(err);
			return Response.status(Response.Status.BAD_REQUEST).entity(errorsList).build();
		}  catch (GenericExceptionList e1) {
			throw e1;
		} catch (Exception e) {
            LOGGER.error("[IndirizziSpedizioneApiServiceImpl::updateIndirizziSpedizione:: operazione insertLogAudit]: "+e);
            ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		
		LOGGER.debug("[IndirizziSpedizioneApiServiceImpl::updateIndirizziSpedizione] END");
		return Response.ok(indSpedUpdated).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}


	@Override
	public Response loadIndirizziSpedizioneByIdGruppoSoggetto(Long idGruppoSoggetto, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[IndirizziSpedizioneApiServiceImpl : loadIndirizziSpedizioneByIdGruppoSoggetto ] BEGIN");
		List<IndirizzoSpedizioneDTO> listIndirizzoSpedizione = indSpedDAO.loadIndirizziSpedizioneByIdGruppoSoggetto(idGruppoSoggetto);
		LOGGER.debug("[IndirizziSpedizioneApiServiceImpl : loadIndirizziSpedizioneByIdGruppoSoggetto ] END");
		return Response.ok(listIndirizzoSpedizione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	
}
