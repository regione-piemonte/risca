package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.RegioniApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.RegioniApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RegioneExtendedDTO;

@Component
public class RegioniApiServiceImpl extends AbstractApiServiceImpl implements RegioniApi {
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

	@Autowired
	RegioniApiServiceHelper regioniApiServiceHelper;

	@Override
	public Response loadRegioni(  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<RegioneExtendedDTO> listRegioni = new ArrayList<>();
        try {
        	listRegioni = regioniApiServiceHelper.loadRegioni(httpHeaders,httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listRegioni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadRegioneByCodRegione(String codRegione,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 	
		 RegioneExtendedDTO  regione = new RegioneExtendedDTO();
        try {
        	regione = regioniApiServiceHelper.loadRegioneByCodRegione(codRegione,  httpHeaders, httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(regione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
