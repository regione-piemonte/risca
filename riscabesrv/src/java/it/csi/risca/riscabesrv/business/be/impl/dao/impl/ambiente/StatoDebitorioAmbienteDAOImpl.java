/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.impl.dao.AccertamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaUsoSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AttivitaStatoDebitorioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoSdUtilizzatoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoContribuzioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.UsoRidaumSdDAO;
import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdDTO;
import it.csi.risca.riscabesrv.dto.AttivitaStatoDebitorioDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagExtendedDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RimborsoDTO;
import it.csi.risca.riscabesrv.dto.RimborsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RimborsoSdUtilizzatoDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneDTO;
import it.csi.risca.riscabesrv.dto.StatoContribuzioneDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.UsoRidaumSdExtendedDTO;
import it.csi.risca.riscabesrv.dto.UtRimbDTO;
import it.csi.risca.riscabesrv.dto.VerifyStatoDebitorioDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type StatoDebitorio dao.
 *
 * @author CSI PIEMONTE
 */
public class StatoDebitorioAmbienteDAOImpl extends RiscaBeSrvGenericDAO<StatoDebitorioDTO> {


	@Autowired
	private AttivitaStatoDebitorioDAO attivitaStatoDebitorioDAO;
	
	@Autowired
	private StatoContribuzioneDAO statoContribuzioneDAO;
	
	@Autowired
	private AnnualitaSdDAO annualitaSdDAO;
	
	@Autowired
	private AnnualitaUsoSdDAO annualitaUsoSdDAO;
	
	@Autowired
	private RimborsoDAO rimborsoDAO;
	
    @Autowired
    private RimborsoSdUtilizzatoDAO rimborsoSdUtilizzatoDAO;
    
	@Autowired
	private UsoRidaumSdDAO usoRidaumSdDAO;
	
	@Autowired
	private RataSdDAO  rataSdDAO;
	
	@Autowired
	private DettaglioPagDAO dettaglioPagDAO;
	
	@Autowired 
	private RiscossioneDAO riscossioneDAO;
	
	@Autowired 
	private AccertamentoDAO accertamentoDAO;
	
	@Autowired 
	private MessaggiDAO messaggiDAO;
	
	public static final String QUERY_INSERT = "INSERT INTO risca_t_stato_debitorio "
			+ "(id_stato_debitorio, id_riscossione, des_tipo_titolo, id_soggetto, id_gruppo_soggetto, id_recapito, "
			+ "id_attivita_stato_deb, id_stato_contribuzione, id_tipo_dilazione, nap, num_titolo, data_provvedimento, "
			+ "num_richiesta_protocollo, data_richiesta_protocollo, data_ultima_modifica, des_usi, note, "
			+ "imp_recupero_canone, imp_recupero_interessi, imp_spese_notifica, imp_compensazione_canone, "
			+ "desc_periodo_pagamento, desc_motivo_annullo, flg_annullato, flg_restituito_mittente, flg_invio_speciale, "
			+ "flg_dilazione, flg_addebito_anno_successivo, gest_attore_ins,  gest_data_ins, gest_attore_upd, gest_data_upd, "
			+ " gest_uid, nota_rinnovo) "
			+ "VALUES(:idStatoDebitorio, :idRiscossione, :desTipoTitolo, :idSoggetto, :idGruppoSoggetto, :idRecapito, "
			+ ":idAttivitaStatoDeb, :idStatoContribuzione, :idTipoDilazione, :nap, :numTitolo, :dataProvvedimento, "
			+ ":numRichiestaProtocollo, :dataRichiestaProtocollo, :dataUltimaModifica, :desUsi, :note, :impRecuperoCanone, "
			+ ":impRecuperoInteressi, :impSpeseNotifica, :impCompensazioneCanone, :descPeriodoPagamento, :descMotivoAnnullo, "
			+ ":flgAnnullato, :flgRestituitoMittente, :flgInvioSpeciale, :flgDilazione, :flgAddebitoAnnoSuccessivo, "
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid, :notaRinnovo) ";
	
	public static final String QUERY_INSERT_W = "INSERT INTO risca_w_stato_debitorio "
			+ "(id_stato_debitorio, id_riscossione, des_tipo_titolo, id_soggetto, id_gruppo_soggetto, id_recapito, "
			+ "id_attivita_stato_deb, id_stato_contribuzione, id_tipo_dilazione, nap, num_titolo, data_provvedimento, "
			+ "num_richiesta_protocollo, data_richiesta_protocollo, data_ultima_modifica, des_usi, note, "
			+ "imp_recupero_canone, imp_recupero_interessi, imp_spese_notifica, imp_compensazione_canone, "
			+ "desc_periodo_pagamento, desc_motivo_annullo, flg_annullato, flg_restituito_mittente, flg_invio_speciale, "
			+ "flg_dilazione, flg_addebito_anno_successivo) "
			+ "VALUES(:idStatoDebitorio, :idRiscossione, :desTipoTitolo, :idSoggetto, :idGruppoSoggetto, :idRecapito, "
			+ ":idAttivitaStatoDeb, :idStatoContribuzione, :idTipoDilazione, :nap, :numTitolo, :dataProvvedimento, "
			+ ":numRichiestaProtocollo, :dataRichiestaProtocollo, :dataUltimaModifica, :desUsi, :note, :impRecuperoCanone, "
			+ ":impRecuperoInteressi, :impSpeseNotifica, :impCompensazioneCanone, :descPeriodoPagamento, :descMotivoAnnullo, "
			+ ":flgAnnullato, :flgRestituitoMittente, :flgInvioSpeciale, :flgDilazione, :flgAddebitoAnnoSuccessivo)";
	
	public static final String QUERY_INSERT_UPD_DA_COMPENSARE = "insert into RISCA_W_STATO_DEBITORIO_UPD "
			+ "select id_stato_debitorio, id_riscossione, des_tipo_titolo, id_soggetto, id_gruppo_soggetto, id_recapito,  "
			+ "id_attivita_stato_deb, id_stato_contribuzione, id_tipo_dilazione, nap, num_titolo, data_provvedimento,  "
			+ "num_richiesta_protocollo, data_richiesta_protocollo, data_ultima_modifica, des_usi, note,  "
			+ "imp_recupero_canone, imp_recupero_interessi, imp_spese_notifica, imp_compensazione_canone,  "
			+ "desc_periodo_pagamento, desc_motivo_annullo, flg_annullato, flg_restituito_mittente, flg_invio_speciale,  "
			+ "flg_dilazione, flg_addebito_anno_successivo "
			+ "from RISCA_T_STATO_DEBITORIO "
			+ "where id_stato_debitorio IN ( "
			+ "   select id_stato_debitorio "
			+ "   from RISCA_R_RIMBORSO a, risca_d_tipo_rimborso b "
			+ "   where a.id_tipo_rimborso = b.id_tipo_rimborso "
			+ "   and b.cod_tipo_rimborso = 'DA_COMPENSARE')";
	
	public static final String QUERY_UPDATE = "update risca_t_stato_debitorio "
			+ " set id_riscossione = :idRiscossione, des_tipo_titolo = :desTipoTitolo, id_soggetto =:idSoggetto, "
			+ "id_gruppo_soggetto = :idGruppoSoggetto, id_recapito =:idRecapito, "
			+ "id_attivita_stato_deb = :idAttivitaStatoDeb, id_stato_contribuzione = :idStatoContribuzione, id_tipo_dilazione = :idTipoDilazione, "
			+ "nap = :nap, num_titolo = :numTitolo, data_provvedimento = :dataProvvedimento, "
			+ "num_richiesta_protocollo = :numRichiestaProtocollo, data_richiesta_protocollo = :dataRichiestaProtocollo, "
			+ "data_ultima_modifica = :dataUltimaModifica, des_usi = :desUsi, note = :note, "
			+ "imp_recupero_canone = :impRecuperoCanone, imp_recupero_interessi = :impRecuperoInteressi, "
			+ "imp_spese_notifica = :impSpeseNotifica, imp_compensazione_canone = :impCompensazioneCanone, "
			+ "desc_periodo_pagamento = :descPeriodoPagamento, desc_motivo_annullo = :descMotivoAnnullo, "
			+ "flg_annullato = :flgAnnullato, flg_restituito_mittente = :flgRestituitoMittente, flg_invio_speciale = :flgInvioSpeciale, "
			+ "flg_dilazione = :flgDilazione, flg_addebito_anno_successivo = :flgAddebitoAnnoSuccessivo,"
			+ " gest_data_upd = :gestDataUpd , "
			+ "gest_attore_upd= :gestAttoreUpd, nota_rinnovo = :notaRinnovo "
			
			+ " where id_stato_debitorio  = :idStatoDebitorio ";

	
	public static final String QUERY_UPDATE_UPD_SD_REGOLARIZZATO = "update RISCA_W_STATO_DEBITORIO_UPD "
			+ " set id_attivita_stato_deb = NULL, "
			+ " id_stato_contribuzione = (select id_stato_contribuzione from risca_d_stato_contribuzione where cod_stato_contribuzione =  'RO') "
			+ " where id_stato_debitorio = :idStatoDebitorio";
	
	public static final String QUERY_UPDATE_WORKING_COMPENSAZIONE = "update RISCA_W_STATO_DEBITORIO "
			+ " set imp_compensazione_canone = COALESCE(imp_compensazione_canone, 0) +  COALESCE(:compensazione, 0) "
			+ " where id_stato_debitorio  = :idStatoDebitorio ";
	
	public static final String QUERY_SELECT_ALL_STATI_DEBITORI = "select  rtsd.* from RISCA_T_STATO_DEBITORIO rtsd ";
	
	public static final String QUERY_SELECT_STATI_DEBITORI_BY_ID_STATO_DEBITORIO = "select rtsd.* from RISCA_T_STATO_DEBITORIO rtsd "
			+ "where rtsd.id_stato_debitorio = :idStatoDebitorio";
	
	public static final String QUERY_SELECT_STATI_DEBITORI_BY_ID_RISCOSSIONE = "select  rtsd.* from RISCA_T_STATO_DEBITORIO rtsd "
			+ " where rtsd.id_riscossione = :idRiscossione";
	
	public static final String QUERY_SELECT_MAX_DATA_PAGAMENTO_BY_ID_STATO_DEBITORIO ="select max(rtp.data_op_val) as data_pagamento from RISCA_T_STATO_DEBITORIO rtsd "
			+" inner join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio "
			+" inner join risca_r_dettaglio_pag rrdp on rrrs.id_rata_sd = rrdp.id_rata_sd "
			+" inner join risca_t_pagamento rtp on rrdp.id_pagamento = rtp.id_pagamento "
			+" where rtsd.id_stato_debitorio = :idStatoDebitorio  and rrrs.id_rata_sd_padre is null ";
	
	public static final String QUERY_SELECT_STATI_DEB_WITH_FLG_INVIO_SPECIALE = "select count(*) from risca_t_stato_debitorio rtsd "
			+ "where rtsd.id_riscossione = :idRiscossione and rtsd.flg_invio_speciale = 1";
	
	public static final String QUERY_SELECT_STATI_DEB_WITH_FLG_INVIO_SPECIALE_BY_ID_STATO_DEB = "select count(*) from risca_t_stato_debitorio rtsd "
			+ "where rtsd.id_riscossione = :idRiscossione and rtsd.flg_invio_speciale = 1 and rtsd.id_stato_debitorio = :idStatoDebitorio";
	
	public static final String QUERY_SELECT_DATA_SCAD_PAG_BY_ID_RATA = "select rrrsd.data_scadenza_pagamento from risca_r_rata_sd rrrsd where rrrsd.id_rata_sd = :idRataSd";
	
	public static final String QUERY_SELECT_STATO_DEB_BY_NAP = "select count(*) from risca_t_stato_debitorio rtsd where rtsd.nap = :nap";
	
	public static final String QUERY_SELECT_UT_RIMBO_BY_ID_RIMBORSO ="select rtr.cod_riscossione , rtsd.desc_periodo_pagamento  from risca_r_rimborso_sd_utilizzato rrrsu "
			+ "join risca_t_stato_debitorio rtsd on rrrsu.id_stato_debitorio = rtsd.id_stato_debitorio "
			+ "join risca_t_riscossione rtr on rtsd.id_riscossione = rtr.id_riscossione "
			+ "WHERE rrrsu.id_rimborso  = :idRimborso";
	
	public static final String QUERY_SELECT_STATI_DEBITORI_BY_NAP ="select  rtsd.* from RISCA_T_STATO_DEBITORIO rtsd "
			+ " inner join risca_t_riscossione rtr on rtsd.id_riscossione = rtr.id_riscossione "
			+ "where rtsd.nap = :nap";
	
	public static final String QUERY_SELECT_STATI_DEBITORI_BY_NAP_GOUP_BY_COD_RISCOSSIONE ="select count(numero_utenza) numero_utenza from (select  count(rtr.cod_riscossione) numero_utenza from RISCA_T_STATO_DEBITORIO rtsd "
			+ "	 inner join risca_t_riscossione rtr on rtsd.id_riscossione = rtr.id_riscossione "
			+ "	where rtsd.nap = :nap"
			+ "	group by rtr.cod_riscossione) t  ";
	
	
	public static final String QUERY_SELECT_NOTA_RINNOVO=" select max(rrp.data_provvedimento) data_provvedimento from risca_r_provvedimento rrp "
			+"join risca_d_tipo_provvedimento rdtp  on rrp.id_tipo_provvedimento = rdtp.id_tipo_provvedimento "
			+"where rrp.id_riscossione  = :idRiscossione "
			+"and rdtp.cod_tipo_provvedimento ='IST_RINNOVO' "
			+"and rdtp.flg_istanza = 1 ";
	
	public static final String QUERY_SELECT_DESC_TIPO_TITOLO ="select rdtt.des_tipo_titolo "
			+"from  risca_r_provvedimento rrp "
			+"left join risca_d_tipo_titolo rdtt on rrp.id_tipo_titolo = rdtt.id_tipo_titolo "
			+"where rrp.id_provvedimento  = :idProvvedimento ";
	
	public static final String QUERY_SELECT_SD_ANNO_PRECEDENTE = "select sd.* "
			+ " from RISCA_T_STATO_DEBITORIO sd, "
			+ "	(select MAX(id_stato_debitorio) max_id, id_riscossione "
			+ "	 from RISCA_T_STATO_DEBITORIO "
			+ "	 where id_riscossione = :idRiscossione "
			+ "	 group by id_riscossione "
			+ "	) st_deb_max "
			+ " where sd.id_riscossione = :idRiscossione "
			+ "     and sd.id_stato_debitorio = st_deb_max.max_id "
			+ "     and sd.id_riscossione = st_deb_max.id_riscossione "
			+ "     and sd.flg_addebito_anno_successivo = 1";
	
	public static final String QUERY_UPDATE_NOTA_RINNOVO_WORKING = " update RISCA_W_STATO_DEBITORIO "
			+ " set nota_rinnovo = :notaRinnovo "
			+ " where id_stato_debitorio = :idStatoDebitorio ";
	
	public static final String QUERY_UPDATE_SD_FROM_SD_UPD = "update RISCA_T_STATO_DEBITORIO a "
			+ " set (id_attivita_stato_deb, id_stato_contribuzione, gest_attore_upd, gest_data_upd) = "
			+ " ( "
			+ "     select id_attivita_stato_deb, id_stato_contribuzione, :gestAttoreUpd, to_timestamp(:gestDataUpd, 'YYYY-MM-DD HH24:MI:SS.MS') "
			+ "     from RISCA_W_STATO_DEBITORIO_UPD b "
			+ "     where a.id_stato_debitorio = b.id_stato_debitorio "
			+ " ) "
			+ " where a.id_stato_debitorio IN (select id_stato_debitorio from RISCA_W_STATO_DEBITORIO_UPD) ";
	
	public static final String  QUERY_SELECT_NUMBER_REPETETION_NAP = "select  COUNT(rtsd.*) as num_ripetizioni_nap from RISCA_T_STATO_DEBITORIO rtsd "
			+ "where rtsd.nap = :nap";
	
	public static final String QUERY_SELECT_SD_WORKING_BY_NAP = " select * from risca_w_stato_debitorio where nap = :nap order by nap ";
	
	public static final String QUERY_UPDATE_SD_FROM_WORKING = " update RISCA_T_STATO_DEBITORIO "
			+ " set nap = :nap, "
			+ " flg_invio_speciale = 0, "
			+ " num_richiesta_protocollo = :numRichiestaProtocollo, "
			+ " data_richiesta_protocollo = :dataRichiestaProtocollo, "
			+ " imp_compensazione_canone = :impCompensazioneCanone, "
			+ " gest_attore_upd = :gestAttoreUpd, "
			+ " gest_data_upd = :gestDataUpd "
			+ " where id_stato_debitorio = :idStatoDebitorio";
	
	private static final String QUERY_DELETE_SD_WORKING_BY_SD = " delete from RISCA_W_STATO_DEBITORIO where id_stato_debitorio = :idStatoDebitorio ";
	
	private static final String QUERY_DELETE_SD_UPD_WORKING_BY_SD = " delete from risca_w_stato_debitorio_upd where id_stato_debitorio = :idStatoDebitorio ";

	private static final String QUERY_SELECT_IMPORTO_VERSATO_BY_ID_STATO_DEBITORIO =  "select SUM(rrdp.importo_versato) importo_versato from risca_r_dettaglio_pag rrdp "
			+ "inner join risca_t_pagamento rtp "
			+ "on rtp.id_pagamento = rrdp.id_pagamento "
			+ "inner join risca_r_rata_sd rrrs   "
			+ "on rrrs.id_rata_sd  = rrdp.id_rata_sd "
			+ "inner join risca_t_stato_debitorio rtsd    "
			+ "on rtsd.id_stato_debitorio  = rrrs.id_stato_debitorio  "
			+ "where rrrs.id_rata_sd_padre is null "
			+ "and rtsd.id_stato_debitorio = :idStatoDebitorio";
	
	
	public StatoDebitorioExtendedDTO saveStatoDebitorio(StatoDebitorioExtendedDTO dto) throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] BEGIN");
		try {
			dto = saveStatoDebitorio(dto, false, false, true);
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::saveStatoDebitorio] ERROR : ", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] END");
		}
		return dto;
	}

	public StatoDebitorioExtendedDTO updateStatoDebitorio(StatoDebitorioExtendedDTO dto) throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorio] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
	        map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
			map.put("idRiscossione", dto.getIdRiscossione());
    		map.put("desTipoTitolo", dto.getTipoTitolo());
			map.put("idSoggetto", dto.getIdSoggetto());
			map.put("idGruppoSoggetto", dto.getIdGruppoSoggetto());
			map.put("idRecapito", dto.getIdRecapito());
			map.put("idAttivitaStatoDeb", dto.getIdAttivitaStatoDeb() != null ? dto.getIdAttivitaStatoDeb() :  dto.getAttivitaStatoDeb() != null ? dto.getAttivitaStatoDeb().getIdAttivitaStatoDeb() : null);
			map.put("idStatoContribuzione", dto.getIdStatoContribuzione() != null ? dto.getIdStatoContribuzione() : dto.getStatoContribuzione() != null ? dto.getStatoContribuzione().getIdStatoContribuzione() : null );
			map.put("idTipoDilazione", dto.getIdTipoDilazione());
			map.put("nap", dto.getNap());
			map.put("numTitolo", dto.getNumTitolo());
			map.put("dataProvvedimento", dto.getDataProvvedimento());
			map.put("numRichiestaProtocollo", dto.getNumRichiestaProtocollo());
			map.put("dataRichiestaProtocollo", dto.getDataRichiestaProtocollo());
			map.put("dataUltimaModifica", dto.getDataUltimaModifica());
			map.put("desUsi", dto.getDesUsi());
			map.put("note", dto.getNote());
			map.put("impRecuperoCanone", dto.getImpRecuperoCanone());
			map.put("impRecuperoInteressi", dto.getImpRecuperoInteressi());
			map.put("impSpeseNotifica", dto.getImpSpeseNotifica());
			map.put("impCompensazioneCanone", dto.getImpCompensazioneCanone());
			map.put("descPeriodoPagamento", dto.getDescPeriodoPagamento());
			map.put("descMotivoAnnullo", dto.getDescMotivoAnnullo());
			map.put("flgAnnullato", dto.getFlgAnnullato());
			map.put("flgRestituitoMittente", dto.getFlgRestituitoMittente());
			map.put("flgInvioSpeciale", dto.getFlgInvioSpeciale());
			map.put("flgDilazione", dto.getFlgDilazione());
			map.put("flgAddebitoAnnoSuccessivo", dto.getFlgAddebitoAnnoSuccessivo());
	        map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("notaRinnovo", dto.getNotaRinnovo());
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_UPDATE;
			template.update(getQuery(query, null, null), params);

			dto.setGestDataUpd(now);
			return dto;
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::updateStatoDebitorio] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorio] END");
		}
	}
	

	public StatoDebitorioExtendedDTO saveStatoDebitorioWorking(StatoDebitorioExtendedDTO dto, boolean generateId) throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorioWorking] BEGIN");
		try {
			dto = saveStatoDebitorio(dto, true, false, generateId);
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::saveStatoDebitorioWorking] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorioWorking] END");
		}
		return dto;
	}
	

	public Integer saveStatiDebitoriUpdDaCompensare() throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::saveStatiDebitoriUpdDaCompensare] BEGIN");
		int result = 0;
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_INSERT_UPD_DA_COMPENSARE;
			
			result = template.update(getQuery(query, null, null), params, keyHolder);
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::saveStatiDebitoriUpdDaCompensare] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::saveStatiDebitoriUpdDaCompensare] END");
		}
		return result;
	}
	

	public Integer updateStatoDebitorioUpdRegolarizzato(Long idStatoDebitorio) throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorioUpdRegolarizzato] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_UPD_SD_REGOLARIZZATO, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::updateStatoDebitorioUpdRegolarizzato] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorioUpdRegolarizzato] END");
		}

		return res;
	}
	

	public Integer updateStatoDebitorioWorkingCompensazione(Long idStatoDebitorio, BigDecimal compensazione) throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::UpdateStatoDebitorioWorkingCompensazione] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			map.put("compensazione", compensazione);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_WORKING_COMPENSAZIONE, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::UpdateStatoDebitorioWorkingCompensazione] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::UpdateStatoDebitorioWorkingCompensazione] END");
		}

		return res;
	}
	
	private StatoDebitorioExtendedDTO saveStatoDebitorio(StatoDebitorioExtendedDTO dto, boolean working, boolean upd, boolean generateId) throws DAOException, DataAccessException, SQLException {
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		Long genId = null;
		if(generateId) {
			genId = findNextSequenceValue("seq_risca_t_stato_debitorio");
			map.put("idStatoDebitorio", genId);
		} else {
			map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
		}
		map.put("idRiscossione", dto.getIdRiscossione());
   		map.put("desTipoTitolo", dto.getTipoTitolo());
		map.put("idSoggetto", dto.getIdSoggetto());
		map.put("idGruppoSoggetto", dto.getIdGruppoSoggetto());
		map.put("idRecapito", dto.getIdRecapito());
		map.put("idAttivitaStatoDeb", dto.getIdAttivitaStatoDeb() != null ? dto.getIdAttivitaStatoDeb() :  dto.getAttivitaStatoDeb() != null ? dto.getAttivitaStatoDeb().getIdAttivitaStatoDeb() : null);
		map.put("idStatoContribuzione", dto.getIdStatoContribuzione() != null ? dto.getIdStatoContribuzione() : dto.getStatoContribuzione() != null ? dto.getStatoContribuzione().getIdStatoContribuzione() : null );
		map.put("idTipoDilazione", dto.getIdTipoDilazione());
		map.put("nap", dto.getNap());
		map.put("numTitolo", dto.getNumTitolo());
		map.put("dataProvvedimento", dto.getDataProvvedimento());
		map.put("numRichiestaProtocollo", dto.getNumRichiestaProtocollo());
		map.put("dataRichiestaProtocollo", dto.getDataRichiestaProtocollo());
		map.put("dataUltimaModifica", dto.getDataUltimaModifica());
		map.put("desUsi", dto.getDesUsi());
		map.put("note", dto.getNote());
		map.put("impRecuperoCanone", dto.getImpRecuperoCanone());
		map.put("impRecuperoInteressi", dto.getImpRecuperoInteressi());
		map.put("impSpeseNotifica", dto.getImpSpeseNotifica());
		map.put("impCompensazioneCanone", dto.getImpCompensazioneCanone());
		map.put("descPeriodoPagamento", dto.getDescPeriodoPagamento());
		map.put("descMotivoAnnullo", dto.getDescMotivoAnnullo());
		map.put("flgAnnullato", dto.getFlgAnnullato());
		map.put("flgRestituitoMittente", dto.getFlgRestituitoMittente());
		map.put("flgInvioSpeciale", dto.getFlgInvioSpeciale());
		map.put("flgDilazione", dto.getFlgDilazione());
		map.put("flgAddebitoAnnoSuccessivo", dto.getFlgAddebitoAnnoSuccessivo());
//		if(dto.getIdRiscossione() != null) {
//			MapSqlParameterSource params = getParameterValue(map);
//			Date dataProvvedimento = template.query(QUERY_SELECT_NOTA_RINNOVO, params, new ResultSetExtractor<Date>(){
//			    @Override
//			    public Date extractData(ResultSet rs) throws SQLException,DataAccessException {
//			    	Date dataProvvedimento = null;
//			        while(rs.next()){
//			        	dataProvvedimento = rs.getDate("data_provvedimento");
//			        }
//			        return dataProvvedimento;
//			    }
//			});
//			
//			if(dataProvvedimento != null) {
//	            SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
//				map.put("notaRinnovo", formatter.format(dataProvvedimento));
//			}else {
//				map.put("notaRinnovo", null);
//			}
//
//		}else {
//			map.put("notaRinnovo",null);
//		}

		map.put("notaRinnovo", dto.getNotaRinnovo());
		if(!working && !upd) {
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
		}

		MapSqlParameterSource params = getParameterValue(map);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String query = QUERY_INSERT;
		if(working) {
			query = QUERY_INSERT_W;
		}
		if(upd) {
			query = QUERY_INSERT_UPD_DA_COMPENSARE;
		}
		template.update(getQuery(query, null, null), params, keyHolder);
		if(generateId) {
			dto.setIdStatoDebitorio(genId);
		}
		dto.setGestDataIns(now);
		dto.setGestDataUpd(now);
		return dto;
	}


	public List<StatoDebitorioExtendedDTO> loadAllStatoDebitorioOrByIdRiscossione(Long idRiscossione,Integer offset,Integer limit,String sort, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<StatoDebitorioExtendedDTO> res = new ArrayList<StatoDebitorioExtendedDTO>();
        MapSqlParameterSource paramST = null;
        BigDecimal impCompensazioneCanone = BigDecimal.ZERO;
        BigDecimal canoneDovuto = BigDecimal.ZERO;
		try {
			
			if(idRiscossione == null)
				res =  template.query(getQuery(QUERY_SELECT_ALL_STATI_DEBITORI , null ,  null), paramST, getExtendedRowMapper());
			
			
			else if(idRiscossione != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("idRiscossione", idRiscossione);
				paramST = getParameterValue(map);
				res =  template.query(getQuery(QUERY_SELECT_STATI_DEBITORI_BY_ID_RISCOSSIONE, null ,   null), paramST, getExtendedRowMapper());
				
			}			
			if(!res.isEmpty() && offset != null && limit != null  ) {
				

				for (StatoDebitorioExtendedDTO statoDebitorioDTO : res) {
					if(statoDebitorioDTO.getIdAttivitaStatoDeb() != null) {
						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] GET Attivita Stato Debitorio");
						AttivitaStatoDebitorioDTO attivitaStatoDebitorioDTO = attivitaStatoDebitorioDAO.getAttivitaStatoDebitorioById(statoDebitorioDTO.getIdAttivitaStatoDeb());
						statoDebitorioDTO.setAttivitaStatoDeb(attivitaStatoDebitorioDTO);
					}
					if(statoDebitorioDTO.getIdStatoContribuzione() != null) {
						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] GET Stato Contribuzione");
						StatoContribuzioneDTO statoContribuzioneDTO = statoContribuzioneDAO.loadStatoContribuzioneById(statoDebitorioDTO.getIdStatoContribuzione());
						statoDebitorioDTO.setStatoContribuzione(statoContribuzioneDTO);
					}

					Map<String, Object> map = new HashMap<>();
					map.put("idStatoDebitorio", statoDebitorioDTO.getIdStatoDebitorio());

					if(statoDebitorioDTO.getNap() != null) {
						map.put("nap", statoDebitorioDTO.getNap());
					}
		            SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
		            
					LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] GET list Annualita SD");
					List<AnnualitaSdDTO> listAnnualitaSdDTO = annualitaSdDAO.loadAnnualitaSd(statoDebitorioDTO.getIdStatoDebitorio(), null, null);
					statoDebitorioDTO.setAnnualitaSd(listAnnualitaSdDTO);
					if(!listAnnualitaSdDTO.isEmpty()) {
						statoDebitorioDTO.setNumAnnualita(Long.valueOf(listAnnualitaSdDTO.size()));
						final Integer[] anno = { null };
						Optional<AnnualitaSdDTO> optionalAnnualitaDTO = listAnnualitaSdDTO.stream()
						    .max(Comparator.comparing(AnnualitaSdDTO::getAnno));

						optionalAnnualitaDTO.ifPresent(annualitaDTO -> {
						    anno[0] = annualitaDTO.getAnno();
						});

						statoDebitorioDTO.setAnno(anno[0]);
						
						for (AnnualitaSdDTO annualitaSdDTO : listAnnualitaSdDTO) {
							LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] GET list Annualita Uso SD");

							List<AnnualitaUsoSdDTO> listAnnualitaUsoSd = annualitaUsoSdDAO.loadAnnualitaUsiByIdAnnualitaSd(annualitaSdDTO.getIdAnnualitaSd());
							for (AnnualitaUsoSdDTO annualitaUsoSdExtendedDTO : listAnnualitaUsoSd) {
								LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] GET Uso Ridaum SD");
								List<UsoRidaumSdExtendedDTO> UsoRidaumSdDTO = usoRidaumSdDAO.loadUsoRidaumSdByIdAnnualitaUsoSd(annualitaUsoSdExtendedDTO.getIdAnnualitaUsoSd());
								if(UsoRidaumSdDTO != null)
									annualitaUsoSdExtendedDTO.setUsoRidaumSd(UsoRidaumSdDTO);
							}
							
							annualitaSdDTO.setAnnualitaUsoSd(listAnnualitaUsoSd);
							

						}
						//[DP] modificare il set del canone dovuto invece che con la somma delle annualita con la somma delle rate.canoneDovuto + impRecuperoCanone
//						BigDecimal impRecuperoCanone= BigDecimal.ZERO;
//						if(statoDebitorioDTO.getImpRecuperoCanone() != null) {
//							impRecuperoCanone = statoDebitorioDTO.getImpRecuperoCanone();
//						}
//						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] calcolo canoneDovuto");				
						//canoneDovuto = listAnnualitaSdDTO.stream().map(AnnualitaSdDTO::getCanoneAnnuo).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRecuperoCanone) ;
//						canoneDovuto = listAnnualitaSdDTO.stream().map(AnnualitaSdDTO::getCanoneAnnuo).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRecuperoCanone) ;
//						
//						statoDebitorioDTO.setCanoneDovuto(canoneDovuto);
//						
//						BigDecimal importoDovuto = canoneDovuto.add(impRecuperoCanone);
//							
//						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] calcolo importo Dovuto");
//						statoDebitorioDTO.setImportoDovuto(importoDovuto);
						
						

						
						if(statoDebitorioDTO.getImpCompensazioneCanone() != null) {
							impCompensazioneCanone= statoDebitorioDTO.getImpCompensazioneCanone() ;
						}

						
						//statoDebitorioDTO.setImpMancanteImpEccedente(impCompensazioneCanone.subtract(canoneDovuto));
						
						
						
					}
					
					LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] GET rimborsi");
					List<RimborsoExtendedDTO>  rimborsi = rimborsoDAO.loadRimborsiByIdStatoDebitorio(statoDebitorioDTO.getIdStatoDebitorio());
					statoDebitorioDTO.setRimborsi(rimborsi);
					
					LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] GET rata");
					
					List<RataSdDTO> ListRataSdDTO = rataSdDAO.loadListRataSdByStatoDebitorio(statoDebitorioDTO.getIdStatoDebitorio());
	
					BigDecimal impSpeseNotifica =  BigDecimal.ZERO;
					BigDecimal interessiMaturati =  BigDecimal.ZERO;
					BigDecimal impRimborso =  BigDecimal.ZERO;
					MapSqlParameterSource params = getParameterValue(map);
					LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] calcolo importo Versato");
					BigDecimal importoVersato = template.query(QUERY_SELECT_IMPORTO_VERSATO_BY_ID_STATO_DEBITORIO, params, new ResultSetExtractor<BigDecimal>(){
					    @Override
					    public BigDecimal extractData(ResultSet rs) throws SQLException,DataAccessException {
					    	BigDecimal importoVersato = null;
					        while(rs.next()){
					        	importoVersato = rs.getBigDecimal("importo_versato") != null ?  rs.getBigDecimal("importo_versato") : BigDecimal.ZERO;
					        }
					        return importoVersato;
					    }
					});
					
					
					
					
					statoDebitorioDTO.setImportoVersato(importoVersato);
					
					if(statoDebitorioDTO.getImpSpeseNotifica() != null) {
						impSpeseNotifica = impSpeseNotifica.add(statoDebitorioDTO.getImpSpeseNotifica());
					}
					if(ListRataSdDTO != null) {
						//[DP] inizio spostamento issue 41				
						BigDecimal impRecuperoCanone= BigDecimal.ZERO;
						if(statoDebitorioDTO.getImpRecuperoCanone() != null) {
							impRecuperoCanone = statoDebitorioDTO.getImpRecuperoCanone();
						}

						//[DP] issue 44/49 canone_dovuto : somma(RISCA_R_RATA_SD.canone_dovuto)
						canoneDovuto = ListRataSdDTO.stream().filter(r -> r.getIdRataSdPadre() == null).map(RataSdDTO::getCanoneDovuto).reduce(BigDecimal.ZERO, BigDecimal::add);

						
						statoDebitorioDTO.setCanoneDovuto(canoneDovuto);
						
						BigDecimal importoDovuto = canoneDovuto.add(impRecuperoCanone);
							
						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] calcolo importo Dovuto");
						statoDebitorioDTO.setImportoDovuto(importoDovuto);
						//[DP]Fine spostamento
						
						interessiMaturati = ListRataSdDTO.stream().filter(r -> r.getInteressiMaturati() != null ).map(RataSdDTO::getInteressiMaturati).reduce(BigDecimal.ZERO, BigDecimal::add) ;
					}
					if(rimborsi != null) {
						//[DP]issue33 da NUOVA SPECIFICA impRimborso si somma solo se tipo rimborso = 1
						//impRimborso = rimborsi.stream().map(RimborsoDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRimborso) ;
						impRimborso = rimborsi.stream().filter(r -> r.getIdTipoRimborso() != null && r.getTipoRimborso().getCodTipoRimborso().equals("RIMBORSATO")).map(RimborsoDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRimborso) ;																							
					}

                    BigDecimal sommRimborsi = rimborsi.stream().filter(r -> r.getTipoRimborso().getCodTipoRimborso().equals("RIMBORSATO")).map(RimborsoExtendedDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add) ;
                    // [VF] issue 75 La somma rimborsi deve tenere conto anche di eventuali
					// compensazioni utilizzate che si trovano nella colonna imp_restituito della
					// tabella r_rimborsi per id_tipo_rimborso = 2 o 3
                    BigDecimal sommComp = getSommComp(rimborsi);
					sommRimborsi = sommRimborsi.add(sommComp);
					statoDebitorioDTO.setSommaRimborsi(sommRimborsi);
					
					//[DP] issue 41					
					if(statoDebitorioDTO.getFlgAnnullato()==1) {
						canoneDovuto = BigDecimal.ZERO;
						impSpeseNotifica = BigDecimal.ZERO;
						interessiMaturati = BigDecimal.ZERO;
					}
					
					//[DP] issue 26 sottratto somma rimborsi
					// BigDecimal impMancImpEcc = importoVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati).subtract(impCompensazioneCanone)).subtract(sommRimborsi);
					//[VF] issue 75 non sottrarre impCompensazioneCanone
					BigDecimal impMancImpEcc = importoVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati)).subtract(sommRimborsi);
					statoDebitorioDTO.setImpMancanteImpEccedente(impMancImpEcc);
					
					statoDebitorioDTO.setRate(ListRataSdDTO);
					
					BigDecimal IntMaturatiSpeseNotifica =   BigDecimal.ZERO;
				    if(ListRataSdDTO != null) {
						
					   IntMaturatiSpeseNotifica = ListRataSdDTO.stream().filter(r -> r.getInteressiMaturati() != null && r.getIdRataSdPadre() == null).map(RataSdDTO::getInteressiMaturati).reduce(BigDecimal.ZERO, BigDecimal::add).add(impSpeseNotifica) ;
					}
				
					statoDebitorioDTO.setIntMaturatiSpeseNotifica(IntMaturatiSpeseNotifica);
					
					// Issue 81: il campo accImportoRimborsato deve tenere conto sia di rimborsi che
					// di eventuali compensazioni utilizzate, quindi valorizzo il campo con
					// sommRimborsi (come per il campo sommaRimborsi)
					// statoDebitorioDTO.setAccImportoRimborsato(impRimborso);
					statoDebitorioDTO.setAccImportoRimborsato(sommRimborsi);
					
					BigDecimal importoEccedente =   BigDecimal.ZERO;
					importoEccedente =	importoVersato.add(impRimborso).subtract(canoneDovuto).subtract(IntMaturatiSpeseNotifica);
					
					statoDebitorioDTO.setImportoEccedente(importoEccedente);
					
					if(statoDebitorioDTO.getNap() != null) {
						int numRipetizioniNap = template.query(QUERY_SELECT_NUMBER_REPETETION_NAP, params, new ResultSetExtractor<Integer>(){
						    @Override
						    public Integer extractData(ResultSet rs) throws SQLException,DataAccessException {
						    	Integer numRipetizioniNap = null;
						        while(rs.next()){
						        	numRipetizioniNap = rs.getInt("num_ripetizioni_nap");
						        }
						        return numRipetizioniNap;
						    }
						});
						

						if(numRipetizioniNap > 1) {
							statoDebitorioDTO.setMultiNap(true);
						}
					}

					Date dataPagamento = template.query(QUERY_SELECT_MAX_DATA_PAGAMENTO_BY_ID_STATO_DEBITORIO, params, new ResultSetExtractor<Date>(){
					    @Override
					    public Date extractData(ResultSet rs) throws SQLException,DataAccessException {
					    	Date dataPagamento = null;
					        while(rs.next()){
					        	dataPagamento = rs.getDate("data_pagamento");
					        }
					        return dataPagamento;
					    }
					});
					if(dataPagamento != null) {
						statoDebitorioDTO.setDataPagamento(formatter.format(dataPagamento));
					}
					if(statoDebitorioDTO.getIdRiscossione() != null) {
						RiscossioneDTO riscossioneDTO = riscossioneDAO.getRiscossione(statoDebitorioDTO.getIdRiscossione().toString());
						statoDebitorioDTO.setCodUtenza(riscossioneDTO.getCodRiscossione());
						if(riscossioneDTO.getDataIniConcessione() != null)
						statoDebitorioDTO.setDataInizioConcessione(formatter.parse(riscossioneDTO.getDataIniConcessione()));
						if(riscossioneDTO.getDataScadConcessione() != null)
						statoDebitorioDTO.setDataFineConcessione(formatter.parse(riscossioneDTO.getDataScadConcessione()));
						statoDebitorioDTO.setStatoRiscossione(riscossioneDTO.getStatiRiscossione() != null ? riscossioneDTO.getStatiRiscossione().getDesStatoRiscossione() : null);
					}

					statoDebitorioDTO.setAccertamenti(accertamentoDAO.loadAllAccertamentiOrByIdStatoDeb(statoDebitorioDTO.getIdStatoDebitorio(), null, null, null));
					
					
				}
				 
				  LOGGER.debug("[StatoDebitorioDAOImpl::StatoDebitorioDAOImpl] BEGIN: Order list By data Pagamento ");
					if(sort.contains("dataPagamento")  && sort.charAt(0) == '-') {
						 res = res.stream().sorted(
					        		Comparator.comparing(StatoDebitorioExtendedDTO::getDataPagamento, Comparator.nullsLast(Comparator.reverseOrder() ))
					        		 ).collect(Collectors.toList());
					}else {
						 res = res.stream().sorted(
					        		Comparator.comparing(StatoDebitorioExtendedDTO::getDataPagamento, Comparator.nullsLast(Comparator.naturalOrder() ))
					        		 ).collect(Collectors.toList());
					}
					
			        LOGGER.debug("[StatoDebitorioDAOImpl::getClassificazione] BEGIN: get list with offset and limit  ");
		            if(offset != null && limit != null ) {
			         	offset = (offset - 1) * 10;
			         	res = res.stream().skip(offset).limit(limit).collect(Collectors.toList());
			         }
			}
			
		  
		        
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] END");
		}

		return res;
	}
	
	private BigDecimal getSommComp(List<RimborsoExtendedDTO> rimborsi) {
		BigDecimal sommComp = BigDecimal.ZERO;
		List<RimborsoExtendedDTO> listComp = rimborsi.stream()
				.filter(r -> (r.getTipoRimborso().getCodTipoRimborso().equals("DA_COMPENSARE")
						|| r.getTipoRimborso().getCodTipoRimborso().equals("COMPENSATO"))).collect(Collectors.toList());
		for (RimborsoExtendedDTO comp : listComp) {
			if(comp.getImpRestituito()!= null) {
				sommComp = sommComp.add(comp.getImpRestituito());
			}
		}
		return sommComp;
	}
	
	private String mapSortConCampiDB(String sort) {
        String nomeCampo="";
		if(sort.contains("codiceUtenza")) {
			return getQuerySortingSegment(sort.replace("codiceUtenza", "rtr.cod_riscossione"));
		}
//		if(sort.contains("dataPagamento")) {
//			String query =  getQuerySortingSegment(sort.replace("dataPagamento", "rtp.data_op_val"));
//			return query.replace("rtp.data_op_val", "(CASE WHEN rtp.data_op_val IS NULL THEN 1 ELSE 0 END), rtp.data_op_val ");
//		}
		return nomeCampo;
	}

	public StatoDebitorioExtendedDTO loadStatoDebitorioByIdStatoDebitorio(Long idStatoDebitorio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] BEGIN");
		StatoDebitorioExtendedDTO statoDebitorioDTO = null;
        BigDecimal impCompensazioneCanone = BigDecimal.ZERO;
        BigDecimal canoneDovuto = BigDecimal.ZERO;
		try {
				Map<String, Object> map = new HashMap<>();
				map.put("idStatoDebitorio", idStatoDebitorio);
				MapSqlParameterSource params = getParameterValue(map);
				statoDebitorioDTO = template.queryForObject(QUERY_SELECT_STATI_DEBITORI_BY_ID_STATO_DEBITORIO, params, getExtendedRowMapper());
				if(statoDebitorioDTO != null) {
					if(statoDebitorioDTO.getIdAttivitaStatoDeb() != null) {
						LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] GET Attivita Stato Debitorio");
						AttivitaStatoDebitorioDTO attivitaStatoDebitorioDTO = attivitaStatoDebitorioDAO.getAttivitaStatoDebitorioById(statoDebitorioDTO.getIdAttivitaStatoDeb());
						statoDebitorioDTO.setAttivitaStatoDeb(attivitaStatoDebitorioDTO);
					}
					if(statoDebitorioDTO.getIdStatoContribuzione() != null) {
						LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] GET Stato Contribuzione");
						StatoContribuzioneDTO statoContribuzioneDTO = statoContribuzioneDAO.loadStatoContribuzioneById(statoDebitorioDTO.getIdStatoContribuzione());
						statoDebitorioDTO.setStatoContribuzione(statoContribuzioneDTO);
					}


					LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] GET list Annualita SD");
					List<AnnualitaSdDTO> listAnnualitaSdDTO = annualitaSdDAO.loadAnnualitaSd(statoDebitorioDTO.getIdStatoDebitorio(), null, null);
					statoDebitorioDTO.setAnnualitaSd(listAnnualitaSdDTO);
					if(!listAnnualitaSdDTO.isEmpty()) {
						statoDebitorioDTO.setNumAnnualita(Long.valueOf(listAnnualitaSdDTO.size()));
						final Integer[] anno = { null };
						Optional<AnnualitaSdDTO> optionalAnnualitaDTO = listAnnualitaSdDTO.stream()
						    .max(Comparator.comparing(AnnualitaSdDTO::getAnno));

						optionalAnnualitaDTO.ifPresent(annualitaDTO -> {
						    anno[0] = annualitaDTO.getAnno();
						});

						statoDebitorioDTO.setAnno(anno[0]);
						
						for (AnnualitaSdDTO annualitaSdDTO : listAnnualitaSdDTO) {
							LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] GET list Annualita Uso SD");

							List<AnnualitaUsoSdDTO> listAnnualitaUsoSd = annualitaUsoSdDAO.loadAnnualitaUsiByIdAnnualitaSd(annualitaSdDTO.getIdAnnualitaSd());
							for (AnnualitaUsoSdDTO annualitaUsoSdExtendedDTO : listAnnualitaUsoSd) {
								LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] GET Uso Ridaum SD");
								List<UsoRidaumSdExtendedDTO> UsoRidaumSdDTO = usoRidaumSdDAO.loadUsoRidaumSdByIdAnnualitaUsoSd(annualitaUsoSdExtendedDTO.getIdAnnualitaUsoSd());
								if(UsoRidaumSdDTO != null)
									annualitaUsoSdExtendedDTO.setUsoRidaumSd(UsoRidaumSdDTO);
							}
							
							annualitaSdDTO.setAnnualitaUsoSd(listAnnualitaUsoSd);
							

						}
						//[DP] modificare il set del canone dovuto invece che con la somma delle annualita con la somma delle rate.canoneDovuto + impRecuperoCanone
//						BigDecimal impRecuperoCanone= BigDecimal.ZERO;
//						if(statoDebitorioDTO.getImpRecuperoCanone() != null) {
//							impRecuperoCanone = statoDebitorioDTO.getImpRecuperoCanone();
//						}
//						LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] calcolo canoneDovuto");
//						canoneDovuto = listAnnualitaSdDTO.stream().map(AnnualitaSdDTO::getCanoneAnnuo).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRecuperoCanone) ;
//						statoDebitorioDTO.setCanoneDovuto(canoneDovuto);
//						
//						BigDecimal importoDovuto = canoneDovuto.add(impRecuperoCanone);
//							
//						LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] calcolo importo Dovuto");
//						statoDebitorioDTO.setImportoDovuto(importoDovuto);
						

						LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] calcolo importo Versato");
						if(statoDebitorioDTO.getImpCompensazioneCanone() != null) {
							impCompensazioneCanone= statoDebitorioDTO.getImpCompensazioneCanone() ;
						}

						
//						statoDebitorioDTO.setImpMancanteImpEccedente(impCompensazioneCanone.subtract(canoneDovuto));
						
					}
					
					
					
					
					LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] GET rimborsi");
					List<RimborsoExtendedDTO>  rimborsi = rimborsoDAO.loadRimborsiByIdStatoDebitorio(statoDebitorioDTO.getIdStatoDebitorio());
					statoDebitorioDTO.setRimborsi(rimborsi);
					
					LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] GET rate");
					
					List<RataSdDTO> ListRataSdDTO = rataSdDAO.loadListRataSdByStatoDebitorio(statoDebitorioDTO.getIdStatoDebitorio());

					BigDecimal impSpeseNotifica =  BigDecimal.ZERO;
					BigDecimal interessiMaturati =  BigDecimal.ZERO;
					BigDecimal impRimborso =  BigDecimal.ZERO;
					LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] calcolo importo Versato");
					BigDecimal importoVersato = template.query(QUERY_SELECT_IMPORTO_VERSATO_BY_ID_STATO_DEBITORIO, params, new ResultSetExtractor<BigDecimal>(){
					    @Override
					    public BigDecimal extractData(ResultSet rs) throws SQLException,DataAccessException {
					    	BigDecimal importoVersato = null;
					        while(rs.next()){
					        	importoVersato = rs.getBigDecimal("importo_versato") != null ?  rs.getBigDecimal("importo_versato") : BigDecimal.ZERO;
					        }
					        return importoVersato;
					    }
					});
					
					
					
					statoDebitorioDTO.setImportoVersato(importoVersato);
					
					if(statoDebitorioDTO.getImpSpeseNotifica() != null) {
						impSpeseNotifica =	impSpeseNotifica.add(statoDebitorioDTO.getImpSpeseNotifica());
					}
					if(ListRataSdDTO != null) {
						//[DP] inizio spostamento issue 41
						BigDecimal impRecuperoCanone= BigDecimal.ZERO;
						if(statoDebitorioDTO.getImpRecuperoCanone() != null) {
							impRecuperoCanone = statoDebitorioDTO.getImpRecuperoCanone();
						}
						//[DP] issue 44/49 canone_dovuto : somma(RISCA_R_RATA_SD.canone_dovuto)
						canoneDovuto = ListRataSdDTO.stream().filter(r -> r.getIdRataSdPadre() == null).map(RataSdDTO::getCanoneDovuto).reduce(BigDecimal.ZERO, BigDecimal::add);
						
						statoDebitorioDTO.setCanoneDovuto(canoneDovuto);
						
						BigDecimal importoDovuto = canoneDovuto.add(impRecuperoCanone);
							
						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] calcolo importo Dovuto");
						statoDebitorioDTO.setImportoDovuto(importoDovuto);
						//[DP]Fine spostamento
						
						interessiMaturati = ListRataSdDTO.stream().filter(r -> r.getInteressiMaturati() != null).map(RataSdDTO::getInteressiMaturati).reduce(BigDecimal.ZERO, BigDecimal::add) ;
					}
					if(rimborsi != null) {
						//[DP]issue33 da NUOVA SPECIFICA impRimborso si somma solo se tipo rimborso = 1
						//impRimborso = rimborsi.stream().map(RimborsoDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRimborso) ;
						impRimborso = rimborsi.stream().filter(r -> r.getIdTipoRimborso() != null && r.getTipoRimborso().getCodTipoRimborso().equals("RIMBORSATO")).map(RimborsoDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRimborso) ;																													
					}

					BigDecimal sommRimborsi = rimborsi.stream().filter(r -> r.getTipoRimborso().getCodTipoRimborso().equals("RIMBORSATO")).map(RimborsoExtendedDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add) ;
					// [VF] issue 75 La somma rimborsi deve tenere conto anche di eventuali
					// compensazioni utilizzate che si trovano nella colonna imp_restituito della
					// tabella r_rimborsi per id_tipo_rimborso = 2 o 3
					BigDecimal sommComp = getSommComp(rimborsi);
					sommRimborsi = sommRimborsi.add(sommComp);	
					statoDebitorioDTO.setSommaRimborsi(sommRimborsi);
						
					//[DP] issue 41
					if(statoDebitorioDTO.getFlgAnnullato()==1) {
						canoneDovuto = BigDecimal.ZERO;
						impSpeseNotifica = BigDecimal.ZERO;
						interessiMaturati = BigDecimal.ZERO;
					}
					
					//[DP] issue 26 sottratto somma rimborsi
					// BigDecimal impMancImpEcc = importoVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati).subtract(impCompensazioneCanone)).subtract(sommRimborsi);
					//[VF] issue 75 non sottrarre impCompensazioneCanone
					BigDecimal impMancImpEcc = importoVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati)).subtract(sommRimborsi);
					statoDebitorioDTO.setImpMancanteImpEccedente(impMancImpEcc);
				
					
					statoDebitorioDTO.setRate(ListRataSdDTO);
					
					
					BigDecimal IntMaturatiSpeseNotifica =   BigDecimal.ZERO;
				    if(ListRataSdDTO != null) {
						
					   IntMaturatiSpeseNotifica = ListRataSdDTO.stream().filter(r -> r.getInteressiMaturati() != null && r.getIdRataSdPadre() == null).map(RataSdDTO::getInteressiMaturati).reduce(BigDecimal.ZERO, BigDecimal::add).add(impSpeseNotifica) ;
					}
				
					statoDebitorioDTO.setIntMaturatiSpeseNotifica(IntMaturatiSpeseNotifica);
					
					// Issue 81: il campo accImportoRimborsato deve tenere conto sia di rimborsi che
					// di eventuali compensazioni utilizzate, quindi valorizzo il campo con
					// sommRimborsi (come per il campo sommaRimborsi)
					// statoDebitorioDTO.setAccImportoRimborsato(impRimborso);
					statoDebitorioDTO.setAccImportoRimborsato(sommRimborsi);
					
					BigDecimal importoEccedente =   BigDecimal.ZERO;
					importoEccedente =	importoVersato.add(impRimborso).subtract(canoneDovuto).subtract(IntMaturatiSpeseNotifica);
					
					statoDebitorioDTO.setImportoEccedente(importoEccedente);
		            SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
					Date dataPagamento = template.query(QUERY_SELECT_MAX_DATA_PAGAMENTO_BY_ID_STATO_DEBITORIO, params, new ResultSetExtractor<Date>(){
					    @Override
					    public Date extractData(ResultSet rs) throws SQLException,DataAccessException {
					    	Date dataPagamento = null;
					        while(rs.next()){
					        	dataPagamento = rs.getDate("data_pagamento");
					        }
					        return dataPagamento;
					    }
					});
					if(dataPagamento != null) {
						statoDebitorioDTO.setDataPagamento(formatter.format(dataPagamento));
					}
					if(statoDebitorioDTO.getIdRiscossione() != null) {

						RiscossioneDTO riscossioneDTO = riscossioneDAO.getRiscossione(statoDebitorioDTO.getIdRiscossione().toString());
						statoDebitorioDTO.setCodUtenza(riscossioneDTO.getCodRiscossione());
						if(riscossioneDTO.getDataIniConcessione() != null)
						statoDebitorioDTO.setDataInizioConcessione(formatter.parse(riscossioneDTO.getDataIniConcessione()));
						if(riscossioneDTO.getDataScadConcessione() != null)
						statoDebitorioDTO.setDataFineConcessione(formatter.parse(riscossioneDTO.getDataScadConcessione()));
						statoDebitorioDTO.setStatoRiscossione(riscossioneDTO.getStatiRiscossione() != null ? riscossioneDTO.getStatiRiscossione().getDesStatoRiscossione() : null);
						
		
						
					}

					}
				
				
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] END");
		}

		return statoDebitorioDTO;
	}

	public List<UtRimbDTO> loadStatoDebitorioByIdRimborso(Long idRimborso) throws DAOException{
		LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdRimborso] BEGIN");
		LOGGER.debug("[RataSdDAOImpl::loadRataSdByStatoDebitorio] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRimborso", idRimborso);
			MapSqlParameterSource params = getParameterValue(map);

			return template.query(getQuery(QUERY_SELECT_UT_RIMBO_BY_ID_RIMBORSO, null, null), params,
					getUtRimbRowMapper());

		} catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[RataSdDAOImpl::loadStatoDebitorioByIdRimborso] No record found in database for idRimborso "+ idRimborso, e);
		    return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RataSdDAOImpl::loadStatoDebitorioByIdRimborso] Errore nell'accesso ai dati", e);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[RataSdDAOImpl::loadStatoDebitorioByIdRimborso] Errore SQL ", e); 
			return null;
		} finally {
			LOGGER.debug("[RataSdDAOImpl::loadStatoDebitorioByIdRimborso] END");
		}
		
	}
	

	public List<StatoDebitorioExtendedDTO> loadAllStatoDebitorioByNap(String nap,Integer offset,Integer limit, String sort) throws DAOException{
		LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] BEGIN");
        List<StatoDebitorioExtendedDTO> res = new ArrayList<StatoDebitorioExtendedDTO>();
        BigDecimal impCompensazioneCanone = BigDecimal.ZERO;
        BigDecimal canoneDovuto = BigDecimal.ZERO;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			MapSqlParameterSource params = getParameterValue(map);
			String dynamicOrderByCondition="";
			if (StringUtils.isNotBlank(sort)) {
			   dynamicOrderByCondition = mapSortConCampiDB(sort);

			}	
			res =  template.query(getQuery(QUERY_SELECT_STATI_DEBITORI_BY_NAP + dynamicOrderByCondition,  offset!= null ? offset.toString(): null ,  limit != null ? limit.toString(): null), params, getExtendedRowMapper());
		
//			questa serve per il prossimo servizio da fare per calcolare numero utenze
//	    	int numeroUtenza = template.query(QUERY_SELECT_STATI_DEBITORI_BY_NAP_GOUP_BY_COD_RISCOSSIONE, params, new ResultSetExtractor<Integer>(){
//			    @Override
//			    public Integer extractData(ResultSet rs) throws SQLException,DataAccessException {
//			    	int numeroUtenza = 0;
//			        while(rs.next()){
//			        	numeroUtenza = rs.getInt("numero_utenza");
//			        }
//			        return numeroUtenza;
//			    }
//			});
			
			if(!res.isEmpty() && offset != null && limit != null) {
				
				for (StatoDebitorioExtendedDTO statoDebitorioDTO : res) {
					if(statoDebitorioDTO.getIdAttivitaStatoDeb() != null) {
						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] GET Attivita Stato Debitorio");
						AttivitaStatoDebitorioDTO attivitaStatoDebitorioDTO = attivitaStatoDebitorioDAO.getAttivitaStatoDebitorioById(statoDebitorioDTO.getIdAttivitaStatoDeb());
						statoDebitorioDTO.setAttivitaStatoDeb(attivitaStatoDebitorioDTO);
					}
					if(statoDebitorioDTO.getIdStatoContribuzione() != null) {
						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] GET Stato Contribuzione");
						StatoContribuzioneDTO statoContribuzioneDTO = statoContribuzioneDAO.loadStatoContribuzioneById(statoDebitorioDTO.getIdStatoContribuzione());
						statoDebitorioDTO.setStatoContribuzione(statoContribuzioneDTO);
					}


					LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] GET list Annualita SD");
					List<AnnualitaSdDTO> listAnnualitaSdDTO = annualitaSdDAO.loadAnnualitaSd(statoDebitorioDTO.getIdStatoDebitorio(), null, null);
					statoDebitorioDTO.setAnnualitaSd(listAnnualitaSdDTO);
					if(!listAnnualitaSdDTO.isEmpty()) {
						statoDebitorioDTO.setNumAnnualita(Long.valueOf(listAnnualitaSdDTO.size()));
						final Integer[] anno = { null };
						Optional<AnnualitaSdDTO> optionalAnnualitaDTO = listAnnualitaSdDTO.stream()
						    .max(Comparator.comparing(AnnualitaSdDTO::getAnno));

						optionalAnnualitaDTO.ifPresent(annualitaDTO -> {
						    anno[0] = annualitaDTO.getAnno();
						});

						statoDebitorioDTO.setAnno(anno[0]);
						
						for (AnnualitaSdDTO annualitaSdDTO : listAnnualitaSdDTO) {
							LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] GET list Annualita Uso SD");

							List<AnnualitaUsoSdDTO> listAnnualitaUsoSd = annualitaUsoSdDAO.loadAnnualitaUsiByIdAnnualitaSd(annualitaSdDTO.getIdAnnualitaSd());
							for (AnnualitaUsoSdDTO annualitaUsoSdExtendedDTO : listAnnualitaUsoSd) {
								LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] GET Uso Ridaum SD");
								List<UsoRidaumSdExtendedDTO> UsoRidaumSdDTO = usoRidaumSdDAO.loadUsoRidaumSdByIdAnnualitaUsoSd(annualitaUsoSdExtendedDTO.getIdAnnualitaUsoSd());
								if(UsoRidaumSdDTO != null)
									annualitaUsoSdExtendedDTO.setUsoRidaumSd(UsoRidaumSdDTO);
							}
							
							annualitaSdDTO.setAnnualitaUsoSd(listAnnualitaUsoSd);
							

						}
						//[DP] modificare il set del canone dovuto invece che con la somma delle annualita con la somma delle rate.canoneDovuto + impRecuperoCanone
//						BigDecimal impRecuperoCanone= BigDecimal.ZERO;
//						if(statoDebitorioDTO.getImpRecuperoCanone() != null) {
//							impRecuperoCanone = statoDebitorioDTO.getImpRecuperoCanone();
//						}
//						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] calcolo canoneDovuto");
//						canoneDovuto = listAnnualitaSdDTO.stream().map(AnnualitaSdDTO::getCanoneAnnuo).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRecuperoCanone) ;
//						statoDebitorioDTO.setCanoneDovuto(canoneDovuto);
//						
//						BigDecimal importoDovuto = canoneDovuto.add(impRecuperoCanone);
//							
//						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] calcolo importo Dovuto");
//						statoDebitorioDTO.setImportoDovuto(importoDovuto);
						
						
						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] calcolo importo Versato");
						impCompensazioneCanone = BigDecimal.ZERO;
						if(statoDebitorioDTO.getImpCompensazioneCanone() != null) {
							impCompensazioneCanone= statoDebitorioDTO.getImpCompensazioneCanone() ;
						}
						BigDecimal importoVersato = canoneDovuto.subtract(impCompensazioneCanone);
						statoDebitorioDTO.setImportoVersato(importoVersato);
						
						//statoDebitorioDTO.setImpMancanteImpEccedente(impCompensazioneCanone.subtract(canoneDovuto));
					}
				
					LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] GET rimborsi");
					List<RimborsoExtendedDTO>  rimborsi = rimborsoDAO.loadRimborsiByIdStatoDebitorio(statoDebitorioDTO.getIdStatoDebitorio());
					statoDebitorioDTO.setRimborsi(rimborsi);
					
					LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] GET rata");
					
					List<RataSdDTO> ListRataSdDTO = rataSdDAO.loadListRataSdByStatoDebitorio(statoDebitorioDTO.getIdStatoDebitorio());
					BigDecimal impVersato = BigDecimal.ZERO;
					BigDecimal impSpeseNotifica =  BigDecimal.ZERO;
					BigDecimal interessiMaturati =  BigDecimal.ZERO;
					BigDecimal impRimborso =  BigDecimal.ZERO;
					for (RataSdDTO rataSdDTO : ListRataSdDTO) {
						List<DettaglioPagExtendedDTO>  listdettaglioPag = dettaglioPagDAO.getDettaglioPagByIdRate(rataSdDTO.getIdRataSd());
						//rataSdDTO.setDettaglioPag(listdettaglioPag);
					    impVersato = listdettaglioPag.stream().filter(d -> d.getImportoVersato() != null).map(DettaglioPagExtendedDTO::getImportoVersato).reduce(BigDecimal.ZERO, BigDecimal::add).add(impVersato) ;
					}
					
					if(statoDebitorioDTO.getImpSpeseNotifica() != null) {
						impSpeseNotifica = impSpeseNotifica.add(statoDebitorioDTO.getImpSpeseNotifica());
					}
					if(ListRataSdDTO != null) {
						//[DP] inizio spostamento issue 41
						BigDecimal impRecuperoCanone= BigDecimal.ZERO;
						if(statoDebitorioDTO.getImpRecuperoCanone() != null) {
							impRecuperoCanone = statoDebitorioDTO.getImpRecuperoCanone();
						}
						//[DP] issue 44/49 canone_dovuto : somma(RISCA_R_RATA_SD.canone_dovuto)
						canoneDovuto = ListRataSdDTO.stream().filter(r -> r.getIdRataSdPadre() == null).map(RataSdDTO::getCanoneDovuto).reduce(BigDecimal.ZERO, BigDecimal::add);
						
						statoDebitorioDTO.setCanoneDovuto(canoneDovuto);
						
						BigDecimal importoDovuto = canoneDovuto.add(impRecuperoCanone);
							
						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioOrByIdRiscossione] calcolo importo Dovuto");
						statoDebitorioDTO.setImportoDovuto(importoDovuto);
						//[DP]Fine spostamento
						interessiMaturati = ListRataSdDTO.stream().filter(r -> r.getInteressiMaturati() != null).map(RataSdDTO::getInteressiMaturati).reduce(BigDecimal.ZERO, BigDecimal::add) ;
					}
					if(rimborsi != null) {
						//[DP]issue33 da NUOVA SPECIFICA impRimborso si somma solo se tipo rimborso = 1
						//impRimborso = rimborsi.stream().map(RimborsoDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRimborso) ;
						impRimborso = rimborsi.stream().filter(r -> r.getIdTipoRimborso() != null && r.getTipoRimborso().getCodTipoRimborso().equals("RIMBORSATO")).map(RimborsoDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRimborso) ;																																			
					}
					
					BigDecimal sommRimborsi = rimborsi.stream().filter(r -> r.getTipoRimborso().getCodTipoRimborso().equals("RIMBORSATO")).map(RimborsoExtendedDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add) ;
					// [VF] issue 75 La somma rimborsi deve tenere conto anche di eventuali
					// compensazioni utilizzate che si trovano nella colonna imp_restituito della
					// tabella r_rimborsi per id_tipo_rimborso = 2 o 3
					BigDecimal sommComp = getSommComp(rimborsi);
					sommRimborsi = sommRimborsi.add(sommComp);	
					statoDebitorioDTO.setSommaRimborsi(sommRimborsi);

					//[DP] issue 41
					if(statoDebitorioDTO.getFlgAnnullato()==1) {
						canoneDovuto = BigDecimal.ZERO;
						impSpeseNotifica = BigDecimal.ZERO;
						interessiMaturati = BigDecimal.ZERO;
					}
					
					//[DP] issue 26 sottratto somma rimborsi
					// BigDecimal impMancImpEcc = impVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati).subtract(impCompensazioneCanone)).subtract(sommRimborsi);
					//[VF] issue 75 non sottrarre impCompensazioneCanone
					BigDecimal impMancImpEcc = impVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati)).subtract(sommRimborsi);
					statoDebitorioDTO.setImpMancanteImpEccedente(impMancImpEcc);
					
					statoDebitorioDTO.setRate(ListRataSdDTO);
					
					
					BigDecimal IntMaturatiSpeseNotifica =   BigDecimal.ZERO;
				    if(ListRataSdDTO != null) {
						
					   IntMaturatiSpeseNotifica = ListRataSdDTO.stream().filter(r -> r.getInteressiMaturati() != null && r.getIdRataSdPadre() == null).map(RataSdDTO::getInteressiMaturati).reduce(BigDecimal.ZERO, BigDecimal::add).add(impSpeseNotifica) ;
					}
				
					statoDebitorioDTO.setIntMaturatiSpeseNotifica(IntMaturatiSpeseNotifica);
					
					// Issue 81: il campo accImportoRimborsato deve tenere conto sia di rimborsi che
					// di eventuali compensazioni utilizzate, quindi valorizzo il campo con
					// sommRimborsi (come per il campo sommaRimborsi)
					// statoDebitorioDTO.setAccImportoRimborsato(impRimborso);
					statoDebitorioDTO.setAccImportoRimborsato(sommRimborsi);
					
					BigDecimal importoEccedente =   BigDecimal.ZERO;
					importoEccedente =	impVersato.add(impRimborso).subtract(canoneDovuto).subtract(IntMaturatiSpeseNotifica);
					
					statoDebitorioDTO.setImportoEccedente(importoEccedente);
					

					map.put("idStatoDebitorio", statoDebitorioDTO.getIdStatoDebitorio());
					params = getParameterValue(map);
		            SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
					Date dataPagamento = template.query(QUERY_SELECT_MAX_DATA_PAGAMENTO_BY_ID_STATO_DEBITORIO, params, new ResultSetExtractor<Date>(){
					    @Override
					    public Date extractData(ResultSet rs) throws SQLException,DataAccessException {
					    	Date dataPagamento = null;
					        while(rs.next()){
					        	dataPagamento = rs.getDate("data_pagamento");
					        }
					        return dataPagamento;
					    }
					});
					if(dataPagamento != null) {
						statoDebitorioDTO.setDataPagamento(formatter.format(dataPagamento));
					}
					if(statoDebitorioDTO.getIdRiscossione() != null) {
						RiscossioneDTO riscossioneDTO = riscossioneDAO.getRiscossione(statoDebitorioDTO.getIdRiscossione().toString());
						statoDebitorioDTO.setCodUtenza(riscossioneDTO.getCodRiscossione());
						if(riscossioneDTO.getDataIniConcessione() != null)
						statoDebitorioDTO.setDataInizioConcessione(formatter.parse(riscossioneDTO.getDataIniConcessione()));
						if(riscossioneDTO.getDataScadConcessione() != null)
						statoDebitorioDTO.setDataFineConcessione(formatter.parse(riscossioneDTO.getDataScadConcessione()));
						statoDebitorioDTO.setStatoRiscossione(riscossioneDTO.getStatiRiscossione() != null ? riscossioneDTO.getStatiRiscossione().getDesStatoRiscossione() : null);
					}

					
				}
				
				
			}

		} catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] No record found in database for idRimborso "+ nap, e);
		    return null;
		} catch (DataAccessException e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] Errore nell'accesso ai dati", e);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] Errore SQL ", e); 
			return null;
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] END");
		}
		return res;
		
	}



	public VerifyStatoDebitorioDTO verifyStatoDebitorio(String modalita, StatoDebitorioExtendedDTO statoDebitorio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericExceptionList, ParseException {
		LOGGER.debug("[StatoDebitorioDAOImpl::verifyStatoDebitorio] BEGIN");
		VerifyStatoDebitorioDTO isStatoDebitorioValid = new VerifyStatoDebitorioDTO();
		List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
				if(modalita.equals("inserimento")) {
					if(!(statoDebitorio.getAnnualitaSd().size()>0)) {
			            ErrorDTO error = new ErrorDTO("400", "E048", "Attenzione: allo stato debitorio deve essere associata almeno un'annualita'", null, null);
			            errorsList.add(error);
					}else {
						 List<AnnualitaSdDTO> listAnnualita = statoDebitorio.getAnnualitaSd().stream().filter(a -> a.getFlgRateoPrimaAnnualita() == 1)
								 .collect(Collectors.toList());
						 if(listAnnualita.size() > 1) {
					            ErrorDTO error = new ErrorDTO("400", "A024", "Attenzione: non e' possibile inserire piu' di un rateo prima annualita'", null, null);
					            errorsList.add(error); 
						 }
					}
					for(int i = 0; i<statoDebitorio.getAnnualitaSd().size(); i++) {
						if(!(statoDebitorio.getAnnualitaSd().get(i).getAnnualitaUsoSd().size()>0)) {
							ErrorDTO error = new ErrorDTO("400", "E049", "Attenzione: ad ogni annualita' deve essere associato almeno un uso", null, null);
				            errorsList.add(error);
				            break;
						}
					}
					if(statoDebitorio.getDescPeriodoPagamento() == null) {
						ErrorDTO error = new ErrorDTO("400", "E053", "Attenzione: il periodo di pagamento deve essere inserito", null, null);
			            errorsList.add(error);
					}
					if(statoDebitorio.getRate().size() > 0) {
						String dataScadenzaPagamento = statoDebitorio.getRate().get(0).getDataScadenzaPagamento();
							if(dataScadenzaPagamento == null) {
						            ErrorDTO error = new ErrorDTO("400", "E054", "Attenzione: non e' stata valorizzata la data di scadenza del pagamento", null, null);
						            errorsList.add(error);
					            }
//							else {
//					            	if(statoDebitorio.getDataRichiestaProtocollo() == null) {
//							            ErrorDTO error = new ErrorDTO("400", "E046", "Attenzione: la Data Protocollo deve essere minore della Data Scadenza Pagamento", null, null);
//							            errorsList.add(error);
//					            	}else {
//					            		Date dateScPagamento = df.parse(dataScadenzaPagamento) ; 
//					            		if(statoDebitorio.getDataRichiestaProtocollo().after(dateScPagamento)) {
//								            ErrorDTO error = new ErrorDTO("400", "E046", "Attenzione: la Data Protocollo deve essere minore della Data Scadenza Pagamento", null, null);
//								            errorsList.add(error);
//					            		}
//					            	}
//					            }
					}
					else {
						  ErrorDTO error = new ErrorDTO("400", "E054", "Attenzione: non e' stata valorizzata la data di scadenza del pagamento", null, null);
				          errorsList.add(error);
					}
					if(statoDebitorio.getFlgAnnullato() == 1 && (statoDebitorio.getDescMotivoAnnullo() == null || statoDebitorio.getDescMotivoAnnullo().equals(""))) {
						ErrorDTO error = new ErrorDTO("400", "E050", "", null, null);
			            errorsList.add(error);
					}

					Integer annoAnnualita[] = new Integer[statoDebitorio.getAnnualitaSd().size()];
					for(int k=0; k < statoDebitorio.getAnnualitaSd().size(); k++) {
						annoAnnualita[k] = statoDebitorio.getAnnualitaSd().get(k).getAnno(); 
					}
					Long distinctCount = Stream.of(annoAnnualita).distinct().count();

					if(annoAnnualita.length != distinctCount) {
						ErrorDTO error = new ErrorDTO("400", "E069", "Attenzione: in questo stato debitorio esiste gia' la stessa annualita' ", null, null);
			            errorsList.add(error);
					}

		        	Map<String, Object> map = new HashMap<>();
		        	map.put("idRiscossione", statoDebitorio.getIdRiscossione());
		        	MapSqlParameterSource params = getParameterValue(map);
					if(template.queryForObject(QUERY_SELECT_STATI_DEB_WITH_FLG_INVIO_SPECIALE, params, Integer.class) > 0) {
						if(template.queryForObject(QUERY_SELECT_STATI_DEB_WITH_FLG_INVIO_SPECIALE, params, Integer.class) > 0) {
						ErrorDTO error = new ErrorDTO("400", "E078", "Attenzione: esiste gia' uno stato debitorio con flag invio speciale", null, null);
			            errorsList.add(error);
						}
					}
				}
				else if(modalita.equals("modifica")) {
					
					if(!(statoDebitorio.getAnnualitaSd().size()>0)) {
			            ErrorDTO error = new ErrorDTO("400", "E048", "Attenzione: allo stato debitorio deve essere associata almeno un'annualita'", null, null);
			            errorsList.add(error);
					}else {
						 List<AnnualitaSdDTO> listAnnualita = statoDebitorio.getAnnualitaSd().stream().filter(a -> a.getFlgRateoPrimaAnnualita() == 1)
								 .collect(Collectors.toList());
						 if(listAnnualita.size() > 1) {
					            ErrorDTO error = new ErrorDTO("400", "A024", "Attenzione: non e' possibile inserire piu' di un rateo prima annualita'", null, null);
					            errorsList.add(error); 
						 }
					}
					for(int i = 0; i<statoDebitorio.getAnnualitaSd().size(); i++) {
						if(!(statoDebitorio.getAnnualitaSd().get(i).getAnnualitaUsoSd().size()>0)) {
							ErrorDTO error = new ErrorDTO("400", "E049", "Attenzione: ad ogni annualita' deve essere associato almeno un uso", null, null);
				            errorsList.add(error);
				            break;
						}
					}
					if(statoDebitorio.getDescPeriodoPagamento() == null) {
						ErrorDTO error = new ErrorDTO("400", "E053", "Attenzione: il periodo di pagamento deve essere inserito", null, null);
			            errorsList.add(error);
					}
					if(statoDebitorio.getRate().size() > 0) {
						String dataScadenzaPagamento = statoDebitorio.getRate().get(0).getDataScadenzaPagamento();
							if(dataScadenzaPagamento == null) {
						            ErrorDTO error = new ErrorDTO("400", "E054", "Attenzione: non e' stata valorizzata la data di scadenza del pagamento", null, null);
						            errorsList.add(error);
					            }
//							else {
//					            	if(statoDebitorio.getDataRichiestaProtocollo() == null) {
//							            ErrorDTO error = new ErrorDTO("400", "E046", "Attenzione: la Data Protocollo deve essere minore della Data Scadenza Pagamento", null, null);
//							            errorsList.add(error);
//					            	}else {
//					            		Date dateScPagamento = df.parse(dataScadenzaPagamento) ; 
//					            		if(statoDebitorio.getDataRichiestaProtocollo().after(dateScPagamento)) {
//								            ErrorDTO error = new ErrorDTO("400", "E046", "Attenzione: la Data Protocollo deve essere minore della Data Scadenza Pagamento", null, null);
//								            errorsList.add(error);
//					            		}
//					            	}
//					            }
					}else {
						  ErrorDTO error = new ErrorDTO("400", "E054", "Attenzione: non e' stata valorizzata la data di scadenza del pagamento", null, null);
				          errorsList.add(error);
					}		
					
					if(statoDebitorio.getFlgAnnullato() == 1 && statoDebitorio.getDescMotivoAnnullo() == null) {
						ErrorDTO error = new ErrorDTO("400", "E050", "", null, null);
			            errorsList.add(error);
					}

					Integer[] annoAnnualita = new Integer[statoDebitorio.getAnnualitaSd().size()];
					for(int k=0; k < statoDebitorio.getAnnualitaSd().size(); k++) {
						annoAnnualita[k] = statoDebitorio.getAnnualitaSd().get(k).getAnno(); 
					}
					Long distinctCount = Stream.of(annoAnnualita).distinct().count();

					if(annoAnnualita.length != distinctCount) {
						ErrorDTO error = new ErrorDTO("400", "E069", "Attenzione: in questo stato debitorio esiste gia' la stessa annualita' ", null, null);
			            errorsList.add(error);
					}

		        	Map<String, Object> map = new HashMap<>();
		        	map.put("idRiscossione", statoDebitorio.getIdRiscossione());
		        	map.put("idStatoDebitorio", statoDebitorio.getIdStatoDebitorio());
		        	MapSqlParameterSource params = getParameterValue(map);
					if(template.queryForObject(QUERY_SELECT_STATI_DEB_WITH_FLG_INVIO_SPECIALE, params, Integer.class) > 0) {
						if(template.queryForObject(QUERY_SELECT_STATI_DEB_WITH_FLG_INVIO_SPECIALE_BY_ID_STATO_DEB, params, Integer.class) == 0) {
							ErrorDTO error = new ErrorDTO("400", "E078", "Attenzione: esiste gia' uno stato debitorio con flag invio speciale", null, null);
				            errorsList.add(error);
						}
					}
					
					if(statoDebitorio.getImpCompensazioneCanone() != null && statoDebitorio.getImpCompensazioneCanone() != BigDecimal.ZERO) {
						ErrorDTO error = new ErrorDTO("400", "E051", "Attenzione. lo stato debitorio non e' modificabile perche' c'e' una compensazione", null, null);
			            errorsList.add(error);
					}
				
					if (statoDebitorio.getRimborsi() != null) {
					    List<RimborsoExtendedDTO> listRimborsi = rimborsoDAO.loadRimborsiByIdStatoDebitorio(statoDebitorio.getIdStatoDebitorio());

					    // Filtra i rimborsi di tipo 2 o 3 in un'unica operazione
					    List<RimborsoExtendedDTO> listRimborsiCompOld = listRimborsi.stream()
					        .filter(r -> r.getTipoRimborso().getIdTipoRimborso() == 2 || r.getTipoRimborso().getIdTipoRimborso() == 3)
					        .collect(Collectors.toList());

					    if (!listRimborsiCompOld.isEmpty()) {
					        // Recupera un'unica mappa di RimborsoSdUtilizzatoDTO da tutti i rimborsoIds
					    	List<Long> idRimborsiDistinct = listRimborsiCompOld.stream()
					    		    .map(RimborsoExtendedDTO::getIdRimborso)
					    		    .distinct()  // Filtra i duplicati
					    		    .collect(Collectors.toList());

					    	List<RimborsoSdUtilizzatoDTO> rimborsiSdUtilizzati = rimborsoSdUtilizzatoDAO
					            .getRimborsoSdUtilizzatoByIdRimborsi(idRimborsiDistinct);

					        // Controlla ogni rimborso nella lista filtrata
					    	if (!rimborsiSdUtilizzati.isEmpty()) {
					                errorsList.add(new ErrorDTO("400", "E077", "Attenzione: sono presenti dei rimborsi da compensare o compensati. Non e' possibile modificare lo stato debitorio.", null, null));
					         }
					    }
					    
					 // VF Issue 64 la somma dei rimborsi da inserire non puo' essere superiore
						// all'eccedenza sullo stato debitorio
						if (!statoDebitorio.getRimborsi().isEmpty()) {
							BigDecimal sommaRimborsi = statoDebitorio.getRimborsi().stream()
									.map(RimborsoExtendedDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add);
							
							BigDecimal interessiESpese = getInteressiESpese(statoDebitorio);
							BigDecimal eccedenzaCalcolata = statoDebitorio.getImportoVersato()
									.subtract(statoDebitorio.getImportoDovuto().add(interessiESpese));
							if (sommaRimborsi.compareTo(eccedenzaCalcolata) > 0) {
								MessaggiDTO messaggio = messaggiDAO.loadMessaggiByCodMessaggio("E117");
								errorsList
										.add(new ErrorDTO("400", "E117", messaggio.getDesTestoMessaggio(), null, null));
							}
						}
					}

                
					//QUI MANCA UN CONTROLLO
					
					if(statoDebitorio.getIdAttivitaStatoDeb() != null && statoDebitorio.getIdAttivitaStatoDeb() != 4) {
						ErrorDTO error = new ErrorDTO("400", "A023", "", null, null);
			            errorsList.add(error);
					}
					
					if(statoDebitorio.getRate() != null) {
						for(int n=0; n<statoDebitorio.getRate().size(); n++) {
							DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
							String dataScadPagStr = statoDebitorio.getRate().get(n).getDataScadenzaPagamento();
							String dataScadPag = "";
							if(dataScadPagStr != null)
								dataScadPag = dataScadPagStr.substring(0, 10);
							//String dataScadPag = dateFormat.format(dataScadPagStr);
							//Date dataScadPag = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataScadPagStr);  
							map.put("idRataSd", statoDebitorio.getRate().get(n).getIdRataSd());
							map.put("nap", statoDebitorio.getNap());
							params = getParameterValue(map);
							String data = template.queryForObject(QUERY_SELECT_DATA_SCAD_PAG_BY_ID_RATA, params, String.class);

							//String strDate = dateFormat.format(data); 
							if(!dataScadPag.equals(data)) {
								Integer countNap = template.queryForObject(QUERY_SELECT_STATO_DEB_BY_NAP, params, Integer.class);
								if(countNap > 0) {
									ErrorDTO error = new ErrorDTO("400", "E076", "", null, null);
						            errorsList.add(error);
								}
							}
						}
					}

				}
				
				if(errorsList.isEmpty()) {
					isStatoDebitorioValid.setIsStatoDebitorioValid(true);
				}
				else {
					isStatoDebitorioValid.setIsStatoDebitorioValid(false);
					throw new GenericExceptionList(errorsList);
				}
			
		} catch (GenericExceptionList gel) {
			LOGGER.error("[StatoDebitorioDAOImpl::verifyStatoDebitorio] Son stati rilevati errori nel json inviato ");
			isStatoDebitorioValid.setIsStatoDebitorioValid(false);
			throw gel;
			
		} catch(Exception e) {
			LOGGER.debug("[StatoDebitorioDAOImpl::verifyStatoDebitorio] END");
			return null;
		}

	
		return isStatoDebitorioValid;
	}
	
	private BigDecimal getInteressiESpese(StatoDebitorioExtendedDTO statoDebitorio) {
		BigDecimal interessi = BigDecimal.ZERO;
		BigDecimal spese = BigDecimal.ZERO;
		for (RataSdDTO rata : statoDebitorio.getRate()) {
			if(rata.getInteressiMaturati()!= null) {
				interessi = interessi.add(rata.getInteressiMaturati());
			}
		}
		if (statoDebitorio.getImpSpeseNotifica() != null) {
			spese = statoDebitorio.getImpSpeseNotifica();
		}
		return interessi.add(spese);
	}

	public StatoDebitorioExtendedDTO loadStatoDebitorioAnnoPrecedente(Long idRiscossione) throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioAnnoPrecedente] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRiscossione", idRiscossione);
			MapSqlParameterSource params = getParameterValue(map);

			return template.queryForObject(getQuery(QUERY_SELECT_SD_ANNO_PRECEDENTE, null, null), params,
					getExtendedRowMapper());

		} catch(EmptyResultDataAccessException e) {
		    //Non esiste lo stato debitorio dell'anno precedente
		    return null;
		} catch (DataAccessException e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadStatoDebitorioAnnoPrecedente] Errore nell'accesso ai dati", e);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadStatoDebitorioAnnoPrecedente] Errore SQL ", e); 
			return null;
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioAnnoPrecedente] END");
		}
		
	};
	

	public Integer updateNotaRinnovoStatoDebitorioWorking(Long idStatoDebitorio, String notaRinnovo)
			throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::updateNotaRinnovoStatoDebitorioWorking] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			map.put("notaRinnovo", notaRinnovo);

			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_UPDATE_NOTA_RINNOVO_WORKING;
			return template.update(getQuery(query, null, null), params);

		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::updateNotaRinnovoStatoDebitorioWorking] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::updateNotaRinnovoStatoDebitorioWorking] END");
		}
	}
	

	public Integer updateStatoDebitorioFromStatoDebitorioUpd(String attore)
			throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorioFromStatoDebitorioUpd] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			MapSqlParameterSource params = getParameterValue(map);
			
			String query = QUERY_UPDATE_SD_FROM_SD_UPD;
			return template.update(getQuery(query, null, null), params);

		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::updateStatoDebitorioFromStatoDebitorioUpd] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorioFromStatoDebitorioUpd] END");
		}
	}
	

	public List<StatoDebitorioDTO> loadStatoDebitorioWorkingByNap(String nap) throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByNap] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			MapSqlParameterSource params = getParameterValue(map);

			return template.query(getQuery(QUERY_SELECT_SD_WORKING_BY_NAP, null, null), params, getRowMapper());

		} catch (EmptyResultDataAccessException e) {
			// Non esiste lo stato debitorio dell'anno precedente
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadStatoDebitorioByNap] Errore nell'accesso ai dati", e);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadStatoDebitorioByNap] Errore SQL ", e);
			return null;
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByNap] END");
		}

	};
	

	public Integer updateStatoDebitorioFromStatoDebitorioWorking(StatoDebitorioDTO sdWorkingDto, String attore)
			throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorioFromStatoDebitorioWorking] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("nap", sdWorkingDto.getNap());
			map.put("numRichiestaProtocollo", sdWorkingDto.getNumRichiestaProtocollo());
			map.put("dataRichiestaProtocollo", sdWorkingDto.getDataRichiestaProtocollo());
			map.put("impCompensazioneCanone", sdWorkingDto.getImpCompensazioneCanone());
			map.put("idStatoDebitorio", sdWorkingDto.getIdStatoDebitorio());
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_UPDATE_SD_FROM_WORKING;
			return template.update(getQuery(query, null, null), params);

		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::updateStatoDebitorioFromStatoDebitorioWorking] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorioFromStatoDebitorioWorking] END");
		}
	}
	

	public Integer deleteStatoDebitorioWorkingByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::deleteRataSdWorkingByIdStatoDebitorio] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_SD_WORKING_BY_SD, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[StatoDebitorioDAOImpl::deleteStatoDebitorioWorkingByIdStatoDebitorio] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::deleteStatoDebitorioWorkingByIdStatoDebitorio] END");
		}

		return res;
	}
	

	public Integer deleteStatoDebitorioUpdWorkingByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::deleteStatoDebitorioUpdWorkingByIdStatoDebitorio] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_SD_UPD_WORKING_BY_SD, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[StatoDebitorioDAOImpl::deleteStatoDebitorioUpdWorkingByIdStatoDebitorio] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::deleteStatoDebitorioUpdWorkingByIdStatoDebitorio] END");
		}

		return res;
	}
	
	private RowMapper<UtRimbDTO> getUtRimbRowMapper() throws SQLException {

		return new UtRimbRowMapper();
	}
	/**
	 * The type UtRimb RowMapper
	 */
	public static class UtRimbRowMapper implements RowMapper<UtRimbDTO> {
		
		/**
		 * Instantiates a new UtRimb RowMapper
		 *
		 * @throws SQLException the sql exception
		 */
		public UtRimbRowMapper() throws SQLException {
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
		public UtRimbDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			UtRimbDTO bean = new UtRimbDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, UtRimbDTO bean) throws SQLException {
	
			bean.setCodUtenza(rs.getString("cod_riscossione"));
			bean.setDescPeriodoPagamento(rs.getString("desc_periodo_pagamento"));

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
	public RowMapper<StatoDebitorioDTO> getRowMapper() throws SQLException {
		return new StatoDebitorioRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<StatoDebitorioExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new StatoDebitorioExtendedRowMapper();
	}

	/**
	 * The type Stato Debitorio row mapper.
	 */
	public static class StatoDebitorioRowMapper implements RowMapper<StatoDebitorioDTO> {
		
		/**
		 * Instantiates a new Stato Debitorio row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public StatoDebitorioRowMapper() throws SQLException {
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
		public StatoDebitorioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			StatoDebitorioDTO bean = new StatoDebitorioDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, StatoDebitorioDTO bean) throws SQLException {
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setTipoTitolo(rs.getString("des_tipo_titolo"));
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
			bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
			
			bean.setIdRecapito(rs.getLong("id_recapito"));
			bean.setIdAttivitaStatoDeb(rs.getLong("id_attivita_stato_deb"));
			bean.setIdStatoContribuzione(rs.getLong("id_stato_contribuzione"));
			bean.setNotaRinnovo(rs.getString("nota_rinnovo"));
			
			
			bean.setIdTipoDilazione(rs.getLong("id_tipo_dilazione"));
			bean.setNap(rs.getString("nap"));
			bean.setNumTitolo(rs.getString("num_titolo"));
			bean.setDataProvvedimento(rs.getDate("data_provvedimento"));
			bean.setNumRichiestaProtocollo(rs.getString("num_richiesta_protocollo"));
			bean.setDataRichiestaProtocollo(rs.getDate("data_richiesta_protocollo"));
			bean.setDataUltimaModifica(rs.getDate("data_ultima_modifica"));
			bean.setDesUsi(rs.getString("des_usi"));
			bean.setNote(rs.getString("note"));
			bean.setImpRecuperoCanone(rs.getBigDecimal("imp_recupero_canone"));
			bean.setImpRecuperoInteressi(rs.getBigDecimal("imp_recupero_interessi"));
			bean.setImpSpeseNotifica(rs.getBigDecimal("imp_spese_notifica"));
			bean.setImpCompensazioneCanone(rs.getBigDecimal("imp_compensazione_canone"));
			bean.setDescPeriodoPagamento(rs.getString("desc_periodo_pagamento"));
			bean.setDescMotivoAnnullo(rs.getString("desc_motivo_annullo"));
			bean.setFlgAnnullato(rs.getInt("flg_annullato"));
			bean.setFlgRestituitoMittente(rs.getInt("flg_restituito_mittente"));
			bean.setFlgInvioSpeciale(rs.getInt("flg_invio_speciale"));
			bean.setFlgDilazione(rs.getInt("flg_dilazione"));
			bean.setFlgAddebitoAnnoSuccessivo(rs.getInt("flg_addebito_anno_successivo"));

			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));
			
		}
		
		private boolean rsHasColumn(ResultSet rs, String column){
		    try{
		        rs.findColumn(column);
		        return true;
		    } catch (SQLException sqlex){
		        //Column not present in resultset
		    }
		    return false;
		}
	}
	/**
	 * The type Stato Debitorio row mapper.
	 */
	public static class StatoDebitorioExtendedRowMapper implements RowMapper<StatoDebitorioExtendedDTO> {
		
		/**
		 * Instantiates a new Stato Debitorio row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public StatoDebitorioExtendedRowMapper() throws SQLException {
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
		public StatoDebitorioExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			StatoDebitorioExtendedDTO bean = new StatoDebitorioExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, StatoDebitorioExtendedDTO bean) throws SQLException {
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setTipoTitolo(rs.getString("des_tipo_titolo"));
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
			if(rs.getLong("id_gruppo_soggetto") > 0) 
				bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
			
			bean.setIdRecapito(rs.getLong("id_recapito"));
			if(rs.getLong("id_attivita_stato_deb") > 0)
			    bean.setIdAttivitaStatoDeb(rs.getLong("id_attivita_stato_deb"));
			if(rs.getLong("id_stato_contribuzione") > 0)
			    bean.setIdStatoContribuzione(rs.getLong("id_stato_contribuzione"));
			bean.setNotaRinnovo(rs.getString("nota_rinnovo"));
			
			if(rs.getLong("id_tipo_dilazione") > 0)
			bean.setIdTipoDilazione(rs.getLong("id_tipo_dilazione"));
			bean.setNap(rs.getString("nap"));
			bean.setNumTitolo(rs.getString("num_titolo"));
			bean.setDataProvvedimento(rs.getDate("data_provvedimento"));
			bean.setNumRichiestaProtocollo(rs.getString("num_richiesta_protocollo"));
			bean.setDataRichiestaProtocollo(rs.getDate("data_richiesta_protocollo"));
			bean.setDataUltimaModifica(rs.getDate("data_ultima_modifica"));
			bean.setDesUsi(rs.getString("des_usi"));
			bean.setNote(rs.getString("note"));
			bean.setImpRecuperoCanone(rs.getBigDecimal("imp_recupero_canone"));
			bean.setImpRecuperoInteressi(rs.getBigDecimal("imp_recupero_interessi"));
			bean.setImpSpeseNotifica(rs.getBigDecimal("imp_spese_notifica"));
			bean.setImpCompensazioneCanone(rs.getBigDecimal("imp_compensazione_canone"));
			bean.setDescPeriodoPagamento(rs.getString("desc_periodo_pagamento"));
			bean.setDescMotivoAnnullo(rs.getString("desc_motivo_annullo"));
			bean.setFlgAnnullato(rs.getInt("flg_annullato"));
			bean.setFlgRestituitoMittente(rs.getInt("flg_restituito_mittente"));
			bean.setFlgInvioSpeciale(rs.getInt("flg_invio_speciale"));
			bean.setFlgDilazione(rs.getInt("flg_dilazione"));
			bean.setFlgAddebitoAnnoSuccessivo(rs.getInt("flg_addebito_anno_successivo"));
			
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));
			
		}
		
		private boolean rsHasColumn(ResultSet rs, String column){
		    try{
		        rs.findColumn(column);
		        return true;
		    } catch (SQLException sqlex){
		        //Column not present in resultset
		    }
		    return false;
		}
	}

}