package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.TipoRicercaMorositaApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipoRicercaMorositaApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoRicercaMorositaDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type TipoRicercaMorositaApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoRicercaMorositaApiServiceImpl extends AbstractApiServiceImpl implements TipoRicercaMorositaApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	private TipoRicercaMorositaApiServiceHelper tipoRicercaMorositaApiServiceHelper;

	@Override
	public Response loadAllTipoRicercaMorosita( HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<TipoRicercaMorositaDTO> tipiRicercaMorosita = new ArrayList<>();
		try {
			tipiRicercaMorosita = tipoRicercaMorositaApiServiceHelper.loadAllTipoRicercaMorosita(
					httpHeaders, httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(tipiRicercaMorosita).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response ricercaMorosita(String fruitore, String tipoRicercaMorosita, String annoS, String flgRestS,
			String flgAnnS, String lim, String offsetS, String limitS, String sort, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Integer  anno = null;
			if(annoS != null)
				anno = ValidationFilter.validateParameter("anno", annoS, 0, Integer.MAX_VALUE);
			Integer  flgRest = ValidationFilter.validateParameter("flgRest", flgRestS, 0, 1);
			Integer  flgAnn = ValidationFilter.validateParameter("flgAnn", flgAnnS, 0, 1);
			
			Integer  offset = ValidationFilter.validateParameter("offset", offsetS, 0, Integer.MAX_VALUE);
			Integer  limit = ValidationFilter.validateParameter("limit", limitS, 0, Integer.MAX_VALUE);

			return tipoRicercaMorositaApiServiceHelper.ricercaMorosita(fruitore, tipoRicercaMorosita, anno, flgRest,
					flgAnn, lim, offset, limit, sort,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

	}

}
