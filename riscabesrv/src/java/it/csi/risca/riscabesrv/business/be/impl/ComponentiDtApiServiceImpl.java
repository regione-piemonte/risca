/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;
import it.csi.risca.riscabesrv.business.be.ComponentiDtApi;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.ComponentiDtDAO;
import it.csi.risca.riscabesrv.dto.ComponenteDtExtendedDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Utils;

@Component
public class ComponentiDtApiServiceImpl extends BaseApiServiceImpl implements ComponentiDtApi{
	
	private static final String IDENTITY = "identity";

	@Autowired
    private ComponentiDtDAO componentiDtDAO;
	@Autowired
	private IrideServiceHelper serviceHelper;

	@Override
	public Response loadComponentiDt(Long idAmbito, String codTipoComponente, boolean attivo, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        LOGGER.debug("[ComponentiDtApiServiceImpl::loadComponentiDt] BEGIN");
        LOGGER.debug("[ComponentiDtApiServiceImpl::loadComponentiDt] Parametri in input idAmbito: [" + idAmbito + "] e codTipoComponente [" + codTipoComponente + "] e attivo ["+attivo+"]");
        List<ComponenteDtExtendedDTO> componenti;
		try {
			componenti = componentiDtDAO.loadComponentiDt(idAmbito, codTipoComponente, attivo);
		} catch (Exception e) {
            LOGGER.error("[ComponentiDtApiServiceImpl::loadComponentiDt] ERROR :", e);
            LOGGER.debug("[ComponentiDtApiServiceImpl::loadComponentiDt] END");
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			
		}
        LOGGER.debug("[ComponentiDtApiServiceImpl::loadComponentiDt] END");
        return Response.ok(componenti).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
}
