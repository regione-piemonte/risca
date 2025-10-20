/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
/**
 * NavigationService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actanavigation;

public interface NavigationService extends javax.xml.rpc.Service {
    public java.lang.String getNavigationServicePortAddress();

    public it.csi.risca.riscabesrv.util.actanavigation.NavigationServicePort_PortType getNavigationServicePort() throws javax.xml.rpc.ServiceException;

    public it.csi.risca.riscabesrv.util.actanavigation.NavigationServicePort_PortType getNavigationServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
