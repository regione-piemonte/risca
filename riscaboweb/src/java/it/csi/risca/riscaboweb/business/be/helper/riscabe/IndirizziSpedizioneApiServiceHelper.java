package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import java.util.List;

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

import it.csi.risca.riscaboweb.business.be.IndirizziSpedizioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.GenericExceptionList;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ImmagineDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class IndirizziSpedizioneApiServiceHelper extends AbstractServiceHelper{

	   public IndirizziSpedizioneApiServiceHelper(String hostname, String endpointBase) {
	        this.hostname = hostname;
	        this.endpointBase = hostname + endpointBase;
	    }
	   
	   
	   
	   public  IndirizzoSpedizioneDTO saveIndirizziSpedizione(IndirizzoSpedizioneDTO indSped, 
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
	        LOGGER.debug("[IndirizziSpedizioneApiServiceHelper::saveIndirizziSpedizione] BEGIN");
	        IndirizzoSpedizioneDTO result = new IndirizzoSpedizioneDTO();
	        String targetUrl = null ;
	        
	        targetUrl = this.endpointBase + "/indirizzi-spedizione";
	        try {
				MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
	            Entity<IndirizzoSpedizioneDTO> entity = Entity.json(indSped);
	            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
	            LOGGER.debug("[IndirizziSpedizioneApiServiceHelper::saveIndirizziSpedizione] RESPONSE STATUS : " + resp.getStatus());
	            if (resp.getStatus() >= 400) {
	                ErrorDTO err = getError(resp);
	                LOGGER.debug("[IndirizziSpedizioneApiServiceHelper::saveIndirizziSpedizione] SERVER EXCEPTION : " + err);
	                throw new GenericException(err);
	            }
	            GenericType<IndirizzoSpedizioneDTO> dtoType = new GenericType<IndirizzoSpedizioneDTO>() {
	            };
	            result = resp.readEntity(dtoType);
	        } catch (ProcessingException e) {
	            LOGGER.debug("[IndirizziSpedizioneApiServiceHelper::saveIndirizziSpedizione] EXCEPTION : " + e);
	            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
	        } finally {
	            LOGGER.debug("[IndirizziSpedizioneApiServiceHelper::saveIndirizziSpedizione] END");
	        }
	        return result;
	    }
	   
	   
	   public  IndirizzoSpedizioneDTO updateIndirizziSpedizione(IndirizzoSpedizioneDTO indSped, Integer modalitaVerifica, 
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericExceptionList {
	        LOGGER.debug("[IndirizziSpedizioneApiServiceHelper::updateIndirizziSpedizione] BEGIN");
	        IndirizzoSpedizioneDTO result = new IndirizzoSpedizioneDTO();
	        String targetUrl = null;
	        if(modalitaVerifica != null)
	            targetUrl = this.endpointBase + "/indirizzi-spedizione?modalitaVerifica="+modalitaVerifica;
	        else
	        	targetUrl = this.endpointBase + "/indirizzi-spedizione";
	        try {
	        	
				MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
	            Entity<IndirizzoSpedizioneDTO> entity = Entity.json(indSped);
	            Response resp = getInvocationBuilder(targetUrl, map).put(entity);
	            LOGGER.debug("[IndirizziSpedizioneApiServiceHelper::updateIndirizziSpedizione] RESPONSE STATUS : " + resp.getStatus());
	            if (resp.getStatus() >= 400) {
	                List<ErrorDTO> err = getErrors(resp);
	                LOGGER.debug("[IndirizziSpedizioneApiServiceHelper::updateIndirizziSpedizione] SERVER EXCEPTION : " + err);
	                throw new GenericExceptionList(err);
	            }
	            GenericType<IndirizzoSpedizioneDTO> dtoType = new GenericType<IndirizzoSpedizioneDTO>() {
	            };
	            result = resp.readEntity(dtoType);
	        } catch (ProcessingException e) {
	            LOGGER.debug("[IndirizziSpedizioneApiServiceHelper::updateIndirizziSpedizione] EXCEPTION : " + e);
	            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
	        } finally {
	            LOGGER.debug("[IndirizziSpedizioneApiServiceHelper::updateIndirizziSpedizione] END");
	        }
	        return result;
	    }
		
}
