package it.csi.risca.riscaboweb.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.ElaboraApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.ElaboraApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ElaboraDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ElaboraExtendedDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Elabora api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class ElaboraApiServiceImpl extends AbstractApiServiceImpl implements ElaboraApi {

	 private final String className = this.getClass().getSimpleName();

	@Autowired
	ElaboraApiServiceHelper elaboraApiServiceHelper;

	@Override
	public Response loadElabora(String fruitore, String idAmbito, List<String> codTipoElabora,
			List<String> codStatoElabora, String dataRichiestaInizio, String dataRichiestaFine, String codFunzionalita,
			String offsets, String limits, String sort, String flgVisibileS, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Response resp;
		try { 
			Integer  limit = ValidationFilter.validateParameter("limits", limits, 0,Integer.MAX_VALUE);
			Integer  offset = ValidationFilter.validateParameter("offset", offsets, 0, Integer.MAX_VALUE);
			Integer  flgVisibile = ValidationFilter.validateParameter("flgVisibile", flgVisibileS, 0, 1);
			LOGGER.debug("[ElaboraApiServiceImpl::loadElabora] --> codFunzionalita = "+codFunzionalita);
			resp = elaboraApiServiceHelper.loadElabora(fruitore, httpHeaders, httpRequest, idAmbito, codTipoElabora,
					codStatoElabora, dataRichiestaInizio, dataRichiestaFine, codFunzionalita, offset, limit, sort,
					flgVisibile);

			 } catch (ParameterValidationException e) {
		        	return handleException(e, className, methodName);   
	          } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return resp;
	}
	
	@Override
	public Response loadElaboraById(String idElaboraS, Boolean download, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		Response resp;
		try {
			Integer  idElabora = ValidationFilter.validateParameter("idElabora", idElaboraS, 0,Integer.MAX_VALUE);
			resp = elaboraApiServiceHelper.loadElaboraById(idElabora, download,   httpHeaders, httpRequest);
	        
			 } catch (ParameterValidationException e) {
		        	return handleException(e, className, methodName);   
	          } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return resp;
	}

	@Override
	public Response saveElabora(ElaboraDTO elabora, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		ElaboraDTO dto = new ElaboraDTO();
		try {
			dto = elaboraApiServiceHelper.saveElabora(elabora,fruitore,  httpHeaders,
					 httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

		return Response.ok(dto).status(201).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response updateElabora(ElaboraExtendedDTO elabora, String fruitore,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		ElaboraDTO dto = new ElaboraDTO();
		try {
			dto = elaboraApiServiceHelper.updateElabora(elabora,fruitore,httpHeaders,httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

		return Response.ok(dto).status(201).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response verifyElabora(String idAmbito, String verifica,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		List<ElaboraDTO> verifyElabora;
		try {
			verifyElabora = elaboraApiServiceHelper.verifyElabora(idAmbito,
					verifica,  httpHeaders, httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
//		if(verifyElabora.size() > 0)
		return Response.ok(verifyElabora).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
//		else
//			return Response.ok(false).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response loadElaboraByCF(String codiceFiscale, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			List<ElaboraDTO> listElabora = elaboraApiServiceHelper.loadElaboraByCF(codiceFiscale, fruitore,  httpHeaders, httpRequest);
			return Response.ok(listElabora).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}



}
