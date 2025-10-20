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

import java.sql.SQLException;
import java.util.List;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.dto.RiscossioneDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchResultDTO;
import it.csi.risca.riscabesrv.dto.VerifyRiscossioneStatoDebDTO;

/**
 * The interface Riscossione dao.
 *
 * @author CSI PIEMONTE
 */
public interface RiscossioneDAO extends BaseRiscaBeSrvDAO {

    /**
     * Save riscossione long.
     *
     * @param dto RiscossioneDTO
     * @return id_riscossione long
     */
    Long saveRiscossione(RiscossioneDTO dto,String fruitore, Long idAmbito) throws BusinessException, Exception;
    
    /**
     * Update riscossione Long.
     *
     * @param dto RiscossioneDTO
     * @return numero record aggiornati
     * @throws Exception 
     */
    Long updateRiscossione(RiscossioneDTO dto, Long idAmbito) throws BusinessException, Exception;
    
    /**
     * Load riscossione by Id or code.
     *
     * @param codRiscossione codRiscossione
     * @return oggetto riscossione
     * @throws SQLException 
     */
    RiscossioneDTO getRiscossione(String codRiscossione) throws SQLException;

    /**
     * Check riscossione recapito.
     *
     * @param idRiscossione idRiscossione
     * @return intero
     */
	Integer checkRecapitoRiscossione(String idRiscossione);
    
    /**
     * Load riscossioni by Id or code gruppo.
     *
     * @param codGruppo codGruppo
     * @return List<RiscossioneDTO>
     * @throws SQLException 
     */
    List<RiscossioneDTO> getRiscossioniGruppo(String codGruppo) throws SQLException;
    

    /**
     * Load riscossioni by RiscossioneSearchDTO.
     *
     * @param RiscossioneSearchDTO dto
     * @return RiscossioneSearchResultDTO
     * @throws Exception 
     */
    List<RiscossioneSearchResultDTO> searchRiscossioni(RiscossioneSearchDTO dto, Long idAmbito,String modalitaRicerca , Integer offset,  Integer limit, String sort) throws Exception;

    /**
     * Load dato tecnico.
     *
     * @param idRiscossione idRiscossione
     * @return oggetto dato tecnico
     * @throws Exception 
     */
    String loadDatoTecnico(Long idRiscossione) throws Exception;
    
    /**
     * Load verify Riscossioni ST Debitori.
     *@param idAmbito idAmbito 
     *@param idOggetto idOggetto
     *@param indTipoOggetto indTipoOggetto
     *@param idTipoOper idTipoOper
     * @param idRiscossione idRiscossione
     * @return verifyRiscossioniSTDebitori
     * @throws SQLException 
     */
    VerifyRiscossioneStatoDebDTO verifyRiscossioniSTDebitori(Long idAmbito, Long idOggetto, String indTipoOggetto, String idTipoOper, Long idRiscossione) throws SQLException;
    
    /**
     * Load cod riscossione by idStatoDebitorio.
     *
     * @param idStatoDebitorio idStatoDebitorio
     * @return List<String>
     * @throws SQLException 
     */
    List<String> getCodiciUtenzaByIdStatoDebitorio(Long idStatoDebitorio) throws SQLException;


    /**
     * count riscossioni by RiscossioneSearchDTO.
     *
     * @param RiscossioneSearchDTO dto
     * @return RiscossioneSearchResultDTO
     * @throws Exception 
     */
    Integer countRiscossioni(RiscossioneSearchDTO dto, Long idAmbito,String modalitaRicerca ) throws Exception;


    /**
     * searchRiscossioniDelegati.
     *
     * @param  RiscossioneSearchDTO dto
     * @param  idAmbito idAmbito
     * @param  modalitaRicerca modalitaRicerca
     * @return RiscossioneSearchResultDTO
     * @throws Exception 
     */
    List<RiscossioneSearchResultDTO> searchRiscossioniDelegati(RiscossioneSearchDTO dto, Long idAmbito,String modalitaRicerca , Integer offset,  Integer limit, String sort) throws Exception;

    /**
     * countRiscossioniDelegati.
     *
     * @param  RiscossioneSearchDTO dto
     * @param  idAmbito idAmbito
     * @param  modalitaRicerca modalitaRicerca
     * @return RiscossioneSearchResultDTO
     * @throws Exception 
     */
	Integer countRiscossioniDelegati(RiscossioneSearchDTO riscossioneSearch, Long idAmbito, String modalitaRicerca) throws Exception;

	Long updateRiscossioneStatoPratica(RiscossioneDTO riscossione, Long idAmbito)throws BusinessException, Exception;

	Long getIdSoggettoRiscossione(Long idRiscossione) throws SQLException;
}
