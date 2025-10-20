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

import it.csi.risca.riscaboweb.business.be.TipiProvvedimentoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiProvvedimentoExtendedDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class TipiProvvedimentoApiServiceHelper extends AbstractServiceHelper {

    public TipiProvvedimentoApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<TipiProvvedimentoExtendedDTO> getTipiProvvedimento( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipiProvvedimento] BEGIN");
            List<TipiProvvedimentoExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/tipi-provvedimentoistanza";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipiProvvedimentoApi tipiProvvedimentoApi = rtarget.proxy(TipiProvvedimentoApi.class);
                Response resp = tipiProvvedimentoApi.getTipiProvvedimento( httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiProvvedimentoApiServiceHelper::getTipiProvvedimento] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipiProvvedimentoExtendedDTO>> tipiProvvedimentoListType = new GenericType<List<TipiProvvedimentoExtendedDTO>>() {
                };
                result = resp.readEntity(tipiProvvedimentoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiProvvedimentoApiServiceHelper::getTipiProvvedimento] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipiProvvedimento] END");
            }
            return result;
        }
        
        public List<TipiProvvedimentoExtendedDTO> getTipiProvvedimentoByIdAmbitoAndFlgIstanza(Integer idAmbito, Integer flgIstanza, String dataIniVal, String dataFineVal,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipiProvvedimentoByIdAmbitoAndFlgIstanza] BEGIN");
            List<TipiProvvedimentoExtendedDTO> result = new ArrayList<>();
            String targetUrl ="";
            if(dataIniVal != null && dataFineVal != null) {
            	  targetUrl = this.endpointBase +"/ambiti/" + idAmbito + "/tipi-provvedimentoistanza?flgIstanza=" + flgIstanza+"&dataIniVal="+dataIniVal+"&dataFineVal="+dataFineVal;
   	          
            }else if(dataIniVal != null && dataFineVal == null) {
            	targetUrl = this.endpointBase + "/ambiti/" + idAmbito + "/tipi-provvedimentoistanza?flgIstanza=" + flgIstanza+"&dataIniVal="+dataIniVal;
      	      
            }else if(dataIniVal == null && dataFineVal != null) {
            	targetUrl = this.endpointBase +"/ambiti/" + idAmbito + "/tipi-provvedimentoistanza?flgIstanza=" + flgIstanza+"&dataFineVal="+dataFineVal;
      	      
            }else {
            	targetUrl = this.endpointBase + "/ambiti/" + idAmbito + "/tipi-provvedimentoistanza?flgIstanza=" + flgIstanza;
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
                TipiProvvedimentoApi tipiProvvedimentoApi = rtarget.proxy(TipiProvvedimentoApi.class);
                Response resp = tipiProvvedimentoApi.getTipiProvvedimentoByIdAmbitoAndFlgIstanza(idAmbito.toString(), flgIstanza.toString(), dataIniVal, dataFineVal,  httpHeaders, httpRequest);
                LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipiProvvedimentoByIdAmbitoAndFlgIstanza] RESPONSE STATUS : " + resp.getStatus());
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiProvvedimentoApiServiceHelper::getTipiProvvedimentoByIdAmbitoAndFlgIstanza] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipiProvvedimentoExtendedDTO>> tipiProvvedimentoListType = new GenericType<List<TipiProvvedimentoExtendedDTO>>() {
                };
                result = resp.readEntity(tipiProvvedimentoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiProvvedimentoApiServiceHelper::getTipiProvvedimentoByIdAmbitoAndFlgIstanza] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipiProvvedimentoByIdAmbitoAndFlgIstanza] END");
            }
            return result;
        }
    
        
        public TipiProvvedimentoExtendedDTO getTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito(Integer idTipoProvvedimento, Integer idAmbito,
				 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito] BEGIN");
            TipiProvvedimentoExtendedDTO result = new TipiProvvedimentoExtendedDTO();
            String targetUrl = this.endpointBase + "/tipi-provvedimentoistanza/id/" + idTipoProvvedimento + "/idAmbito/" + idAmbito;
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
                TipiProvvedimentoApi tipiProvvedimentoApi = rtarget.proxy(TipiProvvedimentoApi.class);
                Response resp = tipiProvvedimentoApi.getTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito(idTipoProvvedimento.toString(), idAmbito.toString(),
        				   httpHeaders, httpRequest);
                LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito] RESPONSE STATUS : " + resp.getStatus());
              if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiProvvedimentoApiServiceHelper::getTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TipiProvvedimentoExtendedDTO> tipiProvvedimentoListType = new GenericType<TipiProvvedimentoExtendedDTO>() {
                };
                result = resp.readEntity(tipiProvvedimentoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiProvvedimentoApiServiceHelper::getTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito] END");
            }
            return result;
        }
        
        public TipiProvvedimentoExtendedDTO getTipoProvvedimentoByCodeAndIdAmbito(String codOrIdTipoProvvedimento, Integer idAmbito,
				 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipoProvvedimentoByCodeAndIdAmbito] BEGIN");
            TipiProvvedimentoExtendedDTO result = new TipiProvvedimentoExtendedDTO();
            String targetUrl = this.endpointBase + "/ambiti/"+idAmbito+"/tipi-provvedimentoistanza/" + codOrIdTipoProvvedimento ;
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
                TipiProvvedimentoApi tipiProvvedimentoApi = rtarget.proxy(TipiProvvedimentoApi.class);
                Response resp = tipiProvvedimentoApi.getTipoProvvedimentoByCodeAndIdAmbito(codOrIdTipoProvvedimento, idAmbito.toString(), 
	    				  httpHeaders,  httpRequest);
                LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito] RESPONSE STATUS : " + resp.getStatus());
    
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiProvvedimentoApiServiceHelper::getTipoProvvedimentoByCodeAndIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TipiProvvedimentoExtendedDTO> tipiProvvedimentoListType = new GenericType<TipiProvvedimentoExtendedDTO>() {
                };
                result = resp.readEntity(tipiProvvedimentoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiProvvedimentoApiServiceHelper::getTipoProvvedimentoByCodeAndIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipoProvvedimentoByCodeAndIdAmbito] END");
            }
            return result;
        }
}
