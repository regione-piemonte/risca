package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.RiscossioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.RiscossioneApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RiscossioneDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RiscossioneSearchDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.VerifyRiscossioneStatoDebDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type riscossione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class RiscossioneApiServiceImpl extends AbstractApiServiceImpl implements RiscossioneApi {

	private final String className = this.getClass().getSimpleName();

	@Autowired
	private RiscossioneApiServiceHelper riscossioneApiServiceHelper;

	@Override
	public Response saveRiscossione(RiscossioneDTO riscossione, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		RiscossioneDTO dto = new RiscossioneDTO();
		try {
			dto = riscossioneApiServiceHelper.saveRiscossione(riscossione, fruitore,  httpHeaders,
					httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

		return Response.ok(dto).status(201).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response updateRiscossione(RiscossioneDTO riscossione, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		RiscossioneDTO dto = new RiscossioneDTO();
		try {
			dto = riscossioneApiServiceHelper.updateRiscossione(riscossione, fruitore,  httpHeaders,
					httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

		return Response.ok(dto).status(201).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response getRiscossione(String codRiscossione, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		RiscossioneDTO riscossione = new RiscossioneDTO();
		try {
			riscossione = riscossioneApiServiceHelper.getRiscossione(codRiscossione, fruitore, 
					httpHeaders, httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(riscossione).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response getRiscossioniGruppo(String codGruppo,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<RiscossioneDTO> riscossioni = new ArrayList<>();
		try {
			riscossioni = riscossioneApiServiceHelper
					.getRiscossioniGruppo(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), codGruppo);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(riscossioni).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response searchRiscossione(RiscossioneSearchDTO RiscossioneSearch, String fruitore, String modalitaRicerca,
			String offsetS, String limitS, String sort,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Integer  offset = ValidationFilter.validateParameter("offset", offsetS, 0, Integer.MAX_VALUE);
			Integer  limit = ValidationFilter.validateParameter("limit", limitS, 0, Integer.MAX_VALUE);
			return riscossioneApiServiceHelper.searchRiscossione(RiscossioneSearch, fruitore, modalitaRicerca, offset,
					limit, sort,  httpHeaders, httpRequest);
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

	@Override
	public Response loadDatoTecnico(String idRiscossioneS, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		String json_dt = null;
		try {
			Integer  idRiscossione = ValidationFilter.validateParameter("idRiscossione", idRiscossioneS, 0, Integer.MAX_VALUE);
			json_dt = riscossioneApiServiceHelper.loadDatoTecnico(idRiscossione, fruitore,  httpHeaders,
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
		return Response.ok(json_dt).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response verifyRiscossioniSTDebitori(String idOggettoS, String indTipoOggetto, String idTipoOper,
			String idRiscossioneS,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		VerifyRiscossioneStatoDebDTO verifyRiscossioneStatoDebDTO = new VerifyRiscossioneStatoDebDTO();
		try {
			Integer  idOggetto = ValidationFilter.validateParameter("idOggetto", idOggettoS, 0, Integer.MAX_VALUE);
			Integer  idRiscossione = null;
			if(idRiscossioneS != null )
				idRiscossione = ValidationFilter.validateParameter("idRiscossione", idRiscossioneS, 0, Integer.MAX_VALUE);
			verifyRiscossioneStatoDebDTO = riscossioneApiServiceHelper.verifyRiscossioniSTDebitori(idOggetto,
					indTipoOggetto, idTipoOper, idRiscossione,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(verifyRiscossioneStatoDebDTO).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response getCodiciUtenzaByIdStatoDebitorio(String idStatoDebitorioS, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) { 
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<String> codUtenza = new ArrayList<>(); 
		try {
			Integer  idStatoDebitorio = ValidationFilter.validateParameter("idStatoDebitorio", idStatoDebitorioS, 0, Integer.MAX_VALUE);
			codUtenza = riscossioneApiServiceHelper.getCodiciUtenzaByIdStatoDebitorio(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idStatoDebitorio);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(codUtenza).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

}
