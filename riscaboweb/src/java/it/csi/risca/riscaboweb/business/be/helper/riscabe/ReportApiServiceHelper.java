package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.LocationJobDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ReportResultDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RicercaMorositaDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RiscossioneSearchDTO;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ReportApiServiceHelper extends AbstractServiceHelper {
	
	private final String className = this.getClass().getSimpleName();
	
    private static final String IDENTITY = "identity";
    private static final String IN_PROGRESS = "IN_PROGRESS";
    private static final String LOCATION = "Location";
    private static final String COMPLETED = "COMPLETED";
    private static final String KO = "KO";
	private static final String FORMAT_DATE ="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public ReportApiServiceHelper(String hostname, String hostnameNodo, String endpointBase) {
        this.hostname = hostname;
        this.hostnameNodo = hostnameNodo;
        this.endpointBase = endpointBase;
    }

    public Response creaReportRicercaAvanzata(RiscossioneSearchDTO riscossioneSearch, String modalitaRicerca, String fruitore,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest)throws GenericException,ProcessingException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        String targetUrl = Utils.buildTargetUrl(this.hostnameNodo+this.endpointBase+"/report/ricerca-avanzata?asynch=true",Constants.FRUITORE, fruitore,"modalitaRicerca", modalitaRicerca);
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<RiscossioneSearchDTO> entity = Entity.json(riscossioneSearch);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
            LOGGER.debug(getClassFunctionDebugString(className, methodName," RESPONSE STATUS : " + resp.getStatus()));
            handleResponseErrors(resp);
            if (resp.getStatus() == 202) {
                LocationJobDTO locationJobDTO = createLocationJobDTO(resp);
                return Response.ok(locationJobDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
            }

            return resp;
        } catch (ProcessingException e) {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
            throw e;
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
    }

    public Response pollingJob(List<LocationJobDTO> listLocationJobDTO,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException, ProcessingException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<ReportResultDTO> listReportResult = new ArrayList<>();
        for (LocationJobDTO locationJobDTO : listLocationJobDTO) {
        	String targetUrl = null;
        	if(locationJobDTO.getLocation().startsWith(this.hostname)) {
                String location = locationJobDTO.getLocation().replace(this.hostname + endpointBase, "");
                targetUrl = this.hostnameNodo + this.endpointBase + location;
        	}else {
        		targetUrl =locationJobDTO.getLocation();
        	}

            try {
                MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
                Response resp = getInvocationBuilder(targetUrl, map).get();
                LOGGER.debug(getClassFunctionDebugString(className, methodName," RESPONSE STATUS : " + resp.getStatus()));
                ReportResultDTO result = createReportResult(resp, locationJobDTO);
                listReportResult.add(result);
            } catch (ProcessingException e) {
                LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
                throw e;
            } finally {
                LOGGER.debug(getClassFunctionEndInfo(className, methodName));
            }
        }
        return Response.ok(listReportResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
    }

    public Response creaReportRicercaMorosita(String fruitore, RicercaMorositaDTO ricercaMorositaDTO, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest) throws GenericException, ProcessingException, UnsupportedEncodingException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        try {
			String limEncoded = ricercaMorositaDTO.getLim() != null ? URLEncoder.encode(ricercaMorositaDTO.getLim(), StandardCharsets.UTF_8.toString()) : null;
			String targetUrl = Utils.buildTargetUrl(this.hostnameNodo+this.endpointBase+"/report/ricerca-morosita?asynch=true",Constants.FRUITORE, fruitore,"tipoRicercaMorosita", ricercaMorositaDTO.getTipoRicercaMorosita(),
            		"anno",ricercaMorositaDTO.getAnno(),"flgRest", ricercaMorositaDTO.getFlgRest(), "flgAnn", ricercaMorositaDTO.getFlgAnn(),"lim", limEncoded);
            
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
            LOGGER.debug(getClassFunctionDebugString(className, methodName," RESPONSE STATUS : " + resp.getStatus()));
            handleResponseErrors(resp);
            if (resp.getStatus() == 202) {
                LocationJobDTO locationJobDTO = createLocationJobDTO(resp);
                return Response.ok(locationJobDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
            }
            return resp;
        } catch (ProcessingException e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
            throw e;
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
            throw e;
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
    }

    public Response creaReportRicercaRimborsi(String fruitore, String tipoRicercaRimborsi, Integer anno,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest)
            throws GenericException, ProcessingException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        String targetUrl = Utils.buildTargetUrl(this.hostnameNodo+this.endpointBase+"/report/ricerca-rimborsi?asynch=true",Constants.FRUITORE, fruitore, "tipoRicercaRimborsi",tipoRicercaRimborsi,"anno", anno);

        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
            LOGGER.debug(getClassFunctionDebugString(className, methodName," RESPONSE STATUS : " + resp.getStatus()));
            handleResponseErrors(resp);
            if (resp.getStatus() == 202) {
                LocationJobDTO locationJobDTO = createLocationJobDTO(resp);
                return Response.ok(locationJobDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
            }
            return resp;
        } catch (ProcessingException e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
            throw e;
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }

    }
    
	public Response creaReportBilancio(String fruitore, Integer anno, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException, ProcessingException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		String targetUrl = Utils.buildTargetUrl(this.hostnameNodo + this.endpointBase + "/report/bilancio?asynch=true",
				Constants.FRUITORE, fruitore, "anno", anno);
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Response resp = getInvocationBuilder(targetUrl, map).get();
			LOGGER.debug(getClassFunctionDebugString(className, methodName, " RESPONSE STATUS : " + resp.getStatus()));
			handleResponseErrors(resp);
			if (resp.getStatus() == 202) {
				LocationJobDTO locationJobDTO = createLocationJobDTO(resp);
				return Response.ok(locationJobDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			}
			return resp;
		} catch (ProcessingException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}
    
    private LocationJobDTO createLocationJobDTO(Response response) {
        String location = response.getHeaderString(LOCATION);
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

        LocationJobDTO locationJobDTO = new LocationJobDTO();
        locationJobDTO.setLocation(location);

        try {
            SecureRandom secureRandom = new SecureRandom();
            long numeroRandomico;
            do {
                numeroRandomico = secureRandom.nextLong();
            } while (numeroRandomico <= 0);
            
            locationJobDTO.setIdRisca(numeroRandomico);

            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String timestamp = dateFormat.format(new Date());
            locationJobDTO.setTimestamp(timestamp);
        } catch (Exception e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
        }
        LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        return locationJobDTO;
    }


    private ReportResultDTO createReportResult(Response response, LocationJobDTO location) {
        ReportResultDTO result = new ReportResultDTO();
        if (response.getStatus() == 202) {
            result.setStatus(IN_PROGRESS);
        } else if (response.getStatus() == 200) {
            GenericType<ReportResultDTO> dtoType = new GenericType<ReportResultDTO>() {};
            result = response.readEntity(dtoType);
            result.setStatus(COMPLETED);
        }else {
        	  result.setStatus(KO);
        	  result.setErrorDTO(getError(response));        	  
        }
        result.setIdRisca(location.getIdRisca());
        result.setTimestamp(location.getTimestamp());
        return result;
    }
    
    public Response creaReportFile450RuoloRicercaMorosita(String fruitore, RicercaMorositaDTO ricercaMorositaDTO, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest) throws GenericException,ProcessingException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));  
        String targetUrl = Utils.buildTargetUrl(this.hostnameNodo+this.endpointBase+"/file-450/ruolo-ricerca-morosita?asynch=true",Constants.FRUITORE, fruitore,
        		"tipoRicercaMorosita", ricercaMorositaDTO.getTipoRicercaMorosita(),"anno",ricercaMorositaDTO.getAnno(),"flgRest",
        		ricercaMorositaDTO.getFlgRest(), "flgAnn", ricercaMorositaDTO.getFlgAnn(),"lim", ricercaMorositaDTO.getLim());
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<List<Long>> entity = Entity.json(ricercaMorositaDTO.getListIdStatoDebitorio());
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
            LOGGER.debug(getClassFunctionDebugString(className, methodName," RESPONSE STATUS : " + resp.getStatus()));
            handleResponseErrors(resp);
            if (resp.getStatus() == 202) {
                LocationJobDTO locationJobDTO = createLocationJobDTO(resp);
                return Response.ok(locationJobDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
            }

            return resp;
        } catch (ProcessingException e) {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
            throw e;
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
    }

}
