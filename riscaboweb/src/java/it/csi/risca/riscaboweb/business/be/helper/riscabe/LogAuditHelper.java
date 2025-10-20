package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CsiLogAuditDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.LockRiscossioneDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TracciamentoDTO;

public class LogAuditHelper extends AbstractServiceHelper {
	
    public LogAuditHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }

    public void saveCsiLogAudit(MultivaluedMap<String, Object> requestHeaders, CsiLogAuditDTO csiLogAuditDTO) throws GenericException {
        LOGGER.debug("[LogAuditHelper::saveCsiLogAudit] BEGIN");

        String targetUrl = this.endpointBase + "/csi-log-audit";
        try {
            Entity<CsiLogAuditDTO> entity = Entity.json(csiLogAuditDTO);
            Response resp = getInvocationBuilder(targetUrl, requestHeaders).post(entity);
            LOGGER.debug("[LogAuditHelper::saveCsiLogAudit] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.debug("[LogAuditHelper::saveCsiLogAudit] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }

        } catch (ProcessingException e) {
            LOGGER.debug("[LogAuditHelper::saveCsiLogAudit] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[LogAuditHelper::saveCsiLogAudit] END");
        }

    }

	
    public void saveTracciamento(MultivaluedMap<String, Object> requestHeaders, TracciamentoDTO tracciamentoDTO) throws GenericException {
        LOGGER.debug("[LogAuditHelper::saveTracciamento] BEGIN");

        String targetUrl = this.endpointBase + "/tracciamento";
        try {
            Entity<TracciamentoDTO> entity = Entity.json(tracciamentoDTO);
            Response resp = getInvocationBuilder(targetUrl, requestHeaders).post(entity);
            LOGGER.debug("[LogAuditHelper::saveTracciamento] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.debug("[LogAuditHelper::saveTracciamento] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }

        } catch (ProcessingException e) {
            LOGGER.debug("[LogAuditHelper::saveTracciamento] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[LogAuditHelper::saveTracciamento] END");
        }

    }
	
	
	public static String getClientIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    } 
	

}
