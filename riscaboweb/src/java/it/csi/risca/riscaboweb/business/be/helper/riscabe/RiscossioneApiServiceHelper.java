package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RiscossioneDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RiscossioneSearchDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.VerifyRiscossioneStatoDebDTO;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.Utils;


public class RiscossioneApiServiceHelper extends AbstractServiceHelper {

    private static final String RISCOSSIONI = "riscossioni";
    public RiscossioneApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
	

    public RiscossioneDTO saveRiscossione(RiscossioneDTO riscossione, String fruitore,  
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[RiscossioneApiServiceHelper::saveRiscossione] BEGIN");
        RiscossioneDTO result = new RiscossioneDTO();
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+"/"+RISCOSSIONI,Constants.FRUITORE,fruitore);

        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<RiscossioneDTO> entity = Entity.json(riscossione);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
            LOGGER.debug("[RiscossioneApiServiceHelper::saveRiscossione] RESPONSE STATUS : " + resp.getStatus());
            handleResponseErrors(resp);
            GenericType<RiscossioneDTO> dtoType = new GenericType<>() {};
            result = resp.readEntity(dtoType);
        } catch (ProcessingException e) {
            LOGGER.debug("[RiscossioneApiServiceHelper::saveRiscossione] EXCEPTION : " + e);
            throw new ProcessingException(e);
        } finally {
            LOGGER.debug("[RiscossioneApiServiceHelper::saveRiscossione] END");
        }
        return result;
    }


	public RiscossioneDTO updateRiscossione(RiscossioneDTO riscossione,  String fruitore,  
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[RiscossioneApiServiceHelper::updateRiscossione] BEGIN");
        RiscossioneDTO result = new RiscossioneDTO();
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+"/"+RISCOSSIONI,Constants.FRUITORE,fruitore);

        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<RiscossioneDTO> entity = Entity.json(riscossione);
            Response resp = getInvocationBuilder(targetUrl, map).put(entity);
            LOGGER.debug("[RiscossioneApiServiceHelper::updateRiscossione] RESPONSE STATUS : " + resp.getStatus());
            handleResponseErrors(resp);
            GenericType<RiscossioneDTO> dtoType = new GenericType<RiscossioneDTO>() {
            };
            result = resp.readEntity(dtoType);
        } catch (ProcessingException e) {
            LOGGER.debug("[RiscossioneApiServiceHelper::updateRiscossione] EXCEPTION : " + e);
            throw new ProcessingException(e);
        } finally {
            LOGGER.debug("[RiscossioneApiServiceHelper::updateRiscossione] END");
        }
        return result;
	}
	
    public RiscossioneDTO getRiscossione(String codRiscossione, String fruitore,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[RiscossioneApiServiceHelper::getRiscossione] BEGIN");
        RiscossioneDTO result = new RiscossioneDTO();
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+ "/"+RISCOSSIONI+"/" + codRiscossione,Constants.FRUITORE,fruitore);
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
            handleResponseErrors(resp);
            GenericType<RiscossioneDTO> riscossione = new GenericType<RiscossioneDTO>() {
            };
            result = resp.readEntity(riscossione);
        } catch (ProcessingException e) {
            LOGGER.error("[RiscossioneApiServiceHelper::getRiscossione] EXCEPTION : " + e);
            throw new ProcessingException(e);
        } finally {
            LOGGER.debug("[RiscossioneApiServiceHelper::getRiscossione] END");
        }
        return result;
    }
    
    public List<RiscossioneDTO> getRiscossioniGruppo(MultivaluedMap<String, Object> requestHeaders, String idGruppo) throws GenericException {
        LOGGER.debug("[RiscossioneApiServiceHelper::getRiscossioniGruppo] BEGIN");
        List<RiscossioneDTO> result = new ArrayList<>();
        String targetUrl = this.endpointBase + "/riscossioni?idGruppo=" + idGruppo;
        try {
            Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
            handleResponseErrors(resp);
            GenericType<List<RiscossioneDTO>> riscossione = new GenericType<List<RiscossioneDTO>>() {
            };
            result = resp.readEntity(riscossione);
        } catch (ProcessingException e) {
            LOGGER.error("[RiscossioneApiServiceHelper::getRiscossioniGruppo] EXCEPTION : " + e);
            throw new ProcessingException(e);
        } finally {
            LOGGER.debug("[RiscossioneApiServiceHelper::getRiscossioniGruppo] END");
        }
        return result;
    }
  
    public Response searchRiscossione(RiscossioneSearchDTO riscossioneSearch, String fruitore, String modalitaRicerca, Integer offset,  Integer limit,  String sort,
			
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
    	 LOGGER.debug("[RiscossioneApiServiceHelper::searchRiscossione] BEGIN");
         String targetUrl = Utils.buildTargetUrl(this.endpointBase+ "/_search/riscossioni",Constants.FRUITORE,fruitore,"modalitaRicerca", modalitaRicerca,"offset", offset,"limit", limit,"sort", sort);
         try {
             MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
             Entity<RiscossioneSearchDTO> entity = Entity.json(riscossioneSearch);
             Response resp = getInvocationBuilder(targetUrl, map).post(entity);
             LOGGER.debug("[RiscossioneApiServiceHelper::searchRiscossione] RESPONSE STATUS : " + resp.getStatus());
             handleResponseErrors(resp);
             return resp;
         } catch (ProcessingException e) {
             LOGGER.debug("[RiscossioneApiServiceHelper::searchRiscossione] EXCEPTION : " + e);
             throw new ProcessingException(e);
         } finally {
             LOGGER.debug("[RiscossioneApiServiceHelper::searchRiscossione] END");
         }
     }

    
    public String loadDatoTecnico(Integer idRiscossione,String fruitore,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[RiscossioneApiServiceHelper::loadDatoTecnico] BEGIN");
        String result = null;
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+ "/"+RISCOSSIONI+"/" + idRiscossione+ "/dati-tecnici", Constants.FRUITORE,fruitore);
        
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
            handleResponseErrors(resp);
            GenericType<String> dtoType = new GenericType<>() {};
            result = resp.readEntity(dtoType);
        } catch (ProcessingException e) {
            LOGGER.error("[RiscossioneApiServiceHelper::loadDatoTecnico] EXCEPTION : " + e);
            throw new ProcessingException(e);
        } finally {
            LOGGER.debug("[RiscossioneApiServiceHelper::loadDatoTecnico] END");
        }
        return result;
    }

    public VerifyRiscossioneStatoDebDTO verifyRiscossioniSTDebitori(Integer idOggetto, String indTipoOggetto, String idTipoOper,
			Integer idRiscossione,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest)  throws GenericException {
        LOGGER.debug("[RiscossioneApiServiceHelper::verifyRiscossioniSTDebitori] BEGIN");
        VerifyRiscossioneStatoDebDTO result = new VerifyRiscossioneStatoDebDTO();
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+ "/_verify_riscossioni_stdebitori/"+idOggetto,"indTipoOggetto", indTipoOggetto,"idTipoOper",idTipoOper,"idRiscossione",idRiscossione);
 
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
            LOGGER.debug("[RiscossioneApiServiceHelper::verifyRiscossioniSTDebitori] RESPONSE STATUS : " + resp.getStatus());
            handleResponseErrors(resp);
            GenericType<VerifyRiscossioneStatoDebDTO> dtoType = new GenericType<VerifyRiscossioneStatoDebDTO>() {};
            result = resp.readEntity(dtoType);
        } catch (ProcessingException e) {
            LOGGER.debug("[RiscossioneApiServiceHelper::verifyRiscossioniSTDebitori] EXCEPTION : " + e);
            throw new ProcessingException(e);
        } finally {
            LOGGER.debug("[RiscossioneApiServiceHelper::verifyRiscossioniSTDebitori] END");
        }
        return result;
    }
    
    public List<String> getCodiciUtenzaByIdStatoDebitorio(MultivaluedMap<String, Object> requestHeaders, Integer idStatoDebitorio) throws GenericException {
        LOGGER.debug("[RiscossioneApiServiceHelper::getCodiciUtenzaByIdStatoDebitorio] BEGIN");
        List<String> result = new ArrayList<>();
        String targetUrl = this.endpointBase + "/utenze-comp/idStatoDebitorio/" + idStatoDebitorio;
        try {
            Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
            handleResponseErrors(resp);
            GenericType<List<String>> codUtenze = new GenericType<List<String>>() {}; 
            result = resp.readEntity(codUtenze);
        } catch (ProcessingException e) {
            LOGGER.error("[RiscossioneApiServiceHelper::getCodiciUtenzaByIdStatoDebitorio] EXCEPTION : " + e);
            throw new ProcessingException(e);
        } finally {
            LOGGER.debug("[RiscossioneApiServiceHelper::getCodiciUtenzaByIdStatoDebitorio] END");
        }
        return result;
    }
    
}
