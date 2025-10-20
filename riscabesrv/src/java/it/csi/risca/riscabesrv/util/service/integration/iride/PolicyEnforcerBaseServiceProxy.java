/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.util.service.integration.iride;

import it.csi.iride2.iridefed.entity.Ruolo;
import it.csi.iride2.policy.entity.Actor;
import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;

public class PolicyEnforcerBaseServiceProxy implements PolicyEnforcerBaseService {
  private String _endpoint = null;
  private PolicyEnforcerBaseService policyEnforcerBaseService = null;
  
  public PolicyEnforcerBaseServiceProxy() {
    _initPolicyEnforcerBaseServiceProxy();
  }
  
  public PolicyEnforcerBaseServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initPolicyEnforcerBaseServiceProxy();
  }
  
  private void _initPolicyEnforcerBaseServiceProxy() {
    try {
      policyEnforcerBaseService = (new PolicyEnforcerBaseServiceServiceLocator()).getPolicyEnforcerBase();
      if (policyEnforcerBaseService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)policyEnforcerBaseService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)policyEnforcerBaseService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (policyEnforcerBaseService != null)
      ((javax.xml.rpc.Stub)policyEnforcerBaseService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public PolicyEnforcerBaseService getPolicyEnforcerBaseService() {
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService;
  }
  
  public Identita identificaUserPassword(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, UnrecoverableException, SystemException, MalformedUsernameException, AuthException, IdProviderNotFoundException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.identificaUserPassword(in0, in1);
  }
  
  public Identita identificaCertificato(byte[] in0) throws java.rmi.RemoteException, UnrecoverableException, CertRevokedException, CertOutsideValidityException, SystemException, IdProviderNotFoundException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.identificaCertificato(in0);
  }
  
  public Identita identificaUserPasswordPIN(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, UnrecoverableException, SystemException, MalformedUsernameException, AuthException, IdProviderNotFoundException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.identificaUserPasswordPIN(in0, in1, in2);
  }
  
  public boolean isPersonaAutorizzataInUseCase(Identita in0, UseCase in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchUseCaseException, NoSuchApplicationException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.isPersonaAutorizzataInUseCase(in0, in1);
  }
  
  public UseCase[] findUseCasesForPersonaInApplication(Identita in0, Application in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchApplicationException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.findUseCasesForPersonaInApplication(in0, in1);
  }
  
  public boolean isIdentitaAutentica(Identita in0) throws java.rmi.RemoteException, UnrecoverableException, SystemException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.isIdentitaAutentica(in0);
  }
  
  public java.lang.String getInfoPersonaInUseCase(Identita in0, UseCase in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchUseCaseException, NoSuchApplicationException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.getInfoPersonaInUseCase(in0, in1);
  }
  
  public Ruolo[] findRuoliForPersonaInUseCase(Identita in0, UseCase in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchUseCaseException, NoSuchApplicationException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.findRuoliForPersonaInUseCase(in0, in1);
  }
  
  public Ruolo[] findRuoliForPersonaInApplication(Identita in0, Application in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchApplicationException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.findRuoliForPersonaInApplication(in0, in1);
  }
  
  public java.lang.String getInfoPersonaSchema(Ruolo in0) throws java.rmi.RemoteException, UnrecoverableException, SystemException, BadRuoloException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.getInfoPersonaSchema(in0);
  }
  
  public Actor[] findActorsForPersonaInApplication(Identita in0, Application in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchApplicationException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.findActorsForPersonaInApplication(in0, in1);
  }
  
  public Actor[] findActorsForPersonaInUseCase(Identita in0, UseCase in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchUseCaseException, NoSuchApplicationException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.findActorsForPersonaInUseCase(in0, in1);
  }
  
  public boolean isPersonaInRuolo(Identita in0, Ruolo in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, BadRuoloException, InternalException{
    if (policyEnforcerBaseService == null)
      _initPolicyEnforcerBaseServiceProxy();
    return policyEnforcerBaseService.isPersonaInRuolo(in0, in1);
  }
  
  
}