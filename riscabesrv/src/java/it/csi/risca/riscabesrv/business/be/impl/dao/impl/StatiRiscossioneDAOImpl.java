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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatiRiscossioneDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.StatiRiscossioneDTO;
import it.csi.risca.riscabesrv.dto.StatiRiscossioneExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoRiscossioneExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class StatiRiscossioneDAOImpl extends RiscaBeSrvGenericDAO<StatiRiscossioneDTO> implements StatiRiscossioneDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	private static final String ID_AMBITO = "idAmbito";
    
    private static final String QUERY_STATI_RISCOSSIONE = "SELECT rdsr.*, rdsr.id_ambito AS ambito_id, rda.* "
            + "FROM risca_d_stato_riscossione rdsr "
            + "INNER JOIN risca_d_ambito rda ON rdsr.id_ambito = rda.id_ambito ";
    
    private static final String QUERY_STATI_RISCOSSIONE_BY_ID_AMBITO = QUERY_STATI_RISCOSSIONE
            + "where rdsr.id_ambito = :idAmbito ";
    
	@Override
	public List<StatiRiscossioneExtendedDTO> loadStatiRiscossione() throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
			return findListByQuery(CLASSNAME, methodName, QUERY_STATI_RISCOSSIONE, null);
		} catch (Exception e) {
			throw new Exception(Constants.ERRORE_GENERICO);
		}
	}

	@Override
	public List<StatiRiscossioneExtendedDTO> loadStatiRiscossioneByIdAmbito(Long idAmbito) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
			Map<String, Object> map = new HashMap<>();
			map.put(ID_AMBITO, idAmbito);
			return findListByQuery(CLASSNAME, methodName, QUERY_STATI_RISCOSSIONE_BY_ID_AMBITO, map);
		} catch (Exception e) {
			throw new Exception(Constants.ERRORE_GENERICO);
		}
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<StatiRiscossioneDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new StatiRiscossioneRowMapper();
	}

	@Override
	public RowMapper<StatiRiscossioneExtendedDTO> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new StatiRiscossioneExtendedRowMapper();
	}
	
    /**
     * The type stati riscossione row mapper.
     */
    public static class StatiRiscossioneRowMapper implements RowMapper<StatiRiscossioneDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public StatiRiscossioneRowMapper() throws SQLException {
            // Instantiate class
        }

        /**
         * Implementations must implement this method to map each row of data
         * in the ResultSet. This method should not call {@code next()} on
         * the ResultSet; it is only supposed to map values of the current row.
         *
         * @param rs     the ResultSet to map (pre-initialized for the current row)
         * @param rowNum the number of the current row
         * @return the result object for the current row (may be {@code null})
         * @throws SQLException if a SQLException is encountered getting
         *                      column values (that is, there's no need to catch SQLException)
         */
        @Override
        public StatiRiscossioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            StatiRiscossioneDTO bean = new StatiRiscossioneDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, StatiRiscossioneDTO bean) throws SQLException {
            bean.setIdStatoRiscossione(rs.getLong("id_stato_riscossione"));
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setCodStatoRiscossione(rs.getString("cod_stato_riscossione"));
            bean.setDesStatoRiscossione(rs.getString("des_stato_riscossione"));
            bean.setFlgDefault(rs.getInt("flg_default"));
        }
    }
    
    /**
     * The type stati riscossione extended row mapper.
     */
    public static class StatiRiscossioneExtendedRowMapper implements RowMapper<StatiRiscossioneExtendedDTO> {

        /**
         * Instantiates a new Tipo adempimento extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public StatiRiscossioneExtendedRowMapper() throws SQLException {
            // Instantiate class
        }

        /**
         * Implementations must implement this method to map each row of data
         * in the ResultSet. This method should not call {@code next()} on
         * the ResultSet; it is only supposed to map values of the current row.
         *
         * @param rs     the ResultSet to map (pre-initialized for the current row)
         * @param rowNum the number of the current row
         * @return the result object for the current row (may be {@code null})
         * @throws SQLException if a SQLException is encountered getting
         *                      column values (that is, there's no need to catch SQLException)
         */
        @Override
        public StatiRiscossioneExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            StatiRiscossioneExtendedDTO bean = new StatiRiscossioneExtendedDTO();
            populateBeanExtended(rs, bean);
            return bean;
        }

        private void populateBeanExtended(ResultSet rs, StatiRiscossioneExtendedDTO bean) throws SQLException {
            bean.setIdStatoRiscossione(rs.getLong("id_stato_riscossione"));
            AmbitoDTO ambito = new AmbitoDTO();
            populateBeanAmbito(rs, ambito);
            bean.setAmbito(ambito);
            bean.setCodStatoRiscossione(rs.getString("cod_stato_riscossione"));
            bean.setDesStatoRiscossione(rs.getString("des_stato_riscossione"));
            bean.setFlgDefault(rs.getInt("flg_default"));

        }

        private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
            bean.setIdAmbito(rs.getLong("ambito_id"));
            bean.setCodAmbito(rs.getString("cod_ambito"));
            bean.setDesAmbito(rs.getString("des_ambito"));
        }
    }

}
