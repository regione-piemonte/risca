/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao;

import it.csi.risca.riscabesrv.util.actaobject.AcarisContentStreamType;
import it.csi.risca.riscabesrv.util.actaobject.AllegatiDTO;
import it.csi.risca.riscabesrv.util.actaobject.ClassificazioniDTO;

/**
 * The interface Documenti Allegati dao.
 *
 * @author CSI PIEMONTE
 */
public interface DocumentiAllegatiDAO {

    ClassificazioniDTO classificazioni(String idRiscossione);   

    AllegatiDTO allegati(String dbKeyClassificazione, String idRiscossione);

    AcarisContentStreamType  actaContentStream(String idClassificazione);

}
