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

import it.csi.risca.riscaboweb.business.be.RataSdApi;
import it.csi.risca.riscaboweb.business.be.exception.DAOException;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RataSdDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class RataSdApiServiceHelper extends AbstractServiceHelper {

    public RataSdApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public RataSdDTO loadRataSdByStatoDebitorio(String fruitore, Integer idStatoDebitorio,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[RataSdApiServiceHelper::loadRataSdByStatoDebitorio] BEGIN");
            RataSdDTO result = new RataSdDTO();
            String targetUrl = this.endpointBase + "/rata-sd";
            boolean firstParam = true;
    		if (fruitore != null) {
    			targetUrl += "?fruitore=" + fruitore;
    			firstParam = false;
    		}
    		if (idStatoDebitorio != null) {
    			targetUrl += firstParam ? "?idStatoDebitorio=" + idStatoDebitorio
    					: "&idStatoDebitorio=" + idStatoDebitorio;
    			firstParam = false;
    		}
    			
    			
            try {

            	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            	Client client = null;
            	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
            		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
               }else {
                      client = ClientBuilder.newClient();
            	}
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                RataSdApi rataSdApi = rtarget.proxy(RataSdApi.class);
                Response resp = rataSdApi.loadRataSdByStatoDebitorio(fruitore, idStatoDebitorio.toString(),  httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[RataSdApiServiceHelper::loadRataSdByStatoDebitorio] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<RataSdDTO> rataSdType = new GenericType<RataSdDTO>() {
                };
                result = resp.readEntity(rataSdType);
            } catch (ProcessingException e) {
                LOGGER.error("[RataSdApiServiceHelper::loadRataSdByStatoDebitorio] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[RataSdApiServiceHelper::loadRataSdByStatoDebitorio] END");
            }
            return result;
        }
        
        public List<Integer> saveNRataSd(String fruitore, Integer idStatoDebitorio,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException, DAOException {
            LOGGER.debug("[RataSdApiServiceHelper::saveNRataSd] BEGIN");
            List<Integer> result = new ArrayList<>();
            String targetUrl;
            targetUrl = this.endpointBase + "/rata-sd/" + idStatoDebitorio;
            if(fruitore != null)
            	targetUrl += "?fruitore="+fruitore;
            try {

            	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            	Client client = null;
            	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
            		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
               }else {
                      client = ClientBuilder.newClient();
            	}
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                RataSdApi rataSdApi = rtarget.proxy(RataSdApi.class);
                Response resp = rataSdApi.saveNRataSd(fruitore, idStatoDebitorio.toString(),  httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[RataSdApiServiceHelper::saveNRataSd] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<Integer>> listRataSdType = new GenericType<List<Integer>>() {
                };
                result = resp.readEntity(listRataSdType);
            } catch (ProcessingException e) {
                LOGGER.error("[RataSdApiServiceHelper::saveNRataSd] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[RataSdApiServiceHelper::saveNRataSd] END");
            }
            return result;
        }
       
}
