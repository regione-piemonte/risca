package it.csi.risca.riscaboweb.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.IuvApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.IuvApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.IuvDTO;

/**
 * The type IUV api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class IuvApiServiceImpl  extends AbstractApiServiceImpl implements IuvApi {

	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

	@Autowired
	private IuvApiServiceHelper iuvApiServiceHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */


	@Override
	public Response getIuvByNap(String fruitore,String nap,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		IuvDTO iuv = new IuvDTO();
		try {
			iuv = iuvApiServiceHelper.getIuvByNap(fruitore, nap, httpHeaders, httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(iuv).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
