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

import it.csi.risca.riscaboweb.business.be.AmbitiConfigApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AmbitoConfigDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class AmbitiConfigApiServiceHelper extends AbstractServiceHelper {

    public AmbitiConfigApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<AmbitoConfigDTO> loadAmbitiConfig( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[AmbitiConfigApiServiceHelper::loadAmbitiConfig] BEGIN");
            List<AmbitoConfigDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/ambiti";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                AmbitiConfigApi ambitiConfigApi = rtarget.proxy(AmbitiConfigApi.class);
                Response resp = ambitiConfigApi.loadAmbitiConfig( httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[AmbitiConfigApiServiceHelper::loadAmbitiConfig] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<AmbitoConfigDTO>> ambitiConfigListType = new GenericType<List<AmbitoConfigDTO>>() {
                };
                result = resp.readEntity(ambitiConfigListType);
            } catch (ProcessingException e) {
                LOGGER.error("[AmbitiConfigApiServiceHelper::loadAmbitiConfig] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[AmbitiConfigApiServiceHelper::loadAmbitiConfig] END");
            }
            return result;
        }
        
        public List<AmbitoConfigDTO> loadAmbitiConfigByIdOrCodAmbito(String idAmbito, 
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[AmbitiConfigApiServiceHelper::loadAmbitiConfigByIdOrCodAmbito] BEGIN");
            List<AmbitoConfigDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/ambiti-config/" + idAmbito;
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
                AmbitiConfigApi ambitiConfigApi = rtarget.proxy(AmbitiConfigApi.class);
                Response resp = ambitiConfigApi.loadAmbitiConfigByIdOrCodAmbito(idAmbito, 
        				 httpHeaders,  httpRequest);
                LOGGER.debug("[AmbitiConfigApiServiceHelper::loadAmbitiConfigByIdOrCodAmbito] RESPONSE STATUS : " + resp.getStatus());
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[AmbitiConfigApiServiceHelper::loadAmbitiConfigByIdOrCodAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<AmbitoConfigDTO>> ambitiConfigListType = new GenericType<List<AmbitoConfigDTO>>() {
                };
                result = resp.readEntity(ambitiConfigListType);
            } catch (ProcessingException e) {
                LOGGER.error("[AmbitiConfigApiServiceHelper::loadAmbitiConfigByIdOrCodAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[AmbitiConfigApiServiceHelper::loadAmbitiConfigByIdOrCodAmbito] END");
            }
            return result;
        }
		public List<AmbitoConfigDTO> loadUnitaMisuraByIdAmbito(
				MultivaluedMap<String, Object> multivaluedMapFromHttpHeaders, String idAmbito) {
			// TODO Auto-generated method stub
			return null;
		}   
        
        public List<AmbitoConfigDTO> loadAmbitiConfigByCodeAndKey(MultivaluedMap<String, Object> requestHeaders, String codAmbito, String chiave) throws GenericException {
            LOGGER.debug("[AmbitiConfigApiServiceHelper::loadAmbitiConfigByCodeAndKey] BEGIN");
            List<AmbitoConfigDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/ambiti-config/" + codAmbito + "/chiave/" + chiave;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[AmbitiConfigApiServiceHelper::loadAmbitiConfigByCodeAndKey] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<AmbitoConfigDTO>> ambitiConfigListType = new GenericType<List<AmbitoConfigDTO>>() {
                };
                result = resp.readEntity(ambitiConfigListType);
            } catch (ProcessingException e) {
                LOGGER.error("[AmbitiConfigApiServiceHelper::loadAmbitiConfigByCodeAndKey] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[AmbitiConfigApiServiceHelper::loadAmbitiConfigByCodeAndKey] END");
            }
            return result;
        }   
        
}
