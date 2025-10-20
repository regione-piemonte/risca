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

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.ProvvedimentiIstanzeApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.GenericException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.ProvvedimentiIstanzeDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.ProvvedimentoDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Provvedimenti Istanze api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class ProvvedimentiIstanzeApiServiceImpl extends BaseApiServiceImpl implements ProvvedimentiIstanzeApi{
	
	private static final String IDENTITY = "identity";

    @Autowired
    private TracciamentoManager tracciamentoManager;
    
	@Autowired
	private ProvvedimentiIstanzeDAO provvedimentiIstanzeDAO;
	
    @Autowired
    private IdentitaDigitaleManager identitaDigitaleManager; 

	@Autowired
	public BusinessLogic businessLogic;	
	
	@Override
	public Response getProvvedimentiIstanze(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws Exception {
        LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::getProvvedimentiIstanze] BEGIN");
        List<ProvvedimentoDTO> listProvvedimentoDTO;
		try {
			listProvvedimentoDTO = provvedimentiIstanzeDAO.getProvvedimentiIstanze();
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
       
        LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::getProvvedimentiIstanze] END");
        return Response.ok(listProvvedimentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getProvvedimentiIstanzeByidRiscossione(Long idRiscossione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::getProvvedimentiIstanzeByidRiscossione] BEGIN");
        List<ProvvedimentoDTO> provvedimentoDTO;
		try {
			long start = System.currentTimeMillis();
			provvedimentoDTO = provvedimentiIstanzeDAO.getProvvedimentiIstanzeByidRiscossione(idRiscossione);
			long stop = System.currentTimeMillis();
			LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::getProvvedimentiIstanzeByidRiscossione] QueryExecutionTime: " + (stop - start));
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
        
        LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::getProvvedimentiIstanzeByidRiscossione] END");
        return Response.ok(provvedimentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getProvvedimentoIstanzaByIdProvvedimenti(Long idProvvedimentiIstanze, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::getProvvedimentoIstanzaByIdProvvedimenti] BEGIN");
        ProvvedimentoDTO provvedimentoDTO;
		try {
			provvedimentoDTO = provvedimentiIstanzeDAO.getProvvedimentoIstanzaByIdProvvedimenti(idProvvedimentiIstanze);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
        
        LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::getProvvedimentoIstanzaByIdProvvedimenti] END");
        return Response.ok(provvedimentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveProvvedimentiIstanze(ProvvedimentoDTO provvedimentoDTO, String fruitore,  SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		  LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::saveProvvedimentiIstanze] BEGIN");
		  Long id = null;
  		try {
			setGestAttoreInsUpd(provvedimentoDTO, fruitore, httpRequest, httpHeaders);
  			businessLogic.validatorDTO(provvedimentoDTO, null, null);
  		   id = provvedimentiIstanzeDAO.saveProvvedimentiIstanze(provvedimentoDTO);
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
  	
		  


	        if (id == null) {
	            LOGGER.error("[ProvvedimentiIstanzeApiServiceImpl::saveProvvedimentiIstanze] ERROR : ");
	            LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::saveProvvedimentiIstanze] END");
	            return Response.ok(id).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	        }else {

	        	try {
	           	LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::saveProvvedimentiIstanze] BEGIN save tracciamento");
               //DP Aggiunto setIdProvvedimento
	           	provvedimentoDTO.setIdProvvedimento(id); 
				Identita identita =IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);

				
				tracciamentoManager.saveTracciamento(fruitore, provvedimentoDTO, identita, provvedimentoDTO.getIdRiscossione(), "JSON PROVVEDIMENTO",
						provvedimentoDTO.getIdProvvedimento() != null ? provvedimentoDTO.getIdProvvedimento().toString() : null, "RISCA_R_PROVVEDIMENTO",
						Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, false, false, httpRequest);
				
	           	LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::saveProvvedimentiIstanze] END save tracciamento");

				} catch (Exception e) {
		            LOGGER.error("[ProvvedimentiIstanzeApiServiceImpl::saveProvvedimentiIstanze:: saveLogAudit]: ",e);
		            return Response.serverError().status(500).build();
		        }
	        }
	        LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::saveProvvedimentiIstanze] END");
	        return Response.ok(provvedimentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response updateProvvedimentiIstanze(ProvvedimentoDTO provvedimentoDTO, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
		  LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::updateProvvedimentiIstanze] BEGIN");
		  Long id = null;
	  		try {
				setGestAttoreInsUpd(provvedimentoDTO, null, httpRequest, httpHeaders);
	  			businessLogic.validatorDTO(provvedimentoDTO, null, null);
	  		  id = provvedimentiIstanzeDAO.updateProvvedimentiIstanze(provvedimentoDTO);
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

	      if (id != null) {
		        try {
		           	LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::updateProvvedimentiIstanze] BEGIN save tracciamento");

					Identita identita =IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);
					tracciamentoManager.saveTracciamento(null, provvedimentoDTO, identita, provvedimentoDTO.getIdRiscossione(), "JSON PROVVEDIMENTO",
							provvedimentoDTO.getIdProvvedimento() != null ? provvedimentoDTO.getIdProvvedimento().toString() : null, "RISCA_R_PROVVEDIMENTO",
							Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, false, false, httpRequest);
		           	LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::updateProvvedimentiIstanze] END save tracciamento");

				} catch (Exception e) {
		            LOGGER.error("[ProvvedimentiIstanzeApiServiceImpl::updateProvvedimentiIstanze:: updateLogAudit]: "+e);
					ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
		            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
				}
	        }
	        LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::updateProvvedimentiIstanze] END");
	        return Response.ok(provvedimentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response deleteProvvedimentiIstanze(Long idProvvedimentiIstanze, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::deleteProvvedimentiIstanze] BEGIN");
        ProvvedimentoDTO provvedimentoDTO = null;
		try {
			provvedimentoDTO = provvedimentiIstanzeDAO.deleteProvvedimentiIstanze(idProvvedimentiIstanze);
		}  catch (GenericException e) {
            LOGGER.error("[ProvvedimentiIstanzeApiServiceImpl::deleteProvvedimentiIstanze] ERROR:  Il provvedimento e' legato ad uno stato debitorio");
			if(e.getErroObjectDTO() != null)
	           return Response.serverError().entity(e.getErroObjectDTO()).status(Integer.parseInt(e.getErroObjectDTO().getStatus())).build();
	    } catch (SQLException e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}	
        if (provvedimentoDTO != null) {
    		try {
	           	LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::deleteProvvedimentiIstanze] BEGIN save tracciamento");

				Identita identita =IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);
				tracciamentoManager.saveTracciamento(null, provvedimentoDTO, identita, provvedimentoDTO.getIdRiscossione(), "JSON PROVVEDIMENTO",
						provvedimentoDTO.getIdProvvedimento() != null ? provvedimentoDTO.getIdProvvedimento().toString() : null, "RISCA_R_PROVVEDIMENTO",
						Constants.FLG_OPERAZIONE_DELETE, Constants.OPERAZIONE_DELETE, false, false, httpRequest);
				
	           	LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::deleteProvvedimentiIstanze] END save tracciamento");

			} catch (Exception e) {
	            LOGGER.error("[ProvvedimentiIstanzeApiServiceImpl::deleteProvvedimentiIstanze:: deleteLogAudit]: "+e);
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
        }
        LOGGER.debug("[ProvvedimentiIstanzeApiServiceImpl::deleteProvvedimentiIstanze] END");
        return Response.ok(provvedimentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
}
