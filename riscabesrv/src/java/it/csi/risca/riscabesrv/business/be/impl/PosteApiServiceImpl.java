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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.PosteApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.FilePosteDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RpEstrcoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RpNonPremarcatiDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.FilePosteDTO;
import it.csi.risca.riscabesrv.dto.RpEstrcoDTO;
import it.csi.risca.riscabesrv.dto.RpNonPremarcatiDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type IUV api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class PosteApiServiceImpl extends BaseApiServiceImpl implements PosteApi {

	private static final String IDENTITY = "identity";
	// private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private FilePosteDAO filePosteDAO;

	@Autowired
	private RpNonPremarcatiDAO rpNonPremarcatiDAO;

	@Autowired
	private RpEstrcoDAO rpEstrcoDAO;

	@Autowired
	private ElaborazionePagamenti elaborazionePagamenti;

	@Autowired
	public BusinessLogic businessLogic;	
	
	@Override
	public Response saveFilePoste(FilePosteDTO filePoste, String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
		setGestAttoreInsUpd(filePoste, fruitore, httpRequest, httpHeaders);
		businessLogic.validatorDTO(filePoste, null, null);
		FilePosteDTO dto = filePosteDAO.saveFilePoste(filePoste);
		return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		
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
	}

	@Override
	public Response saveEstrco(RpEstrcoDTO rpEstrco, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(rpEstrco, null, null);
			RpEstrcoDTO dto = rpEstrcoDAO.saveRpEstrco(rpEstrco);
			return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
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
	}

	@Override
	public Response saveNonPremarcato(RpNonPremarcatiDTO rpNonPremarcato, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(rpNonPremarcato, null, null);
			RpNonPremarcatiDTO dto = rpNonPremarcatiDAO.saveRpNonPremarcati(rpNonPremarcato);
			return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
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
	}

	@Override
	public Response getNonPremarcati(Long idElabora, Long idFilePoste, List<String> tipoDoc,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<RpNonPremarcatiDTO> listaRp = rpNonPremarcatiDAO.loadRpNonPremarcatiByElaboraTipoDoc(idElabora,
				idFilePoste, tipoDoc);
		return Response.ok(listaRp).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		
	}

	@Override
	public Response getEstrco(Long idElabora, Long idFilePoste, String numeroConto, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<RpEstrcoDTO> listaRp = rpEstrcoDAO.loadRpEstrcoByElaboraNumeroConto(idElabora, idFilePoste, numeroConto);
		return Response.ok(listaRp).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response inserisciPagamentoNonPrem(RpNonPremarcatiDTO rpNonPremarcato, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(rpNonPremarcato, null, null);
			boolean result = elaborazionePagamenti.elaboraPagamentoNonPremarcato(rpNonPremarcato);
			return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
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
	}

	@Override
	public Response inserisciPagamentoEstrco(RpEstrcoDTO rpEstrco, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(rpEstrco, null, null);
			boolean result = elaborazionePagamenti.elaboraPagamentoEstrco(rpEstrco);
			return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
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
	}

	@Override
	public Response deleteEstrco(Long idElabora, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Integer result = rpEstrcoDAO.deleteRpEstrco(idElabora);
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response deleteNonPremarcati(Long idElabora, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Integer result = rpNonPremarcatiDAO.deleteRpNonPremarcati(idElabora);
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
