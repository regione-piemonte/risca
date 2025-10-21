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
 * PolicyEnforcerBaseService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscaboweb.util.service.integration.iride;

import it.csi.iride2.iridefed.entity.Ruolo;
import it.csi.iride2.policy.entity.Actor;
import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;

public interface PolicyEnforcerBaseService extends java.rmi.Remote {
    public Identita identificaUserPassword(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, UnrecoverableException, SystemException, MalformedUsernameException, AuthException, IdProviderNotFoundException, InternalException;
    public Identita identificaCertificato(byte[] in0) throws java.rmi.RemoteException, UnrecoverableException, CertRevokedException, CertOutsideValidityException, SystemException, IdProviderNotFoundException, InternalException;
    public Identita identificaUserPasswordPIN(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, UnrecoverableException, SystemException, MalformedUsernameException, AuthException, IdProviderNotFoundException, InternalException;
    public boolean isPersonaAutorizzataInUseCase(Identita in0, UseCase in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchUseCaseException, NoSuchApplicationException, InternalException;
    public UseCase[] findUseCasesForPersonaInApplication(Identita in0, Application in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchApplicationException, InternalException;
    public boolean isIdentitaAutentica(Identita in0) throws java.rmi.RemoteException, UnrecoverableException, SystemException, InternalException;
    public java.lang.String getInfoPersonaInUseCase(Identita in0, UseCase in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchUseCaseException, NoSuchApplicationException, InternalException;
    public Ruolo[] findRuoliForPersonaInUseCase(Identita in0, UseCase in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchUseCaseException, NoSuchApplicationException, InternalException;
    public Ruolo[] findRuoliForPersonaInApplication(Identita in0, Application in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchApplicationException, InternalException;
    public java.lang.String getInfoPersonaSchema(Ruolo in0) throws java.rmi.RemoteException, UnrecoverableException, SystemException, BadRuoloException, InternalException;
    public Actor[] findActorsForPersonaInApplication(Identita in0, Application in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchApplicationException, InternalException;
    public Actor[] findActorsForPersonaInUseCase(Identita in0, UseCase in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, NoSuchUseCaseException, NoSuchApplicationException, InternalException;
    public boolean isPersonaInRuolo(Identita in0, Ruolo in1) throws java.rmi.RemoteException, UnrecoverableException, IdentitaNonAutenticaException, SystemException, BadRuoloException, InternalException;
}
