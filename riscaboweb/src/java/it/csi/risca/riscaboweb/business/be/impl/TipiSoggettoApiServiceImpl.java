package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.TipiSoggettoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipiSoggettoApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiSoggettoDTO;

/**
 * The type Tipi soggetto api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiSoggettoApiServiceImpl extends AbstractApiServiceImpl implements TipiSoggettoApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	private TipiSoggettoApiServiceHelper tipiSoggettoApiServiceHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */

	@Override
	public Response loadTipiSoggetto( HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<TipiSoggettoDTO> listTipiSoggetto = new ArrayList<>();
		try {
			listTipiSoggetto = tipiSoggettoApiServiceHelper.loadTipiSoggetto( httpHeaders, httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listTipiSoggetto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idTipoSoggetto  idTipoSoggetto
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadTipiSoggettoByIdOrCodTipoSoggetto(String idTipoSoggetto, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		TipiSoggettoDTO tipoSoggetto = new TipiSoggettoDTO();
		try {
			tipoSoggetto = tipiSoggettoApiServiceHelper.loadTipiSoggettoByIdOrCodTipoSoggetto(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idTipoSoggetto);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(tipoSoggetto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
