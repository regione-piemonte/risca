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

import it.csi.risca.riscaboweb.business.be.ComuniApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ComuneDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ComuneExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;

public class ComuniApiServiceHelper extends AbstractServiceHelper {

    public ComuniApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<ComuneExtendedDTO> loadComuni(boolean attivo,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[ComuniApiServiceHelper::loadComuni] BEGIN");
            List<ComuneExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/comuni?attivo=" + attivo;
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                ComuniApi comuniApi = rtarget.proxy(ComuniApi.class);
                Response resp = comuniApi.loadComuni(attivo,  httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[ComuniApiServiceHelper::loadComuni] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<ComuneExtendedDTO>> comuniListType = new GenericType<List<ComuneExtendedDTO>>() {
                };
                result = resp.readEntity(comuniListType);
            } catch (ProcessingException e) {
                LOGGER.error("[ComuniApiServiceHelper::loadComuni] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[ComuniApiServiceHelper::loadComuni] END");
            }
            return result;
        }
        
        public List<ComuneDTO> loadComuniByIdOrCod(MultivaluedMap<String, Object> requestHeaders, String idRegione, String idProvincia, String codIstatComune) throws GenericException {
            LOGGER.debug("[ComuniApiServiceHelper::loadComuniByIdOrCod] BEGIN");
            List<ComuneDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/regioni/" + idRegione + "/province/" + idProvincia + "/comuni/" + codIstatComune;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[ComuniApiServiceHelper::loadComuniByIdOrCod] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<ComuneDTO>> comuniListType = new GenericType<List<ComuneDTO>>() {
                };
                result = resp.readEntity(comuniListType);
            } catch (ProcessingException e) {
                LOGGER.error("[ComuniApiServiceHelper::loadComuniByIdOrCod] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[ComuniApiServiceHelper::loadComuniByIdOrCod] END");
            }
            return result;
        }
        
        public List<ComuneDTO> loadComuniByCodRegione(MultivaluedMap<String, Object> requestHeaders, String codRegione, String codProvincia) throws GenericException {
            LOGGER.debug("[ComuniApiServiceHelper::loadComuniByCodRegione] BEGIN");
            List<ComuneDTO> result = new ArrayList<ComuneDTO>();
            String targetUrl = this.endpointBase + "/regioni/" + codRegione + "/province/" + codProvincia + "/comuni" ;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[ComuniApiServiceHelper::loadComuniByCodRegione] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<ComuneDTO>> comuniListType = new GenericType<List<ComuneDTO>>() {
                };
                result = resp.readEntity(comuniListType);
            } catch (ProcessingException e) {
                LOGGER.error("[ComuniApiServiceHelper::loadComuniByCodRegione] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[ComuniApiServiceHelper::loadComuniByCodRegione] END");
            }
            return result;
        }
        
        public List<ComuneExtendedDTO> loadComuniByRicerca(MultivaluedMap<String, Object> requestHeaders, String q, boolean attivo) throws GenericException {
            LOGGER.debug("[ComuniApiServiceHelper::loadComuniByRicerca] BEGIN");
            List<ComuneExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/comuni-qry?q=" + q + "&attivo=" + attivo;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[ComuniApiServiceHelper::loadComuniByRicerca] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<ComuneExtendedDTO>> comuniListType = new GenericType<List<ComuneExtendedDTO>>() {
                };
                result = resp.readEntity(comuniListType);
            } catch (ProcessingException e) {
                LOGGER.error("[ComuniApiServiceHelper::loadComuniByRicerca] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[ComuniApiServiceHelper::loadComuniByRicerca] END");
            }
            return result;
        }
        
        public ComuneDTO loadComuneByCodIstatComune(MultivaluedMap<String, Object> requestHeaders, String codIstatComune) throws GenericException {
            LOGGER.debug("[ComuniApiServiceHelper::loadComuneByCodIstatComune] BEGIN");
            ComuneDTO result = new ComuneDTO();
            String targetUrl = this.endpointBase + "/comuni/" + codIstatComune;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[ComuniApiServiceHelper::loadComuneByCodIstatComune] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<ComuneDTO> comuniListType = new GenericType<ComuneDTO>() {
                };
                result = resp.readEntity(comuniListType);
            } catch (ProcessingException e) {
                LOGGER.error("[ComuniApiServiceHelper::loadComuneByCodIstatComune] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[ComuniApiServiceHelper::loadComuneByCodIstatComune] END");
            }
            return result;
        }
}
