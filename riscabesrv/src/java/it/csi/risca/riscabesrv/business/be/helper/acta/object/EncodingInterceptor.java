/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.acta.object;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * Interceptor necessario per integrazione di un client che utilizza CXF 2.7.14 (da verificare con versioni successive)
 * Nel caso il servizio invocato utilizza MTOM+XOP, l'encoding settato da ACARIS (che utilizza CXF 2.2.3) è
 * ISO-8859-1.
 * Mentre l'interazione tra client e server che utilizzano la stessa versione va a buon fine (il client riceve la
 * response codificata e la decodifica correttamente utilizzando ISO-8859-1), nel caso il client utilizzi un altra
 * versione, questo imposta l'encoding a UTF-8 (forse in base al content-type del pacchetto SOAP?).
 * Utilizzando questo interceptor inserito nella chain default nella fase POST_STREAM (vd
 * http://cxf.apache.org/docs/interceptors.html), si va a modificare l'encoding utilizzato da
 * <code>@link StaxInInterceptor</code> nella stessa fase: questo si occupa di creare il reader per l'unmarshalling del
 * contenuto del messaggio (SOAP body).
 * 
 * @author andrea.siringo
 *
 */
public class EncodingInterceptor extends AbstractPhaseInterceptor<Message> {

    public EncodingInterceptor() {
        super(Phase.POST_STREAM);
    }

    @Override
    public void handleMessage(Message message) throws Fault {

        String contentType = (String) message.get(Message.CONTENT_TYPE);

        if (contentType != null && contentType.contains("type=\"application/xop+xml\"") && MessageUtils.isRequestor(message)) {

            String encoding = (String) message.get(Message.ENCODING);

            if (encoding == null || !encoding.equals("ISO-8859-1")) {
                System.out.println(this.getClass().getSimpleName() + " - encoding: " + encoding);
                System.out.println(this.getClass().getSimpleName() + " - modifica encoding a ISO-8859-1");
                message.put(Message.ENCODING, "ISO-8859-1");
            }
        }

    }
}
