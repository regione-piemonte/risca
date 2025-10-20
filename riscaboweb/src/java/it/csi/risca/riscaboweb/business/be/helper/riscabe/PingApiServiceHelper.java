package it.csi.risca.riscaboweb.business.be.helper.riscabe;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.dto.Ping;

public class PingApiServiceHelper extends AbstractServiceHelper {

    public PingApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }

    public List<Ping> ping(MultivaluedMap<String, Object> requestHeaders) {
        LOGGER.debug("[ApiManagerServiceHelper::ping] BEGIN");
        
        List<Ping> result = new ArrayList<>();
        String targetUrl = this.endpointBase + "/ping";
        LOGGER.info("target_url: " + targetUrl);
        try {
            Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
            GenericType<List<Ping>> pingListType = new GenericType<List<Ping>>() {
            };
            result = resp.readEntity(pingListType);
        } catch (ProcessingException e) {
            LOGGER.error("[ApiManagerServiceHelper::ping] EXCEPTION : " + e);
            Ping errorPing = new Ping();
            errorPing.setLabel("Database");
            errorPing.setStatus("KO");
            result.add(errorPing);
        }
        LOGGER.debug("[ApiManagerServiceHelper::ping] END");
        return result;
    }



}