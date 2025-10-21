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
 * PolicyEnforcerBaseSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscaboweb.util.service.integration.iride;

import java.util.ArrayList;

import it.csi.iride2.iridefed.entity.Ruolo;
import it.csi.iride2.policy.entity.Actor;
import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;

public class PolicyEnforcerBaseSoapBindingStub extends org.apache.axis.client.Stub implements PolicyEnforcerBaseService {
	private ArrayList cachedSerClasses = new ArrayList();
	private ArrayList cachedSerQNames =new ArrayList();
	private ArrayList cachedSerFactories = new ArrayList();
	private ArrayList cachedDeserFactories =new ArrayList();

    static org.apache.axis.description.OperationDesc [] operations;

    static {
    	operations = new org.apache.axis.description.OperationDesc[13];
        initOperationDesc1();
        initOperationDesc2();
    }

    private static void initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("identificaUserPassword");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita"));
        oper.setReturnClass(Identita.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "identificaUserPasswordReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "MalformedUsernameException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "MalformedUsernameException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "AuthException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "AuthException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "IdProviderNotFoundException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdProviderNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("identificaCertificato");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita"));
        oper.setReturnClass(Identita.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "identificaCertificatoReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "CertRevokedException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "CertRevokedException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "CertOutsideValidityException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "CertOutsideValidityException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "IdProviderNotFoundException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdProviderNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("identificaUserPasswordPIN");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita"));
        oper.setReturnClass(Identita.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "identificaUserPasswordPINReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "MalformedUsernameException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "MalformedUsernameException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "AuthException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "AuthException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "IdProviderNotFoundException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdProviderNotFoundException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("isPersonaAutorizzataInUseCase");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita"), Identita.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UseCase"), UseCase.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "isPersonaAutorizzataInUseCaseReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "IdentitaNonAutenticaException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdentitaNonAutenticaException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "NoSuchUseCaseException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchUseCaseException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "NoSuchApplicationException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchApplicationException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("findUseCasesForPersonaInApplication");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita"), Identita.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Application"), Application.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "ArrayOf_tns1_UseCase"));
        oper.setReturnClass(UseCase[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "findUseCasesForPersonaInApplicationReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "IdentitaNonAutenticaException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdentitaNonAutenticaException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "NoSuchApplicationException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchApplicationException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("isIdentitaAutentica");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita"), Identita.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "isIdentitaAutenticaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getInfoPersonaInUseCase");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita"), Identita.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UseCase"), UseCase.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getInfoPersonaInUseCaseReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "IdentitaNonAutenticaException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdentitaNonAutenticaException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "NoSuchUseCaseException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchUseCaseException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "NoSuchApplicationException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchApplicationException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("findRuoliForPersonaInUseCase");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita"), Identita.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UseCase"), UseCase.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "ArrayOf_tns1_Ruolo"));
        oper.setReturnClass(Ruolo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "findRuoliForPersonaInUseCaseReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "IdentitaNonAutenticaException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdentitaNonAutenticaException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "NoSuchUseCaseException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchUseCaseException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "NoSuchApplicationException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchApplicationException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("findRuoliForPersonaInApplication");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita"), Identita.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Application"), Application.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "ArrayOf_tns1_Ruolo"));
        oper.setReturnClass(Ruolo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "findRuoliForPersonaInApplicationReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "IdentitaNonAutenticaException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdentitaNonAutenticaException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "NoSuchApplicationException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchApplicationException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getInfoPersonaSchema");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Ruolo"), Ruolo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getInfoPersonaSchemaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "BadRuoloException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "BadRuoloException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[9] = oper;

    }

    private static void initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("findActorsForPersonaInApplication");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita"), Identita.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Application"), Application.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "ArrayOf_tns1_Actor"));
        oper.setReturnClass(Actor[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "findActorsForPersonaInApplicationReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "IdentitaNonAutenticaException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdentitaNonAutenticaException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "NoSuchApplicationException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchApplicationException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("findActorsForPersonaInUseCase");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita"), Identita.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UseCase"), UseCase.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "ArrayOf_tns1_Actor"));
        oper.setReturnClass(Actor[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "findActorsForPersonaInUseCaseReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "IdentitaNonAutenticaException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdentitaNonAutenticaException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "NoSuchUseCaseException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchUseCaseException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "NoSuchApplicationException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchApplicationException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("isPersonaInRuolo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita"), Identita.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Ruolo"), Ruolo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "isPersonaInRuoloReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "UnrecoverableException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "IdentitaNonAutenticaException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdentitaNonAutenticaException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "SystemException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "BadRuoloException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "BadRuoloException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "fault"),
                      "InternalException",
                      new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException"), 
                      true
                     ));
        operations[12] = oper;

    }

    public PolicyEnforcerBaseSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public PolicyEnforcerBaseSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public PolicyEnforcerBaseSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "ArrayOf_tns1_Actor");
            cachedSerQNames.add(qName);
            cls = Actor[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Actor");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "ArrayOf_tns1_Ruolo");
            cachedSerQNames.add(qName);
            cls = Ruolo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Ruolo");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "ArrayOf_tns1_UseCase");
            cachedSerQNames.add(qName);
            cls = UseCase[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UseCase");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Actor");
            cachedSerQNames.add(qName);
            cls = Actor.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Application");
            cachedSerQNames.add(qName);
            cls = Application.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "AuthException");
            cachedSerQNames.add(qName);
            cls = AuthException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "BadIdentitaException");
            cachedSerQNames.add(qName);
            cls = BadIdentitaException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "BadPasswordException");
            cachedSerQNames.add(qName);
            cls = BadPasswordException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "BadRuoloException");
            cachedSerQNames.add(qName);
            cls = BadRuoloException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "CertException");
            cachedSerQNames.add(qName);
            cls = CertException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "CertOutsideValidityException");
            cachedSerQNames.add(qName);
            cls = CertOutsideValidityException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "CertRevokedException");
            cachedSerQNames.add(qName);
            cls = CertRevokedException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "CSIException");
            cachedSerQNames.add(qName);
            cls = CSIException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Identita");
            cachedSerQNames.add(qName);
            cls = Identita.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdentitaNonAutenticaException");
            cachedSerQNames.add(qName);
            cls = IdentitaNonAutenticaException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "IdProviderNotFoundException");
            cachedSerQNames.add(qName);
            cls = IdProviderNotFoundException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InactiveAccountException");
            cachedSerQNames.add(qName);
            cls = InactiveAccountException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "InternalException");
            cachedSerQNames.add(qName);
            cls = InternalException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "MalformedIdTokenException");
            cachedSerQNames.add(qName);
            cls = MalformedIdTokenException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "MalformedUsernameException");
            cachedSerQNames.add(qName);
            cls = MalformedUsernameException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchActorException");
            cachedSerQNames.add(qName);
            cls = NoSuchActorException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchApplicationException");
            cachedSerQNames.add(qName);
            cls = NoSuchApplicationException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "NoSuchUseCaseException");
            cachedSerQNames.add(qName);
            cls = NoSuchUseCaseException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "PasswordExpiredException");
            cachedSerQNames.add(qName);
            cls = PasswordExpiredException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "Ruolo");
            cachedSerQNames.add(qName);
            cls = Ruolo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "SystemException");
            cachedSerQNames.add(qName);
            cls = SystemException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UnrecoverableException");
            cachedSerQNames.add(qName);
            cls = UnrecoverableException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UseCase");
            cachedSerQNames.add(qName);
            cls = UseCase.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "UserException");
            cachedSerQNames.add(qName);
            cls = UserException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

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
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
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

    public Identita identificaUserPassword(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, UnrecoverableException, SystemException, MalformedUsernameException, AuthException, IdProviderNotFoundException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "identificaUserPassword"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Identita) _resp;
            } catch (java.lang.Exception _exception) {
                return (Identita) org.apache.axis.utils.JavaUtils.convert(_resp, Identita.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof MalformedUsernameException) {
              throw (MalformedUsernameException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof AuthException) {
              throw (AuthException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof IdProviderNotFoundException) {
              throw (IdProviderNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public Identita identificaCertificato(byte[] in0) throws java.rmi.RemoteException, UnrecoverableException, CertRevokedException, CertOutsideValidityException, SystemException, IdProviderNotFoundException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "identificaCertificato"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Identita) _resp;
            } catch (java.lang.Exception _exception) {
                return (Identita) org.apache.axis.utils.JavaUtils.convert(_resp, Identita.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof CertRevokedException) {
              throw (CertRevokedException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof CertOutsideValidityException) {
              throw (CertOutsideValidityException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof IdProviderNotFoundException) {
              throw (IdProviderNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public Identita identificaUserPasswordPIN(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, UnrecoverableException, SystemException, MalformedUsernameException, AuthException, IdProviderNotFoundException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "identificaUserPasswordPIN"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1, in2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Identita) _resp;
            } catch (java.lang.Exception _exception) {
                return (Identita) org.apache.axis.utils.JavaUtils.convert(_resp, Identita.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof MalformedUsernameException) {
              throw (MalformedUsernameException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof AuthException) {
              throw (AuthException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof IdProviderNotFoundException) {
              throw (IdProviderNotFoundException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public boolean isPersonaAutorizzataInUseCase(Identita in0, UseCase in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchUseCaseException, NoSuchApplicationException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "isPersonaAutorizzataInUseCase"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof IdentitaNonAutenticaException) {
              throw (IdentitaNonAutenticaException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof NoSuchUseCaseException) {
              throw (NoSuchUseCaseException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof NoSuchApplicationException) {
              throw (NoSuchApplicationException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public UseCase[] findUseCasesForPersonaInApplication(Identita in0, Application in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchApplicationException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "findUseCasesForPersonaInApplication"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (UseCase[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (UseCase[]) org.apache.axis.utils.JavaUtils.convert(_resp, UseCase[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof IdentitaNonAutenticaException) {
              throw (IdentitaNonAutenticaException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof NoSuchApplicationException) {
              throw (NoSuchApplicationException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public boolean isIdentitaAutentica(Identita in0) throws java.rmi.RemoteException, UnrecoverableException, SystemException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "isIdentitaAutentica"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public java.lang.String getInfoPersonaInUseCase(Identita in0, UseCase in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchUseCaseException, NoSuchApplicationException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "getInfoPersonaInUseCase"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof IdentitaNonAutenticaException) {
              throw (IdentitaNonAutenticaException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof NoSuchUseCaseException) {
              throw (NoSuchUseCaseException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof NoSuchApplicationException) {
              throw (NoSuchApplicationException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public Ruolo[] findRuoliForPersonaInUseCase(Identita in0, UseCase in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchUseCaseException, NoSuchApplicationException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "findRuoliForPersonaInUseCase"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Ruolo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (Ruolo[]) org.apache.axis.utils.JavaUtils.convert(_resp, Ruolo[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof IdentitaNonAutenticaException) {
              throw (IdentitaNonAutenticaException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof NoSuchUseCaseException) {
              throw (NoSuchUseCaseException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof NoSuchApplicationException) {
              throw (NoSuchApplicationException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public Ruolo[] findRuoliForPersonaInApplication(Identita in0, Application in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchApplicationException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "findRuoliForPersonaInApplication"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Ruolo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (Ruolo[]) org.apache.axis.utils.JavaUtils.convert(_resp, Ruolo[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof IdentitaNonAutenticaException) {
              throw (IdentitaNonAutenticaException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof NoSuchApplicationException) {
              throw (NoSuchApplicationException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public java.lang.String getInfoPersonaSchema(Ruolo in0) throws java.rmi.RemoteException, UnrecoverableException, SystemException, BadRuoloException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "getInfoPersonaSchema"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof BadRuoloException) {
              throw (BadRuoloException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public Actor[] findActorsForPersonaInApplication(Identita in0, Application in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchApplicationException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "findActorsForPersonaInApplication"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Actor[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (Actor[]) org.apache.axis.utils.JavaUtils.convert(_resp, Actor[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof IdentitaNonAutenticaException) {
              throw (IdentitaNonAutenticaException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof NoSuchApplicationException) {
              throw (NoSuchApplicationException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public Actor[] findActorsForPersonaInUseCase(Identita in0, UseCase in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchUseCaseException, NoSuchApplicationException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "findActorsForPersonaInUseCase"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (Actor[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (Actor[]) org.apache.axis.utils.JavaUtils.convert(_resp, Actor[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof IdentitaNonAutenticaException) {
              throw (IdentitaNonAutenticaException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof NoSuchUseCaseException) {
              throw (NoSuchUseCaseException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof NoSuchApplicationException) {
              throw (NoSuchApplicationException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public boolean isPersonaInRuolo(Identita in0, Ruolo in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, BadRuoloException, InternalException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://interfaces.policy.iride2.csi.it", "isPersonaInRuolo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof UnrecoverableException) {
              throw (UnrecoverableException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof IdentitaNonAutenticaException) {
              throw (IdentitaNonAutenticaException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof SystemException) {
              throw (SystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof BadRuoloException) {
              throw (BadRuoloException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof InternalException) {
              throw (InternalException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
