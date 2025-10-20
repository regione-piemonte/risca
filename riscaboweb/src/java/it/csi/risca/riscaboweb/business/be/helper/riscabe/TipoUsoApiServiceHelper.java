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

import it.csi.risca.riscaboweb.business.be.TipoUsoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class TipoUsoApiServiceHelper extends AbstractServiceHelper {

    public TipoUsoApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<TipoUsoExtendedDTO> loadTipiUso( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipoUsoApiServiceHelper::loadTipiUso] BEGIN");
            List<TipoUsoExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/tipi-uso";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipoUsoApi tipiUsoApi = rtarget.proxy(TipoUsoApi.class);
                Response resp = tipiUsoApi.loadTipiUso( httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipoUsoApiServiceHelper::loadTipiUso] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipoUsoExtendedDTO>> tipiUsoListType = new GenericType<List<TipoUsoExtendedDTO>>() {
                };
                result = resp.readEntity(tipiUsoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipoUsoApiServiceHelper::loadTipiUso] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipoUsoApiServiceHelper::loadTipiUso] END");
            }
            return result;
        }
        
        public List<TipoUsoExtendedDTO> loadTipiUsoByIdAmbito(MultivaluedMap<String, Object> requestHeaders, Long idAmbito,String  dataIniVal,String dataFineVal) throws GenericException {
            LOGGER.debug("[TipoUsoApiServiceHelper::loadTipiUsoByIdAmbito] BEGIN");
            List<TipoUsoExtendedDTO> result = new ArrayList<>();
            String targetUrl ="";
            if(dataIniVal != null && dataFineVal != null) {
            		  targetUrl = this.endpointBase +"/ambiti/"+idAmbito+"/tipi-uso?dataIniVal="+dataIniVal+"&dataFineVal="+dataFineVal; 	
            }else if(dataIniVal != null && dataFineVal == null) {
                targetUrl = this.endpointBase + "/ambiti/"+idAmbito+"/tipi-uso?dataIniVal="+dataIniVal;
            }else if(dataIniVal == null && dataFineVal != null) {
            	targetUrl = this.endpointBase + "/ambiti/"+idAmbito+"/tipi-uso?dataFineVal="+dataFineVal;
            }else {
            	targetUrl = this.endpointBase   + "/ambiti/"+idAmbito+"/tipi-uso";
            }
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipoUsoApiServiceHelper::loadTipiUsoByIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipoUsoExtendedDTO>> tipiUsoListType = new GenericType<List<TipoUsoExtendedDTO>>() {
                };
                result = resp.readEntity(tipiUsoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipoUsoApiServiceHelper::loadTipiUsoByIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipoUsoApiServiceHelper::loadTipiUsoByIdAmbito] END");
            }
            return result;
        }   
        
        public List<TipoUsoExtendedDTO> loadTipoUsoByIdTipoUsoPadre(MultivaluedMap<String, Object> requestHeaders, String idTipoUsoPadre) throws GenericException {
            LOGGER.debug("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoPadre] BEGIN");
            List<TipoUsoExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/tipi-uso/idTipoUsoPadre/" + idTipoUsoPadre;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoPadre] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipoUsoExtendedDTO>> tipiUsoListType = new GenericType<List<TipoUsoExtendedDTO>>() {
                };
                result = resp.readEntity(tipiUsoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoPadre] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoPadre] END");
            }
            return result;
        } 
        
        public TipoUsoExtendedDTO loadTipoUsoByIdTipoUsoOrCodTipoUso(MultivaluedMap<String, Object> requestHeaders, String idTipoUso) throws GenericException {
            LOGGER.debug("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoOrCodTipoUso] BEGIN");
            TipoUsoExtendedDTO result = new TipoUsoExtendedDTO();
            String targetUrl = this.endpointBase + "/tipi-uso/" + idTipoUso;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoOrCodTipoUso] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TipoUsoExtendedDTO> tipiUsoListType = new GenericType<TipoUsoExtendedDTO>() {
                };
                result = resp.readEntity(tipiUsoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoOrCodTipoUso] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoOrCodTipoUso] END");
            }
            return result;
        } 
        
        public TipoUsoExtendedDTO loadTipoUsoByCodeAndIdAmbito(String codTipoUso, Integer idAmbito, 
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipoUsoApiServiceHelper::loadTipoUsoByCodeAndIdAmbito] BEGIN");
            TipoUsoExtendedDTO result = new TipoUsoExtendedDTO();
            String targetUrl = this.endpointBase + "/ambiti/" + idAmbito + "/tipi-uso/" + codTipoUso ;
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
                TipoUsoApi tipiUsoApi = rtarget.proxy(TipoUsoApi.class);
                Response resp = tipiUsoApi.loadTipoUsoByCodeAndIdAmbito(codTipoUso, idAmbito.toString(), 
	    				 httpHeaders, httpRequest);

                  LOGGER.debug("[TipoUsoApiServiceHelper::loadTipoUsoByCodeAndIdAmbito] RESPONSE STATUS : " + resp.getStatus());
      
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipoUsoApiServiceHelper::loadTipoUsoByCodeAndIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TipoUsoExtendedDTO> tipiUsoListType = new GenericType<TipoUsoExtendedDTO>() {
                };
                result = resp.readEntity(tipiUsoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipoUsoApiServiceHelper::loadTipoUsoByCodeAndIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipoUsoApiServiceHelper::loadTipoUsoByCodeAndIdAmbito] END");
            }
            return result;
        }
        
        public List<TipoUsoExtendedDTO> loadTipoUsoByIdTipoUsoPadreAndIdAmbito(String idTipoUsoPadre, Integer idAmbito, String dataIniVal,String dataFineVal,
				 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoPadreAndIdAmbito] BEGIN");
            List<TipoUsoExtendedDTO> result = new ArrayList<>();

            
            String targetUrl ="";
            if(dataIniVal != null && dataFineVal != null) {
            	if(idTipoUsoPadre != null) {
            		  targetUrl = this.endpointBase + "/ambiti/"+idAmbito+"/tipi-uso?idTipoUsoPadre="+ idTipoUsoPadre+ "&dataIniVal="+dataIniVal+"&dataFineVal="+dataFineVal;
            	}else {
            		  targetUrl = this.endpointBase +"/ambiti/"+idAmbito+"/tipi-uso?dataIniVal="+dataIniVal+"&dataFineVal="+dataFineVal;
            	}
            }else if(dataIniVal != null && dataFineVal == null) {
            	if(idTipoUsoPadre != null) {
            		  targetUrl = this.endpointBase + "/ambiti/"+idAmbito+"/tipi-uso?idTipoUsoPadre="+ idTipoUsoPadre+ "&dataIniVal="+dataIniVal;
            	}else {
            		  targetUrl = this.endpointBase + "/ambiti/"+idAmbito+"/tipi-uso?dataIniVal="+dataIniVal;
            	}
            }else if(dataIniVal == null && dataFineVal != null) {
            	if(idTipoUsoPadre != null) {
            		  targetUrl = this.endpointBase + "/ambiti/"+idAmbito+"/tipi-uso?idTipoUsoPadre="+ idTipoUsoPadre+"&dataFineVal="+dataFineVal;
            	}else {
            		  targetUrl = this.endpointBase + "/ambiti/"+idAmbito+"/tipi-uso?dataFineVal="+dataFineVal;
            	}
            }else {
            	if(idTipoUsoPadre != null) {
            		  targetUrl = this.endpointBase  + "/ambiti/"+idAmbito+"/tipi-uso?idTipoUsoPadre="+ idTipoUsoPadre;
            	}else {
            		  targetUrl = this.endpointBase   + "/ambiti/"+idAmbito+"/tipi-uso";
            	}
            }
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
                TipoUsoApi tipiUsoApi = rtarget.proxy(TipoUsoApi.class);
                Response resp = tipiUsoApi.loadTipoUsoByIdTipoUsoPadreAndIdAmbito(idTipoUsoPadre, idAmbito.toString(), dataIniVal, dataFineVal,
	    				  httpHeaders, httpRequest);
                  LOGGER.debug("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoPadreAndIdAmbito] RESPONSE STATUS : " + resp.getStatus());
      
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoPadreAndIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipoUsoExtendedDTO>> tipiUsoListType = new GenericType<List<TipoUsoExtendedDTO>>() {
                };
                result = resp.readEntity(tipiUsoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoPadreAndIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipoUsoApiServiceHelper::loadTipoUsoByIdTipoUsoPadreAndIdAmbito] END");
            }
            return result;
        }
}
