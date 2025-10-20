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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.File450Api;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.ElaboraDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.ErrorObjectDTO;
import it.csi.risca.riscabesrv.dto.File450DTO;
import it.csi.risca.riscabesrv.dto.ReportResultDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.ErrorMessages;

@Service
public class File450ApiServiceImpl extends BaseApiServiceImpl implements File450Api {

	@Autowired
	public BusinessLogic businessLogic;


	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager; 
	
    @Autowired
    private TracciamentoManager tracciamentoManager;
    
    private static final String RRM="Ruolo Ricerca Morosita'";
    
    private static final String DES_TIPO_ELABORA ="des_tipo_elabora";
    
    private static final String IDENTITY = "identity";
    
    @Autowired
	private AmbitiDAO ambitiDAO;
    
    
	@Override
	public Response createFile450(File450DTO file450, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[File450ApiServiceImpl::createFile450] BEGIN");
		LOGGER.debug("[File450ApiServiceImpl::createFile450] Parametro in input file450 :\n " + file450);
		LOGGER.debug("[File450ApiServiceImpl::createFile450] Parametro in input fruitore :\n " + fruitore);

		try {
			setGestAttoreInsUpd(file450, fruitore, httpRequest, httpHeaders);

			LOGGER.debug("[File450ApiServiceImpl::createFile450] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,
					httpHeaders, Constants.POST_FILE_450); 
			LOGGER.debug("[File450ApiServiceImpl::createFile450] verificaIdentitaDigitale END");
			businessLogic.validatorDTO(file450, null, null);
			
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} 


		Identita identita = null;
		Long idAmbito = null;
		try {

			identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			if(identita != null) {
				idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
				LOGGER.debug("[File450ApiServiceImpl::createFile450] idAmbito:" + idAmbito);
			}


			file450 = businessLogic.createFile450(file450);
			ResponseBuilder rb = Response.ok(file450);
			LOGGER.debug("[File450ApiServiceImpl : createFile450 ] END ");		      		  
			return rb.build();
		}catch (DAOException e) {		
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOGGER.debug("!!!CODE 1 chiave duplicata!!!!");
				LOGGER.error(e);
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA, null, null);
				return Response.serverError().entity(err.getTitle()).build();
			}
			LOGGER.error(e);
			return Response.serverError().entity(e.getMessage()).build();
		} 
		catch (SystemException e) {
			LOGGER.error(e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		catch (NotFoundException e) {
			LOGGER.error(e);
			return Response.serverError().entity(Response.Status.NOT_FOUND).status(Response.Status.NOT_FOUND.getStatusCode()).build();
		}
		catch (DatiInputErratiException e) {
			LOGGER.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getTitle());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), e.getError().getCode(), e.getError().getTitle(), null, null);
			return Response.serverError().entity(err).status(status).build();
		}
		finally {		
		}
	}
	
	@Override
	public Response creaRuoloRicercaMorosita(String fruitore, String tipoRicercaMorosita,
			Integer anno, Integer flgRest, Integer flgAnn,String lim,List<Long> listIdStatiDebitoriSelezionati, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[File450ApiServiceImpl::creaReportRicercaMorosita] BEGIN");
		ReportResultDTO reportResult = null;
		ElaboraDTO elabora =null;
		try {			
			LOGGER.debug("[File450ApiServiceImpl::creaRuoloRicercaMorosita] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_PAGA);
			LOGGER.debug("[File450ApiServiceImpl::creaRuoloRicercaMorosita] verificaIdentitaDigitale END");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);		
			
			LOGGER.debug("[File450ApiServiceImpl::creaRuoloRicercaMorosita] getAmbitoByIdentitaOrFonte BEGIN");
			
			if(tipoRicercaMorosita == null || !tipoRicercaMorosita.equals(Constants.RICMOR08)) {
				ErrorObjectDTO error = new ErrorObjectDTO();
				error.setCode(ErrorMessages.CODE_E001_ERRORE_VALIDAZIONE_DATI_INGRESSO);
				error.setTitle(ErrorMessages.MESSAGE_E001_ERRORE_VALIDAZIONE_DATI_INGRESSO+" tipoRicercaMorosita: "+ tipoRicercaMorosita+" non consentito");
				throw new DatiInputErratiException(error);
			}
			
			Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
			LOGGER.debug("[File450ApiServiceImpl::creaRuoloRicercaMorosita] getAmbitoByIdentitaOrFonte END");
			AmbitoDTO ambito = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			String attore = identita != null ? identita.getCodFiscale() : httpHeaders.getHeaderString(Constants.ATTORE_RISCA);	
			//String attore = identita != null ? identita.getCodFiscale() : fruitore;
			
			LOGGER.debug("attore-->"+attore);
			switch (ambito.getCodAmbito()) {
			  case Constants.AMBIENTE:
				  reportResult = businessLogic.creaRuoloRicercaMorosita(elabora,tipoRicercaMorosita,anno, flgRest,flgAnn, lim, ambito, attore, listIdStatiDebitoriSelezionati);
			    break;
			  case Constants.OPERE_PUBBLICHE:
				//TO DO
			    break;
			  case Constants.ATTIVITA_ESTRATTIVE:
				//TO DO
			    break;
			  case Constants.TRIBUTI:
			    //TO DO
				  break;
			}
			if(reportResult != null && reportResult.getElabora()!=null) {
				tracciamentoManager.saveTracciamento(attore, reportResult, identita, null, "JSON CREA RUOLO RICERCA MOROSITA",
						reportResult.getElabora().getNomeFileGenerato(), "RISCA_T_FILE_450.CREA_RUOLO_RICERCA_MOROSITA",
						Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, true, httpRequest);
				
			}
		
		} catch (DatiInputErratiException e) {
			LOGGER.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getTitle());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), e.getError().getCode(), e.getError().getTitle(), null, null);
			return Response.serverError().entity(err).status(status).build();
		}		
		catch (BusinessException be) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return    handleBusinessException(be, RRM);
		} 		
		catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			return handleUnexpectedException(e, RRM);
		}

		LOGGER.debug("[File450ApiServiceImpl::creaRuoloRicercaMorosita] END");
		return Response.ok(reportResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	
	private Response handleBusinessException(BusinessException be, String  desTipoElabora) {
	    Map<String, String> detail = createDetailMap(desTipoElabora);
	    be.setDetail(detail);
	    return handleBusinessException(be.getHttpStatus(), be);
	}
	
	private Response handleUnexpectedException(Exception e, String  desTipoElabora) {
	    LOGGER.error("[" + getClass().getSimpleName() + "::" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] ERROR : "+ e);
	    ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta.", null, null);
	    Map<String, String> detail = createDetailMap(desTipoElabora);
	    err.setDetail(detail);
	    return  Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
	}
	
	private Map<String, String> createDetailMap(String  desTipoElabora) {
	    Map<String, String> detail = new HashMap<>();
	    detail.put(DES_TIPO_ELABORA, desTipoElabora);
	    return detail;
	}

}
