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

import it.csi.risca.riscaboweb.business.be.StatiRiscossioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.StatiRiscossioneExtendedDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.util.Constants;

public class StatiRiscossioneApiServiceHelper extends AbstractServiceHelper {

    public StatiRiscossioneApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<StatiRiscossioneExtendedDTO> getStatiRiscossione(String XRequestId, String XForwardedFor,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[StatiRiscossioneApiServiceHelper::getStatiRiscossione] BEGIN");
            List<StatiRiscossioneExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/stati-riscossione";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                StatiRiscossioneApi statiRiscossioneApi = rtarget.proxy(StatiRiscossioneApi.class);
                Response resp = statiRiscossioneApi.getStatiRiscossione((String) MDC.get(Constants.X_REQUEST_ID), (String) MDC.get(Constants.X_FORWARDER_FOR),  httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[StatiRiscossioneApiServiceHelper::getStatiRiscossione] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<StatiRiscossioneExtendedDTO>> statiRiscossioneListType = new GenericType<List<StatiRiscossioneExtendedDTO>>() {
                };
                result = resp.readEntity(statiRiscossioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[StatiRiscossioneApiServiceHelper::getStatiRiscossione] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[StatiRiscossioneApiServiceHelper::getStatiRiscossione] END");
            }
            return result;
        }
        
        public List<StatiRiscossioneExtendedDTO> getStatiRiscossioneByIdAmbito(Integer idAmbito,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[StatiRiscossioneApiServiceHelper::getStatiRiscossioneByIdAmbito] BEGIN");
            List<StatiRiscossioneExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase +"/ambiti/" + idAmbito+ "/stati-riscossione";
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
                StatiRiscossioneApi statiRiscossioneApi = rtarget.proxy(StatiRiscossioneApi.class);
                Response resp = statiRiscossioneApi.getStatiRiscossioneByIdAmbito(idAmbito.toString(),  httpHeaders, httpRequest);
             
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[StatiRiscossioneApiServiceHelper::getStatiRiscossioneByIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<StatiRiscossioneExtendedDTO>> statiRiscossioneListType = new GenericType<List<StatiRiscossioneExtendedDTO>>() {
                };
                result = resp.readEntity(statiRiscossioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[StatiRiscossioneApiServiceHelper::getStatiRiscossioneByIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[StatiRiscossioneApiServiceHelper::getStatiRiscossioneByIdAmbito] END");
            }
            return result;
        }
}
