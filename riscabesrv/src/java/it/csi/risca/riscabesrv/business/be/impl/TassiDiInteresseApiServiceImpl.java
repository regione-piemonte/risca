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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.TassiDiInteresseApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TassiDiInteresseDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.PaginationHeaderDTO;
import it.csi.risca.riscabesrv.dto.TassiDiInteresseDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.ErrorMessages;

@Component
public class TassiDiInteresseApiServiceImpl extends BaseApiServiceImpl implements TassiDiInteresseApi {
	private final String className = this.getClass().getSimpleName();
	private static final String IDENTITY = "identity";

	@Autowired
	private TassiDiInteresseDAO tassiDiInteresseDAO;

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;

	@Autowired
	private BusinessLogic businessLogic;

	@Autowired
	private TracciamentoManager tracciamentoManager;

	@Autowired
	private MessaggiDAO messaggiDAO;

	@Override
	public Response loadTassiDiInteresse(Long idAmbito, String fruitore, String tipoDiInteresse, Integer offset,
			Integer limit, String sort, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		// verifica identita digitale
		try {
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::loadTassiDiInteresse] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_DET_PAG);
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::loadTassiDiInteresse] verificaIdentitaDigitale END");
		} catch (Exception e) {
			LOGGER.error("[TassiDiInteresseApiServiceImpl::loadTassiDiInteresse] ERROR ", e);
			return handlerException();
		}

		String jsonString = "";
		try {
			List<TassiDiInteresseDTO> listTassiDiInteresse = tassiDiInteresseDAO.loadTassiDiInteresse(idAmbito,
					tipoDiInteresse, offset, limit, sort);

			// Il primo elemento della lista con data piu recente e cancellabile
			listTassiDiInteresse.get(0).setFlgCancellazione(true);
			Integer countTassiDiInteressi = tassiDiInteresseDAO.countAllTassiDiInteresse(idAmbito, tipoDiInteresse);
			jsonString = pagingHeaderBuild(offset, limit, sort, jsonString, countTassiDiInteressi);
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::loadTassiDiInteresse] END");
			ResponseBuilder resp = Response.ok(listTassiDiInteresse);
			if (!StringUtils.isEmpty(jsonString)) {
				resp.header("PaginationInfo", jsonString);
			}
			resp.header(HttpHeaders.CONTENT_ENCODING, IDENTITY);
			return resp.build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.error("[TassiDiInteresseApiServiceImpl::loadTassiDiInteresse] ERROR ", e);
			return handlerException();
		}
	}

	@Override
	public Response saveTassiDiInteresse(TassiDiInteresseDTO nuovoTassoDiInteresse, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		LOGGER.debug("[TassiDiInteresseDAOImpl::verifica data del TassoDiInteresse piu recente] BEGIN");

		LOGGER.debug("[TassiDiInteresseApiServiceImpl::saveTassiDiInteresse] BEGIN");

		try {
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::saveTassiDiInteresse] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,
					Constants.POST_PUT_DEL_DET_PAG);
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::saveTassiDiInteresse] IdentitaDigitaleManager BEGIN");
			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::saveTassiDiInteresse] IdentitaDigitaleManager END");
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::saveTassiDiInteresse] getAmbitoByIdentitaOrFonte BEGIN");
			Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::saveTassiDiInteresse] verificaIdentitaDigitale END");
			setGestAttoreInsUpd(nuovoTassoDiInteresse, fruitore, httpRequest, httpHeaders);
			nuovoTassoDiInteresse.setIdAmbito(idAmbito);
			businessLogic.validatorDTO(nuovoTassoDiInteresse, null, null);

			LOGGER.debug("[TassiDiInteresseDAOImpl::verifica data del TassoDiInteresse piu recente] BEGIN");
			// recupero tasso con data inizio piu recente
			String tipoInteresse = nuovoTassoDiInteresse.getFlgTipoInteresse();
			String gestAttoreUpd = nuovoTassoDiInteresse.getGestAttoreUpd();
			// tassoRecente => tasso di interesse con data inizio più recente
			TassiDiInteresseDTO tassoRecente = tassiDiInteresseDAO.loadTassoDiInteressePiuRecente(idAmbito,
					tipoInteresse);

			// controllo che la data di inizio di nuovoTassoDiInteresse sia maggiore o uguale 
			//alla data di inizio del tasso di interesse più recente
			checkDataInizioNuovoTassoDiInteresse(nuovoTassoDiInteresse, tassoRecente);

			Calendar cal = Calendar.getInstance();
			cal.setTime(nuovoTassoDiInteresse.getDataInizio());
			cal.add(Calendar.DATE, -1);
			Date newDataFine = cal.getTime();

			tassoRecente.setDataFine(newDataFine);
			tassoRecente.setGestAttoreUpd(gestAttoreUpd);

			tassiDiInteresseDAO.updateTassiDiInteresse(tassoRecente);
			TassiDiInteresseDTO tassoDiInteresseSalvato = tassiDiInteresseDAO
					.saveTassiDiInteresse(nuovoTassoDiInteresse, idAmbito);
			LOGGER.debug("[TassiDiInteresseDAOImpl::verifica data del TassoDiInteresse piu recente] END");

			LOGGER.debug("[TassiDiInteresseApiServiceImpl::saveTassiDiInteresse] END save tracciamento");

			// Traccio update del dato modificato
			tracciamentoManager.saveTracciamento(fruitore, tassoRecente, identita, null, "JSON TASSO INTERESSE",
					tassoRecente.getIdAmbitoInteresse() != null ? tassoRecente.getIdAmbitoInteresse().toString() : null,
					"RISCA_R_AMBITO_INTERESSE", Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, // log
																														// audit
					true, // tracciamento
					httpRequest);

			// traccio inserimento del nuovo dato e log audit
			tracciamentoManager.saveTracciamento(fruitore, tassoDiInteresseSalvato, identita, null,

					"JSON TASSO INTERESSE",
					tassoDiInteresseSalvato.getIdAmbitoInteresse() != null
							? tassoDiInteresseSalvato.getIdAmbitoInteresse().toString()
							: null,
					"RISCA_R_AMBITO_INTERESSE", Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, // log
																													// audit
					true, // tracciamento
					httpRequest);
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::saveTassiDiInteressee] END");

			return Response.ok(nuovoTassoDiInteresse).build();

		} catch (ValidationException ve) {
			return validationExceptionSave(ve);
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (DAOException e) {
			LOGGER.error("[TassiDiInteresseApiServiceImpl::saveTassiDiInteresse] ERROR ", e);
			return handlerDAOException(e);
		} catch (Exception e) {
			LOGGER.error("[TassiDiInteresseApiServiceImpl::saveTassiDiInteresse] ERROR ", e);
			return handlerException();
		}
	}

	private void checkDataInizioNuovoTassoDiInteresse(TassiDiInteresseDTO nuovoTassoDiInteresse,
			TassiDiInteresseDTO tassoRecente) throws ParseException, SQLException {
		Date dataInizioNuovoTasso = nuovoTassoDiInteresse.getDataInizio();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dataInizioFormattataString = formatter.format(dataInizioNuovoTasso);
		Date dataInizioNuovoTassoFormattata = formatter.parse(dataInizioFormattataString);
		
		if (dataInizioNuovoTassoFormattata.compareTo(tassoRecente.getDataInizio()) <= 0) {
			final String messageCode = "E113";
			MessaggiDTO messaggio = messaggiDAO.loadMessaggiByCodMessaggio(messageCode);
			throw new BusinessException(400, messageCode, messaggio.getDesTestoMessaggio());
		}
	}

	@Override
	public Response deleteTassiDiInteresse(String fruitore, Long idAmbitoInteresse, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[TassiDiInteresseApiServiceImpl::deleteTassiDiInteresse] BEGIN");

		try {
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::verificaIdentitaDigitale] BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,
					Constants.POST_PUT_DEL_DET_PAG);
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::verificaIdentitaDigitale] BEGIN");
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			return handlerException();
		}

		try {
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::deleteTassiDiInteresse] deleteTassiDiInteresse BEGIN");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::deleteTassiDiInteresse] verificaIdentitaDigitale END");

			// recupero il tasso di interesse che sta per essere cancellato
			TassiDiInteresseDTO tassoDaEliminare = tassiDiInteresseDAO.loadTassoDiInteresseByIdAmbitoInteresse(idAmbitoInteresse);
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::deleteTassiDiInteresse] esecuzione della delete BEGIN");
			Long idAmbito = tassoDaEliminare.getIdAmbito();
			String tipoInteresse = tassoDaEliminare.getFlgTipoInteresse();
			
			//idAmbitoInteresse del tasso di interesse eliminato
			Long idAmbitoInteresseEliminato = tassiDiInteresseDAO.deleteTassiDiInteresse(idAmbitoInteresse, idAmbito, tipoInteresse);
			
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::deleteTassiDiInteresse] esecuzione della delete END");
			
			//idAmbitoInteresse del tasso recente per tracciare l'update su logaudit
			TassiDiInteresseDTO tassoRecente = tassiDiInteresseDAO.loadTassoDiInteressePiuRecente(idAmbito, tipoInteresse);
			Long idAmbitoInteresseTassoRecente = tassoRecente.getIdAmbitoInteresse();
			
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::saveTracciamento] salvataggio del tracciamento END");

			// tracciamento e log audit del tasso eliminato
			tracciamentoManager.saveTracciamento(fruitore, tassoDaEliminare, identita, null, "JSON TASSO INTERESSE",
					idAmbitoInteresseEliminato != null ? idAmbitoInteresseEliminato.toString() : null, "RISCA_R_AMBITO_INTERESSE",
					Constants.FLG_OPERAZIONE_DELETE, Constants.OPERAZIONE_DELETE, true, true, httpRequest);
			
			//log audit del tasso recente aggiornato con la nuova data fine
			tracciamentoManager.saveTracciamento(fruitore, tassoRecente, identita, null, "JSON TASSO INTERESSE",
					idAmbitoInteresseTassoRecente != null ? idAmbitoInteresseTassoRecente.toString() : null, "RISCA_R_AMBITO_INTERESSE.DATA_FINE",
					Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, false, httpRequest);
			
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::saveTracciamento] salvataggio del tracciamento END");
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::deleteTassiDiInteresse] END");
			return Response.ok(idAmbitoInteresse).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (DAOException e) {
			LOGGER.error("[TassiDiInteresseApiServiceImpl::deleteTassiDiInteresse] ERROR ", e);
			return handlerDAOException(e);
		} catch (Exception e) {
			LOGGER.error("[TassiDiInteresseApiServiceImpl::deleteTassiDiInteresse] ERROR ", e);
			return handlerException();

		}
	}

	@Override
	public Response updateTassiDiInteresse(TassiDiInteresseDTO tassoDiInteresse, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws ParseException {
		LOGGER.debug("[TassiDiInteresseApiServiceImpl::updateTassiDiInteresse] BEGIN");

		final Long idAmbitoInteresse = tassoDiInteresse.getIdAmbitoInteresse();

		// verificaIdentitaDigitale
		try {
			setGestAttoreInsUpd(tassoDiInteresse, fruitore, httpRequest, httpHeaders);
			businessLogic.validatorDTO(tassoDiInteresse, null, null);

			LOGGER.debug("[TassiDiInteresseApiServiceImpl::updateTassiDiInteresse] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,
					Constants.POST_PUT_DEL_DET_PAG);
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::updateTassiDiInteresse] verificaIdentitaDigitale END");
		} catch (ValidationException ve) {
			return validationExceptionSave(ve);
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.error("[TassiDiInteresseApiServiceImpl::updateTassiDiInteresse] Errore generico: ", e);
			return handlerException();
		}

		// Validazione delle date prima di salvare il tasso di interesse modificato
		try {
			validaDateTassoDiInteresse(tassoDiInteresse, idAmbitoInteresse);
		} catch (Exception e) {
			LOGGER.error(
					"[TassiDiInteresseApiServiceImpl::updateTassiDiInteresse] Errore nella validazione delle date: ",
					e);
			return handlerException();
		}

		// Update del tasso di interesse
		TassiDiInteresseDTO tassiDiInteresseDTO = null;
		try {
			tassiDiInteresseDTO = tassiDiInteresseDAO.updateTassiDiInteresse(tassoDiInteresse);
		} catch (BusinessException be) {
			LOGGER.error("[TassiDiInteresseApiServiceImpl::updateTassiDiInteresse]: ERROR update");
			return handleBusinessException(400, be);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

		// Tracciamento e log audit
		try {
			LOGGER.debug("[TassiDiInteresseApiServiceImpl::updateTassiDiInteresse] BEGIN save tracciamento");

			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			tracciamentoManager.saveTracciamento(fruitore, tassoDiInteresse, identita, null, "JSON TASSO INTERESSE",
					tassiDiInteresseDTO.getIdAmbitoInteresse() != null
							? tassiDiInteresseDTO.getIdAmbitoInteresse().toString()
							: null,
					"RISCA_R_AMBITO_INTERESSE", Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true,
					true, httpRequest);

			LOGGER.debug("[TassiDiInteresseApiServiceImpl::updateTassiDiInteresse] END save tracciamento");
		} catch (Exception e) {
			LOGGER.error("[TassiDiInteresseApiServiceImpl::updateTassiDiInteresse:: operazione updateLogAudit]: " + e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

		LOGGER.debug("[TassiDiInteresseApiServiceImpl::updateTassiDiInteresse] END");
		return Response.ok(tassoDiInteresse).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	/*
	 * Metodo per convalidare le date inizio e fine prima di salvarle effettivamente
	 * sul db
	 */
	private void validaDateTassoDiInteresse(TassiDiInteresseDTO tassoDiIntesseUpdate, Long idAmbitoInteresse)
			throws ParseException {
		LOGGER.debug("[TassiDiInteresseDAOImpl::updateTassiDiInteresse >>> validaDateTassoDiInteresse] BEGIN");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// TassoDi InteresseUpdate => in arrivo dalla put
		// TassoDiInteresseSaved => in arrivo dal db
		TassiDiInteresseDTO tassoDiInteresseSaved = tassiDiInteresseDAO
				.loadTassoDiInteresseByIdAmbitoInteresse(idAmbitoInteresse);

		// recupero le date del tasso in update
		String dataInizioUpdate = formatter.format(tassoDiIntesseUpdate.getDataInizio());
		String dataFineUpdate = formatter.format(tassoDiIntesseUpdate.getDataFine());
		// recupero le date del tasso in DB
		String dataInizioSaved = formatter.format(tassoDiInteresseSaved.getDataInizio());
		String dataFineSaved = formatter.format(tassoDiInteresseSaved.getDataFine());

		/*
		 * Controllo se la data fine è diversa da 2100-12-31 TRUE => sono in UPDATE di
		 * un tasso con data fine diversa da 2100-12-31 FALSE => sono in POST o in
		 * UPDATE DEL TASSO PIU RECENTE
		 */

		if (!dataInizioUpdate.equals(dataInizioSaved)) {
			throw new ParseException("Le date di inizio non corrispondono a quelle salvate nel database", 0);
		}

		if (!dataFineUpdate.equals(dataFineSaved)) {
			throw new ParseException("Le date fine non corrispondono a quelle salvate nel database", 0);
		}

		LOGGER.debug("[TassiDiInteresseDAOImpl::updateTassiDiInteresse >>> validaDateTassoDiInteresse] END");
	}

	/* METODI DI COMODO */

	public RowMapper<TassiDiInteresseDTO> getRowMapper() {
		return new RowMapper<TassiDiInteresseDTO>() {
			@Override
			public TassiDiInteresseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				TassiDiInteresseDTO tassiDiInteresse = new TassiDiInteresseDTO();
				tassiDiInteresse.setIdAmbitoInteresse(rs.getLong("id_ambito_interesse"));
				tassiDiInteresse.setIdAmbito(rs.getLong("id_ambito"));
				tassiDiInteresse.setFlgTipoInteresse(rs.getString("tipo_interesse"));
				tassiDiInteresse.setDataInizio(rs.getDate("data_inizio"));
				tassiDiInteresse.setDataFine(rs.getDate("data_fine"));
				tassiDiInteresse.setPercentuale(rs.getBigDecimal("percentuale"));
				tassiDiInteresse.setGiorniLegali(rs.getInt("giorni_legali"));
				return tassiDiInteresse;
			}
		};
	}

	private String pagingHeaderBuild(Integer offset, Integer limit, String sort, String jsonString,
			Integer countTassiDiInteressi) {
		if (offset != null && limit != null) {
			PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
			paginationHeader.setTotalElements(countTassiDiInteressi);
			paginationHeader
					.setTotalPages((countTassiDiInteressi / limit) + ((countTassiDiInteressi % limit) == 0 ? 0 : 1));
			paginationHeader.setPage(offset);
			paginationHeader.setPageSize(limit);
			paginationHeader.setSort(sort);
			JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
			jsonString = jsonPaginationHeader.toString();
		}
		return jsonString;
	}

	/* Gestione eccezzioni */
	private Response validationExceptionSave(ValidationException ve) {
		LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
		Integer status = Response.Status.BAD_REQUEST.getStatusCode();
		ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
		return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
	}

	private Response handlerException() {
		return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
				.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
	}

	private Response handlerDAOException(DAOException e) {
		if (e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
			LOGGER.debug("!!!CODE 1 chiave duplicata!!!!");

			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), ErrorMessages.CODE_1_CHIAVE_DUPLICATA,
					ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA, null, null);
			return Response.serverError().entity(err.getTitle()).build();
		}
		ErrorDTO err = new ErrorDTO("500", "E005",
				"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
		return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
	}
}
