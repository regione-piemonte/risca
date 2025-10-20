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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.BilAccDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.CalcoloCanoneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DatiReportBilancioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.OutputColonnaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.OutputFileDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.OutputFoglioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ReportAmbienteDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoRicercaMorositaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoRicercaRimborsiDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.BilAccDTO;
import it.csi.risca.riscabesrv.dto.CalcoloCanoneDTO;
import it.csi.risca.riscabesrv.dto.ElaboraDTO;
import it.csi.risca.riscabesrv.dto.OutputColonnaDTO;
import it.csi.risca.riscabesrv.dto.OutputDatiDTO;
import it.csi.risca.riscabesrv.dto.OutputFileDTO;
import it.csi.risca.riscabesrv.dto.OutputFoglioDTO;
import it.csi.risca.riscabesrv.dto.ReportResultDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchResultDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.StatoElaborazioneDTO;
import it.csi.risca.riscabesrv.dto.TipoElaboraExtendedDTO;
import it.csi.risca.riscabesrv.util.Utils;
import it.csi.risca.riscabesrv.util.download.DownloadManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

public class ReportAmbienteDAOImpl extends RiscaBeSrvGenericDAO<OutputDatiDTO> implements ReportAmbienteDAO {

	private final String className = this.getClass().getSimpleName();

	private static final String STATO_ELAB_REPORT_ON = "REPORT_ON";
	private static final String STATO_ELAB_REPORT_OK = "REPORT_OK";
	private static final String STATO_ELAB_REPORT_KO = "REPORT_KO";

	private static final String TIPO_ELAB_RU = "RU";
	private static final String TIPO_ELAB_RM = "RM";
	private static final String TIPO_ELAB_RR = "RR";
	private static final String TIPO_ELAB_BI = "BI";
	private static final String CODICE_UTENZA = "codiceUtenza";
	private static final String CALCOLO_NON_CALCOLABILE = "Non calcolabile";
	private static final String TEMPLATE_DIR = "templates";

	private static final String COD_REPORT_ELEUTENZE = "ELEUTENZE";
	private static final String NOME_FILE_ELEUTENZE = "ReportRicercaAvanzata";

	private static final String NOME_FILE_ELENMOROSI = "ReportRicercaMorosita";
	private static final String COD_REPORT_ELENMOROSI = "ELENMOROSI";

	private static final String NOME_FILE_ELENRIMBORSI = "ReportRicercaRimborsi";
	private static final String COD_REPORT_ELENRIMBORSI = "ELENRIMBORSI";

	private static final String NOME_FILE_BILANCIO = "ReportBilancio";
	private static final String COD_REPORT_ACC_BILANCIO = "ACC_BILANCIO";

	private static final String COD_TIPO_PASSO_ELABORA_GENERA_REPORT = "GENERA_REPORT";
	private static final String FORMAT_DATE = "yyyyMMdd-HHmmss";
	private static final String SIMPLE_FORMAT_DATE = "yyyy-MM-dd";
	private static final String QUERY_LOAD_DATI_VIEW_RISCOSSIONE = " select * from v_riscossioni_report_ricerca where id_riscossione in (:listIdRiscossioni)";
	private static final String QUERY_LOAD_JSONDT_BY_ID_RISCOSSIONE = "\r\n"
			+ "with lus as (select dtj.id_riscossione,\r\n" + "                    dtj.uso, \r\n"
			+ "                    dtj.uso_di_legge,                   -- colonna40\r\n"
			+ "                    usp.uso_specifico,                  -- colonna41\r\n"
			+ "                    dtj.unita_di_misura,                -- colonna42\r\n"
			+ "                    dtj.qta_acqua,                      -- colonna43\r\n"
			+ "                    dtj.data_scadenza_emas_iso          -- colonna48\r\n"
			+ "               from (select id_riscossione, uso,\r\n"
			+ "                            json_dt->'riscossione'->'dati_tecnici'->'usi'->uso->>'uso_di_legge' as uso_di_legge ,\r\n"
			+ "                            --json_dt->'riscossione'->'dati_tecnici'->'usi'->uso->>'uso_specifico' as uso_specifico ,\r\n"
			+ "                            json_dt->'riscossione'->'dati_tecnici'->'usi'->uso->>'unita_di_misura' as unita_di_misura ,\r\n"
			+ "                            json_dt->'riscossione'->'dati_tecnici'->'usi'->uso->>'qta_acqua' as qta_acqua ,\r\n"
			+ "                            json_dt->'riscossione'->'dati_tecnici'->'usi'->uso->'riduzione' as riduzione , \r\n"
			+ "                            -- string_agg( replace (CAST(riduzione -> 'motivazione'as varchar), '\"',''), ', ') as motivazioni_concatenate\r\n"
			+ "                            json_dt->'riscossione'->'dati_tecnici'->'usi'->uso->>'data_scadenza_emas_iso' as data_scadenza_emas_iso                            \r\n"
			+ "                       FROM risca_t_riscossione,\r\n"
			+ "                    LATERAL jsonb_object_keys(json_dt->'riscossione'->'dati_tecnici'->'usi') AS uso(uso)\r\n"
			+ "                      --WHERE id_riscossione = 1862  --(3 usi)\r\n"
			+ "                      --WHERE id_riscossione = 19798  --(1 uso)\r\n" + "                   ) dtj\r\n"
			+ "                left join (-- recupera Uso/i specifico/i\r\n"
			+ "                           select valori.id_riscossione, valori.uso, string_agg(rdt.des_tipo_uso, '; ') as uso_specifico\r\n"
			+ "                             from ( select id_riscossione, uso,\r\n"
			+ "                                    replace(CAST(specifico as varchar), '\"','') as cod_tipo_uso \r\n"
			+ "                                      FROM risca_t_riscossione,\r\n"
			+ "                                   LATERAL jsonb_object_keys(json_dt->'riscossione'->'dati_tecnici'->'usi') AS uso(uso),\r\n"
			+ "                                   LATERAL jsonb_array_elements_text (json_dt->'riscossione'->'dati_tecnici'->'usi'->uso->'uso_specifico') as specifico--,\r\n"
			+ "                                     -- WHERE id_riscossione = 19798  --(1 uso)\r\n"
			+ "                          ) valori\r\n"
			+ "                        left join risca_d_tipo_uso rdt on valori.cod_tipo_uso = rdt.cod_tipo_uso\r\n"
			+ "                         group by valori.id_riscossione, valori.uso\r\n"
			+ "                          ) usp on dtj.id_riscossione = usp.id_riscossione and dtj.uso = usp.uso),\r\n"
			+ "     mot_aum as (select id_riscossione, uso, \r\n"
			+ "                    string_agg( replace (CAST(aumento -> 'motivazione'as varchar), '\"',''), ', ') as motivazioni_concatenate_aumen,\r\n"
			+ "                    string_agg( CAST(aumento -> 'perc_aumento_motiv'as varchar), ', ') as perc_aumento_motiv_aumen\r\n"
			+ "               FROM risca_t_riscossione,\r\n"
			+ "             LATERAL jsonb_object_keys(json_dt->'riscossione'->'dati_tecnici'->'usi') AS uso(uso),\r\n"
			+ "             LATERAL jsonb_array_elements(json_dt->'riscossione'->'dati_tecnici'->'usi'->uso->'aumento') as aumento\r\n"
			+ "                 --WHERE id_riscossione = 1862   --(3 usi)\r\n"
			+ "                 -- WHERE id_riscossione = 19798  --(1 uso)\r\n"
			+ "            group by id_riscossione, uso\r\n" + "            ),\r\n"
			+ "     mot_rid as (select id_riscossione, uso, \r\n"
			+ "                    string_agg( replace (CAST(riduzione -> 'motivazione'as varchar), '\"',''), ', ') as motivazioni_concatenate_riduz,\r\n"
			+ "                    string_agg( CAST(riduzione -> 'perc_riduzione_motiv'as varchar), ', ') as perc_riduzione_motiv_riduz\r\n"
			+ "               FROM risca_t_riscossione,\r\n"
			+ "             LATERAL jsonb_object_keys(json_dt->'riscossione'->'dati_tecnici'->'usi') AS uso(uso),\r\n"
			+ "             LATERAL jsonb_array_elements(json_dt->'riscossione'->'dati_tecnici'->'usi'->uso->'riduzione') as riduzione\r\n"
			+ "                 --WHERE id_riscossione = 1862   --(3 usi)\r\n"
			+ "                 --WHERE id_riscossione = 19798  --(1 uso)\r\n"
			+ "            group by id_riscossione, uso\r\n" + "            )        \r\n"
			+ "             select ROW_NUMBER() OVER () AS indice,\r\n" + "                    lus.id_riscossione,\r\n"
			+ "                    lus.uso, \r\n"
			+ "                    lus.uso_di_legge,                         -- colonna40\r\n"
			+ "                    lus.uso_specifico,                        -- colonna41\r\n"
			+ "                    lus.unita_di_misura,                      -- colonna42\r\n"
			+ "                    lus.qta_acqua,                            -- colonna43\r\n"
			+ "                    mot_aum.motivazioni_concatenate_aumen,    -- colonna44\r\n"
			+ "                    perc_aumento_motiv_aumen,                 -- colonna45\r\n"
			+ "                    mot_rid.motivazioni_concatenate_riduz,    -- colonna46\r\n"
			+ "                    perc_riduzione_motiv_riduz,               -- colonna47\r\n"
			+ "                    lus.data_scadenza_emas_iso                -- colonna48\r\n"
			+ "              from lus\r\n"
			+ "         left join mot_aum on lus.id_riscossione = mot_aum.id_riscossione and lus.uso = mot_aum.uso\r\n"
			+ "         left join mot_rid on lus.id_riscossione = mot_rid.id_riscossione and lus.uso = mot_rid.uso\r\n"
			+ "            WHERE lus.id_riscossione = :idRiscossione";

	@Autowired
	private DownloadManager downloadManager;

	@Autowired
	private TipoRicercaMorositaDAO tipoRicercaMorositaDAO;

	@Autowired
	private SoggettiDAO soggettiDAO;

	@Autowired
	private TipoRicercaRimborsiDAO tipoRicercaRimborsiDAO;

	@Autowired
	private CalcoloCanoneDAO calcoloCanoneDAO;

	@Autowired
	private OutputFileDAO outputFileDAO;

	@Autowired
	private OutputFoglioDAO outputFoglioDAO;

	@Autowired
	private OutputColonnaDAO outputColonnaDAO;

	@Autowired
	private RiscossioneDAO riscossioneDAO;

	@Autowired
	private ElaboraDAO elaboraDAO;

	@Autowired
	private DatiReportBilancioDAO datiReportBilancioDAO;

	@Autowired
	public BilAccDAO bilAccDAO;

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<OutputDatiDTO> getRowMapper() throws SQLException {
		return new OutputDatiRowMapper();
	}

	/**
	 * The type OutputDatiRowMapper row mapper.
	 */
	public static class OutputDatiRowMapper implements RowMapper<OutputDatiDTO> {

		/**
		 * Instantiates a new OutputDatiRowMapper row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public OutputDatiRowMapper() throws SQLException {
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

		private void populateBean(ResultSet rs, OutputDatiDTO outputDatiDto) throws SQLException {
			outputDatiDto.setIdRiscossione(rs.getLong("id_riscossione"));
			outputDatiDto.setProgressivo(0);
			outputDatiDto.setValoreColonna1(rs.getString("cod_riscossione"));
			outputDatiDto.setValoreColonna2(rs.getString("num_pratica"));
			outputDatiDto.setValoreColonna3(rs.getString("des_tipo_riscossione"));
			outputDatiDto.setValoreColonna4(rs.getString("des_stato_riscossione"));
			outputDatiDto.setValoreColonna5(formatDate(rs.getString("data_rinuncia_revoca")));
			outputDatiDto.setValoreColonna6(rs.getString("cod_tipo_soggetto"));
			outputDatiDto.setValoreColonna7(rs.getString("des_gruppo_soggetto"));
			outputDatiDto.setValoreColonna8(rs.getString("codice_fiscale"));
			outputDatiDto.setValoreColonna9(rs.getString("partita_iva"));
			outputDatiDto.setValoreColonna10(rs.getString("titolare"));
			outputDatiDto.setValoreColonna11(rs.getString("presso"));
			outputDatiDto.setValoreColonna12(rs.getString("via_principale"));
			outputDatiDto.setValoreColonna13(rs.getString("cap_recapito"));
			outputDatiDto.setValoreColonna14(rs.getString("citta"));
			outputDatiDto.setValoreColonna15(rs.getString("denom_nazione"));
			outputDatiDto.setValoreColonna16(rs.getString("tipo_invio"));
			outputDatiDto.setValoreColonna17(rs.getString("des_pec"));
			outputDatiDto.setValoreColonna18(rs.getString("des_email"));
			outputDatiDto.setValoreColonna19(rs.getString("presso_alternativo"));
			outputDatiDto.setValoreColonna20(rs.getString("via_alternativa"));
			outputDatiDto.setValoreColonna21(rs.getString("cap_recapito_alternativo"));
			outputDatiDto.setValoreColonna22(rs.getString("citta_indirizzo_alternativo"));
			outputDatiDto.setValoreColonna23(rs.getString("denom_nazione_alternativo"));

			outputDatiDto.setValoreColonna24(rs.getString("destipoinvio_alternativo"));
			outputDatiDto.setValoreColonna25(rs.getString("des_pec_alternativo"));
			outputDatiDto.setValoreColonna26(rs.getString("des_email_alternativo"));
			outputDatiDto.setValoreColonna27(rs.getString("istanza_e"));
			outputDatiDto.setValoreColonna28(formatDate(rs.getString("data_ultima_istanza")));

			outputDatiDto.setValoreColonna29(rs.getString("des_tipo_provvedimento"));
			outputDatiDto.setValoreColonna30(rs.getString("des_tipo_titolo"));
			outputDatiDto.setValoreColonna31(rs.getString("num_titolo"));
			outputDatiDto.setValoreColonna32(formatDate(rs.getString("data_titolo")));
			outputDatiDto.setValoreColonna33(formatDate(rs.getString("data_ini_concessione")));
			outputDatiDto.setValoreColonna34(formatDate(rs.getString("data_scad_concessione")));
			outputDatiDto.setValoreColonna35(formatDate(rs.getString("data_ini_sosp_canone")));
			outputDatiDto.setValoreColonna36(formatDate(rs.getString("data_fine_sosp_canone")));
			outputDatiDto.setValoreColonna37(rs.getString("corpo_idrico"));
			outputDatiDto.setValoreColonna38(rs.getString("corpo_captazione"));
			outputDatiDto.setValoreColonna39(rs.getString("nome_impianto"));
			outputDatiDto.setValoreColonna95(rs.getString("note_riscossione"));
			outputDatiDto.setValoreColonna96(rs.getString("note"));

		}

		/**
		 * Metodo per popolare ulteriori dati nell'oggetto OutputDatiDTO.
		 *
		 * @param rs            il ResultSet da cui ottenere i dati
		 * @param rowNum        il numero di riga corrente
		 * @param outputDatiDto l'oggetto OutputDatiDTO da popolare
		 * @throws SQLException in caso di eccezione SQL
		 */
		public void populateAdditionalData(ResultSet rs, int rowNum, OutputDatiDTO outputDatiDto) throws SQLException {

			if (rowNum == 0) {
				outputDatiDto.setValoreColonna40(rs.getString("uso_di_legge"));
				outputDatiDto.setValoreColonna41(rs.getString("uso_specifico"));
				outputDatiDto.setValoreColonna42(rs.getString("unita_di_misura"));
				outputDatiDto.setValoreColonna43(rs.getString("qta_acqua"));
				outputDatiDto.setValoreColonna44(rs.getString("motivazioni_concatenate_aumen"));
				outputDatiDto.setValoreColonna45(rs.getString("perc_aumento_motiv_aumen"));
				outputDatiDto.setValoreColonna46(rs.getString("motivazioni_concatenate_riduz"));
				outputDatiDto.setValoreColonna47(rs.getString("perc_riduzione_motiv_riduz"));
				outputDatiDto.setValoreColonna48(formatDate(rs.getString("data_scadenza_emas_iso")));
			}

			if (rowNum == 1) {
				outputDatiDto.setValoreColonna49(rs.getString("uso_di_legge"));
				outputDatiDto.setValoreColonna50(rs.getString("uso_specifico"));
				outputDatiDto.setValoreColonna51(rs.getString("unita_di_misura"));
				outputDatiDto.setValoreColonna52(rs.getString("qta_acqua"));
				outputDatiDto.setValoreColonna53(rs.getString("motivazioni_concatenate_aumen"));
				outputDatiDto.setValoreColonna54(rs.getString("perc_aumento_motiv_aumen"));
				outputDatiDto.setValoreColonna55(rs.getString("motivazioni_concatenate_riduz"));
				outputDatiDto.setValoreColonna56(rs.getString("perc_riduzione_motiv_riduz"));
				outputDatiDto.setValoreColonna57(formatDate(rs.getString("data_scadenza_emas_iso")));
			}
			if (rowNum == 2) {
				outputDatiDto.setValoreColonna58(rs.getString("uso_di_legge"));
				outputDatiDto.setValoreColonna59(rs.getString("uso_specifico"));
				outputDatiDto.setValoreColonna60(rs.getString("unita_di_misura"));
				outputDatiDto.setValoreColonna61(rs.getString("qta_acqua"));
				outputDatiDto.setValoreColonna62(rs.getString("motivazioni_concatenate_aumen"));
				outputDatiDto.setValoreColonna63(rs.getString("perc_aumento_motiv_aumen"));
				outputDatiDto.setValoreColonna64(rs.getString("motivazioni_concatenate_riduz"));
				outputDatiDto.setValoreColonna65(rs.getString("perc_riduzione_motiv_riduz"));
				outputDatiDto.setValoreColonna66(formatDate(rs.getString("data_scadenza_emas_iso")));
				;
			}
			if (rowNum == 3) {
				outputDatiDto.setValoreColonna67(rs.getString("uso_di_legge"));
				outputDatiDto.setValoreColonna68(rs.getString("uso_specifico"));
				outputDatiDto.setValoreColonna69(rs.getString("unita_di_misura"));
				outputDatiDto.setValoreColonna70(rs.getString("qta_acqua"));
				outputDatiDto.setValoreColonna71(rs.getString("motivazioni_concatenate_aumen"));
				outputDatiDto.setValoreColonna72(rs.getString("perc_aumento_motiv_aumen"));
				outputDatiDto.setValoreColonna73(rs.getString("motivazioni_concatenate_riduz"));
				outputDatiDto.setValoreColonna74(rs.getString("perc_riduzione_motiv_riduz"));
				outputDatiDto.setValoreColonna75(formatDate(rs.getString("data_scadenza_emas_iso")));
			}
			if (rowNum == 4) {
				outputDatiDto.setValoreColonna76(rs.getString("uso_di_legge"));
				outputDatiDto.setValoreColonna77(rs.getString("uso_specifico"));
				outputDatiDto.setValoreColonna78(rs.getString("unita_di_misura"));
				outputDatiDto.setValoreColonna79(rs.getString("qta_acqua"));
				outputDatiDto.setValoreColonna80(rs.getString("motivazioni_concatenate_aumen"));
				outputDatiDto.setValoreColonna81(rs.getString("perc_aumento_motiv_aumen"));
				outputDatiDto.setValoreColonna82(rs.getString("motivazioni_concatenate_riduz"));
				outputDatiDto.setValoreColonna83(rs.getString("perc_riduzione_motiv_riduz"));
				outputDatiDto.setValoreColonna84(formatDate(rs.getString("data_scadenza_emas_iso")));
			}
			if (rowNum == 5) {
				outputDatiDto.setValoreColonna85(rs.getString("uso_di_legge"));
				outputDatiDto.setValoreColonna86(rs.getString("uso_specifico"));
				outputDatiDto.setValoreColonna87(rs.getString("unita_di_misura"));
				outputDatiDto.setValoreColonna88(rs.getString("qta_acqua"));
				outputDatiDto.setValoreColonna89(rs.getString("motivazioni_concatenate_aumen"));
				outputDatiDto.setValoreColonna90(rs.getString("perc_aumento_motiv_aumen"));
				outputDatiDto.setValoreColonna91(rs.getString("motivazioni_concatenate_riduz"));
				outputDatiDto.setValoreColonna92(rs.getString("perc_riduzione_motiv_riduz"));
				outputDatiDto.setValoreColonna93(formatDate(rs.getString("data_scadenza_emas_iso")));
			}
		}

	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportResultDTO creaReportRicercaAvanzata(ElaboraDTO elabora, RiscossioneSearchDTO riscossioneSearch,
			AmbitoDTO ambito, String modalitaRicerca, String attore) throws BusinessException, Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		ReportResultDTO reportResult = new ReportResultDTO();

		try {
			List<RiscossioneSearchResultDTO> riscossioneSearchResult = riscossioneDAO.searchRiscossioni(
					riscossioneSearch, ambito.getIdAmbito(), modalitaRicerca, null, null, CODICE_UTENZA);

			if (riscossioneSearchResult.isEmpty()) {
				return null;
			}

			elabora = insertElabora(ambito.getIdAmbito(), attore, TIPO_ELAB_RU, STATO_ELAB_REPORT_ON);

			OutputFileDTO outFile = outputFileDAO.loadOutputFileByAmbitoTipoElabNomeFile(ambito.getCodAmbito(),
					elabora.getTipoElabora().getCodTipoElabora(), NOME_FILE_ELEUTENZE);
			List<OutputFoglioDTO> outFogli = outputFoglioDAO.loadOutputFoglioByFile(outFile.getIdOutputFile());

			List<Long> listIdRiscossioni = riscossioneSearchResult.stream()
					.map(RiscossioneSearchResultDTO::getIdRiscossione).collect(Collectors.toList());

			Map<String, Object> map = new HashMap<>();
			map.put("listIdRiscossioni", listIdRiscossioni);
			MapSqlParameterSource params = getParameterValue(map);

			List<OutputDatiDTO> listOutputDatiDto = template.query(QUERY_LOAD_DATI_VIEW_RISCOSSIONE, params,
					getRowMapper());
			for (OutputDatiDTO outputDatiDto : listOutputDatiDto) {
				populateOutputDatiDto(outputDatiDto, outFogli, elabora);
				populateImportoCanone(outputDatiDto);
				outputDatiDto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_GENERA_REPORT);
			}

			for (OutputFoglioDTO foglio : outFogli) {
				populateFoglio(foglio, listOutputDatiDto);
			}

			outFile.setFogli(outFogli);

			String relativeFilePath = createReport(elabora, ambito.getCodAmbito(), outFile, attore,
					COD_REPORT_ELEUTENZE, true);

			if (relativeFilePath != null) {
				String reportUrl = downloadManager.copyFileToDownloadArea(
						elabora.getIdElabora() + "_" + elabora.getTipoElabora().getCodTipoElabora(), relativeFilePath);
				reportResult.setReportUrl(reportUrl);
				updateElabora(elabora, STATO_ELAB_REPORT_OK, relativeFilePath);
				reportResult.setElabora(elabora);
			}

			return reportResult;
		} catch (BusinessException e) {
			handleException(elabora, e);
			throw e;
		} catch (Exception e) {
			handleException(elabora, e);
			throw e;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	private void handleException(ElaboraDTO elabora, Exception exception) {
		LOGGER.error(getClassFunctionErrorInfo(className, Thread.currentThread().getStackTrace()[1].getMethodName(),
				exception), exception);
		updateElabora(elabora, STATO_ELAB_REPORT_OK, null);
	}

	private void populateOutputDatiDto(OutputDatiDTO outputDatiDto, List<OutputFoglioDTO> outFogli, ElaboraDTO elabora)
			throws SQLException {
		int riga = outputDatiDto.getProgressivo() + 1;
		outputDatiDto.setProgressivo(riga);
		outputDatiDto.setIdElabora(elabora.getIdElabora());
		outputDatiDto.setIdOutputFoglio(outFogli.get(0).getIdOutputFoglio());

		Map<String, Object> map = new HashMap<>();
		map.put("idRiscossione", outputDatiDto.getIdRiscossione());
		MapSqlParameterSource params = getParameterValue(map);

		OutputDatiRowMapper rowMapper = new OutputDatiRowMapper();
		List<OutputDatiDTO> nuovoOutputDatiDtoList = template.query(QUERY_LOAD_JSONDT_BY_ID_RISCOSSIONE, params,
				(rs, rowNum) -> {
					OutputDatiDTO nuovoOutputDatiDto = new OutputDatiDTO();
					rowMapper.populateAdditionalData(rs, rowNum, nuovoOutputDatiDto);
					return nuovoOutputDatiDto;
				});

		if (!nuovoOutputDatiDtoList.isEmpty()) {
			for (OutputDatiDTO nuovoOutputDati : nuovoOutputDatiDtoList) {
				Utils.copyNonNullValues(nuovoOutputDati, outputDatiDto);
			}

		}
	}

	private void populateImportoCanone(OutputDatiDTO outputDatiDto) {
		String dataRiferimento = Utils.getCurrentDateFormatted(SIMPLE_FORMAT_DATE);
		CalcoloCanoneDTO canone = null;
		try {
			canone = calcoloCanoneDAO.calcoloCanone(outputDatiDto.getIdRiscossione(), dataRiferimento);
		} catch (BusinessException e) {
			LOGGER.debug(
					getClassFunctionDebugString(className, Thread.currentThread().getStackTrace()[1].getMethodName(),
							CALCOLO_NON_CALCOLABILE + " COD_UTENZA: " + outputDatiDto.getValoreColonna1()));
		} catch (Exception e) {
			LOGGER.debug(
					getClassFunctionDebugString(className, Thread.currentThread().getStackTrace()[1].getMethodName(),
							"Errore calcolo canone per COD_UTENZA: " + outputDatiDto.getValoreColonna1()));

		}
		outputDatiDto
				.setValoreColonna94(formatBigDecimal(canone != null ? canone.getCalcoloCanone() : BigDecimal.ZERO));
	}

	private void populateFoglio(OutputFoglioDTO foglio, List<OutputDatiDTO> listOutputDatiDto) throws Exception {
		List<OutputColonnaDTO> outColonne = outputColonnaDAO.loadOutputColonnaByFoglio(foglio.getIdOutputFoglio());
		foglio.setColonne(outColonne);
		foglio.setDati(listOutputDatiDto);
	}

	private String createReport(ElaboraDTO elabora, String codAmbito, OutputFileDTO outFile, String attore,
			String codReport, boolean sort) throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
		String formattedDate = dateFormat.format(currentDate);
		String fileNameODS = attore + "_" + outFile.getCodReport() + "_" + elabora.getIdElabora() + "_" + formattedDate
				+ ".xls";

		String pathRelativoODS = File.separator + codAmbito + File.separator
				+ elabora.getTipoElabora().getCodTipoElabora() + File.separator + fileNameODS;
		Path p = Paths.get(downloadManager.getDownloadPath());
		Path parent = p.getParent();

		String destFileNameOds = parent.toString() + pathRelativoODS;
		String outDirStr = parent.toString() + File.separator + codAmbito + File.separator
				+ elabora.getTipoElabora().getCodTipoElabora();
		try {
			File outDir = new File(outDirStr);
			if (!outDir.exists()) {
				outDir.mkdirs();
				Utils.setFilePermissions(outDirStr);
			}
			// Aggiungo alla lista dei dati un record per le intestazioni

			List<OutputFoglioDTO> fogli = outFile.getFogli();
			List<String> sheetsNames = new ArrayList<String>();
			LOGGER.debug(getClassFunctionDebugString(className, "createReport", "sorting data"));
			for (OutputFoglioDTO foglio : fogli) {
				List<OutputColonnaDTO> colonne = foglio.getColonne();
				List<OutputDatiDTO> dati = foglio.getDati();
				if (sort) {
					dati = dati.stream().sorted(Comparator.comparing(OutputDatiDTO::getValoreColonna1))
							.collect(Collectors.toList());
				}

				OutputDatiDTO intestazione = getRigaIntestazione(colonne);
				dati.add(0, intestazione);
				foglio.setDati(dati);
				sheetsNames.add(foglio.getNomeFoglio());
			}
			LOGGER.debug(getClassFunctionDebugString(className, "createReport", "data sorted"));
			ArrayList<JasperPrint> sheets = new ArrayList<JasperPrint>();
			String[] templateFogli = getTemplates(elabora.getTipoElabora().getCodTipoElabora());
			for (int i = 0; i < templateFogli.length; i++) {
				String templateName = TEMPLATE_DIR + "/" + templateFogli[i] + ".jrxml";

				InputStream template = new ClassPathResource(templateName).getInputStream();
				BufferedInputStream bis = new BufferedInputStream(template);
				JasperDesign design = JRXmlLoader.load(bis);
				LOGGER.debug(getClassFunctionDebugString(className, "createReport", "compileReport"));
				JasperReport reportFoglio = JasperCompileManager.compileReport(design);
				LOGGER.debug(getClassFunctionDebugString(className, "createReport", "report compiled"));

				Map<String, Object> parameters = new HashMap<>();
				parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
				Locale locale = new Locale("it", "IT");
				parameters.put(JRParameter.REPORT_LOCALE, locale);

				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
						outFile.getFogli().get(i).getDati());

				JasperPrint sheet = JasperFillManager.fillReport(reportFoglio, parameters, dataSource);
				sheet.setName(sheetsNames.get(i));
				sheets.add(sheet);
			}

			exportXls(sheets, sheetsNames, destFileNameOds);

		} catch (Exception e) {
			LOGGER.error("[ReportApiServiceImpl] Errore create Report.", e);
			throw e;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			LOGGER.error("[ReportApiServiceImpl] OutOfMemoryError create Report.", e);
			throw e;
		}
		return pathRelativoODS;
	}

	private void exportXls(ArrayList<JasperPrint> sheets, List<String> sheetsNames, String destFileNameXls)
			throws FileNotFoundException, JRException, IOException {

		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		configuration.setOnePagePerSheet(true);
		configuration.setDetectCellType(true); // Detect cell types (date and etc.)
		configuration.setWhitePageBackground(false); // No white background!
		configuration.setFontSizeFixEnabled(false);

		// No spaces between rows and columns
		configuration.setRemoveEmptySpaceBetweenRows(true);
		configuration.setRemoveEmptySpaceBetweenColumns(true);

		configuration.setSheetNames(sheetsNames.toArray(new String[sheetsNames.size()]));

		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setConfiguration(configuration);
		exporter.setExporterInput(SimpleExporterInput.getInstance(sheets));

		ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
		FileOutputStream fos = new FileOutputStream(destFileNameXls);
		OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(excelStream);
		exporter.setExporterOutput(exporterOutput);
		LOGGER.debug(getClassFunctionDebugString(className, "createReport", "exporting report"));
		exporter.exportReport();
		LOGGER.debug(getClassFunctionDebugString(className, "createReport", "report exported"));
		excelStream.writeTo(fos);
		Utils.setFilePermissions(destFileNameXls);
	}

	private OutputDatiDTO getRigaIntestazione(List<OutputColonnaDTO> colonne) throws Exception {
		OutputDatiDTO intestazione = new OutputDatiDTO();

		for (OutputColonnaDTO colonna : colonne) {
			String methodName = "setValoreColonna" + colonna.getProgressivo();
			Method method = intestazione.getClass().getMethod(methodName, String.class);
			method.invoke(intestazione, colonna.getDescEtichetta());
		}

		return intestazione;
	}

	private ElaboraDTO insertElabora(Long idAmbito, String attore, String codTipoElabora, String statoElabora)
			throws Exception {
		ElaboraDTO elaboraDto = new ElaboraDTO();
		try {
			AmbitoDTO ambito = new AmbitoDTO();
			ambito.setIdAmbito(idAmbito);
			elaboraDto.setAmbito(ambito);
			elaboraDto.setDataRichiesta(new Date());
			elaboraDto.setGestAttoreIns(attore);
			elaboraDto.setGestAttoreUpd(attore);
			StatoElaborazioneDTO statoElab = new StatoElaborazioneDTO();
			statoElab.setCodStatoElabora(statoElabora);
			elaboraDto.setStatoElabora(statoElab);
			TipoElaboraExtendedDTO tipoElab = new TipoElaboraExtendedDTO();
			tipoElab.setCodTipoElabora(codTipoElabora);
			elaboraDto.setTipoElabora(tipoElab);
			// Questa elaborazione non ha parametri ma occorre settare comunque un array
			// vuoto
			elaboraDto.setParametri(new ArrayList<>());
			elaboraDto = elaboraDAO.saveElabora(elaboraDto);
			elaboraDto = elaboraDAO.loadElaboraById(elaboraDto.getIdElabora(), null);

		} catch (Exception e) {
			LOGGER.error("[ReportApiServiceImpl] Errore inserimento elaborazione.", e);
			throw e;
		}
		return elaboraDto;
	}

	private ElaboraDTO updateElabora(ElaboraDTO elabora, String statoElabora, String nomeFile) {

		try {
			StatoElaborazioneDTO statoElab = new StatoElaborazioneDTO();
			statoElab.setCodStatoElabora(statoElabora);
			elabora.setStatoElabora(statoElab);
			elabora.setNomeFileGenerato(nomeFile);
			elaboraDAO.updateElabora(elabora);

		} catch (Exception e) {
			LOGGER.error("[ReportApiServiceImpl] Errore durante l aggiornamento elaborazione.", e);
		}
		return elabora;
	}

	@Override
	public ReportResultDTO creaReportRicercaMorosita(ElaboraDTO elabora, String tipoRicercaMorosita, Integer anno,
			Integer flgRest, Integer flgAnn, String lim, AmbitoDTO ambito, String attore)
			throws BusinessException, Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		ReportResultDTO reportResult = new ReportResultDTO();
		try {
			// recuperare dati ricerca morosita
			List<StatoDebitorioExtendedDTO> listMorosita = tipoRicercaMorositaDAO.ricercaMorosita(tipoRicercaMorosita,
					ambito.getIdAmbito(), anno, flgRest, flgAnn, lim, null, null, null);
			// Creazione nuova elaboraa
			elabora = insertElabora(ambito.getIdAmbito(), attore, TIPO_ELAB_RM, STATO_ELAB_REPORT_ON);

			// Caricamento dati file
			OutputFileDTO outFile = outputFileDAO.loadOutputFileByAmbitoTipoElabNomeFile(ambito.getCodAmbito(),
					elabora.getTipoElabora().getCodTipoElabora(), NOME_FILE_ELENMOROSI);
			// Caricamento dati foglio

			List<OutputFoglioDTO> outFogli = outputFoglioDAO.loadOutputFoglioByFile(outFile.getIdOutputFile());

			// Popolamento output_dati
			int riga = 0;
			List<OutputDatiDTO> listOutputDati = new ArrayList<>();
			for (StatoDebitorioExtendedDTO morosita : listMorosita) {
				SoggettiExtendedDTO soggetto = soggettiDAO.getSoggettoById(morosita.getIdSoggetto());
				OutputDatiDTO outputDatiDto = new OutputDatiDTO();
				outputDatiDto.setProgressivo(++riga);
				outputDatiDto.setIdElabora(elabora.getIdElabora());
				outputDatiDto.setIdOutputFoglio(outFogli.get(0).getIdOutputFoglio());
				outputDatiDto.setValoreColonna1(morosita.getCodUtenza());
				outputDatiDto.setValoreColonna2(morosita.getNap());
				outputDatiDto.setValoreColonna3(soggetto.getCfSoggetto());
				if (soggetto.getTipoSoggetto().getCodTipoSoggetto().equals("PF"))
					outputDatiDto.setValoreColonna4(soggetto.getCognome() + " " + soggetto.getNome());
				else
					outputDatiDto.setValoreColonna4(soggetto.getDenSoggetto());

				outputDatiDto.setValoreColonna5(formatBigDecimal(morosita.getImportoDovuto()));

				outputDatiDto.setValoreColonna6(formatDate(morosita.getAccDataScadenzaPag()));
				outputDatiDto.setValoreColonna7(formatBigDecimal(morosita.getIntMaturatiSpeseNotifica()));
				outputDatiDto.setValoreColonna8(formatBigDecimal(morosita.getAccImportoVersato()));
				outputDatiDto.setValoreColonna9(formatBigDecimal(morosita.getAccImportoDiCanoneMancante()));
				outputDatiDto.setValoreColonna10(formatBigDecimal(morosita.getAccInteressiMancanti()));
				outputDatiDto.setValoreColonna11(formatBigDecimal(morosita.getAccInteressiVersati()));
				outputDatiDto.setValoreColonna12(formatBigDecimal(morosita.getAccImportoRimborsato()));
				outputDatiDto.setValoreColonna13(formatDate(morosita.getDataPagamento()));
				outputDatiDto.setValoreColonna14(morosita.getStatoRiscossione());
				
				outputDatiDto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_GENERA_REPORT);
				listOutputDati.add(outputDatiDto);
			}

			for (OutputFoglioDTO foglio : outFogli) {
				populateFoglio(foglio, listOutputDati);
			}

			outFile.setFogli(outFogli);

			String relativeFilePath = createReport(elabora, ambito.getCodAmbito(), outFile, attore,
					COD_REPORT_ELENMOROSI, true);
			if (relativeFilePath != null) {
				String reportUrl = downloadManager.copyFileToDownloadArea(
						elabora.getIdElabora() + "_" + elabora.getTipoElabora().getCodTipoElabora(), relativeFilePath);

				reportResult.setReportUrl(reportUrl);
				// UPDATE STATO
				updateElabora(elabora, STATO_ELAB_REPORT_OK, relativeFilePath);
				reportResult.setElabora(elabora);
			}

			return reportResult;

		} catch (BusinessException e) {
			updateElabora(elabora, STATO_ELAB_REPORT_KO, null);
			throw e;

		} catch (SQLException e) {
			updateElabora(elabora, STATO_ELAB_REPORT_KO, null);
			throw e;

		} catch (Exception e) {
			updateElabora(elabora, STATO_ELAB_REPORT_KO, null);
			throw e;

		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));

		}
	}

	public static String formatBigDecimal(BigDecimal value) {
		if (value == null)
			value = BigDecimal.ZERO;
		Locale currentLocale = Locale.getDefault();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(currentLocale);
		symbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.##", symbols);
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setGroupingUsed(false);
		return df.format(value);
	}

	public static String formatDate(String dateStr) {
		if (dateStr == null || "".equals(dateStr))
			return null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = df.parse(dateStr);
			SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
			return df2.format(d);
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public ReportResultDTO creaReportRicercaRimborsi(ElaboraDTO elabora, String tipoRicercaRimborsi, Integer anno,
			AmbitoDTO ambito, String attore) throws BusinessException, Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		ReportResultDTO reportResult = new ReportResultDTO();

		try {
			// recuperare dati ricerca Rimborsi
			List<StatoDebitorioExtendedDTO> listRimborsi = tipoRicercaRimborsiDAO.ricercaRimborsi(tipoRicercaRimborsi,
					ambito.getIdAmbito(), anno, null, null, null);
			// Creazione nuova elabora
			elabora = insertElabora(ambito.getIdAmbito(), attore, TIPO_ELAB_RR, STATO_ELAB_REPORT_ON);

			// Caricamento dati file
			OutputFileDTO outFile = outputFileDAO.loadOutputFileByAmbitoTipoElabNomeFile(ambito.getCodAmbito(),
					elabora.getTipoElabora().getCodTipoElabora(), NOME_FILE_ELENRIMBORSI);
			// Caricamento dati foglio
			List<OutputFoglioDTO> outFogli = outputFoglioDAO.loadOutputFoglioByFile(outFile.getIdOutputFile());

			// Popolamento output_dati
			int riga = 0;
			List<OutputDatiDTO> listOutputDati = new ArrayList<>();
			for (StatoDebitorioExtendedDTO sd : listRimborsi) {
				SoggettiExtendedDTO soggetto = soggettiDAO.getSoggettoById(sd.getIdSoggetto());
				OutputDatiDTO outputDatiDto = new OutputDatiDTO();
				outputDatiDto.setProgressivo(++riga);
				outputDatiDto.setIdElabora(elabora.getIdElabora());
				outputDatiDto.setIdOutputFoglio(outFogli.get(0).getIdOutputFoglio());
				outputDatiDto.setValoreColonna1(sd.getCodUtenza());
				outputDatiDto.setValoreColonna2(sd.getNap());
				outputDatiDto.setValoreColonna3(soggetto.getCfSoggetto());
				if (soggetto.getTipoSoggetto().getCodTipoSoggetto().equals("PF"))
					outputDatiDto.setValoreColonna4(soggetto.getCognome() + " " + soggetto.getNome());
				else
					outputDatiDto.setValoreColonna4(soggetto.getDenSoggetto());

				outputDatiDto.setValoreColonna5(formatBigDecimal(sd.getImportoDovuto()));
				outputDatiDto.setValoreColonna6(formatDate(sd.getAccDataScadenzaPag()));
				outputDatiDto.setValoreColonna7(formatBigDecimal(sd.getIntMaturatiSpeseNotifica()));

				outputDatiDto.setValoreColonna8(formatBigDecimal(sd.getAccImportoVersato()));
				outputDatiDto.setValoreColonna9(formatBigDecimal(sd.getImportoEccedente()));
				outputDatiDto.setValoreColonna10(formatBigDecimal(sd.getImpCompensazioneCanone()));
				outputDatiDto.setValoreColonna11(formatDate(sd.getDataPagamento()));

				outputDatiDto.setValoreColonna12(sd.getStatoRiscossione() != null ? sd.getStatoRiscossione() : null);
				outputDatiDto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_GENERA_REPORT);
				listOutputDati.add(outputDatiDto);
			}

			for (OutputFoglioDTO foglio : outFogli) {
				populateFoglio(foglio, listOutputDati);
			}

			outFile.setFogli(outFogli);

			String relativeFilePath = createReport(elabora, ambito.getCodAmbito(), outFile, attore,
					COD_REPORT_ELENRIMBORSI, true);
			if (relativeFilePath != null) {
				String reportUrl = downloadManager.copyFileToDownloadArea(
						elabora.getIdElabora() + "_" + elabora.getTipoElabora().getCodTipoElabora(), relativeFilePath);
				reportResult.setReportUrl(reportUrl);
				// UPDATE STATO
				updateElabora(elabora, STATO_ELAB_REPORT_OK, relativeFilePath);
				reportResult.setElabora(elabora);
			}

			return reportResult;
		} catch (BusinessException e) {
			handleException(elabora, e);
			throw e;

		} catch (SQLException e) {
			handleException(elabora, e);
			throw new Exception(e);

		} catch (Exception e) {
			handleException(elabora, e);
			throw e;

		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));

		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

	public ReportResultDTO creaReportBilancio(Integer anno, AmbitoDTO ambito, String attore)
			throws BusinessException, Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		ReportResultDTO reportResult = new ReportResultDTO();
		ElaboraDTO elabora = null;
		try {
			// Creazione nuova elabora
			elabora = insertElabora(ambito.getIdAmbito(), attore, TIPO_ELAB_BI, STATO_ELAB_REPORT_ON);

			// Caricamento dati file
			OutputFileDTO outFile = outputFileDAO.loadOutputFileByAmbitoTipoElabNomeFile(ambito.getCodAmbito(),
					elabora.getTipoElabora().getCodTipoElabora(), NOME_FILE_BILANCIO);
			// Caricamento dati foglio
			List<OutputFoglioDTO> outFogli = outputFoglioDAO.loadOutputFoglioByFile(outFile.getIdOutputFile());

			// Popolamento output_dati per foglio 1 - accerta bilancio
			List<OutputDatiDTO> listDatiFoglioAccertaBil = new ArrayList<OutputDatiDTO>();

			// 1) Visualizzare i dati relativi ai risultati della query SOTTO-QUERY
			// (CANONI_E_MONETIZZAZIONI) Per ciascun elemento estratto, valorizzare le
			// colonne del report
			List<OutputDatiDTO> listDatiCanoniMonetizzazioni = datiReportBilancioDAO.getCanoniEMonetizzazioni(anno);
			listDatiFoglioAccertaBil.addAll(listDatiCanoniMonetizzazioni);

			BigDecimal ruoloCanoni = listDatiCanoniMonetizzazioni.stream()
					.map(row -> new BigDecimal(row.getValoreColonna3())).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal tesCanoni = listDatiCanoniMonetizzazioni.stream()
					.map(row -> new BigDecimal(row.getValoreColonna4())).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal totCanoni = listDatiCanoniMonetizzazioni.stream()
					.map(row -> new BigDecimal(row.getValoreColonna5())).reduce(BigDecimal.ZERO, BigDecimal::add);

			// 2) Richiamare la query SOTTO-QUERY (ECCEDENZE) e, con i dati estratti,
			// visualizzare una riga tante righe quanti sono i risultati
			List<OutputDatiDTO> listDatiEccedenze = datiReportBilancioDAO.getEccedenze(anno);
			for (OutputDatiDTO dto : listDatiEccedenze) {
				dto.setValoreColonna2("Eccedenze " + dto.getValoreColonna2());
			}
			listDatiFoglioAccertaBil.addAll(listDatiEccedenze);
			
			BigDecimal totEccedenze = listDatiEccedenze.stream().map(row -> new BigDecimal(row.getValoreColonna5()))
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			// 3) Richiamare la query SOTTO-QUERY (NON_IDENTIFICATI_E_DA_ASSEGNARE) e,
			// con i dati estratti, visualizzare UNA riga aggiungendola alla
			// listDatiFoglioAccertaBil
			List<OutputDatiDTO> listDatiNIDA = datiReportBilancioDAO.getNonIdentificatiDaAssegnare(anno);
			listDatiFoglioAccertaBil.addAll(listDatiNIDA);

			BigDecimal tesNIDA = listDatiNIDA.stream().map(row -> new BigDecimal(row.getValoreColonna4()))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal totNIDA = listDatiNIDA.stream().map(row -> new BigDecimal(row.getValoreColonna5()))
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal totTes = tesCanoni.add(totNIDA);
			BigDecimal totGenCanoni = totCanoni.add(totEccedenze).add(totNIDA);

			// 4) Visualizzare una riga valorizzandone le colonne nel seguente modo
			OutputDatiDTO dto = new OutputDatiDTO();
			dto.setValoreColonna2("CANONI E MONETIZZAZIONE");
			dto.setValoreColonna3("" + ruoloCanoni);
			dto.setValoreColonna4("" + totTes);
			dto.setValoreColonna6("" + totGenCanoni);
			listDatiFoglioAccertaBil.add(dto);

			// 5) Visualizzare i dati relativi ai risultati della query SOTTO-QUERY
			// (INTERESSI) Per ciascun elemento estratto, visualizzare le colonne del report
			List<OutputDatiDTO> listDatiInteressi = datiReportBilancioDAO.getInteressi(anno);
			listDatiFoglioAccertaBil.addAll(listDatiInteressi);

			BigDecimal ruoloInteressi = listDatiInteressi.stream().map(row -> new BigDecimal(row.getValoreColonna3()))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal tesInteressi = listDatiInteressi.stream().map(row -> new BigDecimal(row.getValoreColonna4()))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal totInteressi = listDatiInteressi.stream().map(row -> new BigDecimal(row.getValoreColonna5()))
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			// 6) Visualizzare una riga valorizzandone le colonne nel seguente modo
			dto = new OutputDatiDTO();
			dto.setValoreColonna2("INTERESSI");
			dto.setValoreColonna3("" + ruoloInteressi);
			dto.setValoreColonna4("" + tesInteressi);
			dto.setValoreColonna6("" + totInteressi);
			listDatiFoglioAccertaBil.add(dto);
			BigDecimal totGenInteressi = totInteressi;

			// 7) Visualizzare i dati relativi ai risultati della query SOTTO-QUERY
			// (SPESE_DI_NOTIFICA). Per ciascun elemento estratto, visualizzare le colonne
			// del report
			List<OutputDatiDTO> listDatiSpeseNotifica = datiReportBilancioDAO.getSpeseNotifica(anno);
			listDatiFoglioAccertaBil.addAll(listDatiSpeseNotifica);

			BigDecimal ruoloNotifica = listDatiSpeseNotifica.stream()
					.map(row -> new BigDecimal(row.getValoreColonna3())).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal tesNotifica = listDatiSpeseNotifica.stream().map(row -> new BigDecimal(row.getValoreColonna4()))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal totNotifica = listDatiSpeseNotifica.stream().map(row -> new BigDecimal(row.getValoreColonna5()))
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			// 8) Visualizzare quindi una riga valorizzandone le colonne nel seguente modo:
			dto = new OutputDatiDTO();
			dto.setValoreColonna2("SPESE DI NOTIFICA");
			dto.setValoreColonna3("" + ruoloNotifica);
			dto.setValoreColonna4("" + tesNotifica);
			dto.setValoreColonna6("" + totNotifica);
			listDatiFoglioAccertaBil.add(dto);
			BigDecimal totGenNotifica = totNotifica;

			// 9) Visualizzare i dati relativi ai risultati della query
			// QUERY_PAGAMENTI-NON-DI-COMPETENZA. Per ciascun elemento estratto,
			// visualizzare:
			List<OutputDatiDTO> listDatiPagNonCompet = datiReportBilancioDAO.getPagamentiNonDiCompetenza(anno);
			listDatiFoglioAccertaBil.addAll(listDatiPagNonCompet);
			BigDecimal totGenNonComp = listDatiPagNonCompet.stream().map(row -> new BigDecimal(row.getValoreColonna6()))
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			// 10) Visualizzare i dati relativi ai risultati della query
			// QUERY_PAGAMENTI-DA-RIMBORSARE. Per ciascun elemento estratto,
			// visualizzare:
			// List<OutputDatiDTO> listDatiPagDaRimb = datiReportBilancioDAO.getPagamentiDaRimborsare(anno);
			// listDatiFoglioAccertaBil.addAll(listDatiPagDaRimb);
			// BigDecimal totGenRimb = listDatiPagDaRimb.stream().map(row -> new BigDecimal(row.getValoreColonna6()))
			//		.reduce(BigDecimal.ZERO, BigDecimal::add);

			// 11) Visualizzare, infine, una riga, valorizzandone le colonne nel seguente
			// modo
			dto = new OutputDatiDTO();
			dto.setValoreColonna2("TOTALE GENERALE");
			BigDecimal totGenerale = totGenCanoni.add(totGenInteressi).add(totGenNotifica).add(totGenNonComp);
					//.add(totGenRimb);
			dto.setValoreColonna6("" + totGenerale);
			listDatiFoglioAccertaBil.add(dto);

			// Popolamento output_dati per foglio 2 - dettaglio pagamenti
			List<OutputDatiDTO> listDatiFoglioDettPag = datiReportBilancioDAO.getDettagliBilAcc(anno);
			listDatiFoglioDettPag.addAll(datiReportBilancioDAO.getAltriPagamenti(anno));

			populateFoglio(outFogli.get(0), listDatiFoglioAccertaBil);
			populateFoglio(outFogli.get(1), listDatiFoglioDettPag);

			outFile.setFogli(outFogli);

			String relativeFilePath = createReport(elabora, ambito.getCodAmbito(), outFile, attore,
					COD_REPORT_ACC_BILANCIO, false);
			if (relativeFilePath != null) {
				String reportUrl = downloadManager.copyFileToDownloadArea(
						elabora.getIdElabora() + "_" + elabora.getTipoElabora().getCodTipoElabora(), relativeFilePath);
				reportResult.setReportUrl(reportUrl);
				// UPDATE STATO
				updateElabora(elabora, STATO_ELAB_REPORT_OK, relativeFilePath);
				reportResult.setElabora(elabora);
			}

			return reportResult;
		} catch (BusinessException e) {
			handleException(elabora, e);
			throw e;
		} catch (Exception e) {
			handleException(elabora, e);
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	private String[] getTemplates(String tipoElab) {
		switch (tipoElab) {
		case TIPO_ELAB_RU:
			return new String[] { COD_REPORT_ELEUTENZE };
		case TIPO_ELAB_RM:
			return new String[] { COD_REPORT_ELENMOROSI };
		case TIPO_ELAB_RR:
			return new String[] { COD_REPORT_ELENRIMBORSI };
		case TIPO_ELAB_BI:
			return new String[] { COD_REPORT_ACC_BILANCIO + "_foglio1", COD_REPORT_ACC_BILANCIO + "_foglio2" };
		default:
			return null;
		}
	}

}
