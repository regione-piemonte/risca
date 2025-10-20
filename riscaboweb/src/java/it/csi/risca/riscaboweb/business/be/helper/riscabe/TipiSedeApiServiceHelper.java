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

import it.csi.risca.riscaboweb.business.be.TipiSedeApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiSedeDTO;

public class TipiSedeApiServiceHelper extends AbstractServiceHelper {

    public TipiSedeApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<TipiSedeDTO> loadTipiSede(String tipoSoggetto , HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiSedeApiServiceHelper::loadTipiSede] BEGIN");
            List<TipiSedeDTO> result = new ArrayList<>();
            String targetUrl ="";
            if(tipoSoggetto != null) {
            	   targetUrl = this.endpointBase + "/tipi-sede?TipoSoggetto="+tipoSoggetto;
            }else {
          	   targetUrl = this.endpointBase + "/tipi-sede";
            }
      
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipiSedeApi tipiSedeApi = rtarget.proxy(TipiSedeApi.class);
                Response resp = tipiSedeApi.loadTipiSede(tipoSoggetto,  httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiSedeApiServiceHelper::loadTipiSede] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipiSedeDTO>> tipiSedeListType = new GenericType<List<TipiSedeDTO>>() {
                };
                result = resp.readEntity(tipiSedeListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiSedeApiServiceHelper::loadTipiSede] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiSedeApiServiceHelper::loadTipiSede] END");
            }
            return result;
        }
}
