package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.GruppiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.GruppiDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.Utils;

public class GruppiApiServiceHelper extends AbstractServiceHelper {

	private static String GRUPPI ="gruppi";
	private static String DES_GRUPPO="desGruppo";
	
	
    public GruppiApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }

	public Response loadGruppiSoggetto(String fruitore,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[GruppiApiServiceHelper::loadGruppiSoggetto] BEGIN");

        String targetUrl = Utils.buildTargetUrl(this.endpointBase + "/"+GRUPPI,Constants.FRUITORE, fruitore);

        try {

          	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();

            handleResponseErrors(resp);
            return resp;
        } catch (ProcessingException e) {
            LOGGER.error("[GruppiApiServiceHelper::loadGruppiSoggetto] EXCEPTION : " + e);
            throw new ProcessingException(e);
        } finally {
            LOGGER.debug("[GruppiApiServiceHelper::loadGruppiSoggetto] END");
        }
	}
	
	public Response loadGruppiById(String fruitore, String codGruppo, String desGruppo,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[GruppiApiServiceHelper::loadGruppiById] BEGIN");

        String targetUrl = Utils.buildTargetUrl( this.endpointBase + "/"+GRUPPI +"/"+ codGruppo,Constants.FRUITORE, fruitore,DES_GRUPPO ,desGruppo) ;

        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();

            handleResponseErrors(resp);
            return resp;
        } catch (ProcessingException e) {
            LOGGER.error("[GruppiApiServiceHelper::loadGruppiById] EXCEPTION : " + e);
            throw new ProcessingException(e);
        } finally {
            LOGGER.debug("[GruppiApiServiceHelper::loadGruppiById] END");
        }
	}
	
	public Response saveGruppi(GruppiDTO gruppi, String fruitore,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[GruppiApiServiceHelper::saveGruppi] BEGIN");

        String targetUrl = Utils.buildTargetUrl(this.endpointBase + "/gruppi",Constants.FRUITORE,fruitore);
 
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<GruppiDTO> entity = Entity.json(gruppi);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);

            handleResponseErrors(resp);
            return resp;
        } catch (ProcessingException e) {
            LOGGER.error("[GruppiApiServiceHelper::saveGruppi] EXCEPTION : " + e);
            throw new ProcessingException(e);
        } finally {
            LOGGER.debug("[GruppiApiServiceHelper::saveGruppi] END");
        }
	}

	public Response updateGruppi(GruppiDTO gruppi, String fruitore,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[GruppiApiServiceHelper::updateGruppi] BEGIN");

        String targetUrl = Utils.buildTargetUrl(this.endpointBase + "/gruppi",Constants.FRUITORE,fruitore);
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<GruppiDTO> entity = Entity.json(gruppi);
            Response resp = getInvocationBuilder(targetUrl, map).put(entity);

            handleResponseErrors(resp);
            return resp;
        } catch (ProcessingException e) {
            LOGGER.error("[GruppiApiServiceHelper::updateGruppi] EXCEPTION : " + e);
            throw new ProcessingException(e);
        } finally {
            LOGGER.debug("[GruppiApiServiceHelper::updateGruppi] END");
        }
	}
	
	public Response deleteGruppi(String fruitore, Integer idGruppo,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[GruppiApiServiceHelper::deleteGruppi] BEGIN");
        String targetUrl = Utils.buildTargetUrl(this.endpointBase  + "/gruppi/" + idGruppo,Constants.FRUITORE,fruitore);

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
            GruppiApi gruppiApi = rtarget.proxy(GruppiApi.class);
            Response resp = gruppiApi.deleteGruppi(fruitore, idGruppo.toString(),  httpHeaders, httpRequest);
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.error("[GruppiApiServiceHelper::deleteGruppi] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            return resp;
        } catch (ProcessingException e) {
            LOGGER.error("[GruppiApiServiceHelper::deleteGruppi] EXCEPTION : " + e);
            throw new ProcessingException(e);
        } finally {
            LOGGER.debug("[GruppiApiServiceHelper::deleteGruppi] END");
        }
	}
	
	public Response loadGruppiByIdAmbitoAndCampoRicerca(String fruitore, Integer idAmbito, String campoRicerca, String flgTipoRicerca,
			 Integer offset,  Integer limit,  String sort,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
	 LOGGER.debug("[GruppiApiServiceHelper::loadGruppiByIdAmbitoAndCampoRicerca] BEGIN");
	 String targetUrlBuilder = Utils.buildTargetUrl(this.endpointBase  + "/gruppi-qry",Constants.FRUITORE,fruitore,
			 "idAmbito",idAmbito,"campoRicerca",campoRicerca,"flgTipoRicerca",flgTipoRicerca,"offset", offset,"limit",limit,"sort",sort);
      try {
          MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
          Response resp = getInvocationBuilder(targetUrlBuilder, map).get();
          handleResponseErrors(resp);
         return resp;

     } catch (ProcessingException e) {
         LOGGER.error("[GruppiApiServiceHelper::loadGruppiByIdAmbitoAndCampoRicerca] EXCEPTION : " + e);
         throw new ProcessingException(e);
     } finally {
         LOGGER.debug("[GruppiApiServiceHelper::loadGruppiByIdAmbitoAndCampoRicerca] END");
     }
     
	}
        
}
