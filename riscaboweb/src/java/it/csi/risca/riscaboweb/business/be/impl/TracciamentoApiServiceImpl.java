package it.csi.risca.riscaboweb.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.TracciamentoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TracciamentoApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TracciamentoDTO;

@Component
public class TracciamentoApiServiceImpl extends AbstractApiServiceImpl implements TracciamentoApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	private TracciamentoApiServiceHelper tracciamentoApiServiceHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response saveTracciamento(TracciamentoDTO tracciamento, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		TracciamentoDTO trac = new TracciamentoDTO();
		try {
			trac = tracciamentoApiServiceHelper.saveTracciamento(tracciamento,  httpHeaders,
					httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(trac).status(201).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
