package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.NazioniApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.NazioniApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.NazioniDTO;

@Component
public class NazioniApiServiceImpl extends AbstractApiServiceImpl implements NazioniApi{
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

	  @Autowired
	  private NazioniApiServiceHelper nazioniApiServiceHelper;


	    /**
	     * @param attivo attivo
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
	
	@Override
	public Response loadNazioni(boolean attivo,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<NazioniDTO> listTipiSede = new ArrayList<>();
		try {
			listTipiSede = nazioniApiServiceHelper.loadNazioni(attivo,  httpHeaders, httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listTipiSede).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}



}
