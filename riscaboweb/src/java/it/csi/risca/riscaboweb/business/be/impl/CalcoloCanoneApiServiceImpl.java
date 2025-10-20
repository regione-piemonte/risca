package it.csi.risca.riscaboweb.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.CalcoloCanoneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.CalcoloCanoneApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CalcoloCanoneDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CalcoloCanoneSingoloDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ambiente.RiscossioneDatoTecnicoDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Calcolo Canone api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class CalcoloCanoneApiServiceImpl extends AbstractApiServiceImpl implements CalcoloCanoneApi {
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

    @Autowired
    private CalcoloCanoneApiServiceHelper calcoloCanoneApiServiceHelper;

	    /**
	     * @param idRiscossione idRiscossione
	     * @param dataRiferimento dataRiferimento
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response calcoloCanone(String idRiscossiones, String dataRiferimento,  HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
			 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

			LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	        CalcoloCanoneDTO canone;
	        try {
				Integer  idRiscossione = ValidationFilter.validateParameter("idRiscossione", idRiscossiones, 0,Integer.MAX_VALUE);
	        	canone = calcoloCanoneApiServiceHelper.calcoloCanone(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idRiscossione, dataRiferimento);
			 } catch (ParameterValidationException e) {
		        	return handleException(e, className, methodName);   
	        
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(canone).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		}

		@Override
		public Response calcoloCanoneSingoloEFrazionato(RiscossioneDatoTecnicoDTO datoTecnico, String dataRiferimento, String dataFrazionamento, String flgFraz,
				 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	        CalcoloCanoneSingoloDTO canone;
	        try {
	        	canone = calcoloCanoneApiServiceHelper.calcoloCanoneSingoloEFrazionato(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), datoTecnico, dataRiferimento, dataFrazionamento, flgFraz);
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(canone).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
}
