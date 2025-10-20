/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.tributi;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ComuneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.NazioneDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ProvinciaExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.RegioneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AreaCompetenzaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.GruppiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ProvvedimentiIstanzeDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.TipoRiscossioneDAOImpl;
import it.csi.risca.riscabesrv.dto.AreaCompetenzaExtendedDTO;
import it.csi.risca.riscabesrv.dto.GruppiDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.ProvvedimentoDTO;
import it.csi.risca.riscabesrv.dto.ProvvedimentoSearchDTO;
import it.csi.risca.riscabesrv.dto.RecapitiDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneRecapitoDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchResultDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoIstanzaDTO;
import it.csi.risca.riscabesrv.dto.TipoRiscossioneExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.tributi.DatiTecniciTributiDTO;
import it.csi.risca.riscabesrv.dto.tributi.TipoUsoDatoTecnicoTributiDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

public class RiscossioneTributiDAOImpl extends RiscaBeSrvGenericDAO<RiscossioneDTO> {

	private final String className = this.getClass().getSimpleName();

	
	@Autowired
	private TipoUsoDAO tipoUsoDao;

	@Autowired
	private GruppiDAO gruppiDAO;
	@Autowired
	private MessaggiDAO messaggiDAO;
	@Autowired
	private TipoRiscossioneDAOImpl tipoRiscossioneDaoImpl;
	
    @Autowired
    private AreaCompetenzaDAO areaCompetenzaDAO;
    
    @Autowired
    private ProvvedimentiIstanzeDAO provvedimentiIstanzeDAO;

	private static final String QUERY_INSERT_RISCOSSIONE = "INSERT INTO risca_t_riscossione (id_riscossione, id_tipo_riscossione, id_componente_dt, id_stato_riscossione, id_soggetto, "
			+ "id_gruppo_soggetto, id_tipo_autorizza, cod_riscossione, cod_riscossione_prov, cod_riscossione_prog, id_sigla_riscossione, cod_riscossione_lettera_prov, num_pratica, flg_prenotata, motivo_prenotazione, note_riscossione, "
			+ "data_ini_concessione, data_scad_concessione, data_ini_sosp_canone, data_fine_sosp_canone, json_dt, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid, data_inizio_titolarita, data_rinuncia_revoca ) "
			+ "VALUES(nextval('seq_risca_t_riscossione'), :idTipoRiscossione, :idComponenteDt, :idStatoRiscossione, :idSoggetto, :idGruppoSoggetto, :idTipoAutorizza, :codRiscossione, :codRiscossioneProv, :codRiscossioneProg, :idSiglaRiscossione, "
			+ ":codRiscossioneLetteraProv, :numPratica, :flgPrenotata, :motivoPrenotazione, :noteRiscossione, :dataIniConcessione, :dataScadConcessione, :dataIniSospCanone, :dataFineSospCanone, :jsonDt, :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid, :dataInizioTitolarita, :dataRinunciaRevoca)";
	
	private static final String QUERY_UPDATE_RISCOSSIONE = "UPDATE risca_t_riscossione "
			+ "SET id_tipo_riscossione = :idTipoRiscossione, id_componente_dt = :idComponenteDt, id_stato_riscossione = :idStatoRiscossione, id_soggetto = :idSoggetto, id_gruppo_soggetto = :idGruppoSoggetto, id_tipo_autorizza = :idTipoAutorizza, cod_riscossione = :codRiscossione, cod_riscossione_prov = :codRiscossioneProv, cod_riscossione_prog = :codRiscossioneProg, id_sigla_riscossione = :idSiglaRiscossione, "
			+ "cod_riscossione_lettera_prov = :codRiscossioneLetteraProv, num_pratica = :numPratica, flg_prenotata= :flgPrenotata, motivo_prenotazione = :motivoPrenotazione, note_riscossione = :noteRiscossione, data_ini_concessione = :dataIniConcessione, data_scad_concessione = :dataScadConcessione, data_ini_sosp_canone = :dataIniSospCanone, data_fine_sosp_canone = :dataFineSospCanone, json_dt = :jsonDt,  gest_data_upd= :gestDataUpd, gest_attore_upd= :gestAttoreUpd, gest_uid = :gestUid,"
			+ " data_inizio_titolarita = :dataInizioTitolarita, data_rinuncia_revoca = :dataRinunciaRevoca  " + "WHERE id_riscossione = :idRiscossione";

	private static final String QUERY_INSERT_PROVVEDIMENTO = "INSERT INTO risca_r_provvedimento (id_provvedimento, id_riscossione, id_tipo_titolo, id_tipo_provvedimento, num_titolo, data_provvedimento, "
			+ "note, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "VALUES(nextval('seq_risca_r_provvedimento'), :idRiscossione, :idTipoTitolo, :idTipoProvvedimento, :numTitolo, :dataProvvedimento, :note, "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";

	private static final String QUERY_INSERT_RISCOSSIONE_RECAPITO = "INSERT INTO risca_r_riscossione_recapito (id_riscossione, id_recapito, "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "VALUES(:idRiscossione, :idRecapito, "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";

	private static final String QUERY_RECAPITI_BY_ID_RECAPITO = "SELECT rrr.* FROM risca_r_recapito rrr "
			+ "WHERE rrr.id_recapito = :idRecapito " + "AND rrr.data_cancellazione is null";

	private static final String QUERY_CHECK_NUM_PRATICA = "SELECT rtr.* FROM risca_t_riscossione rtr where rtr.num_pratica = :numPratica "
			+ "and rtr.id_tipo_riscossione = :idTipoRiscossione ";

	private static final String QUERY_CHECK_COD_UTENZA = "SELECT rtr.* FROM risca_t_riscossione rtr where rtr.cod_riscossione = :codRiscossione "
			+ "and rtr.id_tipo_riscossione = :idTipoRiscossione ";

	
	private static final String QUERY_MAX_PROG_TIPO_RISCO_1_2 =   " AND to_number(rtr.cod_riscossione_prog, '99999') BETWEEN :minValue AND :maxValue";
	private static final String QUERY_MAX_PROG_TIPO_RISCO_3_4 =   " AND to_number(rtr.cod_riscossione_prog, '99999') >= :maxValue";
	private static final String QUERY_MAX_PROG_UTENZA = "SELECT"
										+ " lpad(to_char(max(to_number(rtr.cod_riscossione_prog, '99999')) + 1), 5, '0') as nuovo_progressivo"
										+ " FROM risca_t_riscossione rtr"
									    + " WHERE"
									    + " rtr.cod_riscossione_prov = :codRiscossioneProv"
									    + " AND rtr.id_tipo_riscossione in ( :idTipoRiscossione)";

	private static final String QUERY_DELETE_RISCOSSIONE_RECAPITO = "DELETE FROM risca_r_riscossione_recapito rrrr WHERE rrrr.id_riscossione = :idRiscossione "
			+ "AND rrrr.id_recapito = :idRecapitoDEL";

	private static final String QUERY_CHECK_RISCOSSIONE_RECAPITI = "SELECT rrrr.* from risca_r_riscossione_recapito rrrr "
			+ "INNER JOIN risca_r_recapito rrr ON rrrr.id_recapito = rrr.id_recapito "
			+ "WHERE rrrr.id_riscossione = :idRiscossione ";

	private static final String QUERY_JOIN_RISCOSSIONI = " inner join risca_t_soggetto rts on rtr.id_soggetto = rts.id_soggetto  "
			+ " left join risca_d_tipo_soggetto rdts on rts.id_tipo_soggetto = rdts.id_tipo_soggetto  "
			+ " left join risca_d_tipo_autorizza rdta on  rtr.id_tipo_autorizza = rdta.id_tipo_autorizza  "
			+ " left join risca_d_tipo_riscossione dtr on rtr.id_tipo_riscossione = dtr.id_tipo_riscossione"
			+ " left join risca_d_stato_riscossione rdsr on rtr.id_stato_riscossione = rdsr.id_stato_riscossione  "
			+ " left join risca_t_gruppo_soggetto rtgs on rtr.id_gruppo_soggetto = rtgs.id_gruppo_soggetto "
			+ " left join risca_r_provvedimento rrp  on rtr.id_riscossione = rrp.id_riscossione  "
			+ "  left join risca_d_tipo_provvedimento rdtp   on rrp.id_tipo_provvedimento  = rdtp.id_tipo_provvedimento  ";

	private static final String QUERY_SEARCH_RISCOSSIONI = " SELECT  rtr.cod_riscossione as cod_riscossione, "
			+ "   rtr.id_riscossione as id_riscossione, "
			+ "   rtr.num_pratica as num_pratica , "
			+ "   rdta.id_ambito, "
			+ "   rtr.data_scad_concessione,"
			+ "   rdta.des_tipo_autorizza as procedimento , "
			+ "  CASE \r\n"
			+ " WHEN rtgs.id_gruppo_soggetto is not null THEN rtgs.des_gruppo_soggetto \r\n"
			+ " WHEN ((rdts.cod_tipo_soggetto ='PF') and (rtgs.id_gruppo_soggetto is null) ) then ( rts.cognome|| ' ' ||  rts.nome ) \r\n"
			+ " WHEN (((rdts.cod_tipo_soggetto ='PG') or (rdts.cod_tipo_soggetto ='PB' )) and (rtgs.id_gruppo_soggetto is null) )THEN rts.den_soggetto \r\n"
			+ " END AS titolare,"
			+ "	rdsr.des_stato_riscossione , "
			+ "	rtr.json_dt as json_dt "
			+ "	FROM risca_t_riscossione rtr  " + QUERY_JOIN_RISCOSSIONI;
	
	private static final String QUERY_JOIN_SD = 	" inner join risca_t_soggetto rts on rtsd.id_soggetto = rts.id_soggetto  "
			+ " left join risca_d_tipo_soggetto rdts on rts.id_tipo_soggetto = rdts.id_tipo_soggetto  "
			+ " left join risca_d_tipo_autorizza rdta on  rtr.id_tipo_autorizza = rdta.id_tipo_autorizza  "
			+ " left join risca_d_stato_riscossione rdsr on rtr.id_stato_riscossione = rdsr.id_stato_riscossione  "
			+ " left join risca_t_gruppo_soggetto rtgs on rtr.id_gruppo_soggetto = rtgs.id_gruppo_soggetto "
			+ " left join risca_r_provvedimento rrp  on rtsd.id_riscossione = rrp.id_riscossione  "
			+ "  left join risca_d_tipo_provvedimento rdtp   on rrp.id_tipo_provvedimento  = rdtp.id_tipo_provvedimento  ";
	
	private static final String QUERY_SEARCH_RISCOSSIONI_BY_SD = "select distinct rtsd.id_riscossione from RISCA_T_STATO_DEBITORIO rtsd "
			+ "inner join risca_t_riscossione rtr on rtr.id_riscossione = rtsd.id_riscossione "
			+ QUERY_JOIN_SD;

	private static final String QUERY_COUNT_RISCOSSIONI = "SELECT COUNT(*) FROM ( " + QUERY_SEARCH_RISCOSSIONI;

	private static final String QUERY_GROUP_BY_SEARCH_RISCOSSIONI = "  group by rtr.cod_riscossione, rtr.id_riscossione, rtr.num_pratica , rdta.des_tipo_autorizza, rdsr.des_stato_riscossione  "
			+ "  ,rtr.json_dt "
			+ "  , rdts.cod_tipo_soggetto, rtgs.id_gruppo_soggetto,rts.den_soggetto,rts.cognome, " + "   rts.nome ";

	private static final String QUERY_LOAD_COMUNE = "SELECT distinct rdc.*, rdp.*, rdr.* , rdn.* FROM risca_d_comune rdc "
			+ "INNER JOIN risca_d_provincia rdp ON rdc.id_provincia = rdp.id_provincia "
			+ "INNER JOIN risca_d_regione rdr ON rdp.id_regione = rdr.id_regione  "
			+ "INNER JOIN risca_d_nazione rdn   ON rdr.id_nazione = rdn.id_nazione "
			+ "WHERE rdc.id_comune = :idComune ";

	public static final String QUERY_SELECT_ID_SOGGETTO_BY_ID_RISCOSSIONE = "	 SELECT rtr.id_soggetto  "
			+ " FROM risca_t_riscossione rtr" + " WHERE rtr.id_riscossione = :idRiscossione";

	public static final String QUERY_SELECT_DATA_INIZIO_TITOLARITA_BY_ID_RISCOSSIONE = "SELECT rtr.data_inizio_titolarita  "
			+ " FROM risca_t_riscossione rtr" + " WHERE rtr.id_riscossione = :idRiscossione";
	
	public static final String  QUERY_SELECT_PROVVEDIMENTO_IF_EXIST	="	 SELECT count(id_tipo_provvedimento) as provvedimento "
			 +"FROM risca_d_tipo_provvedimento where id_tipo_provvedimento =  :idTipoProvvedimento";
	

	
	public static final String  QUERY_SELECT_RISCOSSIONI_BY_CF_DELEGATO = " SELECT distinct dtr.id_ambito, \r\n"
			+ " ris.cod_riscossione as cod_riscossione, \r\n"
			+ " ris.id_riscossione as id_riscossione, \r\n"
			+ " ris.num_pratica as num_pratica,\r\n"
			+ " ris.data_scad_concessione,\r\n"
			+ " dta.des_tipo_autorizza as procedimento,\r\n"
			+ " CASE \r\n"
			+ " WHEN rtgs.id_gruppo_soggetto is not null THEN rtgs.des_gruppo_soggetto \r\n"
			+ " WHEN ((rdts.cod_tipo_soggetto ='PF') and (rtgs.id_gruppo_soggetto is null) ) then ( rts.cognome|| ' ' ||  rts.nome ) \r\n"
			+ " WHEN (((rdts.cod_tipo_soggetto ='PG') or (rdts.cod_tipo_soggetto ='PB' )) and (rtgs.id_gruppo_soggetto is null) )THEN rts.den_soggetto \r\n"
			+ " END AS titolare,"
			+ " dsr.des_stato_riscossione , \r\n"
			+ " ris.json_dt as json_dt "
			+ " FROM risca_t_riscossione ris\r\n"
			+ "  left join risca_d_tipo_riscossione dtr on ris.id_tipo_riscossione = dtr.id_tipo_riscossione\r\n"
			+ "  left join risca_d_tipo_autorizza dta on  ris.id_tipo_autorizza = dta.id_tipo_autorizza\r\n"
			+ "  left join risca_t_soggetto rts on ris.id_soggetto = rts.id_soggetto \r\n"
			+ "  left join risca_t_gruppo_soggetto rtgs on ris.id_gruppo_soggetto = rtgs.id_gruppo_soggetto\r\n"
			+ "  left join risca_d_tipo_soggetto rdts on rts.id_tipo_soggetto = rdts.id_tipo_soggetto\r\n"
			+ "  left join risca_d_stato_riscossione dsr on ris.id_stato_riscossione = dsr.id_stato_riscossione\r\n"
			+ " inner join ("
			+ " select lso.id_soggetto, null as id_gruppo_soggetto\r\n"
			+ " from risca_t_soggetto lso \r\n"
			+ " where lso.cf_soggetto = :cfDelegato "
			+ " UNION ALL\r\n"
			+ " SELECT rsd.id_soggetto, rgd.id_gruppo_soggetto\r\n"
			+ " FROM risca_t_delegato tde\r\n"
			+ " left join risca_r_soggetto_delega rsd on tde.id_delegato = rsd.id_delegato "
			+ " and ( rsd.data_disabilitazione is null or rsd.data_disabilitazione >= current_date )"
			+ " left join risca_r_gruppo_delega rgd on tde.id_delegato = rgd.id_delegato "
		 + "	and ( rgd.data_disabilitazione is null or rgd.data_disabilitazione >= current_date )"
			+ " where tde.cf_delegato = :cfDelegato"
			+ " ) validi on ( ris.id_soggetto = validi.id_soggetto\r\n"
			+ " or ris.id_gruppo_soggetto = validi.id_gruppo_soggetto)"
			+ " where dtr.id_ambito = :idAmbito";
	
	private static final String QUERY_COUNT_RISCOSSIONI_BY_CF_DELEGATO = "SELECT COUNT(*) FROM ( "+QUERY_SELECT_RISCOSSIONI_BY_CF_DELEGATO;
	
	public Long saveRiscossione(RiscossioneDTO dto, String fruitore) throws BusinessException, Exception {
		LOGGER.debug("[RiscossioneTributiDAOImpl::saveRiscossione] BEGIN");

//		int checkNumPratica =  checkNumPratica(dto.getNumPratica());
//		if (checkNumPratica > 0 )
//			throw new BusinessException(400, "E016","Attenzione numero pratica gia presente nel sistema");
		if (dto.getCodRiscossioneProg() != null) {
			int checkCodRiscossione = checkCodRiscossione(dto.getCodRiscossioneProv() + dto.getCodRiscossioneProg());
			if (checkCodRiscossione > 0) {
				messaggiDAO.loadMessaggiByCodMessaggio("E015");
				throw new BusinessException(400, "E015", "Attenzione codice utenza gia presente nel sistema");
			}

		}
		validateRiscossione(dto);

		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idTipoRiscossione", dto.getIdTipoRiscossione());
			map.put("idComponenteDt", dto.getIdComponenteDt());
			map.put("idStatoRiscossione", dto.getStatiRiscossione().getIdStatoRiscossione());
			map.put("idSoggetto", dto.getSoggetto().getIdSoggetto());
			// DP sposto if all'interno del ciclo per settare idComune
			// if(fruitore == null) {
			// DP aggiunta if !=null
			if (dto.getSoggetto().getRecapiti() != null) {
				for (int i = 0; i < dto.getSoggetto().getRecapiti().size(); i++) {
					if (dto.getSoggetto().getRecapiti().get(i).getComuneRecapito().getIdComune() != null) {
						map.put("idComune", dto.getSoggetto().getRecapiti().get(i).getComuneRecapito().getIdComune());
						params = getParameterValue(map);
						if (fruitore == null) {
							ComuneExtendedDTO comuneValido = template.queryForObject(QUERY_LOAD_COMUNE, params,
									getComuneRowMapper());
							if (comuneValido.getDataFineValidita() != null) {
								LOGGER.error("[SoggettiDAOImpl::saveSoggetto]: Comune non valido ");
								messaggiDAO.loadMessaggiByCodMessaggio("E085");
								throw new BusinessException(409, "E085",
										"Attenzione: il comune di uno o piu' recapiti non e' piu' valido. Prima di procedere e' necessario selezionare un comune valido.");

							}
						}
					}
				}
			}
			// }

			if (dto.getGruppoSoggetto() != null)
				map.put("idGruppoSoggetto", dto.getGruppoSoggetto().getIdGruppoSoggetto());
			else
				map.put("idGruppoSoggetto", null);
			map.put("idTipoAutorizza", dto.getTipoAutorizza().getIdTipoAutorizza());
			map.put("codRiscossioneProv", dto.getCodRiscossioneProv());

			String ambito = dto.getAmbito();
			Long idAmbito = 0L;
			if (ambito != null)
				idAmbito = Long.parseLong(ambito);
			if (dto.getCodRiscossioneProg() != null) {
				if (idAmbito.equals(4L)) {
					if(fruitore == null || (fruitore != null && !fruitore.equals(Constants.BATCH_PORTING)) ) {
						if (Integer.parseInt(dto.getCodRiscossioneProg()) > 9999 && dto.getIdTipoRiscossione() == 1) {
							messaggiDAO.loadMessaggiByCodMessaggio("E019");
							throw new BusinessException(400, "E019",
									"Attenzione il codice progressivo utenza per le pratiche Ordinarie deve essere compreso tra 1 e 9.999.",
									null);
						}

						if ((Integer.parseInt(dto.getCodRiscossioneProg()) < 10000
								|| Integer.parseInt(dto.getCodRiscossioneProg()) > 49999)
								&& dto.getIdTipoRiscossione() == 2) {
							messaggiDAO.loadMessaggiByCodMessaggio("E020");
							throw new BusinessException(400, "E020",
									"Attenzione il codice progressivo utenza per le pratiche Preferenziale deve essere compreso tra 10.000 e 49.999",
									null);
						}

						if (Integer.parseInt(dto.getCodRiscossioneProg()) < 50000
								&& (dto.getIdTipoRiscossione() == 3 || dto.getIdTipoRiscossione() == 4)) {
							messaggiDAO.loadMessaggiByCodMessaggio("E021");
							throw new BusinessException(400, "E021",
									"Attenzione il codice progressivo utenza per gli Attingimenti deve essere maggiore o uguale a 50.000",
									null);
						}

					}
					map.put("codRiscossione", dto.getCodRiscossioneProv() + dto.getCodRiscossioneProg());
				}

				map.put("codRiscossioneProg", dto.getCodRiscossioneProg());

			} else {
				String codRiscoss = null;
				Map<String, Object> mapCodRisc = new HashMap<>();
				mapCodRisc.put("codRiscossioneProv", dto.getCodRiscossioneProv());
				if (dto.getIdTipoRiscossione() == 1) {
					mapCodRisc.put("idTipoRiscossione", dto.getIdTipoRiscossione());
					mapCodRisc.put("maxValue", 9999);
					mapCodRisc.put("minValue", 0);
					MapSqlParameterSource param = getParameterValue(mapCodRisc);
					codRiscoss = template.queryForObject(QUERY_MAX_PROG_UTENZA +QUERY_MAX_PROG_TIPO_RISCO_1_2 , param, String.class);
					if(codRiscoss == null)
						codRiscoss = "1";
				} else if (dto.getIdTipoRiscossione() == 2) {
					mapCodRisc.put("idTipoRiscossione", dto.getIdTipoRiscossione());
					mapCodRisc.put("maxValue", 49999);
					mapCodRisc.put("minValue", 10000);
					MapSqlParameterSource param = getParameterValue(mapCodRisc);
					codRiscoss = template.queryForObject(QUERY_MAX_PROG_UTENZA +QUERY_MAX_PROG_TIPO_RISCO_1_2 , param, String.class);
					if(codRiscoss == null)
						codRiscoss = "10000";
				} else if (dto.getIdTipoRiscossione() == 3 || dto.getIdTipoRiscossione() == 4) {
					List<Integer> listIdTipoRiscossione = new ArrayList<Integer>();
					listIdTipoRiscossione.add(3);
					listIdTipoRiscossione.add(4);
					mapCodRisc.put("idTipoRiscossione", listIdTipoRiscossione);
					mapCodRisc.put("maxValue", 50000);
					MapSqlParameterSource param = getParameterValue(mapCodRisc);
					codRiscoss = template.queryForObject(QUERY_MAX_PROG_UTENZA +QUERY_MAX_PROG_TIPO_RISCO_3_4 , param, String.class);
					if(codRiscoss == null)
						codRiscoss = "50000";
				}

				if (Integer.parseInt(codRiscoss) > 9999 && dto.getIdTipoRiscossione() == 1) {
					messaggiDAO.loadMessaggiByCodMessaggio("E045");
					throw new BusinessException(400, "E045",
							"Attenzione: per il tipo di riscossione indicato e stato raggiunto il limite previsto per il valore del progressivo utenza. Non e possibile salvare la riscossione.",
							null);

				}

				if ((Integer.parseInt(codRiscoss) < 10000 || Integer.parseInt(codRiscoss) > 49999)
						&& dto.getIdTipoRiscossione() == 2) {
					messaggiDAO.loadMessaggiByCodMessaggio("E045");
					throw new BusinessException(400, "E045",
							"Attenzione: per il tipo di riscossione indicato e stato raggiunto il limite previsto per il valore del progressivo utenza. Non e possibile salvare la riscossione.",
							null);

				}

				if (Integer.parseInt(codRiscoss) < 50000
						&& (dto.getIdTipoRiscossione() == 3 || dto.getIdTipoRiscossione() == 4)) {
					messaggiDAO.loadMessaggiByCodMessaggio("E045");
					throw new BusinessException(400, "E045",
							"Attenzione: per il tipo di riscossione indicato e stato raggiunto il limite previsto per il valore del progressivo utenza. Non e possibile salvare la riscossione.",
							null);

				}

				map.put("codRiscossione", dto.getCodRiscossioneProv() + codRiscoss);
				map.put("codRiscossioneProg", codRiscoss);
				dto.setCodRiscossioneProg(codRiscoss);
			}

			map.put("idSiglaRiscossione", dto.getIdSiglaRiscossione());
			map.put("codRiscossioneLetteraProv", dto.getCodRiscossioneLetteraProv());

			if (!dto.getNumPratica().equals(""))
				map.put("numPratica", dto.getNumPratica());

			else
				map.put("numPratica", "");

			map.put("flgPrenotata", dto.getFlgPrenotata());
			map.put("motivoPrenotazione", dto.getMotivoPrenotazione());
			map.put("noteRiscossione", dto.getNoteRiscossione());

			try {
				String dataIniConcessione = dto.getDataIniConcessione();
				if (dataIniConcessione != null) {
					if (!dataIniConcessione.equals("")) {

						Date dataInConc = formatter.parse(dataIniConcessione);
						map.put("dataIniConcessione", dataInConc);
					} else {
						map.put("dataIniConcessione", null);
					}
				} else {
					map.put("dataIniConcessione", null);
				}

				String dataScadConcessione = dto.getDataScadConcessione();
				if (dataScadConcessione != null) {
					if (!dataScadConcessione.equals("")) {
						Date dataScadConc = formatter.parse(dataScadConcessione);
						map.put("dataScadConcessione", dataScadConc);
					} else {
						map.put("dataScadConcessione", null);
					}
				} else {
					map.put("dataScadConcessione", null);
				}

				String dataIniSospCanone = dto.getDataIniSospCanone();
				if (dataIniSospCanone != null) {
					if (!dataIniSospCanone.equals("")) {
						Date dataIniSospCan = formatter.parse(dataIniSospCanone);
						map.put("dataIniSospCanone", dataIniSospCan);
					} else {
						map.put("dataIniSospCanone", null);
					}
				} else {
					map.put("dataIniSospCanone", null);
				}

				String dataFineSospCanone = dto.getDataFineSospCanone();
				if (dataFineSospCanone != null) {
					if (!dataFineSospCanone.equals("")) {

						Date dataFineSospCan = formatter.parse(dataFineSospCanone);
						map.put("dataFineSospCanone", dataFineSospCan);
					} else {
						map.put("dataFineSospCanone", null);
					}
				} else {
					map.put("dataFineSospCanone", null);
				}
				
				String dataRinunciaRevocaST = dto.getDataRinunciaRevoca();
				if (dataRinunciaRevocaST != null) {
					if (!dataRinunciaRevocaST.equals("")) {

						Date dataRinunciaRevoca = formatter.parse(dataRinunciaRevocaST);
						map.put("dataRinunciaRevoca", dataRinunciaRevoca);
					} else {
						map.put("dataRinunciaRevoca", null);
					}
				} else {
					map.put("dataRinunciaRevoca", null);
				}
				
			} catch (ParseException e) {
				LOGGER.error("[RiscossioneTributiDAOImpl::saveRiscossione] Errore nel mapping dell'oggetto ", e);
			}
			Date dataInizioTitolarita = formatter.parse(formatter.format(now));
			map.put("dataInizioTitolarita", dataInizioTitolarita);
			dto.setDataInizioTitolarita(formatter.format(now));
			map.put("jsonDt", dto.getJsonDt());
			map.put("gestDataIns", now);
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
//            String uid = generateGestUID(dto.getCfAccredito() + dto.getDesEmailAccredito() + now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT_RISCOSSIONE, null, null), params, keyHolder,
					new String[] { "id_riscossione" });
			Number key = keyHolder.getKey();

			int sizeArrayProvv = dto.getProvvedimento().size();

			for (int i = 0; i < sizeArrayProvv; i++) {
				map.put("idRiscossione", key.longValue());
				map.put("idTipoTitolo",
						dto.getProvvedimento().get(i).getTipoTitoloExtendedDTO() != null
								? dto.getProvvedimento().get(i).getTipoTitoloExtendedDTO().getIdTipoTitolo()
								: null);
				map.put("idTipoProvvedimento",
						dto.getProvvedimento().get(i).getTipiProvvedimentoExtendedDTO().getIdTipoProvvedimento());
				map.put("numTitolo", dto.getProvvedimento().get(i).getNumTitolo());

				String dataProvvedimento = dto.getProvvedimento().get(i).getDataProvvedimento();
				if (dataProvvedimento != null) {
					if (!dataProvvedimento.equals("")) {
						Date dataProvv = formatter.parse(dataProvvedimento);
						map.put("dataProvvedimento", dataProvv);
					} else {
						map.put("dataProvvedimento", null);
					}
				} else {
					map.put("dataProvvedimento", null);
				}

				map.put("note", dto.getProvvedimento().get(i).getNote());
				map.put("gestDataIns", now);
				map.put("gestAttoreIns", dto.getGestAttoreIns());
				map.put("gestDataUpd", now);
				map.put("gestAttoreUpd", dto.getGestAttoreUpd());
				// String uid = generateGestUID(dto.getCfAccredito() +
				// dto.getDesEmailAccredito() + now);
				map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));

				MapSqlParameterSource paramsProvv = getParameterValue(map);
				KeyHolder keyHolderProvv = new GeneratedKeyHolder();

				template.update(getQuery(QUERY_INSERT_PROVVEDIMENTO, null, null), paramsProvv, keyHolderProvv,
						new String[] { "id_provvedimento" });
				Number keyIdProvvedimento = keyHolderProvv.getKey();
				Long Provvedimento = keyIdProvvedimento.longValue();
				dto.getProvvedimento().get(i).setIdProvvedimento(Provvedimento);
			}

			if (!CollectionUtils.isEmpty(dto.getRecapitiRiscossione())) {
				for (int i = 0; i < dto.getRecapitiRiscossione().size(); i++) {
					// DP sposto if all'interno del ciclo per settare idComune
					// if(fruitore == null) {
					// DP aggiunta if != null
					if (dto.getRecapitiRiscossione().get(i).getComuneRecapito() != null) {
						if (dto.getRecapitiRiscossione().get(i).getComuneRecapito().getIdComune() != null) {
							map.put("idComune", dto.getRecapitiRiscossione().get(i).getComuneRecapito().getIdComune());
							params = getParameterValue(map);
							if (fruitore == null) {
								ComuneExtendedDTO comuneValido = template.queryForObject(QUERY_LOAD_COMUNE, params,
										getComuneRowMapper());
								if (comuneValido.getDataFineValidita() != null) {
									LOGGER.error("[SoggettiDAOImpl::saveSoggetto]: Comune non valido ");
									messaggiDAO.loadMessaggiByCodMessaggio("E085");
									throw new BusinessException(409, "E085",
											"Attenzione: il comune di uno o piu' recapiti non e' piu' valido. Prima di procedere e' necessario selezionare un comune valido.");
								}
							}
						}
					}
					// }
					map.put("idRecapito", dto.getRecapitiRiscossione().get(i).getIdRecapito());
					params = getParameterValue(map);
					template.update(getQuery(QUERY_INSERT_RISCOSSIONE_RECAPITO, null, null), params, keyHolder,
							new String[] { "id_riscossione" });

				}
			}

			return key.longValue();
		} catch (BusinessException be) {
			LOGGER.error("[RiscossioneTributiDAOImpl::saveRiscossione] ERROR : " + be.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw be;
		} catch (Exception e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::saveRiscossione] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[RiscossioneTributiDAOImpl::saveRiscossione] END");
		}
	}

	public String creaWhereConditionSearch(Map<String, Object> map, SimpleDateFormat formatter,
			RiscossioneSearchDTO dto, Long idAmbito, SimpleDateFormat formatter2, boolean filtriSD)
			throws Exception {
		LOGGER.debug("[RiscossioneTributiDAOImpl::creaWhereConditionSearch] BEGIN");
		String whereCond = "";
		String and = " and ";
		if (!CollectionUtils.isEmpty(dto.getIstanze())) {
			whereCond = " where rdtp.flg_istanza = 1 and rts.id_ambito = :idAmbito ";
			map.put("idAmbito", idAmbito);
		} else {
			whereCond = " where (rdtp.flg_istanza = 0 OR rdtp.flg_istanza = 1 )  and rts.id_ambito = :idAmbito ";
			map.put("idAmbito", idAmbito);
		}
		if (dto.getCodRiscossione() != null && !StringUtils.isEmpty(dto.getCodRiscossione()))  {
			whereCond = whereCond + and + "( upper(rtr.cod_riscossione) ilike '%'||:codRiscossione||'%' )";
			map.put("codRiscossione", dto.getCodRiscossione().toUpperCase());
		}
		if (dto.getCodUtente() != null && !StringUtils.isEmpty(dto.getCodUtente())) {
		    whereCond = whereCond + and + " (upper(rtr.cod_riscossione) ilike '%'||:codUtente||'%' ) ";
		    map.put("codUtente",  dto.getCodUtente().toUpperCase());
		}
		if (dto.getCompetenzaTerritoriale() != null && !dto.getCompetenzaTerritoriale().isEmpty()) {
		    String[] competenze = dto.getCompetenzaTerritoriale().split(";");
		    if (competenze.length > 0) {
		        StringBuilder queryCompetenze = new StringBuilder();
		        for (String competenza : competenze) {
		            String[] keyValue = competenza.split("\\|");
		            String chiave = keyValue[0];
		            String valore = keyValue[1];
		           AreaCompetenzaExtendedDTO areeCompetenze = areaCompetenzaDAO.getAreaCompetenzaByCod(idAmbito, chiave);
	               if(areeCompetenze != null) {
		            	String chiaveAreaCompetenza = areeCompetenze.getChiaveAreaCompetenza();
			            if (queryCompetenze.length() > 0) {
			                queryCompetenze.append(" OR ");
			            }
		            	if(chiaveAreaCompetenza.startsWith("json_dt")) {
		            		queryCompetenze.append(
									 "( upper (replace (CAST("+chiaveAreaCompetenza+" as varchar)  ,'''', ''))= '\""
									+ valore.toUpperCase() + "\"' "
									+ " ) ");
		            	}else {
				            queryCompetenze.append("(")
			                .append(chiaveAreaCompetenza)
			                .append("='")
			                .append(valore)
			                .append("')");
		            	}


		            }
		            
		        }
		        if (queryCompetenze.length() > 0) {
		            queryCompetenze.insert(0, "(");
		            queryCompetenze.append(")");
		            whereCond = whereCond + and + queryCompetenze.toString();
		        }
		    }
		}
		if (dto.getIdTipoRiscossione() != null ) {
			whereCond = whereCond + and + " (dtr.id_tipo_riscossione = :idTipoRiscossione) ";
			map.put("idTipoRiscossione", dto.getIdTipoRiscossione());
		}
		if (dto.getIdSoggetto() != null) {
			whereCond = whereCond + and + " (rts.id_soggetto = :idSoggetto) ";
			map.put("idSoggetto", dto.getIdSoggetto());
		}
		if (dto.getNap() != null) {
			whereCond = whereCond + and + " (rtsd.nap ilike '%'||:napSD||'%' ) ";
			map.put("napSD", dto.getNap());
		}
		if (dto.getCodiceAvviso() != null) {
			whereCond = whereCond + and + " (rti.codice_avviso = :codiceAvviso) ";
			map.put("codiceAvviso", dto.getCodiceAvviso());
		}
		if (dto.getPraticaSospesa() != null && dto.getPraticaSospesa().equals(1)) {
			whereCond = whereCond + and + " (rtr.id_stato_riscossione = 6) ";
		}
		if (dto.getAnnoCanone() != null) {
			whereCond = whereCond + and +" (  rras.anno = :annoCanone )";
			map.put("annoCanone", dto.getAnnoCanone());
		}
		if (dto.getCanone() != null) {
			whereCond = whereCond + and + " ( rrrs.canone_dovuto = :canone) ";
			map.put("canone", dto.getCanone());
		}
		if (dto.getRestituitoAlMittente() != null && dto.getRestituitoAlMittente().equals(1)) {
			whereCond = whereCond + and + " (rtsd.flg_restituito_mittente = :restituitoAlMitente) ";
			map.put("restituitoAlMitente", dto.getRestituitoAlMittente());
		}

		if (dto.getDesTipoSoggetto() != null && !StringUtils.isEmpty(dto.getDesTipoSoggetto())) {
			whereCond = whereCond + and + " (upper(rdts.des_tipo_soggetto) = :desTipoSoggetto) ";
			map.put("desTipoSoggetto", dto.getDesTipoSoggetto().toUpperCase());
		}
		if (dto.getNumeroPratica() != null && !StringUtils.isEmpty(dto.getNumeroPratica())) {
			whereCond = whereCond + and + " (upper(rtr.num_pratica) ilike '%'||:numPratica||'%' ) ";
			map.put("numPratica", dto.getNumeroPratica().toUpperCase());
		}
		if (dto.getIdGruppo() != null) {
			whereCond = whereCond + and + "( rtr.id_gruppo_soggetto =:idGruppo ) ";
			map.put("idGruppo", dto.getIdGruppo());
		}
		if (dto.getRagioneSociale() != null && !StringUtils.isEmpty(dto.getRagioneSociale())) {
			whereCond = whereCond + and
					+ "  (TRIM(upper(rts.den_soggetto)) ilike '%'||:denSoggeto||'%'OR upper(rts.cognome || ' ' || rts.nome) ilike '%'||:denSoggeto||'%' OR upper(rts.nome || ' ' || rts.cognome) ilike '%'||:denSoggeto||'%' OR upper(rts.nome) ilike '%'||:denSoggeto||'%' OR upper(rts.cognome) ilike '%'||:denSoggeto||'%') ";
			map.put("denSoggeto", dto.getRagioneSociale().toUpperCase().trim());
		}
		if (dto.getCodiceFiscale() != null && !StringUtils.isEmpty(dto.getCodiceFiscale())) {
			whereCond = whereCond + and + " ( upper(rts.cf_soggetto) ilike '%'||:cfSoggeto||'%' ) ";
			map.put("cfSoggeto", dto.getCodiceFiscale().toUpperCase());
		}
		if (dto.getPartitaIva() != null && !StringUtils.isEmpty(dto.getPartitaIva())) {
			whereCond = whereCond + and + "( upper(rts.partita_iva_soggetto) ilike '%'||:piSoggeto||'%' ) ";
			map.put("piSoggeto", dto.getPartitaIva().toUpperCase());
		}
		if (dto.getNazione() != null) {
			whereCond = whereCond + and + " (  :nazione in ( "
					+ "	 select  rdn.denom_nazione  from risca_d_nazione rdn  "
					+ "	 join risca_r_recapito rrr on rdn.id_nazione =  rrr.id_nazione_recapito  "
					+ "	 where rrr.id_tipo_recapito =1  " + "    " + "   ) )";
			map.put("nazione", dto.getNazione().getDenomNazione());
		}
		if (dto.getComuneDiResidenza() != null) {
			whereCond = whereCond + and
					+ "    replace (rdc.denom_comune, '''','') = :comuneDiResidenza and rrr.id_tipo_recapito =1  ";
			map.put("comuneDiResidenza", dto.getComuneDiResidenza().getDenomComune().replace("'", ""));
		}

		if(dto.getProvincia() != null)
		if (dto.getProvincia().getSiglaProvincia() != null) {
			whereCond = whereCond + and + " ( rdp.id_provincia = :idProv ) ";
			map.put("idProv", dto.getProvincia().getIdProvincia());
		}
		if (dto.getProvinciaDiCompetenza() != null ) {
			whereCond = whereCond + and + "( upper(rtr.cod_riscossione_prov) ilike '%'||:codRisProv||'%' ) ";
			map.put("codRisProv", dto.getProvinciaDiCompetenza().getSiglaProvincia().toUpperCase());
		}
		if (dto.getIndirizzo() != null && !StringUtils.isEmpty(dto.getIndirizzo())) {
			whereCond = whereCond + and
					+ " ( upper(rrr.indirizzo) ilike '%'||:indirizzo||'%' OR upper(rrr.num_civico) ilike '%'||:indirizzo||'%') ";
			map.put("indirizzo", dto.getIndirizzo().toUpperCase());
		}
		if (dto.getIdTipoTitolo() != null) {
			whereCond = whereCond + and + " ( rrp.id_tipo_titolo  = :idTipoTitolo )";
			map.put("idTipoTitolo", dto.getIdTipoTitolo());
		}
		if (dto.getIdTipoProvvedimento() != null) {
			whereCond = whereCond + and + "( rrp.id_tipo_provvedimento  = :idTipoProvvedimento) ";
			map.put("idTipoProvvedimento", dto.getIdTipoProvvedimento());
		}
		if (dto.getNumeroTitolo() != null) {
			whereCond = whereCond + and + " (upper(rrp.num_titolo)  = :numTitolo )";
			map.put("numTitolo", dto.getNumeroTitolo().toUpperCase());
		}
		if (dto.getDataTitoloDa() != null && dto.getDataTitoloA() == null) {
			whereCond = whereCond + and + " (rrp.data_provvedimento  >= :dataTitoloDa  )";
			Date dataTitoloDa = formatter2.parse(dto.getDataTitoloDa());
			
			map.put("dataTitoloDa", dataTitoloDa);
		}
		if (dto.getDataTitoloDa() == null && dto.getDataTitoloA() != null) {
			whereCond = whereCond + and + "( rrp.data_provvedimento  <= :dataTitoloA) ";
			Date dataTitoloA = formatter2.parse(dto.getDataTitoloA());
			map.put("dataTitoloA", dataTitoloA);
		}
		if (dto.getDataTitoloDa() != null && dto.getDataTitoloA() != null) {
			whereCond = whereCond + and + " ( rrp.data_provvedimento  BETWEEN  :dataTitoloDa and :dataTitoloA )";
			Date dataTitoloDa = formatter2.parse(dto.getDataTitoloDa());
			Date dataTitoloA = formatter2.parse(dto.getDataTitoloA());
			map.put("dataTitoloA", dataTitoloA);
			map.put("dataTitoloDa", dataTitoloDa);
		}
		if (dto.getIdStatoRiscossione() != null) {
			whereCond = whereCond + and + " (rtr.id_stato_riscossione  = :idStatoRiscossione )";
			map.put("idStatoRiscossione", dto.getIdStatoRiscossione());
		}
		if (dto.getScadenzaConcessioneDa() != null && dto.getScadenzaConcessioneA() == null) {
			whereCond = whereCond + and + "( rtr.data_scad_concessione  >= :dataScadenzaConcessioneDa) ";
			Date dataScadenzaConcessioneDa = formatter2.parse(dto.getScadenzaConcessioneDa());
			map.put("dataScadenzaConcessioneDa", dataScadenzaConcessioneDa);
		}
		if (dto.getScadenzaConcessioneDa() == null && dto.getScadenzaConcessioneA() != null) {
			whereCond = whereCond + and + " (rtr.data_scad_concessione  <= :dataScadenzaConcessioneA )";
			Date dataScadenzaConcessioneA =  formatter2.parse(dto.getScadenzaConcessioneA());
			map.put("dataScadenzaConcessioneA", dataScadenzaConcessioneA);
		}
		if (dto.getScadenzaConcessioneDa() != null && dto.getScadenzaConcessioneA() != null) {
			whereCond = whereCond + and
					+ " (rtr.data_scad_concessione  BETWEEN :dataScadenzaConcessioneDa AND :dataScadenzaConcessioneA )";
			Date dataScadenzaConcessioneA = formatter2.parse(dto.getScadenzaConcessioneA());
			Date dataScadenzaConcessioneDa =  formatter2.parse(dto.getScadenzaConcessioneDa());
			map.put("dataScadenzaConcessioneDa", dataScadenzaConcessioneDa);
			map.put("dataScadenzaConcessioneA", dataScadenzaConcessioneA);
		}
		if (!filtriSD) {
			if (dto.getDataRinunciaRevocaDa() != null && dto.getDataRinunciaRevocaA() == null) {
				whereCond = whereCond + and + " (rtr.data_rinuncia_revoca    >= :dataRinunciaRevocaDa ) ";
				Date dataRinunciaRevocaDa =  formatter2.parse(dto.getDataRinunciaRevocaDa());
				map.put("dataRinunciaRevocaDa", dataRinunciaRevocaDa);
			}
			if (dto.getDataRinunciaRevocaDa() == null && dto.getDataRinunciaRevocaA() != null) {
				whereCond = whereCond + and + " (rtr.data_rinuncia_revoca    <= :dataRinunciaRevocaA ) ";
				Date dataRinunciaRevocaA =  formatter2.parse(dto.getDataRinunciaRevocaA());
				map.put("dataRinunciaRevocaA", dataRinunciaRevocaA);
			}
			if (dto.getDataRinunciaRevocaDa() != null && dto.getDataRinunciaRevocaA() != null) {
				whereCond = whereCond + and + "( rtr.data_rinuncia_revoca   BETWEEN :dataRinunciaRevocaDa and :dataRinunciaRevocaA  )";
				Date dataRinunciaRevocaA =  formatter2.parse(dto.getDataRinunciaRevocaA());
				Date dataRinunciaRevocaDa =  formatter2.parse(dto.getDataRinunciaRevocaDa());
				map.put("dataRinunciaRevocaDa", dataRinunciaRevocaDa);
				map.put("dataRinunciaRevocaA", dataRinunciaRevocaA);
			}
		}
		int indexProvvedimenti=0;
		if (!CollectionUtils.isEmpty(dto.getProvvedimenti())) {
			List<ProvvedimentoSearchDTO> listProvvedimenti = dto.getProvvedimenti();
			String queryProvvedimenti ="";
			String andProvvedimenti ="and (";
			String fineAndProvvedimenti= ")";
			for (ProvvedimentoSearchDTO provvedimentoSearch : listProvvedimenti) {
				   String query= "";
				if(indexProvvedimenti > 0) {
					queryProvvedimenti = queryProvvedimenti  + " OR ";
				}
				query = query + " ( ( rdtp.flg_istanza = 0 ) ";
					
				if(provvedimentoSearch.getIdProvvedimento() != null) {
					query = query +  and +   " (rrp.id_provvedimento  = :idProvvedimento"+indexProvvedimenti+") ";
					map.put("idProvvedimento"+indexProvvedimenti, provvedimentoSearch.getIdProvvedimento());
				}
				if(provvedimentoSearch.getIdTipoTitolo() != null) {
					query = query +  and +   " (rrp.id_tipo_titolo  = :idTipoTitolo"+indexProvvedimenti+") ";
					map.put("idTipoTitolo"+indexProvvedimenti, provvedimentoSearch.getIdTipoTitolo());
				}
				if(provvedimentoSearch.getNumeroTitolo() != null) {
					query = query +  and +   " (rrp.num_titolo  = :numeroTitolo"+indexProvvedimenti+") ";
					map.put("numeroTitolo"+indexProvvedimenti, provvedimentoSearch.getNumeroTitolo());
				}
				if (!StringUtils.isEmpty(provvedimentoSearch.getDataProvvedimentoDa()) && StringUtils.isEmpty(provvedimentoSearch.getDataProvvedimentoA()) ) {
					query = query +  and + "( rrp.data_provvedimento  >= :dataProvvedimentoDa"+indexProvvedimenti+" ) ";
					Date dataProvvedimentoDa = formatter2.parse(formatter2.format(formatter.parse(provvedimentoSearch.getDataProvvedimentoDa())));
					map.put("dataProvvedimentoDa"+indexProvvedimenti, dataProvvedimentoDa);
				}
				if (StringUtils.isEmpty(provvedimentoSearch.getDataProvvedimentoDa()) && !StringUtils.isEmpty(provvedimentoSearch.getDataProvvedimentoA()) )  {
					query = query +  and + " (rrp.data_provvedimento  <= :dataProvvedimentoA"+indexProvvedimenti+") ";
					Date dataProvvedimentoA = formatter2.parse(formatter2.format(formatter.parse(provvedimentoSearch.getDataProvvedimentoA())));
					map.put("dataProvvedimentoA"+indexProvvedimenti, dataProvvedimentoA);
				}
				if (!StringUtils.isEmpty(provvedimentoSearch.getDataProvvedimentoDa()) && !StringUtils.isEmpty(provvedimentoSearch.getDataProvvedimentoA()) )  {
					query = query + and
							+ " (rrp.data_provvedimento  BETWEEN :dataProvvedimentoDa"+indexProvvedimenti+" and :dataProvvedimentoA"+indexProvvedimenti+") ";
					Date dataProvvedimentoDa = formatter2.parse(formatter2.format(formatter.parse(provvedimentoSearch.getDataProvvedimentoDa())));
					Date dataProvvedimentoA = formatter2.parse(formatter2.format(formatter.parse(provvedimentoSearch.getDataProvvedimentoA())));
					map.put("dataProvvedimentoDa"+indexProvvedimenti, dataProvvedimentoDa);
					map.put("dataProvvedimentoA"+indexProvvedimenti, dataProvvedimentoA);
					
				}
				
				query = query + " ) ";
				queryProvvedimenti = queryProvvedimenti + query;
				indexProvvedimenti++;
			}
			
			
			whereCond = whereCond + andProvvedimenti + queryProvvedimenti +fineAndProvvedimenti;
		}
		int countHavingTipoIstanza = 0;
		if (!CollectionUtils.isEmpty(dto.getIstanze())) {
			List<TipoIstanzaDTO> listTipoIstanzaDTO = dto.getIstanze();
			String queryTipoIstanza = "";
			String andTipoIstanza = "and (";
			String fineAndTipoIstanza = ")";
			for (TipoIstanzaDTO tipoIstanzaDTO : listTipoIstanzaDTO) {
				String query = "";
				if (countHavingTipoIstanza > 0) {
					queryTipoIstanza = queryTipoIstanza + " OR ";
				}
				query = query + "( ( rdtp.flg_istanza = 1 )";
				if (tipoIstanzaDTO.getIdIstanza() != null) {
					query = query + and + " (rrp.id_tipo_provvedimento  = :idIstanza" + countHavingTipoIstanza + ")";
					map.put("idIstanza" + countHavingTipoIstanza, tipoIstanzaDTO.getIdIstanza());
				}
				if (!StringUtils.isEmpty(tipoIstanzaDTO.getDataIstanzaDa())
						&& StringUtils.isEmpty(tipoIstanzaDTO.getDataIstanzaA())) {
					query = query + and + "( rrp.data_provvedimento  >= :dataIstanzaDa" + countHavingTipoIstanza
							+ " ) ";
					Date dataIstanzaDa = formatter2
							.parse(formatter2.format(formatter.parse(tipoIstanzaDTO.getDataIstanzaDa())));
					map.put("dataIstanzaDa" + countHavingTipoIstanza, dataIstanzaDa);
				}
				if (StringUtils.isEmpty(tipoIstanzaDTO.getDataIstanzaDa())
						&& !StringUtils.isEmpty(tipoIstanzaDTO.getDataIstanzaA())) {
					query = query + and + " (rrp.data_provvedimento  <= :dataIstanzaA" + countHavingTipoIstanza + ") ";
					Date dataIstanzaA = formatter2
							.parse(formatter2.format(formatter.parse(tipoIstanzaDTO.getDataIstanzaA())));
					map.put("dataIstanzaA" + countHavingTipoIstanza, dataIstanzaA);
				}
				if (!StringUtils.isEmpty(tipoIstanzaDTO.getDataIstanzaDa())
						&& !StringUtils.isEmpty(tipoIstanzaDTO.getDataIstanzaA())) {
					query = query + and + " (rrp.data_provvedimento  BETWEEN :dataIstanzaDa" + countHavingTipoIstanza
							+ " and :dataIstanzaA" + countHavingTipoIstanza + ") ";
					Date dataIstanzaA = formatter2
							.parse(formatter2.format(formatter.parse(tipoIstanzaDTO.getDataIstanzaA())));
					Date dataIstanzaDa = formatter2
							.parse(formatter2.format(formatter.parse(tipoIstanzaDTO.getDataIstanzaDa())));
					map.put("dataIstanzaDa" + countHavingTipoIstanza, dataIstanzaDa);
					map.put("dataIstanzaA" + countHavingTipoIstanza, dataIstanzaA);

				}

				query = query + " ) ";
				queryTipoIstanza = queryTipoIstanza + query;
				countHavingTipoIstanza++;
			}

			whereCond = whereCond + andTipoIstanza + queryTipoIstanza + fineAndTipoIstanza;
		}
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		;
		List<String> listNomiTipoUso = new ArrayList<>();
		if (dto.getDatiTecnici() != null) {
			// parte della ricerca Dati Tecnici
			DatiTecniciTributiDTO datiTecniciTributi = mapper.readValue(dto.getDatiTecnici(),
					DatiTecniciTributiDTO.class); // recupero dati tecnici dal campo json_dt

			String nometipoUsoDatoTecnico = "";
			int count = 0;
			String queryTipoUso = "";
			String andTipoUso = "";
			String fineAndTipoUso = "";
			for (String keytipoUsoDatoTecnico : datiTecniciTributi.getUsi().keySet()) {
				andTipoUso = " and ( ";
				nometipoUsoDatoTecnico = keytipoUsoDatoTecnico;

				String query = "";
				if (!"".equals(nometipoUsoDatoTecnico) && !"undefined".equals(nometipoUsoDatoTecnico)) {
					TipoUsoDatoTecnicoTributiDTO tipoUsoDatoTecnicoTributiDTO = datiTecniciTributi.getUsi()
							.get(nometipoUsoDatoTecnico);
					listNomiTipoUso.add(tipoUsoDatoTecnicoTributiDTO.getTipoUsoLegge());

					if (tipoUsoDatoTecnicoTributiDTO != null) {
						if (count > 0) {
							queryTipoUso = queryTipoUso + " AND ";
						}
						query = query + " ( ";

						if (!filtriSD) {
							query = query + "( replace (CAST( json_dt ->'riscossione'->'dati_tecnici'->'usi'->'"
									+ nometipoUsoDatoTecnico + "' ->'uso_di_legge' as varchar),'''', '') = '\""
									+ tipoUsoDatoTecnicoTributiDTO.getTipoUsoLegge().replace("'", "") + "\"'  )";

							long idTipoUsoLegge = tipoUsoDatoTecnicoTributiDTO.getIdTipoUsoLegge() != null
									? tipoUsoDatoTecnicoTributiDTO.getIdTipoUsoLegge()
									: 0;
							if (idTipoUsoLegge > 0)
								query = query + and + " CAST( json_dt ->'riscossione'->'dati_tecnici'->'usi'->'"
										+ nometipoUsoDatoTecnico + "' ->'id_tipo_uso_legge' as Integer) = "
										+ idTipoUsoLegge;

							Long popolazione = tipoUsoDatoTecnicoTributiDTO.getPopolazione() != null
									? tipoUsoDatoTecnicoTributiDTO.getPopolazione()
									: null;
							if (!Objects.isNull(popolazione))
								query = query + and + " (CAST( json_dt ->'riscossione'->'dati_tecnici'->'usi'->'"
										+ nometipoUsoDatoTecnico + "'->'popolazione'   as Integer) = " + popolazione
										+ ")";
							query = query + " ) ";
							queryTipoUso = queryTipoUso + query;
						} else {
							query = query + "( "
									+ " ( replace (CAST( rras.json_dt_riscossione ->'riscossione'->'dati_tecnici'->'usi'->'"
									+ nometipoUsoDatoTecnico + "' ->'uso_di_legge' as varchar),'''', '') = '\""
									+ tipoUsoDatoTecnicoTributiDTO.getTipoUsoLegge().replace("'", "") + "\"'  ) "
									+ " OR " + " ( replace (CAST( rras.json_dt_riscossione ->'usi'->'"
									+ nometipoUsoDatoTecnico + "' ->'uso_di_legge' as varchar),'''', '') = '\""
									+ tipoUsoDatoTecnicoTributiDTO.getTipoUsoLegge().replace("'", "") + "\"'  ) "
									+ " ) ";

							long idTipoUsoLegge = tipoUsoDatoTecnicoTributiDTO.getIdTipoUsoLegge() != null
									? tipoUsoDatoTecnicoTributiDTO.getIdTipoUsoLegge()
									: 0;
							if (idTipoUsoLegge > 0)
								query = query + and + "" + " ( "
										+ " CAST( rras.json_dt_riscossione ->'riscossione'->'dati_tecnici'->'usi'->'"
										+ nometipoUsoDatoTecnico + "' ->'id_tipo_uso_legge' as Integer) = "
										+ idTipoUsoLegge + " OR " + " CAST( rras.json_dt_riscossione ->'usi'->'"
										+ nometipoUsoDatoTecnico + "' ->'id_tipo_uso_legge' as Integer) = "
										+ idTipoUsoLegge + " ) ";

							Long popolazione = tipoUsoDatoTecnicoTributiDTO.getPopolazione() != null
									? tipoUsoDatoTecnicoTributiDTO.getPopolazione()
									: null;
							if (!Objects.isNull(popolazione))
								query = query + and + "" + " ( "
										+ " (CAST(  rras.json_dt_riscossione ->'riscossione'->'dati_tecnici'->'usi'->'"
										+ nometipoUsoDatoTecnico + "'->'popolazione'   as Integer) = " + popolazione
										+ ")" + " OR " + " (CAST(  rras.json_dt_riscossione ->'usi'->'"
										+ nometipoUsoDatoTecnico + "'->'popolazione'   as Integer) = " + popolazione
										+ ")" + " ) ";
							query = query + " ) ";
							queryTipoUso = queryTipoUso + query;
						}

						count++;
					}
				}
				if ("undefined".equals(nometipoUsoDatoTecnico)) {
					List<TipoUsoExtendedDTO> ListTipoUso = tipoUsoDao.loadTipoUso().stream()
							.filter(t -> t.getIdTipoUsoPadre() == null).collect(Collectors.toList());
					TipoUsoDatoTecnicoTributiDTO tipoUsoDatoTecnicoTributiDTO = datiTecniciTributi.getUsi()
							.get(nometipoUsoDatoTecnico);
					for (TipoUsoExtendedDTO nomeTipoUso : ListTipoUso) {
						query = "";
						if (count > 0) {
							queryTipoUso = queryTipoUso + " OR ";
						}
						query = query + " ( ";
						if (!filtriSD) {
							query = query + "( replace (CAST( json_dt ->'riscossione'->'dati_tecnici'->'usi'->'"
									+ nomeTipoUso.getCodTipouso() + "' ->'uso_di_legge' as varchar) ,'''', '')  = '\""
									+ nomeTipoUso.getDesTipouso().replace("'", "") + "\"')";
						} else {
							query = query + "" + " ( "
									+ " ( replace (CAST( rras.json_dt_riscossione ->'riscossione'->'dati_tecnici'->'usi'->'"
									+ nomeTipoUso.getCodTipouso() + "' ->'uso_di_legge' as varchar) ,'''', '')  = '\""
									+ nomeTipoUso.getDesTipouso().replace("'", "") + "\"')" + " OR "
									+ " ( replace (CAST( rras.json_dt_riscossione ->'usi'->'"
									+ nomeTipoUso.getCodTipouso() + "' ->'uso_di_legge' as varchar) ,'''', '')  = '\""
									+ nomeTipoUso.getDesTipouso().replace("'", "") + "\"')" + " ) ";
						}

						query = query + " ) ";
						queryTipoUso = queryTipoUso + query;

						count++;
					}
				}

				fineAndTipoUso = " )";
			}
			whereCond = whereCond + andTipoUso + queryTipoUso + fineAndTipoUso;

		}

		if (!filtriSD) {
			whereCond = whereCond + QUERY_GROUP_BY_SEARCH_RISCOSSIONI;
		}
		if (countHavingTipoIstanza > 1) {
			whereCond = whereCond + "having count(*) > " + (countHavingTipoIstanza - 1);
		}
		LOGGER.debug("[RiscossioneTributiDAOImpl::creaWhereConditionSearch] END");
		return whereCond;
	}

	public List<RiscossioneSearchResultDTO> searchRiscossioni(RiscossioneSearchDTO dto, Long idAmbito,
			String modalitaRicerca, Integer offset, Integer limit, String sort) throws Exception {

		LOGGER.debug("[RiscossioneTributiDAOImpl::searchRiscossione] BEGIN");
		try {

			Map<String, Object> map = new HashMap<>();
			List<RiscossioneSearchResultDTO> riscossioneSearchResultDTO = new ArrayList<RiscossioneSearchResultDTO>();
			MapSqlParameterSource params = null;
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
			String queySearch = "";
			String whereCond = "";
			if (modalitaRicerca != null && "statoDebitorio".equalsIgnoreCase(modalitaRicerca)) {
//					crea query per lo SD 
				queySearch = QUERY_SEARCH_RISCOSSIONI;
				String querySd = " where  rtr.id_riscossione in ( "
						+ addJoinQueryFromDto(dto, modalitaRicerca, QUERY_SEARCH_RISCOSSIONI_BY_SD)
						+ creaWhereConditionSearch(map, formatter, dto, idAmbito, formatter2, true) + " ) ";
				whereCond = querySd + QUERY_GROUP_BY_SEARCH_RISCOSSIONI;
			} else {
				queySearch = addJoinQueryFromDto(dto, modalitaRicerca, QUERY_SEARCH_RISCOSSIONI);
				whereCond = creaWhereConditionSearch(map, formatter, dto, idAmbito, formatter2, false);
			}

			params = getParameterValue(map);

			String dynamicOrderByCondition= mapSortConCampiDB(sort);

			riscossioneSearchResultDTO = template.query(
					getQuery(queySearch + whereCond + dynamicOrderByCondition,
							offset != null ? offset.toString() : null, limit != null ? limit.toString() : null),
					params, getRowMapperRiscossioneSearch());

			return riscossioneSearchResultDTO;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[RiscossioneTributiDAOImpl::searchRiscossione]Data not found");
			return Collections.emptyList();
		} catch (SQLException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::searchRiscossione] Errore nell'esecuzione della query ", e);
			throw new Exception(e);
		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::searchRiscossione] Errore nell'accesso ai dati ", e);
			throw new Exception(e);
		} catch (ParseException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::searchRiscossione] Errore parse  ", e);
			throw new Exception(e);
		} catch (JsonParseException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::searchRiscossione] Errore Json Parse  ", e);
			throw new Exception(e);
		} catch (JsonMappingException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::searchRiscossione] Errore Json Mapping  ", e);
			throw new Exception(e);
		} catch (IOException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::searchRiscossione] Errore generale ", e);
			throw new Exception(e);
		} finally {
			LOGGER.debug("[RiscossioneTributiDAOImpl::searchRiscossione] END");
		}
	}

	private void validateRiscossione(RiscossioneDTO dto) throws SQLException, SystemException  {
		LOGGER.debug("[RiscossioneTributiDAOImpl::validateRiscossione] BEGIN");
		SoggettiExtendedDTO soggetto = dto.getSoggetto();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 

		if (soggetto.getIdSoggetto() == null) {
				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio( "E037");
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
		}
		if (dto.getGruppoSoggetto() != null && dto.getGruppoSoggetto().getIdGruppoSoggetto() != null) {
			Long IdSoggettoReferente = gruppiDAO
					.findIdSoggettoByIdGruppoSoggetto(dto.getGruppoSoggetto().getIdGruppoSoggetto());

			if (!("" + soggetto.getIdSoggetto()).equals("" + IdSoggettoReferente)) {
				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E031");
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
			}

		}

		int recapiti = 0;
		boolean isPrincipale = false;
		for (RecapitiExtendedDTO recapDTO : CollectionUtils.emptyIfNull(dto.getRecapitiRiscossione())) {
			if (recapDTO.getIdRecapito() != null) {
		        Map<String, Object> map = new HashMap<>();
		        map.put("idRecapito", recapDTO.getIdRecapito());

		        try {
		            RecapitiDTO recap = template.queryForObject(QUERY_RECAPITI_BY_ID_RECAPITO, getParameterValue(map), getRecapitiRowMapper());
		            if (recap.getIdTipoRecapito() == 1 /* P - principale */) {
		                if (isPrincipale) {
		                    MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E032");
		                    throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
		                }
		                isPrincipale = true;
		            }
		        	recapiti++;
		        } catch (EmptyResultDataAccessException e) {
		            LOGGER.debug("Recapito non trovato sul database: " + e);
		            throw new BusinessException(400, null, "Recapito non trovato sul database");
		        } catch (DataAccessException e) {
		            LOGGER.error("Errore durante il recupero del recapito: " + e);
		            throw new BusinessException(500, null, "Errore durante il recupero del recapito");
		        }
		    }
			
		}

		if (recapiti == 0) {
			MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E033");
			throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
		}

		if (recapiti > 2) {
			MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E030");
			throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
		}
		if(CollectionUtils.isEmpty(dto.getProvvedimento())) {
			MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E044");
			throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
		}

		
		
		if(dto.getTipoAutorizza() == null) {
			MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E001");
			throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
		}else {
			MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E001");
			if(dto.getCodRiscossioneProv() == null || dto.getCodRiscossioneProv().isEmpty() ) 
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
			if(dto.getTipoAutorizza().getCodTipoAutorizza() == null || dto.getTipoAutorizza().getCodTipoAutorizza().isEmpty()  ) 
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
			if(dto.getTipoAutorizza().getIdTipoAutorizza() == null) 
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
		}
		if(dto.getNumPratica() == null || dto.getNumPratica().isEmpty() ) {
			MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E001");
			throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
		}		
		if (dto.getStatiRiscossione() == null) {
			MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E001");
			throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
		}	
		else {
			if(dto.getStatiRiscossione().getCodStatoRiscossione() == null || dto.getStatiRiscossione().getCodStatoRiscossione().isEmpty())  {
				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E001");
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
			}	
			if(dto.getStatiRiscossione().getIdStatoRiscossione() == null)  {
				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E001");
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
			}	
		}
		

		if (StringUtils.isNotBlank(dto.getDataScadConcessione()) && StringUtils.isNotBlank(dto.getDataIniConcessione()) ) {

			Date dataIniConcessione = null;
			Date dataScadConcessione = null;
			try {
				dataIniConcessione = formatter.parse(dto.getDataIniConcessione());
				dataScadConcessione = formatter.parse(dto.getDataScadConcessione());
			} catch (ParseException e) {
				LOGGER.error("[RiscossioneAmbienteDAOImpl::validateRiscossione] ERROR ParseException : " +e);
			}
			if(dataIniConcessione.getTime() >=  dataScadConcessione.getTime() ) {
				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E017");
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
			}
		}
		if (StringUtils.isNotBlank(dto.getDataIniSospCanone()) && StringUtils.isNotBlank(dto.getDataFineSospCanone()) ) {
			Date dataIniSospCanone = null;
			Date dataFineSospCanone = null;
			try {
				dataIniSospCanone = formatter.parse(dto.getDataIniSospCanone());
				dataFineSospCanone = formatter.parse(dto.getDataFineSospCanone());
			} catch (ParseException e) {
				LOGGER.error("[RiscossioneAmbienteDAOImpl::validateRiscossione] ERROR ParseException : " +e);
			}
			if(dataIniSospCanone.getTime() >= dataFineSospCanone.getTime()) {
				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E018");
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
			}
		}
		List<String> listIdtipoProvvedimento = new ArrayList<>();
		if(!CollectionUtils.isEmpty(dto.getProvvedimento())) {
			for (ProvvedimentoDTO provvedimento : dto.getProvvedimento()) {
				listIdtipoProvvedimento.add(provvedimento.getTipiProvvedimentoExtendedDTO().getCodTipoProvvedimento());
				 String flgIstanza = provvedimento.getTipiProvvedimentoExtendedDTO() != null ?provvedimento.getTipiProvvedimentoExtendedDTO().getFlgIstanza() : "";
				if (provvedimento.getDataProvvedimento() == null && "0".equals(flgIstanza)) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E001");
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}	
				if (provvedimento.getDataProvvedimento() != null && "0".equals(flgIstanza)) {
				    try {
				        LocalDate dataProvvedimentoParsed = LocalDate.parse(provvedimento.getDataProvvedimento(), dtf);
				        LocalDate nowLocalDate = LocalDate.now();

				        if (dataProvvedimentoParsed.isAfter(nowLocalDate)) {
				            MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E022");
				            throw new BusinessException(400, messaggiDTO.getCodMessaggio(), messaggiDTO.getDesTestoMessaggio());
				        }
				    } catch (DateTimeParseException e) {
				        LOGGER.error("[RiscossioneAmbienteDAOImpl::validateRiscossione] ERROR DateTimeParseException: " + e);
				    }
				}
 

				
				if (provvedimento.getTipiProvvedimentoExtendedDTO() == null && "0".equals(flgIstanza)) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E001");
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}	
				

				if (provvedimento.getNumTitolo() == null && "0".equals(flgIstanza)) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E001");
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}	
				

				if (provvedimento.getTipiProvvedimentoExtendedDTO().getIdTipoProvvedimento() == null ) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E044");
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}else {
					Map<String, Object> map = new HashMap<>();
					map.put("idTipoProvvedimento", provvedimento.getTipiProvvedimentoExtendedDTO().getIdTipoProvvedimento());
					MapSqlParameterSource params = getParameterValue(map);
					Integer idTipoProvvedimento = template.query(QUERY_SELECT_PROVVEDIMENTO_IF_EXIST, params, new ResultSetExtractor<Integer>(){
						    @Override
						    public Integer extractData(ResultSet rs) throws SQLException,DataAccessException {
						    	Integer provvedimento = null;
						        while(rs.next()){
						        	provvedimento = rs.getInt("provvedimento");
						        }
						        return provvedimento ;
						    }
						});
					if(idTipoProvvedimento == 0) {
						MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E044");
						throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
					}
				}
			}
			

				

		}else
			throw new BusinessException(400, "E044", " Attenzione: inserire almeno un provvedimento ");
		
		// stato RINUNCIATA
//	    if (dto.getStatiRiscossione().getCodStatoRiscossione().equals("RINUNCIATA")){
//			MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E043");
//	    	boolean verify = false;
//	    	if(listIdtipoProvvedimento.contains("IST_RINUNCIA_TOT") || listIdtipoProvvedimento.contains("RIN_TOTALE") )
//	    		verify = true;
//
//			if(!verify) {
//				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
//			}
//         
//			if(listIdtipoProvvedimento.contains("IST_RINUNCIA_TOT") || listIdtipoProvvedimento.contains("RIN_TOTALE")) {
//				for (ProvvedimentoDTO provvedimento : dto.getProvvedimento()) {
//					if(provvedimento.getTipiProvvedimentoExtendedDTO().getCodTipoProvvedimento().equals("IST_RINUNCIA_TOT")
//						|| 	provvedimento.getTipiProvvedimentoExtendedDTO().getCodTipoProvvedimento().equals("RIN_TOTALE")) {
//						if(provvedimento.getDataProvvedimento() == null) {
//							throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
//						}
//					
//					}
//				}
//			}
//		
//	    }
		// stato SOSPESA
//	    if (dto.getStatiRiscossione().getCodStatoRiscossione().equals("SOSPESA")){
//			MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E043");
//			if(StringUtils.isBlank(dto.getDataIniSospCanone()) || StringUtils.isBlank(dto.getDataFineSospCanone())) {
//				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
//			}
//
//			else
//			{
//				Date dataIniSospCanone = null;
//				Date dataFineSospCanone = null;
//				try {
//					dataIniSospCanone = formatter.parse(dto.getDataIniSospCanone());
//					dataFineSospCanone = formatter.parse(dto.getDataFineSospCanone());
//					now = formatter.parse(formatter.format(cal.getTime())) ;
//				} catch (ParseException e) {
//					LOGGER.error("[RiscossioneAmbienteDAOImpl::validateRiscossione] ERROR ParseException : " +e);
//				}
//				if(dataIniSospCanone.getTime() > now.getTime() || dataFineSospCanone.getTime() < now.getTime()  ) {
//					if(StringUtils.isBlank(dto.getDataIniSospCanone()) || StringUtils.isBlank(dto.getDataFineSospCanone())) {
//						throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
//				}
//			}
//			
//			
//		}
//			}
		// stato SCADUTA
	    boolean contieneIstanzeValide = listIdtipoProvvedimento.stream()
	            .anyMatch(tipoProvvedimento -> 
	                tipoProvvedimento.equals("IST_RINNOVO") ||
	                tipoProvvedimento.equals("IST_SANATORIA") ||
	                tipoProvvedimento.equals("AUT_PROVVISORIA") 
	            );
		if (dto.getStatiRiscossione().getCodStatoRiscossione().equals("SCADUTA")) {
		    MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E043);
		    
			if (dto.getDataScadConcessione() == null && StringUtils.isNotBlank(dto.getDataScadConcessione())) {
		        throw new BusinessException(400, messaggiDTO.getCodMessaggio(), messaggiDTO.getDesTestoMessaggio());
		    } else {
		        LocalDate dataScadenza = LocalDate.parse(dto.getDataScadConcessione(), dtf);
		        LocalDate nowLocalDate = LocalDate.now();

		        if (dataScadenza.isBefore(nowLocalDate)) {
		        	if(contieneIstanzeValide)
		            throw new BusinessException(400, messaggiDTO.getCodMessaggio(), messaggiDTO.getDesTestoMessaggio());
		        }else {
		        	  throw new BusinessException(400, messaggiDTO.getCodMessaggio(), messaggiDTO.getDesTestoMessaggio());
		        }
		    }
		}

	    
		// stato attiva
		if (dto.getStatiRiscossione().getCodStatoRiscossione().equals("ATTIVA")){

//			if(listIdtipoProvvedimento.contains("IST_RINUNCIA_TOT") || listIdtipoProvvedimento.contains("RIN_TOTALE")) {
//				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
//			}
//			if(StringUtils.isNotBlank(dto.getDataIniSospCanone()) && StringUtils.isNotBlank(dto.getDataFineSospCanone())) {
//				Date dataIniSospCanone = null;
//				Date dataFineSospCanone = null;
//				try {
//					dataIniSospCanone = formatter.parse(dto.getDataIniSospCanone());
//					dataFineSospCanone = formatter.parse(dto.getDataFineSospCanone());
//					now = formatter.parse(formatter.format(cal.getTime())) ;
//				} catch (ParseException e) {
//					LOGGER.error("[RiscossioneAmbienteDAOImpl::validateRiscossione] ERROR ParseException : " +e);
//				}
//				if(dataIniSospCanone.getTime() < now.getTime() && dataFineSospCanone.getTime() > now.getTime()  ) {
//					throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
//				}
//			}
			

			if(!dto.getTipoAutorizza().getCodTipoAutorizza().equals(Constants.AUTORIZZAZIONE_PROVV)) {
				if(dto.getDataScadConcessione() == null || StringUtils.isBlank(dto.getDataScadConcessione())) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E001);

					throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
							Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}

			}
			
			if (dto.getDataScadConcessione() != null && StringUtils.isNotBlank(dto.getDataScadConcessione())) {
				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E043);
			    String dataScadConcessione = dto.getDataScadConcessione();
			    
			    if (StringUtils.isBlank(dataScadConcessione)) {
			        throw new BusinessException(400, messaggiDTO.getCodMessaggio(), messaggiDTO.getDesTestoMessaggio());
			    } else {
			        LocalDate dataScadenza = LocalDate.parse(dataScadConcessione, dtf);
			        LocalDate nowLocalDate = LocalDate.now();

			        if (dataScadenza.isBefore(nowLocalDate)) {
			        	if(!contieneIstanzeValide)
			            throw new BusinessException(400, messaggiDTO.getCodMessaggio(), messaggiDTO.getDesTestoMessaggio());
			        }
			    }
		}
		}
		LOGGER.debug("[RiscossioneTributiDAOImpl::validateRiscossione] END");
	}

	public Long updateRiscossione(RiscossioneDTO dto) throws BusinessException, Exception {
		LOGGER.debug("[RiscossioneTributiDAOImpl::updateRiscossione] BEGIN");

		validateRiscossione(dto);

		try {

			Map<String, Object> map = new HashMap<>();

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idRiscossione", dto.getIdRiscossione());
			map.put("idTipoRiscossione", dto.getIdTipoRiscossione());
			map.put("idComponenteDt", dto.getIdComponenteDt());
			map.put("idStatoRiscossione", dto.getStatiRiscossione().getIdStatoRiscossione()); // far arrivare per adesso
																								// un valore statico in
																								// attesa dell'algoritmo
																								// 15
			map.put("idSoggetto", dto.getSoggetto().getIdSoggetto());
			if (dto.getGruppoSoggetto() != null)
				map.put("idGruppoSoggetto", dto.getGruppoSoggetto().getIdGruppoSoggetto());
			else
				map.put("idGruppoSoggetto", null);
			map.put("idTipoAutorizza", dto.getTipoAutorizza().getIdTipoAutorizza());

			String ambito = dto.getAmbito();
			Long idAmbito = 0L;
			if (ambito != null)
				idAmbito = Long.parseLong(ambito);
			if (idAmbito.equals(4L))
				map.put("codRiscossione", dto.getCodRiscossioneProv() + dto.getCodRiscossioneProg());

			map.put("codRiscossioneProv", dto.getCodRiscossioneProv());
			map.put("codRiscossioneProg", dto.getCodRiscossioneProg());
			map.put("idSiglaRiscossione", dto.getIdSiglaRiscossione());
			map.put("codRiscossioneLetteraProv", dto.getCodRiscossioneLetteraProv());

			if (!dto.getNumPratica().equals(""))
				map.put("numPratica", dto.getNumPratica());
			else
				map.put("numPratica", "");

			map.put("flgPrenotata", dto.getFlgPrenotata());
			map.put("motivoPrenotazione", dto.getMotivoPrenotazione());
			map.put("noteRiscossione", dto.getNoteRiscossione());

			try {
				String dataIniConcessione = dto.getDataIniConcessione();
				if (dataIniConcessione != null) {
					if (!dataIniConcessione.equals("")) {

						Date dataInConc = formatter.parse(dataIniConcessione);
						map.put("dataIniConcessione", dataInConc);
					} else {
						map.put("dataIniConcessione", null);
					}
				} else {
					map.put("dataIniConcessione", null);
				}

				String dataScadConcessione = dto.getDataScadConcessione();
				if (dataScadConcessione != null) {
					if (!dataScadConcessione.equals("")) {
						Date dataScadConc = formatter.parse(dataScadConcessione);
						map.put("dataScadConcessione", dataScadConc);
					} else {
						map.put("dataScadConcessione", null);
					}
				} else {
					map.put("dataScadConcessione", null);
				}

				String dataIniSospCanone = dto.getDataIniSospCanone();
				if (dataIniSospCanone != null) {
					if (!dataIniSospCanone.equals("")) {
						Date dataIniSospCan = formatter.parse(dataIniSospCanone);
						map.put("dataIniSospCanone", dataIniSospCan);
					} else {
						map.put("dataIniSospCanone", null);
					}
				} else {
					map.put("dataIniSospCanone", null);
				}

				String dataFineSospCanone = dto.getDataFineSospCanone();
				if (dataFineSospCanone != null) {
					if (!dataFineSospCanone.equals("")) {

						Date dataFineSospCan = formatter.parse(dataFineSospCanone);
						map.put("dataFineSospCanone", dataFineSospCan);
					} else {
						map.put("dataFineSospCanone", null);
					}
				} else {
					map.put("dataFineSospCanone", null);
				}
				
				String dataRinunciaRevocaST = dto.getDataRinunciaRevoca();
				if (dataRinunciaRevocaST != null) {
					if (!dataRinunciaRevocaST.equals("")) {

						Date dataRinunciaRevoca = formatter.parse(dataRinunciaRevocaST);
						map.put("dataRinunciaRevoca", dataRinunciaRevoca);
					} else {
						map.put("dataRinunciaRevoca", null);
					}
				} else {
					map.put("dataRinunciaRevoca", null);
				}
				
			} catch (ParseException e) {
				LOGGER.error("[RiscossioneTributiDAOImpl::updateRiscossione] ERROR ParseException: " + e);
			}

			map.put("jsonDt", dto.getJsonDt());

			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
//            String uid = generateGestUID(dto.getCfAccredito() + dto.getDesEmailAccredito() + now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));

			MapSqlParameterSource params = getParameterValue(map);

			Long idSoggettoOld = template.query(QUERY_SELECT_ID_SOGGETTO_BY_ID_RISCOSSIONE, params,
					new ResultSetExtractor<Long>() {
						@Override
						public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
							Long idSoggettoOld = null;
							while (rs.next()) {
								idSoggettoOld = rs.getLong("id_soggetto");
							}
							return idSoggettoOld;
						}
					});
			if (!idSoggettoOld.equals(dto.getSoggetto().getIdSoggetto())) {
				Date dataInizioTitolarita = formatter.parse(formatter.format(now));
				map.put("dataInizioTitolarita", dataInizioTitolarita);
			} else {

				String dataInizioTitolaritaString = template.query(
						QUERY_SELECT_DATA_INIZIO_TITOLARITA_BY_ID_RISCOSSIONE, params,
						new ResultSetExtractor<String>() {
							@Override
							public String extractData(ResultSet rs) throws SQLException, DataAccessException {
								String dataInizioTitolaritaString = null;
								while (rs.next()) {
									dataInizioTitolaritaString = rs.getString("data_inizio_titolarita");
								}
								return dataInizioTitolaritaString;
							}
						});
				Date dataInizioTitolarita = formatter.parse(dataInizioTitolaritaString);
				map.put("dataInizioTitolarita", dataInizioTitolarita);
			}
			params = getParameterValue(map);
			int num = template.update(getQuery(QUERY_UPDATE_RISCOSSIONE, null, null), params);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			List<RiscossioneRecapitoDTO> recapiti = checkRiscossioneRecapiti(dto.getIdRiscossione());
			for (int i = 0; i < recapiti.size(); i++) {
				map.put("idRecapitoDEL", recapiti.get(i).getIdRecapito());
				params = getParameterValue(map);
				template.update(getQuery(QUERY_DELETE_RISCOSSIONE_RECAPITO, null, null), params, keyHolder,
						new String[] { "id_riscossione" });
			}
			boolean flgRecapPrinNewIns = true;
			boolean flgRecapAltNewIns = true;
			boolean flgRecapPrin = true;
			boolean flgRecapAlt = true;
			if (dto.getRecapitiRiscossione().size() > 0) {
				for (int i = 0; i < dto.getRecapitiRiscossione().size(); i++) {
					map.put("idRecapito", dto.getRecapitiRiscossione().get(i).getIdRecapito());
					map.put("gestAttoreIns", dto.getGestAttoreIns());
					map.put("gestDataIns", now);
					params = getParameterValue(map);
					template.update(getQuery(QUERY_INSERT_RISCOSSIONE_RECAPITO, null, null), params, keyHolder,
							new String[] { "id_riscossione" });
				}
			}
			Long idRiscossione = dto.getIdRiscossione();
//			cancello tutti i provvedimenti prima dell'insert
			provvedimentiIstanzeDAO.deleteProvvedimentiIstanzeByIdRiscossione(idRiscossione);
			
			int sizeArrayProvv = dto.getProvvedimento().size();
			for (int i = 0; i < sizeArrayProvv; i++) {
				if (dto.getProvvedimento().get(i).getIdProvvedimento() == null) {
					map.put("idRiscossione", dto.getIdRiscossione());
					map.put("idTipoTitolo",
							dto.getProvvedimento().get(i).getTipoTitoloExtendedDTO() != null
									? dto.getProvvedimento().get(i).getTipoTitoloExtendedDTO().getIdTipoTitolo()
									: null);
					map.put("idTipoProvvedimento",
							dto.getProvvedimento().get(i).getTipiProvvedimentoExtendedDTO().getIdTipoProvvedimento());
					map.put("numTitolo", dto.getProvvedimento().get(i).getNumTitolo());

					String dataProvvedimento = dto.getProvvedimento().get(i).getDataProvvedimento();
					if (dataProvvedimento != null) {
						if (!dataProvvedimento.equals("")) {
							Date dataProvv = formatter.parse(dataProvvedimento);
							map.put("dataProvvedimento", dataProvv);
						} else {
							map.put("dataProvvedimento", null);
						}
					} else {
						map.put("dataProvvedimento", null);
					}

					map.put("note", dto.getProvvedimento().get(i).getNote());
					map.put("gestDataIns", now);
					map.put("gestAttoreIns", dto.getGestAttoreIns());
					map.put("gestDataUpd", now);
					map.put("gestAttoreUpd", dto.getGestAttoreUpd());
					// String uid = generateGestUID(dto.getCfAccredito() +
					// dto.getDesEmailAccredito() + now);
					map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));

					MapSqlParameterSource paramsProvv = getParameterValue(map);
					KeyHolder keyHolderProvv = new GeneratedKeyHolder();

					template.update(getQuery(QUERY_INSERT_PROVVEDIMENTO, null, null), paramsProvv, keyHolderProvv,
							new String[] { "id_provvedimento" });
					Number keyIdProvvedimento = keyHolderProvv.getKey();
					Long Provvedimento = keyIdProvvedimento.longValue();
					dto.getProvvedimento().get(i).setIdProvvedimento(Provvedimento);
				}
			}

			return idRiscossione;
		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::updateRiscossione] Errore nell'accesso ai dati ", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception();
		} finally {
			LOGGER.debug("[RiscossioneTributiDAOImpl::updateRiscossione] END");
		}
	}
	private String mapSortConCampiDB(String sort) {
        String nomeCampo="";
		if(sort.contains("titolare")) {
			if(sort.substring(1).equals("titolare") && sort.charAt(0) == '-') 
				nomeCampo = sort.substring(0, 1).concat(" CASE \r\n"
						+ " WHEN rtgs.id_gruppo_soggetto is not null THEN rtgs.des_gruppo_soggetto \r\n"
						+ " WHEN ((rdts.cod_tipo_soggetto ='PF') and (rtgs.id_gruppo_soggetto is null) ) then ( rts.cognome|| ' ' ||  rts.nome ) \r\n"
						+ " WHEN (((rdts.cod_tipo_soggetto ='PG') or (rdts.cod_tipo_soggetto ='PB' )) and (rtgs.id_gruppo_soggetto is null) )THEN rts.den_soggetto \r\n"
						+ " END ");
			else
				nomeCampo = " CASE \r\n"
						+ " WHEN rtgs.id_gruppo_soggetto is not null THEN rtgs.des_gruppo_soggetto \r\n"
						+ " WHEN ((rdts.cod_tipo_soggetto ='PF') and (rtgs.id_gruppo_soggetto is null) ) then ( rts.cognome|| ' ' ||  rts.nome ) \r\n"
						+ " WHEN (((rdts.cod_tipo_soggetto ='PG') or (rdts.cod_tipo_soggetto ='PB' )) and (rtgs.id_gruppo_soggetto is null) )THEN rts.den_soggetto \r\n"
						+ " END ";
			return getQuerySortingSegment(nomeCampo);
		}
		if(sort.contains("numero")) {
			if(sort.substring(1).equals("numero") && sort.charAt(0) == '-')
				nomeCampo = sort.substring(0, 1).concat("num_pratica");
			else
				nomeCampo = "num_pratica";
	        return  getQuerySortingSegment(nomeCampo);
		}
		if(sort.contains("procedimento")) {
			if(sort.substring(1).equals("procedimento") && sort.charAt(0) == '-')
				nomeCampo = sort.substring(0, 1).concat("des_tipo_autorizza");
			else
				nomeCampo = "des_tipo_autorizza";
	        return  getQuerySortingSegment(nomeCampo);
		}
		if(sort.contains("stato")) {
			if(sort.substring(1).equals("stato") && sort.charAt(0) == '-')
				nomeCampo = sort.substring(0, 1).concat("des_stato_riscossione");	
			else if(sort.substring(1).equals("statoPratica") && sort.charAt(0) == '-')
				nomeCampo = sort.substring(0, 1).concat("des_stato_riscossione");	
			else
				nomeCampo = "des_stato_riscossione";
			
	        return  getQuerySortingSegment(nomeCampo);
		}
		if(sort.contains("scadenzaConcessione")) {
			if(sort.substring(1).equals("scadenzaConcessione") && sort.charAt(0) == '-')
				nomeCampo = sort.substring(0, 1).concat("data_scad_concessione");	
			else
				nomeCampo = "data_scad_concessione";
	        return  getQuerySortingSegment(nomeCampo);
		}
		if(sort.contains("codiceUtenza") || sort.isBlank() ) {
			if(sort.isBlank()) {
				nomeCampo = "cod_riscossione";
		        return  getQuerySortingSegment(nomeCampo);
			}
			if(sort.substring(1).equals("codiceUtenza") && sort.charAt(0) == '-')
			    nomeCampo = sort.substring(0, 1).concat("cod_riscossione");
			else
				nomeCampo = "cod_riscossione";
	        return  getQuerySortingSegment(nomeCampo);
		}
		  return nomeCampo;


	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<RiscossioneDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new RiscossioneRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * The type riscossione row mapper.
	 */
	public static class RiscossioneRowMapper implements RowMapper<RiscossioneDTO> {

		/**
		 * Instantiates a new Tipo riscossione row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RiscossioneRowMapper() throws SQLException {
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
		public RiscossioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RiscossioneDTO bean = new RiscossioneDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RiscossioneDTO bean) throws SQLException {
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setIdTipoRiscossione(rs.getLong("id_tipo_riscossione"));
			bean.setIdComponenteDt(rs.getLong("id_componente_dt"));
			bean.setIdStatoRiscossione(rs.getLong("id_stato_riscossione"));
			bean.setCodRiscossione(rs.getString("cod_riscossione"));
			bean.setCodRiscossioneProv(rs.getString("cod_riscossione_prov"));
			bean.setCodRiscossioneProg(rs.getString("cod_riscossione_prog"));
			bean.setIdSiglaRiscossione(rs.getLong("id_sigla_riscossione"));
			bean.setCodRiscossioneLetteraProv(rs.getString("cod_riscossione_lettera_prov"));
			bean.setNumPratica(rs.getString("num_pratica"));
			bean.setFlgPrenotata(rs.getInt("flg_prenotata"));
			bean.setMotivoPrenotazione(rs.getString("motivo_prenotazione"));
			bean.setNoteRiscossione(rs.getString("note_riscossione"));
			bean.setDataIniConcessione(rs.getString("data_ini_concessione"));
			bean.setDataScadConcessione(rs.getString("data_scad_concessione"));
			bean.setDataIniSospCanone(rs.getString("data_ini_sosp_canone"));
			bean.setDataFineSospCanone(rs.getString("data_fine_sosp_canone"));
			bean.setJsonDt(rs.getString("json_dt"));
			bean.setGestDataIns(rs.getTimestamp("gest_data_ins"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataUpd(rs.getTimestamp("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
			bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
			bean.setIdTipoAutorizza(rs.getLong("id_tipo_autorizza"));
		}
	}

	private String addJoinQueryFromDto(RiscossioneSearchDTO dto, String modalitaRicerca, String query) {
		LOGGER.debug("[RiscossioneTributiDAOImpl::addJoinQueryFromDto] BEGIN");
		String querySearch = query;
		if (dto.getIndirizzo() != null)
			querySearch = querySearch + " left join risca_r_recapito rrr   on rts.id_soggetto  = rrr.id_soggetto ";
		if (dto.getIdTipoTitolo() != null)
			querySearch = querySearch + " left join risca_d_tipo_titolo rdtt on rts.id_ambito = rdtt.id_ambito";
		if (dto.getComuneDiResidenza() != null) {
			querySearch = querySearch + " left join   risca_r_recapito rrr on rtr.id_soggetto  =  rrr.id_soggetto  "
					+ "	 left join   risca_d_comune rdc on rrr.id_comune_recapito  =  rdc.id_comune  ";
		}
		if (dto.getProvincia() != null) {
			if (dto.getComuneDiResidenza() != null) {
				querySearch = querySearch
						+ "  left join risca_d_provincia rdp  on rdc.id_provincia  =  rdp.id_provincia ";
			} else {
				querySearch = querySearch +  "	left join   risca_d_comune rdc on rts.id_comune_nascita  =  rdc.id_comune  "
						+ "  left join risca_d_provincia rdp  on rdc.id_provincia  =  rdp.id_provincia ";
			}

		}
		if (dto.getCodiceAvviso() != null || dto.getNap() != null) {
			querySearch = querySearch
					+ " left join RISCA_T_STATO_DEBITORIO rtsd   on rtsd.id_riscossione  = rtr.id_riscossione   "
					+ " left join RISCA_T_IUV rti   on rti.nap  = rtsd.nap  ";
		}
		if (dto.getCanone() != null) {
			querySearch = querySearch
					+ " inner join risca_r_rata_sd rrrs ON rtsd.id_stato_debitorio = rrrs.id_stato_debitorio ";
		}
		if (modalitaRicerca != null) {
			if(dto.getDatiTecnici() != null || dto.getAnnoCanone() != null) {
				querySearch = querySearch
						+ " inner join risca_r_annualita_sd rras   on rtsd.id_stato_debitorio = rras.id_stato_debitorio  ";

			}
		}
		LOGGER.debug("[RiscossioneTributiDAOImpl::addJoinQueryFromDto] END");
		return querySearch;
	}

	public RowMapper<RiscossioneSearchResultDTO> getRowMapperRiscossioneSearch() throws SQLException {
		return new RiscossioneSearchRowMapper();
	}

	/**
	 * The type RiscossioneSearch row mapper.
	 */
	public static class RiscossioneSearchRowMapper implements RowMapper<RiscossioneSearchResultDTO> {

		/**
		 * Instantiates a new RiscossioneSearchResultDTO row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RiscossioneSearchRowMapper() throws SQLException {
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
		public RiscossioneSearchResultDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RiscossioneSearchResultDTO bean = new RiscossioneSearchResultDTO();
			try {
				populateBean(rs, bean);
			} catch (SQLException | IOException e) {
				LOGGER.error(
						"[RiscossioneTributiDAOImpl::searchRiscossione::mapRow::populateBean] Errore nella popolazione bean",
						e);
			}
			return bean;
		}

		private void populateBean(ResultSet rs, RiscossioneSearchResultDTO bean)
				throws SQLException, JsonParseException, JsonMappingException, IOException {
			bean.setCodiceUtenza(rs.getString("cod_riscossione"));
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setNumeroPratica(rs.getString("num_pratica"));
			bean.setProcedimento(rs.getString("procedimento"));
			bean.setTitolare(rs.getString("titolare"));
			bean.setStato(rs.getString("des_stato_riscossione"));
			bean.setIdAmbito(rs.getLong("id_ambito"));
			bean.setDataScadConcessione(rs.getString("data_scad_concessione")); 
			DatiTecniciTributiDTO datiTecniciTributi = null;
			if (rs.getString("json_dt") != null) {
				datiTecniciTributi = creaDatiTecniciFromJsonDt(rs.getString("json_dt"), "dati_tecnici");
//				Set<String> nomeUso = datiTecniciTributi.getUsi().keySet();
//				bean.setPopolazione(datiTecniciTributi.getUsi().get(nomeUso).getPopolazione());

			}

		}

	}

	public List<RiscossioneRecapitoDTO> checkRiscossioneRecapiti(Long idRiscossione) {
		LOGGER.debug("[RiscossioneTributiDAOImpl::checkRiscossioneRecapiti] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();

			List<RiscossioneRecapitoDTO> risc = new ArrayList<RiscossioneRecapitoDTO>();
			map.put("idRiscossione", idRiscossione);

			MapSqlParameterSource params = getParameterValue(map);

			risc = template.query(QUERY_CHECK_RISCOSSIONE_RECAPITI, params, getRiscossioneRecapitoRowMapper());

			return risc;

		} catch (SQLException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::checkRiscossioneRecapiti] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::checkRiscossioneRecapiti] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[RiscossioneTributiDAOImpl::checkRiscossioneRecapiti] END");
		}
	}

	public static DatiTecniciTributiDTO creaDatiTecniciFromJsonDt(String jsonDt, String campoDaLeggere) {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		final JSONObject obj = new JSONObject(jsonDt);
		final JSONObject geodata = obj.getJSONObject("riscossione");
		String campo = "";
		DatiTecniciTributiDTO datiTecniciTributi = null;
		for (String key : obj.getJSONObject("riscossione").keySet()) {
			if (key.equals(campoDaLeggere))
				campo = geodata.getJSONObject(campoDaLeggere).toString();
		}
		try {
			datiTecniciTributi = mapper.readValue(campo, DatiTecniciTributiDTO.class);
		} catch (IOException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::searchRiscossione] Errore nella lettura dati json dt", e);
		}

		return datiTecniciTributi;
	}

	public RowMapper<RiscossioneRecapitoDTO> getRiscossioneRecapitoRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new RiscossioneRecapitoRowMapper();
	}

	/**
	 * The type riscossione recapito row mapper.
	 */
	public static class RiscossioneRecapitoRowMapper implements RowMapper<RiscossioneRecapitoDTO> {

		/**
		 * Instantiates a new riscossione recapito row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RiscossioneRecapitoRowMapper() throws SQLException {
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
		public RiscossioneRecapitoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RiscossioneRecapitoDTO bean = new RiscossioneRecapitoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RiscossioneRecapitoDTO bean) throws SQLException {
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setIdRecapito(rs.getLong("id_recapito"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
		}
	}

	public RowMapper<RecapitiExtendedDTO> getRecapitiRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new RecapitiRowMapper();
	}

	/**
	 * The type recapiti row mapper.
	 */
	public static class RecapitiRowMapper implements RowMapper<RecapitiExtendedDTO> {

		/**
		 * Instantiates a new recapiti row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RecapitiRowMapper() throws SQLException {
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
		public RecapitiExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RecapitiExtendedDTO bean = new RecapitiExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RecapitiDTO bean) throws SQLException {
			bean.setIdRecapito(rs.getLong("id_recapito"));
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
			bean.setIdTipoRecapito(rs.getLong("id_tipo_recapito"));
			bean.setIdTipoInvio(rs.getLong("id_tipo_invio"));
			bean.setIdComuneRecapito(rs.getLong("id_comune_recapito"));
			bean.setIdNazioneRecapito(rs.getLong("id_nazione_recapito"));
			bean.setIdFonte(rs.getLong("id_fonte"));
			bean.setIdFonteOrigine(rs.getLong("id_fonte_origine"));
			bean.setCodRecapito(rs.getString("cod_recapito"));
			bean.setCodRecapitoFonte(rs.getString("cod_recapito_fonte"));
			bean.setIndirizzo(rs.getString("indirizzo"));
			bean.setNumCivico(rs.getString("num_civico"));
			bean.setEmail(rs.getString("email"));
			bean.setPec(rs.getString("pec"));
			bean.setTelefono(rs.getString("telefono"));
			bean.setPresso(rs.getString("presso"));
			bean.setCittaEsteraRecapito(rs.getString("citta_estera_recapito"));
			bean.setCapRecapito(rs.getString("cap_recapito"));
			bean.setDesLocalita(rs.getString("des_localita"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			bean.setCellulare(rs.getString("cellulare"));
			bean.setIdTipoSede(rs.getLong("id_tipo_sede"));
			bean.setDataAggiornamento(rs.getString("data_aggiornamento"));
			bean.setDataCancellazione(rs.getString("data_cancellazione"));
		}

	}

	public RowMapper<GruppiDTO> getGruppoRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new GruppoRowMapper();
	}

	/**
	 * The type gruppo row mapper.
	 */
	public static class GruppoRowMapper implements RowMapper<GruppiDTO> {

		/**
		 * Instantiates a new gruppo row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public GruppoRowMapper() throws SQLException {
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
		public GruppiDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			GruppiDTO bean = new GruppiDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, GruppiDTO bean) throws SQLException {
			bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
			bean.setCodGruppoSoggetto(rs.getString("cod_gruppo_soggetto"));
			bean.setCodGruppoFonte(rs.getString("cod_gruppo_fonte"));
			bean.setDesGruppoSoggetto(rs.getString("des_gruppo_soggetto"));
			bean.setIdFonte(rs.getLong("id_fonte"));
			bean.setIdFonteOrigine(rs.getLong("id_fonte_origine"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			bean.setDataAggiornamento(rs.getString("data_aggiornamento"));
			bean.setDataCancellazione(rs.getString("data_cancellazione"));
		}
	}

	public Integer checkNumPratica(String numPratica) {
		LOGGER.debug("[RiscossioneTributiDAOImpl::checkNumPratica] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();

			Long idAmbito = 1L;

			Integer counter = 0;

			String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

			List<TipoRiscossioneExtendedDTO> tipiRiscossione = new ArrayList<>();
			try {
				tipiRiscossione = tipoRiscossioneDaoImpl.loadTipiRiscossioneByIdAmbitoAndDateValidita(idAmbito, null,
						null);
			} catch (ParseException e) {
				LOGGER.error("[RiscossioneTributiDAOImpl::checkNumPratica] Errore nell'esecuzione della query", e);
			}
			if(tipiRiscossione.isEmpty()) {
				return counter;
			}
			List<RiscossioneDTO> risc = new ArrayList<RiscossioneDTO>();
			map.put("numPratica", numPratica);
			for (int i = 0; i < tipiRiscossione.size(); i++) {
				Long idTipoRiscossione = tipiRiscossione.get(i).getIdTipoRiscossione();

				map.put("numPratica", numPratica);
				map.put("idTipoRiscossione", idTipoRiscossione);
				MapSqlParameterSource params = getParameterValue(map);

				risc = template.query(QUERY_CHECK_NUM_PRATICA, params, getRowMapper());
				if (risc != null && risc.size() > 0) {
					counter++;
				}
			}

			return counter;

		} catch (SQLException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::checkNumPratica] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::checkNumPratica] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[RiscossioneTributiDAOImpl::checkNumPratica] END");
		}
	}

	public Integer checkCodRiscossione(String codRiscossione) {
		LOGGER.debug("[RiscossioneTributiDAOImpl::checkCodRiscossione] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();

			Long idAmbito = 1L;

			Integer counter = 0;

			String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

			List<TipoRiscossioneExtendedDTO> tipiRiscossione = null;
			try {
				tipiRiscossione = tipoRiscossioneDaoImpl.loadTipiRiscossioneByIdAmbitoAndDateValidita(idAmbito, null,
						null);
			} catch (ParseException e) {
				LOGGER.error("[RiscossioneTributiDAOImpl::checkCodRiscossione] Errore nell'esecuzione della query", e);
			}

			List<RiscossioneDTO> risc = new ArrayList<RiscossioneDTO>();
			map.put("codRiscossione", codRiscossione);
			for (int i = 0; i < tipiRiscossione.size(); i++) {
				Long idTipoRiscossione = tipiRiscossione.get(i).getIdTipoRiscossione();

				map.put("codRiscossione", codRiscossione);
				map.put("idTipoRiscossione", idTipoRiscossione);
				MapSqlParameterSource params = getParameterValue(map);

				risc = template.query(QUERY_CHECK_COD_UTENZA, params, getRowMapper());
				if (risc != null && risc.size() > 0) {
					counter++;
				}
			}

			return counter;

		} catch (SQLException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::checkCodRiscossione] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::checkCodRiscossione] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[RiscossioneTributiDAOImpl::checkCodRiscossione] END");
		}
	}

	public RowMapper<ComuneExtendedDTO> getComuneRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new ComuniRowMapper();
	}

	/**
	 * The type Comune row mapper.
	 */
	public static class ComuniRowMapper implements RowMapper<ComuneExtendedDTO> {

		/**
		 * Instantiates a new Provincia row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public ComuniRowMapper() throws SQLException {
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
		public ComuneExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ComuneExtendedDTO bean = new ComuneExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, ComuneExtendedDTO bean) throws SQLException {
			bean.setIdComune(rs.getLong("id_comune"));
			bean.setCodIstatComune(rs.getString("cod_istat_comune"));
			bean.setCodBelfioreComune(rs.getString("cod_belfiore_comune"));
			bean.setDenomComune(rs.getString("denom_comune"));
			bean.setIdProvincia(rs.getLong("id_provincia"));
			bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
			bean.setDataFineValidita(rs.getDate("data_fine_validita"));
			bean.setDtIdComune(rs.getLong("dt_id_comune"));
			bean.setDtIdComunePrev(rs.getLong("dt_id_comune_prev"));
			bean.setDtIdComuneNext(rs.getLong("dt_id_comune_next"));
			bean.setCapComune(rs.getString("cap_comune"));
			ProvinciaExtendedDTO ProvinciaExtendedDTO = new ProvinciaExtendedDTO();
			populateBeanProvincia(rs, ProvinciaExtendedDTO);
			bean.setProvincia(ProvinciaExtendedDTO);

		}

		private void populateBeanProvincia(ResultSet rs, ProvinciaExtendedDTO bean) throws SQLException {
			bean.setIdProvincia(rs.getLong("id_provincia"));
			bean.setCodProvincia(rs.getString("cod_provincia"));
			bean.setDenomProvincia(rs.getString("denom_provincia"));
			bean.setSiglaProvincia(rs.getString("sigla_provincia"));
			bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
			bean.setDataFineValidita(rs.getDate("data_fine_validita"));

			RegioneExtendedDTO regione = new RegioneExtendedDTO();
			populateBeanRegione(rs, regione);
			bean.setRegione(regione);
		}

		private void populateBeanRegione(ResultSet rs, RegioneExtendedDTO bean) throws SQLException {
			bean.setIdRegione(rs.getLong("id_regione"));
			bean.setCodRegione(rs.getString("cod_regione"));
			bean.setDenomRegione(rs.getString("denom_regione"));
			// bean.setIdNazione(rs.getLong("id_nazione"));
			bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
			bean.setDataFineValidita(rs.getDate("data_fine_validita"));

			NazioneDTO nazione = new NazioneDTO();
			populateBeanNazione(rs, nazione);
			bean.setNazione(nazione);
		}

		private void populateBeanNazione(ResultSet rs, NazioneDTO bean) throws SQLException {
			bean.setIdNazione(rs.getLong("id_nazione"));
			bean.setCodIstatNazione(rs.getString("cod_istat_nazione"));
			bean.setCodBelfioreNazione(rs.getString("cod_belfiore_nazione"));
			bean.setDenomNazione(rs.getString("denom_nazione"));
			bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
			bean.setDataFineValidita(rs.getDate("data_fine_validita"));
			bean.setDtIdStato(rs.getLong("dt_id_stato"));
			bean.setDtIdStatoPrev(rs.getLong("dt_id_stato_prev"));
			bean.setDtIdStatoNext(rs.getLong("dt_id_stato_next"));
			bean.setCodIso2(rs.getString("cod_iso2"));

		}
	}

	public Integer countRiscossioni(RiscossioneSearchDTO dto, long idAmbito, String modalitaRicerca) throws Exception {

		LOGGER.debug("[RiscossioneTributiDAOImpl::countRiscossioni] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params;
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
			String queySearch = "";
			String whereCond = "";
			if (modalitaRicerca != null && "statoDebitorio".equalsIgnoreCase(modalitaRicerca)) {
//				crea query per lo SD 
				queySearch = QUERY_COUNT_RISCOSSIONI;
				String querySd = "where  rtr.id_riscossione in ( "
						+ addJoinQueryFromDto(dto, modalitaRicerca, QUERY_SEARCH_RISCOSSIONI_BY_SD)
						+ creaWhereConditionSearch(map, formatter, dto, idAmbito, formatter2, true) + " ) ";
				whereCond = querySd + QUERY_GROUP_BY_SEARCH_RISCOSSIONI;

			} else {
				queySearch = addJoinQueryFromDto(dto, modalitaRicerca, QUERY_COUNT_RISCOSSIONI);
				whereCond = creaWhereConditionSearch(map, formatter, dto, idAmbito, formatter2, false);
			}

			params = getParameterValue(map);
			return template.queryForObject(queySearch + whereCond + " ) AS ricerca", params, Integer.class);

		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::countRiscossioni] Errore nell'accesso ai dati ", e);
			throw new Exception(e);
		} catch (ParseException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::countRiscossioni] Errore parse  ", e);
			throw new Exception(e);
		} catch (JsonParseException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::countRiscossioni] Errore Json Parse  ", e);
			throw new Exception(e);
		} catch (JsonMappingException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::countRiscossioni] Errore Json Mapping  ", e);
			throw new Exception(e);
		} catch (IOException e) {
			LOGGER.error("[RiscossioneTributiDAOImpl::countRiscossioni] Errore generale ", e);
			throw new Exception(e);
		} finally {
			LOGGER.debug("[RiscossioneTributiDAOImpl::countRiscossioni] END");
		}
	}

	public List<RiscossioneSearchResultDTO> searchRiscossioniDelegati(RiscossioneSearchDTO dto, Long idAmbito,
			String modalitaRicerca, Integer offset, Integer limit, String sort) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			if(modalitaRicerca == null)
				return Collections.emptyList();
			
			Map<String, Object> map = new HashMap<>();
			List<RiscossioneSearchResultDTO> riscossioneSearchResultDTO = new ArrayList<>();
			map.put("cfDelegato", dto.getCodiceFiscale());
			map.put("idAmbito", idAmbito);
		    MapSqlParameterSource params = getParameterValue(map);
			String dynamicOrderByCondition= mapSortConCampiDB(sort);
			
		    riscossioneSearchResultDTO = template.query(getQuery(QUERY_SELECT_RISCOSSIONI_BY_CF_DELEGATO+ dynamicOrderByCondition, offset!= null ? offset.toString(): null ,  limit != null ? limit.toString(): null), params,
					getRowMapperRiscossioneSearch());

			return riscossioneSearchResultDTO;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(getClassFunctionDebugString(className, methodName," NO DATA FOUND"));
			return Collections.emptyList();
		}  catch (SQLException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e), e);
            throw new Exception(e);
		} catch (DataAccessException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e), e);
            throw new Exception(e);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	public Integer countRiscossioniDelegati(RiscossioneSearchDTO riscossioneSearch, long idAmbito, String modalitaRicerca) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			map.put("cfDelegato", riscossioneSearch.getCodiceFiscale());
		    MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(QUERY_COUNT_RISCOSSIONI_BY_CF_DELEGATO +" ) AS ricerca", params, Integer.class);

		} catch (DataAccessException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e), e);
			throw new Exception(e);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

}
