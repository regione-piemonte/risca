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
 * NavigationServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actanavigation;

public class NavigationServiceLocator extends org.apache.axis.client.Service implements it.csi.risca.riscabesrv.util.actanavigation.NavigationService {

    public NavigationServiceLocator() {
    }


    public NavigationServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NavigationServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NavigationServicePort
    private java.lang.String NavigationServicePort_address = "http://tst-applogic.reteunitaria.piemonte.it/actasrv/navigationWS";

    public java.lang.String getNavigationServicePortAddress() {
        return NavigationServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NavigationServicePortWSDDServiceName = "NavigationServicePort";

    public java.lang.String getNavigationServicePortWSDDServiceName() {
        return NavigationServicePortWSDDServiceName;
    }

    public void setNavigationServicePortWSDDServiceName(java.lang.String name) {
        NavigationServicePortWSDDServiceName = name;
    }

    public it.csi.risca.riscabesrv.util.actanavigation.NavigationServicePort_PortType getNavigationServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NavigationServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNavigationServicePort(endpoint);
    }

    public it.csi.risca.riscabesrv.util.actanavigation.NavigationServicePort_PortType getNavigationServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.csi.risca.riscabesrv.util.actanavigation.NavigationServiceBindingStub _stub = new it.csi.risca.riscabesrv.util.actanavigation.NavigationServiceBindingStub(portAddress, this);
            _stub.setPortName(getNavigationServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNavigationServicePortEndpointAddress(java.lang.String address) {
        NavigationServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.csi.risca.riscabesrv.util.actanavigation.NavigationServicePort_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                it.csi.risca.riscabesrv.util.actanavigation.NavigationServiceBindingStub _stub = new it.csi.risca.riscabesrv.util.actanavigation.NavigationServiceBindingStub(new java.net.URL(NavigationServicePort_address), this);
                _stub.setPortName(getNavigationServicePortWSDDServiceName());
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
        if ("NavigationServicePort".equals(inputPortName)) {
            return getNavigationServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("navigationservice.acaris.acta.doqui.it", "NavigationService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("navigationservice.acaris.acta.doqui.it", "NavigationServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NavigationServicePort".equals(portName)) {
            setNavigationServicePortEndpointAddress(address);
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
