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
 * BackOfficeServiceBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actabackoffice;

public class BackOfficeServiceBindingStub extends org.apache.axis.client.Stub implements it.csi.risca.riscabesrv.util.actabackoffice.BackOfficeServicePort_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[12];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPrincipal");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idUtente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "CodiceFiscaleType"), it.csi.risca.riscabesrv.util.actabackoffice.CodiceFiscaleType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idAOO"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdAOOType"), it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idStruttura"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdStrutturaType"), it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idNodo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdNodoType"), it.csi.risca.riscabesrv.util.actabackoffice.IdNodoType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "PrincipalResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actabackoffice.PrincipalResponseType[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "principal"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPrincipalExt");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idUtente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "CodiceFiscaleType"), it.csi.risca.riscabesrv.util.actabackoffice.CodiceFiscaleType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idAOO"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdAOOType"), it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idStruttura"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdStrutturaType"), it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idNodo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdNodoType"), it.csi.risca.riscabesrv.util.actabackoffice.IdNodoType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "clientApplicationInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "ClientApplicationInfo"), it.csi.risca.riscabesrv.util.actabackoffice.ClientApplicationInfo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "PrincipalExtResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actabackoffice.PrincipalExtResponseType[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "principal"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getDescendants");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "path"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "enumBackOfficeNavigationPathType"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "rootNodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "date"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"), java.util.Date.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "depth"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"), java.lang.Integer.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "filter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType"), it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "maxItems"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"), java.lang.Integer.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "skipCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"), java.lang.Integer.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PagingResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actabackoffice.PagingResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "objects"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getObjectParents");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "rootNodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "date"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"), java.util.Date.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "filter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType"), it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "objects"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getRelazioniStruttura");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "objectId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ruolo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "enumRuoloInGerarchiaType"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "date"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"), java.util.Date.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "filter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType"), it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "RelazioniStrutturaPropertiesType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actabackoffice.RelazioniStrutturaPropertiesType[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getProperties");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "objectId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "filter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType"), it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "object"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPropertiesMassive");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "identifiers"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "filter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType"), it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "object"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getDettaglioAOO");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idAOO"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdAOOType"), it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "DettaglioAOOType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actabackoffice.DettaglioAOOType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "dettaglioAOO"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getDettaglioStruttura");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idStruttura"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdStrutturaType"), it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "DettaglioStrutturaType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actabackoffice.DettaglioStrutturaType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "dettaglioStruttura"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getQueryableObjects");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "service"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "enumServiceType"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryableObjectType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actabackoffice.QueryableObjectType[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "queryableObjectList"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getQueryableObjectMetadata");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "queryableObject"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryableObjectType"), it.csi.risca.riscabesrv.util.actabackoffice.QueryableObjectType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operation"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "enumPropertyFilterOperation"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectMetadataType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actabackoffice.ObjectMetadataType[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "queryableObjectMetadataList"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("query");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "target"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryableObjectType"), it.csi.risca.riscabesrv.util.actabackoffice.QueryableObjectType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "filter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType"), it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "criteria"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryConditionType"), it.csi.risca.riscabesrv.util.actabackoffice.QueryConditionType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "navigationLimits"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "NavigationConditionInfoType"), it.csi.risca.riscabesrv.util.actabackoffice.NavigationConditionInfoType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "maxItems"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"), java.lang.Integer.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "skipCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"), java.lang.Integer.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PagingResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actabackoffice.PagingResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "object"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[11] = oper;

    }

    public BackOfficeServiceBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public BackOfficeServiceBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public BackOfficeServiceBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "ClientApplicationInfo");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.ClientApplicationInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "CollocazioneUtente");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.CollocazioneUtente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "DettaglioAOOType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.DettaglioAOOType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "DettaglioStrutturaType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.DettaglioStrutturaType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "enumBackOfficeNavigationPathType");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "enumRuoloInGerarchiaType");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "enumServiceType");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "PrincipalExtResponseType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.PrincipalExtResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "PrincipalResponseType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.PrincipalResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "ProfiloPropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.ProfiloPropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "RelazioniStrutturaPropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.RelazioniStrutturaPropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean");
            cachedSerQNames.add(qName);
            cls = boolean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "CodiceFiscaleType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.CodiceFiscaleType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ComplexPropertyType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.ComplexPropertyType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date");
            cachedSerQNames.add(qName);
            cls = java.util.Date.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "DecodificaExtType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.DecodificaExtType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "DecodificaType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.DecodificaType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "enumErrorCodeType");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "enumObjectType");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "enumPropertyFilter");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "enumPropertyFilterOperation");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "enumQueryOperator");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "enumServiceException");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdAOOType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IDDBType");
            cachedSerQNames.add(qName);
            cls = long.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdNodoType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.IdNodoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdProfiloType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.IdProfiloType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdStrutturaType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IDType");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ItemType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.ItemType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "NavigationConditionInfoType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.NavigationConditionInfoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectMetadataType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.ObjectMetadataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectResponseType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PagingResponseType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.PagingResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterConfigurationInfoType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterConfigurationInfoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.PropertyType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryableObjectType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.QueryableObjectType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryConditionType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.QueryConditionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryNameType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.QueryNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ValueType");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string");
            qName2 = new javax.xml.namespace.QName("", "content");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "VarargsType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actabackoffice.ItemType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ItemType");
            qName2 = new javax.xml.namespace.QName("", "items");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.PrincipalResponseType[] getPrincipal(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actabackoffice.CodiceFiscaleType idUtente, it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType idAOO, it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType idStruttura, it.csi.risca.riscabesrv.util.actabackoffice.IdNodoType idNodo) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "getPrincipal"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, idUtente, idAOO, idStruttura, idNodo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actabackoffice.PrincipalResponseType[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actabackoffice.PrincipalResponseType[]) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actabackoffice.PrincipalResponseType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.PrincipalExtResponseType[] getPrincipalExt(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actabackoffice.CodiceFiscaleType idUtente, it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType idAOO, it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType idStruttura, it.csi.risca.riscabesrv.util.actabackoffice.IdNodoType idNodo, it.csi.risca.riscabesrv.util.actabackoffice.ClientApplicationInfo clientApplicationInfo) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "getPrincipalExt"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, idUtente, idAOO, idStruttura, idNodo, clientApplicationInfo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actabackoffice.PrincipalExtResponseType[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actabackoffice.PrincipalExtResponseType[]) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actabackoffice.PrincipalExtResponseType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.PagingResponseType getDescendants(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType principalId, java.lang.String path, it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType rootNodeId, java.util.Date date, java.lang.Integer depth, it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType filter, java.lang.Integer maxItems, java.lang.Integer skipCount) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "getDescendants"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, path, rootNodeId, date, depth, filter, maxItems, skipCount});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actabackoffice.PagingResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actabackoffice.PagingResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actabackoffice.PagingResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType[] getObjectParents(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType rootNodeId, java.util.Date date, it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType filter) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "getObjectParents"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, rootNodeId, date, filter});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType[]) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.RelazioniStrutturaPropertiesType[] getRelazioniStruttura(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType objectId, java.lang.String ruolo, java.util.Date date, it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType filter) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "getRelazioniStruttura"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, objectId, ruolo, date, filter});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actabackoffice.RelazioniStrutturaPropertiesType[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actabackoffice.RelazioniStrutturaPropertiesType[]) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actabackoffice.RelazioniStrutturaPropertiesType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType getProperties(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType objectId, it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType filter) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "getProperties"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, objectId, principalId, filter});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType[] getPropertiesMassive(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType[] identifiers, it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType filter) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "getPropertiesMassive"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, identifiers, filter});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType[]) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actabackoffice.ObjectResponseType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.DettaglioAOOType getDettaglioAOO(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType idAOO) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "getDettaglioAOO"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, idAOO});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actabackoffice.DettaglioAOOType) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actabackoffice.DettaglioAOOType) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actabackoffice.DettaglioAOOType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.DettaglioStrutturaType getDettaglioStruttura(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType idStruttura) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "getDettaglioStruttura"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, idStruttura});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actabackoffice.DettaglioStrutturaType) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actabackoffice.DettaglioStrutturaType) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actabackoffice.DettaglioStrutturaType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.QueryableObjectType[] getQueryableObjects(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType repositoryId, java.lang.String service) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "getQueryableObjects"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, service});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actabackoffice.QueryableObjectType[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actabackoffice.QueryableObjectType[]) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actabackoffice.QueryableObjectType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.ObjectMetadataType[] getQueryableObjectMetadata(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actabackoffice.QueryableObjectType queryableObject, java.lang.String operation) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "getQueryableObjectMetadata"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, queryableObject, operation});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actabackoffice.ObjectMetadataType[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actabackoffice.ObjectMetadataType[]) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actabackoffice.ObjectMetadataType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.PagingResponseType query(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actabackoffice.QueryableObjectType target, it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterType filter, it.csi.risca.riscabesrv.util.actabackoffice.QueryConditionType[] criteria, it.csi.risca.riscabesrv.util.actabackoffice.NavigationConditionInfoType navigationLimits, java.lang.Integer maxItems, java.lang.Integer skipCount) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "query"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, target, filter, criteria, navigationLimits, maxItems, skipCount});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actabackoffice.PagingResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actabackoffice.PagingResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actabackoffice.PagingResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actabackoffice.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
