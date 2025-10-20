/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.risca.riscabesrv.business.be.BusinessLogicFile450;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AccertamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiConfigDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.File450DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDatiTitolareDAO;
import it.csi.risca.riscabesrv.dto.AccertamentoDTO;
import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.dto.SollDatiTitolareDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;


public class BusinessLogicManagerFile450 implements BusinessLogicFile450

{ 
	protected transient Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private SollDatiTitolareDAO sollDatiTitolareDAO;
	
	@Autowired
	private AmbitiConfigDAO ambitiConfigDAO;
	
	@Autowired
	private File450DAO file450DAO;
	
	@Autowired
	private SoggettiDAO soggettoDAO;
	
	@Autowired
	private RataSdDAO rataSdDAO;
	
	@Autowired
	private AccertamentoDAO accertamentoDAO;


	public String creaM00(String codAmbito,BigDecimal totaleImportoArticoloRuolo) throws SQLException, DAOException, SystemException {
		String M00 = Constants.M00;
		List<AmbitoConfigDTO> ambitoConfig = new ArrayList<AmbitoConfigDTO>();
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_ENTE_CREDITORE);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),5,Boolean.TRUE);
		}
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_UFFICIO);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_UFFICIO);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);
		}
		Calendar cal = new GregorianCalendar();

		int year = cal.get(Calendar.YEAR);
		M00 += year;
		String progressivo = "";
		long countAnnoDataCreazione = file450DAO.countAnnoDataCreazioneByAnnoCorrente();

		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_PROGRESSIVO_FORNITURA);
		if (ambitoConfig.size() > 0) {
			if(countAnnoDataCreazione > 0) {
				if(countAnnoDataCreazione >= 9) {
				  progressivo = Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);	
				}else {
				  progressivo = "10000" + (countAnnoDataCreazione+1);		
				}
			}else {
				progressivo = Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);	
			}
			M00 += progressivo;	
		}

		//DATA CREAZIONE FILE E' la data di creazione del file
		final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
		Date date = new Date();
		String dataOdierna = sdf.format(date);
		M00 += dataOdierna;	

		//tipo minuta
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_MINUTA);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}

		//tipo cartellazione
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_CARTELLAZIONE);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}

		//specie ruolo
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_SPECIE_RUOLO);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}

		//tipo iscrizione
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_ISCRIZIONE);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}

		//tipo compenso
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_COMPENSO);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}

		//rate ruolo
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_RATE_RUOLO);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString ("0"+ambitoConfig.get(0).getValore(),2,Boolean.TRUE);
		}

		//cadenza rate
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CADENZA_RATE);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString ("00"+ambitoConfig.get(0).getValore(),3,Boolean.TRUE);
		}

		//NUMERO RATE AVVISO 2 ZERI
		String numeroRateAvviso = "00";
		M00 += numeroRateAvviso;
		//CADENZA RATE AVVISO (spazio lung 3)
		M00 += Utils.padString ("",3,Boolean.TRUE);

		//flag esclusione
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_FLAG_ESCLUSIONE);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}

		//FILLER Ex FLAG AVVISI CON MODULO AGGIUNTIVO (spazio lung 1 ) + tipologia importo
		M00 += Utils.padString ("",1,Boolean.TRUE);
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPOLOGIA_IMPORTO);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}

		//importo minimo
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_IMPORTO_MINIMO);
		if (ambitoConfig.size() > 0) {
			String minutaRuoloMancante = "00000000000000";
			minutaRuoloMancante = minutaRuoloMancante + ambitoConfig.get(0).getValore();
			M00 += Utils.padString (minutaRuoloMancante,15,Boolean.TRUE);
		}
		//release
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_RELEASE);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),3,Boolean.TRUE);
		}

		//CAMPO CALCOLATO (coincide con il valore del campo indicato al TIPO RECORD = M99)
		String totaleImportoArticoloRuoloSenzaVirgola = totaleImportoArticoloRuolo.toString().replace('.', ' ');
		totaleImportoArticoloRuoloSenzaVirgola = totaleImportoArticoloRuoloSenzaVirgola.replace(" ", "");
		long importoModificato = Long.parseLong(totaleImportoArticoloRuoloSenzaVirgola);

		//IMPORTO ARTICOLO DI RUOLO  lungh 15 
		M00 += String.format("%015d", (importoModificato));
		//testo comunic contrib
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TESTO_COMUNIC_CONTRIB);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}

		//testo mod opposizione
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TESTO_MOD_OPPOSIZIONE);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}

		//testo singolo contrib
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TESTO_SINGOLO_CONTRIB);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}

		//tipologia ente
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPOLOGIA_ENTE);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}

		//Sigla Provincia Concessione di Competenza degli avvisi (2 spazi) 
		M00 += Utils.padString ("",2,Boolean.TRUE);
		//Scadenza Prima Rata Avvisi. (8 zeri)
		//String.format("%8s", (ambitoConfig.get(0).getValore()));	aggiunge 8 spazi alla stringa
		String SiglaEscadenzaPrimaRataAvvisi = "00000000";
		M00 += SiglaEscadenzaPrimaRataAvvisi;

		//invio volantino
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_INVIO_VOLANTINO);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}
		//MODALITA STAMPA VOLANTINO
		M00 += Utils.padString ("",1,Boolean.TRUE);

		//comunicaz contrib
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_COMUNICAZ_CONTRIB);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}
		//Numero fattura 8 blank + data fattura 8 zeri
		M00 += Utils.padString ("",8,Boolean.TRUE);
		String dataFattura = "00000000";
		M00 += dataFattura;

		String flagProvenienza = "1";
		M00 += flagProvenienza;
		//cognome
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_COGNOME_OBBLIGATORIO);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),30,Boolean.TRUE);
		}

		//nome
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_NOME_OBBLIGATORIO);
		if (ambitoConfig.size() > 0) {
			M00 += Utils.padString (ambitoConfig.get(0).getValore(),30,Boolean.TRUE);
		}

		//FILLER 170 SPAZI
		M00 += Utils.padString ("",170,Boolean.TRUE);
		//PRODUCT KEY 24 SPAZI
		M00 += Utils.padString ("",24,Boolean.TRUE);
		//DATA RELEASE FORMA 8 zero
		String dataReleaseForma = "00000000";
		M00 += dataReleaseForma;
		//VERSIONE FORMA 2 zeri
		String versioneForma = "00";
		M00 += versioneForma;
		//RELEASE FORMA 2 zeri
		String releaseForma = "00";
		M00 += releaseForma;
		//DATA RELEASE SINERGIA 8 zeri
		String dataReleaseSinergia = "00000000";
		M00 += dataReleaseSinergia;
		//VERSIONE SINERGIA 2 zeri
		String versioneSinergia = "00";
		M00 += versioneSinergia;
		//RELEASE SINERGIA 2 zeri
		String releaseSinergia = "00";
		M00 += releaseSinergia;
		//FILLER 47	
		M00 += Utils.padString ("",47,Boolean.TRUE);
		//FILLER Campo ad uso di EquitaliA
		M00 += Utils.padString ("",7,Boolean.TRUE);
		return M00;
	}

	public String creaM10(String codAmbito, long progressivo) throws SQLException, DAOException, SystemException {
		String M10 = Constants.M10;
		//PROGRESSIVO RECORD lung 7		
		M10 += String.format("%07d", (progressivo));	
		List<AmbitoConfigDTO> ambitoConfig = new ArrayList<AmbitoConfigDTO>();
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_ENTE_CREDITORE);
		if (ambitoConfig.size() > 0) {
			M10 += Utils.padString (ambitoConfig.get(0).getValore(),5,Boolean.TRUE);
		}
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_UFFICIO);
		if (ambitoConfig.size() > 0) {
			M10 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_UFFICIO);
		if (ambitoConfig.size() > 0) {
			M10 += Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);
		}

		/**
		 * ESTREMI FORNITURA INIZIO
		 */
		Calendar cal = new GregorianCalendar();

		int year = cal.get(Calendar.YEAR);
		M10 += year;
		String progressivoAnno = "";
		long countAnnoDataCreazione = file450DAO.countAnnoDataCreazioneByAnnoCorrente();

		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_PROGRESSIVO_FORNITURA);
		if (ambitoConfig.size() > 0) {
			if(countAnnoDataCreazione > 0) {
				if(countAnnoDataCreazione >= 9) {
					progressivoAnno = Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);	
				}else {

					progressivoAnno = "10000" + (countAnnoDataCreazione+1);		
				}

			}else {
				progressivoAnno = Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);	
			}
			M10 += progressivoAnno;	
		}

		/**
		 * ESTREMI FORNITURA FINE
		 */
		// DATA CREAZIONE FILE E' la data di creazione del file
		final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
		Date date = new Date();
		String dataOdierna = sdf.format(date);
		M10 += dataOdierna;	

		//tipo minuta
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_MINUTA);
		if (ambitoConfig.size() > 0) {
			M10 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}
		//LUNGHEZZA RIGA
		String lunghezzaRiga = "080";
		M10 += lunghezzaRiga;
		//TESTO1
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TESTO1);
		if (ambitoConfig.size() > 0) {
			M10 += Utils.padString (ambitoConfig.get(0).getValore(),80,Boolean.TRUE);
		}
		//TESTO2
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TESTO2);
		if (ambitoConfig.size() > 0) {
			M10 += Utils.padString (ambitoConfig.get(0).getValore(),80,Boolean.TRUE);
		}
		//TESTO3
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TESTO3);
		if (ambitoConfig.size() > 0) {
			M10 += Utils.padString (ambitoConfig.get(0).getValore(),80,Boolean.TRUE);
		}
		//TESTO4
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TESTO4);
		if (ambitoConfig.size() > 0) {
			M10 += Utils.padString (ambitoConfig.get(0).getValore(),80,Boolean.TRUE);
		}
		//FILLER 86	
		M10 += Utils.padString ("",86,Boolean.TRUE);
		return M10;
	}

	public String creaM20(String codAmbito, StatoDebitorioExtendedDTO statoDebitorioExtendedDTO, long progressivo, long numeroPartita, String presenzaCointestati) throws SQLException, DAOException, SystemException {
		String M20 = Constants.M20;

		//PROGRESSIVO RECORD lung 7
		M20 += String.format("%07d", (progressivo));	

		List<AmbitoConfigDTO> ambitoConfig = new ArrayList<AmbitoConfigDTO>();
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_ENTE_CREDITORE);
		if (ambitoConfig.size() > 0) {
			M20 += Utils.padString (ambitoConfig.get(0).getValore(),5,Boolean.TRUE);
		}

		/**
		 * INIZIO IDENTIFICATIVO PARTITA
		 */

		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_UFFICIO);
		if (ambitoConfig.size() > 0) {
			M20 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_UFFICIO);
		if (ambitoConfig.size() > 0) {
			M20 += Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);
		}
		//ANNO DATA_SCADENZA
		Long anno = rataSdDAO.findAnnoDataScadPagamentoByIdStatoDebitorio(statoDebitorioExtendedDTO.getIdStatoDebitorio());

		M20 += String.format("%04d", (anno));	
		//IDENTIFICATIVO TIPOLOGIA ATTO 
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_IDENTIF_TIPOLOGIA_ATTO);
		if (ambitoConfig.size() > 0) {
			//M20 += padString (ambitoConfig.get(0).getValore(),80,Boolean.TRUE);
			M20 += Utils.padString ("00"+ambitoConfig.get(0).getValore(),3,Boolean.TRUE);
		}
		//NUMERO PARTITA lungh 9
		M20 += String.format("%09d", (numeroPartita));	
		//PROGRESSIVO PARTITA
		String progressivoPartita = "001";
		M20 +=  progressivoPartita;
		//CODICE TIPO ATTO
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_TIPO_ATTO);
		if (ambitoConfig.size() > 0) {
			M20 += Utils.padString (ambitoConfig.get(0).getValore(),2,Boolean.TRUE);
		}
		//DATA_PROTOCOLLO, NUM_PROTOCOLLO (ESTREMI ATTO), DATA_NOTIFICA
		AccertamentoDTO accertamentoDTO = accertamentoDAO.findMaxDataAndNumProtocolloByIdStatoDebitorio(statoDebitorioExtendedDTO.getIdStatoDebitorio());
		if(accertamentoDTO != null) {
			final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
			String dataProtocollo = "";
			if(accertamentoDTO.getDataProtocollo()!=null) {
				dataProtocollo = sdf.format(accertamentoDTO.getDataProtocollo());			
			}

			M20 += Utils.padString (dataProtocollo,8,Boolean.TRUE);

			M20 += Utils.padString (accertamentoDTO.getNumProtocollo(),12,Boolean.TRUE);
			String dataNotifica = "";
			if (accertamentoDTO.getDataNotifica() != null) {
				dataNotifica= sdf.format(accertamentoDTO.getDataNotifica());
			}else {
				dataNotifica="00000000";
			}
			M20 += Utils.padString (dataNotifica,8,Boolean.TRUE);

		}
		//ULTERIORE IDENTIFICATIVO ATTO (cod_utenza) lungh 12
		String codUtenza = statoDebitorioExtendedDTO.getCodUtenza() == null ? "" : statoDebitorioExtendedDTO.getCodUtenza();
		M20 += Utils.padString (codUtenza,12,Boolean.TRUE);
		//MOTIVAZIONE ISCRIZIONE FILLER 28 
		if (accertamentoDTO.getDataNotifica() != null) {
			M20 += Utils.padString ("",28,Boolean.TRUE);
		}else {
			M20 += Utils.padString (Constants.MOTIVAZIONE_ISCRIZIONE_COMPIUTA_GIACENZA,28,Boolean.TRUE);
		}
		/**
		 * FINE IDENTIFICATIVO PARTITA
		 */

		//CODICE FISCALE E PARTITA IVA
		//select sulla tabella RISCA_R_SOLL_DATI_TITOLARE
		//loadSollDatiTitolareByIdAccertamento
		if (accertamentoDTO != null) {

			SollDatiTitolareDTO sollDatiTitolare = sollDatiTitolareDAO.loadSollDatiTitolareByIdAccertamento(accertamentoDTO.getIdAccertamento());
			
			if(sollDatiTitolare != null) {
				
				String cfSoggetto = sollDatiTitolare.getCodiceFiscaleCalc() == null ? "" : sollDatiTitolare.getCodiceFiscaleCalc();
				M20 += Utils.padString (cfSoggetto,16,Boolean.TRUE);
				String pIva = sollDatiTitolare.getPartitaIva() == null ? "" : sollDatiTitolare.getPartitaIva();
				M20 += Utils.padString (pIva,11,Boolean.TRUE);
				
				//flag defunto 0
				M20 += "0";
				
				//data insinuazione fallimento 
				M20 += "00000000";
				//Tipo soggetto
				String idTipoSoggetto = sollDatiTitolare.getIdTipoSoggetto() == null ? "" : sollDatiTitolare.getIdTipoSoggetto().toString();
				M20 += Utils.padString (idTipoSoggetto,1,Boolean.TRUE);
				//PRESENZA INDIRIZZO NOTIFICA LUNGH 1
				M20 += "N";

				//PRESENZA DATI DITTA INDIVIDUALE LUNGH 1
				M20 += "N";

				//PRESENZA ULTERIORI DATI ANAGRAFICI PER LA NOTIFICA LUNGH 1 blank
				M20 += Utils.padString ("",1,Boolean.TRUE);		
				
				//PRESENZA COINTESTATI (CAMPO CALCOLATO)
				M20 += presenzaCointestati;
				//FLAG TIPO AVVISO BLANK LUNGH 1
				M20 += Utils.padString ("",1,Boolean.TRUE);
				//DEN SOGGETTO
				if(sollDatiTitolare.getIdTipoSoggetto() != null && sollDatiTitolare.getIdTipoSoggetto() != Constants.DB.TIPO_SOGGETTO.ID_TIPO_SOGGETTO.PERSONA_FISICA.longValue()) {
					M20 += Utils.padString (sollDatiTitolare.getDenSoggetto()==null ? "" : sollDatiTitolare.getDenSoggetto(),76,Boolean.TRUE); 
					//FILLER 29
					M20 += Utils.padString ("",29,Boolean.TRUE); 
				}else {
					//COGNOME NOME
						String cognome = sollDatiTitolare.getCognome() == null ? "" : sollDatiTitolare.getCognome();
						M20 += Utils.padString (cognome,50,Boolean.TRUE);
						String nome = sollDatiTitolare.getNome() == null ? "" : sollDatiTitolare.getNome();
						M20 += Utils.padString (nome,40,Boolean.TRUE);
					//SESSO 1 BLANK
					M20 += Utils.padString ("",1,Boolean.TRUE);
					//DATA NASCITA SOGGETTO, COD BELFIORE COMUNE, sigla provincia
					M20 += Utils.padString (sollDatiTitolare.getDataNascita()==null ?"" : sollDatiTitolare.getDataNascita(),8,Boolean.TRUE); 
					M20 += Utils.padString (sollDatiTitolare.getCodBelfioreComune()==null ? "" : sollDatiTitolare.getCodBelfioreComune(),4,Boolean.TRUE); 
                    M20 += Utils.padString (sollDatiTitolare.getProvIndPost()==null ? "" : sollDatiTitolare.getProvIndPost() ,2,Boolean.TRUE); 
													
				}								
				
			//indirizzo 35
			//String indirizzo = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
			M20 += Utils.padString (sollDatiTitolare.getIndirizzoIndPost() == null ? "" : sollDatiTitolare.getIndirizzoIndPost(),35,Boolean.TRUE); 

			//NUMERO CIVICO 5 ZERI
			M20 += "00000";
			//LETTERA NUMERO CIVICO 2 blank
			M20 += Utils.padString ("",2,Boolean.TRUE); 
			//KM 6 blank
			M20 += Utils.padString ("",6,Boolean.TRUE); 
			//PALAZZINA 3 blank
			M20 += Utils.padString ("",3,Boolean.TRUE); 
			//SCALA 3 blank
			M20 += Utils.padString ("",3,Boolean.TRUE); 
			//PIANO 3 blank
			M20 += Utils.padString ("",3,Boolean.TRUE); 
			//INTERNO 4 blank
			M20 += Utils.padString ("",4,Boolean.TRUE); 

			//TODO C.A.P.  da approfondire in analisi momentaneamente setto 5 x
			//String cap = "XXXXX";
			M20 += Utils.padString (sollDatiTitolare.getCapComune()==null ?"":sollDatiTitolare.getCapComune(),5,Boolean.TRUE); 

			// DESCRIZIONE COMUNE  40 
			//String descrizioneComune = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
			M20 += Utils.padString (sollDatiTitolare.getComuneIndPost()==null ?"":sollDatiTitolare.getComuneIndPost(),40,Boolean.TRUE); 
			// SIGLA PROVINCIA  2 
			// String siglaProvincia = "XX";
			M20 += Utils.padString (sollDatiTitolare.getProvIndPost()==null ?"":sollDatiTitolare.getProvIndPost(),2,Boolean.TRUE); 
			// CODICE BELFIORE COMUNE  4 
			// String codiceBelfiore = "XXXX";
			M20 += Utils.padString (sollDatiTitolare.getCodBelfioreComune()==null ? "" : sollDatiTitolare.getCodBelfioreComune(),4,Boolean.TRUE); 
		}
	}
		//LOCALITa FRAZIONE 21 blank
		M20 += Utils.padString ("",21,Boolean.TRUE); 

		//TIPOLOGIA ATTO ISCRIZIONE RUOLO
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_ATTO_ISCRIZIONE);
		if (ambitoConfig.size() > 0) {
			M20 += Utils.padString ("T"+ambitoConfig.get(0).getValore(),2,Boolean.TRUE); 
		}

		//FILLER 57
		M20 += Utils.padString ("",57,Boolean.TRUE); 

		return M20;
	}


	public String creaM30(String codAmbito, StatoDebitorioExtendedDTO statoDebitorioExtendedDTO, long progressivo, long numeroPartita) throws SQLException, DAOException, SystemException {
		String M30 = Constants.M30;

		//PROGRESSIVO RECORD lung 7
		M30 += String.format("%07d", (progressivo));	
		List<AmbitoConfigDTO> ambitoConfig = new ArrayList<AmbitoConfigDTO>();
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_ENTE_CREDITORE);
		if (ambitoConfig.size() > 0) {
			M30 += Utils.padString (ambitoConfig.get(0).getValore(),5,Boolean.TRUE);
		}

		/**
		 * INIZIO IDENTIFICATIVO PARTITA
		 */

		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_UFFICIO);
		if (ambitoConfig.size() > 0) {
			M30 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_UFFICIO);
		if (ambitoConfig.size() > 0) {
			M30 += Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);
		}
		//ANNO DATA_SCADENZA
		Long anno = rataSdDAO.findAnnoDataScadPagamentoByIdStatoDebitorio(statoDebitorioExtendedDTO.getIdStatoDebitorio());
		M30 +=  String.format("%04d", (anno));	
		//IDENTIFICATIVO TIPOLOGIA ATTO 
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_IDENTIF_TIPOLOGIA_ATTO);
		if (ambitoConfig.size() > 0) {
			M30 += Utils.padString ("00"+ambitoConfig.get(0).getValore(),3,Boolean.TRUE);
		}
		//NUMERO PARTITA lungh 9
		M30 += String.format("%09d", (numeroPartita));	
		//PROGRESSIVO PARTITA
		String progressivoPartita = "001";
		M30 +=  progressivoPartita;
		//CODICE TIPO ATTO
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_TIPO_ATTO);
		if (ambitoConfig.size() > 0) {
			M30 += Utils.padString (ambitoConfig.get(0).getValore(),2,Boolean.TRUE);
		}
		//DATA_PROTOCOLLO, NUM_PROTOCOLLO, DATA_NOTIFICA
		AccertamentoDTO accertamentoDTO = accertamentoDAO.findMaxDataAndNumProtocolloByIdStatoDebitorio(statoDebitorioExtendedDTO.getIdStatoDebitorio());		
		if(accertamentoDTO != null) {
			final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
			String dataProtocollo = "";
			if(accertamentoDTO.getDataProtocollo() != null) {
				dataProtocollo = sdf.format(accertamentoDTO.getDataProtocollo());
			}
			M30 += Utils.padString (dataProtocollo,8,Boolean.TRUE);

			M30 += Utils.padString (accertamentoDTO.getNumProtocollo()== null ? "" : accertamentoDTO.getNumProtocollo(),12,Boolean.TRUE);

			String dataNotifica = "";
			if (accertamentoDTO.getDataNotifica() != null) {
				dataNotifica= sdf.format(accertamentoDTO.getDataNotifica());
			}else {
				dataNotifica="00000000";
			}				
			M30 += Utils.padString (dataNotifica,8,Boolean.TRUE);
		}

		//ULTERIORE IDENTIFICATIVO ATTO (cod_utenza) lungh 12
		M30 += Utils.padString (statoDebitorioExtendedDTO.getCodUtenza()==null ? "" : statoDebitorioExtendedDTO.getCodUtenza(),12,Boolean.TRUE);

		//MOTIVAZIONE ISCRIZIONE FILLER 28 
		if (accertamentoDTO.getDataNotifica() != null) {
			M30 += Utils.padString ("",28,Boolean.TRUE);
		}else {
			M30 += Utils.padString (Constants.MOTIVAZIONE_ISCRIZIONE_COMPIUTA_GIACENZA,28,Boolean.TRUE);
		}
		/**
		 * FINE IDENTIFICATIVO PARTITA
		 */

		//DESCRIZIONE PARTITA cod utenza
		M30 += Utils.padString (statoDebitorioExtendedDTO.getCodUtenza()== null ?"" :statoDebitorioExtendedDTO.getCodUtenza(),150,Boolean.TRUE); 
		//FILLER 189
		M30 += Utils.padString ("",189,Boolean.TRUE); 

		return M30;
	}


	public String creaM50(String codAmbito, StatoDebitorioExtendedDTO statoDebitorioExtendedDTO, long progressivo,long numeroPartita, long progressivoM50, String codiceEntrata, String tipoCodiceEntrata, BigDecimal importoArticoloRuolo, String numeroAccertamentoBilancio) throws SQLException, DAOException, SystemException {
		String M50 = Constants.M50;

		//PROGRESSIVO RECORD lung 7
		M50 += String.format("%07d", (progressivo));	
		List<AmbitoConfigDTO> ambitoConfig = new ArrayList<AmbitoConfigDTO>();
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_ENTE_CREDITORE);
		if (ambitoConfig.size() > 0) {
			M50 += Utils.padString (ambitoConfig.get(0).getValore(),5,Boolean.TRUE); 
		}

		/**
		 * INIZIO IDENTIFICATIVO PARTITA
		 */

		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_UFFICIO);
		if (ambitoConfig.size() > 0) {
			M50 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_UFFICIO);
		if (ambitoConfig.size() > 0) {
			M50 += Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);
		}
		//ANNO DATA_SCADENZA
		Long anno = rataSdDAO.findAnnoDataScadPagamentoByIdStatoDebitorio(statoDebitorioExtendedDTO.getIdStatoDebitorio());
		M50 +=  String.format("%04d", (anno));	

		//IDENTIFICATIVO TIPOLOGIA ATTO 
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_IDENTIF_TIPOLOGIA_ATTO);
		if (ambitoConfig.size() > 0) {
			M50 += Utils.padString ("00"+ambitoConfig.get(0).getValore(),3,Boolean.TRUE);
		}

		//NUMERO PARTITA lungh 9
		M50 += String.format("%09d", (numeroPartita));	
		//PROGRESSIVO PARTITA
		String progressivoPartita = "001";
		M50 +=  progressivoPartita;
		//CODICE TIPO ATTO
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_TIPO_ATTO);
		if (ambitoConfig.size() > 0) {
			M50 += Utils.padString (ambitoConfig.get(0).getValore(),2,Boolean.TRUE);
		}

		//DATA_PROTOCOLLO, NUM_PROTOCOLLO, DATA_NOTIFICA
		AccertamentoDTO accertamentoDTO = accertamentoDAO.findMaxDataAndNumProtocolloByIdStatoDebitorio(statoDebitorioExtendedDTO.getIdStatoDebitorio());		
		if(accertamentoDTO != null) {
			final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
			String dataProtocollo = "";
			if(accertamentoDTO.getDataProtocollo() != null) {
				dataProtocollo= sdf.format(accertamentoDTO.getDataProtocollo());
			}
			M50 += Utils.padString (dataProtocollo,8,Boolean.TRUE);

			M50 += Utils.padString (accertamentoDTO.getNumProtocollo()==null?"":accertamentoDTO.getNumProtocollo(),12,Boolean.TRUE);

			String dataNotifica = "";
			if (accertamentoDTO.getDataNotifica() != null) {
				dataNotifica= sdf.format(accertamentoDTO.getDataNotifica());
			}else {
				dataNotifica="00000000";
			}
			M50 += Utils.padString (dataNotifica,8,Boolean.TRUE);
		}
		//ULTERIORE IDENTIFICATIVO ATTO (cod_utenza) lungh 12
		M50 += Utils.padString (statoDebitorioExtendedDTO.getCodUtenza()==null?"":statoDebitorioExtendedDTO.getCodUtenza(),12,Boolean.TRUE);
		//MOTIVAZIONE ISCRIZIONE FILLER 28 
		if (accertamentoDTO.getDataNotifica() != null) {
			M50 += Utils.padString ("",28,Boolean.TRUE);
		}else {
			M50 += Utils.padString (Constants.MOTIVAZIONE_ISCRIZIONE_COMPIUTA_GIACENZA,28,Boolean.TRUE);
		}
		/**
		 * FINE IDENTIFICATIVO PARTITA
		 */

		//PROGRESSIVO ARTICOLO DI RUOLO lung 3
		M50 += String.format("%03d", (progressivoM50));	

		//CODICE ENTRATA
		M50 += Utils.padString(codiceEntrata,4,Boolean.TRUE);

		//TIPO CODICE ENTRATA
		M50 += Utils.padString(tipoCodiceEntrata,1,Boolean.TRUE); 

		//COMPETENZA/RESIDUI  1 blank
		M50 += Utils.padString ("",1,Boolean.TRUE); 
		//imponibile 15 zeri
		long imponibile = 0;
		M50 += String.format("%015d", (imponibile));	
		String importoArticoloRuoloSenzaVirgola = importoArticoloRuolo.toString().replace('.', ' ');
		importoArticoloRuoloSenzaVirgola = importoArticoloRuoloSenzaVirgola.replace(" ", "");
		long importoModificato = Long.parseLong(importoArticoloRuoloSenzaVirgola);

		//IMPORTO ARTICOLO DI RUOLO  lungh 15 
		M50 += String.format("%015d", (importoModificato));
		// TETTO SOMME AGGIUNTIVE lungh 15 filler
		M50 += Utils.padString ("",15,Boolean.TRUE); 

		//SEMESTRI 2 filler
		M50 += Utils.padString ("",2,Boolean.TRUE); 

		//REPARTO 2 filler
		M50 += Utils.padString ("",2,Boolean.TRUE); 

		//FILLER 2
		M50 += Utils.padString ("",2,Boolean.TRUE); 

		//CAPO Capo del bilancio. 2 filler
		M50 += Utils.padString ("",2,Boolean.TRUE); 

		//CAPITOLO Capitolo di bilancio. 4 filler
		M50 += Utils.padString ("",4,Boolean.TRUE); 

		//ARTICOLO Articolo di bilancio. 2 filler
		M50 += Utils.padString ("",2,Boolean.TRUE); 

		//DESCRIZIONE ARTICOLO 75 filler 1 riga
		M50 += Utils.padString ("",75,Boolean.TRUE); 

		//DESCRIZIONE ARTICOLO 75 filler 2 riga
		M50 += Utils.padString ("",75,Boolean.TRUE); 

		//TIPOLOGIA REGIME SANZIONATORIO  1 filler
		M50 += Utils.padString ("",1,Boolean.TRUE); 

		//DATA DECORRENZA INTERESSI 8 filler
		M50 += Utils.padString ("",8,Boolean.TRUE); 
		//NUMERO ACCERTAMENTO BILANCIO lungh 25 
		M50 += Utils.padString (numeroAccertamentoBilancio,25,Boolean.TRUE); 
		//FILLER 87
		M50 += Utils.padString ("",87,Boolean.TRUE); 

		return M50;
	}


	public String creaM99(String codAmbito, long totM10, long totM20, long totM30, long totM50 , BigDecimal totaleImportoArticoloRuolo) throws SQLException, DAOException, SystemException {
		String M99 = Constants.M99;

		List<AmbitoConfigDTO> ambitoConfig = new ArrayList<AmbitoConfigDTO>();
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_ENTE_CREDITORE);
		if (ambitoConfig.size() > 0) {
			M99 += Utils.padString (ambitoConfig.get(0).getValore(),5,Boolean.TRUE); 
		}

		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_UFFICIO);
		if (ambitoConfig.size() > 0) {
			M99 += Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_UFFICIO);
		if (ambitoConfig.size() > 0) {
			M99 += Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);
		}
		/**
		 * ESTREMI FORNITURA INIZIO
		 */
		Calendar cal = new GregorianCalendar();

		int year = cal.get(Calendar.YEAR);
		M99 += year;
		String progressivo = "";
		long countAnnoDataCreazione = file450DAO.countAnnoDataCreazioneByAnnoCorrente();

		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_PROGRESSIVO_FORNITURA);
		if (ambitoConfig.size() > 0) {
			if(countAnnoDataCreazione > 0) {
				if(countAnnoDataCreazione >= 9) {
					progressivo = Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);	
				}else {
					progressivo = "10000" + (countAnnoDataCreazione+1);	
				}
			}else {
				progressivo = Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);	
			}
			M99 += progressivo;	
		}
		/**
		 * ESTREMI FORNITURA FINE
		 */
		// DATA CREAZIONE FILE E' la data di creazione del file
		final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
		Date date = new Date();
		String dataOdierna = sdf.format(date);
		M99 += dataOdierna;	
		//TOTALE RECORDS M10 lungh 7
		M99 += String.format("%07d", (totM10));
		//TOTALE RECORDS M40 lungh 7
		M99 += String.format("%07d", (0));
		//TOTALE RECORDS M20 lungh 7
		M99 += String.format("%07d", (totM20));
		//TOTALE RECORDS M21 lungh 7
		M99 += String.format("%07d", (0));
		//TOTALE RECORDS M22 lungh 7
		M99 += String.format("%07d", (0));
		//TOTALE RECORDS M23 lungh 7
		M99 += String.format("%07d", (0));
		//TOTALE RECORDS M30 lungh 7
		M99 += String.format("%07d", (totM30));
		//TOTALE RECORDS M50 lungh 7
		M99 += String.format("%07d", (totM50));

		//FILLER 7
		M99 += Utils.padString ("",7,Boolean.TRUE); 
		//FILLER 7
		M99 += Utils.padString ("",7,Boolean.TRUE); 
		//FILLER 7
		M99 += Utils.padString ("",7,Boolean.TRUE); 

		//TOTALE IMPONIBILE 15 zeri
		M99 += "000000000000000";
		//TOTALE IMPORTO ARTICOLO DI RUOLO lungh 15 Sum(campo dell'M50)
		String totaleImportoArticoloRuoloSenzaVirgola = totaleImportoArticoloRuolo.toString().replace('.', ' ');
		totaleImportoArticoloRuoloSenzaVirgola = totaleImportoArticoloRuoloSenzaVirgola.replace(" ", "");
		long importoModificato = Long.parseLong(totaleImportoArticoloRuoloSenzaVirgola);
		
		//IMPORTO ARTICOLO DI RUOLO  lungh 15 
		M99 += String.format("%015d", (importoModificato));

		//FILLER 310
		M99 += Utils.padString ("",310,Boolean.TRUE); 

		return M99;
	}
}
