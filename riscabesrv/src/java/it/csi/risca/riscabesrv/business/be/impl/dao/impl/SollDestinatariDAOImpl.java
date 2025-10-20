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
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDestinatariDAO;
import it.csi.risca.riscabesrv.dto.SollDestinatariDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type SollDatiAmministr dao.
 *
 * @author CSI PIEMONTE
 */
public class SollDestinatariDAOImpl extends RiscaBeSrvGenericDAO<SollDestinatariDTO> implements SollDestinatariDAO {

	public static final String QUERY_INSERT_W = " INSERT INTO risca_w_soll_destinatari "
			+ "(id_soll_destinatari, id_accertamento, codice_utenza, scadenza_pagamento, "
			+ "codice_utente_tit, codice_utente_dest, rag_soc_cogn, codice_fiscale_calc, "
			+ "codice_fiscale_eti_calc, presso_ind_post, indirizzo_ind_post, comune_ind_post, "
			+ "prov_ind_post, num_prot, data_prot, scadenza_soll, rag_soc_cogn_ru, nome_ru, "
			+ "sesso_ru, data_nascita_ru, luogo_nascita_ru, prov_nascita_ru, cap_ru, comune_ru, "
			+ "prov_ru, indirizzo_civ_ru) "
			+ "VALUES(:idSollDestinatari, :idAccertamento, :codiceUtenza, :scadenzaPagamento, "
			+ ":codiceUtenteTit, :codiceUtenteDest, :ragSocCogn, :codiceFiscaleCalc, "
			+ ":codiceFiscaleEtiCalc, :pressoIndPost, :indirizzoIndPost, :comuneIndPost, "
			+ ":provIndPost, :numProt, :dataProt, :scadenzaSoll, :ragSocCognRu, :nomeRu, "
			+ ":sessoRu, :dataNascitaRu, :luogoNascitaRu, :provNascitaRu, :capRu, :comuneRu, "
			+ ":provRu, :indirizzoCivRu) ";

	@Override
	public SollDestinatariDTO saveSollDestinatariWorking(SollDestinatariDTO dto) throws DAOException {
		LOGGER.debug("[SollDestinatariDAOImpl::saveSollDestinatariWorking] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Long genId = findNextSequenceValue("seq_risca_r_soll_destinatari");

			map.put("idSollDestinatari", genId);
			map.put("idAccertamento", dto.getIdAccertamento());
			map.put("codiceUtenza", dto.getCodiceUtenza());
			map.put("scadenzaPagamento", dto.getScadenzaPagamento());
			map.put("codiceUtenteTit", dto.getCodiceUtenteTit());
			map.put("codiceUtenteDest", dto.getCodiceUtenteDest());
			map.put("ragSocCogn", dto.getRagSocCogn());
			map.put("codiceFiscaleCalc", dto.getCodiceFiscaleCalc());
			map.put("codiceFiscaleEtiCalc", dto.getCodiceFiscaleEtiCalc());
			map.put("pressoIndPost", dto.getPressoIndPost());
			map.put("indirizzoIndPost", dto.getIndirizzoIndPost());
			map.put("comuneIndPost", dto.getComuneIndPost());
			map.put("provIndPost", dto.getProvIndPost());
			map.put("numProt", dto.getNumProt());
			map.put("dataProt", dto.getDataProt());
			map.put("scadenzaSoll", dto.getScadenzaSoll());
			map.put("ragSocCognRu", dto.getRagSocCognRu());
			map.put("nomeRu", dto.getNomeRu());
			map.put("sessoRu", dto.getSessoRu());
			map.put("dataNascitaRu", dto.getDataNascitaRu());
			map.put("luogoNascitaRu", dto.getLuogoNascitaRu());
			map.put("provNascitaRu", dto.getProvNascitaRu());
			map.put("capRu", dto.getCapRu());
			map.put("comuneRu", dto.getComuneRu());
			map.put("provRu", dto.getProvRu());
			map.put("indirizzoCivRu", dto.getIndirizzoCivRu());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_W, null, null), params, keyHolder);
			dto.setIdSollDestinatari(genId);

			return dto;
		} catch (Exception e) {
			LOGGER.error("[SollDestinatariDAOImpl::saveSollDestinatariWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDestinatariDAOImpl::saveSollDestinatariWorking] END");
		}
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
	public RowMapper<SollDestinatariDTO> getRowMapper() throws SQLException {
		return new SollDestinatariRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<SollDestinatariDTO> getExtendedRowMapper() throws SQLException {
		return new SollDestinatariRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class SollDestinatariRowMapper implements RowMapper<SollDestinatariDTO> {

		/**
		 * Instantiates a new AvvisoDatiTitolare row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SollDestinatariRowMapper() throws SQLException {
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
		public SollDestinatariDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SollDestinatariDTO bean = new SollDestinatariDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SollDestinatariDTO bean) throws SQLException {
			bean.setIdSollDestinatari(getLong(rs, "id_soll_destinatari"));
			bean.setIdAccertamento(getLong(rs, "id_accertamento"));
			bean.setCodiceUtenza(rs.getString("codice_utenza"));
			bean.setScadenzaPagamento(rs.getString("scadenza_pagamento"));
			bean.setCodiceUtenteTit(rs.getString("codice_utente_tit"));
			bean.setCodiceUtenteDest(rs.getString("codice_utente_dest"));
			bean.setRagSocCogn(rs.getString("rag_soc_cogn"));
			bean.setCodiceFiscaleCalc(rs.getString("codice_fiscale_calc"));
			bean.setCodiceFiscaleEtiCalc(rs.getString("codice_fiscale_eti_calc"));
			bean.setPressoIndPost(rs.getString("presso_ind_post"));
			bean.setIndirizzoIndPost(rs.getString("indirizzo_ind_post"));
			bean.setComuneIndPost(rs.getString("comune_ind_post"));
			bean.setProvIndPost(rs.getString("prov_ind_post"));
			bean.setNumProt(rs.getString("num_prot"));
			bean.setDataProt(rs.getDate("data_prot"));
			bean.setScadenzaSoll(rs.getString("scadenza_soll"));
			bean.setRagSocCognRu(rs.getString("rag_soc_cogn_ru"));
			bean.setNomeRu(rs.getString("nome_ru"));
			bean.setSessoRu(rs.getString("sesso_ru"));
			bean.setDataNascitaRu(rs.getDate("data_nascita_ru"));
			bean.setLuogoNascitaRu(rs.getString("luogo_nascita_ru"));
			bean.setProvNascitaRu(rs.getString("prov_nascita_ru"));
			bean.setCapRu(rs.getString("cap_ru"));
			bean.setComuneRu(rs.getString("comune_ru"));
			bean.setProvRu(rs.getString("prov_ru"));
			bean.setIndirizzoCivRu(rs.getString("indirizzo_civ_ru"));
		}

		private boolean rsHasColumn(ResultSet rs, String column) {
			try {
				rs.findColumn(column);
				return true;
			} catch (SQLException sqlex) {
				// Column not present in resultset
			}
			return false;
		}

		private Long getLong(ResultSet rs, String strColName) throws SQLException {
			long nValue = rs.getLong(strColName);
			return rs.wasNull() ? null : nValue;
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}