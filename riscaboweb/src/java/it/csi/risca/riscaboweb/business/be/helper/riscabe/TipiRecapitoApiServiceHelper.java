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
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.TipiRecapitoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiRecapitoDTO;

public class TipiRecapitoApiServiceHelper extends AbstractServiceHelper {

    public TipiRecapitoApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<TipiRecapitoDTO> loadTipiRecapito( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiRecapitoApiServiceHelper::loadTipiRecapito] BEGIN");
            List<TipiRecapitoDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/tipi-recapito";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipiRecapitoApi tipiRecapitoApi = rtarget.proxy(TipiRecapitoApi.class);
                Response resp = tipiRecapitoApi.loadTipiRecapito( httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiRecapitoApiServiceHelper::loadTipiRecapito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipiRecapitoDTO>> tipiRecapitoListType = new GenericType<List<TipiRecapitoDTO>>() {
                };
                result = resp.readEntity(tipiRecapitoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiRecapitoApiServiceHelper::loadTipiRecapito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiRecapitoApiServiceHelper::loadTipiRecapito] END");
            }
            return result;
        }
}
