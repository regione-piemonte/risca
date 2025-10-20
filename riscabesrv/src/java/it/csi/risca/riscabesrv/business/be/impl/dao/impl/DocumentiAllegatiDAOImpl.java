/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl;

import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.DocumentiAllegatiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.RiscossioneDTO;
import it.csi.risca.riscabesrv.util.actaobject.AcarisContentStreamType;
import it.csi.risca.riscabesrv.util.actaobject.AllegatiDTO;
import it.csi.risca.riscabesrv.util.actaobject.ClassificazioniDTO;

/**
 * The type Documenti Allegati dao.
 *
 * @author CSI PIEMONTE
 */
@Service
public class DocumentiAllegatiDAOImpl extends RiscaBeSrvGenericDAO<RiscossioneDTO> implements DocumentiAllegatiDAO {

	@Override
	public ClassificazioniDTO classificazioni(String idRiscossione) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AllegatiDTO allegati(String dbKeyClassificazione, String idRiscossione) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AcarisContentStreamType actaContentStream(String idClassificazione) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<RiscossioneDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


}
