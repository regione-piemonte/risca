/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.acta.backoffice;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import it.csi.risca.riscabesrv.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscabesrv.util.actabackoffice.BackOfficeServiceLocator;
import it.csi.risca.riscabesrv.util.actabackoffice.BackOfficeServicePort_PortType;
import it.csi.risca.riscabesrv.util.actabackoffice.ClientApplicationInfo;
import it.csi.risca.riscabesrv.util.actabackoffice.CodiceFiscaleType;
import it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType;
import it.csi.risca.riscabesrv.util.actabackoffice.IdNodoType;
import it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType;
import it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType;
import it.csi.risca.riscabesrv.util.actabackoffice.PrincipalExtResponseType;

public class ActaBackOfficeServiceHelper  extends AbstractServiceHelper {
	private BackOfficeServicePort_PortType service;
    /**
     * The Url service.
     */
    protected String urlService = "";

    /**
     * Instantiates a new Acta BackOffice service helper.
     *
     * @param urlService urlService
     */
    public ActaBackOfficeServiceHelper(String urlService) {
        this.urlService = urlService;
        this.service = this.getService(urlService);
    }

    private BackOfficeServicePort_PortType getService(String urlService) {
        LOGGER.debug("[ActaBackOfficeServiceHelper::getService] BEGIN");
        BackOfficeServicePort_PortType server  = null;
        try {
            BackOfficeServiceLocator locator = new BackOfficeServiceLocator();
            server = locator.getBackOfficeServicePort(new URL(urlService));
            

            LOGGER.debug("[ActaBackOfficeServiceHelper::getService] Service 'JavaServiceDesc' INITIALIZED");
            LOGGER.debug("[ActaBackOfficeServiceHelper::getService] END");
        } catch (MalformedURLException | ServiceException e) {
            LOGGER.error("[ActaBackOfficeServiceHelper::getService] ERROR : invalid url [" + urlService + "]");
        }
        return server;
    }

    /**
     * Gets principalId.
     *
     * @return AcarisRepositoryEntryType[] repositories
     */
    public PrincipalExtResponseType[] getPrincipalExt(ObjectIdType repositoryId, CodiceFiscaleType idUtente, IdAOOType idAOO, IdStrutturaType idStruttura, IdNodoType idNodo, ClientApplicationInfo clientApplicationInfo) {
        LOGGER.debug("[ActaBackOfficeServiceHelper::getPrincipalExt] BEGIN");
        PrincipalExtResponseType[] principalIds = null;
        try {
            LOGGER.debug("[ActaBackOfficeServiceHelper::getPrincipalExt] repositoryId :"+repositoryId.getValue());
            LOGGER.debug("[ActaBackOfficeServiceHelper::getPrincipalExt] idUtente: "+idUtente.getValue());
            LOGGER.debug("[ActaBackOfficeServiceHelper::getPrincipalExt] idAOO: "+idAOO);
            LOGGER.debug("[ActaBackOfficeServiceHelper::getPrincipalExt] idStruttura: "+idStruttura);
            LOGGER.debug("[ActaBackOfficeServiceHelper::getPrincipalExt] idNodo: "+idNodo);
            LOGGER.debug("[ActaBackOfficeServiceHelper::getPrincipalExt] clientApplicationInfo: "+clientApplicationInfo.getAppKey());
        	principalIds = this.service.getPrincipalExt(repositoryId, idUtente, idAOO, idStruttura, idNodo, clientApplicationInfo);
            return principalIds;
        } catch (Exception e) {
            LOGGER.error("[ActaBackOfficeServiceHelper::getPrincipalExt] ERROR ", e);
        }
        LOGGER.debug("[ActaBackOfficeServiceHelper::getPrincipalExt] END");
        return principalIds;
    }
}
