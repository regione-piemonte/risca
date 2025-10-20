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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AccertamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaUsoSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AttivitaStatoDebitorioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.CalcoloInteresseDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoSdUtilizzatoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoContribuzioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.UsoRidaumSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.AvvisoUsoDAOImpl.BigDecimalRowMapper;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente.StatoDebitorioAmbienteDAOImpl;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.mapper.AccertamentoDaoRowMapper;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.mapper.StatoDebitorioDaoRowMapper;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.tributi.StatoDebitorioTributiDAOImpl;
import it.csi.risca.riscabesrv.dto.AccertamentoDTO;
import it.csi.risca.riscabesrv.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdDTO;
import it.csi.risca.riscabesrv.dto.AttivitaStatoDebitorioDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagExtendedDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RicercaPagamentiDaVisionareDTO;
import it.csi.risca.riscabesrv.dto.RimborsoDTO;
import it.csi.risca.riscabesrv.dto.RimborsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RimborsoSdUtilizzatoDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneDTO;
import it.csi.risca.riscabesrv.dto.SommaCanoneTipoUsoSdDTO;
import it.csi.risca.riscabesrv.dto.StatoContribuzioneDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.UsoRidaumSdExtendedDTO;
import it.csi.risca.riscabesrv.dto.UtRimbDTO;
import it.csi.risca.riscabesrv.dto.VerifyStatoDebitorioDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type StatoDebitorio dao.
 *
 * @author CSI PIEMONTE
 */
public class StatoDebitorioDAOImpl extends RiscaBeSrvGenericDAO<StatoDebitorioDTO> implements StatoDebitorioDAO { 

	private final String className = this.getClass().getSimpleName();

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
	private RataSdDAO rataSdDAO;

	@Autowired
	private DettaglioPagDAO dettaglioPagDAO;

	@Autowired
	private RiscossioneDAO riscossioneDAO;

	@Autowired
	private AccertamentoDAO accertamentoDAO;

	@Autowired
	private StatoDebitorioAmbienteDAOImpl sdAmbienteDaoImpl;

	@Autowired
	private StatoDebitorioTributiDAOImpl sdTributiDaoImpl;

	@Autowired
	private AmbitiDAO ambitiDAO;

	@Autowired
	private MessaggiDAO messaggiDAO;
	
	@Autowired
	private CalcoloInteresseDAO calcoloInteresseDAO;
	
	private static final String AMBIENTE = "AMBIENTE";
	private static final String OPERE_PUBBLICHE = "Opere pubbliche";
	private static final String ATTIVITA_ESTRATTIVE = "Attivita estrattive";
	private static final String TRIBUTI = "TRIBUTI";

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

	public static final String QUERY_INSERT_UPD_DA_COMPENSARE = " insert into RISCA_W_STATO_DEBITORIO_UPD "
			+ "(id_elabora, id_stato_debitorio, id_riscossione, des_tipo_titolo, "
			+ "id_soggetto, id_gruppo_soggetto, id_recapito, id_attivita_stato_deb, "
			+ "id_stato_contribuzione, id_tipo_dilazione, nap, num_titolo, "
			+ "data_provvedimento, num_richiesta_protocollo,  "
			+ "data_richiesta_protocollo, data_ultima_modifica, des_usi, note,  "
			+ "imp_recupero_canone, imp_recupero_interessi, imp_spese_notifica, "
			+ "imp_compensazione_canone, desc_periodo_pagamento,  "
			+ "desc_motivo_annullo, flg_annullato, flg_restituito_mittente,  "
			+ "flg_invio_speciale, flg_dilazione, flg_addebito_anno_successivo, nota_rinnovo ) "
			+ "select :idElabora, id_stato_debitorio, sd.id_riscossione, des_tipo_titolo, "
			+ "id_soggetto, id_gruppo_soggetto, id_recapito, id_attivita_stato_deb,  "
			+ "id_stato_contribuzione, id_tipo_dilazione, nap, num_titolo,  "
			+ "data_provvedimento, num_richiesta_protocollo, "
			+ "data_richiesta_protocollo, data_ultima_modifica, des_usi, note,  "
			+ "imp_recupero_canone, imp_recupero_interessi, imp_spese_notifica,  "
			+ "imp_compensazione_canone, desc_periodo_pagamento,  "
			+ "desc_motivo_annullo, flg_annullato, flg_restituito_mittente,  "
			+ "flg_invio_speciale, flg_dilazione, flg_addebito_anno_successivo, nota_rinnovo "
			+ "from RISCA_T_STATO_DEBITORIO sd " + "inner join ( "
			+ "			select ris.id_riscossione, tri.id_tipo_riscossione, tri.id_ambito "
			+ "			from risca_t_riscossione ris "
			+ "			inner join risca_d_tipo_riscossione tri on ris.id_tipo_riscossione = tri.id_tipo_riscossione  "
			+ "			where tri.id_ambito = :idAmbito "
			+ "           ) risc_ambito on sd.id_riscossione = risc_ambito.id_riscossione where sd.id_stato_debitorio IN ( "
			+ "                           select id_stato_debitorio "
			+ "                           from RISCA_R_RIMBORSO "
			+ "                           where id_tipo_rimborso = 2 ) ";

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
			+ " gest_data_upd = :gestDataUpd , " + "gest_attore_upd= :gestAttoreUpd, nota_rinnovo = :notaRinnovo "

			+ " where id_stato_debitorio  = :idStatoDebitorio ";

	public static final String QUERY_UPDATE_UPD_SD_REGOLARIZZATO = "update RISCA_W_STATO_DEBITORIO_UPD "
			+ " set id_attivita_stato_deb = NULL, "
			+ " id_stato_contribuzione = (select id_stato_contribuzione from risca_d_stato_contribuzione where cod_stato_contribuzione =  'RO') "
			+ " where id_stato_debitorio = :idStatoDebitorio " + " and id_elabora = :idElabora ";

	public static final String QUERY_UPDATE_WORKING_COMPENSAZIONE = "update RISCA_W_STATO_DEBITORIO "
			+ " set imp_compensazione_canone = COALESCE(imp_compensazione_canone, 0) +  COALESCE(:compensazione, 0) "
			+ " where id_stato_debitorio  = :idStatoDebitorio ";

	public static final String QUERY_SELECT_ALL_STATI_DEBITORI = "select  rtsd.* from RISCA_T_STATO_DEBITORIO rtsd "
			+ " inner join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio "
			+ " where rrrs.id_rata_sd_padre is null ";

	public static final String QUERY_COUNT_ALL_STATI_DEBITORI = "select  COUNT(rtsd.*) from RISCA_T_STATO_DEBITORIO rtsd "
			+ " inner join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio "
			+ " where rrrs.id_rata_sd_padre is null ";

	public static final String QUERY_SELECT_STATI_DEBITORI_BY_ID_STATO_DEBITORIO = "select rtsd.* from RISCA_T_STATO_DEBITORIO rtsd "
			+ "where rtsd.id_stato_debitorio = :idStatoDebitorio";

	public static final String QUERY_SELECT_STATI_DEBITORI_BY_ID_RISCOSSIONE = "select  rtsd.* from RISCA_T_STATO_DEBITORIO rtsd "
			+ " inner join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio "
			+ " where rtsd.id_riscossione = :idRiscossione" + " and rrrs.id_rata_sd_padre is null ";

	public static final String QUERY_COUNT_STATI_DEBITORI_BY_ID_RISCOSSIONE = "select  COUNT(rtsd.*) from RISCA_T_STATO_DEBITORIO rtsd "
			+ " inner join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio "
			+ " where rtsd.id_riscossione = :idRiscossione" + " and rrrs.id_rata_sd_padre is null ";

	public static final String QUERY_SELECT_MAX_DATA_PAGAMENTO_BY_ID_STATO_DEBITORIO = "select max(rtp.data_op_val) as data_pagamento from RISCA_T_STATO_DEBITORIO rtsd "
			+ " inner join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio "
			+ " inner join risca_r_dettaglio_pag rrdp on rrrs.id_rata_sd = rrdp.id_rata_sd "
			+ " inner join risca_t_pagamento rtp on rrdp.id_pagamento = rtp.id_pagamento "
			+ " where rtsd.id_stato_debitorio = :idStatoDebitorio  and rrrs.id_rata_sd_padre is null ";

	public static final String ID_STATO_DEBITORIO = " and rtsd.id_stato_debitorio <> :idStatoDebitorio";

	public static final String QUERY_SELECT_STATI_DEB_WITH_FLG_INVIO_SPECIALE = "select count(*) from risca_t_stato_debitorio rtsd "
			+ " where rtsd.id_riscossione = :idRiscossione and rtsd.flg_invio_speciale = 1";

	public static final String QUERY_SELECT_STATI_DEB_WITH_FLG_INVIO_SPECIALE_BY_ID_STATO_DEB = "select count(*) from risca_t_stato_debitorio rtsd "
			+ "where rtsd.id_riscossione = :idRiscossione and rtsd.flg_invio_speciale = 1 and rtsd.id_stato_debitorio != :idStatoDebitorio";

	public static final String QUERY_SELECT_DATA_SCAD_PAG_BY_ID_RATA = "select rrrsd.data_scadenza_pagamento from risca_r_rata_sd rrrsd where rrrsd.id_rata_sd = :idRataSd";

	public static final String QUERY_SELECT_STATO_DEB_BY_NAP = "select count(*) from risca_t_stato_debitorio rtsd where rtsd.nap = :nap";

	public static final String QUERY_SELECT_UT_RIMBO_BY_ID_ROMBORSO = "select rtr.cod_riscossione , rtsd.desc_periodo_pagamento  from risca_r_rimborso_sd_utilizzato rrrsu "
			+ "join risca_t_stato_debitorio rtsd on rrrsu.id_stato_debitorio = rtsd.id_stato_debitorio "
			+ "join risca_t_riscossione rtr on rtsd.id_riscossione = rtr.id_riscossione "
			+ "WHERE rrrsu.id_rimborso  = :idRimborso";

	public static final String QUERY_SELECT_STATI_DEBITORI_BY_NAP = "select  rtsd.* from RISCA_T_STATO_DEBITORIO rtsd "
			+ " inner join risca_t_riscossione rtr on rtsd.id_riscossione = rtr.id_riscossione "
			+ " inner join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio "
			+ " where upper(rtsd.nap) ilike '%'||:nap||'%' AND rrrs.id_rata_sd_padre is null ";

	public static final String QUERY_COUNT_STATI_DEBITORI_BY_NAP = "select COUNT(rtsd.*) from RISCA_T_STATO_DEBITORIO rtsd "
			+ " inner join risca_t_riscossione rtr on rtsd.id_riscossione = rtr.id_riscossione "
			+ " where upper(rtsd.nap) ilike '%'||:nap||'%' ";

	public static final String QUERY_SELECT_STATI_DEBITORI_BY_NAP_GOUP_BY_COD_RISCOSSIONE = "select count(numero_utenza) numero_utenza from (select  count(rtr.cod_riscossione) numero_utenza from RISCA_T_STATO_DEBITORIO rtsd "
			+ "	 inner join risca_t_riscossione rtr on rtsd.id_riscossione = rtr.id_riscossione "
			+ "	where rtsd.nap = :nap" + "	group by rtr.cod_riscossione) t  ";

	public static final String QUERY_SELECT_NOTA_RINNOVO = " select max(rrp.data_provvedimento) data_provvedimento from risca_r_provvedimento rrp "
			+ "join risca_d_tipo_provvedimento rdtp  on rrp.id_tipo_provvedimento = rdtp.id_tipo_provvedimento "
			+ "where rrp.id_riscossione  = :idRiscossione " + "and rdtp.cod_tipo_provvedimento ='IST_RINNOVO' "
			+ "and rdtp.flg_istanza = 1 ";

	public static final String QUERY_SELECT_DESC_TIPO_TITOLO = "select rdtt.des_tipo_titolo "
			+ "from  risca_r_provvedimento rrp "
			+ "left join risca_d_tipo_titolo rdtt on rrp.id_tipo_titolo = rdtt.id_tipo_titolo "
			+ "where rrp.id_provvedimento  = :idProvvedimento ";

	public static final String QUERY_SELECT_SD_ANNO_PRECEDENTE = "select sd.* " + " from RISCA_T_STATO_DEBITORIO sd, "
			+ "	(select MAX(id_stato_debitorio) max_id, id_riscossione " + "	 from RISCA_T_STATO_DEBITORIO "
			+ "	 where id_riscossione = :idRiscossione " + "	 group by id_riscossione " + "	) st_deb_max "
			+ " where sd.id_riscossione = :idRiscossione " + "     and sd.id_stato_debitorio = st_deb_max.max_id "
			+ "     and sd.id_riscossione = st_deb_max.id_riscossione "
			+ "     and sd.flg_addebito_anno_successivo = 1";

	public static final String QUERY_UPDATE_NOTA_RINNOVO_WORKING = " update RISCA_W_STATO_DEBITORIO "
			+ " set nota_rinnovo = :notaRinnovo " + " where id_stato_debitorio = :idStatoDebitorio ";

	public static final String QUERY_UPDATE_SD_FROM_SD_UPD = "update RISCA_T_STATO_DEBITORIO a "
			+ " set (id_attivita_stato_deb, id_stato_contribuzione, gest_attore_upd, gest_data_upd) = " + " ( "
			+ "     select id_attivita_stato_deb, id_stato_contribuzione, :gestAttoreUpd, to_timestamp(:gestDataUpd, 'YYYY-MM-DD HH24:MI:SS.MS') "
			+ "     from RISCA_W_STATO_DEBITORIO_UPD b " + "     where a.id_stato_debitorio = b.id_stato_debitorio "
			+ "     and id_elabora = :idElabora1 " + " ) "
			+ " where a.id_stato_debitorio IN (select id_stato_debitorio from RISCA_W_STATO_DEBITORIO_UPD where id_elabora = :idElabora2 ) ";

	public static final String QUERY_SELECT_NUMBER_REPETETION_NAP = "select  COUNT(rtsd.*) as num_ripetizioni_nap from RISCA_T_STATO_DEBITORIO rtsd "
			+ "where rtsd.nap = :nap";

	public static final String QUERY_SELECT_SD_WORKING_BY_NAP = " select * from risca_w_stato_debitorio where nap = :nap order by nap ";

	public static final String QUERY_UPDATE_SD_FROM_WORKING = " update RISCA_T_STATO_DEBITORIO " + " set nap = :nap, "
			+ " flg_invio_speciale = 0, " + " num_richiesta_protocollo = :numRichiestaProtocollo, "
			+ " data_richiesta_protocollo = :dataRichiestaProtocollo, "
			+ " imp_compensazione_canone = :impCompensazioneCanone, " + " gest_attore_upd = :gestAttoreUpd, "
			+ " gest_data_upd = :gestDataUpd " + " where id_stato_debitorio = :idStatoDebitorio";

	private static final String QUERY_DELETE_SD_WORKING_BY_SD = " delete from RISCA_W_STATO_DEBITORIO where id_stato_debitorio = :idStatoDebitorio ";

	private static final String QUERY_DELETE_SD_UPD_WORKING_BY_ELAB = " delete from risca_w_stato_debitorio_upd where id_elabora = :idElabora ";

	private static final String QUERY_SELECT_IMPORTO_VERSATO_BY_ID_STATO_DEBITORIO = "select SUM(rrdp.importo_versato) importo_versato from risca_r_dettaglio_pag rrdp "
			+ "inner join risca_t_pagamento rtp " + "on rtp.id_pagamento = rrdp.id_pagamento "
			+ "inner join risca_r_rata_sd rrrs   " + "on rrrs.id_rata_sd  = rrdp.id_rata_sd "
			+ "inner join risca_t_stato_debitorio rtsd    " + "on rtsd.id_stato_debitorio  = rrrs.id_stato_debitorio  "
			+ "where rrrs.id_rata_sd_padre is null " + "and rtsd.id_stato_debitorio = :idStatoDebitorio";

	private static final String QUERY_CHECK_ST_ANNULATO_ACCERTAMENTO = "	select count(*)   from risca_t_stato_debitorio rtsd "
			+ "	inner join risca_t_accertamento rta on rtsd.id_stato_debitorio = rta.id_stato_debitorio "
			+ "	where rtsd.id_stato_debitorio = :idStatoDebitorio "
			+ "	and (rta.id_tipo_accertamento  = 2 or rta.id_tipo_accertamento = 3) " + "	and rta.flg_annullato = 0";

	private static final String QUERY_UPD_ATTIVITA_STATO_DEB = "update RISCA_T_STATO_DEBITORIO "
			+ "set id_attivita_stato_deb = null " + "where id_stato_debitorio = :idStatoDebitorio "
			+ "and id_attivita_stato_deb NOT IN (5, 7, 9, 10) ";

	private static final String QUERY_INSERT_SD_FROM_WORKING = " insert into RISCA_T_STATO_DEBITORIO ( "
			+ "id_stato_debitorio, id_riscossione, des_tipo_titolo, id_soggetto, id_gruppo_soggetto, id_recapito,  "
			+ "id_attivita_stato_deb, id_stato_contribuzione, id_tipo_dilazione, nap, num_titolo, data_provvedimento,   "
			+ "num_richiesta_protocollo, data_richiesta_protocollo, data_ultima_modifica, des_usi, note, "
			+ "imp_recupero_canone, imp_recupero_interessi, imp_spese_notifica, imp_compensazione_canone,  "
			+ "desc_periodo_pagamento, desc_motivo_annullo, flg_annullato, flg_restituito_mittente,  "
			+ "flg_invio_speciale, flg_dilazione,  flg_addebito_anno_successivo, nota_rinnovo, "
			+ "gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid ) "
			+ "select id_stato_debitorio, id_riscossione, des_tipo_titolo, id_soggetto, id_gruppo_soggetto, id_recapito,  "
			+ "id_attivita_stato_deb, id_stato_contribuzione, id_tipo_dilazione, nap, num_titolo, data_provvedimento,  "
			+ "num_richiesta_protocollo, data_richiesta_protocollo, data_ultima_modifica, des_usi, note, "
			+ "imp_recupero_canone, imp_recupero_interessi, imp_spese_notifica,imp_compensazione_canone,  "
			+ "desc_periodo_pagamento, desc_motivo_annullo, flg_annullato, flg_restituito_mittente,  "
			+ "flg_invio_speciale, flg_dilazione, flg_addebito_anno_successivo, nota_rinnovo, "
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid "
			+ "from RISCA_W_STATO_DEBITORIO where nap = :nap ";

	public static final String QUERY_SELECT_IMPORTO_SPESE_NOTIFICA_BY_ID_STATO_DEBITORIO = "select rtsd.imp_spese_notifica   from risca_t_stato_debitorio rtsd "
			+ "where rtsd.id_stato_debitorio = :idStatoDebitorio ";

	public static final String QUERY_SELECT_STATI_DEBITORI_BY_COD_UTENZA = "select  rtsd.* from RISCA_T_STATO_DEBITORIO rtsd "
			+ " inner join risca_t_riscossione rtr on rtsd.id_riscossione = rtr.id_riscossione "
			+ " inner join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio "
			+ " where upper(rtr.cod_riscossione) = :codUtenza AND rrrs.id_rata_sd_padre is null ";

	public static final String QUERY_COUNT_STATI_DEBITORI_BY_COD_UTENZA = "select COUNT(rtsd.*) from RISCA_T_STATO_DEBITORIO rtsd "
			+ " inner join risca_t_riscossione rtr on rtsd.id_riscossione = rtr.id_riscossione "
			+ "where upper(rtr.cod_riscossione) = :codUtenza";

	public static final String ORDER_BY_SD = " ORDER BY rtsd.id_stato_debitorio asc ";

	private static final String QUERY_UPD_ATTIVITA_FOR_ALL_STATI_DEB = "update RISCA_T_STATO_DEBITORIO "
			+ " set id_attivita_stato_deb = :idAttivitaStatoDeb,"
			+ " gest_attore_upd = :gestAttoreUpd, gest_data_upd = :gestDataUpd "
			+ " where id_stato_debitorio IN ( :listIdStatoDebitorio) ";

	public static final String QUERY_COUNT_STATI_DEBITORI_BY_SOGG_TIPO_ATTIVITA = "select count(*) "
			+ "from RISCA_T_STATO_DEBITORIO sd,  RISCA_T_RISCOSSIONE r "
			+ "where sd.id_attivita_stato_deb = :idAttivitaStatoDeb " + "and sd.id_riscossione = r.id_riscossione "
			+ "and r.id_soggetto = :idSoggetto ";
	public static final String CONDITION_GRUPPO = " and r.id_gruppo_soggetto = :idGruppoSoggetto ";
	public static final String CONDITION_GRUPPO_NULL = " and r.id_gruppo_soggetto is null ";

//	private static final String QUERY_SELECT_ALL_STATI_DEBITORI_IN_RO_EC = "select  id_stato_debitorio " 
//	+ " , id_riscossione ,	id_soggetto, id_gruppo_soggetto ,id_recapito ,id_attivita_stato_deb, id_stato_contribuzione, id_tipo_dilazione ,"
//	+ "	num_titolo ,data_provvedimento ,num_richiesta_protocollo , data_richiesta_protocollo ,	data_ultima_modifica ," 
//	+ "	des_usi , note , imp_recupero_canone ,	imp_recupero_interessi ,	imp_spese_notifica,	imp_compensazione_canone ,"
//	+ "	desc_periodo_pagamento ,desc_motivo_annullo  ,flg_dilazione, flg_annullato,	flg_restituito_mittente ,flg_invio_speciale ," 
//	+ "	flg_addebito_anno_successivo ,nap ,nota_rinnovo, des_tipo_titolo  "
//	+ " from risca_t_stato_debitorio where (id_stato_contribuzione is null or id_stato_contribuzione not in ("+Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.REGOLARE+","+Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.REGOLARIZZATO+"))  order by id_stato_debitorio ";	
	// + " limit :limit "
	// + " offset :offset ";

	public static final String QUERY_COUNT_ALL_STATI_DEBITORI_SIMPLE = "select  COUNT(*) from RISCA_T_STATO_DEBITORIO  ";

	private static final String QUERY_UPD_STATO_CONTRIBUZIONE_FOR_STATO_DEB = "update RISCA_T_STATO_DEBITORIO "
			+ "set id_stato_contribuzione = :idStatoContribuzione , gest_data_upd = CURRENT_TIMESTAMP , gest_attore_upd = :gestAttoreUpd where id_stato_debitorio = :idStatoDebitorio ";

	public static final String ESCLUDE_SD = " AND rtsd.id_stato_debitorio NOT IN (:sdDaEscludere) ";

	private static final String QUERY_UPD_SPESE_NOTIFICA = " UPDATE RISCA_T_STATO_DEBITORIO "
			+ "SET  id_attivita_stato_deb = NULL, imp_spese_notifica = :impSpeseNotifica, gest_data_upd = :gestDataUpd , gest_attore_upd = :gestAttoreUpd "
			+ "WHERE id_stato_debitorio = :idStatoDebitorio ";

	public static final String QUERY_SELECT_ID_RISCOSSIONE_BY_ID_STATO_DEBITORIO = "select rtsd.id_riscossione from RISCA_T_STATO_DEBITORIO rtsd "
			+ " where rtsd.id_stato_debitorio = :idStatoDebitorio ";

	public static final String QUERY_SOMMA_CANONE_DOVUTO = "SELECT SUM(canone_dovuto) FROM ( "
			+ "    SELECT "
			+ "        COALESCE(rtsd.imp_recupero_canone, 0) + "
			+ "        COALESCE(SUM(rras.canone_annuo), 0) AS canone_dovuto"
			+ "    FROM "
			+ "        risca_t_stato_debitorio rtsd"
			+ "    left JOIN "
			+ "        risca_r_annualita_sd rras ON rtsd.id_stato_debitorio = rras.id_stato_debitorio"
			+ "    WHERE "
			+ "        rtsd.nap = :nap"
			+ "    GROUP BY "
			+ "        rtsd.imp_recupero_canone"
			+ ") AS somma_canone";

	
	
	public static final String QUERY_LOAD_ID_SD_BY_ID_RATA = "select rtsd.id_stato_debitorio  from risca_t_stato_debitorio rtsd "
			+ "	inner join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio "
			+ "	where rrrs.id_rata_sd  = :idRata";
	
	public static final String QUERY_SELECT_STATI_DEBITORI_FOR_PAGAMENTI_DA_VISIONARE = "select  rtsd.* from RISCA_T_STATO_DEBITORIO rtsd "
			+ " inner join risca_t_riscossione rtr on rtsd.id_riscossione = rtr.id_riscossione "
			+ " inner join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio ";
		
	public static final String	QUERY_JOIN_SOGGETTO_PRATICA = " inner join risca_t_soggetto rts   on"
			+ "	rtr.id_soggetto  = rts.id_soggetto	";
	
	public static final String	QUERY_JOIN_SOGGETTO_SD = " inner join risca_t_soggetto rts   on"
			+ "	rtsd.id_soggetto  = rts.id_soggetto	";
	
	public static final String	QUERY_WHERE_STATI_DEBITORI_BY_NUM_PRATICA = "  upper(rtr.num_pratica) = :numPratica AND rrrs.id_rata_sd_padre is null ";
	public static final String	QUERY_WHERE_STATI_DEBITORI_BY_COD_UTENZA = "  upper(rtr.cod_riscossione) = :codUtenza AND rrrs.id_rata_sd_padre is null ";
	public static final String	QUERY_WHERE_STATI_DEBITORI_BY_NAP =  "  upper(rtsd.nap) ilike '%'||:nap||'%' AND rrrs.id_rata_sd_padre is null ";
	public static final String IMPORTO_DA =" rrrs.canone_dovuto >= :importoDa";
	public static final String IMPORTO_A =" rrrs.canone_dovuto <= :importoA";
	public static final String TITOLARE = " (TRIM(upper(rts.den_soggetto)) ilike '%'||:titolare||'%' OR upper(rts.cognome || ' ' || rts.nome) ilike '%'||:titolare||'%' OR upper(rts.nome || ' ' || rts.cognome) ilike '%'||:titolare||'%' OR upper(rts.nome) ilike '%'||:titolare||'%' OR upper(rts.cognome) ilike '%'||:titolare||'%') ";	
	public static final String ESCLUDE_SD_FOR_PAGAMENTI_DA_VISIONARE = " rtsd.id_stato_debitorio NOT IN (:sdDaEscludere) ";
	
	public static final String QUERY_GET_TITOLARE_PRATICA_BY_ID_SOGGETTO = "select "
			+ "	  CASE "
			+ " WHEN rtgs.id_gruppo_soggetto is not null THEN rtgs.des_gruppo_soggetto "
			+ " WHEN ((rdts.cod_tipo_soggetto ='PF') and (rtgs.id_gruppo_soggetto is null) ) then ( rts.cognome|| ' ' ||  rts.nome ) "
			+ " WHEN (((rdts.cod_tipo_soggetto ='PG') or (rdts.cod_tipo_soggetto ='PB' )) and (rtgs.id_gruppo_soggetto is null) )THEN rts.den_soggetto "
			+ " END AS titolare"
			+ " from risca_t_soggetto rts "
			+ " left join risca_t_riscossione rtr on rts.id_soggetto = rtr.id_soggetto "
			+ " left join risca_d_tipo_soggetto rdts on rts.id_tipo_soggetto = rdts.id_tipo_soggetto "
			+ " left join risca_t_gruppo_soggetto rtgs on rtr.id_gruppo_soggetto = rtgs.id_gruppo_soggetto "
			+ " where rts.id_soggetto = :idSoggetto ";
	
	
	public static final String QUERY_COUNT_STATI_DEBITORI_FOR_PAGAMENTI_DA_VISIONARE = "select COUNT(rtsd.*) from RISCA_T_STATO_DEBITORIO rtsd "
			+ " inner join risca_t_riscossione rtr on rtsd.id_riscossione = rtr.id_riscossione "
			+ " inner join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio ";
	
	public static final String QUERY_SELECT_SOMMA_CANONI_PER_TIPO_USO = " select det_canone.id_stato_debitorio, det_canone.id_tipo_uso, det_canone.id_accerta_bilancio , "
			+ " det_canone.ordina_tipo_uso, sum (canone_uso) as totale_canone_uso  "
			+ " from (select sta.id_stato_debitorio, ras.id_annualita_sd, ras.anno, "
			+ "              aus.id_annualita_uso_sd , aus.canone_uso, aus.id_tipo_uso, "
			+ "              dtu.id_accerta_bilancio , dtu.ordina_tipo_uso "
			+ "         from risca_t_stato_debitorio sta "
			+ "   inner join risca_r_annualita_sd ras on sta.id_stato_debitorio = ras.id_stato_debitorio "
			+ "   inner join risca_r_annualita_uso_sd aus on ras.id_annualita_sd = aus.id_annualita_sd "
			+ "   inner join risca_d_tipo_uso dtu on aus.id_tipo_uso = dtu.id_tipo_uso "
			+ "        where sta.id_stato_debitorio = :idStatoDebitorio and dtu.id_tipo_uso_padre is null ) det_canone "
			+ " group by det_canone.id_stato_debitorio, det_canone.id_tipo_uso, det_canone.id_accerta_bilancio , det_canone.ordina_tipo_uso "
			+ " order by det_canone.ordina_tipo_uso ";
	
	
	public static final String QUERY_COUNT_STATI_DEBITORI_BY_ID_RECAPITO = "select count(*) from RISCA_T_STATO_DEBITORIO rtsd "
			+ " where rtsd.id_recapito =:idRecapito ";
	
	@Override
	public StatoDebitorioExtendedDTO saveStatoDebitorio(StatoDebitorioExtendedDTO dto, Long idAmbito)
			throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			dto = saveStatoDebitorio(dto, idAmbito, false, false, true);
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::saveStatoDebitorio] ERROR : ", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] END");
		}
		return dto;
	}

	@Override
	public StatoDebitorioExtendedDTO updateStatoDebitorio(StatoDebitorioExtendedDTO dto, Long idAmbito)
			throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		String ambito = "";
		StatoDebitorioExtendedDTO statoDeb = new StatoDebitorioExtendedDTO();
		Utils utils = new Utils();
		if (utils.isLocalMod()) {
			ambito = AMBIENTE;
			statoDeb = sdAmbienteDaoImpl.updateStatoDebitorio(dto);
		} else {
			// TO DO QUERY SU TABELLA AMBITO PER VERIFICA AMBITO
			LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorio] verifica ambito");
			AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			ambito = ambitoDTO.getCodAmbito();
			LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorio] ambito: " + ambito);
			switch (ambito) {
			case AMBIENTE:
				LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorio] ambito: AMBIENTE");
				statoDeb = sdAmbienteDaoImpl.updateStatoDebitorio(dto);
				break;
			case OPERE_PUBBLICHE:
				// TO DO
				break;
			case ATTIVITA_ESTRATTIVE:
				// TO DO
				break;
			case TRIBUTI:
				LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorio] ambito: TRIBUTI");
				statoDeb = sdTributiDaoImpl.updateStatoDebitorio(dto);
				break;
			}
		}
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorio] END");
		return statoDeb;
	}

	@Override
	public StatoDebitorioExtendedDTO saveStatoDebitorioWorking(StatoDebitorioExtendedDTO dto, boolean generateId)
			throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			dto = saveStatoDebitorioWorking(dto, true, false, generateId);
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::saveStatoDebitorioWorking] ERROR : ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorioWorking] END");
		}
		return dto;
	}

	private StatoDebitorioExtendedDTO saveStatoDebitorioWorking(StatoDebitorioExtendedDTO dto, boolean working,
			boolean upd, boolean generateId) throws DAOException, DataAccessException, SQLException {
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		Long genId = null;
		if (generateId) {
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
		map.put("idAttivitaStatoDeb", dto.getIdAttivitaStatoDeb() != null ? dto.getIdAttivitaStatoDeb()
				: dto.getAttivitaStatoDeb() != null ? dto.getAttivitaStatoDeb().getIdAttivitaStatoDeb() : null);
		map.put("idStatoContribuzione", dto.getIdStatoContribuzione() != null ? dto.getIdStatoContribuzione()
				: dto.getStatoContribuzione() != null ? dto.getStatoContribuzione().getIdStatoContribuzione() : null);
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
		if (!working && !upd) {
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
		}

		MapSqlParameterSource params = getParameterValue(map);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String query = QUERY_INSERT;
		if (working) {
			query = QUERY_INSERT_W;
		}
		if (upd) {
			query = QUERY_INSERT_UPD_DA_COMPENSARE;
		}
		template.update(getQuery(query, null, null), params, keyHolder);
		if (generateId) {
			dto.setIdStatoDebitorio(genId);
		}
		dto.setGestDataIns(now);
		dto.setGestDataUpd(now);
		return dto;
	}

	@Override
	public Integer saveStatiDebitoriUpdDaCompensare(Long idElabora, Long idAmbito) throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		int result = 0;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("idAmbito", idAmbito);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_INSERT_UPD_DA_COMPENSARE;

			result = template.update(getQuery(query, null, null), params, keyHolder);
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::saveStatiDebitoriUpdDaCompensare] ERROR : ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::saveStatiDebitoriUpdDaCompensare] END");
		}
		return result;
	}

	@Override
	public Integer updateStatoDebitorioUpdRegolarizzato(Long idStatoDebitorio, Long idElabora) throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			map.put("idElabora", idElabora);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_UPD_SD_REGOLARIZZATO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::updateStatoDebitorioUpdRegolarizzato] ERROR : ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorioUpdRegolarizzato] END");
		}

		return res;
	}

	@Override
	public Integer updateStatoDebitorioWorkingCompensazione(Long idStatoDebitorio, BigDecimal compensazione)
			throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			map.put("compensazione", compensazione);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_WORKING_COMPENSAZIONE, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::UpdateStatoDebitorioWorkingCompensazione] ERROR : ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::UpdateStatoDebitorioWorkingCompensazione] END");
		}

		return res;
	}

	private StatoDebitorioExtendedDTO saveStatoDebitorio(StatoDebitorioExtendedDTO dto, Long idAmbito, boolean working, boolean upd,
			boolean generateId) throws DAOException, DataAccessException, SQLException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	String ambito = "";
	StatoDebitorioExtendedDTO statoDeb = new StatoDebitorioExtendedDTO();
	Utils utils = new Utils();
    if(utils.isLocalMod()){
    	ambito = AMBIENTE;
	    statoDeb = sdAmbienteDaoImpl.saveStatoDebitorio(dto);
	}else {
			//TO DO QUERY SU TABELLA AMBITO PER VERIFICA AMBITO
			LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] verifica ambito");
			AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			ambito = ambitoDTO.getCodAmbito();
			LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] ambito: " + ambito);
			switch (ambito) {
			  case AMBIENTE:
				LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] ambito: AMBIENTE");
				statoDeb = sdAmbienteDaoImpl.saveStatoDebitorio(dto);
			    break;
			  case OPERE_PUBBLICHE:
				//TO DO
			    break;
			  case ATTIVITA_ESTRATTIVE:
				//TO DO
			    break;
			  case TRIBUTI:
				  LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] ambito: TRIBUTI");
				  statoDeb = sdTributiDaoImpl.saveStatoDebitorio(dto);
				  break;
			}
		}	   
		LOGGER.debug("[StatoDebitorioDAOImpl::saveStatoDebitorio] END");
        return statoDeb;
	}

	@Override
	public List<StatoDebitorioExtendedDTO> loadAllStatoDebitorioOrByIdRiscossione(Long idRiscossione,Integer offset,Integer limit,String sort) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<StatoDebitorioExtendedDTO> res = new ArrayList<StatoDebitorioExtendedDTO>();
        MapSqlParameterSource paramST = null;

        BigDecimal canoneDovuto = BigDecimal.ZERO;
		try {
			String dynamicOrderByCondition="";
			if (StringUtils.isNotBlank(sort)) {
				   dynamicOrderByCondition = mapSortConCampiDB(sort);
				   if(dynamicOrderByCondition != null) {
					   dynamicOrderByCondition = dynamicOrderByCondition.concat(",").concat(ORDER_BY_SD.replace("ORDER BY", ""));
				   }

				}else {
					dynamicOrderByCondition = ORDER_BY_SD;
				}
			
			if(idRiscossione == null)
				res =  template.query(getQuery(QUERY_SELECT_ALL_STATI_DEBITORI + dynamicOrderByCondition, offset!= null ? offset.toString(): null ,  limit != null ? limit.toString(): null), paramST, getExtendedRowMapper());
			
			
			else if(idRiscossione != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("idRiscossione", idRiscossione);
				paramST = getParameterValue(map);
				res =  template.query(getQuery(QUERY_SELECT_STATI_DEBITORI_BY_ID_RISCOSSIONE + dynamicOrderByCondition, offset!= null ? offset.toString(): null ,  limit != null ? limit.toString(): null), paramST, getExtendedRowMapper());
				
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
			        BigDecimal impCompensazioneCanone = BigDecimal.ZERO;
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
//						canoneDovuto = listAnnualitaSdDTO.stream().map(AnnualitaSdDTO::getCanoneAnnuo).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRecuperoCanone) ;
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
						impSpeseNotifica = statoDebitorioDTO.getImpSpeseNotifica();
					}
					if(ListRataSdDTO != null) {
						//[DP] inizio spostamento
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
						interessiMaturati = ListRataSdDTO.stream().filter(r -> r.getInteressiMaturati() != null && r.getIdRataSdPadre() == null).map(RataSdDTO::getInteressiMaturati).reduce(BigDecimal.ZERO, BigDecimal::add) ;
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
					
					
					//BigDecimal impMancImpEcc = importoVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati).subtract(impCompensazioneCanone)).subtract(sommRimborsi);
					//[VF] issue 75 non sottrarre impCompensazioneCanone
					BigDecimal impMancImpEcc = importoVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati)).subtract(sommRimborsi);
					statoDebitorioDTO.setImpMancanteImpEccedente(impMancImpEcc);
				
					BigDecimal intMaturatiSpeseNotifica =  BigDecimal.ZERO;
					intMaturatiSpeseNotifica = impSpeseNotifica.add(interessiMaturati);
					statoDebitorioDTO.setIntMaturatiSpeseNotifica(intMaturatiSpeseNotifica);
					
					statoDebitorioDTO.setRate(ListRataSdDTO);
					
					// Issue 81: il campo accImportoRimborsato deve tenere conto sia di rimborsi che
					// di eventuali compensazioni utilizzate, quindi valorizzo il campo con
					// sommRimborsi (come per il campo sommaRimborsi)
					// statoDebitorioDTO.setAccImportoRimborsato(impRimborso);
					statoDebitorioDTO.setAccImportoRimborsato(sommRimborsi);
					
//					BigDecimal importoEccedente =   BigDecimal.ZERO;
//					importoEccedente =	importoVersato.add(impRimborso).subtract(canoneDovuto).subtract(intMaturatiSpeseNotifica);
					if(impMancImpEcc.compareTo(BigDecimal.ZERO) > 0) {
						statoDebitorioDTO.setImportoEccedente(impMancImpEcc);
					}else {
						statoDebitorioDTO.setImportoEccedente(BigDecimal.ZERO);
					}

					
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
					

					String attivita = mapAttivitaSD(statoDebitorioDTO);
					statoDebitorioDTO.setAttivita(attivita);
					
					// parte di accertamento
					BigDecimal accImportoDovuto = BigDecimal.ZERO;
					String accDataScadenzaPag = null;
					BigDecimal accImportoVersato = BigDecimal.ZERO;
					BigDecimal accImportoDiCanoneMancante = BigDecimal.ZERO;
					BigDecimal accInteressiMancanti  = BigDecimal.ZERO;
					BigDecimal accInteressiVersati  = BigDecimal.ZERO; 
					BigDecimal statoDebImpSpesNot = BigDecimal.ZERO;
					if(ListRataSdDTO != null) {
						RataSdDTO rata = ListRataSdDTO.stream().filter(r -> r.getIdRataSdPadre() == null).collect(Collectors.toList()).get(0) ;
						BigDecimal canoneDovutoRata = rata.getCanoneDovuto() != null ? rata.getCanoneDovuto() : BigDecimal.ZERO;
						accImportoDovuto = accImportoDovuto.add(canoneDovutoRata);
						statoDebitorioDTO.setAccImportoDovuto(accImportoDovuto);
						
						accDataScadenzaPag = rata.getDataScadenzaPagamento();
						statoDebitorioDTO.setAccDataScadenzaPag(accDataScadenzaPag);
					
						if(statoDebitorioDTO.getImpSpeseNotifica() != null)
							statoDebImpSpesNot = statoDebitorioDTO.getImpSpeseNotifica();
						
						
						accImportoVersato = accImportoVersato.add(statoDebitorioDTO.getImportoVersato());
						statoDebitorioDTO.setAccImportoVersato(accImportoVersato);										
						
						//accImportoDiCanoneMancante = canoneDovuto.subtract(impRimborso).subtract(statoDebitorioDTO.getImportoVersato());
						//[DP] modificata analisi aggiunto imp rimborso invece che sottratto solo se maggiore di 0 
						if(impRimborso.compareTo(BigDecimal.ZERO) > 0){
						  accImportoDiCanoneMancante = canoneDovuto.add(impRimborso).subtract(statoDebitorioDTO.getImportoVersato());
						}else {
							accImportoDiCanoneMancante = canoneDovuto.subtract(statoDebitorioDTO.getImportoVersato());
						}
						
						//[DP] issue 26
						//[VF] issue 90: questa if secondo l'analisi va commentata
						//Vedi doc WP2-2.2-SRV-V102-Servizi_RISCABESRV - Pag. 192 
//						if(impCompensazioneCanone.compareTo(BigDecimal.ZERO) > 0) {
//							accImportoDiCanoneMancante = accImportoDiCanoneMancante.subtract(impCompensazioneCanone);
//						}
						
						
						
						if(accImportoDiCanoneMancante.compareTo(BigDecimal.ZERO) >= 0) {
							statoDebitorioDTO.setAccImportoDiCanoneMancante(accImportoDiCanoneMancante);
							// [VF] Fix per ISSUE 90 accInteressiMancanti deve essere valorizzata
							// correttamente per i calcoli successivi
							accInteressiMancanti = statoDebitorioDTO.getIntMaturatiSpeseNotifica();
							statoDebitorioDTO.setAccInteressiMancanti(accInteressiMancanti);
						}else {
							statoDebitorioDTO.setAccImportoDiCanoneMancante(BigDecimal.ZERO);
							//[DP] issue33 Da NUOVA SPECIFICA non si sottrae impRimborso
							//(- impCompensazioneCanone + accImportoRImborsato)
//							accInteressiMancanti = canoneDovutoRata.add(statoDebitorioDTO.getIntMaturatiSpeseNotifica())
//									.subtract(statoDebitorioDTO.getImportoVersato()).subtract(impRimborso);
							accInteressiMancanti = canoneDovutoRata.add(statoDebitorioDTO.getIntMaturatiSpeseNotifica())
									.subtract(statoDebitorioDTO.getImportoVersato()).add(impRimborso).subtract(impCompensazioneCanone);
							if(accInteressiMancanti.compareTo(BigDecimal.ZERO) > 0) {
								statoDebitorioDTO.setAccInteressiMancanti(accInteressiMancanti);
							}else {	
								statoDebitorioDTO.setAccInteressiMancanti(BigDecimal.ZERO);
								// [VF] Issue 90 - occorre azzerare questo valore altrimenti quando viene negativo 
								// influisce sul valore calcolato per accInteressiVersati
								accInteressiMancanti = BigDecimal.ZERO;
							}
							
						}
						//[DP] issue33 da nuova specifica accInteressiVersati = interessi e spese di notifica - accInteressiMancanti
						//accInteressiVersati = statoDebitorioDTO.getImportoVersato().add(impRimborso).subtract(canoneDovuto);
						accInteressiVersati =  intMaturatiSpeseNotifica.subtract(accInteressiMancanti);
						if(accInteressiVersati.compareTo(BigDecimal.ZERO) > 0) {
							statoDebitorioDTO.setAccInteressiVersati(accInteressiVersati);
						}else {
							statoDebitorioDTO.setAccInteressiVersati(BigDecimal.ZERO);
						}
						// Issue 81: il campo accImportoRimborsato deve tenere conto sia di rimborsi che
						// di eventuali compensazioni utilizzate, quindi valorizzo il campo con
						// sommRimborsi (come per il campo sommaRimborsi)
						// statoDebitorioDTO.setAccImportoRimborsato(impRimborso);
						statoDebitorioDTO.setAccImportoRimborsato(sommRimborsi);
						
					}

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
			if(sort.substring(1).equals("codiceUtenza") && sort.charAt(0) == '-')
			    nomeCampo = sort.substring(0, 1).concat(" rtr.cod_riscossione ");
			else
				nomeCampo = " rtr.cod_riscossione ";
		}
		if(sort.contains("dataScadenza")) {
			if(sort.substring(1).equals("dataScadenza") && sort.charAt(0) == '-')
			    nomeCampo = sort.substring(0, 1).concat(" rrrs.data_scadenza_pagamento ");
			else
				nomeCampo = " rrrs.data_scadenza_pagamento ";
		}
		return  getQuerySortingSegment(nomeCampo);

	}
	@Override
	public StatoDebitorioExtendedDTO loadStatoDebitorioByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 StatoDebitorioExtendedDTO statoDebitorioDTO = new StatoDebitorioExtendedDTO();

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

					if(statoDebitorioDTO.getNap() != null) {
						map.put("nap", statoDebitorioDTO.getNap());
					}
		            SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
		            
					LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] GET list Annualita SD");
					List<AnnualitaSdDTO> listAnnualitaSdDTO = annualitaSdDAO.loadAnnualitaSd(statoDebitorioDTO.getIdStatoDebitorio(), null, null);
					statoDebitorioDTO.setAnnualitaSd(listAnnualitaSdDTO);
			        BigDecimal impCompensazioneCanone = BigDecimal.ZERO;
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
//						BigDecimal importoDovuto = canoneDovuto.add(impRecuperoCanone);
//
//						LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] calcolo importo Dovuto");
//						statoDebitorioDTO.setImportoDovuto(importoDovuto);
						
						

						
						if(statoDebitorioDTO.getImpCompensazioneCanone() != null) {
							impCompensazioneCanone= statoDebitorioDTO.getImpCompensazioneCanone() ;
						}

						
						//statoDebitorioDTO.setImpMancanteImpEccedente(impCompensazioneCanone.subtract(canoneDovuto));
						
						
						
					}
					
					LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] GET rimborsi");
					List<RimborsoExtendedDTO>  rimborsi = rimborsoDAO.loadRimborsiByIdStatoDebitorio(statoDebitorioDTO.getIdStatoDebitorio());
					statoDebitorioDTO.setRimborsi(rimborsi);
					
					LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioByIdStatoDebitorio] GET rata");
					
					List<RataSdDTO> ListRataSdDTO = rataSdDAO.loadListRataSdByStatoDebitorio(statoDebitorioDTO.getIdStatoDebitorio());
	
					BigDecimal impSpeseNotifica =  BigDecimal.ZERO;
					BigDecimal interessiMaturati =  BigDecimal.ZERO;
					BigDecimal impRimborso =  BigDecimal.ZERO;
					 params = getParameterValue(map);
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
						impSpeseNotifica = statoDebitorioDTO.getImpSpeseNotifica();
					}
					if(ListRataSdDTO != null) {
						//[DP] inizio spostamento
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
						interessiMaturati = ListRataSdDTO.stream().filter(r -> r.getInteressiMaturati() != null && r.getIdRataSdPadre() == null).map(RataSdDTO::getInteressiMaturati).reduce(BigDecimal.ZERO, BigDecimal::add) ;
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
									
					//BigDecimal impMancImpEcc = importoVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati).subtract(impCompensazioneCanone)).subtract(sommRimborsi);
					//[VF] issue 75 non sottrarre impCompensazioneCanone
					BigDecimal impMancImpEcc = importoVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati)).subtract(sommRimborsi);
					statoDebitorioDTO.setImpMancanteImpEccedente(impMancImpEcc);
				
					BigDecimal intMaturatiSpeseNotifica =  BigDecimal.ZERO;
					intMaturatiSpeseNotifica = impSpeseNotifica.add(interessiMaturati);
					statoDebitorioDTO.setIntMaturatiSpeseNotifica(intMaturatiSpeseNotifica);
					
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
					
//					BigDecimal importoEccedente =   BigDecimal.ZERO;
//					importoEccedente =	importoVersato.add(impRimborso).subtract(canoneDovuto).subtract(IntMaturatiSpeseNotifica);
					
					if(impMancImpEcc.compareTo(BigDecimal.ZERO) > 0) {
						statoDebitorioDTO.setImportoEccedente(impMancImpEcc);
					}else {
						statoDebitorioDTO.setImportoEccedente(BigDecimal.ZERO);
					}
					
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
					

					String attivita = mapAttivitaSD(statoDebitorioDTO);
					statoDebitorioDTO.setAttivita(attivita);
					
					// parte di accertamento
					BigDecimal accImportoDovuto = BigDecimal.ZERO;
					String accDataScadenzaPag = null;
					BigDecimal accImportoVersato = BigDecimal.ZERO;
					BigDecimal accImportoDiCanoneMancante = BigDecimal.ZERO;
					BigDecimal accInteressiMancanti  = BigDecimal.ZERO;
					BigDecimal accInteressiVersati  = BigDecimal.ZERO; 
					BigDecimal statoDebImpSpesNot = BigDecimal.ZERO;
					if(ListRataSdDTO != null) {
						RataSdDTO rata = ListRataSdDTO.stream().filter(r -> r.getIdRataSdPadre() == null).collect(Collectors.toList()).get(0) ;
						BigDecimal canoneDovutoRata = rata.getCanoneDovuto() != null ? rata.getCanoneDovuto() : BigDecimal.ZERO;
						accImportoDovuto = accImportoDovuto.add(canoneDovutoRata);
						statoDebitorioDTO.setAccImportoDovuto(accImportoDovuto);
						
						accDataScadenzaPag = rata.getDataScadenzaPagamento();
						statoDebitorioDTO.setAccDataScadenzaPag(accDataScadenzaPag);
					
						if(statoDebitorioDTO.getImpSpeseNotifica() != null)
							statoDebImpSpesNot = statoDebitorioDTO.getImpSpeseNotifica();
						
						
						accImportoVersato = accImportoVersato.add(statoDebitorioDTO.getImportoVersato());
						statoDebitorioDTO.setAccImportoVersato(accImportoVersato);
						
					//	accImportoDiCanoneMancante = canoneDovuto.subtract(impRimborso).subtract(statoDebitorioDTO.getImportoVersato());
						//[DP] modificata analisi aggiunto imp rimborso invece che sottratto solo se maggiore di 0 
						if(impRimborso.compareTo(BigDecimal.ZERO) > 0){
						  accImportoDiCanoneMancante = canoneDovuto.add(impRimborso).subtract(statoDebitorioDTO.getImportoVersato());
						}else {
							accImportoDiCanoneMancante = canoneDovuto.subtract(statoDebitorioDTO.getImportoVersato());
						}
						
						//[DP] issue 26
						//[VF] issue 90: questa if secondo l'analisi va commentata
						//Vedi doc WP2-2.2-SRV-V102-Servizi_RISCABESRV - Pag. 192
//						if(impCompensazioneCanone.compareTo(BigDecimal.ZERO) > 0) {
//							accImportoDiCanoneMancante = accImportoDiCanoneMancante.subtract(impCompensazioneCanone);
//						}
						
						
						if(accImportoDiCanoneMancante.compareTo(BigDecimal.ZERO) >= 0) {
							statoDebitorioDTO.setAccImportoDiCanoneMancante(accImportoDiCanoneMancante);
							// [VF] Fix per ISSUE 90 accInteressiMancanti deve essere valorizzata
							// correttamente per i calcoli successivi
							accInteressiMancanti = statoDebitorioDTO.getIntMaturatiSpeseNotifica();
							statoDebitorioDTO.setAccInteressiMancanti(accInteressiMancanti);
						}else {
							statoDebitorioDTO.setAccImportoDiCanoneMancante(BigDecimal.ZERO);
							//[DP] issue33 Da NUOVA SPECIFICA non si sottrae impRimborso
							//(- impCompensazioneCanone + accImportoRImborsato)
//							accInteressiMancanti = canoneDovutoRata.add(statoDebitorioDTO.getIntMaturatiSpeseNotifica())
//									.subtract(statoDebitorioDTO.getImportoVersato()).subtract(impRimborso);
							accInteressiMancanti = canoneDovutoRata.add(statoDebitorioDTO.getIntMaturatiSpeseNotifica())
									.subtract(statoDebitorioDTO.getImportoVersato()).add(impRimborso).subtract(impCompensazioneCanone);
							if(accInteressiMancanti.compareTo(BigDecimal.ZERO) > 0) {
								statoDebitorioDTO.setAccInteressiMancanti(accInteressiMancanti);
							}else {
								statoDebitorioDTO.setAccInteressiMancanti(BigDecimal.ZERO);
								// [VF] Issue 90 - occorre azzerare questo valore altrimenti quando viene negativo 
								// influisce sul valore calcolato per accInteressiVersati
								accInteressiMancanti = BigDecimal.ZERO;
							}
							
						}
						//[DP] issue33 da nuova specifica accInteressiVersati = interessi e spese di notifica - accInteressiMancanti
						//accInteressiVersati = statoDebitorioDTO.getImportoVersato().add(impRimborso).subtract(canoneDovuto);
						accInteressiVersati =  intMaturatiSpeseNotifica.subtract(accInteressiMancanti);
						
						if(accInteressiVersati.compareTo(BigDecimal.ZERO) > 0) {
							statoDebitorioDTO.setAccInteressiVersati(accInteressiVersati);
						}else {
							statoDebitorioDTO.setAccInteressiVersati(BigDecimal.ZERO);
						}
						// Issue 81: il campo accImportoRimborsato deve tenere conto sia di rimborsi che
						// di eventuali compensazioni utilizzate, quindi valorizzo il campo con
						// sommRimborsi (come per il campo sommaRimborsi)
						// statoDebitorioDTO.setAccImportoRimborsato(impRimborso);
						statoDebitorioDTO.setAccImportoRimborsato(sommRimborsi);
						
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
	
	@Override
	public StatoDebitorioDTO loadStatoDebitorioLightById(Long idStatoDebitorio) throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		StatoDebitorioDTO statoDebitorioDTO = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);
			statoDebitorioDTO = template.queryForObject(QUERY_SELECT_STATI_DEBITORI_BY_ID_STATO_DEBITORIO, params,
					getExtendedRowMapper());
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadStatoDebitorioLightById] ERROR : ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::loadStatoDebitorioLightById] END");
		}
		return statoDebitorioDTO;
	}
	
	@Override
	public List<UtRimbDTO> loadStatoDebitorioByIdRimborso(Long idRimborso) throws DAOException{
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRimborso", idRimborso);
			MapSqlParameterSource params = getParameterValue(map);

			return template.query(getQuery(QUERY_SELECT_UT_RIMBO_BY_ID_ROMBORSO, null, null), params,
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
	
	@Override
	public List<StatoDebitorioExtendedDTO> loadAllStatoDebitorioByNap(String nap, List<Integer> sdDaEscludere, Integer offset,Integer limit, String sort) throws DAOException{
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<StatoDebitorioExtendedDTO> res = new ArrayList<StatoDebitorioExtendedDTO>();

        BigDecimal canoneDovuto = BigDecimal.ZERO;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap.trim().toUpperCase());
			String query = QUERY_SELECT_STATI_DEBITORI_BY_NAP ;
			if(Utils.isNotEmpty(sdDaEscludere)) {
//				List<Long> idSD = Utils.convertStringToList(sdDaEscludere,"_", Long.class);
				map.put("sdDaEscludere", sdDaEscludere);
				query+=ESCLUDE_SD;
			}
			MapSqlParameterSource params = getParameterValue(map);
			String dynamicOrderByCondition="";
			if (StringUtils.isNotBlank(sort)) {
			   dynamicOrderByCondition = mapSortConCampiDB(sort);
			   if(dynamicOrderByCondition != null) {
				   dynamicOrderByCondition = dynamicOrderByCondition.concat(",").concat(ORDER_BY_SD.replace("ORDER BY", ""));
			   }

			}else {
				dynamicOrderByCondition = ORDER_BY_SD;
			}
			res =  template.query(getQuery( query+ dynamicOrderByCondition,  offset!= null ? offset.toString(): null ,  limit != null ? limit.toString(): null), params, getExtendedRowMapper());
		
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
			
			if(!res.isEmpty()) {
				
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

			        BigDecimal impCompensazioneCanone = BigDecimal.ZERO;
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
//			           statoDebitorioDTO.setCanoneDovuto(canoneDovuto);
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
						if(!listdettaglioPag.isEmpty())
					        impVersato = listdettaglioPag.stream().filter(d -> d.getImportoVersato() != null).map(DettaglioPagExtendedDTO::getImportoVersato).reduce(BigDecimal.ZERO, BigDecimal::add).add(impVersato) ;
					}
					if(statoDebitorioDTO.getImpSpeseNotifica() != null) {
						impSpeseNotifica = statoDebitorioDTO.getImpSpeseNotifica();
					}
					if(ListRataSdDTO != null) {						
						interessiMaturati = ListRataSdDTO.stream().filter(r -> r.getInteressiMaturati() != null && r.getIdRataSdPadre() == null).map(RataSdDTO::getInteressiMaturati).reduce(BigDecimal.ZERO, BigDecimal::add) ;
					}
					if(rimborsi != null) {
						impRimborso = rimborsi.stream().map(RimborsoDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRimborso) ;
					}
					LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] calcolo importo Versato");
					map.put("idStatoDebitorio", statoDebitorioDTO.getIdStatoDebitorio());
				   params = getParameterValue(map);
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
				
					//[DP] inizio spostamento
					if(ListRataSdDTO != null) {		
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
					}
					//[DP]Fine spostamento
					
					
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
					
					//BigDecimal impMancImpEcc = importoVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati).subtract(impCompensazioneCanone)).subtract(sommRimborsi);
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
					
//					BigDecimal importoEccedente =   BigDecimal.ZERO;
//					importoEccedente =	importoVersato.add(impRimborso).subtract(canoneDovuto).subtract(IntMaturatiSpeseNotifica);
					
					if(impMancImpEcc.compareTo(BigDecimal.ZERO) > 0) {
						statoDebitorioDTO.setImportoEccedente(impMancImpEcc);
					}else {
						statoDebitorioDTO.setImportoEccedente(BigDecimal.ZERO);
					}
					
					BigDecimal intMaturatiSpeseNotifica =  BigDecimal.ZERO;
					intMaturatiSpeseNotifica = impSpeseNotifica.add(interessiMaturati);
					statoDebitorioDTO.setIntMaturatiSpeseNotifica(intMaturatiSpeseNotifica);

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

					String attivita = mapAttivitaSD(statoDebitorioDTO);
					statoDebitorioDTO.setAttivita(attivita);
					// parte di accertamento
					BigDecimal accImportoDovuto = BigDecimal.ZERO;
					String accDataScadenzaPag = null;
					BigDecimal accInteressiDovuti = BigDecimal.ZERO;
					BigDecimal accImportoVersato = BigDecimal.ZERO;
					BigDecimal accImportoDiCanoneMancante = BigDecimal.ZERO;
					BigDecimal accInteressiMancanti  = BigDecimal.ZERO;
					BigDecimal accInteressiVersati  = BigDecimal.ZERO; 
					BigDecimal statoDebImpSpesNot = BigDecimal.ZERO;
					if(ListRataSdDTO != null) {
						RataSdDTO rata = ListRataSdDTO.stream().filter(r -> r.getIdRataSdPadre() == null).collect(Collectors.toList()).get(0) ;
						BigDecimal canoneDovutoRata = rata.getCanoneDovuto() != null ? rata.getCanoneDovuto() : BigDecimal.ZERO;
						accImportoDovuto = accImportoDovuto.add(canoneDovutoRata);
						statoDebitorioDTO.setAccImportoDovuto(accImportoDovuto);
						
						accDataScadenzaPag = rata.getDataScadenzaPagamento();
						statoDebitorioDTO.setAccDataScadenzaPag(accDataScadenzaPag);
					
						if(statoDebitorioDTO.getImpSpeseNotifica() != null)
							statoDebImpSpesNot = statoDebitorioDTO.getImpSpeseNotifica();
						
						accInteressiDovuti= accInteressiDovuti.add(interessiMaturati).add(statoDebImpSpesNot);
						
						accImportoVersato = accImportoVersato.add(statoDebitorioDTO.getImportoVersato());
						statoDebitorioDTO.setAccImportoVersato(accImportoVersato);
						
						//accImportoDiCanoneMancante = canoneDovuto.subtract(impRimborso).subtract(statoDebitorioDTO.getImportoVersato());
						//[DP] modificata analisi aggiunto imp rimborso invece che sottratto solo se maggiore di 0 
						if(impRimborso.compareTo(BigDecimal.ZERO) > 0){
						  accImportoDiCanoneMancante = canoneDovuto.add(impRimborso).subtract(statoDebitorioDTO.getImportoVersato());
						}else {
							accImportoDiCanoneMancante = canoneDovuto.subtract(statoDebitorioDTO.getImportoVersato());
						}
						
						//[DP] issue 26
						//[VF] issue 90: questa if secondo l'analisi va commentata
						//Vedi doc WP2-2.2-SRV-V102-Servizi_RISCABESRV - Pag. 192
//						if(impCompensazioneCanone.compareTo(BigDecimal.ZERO) > 0) {
//							accImportoDiCanoneMancante = accImportoDiCanoneMancante.subtract(impCompensazioneCanone);
//						}
						
						if(accImportoDiCanoneMancante.compareTo(BigDecimal.ZERO) >= 0) {
							statoDebitorioDTO.setAccImportoDiCanoneMancante(accImportoDiCanoneMancante);
							// [VF] Fix per ISSUE 90 accInteressiMancanti deve essere valorizzata
							// correttamente per i calcoli successivi
							accInteressiMancanti = statoDebitorioDTO.getIntMaturatiSpeseNotifica();
							statoDebitorioDTO.setAccInteressiMancanti(accInteressiMancanti);
						}else {
							statoDebitorioDTO.setAccImportoDiCanoneMancante(BigDecimal.ZERO);
							//[DP] issue33 Da NUOVA SPECIFICA non si sottrae impRimborso
							//(- impCompensazioneCanone + accImportoRImborsato)							
//							accInteressiMancanti = canoneDovutoRata.add(statoDebitorioDTO.getIntMaturatiSpeseNotifica())
//									.subtract(statoDebitorioDTO.getImportoVersato()).subtract(impRimborso);
							accInteressiMancanti = canoneDovutoRata.add(statoDebitorioDTO.getIntMaturatiSpeseNotifica())
									.subtract(statoDebitorioDTO.getImportoVersato()).add(impRimborso).subtract(impCompensazioneCanone);

							if(accInteressiMancanti.compareTo(BigDecimal.ZERO) > 0) {
								statoDebitorioDTO.setAccInteressiMancanti(accInteressiMancanti);
							} else {	
								statoDebitorioDTO.setAccInteressiMancanti(BigDecimal.ZERO);
								// [VF] Issue 90 - occorre azzerare questo valore altrimenti quando viene negativo 
								// influisce sul valore calcolato per accInteressiVersati
								accInteressiMancanti = BigDecimal.ZERO;
							}
							
						}
						
						//[DP] issue33 da nuova specifica accInteressiVersati = interessi e spese di notifica - accInteressiMancanti
						//accInteressiVersati = statoDebitorioDTO.getImportoVersato().add(impRimborso).subtract(canoneDovuto);
						accInteressiVersati =  intMaturatiSpeseNotifica.subtract(accInteressiMancanti);
						
						if(accInteressiVersati.compareTo(BigDecimal.ZERO) > 0) {
							statoDebitorioDTO.setAccInteressiVersati(accInteressiVersati);
						}else {
							statoDebitorioDTO.setAccInteressiVersati(BigDecimal.ZERO);
						}
						// Issue 81: il campo accImportoRimborsato deve tenere conto sia di rimborsi che
						// di eventuali compensazioni utilizzate, quindi valorizzo il campo con
						// sommRimborsi (come per il campo sommaRimborsi)
						// statoDebitorioDTO.setAccImportoRimborsato(impRimborso);
						statoDebitorioDTO.setAccImportoRimborsato(sommRimborsi);
						
					
					  }

				}

				
			}

		} catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] No record found in database for idRimborso "+ nap, e);
		    return Collections.emptyList();
		} catch (DataAccessException e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] Errore nell'accesso ai dati", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] Errore SQL ", e); 
			throw new DAOException(Constants.ERRORE_GENERICO);
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] ERROR : " +e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByNap] END");
		}
		return res;
		
	}


	private void calcolaInteressiSD(StatoDebitorioExtendedDTO statoDebitorioDTO, String dataVersamento,Long idAmbito) throws Exception {
		BigDecimal Interessi = BigDecimal.ZERO;
		try {
			Interessi = calcoloInteresseDAO.calcoloInteressi(idAmbito, statoDebitorioDTO.getAccImportoDiCanoneMancante(), statoDebitorioDTO.getAccDataScadenzaPag(),
					dataVersamento);
					
		} catch (BusinessException e) {
		    LOGGER.debug(getClassFunctionErrorInfo(className, Thread.currentThread().getStackTrace()[1].getMethodName(), e));
		    throw e;

		} catch (Exception e) {
		    LOGGER.error(getClassFunctionErrorInfo(className, Thread.currentThread().getStackTrace()[1].getMethodName(), e));
		    throw e;
		} 
		statoDebitorioDTO.setCalcoloInteressi(Interessi);
		
	}

	@Override
	public VerifyStatoDebitorioDTO verifyStatoDebitorio(String modalita, String fruitore,  String flgUpDataScadenza, StatoDebitorioExtendedDTO statoDebitorio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericExceptionList , ParseException{
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		VerifyStatoDebitorioDTO isStatoDebitorioValid = new VerifyStatoDebitorioDTO();
		List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
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
						if(fruitore == null || !fruitore.equals("BATCH_PORTING") ) {
							for(int i = 0; i<statoDebitorio.getAnnualitaSd().size(); i++) {
								if(!(statoDebitorio.getAnnualitaSd().get(i).getAnnualitaUsoSd().size()>0)) {
									ErrorDTO error = new ErrorDTO("400", "E049", "Attenzione: ad ogni annualita' deve essere associato almeno un uso", null, null);
						            errorsList.add(error);
						            break;
								}
							}
						}
//						controllo CanoneUnitario CanoneUso
						statoDebitorio.getAnnualitaSd().forEach(annualitaSd -> {
						    List<ErrorDTO> errorsForAnnualitaSd = new ArrayList<>();

						    annualitaSd.getAnnualitaUsoSd().forEach(annualitaUsoSd -> {
						        if (annualitaUsoSd.getCanoneUnitario() == null || annualitaUsoSd.getCanoneUso() == null) {
						            errorsForAnnualitaSd.add(new ErrorDTO("400","E001", "Attenzione: compila tutti i campi obbligatori.", null, null));
						        }
						    });

						    errorsList.addAll(errorsForAnnualitaSd);
						});
						
						Integer annoAnnualita[] = new Integer[statoDebitorio.getAnnualitaSd().size()];
						for(int k=0; k < statoDebitorio.getAnnualitaSd().size(); k++) {
							annoAnnualita[k] = statoDebitorio.getAnnualitaSd().get(k).getAnno(); 
						}
						Long distinctCount = Stream.of(annoAnnualita).distinct().count();

						if(annoAnnualita.length != distinctCount) {
							ErrorDTO error = new ErrorDTO("400", "E069", "Attenzione: in questo stato debitorio esiste gia' la stessa annualita' ", null, null);
				            errorsList.add(error);
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

					

		        	Map<String, Object> map = new HashMap<>();
		        	map.put("idRiscossione", statoDebitorio.getIdRiscossione());
		        	MapSqlParameterSource params = getParameterValue(map);
					if(template.queryForObject(QUERY_SELECT_STATI_DEB_WITH_FLG_INVIO_SPECIALE, params, Integer.class) > 0) {
						if(statoDebitorio.getFlgInvioSpeciale() == 1) {
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
						
						Integer[] annoAnnualita = new Integer[statoDebitorio.getAnnualitaSd().size()];
						for(int k=0; k < statoDebitorio.getAnnualitaSd().size(); k++) {
							annoAnnualita[k] = statoDebitorio.getAnnualitaSd().get(k).getAnno(); 
						}
						Long distinctCount = Stream.of(annoAnnualita).distinct().count();

						if(annoAnnualita.length != distinctCount) {
							ErrorDTO error = new ErrorDTO("400", "E069", "Attenzione: in questo stato debitorio esiste gia' la stessa annualita' ", null, null);
				            errorsList.add(error);
						}
					
					
//						controllo CanoneUnitario CanoneUso
					statoDebitorio.getAnnualitaSd().forEach(annualitaSd -> {
					    List<ErrorDTO> errorsForAnnualitaSd = new ArrayList<>();

					    annualitaSd.getAnnualitaUsoSd().forEach(annualitaUsoSd -> {
					        if (annualitaUsoSd.getCanoneUnitario() == null || annualitaUsoSd.getCanoneUso() == null) {
					            errorsForAnnualitaSd.add(new ErrorDTO("400","E001", "Attenzione: compila tutti i campi obbligatori.", null, null));
					        }
					    });

					    errorsList.addAll(errorsForAnnualitaSd);
					});
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

					

		        	Map<String, Object> map = new HashMap<>();
		        	map.put("idRiscossione", statoDebitorio.getIdRiscossione());
		        	map.put("idStatoDebitorio", statoDebitorio.getIdStatoDebitorio());
		        	MapSqlParameterSource params = getParameterValue(map);
//					if(template.queryForObject(QUERY_SELECT_STATI_DEB_WITH_FLG_INVIO_SPECIALE_BY_ID_STATO_DEB, params, Integer.class) > 0) {
//						if(statoDebitorio.getFlgInvioSpeciale() == 1) {
//							ErrorDTO error = new ErrorDTO("400", "E078", "Attenzione: esiste gia' uno stato debitorio con flag invio speciale", null, null);
//				            errorsList.add(error);
//						}
//					}
					
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


					if(statoDebitorio.getFlgAnnullato() == 1) {
			        	map.put("idStatoDebitorio", statoDebitorio.getIdStatoDebitorio());
			        	params = getParameterValue(map);
						if(template.queryForObject(QUERY_CHECK_ST_ANNULATO_ACCERTAMENTO, params, Integer.class) > 0) {
							ErrorDTO error = new ErrorDTO("400", "A025", "Attenzione: non e' possibile annullare lo stato debitorio in quanto sono presenti solleciti/ruoli attivi", null, null);
				            errorsList.add(error);
						}

					}

					if(statoDebitorio.getRate() != null && statoDebitorio.getNap() != null && flgUpDataScadenza == null) {
						
						for(int n=0; n<statoDebitorio.getRate().size(); n++) {
							String dataScadPagStr = statoDebitorio.getRate().get(n).getDataScadenzaPagamento();
							String dataScadPag = "";
							if(dataScadPagStr != null)
								dataScadPag = dataScadPagStr.substring(0, 10);
							
							map.put("idRataSd", statoDebitorio.getRate().get(n).getIdRataSd());
							map.put("nap", statoDebitorio.getNap());
							params = getParameterValue(map);
							String data = template.queryForObject(QUERY_SELECT_DATA_SCAD_PAG_BY_ID_RATA, params, String.class);

							if(!dataScadPag.equals(data)) {
								Integer countNap = template.queryForObject(QUERY_SELECT_STATO_DEB_BY_NAP, params, Integer.class);
								if(countNap > 1) {
									MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.A027);
									ErrorDTO error = new ErrorDTO("400", messaggiDTO.getCodMessaggio(),Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()),
											null, null);
									errorsList.add(error);	
								}
							}
						}
					}else {
						// [DP] modifica caso con nap nullo
						
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
			
		}  catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::verifyStatoDebitorio] ERROR : " +e);
			throw new GenericExceptionList(e);
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

	@Override
	public StatoDebitorioExtendedDTO loadStatoDebitorioAnnoPrecedente(Long idRiscossione) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
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
	
	@Override
	public Integer updateNotaRinnovoStatoDebitorioWorking(Long idStatoDebitorio, String notaRinnovo)
			throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			map.put("notaRinnovo", notaRinnovo);

			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_UPDATE_NOTA_RINNOVO_WORKING;
			return template.update(getQuery(query, null, null), params);

		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::updateNotaRinnovoStatoDebitorioWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::updateNotaRinnovoStatoDebitorioWorking] END");
		}
	}
	
	@Override
	public Integer updateStatoDebitorioFromStatoDebitorioUpd(String attore, Long idElabora)
			throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			map.put("idElabora1", idElabora);
			map.put("idElabora2", idElabora);
			MapSqlParameterSource params = getParameterValue(map);
			
			String query = QUERY_UPDATE_SD_FROM_SD_UPD;
			return template.update(getQuery(query, null, null), params);

		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::updateStatoDebitorioFromStatoDebitorioUpd] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorioFromStatoDebitorioUpd] END");
		}
	}
	
	@Override
	public List<StatoDebitorioDTO> loadStatoDebitorioWorkingByNap(String nap) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
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
	
	@Override
	public Integer updateStatoDebitorioFromStatoDebitorioWorking(StatoDebitorioDTO sdWorkingDto, String attore)
			throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
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
			LOGGER.error("[StatoDebitorioDAOImpl::updateStatoDebitorioFromStatoDebitorioWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoDebitorioFromStatoDebitorioWorking] END");
		}
	}
	
	@Override
	public Integer deleteStatoDebitorioWorkingByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_SD_WORKING_BY_SD, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[StatoDebitorioDAOImpl::deleteStatoDebitorioWorkingByIdStatoDebitorio] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::deleteStatoDebitorioWorkingByIdStatoDebitorio] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteStatoDebitorioUpdWorkingByIdElabora(Long idElabora) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_SD_UPD_WORKING_BY_ELAB, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[StatoDebitorioDAOImpl::deleteStatoDebitorioUpdWorkingByIdElabora] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::deleteStatoDebitorioUpdWorkingByIdElabora] END");
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
	
	private RowMapper<SommaCanoneTipoUsoSdDTO> getSommaCanoneTipoUsoSdRowMapper() throws SQLException {

		return new SommaCanoneTipoUsoSdRowMapper();
	}
	/**
	 * The type UtRimb RowMapper
	 */
	public static class SommaCanoneTipoUsoSdRowMapper implements RowMapper<SommaCanoneTipoUsoSdDTO> {
		
		/**
		 * Instantiates a new SommaCanoneTipoUsoSd RowMapper
		 *
		 * @throws SQLException the sql exception
		 */
		public SommaCanoneTipoUsoSdRowMapper() throws SQLException {
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
		public SommaCanoneTipoUsoSdDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SommaCanoneTipoUsoSdDTO bean = new SommaCanoneTipoUsoSdDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SommaCanoneTipoUsoSdDTO bean) throws SQLException {
	
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			bean.setIdTipoUso(rs.getLong("id_tipo_uso"));
			bean.setIdAccertaBilancio(rs.getLong("id_accerta_bilancio"));
			bean.setOrdinaTipoUso(rs.getLong("ordina_tipo_uso"));
			bean.setTotaleCanoneUso(rs.getBigDecimal("totale_canone_uso"));
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
	private String mapAttivitaSD(StatoDebitorioExtendedDTO statoDebitorioDTO) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		String attivita ="";
		if(statoDebitorioDTO.getAttivitaStatoDeb() != null) {
			String desAttivitaStatoDeb = statoDebitorioDTO.getAttivitaStatoDeb().getDesAttivitaStatoDeb();
			   attivita = attivita.concat(desAttivitaStatoDeb);
		}else {
			String	desTipoAccertamento ="";
			if(statoDebitorioDTO.getAccertamenti() != null && !statoDebitorioDTO.getAccertamenti().isEmpty()) {
				List<AccertamentoExtendedDTO>  listAccertamento = statoDebitorioDTO.getAccertamenti().stream()
						.filter(a -> a.getFlgAnnullato() == 0 && a.getFlgRestituito() == 0).collect(Collectors.toList());
				if(!listAccertamento.isEmpty()) {
					AccertamentoExtendedDTO maxAccertamentoExtendedDTO = listAccertamento.stream()
						    .max(Comparator.comparing(AccertamentoExtendedDTO::getDataProtocollo))
						    .orElse(null);
					if(maxAccertamentoExtendedDTO != null) {
						desTipoAccertamento = maxAccertamentoExtendedDTO.getTipoAccertamento().getDesTipoAccertamento();
						attivita= attivita.length() > 1 ? attivita.concat(" -").concat(desTipoAccertamento) :attivita.concat(desTipoAccertamento);
					}
				}
				
						
			}
		}
		if(statoDebitorioDTO.getImpRecuperoCanone() != null && statoDebitorioDTO.getImpRecuperoCanone().compareTo(BigDecimal.ZERO) > 0) {
			attivita= attivita.length() > 1 ? attivita.concat(" -").concat("Annual. Prec.") : attivita.concat("Annual. Prec.");
		}
		if(statoDebitorioDTO.getFlgAddebitoAnnoSuccessivo() == 1) {
			attivita= attivita.length() > 1 ? attivita.concat(" -").concat("Annual. Succes."): attivita.concat("Annual. Succes.");
		}
		if(statoDebitorioDTO.getImpCompensazioneCanone() != null && statoDebitorioDTO.getImpCompensazioneCanone().compareTo(BigDecimal.ZERO) > 0) {
			attivita= attivita.length() > 1 ? attivita.concat(" -").concat("Con compens."): attivita.concat("Con compens.");
		}
		if(statoDebitorioDTO.getFlgDilazione() > 0) {
			attivita= attivita.length() > 1 ? attivita.concat(" -").concat("Dilaz."): attivita.concat("Dilaz.");
		}

		if(!statoDebitorioDTO.getRimborsi().isEmpty()) {
			   List<String> collect = statoDebitorioDTO.getRimborsi().stream()
					   .map(r -> r.getTipoRimborso().getDesTipoRimborso()).distinct()
					   .collect(Collectors.toList());
			
			   for (String desRimborsi : collect) {
						attivita= attivita.length() > 1 ? attivita.concat(" -").concat(desRimborsi): attivita.concat(desRimborsi);
				}
		}
		LOGGER.debug("[StatoDebitorioDAOImpl::mapAttivitaSD] END");
		return attivita;
		
	}
	@Override
	public Integer verifyStatoDebitorioInvioSpeciale(Long idRiscossione, Long idStatoDebitorio) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRiscossione", idRiscossione);
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);
			return 	template.queryForObject(QUERY_SELECT_STATI_DEB_WITH_FLG_INVIO_SPECIALE + ID_STATO_DEBITORIO , params, Integer.class);
		} catch (DataAccessException e) {
			LOGGER.error("[StatoDebitorioDAOImpl::verifyStatoDebitorioInvioSpeciale] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		}
	}
	
	@Override
	public Integer updateAttivitaStatoDebitorio(Long idStatoDebitorio) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPD_ATTIVITA_STATO_DEB, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::updateAttivitaStatoDebitorio] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::updateAttivitaStatoDebitorio] END");
		}

		return res;
	}

	@Override
	public Integer countAllStatoDebitorioOrByIdRiscossione(Long idRiscossione) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        MapSqlParameterSource paramST  = new MapSqlParameterSource();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRiscossione", idRiscossione);
			paramST = getParameterValue(map);
			return 	template.queryForObject(idRiscossione != null? QUERY_COUNT_STATI_DEBITORI_BY_ID_RISCOSSIONE : QUERY_COUNT_ALL_STATI_DEBITORI , paramST, Integer.class);
						
		}catch (DataAccessException e) {
				LOGGER.error("[StatoDebitorioDAOImpl::countAllStatoDebitorioOrByIdRiscossione] ERROR : ",e);
				throw new DAOException(Constants.ERRORE_GENERICO);
			}
		}
	@Override
	public Integer countAllStatoDebitorioByNap(String nap, List<Integer> sdDaEscludere) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap.trim().toUpperCase());
			String query = QUERY_COUNT_STATI_DEBITORI_BY_NAP ;
			if(Utils.isNotEmpty(sdDaEscludere)) {
				map.put("sdDaEscludere", sdDaEscludere);
				query+=ESCLUDE_SD;
			}
			MapSqlParameterSource params = getParameterValue(map);
			return 	template.queryForObject(query, params, Integer.class);
						
		}catch (DataAccessException e) {
			 LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
				throw new DAOException(Constants.ERRORE_GENERICO);
			}
		}
	
	@Override
	public Integer copyStatoDebitorioFromWorkingByNap(String nap, String attore) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("nap", nap);
			map.put("gestAttoreIns", attore);
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_INSERT_SD_FROM_WORKING, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[StatoDebitorioDAOImpl::copyStatoDebitorioFromWorkingByNap] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::copyStatoDebitorioFromWorkingByNap] END");
		}

		return res;
	}
	@Override
	public List<StatoDebitorioExtendedDTO> loadAllStatoDebitorioByCodUtenza(String codUtenza, List<Integer> sdDaEscludere, Integer offset,
			Integer limit, String sort) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<StatoDebitorioExtendedDTO> res = new ArrayList<StatoDebitorioExtendedDTO>();

        BigDecimal canoneDovuto = BigDecimal.ZERO;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codUtenza", codUtenza.trim().toUpperCase());
			String query = QUERY_SELECT_STATI_DEBITORI_BY_COD_UTENZA ;
			if(Utils.isNotEmpty(sdDaEscludere)) {
				map.put("sdDaEscludere", sdDaEscludere);
				query+=ESCLUDE_SD;
			}
			MapSqlParameterSource params = getParameterValue(map);
			String dynamicOrderByCondition=""; 
			if (StringUtils.isNotBlank(sort)) {
				   dynamicOrderByCondition = mapSortConCampiDB(sort);
				   if(dynamicOrderByCondition != null) {
					   dynamicOrderByCondition = dynamicOrderByCondition.concat(",").concat(ORDER_BY_SD.replace("ORDER BY", ""));
				   }
			}else {
				dynamicOrderByCondition = ORDER_BY_SD;
			}
			res =  template.query(getQuery(query + dynamicOrderByCondition,  offset!= null ? offset.toString(): null ,  limit != null ? limit.toString(): null), params, getExtendedRowMapper());

			
			if(!res.isEmpty()) {
				
				for (StatoDebitorioExtendedDTO statoDebitorioDTO : res) {
					if(statoDebitorioDTO.getIdAttivitaStatoDeb() != null) {
						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] GET Attivita Stato Debitorio");
						AttivitaStatoDebitorioDTO attivitaStatoDebitorioDTO = attivitaStatoDebitorioDAO.getAttivitaStatoDebitorioById(statoDebitorioDTO.getIdAttivitaStatoDeb());
						statoDebitorioDTO.setAttivitaStatoDeb(attivitaStatoDebitorioDTO);
					}
					if(statoDebitorioDTO.getIdStatoContribuzione() != null) {
						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] GET Stato Contribuzione");
						StatoContribuzioneDTO statoContribuzioneDTO = statoContribuzioneDAO.loadStatoContribuzioneById(statoDebitorioDTO.getIdStatoContribuzione());
						statoDebitorioDTO.setStatoContribuzione(statoContribuzioneDTO);
					}

			        BigDecimal impCompensazioneCanone = BigDecimal.ZERO;
					LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] GET list Annualita SD");
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
							LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] GET list Annualita Uso SD");

							List<AnnualitaUsoSdDTO> listAnnualitaUsoSd = annualitaUsoSdDAO.loadAnnualitaUsiByIdAnnualitaSd(annualitaSdDTO.getIdAnnualitaSd());
							for (AnnualitaUsoSdDTO annualitaUsoSdExtendedDTO : listAnnualitaUsoSd) {
								LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] GET Uso Ridaum SD");
								List<UsoRidaumSdExtendedDTO> UsoRidaumSdDTO = usoRidaumSdDAO.loadUsoRidaumSdByIdAnnualitaUsoSd(annualitaUsoSdExtendedDTO.getIdAnnualitaUsoSd());
								if(UsoRidaumSdDTO != null)
									annualitaUsoSdExtendedDTO.setUsoRidaumSd(UsoRidaumSdDTO);
							}
							
							annualitaSdDTO.setAnnualitaUsoSd(listAnnualitaUsoSd);
							

						}
//						BigDecimal impRecuperoCanone= BigDecimal.ZERO;
//						if(statoDebitorioDTO.getImpRecuperoCanone() != null) {
//							impRecuperoCanone = statoDebitorioDTO.getImpRecuperoCanone();
//						}
//						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] calcolo canoneDovuto");
//						canoneDovuto = listAnnualitaSdDTO.stream().map(AnnualitaSdDTO::getCanoneAnnuo).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRecuperoCanone) ;
//						statoDebitorioDTO.setCanoneDovuto(canoneDovuto);
//						
//						BigDecimal importoDovuto = canoneDovuto.add(impRecuperoCanone);
//							
//						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] calcolo importo Dovuto");
//						statoDebitorioDTO.setImportoDovuto(importoDovuto);
						
						LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] calcolo importo Versato");
//[DP] compensazioni da rivedere
//						impCompensazioneCanone = BigDecimal.ZERO;
//						if(statoDebitorioDTO.getImpCompensazioneCanone() != null) {
//							impCompensazioneCanone= statoDebitorioDTO.getImpCompensazioneCanone() ;
//						}
//						BigDecimal importoVersato = canoneDovuto.subtract(impCompensazioneCanone);
//						statoDebitorioDTO.setImportoVersato(importoVersato);
						
						//statoDebitorioDTO.setImpMancanteImpEccedente(impCompensazioneCanone.subtract(canoneDovuto));
					}
				
					LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] GET rimborsi");
					List<RimborsoExtendedDTO>  rimborsi = rimborsoDAO.loadRimborsiByIdStatoDebitorio(statoDebitorioDTO.getIdStatoDebitorio());
					statoDebitorioDTO.setRimborsi(rimborsi);
					
					LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] GET rata");
					
					List<RataSdDTO> ListRataSdDTO = rataSdDAO.loadListRataSdByStatoDebitorio(statoDebitorioDTO.getIdStatoDebitorio());
					BigDecimal impVersato = BigDecimal.ZERO;
					BigDecimal impSpeseNotifica =  BigDecimal.ZERO;
					BigDecimal interessiMaturati =  BigDecimal.ZERO;
					BigDecimal impRimborso =  BigDecimal.ZERO;
					for (RataSdDTO rataSdDTO : ListRataSdDTO) {
						List<DettaglioPagExtendedDTO>  listdettaglioPag = dettaglioPagDAO.getDettaglioPagByIdRate(rataSdDTO.getIdRataSd());
						//rataSdDTO.setDettaglioPag(listdettaglioPag);
						if(!listdettaglioPag.isEmpty())
					       impVersato = listdettaglioPag.stream().filter(d -> d.getImportoVersato() != null).map(DettaglioPagExtendedDTO::getImportoVersato).reduce(BigDecimal.ZERO, BigDecimal::add).add(impVersato) ;
					}
					if(statoDebitorioDTO.getImpSpeseNotifica() != null) {
						impSpeseNotifica = statoDebitorioDTO.getImpSpeseNotifica();
					}
					if(ListRataSdDTO != null) {
						
						interessiMaturati = ListRataSdDTO.stream().filter(r -> r.getInteressiMaturati() != null && r.getIdRataSdPadre() == null).map(RataSdDTO::getInteressiMaturati).reduce(BigDecimal.ZERO, BigDecimal::add) ;
					}
					if(rimborsi != null) {
						//[DP]issue33 da NUOVA SPECIFICA impRimborso si somma solo se tipo rimborso = 1
						//impRimborso = rimborsi.stream().map(RimborsoDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRimborso) ;
						impRimborso = rimborsi.stream().filter(r -> r.getIdTipoRimborso() != null && r.getTipoRimborso().getCodTipoRimborso().equals("RIMBORSATO")).map(RimborsoDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRimborso) ;					
					}
					
					LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] calcolo importo Versato");
					map.put("idStatoDebitorio", statoDebitorioDTO.getIdStatoDebitorio());
				   params = getParameterValue(map);
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

					//[DP] inizio spostamento
					if(ListRataSdDTO != null) {	
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
					}
					//[DP]Fine spostamento
					
					statoDebitorioDTO.setImportoVersato(importoVersato);
				
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
					
					//BigDecimal impMancImpEcc = importoVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati).subtract(impCompensazioneCanone)).subtract(sommRimborsi);
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
					
//					bigdecimal importoeccedente =   bigdecimal.zero;
//					importoEccedente =	importoVersato.add(impRimborso).subtract(canoneDovuto).subtract(IntMaturatiSpeseNotifica);
					
					if(impMancImpEcc.compareTo(BigDecimal.ZERO) > 0) {
						statoDebitorioDTO.setImportoEccedente(impMancImpEcc);
					}else {
						statoDebitorioDTO.setImportoEccedente(BigDecimal.ZERO);
					}
					
					
					BigDecimal intMaturatiSpeseNotifica =  BigDecimal.ZERO;
					intMaturatiSpeseNotifica = impSpeseNotifica.add(interessiMaturati);
					statoDebitorioDTO.setIntMaturatiSpeseNotifica(intMaturatiSpeseNotifica);

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

					String attivita = mapAttivitaSD(statoDebitorioDTO);
					statoDebitorioDTO.setAttivita(attivita);
					// parte di accertamento
					BigDecimal accImportoDovuto = BigDecimal.ZERO;
					String accDataScadenzaPag = null;
					BigDecimal accInteressiDovuti = BigDecimal.ZERO;
					BigDecimal accImportoVersato = BigDecimal.ZERO;
					BigDecimal accImportoDiCanoneMancante = BigDecimal.ZERO;
					BigDecimal accInteressiMancanti  = BigDecimal.ZERO;
					BigDecimal accInteressiVersati  = BigDecimal.ZERO; 
					BigDecimal statoDebImpSpesNot = BigDecimal.ZERO;
					if(ListRataSdDTO != null) {
						RataSdDTO rata = ListRataSdDTO.stream().filter(r -> r.getIdRataSdPadre() == null).collect(Collectors.toList()).get(0) ;
						 BigDecimal canoneDovutoRata = rata.getCanoneDovuto() != null ? rata.getCanoneDovuto() : BigDecimal.ZERO;
						accImportoDovuto = accImportoDovuto.add(canoneDovutoRata);
						statoDebitorioDTO.setAccImportoDovuto(accImportoDovuto);
						
						accDataScadenzaPag = rata.getDataScadenzaPagamento();
						statoDebitorioDTO.setAccDataScadenzaPag(accDataScadenzaPag);
					
						if(statoDebitorioDTO.getImpSpeseNotifica() != null)
							statoDebImpSpesNot = statoDebitorioDTO.getImpSpeseNotifica();
						
						accInteressiDovuti= accInteressiDovuti.add(interessiMaturati).add(statoDebImpSpesNot);
						
						accImportoVersato = accImportoVersato.add(statoDebitorioDTO.getImportoVersato());
						statoDebitorioDTO.setAccImportoVersato(accImportoVersato);
						
						//accImportoDiCanoneMancante = canoneDovuto.subtract(impRimborso).subtract(statoDebitorioDTO.getImportoVersato());
						//[DP] modificata analisi aggiunto imp rimborso invece che sottratto solo se maggiore di 0 
						if(impRimborso.compareTo(BigDecimal.ZERO) > 0){
						  accImportoDiCanoneMancante = canoneDovuto.add(impRimborso).subtract(statoDebitorioDTO.getImportoVersato());
						}else {
							accImportoDiCanoneMancante = canoneDovuto.subtract(statoDebitorioDTO.getImportoVersato());
						}
						
						//[DP] issue 26 
						//[VF] issue 90: questa if secondo l'analisi va commentata
						//Vedi doc WP2-2.2-SRV-V102-Servizi_RISCABESRV - Pag. 192
//						if(impCompensazioneCanone.compareTo(BigDecimal.ZERO) > 0) {
//							accImportoDiCanoneMancante = accImportoDiCanoneMancante.subtract(impCompensazioneCanone);
//						}
						
						if(accImportoDiCanoneMancante.compareTo(BigDecimal.ZERO) >= 0) {
							statoDebitorioDTO.setAccImportoDiCanoneMancante(accImportoDiCanoneMancante);
							// [VF] Fix per ISSUE 90 accInteressiMancanti deve essere valorizzata
							// correttamente per i calcoli successivi
							accInteressiMancanti = statoDebitorioDTO.getIntMaturatiSpeseNotifica();
							statoDebitorioDTO.setAccInteressiMancanti(accInteressiMancanti);
						}else {
							statoDebitorioDTO.setAccImportoDiCanoneMancante(BigDecimal.ZERO);
							//[DP] issue33 Da NUOVA SPECIFICA non si sottrae impRimborso
							//(- impCompensazioneCanone + accImportoRImborsato)							
//							accInteressiMancanti = canoneDovutoRata.add(statoDebitorioDTO.getIntMaturatiSpeseNotifica())
//                      	.subtract(statoDebitorioDTO.getImportoVersato()).subtract(impRimborso);
							accInteressiMancanti = canoneDovutoRata.add(statoDebitorioDTO.getIntMaturatiSpeseNotifica())
									.subtract(statoDebitorioDTO.getImportoVersato()).add(impRimborso).subtract(impCompensazioneCanone);
							
							
							if(accInteressiMancanti.compareTo(BigDecimal.ZERO) > 0) {
								statoDebitorioDTO.setAccInteressiMancanti(accInteressiMancanti);
							}else {	
								statoDebitorioDTO.setAccInteressiMancanti(BigDecimal.ZERO);
								// [VF] Issue 90 - occorre azzerare questo valore altrimenti quando viene negativo 
								// influisce sul valore calcolato per accInteressiVersati
								accInteressiMancanti = BigDecimal.ZERO;
							}
							
						}
						//[DP] issue33 da nuova specifica accInteressiVersati = interessi e spese di notifica - accInteressiMancanti
						//accInteressiVersati = statoDebitorioDTO.getImportoVersato().add(impRimborso).subtract(canoneDovuto);
						accInteressiVersati =  intMaturatiSpeseNotifica.subtract(accInteressiMancanti);
						
						if(accInteressiVersati.compareTo(BigDecimal.ZERO) > 0) {
							statoDebitorioDTO.setAccInteressiVersati(accInteressiVersati);
						}else {
							statoDebitorioDTO.setAccInteressiVersati(BigDecimal.ZERO);
						}
						// Issue 81: il campo accImportoRimborsato deve tenere conto sia di rimborsi che
						// di eventuali compensazioni utilizzate, quindi valorizzo il campo con
						// sommRimborsi (come per il campo sommaRimborsi)
						// statoDebitorioDTO.setAccImportoRimborsato(impRimborso);
						statoDebitorioDTO.setAccImportoRimborsato(sommRimborsi);
						
					
					  }

				}

				
			}

		} catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] No record found in database for CodUtenza "+ codUtenza, e);
		    return Collections.emptyList();
		} catch (DataAccessException e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] Errore nell'accesso ai dati", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] Errore SQL ", e); 
			throw new DAOException(Constants.ERRORE_GENERICO);
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] ERROR : " +e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorioByCodUtenza] END");
		}
		return res;
	}
	@Override
	public Integer countAllStatoDebitorioByCodUtenza(String codUtenza, List<Integer> sdDaEscludere) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codUtenza", codUtenza.trim().toUpperCase());
			String query = QUERY_COUNT_STATI_DEBITORI_BY_COD_UTENZA ;
			if(Utils.isNotEmpty(sdDaEscludere)) {
				map.put("sdDaEscludere", sdDaEscludere);
				query+=ESCLUDE_SD;
			}
			MapSqlParameterSource params = getParameterValue(map);
			return 	template.queryForObject(query, params, Integer.class);
						
		}catch (DataAccessException e) {
				LOGGER.error("[StatoDebitorioDAOImpl::countAllStatoDebitorioByCodUtenza] ERROR : " ,e);
				throw new DAOException(Constants.ERRORE_GENERICO);
		}
	
	}
	@Override
	public void updateAttivitaForAllStatoDebitori(List<StatoDebitorioExtendedDTO> statoDebitorio , String idAttivitaSD) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			if(statoDebitorio == null || statoDebitorio.size()==0) {
				return;
			}
			
			Long idAttivitaStatoDeb = null;

			if(idAttivitaSD != null) {
				idAttivitaStatoDeb = Long.valueOf(idAttivitaSD);
			}else {
				List<AttivitaStatoDebitorioDTO> attivitaStatoDebitorioDTO = statoDebitorio.stream().map(StatoDebitorioExtendedDTO::getAttivitaStatoDeb)
						.collect(Collectors.toList());
				if(attivitaStatoDebitorioDTO.get(0) != null)
					idAttivitaStatoDeb = attivitaStatoDebitorioDTO.get(0).getIdAttivitaStatoDeb();
			}

			List<Long> listIdStatoDebitorio = (List<Long>) statoDebitorio.stream().map(StatoDebitorioExtendedDTO::getIdStatoDebitorio)
					.collect(Collectors.toList());
			
			String attore = statoDebitorio.get(0).getGestAttoreUpd();
			
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			
			Map<String, Object> map = new HashMap<>();
			map.put("idAttivitaStatoDeb", idAttivitaStatoDeb);
			map.put("listIdStatoDebitorio", listIdStatoDebitorio);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			
			MapSqlParameterSource params = getParameterValue(map);
			
			String query = QUERY_UPD_ATTIVITA_FOR_ALL_STATI_DEB;
			template.update(getQuery(query, null, null), params);
						
		}catch (DataAccessException e) {
				LOGGER.error("[StatoDebitorioDAOImpl::updateAttivitaStatoDebitorio] ERROR : " ,e);
				throw new DAOException(Constants.ERRORE_GENERICO);
		}
		
	}
	
	@Override
	public Integer countStatoDebitorioBySoggettoTipoAttivita(Long idSoggetto, Long idGruppoSoggetto,
			Long idAttivitaStatoDeb) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idSoggetto", idSoggetto);
			map.put("idAttivitaStatoDeb", idAttivitaStatoDeb);

			String query = QUERY_COUNT_STATI_DEBITORI_BY_SOGG_TIPO_ATTIVITA;
			if (idGruppoSoggetto != null) {
				query = query + CONDITION_GRUPPO;
				map.put("idGruppoSoggetto", idGruppoSoggetto);
			} else {
				query = query + CONDITION_GRUPPO_NULL;
			}

			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(query, params, Integer.class);

		} catch (DataAccessException e) {
			LOGGER.error("[StatoDebitorioDAOImpl::countStatoDebitorioBySoggettoTipoAttivita] ERROR : ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::countStatoDebitorioBySoggettoTipoAttivita] END");
		}
	}
	
	
	@Override
	public List<StatoDebitorioExtendedDTO> loadAllStatoDebitorio(Integer offset, Integer limit, String sort) throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorio] BEGIN");
		
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		
		
		sql.append("select ");
		sql.append("sta.id_stato_debitorio, ");
		sql.append("sta.id_riscossione, ");
		sql.append("sta.id_soggetto, ");
		sql.append("sta.id_gruppo_soggetto, ");
		sql.append("sta.id_recapito, ");
		sql.append("sta.id_attivita_stato_deb, ");
		sql.append("sta.id_stato_contribuzione, ");
		sql.append("sta.id_tipo_dilazione, ");
		sql.append("sta.num_titolo, ");
		sql.append("sta.data_provvedimento, ");
		sql.append("sta.num_richiesta_protocollo, ");
		sql.append("sta.data_richiesta_protocollo, ");
		sql.append("sta.data_ultima_modifica, ");
		sql.append("sta.des_usi, ");
		sql.append("sta.note, ");
		sql.append("sta.imp_recupero_canone, ");
		sql.append("sta.imp_recupero_interessi, ");
		sql.append("sta.imp_spese_notifica, ");
		sql.append("sta.imp_compensazione_canone, ");
		sql.append("sta.desc_periodo_pagamento, ");
		sql.append("sta.desc_motivo_annullo, ");
		sql.append("sta.flg_dilazione, ");
		sql.append("sta.flg_annullato, ");
		sql.append("sta.flg_restituito_mittente, ");
		sql.append("sta.flg_invio_speciale, ");
		sql.append("sta.flg_addebito_anno_successivo, ");
		sql.append("sta.nap, ");
		sql.append("sta.nota_rinnovo, ");
		sql.append("sta.des_tipo_titolo ");
		sql.append("from risca_t_stato_debitorio sta ");
		sql.append("join ( ");
		sql.append("      select ");
		sql.append("      id_stato_debitorio ");
		sql.append("      from risca_t_stato_debitorio");
		sql.append("      where id_stato_contribuzione not in ("+Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.REGOLARE+", "+Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.REGOLARIZZATO+") ");
		sql.append("      or id_stato_contribuzione is null  ");
		sql.append("union all ");
		sql.append("select * from ( ");
		sql.append("                select");
		sql.append("                id_stato_debitorio ");
		sql.append("                from risca_t_stato_debitorio ");
		sql.append("                where id_stato_contribuzione in ("+Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.REGOLARE+", "+Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.REGOLARIZZATO+") ");
		sql.append("                and ( ");
		sql.append("                DATE(gest_data_upd) >= DATE(current_date-4) ");
		sql.append("                and gest_attore_upd != '"+Constants.BATCH_STATO_CONT+"'  ");
		sql.append("                and gest_attore_upd != '"+Constants.STATO_CONTRIBUZIONE+"' ");
		sql.append("                and gest_attore_upd != '"+Constants.MIGRAZIONE+"' ");
		sql.append("                ) ");
		sql.append("union all ");
		sql.append("select ");
		sql.append("rrs.id_stato_debitorio ");
		sql.append("from risca_t_stato_debitorio dsd ");
		sql.append("join risca_t_stato_debitorio rrs on dsd.nap = rrs.nap  ");
		sql.append("and dsd.id_stato_debitorio <> rrs.id_stato_debitorio ");
		sql.append("where dsd.id_stato_contribuzione in ("+Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.REGOLARE+", "+Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.REGOLARIZZATO+") ");
		sql.append("and ( ");
		sql.append("     DATE(dsd.gest_data_upd) >= DATE(current_date-4) ");
		sql.append("     and dsd.gest_attore_upd != '"+Constants.BATCH_STATO_CONT+"' ");
		sql.append("     and dsd.gest_attore_upd != '"+Constants.STATO_CONTRIBUZIONE+"'  ");
		sql.append("     and dsd.gest_attore_upd != '"+Constants.MIGRAZIONE+"' ");
		sql.append("     ) ");
		sql.append("   ) lista_in_union ");
		sql.append(") condizione on sta.id_stato_debitorio = condizione.id_stato_debitorio ");
		sql.append("order by sta.id_stato_debitorio ");
		
		
		if(offset != null) {
			sql.append(" offset :offset  ");
			}
			if(limit != null) {
				sql.append(" limit :limit ");
			}

		List<StatoDebitorioExtendedDTO> list = new ArrayList<StatoDebitorioExtendedDTO>();
		if(offset != null) {
		  paramMap.addValue("offset", offset);
		}
		if(limit != null) {
		  paramMap.addValue("limit", limit);
		}
		
		try {
			list = template.query(sql.toString(), paramMap, getExtendedRowMapper());

		} catch (Exception ex) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadAllStatoDebitorio] esecuzione query", ex);
			return list;
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::loadAllStatoDebitorio] END");
		}
		return list;
	}
	
	
	
	@Override
	public Integer countAllStatoDebitorio() throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::countAllStatoDebitorio] BEGIN");
        MapSqlParameterSource paramST  = new MapSqlParameterSource();
		try {
			return 	template.queryForObject( QUERY_COUNT_ALL_STATI_DEBITORI_SIMPLE , paramST, Integer.class);
						
		}catch (DataAccessException e) {
				LOGGER.error("[StatoDebitorioDAOImpl::countAllStatoDebitorio] ERROR : " +e);
				throw new DAOException(e);
			}
	}
	
	@Override
	public void updateStatoContribuzioneForStatoDebitorio(Long idStatoContribuzione, Long idStatoDebitorio, String fruitore)
			throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoContribuzioneForStatoDebitorio] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoContribuzione", idStatoContribuzione);
			map.put("idStatoDebitorio", idStatoDebitorio);
			map.put("gestAttoreUpd", fruitore);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
		    template.update(getQuery(QUERY_UPD_STATO_CONTRIBUZIONE_FOR_STATO_DEB, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::updateStatoContribuzioneForStatoDebitorio] ERROR : " +e);
			throw new DAOException(e.getMessage());
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::updateStatoContribuzioneForStatoDebitorio] END");
		}
		
	}
	


	@Override
	public Integer updateSpeseNotificaStatoDebitorio(Long idStatoDebitorio, BigDecimal impSpeseNotifica, String attore) throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Integer res = null; 
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			map.put("impSpeseNotifica", impSpeseNotifica);
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", attore);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPD_SPESE_NOTIFICA, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::updateSpeseNotificaStatoDebitorio] ERROR : " , e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::updateSpeseNotificaStatoDebitorio] END");
		}

		return res;
	}
	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public BigDecimal sommaAllCanoneDovutoByNap(String nap) throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			String query = QUERY_SOMMA_CANONE_DOVUTO;
			MapSqlParameterSource params = getParameterValue(map);
			return 	template.queryForObject(query, params, BigDecimal.class);
		} catch (EmptyResultDataAccessException e) {
			return BigDecimal.ZERO;
			
		} catch (Exception e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public List<StatoDebitorioExtendedDTO> loadStatiDebitoriModificatiOggi(Integer offset, Integer limit, String sort)
			throws DAOException {
		LOGGER.debug("[StatoDebitorioDAOImpl::loadStatiDebitoriModificatiOggi] BEGIN");

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();

		sql.append("WITH lista_sd_identificati  ");
		sql.append("  AS ( ");
		sql.append("          select ");
		sql.append("                 rtsd.id_stato_debitorio, rtsd.id_riscossione, rtsd.id_soggetto, rtsd.id_gruppo_soggetto, rtsd.id_recapito, ");
		sql.append("                 rtsd.id_attivita_stato_deb, rtsd.id_stato_contribuzione, rtsd.id_tipo_dilazione, rtsd.num_titolo, ");
		sql.append("                 rtsd.data_provvedimento, rtsd.num_richiesta_protocollo, rtsd.data_richiesta_protocollo, rtsd.data_ultima_modifica, ");
		sql.append("                 rtsd.des_usi, rtsd.note, rtsd.imp_recupero_canone, rtsd.imp_recupero_interessi, rtsd.imp_spese_notifica, rtsd.imp_compensazione_canone, ");
		sql.append("                 rtsd.desc_periodo_pagamento, rtsd.desc_motivo_annullo, rtsd.flg_dilazione, rtsd.flg_annullato, rtsd.flg_restituito_mittente , ");
		sql.append("                 rtsd.flg_invio_speciale, rtsd.flg_addebito_anno_successivo, rtsd.nap, rtsd.nota_rinnovo, rtsd.des_tipo_titolo, ");
		sql.append("                 rtsd.gest_data_upd, rtsd.gest_attore_upd ");
		sql.append("            from risca_t_stato_debitorio rtsd ");
		sql.append("left outer join ( ");
		sql.append("                  select id_stato_debitorio, count(*) as rate_quanti_aggiornati ");
		sql.append("                    from risca_r_rata_sd ");
		sql.append("                  where DATE(gest_data_upd) = DATE(current_date) ");
		sql.append("                    and gest_attore_upd != '"+Constants.BATCH_STATO_CONT+"'  ");
		sql.append("                    and gest_attore_upd != '"+Constants.STATO_CONTRIBUZIONE+"' "); 
		sql.append("                     and gest_attore_upd != '"+Constants.MIGRAZIONE+"' "); 
		sql.append("               group by id_stato_debitorio ");
		sql.append("                ) rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio ");
		sql.append("left outer join ( ");
		sql.append("                  select rat.id_stato_debitorio,   ");  
		sql.append("                        count(*) as dett_quanti_aggiornati ");
		sql.append("                 from risca_r_rata_sd rat   ");
		sql.append("                  join risca_r_dettaglio_pag dpa on rat.id_rata_sd = dpa.id_rata_sd ");
		sql.append("                 where DATE(dpa.gest_data_upd) = DATE(current_date) ");
		sql.append("                  and dpa.gest_attore_upd != '"+Constants.BATCH_STATO_CONT+"'  ");
		sql.append("                  and dpa.gest_attore_upd != '"+Constants.STATO_CONTRIBUZIONE+"' ");
		sql.append("               and dpa.gest_attore_upd != '"+Constants.MIGRAZIONE+"' ");
		sql.append("          group by rat.id_stato_debitorio ");
		sql.append("           ) dparate on rtsd.id_stato_debitorio = dparate.id_stato_debitorio ");
		sql.append("    where ( ");
		sql.append("            DATE(rtsd.gest_data_upd) = DATE(current_date) ");
		sql.append("             and ( rtsd.gest_attore_upd != '"+Constants.STATO_CONTRIBUZIONE+"' ");
		sql.append("                   and rtsd.gest_attore_upd != '"+Constants.MIGRAZIONE+"'  ");
		sql.append("                   and rtsd.gest_attore_upd != '"+Constants.BATCH_STATO_CONT+"') ");
		sql.append("          )  ");
		sql.append("      or ( ");
		sql.append("           rrrs.rate_quanti_aggiornati <>0) ");
		sql.append("      or ( ");
		sql.append("           dparate.dett_quanti_aggiornati <>0) ");
		sql.append("    ) ");
		sql.append(" SELECT ");
		sql.append("        lsd.* ");
		sql.append("   FROM lista_sd_identificati as lsd ");
		sql.append(" union all ");
		sql.append("  SELECT ");
		sql.append("            rrs.id_stato_debitorio, rrs.id_riscossione, rrs.id_soggetto, rrs.id_gruppo_soggetto, rrs.id_recapito , ");
		sql.append("            rrs.id_attivita_stato_deb, rrs.id_stato_contribuzione, rrs.id_tipo_dilazione, rrs.num_titolo, ");
		sql.append("            rrs.data_provvedimento, rrs.num_richiesta_protocollo, rrs.data_richiesta_protocollo, rrs.data_ultima_modifica, ");
		sql.append("            rrs.des_usi, rrs.note, rrs.imp_recupero_canone, rrs.imp_recupero_interessi, rrs.imp_spese_notifica, rrs.imp_compensazione_canone, ");
		sql.append("            rrs.desc_periodo_pagamento, rrs.desc_motivo_annullo, rrs.flg_dilazione, rrs.flg_annullato, rrs.flg_restituito_mittente , "); 
		sql.append("           rrs.flg_invio_speciale, rrs.flg_addebito_anno_successivo, rrs.nap, rrs.nota_rinnovo, rrs.des_tipo_titolo, ");
		sql.append("           rrs.gest_data_upd, rrs.gest_attore_upd ");
		sql.append("   FROM lista_sd_identificati as dsd ");
		sql.append("   join risca_t_stato_debitorio rrs on dsd.nap = rrs.nap ");
		sql.append("                                   and dsd.id_stato_debitorio <> rrs.id_stato_debitorio ");
		sql.append(" where dsd.nap is not null ");
		sql.append(" order by  id_stato_debitorio ");



		if(offset != null) {
			sql.append(" offset :offset  ");
		}
		if(limit != null) {
			sql.append(" limit :limit ");
		}

		List<StatoDebitorioExtendedDTO> list = new ArrayList<StatoDebitorioExtendedDTO>();
		if(offset != null) {
			paramMap.addValue("offset", offset);
		}
		if(limit != null) {
			paramMap.addValue("limit", limit);
		}

		try {
			list = template.query(sql.toString(), paramMap, getExtendedRowMapper());

		} catch (Exception ex) {
			LOGGER.error("[StatoDebitorioDAOImpl::loadStatiDebitoriModificatiOggi] esecuzione query", ex);
			return list;
		} finally {
			LOGGER.debug("[StatoDebitorioDAOImpl::loadStatiDebitoriModificatiOggi] END");
		}
		return list;

	}

	@Override
	public StatoDebitorioExtendedDTO loadStatoDebitorioByIdRata(Long idRata) throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRata", idRata);
			String query = QUERY_LOAD_ID_SD_BY_ID_RATA;
			MapSqlParameterSource params = getParameterValue(map);
			Long idStatoDebitorio = template.queryForObject(query, params, Long.class);
			return loadStatoDebitorioByIdStatoDebitorio(idStatoDebitorio);
			
		} catch (EmptyResultDataAccessException e) {
			return new StatoDebitorioExtendedDTO();
			
		} catch (Exception e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	// Metodo per aggiungere "WHERE" o "AND" al queryBuilder
	private void addWhereOrAnd(StringBuilder queryBuilder) {
	    if (queryBuilder.toString().contains(" WHERE ")) {
	        queryBuilder.append(" AND ");
	    } else {
	        queryBuilder.append(" WHERE ");
	    }
	}

	   

	private void buildQueryAndParameters(RicercaPagamentiDaVisionareDTO dto, StringBuilder queryBuilder, Map<String, Object> map) {
	    if (dto.getFlgPratica() != null && "SI".equals(dto.getFlgPratica())) {
	        queryBuilder.append(QUERY_JOIN_SOGGETTO_PRATICA);
	    } else {
	        queryBuilder.append(QUERY_JOIN_SOGGETTO_SD);
	    }
	    
	    addCondition(dto.getNumPratica(), "numPratica", QUERY_WHERE_STATI_DEBITORI_BY_NUM_PRATICA, queryBuilder, map);
	    addCondition(dto.getCodUtenza(), "codUtenza", QUERY_WHERE_STATI_DEBITORI_BY_COD_UTENZA, queryBuilder, map);
	    addCondition(dto.getNap(), "nap", QUERY_WHERE_STATI_DEBITORI_BY_NAP, queryBuilder, map);
	    
	    if (Utils.isNotEmpty(dto.getSdDaEscludere())) {
	        map.put("sdDaEscludere", dto.getSdDaEscludere());
	        addWhereOrAnd(queryBuilder);
	        queryBuilder.append(ESCLUDE_SD_FOR_PAGAMENTI_DA_VISIONARE);
	    }
	    addCondition(dto.getImportoDa(), "importoDa", IMPORTO_DA, queryBuilder, map);
	    addCondition(dto.getImportoA(), "importoA", IMPORTO_A, queryBuilder, map);
	    
	    if (dto.getFlgPratica() != null && StringUtils.isNotBlank(dto.getTitolare())) {
	        map.put("titolare", dto.getTitolare().toUpperCase().trim());
	        addWhereOrAnd(queryBuilder);
	        queryBuilder.append(TITOLARE);
	    }
	}

	private void addCondition(Object value, String paramName, String query, StringBuilder queryBuilder, Map<String, Object> map) {
	    if (value != null) {
	        map.put(paramName, value instanceof String ? ((String) value).trim().toUpperCase() : value);
	        addWhereOrAnd(queryBuilder);
	        queryBuilder.append(query);
	    }
	}

	private String getDynamicOrderByCondition(String sort) {
	    if (StringUtils.isNotBlank(sort)) {
	        String dynamicOrderByCondition = mapSortConCampiDB(sort);
	        if (dynamicOrderByCondition != null) {
	            return dynamicOrderByCondition.concat(",").concat(ORDER_BY_SD.replace("ORDER BY", ""));
	        }
	    }
	    return ORDER_BY_SD;
	} 
	@Override
	public List<StatoDebitorioExtendedDTO> loadStatiDebitoriPerPagamentiDaVisionare(
	        RicercaPagamentiDaVisionareDTO ricercaPagamentiDaVisionareDTO, Long idAmbito, Integer offset, Integer limit,
	        String sort) throws DAOException {

	    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	    List<StatoDebitorioExtendedDTO> res = new ArrayList<>();
	    StringBuilder queryBuilder = new StringBuilder(QUERY_SELECT_STATI_DEBITORI_FOR_PAGAMENTI_DA_VISIONARE);
	    Map<String, Object> map = new HashMap<>();
	    BigDecimal canoneDovuto = BigDecimal.ZERO;
	    try {
	        buildQueryAndParameters(ricercaPagamentiDaVisionareDTO, queryBuilder, map);

	        String dynamicOrderByCondition = getDynamicOrderByCondition(sort);

	        MapSqlParameterSource params = getParameterValue(map);
	        res = template.query(
	            getQuery(queryBuilder.toString() + dynamicOrderByCondition,  offset!= null ? offset.toString(): null ,  limit != null ? limit.toString(): null), params, getExtendedRowMapper());
	        
			if(!res.isEmpty()) {
				
				for (StatoDebitorioExtendedDTO statoDebitorioDTO : res) {
					if(statoDebitorioDTO.getIdAttivitaStatoDeb() != null) {
						AttivitaStatoDebitorioDTO attivitaStatoDebitorioDTO = attivitaStatoDebitorioDAO.getAttivitaStatoDebitorioById(statoDebitorioDTO.getIdAttivitaStatoDeb());
						statoDebitorioDTO.setAttivitaStatoDeb(attivitaStatoDebitorioDTO);
					}
					if(statoDebitorioDTO.getIdStatoContribuzione() != null) {
						StatoContribuzioneDTO statoContribuzioneDTO = statoContribuzioneDAO.loadStatoContribuzioneById(statoDebitorioDTO.getIdStatoContribuzione());
						statoDebitorioDTO.setStatoContribuzione(statoContribuzioneDTO);
					}

			        BigDecimal impCompensazioneCanone = BigDecimal.ZERO;
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

							List<AnnualitaUsoSdDTO> listAnnualitaUsoSd = annualitaUsoSdDAO.loadAnnualitaUsiByIdAnnualitaSd(annualitaSdDTO.getIdAnnualitaSd());
							for (AnnualitaUsoSdDTO annualitaUsoSdExtendedDTO : listAnnualitaUsoSd) {
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
//						canoneDovuto = listAnnualitaSdDTO.stream().map(AnnualitaSdDTO::getCanoneAnnuo).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRecuperoCanone) ;
//						statoDebitorioDTO.setCanoneDovuto(canoneDovuto);
//						
//						BigDecimal importoDovuto = canoneDovuto.add(impRecuperoCanone);
//							
//						statoDebitorioDTO.setImportoDovuto(importoDovuto);
						//[DP] compensazioni da rivedere
//						impCompensazioneCanone = BigDecimal.ZERO;
//						if(statoDebitorioDTO.getImpCompensazioneCanone() != null) {
//							impCompensazioneCanone= statoDebitorioDTO.getImpCompensazioneCanone() ;
//						}
//						BigDecimal importoVersato = canoneDovuto.subtract(impCompensazioneCanone);
//						statoDebitorioDTO.setImportoVersato(importoVersato);
						
						//statoDebitorioDTO.setImpMancanteImpEccedente(impCompensazioneCanone.subtract(canoneDovuto));
					}
				
					List<RimborsoExtendedDTO>  rimborsi = rimborsoDAO.loadRimborsiByIdStatoDebitorio(statoDebitorioDTO.getIdStatoDebitorio());
					statoDebitorioDTO.setRimborsi(rimborsi);
					
					
					List<RataSdDTO> ListRataSdDTO = rataSdDAO.loadListRataSdByStatoDebitorio(statoDebitorioDTO.getIdStatoDebitorio());
					BigDecimal impVersato = BigDecimal.ZERO;
					BigDecimal impSpeseNotifica =  BigDecimal.ZERO;
					BigDecimal interessiMaturati =  BigDecimal.ZERO;
					BigDecimal impRimborso =  BigDecimal.ZERO;
					for (RataSdDTO rataSdDTO : ListRataSdDTO) {
						List<DettaglioPagExtendedDTO>  listdettaglioPag = dettaglioPagDAO.getDettaglioPagByIdRate(rataSdDTO.getIdRataSd());
						//rataSdDTO.setDettaglioPag(listdettaglioPag);
						if(!listdettaglioPag.isEmpty())
					       impVersato = listdettaglioPag.stream().filter(d -> d.getImportoVersato() != null).map(DettaglioPagExtendedDTO::getImportoVersato).reduce(BigDecimal.ZERO, BigDecimal::add).add(impVersato) ;
					}
					if(statoDebitorioDTO.getImpSpeseNotifica() != null) {
						impSpeseNotifica = statoDebitorioDTO.getImpSpeseNotifica();
					}
					if(ListRataSdDTO != null) {
						
						interessiMaturati = ListRataSdDTO.stream().filter(r -> r.getInteressiMaturati() != null && r.getIdRataSdPadre() == null).map(RataSdDTO::getInteressiMaturati).reduce(BigDecimal.ZERO, BigDecimal::add) ;
					}
					if(rimborsi != null) {
						//[DP]issue33 da NUOVA SPECIFICA impRimborso si somma solo se tipo rimborso = 1
						//impRimborso = rimborsi.stream().map(RimborsoDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRimborso) ;
						impRimborso = rimborsi.stream().filter(r -> r.getIdTipoRimborso() != null && r.getTipoRimborso().getCodTipoRimborso().equals("RIMBORSATO")).map(RimborsoDTO::getImpRimborso).reduce(BigDecimal.ZERO, BigDecimal::add).add(impRimborso) ;											
					}
					
					map.put("idStatoDebitorio", statoDebitorioDTO.getIdStatoDebitorio());
				   params = getParameterValue(map);
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
					
					//[DP] inizio spostamento issue 41
					 if(ListRataSdDTO != null) {
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
					 }
					//[DP]Fine spostamento
					
					statoDebitorioDTO.setImportoVersato(importoVersato);
				
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
					
					//BigDecimal impMancImpEcc = importoVersato.subtract(canoneDovuto.add(impSpeseNotifica).add(interessiMaturati).subtract(impCompensazioneCanone)).subtract(sommRimborsi);
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
					
//					bigdecimal importoeccedente =   bigdecimal.zero;
//					importoEccedente =	importoVersato.add(impRimborso).subtract(canoneDovuto).subtract(IntMaturatiSpeseNotifica);
					
					if(impMancImpEcc.compareTo(BigDecimal.ZERO) > 0) {
						statoDebitorioDTO.setImportoEccedente(impMancImpEcc);
					}else {
						statoDebitorioDTO.setImportoEccedente(BigDecimal.ZERO);
					}
					
					
					BigDecimal intMaturatiSpeseNotifica =  BigDecimal.ZERO;
					intMaturatiSpeseNotifica = impSpeseNotifica.add(interessiMaturati);
					statoDebitorioDTO.setIntMaturatiSpeseNotifica(intMaturatiSpeseNotifica);

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
						statoDebitorioDTO.setNumPratica(riscossioneDTO.getNumPratica());
					}

					String attivita = mapAttivitaSD(statoDebitorioDTO);
					statoDebitorioDTO.setAttivita(attivita);
					// parte di accertamento
					BigDecimal accImportoDovuto = BigDecimal.ZERO;
					String accDataScadenzaPag = null;
					BigDecimal accInteressiDovuti = BigDecimal.ZERO;
					BigDecimal accImportoVersato = BigDecimal.ZERO;
					BigDecimal accImportoDiCanoneMancante = BigDecimal.ZERO;
					BigDecimal accInteressiMancanti  = BigDecimal.ZERO;
					BigDecimal accInteressiVersati  = BigDecimal.ZERO; 
					BigDecimal statoDebImpSpesNot = BigDecimal.ZERO;
					if(ListRataSdDTO != null) {
						RataSdDTO rata = ListRataSdDTO.stream().filter(r -> r.getIdRataSdPadre() == null).collect(Collectors.toList()).get(0) ;
						 BigDecimal canoneDovutoRata = rata.getCanoneDovuto() != null ? rata.getCanoneDovuto() : BigDecimal.ZERO;
						accImportoDovuto = accImportoDovuto.add(canoneDovutoRata);
						statoDebitorioDTO.setAccImportoDovuto(accImportoDovuto);
						
						accDataScadenzaPag = rata.getDataScadenzaPagamento();
						statoDebitorioDTO.setAccDataScadenzaPag(accDataScadenzaPag);
					
						if(statoDebitorioDTO.getImpSpeseNotifica() != null)
							statoDebImpSpesNot = statoDebitorioDTO.getImpSpeseNotifica();
						
						accInteressiDovuti= accInteressiDovuti.add(interessiMaturati).add(statoDebImpSpesNot);
						
						accImportoVersato = accImportoVersato.add(statoDebitorioDTO.getImportoVersato());
						statoDebitorioDTO.setAccImportoVersato(accImportoVersato);
						
						//accImportoDiCanoneMancante = canoneDovuto.subtract(impRimborso).subtract(statoDebitorioDTO.getImportoVersato());
						//[DP] modificata analisi aggiunto imp rimborso invece che sottratto solo se maggiore di 0 
						if(impRimborso.compareTo(BigDecimal.ZERO) > 0){
						  accImportoDiCanoneMancante = canoneDovuto.add(impRimborso).subtract(statoDebitorioDTO.getImportoVersato());
						}else {
							accImportoDiCanoneMancante = canoneDovuto.subtract(statoDebitorioDTO.getImportoVersato());
						}
						
						//[DP] issue 26
						//[VF] issue 90: questa if secondo l'analisi va commentata
						//Vedi doc WP2-2.2-SRV-V102-Servizi_RISCABESRV - Pag. 192
//						if(impCompensazioneCanone.compareTo(BigDecimal.ZERO) > 0) {
//							accImportoDiCanoneMancante = accImportoDiCanoneMancante.subtract(impCompensazioneCanone);
//						}
						
						if(accImportoDiCanoneMancante.compareTo(BigDecimal.ZERO) >= 0) {
							statoDebitorioDTO.setAccImportoDiCanoneMancante(accImportoDiCanoneMancante);
							// [VF] Fix per ISSUE 90 accInteressiMancanti deve essere valorizzata
							// correttamente per i calcoli successivi
							accInteressiMancanti = statoDebitorioDTO.getIntMaturatiSpeseNotifica();
							statoDebitorioDTO.setAccInteressiMancanti(accInteressiMancanti);
						}else {
							statoDebitorioDTO.setAccImportoDiCanoneMancante(BigDecimal.ZERO);
							
							//[DP] issue33 Da NUOVA SPECIFICA non si sottrae impRimborso
							//(- impCompensazioneCanone + accImportoRImborsato)			
//							accInteressiMancanti = canoneDovutoRata.add(statoDebitorioDTO.getIntMaturatiSpeseNotifica())
//									.subtract(statoDebitorioDTO.getImportoVersato()).subtract(impRimborso);
							accInteressiMancanti = canoneDovutoRata.add(statoDebitorioDTO.getIntMaturatiSpeseNotifica())
									.subtract(statoDebitorioDTO.getImportoVersato()).add(impRimborso).subtract(impCompensazioneCanone);

							if(accInteressiMancanti.compareTo(BigDecimal.ZERO) > 0) {
								statoDebitorioDTO.setAccInteressiMancanti(accInteressiMancanti);
							}else {	
								statoDebitorioDTO.setAccInteressiMancanti(BigDecimal.ZERO);
								// [VF] Issue 90 - occorre azzerare questo valore altrimenti quando viene negativo 
								// influisce sul valore calcolato per accInteressiVersati
								accInteressiMancanti = BigDecimal.ZERO;
							}
							
						}
						//[DP] issue33 da nuova specifica accInteressiVersati = interessi e spese di notifica - accInteressiMancanti
						//accInteressiVersati = statoDebitorioDTO.getImportoVersato().add(impRimborso).subtract(canoneDovuto);
						accInteressiVersati =  intMaturatiSpeseNotifica.subtract(accInteressiMancanti);
						
						if(accInteressiVersati.compareTo(BigDecimal.ZERO) > 0) {
							statoDebitorioDTO.setAccInteressiVersati(accInteressiVersati);
						}else {
							statoDebitorioDTO.setAccInteressiVersati(BigDecimal.ZERO);
						}
						// Issue 81: il campo accImportoRimborsato deve tenere conto sia di rimborsi che
						// di eventuali compensazioni utilizzate, quindi valorizzo il campo con
						// sommRimborsi (come per il campo sommaRimborsi)
						// statoDebitorioDTO.setAccImportoRimborsato(impRimborso);
						statoDebitorioDTO.setAccImportoRimborsato(sommRimborsi);
						
					
					  }
					if(ricercaPagamentiDaVisionareDTO.getCalcolaInteressi() != null) {
						calcolaInteressiSD(statoDebitorioDTO, ricercaPagamentiDaVisionareDTO.getCalcolaInteressi(), idAmbito);
					}
					
					map.put("idSoggetto", statoDebitorioDTO.getIdSoggetto());
					params = getParameterValue(map);
					String titolare = template.query(QUERY_GET_TITOLARE_PRATICA_BY_ID_SOGGETTO, params, new ResultSetExtractor<String>(){
					    @Override
					    public String extractData(ResultSet rs) throws SQLException,DataAccessException {
					    	String titolare = null;
					        while(rs.next()){
					        	titolare = rs.getString("titolare");
					        }
					        return titolare;
					    }
					});
					
					statoDebitorioDTO.setTitolare(titolare);
					
				}

				
			}

		} catch(EmptyResultDataAccessException e) {
		    return Collections.emptyList();
		} catch (DataAccessException e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} catch (Exception e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return res;
	}

	@Override
	public Integer countStatiDebitoriPerPagamentiDaVisionare(
			RicercaPagamentiDaVisionareDTO ricercaPagamentiDaVisionareDTO, Long idAmbito) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
		    StringBuilder queryBuilder = new StringBuilder(QUERY_COUNT_STATI_DEBITORI_FOR_PAGAMENTI_DA_VISIONARE);
		     buildQueryAndParameters(ricercaPagamentiDaVisionareDTO, queryBuilder, map);

			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(queryBuilder.toString(), params, Integer.class);

		} catch (DataAccessException e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public List<SommaCanoneTipoUsoSdDTO> loadSommaCanonePerTipoUsoAnnualitaSd(Long idStatoDebitorio)
			throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);

			return template.query(getQuery(QUERY_SELECT_SOMMA_CANONI_PER_TIPO_USO, null, null), params,
					getSommaCanoneTipoUsoSdRowMapper());

		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(
					"[RataSdDAOImpl::loadStatoDebitorioByIdRimborso] No record found in database for idStatoDebitorio "
							+ idStatoDebitorio,
					e);
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


	
	@Override
	public StatoDebitorioExtendedDTO findStatoDebitorioForM50ByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException, SystemException
	{
		LOGGER.debug("[StatoDebitorioDAOImpl::findStatoDebitorioForM50ByIdStatoDebitorio] BEGIN");
		LOGGER.debug("[StatoDebitorioDAOImpl::findStatoDebitorioForM50ByIdStatoDebitorio] idStatoDebitorio: "+idStatoDebitorio);
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();		
		
		sql.append("select rtsd.imp_spese_notifica ");
		sql.append("from risca_t_stato_debitorio rtsd  ");
		sql.append(" where rtsd.id_stato_debitorio = :id_stato_debitorio  ");

		paramMap.addValue("id_stato_debitorio", idStatoDebitorio);

		LOGGER.debug("[StatoDebitorioDAOImpl - findStatoDebitorioForM50ByIdStatoDebitorio] query =" + sql.toString());

		StatoDebitorioExtendedDTO statoDebitorioDto = null;
		try
		{
			statoDebitorioDto = template.queryForObject(sql.toString(), paramMap, new StatoDebitorioDaoRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOGGER.debug("\"[StatoDebitorioDAOImpl::findStatoDebitorioForM50ByIdStatoDebitorio]  NESSUN RISULTATO");
			throw new DAOException("Nessun dato in base alla richiesta");
		}
		catch (Throwable ex)
		{
			LOGGER.error("[StatoDebitorioDAOImpl::findStatoDebitorioForM50ByIdStatoDebitorio] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOGGER.debug("[StatoDebitorioDAOImpl::findStatoDebitorioForM50ByIdStatoDebitorio] END");
		}
		return statoDebitorioDto;
	}

	@Override
	public Integer countStatoDebitorioByIdRecapito(Long idRecapito) throws DAOException {		
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        MapSqlParameterSource paramST  = new MapSqlParameterSource();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRecapito", idRecapito);
			paramST = getParameterValue(map);
			return 	template.queryForObject(QUERY_COUNT_STATI_DEBITORI_BY_ID_RECAPITO  , paramST, Integer.class);
						
		}catch (DataAccessException e) {
				LOGGER.error("[StatoDebitorioDAOImpl::countStatoDebitorioByIdRecapito] ERROR : ",e);
				throw new DAOException(Constants.ERRORE_GENERICO);
			}
	}	

}