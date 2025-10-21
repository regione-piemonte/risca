/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.csi.risca.riscaboweb.business.be.impl.PingApiServiceImpl;


@ApplicationPath("api/v1")
public class RiscabowebRestApplication extends Application{
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();
    public RiscabowebRestApplication(){
         //singletons.add(new LgspawclBE());
         singletons.add(new PingApiServiceImpl());

         
    }
    @Override
    public Set<Class<?>> getClasses() {
         return empty;
    }
    @Override
    public Set<Object> getSingletons() {
         return singletons;
    }

}