package it.csi.risca.riscaboweb.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.SoggettiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.SoggettiApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.SoggettiExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.SoggettiExtendedResponseDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type soggetto api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class SoggettiApiServiceImpl extends AbstractApiServiceImpl implements SoggettiApi {

	private final String CLASSNAME = this.getClass().getSimpleName();

	@Autowired
	private SoggettiApiServiceHelper soggettiApiServiceHelper;

	@Override
	public Response loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg(String idAmbitoS, String idTipoSoggettoS, String cfSoggetto,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) { 
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));
		SoggettiExtendedResponseDTO dto = new SoggettiExtendedResponseDTO();
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);

			Integer  idTipoSoggetto = ValidationFilter.validateParameter("idTipoSoggetto", idTipoSoggettoS, 0, Integer.MAX_VALUE);

			dto = soggettiApiServiceHelper.loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg(idAmbito, idTipoSoggetto,
					cfSoggetto,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, CLASSNAME, methodName);   

        }  catch (GenericException e) {
			return handleException(e, CLASSNAME, methodName);
		} catch (ProcessingException e) {
			return handleException(e, CLASSNAME, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
		}

		return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response loadSoggettoById(String idSoggettoS, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));
		SoggettiExtendedDTO dto = new SoggettiExtendedDTO(); 
		try {
			Integer  idSoggetto = ValidationFilter.validateParameter("idSoggetto", idSoggettoS, 0, Integer.MAX_VALUE);
			dto = soggettiApiServiceHelper.loadSoggettoById(idSoggetto, fruitore,  httpHeaders,
					httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, CLASSNAME, methodName);   

        }  catch (GenericException e) {
			return handleException(e, CLASSNAME, methodName);
		} catch (ProcessingException e) {
			return handleException(e, CLASSNAME, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
		}

		return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response saveSoggetto(SoggettiExtendedDTO soggetto, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));
		try {
			return soggettiApiServiceHelper.saveSoggetto(soggetto, fruitore,  httpHeaders, httpRequest);

		} catch (GenericException e) {
			if (e.getError() != null) {
				LOGGER.error(getClassFunctionErrorInfo(CLASSNAME, methodName, e),e);
				return Response.serverError().entity(e.getError()).status(400).build();
			} else {
				LOGGER.error(getClassFunctionErrorInfo(CLASSNAME, methodName, e),e);
				return Response.serverError().entity(e.getErroObjectDTO())
						.status(Integer.parseInt(e.getErroObjectDTO().getStatus())).build();
			}
		} catch (ProcessingException e) {
			return handleException(e, CLASSNAME, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
		}
	}

	@Override
	public Response updateSoggetto(SoggettiExtendedDTO soggetto, String fruitore, String indModManualeS,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) { 
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));
		try {
			Integer  indModManuale = null;
			if(indModManualeS != null)
				indModManuale = ValidationFilter.validateParameter("indModManuale", indModManualeS, 0, Integer.MAX_VALUE);

			return soggettiApiServiceHelper.updateSoggetto(soggetto, fruitore, indModManuale, 
					httpHeaders, httpRequest);

		} catch (ParameterValidationException e) {
        	return handleException(e, CLASSNAME, methodName);   

        } catch (GenericException e) {
			if (e.getError() != null) {
				LOGGER.error(getClassFunctionErrorInfo(CLASSNAME, methodName, e),e);
				return Response.serverError().entity(e.getError()).status(400).build();
			} else {
				LOGGER.error(getClassFunctionErrorInfo(CLASSNAME, methodName, e),e);
				return Response.serverError().entity(e.getErroObjectDTO())
						.status(Integer.parseInt(e.getErroObjectDTO().getStatus())).build();
			}
		} catch (ProcessingException e) {
			return handleException(e, CLASSNAME, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
		}

	}

	@Override
	public Response deleteSoggetto(String idSoggettoS, String fruitore, String idRecapitoS, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));
		SoggettiExtendedDTO soggetto = new SoggettiExtendedDTO();
		try {
			Integer  idSoggetto = ValidationFilter.validateParameter("idSoggetto", idSoggettoS, 0, Integer.MAX_VALUE);
			Integer  idRecapito = null;
			if(idRecapitoS != null)
			  idRecapito = ValidationFilter.validateParameter("idRecapito", idRecapitoS, 0, Integer.MAX_VALUE);
 
			soggetto = soggettiApiServiceHelper.deleteSoggetto(idSoggetto, fruitore, idRecapito, 
					httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, CLASSNAME, methodName);   

        } catch (GenericException e) {
			return handleException(e, CLASSNAME, methodName);
		} catch (ProcessingException e) {
			return handleException(e, CLASSNAME, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
		}

		return Response.ok(soggetto).status(201).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response loadSoggettiByCampoRicerca(String idAmbitoS, String campoRicerca, String offsetS, String limitS,
			String sort,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName)); 

		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);

			Integer  offset = ValidationFilter.validateParameter("offset", offsetS, 0, Integer.MAX_VALUE);

			Integer  limit = ValidationFilter.validateParameter("limit", limitS, 0, Integer.MAX_VALUE);

			return soggettiApiServiceHelper.loadSoggettiByCampoRicerca(idAmbito, campoRicerca, offset, limit, sort,
					 httpHeaders, httpRequest);

		} catch (ParameterValidationException e) {
        	return handleException(e, CLASSNAME, methodName);   

        }  catch (GenericException e) {
			return handleException(e, CLASSNAME, methodName);
		} catch (ProcessingException e) {
			return handleException(e, CLASSNAME, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
		}

	}

}
