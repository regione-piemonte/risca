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
import it.csi.risca.riscabesrv.business.be.impl.dao.RpEstrcoDAO;
import it.csi.risca.riscabesrv.dto.RpEstrcoDTO;

/**
 * The type RpEstrco dao.
 *
 * @author CSI PIEMONTE
 */
public class RpEstrcoDAOImpl extends RiscaBeSrvGenericDAO<RpEstrcoDTO> implements RpEstrcoDAO {

	private static final String QUERY_INSERT = " INSERT INTO risca_w_rp_estrco "
			+ " (id_elabora, numero_progressivo, tipo_record, mittente, ricevente, data_creazione, nome_supporto, centro_applicativo, "
			+ " numero_rendicontazioni, numero_record, giornata_applicativa, codice_abi, causale, descrizione, tipo_conto, "
			+ " coordinate_bancarie, codice_divisa, data_contabile, segno, saldo_iniziale_quad, data_valuta, data_registrazione, "
			+ " segno_movimento, importo_movimento, causale_abi, causale_interna, numero_assegno, riferimento_banca, riferimento_cliente, "
			+ " descrizione_movimento, cin, abi, cab, numero_cc, id_file_poste, progressivo_movimento, prog_riga, flg_validita) "
			+ " VALUES(:idElabora, :numeroProgressivo, :tipoRecord, :mittente, :ricevente, :dataCreazione, :nomeSupporto, :centroApplicativo, "
			+ " :numeroRendicontazioni, :numeroRecord, :giornataApplicativa, :codiceAbi, :causale, :descrizione, :tipoConto, "
			+ " :coordinateBancarie, :codiceDivisa, :dataContabile, :segno, :saldoInizialeQuad, :dataValuta, :dataRegistrazione, "
			+ " :segnoMovimento, :importoMovimento, :causaleAbi, :causaleInterna, :numeroAssegno, :riferimentoBanca, :riferimentoCliente, "
			+ " :descrizioneMovimento, :cin, :abi, :cab, :numeroCc, :idFilePoste, :progressivoMovimento, :progRiga, :flgValidita) ";

	private static final String QUERY_LOAD = " select * from risca_w_rp_estrco where id_elabora = :idElabora and id_file_poste = :idFilePoste and numero_cc = :numeroConto ";

	private static final String QUERY_DELETE_BY_ID_ELABORA = " delete from risca_w_rp_estrco where id_elabora = :idElabora ";

	@Override
	public RpEstrcoDTO saveRpEstrco(RpEstrcoDTO dto) {
		LOGGER.debug("[RpEstrcoDAOImpl::saveRpEstrco] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", dto.getIdElabora());
			map.put("numeroProgressivo", dto.getNumeroProgressivo());
			map.put("tipoRecord", dto.getTipoRecord());
			map.put("mittente", dto.getMittente());
			map.put("ricevente", dto.getRicevente());
			map.put("dataCreazione", dto.getDataCreazione());
			map.put("nomeSupporto", dto.getNomeSupporto());
			map.put("centroApplicativo", dto.getCentroApplicativo());
			map.put("numeroRendicontazioni", dto.getNumeroRendicontazioni());
			map.put("numeroRecord", dto.getNumeroRecord());
			map.put("giornataApplicativa", dto.getGiornataApplicativa());
			map.put("codiceAbi", dto.getCodiceAbi());
			map.put("causale", dto.getCausale());
			map.put("descrizione", dto.getDescrizione());
			map.put("tipoConto", dto.getTipoConto());
			map.put("coordinateBancarie", dto.getCoordinateBancarie());
			map.put("codiceDivisa", dto.getCodiceDivisa());
			map.put("dataContabile", dto.getDataContabile());
			map.put("segno", dto.getSegno());
			map.put("saldoInizialeQuad", dto.getSaldoInizialeQuad());
			map.put("dataValuta", dto.getDataValuta());
			map.put("dataRegistrazione", dto.getDataRegistrazione());
			map.put("segnoMovimento", dto.getSegnoMovimento());
			map.put("importoMovimento", dto.getImportoMovimento());
			map.put("causaleAbi", dto.getCausaleAbi());
			map.put("causaleInterna", dto.getCausaleInterna());
			map.put("numeroAssegno", dto.getNumeroAssegno());
			map.put("riferimentoBanca", dto.getRiferimentoBanca());
			map.put("riferimentoCliente", dto.getRiferimentoCliente());
			map.put("descrizioneMovimento", dto.getDescrizioneMovimento());
			map.put("cin", dto.getCin());
			map.put("abi", dto.getAbi());
			map.put("cab", dto.getCab());
			map.put("numeroCc", dto.getNumeroCc());
			map.put("idFilePoste", dto.getIdFilePoste());
			map.put("progressivoMovimento", dto.getProgressivoMovimento());
			map.put("progRiga", dto.getProgRiga());
			map.put("flgValidita", dto.getFlgValidita());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[RpEstrcoDAOImpl::saveRpEstrco] ERROR : " + e);
			return null;
		} finally {
			LOGGER.debug("[RpEstrcoDAOImpl::saveRpEstrco] END");
		}

		return dto;
	}

	@Override
	public List<RpEstrcoDTO> loadRpEstrcoByElaboraNumeroConto(Long idElabora, Long idFilePoste, String numeroConto) {
		LOGGER.debug("[RpEstrcoDAOImpl::loadRpEstrcoByElaboraNumeroConto] BEGIN");
		List<RpEstrcoDTO> listRpEstrco = new ArrayList<RpEstrcoDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("numeroConto", numeroConto);
			map.put("idFilePoste", idFilePoste);

			MapSqlParameterSource params = getParameterValue(map);
			listRpEstrco = template.query(getQuery(QUERY_LOAD, null, null), params, new RpRpEstrcoRowMapper());
		} catch (Exception e) {
			LOGGER.error("[RpEstrcoDAOImpl::loadRpEstrcoByElaboraNumeroConto] Errore generale ", e);
			LOGGER.debug("[RpEstrcoDAOImpl::loadRpEstrcoByElaboraNumeroConto] END");
			return listRpEstrco;
		}
		LOGGER.debug("[RpEstrcoDAOImpl::loadRpEstrcoByElaboraNumeroConto] END");
		return listRpEstrco;
	}

	@Override
	public Integer deleteRpEstrco(Long idElabora) {
		LOGGER.debug("[RpEstrcoDAOImpl::deleteRpEstrco] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_BY_ID_ELABORA, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[RpEstrcoDAOImpl::deleteRpEstrco] ERROR : " + e);
			return null;
		} finally {
			LOGGER.debug("[RpEstrcoDAOImpl::deleteRpEstrco] END");
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
	public RowMapper<RpEstrcoDTO> getRowMapper() throws SQLException {
		return new RpRpEstrcoRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<RpEstrcoDTO> getExtendedRowMapper() throws SQLException {
		return new RpRpEstrcoRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class RpRpEstrcoRowMapper implements RowMapper<RpEstrcoDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RpRpEstrcoRowMapper() throws SQLException {
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
		public RpEstrcoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RpEstrcoDTO bean = new RpEstrcoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RpEstrcoDTO bean) throws SQLException {
			bean.setIdElabora(rs.getLong("id_elabora"));
			bean.setNumeroProgressivo(rs.getLong("numero_progressivo"));
			bean.setTipoRecord(rs.getString("tipo_record"));
			bean.setMittente(rs.getString("mittente"));
			bean.setRicevente(rs.getString("ricevente"));
			bean.setDataCreazione(rs.getString("data_creazione"));
			bean.setNomeSupporto(rs.getString("nome_supporto"));
			bean.setCentroApplicativo(rs.getString("centro_applicativo"));
			bean.setNumeroRendicontazioni(rs.getLong("numero_rendicontazioni"));
			bean.setNumeroRecord(rs.getLong("numero_record"));
			bean.setGiornataApplicativa(rs.getLong("giornata_applicativa"));
			bean.setCodiceAbi(rs.getString("codice_abi"));
			bean.setCausale(rs.getString("causale"));
			bean.setDescrizione(rs.getString("descrizione"));
			bean.setTipoConto(rs.getString("tipo_conto"));
			bean.setCoordinateBancarie(rs.getString("coordinate_bancarie"));
			bean.setCodiceDivisa(rs.getString("codice_divisa"));
			bean.setDataContabile(rs.getString("data_contabile"));
			bean.setSegno(rs.getString("segno"));
			bean.setSaldoInizialeQuad(rs.getBigDecimal("saldo_iniziale_quad"));
			bean.setDataValuta(rs.getString("data_valuta"));
			bean.setDataRegistrazione(rs.getString("data_registrazione"));
			bean.setSegnoMovimento(rs.getString("segno_movimento"));
			bean.setImportoMovimento(rs.getBigDecimal("importo_movimento"));
			bean.setCausaleAbi(rs.getString("causale_abi"));
			bean.setCausaleInterna(rs.getString("causale_interna"));
			bean.setNumeroAssegno(rs.getString("numero_assegno"));
			bean.setRiferimentoBanca(rs.getString("riferimento_banca"));
			bean.setRiferimentoCliente(rs.getString("riferimento_cliente"));
			bean.setDescrizioneMovimento(rs.getString("descrizione_movimento"));
			bean.setCin(rs.getString("cin"));
			bean.setAbi(rs.getString("abi"));
			bean.setCab(rs.getString("cab"));
			bean.setNumeroCc(rs.getString("numero_cc"));
			bean.setIdFilePoste(rs.getLong("id_file_poste"));
			bean.setProgressivoMovimento(rs.getLong("progressivo_movimento"));
			bean.setProgRiga(rs.getInt("prog_riga"));
			bean.setFlgValidita(rs.getString("flg_validita"));

		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}