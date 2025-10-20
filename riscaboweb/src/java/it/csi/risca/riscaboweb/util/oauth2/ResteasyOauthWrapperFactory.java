package it.csi.risca.riscaboweb.util.oauth2;

import it.csi.risca.riscaboweb.util.Constants;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import java.lang.reflect.Proxy;


/**
 * Factory Bean (in terminologia spring) dei wrapper
 * <p>
 * Il metodo <code>create</code> ritorna i wrapper
 * <p>
 * Per la configurazione occorre:
 * <p>
 * creare un GenericWrapperFactoryBean mediante il costruttore di default
 * impostare i seguenti parametri mediante setter:
 * <p>
 * <p>
 * endpoint
 * classe dell'interfaccia da wrappare
 * oggetto port da wrappare
 * un TokenRetryManager
 * <p>
 * chiamare il metodo create() per ottenere un oggetto wrappato
 *
 * <p>
 * Esempio di uso:
 * <pre>
 *      GenericWrapperFactoryBean gwfb = new GenericWrapperFactoryBean();
 *      gwfb.setEndPoint("http://tst-sw-eng.csi.it:80/wso108/api/ITT_QSP_echo/1.0");
 *      gwfb.setWrappedInterface(EipMockServiceAxisPortType.class);
 *      gwfb.setPort(port);
 *      gwfb.setTokenRetryManager(trm);
 *      EipMockServiceAxisPortType p = (EipMockServiceAxisPortType) gwfb.create();
 * </pre>
 */
public class ResteasyOauthWrapperFactory /*extends BaseFactoryBean*/ {


    protected static Logger logger = Logger.getLogger(Constants.COMPONENT_NAME);


    protected Class wrappedInterface;
    protected String endPoint;


    /**
     * @return l'endpoint del servizio
     */
    public String getEndPoint() {
        return endPoint;
    }

    /**
     * imposta l'endpoint del servizio
     *
     * @param endPoint endpoint del servizio (tipicamente un indirizzo sull'API Manager)
     */
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * @return ottiene l'interfaccia wrappata del servizio
     */
    public Class getWrappedInterface() {
        return wrappedInterface;
    }

    /**
     * imposta l'interfaccia del servizio
     *
     * @param wrappedInterface interfaccia del servizio
     */
    public void setWrappedInterface(Class wrappedInterface) {
        this.wrappedInterface = wrappedInterface;
    }

    /**
     * @return un oggetto che implementa l'interfaccia <code>wrappedClass</code> e wrappa
     * l'oggetto <code>port</code>
     * @throws ClassNotFoundException ClassNotFoundException
     */
    public Object create() throws ClassNotFoundException {
        logger.info("[GenericWrapperFactoryBean.create]" +
                "\nendpoint ......: " + endPoint +
                "\nwrappedClass ..: " + wrappedInterface);


        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(endPoint);

        ResteasyOauthWrapper wrapper = new ResteasyOauthWrapper(target, wrappedInterface, oauthHelper);
        Object o = Proxy.newProxyInstance(wrappedInterface.getClassLoader(), new Class[]{wrappedInterface}, wrapper);

        if (logger.isDebugEnabled())
            logger.debug("[GenericWrapperFactoryBean.create] created " + o.getClass().getName());
        return o;
    }


    private OauthHelper oauthHelper;

    /**
     * @return il <code>tokenRetryManager</code> proprietario dell'oggetto <code>port</code>
     */
    public OauthHelper getOauthHelper() {
        return oauthHelper;
    }

    /**
     * @param tokenRetryManager il proprietario dell'oggetto <code>port</code>
     */
    public void setOauthHelper(OauthHelper oauthHelper) {
        this.oauthHelper = oauthHelper;
    }

}