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
 * NavigationServicePort_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actanavigation;

public interface NavigationServicePort_PortType extends java.rmi.Remote {
    public it.csi.risca.riscabesrv.util.actanavigation.PagingResponseType getDescendants(it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType folderId, it.csi.risca.riscabesrv.util.actanavigation.PrincipalIdType principalId, java.lang.Integer depth, it.csi.risca.riscabesrv.util.actanavigation.PropertyFilterType filter, java.lang.Integer maxItems, java.lang.Integer skipCount) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actanavigation.AcarisFaultType;
    public it.csi.risca.riscabesrv.util.actanavigation.PagingResponseType getChildren(it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType folderId, it.csi.risca.riscabesrv.util.actanavigation.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actanavigation.PropertyFilterType filter, java.lang.Integer maxItems, java.lang.Integer skipCount) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actanavigation.AcarisFaultType;
    public it.csi.risca.riscabesrv.util.actanavigation.ObjectResponseType getFolderParent(it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType folderId, it.csi.risca.riscabesrv.util.actanavigation.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actanavigation.PropertyFilterType filter) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actanavigation.AcarisFaultType;
    public it.csi.risca.riscabesrv.util.actanavigation.ObjectResponseType[] getObjectParents(it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType objectId, it.csi.risca.riscabesrv.util.actanavigation.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actanavigation.PropertyFilterType filter) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actanavigation.AcarisFaultType;
}
