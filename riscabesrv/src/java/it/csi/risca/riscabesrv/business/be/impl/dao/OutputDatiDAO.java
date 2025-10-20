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

import java.math.BigDecimal;
import java.util.List;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.OutputDatiDTO;
import it.csi.risca.riscabesrv.dto.PagopaPosizioniDebitorieDTO;

/**
 * The interface OutputDati dao.
 *
 * @author CSI PIEMONTE
 */
public interface OutputDatiDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Save Output Dati
	 *
	 * @param dto OutputDatiDTO
	 * @return OutputDatiDTO
	 */
	OutputDatiDTO saveOutputDati(OutputDatiDTO dto) throws DAOException;
	
	Integer updateXlsSollImportoDaVersareByIuv(Long idElabora, String iuv, Long idOutputFoglio, BigDecimal importoDaVersare) throws DAOException;
	Integer updateTxtSollImportoDaVersareByIuv(Long idElabora, String iuv, Long idOutputFoglio, String valoreColonna1) throws DAOException;

	List<OutputDatiDTO> loadOutputDatiByFoglio(Long idElabora, Long idOutputFoglio, String codTipoElabora);

	Integer updateOutputDatiNomiLotto(Long idElabora, String codSettore, String codVersamento, String dataLotto,
			Integer recordPerLotto, String tipoElab) throws DAOException;

	List<String> loadNomiLotto(Long idElabora, Long idOutputFoglio);

	List<PagopaPosizioniDebitorieDTO> loadPosizioniDebitorieByNomeLotto(String nomeLotto, String causale,
			boolean bollettazioneSpeciale);

	Integer updateOutputDatiIuvCodiceAvviso(Long idElabora, String codTipoElabora) throws DAOException;

	List<OutputDatiDTO> loadOutputDatiTitolare(Long idElabora);

	Integer updateOutputDatiPecMail(Long idElabora, Integer progressivo, Long idOutputFoglio) throws DAOException;

	Integer updateOutputDatiIdAccertamento(Long idElabora, Integer progressivo, Long idOutputFoglio, Long idAccertamento, boolean isFoglioOds);
	
	Integer updateOutputDatiCodiceAvviso(Long idElabora, Long idOutputFoglio, String idAccertamento, String codiceAvviso, boolean isFoglioOds);

	Integer loadProgressivoTitolareTxt(Long idElabora, Long idOutputFoglio, String soggettoGruppo);
	
	List<OutputDatiDTO> loadOutputDatiByTipoRecord(Long idElabora, Long idOutputFoglio, String soggettoGruppo, String tipoRecord);
	
	Integer updateOutputDatiProgressivo(OutputDatiDTO dto, Integer progressivo);
	
}
