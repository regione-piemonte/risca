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
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiAccertamentoDAO;
import it.csi.risca.riscabesrv.dto.TipiAccertamentoDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipo Titolo dao.
 *
 * @author CSI PIEMONTE
 */
public class TipiAccertamentoDAOImpl extends RiscaBeSrvGenericDAO<TipiAccertamentoDTO> implements TipiAccertamentoDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();

    private static final String QUERY_TIPI_ACCERTAMENTO = "SELECT rdta.* "
            + "FROM risca_d_tipo_accertamento rdta ";
    
	@Override
	public List<TipiAccertamentoDTO> loadTipiAccertamento() throws Exception {
		LOGGER.debug("[TipiAccertamentoDAOImpl::loadTipiAccertamento] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug("[TipiAccertamentoDAOImpl::loadTipiAccertamento] BEGIN");
        try {
			return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_ACCERTAMENTO, null);
		} catch (Exception e) {
            LOGGER.error("[TipiAccertamentoDAOImpl::loadTipiAccertamento] Errore nell'esecuzione della query", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		}
	}


	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<TipiAccertamentoDTO> getRowMapper() throws SQLException {
		 return new TipiAccertamentoRowMapper();
	}

	@Override
	public RowMapper<TipiAccertamentoDTO> getExtendedRowMapper() throws SQLException {
		return new TipiAccertamentoRowMapper();
	}

    /**
     * The type Tipi Accertamento row mapper.
     */
    public static class TipiAccertamentoRowMapper implements RowMapper<TipiAccertamentoDTO> {

        /**
         * Instantiates a new Tipo Accertamento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipiAccertamentoRowMapper() throws SQLException {
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
        public TipiAccertamentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiAccertamentoDTO bean = new TipiAccertamentoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiAccertamentoDTO bean) throws SQLException {
            bean.setIdTipoAccertamento(rs.getLong("id_tipo_accertamento"));
            bean.setCodTipoAccertamento(rs.getString("cod_tipo_accertamento"));
            bean.setDesTipoAccertamento(rs.getString("des_tipo_accertamento"));
            bean.setFlgAutomatico(rs.getInt("flg_automatico"));
            bean.setFlgManuale(rs.getInt("flg_manuale"));
        }
    }
}
