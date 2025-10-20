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
 * RelationshipsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public class RelationshipsServiceLocator extends org.apache.axis.client.Service implements it.csi.risca.riscabesrv.util.actarelationships.RelationshipsService {

    public RelationshipsServiceLocator() {
    }


    public RelationshipsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RelationshipsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RelationshipsServicePort
    private java.lang.String RelationshipsServicePort_address = "http://tst-applogic.reteunitaria.piemonte.it/actasrv/relationshipsWS";

    public java.lang.String getRelationshipsServicePortAddress() {
        return RelationshipsServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RelationshipsServicePortWSDDServiceName = "RelationshipsServicePort";

    public java.lang.String getRelationshipsServicePortWSDDServiceName() {
        return RelationshipsServicePortWSDDServiceName;
    }

    public void setRelationshipsServicePortWSDDServiceName(java.lang.String name) {
        RelationshipsServicePortWSDDServiceName = name;
    }

    public it.csi.risca.riscabesrv.util.actarelationships.RelationshipsServicePort_PortType getRelationshipsServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RelationshipsServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRelationshipsServicePort(endpoint);
    }

    public it.csi.risca.riscabesrv.util.actarelationships.RelationshipsServicePort_PortType getRelationshipsServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.csi.risca.riscabesrv.util.actarelationships.RelationshipsServiceBindingStub _stub = new it.csi.risca.riscabesrv.util.actarelationships.RelationshipsServiceBindingStub(portAddress, this);
            _stub.setPortName(getRelationshipsServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRelationshipsServicePortEndpointAddress(java.lang.String address) {
        RelationshipsServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.csi.risca.riscabesrv.util.actarelationships.RelationshipsServicePort_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                it.csi.risca.riscabesrv.util.actarelationships.RelationshipsServiceBindingStub _stub = new it.csi.risca.riscabesrv.util.actarelationships.RelationshipsServiceBindingStub(new java.net.URL(RelationshipsServicePort_address), this);
                _stub.setPortName(getRelationshipsServicePortWSDDServiceName());
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
        if ("RelationshipsServicePort".equals(inputPortName)) {
            return getRelationshipsServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("relationshipsservice.acaris.acta.doqui.it", "RelationshipsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("relationshipsservice.acaris.acta.doqui.it", "RelationshipsServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RelationshipsServicePort".equals(portName)) {
            setRelationshipsServicePortEndpointAddress(address);
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
