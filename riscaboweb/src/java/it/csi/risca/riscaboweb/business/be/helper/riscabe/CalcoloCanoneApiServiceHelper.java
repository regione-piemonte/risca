package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CalcoloCanoneDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CalcoloCanoneSingoloDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ambiente.RiscossioneDatoTecnicoDTO;


public class CalcoloCanoneApiServiceHelper extends AbstractServiceHelper {

    public CalcoloCanoneApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }

    public CalcoloCanoneDTO calcoloCanone(MultivaluedMap<String, Object> requestHeaders, Integer idRiscossione, String dataRiferimento) throws GenericException {
        LOGGER.debug("[CalcoloCanoneApiServiceHelper::calcoloCanone] BEGIN");
        CalcoloCanoneDTO result = new CalcoloCanoneDTO();// BigDecimal.ZERO;;
        String targetUrl = this.endpointBase + "/calcolo-canone/idRiscossione/" + idRiscossione + "/dataRif/" + dataRiferimento;
        try {

            Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
            LOGGER.debug("[CalcoloCanoneApiServiceHelper::calcoloCanone] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.debug("[CalcoloCanoneApiServiceHelper::calcoloCanone] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<CalcoloCanoneDTO> canone = new GenericType<CalcoloCanoneDTO>(){};
            result = resp.readEntity(canone);
        } catch (ProcessingException e) {
            LOGGER.debug("[CalcoloCanoneApiServiceHelper::calcoloCanone] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[CalcoloCanoneApiServiceHelper::calcoloCanone] END");
        }
        return result;
    }

    
    public Integer loadDatoTecnico(MultivaluedMap<String, Object> requestHeaders, Long idRiscossione) throws GenericException {

        return null;
    }
    
    public CalcoloCanoneSingoloDTO calcoloCanoneSingoloEFrazionato(MultivaluedMap<String, Object> requestHeaders, RiscossioneDatoTecnicoDTO datoTecnico, String dataRiferimento, String dataFrazionamento, String flgFraz) throws GenericException {
        LOGGER.debug("[CalcoloCanoneApiServiceHelper::calcoloCanoneSingoloEFrazionato] BEGIN");
        CalcoloCanoneSingoloDTO result = new CalcoloCanoneSingoloDTO();
        String targetUrl = null;
        if(dataFrazionamento != null && flgFraz != null)
        	targetUrl = this.endpointBase + "/_calcolo-canone/dataRif/" + dataRiferimento + "?dataFraz=" + dataFrazionamento + "&flgFraz=" + flgFraz;
        else
        	targetUrl = this.endpointBase + "/_calcolo-canone/dataRif/" + dataRiferimento;
        try {
        	Entity<RiscossioneDatoTecnicoDTO> entity = Entity.json(datoTecnico);
            Response resp = getInvocationBuilder(targetUrl, requestHeaders).post(entity);
            LOGGER.debug("[CalcoloCanoneApiServiceHelper::calcoloCanoneSingoloEFrazionato] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.debug("[CalcoloCanoneApiServiceHelper::calcoloCanoneSingoloEFrazionato] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<CalcoloCanoneSingoloDTO> canone = new GenericType<CalcoloCanoneSingoloDTO>(){};
            result = resp.readEntity(canone);
        } catch (ProcessingException e) {
            LOGGER.debug("[CalcoloCanoneApiServiceHelper::calcoloCanoneSingoloEFrazionato] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[CalcoloCanoneApiServiceHelper::calcoloCanoneSingoloEFrazionato] END");
        }
        return result;
    }

}
