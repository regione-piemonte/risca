/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.StatoDebitorioApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.GenericExceptionList;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.StatoDebitorioApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AttivitaStatoDebitorioDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RicercaPagamentiDaVisionareDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.UtRimbDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.VerifyInvioSpecialeDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.VerifyStatoDebitorioDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Stato Debitorio api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class StatoDebitorioApiServiceImpl extends AbstractApiServiceImpl implements StatoDebitorioApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	private StatoDebitorioApiServiceHelper statoDebitorioApiServiceHelper;

	@Override
	public Response saveStatoDebitorio(StatoDebitorioExtendedDTO statoDebitorio, String fruitore,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		StatoDebitorioExtendedDTO result = new StatoDebitorioExtendedDTO();
		try {
			result = statoDebitorioApiServiceHelper.saveStatoDebitorio(statoDebitorio, fruitore, 
					httpHeaders, httpRequest);
		} catch (GenericExceptionList e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response updateStatoDebitorio(StatoDebitorioExtendedDTO statoDebitorio,
			String fruitore, String flgUpDataScadenza,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericExceptionList {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		StatoDebitorioExtendedDTO result = new StatoDebitorioExtendedDTO();
		try {
			result = statoDebitorioApiServiceHelper.updateStatoDebitorio(statoDebitorio, fruitore,
					flgUpDataScadenza,  httpHeaders, httpRequest);
		} catch (GenericExceptionList e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadAllStatoDebitorioOrByIdRiscossione(String idRiscossioneS, String fruitore, String offsetS,
			String limitS, String sort,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) { 
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Response resp = null;
		try {
			Integer  idRiscossione = ValidationFilter.validateParameter("idRiscossione", idRiscossioneS, 0, Integer.MAX_VALUE);
			Integer  limit = ValidationFilter.validateParameter("limits", limitS, 0,Integer.MAX_VALUE);
			Integer  offset = ValidationFilter.validateParameter("offset", offsetS, 0, Integer.MAX_VALUE);
	
			resp = statoDebitorioApiServiceHelper.loadAllStatoDebitorioOrByIdRiscossione(idRiscossione, fruitore,
					offset, limit, sort,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return resp;
	}

	@Override
	public Response loadStatoDebitorioByIdStatoDebitorio(String idStatoDebitorioS, String fruitore,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		StatoDebitorioExtendedDTO statoDebitorio = new StatoDebitorioExtendedDTO();
		try {
			Integer  idStatoDebitorio = ValidationFilter.validateParameter("idStatoDebitorio", idStatoDebitorioS, 0, Integer.MAX_VALUE);
			statoDebitorio = statoDebitorioApiServiceHelper.loadStatoDebitorioByIdStatoDebitorio(idStatoDebitorio,
					fruitore,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(statoDebitorio).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response verifyStatoDebitorio(String modalita, StatoDebitorioExtendedDTO statoDebitorio,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		VerifyStatoDebitorioDTO verifyStatoDebitorio = new VerifyStatoDebitorioDTO();
		try {
			verifyStatoDebitorio = statoDebitorioApiServiceHelper.verifyStatoDebitorio(modalita, statoDebitorio,
					 httpHeaders, httpRequest);
		} catch (GenericExceptionList e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(verifyStatoDebitorio).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadStatoDebitorioByIdRimborso(String idRimborsoS, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<UtRimbDTO> listUtRimbDTO = new ArrayList<>();
		try {
			Integer  idRimborso = ValidationFilter.validateParameter("idRimborso", idRimborsoS, 0, Integer.MAX_VALUE);

			listUtRimbDTO = statoDebitorioApiServiceHelper.loadStatoDebitorioByIdRimborso(idRimborso, fruitore,
					 httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listUtRimbDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadStatoDebitorioByNap(String nap, String fruitore,List<Integer> sdDaEscludere,  String offsetS, String limitS, String sort,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Response resp = null;
		try {
			Integer  limit = null;
			if(limitS != null)
				limit = ValidationFilter.validateParameter("limits", limitS, 0,Integer.MAX_VALUE);
			Integer  offset = null;
			if(offsetS != null)
				offset = ValidationFilter.validateParameter("offset", offsetS, 0, Integer.MAX_VALUE);
			resp = statoDebitorioApiServiceHelper.loadStatoDebitorioByNap(nap ,fruitore ,sdDaEscludere , offset, limit, sort,
					 httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return resp;
	}

	@Override
	public Response verifyStatoDebitorioInvioSpeciale(String idRiscossioneS,String idStatoDebitorioS,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		VerifyInvioSpecialeDTO verifyStatoDebitorio = new VerifyInvioSpecialeDTO();
		try {
			Integer  idRiscossione = ValidationFilter.validateParameter("idRiscossione", idRiscossioneS, 0, Integer.MAX_VALUE);
			Integer  idStatoDebitorio = null;
			if(idStatoDebitorioS != null)
				idStatoDebitorio = ValidationFilter.validateParameter("idStatoDebitorio", idStatoDebitorioS, 0, Integer.MAX_VALUE);
			
			verifyStatoDebitorio = statoDebitorioApiServiceHelper.verifyStatoDebitorioInvioSpeciale(idRiscossione,idStatoDebitorio,
					 httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        } catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(verifyStatoDebitorio).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadStatoDebitorioByCodUtenza(String codUtenza,  String fruitore,List<Integer> sdDaEscludere ,  String offsetS, String limitS,
			String sort,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName)); 
		Response resp = null;
		try {
			Integer  limit = null;
			if(limitS != null)
				limit = ValidationFilter.validateParameter("limits", limitS, 0,Integer.MAX_VALUE);
			Integer  offset = null;
			if(offsetS != null)
				offset = ValidationFilter.validateParameter("offset", offsetS, 0, Integer.MAX_VALUE);
			
			resp = statoDebitorioApiServiceHelper.loadStatoDebitorioByCodUtenza(codUtenza, fruitore,sdDaEscludere , offset, limit,
					sort,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return resp;
	}

	@Override
	public Response updateAttivitaStatoDebitorio(List<StatoDebitorioExtendedDTO> statoDebitorio, String fruitore,
			String idAttivitaSD,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<StatoDebitorioExtendedDTO> result = new ArrayList<>();
		try {
			result = statoDebitorioApiServiceHelper.updateAttivitaStatoDebitorio(statoDebitorio, fruitore, idAttivitaSD,
					 httpHeaders, httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}


	@Override
	public Response updateAttivitaForAllSDByFilter(AttivitaStatoDebitorioDTO attivitaStatoDeb,
			String tipoRicercaMorosita, String annoS, String flgRestS, String flgAnnS, String lim, String fruitore,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<StatoDebitorioExtendedDTO> result = new ArrayList<>();
		try {
			Integer  flgRest = ValidationFilter.validateParameter("flgRest", flgRestS, 0,1);
			Integer  flgAnn = ValidationFilter.validateParameter("flgAnn", flgAnnS, 0, 1);
			Integer  anno = null;
			if(annoS != null && !annoS.equals("")) {
			    anno = ValidationFilter.validateParameter("anno", annoS, 0,Integer.MAX_VALUE);
			}
			result = statoDebitorioApiServiceHelper.updateAttivitaForAllSDByFilter(attivitaStatoDeb,
					tipoRicercaMorosita, anno, flgRest, flgAnn, lim, fruitore,  httpHeaders,
					httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response updateAttivitaForAllSDByFilterRimborsi(AttivitaStatoDebitorioDTO attivitaStatoDeb,
			String tipoRicercaRimborsi, String annoS, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<StatoDebitorioExtendedDTO> result = new ArrayList<>();
		try {
			Integer  anno = null;
			if(annoS != null && !annoS.equals("")) {
			    anno = ValidationFilter.validateParameter("anno", annoS, 0,Integer.MAX_VALUE);
			}
			result = statoDebitorioApiServiceHelper.updateAttivitaForAllSDByFilterRimborsi(attivitaStatoDeb,
					tipoRicercaRimborsi, anno, fruitore,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   
        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response sommaAllCanoneDovutoByNap(String nap, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		BigDecimal result = null;
		try { 
			result = statoDebitorioApiServiceHelper.sommaAllCanoneDovutoByNap(nap, httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   
        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadStatiDebitoriPerPagamentiDaVisionare(RicercaPagamentiDaVisionareDTO ricercaPagamentiDaVisionareDTO,
			 String fruitore,String offsetS, String limitS,String sort, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try { 
			Integer  limit = null;
			if(limitS != null)
				limit = ValidationFilter.validateParameter("limits", limitS, 0,Integer.MAX_VALUE);
			Integer  offset = null;
			if(offsetS != null)
				offset = ValidationFilter.validateParameter("offset", offsetS, 0, Integer.MAX_VALUE);
			
			return statoDebitorioApiServiceHelper.loadStatiDebitoriPerPagamentiDaVisionare(ricercaPagamentiDaVisionareDTO ,fruitore,
					 offset,  limit, sort, httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   
        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

}
