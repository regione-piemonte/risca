/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.util;

/**
 * The type Constants.
 *
 * @author CSI PIEMONTE
 */
public class Constants {
	/**
	 * The constant COMPONENT_NAME.
	 */
	public final static String COMPONENT_NAME = "riscabesrv";
	/**
	 * The constant LOGGER_NAME.
	 */
	public final static String LOGGER_NAME = "riscabesrv";

	/**
	 * The constant parola Chiave SF.
	 */
	public final static String ACTA_PAROLE_CHIAVE_SF = "ACTA.parolaChiaveSF";
	/**
	 * The constant ACTA IDAOO.
	 */
	public final static String ACTA_ID_AOO = "ACTA.idAOO";
	/**
	 * The constant ACTA IDNODO.
	 */
	public final static String ACTA_ID_NODO = "ACTA.idNodo";
	/**
	 * The constant ACTA ID_ TRUTTURA.
	 */
	public final static String ACTA_ID_STRUTTURA = "ACTA.idStruttura";

	/**
	 * The constant ACTA CLIENT APPLICATION INFO.
	 */
	public final static String ACTA_CLIENT_APPLICATION_INFO = "RISCA_ACTA.client_application_info";

	/**
	 * The constant ACTA CODICE FISCALE.
	 */
	public final static String ACTA_CODICE_FISCALE = "RISCA_ACTA.codice_fiscale";
	/**
	 * The constant ACTA REPOSITORY NAME.
	 */
	public final static String ACTA_REPOSITORY_NAME = "RISCA_ACTA.repository_name";
	/**
	 * The constant ACTA PRINCIPAL ID principalId .
	 */
	public final static String ACTA_SESSION_PRINCIPAL_ID = "principal_id";
	/**
	 * The constant ELENCO ALLEGATI A CLASSIFICAZIONE PRINCIPAL VIEW .
	 */
	public final static String ELENCO_ALLEGATI_A_CLASSIFICAZIONE_PRINCIPAL_VIEW = "ElencoAllegatiAClassificazionePrincipaleView";
	/**
	 * The constant DOC_SEMPLICE_PROPERTIES_TYPE .
	 */
	public final static String DOC_SEMPLICE_PROPERTIES_TYPE = "DocumentoSemplicePropertiesType";

	/**
	 * The constant FILTER TYPE ALL
	 */
	public final static String FILTER_TYPE_ALL = "all";

	/**
	 * The constant Serie Fascicoli Properties Type
	 * 
	 */
    /**
     * The constant USERINFO_SESSIONATTR.
     */
    public static final String USERINFO_SESSIONATTR = "appDatacurrentUser";
    
    /**
     * The constant AUTH_ID_MARKER.
     */
    public static final String AUTH_ID_MARKER = "Shib-Iride-IdentitaDigitale";
    
	public final static String SERIE_FSC_PROPERTIES_TYPE = "SerieFascicoliPropertiesType";

	public final static String RISCA_RP_PROD_RISCABE= "RISCA_RP_PROD_RISCABE";
	public final static String FLG_OPERAZIONE_INSERT ="I";
	public final static String FLG_OPERAZIONE_UPDATE ="U";
	public final static String FLG_OPERAZIONE_DELETE ="D";
	public final static String OPERAZIONE_INSERT ="insert";
	public final static String OPERAZIONE_UPDATE ="update";
	public final static String OPERAZIONE_DELETE ="delete";
	public static class DB {

		public static class TIPO_SOGGETTO {
			public static class ID_TIPO_SOGGETTO {
				public static final Integer PERSONA_FISICA = 1;
				public static final Integer PERSONA_GIURIDICA_PRIVATA = 2;
				public static final Integer PERSONA_GIURIDICA_PUBBLICA = 3;
			}

			public static class COD_TIPO_SOGGETTO {
				public static final String PERSONA_FISICA = "PF";
				public static final String PERSONA_GIURIDICA_PRIVATA = "PG";
				public static final String PERSONA_GIURIDICA_PUBBLICA = "PB";
			}
		}

		public static class NAZIONE {
			public static final Integer ID_NAZIONE_ITALIA = 1;
			public static final String DENOM_NAZIONE_ITALIA = "ITALIA";
		}
		
		public static class STATO_RISCOSSIONE {
			public static class ID_STATO_RISCOSSIONE {
				public static final Integer SCADUTA = 7;
				public static final Integer ATTIVA = 1;
				public static final Integer SOSPESA = 6;
			}
		}
		
		public static class TIPO_PROVVEDIMENTO {
			public static class ID_TIPO_PROVVEDIMENTO {
				public static final Integer IST_RINNOVO = 22;
				public static final Integer IST_RINNOVO_PR_88_96 = 23;
				public static final Integer IST_SANATORIA = 24;
				public static final Integer AUT_PROVVISORIA =1;
			}
		}
		
		public static class STATO_CONTRIBUZIONE {
			public static class ID_STATO_CONTRIBUZIONE{
				public static final Integer REGOLARE = 1;
				public static final Integer INSUFFICIENTE = 2;
				public static final Integer REGOLARIZZATO = 3;
				public static final Integer ECCEDENTE = 4;
				public static final Integer INESIGIBILE = 5;
				
			}
		}
		
		public static class TIPO_ACCERTAMENTO{
			public static class ID_TIPO_ACCERTAMENTO {
				public static final Integer RISCOSSIONE_COATTIVA = 3;
				public static final Integer FALLIMENTO = 7;
			}
		}
		
		public static class ATTIVITA_STATO_DEB {
			public static class ID_ATTIVITA_STATO_DEB{
				public static final Integer CONCORDATO = 10;			
				
			}
		}
		
		public static class TIPO_MODALITA_PAG {
			public static class ID_TIPO_MODALITA_PAG{
				public static final Integer PAGOPA = 11;
			}
		}
		
		public static class STATO_IUV {
			public static class ID_STATO_IUV{
				public static final Integer ATTIVO = 1;
			}
			
		}
		
		public static class STATO_ELABORA{
			public static class ID_STATO_ELABORA{
				public static final Integer STACONT_OK = 33;
			}
			public static class COD_STATO_ELABORA{
				public static final String FILE450_ON = "FILE450_ON";
				public static final String FILE450_OK = "FILE450_OK";
				public static final String FILE450_KO = "FILE450_KO";
			}
		}
		
		public static class TIPO_ELABORA{
			public static class ID_TIPO_ELABORA{
				public static final Integer SO = 7;
			}
			public static class COD_TIPO_ELABORA{
				public static final String SO = "SO";
			}
			public static class DES_TIPO_ELABORA{
				public static final String SO = "File 450 SORIS";
			}
		}
		
		public static class AMBITO_CONFIG{
			public static class CHIAVE{
				public static final String MINUTA_RUOLO_CODICE_ENTE_CREDITORE=  "MinutaRuolo.CodiceEnteCreditore";
			    public static final String MINUTA_RUOLO_TIPO_UFFICIO = "MinutaRuolo.TipoUfficio";
			    public static final String MINUTA_RUOLO_CODICE_UFFICIO = "MinutaRuolo.CodiceUfficio";
			    public static final String MINUTA_RUOLO_TIPO_MINUTA = "MinutaRuolo.TipoMinuta";
			    public static final String MINUTA_RUOLO_TIPO_CARTELLAZIONE = "MinutaRuolo.TipoCartellazione";
			    public static final String MINUTA_RUOLO_SPECIE_RUOLO ="MinutaRuolo.SpecieRuolo";
			    public static final String MINUTA_RUOLO_TIPO_ISCRIZIONE ="MinutaRuolo.TipoIscrizione";
			    public static final String MINUTA_RUOLO_TIPO_COMPENSO = "MinutaRuolo.TipoCompenso";
			    public static final String MINUTA_RUOLO_RATE_RUOLO ="MinutaRuolo.RateRuolo";
			    public static final String MINUTA_RUOLO_CADENZA_RATE = "MinutaRuolo.CadenzaRate";
			    public static final String MINUTA_RUOLO_FLAG_ESCLUSIONE = "MinutaRuolo.FlagEsclusione";
			    public static final String MINUTA_RUOLO_TIPOLOGIA_IMPORTO = "MinutaRuolo.TipologiaImporto";
			    public static final String MINUTA_RUOLO_IMPORTO_MINIMO = "MinutaRuolo.ImportoMinimo";
			    public static final String MINUTA_RUOLO_RELEASE = "MinutaRuolo.Release";
			    public static final String MINUTA_RUOLO_TESTO_COMUNIC_CONTRIB = "MinutaRuolo.TestoComunicContrib";
			    public static final String MINUTA_RUOLO_TESTO_MOD_OPPOSIZIONE = "MinutaRuolo.TestoModOpposizione";
			    public static final String MINUTA_RUOLO_TESTO_SINGOLO_CONTRIB = "MinutaRuolo.TestoSingoloContrib";
			    public static final String MINUTA_RUOLO_TIPOLOGIA_ENTE = "MinutaRuolo.TipologiaEnte";
			    public static final String MINUTA_RUOLO_INVIO_VOLANTINO ="MinutaRuolo.InvioVolantino";
			    public static final String MINUTA_RUOLO_COMUNICAZ_CONTRIB = "MinutaRuolo.ComunicazContrib";
		        public static final String MINUTA_RUOLO_COGNOME_OBBLIGATORIO = "MinutaRuolo.CognomeObbligatorio";
		        public static final String MINUTA_RUOLO_NOME_OBBLIGATORIO = "MinutaRuolo.NomeObbligatorio";
		        public static final String MINUTA_RUOLO_TESTO1 = "MinutaRuolo.Testo1";
		        public static final String MINUTA_RUOLO_TESTO2 = "MinutaRuolo.Testo2";
		        public static final String MINUTA_RUOLO_TESTO3 = "MinutaRuolo.Testo3";
		        public static final String MINUTA_RUOLO_TESTO4 = "MinutaRuolo.Testo4";
		        public static final String MINUTA_RUOLO_IDENTIF_TIPOLOGIA_ATTO = "MinutaRuolo.IdentifTipologiaAtto";
			    public static final String MINUTA_RUOLO_CODICE_TIPO_ATTO = "MinutaRuolo.CodiceTipoAtto";
			    public static final String MINUTA_RUOLO_TIPO_ATTO_ISCRIZIONE = "MinutaRuolo.TipoAttoIscrizione";
			    public static final String MINUTA_RUOLO_PROGRESSIVO_FORNITURA = "MinutaRuolo.ProgressivoFornitura";
			}
		}
		
		public static class PASSO_ELABORA{
			public static class COD_PASSO_ELABORA{
				public static final String 	CREA_FILE_450 = "CREA_FILE_450";
				public static final String INVIO_MAIL_SERVIZIO = "INVIO_MAIL_SERVIZIO";
				public static final String 	UPD_STATO_CONTRIB = "UPD_STATO_CONTRIB";
			}
		}
		
		public static class FASE_ELABORA{
			public static class COD_FASE_ELABORA {
				public static final String CREA_RUOLO = "CREA_RUOLO";
				public static final String AGGIORNA_STATO_CONTRIBUZIONE = "AGGIORNA_STATO_CONTRIBUZIONE";
			}
		}
		
		public static class EMAIL_SERVIZIO{
			public static class COD_EMAIL_SERVIZIO{
				public static final String 	E_CREA_RUOLO = "E_CREA_RUOLO";
			}
		}
				
	}
	
	
	public static final Integer FLAG_ANNULLATO_TRUE = 1;
	
	public static final Integer FLAG_ADDEBITO_ANNO_SUCCESSIVO_TRUE = 1;

	public final static String POST_PUT_DEL_ACCER = "POST_PUT_DEL_ACCER";

	public final static String POST_PUT_DEL_ANNUA = "POST_PUT_DEL_ANNUA";

	public final static String POST_PUT_DEL_DT = "POST_PUT_DEL_DT";

	public final static String POST_PUT_DEL_ELABO = "POST_PUT_DEL_ELABO";

	public final static String POST_PUT_DEL_GRUPPI = "POST_PUT_DEL_GRUPPI";

	public final static String POST_PUT_DEL_IUV = "POST_PUT_DEL_IUV";

	public final static String POST_PUT_DEL_PAGA = "POST_PUT_DEL_PAGA";

	public final static String POST_PUT_DEL_PARAM = "POST_PUT_DEL_PARAM";

	public final static String POST_PUT_DEL_RATA = "POST_PUT_DEL_RATA";

	public final static String POST_PUT_DEL_STDEBI = "POST_PUT_DEL_STDEBI";

	public final static String LOAD_ACCER = "LOAD_ACCER";

	public final static String LOAD_ANNUA = "LOAD_ANNUA";

	public final static String LOAD_DT = "LOAD_DT";

	public final static String LOAD_ELABO = "LOAD_ELABO";

	public final static String LOAD_GRUPPI = "LOAD_GRUPPI";

	public final static String LOAD_IUV = "LOAD_IUV";

	public final static String LOAD_PAGA = "LOAD_PAGA";

	public final static String LOAD_PARAM_ELAB = "LOAD_PARAM_ELAB";

	public final static String LOAD_RATA = "LOAD_RATA";

	public final static String LOAD_STDEBI = "LOAD_STDEBI";

	public final static String POST_PUT_DEL_RISC = "POST_PUT_DEL_RISC";

	public final static String POST_PUT_DEL_SOGG = "POST_PUT_DEL_SOGG";

	public final static String LOAD_RISC = "LOAD_RISC";

	public final static String LOAD_SOGG = "LOAD_SOGG";

	public final static String LOAD_CAL_INTERESSI = "LOAD_CAL_INTERESSI";

	public final static String POST_PUT_DEL_DET_PAG = "POST_PUT_DEL_DET_PAG";

	public final static String LOAD_DET_PAG = "LOAD_DET_PAG";

	public final static Long ID_TIPOLOGIA_PAGAMENTO_MANUALE = 2L;

	public final static Long ID_TIPO_MODALITA_PAGAMENTO_MANUALE = 10L;

	public final static Integer FLAG_RIMBORSATO = 0;

	public final static String POST_FILE_450 = "POST_FILE_450";

	public final static String POST_AVV_PAGA = "POST_AVV_PAGA";
	
	public final static String POST_IUV = "POST_IUV";
	
	public final static String POST_LOTTO = "POST_LOTTO";
	
	public final static String POST_PAGOPA_SCOMP_RICH_IUV = "POST_PAGOPA_SCOMP_RICH_IUV";

	public final static String POST_PUT_DEL_lOC_RIS = "POST_PUT_DEL_lOC_RIS";

	public final static String LOAD_LOC_RIS = "LOAD_lOC_RIS";
	
	public final static String POST_RIMBORSO_SD_UTILIZZATO = "POST_RIMBORSO_SD_UTILIZZATO";
	
	public final static String LOAD_IMM = "LOAD_IMM";
	
	public final static String POST_PUT_DEL_IMM = "POST_PUT_DEL_IMM";
	
	public static final String POST_DELEGATO = "POST_DELEGATO";
	
	public final static String POST_PUT_DEL_USI = "POST_PUT_DEL_USI";

	public final static String LOAD_USI = "LOAD_USI";
	
	public final static String POST_DETT_PAG_BIL_ACC = "POST_DETT_PAG_BIL_ACC";
	public final static String DELETE_DETT_PAG_BIL_ACC = "DELETE_DETT_PAG_BIL_ACC";
	
	public static String A001 = "A001";
	public static String A002 = "A002";
	public static String A003 = "A003";
	public static String A004 = "A004";
	public static String A005 = "A005";
	public static String A006 = "A006";
	public static String A007 = "A007";
	public static String A008 = "A008";
	public static String A009 = "A009";
	public static String A010 = "A010";
	public static String A011 = "A011";
	public static String A012 = "A012";
	public static String A013 = "A013";
	public static String A014 = "A014";
	public static String A015 = "A015";
	public static String A016 = "A016";
	public static String A017 = "A017";
	public static String A018 = "A018";
	public static String A019 = "A019";
	public static String A020 = "A020";
	public static String A021 = "A021";
	public static String A022 = "A022";
	public static String A023 = "A023";
	public static String A024 = "A024";
	public static String A025 = "A025";
	public static String A026 = "A026";
	public static String A027 = "A027";
	public static String A028 = "A028";
	public static String A029 = "A029";
	public static String C001 = "C001";
	public static String C002 = "C002";
	public static String C003 = "C003";
	public static String C004 = "C004";
	public static String C005 = "C005";
	public static String E001 = "E001";
	public static String E002 = "E002";
	public static String E003 = "E003";
	public static String E004 = "E004";
	public static String E005 = "E005";
	public static String E006 = "E006";
	public static String E007 = "E007";
	public static String E008 = "E008";
	public static String E009 = "E009";
	public static String E010 = "E010";
	public static String E011 = "E011";
	public static String E012 = "E012";
	public static String E013 = "E013";
	public static String E014 = "E014";
	public static String E015 = "E015";
	public static String E016 = "E016";
	public static String E017 = "E017";
	public static String E018 = "E018";
	public static String E019 = "E019";
	public static String E020 = "E020";
	public static String E021 = "E021";
	public static String E022 = "E022";
	public static String E023 = "E023";
	public static String E024 = "E024";
	public static String E025 = "E025";
	public static String E026 = "E026";
	public static String E027 = "E027";
	public static String E028 = "E028";
	public static String E029 = "E029";
	public static String E030 = "E030";
	public static String E031 = "E031";
	public static String E032 = "E032";
	public static String E033 = "E033";
	public static String E035 = "E035";
	public static String E036 = "E036";
	public static String E037 = "E037";
	public static String E038 = "E038";
	public static String E039 = "E039";
	public static String E040 = "E040";
	public static String E041 = "E041";
	public static String E042 = "E042";
	public static String E043 = "E043";
	public static String E044 = "E044";
	public static String E045 = "E045";
	public static String E046 = "E046";
	public static String E047 = "E047";
	public static String E048 = "E048";
	public static String E049 = "E049";
	public static String E050 = "E050";
	public static String E051 = "E051";
	public static String E053 = "E053";
	public static String E054 = "E054";
	public static String E055 = "E055";
	public static String E056 = "E056";
	public static String E057 = "E057";
	public static String E058 = "E058";
	public static String E059 = "E059";
	public static String E060 = "E060";
	public static String E061 = "E061";
	public static String E062 = "E062";
	public static String E063 = "E063";
	public static String E064 = "E064";
	public static String E065 = "E065";
	public static String E066 = "E066";
	public static String E067 = "E067";
	public static String E068 = "E068";
	public static String E069 = "E069";
	public static String E070 = "E070";
	public static String E071 = "E071";
	public static String E072 = "E072";
	public static String E073 = "E073";
	public static String E074 = "E074";
	public static String E076 = "E076";
	public static String E077 = "E077";
	public static String E078 = "E078";
	public static String E079 = "E079";
	public static String E080 = "E080";
	public static String E081 = "E081";
	public static String E082 = "E082";
	public static String E083 = "E083";
	public static String E084 = "E084";
	public static String E085 = "E085";
	public static String E086 = "E086";
	public static String E087 = "E087";
	public static String E088 = "E088";
	public static String E089 = "E089";
	public static String E090 = "E090";
	public static String E091 = "E091";
	public static String E092 = "E092";
	public static String E093 = "E093";
	public static String E094 = "E094";
	public static String E095 = "E095";
	public static String E096 = "E096";
	public static String E097 = "E097";
	public static String E098 = "E098";
	public static String E099 = "E099";
	public static String E100 = "E100";
	public static String E101 = "E101";
	public static String E103 = "E103";
	public static String E104 = "E104";
	public static String E105 = "E105";
	public static String E106 = "E106";
	public static String E107 = "E107";	
	public static String E108 = "E108";
	public static String E109 = "E109";
	public static String E112 = "E112";
	public static String E114 = "E114";
	public static String F001 = "F001";
	public static String F002 = "F002";
	public static String F003 = "F003";
	public static String F004 = "F004";
	public static String F005 = "F005";
	public static String F006 = "F006";
	public static String F007 = "F007";
	public static String F008 = "F008";
	public static String F009 = "F009";
	public static String F010 = "F010";
	public static String I001 = "I001";
	public static String I002 = "I002";
	public static String I003 = "I003";
	public static String I004 = "I004";
	public static String I005 = "I005";
	public static String I006 = "I006";
	public static String I007 = "I007";
	public static String I008 = "I008";
	public static String I009 = "I009";
	public static String I010 = "I010";
	public static String I011 = "I011";
	public static String I012 = "I012";
	public static String I013 = "I013";
	public static String I014 = "I014";
	public static String I015 = "I015";
	public static String I016 = "I016";
	public static String I017 = "I017";
	public static String I018 = "I018";
	public static String I019 = "I019";
	public static String I020 = "I020";
	public static String I021 = "I021";
	public static String I022 = "I022";
	public static String I023 = "I023";
	public static String I024 = "I024";
	public static String I025 = "I025";
	public static String I026 = "I026";
	public static String I034 = "I034";
	public static String I040 = "I040";
	public static String P001 = "P001";
	public static String P002 = "P002";
	public static String P006 = "P006";
	public static String P007 = "P007";
	public static String P008 = "P008";
	public static String U001 = "U001";
	public static String U002 = "U002";
	public static String U003 = "U003";
	public static String U004 = "U004";
	public static String BATCH_PORTING= "BATCH_PORTING";
	public static String RICMOR01 = "RICMOR01";
	public static String RICMOR02 = "RICMOR02";
	public static String RICMOR03 = "RICMOR03";
	public static String RICMOR04 = "RICMOR04";
	public static String RICMOR05 = "RICMOR05";
	public static String RICMOR06 = "RICMOR06";
	public static String RICMOR07 = "RICMOR07";
	public static String RICMOR08 = "RICMOR08";
	public static String RICMOR09 = "RICMOR09";
	public static String RICMOR10 = "RICMOR10";
	public static String RICMOR11 = "RICMOR11";
	public static String RICMOR12 = "RICMOR12";
	public static String RICMOR13 = "RICMOR13";
	public static String RICMOR14 = "RICMOR14";
	public static String RICMOR15 = "RICMOR15";
	public static String RICMOR16 = "RICMOR16";
	public static final String COD_TIPO_ELABORA_BS = "BS";
	public static final String COD_TIPO_ELABORA_BO = "BO";
	public static final String CONCESSIONE = "CONCESSIONE" ;
	public static final String ATTINGIMENTO = "ATTINGIMENTO";
	public static final String ATTINGIMENTO_PLURI = "ATTINGIMENTO_PLURI";
	public static final String AUTORIZZAZIONE_PROVV = "AUTORIZZAZIONE_PROVV";
	public static final String TIPO_AUT_VUOTA = "TIPO_AUT_VUOTA";
	public static final String TIPO_TRIBUTI = "TIPO_TRIBUTI";
	public static final String RICRIM01 = "RICRIM01";
	public static final String RICRIM02 = "RICRIM02";
	public static final String RICRIM03 = "RICRIM03";
	public static final String RICRIM04 = "RICRIM04";
	public static final String RICRIM05 = "RICRIM05";
	public static final String RICRIM06 = "RICRIM06";
	public static final String DA_CONFERMARE = "DA CONFERMARE";
	public static final String DA_RIMBORSARE = "DA RIMBORSARE";
	public static final String DA_VISIONARE = "DA VISIONARE";
	public static final String NON_DI_COMPETENZA = "NON DI COMPETENZA";
	public static final String NON_IDENTIFICATO = "NON IDENTIFICATO";
	public static final String RIMBORSATO = "RIMBORSATO";
	public static final String COLLEGATO = "COLLEGATO";
	

	public static final String AMBIENTE = "AMBIENTE";
	public static final String OPERE_PUBBLICHE = "Opere pubbliche";
	public static final String ATTIVITA_ESTRATTIVE = "Attivita estrattive";
	public static final String TRIBUTI = "TRIBUTI";
	
    public static final String ERRORE_GENERICO = "ERRORE NELLA QUERY "; 
    
    public static final String ATTORE_RISCA = "Attore-Risca"; 
    
	public static final String DATA_SCONOSCIUTA = "0001-01-01";
	
	public static final Integer FLAG_ACCERTAMENTO_TRUE = 1;
	public static final Integer FLAG_DILAZIONE_FALSE = 0;
	public static final Integer FLAG_DILAZIONE_TRUE = 1;
	public static final String  GEST_ATTORE_INS =" getGestAttoreIns";
	public static final String  DATA_PROTOCOLLO_NULL = "31/12/9999";
	public static final Integer FLAG_ANNULLATO_FALSE = 0;
	public static final String  GEST_ATTORE_STATO_CONTRIBUZIONE = "BATCH_STATO_CONT";
	
	public static String BATCH= "BATCH";
	public static String BATCH_STATO_CONT = "BATCH_STATO_CONT";
	public static String MIGRAZIONE= "MIGRAZIONE";
	public static String SHELL = "SHELL";
	public static String RISCABATCHARC = "riscabatcharc";

	public static String RISCA_FO= "RISCA_FO";
	public static String STATO_CONTRIBUZIONE = "STATO_CONTRIBUZIONE";
	
	public static final String FORMAT_DATE = "yyyyMMdd-HHmmss";
	
	public static final String M00 = "M00";
	public static final String M10 = "M10";
	public static final String M20 = "M20";
	public static final String M30 = "M30";
	public static final String M50 = "M50";
	public static final String M99 = "M99";
	
	public static final String PRESENZA_COMPONENTE_GRUPPO = "C";
	public static final String INTESTATARIO_GRUPPO = "2";
	public static final String ASSENZA_COMPONENTE_GRUPPO = "1";
	public static final String FLG_CAPOGRUPPO_PRESENTE ="1";
	
	public static final String CODICE_ENTRATA_1R96 = "1R96";
	public static final String CODICE_ENTRATA_1R97 = "1R97";
	public static final String CODICE_ENTRATA_1R98 = "1R98";
	public static final String TIPO_CODICE_ENTRATA_I = "I";
	public static final String TIPO_CODICE_ENTRATA_T = "T";
	public static final String TIPO_CODICE_ENTRATA_A = "A";
	
	public static final String NOTA_ELABORA_FILE_450_KO = "KO - Produzione File 450 Invio e creazione ruolo per Riscossione Coattiva: ";
	public static final String NOTA_ELABORA_FILE_450_OK = "OK - Creazione File 450 N. posizioni debitorie scritte, per riscossione coattiva: ";
	
	public static final String MOTIVAZIONE_ISCRIZIONE_COMPIUTA_GIACENZA= "COMPIUTA GIACENZA";
	
	public static final String NOME_FILE_450 = "R_P_S4_Canoni_Acque-";
	
	public static final String AMBITO_CONFIG_CHIAVE_MAIL_DEST = "SRVAPP.DestinatarioServizioApplicativo";
	public static final String AMBITO_CONFIG_CHIAVE_MAIL_MITT = "SRVAPP.MittenteSegnalazioniServizioApplicativo";
	public static final String AMBITO_CONFIG_CHIAVE_NOME_ALLEGATO = "SRVAPP.NomeAllegatoServizioApplicativo";
	public static final String AMBITO_CONFIG_CHIAVE_MAIL_USERNAME = "SRVAPP.UsernameServizioApplicativo";
	public static final String AMBITO_CONFIG_CHIAVE_MAIL_PASSWORD = "SRVAPP.PasswordServizioApplicativo";
	
	public static final String DATE_FORMAT_MAIL = "dd/MM/yyyy";
	public static final String HOUR_FORMAT_MAIL = "HH:mm:ss";


	
	
}