package it.csi.risca.riscaboweb.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.UnitaMisuraApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.UnitaMisuraApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.UnitaMisuraDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Unita Misura api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class UnitaMisuraApiServiceImpl extends AbstractApiServiceImpl implements UnitaMisuraApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	private UnitaMisuraApiServiceHelper unitaMisuraApiServiceHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadUnitaMisura( HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		List<UnitaMisuraDTO> listUnitaMisura;
		try {
			listUnitaMisura = unitaMisuraApiServiceHelper.loadUnitaMisura( httpHeaders, httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listUnitaMisura).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idAmbito        idAmbito
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadUnitaMisuraByIdAmbito(String idAmbitoS,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<UnitaMisuraDTO> listUnitaMisura;
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);

			listUnitaMisura = unitaMisuraApiServiceHelper.loadUnitaMisuraByIdAmbito(idAmbito, 
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
		return Response.ok(listUnitaMisura).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param keyUnitaMisura  keyUnitaMisura
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadUnitaMisuraByKeyUnitaMisura(String keyUnitaMisura, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		UnitaMisuraDTO unitaMisura;
		try {
			unitaMisura = unitaMisuraApiServiceHelper.loadUnitaMisuraByKeyUnitaMisura(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), keyUnitaMisura);

		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(unitaMisura).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idUnitaMisura   idUnitaMisura
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadUnitaMisuraByIdUnitaMisura(String idUnitaMisura, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		UnitaMisuraDTO unitaMisura;
		try {
			unitaMisura = unitaMisuraApiServiceHelper.loadUnitaMisuraByIdUnitaMisura(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idUnitaMisura);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(unitaMisura).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
