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
 * ObjectServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class ObjectServiceLocator extends org.apache.axis.client.Service implements it.csi.risca.riscabesrv.util.actaobject.ObjectService {

    public ObjectServiceLocator() {
    }


    public ObjectServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ObjectServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ObjectServicePort
    private java.lang.String ObjectServicePort_address = "http://tst-applogic.reteunitaria.piemonte.it/actasrv/objectWS";

    public java.lang.String getObjectServicePortAddress() {
        return ObjectServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ObjectServicePortWSDDServiceName = "ObjectServicePort";

    public java.lang.String getObjectServicePortWSDDServiceName() {
        return ObjectServicePortWSDDServiceName;
    }

    public void setObjectServicePortWSDDServiceName(java.lang.String name) {
        ObjectServicePortWSDDServiceName = name;
    }

    public it.csi.risca.riscabesrv.util.actaobject.ObjectServicePort_PortType getObjectServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ObjectServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getObjectServicePort(endpoint);
    }

    public it.csi.risca.riscabesrv.util.actaobject.ObjectServicePort_PortType getObjectServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.csi.risca.riscabesrv.util.actaobject.ObjectServiceBindingStub _stub = new it.csi.risca.riscabesrv.util.actaobject.ObjectServiceBindingStub(portAddress, this);
            _stub.setPortName(getObjectServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setObjectServicePortEndpointAddress(java.lang.String address) {
        ObjectServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.csi.risca.riscabesrv.util.actaobject.ObjectServicePort_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                it.csi.risca.riscabesrv.util.actaobject.ObjectServiceBindingStub _stub = new it.csi.risca.riscabesrv.util.actaobject.ObjectServiceBindingStub(new java.net.URL(ObjectServicePort_address), this);
                _stub.setPortName(getObjectServicePortWSDDServiceName());
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
        if ("ObjectServicePort".equals(inputPortName)) {
            return getObjectServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("objectservice.acaris.acta.doqui.it", "ObjectService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("objectservice.acaris.acta.doqui.it", "ObjectServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ObjectServicePort".equals(portName)) {
            setObjectServicePortEndpointAddress(address);
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
