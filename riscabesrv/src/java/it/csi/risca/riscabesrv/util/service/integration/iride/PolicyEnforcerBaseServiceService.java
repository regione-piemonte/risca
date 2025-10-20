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
 * PolicyEnforcerBaseServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.service.integration.iride;

public interface PolicyEnforcerBaseServiceService extends javax.xml.rpc.Service {
    public java.lang.String getPolicyEnforcerBaseAddress();

    public PolicyEnforcerBaseService getPolicyEnforcerBase() throws javax.xml.rpc.ServiceException;

    public PolicyEnforcerBaseService getPolicyEnforcerBase(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
