package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.DocumentiAllegatiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AcarisContentStreamType;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AllegatiDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ClassificazioniDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.Utils;


public class DocumentiAllegatiApiServiceHelper extends AbstractServiceHelper {

	private final String className = this.getClass().getSimpleName();
    private static final String LOCATION = "Location";
    public DocumentiAllegatiApiServiceHelper(String hostname, String hostnameNodo,  String endpointBase) {
        this.hostname = hostname;
        this.hostnameNodo = hostnameNodo;
        this.endpointBase = endpointBase;
    }
    
 // ATTENZIONE!!!! 
 // Il componente del servizio /classificazioni e' stato messo in scope Request
 // Lo scope request sul componen te NON e' compatibile con l'eventuale gestione asincrona dei servizi
 // Quindi non si puo' richiamare il servizio /classificazioni con parametro  asynch=true
    
    /*public List<ClassificazioniDTO> classificazioni(String fruitore, HttpHeaders httpHeaders,
                                                         HttpServletRequest httpRequest, String idRiscossione) throws GenericException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(getClassFunctionBeginInfo(className, methodName));
        String targetUrl = Utils.buildTargetUrl(
        		this.hostnameNodo+this.endpointBase + "/classificazioni/" + idRiscossione + "?asynch=true", Constants.FRUITORE, fruitore);
        
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName+" targetUrl: "+targetUrl));
        List<ClassificazioniDTO> result = null;
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response response = getInvocationBuilder(targetUrl, map).get();
            

            String locationJob = response.getHeaderString(LOCATION);
            
        	String targetUrlJob = null;
        	if(locationJob.startsWith(this.hostname)) {
                String location = locationJob.replace(this.hostname + endpointBase, "");
                targetUrlJob = this.hostnameNodo + this.endpointBase + location;
        	}else {
        		targetUrlJob =locationJob;
        	}
                MultivaluedMap<String, Object> mapJob = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
                boolean riprovare = true;
                while(riprovare) {
                	Response resp = getInvocationBuilder(targetUrlJob, mapJob).get();
                    if (resp.getStatus() == 202) {
                    	riprovare = true;
                    }
                    else if (resp.getStatus() == 200) {
                        GenericType<List<ClassificazioniDTO>> dtoType = new GenericType<List<ClassificazioniDTO>>() {};
                        result = resp.readEntity(dtoType);
                        riprovare = false;
                    }else {
                        handleResponseErrors(resp);     	
                        riprovare = false;
                    }
                }
      
        } catch (ProcessingException e) {
            LOGGER.info(getClassFunctionEndInfo(className, methodName));
        } finally {
            LOGGER.info(getClassFunctionEndInfo(className, methodName));
        }
        return result;
    }*/
    
	public List<ClassificazioniDTO> classificazioni(String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String idRiscossione) throws GenericException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.info(getClassFunctionBeginInfo(className, methodName));
		String targetUrl = Utils.buildTargetUrl(
				this.hostnameNodo + this.endpointBase + "/classificazioni/" + idRiscossione, Constants.FRUITORE,
				fruitore);

		LOGGER.debug(getClassFunctionBeginInfo(className, methodName + " targetUrl: " + targetUrl));
		List<ClassificazioniDTO> result = null;
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Response response = getInvocationBuilder(targetUrl, map).get();

			if (response.getStatus() == 200) {
				GenericType<List<ClassificazioniDTO>> dtoType = new GenericType<List<ClassificazioniDTO>>() {
				};
				result = response.readEntity(dtoType);
			} else {
				handleResponseErrors(response);
			}

		} catch (ProcessingException e) {
			LOGGER.info(getClassFunctionEndInfo(className, methodName));
		} finally {
			LOGGER.info(getClassFunctionEndInfo(className, methodName));
		}
		return result;
	}
    
    public List<AllegatiDTO> allegati(
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, String dbKeyClassificazione, String idRiscossione) throws GenericException {
        LOGGER.debug("[DocumentiAllegatiApiServiceHelper::allegati] BEGIN");
        List<AllegatiDTO> result = new ArrayList<>();
        String targetUrl = this.hostname + this.endpointBase + "/allegati/" + dbKeyClassificazione + "?idRiscossione=" + idRiscossione;
        try {
        	
        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
        	Client client = ClientBuilder.newClient();
        	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
        		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
           }
            WebTarget target = client.target(this.hostname + this.endpointBase);
            ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
            DocumentiAllegatiApi documentiAllegatiApi = rtarget.proxy(DocumentiAllegatiApi.class);
            Response resp = documentiAllegatiApi.allegati(dbKeyClassificazione, idRiscossione,  httpHeaders, httpRequest);
            LOGGER.debug("[DocumentiAllegatiApiServiceHelper::allegati] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.debug("[DocumentiAllegatiApiServiceHelper::allegati] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<List<AllegatiDTO>> dtoType = new GenericType<List<AllegatiDTO>>() {
            };
            result = resp.readEntity(dtoType);
        } catch (ProcessingException e) {
            LOGGER.debug("[DocumentiAllegatiApiServiceHelper::allegati] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[DocumentiAllegatiApiServiceHelper::allegati] END");
        }
        return result;
    }
    
    
    public AcarisContentStreamType actaContentStream(
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, String idClassificazione, String idRiscossione) throws GenericException {
        LOGGER.debug("[DocumentiAllegatiApiServiceHelper::actaContentStream] BEGIN");
        AcarisContentStreamType result = new AcarisContentStreamType();
        String targetUrl = this.hostname + this.endpointBase + "/actaContentStream/" + idClassificazione+ "?idRiscossione=" + idRiscossione;
        try {

        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
        	Client client = ClientBuilder.newClient();
        	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
        		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
           }
            WebTarget target = client.target(this.hostname + this.endpointBase);
            ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
            DocumentiAllegatiApi documentiAllegatiApi = rtarget.proxy(DocumentiAllegatiApi.class);
            Response resp = documentiAllegatiApi.actaContentStream(idClassificazione, idRiscossione,  httpHeaders, httpRequest);
            LOGGER.debug("[DocumentiAllegatiApiServiceHelper::actaContentStream] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.debug("[DocumentiAllegatiApiServiceHelper::actaContentStream] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<AcarisContentStreamType> dtoType = new GenericType<AcarisContentStreamType>() {
            };
            result = resp.readEntity(dtoType);
        } catch (ProcessingException e) {
            LOGGER.debug("[DocumentiAllegatiApiServiceHelper::actaContentStream] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[DocumentiAllegatiApiServiceHelper::actaContentStream] END");
        }
        return result;
    }
    


}
