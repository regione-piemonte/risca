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

import it.csi.risca.riscaboweb.business.be.UnitaMisuraApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.UnitaMisuraDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class UnitaMisuraApiServiceHelper extends AbstractServiceHelper {

    public UnitaMisuraApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<UnitaMisuraDTO> loadUnitaMisura( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[UnitaMisuraApiServiceHelper::loadUnitaMisura] BEGIN");
            List<UnitaMisuraDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/unita-misura";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                UnitaMisuraApi unitaMisuraApi = rtarget.proxy(UnitaMisuraApi.class);
                Response resp = unitaMisuraApi.loadUnitaMisura( httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[UnitaMisuraApiServiceHelper::loadUnitaMisura] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<UnitaMisuraDTO>> unitaMisuraListType = new GenericType<List<UnitaMisuraDTO>>() {
                };
                result = resp.readEntity(unitaMisuraListType);
            } catch (ProcessingException e) {
                LOGGER.error("[UnitaMisuraApiServiceHelper::loadUnitaMisura] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[UnitaMisuraApiServiceHelper::loadUnitaMisura] END");
            }
            return result;
        }
        
        public List<UnitaMisuraDTO> loadUnitaMisuraByIdAmbito(Integer idAmbito,  HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByIdAmbito] BEGIN");
            List<UnitaMisuraDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/unita-misura/idAmbito/" + idAmbito;
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
                UnitaMisuraApi unitaMisuraApi = rtarget.proxy(UnitaMisuraApi.class);
                Response resp = unitaMisuraApi.loadUnitaMisuraByIdAmbito(idAmbito.toString(),  httpHeaders, httpRequest);
                  LOGGER.debug("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByIdAmbito] RESPONSE STATUS : " + resp.getStatus());
      
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<UnitaMisuraDTO>> unitaMisuraListType = new GenericType<List<UnitaMisuraDTO>>() {
                };
                result = resp.readEntity(unitaMisuraListType);
            } catch (ProcessingException e) {
                LOGGER.error("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByIdAmbito] END");
            }
            return result;
        }   
        
        public UnitaMisuraDTO loadUnitaMisuraByKeyUnitaMisura(MultivaluedMap<String, Object> requestHeaders, String keyUnitaMisura) throws GenericException {
            LOGGER.debug("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByKeyUnitaMisura] BEGIN");
            UnitaMisuraDTO result = new UnitaMisuraDTO();
            String targetUrl = this.endpointBase + "/unita-misura/keyUnitaMisura/" + keyUnitaMisura;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByKeyUnitaMisura] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<UnitaMisuraDTO> unitaMisuraType = new GenericType<UnitaMisuraDTO>() {
                };
                result = resp.readEntity(unitaMisuraType);
            } catch (ProcessingException e) {
                LOGGER.error("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByKeyUnitaMisura] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByKeyUnitaMisura] END");
            }
            return result;
        } 
        
        public UnitaMisuraDTO loadUnitaMisuraByIdUnitaMisura(MultivaluedMap<String, Object> requestHeaders, String idUnitaMisura) throws GenericException {
            LOGGER.debug("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByIdUnitaMisura] BEGIN");
            UnitaMisuraDTO result = new UnitaMisuraDTO();
            String targetUrl = this.endpointBase + "/unita-misura/" + idUnitaMisura;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByIdUnitaMisura] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<UnitaMisuraDTO> unitaMisuraListType = new GenericType<UnitaMisuraDTO>() {
                };
                result = resp.readEntity(unitaMisuraListType);
            } catch (ProcessingException e) {
                LOGGER.error("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByIdUnitaMisura] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[UnitaMisuraApiServiceHelper::loadUnitaMisuraByIdUnitaMisura] END");
            }
            return result;
        } 
        
}
