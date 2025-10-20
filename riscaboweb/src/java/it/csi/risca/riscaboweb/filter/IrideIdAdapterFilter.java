package it.csi.risca.riscaboweb.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import it.csi.iride2.iridefed.entity.Ruolo;
import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;
import it.csi.iride2.policy.exceptions.MalformedIdTokenException;
import it.csi.risca.riscaboweb.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.enumeration.TipoRuolo;
import it.csi.risca.riscaboweb.business.be.impl.AbstractApiServiceImpl;
import it.csi.risca.riscaboweb.util.Constants;

/**
 * Inserisce in sessione:
 * <ul> 
 *  <li>l'identit&agrave; digitale relativa all'utente autenticato.
 *  <li>l'oggetto <code>currentUser</code>
 * </ul>
 * Funge da adapter tra il filter del metodo di autenticaizone previsto e la
 * logica applicativa.
 *
 * @author CSIPiemonte
 */
public class IrideIdAdapterFilter extends AbstractApiServiceImpl implements Filter {
	
    @Autowired
    IrideServiceHelper serviceHelper;

    public static final String IRIDE_ID_SESSIONATTR = "iride2_id";

    public static final String AUTH_ID_MARKER = "Shib-Iride-IdentitaDigitale";

    public static final String USERINFO_SESSIONATTR = "appDatacurrentUser";
    
    public static final String AMBITICONFIG_SESSIONATTR = "appDataAmbitiConfigCurrentUser";

    /**  */
    protected static final Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME + ".security");
    
    private Boolean devMode;

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain fchn)
            throws IOException, ServletException {
        HttpServletRequest hreq = (HttpServletRequest) req;
        String marker = getToken(hreq);
        Identita identita = null;
        if (marker != null) {
        	try {
				identita = new Identita(normalizeToken(marker));
			} catch (MalformedIdTokenException e) {
                LOG.info("[IrideIdAdapterFilter::doFilter] " + e.toString(), e);
			}            
        }
        if (hreq.getSession().getAttribute(IRIDE_ID_SESSIONATTR) == null) {
            marker = getToken(hreq);           
            if (marker != null && identita != null) {
                try {
                    identita = new Identita(normalizeToken(marker));
                    hreq.getSession().setAttribute(IRIDE_ID_SESSIONATTR, identita);
                    it.csi.risca.riscaboweb.dto.UserInfo userInfo = new it.csi.risca.riscaboweb.dto.UserInfo();
                    userInfo.setNome(identita.getNome());
                    userInfo.setCognome(identita.getCognome());
                    userInfo.setEnte("--");
                    userInfo.setRuolo("--");
                    userInfo.setCodFisc(identita.getCodFiscale());
                    userInfo.setLivAuth(identita.getLivelloAutenticazione());
                    userInfo.setCommunity(identita.getIdProvider());
                    hreq.getSession().setAttribute(USERINFO_SESSIONATTR, userInfo);                    
                    
                    Application cod = new Application();
                    cod.setId("RISCA");
                    
                    UseCase use = new UseCase();
                    use.setAppId(cod);
                    use.setId("UC_SIPRA");
                    
                    Ruolo[] ruoli = serviceHelper.getRuoli(identita, cod);
                    Long ambito = serviceHelper.getInfoPersonaInUseCase(identita, use);
                    LOG.debug("getInfoPersonaInUseCase restituisce questo AMBITO: " + ambito);
                    userInfo.setAmbito(ambito);
                    HttpServletResponse res;
                    if(ruoli !=null && ruoli.length > 0) {
                    	
                    	for(int i=0; i<ruoli.length; i++) {

                    			String ruoloUtenteLoggato = ruoli[i].getCodiceRuolo();
                    			
                    			if(!(ruoloUtenteLoggato.equals(TipoRuolo.AMMINISTRATORE.getDescrizione()) ||
                    			   ruoloUtenteLoggato.equals(TipoRuolo.GESTORE_BASE.getDescrizione()) ||		
                    			   ruoloUtenteLoggato.equals(TipoRuolo.GESTORE_DATI.getDescrizione()) ||
                    			   ruoloUtenteLoggato.equals(TipoRuolo.CONSULTATORE.getDescrizione()))) {
                    				//redirect
                    				LOG.debug("[IrideIdAdapterFilter::doFilter] Redirect alla landing page per gli utenti non autorizzati");
                    				res = (HttpServletResponse) resp;
                    				res.sendRedirect("?unauthorized");

                    			}
                    			userInfo.setRuolo(ruoli[i].getCodiceRuolo());
                    	}
                    	
                    } else {
                    	LOG.debug("[IrideIdAdapterFilter::doFilter] Ruolo non presente e redirect alla landing page per gli utenti non autorizzati");
                    	res = (HttpServletResponse) resp;
        				res.sendRedirect("?unauthorized");

                    }
                    
                } catch (MalformedIdTokenException e) {
                    LOG.error("[IrideIdAdapterFilter::doFilter] " + e.toString(), e);
                }
            } else {
            	
            	InputStream is = getClass().getResourceAsStream("/application.properties");
    			Properties p=new Properties();  
    			p.load(is);

    			String devModeString =p.getProperty("dev.mode");
    			
    			if ("true".equalsIgnoreCase(devModeString)) {
    			    this.devMode = true;
    			} else {
    			    this.devMode = false; // Imposta devMode su false in caso contrario
    			}
    			
            	if(isDevMode()) {
            		LOG.debug("[IrideIdAdapterFilter::doFilter] dev mode attivo");
            		it.csi.risca.riscaboweb.dto.UserInfo userInfo = new it.csi.risca.riscaboweb.dto.UserInfo();
                    userInfo.setNome("Mario");
                    userInfo.setCognome("Rossi");
                    userInfo.setEnte("--");
                    userInfo.setRuolo("AMMINISTRATORE");
                    userInfo.setCodFisc("BVORCR93E08A182G");
                    userInfo.setLivAuth(0);
                    userInfo.setCommunity("");
                    userInfo.setAmbito(1L);
                    hreq.getSession().setAttribute(USERINFO_SESSIONATTR, userInfo);

                    
            	}else {
            		// il marcatore deve sempre essere presente altrimenti e' una
                    // condizione di errore (escluse le pagine home e di servizio)
            		if (marker == null) 
                    if (mustCheckPage(hreq.getRequestURI())) {
                        LOG.error(
                                "[IrideIdAdapterFilter::doFilter] Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza");
                        throw new ServletException(
                                "Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza");
                    }
            	}
            }
        }   

        
        fchn.doFilter(req, resp);

    }

    private boolean mustCheckPage(String requestURI) {

        return true;
    }

    public void destroy() {
        // NOP
    }

    private static final String DEVMODE_INIT_PARAM = "devmode";

    private boolean localMode = false;

    public void init(FilterConfig fc) throws ServletException {
        String sDevmode = fc.getInitParameter(DEVMODE_INIT_PARAM);
        if ("true".equals(sDevmode)) {
        	localMode = true;
        } else {
        	localMode = false;
        }
        
        ApplicationContext ctx = WebApplicationContextUtils
        	      .getRequiredWebApplicationContext(fc.getServletContext());
        	    this.serviceHelper = ctx.getBean(IrideServiceHelper.class);
        
    }

    public String getToken(HttpServletRequest httpreq) {
        String marker = (String) httpreq.getHeader(AUTH_ID_MARKER);
        if (marker == null && localMode) {
            return getTokenDevMode(httpreq);
        } else {
            try {
            	  if (marker != null) {
                      // gestione dell'encoding
                      String decodedMarker = new String(marker.getBytes("ISO-8859-1"), "UTF-8");
                      return decodedMarker;
            	  }

            } catch (java.io.UnsupportedEncodingException e) {
                // se la decodifica non funziona comunque sempre meglio restituire
                // il marker originale non decodificato
                return marker;
            }
        }
		return marker;
    }

    private String getTokenDevMode(HttpServletRequest httpreq) {
        String marker = (String) httpreq.getParameter(AUTH_ID_MARKER);
        return marker;
    }

    private String normalizeToken(String token) {
        return token;
    }
    
    private boolean isDevMode() {
		if (devMode != null) {
			return devMode;
		} else {
			return false;
		}
	}
    
    
    protected boolean isIdentitaAutentica(Identita identita) {
        LOGGER.debug("[IrideIdAdapterFilter::isIdentitaAutentica] BEGIN");
        boolean result = localMode || identita == null ? Boolean.TRUE : serviceHelper.isIdentitaAutentica(identita);
        LOGGER.debug("[IrideIdAdapterFilter::isIdentitaAutentica] END result: " + result);
        return result;
    }

}