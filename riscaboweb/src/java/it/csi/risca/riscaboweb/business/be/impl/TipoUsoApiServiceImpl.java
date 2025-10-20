package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.TipoUsoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipoUsoApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Tipo uso api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoUsoApiServiceImpl extends AbstractApiServiceImpl implements TipoUsoApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	private TipoUsoApiServiceHelper tipoUsoApiServiceHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadTipiUso( HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<TipoUsoExtendedDTO> listTipiUso = new ArrayList<>();
		try {
			listTipiUso = tipoUsoApiServiceHelper.loadTipiUso( httpHeaders, httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listTipiUso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idAmbito        idAmbito
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
//		@Override
//		public Response loadTipiUsoByIdAmbito(Long idAmbito,String  dataIniVal,String dataFineVal,  HttpHeaders httpHeaders,
//				HttpServletRequest httpRequest) {
//	        LOGGER.debug("[TipiUsoApiServiceImpl::loadTipiUsoByIdAmbito] BEGIN");
//	        LOGGER.debug("[TipiUsoApiServiceImpl::loadTipiUsoByIdAmbito] Parametro in input idAmbito [" + idAmbito + "]");
//	        List<TipoUsoExtendedDTO> listTipiUso = new ArrayList<>();
//	        try {
//	        	listTipiUso = tipoUsoApiServiceHelper.loadTipiUsoByIdAmbito(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idAmbito, dataIniVal, dataFineVal);
//	        } catch (GenericException e) {
//	            ErrorDTO err = e.getError();
//	            LOGGER.error("[TipiUsoApiServiceImpl::loadTipiUsoByIdAmbito] ERROR : " + err);
//	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
//	        } catch (ProcessingException e) {
//	            String errorMessage = e.getMessage();
//	            ErrorDTO err = new ErrorDTO("500", "E100", errorMessage, null, null);
//	            LOGGER.error("[TipiUsoApiServiceImpl::loadTipiUsoByIdAmbito] ERROR : " + e);
//	            return Response.serverError().entity(err).status(500).build();
//	        } finally {
//	            LOGGER.debug("[TipiUsoApiServiceImpl::loadTipiUsoByIdAmbito] END");
//	        }
//	        return Response.ok(listTipiUso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
//
//		}

	/**
	 * @param idTipoUsoPadre  idTipoUsoPadre
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadTipoUsoByIdTipoUsoPadre(String idTipoUsoPadre, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<TipoUsoExtendedDTO> tipoUso = new ArrayList<>();
		try {
			tipoUso = tipoUsoApiServiceHelper.loadTipoUsoByIdTipoUsoPadre(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idTipoUsoPadre);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(tipoUso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	/**
	 * @param idTipoUso       idTipoUso
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadTipoUsoByIdTipoUsoOrCodTipoUso(String idTipoUso, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		TipoUsoExtendedDTO tipoUso = new TipoUsoExtendedDTO();
		try {
			tipoUso = tipoUsoApiServiceHelper.loadTipoUsoByIdTipoUsoOrCodTipoUso(
					getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idTipoUso);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(tipoUso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param codTipoUso      codTipoUso
	 * @param idAmbito        idAmbito
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadTipoUsoByCodeAndIdAmbito(String codTipoUso, String idAmbitoS, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) { 
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		TipoUsoExtendedDTO tipoUso = new TipoUsoExtendedDTO();
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);

			tipoUso = tipoUsoApiServiceHelper.loadTipoUsoByCodeAndIdAmbito(codTipoUso, idAmbito, 
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
		return Response.ok(tipoUso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	/**
	 * @param idTipoUsoPadre  idTipoUsoPadre
	 * @param idAmbito        idAmbito
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadTipoUsoByIdTipoUsoPadreAndIdAmbito(String idTipoUsoPadre, String idAmbitoS, String dataIniVal,
			String dataFineVal,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<TipoUsoExtendedDTO> tipoUso = new ArrayList<>(); 
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);

			tipoUso = tipoUsoApiServiceHelper.loadTipoUsoByIdTipoUsoPadreAndIdAmbito(idTipoUsoPadre, idAmbito,
					dataIniVal, dataFineVal,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(tipoUso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

}
