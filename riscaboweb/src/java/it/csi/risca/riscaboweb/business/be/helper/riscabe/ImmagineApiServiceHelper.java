package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.ImmagineApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ElaboraDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ImmagineDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class ImmagineApiServiceHelper  extends AbstractServiceHelper {

    public ImmagineApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
	public ImmagineDTO saveImmagine(ImmagineDTO immagine, String fruitore, 
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[ImmagineApiServiceHelper::saveImmagine] BEGIN");
	    ImmagineDTO result = new ImmagineDTO();
        String targetUrl = this.endpointBase + "/immagine" ;
        if(fruitore != null)
        	targetUrl += "?fruitore="+fruitore;
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<ImmagineDTO> entity = Entity.json(immagine);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.error("[ImmagineApiServiceHelper::saveImmagine] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
			GenericType<ImmagineDTO> immagineDTO = new GenericType<ImmagineDTO>() {
			};
			result = resp.readEntity(immagineDTO);
		} catch (ProcessingException e) {
			LOGGER.error("[ImmagineApiServiceHelper::saveImmagine] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[ImmagineApiServiceHelper::saveImmagine] END");
		}
		return result;
	}
	public ImmagineDTO loadImmagineById(String fruitore, Integer idImmagine, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		    LOGGER.debug("[ImmagineApiServiceHelper::loadImmagineById] BEGIN");
		    ImmagineDTO result = new ImmagineDTO();
	        StringBuilder targetUrlBuilder = new StringBuilder(this.endpointBase + "/immagine");
		   	 if (fruitore != null) {
			     targetUrlBuilder.append("?")
			                    .append("fruitore=").append(fruitore);
			 }
	        if (idImmagine != null) {
	            targetUrlBuilder.append(targetUrlBuilder.toString().contains("?") ? "&" : "?")
	                           .append("idImmagine=").append(idImmagine);
	        }
	        String targetUrl = targetUrlBuilder.toString();
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
	            ImmagineApi immagineApi = rtarget.proxy(ImmagineApi.class);
	            Response resp = immagineApi.loadImmagineById(fruitore, idImmagine.toString(),  httpHeaders, httpRequest);
	            if (resp.getStatus() >= 400) {
	                ErrorDTO err = getError(resp);
	                LOGGER.error("[ImmagineApiServiceHelper::loadImmagineById] SERVER EXCEPTION : " + err);
	                throw new GenericException(err);
	            }
	            GenericType<ImmagineDTO> pagamentoType = new GenericType<ImmagineDTO>() {};
				result = resp.readEntity(pagamentoType);
	        } catch (ProcessingException e) {
	            LOGGER.error("[ImmagineApiServiceHelper::loadImmagineById] EXCEPTION : " + e);
	            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
	        } finally {
	            LOGGER.debug("[ImmagineApiServiceHelper::loadImmagineById] END");
	        }
	        return result;
	}

}
