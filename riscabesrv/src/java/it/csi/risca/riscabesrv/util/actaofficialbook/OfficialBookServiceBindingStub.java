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
 * OfficialBookServiceBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class OfficialBookServiceBindingStub extends org.apache.axis.client.Stub implements it.csi.risca.riscabesrv.util.actaofficialbook.OfficialBookServicePort_PortType {
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
        oper.setName("creaRegistrazione");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tipologiaCreazione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipoRegistrazioneDaCreare"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "infoRichiestaCreazione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneRequest"), it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneRegistrazione"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneRegistrazione.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "identificazioneCreazione"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("importaRegistrazione");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "classificazioneId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "registroId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "infoRegistrazione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoRegistrazione"), it.csi.risca.riscabesrv.util.actaofficialbook.InfoRegistrazione.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "senzaCreazioneSoggetti"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneRegistrazione"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneRegistrazione.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "identificazioneCreazione"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getRegistries");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "aooId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "anno"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"), java.lang.Integer.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "filter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType"), it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PagingResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "object"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getProperties");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "objectId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "filter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType"), it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "object"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("updateProperties");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "objectId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "changeToken"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ChangeTokenType"), it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "properties"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyType"), it.csi.risca.riscabesrv.util.actaofficialbook.PropertyType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "SimpleResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actaofficialbook.SimpleResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("query");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "target"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryableObjectType"), it.csi.risca.riscabesrv.util.actaofficialbook.QueryableObjectType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "filter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType"), it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "criteria"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryConditionType"), it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "navigationLimits"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "NavigationConditionInfoType"), it.csi.risca.riscabesrv.util.actaofficialbook.NavigationConditionInfoType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "maxItems"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"), java.lang.Integer.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "skipCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"), java.lang.Integer.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PagingResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "object"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPropertiesMassive");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "identifiers"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "filter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType"), it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectResponseType[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "object"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("creaAnnotazioneOB");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "target"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tipologiaTarget"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipologiaTargetAnnotazioneOB"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "properties"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertiesType"), it.csi.risca.riscabesrv.util.actaofficialbook.PropertiesType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneAnnotazioneOB"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneAnnotazioneOB.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "identificazioneCreazione"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("inviaSegnatura");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "origine"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "identificatoreRegistraizone"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "mittente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoMittenteIS"), it.csi.risca.riscabesrv.util.actaofficialbook.InfoMittenteIS.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "destinatari"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoDestinatarioIS"), it.csi.risca.riscabesrv.util.actaofficialbook.InfoDestinatarioIS[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "identitaDigitale"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("inviaSegnaturaRegistrazione");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "identificatoreRegistrazione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "mittente"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoMittenteIS"), it.csi.risca.riscabesrv.util.actaofficialbook.InfoMittenteIS.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "destinatari"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoDestinatarioRegistrazione"), it.csi.risca.riscabesrv.util.actaofficialbook.InfoDestinatarioRegistrazione[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "info"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoInvioSegnatura"), it.csi.risca.riscabesrv.util.actaofficialbook.InfoInvioSegnatura.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "identificatoreInvioSegnatura"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ricercaDatiInterscambio");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "identificatoreRegistrazione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "identificatoreSegnatura"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tipoMessaggio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipoMessaggio"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "maxItems"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"), java.lang.Integer.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "skipCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"), java.lang.Integer.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "DatiInterscambioResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actaofficialbook.DatiInterscambioResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "object"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("annullaRegistrazione");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "repositoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "principalId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "identificatoreRegistrazione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"), it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "motivazione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "SimpleResponseType"));
        oper.setReturnClass(it.csi.risca.riscabesrv.util.actaofficialbook.SimpleResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFault"),
                      "it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType",
                      new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"), 
                      true
                     ));
        _operations[11] = oper;

    }

    public OfficialBookServiceBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public OfficialBookServiceBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public OfficialBookServiceBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "AnnotazioniPropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.AnnotazioniPropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean");
            cachedSerQNames.add(qName);
            cls = boolean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ChangeTokenType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "CommonPropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.CommonPropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ComplexPropertyType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.ComplexPropertyType.class;
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
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaExtType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "DecodificaType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "enumCommonObjectType");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

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

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IDDBType");
            cachedSerQNames.add(qName);
            cls = long.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IDType");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdUtenteType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.IdUtenteType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "NavigationConditionInfoType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.NavigationConditionInfoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectResponseType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.ObjectResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PagingResponseType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.PropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.PropertyType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ProtocolloPropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.ProtocolloPropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryableObjectType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.QueryableObjectType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryConditionType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryNameType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.QueryNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "SimpleResponseType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.SimpleResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "timeStamp");
            cachedSerQNames.add(qName);
            cls = java.util.Calendar.class;
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

            qName = new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "XMLType");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "AnnotazioneOBPropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.AnnotazioneOBPropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "CorrispondenteMessaggioPropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.CorrispondenteMessaggioPropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "CorrispondentePropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.CorrispondentePropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "DatiInterscambioResponseType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.DatiInterscambioResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "DatoInterscambio");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.DatoInterscambio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "DestinatarioEsterno");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioEsterno.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "DestinatarioInterno");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumOfficialBookObjectType");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumStatoPec");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipoAPI");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipoContenitore");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipoCorrispondente");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipoIndirizzoTelematico");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipologiaCorrispondente");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipologiaSoggettoAssociato");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipologiaTargetAnnotazioneOB");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipoMessaggio");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipoPFPG");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "enumTipoRegistrazioneDaCreare");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneAnnotazioneOB");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneAnnotazioneOB.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneProtocollante");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollante.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneProtocollanteEstesa");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollanteEstesa.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneRegistrazione");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneRegistrazione.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IndirizzoTelematicoType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoAnnullamentoRegistrazione");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.InfoAnnullamentoRegistrazione.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoCreazioneAnnotazioneOB");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoCreazioneCorrispondente");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneCorrispondente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoCreazioneRegistrazione");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneRegistrazione.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoDateArrivo");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.InfoDateArrivo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoDestinatarioIS");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.InfoDestinatarioIS.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoDestinatarioRegistrazione");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.InfoDestinatarioRegistrazione.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoInvioSegnatura");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.InfoInvioSegnatura.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoMittenteIS");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.InfoMittenteIS.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoProtocolloMittente");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.InfoProtocolloMittente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoRegistrazione");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.InfoRegistrazione.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoSoggettoAssociato");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.InfoSoggettoAssociato.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "LogProtocolloPropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.LogProtocolloPropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "MessaggioPropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.MessaggioPropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "MittenteEsterno");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.MittenteEsterno.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "MittenteInterno");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.MittenteInterno.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "OfficialBookPropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.OfficialBookPropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "Protocollazione");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.Protocollazione.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "ProtocollazioneDaSmistamento");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.ProtocollazioneDaSmistamento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "ProtocollazioneDocumentoEsistente");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.ProtocollazioneDocumentoEsistente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneAPI");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneAPI.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneArrivo");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneArrivo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneInterna");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneInterna.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazionePartenza");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazionePartenza.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazionePropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazionePropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneRapida");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneRapida.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneRequest");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistroProtocolloPropertiesType");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.RegistroProtocolloPropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RiferimentoSoggettoEsistente");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.RiferimentoSoggettoEsistente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "SoggettoEsterno");
            cachedSerQNames.add(qName);
            cls = it.csi.risca.riscabesrv.util.actaofficialbook.SoggettoEsterno.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("prt.common.acaris.acta.doqui.it", "enumPFPGUL");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

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

    public it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneRegistrazione creaRegistrazione(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalId, java.lang.String tipologiaCreazione, it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneRequest infoRichiestaCreazione) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "creaRegistrazione"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, tipologiaCreazione, infoRichiestaCreazione});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneRegistrazione) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneRegistrazione) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneRegistrazione.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }
    @SuppressWarnings("deprecation")
    public it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneRegistrazione importaRegistrazione(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType classificazioneId, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registroId, it.csi.risca.riscabesrv.util.actaofficialbook.InfoRegistrazione infoRegistrazione, boolean senzaCreazioneSoggetti) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "importaRegistrazione"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, classificazioneId, registroId, infoRegistrazione, new java.lang.Boolean(senzaCreazioneSoggetti)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneRegistrazione) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneRegistrazione) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneRegistrazione.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType getRegistries(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType aooId, java.lang.Integer anno, it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType filter) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "getRegistries"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, aooId, anno, filter});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectResponseType getProperties(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectId, it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType filter) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "getProperties"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, objectId, principalId, filter});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.ObjectResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.ObjectResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.SimpleResponseType updateProperties(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectId, it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType changeToken, it.csi.risca.riscabesrv.util.actaofficialbook.PropertyType[] properties) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "updateProperties"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, objectId, principalId, changeToken, properties});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.SimpleResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.SimpleResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actaofficialbook.SimpleResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType query(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actaofficialbook.QueryableObjectType target, it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType filter, it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType[] criteria, it.csi.risca.riscabesrv.util.actaofficialbook.NavigationConditionInfoType navigationLimits, java.lang.Integer maxItems, java.lang.Integer skipCount) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
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
                return (it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectResponseType[] getPropertiesMassive(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] identifiers, it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType filter) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType {
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
                return (it.csi.risca.riscabesrv.util.actaofficialbook.ObjectResponseType[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.ObjectResponseType[]) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectResponseType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneAnnotazioneOB creaAnnotazioneOB(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType target, java.lang.String tipologiaTarget, it.csi.risca.riscabesrv.util.actaofficialbook.PropertiesType properties) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "creaAnnotazioneOB"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, target, tipologiaTarget, properties});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneAnnotazioneOB) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneAnnotazioneOB) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneAnnotazioneOB.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void inviaSegnatura(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalId, java.lang.String origine, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatoreRegistraizone, it.csi.risca.riscabesrv.util.actaofficialbook.InfoMittenteIS mittente, it.csi.risca.riscabesrv.util.actaofficialbook.InfoDestinatarioIS[] destinatari, java.lang.String identitaDigitale) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "inviaSegnatura"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, origine, identificatoreRegistraizone, mittente, destinatari, identitaDigitale});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType inviaSegnaturaRegistrazione(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatoreRegistrazione, it.csi.risca.riscabesrv.util.actaofficialbook.InfoMittenteIS mittente, it.csi.risca.riscabesrv.util.actaofficialbook.InfoDestinatarioRegistrazione[] destinatari, it.csi.risca.riscabesrv.util.actaofficialbook.InfoInvioSegnatura info) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "inviaSegnaturaRegistrazione"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, identificatoreRegistrazione, mittente, destinatari, info});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.DatiInterscambioResponseType ricercaDatiInterscambio(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatoreRegistrazione, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatoreSegnatura, java.lang.String tipoMessaggio, java.lang.Integer maxItems, java.lang.Integer skipCount) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "ricercaDatiInterscambio"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, identificatoreRegistrazione, identificatoreSegnatura, tipoMessaggio, maxItems, skipCount});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.DatiInterscambioResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.DatiInterscambioResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actaofficialbook.DatiInterscambioResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.SimpleResponseType annullaRegistrazione(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repositoryId, it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalId, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatoreRegistrazione, java.lang.String motivazione) throws java.rmi.RemoteException, it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "annullaRegistrazione"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {repositoryId, principalId, identificatoreRegistrazione, motivazione});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.SimpleResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.csi.risca.riscabesrv.util.actaofficialbook.SimpleResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, it.csi.risca.riscabesrv.util.actaofficialbook.SimpleResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) {
              throw (it.csi.risca.riscabesrv.util.actaofficialbook.AcarisFaultType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
