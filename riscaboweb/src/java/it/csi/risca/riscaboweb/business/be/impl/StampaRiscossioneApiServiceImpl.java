package it.csi.risca.riscaboweb.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.StampaRiscossioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.StampaRiscossioneApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.FileDownloadDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Stampa Riscossione Api Service Impl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class StampaRiscossioneApiServiceImpl extends AbstractApiServiceImpl implements StampaRiscossioneApi {

	private final String className = this.getClass().getSimpleName();

	@Autowired
	private StampaRiscossioneApiServiceHelper stampaRiscossioneApiServiceHelper;

	@Override
	public Response stampaRiscossione(String idRiscossioneS,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		FileDownloadDTO fileDownloadDTO = null;
		try {
			Integer  idRiscossione = ValidationFilter.validateParameter("idRiscossione", idRiscossioneS, 0, Integer.MAX_VALUE);

			fileDownloadDTO = stampaRiscossioneApiServiceHelper.stampaRiscossione(idRiscossione, 
					httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(fileDownloadDTO).header(HttpHeaders.CONTENT_ENCODING, "identity").build();

	}

}
