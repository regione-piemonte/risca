/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.util.oauth2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;


/**
 * Dato un ResteasyWebTarget, e data la classe di interfaccia JAX-RS, questo InvocationHandler
 * fornisce l'implementazione di un proxy dinamico che, in aggiunta,
 * implementa il meccanismo di autenticazione oauth2, che prevede:
 * 1) il richiamo del servizio utilizzando un token oauth2 in cache (gestita dall'OauthHelper)
 * 2) se l'invocazione fallisce (http 401) si effettua il refresh del token, si aggiorna
 * la cache e si rieffettua la chiamata
 * Poiche' si utilizza un proxy dinamico questa classe implementa InvocationHandler.
 * Di fatto si tratta di un proxy di proxy. Il proxy più a valle e' un proxy costruito con le client api
 * di resteasy, e che implementa l'interfaccia JAX-RS dell'API effettiva. Il proxy su cui si basa questo
 * InvocationHandler implementa la stessa interfaccia ma wrappa il proxy effettivo con la logica di
 * gestione dell'autenticazione oauth e del retry.
 * <p>
 * Per ottenere un nuovo token quando necessario viene utilizzato un oggetto di tipo OAuthHelper, che deve
 * essere istanziato e configurato esternamente e passato al costruttore.
 * <p>
 * E' prevista una cache del token OAuth2, in modo da non dover ogni volta ottenere un nuovo token.
 */
public class ResteasyOauthWrapper implements InvocationHandler {

    private Class jaxrsIntf;

    private OauthHelper oauthHelper;

    private ResteasyWebTarget webTarget;

    /**
     * @param target      il web-target base del servizio effettivo (da questo viene ottenuto il proxy
     *                    jax-rs
     * @param jaxrsIntf   l'interfaccia jaxrs del servizio effettivo
     * @param oauthHelper helper per la gestione dei token
     */
    public ResteasyOauthWrapper(ResteasyWebTarget target, Class jaxrsIntf, OauthHelper oauthHelper) {
        this.jaxrsIntf = jaxrsIntf;
        this.oauthHelper = oauthHelper;
        this.webTarget = target;
        // registrazione di un interceptor che inserisce nelle chiamate l'header di autenticazione oauth2
        webTarget.register(new BearerAuthentication(oauthHelper));
    }


    /**
     * @param proxy  non utilizzato (si crea un proxy jaxrs ogni volta, a partire dal web-target)
     * @param method il metodo invocato
     * @param args   gli argomenti da passare al metodo
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Questo proxy implementa jaxrsintf
        Object jaxrsProxy = getNewJaxrsProxy();
        try {
            Response resp = (Response) method.invoke(jaxrsProxy, args);
            // In caso di errore di autenticazione (401) non viene restituita una eccezione
            // ma una response con codice 401
            if (isOauthError(resp)) {
                // Refresh token & retry....
                refreshToken();
                jaxrsProxy = getNewJaxrsProxy();
                try {
                    return method.invoke(jaxrsProxy, args);
                } catch (InvocationTargetException ite2) {
                    throw ite2.getTargetException();
                }
            } else {
                return resp;
            }
        } catch (InvocationTargetException ite) {
            // Se l'eccezione e legata ad un problema di autenticazione effettua il giro di retry
            // altrimenti restituisce l'eccezione interna.
            // Controllo inserito per completezza ma normalmente un errore 401 non viene restituito
            // come exception
            throw ite.getTargetException();
        }
    }

    /**
     * tramite l'oauth helper ottiene un nuovo token (che viene mantenuto nella cache
     * di OauthHelper)
     */
    private void refreshToken() {
        oauthHelper.getNewToken();
    }

    /**
     * Gli errori di autenticazione OAUTH2 sono  restituiti come HTTP 401
     *
     * @param resp Response
     * @return boolean
     */
    private boolean isOauthError(Response resp) {
        return resp.getStatus() == 401;
    }



    private Object _cachedProxy = null;

    /**
     * crea un nuovo proxy per l''interfaccia jax-rs specificata, a partire dal
     * web target configurato
     */
    @SuppressWarnings("unchecked")
    private Object getNewJaxrsProxy() {
        if (_cachedProxy == null) {
            Object newProxy = this.webTarget.proxy(jaxrsIntf);
            _cachedProxy = newProxy;
        }
        //
        return _cachedProxy;
    }

}