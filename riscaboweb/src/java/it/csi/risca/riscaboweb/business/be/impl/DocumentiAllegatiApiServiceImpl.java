package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.DocumentiAllegatiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.DocumentiAllegatiApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AcarisContentStreamType;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AllegatiDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ClassificazioniDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Documenti allegati api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class DocumentiAllegatiApiServiceImpl extends AbstractApiServiceImpl implements DocumentiAllegatiApi {
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

    @Autowired
    private DocumentiAllegatiApiServiceHelper documentiAllegatiApiServiceHelper;

		@Override
		public Response classificazioni(String idRiscossione, String fruitore, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
			 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			 List<ClassificazioniDTO> listClassificazioni = null;
	        
	        try {
	        	listClassificazioni = documentiAllegatiApiServiceHelper.classificazioni(fruitore,  httpHeaders, httpRequest, idRiscossione);
	            
			 } catch (ParameterValidationException e) {
		        	return handleException(e, className, methodName);   
	          } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(listClassificazioni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		@Override
		public Response allegati(String dbKeyClassificazione, String idRiscossione, 
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	        List<AllegatiDTO> listAllegati = new ArrayList<>();
	        try {
	        	listAllegati = documentiAllegatiApiServiceHelper.allegati(httpHeaders, httpRequest, dbKeyClassificazione, idRiscossione);
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(listAllegati).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		@Override
		public Response actaContentStream(String idClassificazione,  String idRiscossione, 
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	        AcarisContentStreamType contentStreamType; // CORREGGERE
	        try {
	        	contentStreamType = documentiAllegatiApiServiceHelper.actaContentStream( httpHeaders, httpRequest, idClassificazione, idRiscossione);
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(contentStreamType).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}


}
