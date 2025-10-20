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
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.PagamentoBilAccApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagBilAccDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.dto.BilAccResultDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class PagamentoBilAccApiServiceImpl extends BaseApiServiceImpl implements PagamentoBilAccApi {

	private static final String IDENTITY = "identity";

	@Autowired
	private PagamentoDAO pagamentoDAO;

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;

	@Autowired
	private RataSdDAO rataSdDAO;

	@Autowired
	private DettaglioPagBilAccDAO dettaglioPagBilAccDAO;

	@Autowired
	private DettaglioPagDAO dettaglioPagDAO;

	@Autowired
	private PagamentoBilAccLogic pagamentoBilAccLogic;

	@Override
	public Response getListaIdPagamentiByAnnoDataRif(String anno, String dataRif, List<Long> idPagamento,
			String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoBilAccApiServiceImpl::getListaIdPagamentiByAnnoDataRif] BEGIN");
		List<Long> listaPag = new ArrayList<Long>();
		try {
			LOGGER.debug(
					"[PagamentoBilAccApiServiceImpl::getListaIdPagamentiByAnnoDataRif] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_PAGA);
			LOGGER.debug(
					"[PagamentoBilAccApiServiceImpl::getPagamentiByIdStatoDebitorio] verificaIdentitaDigitale END");
			listaPag = pagamentoDAO.getListaIdPagamentiByAnnoDataRif(anno, dataRif, idPagamento);
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.debug("[PagamentoBilAccApiServiceImpl::getListaIdPagamentiByAnnoDataRif] ERROR: Exception - END", e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[PagamentoBilAccApiServiceImpl::getListaIdPagamentiByAnnoDataRif] END");
		return Response.ok(listaPag).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveDettaglioPagamentoBilAcc(String fruitore, Long idPagamento, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoBilAccApiServiceImpl::saveDettaglioPagamentoBilAcc] BEGIN");
		BilAccResultDTO result = new BilAccResultDTO();
		String attore = null;
		try {
			LOGGER.debug(
					"[PagamentoBilAccApiServiceImpl::saveDettaglioPagamentoBilAcc] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,
					Constants.POST_DETT_PAG_BIL_ACC);
			LOGGER.debug("[PagamentoBilAccApiServiceImpl::saveDettaglioPagamentoBilAcc] verificaIdentitaDigitale END");

			if (fruitore != null) {
				attore = fruitore;
			} else {
				Identita identita = IdentitaDigitaleManager
						.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), null);
				attore = identita.getCodFiscale();
			}

			result = pagamentoBilAccLogic.saveDettaglioPagamentoBilAcc(attore, idPagamento);

			if (result.getStatus().equals("KO")) {
				return Response.serverError().entity(result).status(500).build();
			}

			// Fine elaborazione del pagamento
			result.setStatus("OK");
			result.setMessage("Pagamento elaborato con successo");

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("[PagamentoBilAccApiServiceImpl::saveDettaglioPagamentoBilAcc] ERROR: Exception - END - "
					+ e.getMessage());
			result.setStatus("KO");
			result.setMessage(e.getMessage());
			return Response.serverError().entity(result).status(500).build();
		}

		LOGGER.debug("[PagamentoBilAccApiServiceImpl::saveDettaglioPagamentoBilAcc] END");
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response deleteDettaglioPagamentoBilAcc(String fruitore, Long idPagamento, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoBilAccApiServiceImpl::deleteDettaglioPagamentoBilAcc] BEGIN");
		BilAccResultDTO result = new BilAccResultDTO();
		int totRet = 0;
		try {
			LOGGER.debug(
					"[PagamentoBilAccApiServiceImpl::deleteDettaglioPagamentoBilAcc] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,
					Constants.DELETE_DETT_PAG_BIL_ACC);
			LOGGER.debug(
					"[PagamentoBilAccApiServiceImpl::deleteDettaglioPagamentoBilAcc] verificaIdentitaDigitale END");

			PagamentoExtendedDTO pagamento = pagamentoDAO.getPagamentoByIdPagamento(idPagamento);
			if (pagamento == null) {
				return getErrorResponse("[PagamentoBilAccApiServiceImpl::deleteDettaglioPagamentoBilAcc]",
						"Pagamento non trovato - idPagamento = " + idPagamento);
			}
			result.setIdPagamento(idPagamento);

			List<DettaglioPagDTO> dettagliPag = pagamento.getDettaglioPag();
			if (dettagliPag == null || dettagliPag.size() == 0) {
				return getErrorResponse("[PagamentoBilAccApiServiceImpl::deleteDettaglioPagamentoBilAcc]",
						"Pagamento senza dettaglio - idPagamento = " + idPagamento);
			}

			int ret = dettaglioPagBilAccDAO.deleteDettaglioPagBilAccByIdPagamento(idPagamento);
			totRet = totRet + ret;
			for (DettaglioPagDTO dettaglioPag : dettagliPag) {
				// Dati della rata dello stato debitorio
				RataSdDTO rataSd = rataSdDAO.loadRataSdById(dettaglioPag.getIdRataSd());

				// devo cancellare anche eventuali record in risca_r_dettaglio_pag_bil acc che
				// si riferiscano alla stessa rata_sd pur avendo id_dettaglio_pag diverso
				// Vedi Algoritmo 02 - passo 2: Verificare su risca_r_dettaglio_pag_bil_acc
				// eventuali record associati, ovvero se esistono altri record con
				// id_dettaglio_pag, diverso da quello su cui si sta lavorando, associato allo
				// stesso id_rata_sd
				ret = dettaglioPagBilAccDAO.deleteDettaglioPagBilAccByIdRataSd(rataSd.getIdRataSd());
				totRet = totRet + ret;
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("[PagamentoBilAccApiServiceImpl::deleteDettaglioPagamentoBilAcc] ERROR: Exception - END - "
					+ e.getMessage());
			result.setStatus("KO");
			result.setMessage(e.getMessage());
			return Response.serverError().entity(result).status(500).build();
		}
		result.setStatus("OK");
		result.setMessage(
				"Dettagli di pagamento per accertamento bilancio cancellati con successo (" + totRet + " cancellati)");
		LOGGER.debug("[PagamentoBilAccApiServiceImpl::deleteDettaglioPagamentoBilAcc] END");
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/*@Override
	public Response deleteDettagliPagamentoBilAcc(String fruitore, List<Long> listaIdPagamento,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoBilAccApiServiceImpl::deleteDettagliPagamentoBilAcc] BEGIN");
		BilAccResultDTO result = new BilAccResultDTO();
		int totRet = 0;
		try {
			LOGGER.debug(
					"[PagamentoBilAccApiServiceImpl::deleteDettagliPagamentoBilAcc] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,
					Constants.DELETE_DETT_PAG_BIL_ACC);
			LOGGER.debug("[PagamentoBilAccApiServiceImpl::deleteDettagliPagamentoBilAcc] verificaIdentitaDigitale END");

			// devo cancellare anche eventuali record in risca_r_dettaglio_pag_bil acc che
			// si riferiscano alla stessa rata_sd pur avendo id_dettaglio_pag diverso
			// Vedi Algoritmo 02 - passo 2: Verificare su risca_r_dettaglio_pag_bil_acc
			// eventuali record associati, ovvero se esistono altri record con
			// id_dettaglio_pag, diverso da quello su cui si sta lavorando, associato allo
			// stesso id_rata_sd

			// Quindi prima di cancellare i dettagli su risca_r_dettaglio_pag_bil_acc devo
			// recuperar la lista dei dettagliPag da cui estrarre gli idRataSd
			List<DettaglioPagDTO> dettagliPag = dettaglioPagDAO.getDettagliPagByListaIdPag(listaIdPagamento);
			List<Long> listaIdRataSd = dettagliPag.stream().map(dto -> dto.getIdRataSd()).distinct()
					.collect(Collectors.toList());
			
			// Determino gli idPagamento che corrispondono ai dettagli che poi andro' a cancellare dalla risca_r_dettaglio_pag_bil
			// in modo da poterli poi inserire nuovamente (restituisco la lista nel result)
			List<DettaglioPagDTO> altriDettagliPagStessaRata = dettaglioPagDAO.getDettagliPagByListaIdRata(listaIdRataSd);
			List<Long> altriIdPagStessaRata = altriDettagliPagStessaRata.stream().map(dto -> dto.getIdPagamento()).distinct()
					.collect(Collectors.toList());
			result.setListaIdPagDaRielaborare(altriIdPagStessaRata);
			
			int ret1 = dettaglioPagBilAccDAO.deleteDettaglioPagBilAccByListaIdRataSd(listaIdRataSd);

			// TODO verificare --> probabilmente questa seconda delete non serve percha' i
			// dettagli che cancella sono gia' inclusi nella delete precedente

			// Adesso posso cancellare i dettagli su risca_r_dettaglio_pag_bil_acc in base
			// alla lista di id_pagamento
			int ret2 = dettaglioPagBilAccDAO.deleteDettaglioPagBilAccByListaIdPagamento(listaIdPagamento);
			totRet = ret1 + ret2;

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("[PagamentoBilAccApiServiceImpl::deleteDettagliPagamentoBilAcc] ERROR: Exception - END - "
					+ e.getMessage());
			result.setStatus("KO");
			result.setMessage(e.getMessage());
			return Response.serverError().entity(result).status(500).build();
		}
		result.setStatus("OK");
		result.setMessage(
				"Dettagli di pagamento per accertamento bilancio cancellati con successo (" + totRet + " cancellati)");
		LOGGER.debug("[PagamentoBilAccApiServiceImpl::deleteDettagliPagamentoBilAcc] END");
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}*/
	
	@Override
	public Response deleteDettagliPagamentoBilAcc(String fruitore, List<Long> listaIdPagamento,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[PagamentoBilAccApiServiceImpl::deleteDettagliPagamentoBilAcc] BEGIN");
		BilAccResultDTO result = new BilAccResultDTO();
		int totRet = 0;
		try {
			LOGGER.debug(
					"[PagamentoBilAccApiServiceImpl::deleteDettagliPagamentoBilAcc] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,
					Constants.DELETE_DETT_PAG_BIL_ACC);
			LOGGER.debug("[PagamentoBilAccApiServiceImpl::deleteDettagliPagamentoBilAcc] verificaIdentitaDigitale END");

			dettaglioPagBilAccDAO.deleteDettaglioPagBilAccByListaIdPagamento(listaIdPagamento);

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("[PagamentoBilAccApiServiceImpl::deleteDettagliPagamentoBilAcc] ERROR: Exception - END - "
					+ e.getMessage());
			result.setStatus("KO");
			result.setMessage(e.getMessage());
			return Response.serverError().entity(result).status(500).build();
		}
		result.setStatus("OK");
		result.setMessage(
				"Dettagli di pagamento per accertamento bilancio cancellati con successo (" + totRet + " cancellati)");
		LOGGER.debug("[PagamentoBilAccApiServiceImpl::deleteDettagliPagamentoBilAcc] END");
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private Response getErrorResponse(String label, String msg) {
		LOGGER.error(label + msg);
		BilAccResultDTO result = new BilAccResultDTO();
		result.setStatus("KO");
		result.setMessage(msg);
		return Response.serverError().entity(result).status(500).build();
	}

}
