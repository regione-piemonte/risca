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

import it.csi.risca.riscaboweb.business.be.TipiInvioApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiInvioDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class TipiInvioApiServiceHelper extends AbstractServiceHelper {

    public TipiInvioApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<TipiInvioDTO> loadTipiInvio( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiInvioApiServiceHelper::loadTipiInvio] BEGIN");
            List<TipiInvioDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/tipi-invio";
            try {
            	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
                //HttpHeaders headers 
            	Client client = null;
            	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
            		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
                 }else {
                      client = ClientBuilder.newClient();
            	}
   
                //                client.register(new AddHeaderFilter("test", "test")); //AddAuthHeadersRequestFilter("root", "DefaultPasswordsAre:-("))
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipiInvioApi tipiInvioApi = rtarget.proxy(TipiInvioApi.class);
                Response resp = tipiInvioApi.loadTipiInvio( httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiInvioApiServiceHelper::loadTipiInvio] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipiInvioDTO>> tipiInvioListType = new GenericType<List<TipiInvioDTO>>() {
                };
                result = resp.readEntity(tipiInvioListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiInvioApiServiceHelper::loadTipiInvio] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiInvioApiServiceHelper::loadTipiInvio] END");
            }
            return result;
        }
}
