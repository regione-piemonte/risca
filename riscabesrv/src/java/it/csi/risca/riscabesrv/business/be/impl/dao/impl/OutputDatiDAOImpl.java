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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.OutputDatiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.OutputDatiDTO;
import it.csi.risca.riscabesrv.dto.PagopaPosizioniDebitorieDTO;
import it.csi.risca.riscabesrv.util.BollUtils;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type ParametroElabora dao.
 *
 * @author CSI PIEMONTE
 */
public class OutputDatiDAOImpl extends RiscaBeSrvGenericDAO<OutputDatiDTO> implements OutputDatiDAO {

	private static final String QUERY_INSERT = "INSERT INTO risca_w_output_dati "
			+ "(id_elabora, progressivo, id_output_foglio, id_tipo_passo_elabora, "
			+ "valore_colonna1, valore_colonna2, valore_colonna3, valore_colonna4, valore_colonna5, "
			+ "valore_colonna6, valore_colonna7, valore_colonna8, valore_colonna9, valore_colonna10, "
			+ "valore_colonna11, valore_colonna12, valore_colonna13, valore_colonna14, valore_colonna15, "
			+ "valore_colonna16, valore_colonna17, valore_colonna18, valore_colonna19, valore_colonna20, "
			+ "valore_colonna21, valore_colonna22, valore_colonna23, valore_colonna24, valore_colonna25, "
			+ "valore_colonna26, valore_colonna27, valore_colonna28, valore_colonna29, valore_colonna30, "
			+ "valore_colonna31, valore_colonna32, valore_colonna33, valore_colonna34, valore_colonna35, "
			+ "valore_colonna36, valore_colonna37, valore_colonna38, valore_colonna39, valore_colonna40, "
			+ "valore_colonna41, valore_colonna42, valore_colonna43, valore_colonna44, valore_colonna45, "
			+ "valore_colonna46, valore_colonna47, valore_colonna48, valore_colonna49, valore_colonna50, "
			+ "valore_colonna51, valore_colonna52, valore_colonna53, valore_colonna54, valore_colonna55, "
			+ "valore_colonna56, valore_colonna57, valore_colonna58, valore_colonna59, valore_colonna60, "
			+ "valore_colonna61, valore_colonna62, valore_colonna63, valore_colonna64, valore_colonna65, "
			+ "valore_colonna66, valore_colonna67, valore_colonna68, valore_colonna69, valore_colonna70, "
			+ "valore_colonna71, valore_colonna72, valore_colonna73, valore_colonna74, valore_colonna75, "
			+ "valore_colonna76, valore_colonna77, valore_colonna78, valore_colonna79, valore_colonna80, "
			+ "valore_colonna81, valore_colonna82, valore_colonna83, valore_colonna84, valore_colonna85, "
			+ "valore_colonna86, valore_colonna87, valore_colonna88, valore_colonna89, valore_colonna90, "
			+ "flg_invio_pec_email) " + "SELECT :idElabora, :progressivo, :idOutputFoglio, id_tipo_passo_elabora, "
			+ ":valoreColonna1, :valoreColonna2, :valoreColonna3, :valoreColonna4, :valoreColonna5, "
			+ ":valoreColonna6, :valoreColonna7, :valoreColonna8, :valoreColonna9, :valoreColonna10, "
			+ ":valoreColonna11, :valoreColonna12, :valoreColonna13, :valoreColonna14, :valoreColonna15, "
			+ ":valoreColonna16, :valoreColonna17, :valoreColonna18, :valoreColonna19, :valoreColonna20, "
			+ ":valoreColonna21, :valoreColonna22, :valoreColonna23, :valoreColonna24, :valoreColonna25, "
			+ ":valoreColonna26, :valoreColonna27, :valoreColonna28, :valoreColonna29, :valoreColonna30, "
			+ ":valoreColonna31, :valoreColonna32, :valoreColonna33, :valoreColonna34, :valoreColonna35, "
			+ ":valoreColonna36, :valoreColonna37, :valoreColonna38, :valoreColonna39, :valoreColonna40, "
			+ ":valoreColonna41, :valoreColonna42, :valoreColonna43, :valoreColonna44, :valoreColonna45, "
			+ ":valoreColonna46, :valoreColonna47, :valoreColonna48, :valoreColonna49, :valoreColonna50, "
			+ ":valoreColonna51, :valoreColonna52, :valoreColonna53, :valoreColonna54, :valoreColonna55, "
			+ ":valoreColonna56, :valoreColonna57, :valoreColonna58, :valoreColonna59, :valoreColonna60, "
			+ ":valoreColonna61, :valoreColonna62, :valoreColonna63, :valoreColonna64, :valoreColonna65, "
			+ ":valoreColonna66, :valoreColonna67, :valoreColonna68, :valoreColonna69, :valoreColonna70, "
			+ ":valoreColonna71, :valoreColonna72, :valoreColonna73, :valoreColonna74, :valoreColonna75, "
			+ ":valoreColonna76, :valoreColonna77, :valoreColonna78, :valoreColonna79, :valoreColonna80, "
			+ ":valoreColonna81, :valoreColonna82, :valoreColonna83, :valoreColonna84, :valoreColonna85, "
			+ ":valoreColonna86, :valoreColonna87, :valoreColonna88, :valoreColonna89, :valoreColonna90, 0 "
			+ "FROM risca_d_tipo_passo_elabora where cod_tipo_passo_elabora = :codTipoPassoElabora ";

	private static final String QUERY_LOAD_OUTPUT_DATI_BY_FOGLIO_BS = " SELECT a.*, b.cod_tipo_passo_elabora  "
			+ "FROM risca_w_output_dati a "
			+ "inner join risca_d_tipo_passo_elabora b on a.id_tipo_passo_elabora = b.id_tipo_passo_elabora  "
			+ "left join risca_t_iuv rti on a.valore_colonna16 = rti.nap	 " + "WHERE id_elabora = :idElabora  "
			+ "AND id_output_foglio  = :idOutputFoglio " + "and rti.nap is null " + "ORDER BY progressivo ";

	private static final String QUERY_LOAD_OUTPUT_DATI_BY_FOGLIO_BO = " SELECT a.*, b.cod_tipo_passo_elabora  "
			+ "FROM risca_w_output_dati a "
			+ "inner join risca_d_tipo_passo_elabora b on a.id_tipo_passo_elabora = b.id_tipo_passo_elabora  "
			+ "left join risca_t_iuv rti on a.valore_colonna15 = rti.nap	 " + "WHERE id_elabora = :idElabora  "
			+ "AND id_output_foglio  = :idOutputFoglio " + "and rti.nap is null " + "ORDER BY progressivo ";

	private static final String QUERY_LOAD_OUTPUT_DATI_BY_FOGLIO = " SELECT a.*, b.cod_tipo_passo_elabora "
			+ "FROM risca_w_output_dati a "
			+ "inner join risca_d_tipo_passo_elabora b on a.id_tipo_passo_elabora = b.id_tipo_passo_elabora "
			+ "WHERE id_elabora = :idElabora " + "AND id_output_foglio = :idOutputFoglio " + "ORDER BY progressivo ";

	private static final String QUERY_LOAD_OUTPUT_DATI_TITOLARE = " SELECT * FROM RISCA_W_OUTPUT_DATI d "
			+ "INNER JOIN RISCA_D_OUTPUT_FOGLIO f ON d.id_output_foglio = f.id_output_foglio  "
			+ "WHERE d.id_elabora = :idElabora " + "AND f.nome_foglio = 'Dati_Titolare' "
			+ "AND d.flg_invio_pec_email = 0 ";

	private static final String QUERY_UPDATE_NOMI_LOTTO_BS = " update risca_w_output_dati as wod "
			+ " set valore_colonna89 = :codSettore || :codVersamento || '_' || :dataLotto || '_' || :idElabora ||  '_' || LPAD(''||(TRUNC((progressivo - 1)/ :recordPerLotto) + 1), 2, '0') "
			// + " from risca_t_iuv as tiu "
			+ " where wod.id_output_foglio = 56 " + " and wod.id_elabora = :idElabora "
			+ " and wod.valore_colonna16 not in ( select nap from risca_t_iuv ) "
			+ " and wod.valore_colonna9 <> '0.00' ";
	// + " and wod.valore_colonna16 <> tiu.nap ";
	
	private static final String QUERY_UPDATE_NOMI_LOTTO_BG = " update risca_w_output_dati as wod "
			+ " set valore_colonna89 = :codSettore || :codVersamento || '_' || :dataLotto || '_' || :idElabora ||  '_' || LPAD(''||(TRUNC((progressivo - 1)/ :recordPerLotto) + 1), 2, '0') "
			// + " from risca_t_iuv as tiu "
			+ " where wod.id_output_foglio = 75 " + " and wod.id_elabora = :idElabora "
			+ " and wod.valore_colonna16 not in ( select nap from risca_t_iuv ) "
			+ " and wod.valore_colonna9 <> '0.00' ";
	// + " and wod.valore_colonna16 <> tiu.nap ";

	private static final String QUERY_UPDATE_NOMI_LOTTO_BO = " update risca_w_output_dati as wod "
			+ " set valore_colonna89 = :codSettore || :codVersamento || '_' || :dataLotto || '_' || :idElabora ||  '_' || LPAD(''||(TRUNC((progressivo - 1)/ :recordPerLotto) + 1), 2, '0') "
			// + " from risca_t_iuv as tiu "
			+ " where wod.id_output_foglio = 40 " + " and wod.id_elabora = :idElabora "
			+ " and wod.valore_colonna15 not in ( select nap from risca_t_iuv ) "
			+ " and wod.valore_colonna9 <> '0.00' ";
	// + " and wod.valore_colonna16 <> tiu.nap ";

	private static final String QUERY_LOAD_NOMI_LOTTO = " select DISTINCT( valore_colonna89 ) "
			+ " from risca_w_output_dati " 
			+ " where id_output_foglio = :idOutputFoglio "
			+ " and id_elabora = :idElabora " 
			+ " and valore_colonna89 not in (select nome_lotto from risca_t_lotto) "
			+ " order by valore_colonna89 ";

	// Questa query fa in modo di estrarre solo i NAP per i quali non e' ancora
	// stato ricevuto lo IUV
	// ci permette di ritentare una richiesta IUV una seconda volta per quelli
	// andati male la prima
	public static final String QUERY_LOAD_DATI_IUV_BS = "select " + "valore_colonna89 nome_lotto, "
			+ "valore_colonna16 nap, " + "valore_colonna12 anno, " + "valore_colonna9 importo, "
			+ "valore_colonna10 data_scadenza, " + "to_char(current_date, 'DD/MM/YYYY') data_inizio_validita,  "
			+ "'31/12/' ||(to_number(to_char(current_date, 'YYYY')) + 10) data_fine_validita, " + ":causale causale, "
			+ "decode(id_tipo_soggetto, 1, 'F', 'G') tipo_soggetto, " + "substr(den_soggetto,1,70) ragione_sociale, "
			+ "substr(cognome,	1,70) cognome, " + "substr(nome, 1,	70) nome, " + "valore_colonna15 cod_fiscale, "
			+ "'NAP: ' || valore_colonna16 note, " + "rti.nap " + "from risca_w_output_dati wod "
			+ "inner join risca_t_soggetto s on to_number( split_part(split_part(valore_colonna2, '*', 1), 'R', 1)) = s.id_soggetto "
			+ "left join risca_t_iuv rti on wod.valore_colonna16 = rti.nap	" + "where valore_colonna89 = :nomeLotto "
			+ "and valore_colonna9 <> '0.00' " + "and rti.nap is null";

	public static final String QUERY_LOAD_DATI_IUV_BO = "select " + "valore_colonna89 nome_lotto, "
			+ "valore_colonna15 nap, " + "valore_colonna12 anno, " + "valore_colonna9 importo, "
			+ "valore_colonna10 data_scadenza, " + "to_char(current_date, 'DD/MM/YYYY') data_inizio_validita,  "
			+ "'31/12/' ||(to_number(to_char(current_date, 'YYYY')) + 10) data_fine_validita, " + ":causale causale, "
			+ "decode(id_tipo_soggetto, 1, 'F', 'G') tipo_soggetto, " + "substr(den_soggetto,1,70) ragione_sociale, "
			+ "substr(cognome,	1,70) cognome, " + "substr(nome, 1,	70) nome, " + "valore_colonna14 cod_fiscale, "
			+ "'NAP: ' || valore_colonna15 note, " + "rti.nap " + "from risca_w_output_dati wod "
			+ "inner join risca_t_soggetto s on to_number( split_part(split_part(valore_colonna2, '*', 1), 'R', 1)) = s.id_soggetto "
			+ "left join risca_t_iuv rti on wod.valore_colonna15 = rti.nap	" + "where valore_colonna89 = :nomeLotto "
			+ "and valore_colonna9 <> '0.00' " + "and rti.nap is null";

	private static final String QUERY_UPDATE_IUV_CODICE_AVVISO_BS = "update risca_w_output_dati "
			+ " set valore_colonna1 = valore_colonna1 || " + "      ( " + "        select codice_avviso || ';' || iuv "
			+ "        from risca_t_iuv "
			+ "        where nap = SUBSTR(valore_colonna1, INSTR(valore_colonna1, ';', 1, 15) + 1, 11) "
			+ "      ) ||';' " + " where id_elabora = :idElabora " + " and id_output_foglio in (61, 62) "
			+ " and SUBSTR(valore_colonna1, 1, 1) = '2' " + " and exists ( "
			+ "         select codice_avviso || ';' || iuv " + "         from risca_t_iuv "
			+ "         where nap = SUBSTR(valore_colonna1, INSTR(valore_colonna1, ';', 1, 15) + 1, 11) " + " ) ";
	
	private static final String QUERY_UPDATE_IUV_CODICE_AVVISO_BG = "update risca_w_output_dati "
			+ " set valore_colonna1 = valore_colonna1 || " + "      ( " + "        select codice_avviso || ';' || iuv "
			+ "        from risca_t_iuv "
			+ "        where nap = SUBSTR(valore_colonna1, INSTR(valore_colonna1, ';', 1, 15) + 1, 11) "
			+ "      ) ||';' " + " where id_elabora = :idElabora " + " and id_output_foglio in (80, 81) "
			+ " and SUBSTR(valore_colonna1, 1, 1) = '2' " + " and exists ( "
			+ "         select codice_avviso || ';' || iuv " + "         from risca_t_iuv "
			+ "         where nap = SUBSTR(valore_colonna1, INSTR(valore_colonna1, ';', 1, 15) + 1, 11) " + " ) ";

	private static final String QUERY_UPDATE_IUV_CODICE_AVVISO_BO = "update risca_w_output_dati "
			+ " set valore_colonna1 = valore_colonna1 || " + "      ( " + "        select codice_avviso || ';' || iuv "
			+ "        from risca_t_iuv "
			+ "        where nap = SUBSTR(valore_colonna1, INSTR(valore_colonna1, ';', 1, 14) + 1, 11) "
			+ "      ) ||';' " + " where id_elabora = :idElabora " + " and id_output_foglio in (45, 46) "
			+ " and SUBSTR(valore_colonna1, 1, 1) = '2' " + " and exists ( "
			+ "         select codice_avviso || ';' || iuv " + "         from risca_t_iuv "
			+ "         where nap = SUBSTR(valore_colonna1, INSTR(valore_colonna1, ';', 1, 14) + 1, 11) " + " ) ";

	private static final String QUERY_UPDATE_FLAG_PEC_MAIL = " update risca_w_output_dati set flg_invio_pec_email = 1 "
			+ " where id_elabora = :idElabora " + " and progressivo = :progressivo "
			+ " and id_output_foglio = :idOutputFoglio ";

	private static final String QUERY_UPDATE_ID_ACCERTAMENTO_ODS = " update risca_w_output_dati "
			+ "set valore_colonna89 = :idAccertamento " + "where id_elabora = :idElabora "
			+ "and progressivo = :progressivo " + "and id_output_foglio = :idOutputFoglio";

	private static final String QUERY_UPDATE_ID_ACCERTAMENTO_TXT = " update risca_w_output_dati "
			+ "set valore_colonna7 = :idAccertamento " + "where id_elabora = :idElabora "
			+ "and progressivo = :progressivo " + "and id_output_foglio = :idOutputFoglio "
			+ "and valore_colonna1 like '3;%'";

	private static final String QUERY_UPDATE_CODICE_AVVISO_ODS = " update risca_w_output_dati  "
			+ "set valore_colonna27 = :codiceAvviso " + "where id_elabora = :idElabora  "
			+ "and id_output_foglio = :idOutputFoglio " + "and valore_colonna1 = '3' "
			+ "and valore_colonna89 = :idAccertamento";

	private static final String QUERY_UPDATE_CODICE_AVVISO_TXT = " update risca_w_output_dati "
			+ "set valore_colonna1 = valore_colonna1 || :codiceAvviso || ';' " + "where id_elabora = :idElabora "
			+ "and id_output_foglio = :idOutputFoglio " + "and valore_colonna1 like '3;%' "
			+ "and valore_colonna7 = :idAccertamento";

	private static final String QUERY_LOAD_PROGRESSIVO_TITOLARE_TXT = "select progressivo "
			+ "from RISCA_W_OUTPUT_DATI " + "where id_elabora = :idElabora " + "and id_output_foglio = :idOutputFoglio "
			+ "and valore_colonna2 = :soggettoGruppo " + "and valore_colonna3 = '2' ";

	private static final String QUERY_LOAD_OUTPUT_DATI_TXT_BY_TIPO_RECORD = "select * " + "from RISCA_W_OUTPUT_DATI "
			+ "where id_elabora = :idElabora " + "and id_output_foglio = :idOutputFoglio "
			+ "and valore_colonna2 = :soggettoGruppo " + "and valore_colonna3 = :tipoRecord ";

	private static final String ORDER_BY_TIPO_REC_3 = " order by valore_colonna4,  TO_DATE(valore_colonna5, 'DD/MM/YYYY') ";
	private static final String ORDER_BY_TIPO_REC_4 = " order by valore_colonna4,  TO_DATE(valore_colonna5, 'DD/MM/YYYY'), TO_DATE(valore_colonna6, 'DD/MM/YYYY') ";
	private static final String ORDER_BY_TIPO_REC_6 = " order by valore_colonna4, valore_colonna7 ";

	private static final String QUERY_UPDATE_PROGRESSIVO_TXT = "update RISCA_W_OUTPUT_DATI "
			+ "set progressivo = :progressivo " + "where id_elabora = :idElabora "
			+ "and progressivo = :progressivoOld " + "and id_output_foglio = :idOutputFoglio ";
	
	private static final String QUERY_UPDATE_XLS_SOLL_IMP_VERS = "update RISCA_W_OUTPUT_DATI "
			+ " set valore_colonna7 = :importoDaVersare "
			+ " where id_elabora = :idElabora "
			+ " and id_output_foglio = :idOutputFoglio " 
			+ " and valore_colonna4 = :iuv "
			+ " and valore_colonna1 = '6' ";
	
	private static final String QUERY_UPDATE_TXT_SOLL_IMP_VERS = "update RISCA_W_OUTPUT_DATI "
			+ " set valore_colonna1 = :valoreColonna1 "
			+ " where id_elabora = :idElabora "
			+ " and id_output_foglio = :idOutputFoglio "
			+ " and valore_colonna3 = '6' "
			+ " and substring(valore_colonna1 from 17 for 17) = :iuv";

	@Override
	public OutputDatiDTO saveOutputDati(OutputDatiDTO dto) throws DAOException {
		LOGGER.debug("[OutputDatiDAOImpl::saveOutputDati] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			populateMap(dto, map);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);
		} catch (Exception e) {
			LOGGER.error("[OutputDatiDAOImpl::saveOutputDati] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::saveOutputDati] END");
		}
		return dto;
	}
	
	@Override
	public Integer updateXlsSollImportoDaVersareByIuv(Long idElabora, String iuv, Long idOutputFoglio, BigDecimal importoDaVersare) throws DAOException {
		LOGGER.debug("[OutputDatiDAOImpl::updateXlsSollImportoDaVersareByIuv] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("iuv", iuv);
			map.put("idOutputFoglio", idOutputFoglio);
			map.put("importoDaVersare", importoDaVersare);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			return template.update(getQuery(QUERY_UPDATE_XLS_SOLL_IMP_VERS, null, null), params, keyHolder);
		} catch (Exception e) {
			LOGGER.error("[OutputDatiDAOImpl::updateXlsSollImportoDaVersareByIuv] ERROR : ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::updateXlsSollImportoDaVersareByIuv] END");
		}
	}

	@Override
	public Integer updateTxtSollImportoDaVersareByIuv(Long idElabora, String iuv, Long idOutputFoglio, String valoreColonna1) throws DAOException {
		LOGGER.debug("[OutputDatiDAOImpl::updateTxtSollImportoDaVersareByIuv] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("iuv", iuv);
			map.put("idOutputFoglio", idOutputFoglio);
			map.put("valoreColonna1", valoreColonna1);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			return template.update(getQuery(QUERY_UPDATE_TXT_SOLL_IMP_VERS, null, null), params, keyHolder);
		} catch (Exception e) {
			LOGGER.error("[OutputDatiDAOImpl::updateTxtSollImportoDaVersareByIuv] ERROR : ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::updateTxtSollImportoDaVersareByIuv] END");
		}
	}

	@Override
	public List<OutputDatiDTO> loadOutputDatiByFoglio(Long idElabora, Long idOutputFoglio, String codTipoElabora) {
		LOGGER.debug("[OutputDatiDAOImpl::loadOutputDatiByFoglio] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("idOutputFoglio", idOutputFoglio);
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_LOAD_OUTPUT_DATI_BY_FOGLIO;
			if (codTipoElabora != null && codTipoElabora.equals(Constants.COD_TIPO_ELABORA_BO)) {
				query = QUERY_LOAD_OUTPUT_DATI_BY_FOGLIO_BO;
			} else if (codTipoElabora != null && codTipoElabora.equals(Constants.COD_TIPO_ELABORA_BS)) {
				query = QUERY_LOAD_OUTPUT_DATI_BY_FOGLIO_BS;
			}
			return template.query(getQuery(query, null, null), params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[OutputDatiDAOImpl::loadOutputDatiByFoglio] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[OutputDatiDAOImpl::loadOutputDatiByFoglio] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::loadOutputDatiByFoglio] END");
		}
	}

	@Override
	public List<OutputDatiDTO> loadOutputDatiTitolare(Long idElabora) {
		LOGGER.debug("[OutputDatiDAOImpl::loadOutputDatiTitolare] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_LOAD_OUTPUT_DATI_TITOLARE, null, null), params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[OutputDatiDAOImpl::loadOutputDatiTitolare] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[OutputDatiDAOImpl::loadOutputDatiTitolare] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::loadOutputDatiTitolare] END");
		}
	}

	@Override
	public Integer updateOutputDatiNomiLotto(Long idElabora, String codSettore, String codVersamento, String dataLotto,
			Integer recordPerLotto, String tipoElab) throws DAOException {
		LOGGER.debug("[OutputDatiDAOImpl::updateOutputDatiNomiLotto] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("codSettore", codSettore);
			map.put("codVersamento", codVersamento);
			map.put("dataLotto", dataLotto);
			map.put("recordPerLotto", recordPerLotto);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_UPDATE_NOMI_LOTTO_BO;
			if (tipoElab.equalsIgnoreCase(BollUtils.COD_TIPO_ELABORA_BS)) {
				query = QUERY_UPDATE_NOMI_LOTTO_BS;
			} else if (tipoElab.equalsIgnoreCase(BollUtils.COD_TIPO_ELABORA_BG)) {
				query = QUERY_UPDATE_NOMI_LOTTO_BG;
			} 
			res = template.update(getQuery(query, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[OutputDatiDAOImpl::updateOutputDatiNomiLotto] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::updateOutputDatiNomiLotto] END");
		}

		return res;
	}

	@Override
	public List<String> loadNomiLotto(Long idElabora, Long idOutputFoglio) {
		LOGGER.debug("[OutputDatiDAOImpl::loadNomiLotto] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("idOutputFoglio", idOutputFoglio);

			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_LOAD_NOMI_LOTTO, null, null), params, new NomeLottoRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error("[OutputDatiDAOImpl::loadNomiLotto] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::loadNomiLotto] END");
		}
	}

	@Override
	public List<PagopaPosizioniDebitorieDTO> loadPosizioniDebitorieByNomeLotto(String nomeLotto, String causale,
			boolean bollettazioneSpeciale) {
		LOGGER.debug("[OutputDatiDAOImpl::loadPosizioniDebitorieByNomeLotto] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nomeLotto", nomeLotto);
			map.put("causale", causale);

			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_LOAD_DATI_IUV_BO;
			if (bollettazioneSpeciale) {
				query = QUERY_LOAD_DATI_IUV_BS;
			}
			return template.query(getQuery(query, null, null), params, new PagopaPosizioniDebitorieRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error("[OutputDatiDAOImpl::loadPosizioniDebitorieByNomeLotto] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::loadPosizioniDebitorieByNomeLotto] END");
		}
	}

	@Override
	public Integer updateOutputDatiIuvCodiceAvviso(Long idElabora, String codTipoElabora) throws DAOException {
		LOGGER.debug("[OutputDatiDAOImpl::updateOutputDatiIuvCodiceAvviso] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_UPDATE_IUV_CODICE_AVVISO_BO;
			if (codTipoElabora.equalsIgnoreCase(BollUtils.COD_TIPO_ELABORA_BS)) {
				query = QUERY_UPDATE_IUV_CODICE_AVVISO_BS;
			} else if (codTipoElabora.equalsIgnoreCase(BollUtils.COD_TIPO_ELABORA_BG)) {
				query = QUERY_UPDATE_IUV_CODICE_AVVISO_BG;
			}
			res = template.update(getQuery(query, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[OutputDatiDAOImpl::updateOutputDatiIuvCodiceAvviso] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::updateOutputDatiIuvCodiceAvviso] END");
		}

		return res;
	}

	@Override
	public Integer updateOutputDatiPecMail(Long idElabora, Integer progressivo, Long idOutputFoglio)
			throws DAOException {
		LOGGER.debug("[OutputDatiDAOImpl::updateOutputDatiPecMail] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("progressivo", progressivo);
			map.put("idOutputFoglio", idOutputFoglio);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_FLAG_PEC_MAIL, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[OutputDatiDAOImpl::updateOutputDatiPecMail] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::updateOutputDatiPecMail] END");
		}

		return res;
	}

	@Override
	public Integer updateOutputDatiIdAccertamento(Long idElabora, Integer progressivo, Long idOutputFoglio,
			Long idAccertamento, boolean isFoglioOds) {
		LOGGER.debug("[OutputDatiDAOImpl::updateOutputDatiIdAccertamento] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("progressivo", progressivo);
			map.put("idOutputFoglio", idOutputFoglio);
			map.put("idAccertamento", idAccertamento);

			String query = isFoglioOds ? QUERY_UPDATE_ID_ACCERTAMENTO_ODS : QUERY_UPDATE_ID_ACCERTAMENTO_TXT;

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(query, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[OutputDatiDAOImpl::updateOutputDatiIdAccertamento] ERROR : " + e);
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::updateOutputDatiIdAccertamento] END");
		}

		return res;
	}

	@Override
	public Integer updateOutputDatiCodiceAvviso(Long idElabora, Long idOutputFoglio, String idAccertamento,
			String codiceAvviso, boolean isFoglioOds) {
		LOGGER.debug("[OutputDatiDAOImpl::updateOutputDatiCodiceAvvisoOds] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("idOutputFoglio", idOutputFoglio);
			map.put("idAccertamento", idAccertamento);
			map.put("codiceAvviso", codiceAvviso);

			String query = isFoglioOds ? QUERY_UPDATE_CODICE_AVVISO_ODS : QUERY_UPDATE_CODICE_AVVISO_TXT;

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(query, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[OutputDatiDAOImpl::updateOutputDatiCodiceAvvisoOds] ERROR : " + e);
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::updateOutputDatiCodiceAvvisoOds] END");
		}

		return res;
	}

	@Override
	public Integer loadProgressivoTitolareTxt(Long idElabora, Long idOutputFoglio, String soggettoGruppo) {
		LOGGER.debug("[OutputDatiDAOImpl::loadProgressivoTitolareTxt] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("idOutputFoglio", idOutputFoglio);
			map.put("soggettoGruppo", soggettoGruppo);
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_LOAD_PROGRESSIVO_TITOLARE_TXT;

			return template.query(query, params, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					Integer progressivo = null;
					while (rs.next()) {
						progressivo = rs.getInt("progressivo");
					}
					return progressivo;
				}
			});
		} catch (Exception e) {
			LOGGER.error("[OutputDatiDAOImpl::loadProgressivoTitolareTxt] Errore nell'esecuzione della query", e);
			return null;
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::loadProgressivoTitolareTxt] END");
		}
	}

	@Override
	public List<OutputDatiDTO> loadOutputDatiByTipoRecord(Long idElabora, Long idOutputFoglio, String soggettoGruppo,
			String tipoRecord) {
		LOGGER.debug("[OutputDatiDAOImpl::loadOutputDatiByTipoRecord] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("idOutputFoglio", idOutputFoglio);
			map.put("soggettoGruppo", soggettoGruppo);
			map.put("tipoRecord", tipoRecord);
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_LOAD_OUTPUT_DATI_TXT_BY_TIPO_RECORD;
			switch (tipoRecord) {
			case "3":
				query = query + ORDER_BY_TIPO_REC_3;
				break;
			case "4":
				query = query + ORDER_BY_TIPO_REC_3;
				break;
			case "6":
				query = query + ORDER_BY_TIPO_REC_3;
				break;
			default:
				break;
			}
			return template.query(getQuery(query, null, null), params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error("[OutputDatiDAOImpl::loadOutputDatiByTipoRecord] Errore nell'esecuzione della query", e);
			return null;
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::loadOutputDatiByTipoRecord] END");
		}
	}

	@Override
	public Integer updateOutputDatiProgressivo(OutputDatiDTO dto, Integer progressivo) {
		LOGGER.debug("[OutputDatiDAOImpl::updateOutputDatiProgressivo] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("progressivo", progressivo);
			map.put("idElabora", dto.getIdElabora());
			map.put("idOutputFoglio", dto.getIdOutputFoglio());
			map.put("progressivoOld", dto.getProgressivo());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_PROGRESSIVO_TXT, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[OutputDatiDAOImpl::updateOutputDatiProgressivo] ERROR : " + e);
		} finally {
			LOGGER.debug("[OutputDatiDAOImpl::updateOutputDatiProgressivo] END");
		}

		return res;
	}

	private void populateMap(OutputDatiDTO dto, Map<String, Object> map) {
		map.put("idElabora", dto.getIdElabora());
		map.put("progressivo", dto.getProgressivo());
		map.put("idOutputFoglio", dto.getIdOutputFoglio());
		map.put("codTipoPassoElabora", dto.getCodTipoPassoElabora());
		map.put("valoreColonna1", dto.getValoreColonna1());
		map.put("valoreColonna2", dto.getValoreColonna2());
		map.put("valoreColonna3", dto.getValoreColonna3());
		map.put("valoreColonna4", dto.getValoreColonna4());
		map.put("valoreColonna5", dto.getValoreColonna5());
		map.put("valoreColonna6", dto.getValoreColonna6());
		map.put("valoreColonna7", dto.getValoreColonna7());
		map.put("valoreColonna8", dto.getValoreColonna8());
		map.put("valoreColonna9", dto.getValoreColonna9());
		map.put("valoreColonna10", dto.getValoreColonna10());
		map.put("valoreColonna11", dto.getValoreColonna11());
		map.put("valoreColonna12", dto.getValoreColonna12());
		map.put("valoreColonna13", dto.getValoreColonna13());
		map.put("valoreColonna14", dto.getValoreColonna14());
		map.put("valoreColonna15", dto.getValoreColonna15());
		map.put("valoreColonna16", dto.getValoreColonna16());
		map.put("valoreColonna17", dto.getValoreColonna17());
		map.put("valoreColonna18", dto.getValoreColonna18());
		map.put("valoreColonna19", dto.getValoreColonna19());
		map.put("valoreColonna20", dto.getValoreColonna20());
		map.put("valoreColonna21", dto.getValoreColonna21());
		map.put("valoreColonna22", dto.getValoreColonna22());
		map.put("valoreColonna23", dto.getValoreColonna23());
		map.put("valoreColonna24", dto.getValoreColonna24());
		map.put("valoreColonna25", dto.getValoreColonna25());
		map.put("valoreColonna26", dto.getValoreColonna26());
		map.put("valoreColonna27", dto.getValoreColonna27());
		map.put("valoreColonna28", dto.getValoreColonna28());
		map.put("valoreColonna29", dto.getValoreColonna29());
		map.put("valoreColonna30", dto.getValoreColonna30());
		map.put("valoreColonna31", dto.getValoreColonna31());
		map.put("valoreColonna32", dto.getValoreColonna32());
		map.put("valoreColonna33", dto.getValoreColonna33());
		map.put("valoreColonna34", dto.getValoreColonna34());
		map.put("valoreColonna35", dto.getValoreColonna35());
		map.put("valoreColonna36", dto.getValoreColonna36());
		map.put("valoreColonna37", dto.getValoreColonna37());
		map.put("valoreColonna38", dto.getValoreColonna38());
		map.put("valoreColonna39", dto.getValoreColonna39());
		map.put("valoreColonna40", dto.getValoreColonna40());
		map.put("valoreColonna41", dto.getValoreColonna41());
		map.put("valoreColonna42", dto.getValoreColonna42());
		map.put("valoreColonna43", dto.getValoreColonna43());
		map.put("valoreColonna44", dto.getValoreColonna44());
		map.put("valoreColonna45", dto.getValoreColonna45());
		map.put("valoreColonna46", dto.getValoreColonna46());
		map.put("valoreColonna47", dto.getValoreColonna47());
		map.put("valoreColonna48", dto.getValoreColonna48());
		map.put("valoreColonna49", dto.getValoreColonna49());
		map.put("valoreColonna50", dto.getValoreColonna50());
		map.put("valoreColonna51", dto.getValoreColonna51());
		map.put("valoreColonna52", dto.getValoreColonna52());
		map.put("valoreColonna53", dto.getValoreColonna53());
		map.put("valoreColonna54", dto.getValoreColonna54());
		map.put("valoreColonna55", dto.getValoreColonna55());
		map.put("valoreColonna56", dto.getValoreColonna56());
		map.put("valoreColonna57", dto.getValoreColonna57());
		map.put("valoreColonna58", dto.getValoreColonna58());
		map.put("valoreColonna59", dto.getValoreColonna59());
		map.put("valoreColonna60", dto.getValoreColonna60());
		map.put("valoreColonna61", dto.getValoreColonna61());
		map.put("valoreColonna62", dto.getValoreColonna62());
		map.put("valoreColonna63", dto.getValoreColonna63());
		map.put("valoreColonna64", dto.getValoreColonna64());
		map.put("valoreColonna65", dto.getValoreColonna65());
		map.put("valoreColonna66", dto.getValoreColonna66());
		map.put("valoreColonna67", dto.getValoreColonna67());
		map.put("valoreColonna68", dto.getValoreColonna68());
		map.put("valoreColonna69", dto.getValoreColonna69());
		map.put("valoreColonna70", dto.getValoreColonna70());
		map.put("valoreColonna71", dto.getValoreColonna71());
		map.put("valoreColonna72", dto.getValoreColonna72());
		map.put("valoreColonna73", dto.getValoreColonna73());
		map.put("valoreColonna74", dto.getValoreColonna74());
		map.put("valoreColonna75", dto.getValoreColonna75());
		map.put("valoreColonna76", dto.getValoreColonna76());
		map.put("valoreColonna77", dto.getValoreColonna77());
		map.put("valoreColonna78", dto.getValoreColonna78());
		map.put("valoreColonna79", dto.getValoreColonna79());
		map.put("valoreColonna80", dto.getValoreColonna80());
		map.put("valoreColonna81", dto.getValoreColonna81());
		map.put("valoreColonna82", dto.getValoreColonna82());
		map.put("valoreColonna83", dto.getValoreColonna83());
		map.put("valoreColonna84", dto.getValoreColonna84());
		map.put("valoreColonna85", dto.getValoreColonna85());
		map.put("valoreColonna86", dto.getValoreColonna86());
		map.put("valoreColonna87", dto.getValoreColonna87());
		map.put("valoreColonna88", dto.getValoreColonna88());
		map.put("valoreColonna89", dto.getValoreColonna89());
		map.put("valoreColonna90", dto.getValoreColonna90());
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
	public RowMapper<OutputDatiDTO> getRowMapper() throws SQLException {
		return new OutputDatiRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<OutputDatiDTO> getExtendedRowMapper() throws SQLException {
		return new OutputDatiRowMapper();
	}

	/**
	 * The type Configurazione row mapper.
	 */
	public static class OutputDatiRowMapper implements RowMapper<OutputDatiDTO> {

		/**
		 * Instantiates a new Elabora row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public OutputDatiRowMapper() throws SQLException {
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
		public OutputDatiDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			OutputDatiDTO bean = new OutputDatiDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, OutputDatiDTO bean) throws SQLException {
			bean.setIdElabora(rs.getLong("id_elabora"));
			bean.setProgressivo(rs.getInt("progressivo"));
			bean.setIdOutputFoglio(rs.getLong("id_output_foglio"));
			if (rsHasColumn(rs, "cod_tipo_passo_elabora"))
				bean.setCodTipoPassoElabora(rs.getString("cod_tipo_passo_elabora"));
			bean.setValoreColonna1(rs.getString("valore_colonna1"));
			bean.setValoreColonna2(rs.getString("valore_colonna2"));
			bean.setValoreColonna3(rs.getString("valore_colonna3"));
			bean.setValoreColonna4(rs.getString("valore_colonna4"));
			bean.setValoreColonna5(rs.getString("valore_colonna5"));
			bean.setValoreColonna6(rs.getString("valore_colonna6"));
			bean.setValoreColonna7(rs.getString("valore_colonna7"));
			bean.setValoreColonna8(rs.getString("valore_colonna8"));
			bean.setValoreColonna9(rs.getString("valore_colonna9"));
			bean.setValoreColonna10(rs.getString("valore_colonna10"));
			bean.setValoreColonna11(rs.getString("valore_colonna11"));
			bean.setValoreColonna12(rs.getString("valore_colonna12"));
			bean.setValoreColonna13(rs.getString("valore_colonna13"));
			bean.setValoreColonna14(rs.getString("valore_colonna14"));
			bean.setValoreColonna15(rs.getString("valore_colonna15"));
			bean.setValoreColonna16(rs.getString("valore_colonna16"));
			bean.setValoreColonna17(rs.getString("valore_colonna17"));
			bean.setValoreColonna18(rs.getString("valore_colonna18"));
			bean.setValoreColonna19(rs.getString("valore_colonna19"));
			bean.setValoreColonna20(rs.getString("valore_colonna20"));
			bean.setValoreColonna21(rs.getString("valore_colonna21"));
			bean.setValoreColonna22(rs.getString("valore_colonna22"));
			bean.setValoreColonna23(rs.getString("valore_colonna23"));
			bean.setValoreColonna24(rs.getString("valore_colonna24"));
			bean.setValoreColonna25(rs.getString("valore_colonna25"));
			bean.setValoreColonna26(rs.getString("valore_colonna26"));
			bean.setValoreColonna27(rs.getString("valore_colonna27"));
			bean.setValoreColonna28(rs.getString("valore_colonna28"));
			bean.setValoreColonna29(rs.getString("valore_colonna29"));
			bean.setValoreColonna30(rs.getString("valore_colonna30"));
			bean.setValoreColonna31(rs.getString("valore_colonna31"));
			bean.setValoreColonna32(rs.getString("valore_colonna32"));
			bean.setValoreColonna33(rs.getString("valore_colonna33"));
			bean.setValoreColonna34(rs.getString("valore_colonna34"));
			bean.setValoreColonna35(rs.getString("valore_colonna35"));
			bean.setValoreColonna36(rs.getString("valore_colonna36"));
			bean.setValoreColonna37(rs.getString("valore_colonna37"));
			bean.setValoreColonna38(rs.getString("valore_colonna38"));
			bean.setValoreColonna39(rs.getString("valore_colonna39"));
			bean.setValoreColonna40(rs.getString("valore_colonna40"));
			bean.setValoreColonna41(rs.getString("valore_colonna41"));
			bean.setValoreColonna42(rs.getString("valore_colonna42"));
			bean.setValoreColonna43(rs.getString("valore_colonna43"));
			bean.setValoreColonna44(rs.getString("valore_colonna44"));
			bean.setValoreColonna45(rs.getString("valore_colonna45"));
			bean.setValoreColonna46(rs.getString("valore_colonna46"));
			bean.setValoreColonna47(rs.getString("valore_colonna47"));
			bean.setValoreColonna48(rs.getString("valore_colonna48"));
			bean.setValoreColonna49(rs.getString("valore_colonna49"));
			bean.setValoreColonna50(rs.getString("valore_colonna50"));
			bean.setValoreColonna51(rs.getString("valore_colonna51"));
			bean.setValoreColonna52(rs.getString("valore_colonna52"));
			bean.setValoreColonna53(rs.getString("valore_colonna53"));
			bean.setValoreColonna54(rs.getString("valore_colonna54"));
			bean.setValoreColonna55(rs.getString("valore_colonna55"));
			bean.setValoreColonna56(rs.getString("valore_colonna56"));
			bean.setValoreColonna57(rs.getString("valore_colonna57"));
			bean.setValoreColonna58(rs.getString("valore_colonna58"));
			bean.setValoreColonna59(rs.getString("valore_colonna59"));
			bean.setValoreColonna60(rs.getString("valore_colonna60"));
			bean.setValoreColonna61(rs.getString("valore_colonna61"));
			bean.setValoreColonna62(rs.getString("valore_colonna62"));
			bean.setValoreColonna63(rs.getString("valore_colonna63"));
			bean.setValoreColonna64(rs.getString("valore_colonna64"));
			bean.setValoreColonna65(rs.getString("valore_colonna65"));
			bean.setValoreColonna66(rs.getString("valore_colonna66"));
			bean.setValoreColonna67(rs.getString("valore_colonna67"));
			bean.setValoreColonna68(rs.getString("valore_colonna68"));
			bean.setValoreColonna69(rs.getString("valore_colonna69"));
			bean.setValoreColonna70(rs.getString("valore_colonna70"));
			bean.setValoreColonna71(rs.getString("valore_colonna71"));
			bean.setValoreColonna72(rs.getString("valore_colonna72"));
			bean.setValoreColonna73(rs.getString("valore_colonna73"));
			bean.setValoreColonna74(rs.getString("valore_colonna74"));
			bean.setValoreColonna75(rs.getString("valore_colonna75"));
			bean.setValoreColonna76(rs.getString("valore_colonna76"));
			bean.setValoreColonna77(rs.getString("valore_colonna77"));
			bean.setValoreColonna78(rs.getString("valore_colonna78"));
			bean.setValoreColonna79(rs.getString("valore_colonna79"));
			bean.setValoreColonna80(rs.getString("valore_colonna80"));
			bean.setValoreColonna81(rs.getString("valore_colonna81"));
			bean.setValoreColonna82(rs.getString("valore_colonna82"));
			bean.setValoreColonna83(rs.getString("valore_colonna83"));
			bean.setValoreColonna84(rs.getString("valore_colonna84"));
			bean.setValoreColonna85(rs.getString("valore_colonna85"));
			bean.setValoreColonna86(rs.getString("valore_colonna86"));
			bean.setValoreColonna87(rs.getString("valore_colonna87"));
			bean.setValoreColonna88(rs.getString("valore_colonna88"));
			bean.setValoreColonna89(rs.getString("valore_colonna89"));
			bean.setValoreColonna90(rs.getString("valore_colonna90"));
			bean.setFlgInvioPecEmail(rs.getInt("flg_invio_pec_email"));
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
	}

	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	public static class NomeLottoRowMapper implements RowMapper<String> {

		/**
		 * Instantiates a new NomeLottoRowMapper row mapper.
		 */
		public NomeLottoRowMapper() {
			// Instatiate class
		}

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("valore_colonna89");
		}
	}

	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	public static class PagopaPosizioniDebitorieRowMapper implements RowMapper<PagopaPosizioniDebitorieDTO> {

		/**
		 * Instantiates a new NomeLottoRowMapper row mapper.
		 */
		public PagopaPosizioniDebitorieRowMapper() {
			// Instatiate class
		}

		@Override
		public PagopaPosizioniDebitorieDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			PagopaPosizioniDebitorieDTO bean = new PagopaPosizioniDebitorieDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, PagopaPosizioniDebitorieDTO bean) throws SQLException {
			try {
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				bean.setNomeLotto(rs.getString("nome_lotto"));
				bean.setNap(rs.getString("nap"));
				bean.setAnno(rs.getString("anno"));
				bean.setImporto(rs.getBigDecimal("importo"));
				bean.setDataScadenza(df.parse(rs.getString("data_scadenza")));
				bean.setDataInizioValidita(df.parse(rs.getString("data_inizio_validita")));
				bean.setDataFineValidita(df.parse(rs.getString("data_fine_validita")));
				bean.setCausale(rs.getString("causale"));
				bean.setTipoSoggetto(rs.getString("tipo_soggetto"));
				bean.setRagioneSociale(rs.getString("ragione_sociale"));
				bean.setCognome(rs.getString("cognome"));
				bean.setNome(rs.getString("nome"));
				bean.setCodFiscale(rs.getString("cod_fiscale"));
				bean.setNote(rs.getString("note"));
			} catch (Exception e) {
				throw new SQLException(e);
			}
		}

	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}