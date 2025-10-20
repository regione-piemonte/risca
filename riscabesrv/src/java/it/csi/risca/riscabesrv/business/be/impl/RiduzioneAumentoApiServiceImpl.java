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

import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;
import it.csi.risca.riscabesrv.business.be.RiduzioneAumentoApi;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiduzioneAumentoDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.RiduzioneAumentoDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Riduzione Aumento api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class RiduzioneAumentoApiServiceImpl implements RiduzioneAumentoApi {

	private static final String IDENTITY = "identity";
	private static Logger lOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private RiduzioneAumentoDAO riduzioneAumentoDAO;

	@Autowired
	private IrideServiceHelper serviceHelper;

	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadRiduzioneAumento(SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumento] BEGIN");
	        List<RiduzioneAumentoDTO> listRiduzioneAumento;
			try {
				listRiduzioneAumento = riduzioneAumentoDAO.loadRiduzioneAumento();
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			
			}
	        
	        lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumento] END");
	        return Response.ok(listRiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	    /**
	     * @param idAmbito idAmbito
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadRiduzioneAumentoByIdAmbito(Long idAmbito, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdAmbito] BEGIN");
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdAmbito] Parametro in input idAmbito [" + idAmbito + "]");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);

			Application cod = new Application();
			cod.setId("RISCA");
	        UseCase use = new UseCase();
	        use.setAppId(cod);
	        use.setId("UC_SIPRA");
	        Long idAmbitoSess =null;
	        if(identita != null) {
				idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
				lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdAmbito] idAmbitoSess:"+ idAmbitoSess);
				if (idAmbito != null && idAmbitoSess != null) {
					lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdAmbito] idAmbito:" + idAmbito);
					if (!idAmbito.equals(idAmbitoSess)) {
						ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
						return Response.serverError().entity(err).status(401).build();
					}
				}
	        }
	        List<RiduzioneAumentoDTO> listRiduzioneAumento;
			try {
				listRiduzioneAumento = riduzioneAumentoDAO.loadRiduzioneAumentoByIdAmbito(idAmbito);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			        
			}
	       
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdAmbito] END");
	        return Response.ok(listRiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
		
	    /**
	     * @param idTipoUso idTipoUso
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadRiduzioneAumentoByIdTipoUso(Long idTipoUso,SecurityContext securityContext,
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdTipoUso] BEGIN");
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdTipoUso] Parametro in input idTipoUso [" + idTipoUso + "]");
	        List<RiduzioneAumentoDTO> RiduzioneAumento;
			try {
				RiduzioneAumento = riduzioneAumentoDAO.loadRiduzioneAumentoByIdTipoUso(idTipoUso);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			        
			}
	        
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByKeyRiduzioneAumento] END");
	        return Response.ok(RiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
		
	    /**
	     * @param idTipoUso idTipoUso
	     * @param flgRidAum flgRidAum
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum(Long idTipoUso, String flgRidAum,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum] BEGIN");
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum] Parametro in input idTipoUso [" + idTipoUso + "] e flgRidAum [" + flgRidAum + "]");
	        List<RiduzioneAumentoDTO> RiduzioneAumento;
			try {
				RiduzioneAumento = riduzioneAumentoDAO.loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum(idTipoUso, flgRidAum);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			        
			}

			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum] END");
	        return Response.ok(RiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	    /**
	     * @param idRiduzioneAumento idRiduzioneAumento
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadRiduzioneAumentoByIdRiduzioneAumento(Long idRiduzioneAumento, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdRiduzioneAumento] BEGIN");
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdRiduzioneAumento] Parametro in input idRiduzioneAumentoPadre [" + idRiduzioneAumento + "]");
	        RiduzioneAumentoDTO RiduzioneAumento;
			try {
				RiduzioneAumento = riduzioneAumentoDAO.loadRiduzioneAumentoByIdRiduzioneAumento(idRiduzioneAumento);
			}  catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			        
			}
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdRiduzioneAumento] END");
	        return Response.ok(RiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		@Override
		public Response loadRiduzioniByIdOrCodRiduzioneAumento(String idOrCodRiduzioneAumento, String dataIniVal,
				String dataFineVal, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioniByIdOrCodRiduzioneAumento] BEGIN");
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioniByIdOrCodRiduzioneAumento] Parametro in input idOrCodRiduzioneAumento [" + idOrCodRiduzioneAumento + "]");
		        RiduzioneAumentoDTO RiduzioneAumento;
				try {
					RiduzioneAumento = riduzioneAumentoDAO.loadRiduzioniByIdOrCodRiduzioneAumento(idOrCodRiduzioneAumento, dataIniVal, dataFineVal );
				} catch (Exception e) {
					ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
		            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
				        
				}
				lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioniByIdOrCodRiduzioneAumento] END");
		        return Response.ok(RiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		@Override
		public Response loadAumentoByIdOrCodRiduzioneAumento(String idOrCodRiduzioneAumento, String dataIniVal,
				String dataFineVal, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioniByIdOrCodRiduzioneAumento] BEGIN");
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioniByIdOrCodRiduzioneAumento] Parametro in input idOrCodRiduzioneAumento [" + idOrCodRiduzioneAumento + "]");
	         RiduzioneAumentoDTO RiduzioneAumento;
			try {
				RiduzioneAumento = riduzioneAumentoDAO.loadRiduzioniByIdOrCodRiduzioneAumento(idOrCodRiduzioneAumento, dataIniVal, dataFineVal );
			}  catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			        
			}
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioniByIdOrCodRiduzioneAumento] END");
		        return Response.ok(RiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		@Override
		public Response loadRiduzioniByIdOrCodTipoUsoFlgManuale(String idOrCodTipoUso, String flgManuale,
				String dataIniVal, String dataFineVal, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdOrCodTipoUsoFlgManuale] BEGIN");
	         List<RiduzioneAumentoDTO> RiduzioneAumento;
			try {
				RiduzioneAumento = riduzioneAumentoDAO.loadRiduzioniByIdOrCodTipoUsoFlgManuale(idOrCodTipoUso,flgManuale, dataIniVal, dataFineVal );
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			        
			}
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioneAumentoByIdOrCodTipoUsoFlgManuale] END");
	        return Response.ok(RiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		@Override
		public Response loadAumentiByIdOrCodTipoUsoFlgManuale(String idOrCodTipoUso, String flgManuale,
				String dataIniVal, String dataFineVal, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] BEGIN");
	         List<RiduzioneAumentoDTO> RiduzioneAumento;
			try {
				RiduzioneAumento = riduzioneAumentoDAO.loadAumentiByIdOrCodTipoUsoFlgManuale(idOrCodTipoUso,flgManuale, dataIniVal, dataFineVal );
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			        
			}
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] END");
	        return Response.ok(RiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		}

		@Override
		public Response loadRiduzioniByflgManuale(String flgManuale, String dataIniVal, String dataFineVal,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioniByflgManuale] BEGIN");
	         List<RiduzioneAumentoDTO> RiduzioneAumento;
			try {
				RiduzioneAumento = riduzioneAumentoDAO.loadRiduzioniByflgManuale(flgManuale, dataIniVal, dataFineVal );
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			        
			}
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadRiduzioniByflgManuale] END");
	        return Response.ok(RiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		@Override
		public Response loadAumentiByflgManuale(String flgManuale, String dataIniVal, String dataFineVal,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadAumentiByflgManuale] BEGIN");
	         List<RiduzioneAumentoDTO> RiduzioneAumento;
			try {
				RiduzioneAumento = riduzioneAumentoDAO.loadAumentiByflgManuale(flgManuale, dataIniVal, dataFineVal );
			}  catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			        
			}
			lOGGER.debug("[RiduzioneAumentoApiServiceImpl::loadAumentiByflgManuale] END");
	        return Response.ok(RiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
	
}
