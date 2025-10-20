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

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.GruppiApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.GruppiDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.GruppiDTO;
import it.csi.risca.riscabesrv.dto.PaginationHeaderDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type gruppi api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class GruppiApiServiceImpl extends BaseApiServiceImpl implements GruppiApi {

	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	
    @Autowired
    private GruppiDAO gruppiDAO;
    

    @Autowired
    private IrideServiceHelper serviceHelper;
    
    @Autowired
    private IdentitaDigitaleManager identitaDigitaleManager; 
    
	@Autowired
	public BusinessLogic businessLogic;
	
    @Autowired
    private TracciamentoManager tracciamentoManager;
    
    
	@Override
	public Response loadGruppiSoggetto(String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[GruppiApiServiceImpl::loadGruppiSoggetto] BEGIN");
	        try {
			    LOGGER.debug("[GruppiApiServiceImpl::loadGruppiSoggetto] verificaIdentitaDigitale BEGIN");
			     identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
						httpHeaders, Constants.LOAD_GRUPPI); 
			    LOGGER.debug("[GruppiApiServiceImpl::loadGruppiSoggetto] verificaIdentitaDigitale END");
			}
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} 
			 catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		
			} 	
	        
	      
	        List<GruppiDTO> listGruppiSoggetto;
			try {
				listGruppiSoggetto = gruppiDAO.loadGruppiSoggetto();
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			        
			}

	        LOGGER.debug("[GruppiApiServiceImpl::loadGruppiSoggetto] END");
	        return Response.ok(listGruppiSoggetto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadGruppiById(String fruitore, String codGruppo, String desGruppo, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
        LOGGER.debug("[GruppiApiServiceImpl::loadGruppiById] BEGIN");
        LOGGER.debug("[GruppiApiServiceImpl::loadGruppiById] Parametri in input codGruppo: [" + codGruppo + "] e desGruppo: [" + desGruppo + "]");
        GruppiDTO soggetto;
        try {
		    LOGGER.debug("[GruppiApiServiceImpl::loadGruppiById] verificaIdentitaDigitale BEGIN");
		     identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
					httpHeaders, Constants.LOAD_GRUPPI); 
		    LOGGER.debug("[GruppiApiServiceImpl::loadGruppiById] verificaIdentitaDigitale END");
			soggetto = gruppiDAO.loadGruppiById(codGruppo, desGruppo);
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (SQLException e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
	catch (Exception e) {
		return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

	} 
        LOGGER.debug("[GruppiApiServiceImpl::loadGruppiById] END");
        return Response.ok(soggetto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
    
	@Override
	@Transactional
	public Response saveGruppi(GruppiDTO gruppo,String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	    LOGGER.debug("[GruppiApiServiceImpl::saveGruppi] BEGIN");
        LOGGER.debug("[GruppiApiServiceImpl::saveGruppi] Parametro in input gruppo :\n " + gruppo);
        Long id = null;
        try {
		    LOGGER.debug("[GruppiApiServiceImpl::saveGruppi] verificaIdentitaDigitale BEGIN");
		    identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
					httpHeaders, Constants.POST_PUT_DEL_GRUPPI); 
		    LOGGER.debug("[GruppiApiServiceImpl::saveGruppi] verificaIdentitaDigitale END");
			businessLogic.validatorDTO(gruppo, null, null);
			setGestAttoreInsUpd(gruppo, fruitore, httpRequest, httpHeaders);
	        id = gruppiDAO.saveGruppi(gruppo);
	            
        } catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
        

        
        if (id != null) {
        	gruppo.setIdGruppoSoggetto(id);
        	
        	try {
               	LOGGER.debug("[GruppiApiServiceImpl::saveGruppi] BEGIN save tracciamento");
				Identita identita =IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
				tracciamentoManager.saveTracciamento(fruitore, gruppo, identita, null, "JSON GRUPPO",
						gruppo.getIdGruppoSoggetto() != null ? gruppo.getIdGruppoSoggetto().toString() : null, "RISCA_T_GRUPPO_SOGGETTO",
						Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, true, httpRequest);

	           	LOGGER.debug("[GruppiApiServiceImpl::saveGruppi] END save tracciamento");
			} catch (Exception e) {
	            LOGGER.error("[GruppiApiServiceImpl::saveGruppi:: operazione insertLogAudit]: "+e);
	            ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}

        }
	    LOGGER.debug("[GruppiApiServiceImpl::saveGruppi] END");
        return Response.ok(gruppo).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	@Transactional
	public Response updateGruppi(GruppiDTO gruppo,String fruitore,  SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[GruppiApiServiceImpl::updateGruppi] BEGIN");
        try {
		    LOGGER.debug("[GruppiApiServiceImpl::updateGruppi] verificaIdentitaDigitale BEGIN");
		     identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
					httpHeaders, Constants.POST_PUT_DEL_GRUPPI); 
		    LOGGER.debug("[GruppiApiServiceImpl::updateGruppi] verificaIdentitaDigitale END");
			businessLogic.validatorDTO(gruppo, null, null);
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

        Long id = null;
        try {
			setGestAttoreInsUpd(gruppo, fruitore, httpRequest, httpHeaders);
            id = gruppiDAO.updateGruppi(gruppo);
		} catch (BusinessException be) {
			LOGGER.error("[GruppiApiServiceImpl::updateGruppi]: ERROR update");
			return handleBusinessException(400, be);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

		}
        

    	try {
           	LOGGER.debug("[GruppiApiServiceImpl::updateGruppi] BEGIN save tracciamento");
			Identita identita =IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			tracciamentoManager.saveTracciamento(fruitore, gruppo, identita, null, "JSON GRUPPO",
					gruppo.getIdGruppoSoggetto() != null ? gruppo.getIdGruppoSoggetto().toString() : null, "RISCA_T_GRUPPO_SOGGETTO",
					Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, true, httpRequest);
           	LOGGER.debug("[GruppiApiServiceImpl::updateGruppi] END save tracciamento");

		} catch (Exception e) {
            LOGGER.error("[GruppiApiServiceImpl::updateGruppi:: operazione updateLogAudit]: "+e);
            ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

    	LOGGER.debug("[GruppiApiServiceImpl::updateGruppi] END");
        return Response.ok(gruppo).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response deleteGruppi(String fruitore, Long idGruppo, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[GruppiApiServiceImpl::deleteGruppi] BEGIN");
        try {
		    LOGGER.debug("[GruppiApiServiceImpl::deleteGruppi] verificaIdentitaDigitale BEGIN");
		     identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
					httpHeaders, Constants.POST_PUT_DEL_GRUPPI); 
		    LOGGER.debug("[GruppiApiServiceImpl::deleteGruppi] verificaIdentitaDigitale END");
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);

		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} 
        Long id = null;
        
    	try {
    		 GruppiDTO gruppo = gruppiDAO.loadGruppiById(idGruppo != null ? idGruppo.toString() : null , null);
    	      id = gruppiDAO.deleteGruppi(idGruppo, true /* confermato  */);
    	        
           	LOGGER.debug("[GruppiApiServiceImpl::deleteGruppi] BEGIN save tracciamento");
			Identita identita =IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			
			tracciamentoManager.saveTracciamento(fruitore, gruppo, identita, null, "JSON GRUPPO",
					idGruppo != null ? idGruppo.toString() : null , "RISCA_T_GRUPPO_SOGGETTO",
					Constants.FLG_OPERAZIONE_DELETE, Constants.OPERAZIONE_DELETE, true, true, httpRequest);
           	LOGGER.debug("[GruppiApiServiceImpl::deleteGruppi] END save tracciamento");
		} catch (Exception e) {
            LOGGER.error("[GruppiApiServiceImpl::deleteGruppi:: operazione deleteLogAudit]: "+e);
            ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

        //vedere come restituire error in caso sia presente nella tabella riscossione
        return Response.ok(id).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadGruppiByIdAmbitoAndCampoRicerca(String fruitore,Long idAmbito, String campoRicerca, String flgTipoRicerca,
			 Integer offset, Integer limit,String sort,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        LOGGER.debug("[GruppiApiServiceImpl::loadGruppiByIdAmbitoAndCampoRicerca] BEGIN");
        LOGGER.debug("[GruppiApiServiceImpl::loadGruppiByIdAmbitoAndCampoRicerca] Parametri in input idAmbito [" + idAmbito + "] e campoRicerca [" + campoRicerca + "] e flgTipoRicerca [" + flgTipoRicerca + "] ");
        try {
		    LOGGER.debug("[GruppiApiServiceImpl::loadGruppiByIdAmbitoAndCampoRicerca] verificaIdentitaDigitale BEGIN");
		     identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
					httpHeaders, Constants.LOAD_GRUPPI); 
		    LOGGER.debug("[GruppiApiServiceImpl::loadGruppiByIdAmbitoAndCampoRicerca] verificaIdentitaDigitale END");
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);

		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} 
		Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);

		Application cod = new Application();
		cod.setId("RISCA");
        UseCase use = new UseCase();
        use.setAppId(cod);
        use.setId("UC_SIPRA");
        Long idAmbitoSess =null;
		if (identita != null) {
			idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
			LOGGER.debug("[GruppiApiServiceImpl::loadGruppiByIdAmbitoAndCampoRicerca] idAmbitoSess:" + idAmbitoSess);
			if (idAmbito != null && idAmbitoSess != null) {
				LOGGER.debug("[GruppiApiServiceImpl::loadGruppiByIdAmbitoAndCampoRicerca] idAmbito:" + idAmbito);
				if (!idAmbito.equals(idAmbitoSess)) {
					ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
					return Response.serverError().entity(err).status(401).build();
				}
			}
		}
        List<GruppiDTO> listGruppi = gruppiDAO.loadGruppiByIdAmbitoAndCampoRicerca(idAmbito, campoRicerca, flgTipoRicerca, offset,  limit, sort);
        Integer numberAllGruppi = gruppiDAO.countGruppiByIdAmbitoAndCampoRicerca(idAmbito, campoRicerca, flgTipoRicerca);
		PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
        paginationHeader.setTotalElements(numberAllGruppi);
	    paginationHeader.setTotalPages((numberAllGruppi / limit) + ((numberAllGruppi % limit) == 0 ? 0 : 1));
	    paginationHeader.setPage(offset);
	    paginationHeader.setPageSize(limit);
	    paginationHeader.setSort(sort);
		JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
		String jsonString =  jsonPaginationHeader.toString();
        LOGGER.debug("[GruppiApiServiceImpl::loadGruppiByIdAmbitoAndCampoRicerca] END");
        return Response.ok(listGruppi).header("PaginationInfo",jsonString ).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
}
