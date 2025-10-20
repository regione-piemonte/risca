package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.AttivitaStatoDebitorioApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.AttivitaStatoDebitorioApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AttivitaStatoDebitorioDTO;

/**
 * The type Attivita Stato Debitorio api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class AttivitStatoDebitorioApiServiceImpl  extends AbstractApiServiceImpl  implements AttivitaStatoDebitorioApi {

	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

	@Autowired
	private AttivitaStatoDebitorioApiServiceHelper attivitaHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */


	@Override
	public Response getAttivitaStatoDeb(String tipoAttivita,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		 List<AttivitaStatoDebitorioDTO> listAttivita = new ArrayList<>();
		try {
			listAttivita = attivitaHelper.getAttivitaStatoDeb(tipoAttivita, httpHeaders, httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listAttivita).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		
	}

}
