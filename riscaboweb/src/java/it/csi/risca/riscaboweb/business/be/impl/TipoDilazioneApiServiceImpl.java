package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.TipoDilazioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipoDilazioneApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoDilazioneDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;



/**
 * The type Tipo Dilazione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoDilazioneApiServiceImpl extends AbstractApiServiceImpl implements TipoDilazioneApi {
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

    @Autowired
    TipoDilazioneApiServiceHelper tipoDilazioneApiServiceHelper;



	    /**
	     * @param idTipoDilazione idTipoDilazione
	     * @param idAmbito    idAmbito
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */

		@Override
		public Response loadTipoDilazioneByIdAmbitoAndIdTipoDilazione(String idAmbitoS, String idTipoDilazioneS,
				 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
             List<TipoDilazioneDTO> tipoDilazione = new ArrayList<>();
	        try {
				Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);
				Integer  idTipoDilazione = null;
				if(idTipoDilazioneS != null)
					idTipoDilazione = ValidationFilter.validateParameter("idTipoDilazione", idTipoDilazioneS, 0, Integer.MAX_VALUE);

	        	tipoDilazione = tipoDilazioneApiServiceHelper.loadTipoDilazioneByIdAmbitoAndIdTipoDilazione(idAmbito, idTipoDilazione, 
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
	        return Response.ok(tipoDilazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

}
