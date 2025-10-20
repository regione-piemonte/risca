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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.AccertamentoApi;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AccertamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDatiAmministrDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDatiPagopaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDatiTitolareDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDettVersDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.dto.SollDatiTitolareDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Accertamento api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class AccertamentoApiServiceImpl extends BaseApiServiceImpl implements AccertamentoApi {

	private static final String IDENTITY = "identity";

	@Autowired
	private AccertamentoDAO accertamentoDAO;

	@Autowired
	private SollDatiTitolareDAO sollDatiTitolareDAO;

	@Autowired
	private SollDatiAmministrDAO sollDatiAmministrDAO;

	@Autowired
	private SollDatiPagopaDAO sollDatiPagopaDAO;

	@Autowired
	private SollDettVersDAO sollDettVersDAO;

	@Autowired
	private SoggettiDAO soggettiDAO;

	@Autowired
	private StatoDebitorioDAO statoDebitorioDAO;

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;

	@Autowired
	private BusinessLogic businessLogic;

	@Autowired
	private TracciamentoManager tracciamentoManager;

	private static final String Codice_Fiscale_Eti_Calc_Dest = "Cod.Fisc./P.Iva";
	private static final String DATE_FORMAT_SPED = "dd/MM/yyyy";

	@Override
	@Transactional
	public Response saveAccertamenti(AccertamentoExtendedDTO accertamento, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException, BusinessException {
		LOGGER.debug("[AccertamentoApiServiceImpl::saveAccertamenti] BEGIN");
		LOGGER.debug(
				"[AccertamentoApiServiceImpl::saveAccertamenti] Parametro in input accertamento :\n " + accertamento);
		try {
			setGestAttoreInsUpd(accertamento, fruitore, httpRequest, httpHeaders);

			LOGGER.debug("[AccertamentoApiServiceImpl::saveAccertamenti] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_ACCER);
			LOGGER.debug("[AccertamentoApiServiceImpl::saveAccertamenti] verificaIdentitaDigitale END");

			businessLogic.validatorDTO(accertamento, null, null);

			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);

			accertamento = accertamentoDAO.saveAccertamenti(accertamento);

			tracciamentoManager.saveTracciamento(fruitore, accertamento, identita, null, "JSON accertamento",
					accertamento.getIdAccertamento() != null ? accertamento.getIdAccertamento().toString() : null,
					"RISCA_T_ACCERTAMENTO", Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, true,
					httpRequest);

			// TODO se tipo accertamento vale 4,5,6 occorre inserire una riga su
			// r_soll_dati_titolare
			// 4 ABM Avviso bonario manuale
			// 5 SPM Sollecito di pagamento manuale
			// 6 RPA Rateizzazione pagamento
			if (accertamento.getTipoAccertamento().getCodTipoAccertamento().equals("ABM")
					|| accertamento.getTipoAccertamento().getCodTipoAccertamento().equals("SPM")
					|| accertamento.getTipoAccertamento().getCodTipoAccertamento().equals("RPA")) {
				SollDatiTitolareDTO sollDatiTit = saveSollDatiTitolare(accertamento);

				tracciamentoManager.saveTracciamento(fruitore, sollDatiTit, identita, null, "JSON SOLL_DATI_TITOLARE",
						sollDatiTit.getIdSollDatiTitolare() != null ? sollDatiTit.getIdSollDatiTitolare().toString()
								: null,
						"RISCA_R_SOLL_DATI_TITOLARE", Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT,
						true, true, httpRequest);
			}

		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}

		return Response.ok(accertamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private SollDatiTitolareDTO saveSollDatiTitolare(AccertamentoExtendedDTO accertamento) throws SystemException {
		SollDatiTitolareDTO dto = null;
		try {
			Long idStatoDebitorio = accertamento.getIdStatoDebitorio();
			StatoDebitorioDTO sd = statoDebitorioDAO.loadStatoDebitorioLightById(idStatoDebitorio);
			SoggettiExtendedDTO soggetto = soggettiDAO.loadSoggettoById(sd.getIdSoggetto());

			RecapitiExtendedDTO recPrinc = soggetto.getRecapiti().stream()
					.filter(r -> r.getTipoRecapito().getCodTipoRecapito().equals("P")).collect(Collectors.toList())
					.get(0);

			IndirizzoSpedizioneDTO indSped = getIndirizzoSpedizione(recPrinc, sd.getIdGruppoSoggetto());

			dto = new SollDatiTitolareDTO();
			dto.setIdAccertamento(accertamento.getIdAccertamento());
			dto.setNomeTitolareIndPost(formatString50(indSped.getDestinatarioPostel()));
			dto.setCodiceFiscaleCalc(soggetto.getCfSoggetto());
			dto.setCodiceFiscaleEtiCalc(Codice_Fiscale_Eti_Calc_Dest);
			if (accertamento.getTipoAccertamento().getCodTipoAccertamento().equals("SPM")) {
				if (indSped != null) {
					dto.setProvIndPost(indSped.getProvinciaPostel());
					dto.setPressoIndPost(indSped.getPressoPostel());
					dto.setIndirizzoIndPost(indSped.getIndirizzoPostel() + " " + indSped.getCapPostel());
					dto.setComuneIndPost(indSped.getCittaPostel());
				}
				dto.setIdTipoInvio(
						recPrinc != null && recPrinc.getTipoInvio() != null ? recPrinc.getTipoInvio().getIdTipoInvio()
								: null);
			}
			dto.setNumProt(null);
			dto.setDataProt(accertamento.getDataProtocollo());
			SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_SPED);
			dto.setScadenzaSoll(
					accertamento.getDataScadenza() != null ? df.format(accertamento.getDataScadenza()) : null);
			dto.setIdSoggetto(soggetto.getIdSoggetto());
			dto.setGestAttoreIns(accertamento.getGestAttoreIns());
			dto.setGestAttoreUpd(accertamento.getGestAttoreUpd());

			dto = sollDatiTitolareDAO.saveSollDatiTitolare(dto);
		} catch (Exception e) {
			LOGGER.error("[AccertamentoApiServiceImpl::saveAccertamenti] Errore inserimento SollDatiTitolare: "
					+ e.getMessage());
			throw new SystemException("Errore inserimento SollDatiTitolare");
		}

		return dto;
	}

	private IndirizzoSpedizioneDTO getIndirizzoSpedizione(RecapitiExtendedDTO recapito, Long idGruppoSoggetto) {
		IndirizzoSpedizioneDTO indSped = null;
		if (recapito.getIndirizziSpedizione() != null && recapito.getIndirizziSpedizione().size() > 0) {
			if (idGruppoSoggetto == null) {
				for (IndirizzoSpedizioneDTO ind : recapito.getIndirizziSpedizione()) {
					if (ind.getIdGruppoSoggetto() == null && ind.getIndValidoPostel().equals(1l)) {
						indSped = ind;
					}
				}
			} else {
				for (IndirizzoSpedizioneDTO ind : recapito.getIndirizziSpedizione()) {
					if (ind.getIdGruppoSoggetto() != null && ind.getIdGruppoSoggetto().equals(idGruppoSoggetto)
							&& ind.getIndValidoPostel().equals(1l)) {
						indSped = ind;
					}
				}
			}
		}
		return indSped;
	}

	private String formatString50(String value) {
		if (value == null) {
			return "";
		} else if (value.trim().length() > 50) {
			return value.trim().substring(0, 50);
		} else {
			return value;
		}
	}

	@Override
	@Transactional
	public Response updateAccertamenti(AccertamentoExtendedDTO accertamento, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException {

		LOGGER.debug("[AccertamentoApiServiceImpl::updateAccertamenti] BEGIN");
		LOGGER.debug(
				"[AccertamentoApiServiceImpl::updateAccertamenti] Parametro in input accertamento :\n " + accertamento);
		try {
			setGestAttoreInsUpd(accertamento, fruitore, httpRequest, httpHeaders);

			LOGGER.debug("[AccertamentoApiServiceImpl::updateAccertamenti] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_ACCER);
			LOGGER.debug("[AccertamentoApiServiceImpl::updateAccertamenti] verificaIdentitaDigitale END");
			businessLogic.validatorDTO(accertamento, null, null);

			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);

			accertamentoDAO.updateAccertamenti(accertamento);

			tracciamentoManager.saveTracciamento(fruitore, accertamento, identita, null, "JSON accertamento",
					accertamento.getIdAccertamento() != null ? accertamento.getIdAccertamento().toString() : null,
					"RISCA_T_ACCERTAMENTO", Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, true,
					httpRequest);

		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error("[AccertamentoApiServiceImpl::updateAccertamenti:: EXCEPTION ]: " + e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

		return Response.ok(accertamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	@Override
	public Response loadAllAccertamentiOrByIdStatoDeb(String fruitore, Long idStatoDebitorio, Integer offset,
			Integer limit, String sort, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[AccertamentoApiServiceImpl::loadAllAccertamentiOrByIdStatoDeb] BEGIN");
		List<AccertamentoExtendedDTO> accertamenti = new ArrayList<AccertamentoExtendedDTO>();
		try {
			LOGGER.debug(
					"[AccertamentoApiServiceImpl::loadAllAccertamentiOrByIdStatoDeb] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_ACCER);
			LOGGER.debug(
					"[AccertamentoApiServiceImpl::loadAllAccertamentiOrByIdStatoDeb] verificaIdentitaDigitale END");

			accertamenti = accertamentoDAO.loadAllAccertamentiOrByIdStatoDeb(idStatoDebitorio, null, null, null);

		} catch (DAOException e) {
			LOGGER.debug(
					"[AccertamentoApiServiceImpl::loadAllAccertamentiOrByIdStatoDeb] ERROR:DAOException - END" + e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} catch (SQLException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		}

		LOGGER.debug("[AccertamentoApiServiceImpl::loadAllAccertamentiOrByIdStatoDeb] END");
		return Response.ok(accertamenti).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response deleteAccertamento(Long idAccertamento, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericExceptionList, ParseException {
		LOGGER.debug("[AccertamentoApiServiceImpl::deleteAccertamento] BEGIN");
		LOGGER.debug("[AccertamentoApiServiceImpl::deleteAccertamento] Parametro in input idAccertamento :"
				+ idAccertamento);
		try {
			
			LOGGER.debug("[AccertamentoApiServiceImpl::deleteAccertamento] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_PAGA);
			LOGGER.debug("[AccertamentoApiServiceImpl::deleteAccertamento] verificaIdentitaDigitale END");

			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			
			//ricerco per idAccertamento per aver l'oggetto da settare in tracciamento
			AccertamentoExtendedDTO accertamentoDTO =  accertamentoDAO.loadAccertamentoByIdAccertamento(idAccertamento);
			// Prima cancello i record dalle tabelle dei solleciti legate in foreignKey con
			// l'idAccertamento
			sollDatiTitolareDAO.deleteSollDatiTitolareByIdAccertamento(idAccertamento);
			sollDatiAmministrDAO.deleteSollDatiAmministrByIdAccertamento(idAccertamento);
			sollDatiPagopaDAO.deleteSollDatiPagopaByIdAccertamento(idAccertamento);
			sollDettVersDAO.deleteSollDettVersByIdAccertamento(idAccertamento);
			// Poi cancello l'accertamento
			accertamentoDAO.deleteAccertamentoById(idAccertamento);
			
			tracciamentoManager.saveTracciamento(fruitore, accertamentoDTO, identita, null, "JSON accertamento",
					idAccertamento != null ? idAccertamento.toString() : null,
					"RISCA_T_ACCERTAMENTO", Constants.FLG_OPERAZIONE_DELETE, Constants.OPERAZIONE_DELETE, true, true,
					httpRequest);

		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (SQLException e) {
			LOGGER.error("[AccertamentoApiServiceImpl::deleteAccertamento:: ERROR ]: " + e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		return Response.ok(idAccertamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
