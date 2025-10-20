//package it.csi.risca.riscaboweb.business.be.helper.riscabe;
//
//import java.io.IOException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.ws.rs.ProcessingException;
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.ClientRequestContext;
//import javax.ws.rs.client.ClientRequestFilter;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.GenericType;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.MultivaluedMap;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.SecurityContext;
//
//import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
//
//import it.csi.risca.riscaboweb.business.be.InfoUtenteApi;
//import it.csi.risca.riscaboweb.business.be.exception.GenericException;
//import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
//import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
//import it.csi.iride2.policy.entity.Identita;
//import it.csi.risca.riscaboweb.dto.UserInfo;
//
//public class InfoUtenteApiServiceHelper extends AbstractServiceHelper {
//	
//    public InfoUtenteApiServiceHelper(String hostname, String endpointBase) {
//        this.hostname = hostname;
//        this.endpointBase = hostname + endpointBase;
//    }
//	
//    public UserInfo getInfoUtente(MultivaluedMap<String, Object> requestHeaders, Identita identita,  HttpHeaders httpHeaders,
//			HttpServletRequest httpRequest) throws GenericException {
//        LOGGER.debug("[InfoUtenteApiServiceHelper::getInfoUtente] BEGIN");
//        UserInfo result = new UserInfo();
//        String targetUrl = this.endpointBase + "/info-utente";
//        try {
//        	
//        	
//            Client client = ClientBuilder.newClient();
//            
//            //client.register(new LoggingFilter());
//            
//            WebTarget target = client.target(this.endpointBase);
//            ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
//            InfoUtenteApi infoUtenteApi = rtarget.proxy(InfoUtenteApi.class);
//            Response resp = infoUtenteApi.getInfoUtente(identita,  httpHeaders, httpRequest);
//            //Response resp = getInvocationBuilderUser(targetUrl, requestHeaders, identita).get();
//            if (resp.getStatus() >= 400) {
//                ErrorDTO err = getError(resp);
//                LOGGER.error("[InfoUtenteApiServiceHelper::getInfoUtente] SERVER EXCEPTION : " + err);
//                throw new GenericException(err);
//            }
//            GenericType<UserInfo> userInfo = new GenericType<UserInfo>() {
//            };
//            result = resp.readEntity(userInfo);
//        } catch (ProcessingException e) {
//            LOGGER.error("[InfoUtenteApiServiceHelper::getInfoUtente] EXCEPTION : " + e);
//            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
//        } finally {
//            LOGGER.debug("[InfoUtenteApiServiceHelper::getInfoUtente] END");
//        }
//        return result;
//    }
//
//    
//	public class LoggingFilter implements ClientRequestFilter {
//		   
//
//	    @Override
//	    public void filter(ClientRequestContext requestContext) throws IOException {
//	    	System.out.println("************** " + requestContext.getEntity().toString() + " *******************");
//
//	    }
//	}
//    
//}
