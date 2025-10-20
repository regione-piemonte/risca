package it.csi.risca.riscaboweb.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.RiduzioneAumentoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.RiduzioneAumentoApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RiduzioneAumentoDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Riduzione Aumento api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class RiduzioneAumentoApiServiceImpl extends AbstractApiServiceImpl implements RiduzioneAumentoApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	private RiduzioneAumentoApiServiceHelper riduzioneAumentoApiServiceHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadRiduzioneAumento( HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<RiduzioneAumentoDTO> listRiduzioneAumento;
		try {
			listRiduzioneAumento = riduzioneAumentoApiServiceHelper.loadRiduzioneAumento( httpHeaders,
					httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listRiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idAmbito        idAmbito
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadRiduzioneAumentoByIdAmbito(String idAmbitoS, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<RiduzioneAumentoDTO> listRiduzioneAumento;
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);
			listRiduzioneAumento = riduzioneAumentoApiServiceHelper.loadRiduzioneAumentoByIdAmbito(idAmbito,
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
		return Response.ok(listRiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idTipoUso       idTipoUso
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadRiduzioneAumentoByIdTipoUso(String idTipoUsoS,  
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<RiduzioneAumentoDTO> listRiduzioneAumento;
		try {
			Integer  idTipoUso = ValidationFilter.validateParameter("idTipoUso", idTipoUsoS, 0, Integer.MAX_VALUE);

			listRiduzioneAumento = riduzioneAumentoApiServiceHelper.loadRiduzioneAumentoByIdTipoUso(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idTipoUso);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listRiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idTipoUso       idTipoUso
	 * @param flgRidAum       flgRidAum
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum(String idTipoUsoS, String flgRidAum,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) { 
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<RiduzioneAumentoDTO> listRiduzioneAumento;
		try {
			Integer  idTipoUso = ValidationFilter.validateParameter("idTipoUso", idTipoUsoS, 0, Integer.MAX_VALUE);
			listRiduzioneAumento = riduzioneAumentoApiServiceHelper.loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idTipoUso, flgRidAum);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listRiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idRiduzioneAumento idRiduzioneAumento
	 * @param securityContext    SecurityContext
	 * @param httpHeaders        HttpHeaders
	 * @param httpRequest        HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadRiduzioneAumentoByIdRiduzioneAumento(String idRiduzioneAumentoS, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		RiduzioneAumentoDTO riduzioneAumento;
		try {
			Integer  idRiduzioneAumento = ValidationFilter.validateParameter("idRiduzioneAumento", idRiduzioneAumentoS, 0, Integer.MAX_VALUE);
			riduzioneAumento = riduzioneAumentoApiServiceHelper.loadRiduzioneAumentoByIdRiduzioneAumento(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idRiduzioneAumento);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        } catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(riduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idOrCodRiduzioneAumento idOrCodRiduzioneAumento
	 * @param securityContext         SecurityContext
	 * @param httpHeaders             HttpHeaders
	 * @param httpRequest             HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadRiduzioniByIdOrCodRiduzioneAumento(String idOrCodRiduzioneAumento, String dataIniVal,
			String dataFineVal,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		RiduzioneAumentoDTO riduzioneAumento;
		try {
			riduzioneAumento = riduzioneAumentoApiServiceHelper.loadRiduzioniByIdOrCodRiduzioneAumento(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idOrCodRiduzioneAumento, dataIniVal,
					dataFineVal);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(riduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idOrCodRiduzioneAumento idOrCodRiduzioneAumento
	 * @param dataIniVal              data inizio validita
	 * @param dataFinVal              data fine validita
	 * @param securityContext         SecurityContext
	 * @param httpHeaders             HttpHeaders
	 * @param httpRequest             HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadAumentoByIdOrCodRiduzioneAumento(String idOrCodRiduzioneAumento, String dataIniVal,
			String dataFineVal,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		RiduzioneAumentoDTO riduzioneAumento;
		try {
			riduzioneAumento = riduzioneAumentoApiServiceHelper.loadAumentoByIdOrCodRiduzioneAumento(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idOrCodRiduzioneAumento, dataIniVal,
					dataFineVal);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(riduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadRiduzioniByIdOrCodTipoUsoFlgManuale(String idOrCodTipoUso, String flgManuale, String dataIniVal,
			String dataFineVal,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<RiduzioneAumentoDTO> listRiduzioneAumento;
		try {
			listRiduzioneAumento = riduzioneAumentoApiServiceHelper.loadRiduzioniByIdOrCodTipoUsoFlgManuale(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idOrCodTipoUso, flgManuale, dataIniVal,
					dataFineVal);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listRiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadAumentiByIdOrCodTipoUsoFlgManuale(String idOrCodTipoUso, String flgManuale, String dataIniVal,
			String dataFineVal,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<RiduzioneAumentoDTO> listRiduzioneAumento;
		try {
			listRiduzioneAumento = riduzioneAumentoApiServiceHelper.loadAumentiByIdOrCodTipoUsoFlgManuale(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idOrCodTipoUso, flgManuale, dataIniVal,
					dataFineVal);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listRiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	@Override
	public Response loadRiduzioniByflgManuale(String flgManuale, String dataIniVal, String dataFineVal,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<RiduzioneAumentoDTO> listRiduzioneAumento;
		try {
			listRiduzioneAumento = riduzioneAumentoApiServiceHelper.loadRiduzioniByflgManuale(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), flgManuale, dataIniVal, dataFineVal);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listRiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	@Override
	public Response loadAumentiByflgManuale(String flgManuale, String dataIniVal, String dataFineVal,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<RiduzioneAumentoDTO> listRiduzioneAumento;
		try {
			listRiduzioneAumento = riduzioneAumentoApiServiceHelper.loadAumentiiByflgManuale(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), flgManuale, dataIniVal, dataFineVal);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listRiduzioneAumento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

}
