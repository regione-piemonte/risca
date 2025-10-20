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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.TipoUsoRegolaApi;
import it.csi.risca.riscabesrv.business.be.VerifyTipoUsoRegola;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoRegolaDAO;
import it.csi.risca.riscabesrv.dto.CreaUsoRegolaDTO;
import it.csi.risca.riscabesrv.dto.JsonRangeDTO;
import it.csi.risca.riscabesrv.dto.JsonRegolaDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.PaginationHeaderDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaExtendedDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type Tipo Uso Regola ApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoUsoRegolaApiServiceImpl extends BaseApiServiceImpl implements TipoUsoRegolaApi {

	private final String className = this.getClass().getSimpleName();

	private static final String IDENTITY = "identity";
	private static final String REGEX_3_INTERI_4_DECIMALI = "^-?\\d{1,3}(\\.\\d{1,4})?$";
	@Autowired
	private TipoUsoRegolaDAO tipoUsoRegolaDAO;

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;

	@Autowired
	private MessaggiDAO messaggiDAO;

	@Autowired
	private VerifyTipoUsoRegola verifyTipoUsoRegola;

    @Autowired
    private TracciamentoManager tracciamentoManager;

	@Override
	public Response loadAllAnniFromDTInizio(Integer idAmbito, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			List<Integer> listAnni = tipoUsoRegolaDAO.loadAllAnniFromDTInizio(idAmbito);
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

	@Override
	public Response loadAllUsoRegolaByIdAmbitoAndAnno(Integer idAmbito, Integer anno, Integer offset, Integer limit,
			String sort, String fruitore, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		String jsonString = null;
		try {
			List<TipoUsoRegolaExtendedDTO> listUsoRegola = tipoUsoRegolaDAO.loadAllUsoRegolaByIdAmbitoAndAnno(idAmbito,
					anno, offset, limit, sort);
			Integer numberAllUsoRegola = tipoUsoRegolaDAO.countAllUsoRegolaByIdAmbitoAndAnno(idAmbito, anno);
			PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
			paginationHeader.setTotalElements(numberAllUsoRegola);
			paginationHeader.setTotalPages((numberAllUsoRegola / limit) + ((numberAllUsoRegola % limit) == 0 ? 0 : 1));
			paginationHeader.setPage(offset);
			paginationHeader.setPageSize(limit);
			paginationHeader.setSort(sort);
			JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
			jsonString = jsonPaginationHeader.toString();

			return Response.ok(listUsoRegola).header("PaginationInfo", jsonString)
					.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

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

	@Override
	public Response updateTipoUsoRegola(TipoUsoRegolaExtendedDTO tipoUsoRegola, String fruitore,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_USI);
			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			setGestAttoreInsUpd(tipoUsoRegola, fruitore, httpRequest, httpHeaders);
         	identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_USI);
			verifyTipoUsoRegola.checkTipoUsoRegola(tipoUsoRegola);
			tipoUsoRegola = tipoUsoRegolaDAO.updateTipoUsoRegola(tipoUsoRegola);
			
			tracciamentoManager.saveTracciamento(fruitore, tipoUsoRegola, identita, null, "JSON TIPO USO REGOLA",tipoUsoRegola.getIdTipoUsoRegola().toString(),
					"RISCA_R_T_USO_REGOLA.JSON_REGOLA",
					Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, true, httpRequest);
			
			

			return Response.ok(tipoUsoRegola).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public Response saveTipoUsoRegola(String fruitore, CreaUsoRegolaDTO usoRegola,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		try {
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_USI);
			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			// Verifica anno
			verifyTipoUsoRegola.verificaAnno(usoRegola.getIdAmbito(), usoRegola.getAnno());
			// Verifica percentuale
			if (usoRegola.getPercentuale() != null)
				checkPercentuale(usoRegola.getPercentuale());

			// Carica tutte le regole di utilizzo non terminate per l'ambito specificato
			List<TipoUsoRegolaExtendedDTO> listTipoUsoRegola = tipoUsoRegolaDAO
					.loadAllUsoRegolaByIdAmbitoAndDataFineIsNull(usoRegola.getIdAmbito());
			if (listTipoUsoRegola.isEmpty()) {
				// Se non ci sono regole non terminate, restituisci un'empty response
				return Response.ok(listTipoUsoRegola).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			}

			String cf = identita != null ? identita.getCodFiscale() :  listTipoUsoRegola.get(0).getGestAttoreIns() != null ? listTipoUsoRegola.get(0).getGestAttoreIns() : "" ;;

			// Aggiorna le regole esistenti impostando la data di fine
			List<Long> listIdTipoUso = listTipoUsoRegola.stream().map(TipoUsoRegolaExtendedDTO::getIdTipoUsoRegola)
					.collect(Collectors.toList());
			tipoUsoRegolaDAO.updateDataFineTipoUsoRegola(listIdTipoUso, usoRegola.getAnno(), cf );

			// Crea il log di aggiornamento per le regole esistenti
			String idTipoUsoStringOld = listIdTipoUso.stream().map(String::valueOf).collect(Collectors.joining(", "));
			
			
			tracciamentoManager.saveTracciamento(fruitore, listTipoUsoRegola.get(0), identita, null, null ,idTipoUsoStringOld,
					"RISCA_R_T_USO_REGOLA.DATA_FINE",
					null, Constants.OPERAZIONE_UPDATE, true, false, httpRequest);
			

			// Salva le nuove regole di utilizzo
			for (TipoUsoRegolaExtendedDTO tipoUsoRegolaExtendedDTO : listTipoUsoRegola) {
				setGestAttoreInsUpd(tipoUsoRegolaExtendedDTO, fruitore, httpRequest, httpHeaders);
				tipoUsoRegolaExtendedDTO = mapDatiTipoUsoRegolaExtendedDTO(tipoUsoRegolaExtendedDTO, usoRegola.getAnno(), usoRegola.getPercentuale());
				TipoUsoRegolaExtendedDTO result =  tipoUsoRegolaDAO.saveTipoUsoRegola(tipoUsoRegolaExtendedDTO);
				
				tracciamentoManager.saveTracciamento(fruitore, tipoUsoRegolaExtendedDTO, identita, null, "JSON TIPO USO REGOLA", result.getIdTipoUsoRegola() == null ? null :result.getIdTipoUsoRegola().toString() ,
						"RISCA_R_T_USO_REGOLA",
						Constants.FLG_OPERAZIONE_INSERT ,null, false, true, httpRequest);
				
			}

			// Crea il log di inserimento per le nuove regole di utilizzo
			String idTipoUsoString = listTipoUsoRegola.stream().map(TipoUsoRegolaExtendedDTO::getIdTipoUsoRegola)
					.map(String::valueOf).collect(Collectors.joining(", "));
			
			tracciamentoManager.saveTracciamento(fruitore, listTipoUsoRegola.get(0), identita, null, null,idTipoUsoString,
					"RISCA_R_T_USO_REGOLA",
					null, Constants.OPERAZIONE_INSERT, true, false, httpRequest);
			

			return Response.ok(listTipoUsoRegola).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	private TipoUsoRegolaExtendedDTO mapDatiTipoUsoRegolaExtendedDTO(TipoUsoRegolaExtendedDTO tipoUsoRegola,
			Integer anno, BigDecimal percentuale) throws IOException {
		tipoUsoRegola.setDataInizio(anno + "-01-01");
		if (percentuale != null) {
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
			JsonRegolaDTO jsonRegola = mapper.readValue(tipoUsoRegola.getJsonRegola(), JsonRegolaDTO.class);

			if (jsonRegola != null) {
				BigDecimal percentualeABS = percentuale.abs();
				jsonRegola.setCanoneUnitario(updateCanone(jsonRegola.getCanoneUnitario(), percentualeABS, percentuale,
						2, RoundingMode.HALF_UP));
				jsonRegola.setCanonePercentuale(updateCanone(jsonRegola.getCanonePercentuale(), percentualeABS,
						percentuale, 2, RoundingMode.HALF_UP));
				jsonRegola.setCanoneMinimo(updateCanone(jsonRegola.getCanoneMinimo(), percentualeABS, percentuale, 2,
						RoundingMode.HALF_UP));
				jsonRegola.setCanoneMinimoSogliaInf(updateCanone(jsonRegola.getCanoneMinimoSogliaInf(), percentualeABS,
						percentuale, 2, RoundingMode.HALF_UP));
				jsonRegola.setCanoneMinimoSogliaSup(updateCanone(jsonRegola.getCanoneMinimoSogliaSup(), percentualeABS,
						percentuale, 2, RoundingMode.HALF_UP));

				if (Utils.isNotEmpty(jsonRegola.getJsonRanges())) {
					for (JsonRangeDTO jsonRanges : jsonRegola.getJsonRanges()) {
						jsonRanges.setCanoneMinimoRange(updateCanone(jsonRanges.getCanoneMinimoRange(), percentualeABS,
								percentuale, 2, RoundingMode.HALF_UP));
					}
				}

				mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
				ObjectNode jsonNode = mapper.convertValue(jsonRegola, ObjectNode.class);

				String jsonRegolaString = jsonNode.toString();
				tipoUsoRegola.setJsonRegola(jsonRegolaString);
			}
		}
		return tipoUsoRegola;
	}

	private BigDecimal updateCanone(BigDecimal canone, BigDecimal percentualeABS, BigDecimal percentuale, int scala,
			RoundingMode roundingMode) {
		if (canone != null) {
			BigDecimal valoreDaAggiungere = canone.multiply(percentuale).divide(new BigDecimal("100"));
			BigDecimal nuovoValore = BigDecimal.ZERO;
			if (percentualeABS.signum() < 0)
				nuovoValore = canone.subtract(valoreDaAggiungere).setScale(scala, roundingMode);
			else
				nuovoValore = canone.add(valoreDaAggiungere).setScale(scala, roundingMode);

			return nuovoValore;
		}
		return canone;
	}

	private void checkPercentuale(BigDecimal percentuale) throws SQLException {
		if (!verifyTipoUsoRegola.verificaCanone(percentuale, REGEX_3_INTERI_4_DECIMALI)) {
			throw createBusinessException(Constants.E107);
		}
	}

	private BusinessException createBusinessException(String codMessaggio) throws SQLException {
		MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(codMessaggio);
		return new BusinessException(400, messaggiDTO.getCodMessaggio(),
				Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
	}

	@Override
	public Response updateAllTipoUsoRegola(List<TipoUsoRegolaExtendedDTO> listTipoUsoRegola, String fruitore,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_USI);
			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);

			for (TipoUsoRegolaExtendedDTO tipoUsoRegolaExtended : listTipoUsoRegola) {
				setGestAttoreInsUpd(tipoUsoRegolaExtended, fruitore, httpRequest, httpHeaders);
				verifyTipoUsoRegola.checkTipoUsoRegola(tipoUsoRegolaExtended);
			}

			for (TipoUsoRegolaExtendedDTO tipoUsoRegolaExtended : listTipoUsoRegola) {
				tipoUsoRegolaExtended = tipoUsoRegolaDAO.updateTipoUsoRegola(tipoUsoRegolaExtended);
			}

			String cf = identita != null ? identita.getCodFiscale() :  listTipoUsoRegola.get(0).getGestAttoreIns() != null ? listTipoUsoRegola.get(0).getGestAttoreIns() : "" ;;
			// Crea il log di inserimento per le nuove regole di utilizzo
			String idTipoUsoString = listTipoUsoRegola.stream().map(TipoUsoRegolaExtendedDTO::getIdTipoUsoRegola)
					.map(String::valueOf).collect(Collectors.joining(", "));
			
			tracciamentoManager.saveTracciamento(fruitore, listTipoUsoRegola.get(0), identita, null, null, idTipoUsoString,
					"RISCA_R_T_USO_REGOLA.JSON_REGOLA_LIST",
					null , Constants.OPERAZIONE_UPDATE, true, false, httpRequest);
			
			for (TipoUsoRegolaExtendedDTO tipoUsoRegolaExtendedDTO : listTipoUsoRegola) {
				
				tracciamentoManager.saveTracciamento(fruitore, tipoUsoRegolaExtendedDTO, identita, null, "JSON TIPO USO REGOLA LIST", idTipoUsoString,
						null,
						Constants.FLG_OPERAZIONE_UPDATE , Constants.OPERAZIONE_UPDATE, false, true, httpRequest);
				
			}
			return Response.ok(listTipoUsoRegola).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);

		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

}
