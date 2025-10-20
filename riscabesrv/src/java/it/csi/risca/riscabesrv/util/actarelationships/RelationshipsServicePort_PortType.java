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
 * RelationshipsServicePort_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public interface RelationshipsServicePort_PortType extends java.rmi.Remote {
    public it.csi.risca.riscabesrv.util.actarelationships.RelationshipPropertiesType[] getObjectRelationships(it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actarelationships.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType objectId, java.lang.String typeId, java.lang.String direction, it.csi.risca.riscabesrv.util.actarelationships.PropertyFilterType filter) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actarelationships.AcarisFaultType;
}
