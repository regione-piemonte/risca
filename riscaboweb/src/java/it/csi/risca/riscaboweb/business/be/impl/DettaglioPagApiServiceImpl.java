package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.DettaglioPagApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.DettaglioPagApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.DettaglioPagDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.DettaglioPagListDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type DettaglioPag api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class DettaglioPagApiServiceImpl extends AbstractApiServiceImpl implements DettaglioPagApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	private DettaglioPagApiServiceHelper dettaglioPagApiServiceHelper;

	@Override
	public Response getDettaglioPagByIdRiscossioneAndIdSD(String fruitore, String idRiscossioneS,
			String idStatoDebitorioS, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		Response response = null;
		try {
			Integer idRiscossione = ValidationFilter.validateParameter("idRiscossione", idRiscossioneS, 0,
					Integer.MAX_VALUE);
			Integer idStatoDebitorio = ValidationFilter.validateParameter("idStatoDebitorio", idStatoDebitorioS, 0,
					Integer.MAX_VALUE);
			response = dettaglioPagApiServiceHelper.getDettaglioPagByIdRiscossioneAndIdSD(fruitore, idRiscossione,
					idStatoDebitorio, httpHeaders, httpRequest);

		} catch (ParameterValidationException e) {
			return handleException(e, className, methodName);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return response;
	}

	@Override
	public Response deleteDettaglioPagByIdDettaglioPag(String idDettaglioPagS, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Response response = null;
		try {
			Integer idDettaglioPag = ValidationFilter.validateParameter("idDettaglioPag", idDettaglioPagS, 0,
					Integer.MAX_VALUE);
			response = dettaglioPagApiServiceHelper.deleteDettaglioPagByIdDettaglioPag(fruitore, idDettaglioPag,
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
		return response;
	}

	@Override
	public Response saveDettaglioPag(String fruitore, DettaglioPagDTO dettaglioPag, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		DettaglioPagDTO dettaglioPagExtendedDTO = new DettaglioPagDTO();
		try {
			dettaglioPagExtendedDTO = dettaglioPagApiServiceHelper.saveDettaglioPag(fruitore, dettaglioPag, httpHeaders,
					httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(dettaglioPagExtendedDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveDettaglioPagList(String fruitore, DettaglioPagListDTO dettaglioPagList, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<DettaglioPagDTO> dettaglioPagExtendedDTO = new ArrayList<>();
		try {
			dettaglioPagExtendedDTO = dettaglioPagApiServiceHelper.saveDettaglioPagList(fruitore, dettaglioPagList,
					httpHeaders, httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(dettaglioPagExtendedDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
