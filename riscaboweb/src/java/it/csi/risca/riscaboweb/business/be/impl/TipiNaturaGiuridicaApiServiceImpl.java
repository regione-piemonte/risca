package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.TipiNaturaGiuridicaApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipiNaturaGiuridicaApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiNaturaGiuridicaDTO;

/**
 * The type Tipi natura giuridica api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiNaturaGiuridicaApiServiceImpl extends AbstractApiServiceImpl implements TipiNaturaGiuridicaApi {
	
	private static final String IDENTITY = "identity";
	 private final String CLASSNAME = this.getClass().getSimpleName();

	  @Autowired
	  private TipiNaturaGiuridicaApiServiceHelper tipiNaturaGiuridicaApiServiceHelper;

	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipiNaturaGiuridica( HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
			 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			 LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));
			List<TipiNaturaGiuridicaDTO> listTipiNaturaGiuridica = new ArrayList<>();
			try {
				listTipiNaturaGiuridica = tipiNaturaGiuridicaApiServiceHelper.loadTipiNaturaGiuridica( httpHeaders, httpRequest);
	        } catch (GenericException e) {
	        	return handleException(e, CLASSNAME, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, CLASSNAME, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
	        }
	        return Response.ok(listTipiNaturaGiuridica).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	        
		}

	    /**
	     * @param idTipoNaturaGiuridica idTipoNaturaGiuridica
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipoNaturaGiuridicaByIdOrCod(String idTipoNaturaGiuridica, 
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			 LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));
             TipiNaturaGiuridicaDTO tipoNaturaGiuridica = new TipiNaturaGiuridicaDTO();
			try {
				tipoNaturaGiuridica = tipiNaturaGiuridicaApiServiceHelper.loadTipoNaturaGiuridicaByIdOrCod(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idTipoNaturaGiuridica);
	        } catch (GenericException e) {
	        	return handleException(e, CLASSNAME, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, CLASSNAME, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
	        }
	        return Response.ok(tipoNaturaGiuridica).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
}
