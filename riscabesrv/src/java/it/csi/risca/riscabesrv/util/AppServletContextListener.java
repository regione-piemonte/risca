/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * The type App servlet context listener.
 *
 * @author CSI PIEMONTE
 */
public class AppServletContextListener implements ServletContextListener {

    private static ServletContext sc;

    /**
     *
     * @param servletContextEvent servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        /* Sets the context in a static variable */
        AppServletContextListener.sc = servletContextEvent.getServletContext();
    }

    /**
     *
     * @param servletContextEvent servletContextEvent
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    /**
     * Gets servlet context.
     *
     * @return ServletContext servlet context
     */
    public static ServletContext getServletContext() {
        return sc;
    }
}