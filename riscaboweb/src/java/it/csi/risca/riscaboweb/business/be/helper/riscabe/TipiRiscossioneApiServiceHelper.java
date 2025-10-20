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

import org.apache.log4j.MDC;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.TipiRiscossioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoRiscossioneExtendedDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.util.Constants;

public class TipiRiscossioneApiServiceHelper extends AbstractServiceHelper {

    public TipiRiscossioneApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<TipoRiscossioneExtendedDTO> getTipiRiscossione(String XRequestId, String XForwardedFor,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiRiscossioneApiServiceHelper::getTipiRiscossione] BEGIN");
            List<TipoRiscossioneExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/tipi-riscossione";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipiRiscossioneApi tipiRiscossioneApi = rtarget.proxy(TipiRiscossioneApi.class);
                Response resp = tipiRiscossioneApi.getTipiRiscossione((String) MDC.get(Constants.X_REQUEST_ID), (String) MDC.get(Constants.X_FORWARDER_FOR),  httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiRiscossioneApiServiceHelper::getTipiRiscossione] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipoRiscossioneExtendedDTO>> tipiRiscossioneListType = new GenericType<List<TipoRiscossioneExtendedDTO>>() {
                };
                result = resp.readEntity(tipiRiscossioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiRiscossioneApiServiceHelper::getTipiRiscossione] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiRiscossioneApiServiceHelper::getTipiRiscossione] END");
            }
            return result;
        }
        
        public List<TipoRiscossioneExtendedDTO> getTipiRiscossioneByIdAmbito(Integer idAmbito, String dataIniVal, String dataFineVal, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiRiscossioneApiServiceHelper::getTipiRiscossioneByIdAmbito] BEGIN");
            List<TipoRiscossioneExtendedDTO> result = new ArrayList<>();
            String targetUrl ="";
            if(dataIniVal != null && dataFineVal != null) {
            	targetUrl = this.endpointBase + "/ambiti/"+idAmbito+"/tipi-riscossione?dataIniVal="+dataIniVal+"&dataFineVal="+dataFineVal;
                     
            }else if(dataIniVal != null && dataFineVal == null) {
            	targetUrl = this.endpointBase + "/ambiti/"+idAmbito+"/tipi-riscossione?dataIniVal="+dataIniVal;
                       
            }else if(dataIniVal == null && dataFineVal != null) {
            	targetUrl = this.endpointBase + "/ambiti/"+idAmbito+"/tipi-riscossione?dataFineVal="+dataFineVal;
                     
            }else {
            	targetUrl = this.endpointBase + "/ambiti/"+idAmbito+"/tipi-riscossione";
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
                TipiRiscossioneApi tipiRiscossioneApi = rtarget.proxy(TipiRiscossioneApi.class);
                Response resp = tipiRiscossioneApi.getTipiRiscossioneByIdAmbitoAndDateValidita(idAmbito.toString(), dataIniVal, dataFineVal,  httpHeaders, httpRequest);
                  LOGGER.debug("[TipiRiscossioneApiServiceHelper::getTipiRiscossioneByIdAmbito] RESPONSE STATUS : " + resp.getStatus());
      
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiRiscossioneApiServiceHelper::getTipiRiscossioneByIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipoRiscossioneExtendedDTO>> tipiRiscossioneListType = new GenericType<List<TipoRiscossioneExtendedDTO>>() {
                };
                result = resp.readEntity(tipiRiscossioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiRiscossioneApiServiceHelper::getTipiRiscossioneByIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiRiscossioneApiServiceHelper::getTipiRiscossioneByIdAmbito] END");
            }
            return result;
        }
    
        
        public TipoRiscossioneExtendedDTO getTipoRiscossioneByIdTipoRiscossioneAndIdAmbito(String idOrCodTipoRiscossione, Integer idAmbito,
				 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiRiscossioneApiServiceHelper::getTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] BEGIN");
            TipoRiscossioneExtendedDTO result = new TipoRiscossioneExtendedDTO();
            String targetUrl = this.endpointBase +"/ambiti/"+idAmbito+"/tipi-riscossione/"+idOrCodTipoRiscossione;
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
                TipiRiscossioneApi tipiRiscossioneApi = rtarget.proxy(TipiRiscossioneApi.class);
                Response resp = tipiRiscossioneApi.getTipoRiscossioneByIdTipoRiscossioneAndIdAmbito(idOrCodTipoRiscossione, idAmbito.toString(),
	    				  httpHeaders,  httpRequest);
                  LOGGER.debug("[TipiRiscossioneApiServiceHelper::getTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] RESPONSE STATUS : " + resp.getStatus());
      
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiRiscossioneApiServiceHelper::getTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TipoRiscossioneExtendedDTO> tipiRiscossioneListType = new GenericType<TipoRiscossioneExtendedDTO>() {
                };
                result = resp.readEntity(tipiRiscossioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiRiscossioneApiServiceHelper::getTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiRiscossioneApiServiceHelper::getTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] END");
            }
            return result;
        }
        
        public TipoRiscossioneExtendedDTO getTipoRiscossioneByCodeAndIdAmbito(String codTipoRiscossione, Integer idAmbito,
				 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiRiscossioneApiServiceHelper::getTipoRiscossioneByCodeAndIdAmbito] BEGIN");
            TipoRiscossioneExtendedDTO result = new TipoRiscossioneExtendedDTO();
            String targetUrl = this.endpointBase + "/tipi-riscossione/codice/" + codTipoRiscossione + "/id-ambito/" + idAmbito;
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
                TipiRiscossioneApi tipiRiscossioneApi = rtarget.proxy(TipiRiscossioneApi.class);
                Response resp = tipiRiscossioneApi.getTipoRiscossioneByIdTipoRiscossioneAndIdAmbito(codTipoRiscossione, idAmbito.toString(),
        				  httpHeaders, httpRequest);
                  LOGGER.debug("[TipiRiscossioneApiServiceHelper::getTipoRiscossioneByCodeAndIdAmbito] RESPONSE STATUS : " + resp.getStatus());
      
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiRiscossioneApiServiceHelper::getTipoRiscossioneByCodeAndIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TipoRiscossioneExtendedDTO> tipiRiscossioneListType = new GenericType<TipoRiscossioneExtendedDTO>() {
                };
                result = resp.readEntity(tipiRiscossioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiRiscossioneApiServiceHelper::getTipoRiscossioneByCodeAndIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiRiscossioneApiServiceHelper::getTipoRiscossioneByCodeAndIdAmbito] END");
            }
            return result;
        }
}
