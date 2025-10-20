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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.TipoUsoApi;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipo uso api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoUsoApiServiceImpl implements TipoUsoApi {
	
	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private TipoUsoDAO tipoUsoDAO;

	@Autowired
	private IrideServiceHelper serviceHelper;

	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipiUso(SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUso] BEGIN");
	        
	        List<TipoUsoExtendedDTO> listTipoUso;
			try {
				listTipoUso = tipoUsoDAO.loadTipoUso();
			}catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}
	        return Response.ok(listTipoUso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	    /**
	     * @param idAmbito idAmbito
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
//		@Override
//		public Response loadTipiUsoByIdAmbito(Long idAmbito,  String dataIniVal,  String dataFineVal, SecurityContext securityContext, HttpHeaders httpHeaders,
//				HttpServletRequest httpRequest) {
//	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdAmbito] BEGIN");
//	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdAmbito] Parametro in input idAmbito [" + idAmbito + "]");
//	        List<TipoUsoExtendedDTO> listTipoUso = tipoUsoDAO.loadTipoUsoByIdAmbito(idAmbito,dataIniVal,dataFineVal);
//	        if (listTipoUso == null) {
//	            LOGGER.error("[TipoUsoApiServiceImpl::loadTipoUsoByIdAmbito] ERROR : idAmbito [" + idAmbito + "]\n");
//	            LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdAmbito] END");
//	            return Response.serverError().status(500).build();
//	        }
//	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdAmbito] END");
//	        return Response.ok(listTipoUso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
//		}

	    /**
	     * @param idTipoUsoPadre idTipoUsoPadre
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipoUsoByIdTipoUsoPadre(String idTipoUsoPadre, SecurityContext securityContext,
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdTipoUsoPadre] BEGIN");
	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdTipoUsoPadre] Parametro in input idTipoUsoPadre [" + idTipoUsoPadre + "]");
	        List<TipoUsoExtendedDTO> listTipoUso;
			try {
				listTipoUso = tipoUsoDAO.loadTipoUsoByIdTipoUsoPadre(idTipoUsoPadre);
			}catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}

	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdTipoUsoPadre] END");
	        return Response.ok(listTipoUso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	    /**
	     * @param idTipoUso idTipoUso
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipoUsoByIdTipoUsoOrCodTipoUso(String idTipoUso, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] BEGIN");
	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] Parametro in input idTipoUsoPadre [" + idTipoUso + "]");
	        TipoUsoDTO TipoUso;
			try {
				TipoUso = tipoUsoDAO.loadTipoUsoByIdTipoUsoOrCodTipoUso(idTipoUso);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}

	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] END");
	        return Response.ok(TipoUso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
		
		
	    /**
	     * @param codTipoUso codTipoUso
	     * @param idAmbito idAmbito
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipoUsoByCodeAndIdAmbito(String codTipoUso, Long idAmbito, SecurityContext securityContext,
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByCodeAndIdAmbito] BEGIN");
	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByCodeAndIdAmbito] Parametro in input codTipoUso [" + codTipoUso + "] e idAmbito [" + idAmbito + "]");
	        TipoUsoDTO TipoUso;
			try {
				TipoUso = tipoUsoDAO.loadTipoUsoByCodeAndIdAmbito(codTipoUso, idAmbito);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}
	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByCodeAndIdAmbito] END");
	        return Response.ok(TipoUso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	    /**
	     * @param idTipoUsoPadre idTipoUsoPadre
	     * @param idAmbito idAmbito
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipoUsoByIdTipoUsoPadreAndIdAmbito(String idTipoUsoPadre, Long idAmbito,  String dataIniVal,  String dataFineVal,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdTipoUsoPadreAndIdAmbito] BEGIN");
	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdTipoUsoPadreAndIdAmbito] Parametro in input codTipoUso [" + idTipoUsoPadre + "] e idAmbito [" + idAmbito + "]");
	        List<TipoUsoExtendedDTO> listTipoUso;
			try {
				listTipoUso = tipoUsoDAO.loadTipoUsoByIdTipoUsoPadreAndIdAmbito(idTipoUsoPadre, idAmbito , dataIniVal,  dataFineVal);
			}catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}
	        LOGGER.debug("[TipoUsoApiServiceImpl::loadTipoUsoByIdTipoUsoPadreAndIdAmbito] END");
	        return Response.ok(listTipoUso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	
}
