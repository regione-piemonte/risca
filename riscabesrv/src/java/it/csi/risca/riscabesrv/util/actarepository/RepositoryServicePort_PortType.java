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
 * RepositoryServicePort_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarepository;

public interface RepositoryServicePort_PortType extends java.rmi.Remote {
    public it.csi.risca.riscabesrv.util.actarepository.AcarisRepositoryEntryType[] getRepositories() throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actarepository.AcarisFaultType;
    public it.csi.risca.riscabesrv.util.actarepository.AcarisRepositoryInfoType getRepositoryInfo(it.csi.risca.riscabesrv.util.actarepository.ObjectIdType repositoryId) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actarepository.AcarisFaultType;
}
