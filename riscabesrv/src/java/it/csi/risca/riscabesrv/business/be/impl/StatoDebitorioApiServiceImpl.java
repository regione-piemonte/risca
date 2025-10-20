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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.StatoDebitorioApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.LockRiscossioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.ErrorObjectDTO;
import it.csi.risca.riscabesrv.dto.PaginationHeaderDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RicercaPagamentiDaVisionareDTO;
import it.csi.risca.riscabesrv.dto.RimborsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.UsoRidaumSdExtendedDTO;
import it.csi.risca.riscabesrv.dto.UtRimbDTO;
import it.csi.risca.riscabesrv.dto.VerifyStatoDebitorioDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type Stato Debitorio api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class StatoDebitorioApiServiceImpl extends BaseApiServiceImpl implements StatoDebitorioApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();
	@Autowired
	private StatoDebitorioDAO statoDebitorioDAO;

    @Autowired
    private TracciamentoManager tracciamentoManager;

	@Autowired
	private AnnualitaSdDAO annualitaSdDAO;

	@Autowired
	private RataSdDAO rataSdDAO;

	@Autowired
	private RimborsoDAO rimborsoDAO;

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;

	@Autowired
	private RiscossioneDAO riscossioneDAO;

	@Autowired
	private LockRiscossioneDAO lockRiscossioneDAO;

	@Autowired
	public BusinessLogic businessLogic;

	@Override
	@Transactional
	public Response saveStatoDebitorio(StatoDebitorioExtendedDTO statoDebitorio, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException, BusinessException {
		LOGGER.debug("[StatoDebitorioApiServiceImpl::saveStatoDebitorio] BEGIN");
		LOGGER.debug("[StatoDebitorioApiServiceImpl::saveStatoDebitorio] Parametro in input statoDebitorio :\n "
				+ statoDebitorio.toString());
		try {
			if (statoDebitorio != null) {
				validateStatoDebitorio(statoDebitorio, fruitore, httpHeaders, httpRequest);
			}
			LOGGER.debug("[StatoDebitorioApiServiceImpl::saveStatoDebitorio] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,
					Constants.POST_PUT_DEL_STDEBI);
			LOGGER.debug("[StatoDebitorioApiServiceImpl::saveStatoDebitorio] verificaIdentitaDigitale END");
		} catch (ValidationException ve) {
			List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			errorsList.add(err);
			return Response.status(Response.Status.BAD_REQUEST).entity(errorsList).build();
		} catch (BusinessException be) {
			List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
			ErrorDTO error1 = new ErrorDTO("401", "U001", "Utente non abiltato ad eseguire questa funzionalita'", null,
					null);
			errorsList.add(error1);
			throw new GenericExceptionList(errorsList);

		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		try {
			statoDebitorioDAO.verifyStatoDebitorio("inserimento", fruitore, null, statoDebitorio, securityContext,
					httpHeaders, httpRequest);
		} catch (GenericExceptionList gel) {
			LOGGER.error("[StatoDebitorioDAOImpl::saveStatoDebitorio] Sono stati rilevati errori nel json inviato ");
			for (ErrorDTO err : gel.getErrors()) {
				if (err.getCode().equals("A024")) {
					err.setCode("E084");
					err.setTitle("Errore: non e' possibile inserire piu' di un rateo prima annualita'");
				}
			}
			throw gel;

		}
		LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] IdentitaDigitaleManager BEGIN");
		Identita identita = IdentitaDigitaleManager
				.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
		LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] IdentitaDigitaleManager END");
		LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] getAmbitoByIdentitaOrFonte BEGIN");
		Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
		LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] getAmbitoByIdentitaOrFonte END");

		try {

			statoDebitorio = statoDebitorioDAO.saveStatoDebitorio(statoDebitorio, idAmbito);
			if (!statoDebitorio.getAnnualitaSd().isEmpty()) {
				for (AnnualitaSdDTO annualitaSd : statoDebitorio.getAnnualitaSd()) {
					annualitaSd.setIdStatoDebitorio(statoDebitorio.getIdStatoDebitorio());
					annualitaSd = annualitaSdDAO.insertAnnualitaSd(annualitaSd, idAmbito);
				}
			}

			LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] calcolo canoneDovuto");
			BigDecimal impRecuperoCanone = BigDecimal.ZERO;
			if (statoDebitorio.getImpRecuperoCanone() != null) {
				impRecuperoCanone = statoDebitorio.getImpRecuperoCanone();
			}

			BigDecimal canoneDovuto = BigDecimal.ZERO;
			if (fruitore == null || (fruitore != null && !fruitore.equals(Constants.BATCH_PORTING))) {
				canoneDovuto = statoDebitorio.getAnnualitaSd().stream().map(AnnualitaSdDTO::getCanoneAnnuo)
						.reduce(BigDecimal.ZERO, BigDecimal::add).add(impRecuperoCanone)
						.setScale(2, RoundingMode.HALF_UP);
				statoDebitorio.setCanoneDovuto(canoneDovuto);
			}

			for (RataSdDTO rata : statoDebitorio.getRate()) {
				rata.setIdStatoDebitorio(statoDebitorio.getIdStatoDebitorio());
				if (fruitore == null || (fruitore != null && !fruitore.equals(Constants.BATCH_PORTING)))
					rata.setCanoneDovuto(canoneDovuto);
				rataSdDAO.saveRataSd(rata);
			}

			if (fruitore != null) {

				if (!statoDebitorio.getRimborsi().isEmpty()) {
					List<RimborsoExtendedDTO> listRimborso = new ArrayList<RimborsoExtendedDTO>();
					for (RimborsoExtendedDTO rimborsoExtendedDTO : statoDebitorio.getRimborsi()) {
						rimborsoExtendedDTO.setIdStatoDebitorio(statoDebitorio.getIdStatoDebitorio());
						if (rimborsoExtendedDTO.getSoggettoRimborso() == null) {
							SoggettiExtendedDTO soggettiExtendedDTO = new SoggettiExtendedDTO();
							soggettiExtendedDTO.setIdSoggetto(statoDebitorio.getIdSoggetto());
							rimborsoExtendedDTO.setSoggettoRimborso(soggettiExtendedDTO);
						}
						rimborsoExtendedDTO = rimborsoDAO.saveRimborso(rimborsoExtendedDTO, idAmbito);
						listRimborso.add(rimborsoExtendedDTO);
					}

					if (listRimborso != null && !listRimborso.isEmpty()) {
						statoDebitorio.setRimborsi(listRimborso);
					}
				}

			}
			//updateStatoContribuzione 
	        List<StatoDebitorioExtendedDTO> statoDebitorioList = new ArrayList<>();
	        statoDebitorioList.add(statoDebitorio);
			businessLogic.aggiornaStatoContribuzione(statoDebitorioList,idAmbito ,statoDebitorio.getGestAttoreUpd(), null);


		} catch (ValidationException ve) {
			List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			errorsList.add(err);
			return Response.status(Response.Status.BAD_REQUEST).entity(errorsList).build();

		} catch (BusinessException be) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error(
					"[StatoDebitorioApiServiceImpl::saveStatoDebitorio ] Sono stati rilevati errori nel saveNRataSd ");
			List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
			ErrorDTO error1 = new ErrorDTO(be.getHttpStatus().toString(), be.getMessageCode(), be.getMessage(), null,
					null);
			errorsList.add(error1);
			throw new GenericExceptionList(errorsList);
		} catch (Exception e) {
			List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error("[StatoDebitorioApiServiceImpl::updateStatoDebitorio:: EXCEPTION ]: " + e);
			ErrorDTO error1 = new ErrorDTO("400", "E001", e.getMessage(), null, null);
			ErrorDTO error2 = new ErrorDTO("400", "E080",
					"Attenzione: e' associata un'attivita' diversa Da inviare a riscossione coattiva.Non e' possibile effettuare la modifica.",
					null, null);
			errorsList.add(error1);
			errorsList.add(error2);
			throw new GenericExceptionList(errorsList);
		}
		try {
			LOGGER.debug("[StatoDebitorioApiServiceImpl::saveStatoDebitorio] BEGIN save tracciamento");
			
			tracciamentoManager.saveTracciamento(fruitore, statoDebitorio, identita, statoDebitorio.getIdRiscossione() , "JSON Stato debitorio",
					statoDebitorio.getIdStatoDebitorio() != null ? statoDebitorio.getIdStatoDebitorio().toString() : null, "RISCA_T_STATO_DEBITORIO",
					Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, true, httpRequest);
	

			LOGGER.debug("[StatoDebitorioApiServiceImpl::saveStatoDebitorio] END save tracciamento");

		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioApiServiceImpl::saveStatoDebitorio:: operazione insertLogAudit]: ", e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}

		return Response.ok(statoDebitorio).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	@Transactional
	public Response updateStatoDebitorio(StatoDebitorioExtendedDTO statoDebitorio,
			String fruitore, String flgUpDataScadenza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericExceptionList, ParseException {
		LOGGER.debug("[StatoDebitorioApiServiceImpl::updateStatoDebitorio] BEGIN");
		LOGGER.debug("[StatoDebitorioApiServiceImpl::updateStatoDebitorio] Parametro in input statoDebitorio :\n "
				+ statoDebitorio.toString());
		try {
			if (statoDebitorio != null) {
				validateStatoDebitorio(statoDebitorio, fruitore, httpHeaders, httpRequest);
			}
			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateStatoDebitorio] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,
					Constants.POST_PUT_DEL_STDEBI);
			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateStatoDebitorio] verificaIdentitaDigitale END");
			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateStatoDebitorio] verifyLockRiscossione BEGIN");
			RiscossioneDTO riscossione = riscossioneDAO
					.getRiscossione(String.valueOf(statoDebitorio.getIdRiscossione()));
			lockRiscossioneDAO.verifyLockRiscossione(riscossione.getIdRiscossione(), fruitore,
					riscossione.getGestAttoreUpd());
			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateStatoDebitorio] verifyLockRiscossione END");
		} catch (BusinessException be) {
			List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
			ErrorDTO error1 = new ErrorDTO(be.getHttpStatus().toString(), be.getMessageCode(),
					Utils.removeAccentedCharacters(be.getMessage()), null, null);
			errorsList.add(error1);
			throw new GenericExceptionList(errorsList);
		} catch (ValidationException ve) {
			List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			errorsList.add(err);
			return Response.status(Response.Status.BAD_REQUEST).entity(errorsList).build();

		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorio] IdentitaDigitaleManager BEGIN");
		Identita identita = IdentitaDigitaleManager
				.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorio] IdentitaDigitaleManager END");
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorio] getAmbitoByIdentitaOrFonte BEGIN");
		Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorio] getAmbitoByIdentitaOrFonte END");
		
		try {
			statoDebitorioDAO.verifyStatoDebitorio("modifica", fruitore, flgUpDataScadenza, statoDebitorio,
					securityContext, httpHeaders, httpRequest);
		} catch (GenericExceptionList gel) {
			LOGGER.error(
					"[StatoDebitorioDAOImpl::updateStatoDebitorio] Sono stati rilevati errori nel json inviato ");
			for (ErrorDTO err : gel.getErrors()) {
				if (err.getCode().equals("A027")) {
					err.setCode("E089");
					err.setTitle("Attenzione:lo stato IUV e' attivo.  Non e' possibile effettuare la modifica.");
				}
				if (err.getCode().equals("A028")) {
					err.setCode("E090");
					err.setTitle(
							"Attenzione:lo stato IUV non e' attivo.  Non e' possibile effettuare la modifica.");
				}
				if (err.getCode().equals("A024")) {
					err.setCode("E084");
					err.setTitle("Errore: non e' possibile inserire piu' di un rateo prima annualita'");
				}
			}

			throw gel;
		}
		
		try {
			StatoDebitorioExtendedDTO statoDebitorioExtendedDTO = statoDebitorioDAO
					.loadStatoDebitorioByIdStatoDebitorio(statoDebitorio.getIdStatoDebitorio());

			statoDebitorio = statoDebitorioDAO.updateStatoDebitorio(statoDebitorio, idAmbito);

			for (AnnualitaSdDTO annualitaSd : statoDebitorioExtendedDTO.getAnnualitaSd()) {
				annualitaSd = annualitaSdDAO.deleteAnnualitaSd(annualitaSd);

			}
			for (AnnualitaSdDTO annualitaSd : statoDebitorio.getAnnualitaSd()) {
				annualitaSd.setIdStatoDebitorio(statoDebitorio.getIdStatoDebitorio());
				annualitaSd = annualitaSdDAO.insertAnnualitaSd(annualitaSd, idAmbito);

			}
			LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorio] calcolo canoneDovuto");
			BigDecimal impRecuperoCanone = BigDecimal.ZERO;
			if (statoDebitorio.getImpRecuperoCanone() != null) {
				impRecuperoCanone = statoDebitorio.getImpRecuperoCanone();
			}
			BigDecimal canoneDovuto = statoDebitorio.getAnnualitaSd().stream().map(AnnualitaSdDTO::getCanoneAnnuo)
					.reduce(BigDecimal.ZERO, BigDecimal::add).add(impRecuperoCanone)
					.setScale(2, RoundingMode.HALF_UP);
			statoDebitorio.setCanoneDovuto(canoneDovuto);
			Date dataScadPag = null;
			for (RataSdDTO rata : statoDebitorio.getRate()) {
				if (rata.getIdRataSdPadre() == null) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					dataScadPag = df.parse(rata.getDataScadenzaPagamento());
					rata.setIdStatoDebitorio(statoDebitorio.getIdStatoDebitorio());
					rata.setCanoneDovuto(canoneDovuto);
					rataSdDAO.updateRataSd(rata);
				}
			}
			// Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita,
			// fruitore);

			if (statoDebitorio.getFlgDilazione() == 1) {
				if (statoDebitorioExtendedDTO.getFlgDilazione() == 0) {
					try {
						rataSdDAO.saveNRataSd(statoDebitorio, idAmbito);
					} catch (BusinessException e) {
						LOGGER.error(
								"[StatoDebitorioApiServiceImpl::updateStatoDebitorio ] Sono stati rilevati errori nel saveNRataSd ");
						throw e;
					}
				} else {
					try {
						rataSdDAO.updateNRataSd(statoDebitorio, idAmbito);
					} catch (BusinessException e) {
						LOGGER.error(
								"[StatoDebitorioApiServiceImpl::updateStatoDebitorio ] Sono stati rilevati errori nel updateNRataSd ");
						throw e;
					}
				}

			} else {
				if (statoDebitorioExtendedDTO.getFlgDilazione() == 1) {
					try {
						rataSdDAO.deleteNRataSd(statoDebitorio.getIdStatoDebitorio(), idAmbito);
					} catch (BusinessException e) {
						LOGGER.error(
								"[StatoDebitorioApiServiceImpl::updateStatoDebitorio ] Sono stati rilevati errori nel deleteNRataSd ");
						throw e;
					}
				}
			}
			//[DP] issue 40
			if (flgUpDataScadenza != null) {
				if (flgUpDataScadenza.equals("SI")) {
					// get all sd for update data scadenza
					if(statoDebitorio.getNap() != null) {
						List<StatoDebitorioExtendedDTO> listSdByNap = statoDebitorioDAO
								.loadAllStatoDebitorioByNap(statoDebitorio.getNap(), null, null, null, null);
						for (StatoDebitorioExtendedDTO sd : listSdByNap) {
							rataSdDAO.updateDataScadenzaByIdSD(sd.getIdStatoDebitorio(), dataScadPag);	
							
							if(!sd.getIdStatoDebitorio().equals(statoDebitorio.getIdStatoDebitorio())) {
								tracciamentoManager.saveTracciamento(fruitore, statoDebitorio, identita, statoDebitorio.getIdRiscossione(), "JSON Stato debitorio",
										sd.getIdStatoDebitorio() != null ? sd.getIdStatoDebitorio().toString() : null, "RISCA_T_STATO_DEBITORIO",
										Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, false, true, httpRequest);
							}
						}
					}else {
						//DP issue 40
						rataSdDAO.updateDataScadenzaByIdSD(statoDebitorio.getIdStatoDebitorio(), dataScadPag);
					}
				}
			} else {
				// VF controllo in caso di dilazione aggiunto per Issue 83, altrimenti l'update
				// ricopre la data di scadenza calcolata per le rate successive alla prima
				if (statoDebitorio.getFlgDilazione() != 1) {
					//DP issue 40
					rataSdDAO.updateDataScadenzaByIdSD(statoDebitorio.getIdStatoDebitorio(), dataScadPag);
				}
			}

			if(statoDebitorio.getNap() != null) {
				Integer countNap = statoDebitorioDAO.countAllStatoDebitorioByNap(statoDebitorio.getNap(), null);
				if (countNap == 1) {
					// VF controllo in caso di dilazione aggiunto per Issue 83, altrimenti l'update
					// ricopre la data di scadenza calcolata per le rate successive alla prima
					if (statoDebitorio.getFlgDilazione() != 1) {
						rataSdDAO.updateDataScadenzaByIdSD(statoDebitorio.getIdStatoDebitorio(), dataScadPag);
					}
				}	
			}
			List<RataSdDTO> listAllRate = rataSdDAO
					.loadListRataSdByStatoDebitorio(statoDebitorio.getIdStatoDebitorio());
			statoDebitorio.setRate(listAllRate);

			rimborsoDAO.deleteRimborso(statoDebitorio.getIdStatoDebitorio());
			if (statoDebitorio.getRimborsi() != null && !statoDebitorio.getRimborsi().isEmpty()) {
				for (RimborsoExtendedDTO rimborsoExtendedDTO : statoDebitorio.getRimborsi()) {
					rimborsoExtendedDTO.setIdStatoDebitorio(statoDebitorio.getIdStatoDebitorio());
					rimborsoExtendedDTO = rimborsoDAO.saveRimborso(rimborsoExtendedDTO, idAmbito);
				}
			}
			// L'importo eccedente e il campo accImportoRimborsato devono essere ricalcolati
			// se ci sono modifiche sulla lista dei rimborsi
			// altrimenti la modifica non si riflette sull'interfaccia
			ricalcolaImportiPerRimborsi(statoDebitorio, canoneDovuto);
			
			//updateStatoContribuzione 
	        List<StatoDebitorioExtendedDTO> statoDebitorioList = new ArrayList<>();
	        statoDebitorioList.add(statoDebitorio);
			businessLogic.aggiornaStatoContribuzione(statoDebitorioList,idAmbito ,statoDebitorio.getGestAttoreUpd(), null);		

		} catch (BusinessException be) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error("[StatoDebitorioApiServiceImpl::updateStatoDebitorio :: EXCEPTION] " + be);
			List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
			ErrorDTO error1 = new ErrorDTO(be.getHttpStatus().toString(), be.getMessageCode(), be.getMessage(), null,
					null);
			errorsList.add(error1);
			throw new GenericExceptionList(errorsList);
		} catch (Exception e) {
			List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error("[StatoDebitorioApiServiceImpl::updateStatoDebitorio:: EXCEPTION ]: " + e);
			ErrorDTO error1 = new ErrorDTO("400", "E001", e.getMessage(), null, null);
			ErrorDTO error2 = new ErrorDTO("400", "E080",
					"Attenzione: e' associata un'attivita' diversa Da inviare a riscossione coattiva.Non e' possibile effettuare la modifica.",
					null, null);
			errorsList.add(error1);
			errorsList.add(error2);
			throw new GenericExceptionList(errorsList);
		}
		try {
			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateStatoDebitorio] BEGIN save tracciamento");

			tracciamentoManager.saveTracciamento(fruitore, statoDebitorio, identita, statoDebitorio.getIdRiscossione(), "JSON Stato debitorio",
					statoDebitorio.getIdStatoDebitorio() != null ? statoDebitorio.getIdStatoDebitorio().toString() : null, "RISCA_T_STATO_DEBITORIO",
					Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, true, httpRequest);
		

			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateStatoDebitorio] END save tracciamento");

		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioApiServiceImpl::updateStatoDebitorio:: operazione insertLogAudit]: " + e);

		}

		return Response.ok(statoDebitorio).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	private void ricalcolaImportiPerRimborsi(StatoDebitorioExtendedDTO statoDebitorio, BigDecimal canoneDovuto) {
		BigDecimal importoVersato = statoDebitorio.getImportoVersato();
		BigDecimal sommRimborsi = BigDecimal.ZERO;
		BigDecimal sommRimborsiComp = BigDecimal.ZERO;
		if (statoDebitorio.getRimborsi() != null && !statoDebitorio.getRimborsi().isEmpty()) {
			sommRimborsi = statoDebitorio.getRimborsi().stream()
					.filter(r -> r.getTipoRimborso().getCodTipoRimborso().equals("RIMBORSATO"))
					.map(RimborsoExtendedDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add);

			// [VF] issue 75 La somma rimborsi deve tenere conto anche di eventuali
			// compensazioni utilizzate che si trovano nella colonna imp_restituito della
			// tabella r_rimborsi per id_tipo_rimborso = 2 o 3
			BigDecimal sommComp = getSommComp(statoDebitorio.getRimborsi());
			sommRimborsiComp = sommRimborsi.add(sommComp);
		}
		BigDecimal intMaturatiSpeseNotifica = getInteressiESpese(statoDebitorio);
		BigDecimal importoEccedente = importoVersato.subtract(canoneDovuto.add(intMaturatiSpeseNotifica))
				.subtract(sommRimborsiComp);

		statoDebitorio.setAccImportoRimborsato(sommRimborsi);
		statoDebitorio.setImportoEccedente(importoEccedente);
	}
	
	private BigDecimal getSommComp(List<RimborsoExtendedDTO> rimborsi) {
		BigDecimal sommComp = BigDecimal.ZERO;
		List<RimborsoExtendedDTO> listComp = rimborsi.stream()
				.filter(r -> (r.getTipoRimborso().getCodTipoRimborso().equals("DA_COMPENSARE")
						|| r.getTipoRimborso().getCodTipoRimborso().equals("COMPENSATO"))).collect(Collectors.toList());
		for (RimborsoExtendedDTO comp : listComp) {
			if(comp.getImpRestituito()!= null) {
				sommComp = sommComp.add(comp.getImpRestituito());
			}
		}
		return sommComp;
	}
	
	private BigDecimal getInteressiESpese(StatoDebitorioExtendedDTO statoDebitorio) {
		BigDecimal interessi = BigDecimal.ZERO;
		BigDecimal spese = BigDecimal.ZERO;
		for (RataSdDTO rata : statoDebitorio.getRate()) {
			if(rata.getInteressiMaturati()!= null) {
				interessi = interessi.add(rata.getInteressiMaturati());
			}
		}
		if (statoDebitorio.getImpSpeseNotifica() != null) {
			spese = statoDebitorio.getImpSpeseNotifica();
		}
		return interessi.add(spese);
	}

	// Metodi di supporto per la validazione
	private void validateStatoDebitorio(StatoDebitorioExtendedDTO statoDebitorio, String fruitore,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws BusinessException, ValidationException, Exception {
		setGestAttoreInsUpd(statoDebitorio, fruitore, httpRequest, httpHeaders);
		businessLogic.validatorDTO(statoDebitorio, null, null);
		if (statoDebitorio.getAnnualitaSd() != null && Utils.isNotEmpty(statoDebitorio.getAnnualitaSd())) {
			for (AnnualitaSdDTO annualitaSd : statoDebitorio.getAnnualitaSd()) {
				setGestAttoreInsUpd(annualitaSd, fruitore, httpRequest, httpHeaders);
				validateAnnualitaSd(annualitaSd, fruitore, httpRequest, httpHeaders);
			}
		}
		validateRate(statoDebitorio, fruitore, httpRequest, httpHeaders);
		validateRimborsi(statoDebitorio, fruitore, httpRequest, httpHeaders);
		validateAccertamenti(statoDebitorio, fruitore, httpRequest, httpHeaders);
	}

	private void validateAccertamenti(StatoDebitorioExtendedDTO statoDebitorio, String fruitore,
			HttpServletRequest httpRequest, HttpHeaders httpHeaders)
			throws BusinessException, ValidationException, Exception {

		if (statoDebitorio.getAccertamenti() != null && Utils.isNotEmpty(statoDebitorio.getAccertamenti())) {
			for (AccertamentoExtendedDTO accertamentoExtendedDTO : statoDebitorio.getAccertamenti()) {
				setGestAttoreInsUpd(accertamentoExtendedDTO, fruitore, httpRequest, httpHeaders);
				businessLogic.validatorDTO(accertamentoExtendedDTO, null, null);
			}
		}
	}

	private void validateRimborsi(StatoDebitorioExtendedDTO statoDebitorio, String fruitore,
			HttpServletRequest httpRequest, HttpHeaders httpHeaders)
			throws BusinessException, ValidationException, Exception {

		if (statoDebitorio.getRimborsi() != null && Utils.isNotEmpty(statoDebitorio.getRimborsi())) {
			for (RimborsoExtendedDTO rimborsoExtendedDTO : statoDebitorio.getRimborsi()) {
				setGestAttoreInsUpd(rimborsoExtendedDTO, fruitore, httpRequest, httpHeaders);
				businessLogic.validatorDTO(rimborsoExtendedDTO, null, null);
			}
		}
	}

	private void validateRate(StatoDebitorioExtendedDTO statoDebitorio, String fruitore, HttpServletRequest httpRequest,
			HttpHeaders httpHeaders) throws BusinessException, ValidationException, Exception {

		if (statoDebitorio.getRate() != null && Utils.isNotEmpty(statoDebitorio.getRate())) {
			for (RataSdDTO rataSdDTO : statoDebitorio.getRate()) {
				setGestAttoreInsUpd(rataSdDTO, fruitore, httpRequest, httpHeaders);
				businessLogic.validatorDTO(rataSdDTO, null, null);
			}
		}
	}

	private void validateAnnualitaSd(AnnualitaSdDTO annualitaSd, String fruitore, HttpServletRequest httpRequest,
			HttpHeaders httpHeaders) throws BusinessException, ValidationException, Exception {
		businessLogic.validatorDTO(annualitaSd, null, null);
		// DP SE FRUITORE BATCH_PORTING NON VERIFICO IL JSON
		if (fruitore == null || (fruitore != null && !fruitore.equals(Constants.BATCH_PORTING))) {
			businessLogic.validatorDTO(null, null, annualitaSd.getJsonDtAnnualitaSd());
		}

		if (annualitaSd.getAnnualitaUsoSd() != null && Utils.isNotEmpty(annualitaSd.getAnnualitaUsoSd())) {
			for (AnnualitaUsoSdDTO annualitaUsoSdDTO : annualitaSd.getAnnualitaUsoSd()) {
				setGestAttoreInsUpd(annualitaUsoSdDTO, fruitore, httpRequest, httpHeaders);
				validateAnnualitaUsoSd(annualitaUsoSdDTO, fruitore, httpRequest, httpHeaders);
			}
		}
	}

	private void validateAnnualitaUsoSd(AnnualitaUsoSdDTO annualitaUsoSdDTO, String fruitore,
			HttpServletRequest httpRequest, HttpHeaders httpHeaders)
			throws BusinessException, ValidationException, Exception {
		businessLogic.validatorDTO(annualitaUsoSdDTO, null, null);

		if (annualitaUsoSdDTO.getUsoRidaumSd() != null && Utils.isNotEmpty(annualitaUsoSdDTO.getUsoRidaumSd())) {
			for (UsoRidaumSdExtendedDTO usoRidaumSdExtendedDTO : annualitaUsoSdDTO.getUsoRidaumSd()) {
				setGestAttoreInsUpd(usoRidaumSdExtendedDTO, fruitore, httpRequest, httpHeaders);
				businessLogic.validatorDTO(usoRidaumSdExtendedDTO, null, null);
			}
		}
	}

	@Override
	public Response loadAllStatoDebitorioOrByIdRiscossione(Long idRiscossione, String fruitore, Integer offset,
			Integer limit, String sort, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[StatoDebitorioApiServiceImpl::loadAllStatoDebitorioOrByIdRiscossione] BEGIN");

		try {
			LOGGER.debug(
					"[StatoDebitorioApiServiceImpl::loadAllStatoDebitorioOrByIdRiscossione] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_STDEBI);
			LOGGER.debug(
					"[StatoDebitorioApiServiceImpl::loadAllStatoDebitorioOrByIdRiscossione] verificaIdentitaDigitale END");
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);

		} catch (SQLException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		List<StatoDebitorioExtendedDTO> statiDebitori = new ArrayList<StatoDebitorioExtendedDTO>();
		Integer numberAllStatiDebitoriByIdRiscossione = null;
		String jsonString = null;
		try {
			statiDebitori = statoDebitorioDAO.loadAllStatoDebitorioOrByIdRiscossione(idRiscossione, offset, limit,
					sort);
			numberAllStatiDebitoriByIdRiscossione = statoDebitorioDAO
					.countAllStatoDebitorioOrByIdRiscossione(idRiscossione);
			PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
			paginationHeader.setTotalElements(numberAllStatiDebitoriByIdRiscossione);
			paginationHeader.setTotalPages((numberAllStatiDebitoriByIdRiscossione / limit)
					+ ((numberAllStatiDebitoriByIdRiscossione % limit) == 0 ? 0 : 1));
			paginationHeader.setPage(offset);
			paginationHeader.setPageSize(limit);
			paginationHeader.setSort(sort);
			JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
			jsonString = jsonPaginationHeader.toString();

		} catch (DAOException e) {
			LOGGER.debug(
					"[StatoDebitorioApiServiceImpl::loadAllStatoDebitorioOrByIdRiscossione] ERROR:DAOException - END");
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

		LOGGER.debug("[StatoDebitorioApiServiceImpl::loadAllStatoDebitorioOrByIdRiscossione] END");
		return Response.ok(statiDebitori).header("PaginationInfo", jsonString)
				.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadStatoDebitorioByIdStatoDebitorio(Long idStatoDebitorio, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByIdStatoDebitorio] BEGIN");

		try {
			LOGGER.debug(
					"[StatoDebitorioApiServiceImpl::loadStatoDebitorioByIdStatoDebitorio] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_STDEBI);
			LOGGER.debug(
					"[StatoDebitorioApiServiceImpl::loadStatoDebitorioByIdStatoDebitorio] verificaIdentitaDigitale END");
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);

		} catch (SQLException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}

		StatoDebitorioExtendedDTO statoDebitorio = new StatoDebitorioExtendedDTO();
		try {
			statoDebitorio = statoDebitorioDAO.loadStatoDebitorioByIdStatoDebitorio(idStatoDebitorio);

		} catch (DAOException e) {
			LOGGER.debug(
					"[StatoDebitorioApiServiceImpl::loadStatoDebitorioByIdStatoDebitorio] ERROR - errore nel recuperare l'informazione da db ");
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

		LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByIdStatoDebitorio] END");
		return Response.ok(statoDebitorio).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response verifyStatoDebitorio(String modalita, StatoDebitorioExtendedDTO statoDebitorio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException {
		LOGGER.debug("[StatoDebitorioApiServiceImpl::verifyStatoDebitorio] BEGIN");

		VerifyStatoDebitorioDTO verifyStatoDebitorio = new VerifyStatoDebitorioDTO();
		verifyStatoDebitorio = statoDebitorioDAO.verifyStatoDebitorio(modalita, null, null, statoDebitorio,
				securityContext, httpHeaders, httpRequest);

		LOGGER.debug("[StatoDebitorioApiServiceImpl::verifyStatoDebitorio] END");
		return Response.ok(verifyStatoDebitorio).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadStatoDebitorioByIdRimborso(Long idRimborso, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByIdRimborso] BEGIN");
		try {
			LOGGER.debug(
					"[StatoDebitorioApiServiceImpl::loadStatoDebitorioByIdRimborso] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_STDEBI);
			LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByIdRimborso] verificaIdentitaDigitale END");
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);

		} catch (SQLException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		List<UtRimbDTO> UtRimbDTO = new ArrayList<UtRimbDTO>();

		try {
			UtRimbDTO = statoDebitorioDAO.loadStatoDebitorioByIdRimborso(idRimborso);
		} catch (Exception e) {
			LOGGER.debug(
					"[StatoDebitorioApiServiceImpl::loadStatoDebitorioByIdRimborso] ERROR - errore nel controllo delle informazioni ");
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

		LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByIdRimborso] END");
		return Response.ok(UtRimbDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadStatoDebitorioByNap(String nap,  String fruitore, List<Integer> sdDaEscludere, Integer offset,
			Integer limit, String sort, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByNap] BEGIN");
		try {
			LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByNap] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_STDEBI);
			LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByNap] verificaIdentitaDigitale END");
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);

		} catch (SQLException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		List<StatoDebitorioExtendedDTO> statiDebitori = new ArrayList<StatoDebitorioExtendedDTO>();
		Integer numberAllStatiDebitoriByNap = null;
		String jsonString = null;
		try {
			statiDebitori = statoDebitorioDAO.loadAllStatoDebitorioByNap(nap,  sdDaEscludere, offset, limit, sort);
			if (offset != null && limit != null) {
				numberAllStatiDebitoriByNap = statoDebitorioDAO.countAllStatoDebitorioByNap(nap, sdDaEscludere);
				PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
				paginationHeader.setTotalElements(numberAllStatiDebitoriByNap);
				paginationHeader.setTotalPages(
						(numberAllStatiDebitoriByNap / limit) + ((numberAllStatiDebitoriByNap % limit) == 0 ? 0 : 1));
				paginationHeader.setPage(offset);
				paginationHeader.setPageSize(limit);
				paginationHeader.setSort(sort);
				JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
				jsonString = jsonPaginationHeader.toString();
			}

		} catch (DAOException e) {
			LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByNap] END");
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

		LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByNap] END");
		return Response.ok(statiDebitori).header("PaginationInfo", jsonString)
				.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	@Override
	public Response verifyStatoDebitorioInvioSpeciale(Long idRiscossione, Long idStatoDebitorio,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		;

		LOGGER.debug("[StatoDebitorioApiServiceImpl::verifyStatoDebitorioInvioSpeciale] BEGIN");

		Integer countStatoDebitorioInvioSpeciale = null;
		try {
			countStatoDebitorioInvioSpeciale = statoDebitorioDAO.verifyStatoDebitorioInvioSpeciale(idRiscossione,
					idStatoDebitorio);

		} catch (DAOException e) {
			LOGGER.error("[StatoDebitorioApiServiceImpl::verifyStatoDebitorioInvioSpeciale] ERROR", e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}

		LOGGER.debug("[StatoDebitorioApiServiceImpl::verifyStatoDebitorioInvioSpeciale] END");
		return Response.ok(countStatoDebitorioInvioSpeciale).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	@Override
	public Response loadStatoDebitorioByCodUtenza(String codUtenza,  String fruitore, List<Integer> sdDaEscludere,
			Integer offset, Integer limit, String sort, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByCodUtenza] BEGIN");
		try {
			LOGGER.debug(
					"[StatoDebitorioApiServiceImpl::loadStatoDebitorioByCodUtenza] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_STDEBI);
			LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByCodUtenza] verificaIdentitaDigitale END");
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);

		} catch (SQLException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		List<StatoDebitorioExtendedDTO> statiDebitori = new ArrayList<StatoDebitorioExtendedDTO>();
		Integer numberAllStatiDebitoriByCodUtenza = null;
		String jsonString = null;
		try {
			statiDebitori = statoDebitorioDAO.loadAllStatoDebitorioByCodUtenza(codUtenza,sdDaEscludere, offset, limit,
					sort);
			if (offset != null && limit != null) {
				numberAllStatiDebitoriByCodUtenza = statoDebitorioDAO.countAllStatoDebitorioByCodUtenza(codUtenza,
						sdDaEscludere);
				PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
				paginationHeader.setTotalElements(numberAllStatiDebitoriByCodUtenza);
				paginationHeader.setTotalPages((numberAllStatiDebitoriByCodUtenza / limit)
						+ ((numberAllStatiDebitoriByCodUtenza % limit) == 0 ? 0 : 1));
				paginationHeader.setPage(offset);
				paginationHeader.setPageSize(limit);
				paginationHeader.setSort(sort);
				JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
				jsonString = jsonPaginationHeader.toString();
			}

		} catch (DAOException e) {
			LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByCodUtenza] END");
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

		LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByCodUtenza] END");
		return Response.ok(statiDebitori).header("PaginationInfo", jsonString)
				.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	@Override
	public Response updateAttivitaStatoDebitorio(List<StatoDebitorioExtendedDTO> statoDebitorio, String fruitore,
			String idAttivitaSD, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[StatoDebitorioApiServiceImpl::updateAttivitaStatoDebitorio] BEGIN");
		try {
			
			
			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateAttivitaStatoDebitorio] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,
					Constants.POST_PUT_DEL_STDEBI);
			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateAttivitaStatoDebitorio] verificaIdentitaDigitale END");
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);

		} catch (SQLException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		try {	
			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			
			for (StatoDebitorioExtendedDTO sd : statoDebitorio) {
				setGestAttoreInsUpd(sd, fruitore, httpRequest, httpHeaders);
			}	
			statoDebitorioDAO.updateAttivitaForAllStatoDebitori(statoDebitorio, idAttivitaSD);
			
			
			
			for (int i = 0; i < statoDebitorio.size(); i++) {
				if (i == 0) {
					tracciamentoManager.saveTracciamento(fruitore, statoDebitorio.get(i), identita, null, "JSON Attivita Stato debitorio",
							idAttivitaSD != null ? idAttivitaSD.toString() : null, "RISCA_T_STATO_DEBITORIO.ATTIVITA",
							Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, false, httpRequest);	
				}
				tracciamentoManager.saveTracciamento(fruitore, statoDebitorio.get(i), identita, null, "JSON Attivita Stato debitorio",
						idAttivitaSD != null ? idAttivitaSD.toString() : null, "RISCA_T_STATO_DEBITORIO.ATTIVITA",
						Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, false, true, httpRequest);	
			}

		} catch (Exception e) {
			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateAttivitaStatoDebitorio] END");
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

		LOGGER.debug("[StatoDebitorioApiServiceImpl::updateAttivitaStatoDebitorio] END");
		return Response.ok(statoDebitorio).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	@Override
	public Response updateStatoDebitorioFromStatoDebitorioUpd(String attore, Long idElabora,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Integer res = null;
		try {
			res = statoDebitorioDAO.updateStatoDebitorioFromStatoDebitorioUpd(attore, idElabora);

		} catch (DAOException e) {
			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateAttivitaStatoDebitorio] END");
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

		LOGGER.debug("[StatoDebitorioApiServiceImpl::updateAttivitaStatoDebitorio] END");
		return Response.ok(res).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadAllStatoDebitorio(String fruitore,  Integer offset,Integer limit, String sort,Boolean isNotturnoTurnOn, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 LOGGER.debug("[StatoDebitorioApiServiceImpl::loadAllStatoDebitorio] BEGIN");
         List<StatoDebitorioExtendedDTO> statiDebitori = new ArrayList<StatoDebitorioExtendedDTO>();
		try {
			LOGGER.debug("[StatoDebitorioApiServiceImpl::loadAllStatoDebitorio] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_STDEBI);
			LOGGER.debug("[StatoDebitorioApiServiceImpl::loadAllStatoDebitorio] verificaIdentitaDigitale END");

			
			 if (limit == null || limit > 100) {
	            	ErrorObjectDTO error = new ErrorObjectDTO();
	            	error.setCode("E01");
	            	error.setTitle("Errore: parametro limit obbligatorio e minore di 100 ");
	            	throw new DatiInputErratiException(error);
	            }
     	 
     	 statiDebitori =  businessLogic.loadAllStatoDebitorio(offset,limit, sort, isNotturnoTurnOn);	
			
			
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);


		} catch (SQLException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (DatiInputErratiException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} catch (DAOException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		LOGGER.debug("[StatoDebitorioApiServiceImpl::loadAllStatoDebitorio] END");
		return Response.ok(statiDebitori).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	@Override
	public Response updateStatoContribuzione(List<StatoDebitorioExtendedDTO> listStatoDebitorio, String fruitore,
			Long idElabora, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException {
		LOGGER.debug("[StatoDebitorioApiServiceImpl::updateStatoContribuzione] BEGIN");
		LOGGER.debug(
				"[StatoDebitorioApiServiceImpl::updateStatoContribuzione] Parametro in input listStatoDebitorio :\n "
						+ listStatoDebitorio.size());
		try {
			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateStatoContribuzione] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,
					Constants.POST_PUT_DEL_STDEBI);
			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateStatoContribuzione] verificaIdentitaDigitale END");
		} catch (BusinessException be) {
			List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
			ErrorDTO error1 = new ErrorDTO(be.getHttpStatus().toString(), be.getMessageCode(),
					Utils.removeAccentedCharacters(be.getMessage()), null, null);
			errorsList.add(error1);
			throw new GenericExceptionList(errorsList);
		} catch (SQLException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoContribuzione] IdentitaDigitaleManager BEGIN");
		Identita identita = IdentitaDigitaleManager
				.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoContribuzione] IdentitaDigitaleManager END");
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoContribuzione] getAmbitoByIdentitaOrFonte BEGIN");
		Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoContribuzione] getAmbitoByIdentitaOrFonte END");
		int stepStatus = 0;
		try {
			// verificare se serve nel caso di ingresso da risca e non da batch oppure se mi
			// servono altre properties
			// StatoDebitorioExtendedDTO statoDebitorioExtendedDTO =
			// statoDebitorioDAO.loadStatoDebitorioByIdStatoDebitorio(statoDebitorio.getIdStatoDebitorio());
			if (listStatoDebitorio == null || listStatoDebitorio.size() == 0) {
				ErrorObjectDTO error = new ErrorObjectDTO();
				error.setCode("E01");
				error.setTitle("Errore: lista vuota ");
				stepStatus = 1;
				throw new DatiInputErratiException(error);
			}

			stepStatus = businessLogic.aggiornaStatoContribuzione(listStatoDebitorio, idAmbito, fruitore, idElabora);

		} catch (BusinessException be) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error("[StatoDebitorioApiServiceImpl::updateStatoDebitorio :: EXCEPTION] " + be);
			List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
			ErrorDTO error1 = new ErrorDTO(be.getHttpStatus().toString(), be.getMessageCode(), be.getMessage(), null,
					null);
			errorsList.add(error1);
			throw new GenericExceptionList(errorsList);
		} catch (DatiInputErratiException e) {
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), e.getError().getCode(), e.getError().getTitle(), null, null);
			return Response.serverError().entity(err).status(status).build();
		} catch (Exception e) {
			List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
			LOGGER.error("[StatoDebitorioApiServiceImpl::updateStatoDebitorio:: EXCEPTION ]: " + e);
			ErrorDTO error1 = new ErrorDTO("400", "E001","Attenzione: errore in aggiorna stato contribuzione."+ e.getMessage(), null, null);	
			errorsList.add(error1);
			throw new GenericExceptionList(errorsList);
		}
		return Response.ok(stepStatus).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	public Response updateSpeseNotificaStatoDebitorio(Long idStatoDebitorio, BigDecimal impSpeseNotifica,
			String attore, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException {
		Integer res = null;
		try {
			res = statoDebitorioDAO.updateSpeseNotificaStatoDebitorio(idStatoDebitorio, impSpeseNotifica, attore);
		} catch (DAOException e) {
			LOGGER.debug("[StatoDebitorioApiServiceImpl::updateSpeseNotificaStatoDebitorio] END");
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta.", null,
					null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}

		LOGGER.debug("[StatoDebitorioApiServiceImpl::updateSpeseNotificaStatoDebitorio] END");
		return Response.ok(res).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	
	@Override
	public Response sommaAllCanoneDovutoByNap(String nap, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			identitaDigitaleManager.verificaIdentitaDigitale(null, null, httpHeaders, Constants.LOAD_STDEBI);
			BigDecimal sommaCanoneDovuto = statoDebitorioDAO.sommaAllCanoneDovutoByNap(nap);
			return Response.ok(sommaCanoneDovuto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		} catch (BusinessException be) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,be) ,be);
			return handleBusinessException(be.getHttpStatus(), be);

		} catch (Exception e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

	}

	@Override
	public Response loadStatiDebitoriPerPagamentiDaVisionare(
			RicercaPagamentiDaVisionareDTO ricercaPagamentiDaVisionareDTO, String fruitore, Integer offset, Integer limit,
			String sort, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			LOGGER.debug(
					"[StatoDebitorioApiServiceImpl::loadStatoDebitorioByCodUtenza] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_STDEBI);
			LOGGER.debug("[StatoDebitorioApiServiceImpl::loadStatoDebitorioByCodUtenza] verificaIdentitaDigitale END");
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);

		} catch (SQLException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		List<StatoDebitorioExtendedDTO> statiDebitori = new ArrayList<StatoDebitorioExtendedDTO>();
		Integer numberAllStatiDebitoriPerPagamentiDaVisionare = null;
		String jsonString = null;
		Identita identita = null;
		Long idAmbito = null;
		try {
			identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
			statiDebitori = statoDebitorioDAO.loadStatiDebitoriPerPagamentiDaVisionare(ricercaPagamentiDaVisionareDTO, idAmbito, offset, limit, sort);
			if (offset != null && limit != null) {
				numberAllStatiDebitoriPerPagamentiDaVisionare = statoDebitorioDAO.countStatiDebitoriPerPagamentiDaVisionare(ricercaPagamentiDaVisionareDTO, idAmbito);
				PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
				paginationHeader.setTotalElements(numberAllStatiDebitoriPerPagamentiDaVisionare);
				paginationHeader.setTotalPages((numberAllStatiDebitoriPerPagamentiDaVisionare / limit)
						+ ((numberAllStatiDebitoriPerPagamentiDaVisionare % limit) == 0 ? 0 : 1));
				paginationHeader.setPage(offset);
				paginationHeader.setPageSize(limit);
				paginationHeader.setSort(sort);
				JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
				jsonString = jsonPaginationHeader.toString();
			}


		} catch (Exception e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

		return Response.ok(statiDebitori).header("PaginationInfo", jsonString)
				.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
