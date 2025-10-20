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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RpPagopaDAO;
import it.csi.risca.riscabesrv.dto.RpPagopaDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type PagopaListaCarIuv dao.
 *
 * @author CSI PIEMONTE
 */
public class RpPagopaDAOImpl extends RiscaBeSrvGenericDAO<RpPagopaDTO> implements RpPagopaDAO {

	private static final String QUERY_INSERT = "INSERT INTO risca_w_rp_pagopa "
			+ "(id_elabora, posiz_debitoria, anno, iuv, importo_pagato, data_scadenza, causale, data_esito_pagamento, cogn_rsoc_debitore, "
			+ "nome_debitore, cf_pi_debitore, cogn_rsoc_versante, nome_versante, cf_pi_versante, importo_transitato, importo_commissioni, "
			+ "codice_avviso, note) "
			+ "VALUES(:idElabora, :posizDebitoria, :anno, :iuv, :importoPagato, :dataScadenza, :causale, :dataEsitoPagamento, :cognRsocDebitore, "
			+ ":nomeDebitore, :cfPiDebitore, :cognRsocVersante, :nomeVersante, :cfPiVersante, :importoTransitato, :importoCommissioni, "
			+ ":codiceAvviso, :note) ";

	private static final String QUERY_LOAD = "select * from RISCA_W_RP_PAGOPA where id_elabora = :idElabora order by posiz_debitoria ";

	@Override
	public RpPagopaDTO saveRpPagopaDTO(RpPagopaDTO dto) throws DAOException {
		LOGGER.debug("[RpPagopaDAOImpl::saveRpPagopaDTO] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", dto.getIdElabora());
			map.put("posizDebitoria", dto.getPosizDebitoria());
			map.put("anno", dto.getAnno());
			map.put("iuv", dto.getIuv());
			map.put("importoPagato", dto.getImportoPagato());
			map.put("dataScadenza", dto.getDataScadenza());
			map.put("causale", dto.getCausale());
			map.put("dataEsitoPagamento", dto.getDataEsitoPagamento());
			map.put("cognRsocDebitore", dto.getCognRsocDebitore());
			map.put("nomeDebitore", dto.getNomeDebitore());
			map.put("cfPiDebitore", dto.getCfPiDebitore());
			map.put("cognRsocVersante", dto.getCognRsocVersante());
			map.put("nomeVersante", dto.getNomeVersante());
			map.put("cfPiVersante", dto.getCfPiVersante());
			map.put("importoTransitato", dto.getImportoTransitato());
			map.put("importoCommissioni", dto.getImportoCommissioni());
			map.put("codiceAvviso", dto.getCodiceAvviso());
			map.put("note", dto.getNote());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[RpPagopaDAOImpl::saveRpPagopaDTO] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RpPagopaDAOImpl::saveRpPagopaDTO] END");
		}

		return dto;
	}

	@Override
	public List<RpPagopaDTO> loadRpPagopaByElabora(Long idElabora) {
		LOGGER.debug("[RpPagopaDAOImpl::loadRpPagopaByElabora] BEGIN");
		List<RpPagopaDTO> listRpPagopa = new ArrayList<RpPagopaDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			MapSqlParameterSource params = getParameterValue(map);
			listRpPagopa = template.query(getQuery(QUERY_LOAD, null, null), params, new RpPagopaRowMapper());
		} catch (Exception e) {
			LOGGER.error("[RpPagopaDAOImpl::loadRpPagopaByElabora] Errore generale ", e);
			LOGGER.debug("[RpPagopaDAOImpl::loadRpPagopaByElabora] END");
			return listRpPagopa;
		}
		LOGGER.debug("[RpPagopaDAOImpl::loadRpPagopaByElabora] END");
		return listRpPagopa;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowMapper<RpPagopaDTO> getRowMapper() throws SQLException {
		return new RpPagopaRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<RpPagopaDTO> getExtendedRowMapper() throws SQLException {
		return new RpPagopaRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class RpPagopaRowMapper implements RowMapper<RpPagopaDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RpPagopaRowMapper() throws SQLException {
			// Instantiate class
		}

		/**
		 * Implementations must implement this method to map each row of data in the
		 * ResultSet. This method should not call {@code next()} on the ResultSet; it is
		 * only supposed to map values of the current row.
		 *
		 * @param rs     the ResultSet to map (pre-initialized for the current row)
		 * @param rowNum the number of the current row
		 * @return the result object for the current row (may be {@code null})
		 * @throws SQLException if a SQLException is encountered getting column values
		 *                      (that is, there's no need to catch SQLException)
		 */
		@Override
		public RpPagopaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RpPagopaDTO bean = new RpPagopaDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RpPagopaDTO bean) throws SQLException {
			bean.setIdElabora(rs.getLong("id_elabora"));
			bean.setPosizDebitoria(rs.getString("posiz_debitoria"));
			bean.setAnno(rs.getInt("anno"));
			bean.setIuv(rs.getString("iuv"));
			bean.setImportoPagato(rs.getBigDecimal("importo_pagato"));
			bean.setDataScadenza(rs.getDate("data_scadenza"));
			bean.setCausale(rs.getString("causale"));
			bean.setDataEsitoPagamento(rs.getDate("data_esito_pagamento"));
			bean.setCognRsocDebitore(rs.getString("cogn_rsoc_debitore"));
			bean.setNomeDebitore(rs.getString("nome_debitore"));
			bean.setCfPiDebitore(rs.getString("cf_pi_debitore"));
			bean.setCognRsocVersante(rs.getString("cogn_rsoc_versante"));
			bean.setNomeVersante(rs.getString("nome_versante"));
			bean.setCfPiVersante(rs.getString("cf_pi_versante"));
			bean.setImportoTransitato(rs.getBigDecimal("importo_transitato"));
			bean.setImportoCommissioni(rs.getBigDecimal("importo_commissioni"));
			bean.setCodiceAvviso(rs.getString("codice_avviso"));
			bean.setNote(rs.getString("note"));
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}