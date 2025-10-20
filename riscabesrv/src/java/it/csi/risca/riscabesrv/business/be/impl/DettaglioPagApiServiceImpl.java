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

import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.DettaglioPagApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagBilAccDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagDAO;
import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagExtendedDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagListDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.ErrorMessages;

/**
 * The type Dettaglio Pag Api Service Impl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class DettaglioPagApiServiceImpl extends BaseApiServiceImpl implements DettaglioPagApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private DettaglioPagDAO dettaglioPagDAO;
	
	@Autowired
	private DettaglioPagBilAccDAO dettaglioPagBilAccDAO;

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;
	
	@Autowired
	private BusinessLogic businessLogic;
	

	@Override
	public Response getDettaglioPagByIdRiscossioneAndIdSD(String fruitore, Long idRiscossione, Long idStatoDebitorio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] BEGIN");
		try {
			LOGGER.debug("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_DET_PAG);
			LOGGER.debug("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] verificaIdentitaDigitale END");
            List<DettaglioPagExtendedDTO> listDettaglioPag = dettaglioPagDAO.getDettaglioPagByIdRiscossioneAndIdSD(idRiscossione, idStatoDebitorio);
			LOGGER.debug("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] END");
			return Response.ok(listDettaglioPag).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (DAOException e) {
			LOGGER.error("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] ERROR ", e);
			if (e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOGGER.debug("!!!CODE 1 chiave duplicata!!!!");
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), ErrorMessages.CODE_1_CHIAVE_DUPLICATA,
						ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA, null, null);
				return Response.serverError().entity(err.getTitle()).build();
			}
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (Exception e) {
			LOGGER.error("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] ERROR " , e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
	}

	@Override
	public Response deleteDettaglioPagByIdDettaglioPag(String fruitore, Long idDettaglioPag,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[DettaglioPagApiServiceImpl::deleteDettaglioPagByIdDettaglioPagD] BEGIN");
		try {
			LOGGER.debug("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,Constants.POST_PUT_DEL_DET_PAG);
			LOGGER.debug("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] verificaIdentitaDigitale END");
			dettaglioPagBilAccDAO.deleteDettaglioPagBilAccByIdDettaglioPag(idDettaglioPag);
			idDettaglioPag = dettaglioPagDAO.deleteDettaglioPagById(idDettaglioPag);
			LOGGER.debug("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] END");
			return Response.ok(idDettaglioPag).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (DAOException e) {
			LOGGER.error("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] ERROR " , e);
			if (e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOGGER.debug("!!!CODE 1 chiave duplicata!!!!");

				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), ErrorMessages.CODE_1_CHIAVE_DUPLICATA,
						ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA, null, null);
				return Response.serverError().entity(err.getTitle()).build();
			}
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (Exception e) {
			LOGGER.error("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] ERROR " , e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
	}

	@Override
	public Response saveDettaglioPag(String fruitore, DettaglioPagDTO dettaglioPag,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[DettaglioPagApiServiceImpl::saveDettaglioPag] BEGIN");
		try {
			LOGGER.debug("[DettaglioPagApiServiceImpl::saveDettaglioPag] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,Constants.POST_PUT_DEL_DET_PAG);
			LOGGER.debug("[DettaglioPagApiServiceImpl::saveDettaglioPag] verificaIdentitaDigitale END");
			setGestAttoreInsUpd(dettaglioPag, fruitore, httpRequest, httpHeaders);
		    businessLogic.validatorDTO(dettaglioPag, null, null);
			dettaglioPag = dettaglioPagDAO.saveDettaglioPag(dettaglioPag);
			LOGGER.debug("[DettaglioPagApiServiceImpl::saveDettaglioPag] END");
			return Response.ok(dettaglioPag).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		} catch (ValidationException  ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:"+ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003,ve.getMessage(), null, null);
			   return Response.status(Response.Status.BAD_REQUEST)
	                   .entity(err)
	                   .build();
	    } catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (DAOException e) {
			LOGGER.error("[DettaglioPagApiServiceImpl::saveDettaglioPag] ERROR " , e);
			if (e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOGGER.debug("!!!CODE 1 chiave duplicata!!!!");

				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), ErrorMessages.CODE_1_CHIAVE_DUPLICATA,
						ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA, null, null);
				return Response.serverError().entity(err.getTitle()).build();
			}
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (Exception e) {
			LOGGER.error("[DettaglioPagApiServiceImpl::saveDettaglioPag] ERROR " , e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
	}

	@Override
	public Response saveDettaglioPagList(String fruitore, DettaglioPagListDTO DettaglioPagList,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[DettaglioPagApiServiceImpl::deleteDettaglioPagByIdDettaglioPagD] BEGIN");
		List<DettaglioPagDTO> listDettaglioPag = new ArrayList<DettaglioPagDTO>();
		try {
			LOGGER.debug("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,Constants.POST_PUT_DEL_DET_PAG);
			LOGGER.debug("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] verificaIdentitaDigitale END");
			businessLogic.saveDettaglioPagList(DettaglioPagList, fruitore, httpHeaders, httpRequest);
			LOGGER.debug("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] END");
			return Response.ok(listDettaglioPag).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		}catch (ValidationException  ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:"+ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003,ve.getMessage(), null, null);
			   return Response.status(Response.Status.BAD_REQUEST)
	                   .entity(err)
	                   .build();
	    }catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		}  catch (Exception e) {
			LOGGER.error("[DettaglioPagApiServiceImpl::getDettaglioPagByIdRiscossioneAndIdSD] ERROR " , e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
	}

	@Override
	public Response updateDettaglioPagAnnullamentoSoris(DettaglioPagDTO dettaglioPag, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[DettaglioPagApiServiceImpl::updateDettaglioPagAnnullamentoSoris] BEGIN");
		try {
			LOGGER.debug("[DettaglioPagApiServiceImpl::updateDettaglioPagAnnullamentoSoris] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,Constants.POST_PUT_DEL_DET_PAG);
			LOGGER.debug("[DettaglioPagApiServiceImpl::updateDettaglioPagAnnullamentoSoris] verificaIdentitaDigitale END");
			setGestAttoreInsUpd(dettaglioPag, fruitore, httpRequest, httpHeaders);
		    businessLogic.validatorDTO(dettaglioPag, null, null);
			dettaglioPag = dettaglioPagDAO.updateDettaglioPagAnnullamentoSoris(dettaglioPag);
			LOGGER.debug("[DettaglioPagApiServiceImpl::updateDettaglioPagAnnullamentoSoris] END");
			return Response.ok(dettaglioPag).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		} catch (ValidationException  ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:"+ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003,ve.getMessage(), null, null);
			   return Response.status(Response.Status.BAD_REQUEST)
	                   .entity(err)
	                   .build();
	    } catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (DAOException e) {
			LOGGER.error("[DettaglioPagApiServiceImpl::updateDettaglioPagAnnullamentoSoris] ERROR " , e);
			if (e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOGGER.debug("!!!CODE 1 chiave duplicata!!!!");

				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), ErrorMessages.CODE_1_CHIAVE_DUPLICATA,
						ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA, null, null);
				return Response.serverError().entity(err.getTitle()).build();
			}
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (Exception e) {
			LOGGER.error("[DettaglioPagApiServiceImpl::updateDettaglioPagAnnullamentoSoris] ERROR " , e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
	}

}
