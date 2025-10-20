package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RiduzioneAumentoDTO;
import it.csi.risca.riscaboweb.util.Utils;

public class RiduzioneAumentoApiServiceHelper extends AbstractServiceHelper {

    public RiduzioneAumentoApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<RiduzioneAumentoDTO> loadRiduzioneAumento( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumento] BEGIN");
            List<RiduzioneAumentoDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/riduzione-aumento";
            try {
                MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
                Response resp = getInvocationBuilder(targetUrl, map).get();

                handleResponseErrors(resp);
                GenericType<List<RiduzioneAumentoDTO>> riduzioneAumentoListType = new GenericType<List<RiduzioneAumentoDTO>>() {
                };
                result = resp.readEntity(riduzioneAumentoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumento] EXCEPTION : "+targetUrl , e);
                throw new ProcessingException(e);
            } finally {
                LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumento] END");
            }
            return result;
        }
        
        public List<RiduzioneAumentoDTO> loadRiduzioneAumentoByIdAmbito(Integer idAmbito,  HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumentoByIdAmbito] BEGIN");
            List<RiduzioneAumentoDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/riduzione-aumento/idAmbito/" + idAmbito;
            try {
              	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
                Response resp = getInvocationBuilder(targetUrl, map).get();
                 handleResponseErrors(resp);
                GenericType<List<RiduzioneAumentoDTO>> riduzioneAumentoListType = new GenericType<List<RiduzioneAumentoDTO>>() {
                };
                result = resp.readEntity(riduzioneAumentoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumentoByIdAmbito] EXCEPTION : " +targetUrl, e);
                throw new ProcessingException(e);
            } finally {
                LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumentoByIdAmbito] END");
            }
            return result;
        }   
        
        public List<RiduzioneAumentoDTO> loadRiduzioneAumentoByIdTipoUso(MultivaluedMap<String, Object> requestHeaders, Integer idTipoUso) throws GenericException {
            LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumentoByIdTipoUso] BEGIN");
            List<RiduzioneAumentoDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/riduzione-aumento/idTipoUso/" + idTipoUso;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                handleResponseErrors(resp);
                GenericType<List<RiduzioneAumentoDTO>> riduzioneAumentoType = new GenericType<List<RiduzioneAumentoDTO>>() {
                };
                result = resp.readEntity(riduzioneAumentoType);
            } catch (ProcessingException e) {
                LOGGER.error("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumentoByIdTipoUso] EXCEPTION : " + e);
                throw new ProcessingException(e);
            } finally {
                LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumentoByIdTipoUso] END");
            }
            return result;
        } 
        
        public List<RiduzioneAumentoDTO> loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum(MultivaluedMap<String, Object> requestHeaders, Integer idTipoUso, String flgRidAum) throws GenericException {
            LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum] BEGIN");
            List<RiduzioneAumentoDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/riduzione-aumento/idTipoUso/" + idTipoUso + "/flgRidAum/" + flgRidAum;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                handleResponseErrors(resp);
                GenericType<List<RiduzioneAumentoDTO>> riduzioneAumentoListType = new GenericType<List<RiduzioneAumentoDTO>>() {
                };
                result = resp.readEntity(riduzioneAumentoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum] EXCEPTION : " + e);
                throw new ProcessingException(e);
            } finally {
                LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum] END");
            }
            return result;
        } 
        
        public RiduzioneAumentoDTO loadRiduzioneAumentoByIdRiduzioneAumento(MultivaluedMap<String, Object> requestHeaders, Integer idRiduzioneAumento) throws GenericException {
            LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumentoByIdRiduzioneAumento] BEGIN");
            RiduzioneAumentoDTO result = new RiduzioneAumentoDTO();
            String targetUrl = this.endpointBase + "/riduzione-aumento/idRiduzioneAumento/" + idRiduzioneAumento;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                handleResponseErrors(resp);
                GenericType<RiduzioneAumentoDTO> riduzioneAumento = new GenericType<RiduzioneAumentoDTO>() {
                };
                result = resp.readEntity(riduzioneAumento);
            } catch (ProcessingException e) {
                LOGGER.error("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumentoByIdRiduzioneAumento] EXCEPTION : " + e);
                throw new ProcessingException(e);
            } finally {
                LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioneAumentoByIdRiduzioneAumento] END");
            }
            return result;
        }
		public RiduzioneAumentoDTO loadRiduzioniByIdOrCodRiduzioneAumento(
				MultivaluedMap<String, Object> requestHeaders, String idOrCodRiduzioneAumento,
				String dataIniVal, String dataFineVal) throws GenericException {
			   LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioniByIdOrCodRiduzioneAumento] BEGIN");
	            RiduzioneAumentoDTO result = new RiduzioneAumentoDTO();
	            String targetUrl = Utils.buildTargetUrl(this.endpointBase+ "/riduzioni/" + idOrCodRiduzioneAumento,"dataIniVal", dataIniVal,"dataFineVal", dataFineVal);

	            try {
	                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
	                handleResponseErrors(resp);
	                GenericType<RiduzioneAumentoDTO> riduzioneAumento = new GenericType<RiduzioneAumentoDTO>() {
	                };
	                result = resp.readEntity(riduzioneAumento);
	            } catch (ProcessingException e) {
	                LOGGER.error("[RiduzioneAumentoApiServiceHelper::loadRiduzioniByIdOrCodRiduzioneAumento] EXCEPTION : " + e);
	                throw new ProcessingException(e);
	            } finally {
	                LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioniByIdOrCodRiduzioneAumento] END");
	            }
	            return result;
		} 
        
		public RiduzioneAumentoDTO loadAumentoByIdOrCodRiduzioneAumento(
				MultivaluedMap<String, Object> requestHeaders, String idOrCodRiduzioneAumento,
				String dataIniVal, String dataFineVal) throws GenericException {
			   LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadAumentoByIdOrCodRiduzioneAumento] BEGIN");
	            RiduzioneAumentoDTO result = new RiduzioneAumentoDTO();
	            String targetUrl = Utils.buildTargetUrl(this.endpointBase+ "/aumenti/" + idOrCodRiduzioneAumento,"dataIniVal", dataIniVal,"dataFineVal", dataFineVal);

	            try {
	                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
	                handleResponseErrors(resp);
	                GenericType<RiduzioneAumentoDTO> riduzioneAumento = new GenericType<RiduzioneAumentoDTO>() {
	                };
	                result = resp.readEntity(riduzioneAumento);
	            } catch (ProcessingException e) {
	                LOGGER.error("[RiduzioneAumentoApiServiceHelper::loadAumentoByIdOrCodRiduzioneAumento] EXCEPTION : " + e);
	                throw new ProcessingException(e);
	            } finally {
	                LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadAumentoByIdOrCodRiduzioneAumento] END");
	            }
	            return result;
		}
		public List<RiduzioneAumentoDTO> loadRiduzioniByIdOrCodTipoUsoFlgManuale(
				MultivaluedMap<String, Object> multivaluedMapFromHttpHeaders, String idOrCodTipoUso, String flgManuale,
				String dataIniVal, String dataFineVal) throws GenericException {
			 LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioniByIdOrCodTipoUsoFlgManuale] BEGIN");
	            List<RiduzioneAumentoDTO> result = new ArrayList<>();
	            String targetUrl = Utils.buildTargetUrl(this.endpointBase + "/tipi-uso/" + idOrCodTipoUso+  "/riduzioni","flgManuale", flgManuale,"dataIniVal", dataIniVal,"dataFineVal", dataFineVal);
                try {
	                Response resp = getInvocationBuilder(targetUrl, multivaluedMapFromHttpHeaders).get();
	                handleResponseErrors(resp);
	                GenericType<List<RiduzioneAumentoDTO>> riduzioneAumentoListType = new GenericType<List<RiduzioneAumentoDTO>>() {
	                };
	                result = resp.readEntity(riduzioneAumentoListType);
	            } catch (ProcessingException e) {
	                LOGGER.error("[RiduzioneAumentoApiServiceHelper::loadRiduzioniByIdOrCodTipoUsoFlgManuale] EXCEPTION : " + e);
	                throw new ProcessingException(e);
	            } finally {
	                LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioniByIdOrCodTipoUsoFlgManuale] END");
	            }
	            return result;
			
		}
		public List<RiduzioneAumentoDTO> loadAumentiByIdOrCodTipoUsoFlgManuale(
				MultivaluedMap<String, Object> multivaluedMapFromHttpHeaders, String idOrCodTipoUso, String flgManuale,
				String dataIniVal, String dataFineVal) throws GenericException {
			 LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadAumentiByIdOrCodTipoUsoFlgManuale] BEGIN");
	            List<RiduzioneAumentoDTO> result = new ArrayList<>();
	            String targetUrl = Utils.buildTargetUrl(this.endpointBase + "/tipi-uso/" + idOrCodTipoUso+  "/aumenti", "flgManuale", flgManuale,"dataIniVal", dataIniVal,"dataFineVal", dataFineVal);
	            try {
	                Response resp = getInvocationBuilder(targetUrl, multivaluedMapFromHttpHeaders).get();
	                handleResponseErrors(resp);
	                GenericType<List<RiduzioneAumentoDTO>> riduzioneAumentoListType = new GenericType<List<RiduzioneAumentoDTO>>() {
	                };
	                result = resp.readEntity(riduzioneAumentoListType);
	            } catch (ProcessingException e) {
	                LOGGER.error("[RiduzioneAumentoApiServiceHelper::loadAumentiByIdOrCodTipoUsoFlgManuale] EXCEPTION : " + e);
	                throw new ProcessingException(e);
	            } finally {
	                LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadAumentiByIdOrCodTipoUsoFlgManuale] END");
	            }
	            return result;
		}
		
		public List<RiduzioneAumentoDTO> loadRiduzioniByflgManuale(
				MultivaluedMap<String, Object> multivaluedMapFromHttpHeaders, String flgManuale, String dataIniVal,
				String dataFineVal) throws GenericException {
			 LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioniByflgManuale] BEGIN");
	            List<RiduzioneAumentoDTO> result = new ArrayList<>();
	            String targetUrl = Utils.buildTargetUrl(this.endpointBase +  "/riduzioni","flgManuale", flgManuale,"dataIniVal", dataIniVal,"dataFineVal", dataFineVal);
                try {
	                Response resp = getInvocationBuilder(targetUrl, multivaluedMapFromHttpHeaders).get();
	                handleResponseErrors(resp);
	                GenericType<List<RiduzioneAumentoDTO>> riduzioneAumentoListType = new GenericType<List<RiduzioneAumentoDTO>>() {
	                };
	                result = resp.readEntity(riduzioneAumentoListType);
	            } catch (ProcessingException e) {
	                LOGGER.error("[RiduzioneAumentoApiServiceHelper::loadRiduzioniByflgManuale] EXCEPTION : " + e);
	                throw new ProcessingException(e);
	           } finally {
	                LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadRiduzioniByflgManuale] END");
	            }
	            return result;
		}
		public List<RiduzioneAumentoDTO> loadAumentiiByflgManuale(
				MultivaluedMap<String, Object> multivaluedMapFromHttpHeaders, String flgManuale, String dataIniVal,
				String dataFineVal) throws GenericException {
			 LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadAumentiiByflgManuale] BEGIN");
	            List<RiduzioneAumentoDTO> result = new ArrayList<>();
	            String targetUrl = Utils.buildTargetUrl(this.endpointBase +  "/aumenti","flgManuale", flgManuale,"dataIniVal", dataIniVal,"dataFineVal", dataFineVal);

	            try {
	                Response resp = getInvocationBuilder(targetUrl, multivaluedMapFromHttpHeaders).get();
	                handleResponseErrors(resp);
	                GenericType<List<RiduzioneAumentoDTO>> riduzioneAumentoListType = new GenericType<List<RiduzioneAumentoDTO>>() {
	                };
	                result = resp.readEntity(riduzioneAumentoListType);
	            } catch (ProcessingException e) {
	                LOGGER.error("[RiduzioneAumentoApiServiceHelper::loadAumentiiByflgManuale] EXCEPTION : " + e);
	                throw new ProcessingException(e);
	            } finally {
	                LOGGER.debug("[RiduzioneAumentoApiServiceHelper::loadAumentiiByflgManuale] END");
	            }
	            return result;
		} 
        
}
