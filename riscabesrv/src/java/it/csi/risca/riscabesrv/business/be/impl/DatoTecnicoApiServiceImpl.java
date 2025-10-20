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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.DatoTecnicoApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.DatoTecnicoDAO;
import it.csi.risca.riscabesrv.dto.DTGrandeIdroDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.ambiente.RiscossioneDatoTecnicoDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type dato tecnico api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class DatoTecnicoApiServiceImpl extends BaseApiServiceImpl  implements DatoTecnicoApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();
	
    @Autowired
    private DatoTecnicoDAO datoTecnicoDAO;
    
    
    @Autowired
    private IdentitaDigitaleManager identitaDigitaleManager; 
    
    @Autowired
	public BusinessLogic businessLogic;
    
    @Autowired
    private TracciamentoManager tracciamentoManager;
    
	@Override
	public Response saveDatoTecnico(RiscossioneDatoTecnicoDTO datoTecnico, String fruitore,  SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
		LOGGER.debug("[DatoTecnicoApiServiceImpl::saveDatoTecnico] BEGIN");

        try {
		    LOGGER.debug("[DatoTecnicoApiServiceImpl::saveDatoTecnico] verificaIdentitaDigitale BEGIN");
		    identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,httpHeaders, Constants.POST_PUT_DEL_DT);  
		    LOGGER.debug("[DatoTecnicoApiServiceImpl::saveDatoTecnico] verificaIdentitaDigitale END");
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} 
		LOGGER.debug("[RiscossioneApiServiceImpl::saveDatoTecnico] get IdentitaDigitale END");
		Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
	    LOGGER.debug("[RiscossioneApiServiceImpl::saveDatoTecnico] get IdentitaDigitaler END");
	    String cfIdentita = identita != null? identita.getCodFiscale() : fruitore;
	    Utils util = new Utils();
	    if(util.isLocalMod() && fruitore == null){
	    	cfIdentita = "localhost";
	    }
	    
	  //DP setto il cfIdentita con il valore settato per il batch porting (in questo caso non essendoci sul dto la properties gestAttoreIns lo setto a mano)
		if(cfIdentita.equals(Constants.BATCH_PORTING)) {
			cfIdentita = Constants.MIGRAZIONE;
		}
        RiscossioneDatoTecnicoDTO newDatoTecnico = null;
        

		Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
		try {
			newDatoTecnico = datoTecnicoDAO.saveDatoTecnico(datoTecnico, cfIdentita, idAmbito);

		} catch (ValidationException  ve) {
					LOGGER.debug("BAD REQUEST: MESSAGE:"+ve.getMessage());
					Integer status = Response.Status.BAD_REQUEST.getStatusCode();
					ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003,ve.getMessage(), null, null);
					   return Response.status(Response.Status.BAD_REQUEST)
			                   .entity(err)
			                   .build();
		} catch (BusinessException be) {
            LOGGER.error("[DatoTecnicoApiServiceImpl::saveDatoTecnico] ERROR : " , be);
			return handleBusinessException(400, be);
		} catch (Exception e1) {
            LOGGER.error("[DatoTecnicoApiServiceImpl::saveDatoTecnico] ERROR : " , e1);
        	ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

        try {	
        	LOGGER.debug("[DatoTecnicoApiServiceImpl::saveDatoTecnico] BEGIN save tracciamento");

			
			String dataIns = newDatoTecnico.getRiscossione().getDataInserimento(); 
			String dataUpd = newDatoTecnico.getRiscossione().getDataModifica();
			
		    if (dataIns.equals(dataUpd)) {
				tracciamentoManager.saveTracciamento(cfIdentita, newDatoTecnico, identita, newDatoTecnico.getRiscossione().getIdRiscossione(), "JSON DATI TECNICI",
						newDatoTecnico.getRiscossione().getIdRiscossione() != null ? newDatoTecnico.getRiscossione().getIdRiscossione().toString() : null, "RISCA_T_RISCOSSIONE.JSON_DATI_TECNICI",
						Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, false, true, httpRequest);				
		     }
		    else {
				tracciamentoManager.saveTracciamento(cfIdentita, newDatoTecnico, identita, newDatoTecnico.getRiscossione().getIdRiscossione(), "JSON DATI TECNICI",
						newDatoTecnico.getRiscossione().getIdRiscossione() != null ? newDatoTecnico.getRiscossione().getIdRiscossione().toString() : null, "RISCA_T_RISCOSSIONE.JSON_DATI_TECNICI",
						Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, false, true, httpRequest);
		    }

			LOGGER.debug("[DatoTecnicoApiServiceImpl::saveDatoTecnico] END save tracciamento");
		} catch (Exception e) {
            LOGGER.error("[DatoTecnicoApiServiceImpl::saveDatoTecnico] ERROR : " , e);
        	ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

        LOGGER.debug("[DatoTecnicoApiServiceImpl::saveDatoTecnico] END");
        return Response.ok(newDatoTecnico).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}


	@Override
	public Response updateDTGrandeIdro(String fruitore, Integer idRiscossione, DTGrandeIdroDTO dTGrandeIdroDTO,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        try {
		    identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_DT);  
	        RiscossioneDatoTecnicoDTO datoTecnico = datoTecnicoDAO.updateDTGrandeIdro(idRiscossione, dTGrandeIdroDTO, httpHeaders, httpRequest);
	        return Response.ok(datoTecnico).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		}catch (Exception e) {
	        LOGGER.debug(getClassFunctionErrorInfo(className, methodName,e));
        	ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}finally {
	        LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		
	}

}
