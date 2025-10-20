package it.csi.risca.riscaboweb.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.AssegnaPagamentiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.AssegnaPagamentiApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AssegnaPagamentoDTO;

/**
 * The type AssegnaPagamentiApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class AssegnaPagamentiApiServiceImpl extends AbstractApiServiceImpl implements AssegnaPagamentiApi {
	
	private static final String IDENTITY = "identity";
    private final String className = this.getClass().getSimpleName();

    @Autowired
    private AssegnaPagamentiApiServiceHelper AssegnaPagamentiApiServiceHelper;

	@Override
	public Response AssegnaPagamentiPost(String fruitore, AssegnaPagamentoDTO assegnaPagamento,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
         LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	     AssegnaPagamentoDTO assegnaPagamentoDTO;
	        try {
	        	assegnaPagamentoDTO = AssegnaPagamentiApiServiceHelper.AssegnaPagamentiPost(fruitore, assegnaPagamento, httpHeaders, httpRequest);
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(assegnaPagamentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	
	}

	

}
