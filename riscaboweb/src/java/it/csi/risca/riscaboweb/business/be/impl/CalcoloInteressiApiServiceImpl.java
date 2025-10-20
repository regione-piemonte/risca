package it.csi.risca.riscaboweb.business.be.impl;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.CalcoloInteressiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.CalcoloInteressiApiServiceHelper;

/**
 * The type Calcolo Interessi api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class CalcoloInteressiApiServiceImpl extends AbstractApiServiceImpl implements CalcoloInteressiApi {
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

    @Autowired
    private CalcoloInteressiApiServiceHelper calcoloInteressiApiServiceHelper;
	@Override
	public Response calcoloInteressi(String fruitore, BigDecimal importo, String dataScadenza, String dataVersamento,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	        BigDecimal sommaInteressiDovuto;
	        try {
	        	sommaInteressiDovuto = calcoloInteressiApiServiceHelper.calcoloInteressi(fruitore, importo, dataScadenza, dataVersamento,  httpHeaders, httpRequest);
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(sommaInteressiDovuto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
