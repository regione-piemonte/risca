package it.csi.risca.riscaboweb.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.LocalLogoutApi;

/**
 * The type Local Logout api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class LocalLogoutApiServiceImpl extends AbstractApiServiceImpl implements LocalLogoutApi {
	
	 private final String className = this.getClass().getSimpleName();


	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return 
	     * @return Response
	     */
		@Override
		public void localLogout( HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
			String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			try {
				httpRequest.getSession().invalidate();
	        } catch (ProcessingException e) {
	        	 handleException(e, className, methodName);   
            } finally {
            	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		}


	
}
