package it.csi.risca.riscaboweb.business.be.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.ReportApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.ReportApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.LocationJobDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RicercaMorositaDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RuoloMorositaSearchDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RiscossioneSearchDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type ReportApiServiceImpl .
 *
 * @author CSI PIEMONTE
 */
@Component
public class ReportApiServiceImpl extends AbstractApiServiceImpl implements ReportApi {
   
	 private final String className = this.getClass().getSimpleName();

	
	@Autowired
    ReportApiServiceHelper reportApiServiceHelper;

    @Override
    public Response creaReportRicercaAvanzata(RiscossioneSearchDTO riscossioneSearch, String modalitaRicerca,
            String fruitore, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
    	String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    	LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        try {
            return reportApiServiceHelper.creaReportRicercaAvanzata(riscossioneSearch, modalitaRicerca, fruitore,
                    httpHeaders, httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
    }

    @Override
    public Response pollingJob(List<LocationJobDTO> locationJobDTO, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest) {
    	String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    	LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        try {
            return reportApiServiceHelper.pollingJob(locationJobDTO, httpHeaders, httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
    }

    @Override
    public Response creaReportRicercaMorosita(String fruitore, String tipoRicercaMorosita, String annoS, String flgRestS,
    		String flgAnnS, String lim, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
    	String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    	LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        try {
			Integer  flgAnn = ValidationFilter.validateParameter("flgAnn", flgAnnS, 0, 1);
			Integer  flgRest = ValidationFilter.validateParameter("flgRest", flgRestS, 0, 1);
			Integer  anno = null;
			if(annoS != null)
				anno = ValidationFilter.validateParameter("anno", annoS, 0, Integer.MAX_VALUE);
			
            RicercaMorositaDTO ricercaMorositaDTO = new RicercaMorositaDTO(tipoRicercaMorosita, anno, flgRest,
                    flgAnn, lim);
            return reportApiServiceHelper.creaReportRicercaMorosita(fruitore, ricercaMorositaDTO, httpHeaders,
                    httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } catch (UnsupportedEncodingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
    }

    @Override
    public Response creaReportRicercaRimborsi(String fruitore, String tipoRicercaRimborsi, String annoS,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
    	String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    	LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        try {
			Integer  anno = null;
			if(annoS != null)
				anno = ValidationFilter.validateParameter("anno", annoS, 0, Integer.MAX_VALUE);
			
            return reportApiServiceHelper.creaReportRicercaRimborsi(fruitore, tipoRicercaRimborsi, anno, httpHeaders,
                    httpRequest);
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
    
	@Override
	public Response creaReportBilancio(String fruitore, String annoS, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Integer anno = null;
			if (annoS != null)
				anno = ValidationFilter.validateParameter("anno", annoS, 0, Integer.MAX_VALUE);

			return reportApiServiceHelper.creaReportBilancio(fruitore, anno, httpHeaders, httpRequest);
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
	
	 @Override
	    public Response creaReportFile450RuoloRicercaMorosita(String fruitore, String tipoRicercaMorosita, String annoS, String flgRestS,
	    		String flgAnnS, String lim,List<Long> listIdStatiDebitoriSelezionati, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	    	String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    	LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	        try {
				Integer  flgAnn = ValidationFilter.validateParameter("flgAnn", flgAnnS, 0, 1);
				Integer  flgRest = ValidationFilter.validateParameter("flgRest", flgRestS, 0, 1);
				Integer  anno = null;
				if(annoS != null)
					anno = ValidationFilter.validateParameter("anno", annoS, 0, Integer.MAX_VALUE);				
				
	            RicercaMorositaDTO ricercaMorositaDTO = new RicercaMorositaDTO(tipoRicercaMorosita, anno, flgRest,
	            		flgAnn, lim, listIdStatiDebitoriSelezionati );
				
				
	            return reportApiServiceHelper.creaReportFile450RuoloRicercaMorosita(fruitore, ricercaMorositaDTO, httpHeaders,
	                    httpRequest);
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
