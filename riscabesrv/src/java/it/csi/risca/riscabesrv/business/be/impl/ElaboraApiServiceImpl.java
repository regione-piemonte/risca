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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.ElaboraApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.ElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ParametroElaboraDAO;
import it.csi.risca.riscabesrv.dto.ElaboraDTO;
import it.csi.risca.riscabesrv.dto.ElaboraExtendedDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.PaginationHeaderDTO;
import it.csi.risca.riscabesrv.dto.ParametroElaboraDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.download.DownloadException;
import it.csi.risca.riscabesrv.util.download.DownloadManager;

@Component
public class ElaboraApiServiceImpl extends BaseApiServiceImpl implements ElaboraApi { 

	private final String className = this.getClass().getSimpleName();
	
	private static final String IDENTITY = "identity";

	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	public static final String RAGGRUPPAMENTO_RIS_IDRICHE = "R1";
	public static final String STATO_ELAB_EMRICH = "EMRICH";
	public static final String STATO_ELAB_EMESS = "EMESS";
	public static final String STATO_ELAB_CONFRICH = "CONFRICH";
	public static final String STATO_ELAB_ANNULLA_RICHIESTA = "ANNULLRICH";
	public static final String STATO_ELAB_ATTESA_IUV = "ATTESAIUV";
	public static final String STATO_ELAB_IUV_OK = "IUVOK";
	public static final String STATO_ELAB_TXT_PRONTI= "TXTPRONTI";
	public static final String STATO_ELAB_ATTESA_PDF = "ATTESAPDF";
	public static final String STATO_ELAB_PDF_PRONTI = "PDFPRONTI";
	public static final String STATO_ELAB_PDF_OK = "PDFOK";
	public static final String STATO_ELAB_MAIL_OK = "MAILOK";
	public static final String STATO_ELAB_MAIL_KO = "MAILKO";

	@Autowired
	private ElaboraDAO elaboraDAO;

	@Autowired
	private ParametroElaboraDAO parametroElaboraDAO;

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;

	@Autowired
	private DownloadManager downloadManager;
	
	@Autowired
	private BusinessLogic businessLogic;

    @Autowired
    private TracciamentoManager tracciamentoManager;
    
	@Override
	public Response loadElabora(String fruitore, String idAmbito, List<String> codTipoElabora,
			List<String> codStatoElabora, String dataRichiestaInizio, String dataRichiestaFine, String codFunzionalita,
			Integer offset, Integer limit, String sort, Integer flgVisibile, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[ElaboraApiServiceImpl::loadElabora] BEGIN");

        try {
		    LOGGER.debug("[ElaboraApiServiceImpl::loadElabora] verificaIdentitaDigitale BEGIN");
		    identitaDigitaleManager.verificaIdentitaDigitale(fruitore, Long.parseLong(idAmbito),httpHeaders, Constants.LOAD_ELABO);  
		    LOGGER.debug("[ElaboraApiServiceImpl::loadElabora] verificaIdentitaDigitale END");

        }
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (NumberFormatException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} 

		List<ElaboraDTO> listElabora = elaboraDAO.loadElabora(idAmbito, codTipoElabora, codStatoElabora,
				dataRichiestaInizio, dataRichiestaFine, codFunzionalita, offset, limit, sort, flgVisibile);
		String jsonString = "";
		if (offset != null && limit != null) {
			Integer numberAllElabora = elaboraDAO.countAllElabora(idAmbito, codTipoElabora, codStatoElabora,
					dataRichiestaInizio, dataRichiestaFine, codFunzionalita, flgVisibile);

			PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
			paginationHeader.setTotalElements(numberAllElabora);
			paginationHeader.setTotalPages(
					(numberAllElabora / limit) + ((numberAllElabora % limit) == 0 ? 0 : 1));
			paginationHeader.setPage(offset);
			paginationHeader.setPageSize(limit);
		    paginationHeader.setSort(sort);
			JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
			jsonString = jsonPaginationHeader.toString();
		}

		for (ElaboraDTO elaboraDTO : listElabora) {
			List<ParametroElaboraDTO> parametri = parametroElaboraDAO.loadParametroElaboraByElaboraRaggruppamento(
					String.valueOf(elaboraDTO.getIdElabora()), RAGGRUPPAMENTO_RIS_IDRICHE);
			elaboraDTO.setParametri(parametri);
		}

		ResponseBuilder resp = Response.ok(listElabora);
		if (!StringUtils.isEmpty(jsonString)) {
			resp.header("PaginationInfo", jsonString);
		}
		resp.header(HttpHeaders.CONTENT_ENCODING, IDENTITY);

		LOGGER.debug("[ElaboraApiServiceImpl::loadElabora] END");

		return resp.build();
	}

	@Override
	public Response loadElaboraById(Long idElabora, Boolean download, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[ElaboraApiServiceImpl::loadElaboraById] BEGIN");


		ElaboraDTO elaboraDTO = elaboraDAO.loadElaboraById(idElabora, download);
		List<ParametroElaboraDTO> parametri = parametroElaboraDAO.loadParametroElaboraByElaboraRaggruppamento(
				String.valueOf(elaboraDTO.getIdElabora()), RAGGRUPPAMENTO_RIS_IDRICHE);
		elaboraDTO.setParametri(parametri);

		if (download != null && download == true) {
			// eseguiamo una copia del file nella cartella di download sul server
			// rinominandolo con ID_ELAB + TIMESTAMP in millisecondi
			// restituiamo il path del file nel campo setNomeFileGenerato
			try {
				String filePath = downloadManager.copyFileToDownloadArea(
						elaboraDTO.getIdElabora() + "_" + elaboraDTO.getTipoElabora().getCodTipoElabora(),
						elaboraDTO.getNomeFileGenerato());
				elaboraDTO.setNomeFileGenerato(filePath);
			} catch (DownloadException e) {
				elaboraDTO.setNomeFileGenerato(null);
				LOGGER.error("[ElaboraApiServiceImpl::loadElaboraById] ERROR " +e);
			}
		} else {
			// Oscuriamo il nome file per ragioni di sicurezza
			elaboraDTO.setNomeFileGenerato(null);
		}

		ResponseBuilder resp = Response.ok(elaboraDTO);

		resp.header(HttpHeaders.CONTENT_ENCODING, IDENTITY);

		LOGGER.debug("[ElaboraApiServiceImpl::loadElaboraById] END");

		return resp.build();
	}

	@Override
	public Response saveElabora(ElaboraDTO elabora, String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

	 	LOGGER.debug("[ElaboraApiServiceImpl::saveElabora] BEGIN ");
		try {
			LOGGER.debug("[ElaboraApiServiceImpl::saveElabora] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_ELABO);
			LOGGER.debug("[ElaboraApiServiceImpl::saveElabora] verificaIdentitaDigitale END");

			businessLogic.validatorDTO(elabora, null, null);
			for (ParametroElaboraDTO parametro : elabora.getParametri()) {
				if (parametro != null) {
					businessLogic.validatorDTO(parametro, null, null);
				}
			}
			elaboraDAO.verifyBollOrdinaria(elabora);
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} 
		ElaboraDTO regElab;
		try {
			setGestAttoreInsUpd(elabora, fruitore, httpRequest, httpHeaders);
			regElab = elaboraDAO.saveElabora(elabora);

			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			tracciamentoManager.saveTracciamento(fruitore, regElab, identita, null, "JSON ELABORA",
					regElab.getIdElabora() != null ? regElab.getIdElabora().toString() : null, "RISCA_T_ELABORA",
					Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, true, httpRequest);
			LOGGER.debug("[ElaboraApiServiceImpl::saveElabora] END save tracciamento");
			ErrorDTO error = validateParametri(elabora.getParametri());
			if (error != null) {
				LOGGER.error("[ElaboraApiServiceImpl::saveElabora] ERROR : errore validazione parametri :\n "
						+ elabora.getParametri() + "\n" + error);
				return Response.serverError().entity(error).status(400).build();
			}
			for (ParametroElaboraDTO parametro : elabora.getParametri()) {
				parametro.setIdElabora(elabora.getIdElabora());
				parametroElaboraDAO.saveParametroElabora(parametro);	
		
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error("[ElaboraApiServiceImpl::saveElabora] ERROR : elabora :\n " + elabora + "\n");
			return Response.serverError().status(500).build();
		}
		return Response.ok(regElab).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	@Override
	public Response updateElabora(ElaboraExtendedDTO elabora, String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

	    LOGGER.debug("[ElaboraApiServiceImpl::updateElabora] BEGIN");
        try {
		    LOGGER.debug("[ElaboraApiServiceImpl::saveElabora] verificaIdentitaDigitale BEGIN");
		    identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,httpHeaders, Constants.POST_PUT_DEL_ELABO);  
		    LOGGER.debug("[ElaboraApiServiceImpl::saveElabora] verificaIdentitaDigitale END");
			businessLogic.validatorDTO(elabora, null, null);
			if (elabora.getParametri() != null) {
				for (ParametroElaboraDTO parametro : elabora.getParametri()) {
					if (parametro != null) {
						businessLogic.validatorDTO(parametro, null, null);
					}
				}
			}		
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
        ElaboraExtendedDTO regElab;
		try {
			setGestAttoreInsUpd(elabora, fruitore, httpRequest, httpHeaders);
			ElaboraDTO elaboraDto = elaboraDAO.updateElabora(elabora);
			regElab = elabora;
			regElab.setDataRichiesta(elaboraDto.getDataRichiesta());
			regElab.setGestDataUpd(elaboraDto.getGestDataUpd());
			LOGGER.debug("[ElaboraApiServiceImpl::saveElabora] BEGIN save tracciamento");
			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			tracciamentoManager.saveTracciamento(fruitore, regElab, identita, null, "JSON ELABORA",
					regElab.getIdElabora() != null ? regElab.getIdElabora().toString() : null, "RISCA_T_ELABORA",
					Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, true, httpRequest);

			LOGGER.debug("[ElaboraApiServiceImpl::saveElabora] END save tracciamento");
			ErrorDTO error = validateParametri(elabora.getParametri());
			if (error != null) {
				LOGGER.error("[ElaboraApiServiceImpl::updateElabora] ERROR : errore validazione parametri :\n "
						+ elabora.getParametri() + "\n" + error);
				return Response.serverError().entity(error).status(400).build();
			}
			for (ParametroElaboraDTO parametro : elabora.getParametri()) {
				if (parametro.getIdParametroElabora() != null) {
					parametroElaboraDAO.updateParametroElabora(parametro);					
				} else {
					parametro.setIdElabora(elabora.getIdElabora());
					parametroElaboraDAO.saveParametroElabora(parametro);					
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error("[ElaboraApiServiceImpl::updateElabora] ERROR : elabora :\n " + elabora + "\n");
			return Response.serverError().status(500).build();
		}
		return Response.ok(regElab).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	@Override
	public Response verifyElabora(String idAmbito,  String verifica,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		List<String> statoElab = new ArrayList<String>();
		if (verifica.equals("inserimento")) {
			statoElab.add(STATO_ELAB_EMRICH);
			statoElab.add(STATO_ELAB_EMESS);
			statoElab.add(STATO_ELAB_CONFRICH);
			statoElab.add(STATO_ELAB_ANNULLA_RICHIESTA);
			statoElab.add(STATO_ELAB_ATTESA_IUV);
			statoElab.add(STATO_ELAB_IUV_OK);
			statoElab.add(STATO_ELAB_TXT_PRONTI);
			statoElab.add(STATO_ELAB_ATTESA_PDF);
			statoElab.add(STATO_ELAB_PDF_PRONTI);
			statoElab.add(STATO_ELAB_PDF_OK);
			statoElab.add(STATO_ELAB_MAIL_OK);
			statoElab.add(STATO_ELAB_MAIL_KO);
			
		}else if (verifica.equals("annulla")) {
			statoElab.add(STATO_ELAB_ANNULLA_RICHIESTA);
		} else
			statoElab.add(STATO_ELAB_CONFRICH);
		
		List<Integer>  idTipoElabora = new ArrayList<>();
//		aggiungere manualmente i valori del cod_tipo_elabora 
		idTipoElabora.add(1); // BO -> 1 
		idTipoElabora.add(2); // BS -> 2
		idTipoElabora.add(3); // AB -> 3
		idTipoElabora.add(4); // SP -> 4
		idTipoElabora.add(23); // BG -> 23
		
		List<ElaboraDTO> verifyResult = elaboraDAO.verifyElabora(String.valueOf(idAmbito), idTipoElabora, statoElab);
		return Response.ok(verifyResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private ErrorDTO validateParametri(List<ParametroElaboraDTO> parametri) {
		for (ParametroElaboraDTO parametro : parametri) {
			if (parametro.getChiave() == null || parametro.getValore() == null
					|| parametro.getRaggruppamento() == null) {
				ErrorDTO error = new ErrorDTO("400", "", "Validation error", null, null);
				return error;
			}
		}
		return null;
	}

	@Override
	public Response isNotturnoTurnOn(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			boolean result = businessLogic.isNotturnoTurnOn();
			return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		  }
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	      }
	}

	@Override
	public Response loadElaboraByCF(String codiceFiscale, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
		    identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_ELABO);  

			List<ElaboraDTO> listAnni = elaboraDAO.loadElaboraByCF(codiceFiscale);
			return Response.ok(listAnni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

}
