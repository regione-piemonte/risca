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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDatiTitolareDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.mapper.SollDatiTitolareDaoRowMapper;
import it.csi.risca.riscabesrv.dto.SollDatiTitolareDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type SollDatiTitolare dao.
 *
 * @author CSI PIEMONTE
 */
public class SollDatiTitolareDAOImpl extends RiscaBeSrvGenericDAO<SollDatiTitolareDTO> implements SollDatiTitolareDAO {

	public static final String QUERY_INSERT_W = " INSERT INTO risca_w_soll_dati_titolare "
			+ "(id_soll_dati_titolare, id_accertamento, id_tipo_invio, nome_titolare_ind_post, "
			+ "codice_fiscale_calc, codice_fiscale_eti_calc, prov_ind_post, presso_ind_post, "
			+ "indirizzo_ind_post, comune_ind_post, num_prot, data_prot, scadenza_soll, pec_email,"
			+ "id_titolare, id_soggetto ) "
			+ "VALUES(:idSollDatiTitolare, :idAccertamento, :idTipoInvio, :nomeTitolareIndPost, "
			+ ":codiceFiscaleCalc, :codiceFiscaleEtiCalc, :provIndPost, :pressoIndPost, "
			+ ":indirizzoIndPost, :comuneIndPost, :numProt, :dataProt, :scadenzaSoll, :pecEmail, "
			+ ":idTitolare, :idSoggetto) ";
	
	public static final String QUERY_INSERT = " INSERT INTO risca_r_soll_dati_titolare "
			+ "(id_soll_dati_titolare, id_accertamento, id_tipo_invio, "
			+ "nome_titolare_ind_post, codice_fiscale_calc, codice_fiscale_eti_calc, "
			+ "prov_ind_post, presso_ind_post, indirizzo_ind_post, comune_ind_post, "
			+ "num_prot, data_prot, scadenza_soll, pec_email, "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd , gest_attore_upd , gest_uid,"
			+ "id_titolare, id_soggetto ) "
			+ "VALUES(:idSollDatiTitolare, :idAccertamento, :idTipoInvio, :nomeTitolareIndPost, "
			+ ":codiceFiscaleCalc, :codiceFiscaleEtiCalc, :provIndPost, :pressoIndPost, "
			+ ":indirizzoIndPost, :comuneIndPost, :numProt, :dataProt, :scadenzaSoll, :pecEmail, "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid, "
			+ ":idTitolare, :idSoggetto) ";

	public static final String QUERY_DELETE_W_BY_ELABORA = " delete from RISCA_W_SOLL_DATI_TITOLARE "
			+ " where id_accertamento in "
			+ " (SELECT id_accertamento FROM RISCA_W_ACCERTAMENTO  WHERE id_elabora = :idElabora)";
	
	public static final String QUERY_DELETE_BY_ID_ACCERTAMENTO = "DELETE FROM  RISCA_R_SOLL_DATI_TITOLARE WHERE id_accertamento = :idAccertamento";

	public static final String QUERY_COPY_FROM_WORKING = " INSERT INTO RISCA_R_SOLL_DATI_TITOLARE( "
			+ "id_soll_dati_titolare, id_accertamento, id_tipo_invio, "
			+ "nome_titolare_ind_post, codice_fiscale_calc, codice_fiscale_eti_calc, "
			+ "prov_ind_post, presso_ind_post, indirizzo_ind_post, comune_ind_post, "
			+ "num_prot, data_prot, scadenza_soll, pec_email, "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd , gest_attore_upd , gest_uid,"
			+ "id_titolare, id_soggetto ) "
			+ "SELECT id_soll_dati_titolare, id_accertamento, id_tipo_invio, "
			+ "nome_titolare_ind_post, codice_fiscale_calc, codice_fiscale_eti_calc, "
			+ "prov_ind_post, presso_ind_post, indirizzo_ind_post, comune_ind_post, "
			+ "num_prot, data_prot, scadenza_soll, pec_email, "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid, "
			+ "id_titolare, id_soggetto "
			+ "FROM RISCA_W_SOLL_DATI_TITOLARE a WHERE a.id_accertamento IN( "
			+ "SELECT b.id_accertamento FROM RISCA_W_ACCERTAMENTO b WHERE b.id_elabora = :idElabora)";

	@Override
	public SollDatiTitolareDTO saveSollDatiTitolare(SollDatiTitolareDTO dto) throws DAOException {
		LOGGER.debug("[SollDatiTitolareDAOImpl::saveSollDatiTitolare] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Long genId = findNextSequenceValue("seq_risca_r_soll_dati_titolare");
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();

			map.put("idSollDatiTitolare", genId);
			map.put("idAccertamento", dto.getIdAccertamento());
			map.put("idTipoInvio", dto.getIdTipoInvio());
			map.put("nomeTitolareIndPost", dto.getNomeTitolareIndPost());
			map.put("codiceFiscaleCalc", dto.getCodiceFiscaleCalc());
			map.put("codiceFiscaleEtiCalc", dto.getCodiceFiscaleEtiCalc());
			map.put("provIndPost", dto.getProvIndPost());
			map.put("pressoIndPost", dto.getPressoIndPost());
			map.put("indirizzoIndPost", dto.getIndirizzoIndPost());
			map.put("comuneIndPost", dto.getComuneIndPost());
			map.put("numProt", dto.getNumProt());
			map.put("dataProt", dto.getDataProt());
			map.put("scadenzaSoll", dto.getScadenzaSoll());
			map.put("pecEmail", dto.getPecEmail());
			map.put("idTitolare", dto.getIdTitolare());
			map.put("idSoggetto", dto.getIdSoggetto());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataIns", now);
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);
			dto.setIdSollDatiTitolare(genId);

			return dto;
		} catch (Exception e) {
			LOGGER.error("[SollDatiTitolareDAOImpl::saveSollDatiTitolare] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiTitolareDAOImpl::saveSollDatiTitolare] END");
		}
	}
	
	@Override
	public SollDatiTitolareDTO saveSollDatiTitolareWorking(SollDatiTitolareDTO dto) throws DAOException {
		LOGGER.debug("[SollDatiTitolareDAOImpl::saveSollDatiTitolareWorking] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Long genId = findNextSequenceValue("seq_risca_r_soll_dati_titolare");

			map.put("idSollDatiTitolare", genId);
			map.put("idAccertamento", dto.getIdAccertamento());
			map.put("idTipoInvio", dto.getIdTipoInvio());
			map.put("nomeTitolareIndPost", dto.getNomeTitolareIndPost());
			map.put("codiceFiscaleCalc", dto.getCodiceFiscaleCalc());
			map.put("codiceFiscaleEtiCalc", dto.getCodiceFiscaleEtiCalc());
			map.put("provIndPost", dto.getProvIndPost());
			map.put("pressoIndPost", dto.getPressoIndPost());
			map.put("indirizzoIndPost", dto.getIndirizzoIndPost());
			map.put("comuneIndPost", dto.getComuneIndPost());
			map.put("numProt", dto.getNumProt());
			map.put("dataProt", dto.getDataProt());
			map.put("scadenzaSoll", dto.getScadenzaSoll());
			map.put("pecEmail", dto.getPecEmail());
			map.put("idTitolare", dto.getIdTitolare());
			map.put("idSoggetto", dto.getIdSoggetto());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_W, null, null), params, keyHolder);
			dto.setIdSollDatiTitolare(genId);

			return dto;
		} catch (Exception e) {
			LOGGER.error("[SollDatiTitolareDAOImpl::saveSollDatiTitolareWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiTitolareDAOImpl::saveSollDatiTitolareWorking] END");
		}
	}

	@Override
	public Integer deleteSollDatiTitolareWorkingByElabora(Long idElabora) throws DAOException {
		LOGGER.debug("[SollDatiTitolareDAOImpl::deleteSollDatiTitolareWorkingByElabora] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_W_BY_ELABORA, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDatiTitolareDAOImpl::deleteSollDatiTitolareWorkingByElabora] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiTitolareDAOImpl::deleteSollDatiTitolareWorkingByElabora] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteSollDatiTitolareByIdAccertamento(Long idAccertamento) throws DAOException {
		LOGGER.debug("[SollDatiTitolareDAOImpl::deleteSollDatiTitolareByIdAccertamento] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAccertamento", idAccertamento);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_BY_ID_ACCERTAMENTO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDatiTitolareDAOImpl::deleteSollDatiTitolareByIdAccertamento] ERROR : ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiTitolareDAOImpl::deleteSollDatiTitolareByIdAccertamento] END");
		}

		return res;
	}

	@Override
	public Integer copySollDatiTitolareFromWorking(Long idElabora, String attore) throws DAOException {
		LOGGER.debug("[SollDatiTitolareDAOImpl::copySollDatiTitolareFromWorking] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestAttoreIns", attore);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataIns", now);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_COPY_FROM_WORKING, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDatiTitolareDAOImpl::copySollDatiTitolareFromWorking] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiTitolareDAOImpl::copySollDatiTitolareFromWorking] END");
		}
	}
	
	
	@Override
	public SollDatiTitolareDTO loadSollDatiTitolareByIdAccertamento(Long idAccertamento) throws DataAccessException, SQLException, DAOException, SystemException {
		LOGGER.debug("[SollDatiTitolareDAOImpl::loadSollDatiTitolareByIdAccertamento] BEGIN");
		LOGGER.debug("[SollDatiTitolareDAOImpl::loadSollDatiTitolareByIdAccertamento] idAccertamento : "+idAccertamento);
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();		

		sql.append("		select  ");
		sql.append("		ist.id_accertamento as id_accertamento_soll, ");
		sql.append("	    isd.id_soggetto  id_soggetto, ");
		sql.append("	    case ");
		sql.append("			when ist.id_accertamento is not null then  ");
		sql.append("			ist.tit_codice_fiscale	 " );
		sql.append("			else isd.sd_codice_fiscale end codice_fiscale, ");
		sql.append("		case ");
		sql.append("			when ist.id_accertamento is not null then ist.tit_indirizzo ");
		sql.append("			else isd.sd_indirizzo end indirizzo, ");
		sql.append("		case ");
		sql.append("			when ist.id_accertamento is not null then ist.tit_comune ");
		sql.append("			else isd.sd_comune end comune, ");
		sql.append("		case ");
		sql.append("			when ist.id_accertamento is not null then ist.tit_provincia ");
		sql.append("			else isd.sd_provincia end provincia, ");
		sql.append("		case ");
		sql.append("			when ist.id_accertamento is not null then ist.tit_cod_belfiore ");
		sql.append("			else isd.sd_cod_belfiore end cod_belfiore, " );
		sql.append("		 case ");
		sql.append("			when ist.id_accertamento is not null then ist.tit_cap_comune ");
		sql.append("			else isd.sd_cap_comune end cap_comune, ");
		sql.append("		 case ");
		sql.append("			when ist.id_accertamento is not null then ist.tit_den_soggetto ");
		sql.append("			else isd.sd_den_soggetto end den_soggetto, " );
		sql.append("		case ");
		sql.append("			when ist.id_accertamento is not null then '' ");
		sql.append("			else isd.sd_nome end nome, ");
		sql.append("		case ");
		sql.append("			when ist.id_accertamento is not null then ist.tit_cognome ");
		sql.append("			else isd.sd_cognome end cognome, ");
		sql.append("		case ");
		sql.append("			when ist.id_accertamento is not null then '' ");
		sql.append("			else isd.sd_data_nascita end data_nascita, ");
		sql.append("		case ");
		sql.append("			when ist.id_accertamento is not null then  ");
		sql.append("	       (case when length (ist.tit_codice_fiscale) =11 then  ist.tit_codice_fiscale else '00000000000' end )	  ");
		sql.append("			else isd.sd_partita_iva_soggetto end partita_iva, ");
		sql.append("		 case ");
		sql.append("			when ist.id_accertamento is not null then  ");
		sql.append("			(case when length (ist.tit_codice_fiscale) =16 then  1 else 2 end )	  ");
		sql.append("			else isd.sd_id_tipo_soggetto end id_tipo_soggetto ");
		sql.append("	from ");
		sql.append("		risca_t_accertamento acc ");
		sql.append("	left join ( ");
		sql.append("		select ");
		sql.append("		rsd.id_soggetto , ");
		sql.append("			rsd.id_accertamento, ");
		sql.append("			codice_fiscale_calc as tit_codice_fiscale, ");
		sql.append("			split_part(split_part(indirizzo_ind_post, right ( indirizzo_ind_post, 6), 1), right ( indirizzo_ind_post, 6), 1) as tit_indirizzo , ");
		sql.append("			comune_ind_post as tit_comune, ");
		sql.append("			prov_ind_post as tit_provincia, ");
		sql.append("			dco.cod_belfiore_comune as tit_cod_belfiore, ");
		sql.append("			reverse(split_part(reverse(indirizzo_ind_post), ' ', 1)) as tit_cap_comune, ");
		sql.append("			rsd.nome_titolare_ind_post as tit_den_soggetto, ");
		sql.append("			rsd.nome_titolare_ind_post as tit_nome, ");
		sql.append("			rsd.nome_titolare_ind_post as tit_cognome ");
		sql.append("		from ");
		sql.append("			risca_r_soll_dati_titolare rsd ");
		sql.append("		left join ( ");
		sql.append("			select ");
		sql.append("				distinct ddc.denom_comune, ");
		sql.append("				ddc.cod_belfiore_comune ");
		sql.append("			from ");
		sql.append("				risca_d_comune ddc ");
		sql.append("			where ");
		sql.append("				ddc.data_fine_validita is null) dco on ");
		sql.append("			rsd.comune_ind_post = dco.denom_comune ) ist on ");
		sql.append("		acc.id_accertamento = ist.id_accertamento ");
		sql.append("	left join ( ");
		sql.append("		select ");
		sql.append("		rts.id_soggetto , ");
		sql.append("			rta.id_accertamento, ");
		sql.append("			rts.cf_soggetto as sd_codice_fiscale, ");
		sql.append("			rrr.indirizzo || ', ' || rrr.num_civico as sd_indirizzo, ");
		sql.append("			rdc.denom_comune as sd_comune, ");
		sql.append("			rdp.sigla_provincia as sd_provincia, ");
		sql.append("			rdc.cod_belfiore_comune as sd_cod_belfiore, ");
		sql.append("			rdc.cap_comune as sd_cap_comune, "  );
		sql.append("			rts.den_soggetto as sd_den_soggetto, ");
		sql.append("			rts.nome as sd_nome, ");
		sql.append("			rts.cognome as sd_cognome, ");
		sql.append("			to_char(rts.data_nascita_soggetto, 'YYYYMMdd') as sd_data_nascita, ");
		sql.append("			decode(rts.id_tipo_soggetto, 1, '00000000000'  , rts.partita_iva_soggetto ) sd_partita_iva_soggetto , ");
		sql.append("			rts.id_tipo_soggetto as sd_id_tipo_soggetto ");
		sql.append("		from ");
		sql.append("			risca_t_accertamento rta, ");
		sql.append("			risca_t_stato_debitorio rtsd, ");
		sql.append("			risca_t_soggetto rts, ");
		sql.append("			risca_r_recapito rrr, ");
		sql.append("			risca_d_comune rdc, ");
		sql.append("			risca_d_provincia rdp ");
		sql.append("		where ");
		sql.append("			rta.id_stato_debitorio = rtsd.id_stato_debitorio ");
		sql.append("			and rtsd.id_soggetto = rts.id_soggetto ");
		sql.append("			and rrr.id_recapito = rtsd.id_recapito ");
		sql.append("			and rdc.id_comune = rrr.id_comune_recapito ");
		sql.append("			and rdp.id_provincia = rdc.id_provincia ) isd on ");
		sql.append("		acc.id_accertamento = isd.id_accertamento ");
		sql.append("	where  ");
		sql.append("		 acc.id_accertamento = :id_accertamento  ");


		paramMap.addValue("id_accertamento", idAccertamento);
		LOGGER.debug("[SollDatiTitolareDAOImpl - loadSollDatiTitolareByIdAccertamento] query =" + sql.toString());

		SollDatiTitolareDTO sollDatiTitolareDto = null;
		try
		{
			sollDatiTitolareDto = template.queryForObject(sql.toString(), paramMap, new SollDatiTitolareDaoRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOGGER.debug("\"[SollDatiTitolareDAOImpl::loadSollDatiTitolareByIdAccertamento]  NESSUN RISULTATO");
			throw new DAOException("Nessun soll dato titolare trovato in base alla richiesta");
		}
		catch (Throwable ex)
		{
			LOGGER.error("[SollDatiTitolareDAOImpl::loadSollDatiTitolareByIdAccertamento] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOGGER.debug("[SollDatiTitolareDAOImpl::loadSollDatiTitolareByIdAccertamento] END");
		}
		return sollDatiTitolareDto;
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
	public RowMapper<SollDatiTitolareDTO> getRowMapper() throws SQLException {
		return new SollDatiTitolareRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<SollDatiTitolareDTO> getExtendedRowMapper() throws SQLException {
		return new SollDatiTitolareRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class SollDatiTitolareRowMapper implements RowMapper<SollDatiTitolareDTO> {

		/**
		 * Instantiates a new AvvisoDatiTitolare row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SollDatiTitolareRowMapper() throws SQLException {
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
		public SollDatiTitolareDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SollDatiTitolareDTO bean = new SollDatiTitolareDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SollDatiTitolareDTO bean) throws SQLException {
			bean.setIdSollDatiTitolare(getLong(rs, "id_soll_dati_titolare"));
			bean.setIdAccertamento(getLong(rs, "id_accertamento"));
			bean.setIdTipoInvio(getLong(rs, "id_tipo_invio"));
			bean.setNomeTitolareIndPost(rs.getString("nome_titolare_ind_post"));
			bean.setCodiceFiscaleCalc(rs.getString("codice_fiscale_calc"));
			bean.setCodiceFiscaleEtiCalc(rs.getString("codice_fiscale_eti_calc"));
			bean.setProvIndPost(rs.getString("prov_ind_post"));
			bean.setPressoIndPost(rs.getString("presso_ind_post"));
			bean.setIndirizzoIndPost(rs.getString("indirizzo_ind_post"));
			bean.setComuneIndPost(rs.getString("comune_ind_post"));
			bean.setNumProt(rs.getString("num_prot"));
			bean.setDataProt(rs.getDate("data_prot"));
			bean.setScadenzaSoll(rs.getString("scadenza_soll"));
			bean.setPecEmail(rs.getString("pec_email"));
			bean.setIdTitolare(rs.getString("id_titolare"));
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
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