/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.csi.risca.riscabesrv.business.be.exception.ExceptionHandler;

import java.util.HashSet;
import java.util.Set;


/**
 * classe di avvio dell'applicazione
 */
@ApplicationPath("/api/v1")
public class RiscabesrvRestApplication extends Application{
    private final Set<Object> singletons = new HashSet<>();
    private final Set<Class<?>> empty = new HashSet<>();

    /**
     * costruttore della classe
     */
    public RiscabesrvRestApplication(){
        singletons.add(new ExceptionHandler());
    }

    /**
     * Non utilizzato
     *
     * @return un set vuoto
     */
    @Override
    public Set<Class<?>> getClasses() {
        return empty;
    }

    /**
     * Non utilizzato
     *
     * @return un set vuoto
     */
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}