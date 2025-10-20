package it.csi.risca.riscaboweb.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.GruppiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.GruppiApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.GruppiDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

@Component
public class GruppiImpl extends AbstractApiServiceImpl implements GruppiApi {
	 private final String className = this.getClass().getSimpleName();

	@Autowired
    private GruppiApiServiceHelper gruppiApiServiceHelper;

	
	@Override
	public Response loadGruppiSoggetto(String fruitore,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 	
        try {
        	return gruppiApiServiceHelper.loadGruppiSoggetto(fruitore, httpHeaders,httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
	}
	
	@Override
	public Response loadGruppiById(String fruitore,String codGruppo, String desGruppo,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

        try {
        	return gruppiApiServiceHelper.loadGruppiById(fruitore, codGruppo, desGruppo, httpHeaders,httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
	}
	
	@Override
	public Response saveGruppi(GruppiDTO gruppi,String fruitore,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        Response resp;
        try {
        	resp = gruppiApiServiceHelper.saveGruppi(gruppi,fruitore, httpHeaders,httpRequest);       	
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
	public Response updateGruppi(GruppiDTO gruppi,String fruitore,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        Response resp;
        try {
        	resp = gruppiApiServiceHelper.updateGruppi(gruppi,fruitore, httpHeaders,httpRequest);     	
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
	public Response deleteGruppi(String fruitore,String idGruppoS,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        Response resp;
        try {
			Integer  idGruppo = ValidationFilter.validateParameter("idGruppo", idGruppoS, 0,Integer.MAX_VALUE);
        	resp = gruppiApiServiceHelper.deleteGruppi(fruitore, idGruppo, httpHeaders,httpRequest);
        	
            
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
	public Response loadGruppiByIdAmbitoAndCampoRicerca(String fruitore, String idAmbitoS, String campoRicerca, String flgTipoRicerca,
			 String offsets,  String limits,  String sort,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			try {
				Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0,Integer.MAX_VALUE);
				Integer  limit = ValidationFilter.validateParameter("limits", limits, 0,Integer.MAX_VALUE);
				Integer  offset = ValidationFilter.validateParameter("offset", offsets, 0, Integer.MAX_VALUE);
				return gruppiApiServiceHelper.loadGruppiByIdAmbitoAndCampoRicerca(fruitore, idAmbito,  campoRicerca,  flgTipoRicerca,
						  offset,   limit,   sort,
							   httpHeaders,  httpRequest);
		        
			 } catch (ParameterValidationException e) {
		        	return handleException(e, className, methodName);   
	          } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }

	}
	

}
