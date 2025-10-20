package it.csi.risca.riscaboweb.util.oauth2;

import it.csi.risca.riscaboweb.util.Constants;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Gestione token oauth2
 * <p>
 * <p>
 * Accede al servizio /api/token dell'API Manager.
 * mantiene in cache locale l'utimo token
 * <p>
 * L'implemetazione attuale si basa su Resteasy per
 * l'accesso al servizio REST
 * <p>
 * Per la configurazione usare un costruttore opportuno; i parametri da usare sono:
 * <ul>
 * <li>oauthURL l'endpoint del servizio REST /api/token</li>
 * <li>@see <a href="https://docs.wso2.com/display/AM191/Token+API">consumerKey</a>
 * <li>@see <a href="https://docs.wso2.com/display/AM191/Token+API">consumerSecret</a>
 * <li>timeout       timeout in millisecondi della connessione verso /api/token</li>
 * </ul>
 * <p>
 * Esempio:
 * <pre>
 *         OauthHelper oauth = new OauthHelper("https://tst-sw-eng.csi.it:443/wso108/api/token",
 *                                             "660PznSzJu706tpmfHaPsMHT5coa",
 *                                             "hhimfC5jF3Y0eonBL2PECvMJDGIa",
 *                                             10000);
 *
 * </pre>
 */
public class OauthHelper {

    protected static Logger logger = Logger.getLogger(Constants.COMPONENT_NAME);

    public static final String ACCESS_TOKEN = "access_token";
    public static final String OAUTH2_GRANT_TYPE = "grantType";
    public static final String OAUTH2_GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

    /**
     * @param oauthURL       l'endpoint del servizio REST /api/token
     * @param consumerKey    consumerKey @see <a href="https://docs.wso2.com/display/AM191/Token+API">consumerKey</a>
     * @param consumerSecret consumerSecret @see <a href="https://docs.wso2.com/display/AM191/Token+API">consumerSecret</a>
     * @param timeout        timeout in millisecondi della connessione verso /api/token
     */
    public OauthHelper(String oauthURL, String consumerKey, String consumerSecret, int timeout) {
        this.oauthURL = (oauthURL != null) ? oauthURL.trim() : oauthURL;
        this.consumerKey = (consumerKey != null) ? consumerKey.trim() : consumerKey;
        this.consumerSecret = (consumerSecret != null) ? consumerSecret.trim() : consumerSecret;
        this.timeout = timeout;
    }

    /**
     * @param oauthURL       l'endpoint del servizio REST /api/token
     * @param consumerKey    consumerKey @see <a href="https://docs.wso2.com/display/AM191/Token+API">consumerKey</a>
     * @param consumerSecret consumerSecret @see <a href="https://docs.wso2.com/display/AM191/Token+API">consumerSecret</a>
     */

    public OauthHelper(String oauthURL, String consumerKey, String consumerSecret) {
        this(oauthURL, consumerKey, consumerSecret, 30000);
    }

    private String oauthURL;
    private String consumerKey;
    private String consumerSecret;
    private int timeout;
    private AtomicLong hashCount = new AtomicLong(0);
    private static volatile String token = "dummytoken";

    private static volatile Long expires;
// # accessi token in cache
    private static volatile long counter;   // # richieste token


    /**
     * @return il tempo di expires del token in cache
     */
    public static long getExpires() {
        if (expires != null)
            return expires.longValue();
        return 0;
    }

    /**
     * @return il token da usare verso l'API Manager
     * <p>
     * Se esiste in cache viene tornato l'ultimo token ottenuto dall'API Manager
     * altrimenti si effettua la richiesta e si torna il valore ottenuto
     */
    public String getToken() {
        if (token != null) {
            hashCount.incrementAndGet();
            return token; // use cache if exist
        }
        return getTokenInternal();
    }

    /**
     * @return il token da usare verso l'API Manager
     * <p>
     * Forza l'ottenimento del token dall'API Manager. Il valore ritornato viene tenuto
     * in cache locale per soddisfare successive <code>getToken</code>
     * Usare questo metodo solo se e' noto a priori che il valore in cache e' invalido
     */
    public String getNewToken() {
        token = null;                  // invalidate cache
        return getTokenInternal();
    }

    /**
     * Implementa la connessione verso l'API Manager
     *
     * @return il token ottenuto dall'API Manager
     */
    private String getTokenInternal() {

        logger.info("[OauthHelper.getTokenInternal]\n" +
                "oauthURL ......: " + oauthURL + "\n" +
                "consumerKey ...: " + consumerKey + "\n" +
                "consumerSecret : " + Util.maskForLog(consumerSecret) + "\n" +
                "timeout .......: " + timeout);

        ResteasyClient client = new ResteasyClientBuilder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS).build();
        ResteasyWebTarget target = client.target(this.oauthURL);
        Form tokenForm = new Form();
        tokenForm
                .param("grant_type", "client_credentials")
                .param("client_id", consumerKey)
                .param("client_secret", consumerSecret);

        Response tokenResponse = target.request()
                .header("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2")
                .header("Content-type", "application/x-www-form-urlencoded")
                .post(Entity.form(tokenForm));
        int statusCode = tokenResponse.getStatus();
        if (statusCode != HttpStatus.SC_OK) {
            logger.error("wrong HTTP status code: " + statusCode + "\nStatusLine:" + tokenResponse.getStatusInfo());
            token = null;
            expires = null;
            logger.warn("OAUT2 token set to null");
            return token;
        }

        // Read the response body.
        GetTokenResponse responseBody = tokenResponse.readEntity(GetTokenResponse.class);

        token = responseBody.getAccess_token();
        expires = responseBody.getExpires_in();

        if (token == null || expires == null) {
            logger.error("unexpected reply: ");
            token = null;
            expires = null;
            logger.info("OAUT2 token set to null");
            return token;
        }

        String out = "token di accesso apiman[" + counter + "," + hashCount + "] " + Util.maskForLog(token) +
                " expires in " + expires;

        logger.info(out);
        return token;
    }

}