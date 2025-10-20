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

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RpNonPremarcatiDAO;
import it.csi.risca.riscabesrv.dto.RpNonPremarcatiDTO;

/**
 * The type RpNonPremarcati dao.
 *
 * @author CSI PIEMONTE
 */
public class RpNonPremarcatiDAOImpl extends RiscaBeSrvGenericDAO<RpNonPremarcatiDTO> implements RpNonPremarcatiDAO {

	private static final String QUERY_INSERT = " INSERT INTO risca_w_rp_nonpremarcati "
			+ "(id_elabora, progr, identif_inizio_flusso, data_rif_dati, numero_conto, cuas, valuta, importo, "
			+ "data_acc, data_all, fraz_uff, tipo_doc, quinto_campo, numero_avviso, iden_imm, vcy, identif, "
			+ "num_vers, id_file_poste, id_immagine, flg_elaborato, prog_riga, flg_validita, file_immagine) "
			+ "VALUES(:idElabora, :progr, :identifInizioFlusso, :dataRifDati, :numeroConto, :cuas, :valuta, "
			+ ":importo, :dataAcc, :dataAll, :frazUff, :tipoDoc, :quintoCampo, :numeroAvviso, :idenImm, :vcy, :identif, "
			+ ":numVers, :idFilePoste, :idImmagine, :flgElaborato, :progRiga, :flgValidita, :fileImmagine)";

	private static final String QUERY_LOAD = " select * from risca_w_rp_nonpremarcati where id_elabora = :idElabora and id_file_poste = :idFilePoste and tipo_doc in (:tipoDoc) ";

	private static final String QUERY_DELETE_BY_ID_ELABORA = "delete from risca_w_rp_nonpremarcati where id_elabora = :idElabora ";
	
	@Override
	public RpNonPremarcatiDTO saveRpNonPremarcati(RpNonPremarcatiDTO dto) {
		LOGGER.debug("[RpNonPremarcatiDAOImpl::saveRpNonPremarcati] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", dto.getIdElabora());
			map.put("progr", dto.getProgr());
			map.put("identifInizioFlusso", dto.getIdentifInizioFlusso());
			map.put("dataRifDati", dto.getDataRifDati());
			map.put("numeroConto", dto.getNumeroConto());
			map.put("cuas", dto.getCuas());
			map.put("valuta", dto.getValuta());
			map.put("importo", dto.getImporto());
			map.put("dataAcc", dto.getDataAcc());
			map.put("dataAll", dto.getDataAll());
			map.put("frazUff", dto.getFrazUff());
			map.put("tipoDoc", dto.getTipoDoc());
			map.put("quintoCampo", dto.getQuintoCampo());
			map.put("numeroAvviso", dto.getNumeroAvviso());
			map.put("idenImm", dto.getIdenImm());
			map.put("vcy", dto.getVcy());
			map.put("identif", dto.getIdentif());
			map.put("numVers", dto.getNumVers());
			map.put("idFilePoste", dto.getIdFilePoste());
			map.put("idImmagine", dto.getIdImmagine());
			map.put("flgElaborato", dto.getFlgElaborato());
			map.put("progRiga", dto.getProgRiga());
			map.put("flgValidita", dto.getFlgValidita());
			map.put("fileImmagine", dto.getFileImmagine());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[RpNonPremarcatiDAOImpl::saveRpNonPremarcati] ERROR : " + e);
			return null;
		} finally {
			LOGGER.debug("[RpNonPremarcatiDAOImpl::saveRpNonPremarcati] END");
		}

		return dto;
	}

	@Override
	public List<RpNonPremarcatiDTO> loadRpNonPremarcatiByElaboraTipoDoc(Long idElabora, Long idFilePoste, List<String> tipoDoc) {
		LOGGER.debug("[RpNonPremarcatiDAOImpl::loadRpNonPremarcatiByElaboraTipoDoc] BEGIN");
		List<RpNonPremarcatiDTO> listRpNonPremarcati = new ArrayList<RpNonPremarcatiDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("idFilePoste", idFilePoste);
			map.put("tipoDoc", tipoDoc);
			MapSqlParameterSource params = getParameterValue(map);
			listRpNonPremarcati = template.query(getQuery(QUERY_LOAD, null, null), params,
					new RpNonPremarcatiRowMapper());
		} catch (Exception e) {
			LOGGER.error("[RpNonPremarcatiDAOImpl::loadRpNonPremarcatiByElaboraTipoDoc] Errore generale ", e);
			LOGGER.debug("[RpNonPremarcatiDAOImpl::loadRpNonPremarcatiByElaboraTipoDoc] END");
			return listRpNonPremarcati;
		}
		LOGGER.debug("[RpNonPremarcatiDAOImpl::loadRpRpNonPremarcatiByElaboraNumeroConto] END");
		return listRpNonPremarcati;
	}
	
	@Override
	public Integer deleteRpNonPremarcati(Long idElabora) {
		LOGGER.debug("[RpNonPremarcatiDAOImpl::deleteRpNonPremarcati] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_BY_ID_ELABORA, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[RpNonPremarcatiDAOImpl::deleteRpNonPremarcati] ERROR : " + e);
			return null;
		} finally {
			LOGGER.debug("[RpNonPremarcatiDAOImpl::deleteRpNonPremarcati] END");
		}

		return res;
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
	public RowMapper<RpNonPremarcatiDTO> getRowMapper() throws SQLException {
		return new RpNonPremarcatiRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<RpNonPremarcatiDTO> getExtendedRowMapper() throws SQLException {
		return new RpNonPremarcatiRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class RpNonPremarcatiRowMapper implements RowMapper<RpNonPremarcatiDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RpNonPremarcatiRowMapper() throws SQLException {
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
		public RpNonPremarcatiDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RpNonPremarcatiDTO bean = new RpNonPremarcatiDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RpNonPremarcatiDTO bean) throws SQLException {
			bean.setIdElabora(rs.getLong("id_elabora"));
			bean.setProgr(rs.getInt("progr"));
			bean.setIdentifInizioFlusso(rs.getString("identif_inizio_flusso"));
			bean.setDataRifDati(rs.getString("data_rif_dati"));
			bean.setNumeroConto(rs.getString("numero_conto"));
			bean.setCuas(rs.getString("cuas"));
			bean.setValuta(rs.getString("valuta"));
			bean.setImporto(rs.getBigDecimal("importo"));
			bean.setDataAcc(rs.getString("data_acc"));
			bean.setDataAll(rs.getString("data_all"));
			bean.setFrazUff(rs.getString("fraz_uff"));
			bean.setTipoDoc(rs.getString("tipo_doc"));
			bean.setQuintoCampo(rs.getString("quinto_campo"));
			bean.setNumeroAvviso(rs.getString("numero_avviso"));
			bean.setIdenImm(rs.getString("iden_imm"));
			bean.setVcy(rs.getString("vcy"));
			bean.setIdentif(rs.getString("identif"));
			bean.setNumVers(rs.getLong("num_vers"));
			bean.setIdFilePoste(rs.getLong("id_file_poste"));
			bean.setIdImmagine(rs.getLong("id_immagine"));
			bean.setFlgElaborato(rs.getInt("flg_elaborato"));
			bean.setProgRiga(rs.getInt("prog_riga"));
			bean.setFlgValidita(rs.getString("flg_validita"));
			bean.setFileImmagine(rs.getString("file_immagine"));
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}