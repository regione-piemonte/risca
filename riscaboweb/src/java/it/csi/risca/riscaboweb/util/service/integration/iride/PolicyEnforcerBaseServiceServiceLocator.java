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
 * PolicyEnforcerBaseServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscaboweb.util.service.integration.iride;

public class PolicyEnforcerBaseServiceServiceLocator extends org.apache.axis.client.Service implements PolicyEnforcerBaseServiceService {

    public PolicyEnforcerBaseServiceServiceLocator() {
    }


    public PolicyEnforcerBaseServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PolicyEnforcerBaseServiceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PolicyEnforcerBase
    private java.lang.String PolicyEnforcerBase_address = "http://tst-appweb.reteunitaria.piemonte.it/pep_wsfad_policy/services/PolicyEnforcerBase";

    public java.lang.String getPolicyEnforcerBaseAddress() {
        return PolicyEnforcerBase_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PolicyEnforcerBaseWSDDServiceName = "PolicyEnforcerBase";

    public java.lang.String getPolicyEnforcerBaseWSDDServiceName() {
        return PolicyEnforcerBaseWSDDServiceName;
    }

    public void setPolicyEnforcerBaseWSDDServiceName(java.lang.String name) {
        PolicyEnforcerBaseWSDDServiceName = name;
    }

    public PolicyEnforcerBaseService getPolicyEnforcerBase() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PolicyEnforcerBase_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPolicyEnforcerBase(endpoint);
    }

    public PolicyEnforcerBaseService getPolicyEnforcerBase(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            PolicyEnforcerBaseSoapBindingStub _stub = new PolicyEnforcerBaseSoapBindingStub(portAddress, this);
            _stub.setPortName(getPolicyEnforcerBaseWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPolicyEnforcerBaseEndpointAddress(java.lang.String address) {
        PolicyEnforcerBase_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (PolicyEnforcerBaseService.class.isAssignableFrom(serviceEndpointInterface)) {
                PolicyEnforcerBaseSoapBindingStub _stub = new PolicyEnforcerBaseSoapBindingStub(new java.net.URL(PolicyEnforcerBase_address), this);
                _stub.setPortName(getPolicyEnforcerBaseWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("PolicyEnforcerBase".equals(inputPortName)) {
            return getPolicyEnforcerBase();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "PolicyEnforcerBaseServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tst-wfexp-vip01.csi.it/pep_wsfad_policy/services/PolicyEnforcerBase", "PolicyEnforcerBase"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PolicyEnforcerBase".equals(portName)) {
            setPolicyEnforcerBaseEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
