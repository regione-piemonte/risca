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
 * BackOfficeServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actabackoffice;

public class BackOfficeServiceLocator extends org.apache.axis.client.Service implements it.csi.risca.riscabesrv.util.actabackoffice.BackOfficeService {

    public BackOfficeServiceLocator() {
    }


    public BackOfficeServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public BackOfficeServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BackOfficeServicePort
    private java.lang.String BackOfficeServicePort_address = "http://tst-applogic.reteunitaria.piemonte.it/actasrv/backofficeWS";

    public java.lang.String getBackOfficeServicePortAddress() {
        return BackOfficeServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BackOfficeServicePortWSDDServiceName = "BackOfficeServicePort";

    public java.lang.String getBackOfficeServicePortWSDDServiceName() {
        return BackOfficeServicePortWSDDServiceName;
    }

    public void setBackOfficeServicePortWSDDServiceName(java.lang.String name) {
        BackOfficeServicePortWSDDServiceName = name;
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.BackOfficeServicePort_PortType getBackOfficeServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BackOfficeServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBackOfficeServicePort(endpoint);
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.BackOfficeServicePort_PortType getBackOfficeServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.csi.risca.riscabesrv.util.actabackoffice.BackOfficeServiceBindingStub _stub = new it.csi.risca.riscabesrv.util.actabackoffice.BackOfficeServiceBindingStub(portAddress, this);
            _stub.setPortName(getBackOfficeServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBackOfficeServicePortEndpointAddress(java.lang.String address) {
        BackOfficeServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.csi.risca.riscabesrv.util.actabackoffice.BackOfficeServicePort_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                it.csi.risca.riscabesrv.util.actabackoffice.BackOfficeServiceBindingStub _stub = new it.csi.risca.riscabesrv.util.actabackoffice.BackOfficeServiceBindingStub(new java.net.URL(BackOfficeServicePort_address), this);
                _stub.setPortName(getBackOfficeServicePortWSDDServiceName());
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
        if ("BackOfficeServicePort".equals(inputPortName)) {
            return getBackOfficeServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("backofficeservice.acaris.acta.doqui.it", "BackOfficeService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("backofficeservice.acaris.acta.doqui.it", "BackOfficeServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BackOfficeServicePort".equals(portName)) {
            setBackOfficeServicePortEndpointAddress(address);
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
