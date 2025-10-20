package it.csi.risca.riscaboweb.business.be.impl;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.TassiDiInteresseApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.PingApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TassiDiInteresseApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.DettaglioPagDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TassiDiInteresseDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type TassiDiInteresseApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component 
public class TassiDiInteresseApiServiceImpl extends AbstractApiServiceImpl implements TassiDiInteresseApi {

    private final String className = this.getClass().getSimpleName();

    private static final String IDENTITY = "identity";

    @Autowired
    private TassiDiInteresseApiServiceHelper tassiDiInteresseApiServiceHelper;
    

    @Override
    public Response loadTassiDiInteresse(
    		String idAmbito, 
    		String fruitore,
    		String tipoDiInteresse,
    		String offsets, 
    		String limits, 
    		String sort, 
    		HttpHeaders httpHeaders, 
    		HttpServletRequest httpRequest) {
    	
    	Response resp;
    	
    	try {
    		Integer limit = ValidationFilter.validateParameter("limits", limits, 0,Integer.MAX_VALUE);
			Integer offset = ValidationFilter.validateParameter("offsets", offsets, 0, Integer.MAX_VALUE);
        	return tassiDiInteresseApiServiceHelper.loadTassiDiInteresse(
        			idAmbito, 
        			fruitore, 
        			tipoDiInteresse,
        			offset, 
        			limit, 
        			sort,
        			getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest));
        	
        } catch (Exception e) {
            // Log dell'eccezione completa
            LOGGER.error("Errore durante il caricamento dei tassi di interesse: " + e.getMessage(), e);
            // Restituzione di una risposta di errore
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore durante il caricamento dei tassi di interesse: " + e.getMessage()).build();
        }
    }
    
    @Override
	public Response saveTassiDiInteresse(TassiDiInteresseDTO tassiDiInteresse, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
    	String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		TassiDiInteresseDTO tassiDiInteresseDTO = new TassiDiInteresseDTO();
		try {
			tassiDiInteresseDTO = tassiDiInteresseApiServiceHelper.savetassiDiInteresse(tassiDiInteresse, fruitore,
				securityContext, httpHeaders, httpRequest);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		
		return Response.ok(tassiDiInteresseDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
    }

	@Override
	public Response deleteTassiDiInteresse(
			String fruitore, String idAmbitoInteresseS, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		
		try {
			Integer idAmbitoInteresse = ValidationFilter.validateParameter("idAmbitoInteresse", idAmbitoInteresseS, 0, Integer.MAX_VALUE);
			
			return tassiDiInteresseApiServiceHelper.deleteTassiDiInteresse(
					fruitore, 
					idAmbitoInteresse,
					httpHeaders, 
					httpRequest);
			
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		}
	}

	@Override
	public Response updateTassiDiInteresse(TassiDiInteresseDTO tassiDiInteresse,
			String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws ParseException {
				
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		TassiDiInteresseDTO tassiDiInteresseDTO = new TassiDiInteresseDTO();
		try {
			tassiDiInteresseDTO = tassiDiInteresseApiServiceHelper.updateTassiDiInteresse(
					tassiDiInteresse,
					fruitore,
					securityContext,
					httpHeaders,
					httpRequest);
			
		} catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
		}

		return Response.ok(tassiDiInteresseDTO).status(201).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
}
