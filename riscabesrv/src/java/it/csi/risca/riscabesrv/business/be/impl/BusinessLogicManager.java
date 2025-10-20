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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.BusinessLogicFile450;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AccertamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiConfigDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitoFonteDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitoInteresseDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaUsoSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaUsoSdRaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoAnnualitaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoDatiAmminDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoDatiTitolareDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoPagamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoUsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.BilAccDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.CalcoloInteresseDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DelegheDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagBilAccDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.EmailServizioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.File450DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.FonteDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.GruppiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.GruppoDelegaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.IndirizziSpedizioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.IuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.IuvDaInviareDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.LottoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagNonPropriDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagopaScompRichIuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RecapitoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoSdUtilizzatoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RuoloDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettoDelegaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettoGruppoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDatiTitolareDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TableColumnDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoModalitaPagDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoRicercaMorositaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.StatoDebitorioDAOImpl;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.Soris00CDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.Soris99CDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr0DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr1DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr3DAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.SorisFr7DAO;
import it.csi.risca.riscabesrv.dto.AccertamentoDTO;
import it.csi.risca.riscabesrv.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.AmbitoFonteExtendedDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdDTO;
import it.csi.risca.riscabesrv.dto.AssegnaPagamentoDTO;
import it.csi.risca.riscabesrv.dto.AttivitaStatoDebitorioDTO;
import it.csi.risca.riscabesrv.dto.AvvisoPagamentoDTO;
import it.csi.risca.riscabesrv.dto.BilAccDTO;
import it.csi.risca.riscabesrv.dto.DTOValidator;
import it.csi.risca.riscabesrv.dto.DelegatoExtendedDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagExtendedDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagListDTO;
import it.csi.risca.riscabesrv.dto.ElaboraDTO;
import it.csi.risca.riscabesrv.dto.EmailServizioDTO;
import it.csi.risca.riscabesrv.dto.ErrorObjectDTO;
import it.csi.risca.riscabesrv.dto.File450DTO;
import it.csi.risca.riscabesrv.dto.FonteDTO;
import it.csi.risca.riscabesrv.dto.GruppiDTO;
import it.csi.risca.riscabesrv.dto.GruppiSoggettoDTO;
import it.csi.risca.riscabesrv.dto.GruppoDelegaDTO;
import it.csi.risca.riscabesrv.dto.GruppoDelegaExtendedDTO;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscabesrv.dto.IuvDTO;
import it.csi.risca.riscabesrv.dto.IuvDaInviareDTO;
import it.csi.risca.riscabesrv.dto.IuvExtendedDTO;
import it.csi.risca.riscabesrv.dto.LottoDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.OutputColonnaDTO;
import it.csi.risca.riscabesrv.dto.OutputDatiDTO;
import it.csi.risca.riscabesrv.dto.OutputFileDTO;
import it.csi.risca.riscabesrv.dto.OutputFoglioDTO;
import it.csi.risca.riscabesrv.dto.PagNonPropriExtendedDTO;
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.PagamentoRataSdExtendedDTO;
import it.csi.risca.riscabesrv.dto.PagopaScompRichIuvDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RataSdExtendedDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraExtendedDTO;
import it.csi.risca.riscabesrv.dto.ReportResultDTO;
import it.csi.risca.riscabesrv.dto.RimborsoSdUtilizzatoDTO;
import it.csi.risca.riscabesrv.dto.RuoloDTO;
import it.csi.risca.riscabesrv.dto.SoggettiDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.dto.SoggettoDelegaDTO;
import it.csi.risca.riscabesrv.dto.SoggettoDelegaExtendedDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.StatoElaborazioneDTO;
import it.csi.risca.riscabesrv.dto.TipiAccertamentoDTO;
import it.csi.risca.riscabesrv.dto.TipoElaboraExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoModalitaPagDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.ErrorMessages;
import it.csi.risca.riscabesrv.util.Utils;
import it.csi.risca.riscabesrv.util.download.DownloadManager;
import it.csi.risca.riscabesrv.util.mail.AttachmentData;
import it.csi.risca.riscabesrv.util.mail.MailException;
import it.csi.risca.riscabesrv.util.mail.MailInfo;
import it.csi.risca.riscabesrv.util.mail.MailManager;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class BusinessLogicManager  implements BusinessLogic
{ 
	protected transient Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);

	@Context 
	private ServletContext context;

	@Autowired
	private RecapitoDAO recapitoDAO;

	@Autowired
	private SoggettiDAO soggettoDAO;

	@Autowired
	private GruppiDAO gruppiDAO;

	@Autowired
	private IndirizziSpedizioneDAO indirizziSpedizioneDAO;

	@Autowired
	private AvvisoPagamentoDAO avvisoPagamentoDAO;

	@Autowired
	private RataSdDAO rataSdDAO;

	@Autowired
	private TipoModalitaPagDAO tipoModalitaPagDAO;

	@Autowired
	private AmbitoFonteDAO ambitoFonteDao;

	@Autowired
	private FonteDAO fonteDao;

	@Autowired
	private File450DAO file450DAO;

	@Autowired
	private IuvDAO iuvDAO;

	@Autowired
	private LottoDAO lottoDAO;

	@Autowired
	private PagopaScompRichIuvDAO pagopaScompRichIuvDAO;

	@Autowired
	private RimborsoSdUtilizzatoDAO rimborsoSdUtilizzatoDAO;

	@Autowired
	private CalcoloInteresseDAO calcoloInteresseDAO;

	@Autowired
	private PagamentoDAO pagamentoDAO;

	@Autowired
	private PagNonPropriDAO pagNonPropriDAO;

	@Autowired
	private DettaglioPagDAO dettaglioPagDAO;

	@Autowired
	private DettaglioPagBilAccDAO dettaglioPagBilAccDAO;

	@Autowired
	private MessaggiDAO messaggiDAO;

	@Autowired
	private StatoDebitorioDAO statoDebitorioDAO;
	@Autowired
	private AvvisoDatiTitolareDAO avvisoDatiTitolareDAO;
	@Autowired
	private AvvisoDatiAmminDAO avvisoDatiAmminDAO;
	@Autowired
	private AvvisoAnnualitaDAO avvisoAnnualitaDAO;
	@Autowired
	private AvvisoUsoDAO avvisoUsoDAO;
	@Autowired
	private AnnualitaSdDAO annualitaSdDAO;
	@Autowired
	private AnnualitaUsoSdRaDAO annualitaUsoSdRaDAO;
	@Autowired
	private AnnualitaUsoSdDAO annualitaUsoSdDAO;

	@Autowired
	private DelegheDAO delegatoDAO;

	@Autowired
	private SoggettoDelegaDAO soggettoDelegaDAO;  

	@Autowired
	private GruppoDelegaDAO gruppoDelegaDAO;  

	@Autowired
	private AmbitoInteresseDAO ambitoInteresseDAO;

	@Autowired
	private AccertamentoDAO accertamentoDAO;

	@Autowired
	private  TableColumnDAO tableColumnDAO;

	@Autowired
	private IuvDaInviareDAO iuvDaInviareDAO;	

	@Autowired
	private SoggettoGruppoDAO soggettoGruppoDAO;

	@Autowired
	private BilAccDAO bilAccDAO;

	private BigDecimal interessi = null;

	private BigDecimal interesRitPag = null;


	@Autowired
	private ElaboraDAO elaboraDAO;

	@Autowired
	private DownloadManager downloadManager;	

	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;

	@Autowired
	private BusinessLogicFile450 businessLogicFile450;

	@Autowired
	private RuoloDAO ruoloDAO;

	@Autowired
	private AmbitiConfigDAO ambitiConfigDAO;

	@Autowired
	private EmailServizioDAO emailServizioDAO;

	@Autowired
	private TipoRicercaMorositaDAO tipoRicercaMorositaDAO;
	
	@Autowired
	private Soris00CDAO soris00CDAO;
	
	@Autowired
	private SorisFr0DAO sorisFr0DAO;
	
	@Autowired
	private SorisFr1DAO sorisFr1DAO;
	
	@Autowired
	private SorisFr3DAO sorisFr3DAO;
	
	@Autowired
	private SorisFr7DAO sorisFr7DAO;
	
	@Autowired
	private Soris99CDAO soris99CDAO;
	
	

	@Autowired
	private MailManager mailManager;

	private Long idStatoDebitorioEmail = null;
	private String codiceUtenzaEmail = null;
	
	private static final String COD_ATTIVITA_SD_FALLIMENTO = "FA";
	private static final String COD_ATTIVITA_SD_DA_VERIFICARE = "DV";
	private static final String COD_ATTIVITA_SD_NON_ESIGIBILE = "CI";
	private static final String COD_ATTIVITA_SD_CONCORDATO = "CO";
	
	
    @Autowired
    private TracciamentoManager tracciamentoManager;

	@Override
	public RecapitiExtendedDTO readRecapitoByPk(Long idRecapito)throws DAOException, SystemException{
		RecapitiExtendedDTO recapito;
		LOG.debug("[BusinessLogicManager : readRecapitiByPk ] ");
		recapito = recapitoDAO.getRecapitiExtendedByIdRecapito(idRecapito);
		return recapito;

	}

	@Override
	public RecapitiExtendedDTO createRecapito(RecapitiExtendedDTO recapito) throws DAOException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : createRecapito] param recapito = " + recapito);

		//controllo che ci sia il recapito principale prima di inserire l'alternativo
		long countRecapitoPrincipale = recapitoDAO.countRecapitoPrincipaleByIdSoggetto(recapito.getIdSoggetto());
		if(countRecapitoPrincipale == 0) {
			LOG.debug("[BusinessLogicManager::createRecapito]: recapito principale non ancora presente " );
		}else {
			if(countRecapitoPrincipale > 0 && recapito.getTipoRecapito().getIdTipoRecapito() == 1 ) {
				ErrorObjectDTO error = new ErrorObjectDTO();
				error.setCode(ErrorMessages.CODE_E023_RECAPITO_PRINCIPALE_GIA_PRESENTE);
				error.setTitle(ErrorMessages.MESSAGE_E023_RECAPITO_PRINCIPALE_GIA_PRESENTE);
				throw new DatiInputErratiException(error) ;
			}
		}

		boolean isValid = true;

		recapito = recapitoDAO.createRecapito(recapito);

		SoggettiExtendedDTO soggettoDto = null;
		try {
			soggettoDto = soggettoDAO.loadSoggettoById(recapito.getIdSoggetto());
		} catch (Exception e) {
			LOG.error(e);
		}

		//inserisco gli indirizzi di spedizione 
		List<IndirizzoSpedizioneDTO> listIndirizzoSpedizione = indirizziSpedizioneDAO.getIndirizzoSpedizioneByIdRecapitoAndIdGruppo(recapito.getIdRecapito(), null);
		if (listIndirizzoSpedizione.isEmpty()) {
			Number idRecapito = recapito.getIdRecapito(); 
			if (recapito.getIndirizziSpedizione() == null || recapito.getIndirizziSpedizione().isEmpty()) {
				IndirizzoSpedizioneDTO indirizzoSpedizioneDTO = new IndirizzoSpedizioneDTO();
				indirizzoSpedizioneDTO.setIdRecapito(idRecapito.longValue());
				if (soggettoDto.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_FISICA)) {
					indirizzoSpedizioneDTO.setDestinatarioPostel(soggettoDto.getCognome() + " " + soggettoDto.getNome());
				}
				if (soggettoDto.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PUBBLICA)
						|| soggettoDto.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PRIVATA)) {
					indirizzoSpedizioneDTO.setDestinatarioPostel(soggettoDto.getDenSoggetto());
				}

				if (recapito.getPresso() != null) {
					if (recapito.getPresso().trim().equals(indirizzoSpedizioneDTO.getDestinatarioPostel().trim()) ||  recapito.getPresso().trim().equals("N.A. N.A.".trim())) {
						indirizzoSpedizioneDTO.setPressoPostel(null);
					}else {
						indirizzoSpedizioneDTO.setPressoPostel("C/O " + recapito.getPresso());
					}
				}

				indirizzoSpedizioneDTO.setIndirizzoPostel(recapito.getIndirizzo() + ", "
						+ recapito.getNumCivico());

				if (recapito.getNazioneRecapito().getDenomNazione().equals(Constants.DB.NAZIONE.DENOM_NAZIONE_ITALIA)) {
					//[DP] aggiunto controllo null
					if(recapito.getComuneRecapito()!=null) {
						indirizzoSpedizioneDTO.setCittaPostel(recapito.getComuneRecapito().getDenomComune());
					}
				} else {
					indirizzoSpedizioneDTO.setCittaPostel(recapito.getCittaEsteraRecapito());
				}
				if (recapito.getNazioneRecapito().getDenomNazione().equals(Constants.DB.NAZIONE.DENOM_NAZIONE_ITALIA)) {
					//[DP] aggiunto controllo null
					if(recapito.getComuneRecapito()!=null) {
						indirizzoSpedizioneDTO.setProvinciaPostel(
								recapito.getComuneRecapito().getProvincia().getSiglaProvincia());
					}
				}
				indirizzoSpedizioneDTO.setCapPostel(recapito.getCapRecapito());
				if(recapito.getDesLocalita() != null)
					indirizzoSpedizioneDTO.setFrazionePostel(recapito.getDesLocalita());

				// da chiarire questo punto della nazione 
				//				if (!recapito.getNazioneRecapito().getDenomNazione().equals(Constants.DB.NAZIONE.DENOM_NAZIONE_ITALIA)) {
				//					indirizzoSpedizioneDTO.setNazionePostel(recapito.getNazioneRecapito().getDenomNazione());
				//				}else {
				indirizzoSpedizioneDTO.setNazionePostel(recapito.getNazioneRecapito().getDenomNazione());
				//				}
				indirizzoSpedizioneDTO.setGestAttoreIns(soggettoDto.getGestAttoreIns());
				indirizzoSpedizioneDTO.setGestAttoreUpd(soggettoDto.getGestAttoreUpd());
				try {
					indirizzoSpedizioneDTO = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizioneDTO);
                    listIndirizzoSpedizione.add(indirizzoSpedizioneDTO);
					
					//[DP] issue 87
                    if(soggettoDto.getGruppoSoggetto()!=null && !soggettoDto.getGruppoSoggetto().isEmpty()) {
                    	for (int i = 0; i < soggettoDto.getGruppoSoggetto().size(); i++) {
                    		//se id soggetto e' all'interno del componenteGruppo inserire indirizzo spedizione con id gruppo soggetto solo se e' capogruppo 
                    		for (int j = 0; j < soggettoDto.getGruppoSoggetto().get(i).getComponentiGruppo().size(); j++) {
								if ((soggettoDto.getGruppoSoggetto().get(i).getComponentiGruppo().get(j).getIdSoggetto().equals(soggettoDto.getIdSoggetto())
										&& soggettoDto.getGruppoSoggetto().get(i).getComponentiGruppo().get(j).getFlgCapoGruppo() == 1 )) {
									indirizzoSpedizioneDTO.setIdGruppoSoggetto(soggettoDto.getGruppoSoggetto().get(i).getIdGruppoSoggetto());
		                    		indirizzoSpedizioneDTO = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizioneDTO);
		                            listIndirizzoSpedizione.add(indirizzoSpedizioneDTO);
								}							                    		
                    		}
						}
                    }
                    
                    
				} catch (Exception e) {
					LOG.error(e);
				}
				
				recapito.setIndirizziSpedizione(listIndirizzoSpedizione);
				recapito.setIdRecapito(idRecapito.longValue());
				if (indirizzoSpedizioneDTO.getIndValidoPostel().equals(0l)) {
					isValid = false;
				}
			}
			else {
				List<IndirizzoSpedizioneDTO> ListIndirizzoSpedizioneDTO = recapito.getIndirizziSpedizione();
				IndirizzoSpedizioneDTO indirizzoSpedizione= null;
				if(!ListIndirizzoSpedizioneDTO.isEmpty()) {
					indirizzoSpedizione = ListIndirizzoSpedizioneDTO.get(0);
					indirizzoSpedizione.setIdRecapito(idRecapito.longValue());
					indirizzoSpedizione.setGestAttoreIns(soggettoDto.getGestAttoreIns());
					indirizzoSpedizione.setGestAttoreUpd(soggettoDto.getGestAttoreUpd());
				}

				try {
					indirizzoSpedizione = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizione);
				} catch (Exception e) {
					LOG.error(e);
				}
				recapito.setIdRecapito(idRecapito.longValue());
				if (indirizzoSpedizione.getIndValidoPostel().equals(0l)) {
					isValid = false;
				}
			}
		}

		if (!isValid) {      
			ErrorObjectDTO error = new ErrorObjectDTO();
			error.setCode(ErrorMessages.CODE_E065_FORMATO_INDIRIZZI_SPEDIZIONE_NON_VALIDO);
			error.setTitle(ErrorMessages.MESSAGE_E065_FORMATO_INDIRIZZI_SPEDIZIONE_NON_VALIDO);
			error.setStatus("400");
			throw new DatiInputErratiException(error) ;
		}

		return recapitoDAO.getRecapitiExtendedByIdRecapito(recapito.getIdRecapito());
	}

	@Override
	public RecapitiExtendedDTO updateRecapito(RecapitiExtendedDTO recapito,SoggettiExtendedDTO soggettiExtendedOLD, SoggettiExtendedDTO soggettiExtendedDTO,Long indModManuale)
			throws DAOException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : updateRecapito] param recapito = " + recapito);
		boolean isValid = true;		
			List<IndirizzoSpedizioneDTO> listIndirizzoSpedizione = indirizziSpedizioneDAO.getIndirizzoSpedizioneByIdRecapitoAndIdGruppo(recapito.getIdRecapito(), null);
			long countRecapito = recapitoDAO.countRecapitoByIdSoggettoAndIdRecapito(recapito.getIdSoggetto(), recapito.getIdRecapito());

			if(countRecapito > 0) {
				//				AGGIORNARE RECAPITO 
				recapito = recapitoDAO.updateRecapito(recapito);

				IndirizzoSpedizioneDTO indirizzoSpedizione = null;
				List<IndirizzoSpedizioneDTO> listIndirizzoSpedizioneDTO = recapito.getIndirizziSpedizione();
				List<IndirizzoSpedizioneDTO> listIndirizzoSpedizioneDTONew  = new ArrayList<>();
		//[DP] controllo listIndirizzoSpedizioneDTO null
			if(listIndirizzoSpedizioneDTO != null && !listIndirizzoSpedizioneDTO.isEmpty()) {

					for (IndirizzoSpedizioneDTO indirizzoDTO : listIndirizzoSpedizioneDTO) {
						indirizzoSpedizione = indirizzoDTO;
						if(indModManuale== null || indModManuale.equals(0L)) {
							//						AGGIORNARE RECAPITO DI INDIRIZZO DI SPEDIZIONE 
							// recupero soggetto prima di fare update per il controllo dell indirizzi di spedizione 
							//  controllo che riguarda solo il recapito modificato
							RecapitiExtendedDTO recapitoOld =null;
							if(soggettiExtendedOLD != null) {
								List<RecapitiExtendedDTO> listRecapiti = soggettiExtendedOLD.getRecapiti().stream().collect(Collectors.toList());
								Long idRecapito = recapito.getIdRecapito();
								recapitoOld = listRecapiti.stream().filter(r -> r.getIdRecapito().equals(idRecapito)).collect(Collectors.toList()).get(0);
							}else {
								recapitoOld = readRecapitoByPk(recapito.getIdRecapito());
							}

							boolean isCambiato = false;

							if(recapito.getPresso() != null)     
								if(!recapito.getPresso().equals(recapitoOld.getPresso())) {
									isCambiato = true;
								}

							if(indirizzoSpedizione.getGestAttoreUpd() == null)
								indirizzoSpedizione.setGestAttoreUpd(recapito.getGestAttoreUpd());
							if(indirizzoSpedizione.getGestAttoreIns() == null)
								indirizzoSpedizione.setGestAttoreIns(recapito.getGestAttoreIns());

							if (!recapito.getIndirizzo().equals(recapitoOld.getIndirizzo())
									|| 	!recapito.getNumCivico().equals(recapitoOld.getNumCivico())	
									) {
								indirizzoSpedizione.setIndirizzoPostel(recapito.getIndirizzo() + ", "
										+ recapito.getNumCivico());
								isCambiato = true;
							}
							if(recapito.getComuneRecapito() != null) {
								if (!recapito.getComuneRecapito().getDenomComune().equals(recapitoOld.getComuneRecapito().getDenomComune())) {
									if (recapito.getNazioneRecapito().getDenomNazione().equals(Constants.DB.NAZIONE.DENOM_NAZIONE_ITALIA)) {
										indirizzoSpedizione.setCittaPostel(recapito.getComuneRecapito().getDenomComune());
										isCambiato = true;
									} 
								}
							}
							else {

								if (!recapito.getNazioneRecapito().getDenomNazione().equals(Constants.DB.NAZIONE.DENOM_NAZIONE_ITALIA)) {
									indirizzoSpedizione.setCittaPostel(recapito.getCittaEsteraRecapito());
									isCambiato = true;
								}

							}
							if (!recapito.getCapRecapito().equals(recapitoOld.getCapRecapito())) {
								indirizzoSpedizione.setCapPostel(recapito.getCapRecapito());
								isCambiato = true;
							}
							if(recapito.getDesLocalita() != null)
								if (!recapito.getDesLocalita().equals(recapitoOld.getDesLocalita())) {
									indirizzoSpedizione.setFrazionePostel(recapito.getDesLocalita());
									isCambiato = true;
								}
							if (!recapito.getNazioneRecapito().getDenomNazione().equals(recapitoOld.getNazioneRecapito().getDenomNazione())) {
								//							if (!recapito.getNazioneRecapito().getDenomNazione().equals(Constants.DB.NAZIONE.DENOM_NAZIONE_ITALIA)) {
								//								indirizzoSpedizione.setNazionePostel(recapito.getNazioneRecapito().getDenomNazione());
								//								isCambiato = true;
								//							}else {
								indirizzoSpedizione.setNazionePostel(recapito.getNazioneRecapito().getDenomNazione());
								isCambiato = true;
								//							}
							}
							listIndirizzoSpedizioneDTO  = new ArrayList<>();
							try {
								if (indirizzoSpedizione.getIdGruppoSoggetto() != null) {
									IndirizzoSpedizioneDTO indirizzoSpedizioneDTO = indirizzoSpedizione;
									if (recapito.getPresso() != null && StringUtils.isNotBlank(recapito.getPresso())) {
										if (!recapito.getPresso().equals(recapitoOld.getPresso())) {
											if  ((!recapito.getPresso().trim().equals(indirizzoSpedizione.getDestinatarioPostel().trim()) &&  !recapito.getPresso().trim().equals("N.A. N.A.".trim())))
												indirizzoSpedizione.setPressoPostel("C/O " + recapito.getPresso());
											else
												indirizzoSpedizione.setPressoPostel(null);
										}
									} else {
										if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_FISICA)) {
											indirizzoSpedizioneDTO.setPressoPostel(
													"C/O " + soggettiExtendedDTO.getCognome() + " " + soggettiExtendedDTO.getNome());
											isCambiato = true;
										} else if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PRIVATA)
												|| soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto()
												.equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PUBBLICA)) {
											indirizzoSpedizioneDTO.setPressoPostel("C/O " + soggettiExtendedDTO.getDenSoggetto());
											isCambiato = true;
										}

									}

									try {

										if(isCambiato) {
											List<IndirizzoSpedizioneDTO> listIndirizzoSpedizioneGruppi = indirizziSpedizioneDAO
													.getIndirizzoSpedizioneByIdRecapitoAndIdGruppo(indirizzoSpedizione.getIdRecapito(),
															Arrays.asList(indirizzoSpedizione.getIdGruppoSoggetto()));
											if (listIndirizzoSpedizioneGruppi.isEmpty()) {
												try {
													indirizzoSpedizioneDTO = indirizziSpedizioneDAO
															.saveIndirizziSpedizione(indirizzoSpedizioneDTO);
												} catch (Exception e) {
													LOG.error(e);
												}
												if (indirizzoSpedizioneDTO.getIndValidoPostel().equals(0l)) {
													isValid = false;
												}
											} else
												indirizzoSpedizioneDTO = indirizziSpedizioneDAO
												.updateIndirizziSpedizione(indirizzoSpedizioneDTO, 0l);
										}


										listIndirizzoSpedizioneDTONew.add(indirizzoSpedizioneDTO);

									} catch (GenericExceptionList e) {
										indirizzoSpedizioneDTO.setIndValidoPostel(0L);
										isValid = false;
										listIndirizzoSpedizioneDTONew.add(indirizzoSpedizioneDTO);
									}



								}else {
									// controllo per il soggetto
									boolean isCambiatoDestinatario = false;
									//TODO COMMENTO MOMENTANEAMENTE .DA VERIFICARE!!!!!!!!!!!!!!!!!!!!!!
									if(soggettiExtendedOLD != null) {
										if (soggettiExtendedDTO.getNome() != null)
											if (!soggettiExtendedDTO.getNome().equals(soggettiExtendedOLD.getNome())) {
												isCambiatoDestinatario = true;
											}
										if (soggettiExtendedDTO.getCognome() != null)
											if (!soggettiExtendedDTO.getCognome().equals(soggettiExtendedOLD.getCognome())) {
												isCambiatoDestinatario = true;
											}
										if (soggettiExtendedDTO.getDenSoggetto() != null)
											if (!soggettiExtendedDTO.getDenSoggetto().equals(soggettiExtendedOLD.getDenSoggetto())) {
												isCambiatoDestinatario = true;
											}	
									}


									if(isCambiatoDestinatario) {
										if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_FISICA)) {
											indirizzoSpedizione.setDestinatarioPostel(soggettiExtendedDTO.getCognome() + " " + soggettiExtendedDTO.getNome());
											isCambiato = true;
										}
										if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PUBBLICA)
												|| soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PRIVATA)) {
											indirizzoSpedizione.setDestinatarioPostel(soggettiExtendedDTO.getDenSoggetto());
											isCambiato = true;
										}
									}
									//controllo per il recapito
									if (recapito.getPresso() != null) {
										if (!recapito.getPresso().equals(recapitoOld.getPresso())) {
											if (StringUtils.isNotBlank(recapito.getPresso()) &&
													(!recapito.getPresso().trim().equals(indirizzoSpedizione.getDestinatarioPostel().trim()) && !recapito.getPresso().trim().equals("N.A. N.A.".trim())))
												indirizzoSpedizione.setPressoPostel("C/O " + recapito.getPresso());
											else
												indirizzoSpedizione.setPressoPostel(null);
											isCambiato = true;
										}	
										if (StringUtils.isNotBlank(recapito.getPresso()) &&
												(recapito.getPresso().trim().equals(indirizzoSpedizione.getDestinatarioPostel().trim()) || recapito.getPresso().trim().equals("N.A. N.A.".trim())))
											indirizzoSpedizione.setPressoPostel(null);
									}


									if(isCambiato) {
										indirizzoSpedizione.setIdGruppoSoggetto(null);
										List<IndirizzoSpedizioneDTO> listIndirizzoSpedizioni = indirizziSpedizioneDAO
												.getIndirizzoSpedizioneByIdRecapitoAndIdGruppo(
														recapito.getIdRecapito(), null);
										listIndirizzoSpedizioni = listIndirizzoSpedizioni.stream()
												.filter(r -> r.getIdGruppoSoggetto() == null)
												.collect(Collectors.toList());
										if (listIndirizzoSpedizioni.isEmpty()) {
											indirizzoSpedizione.setGestAttoreIns(recapito.getGestAttoreIns());
											indirizzoSpedizione.setGestAttoreUpd(recapito.getGestAttoreUpd());
											try {
												indirizzoSpedizione = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizione);
											} catch (Exception e) {
												LOG.error(e);
											}
											if (indirizzoSpedizione.getIndValidoPostel().equals(0l)) {
												isValid = false;
											}
										} else
											indirizzoSpedizione = indirizziSpedizioneDAO.updateIndirizziSpedizione(indirizzoSpedizione, 0l);
									}
									listIndirizzoSpedizioneDTONew.add(indirizzoSpedizione);

								}

							} catch (GenericExceptionList e) {
								indirizzoSpedizione.setIndValidoPostel(0L);
								listIndirizzoSpedizioneDTONew.add(indirizzoSpedizione);

								isValid = false;	
							}
						}else {
							try {
								indirizzoSpedizione = indirizziSpedizioneDAO.updateIndirizziSpedizione(indirizzoSpedizione, 0l);
							} catch (GenericExceptionList e) {
								indirizzoSpedizione.setIndValidoPostel(0L);
								listIndirizzoSpedizioneDTONew.add(indirizzoSpedizione);
								isValid = false;
							}
						}
					}

				}else {
//					//[DP] aggiunto controllo su indirizzoSpedizione null
					if(recapito.getIndirizziSpedizione() == null) {
						List<IndirizzoSpedizioneDTO> indirizzoSpedizioneList = new ArrayList<IndirizzoSpedizioneDTO>();
						recapito.setIndirizziSpedizione(indirizzoSpedizioneList);
					}
					if (recapito.getIndirizziSpedizione().isEmpty()) {
						// INSERIMENTO NUOVO INDIRIZZO DI SPEDIZIONE 
						IndirizzoSpedizioneDTO indirizzoSpedizioneDTO = new IndirizzoSpedizioneDTO();
						indirizzoSpedizioneDTO.setIdRecapito(recapito.getIdRecapito());

						indirizzoSpedizioneDTO.setIndirizzoPostel(recapito.getIndirizzo() + ", "
								+ recapito.getNumCivico());

						if (recapito.getNazioneRecapito().getDenomNazione().equals(Constants.DB.NAZIONE.DENOM_NAZIONE_ITALIA)) {
							indirizzoSpedizioneDTO.setCittaPostel(recapito.getComuneRecapito().getDenomComune());
						} else {
							indirizzoSpedizioneDTO.setCittaPostel(recapito.getCittaEsteraRecapito());
						}
						if (recapito.getNazioneRecapito().getDenomNazione().equals(Constants.DB.NAZIONE.DENOM_NAZIONE_ITALIA)) {
							indirizzoSpedizioneDTO.setProvinciaPostel(
									recapito.getComuneRecapito().getProvincia().getSiglaProvincia());
						}
						indirizzoSpedizioneDTO.setCapPostel(recapito.getCapRecapito());
						if(recapito.getDesLocalita() != null)
							indirizzoSpedizioneDTO.setFrazionePostel(recapito.getDesLocalita());

						// da chiarire questo punto della nazione 
						//					if (!recapito.getNazioneRecapito().getDenomNazione().equals(Constants.DB.NAZIONE.DENOM_NAZIONE_ITALIA)) {
						//						indirizzoSpedizioneDTO.setNazionePostel(recapito.getNazioneRecapito().getDenomNazione());
						//					}else {
						indirizzoSpedizioneDTO.setNazionePostel(recapito.getNazioneRecapito().getDenomNazione());
						//					}
						indirizzoSpedizioneDTO.setGestAttoreIns(soggettiExtendedDTO.getGestAttoreIns());
						indirizzoSpedizioneDTO.setGestAttoreUpd(soggettiExtendedDTO.getGestAttoreUpd());
						listIndirizzoSpedizione = new ArrayList<>();
						//[DP] verifico gruppoSOggetto != null
						if (soggettiExtendedDTO.getGruppoSoggetto() != null && !soggettiExtendedDTO.getGruppoSoggetto().isEmpty()) {
							for (GruppiDTO gruppo : soggettiExtendedDTO.getGruppoSoggetto()) {
								IndirizzoSpedizioneDTO indirizzoSpedizioneGruppo = indirizzoSpedizioneDTO;

								indirizzoSpedizioneGruppo.setIdGruppoSoggetto(gruppo.getIdGruppoSoggetto());
								indirizzoSpedizioneGruppo.setDestinatarioPostel(gruppo.getDesGruppoSoggetto());

								if (recapito.getPresso() != null) {
									if (recapito.getPresso().trim().equals(gruppo.getDesGruppoSoggetto().trim()) || recapito.getPresso().trim().equals("N.A. N.A.".trim())) {
										indirizzoSpedizioneDTO.setPressoPostel(null);
									}else {
										indirizzoSpedizioneDTO.setPressoPostel("C/O " + recapito.getPresso());
									}
								} else {
									if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_FISICA)) {
										indirizzoSpedizioneGruppo.setPressoPostel("C/O " + soggettiExtendedDTO.getCognome() + " " + soggettiExtendedDTO.getNome());
									} else if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PUBBLICA)
											|| soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PRIVATA)) {
										indirizzoSpedizioneGruppo.setPressoPostel("C/O " + soggettiExtendedDTO.getDenSoggetto());
									}
								}
								try {
									indirizzoSpedizioneGruppo = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizioneGruppo);
								} catch (Exception e) {
									LOG.error(e);
								}
								listIndirizzoSpedizione.add(indirizzoSpedizioneGruppo);
								if (indirizzoSpedizioneGruppo.getIndValidoPostel().equals(0l)) {
									isValid = false;
								}

							}
						}
						if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_FISICA)) {
							indirizzoSpedizioneDTO.setDestinatarioPostel(soggettiExtendedDTO.getCognome() + " " + soggettiExtendedDTO.getNome());
						}
						if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PUBBLICA)
								|| soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PRIVATA)) {
							indirizzoSpedizioneDTO.setDestinatarioPostel(soggettiExtendedDTO.getDenSoggetto());
						}


						if (recapito.getPresso() != null ) {
							if (StringUtils.isNotBlank(recapito.getPresso()) && (!recapito.getPresso().trim().equals(indirizzoSpedizioneDTO.getDestinatarioPostel().trim()) &&  !recapito.getPresso().trim().equals("N.A. N.A.".trim())))
								indirizzoSpedizioneDTO.setPressoPostel("C/O " + recapito.getPresso());
							else
								indirizzoSpedizioneDTO.setPressoPostel(null);	
						}
						indirizzoSpedizioneDTO.setIdGruppoSoggetto(null);
						try {
							indirizzoSpedizioneDTO = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizioneDTO);
						} catch (Exception e) {
							LOG.error(e);
						}

						listIndirizzoSpedizione.add(indirizzoSpedizioneDTO);
						recapito.setIndirizziSpedizione(listIndirizzoSpedizione);
						recapito.setIdRecapito(recapito.getIdRecapito());
						if (indirizzoSpedizioneDTO.getIndValidoPostel().equals(0l)) {
							isValid = false;
						}
					}
					else {
						//INSERIMENTO NUOVO INDIRIZZO DI SPEDIZIONE DOPO LA CORREZZIONE 
						List<IndirizzoSpedizioneDTO> ListIndirizzoSpedizioneDTO = recapito.getIndirizziSpedizione();
						for (IndirizzoSpedizioneDTO indirizzoSpedizione1 : ListIndirizzoSpedizioneDTO) {
							indirizzoSpedizione1.setIdRecapito(recapito.getIdRecapito());
							indirizzoSpedizione1.setGestAttoreIns(soggettiExtendedDTO.getGestAttoreIns());
							indirizzoSpedizione1.setGestAttoreUpd(soggettiExtendedDTO.getGestAttoreUpd());
							try {
								indirizzoSpedizione1 = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizione1);
							} catch (Exception e) {
								LOG.error(e);
							}
							if (indirizzoSpedizione1.getIndValidoPostel().equals(0l)) {
								isValid = false;
							}
						}
					}

				}
			}else {
				//INSERIMENTO NUOVO RECAPITO 
				recapitoDAO.createRecapito(recapito);
				Number idRecapito = recapito.getIdRecapito();
				if (recapito.getIndirizziSpedizione().isEmpty()) {
					// INSERIMENTO NUOVO INDIRIZZO DI SPEDIZIONE 
					IndirizzoSpedizioneDTO indirizzoSpedizioneDTO = new IndirizzoSpedizioneDTO();
					indirizzoSpedizioneDTO.setIdRecapito(idRecapito.longValue());

					indirizzoSpedizioneDTO.setIndirizzoPostel(recapito.getIndirizzo() + ", "
							+ recapito.getNumCivico());

					if (recapito.getNazioneRecapito().getDenomNazione().equals(Constants.DB.NAZIONE.DENOM_NAZIONE_ITALIA)) {
						indirizzoSpedizioneDTO.setCittaPostel(recapito.getComuneRecapito().getDenomComune());
					} else {
						indirizzoSpedizioneDTO.setCittaPostel(recapito.getCittaEsteraRecapito());
					}
					if (recapito.getNazioneRecapito().getDenomNazione().equals(Constants.DB.NAZIONE.DENOM_NAZIONE_ITALIA)) {
						indirizzoSpedizioneDTO.setProvinciaPostel(
								recapito.getComuneRecapito().getProvincia().getSiglaProvincia());
					}
					indirizzoSpedizioneDTO.setCapPostel(recapito.getCapRecapito());
					if(recapito.getDesLocalita() != null)
						indirizzoSpedizioneDTO.setFrazionePostel(recapito.getDesLocalita());

					// da chiarire questo punto della nazione 
					//				if (!recapito.getNazioneRecapito().getDenomNazione().equals(Constants.DB.NAZIONE.DENOM_NAZIONE_ITALIA)) {
					//					indirizzoSpedizioneDTO.setNazionePostel(recapito.getNazioneRecapito().getDenomNazione());
					//				}else {
					indirizzoSpedizioneDTO.setNazionePostel(recapito.getNazioneRecapito().getDenomNazione());
					//				}
					indirizzoSpedizioneDTO.setGestAttoreIns(soggettiExtendedDTO.getGestAttoreIns());
					indirizzoSpedizioneDTO.setGestAttoreUpd(soggettiExtendedDTO.getGestAttoreUpd());
					listIndirizzoSpedizione = new ArrayList<>();
					if (!soggettiExtendedDTO.getGruppoSoggetto().isEmpty()) {
						for (GruppiDTO gruppo : soggettiExtendedDTO.getGruppoSoggetto()) {
							IndirizzoSpedizioneDTO indirizzoSpedizioneGruppo = indirizzoSpedizioneDTO;

							indirizzoSpedizioneGruppo.setIdGruppoSoggetto(gruppo.getIdGruppoSoggetto());
							indirizzoSpedizioneGruppo.setDestinatarioPostel(gruppo.getDesGruppoSoggetto());

							if (recapito.getPresso() != null) {
								if (recapito.getPresso().trim().equals(gruppo.getDesGruppoSoggetto().trim()) || recapito.getPresso().trim().equals("N.A. N.A.".trim())) {
									indirizzoSpedizioneDTO.setPressoPostel(null);
								}else {
									indirizzoSpedizioneDTO.setPressoPostel("C/O " + recapito.getPresso());
								}					
							} else {
								if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_FISICA)) {
									indirizzoSpedizioneGruppo.setPressoPostel("C/O " + soggettiExtendedDTO.getCognome() + " " + soggettiExtendedDTO.getNome());
								} else if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PUBBLICA)
										|| soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PRIVATA)) {
									indirizzoSpedizioneGruppo.setPressoPostel("C/O " + soggettiExtendedDTO.getDenSoggetto());
								}
							}
							try {
								indirizzoSpedizioneGruppo = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizioneGruppo);
							} catch (Exception e) {
								LOG.error(e);
							}
							listIndirizzoSpedizione.add(indirizzoSpedizioneGruppo);
							if (indirizzoSpedizioneGruppo.getIndValidoPostel().equals(0l)) {
								isValid = false;
							}

						}
					}
					if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_FISICA)) {
						indirizzoSpedizioneDTO.setDestinatarioPostel(soggettiExtendedDTO.getCognome() + " " + soggettiExtendedDTO.getNome());
					}
					if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PUBBLICA)
							|| soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals(Constants.DB.TIPO_SOGGETTO.COD_TIPO_SOGGETTO.PERSONA_GIURIDICA_PRIVATA)) {
						indirizzoSpedizioneDTO.setDestinatarioPostel(soggettiExtendedDTO.getDenSoggetto());
					}

					if (recapito.getPresso() != null) {
						if (StringUtils.isNotBlank(recapito.getPresso()) && (!recapito.getPresso().trim().equals(indirizzoSpedizioneDTO.getDestinatarioPostel().trim()) &&  !recapito.getPresso().trim().equals("N.A. N.A.".trim())))
							indirizzoSpedizioneDTO.setPressoPostel("C/O " + recapito.getPresso());
						else
							indirizzoSpedizioneDTO.setPressoPostel(null);
					}
					indirizzoSpedizioneDTO.setIdGruppoSoggetto(null);
					try {
						indirizzoSpedizioneDTO = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizioneDTO);
					} catch (Exception e) {
						LOG.error(e);
					}

					listIndirizzoSpedizione.add(indirizzoSpedizioneDTO);
					recapito.setIndirizziSpedizione(listIndirizzoSpedizione);
					recapito.setIdRecapito(idRecapito.longValue());
					if (indirizzoSpedizioneDTO.getIndValidoPostel().equals(0l)) {
						isValid = false;
					}
				}
				else {
					//INSERIMENTO NUOVO INDIRIZZO DI SPEDIZIONE DOPO LA CORREZZIONE 
					List<IndirizzoSpedizioneDTO> ListIndirizzoSpedizioneDTO = recapito.getIndirizziSpedizione();
					for (IndirizzoSpedizioneDTO indirizzoSpedizione1 : ListIndirizzoSpedizioneDTO) {
						indirizzoSpedizione1.setIdRecapito(idRecapito.longValue());
						indirizzoSpedizione1.setGestAttoreIns(soggettiExtendedDTO.getGestAttoreIns());
						indirizzoSpedizione1.setGestAttoreUpd(soggettiExtendedDTO.getGestAttoreUpd());
						try {
							indirizzoSpedizione1 = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizione1);
						} catch (Exception e) {
							LOG.error(e);
						}
						if (indirizzoSpedizione1.getIndValidoPostel().equals(0l)) {
							isValid = false;
						}
					}
				}


			}

			if (!isValid) {    
				return recapito;

			}
			else {
				List<IndirizzoSpedizioneDTO> listIS = indirizziSpedizioneDAO.getIndirizzoSpedizioneByIdRecapitoAndIdGruppo(recapito.getIdRecapito(), null);
				recapito.setIndirizziSpedizione(listIS);
			}

			return recapitoDAO.getRecapitiExtendedByIdRecapito(recapito.getIdRecapito());		
	}	


	@Override
	public AvvisoPagamentoDTO createAvvisoPagamento(AvvisoPagamentoDTO avvisoPagamento) throws DAOException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : createAvvisoPagamento] param avvisoPagamento = " + avvisoPagamento);
		avvisoPagamento = avvisoPagamentoDAO.saveAvvisoPagamento(avvisoPagamento);		

		return avvisoPagamentoDAO.loadAvvisoPagamentoByIdNap(avvisoPagamento.getNap());
	}

	@Override
	public AvvisoPagamentoDTO readAvvisoPagamentoByNap(String nap) throws DAOException, SystemException {
		LOG.debug("[BusinessLogicManager : readAvvisoPagamentoByNap] param nap = " + nap);
		return avvisoPagamentoDAO.loadAvvisoPagamentoByIdNap(nap);
	}

	@Override
	public RataSdDTO createRataSd(RataSdDTO rataSd) throws DAOException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : createRataSd] param rataSd = " + rataSd);
		return rataSd = rataSdDAO.saveRataSd(rataSd);		

		//return rataSdDAO.loadRataSdByStatoDebitorio(rataSd.getIdStatoDebitorio());
	}

	@Override
	public TipoModalitaPagDTO readTipoModalitaPagByCodTipoModalitaPag(String codTipoModalitaPag)
			throws DAOException, SystemException {
		TipoModalitaPagDTO tipoModalitaPag;
		LOG.debug("[BusinessLogicManager : readTipoModalitaPagByCodTipoModalitaPag ] ");
		tipoModalitaPag = tipoModalitaPagDAO.loadTipoModalitaPagByCodTipoModalitaPag(codTipoModalitaPag);
		return tipoModalitaPag;
	}

	@Override
	public Long getIdAmbitoByIdFonte(Long idFonte) throws DAOException {
		LOG.debug("[BusinessLogicManager::getIdAmbitoByIdFonte] BEGIN");

		Long idAmbito = ambitoFonteDao.findIdAmbitoByIdFonte(idFonte);	

		LOG.debug("[BusinessLogicManager::getIdAmbitoByIdFonte] END");
		return idAmbito;
	}

	@Override
	public FonteDTO verificaFruitoreBatch(String fruitore) {
		LOG.debug("[BusinessLogicManager::verificaFruitoreBatch] BEGIN");
		FonteDTO fonteDTO = fonteDao.loadFonteByCodFonte(fruitore);
		if (fonteDTO == null) {
			throw new BusinessException(400, "U002", "Batch  non abiltato ");
		}
		LOG.debug("[BusinessLogicManager::verificaFruitoreBatch] END");
		return fonteDTO;
	}

	@Override
	public File450DTO createFile450(File450DTO file450) throws DAOException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : createFile450] param file450 = " + file450);
		file450 = file450DAO.saveFile450(file450);		

		return file450DAO.loadFile450ByIdFile450(file450.getIdFile450());
	}


	@Override
	public IuvDTO createIuv(IuvDTO iuv) throws Exception {
		LOG.debug("[BusinessLogicManager : createIuv] param iuv = " + iuv);
		iuv = iuvDAO.saveIuv(iuv);		

		return iuv;
	}

	@Override
	public LottoDTO createLotto(LottoDTO lotto) throws Exception {
		LOG.debug("[BusinessLogicManager : createLotto] param lotto = " + lotto);
		lotto = lottoDAO.saveLotto(lotto);		

		return lotto;
	}

	@Override
	public PagopaScompRichIuvDTO createPagopaScompRichIuv(PagopaScompRichIuvDTO pagopaScompRichIuv) throws Exception {
		LOG.debug("[BusinessLogicManager : createPagopaScompRichIuv] param pagopaScompRichIuv = " + pagopaScompRichIuv);
		pagopaScompRichIuv = pagopaScompRichIuvDAO.savePagopaScompRichIuv(pagopaScompRichIuv);		

		return pagopaScompRichIuv;
	}

	@Override
	public RimborsoSdUtilizzatoDTO createRimborsoSdUtilizzato(RimborsoSdUtilizzatoDTO rimborsoSdUtilizzato)
			throws Exception {
		LOG.debug("[BusinessLogicManager : createRimborsoSdUtilizzato] param rimborsoSdUtilizzato = " + rimborsoSdUtilizzato);
		rimborsoSdUtilizzato = rimborsoSdUtilizzatoDAO.saveRimborsoSdUtilizzato(rimborsoSdUtilizzato);		

		return rimborsoSdUtilizzato;
	}

	@Override
	public List<RecapitiExtendedDTO> insertUpdateAllRecapiti(List<RecapitiExtendedDTO> recapito,
			SoggettiExtendedDTO soggettiExtendedOLD, SoggettiExtendedDTO soggettiExtended, Long indModManuale)
					throws DAOException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : insertUpdateAllRecapiti] BEGIN");

		List<RecapitiExtendedDTO> listRecapiti = new ArrayList<>();

		for (RecapitiExtendedDTO recapitiExtendedDTO : recapito) {
			RecapitiExtendedDTO recapitoResult = null;

			if (recapitiExtendedDTO.getIdRecapito() != null) {
				recapitoResult = updateRecapito(recapitiExtendedDTO, soggettiExtendedOLD, soggettiExtended,
						indModManuale);
			} else {
				recapitoResult = createRecapito(recapitiExtendedDTO);
			}

			listRecapiti.add(recapitoResult);
		}

		soggettiExtended.setRecapiti(listRecapiti);
		boolean isValid = true;

		for (RecapitiExtendedDTO recapitiExtendedDTO : recapito) {
			if (!recapitiExtendedDTO.getIndirizziSpedizione().isEmpty()) {
				for (IndirizzoSpedizioneDTO indirizzoSpedizione : recapitiExtendedDTO.getIndirizziSpedizione()) {
					if (isNotValidoPostel(indirizzoSpedizione)) {
						isValid = false;
						break;
					}
				}
			}
		}

		if (!isValid) {
			ErrorObjectDTO error = new ErrorObjectDTO();
			error.setCode(ErrorMessages.CODE_E065_FORMATO_INDIRIZZI_SPEDIZIONE_NON_VALIDO);
			error.setTitle(ErrorMessages.MESSAGE_E065_FORMATO_INDIRIZZI_SPEDIZIONE_NON_VALIDO);
			error.setStatus("400");
			throw new DatiInputErratiException(error);
		}
		LOG.debug("[BusinessLogicManager : insertUpdateAllRecapiti] END");

		return listRecapiti;
	}
	private boolean isNotValidoPostel(IndirizzoSpedizioneDTO indirizzo) {
		return indirizzo.getIndValidoPostel().equals(0L);
	}

	@Override
	public List<RecapitiExtendedDTO> readRecapitiByIdSoggetto(Long idSoggetto) throws DAOException {
		LOG.debug("[BusinessLogicManager : readRecapitiByIdSoggetto] BEGIN");
		List<RecapitiExtendedDTO> listRecapitiByIdSoggetto = recapitoDAO.getRecapitiExtendedByIdSoggetto(idSoggetto);
		LOG.debug("[BusinessLogicManager : readRecapitiByIdSoggetto] END");
		return listRecapitiByIdSoggetto;
	}

	@Override
	public void deleteRecapitiByIdSoggetto(long idSoggetto) throws DAOException {
		LOG.debug("[BusinessLogicManager : deleteRecapitiByIdSoggetto] BEGIN");
		recapitoDAO.deleteRecapitiByIdSoggetto(idSoggetto);
		LOG.debug("[BusinessLogicManager : deleteRecapitiByIdSoggetto] END");
	}

	@Override
	public void deleteRecapitoAlternativoByIdRecapito(long idRecapito) throws DAOException {
		LOG.debug("[BusinessLogicManager : deleteRecapitoAlternativoByIdRecapito] BEGIN");
		recapitoDAO.deleteRecapitoAlternativoByIdRecapito(idRecapito);
		LOG.debug("[BusinessLogicManager : deleteRecapitoAlternativoByIdRecapito] END");
	}

	@Override
	public List<DettaglioPagDTO> saveDettaglioPagList(DettaglioPagListDTO dettaglioPagList,  String fruitore,HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ValidationException, Exception {
		LOG.debug("[BusinessLogicManager : saveDettaglioPagList] BEGIN");
		List<DettaglioPagDTO> listDettaglioPag = new ArrayList<>();
		SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd");

		try {
			for (RataSdDTO rata : dettaglioPagList.getRateSd()) {
				DettaglioPagDTO dettaglioPag = createDettaglioPag(rata, dettaglioPagList.getPagamento(),dettaglioPagList.getPagamento().getImportoVersato(), fruitore, formatter,httpHeaders, httpRequest);

				validatorDTO(dettaglioPag, null, null);
				dettaglioPag = dettaglioPagDAO.saveDettaglioPag(dettaglioPag);
				listDettaglioPag.add(dettaglioPag);
			}
		}  catch (ValidationException  ve) {
			throw ve;
		}	catch (Exception e) {
			LOG.error("[BusinessLogicManager : saveDettaglioPagList] ERROR", e);
			throw new Exception("Errore durante il salvataggio dei dettagli pagamento.", e);
		}
		LOG.debug("[BusinessLogicManager : saveDettaglioPagList] END");
		return listDettaglioPag;
	}


	private DettaglioPagDTO createDettaglioPag(RataSdDTO rata,PagamentoExtendedDTO pagamento, BigDecimal ImportoVersato, 
			String fruitore, SimpleDateFormat formatter,HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception, DAOException, DataAccessException, BusinessException, SQLException, SystemException {
		DettaglioPagDTO dettaglioPag = new DettaglioPagDTO();
		dettaglioPag.setImportoVersato(ImportoVersato);
		dettaglioPag.setIdRataSd(rata.getIdRataSd());
		dettaglioPag.setIdPagamento(pagamento.getIdPagamento());
		Long idAmbito = rataSdDAO.getIdAmbitoByIdRata(rata.getIdRataSd());
		//issue 47
		BigDecimal interessiMaturati = calcoloInteresseDAO.calcoloInteressi(idAmbito, ImportoVersato, 
				rata.getDataScadenzaPagamento(), formatter.format(pagamento.getDataOpVal()));
		dettaglioPag.setInteressiMaturati(interessiMaturati);
		BaseApiServiceImpl.setGestAttoreInsUpd(dettaglioPag, fruitore, httpRequest,httpHeaders); // Metodo per impostare i dettagli dell'attore

		return dettaglioPag;
	}

	@Override
	/**
	 * Gestisce il processo di assegnazione dei pagamenti, compresi il delete e l'aggiornamento dei dettagli dei pagamenti e del pagamento stesso.
	 *
	 * @param fruitore         Il fruitore dell'operazione.
	 * @param assegnaPagamento L'oggetto AssegnaPagamentoDTO contenente le informazioni sull'assegnazione dei pagamenti.
	 * @param httpHeaders      Gli header HTTP della richiesta.
	 * @param httpRequest      L'oggetto HttpServletRequest della richiesta.
	 * @throws Exception se si verifica un errore durante il processo di assegnazione dei pagamenti.
	 */
	public void assegnaPagamentiPost(String fruitore, AssegnaPagamentoDTO assegnaPagamento,Long idAmbito, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ValidationException, Exception {
		try {
			LOG.debug("[BusinessLogicManager : assegnaPagamentiPost] BEGIN");
			BaseApiServiceImpl.setGestAttoreInsUpd(assegnaPagamento.getPagamento(), fruitore, httpRequest,httpHeaders); // Metodo per impostare i dettagli dell'attore
			Identita identita =IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			// Eliminazione dei dettagli dei pagamenti
			deleteDettaglioPagamenti(assegnaPagamento,idAmbito, assegnaPagamento.getPagamento(), fruitore, httpRequest, identita);

			// inserimento dei dettagli dei pagamenti
			saveDettaglioPagamenti(assegnaPagamento.getStatiDebitori(), assegnaPagamento.getPagamento(), fruitore, httpHeaders, httpRequest, identita);
			// update impversato dei dettagli dei pagamenti
			updateDettaglioPag(assegnaPagamento.getDettagliPagamento(), fruitore, httpHeaders, httpRequest, identita);
			// Aggiornamento del pagamento
			updatePagamento(assegnaPagamento.getPagamento(), fruitore, httpHeaders, httpRequest, identita);

			// Salvataggio o aggiornamento dei pagamenti non propri
			updatePagamentiNonPropri(assegnaPagamento.getPagamento().getPagNonPropri(),assegnaPagamento.getPagamento(), httpHeaders,  httpRequest, fruitore,identita );

			// [VF - ISSUE_86] Verifica ed eventuale pulizia dell'attivita sullo SD
			verificaPuliziaAttivitaSD(assegnaPagamento);
			
			LOG.debug("[BusinessLogicManager : assegnaPagamentiPost] END");
		} 
		catch (ValidationException  ve) {
			throw ve;
		}
		catch (Exception e) {
			LOG.error("[BusinessLogicManager : assegnaPagamentiPost] ERROR", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		}
	}

	private void verificaPuliziaAttivitaSD(AssegnaPagamentoDTO assegnaPagamento) throws DAOException {
		for (StatoDebitorioExtendedDTO statoDebitorio : assegnaPagamento.getStatiDebitori()) {
			AttivitaStatoDebitorioDTO attivitaStatoDeb = statoDebitorio.getAttivitaStatoDeb();
			if (attivitaStatoDeb != null) {
				String codAttivita = attivitaStatoDeb.getCodAttivitaStatoDeb();
				// Quando si collega un pagamento ad uno SD pulire l'attività dello SD ad
				// eccezione che si tratti di
				// 'FALLIMENTO', 'DA VERIFICARE', 'CANONE NON ESIGIBILE', 'CONCORDATO'.
				if (codAttivita != null && !codAttivita.equalsIgnoreCase(COD_ATTIVITA_SD_FALLIMENTO)
						&& !codAttivita.equalsIgnoreCase(COD_ATTIVITA_SD_DA_VERIFICARE)
						&& !codAttivita.equalsIgnoreCase(COD_ATTIVITA_SD_NON_ESIGIBILE)
						&& !codAttivita.equalsIgnoreCase(COD_ATTIVITA_SD_CONCORDATO)) {
					statoDebitorioDAO.updateAttivitaStatoDebitorio(statoDebitorio.getIdStatoDebitorio());
				}
			}
		}
	}

	private void updateDettaglioPag(List<DettaglioPagExtendedDTO> ListDettagliPagamento, String fruitore, HttpHeaders httpHeaders, HttpServletRequest httpRequest, Identita identita) throws ValidationException, Exception {
		LOG.debug("[BusinessLogicManager : updateDettaglioPag] BEGIN");
		if(Utils.isNotEmpty(ListDettagliPagamento)){
			for (DettaglioPagExtendedDTO dettagliPagamento : ListDettagliPagamento) {
				BaseApiServiceImpl.setGestAttoreInsUpd(dettagliPagamento, fruitore, httpRequest,httpHeaders);
				dettaglioPagDAO.updateDettaglioPag(dettagliPagamento);
				
				tracciamentoManager.saveTracciamento(fruitore, dettagliPagamento, identita, null, "JSON DETTAGLIO PAGAMENTO",
						dettagliPagamento.getIdPagamento() != null ? dettagliPagamento.getIdPagamento().toString() : null, "RISCA_T_PAGAMENTO.ASSEGNA_PAGAMENTO",
						Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, false, true, httpRequest);
			}
		}
		LOG.debug("[BusinessLogicManager : updateDettaglioPag] END");


	}

	private void deleteDettaglioPagamenti(AssegnaPagamentoDTO assegnaPagamento, Long idAmbito, PagamentoExtendedDTO pagamento, String fruitore, HttpServletRequest httpRequest, Identita identita) throws Exception {
		LOG.debug("[BusinessLogicManager : deleteDettaglioPagamenti] BEGIN");
		if(Utils.isNotEmpty(assegnaPagamento.getDettagliPagamentoDaCancellare()))
			for (DettaglioPagExtendedDTO dettagliPagamento : assegnaPagamento.getDettagliPagamentoDaCancellare()) {
				dettaglioPagBilAccDAO.deleteDettaglioPagBilAccByIdDettaglioPag(dettagliPagamento.getIdDettaglioPag());
				dettaglioPagDAO.deleteDettaglioPagById(dettagliPagamento.getIdDettaglioPag());
				//				aggiorna aggiornaStatoContribuzione
				if(dettagliPagamento.getRataSd() != null) {
					StatoDebitorioExtendedDTO statoDebitorio = statoDebitorioDAO.loadStatoDebitorioByIdStatoDebitorio(dettagliPagamento.getRataSd().getIdStatoDebitorio());
					List<StatoDebitorioExtendedDTO> list= new ArrayList<>();
					list.add(statoDebitorio);
					aggiornaStatoContribuzione(list, idAmbito, pagamento.getGestAttoreIns(), null);

				}
				tracciamentoManager.saveTracciamento(fruitore, dettagliPagamento, identita, null, "JSON DETTAGLIO PAGAMENTO",
						pagamento.getIdPagamento() != null ? pagamento.getIdPagamento().toString() : null, "RISCA_T_PAGAMENTO.ASSEGNA_PAGAMENTO",
						Constants.FLG_OPERAZIONE_DELETE, Constants.OPERAZIONE_DELETE, false, true, httpRequest);

			}
		LOG.debug("[BusinessLogicManager : deleteDettaglioPagamenti] END");

	}

	private void saveDettaglioPagamenti(List<StatoDebitorioExtendedDTO> statoDebitorioExtendedList,PagamentoExtendedDTO pagamento, String fruitore, HttpHeaders httpHeaders, HttpServletRequest httpRequest, Identita identita) throws ValidationException, Exception {
		LOG.debug("[BusinessLogicManager : updateDettaglioPagamenti] BEGIN");
		for (StatoDebitorioExtendedDTO statoDebitorio : statoDebitorioExtendedList) {
			saveDettaglioPagamento(statoDebitorio,pagamento, fruitore, httpHeaders, httpRequest, identita);						
		}
		LOG.debug("[BusinessLogicManager : updateDettaglioPagamenti] END");
	}


	private void saveDettaglioPagamento(StatoDebitorioExtendedDTO statoDebitorio, PagamentoExtendedDTO pagamento, String fruitore,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, Identita identita) throws ValidationException, Exception {
		LOG.debug("[BusinessLogicManager : assegnaPagamentiPost] BEGIN");
		SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			for (RataSdDTO rata : statoDebitorio.getRate()) {
				//[DP] issue 48
				if(rata.getIdRataSdPadre() == null) {
					DettaglioPagDTO dettaglioPag = createDettaglioPag(rata, pagamento,statoDebitorio.getImportoVersato(), fruitore, formatter,httpHeaders, httpRequest);
					validatorDTO(dettaglioPag, null, null);
					dettaglioPag = dettaglioPagDAO.saveDettaglioPag(dettaglioPag);
					
					tracciamentoManager.saveTracciamento(fruitore, dettaglioPag, identita, null, "JSON DETTAGLIO PAGAMENTO",
							pagamento.getIdPagamento() != null ? pagamento.getIdPagamento().toString() : null, "RISCA_T_PAGAMENTO.ASSEGNA_PAGAMENTO",
							Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, false, true, httpRequest);
				}
			}
		}catch (ValidationException  ve) {
			throw ve;
		}
		catch (DataAccessException | DAOException | BusinessException | SQLException | SystemException e) {
			LOG.error("[BusinessLogicManager : saveDettaglioPagList] ERROR", e);
			throw new Exception("Errore durante il salvataggio dei dettagli pagamento.", e);
		}
		LOG.debug("[BusinessLogicManager : saveDettaglioPagamento] END");
	}


	private void updatePagamento(PagamentoExtendedDTO pagamentoExtendedDTO, String fruitore,HttpHeaders httpHeaders, HttpServletRequest httpRequest, Identita identita) throws BusinessException, ValidationException, Exception {
		LOG.debug("[BusinessLogicManager : updatePagamento] BEGIN");
		validatorDTO(pagamentoExtendedDTO, null, null);
		pagamentoDAO.updatePagamento(pagamentoExtendedDTO, fruitore);
		
		tracciamentoManager.saveTracciamento(fruitore, pagamentoExtendedDTO, identita, null, "JSON PAGAMENTO",
				pagamentoExtendedDTO.getIdPagamento() != null ? pagamentoExtendedDTO.getIdPagamento().toString() : null, "RISCA_T_PAGAMENTO.ASSEGNA_PAGAMENTO",
				Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, true, httpRequest);
		LOG.debug("[BusinessLogicManager : updatePagamento] BEGIN");
	}

	@Override 
	public void updatePagamentiNonPropri(List<PagNonPropriExtendedDTO> pagNonPropriList, PagamentoExtendedDTO pagamento, HttpHeaders httpHeaders, HttpServletRequest httpRequest, String fruitore, Identita identita) throws ValidationException, Exception {
		LOG.debug("[BusinessLogicManager : updatePagamentiNonPropri] BEGIN");
		//		Bisogna fare delete by id pagamento prima della insert
		pagNonPropriDAO.deletePagNonPropriByIdPagamento(pagamento.getIdPagamento());
		if(Utils.isNotEmpty(pagNonPropriList)) {
			for (PagNonPropriExtendedDTO pagNonPropri : pagNonPropriList) {
				BaseApiServiceImpl.setGestAttoreInsUpd(pagNonPropri, null, httpRequest,httpHeaders); // Metodo per impostare i dettagli dell'attore
				validatorDTO(pagNonPropri, null, null);
				pagNonPropriDAO.savePagNonPropri(pagNonPropri);
				
				tracciamentoManager.saveTracciamento(fruitore, pagNonPropri, identita, null, "JSON PAGAMENTI NON PROPRI",
						pagNonPropri.getIdPagamento() != null ? pagNonPropri.getIdPagamento().toString() : null, "RISCA_T_PAGAMENTO.ASSEGNA_PAGAMENTO",
						Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, false, true, httpRequest);

			}
		}
		
		
		LOG.debug("[BusinessLogicManager : updatePagamentiNonPropri] END");
	}

	/**
	 * Verifica le condizioni necessarie per l'assegnazione dei pagamenti.
	 *
	 * @param assegnaPagamento L'oggetto AssegnaPagamentoDTO contenente le informazioni del pagamento da verificare.
	 * @throws BusinessException se si verifica un errore di business durante la verifica.
	 * @throws Exception se si verifica un errore generico durante la verifica.
	 */
	@Override
	public void verifyAssegnaPagamenti(AssegnaPagamentoDTO assegnaPagamento) throws BusinessException, Exception {
		LOG.debug("[BusinessLogicManager : verifyAssegnaPagamenti] BEGIN");
		final String ERROR_CODE_E096 = Constants.E096;
		final String ERROR_CODE_E099 = Constants.E099;
		// Verifica se il pagamento contiene un soggetto di versamento
		verifySoggettoVersamento(assegnaPagamento);

		// Verifica se ci sono ID comuni tra gli Stati Debitori e i Dettagli Pagamento -> Rata -> Stati Debitori
		if (containsCommonId(assegnaPagamento)) {
			handleException(ERROR_CODE_E099);
		}

		// Verifica l'importo residuo del pagamento
		verifyImportoResiduo(assegnaPagamento, ERROR_CODE_E096);

		LOG.debug("[BusinessLogicManager : verifyAssegnaPagamenti] END");
	}


	/**
	 * Verifica se il pagamento contiene un soggetto di versamento. Se il pagamento e' presente ma
	 * il soggetto di versamento e' nullo, viene gestita un'eccezione con il codice di errore E097.
	 *
	 * @param assegnaPagamento L'oggetto AssegnaPagamentoDTO contenente le informazioni del pagamento.
	 * @throws Exception se si verifica un errore durante la gestione dell'eccezione.
	 */
	private void verifySoggettoVersamento(AssegnaPagamentoDTO assegnaPagamento) throws BusinessException, Exception {
		final String ERROR_CODE_E097 = Constants.E097;
		if (assegnaPagamento.getPagamento() != null && assegnaPagamento.getPagamento().getSoggettoVersamento() == null) {
			handleException(ERROR_CODE_E097);
		}
	}

	/**
	 * Verifica se ci sono ID comuni tra la lista degli ID degli Stati Debitori (idSDList)
	 * e la lista degli ID degli Stati Debitori nei Dettagli Pagamento (idSDDettaglioPagList).
	 *
	 * @param assegnaPagamento L'oggetto AssegnaPagamentoDTO contenente le informazioni del pagamento.
	 * @return true se almeno un ID comune e' presente tra le due liste, altrimenti false.
	 */
	private boolean containsCommonId(AssegnaPagamentoDTO assegnaPagamento) {
		if(!Utils.isNotEmpty(assegnaPagamento.getStatiDebitori()) || !Utils.isNotEmpty(assegnaPagamento.getDettagliPagamento())) {
			return false;
		}
		List<Long> idSDList = assegnaPagamento.getStatiDebitori().stream()
				.map(d -> d.getIdStatoDebitorio())
				.collect(Collectors.toList());
		List<Long> idSDDettaglioPagList = assegnaPagamento.getDettagliPagamento().stream()
				.map(d -> d.getRataSd().getIdStatoDebitorio())
				.collect(Collectors.toList());

		return idSDList.stream().anyMatch(id -> idSDDettaglioPagList.contains(id));
	}

	/**
	 * Verifica se l'importo residuo del pagamento e' negativo e, in caso affermativo,
	 * gestisce un'eccezione con il codice di errore specificato.
	 *
	 * @param assegnaPagamento L'oggetto AssegnaPagamentoDTO contenente le informazioni del pagamento.
	 * @param errorCode        Il codice di errore da utilizzare in caso di importo residuo negativo.
	 * @throws Exception se si verifica un errore durante la gestione dell'eccezione.
	 */
	private void verifyImportoResiduo(AssegnaPagamentoDTO assegnaPagamento, String errorCode) throws BusinessException, Exception  {
		PagamentoExtendedDTO pagamento = assegnaPagamento.getPagamento();
		if (pagamento != null) {

			BigDecimal sommaImpVersatiSD = calculateTotalImportoVersato(assegnaPagamento.getStatiDebitori());
			BigDecimal sommaImpVersatiPagNonPro = calculateTotalImportoVersatoPagNonPro(pagamento);
			BigDecimal sommaImpVersatiDettPag = calculateTotalImportoVersatoDettagliPagamenti(pagamento);
			//	        se non ci sono dettagli pagamenti da sommare allora prendiamo l'importo versato del pagamento stesso
			BigDecimal importoVersato = Utils.isNotEmpty(pagamento.getDettaglioPag()) ? sommaImpVersatiDettPag : pagamento.getImportoVersato() ;
			BigDecimal importoResiduo = importoVersato.subtract(sommaImpVersatiSD).subtract(sommaImpVersatiPagNonPro);
			//          se importo e' negativo genera un eccezione
			if (importoResiduo.compareTo(BigDecimal.ZERO) < 0) {
				handleException(errorCode);
			}
		}
	}

	/**
	 * Calcola la somma dell'importo versato per una lista di DettaglioPag.
	 *
	 * @param pagamento PagamentoExtendedDTO
	 * @return la somma dell'importo versato
	 */
	private BigDecimal calculateTotalImportoVersatoDettagliPagamenti(PagamentoExtendedDTO pagamento) {
		if(Utils.isNotEmpty(pagamento.getDettaglioPag()))
			return pagamento.getDettaglioPag().stream().map(DettaglioPagDTO::getImportoVersato).filter(Objects::nonNull).map(value -> {
				return BigDecimal.ZERO; // Converti i valori nulli in BigDecimal
			}) 
					.reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
		return BigDecimal.ZERO;
	}

	/**
	 * Calcola la somma dell'importo versato per una lista di PagNonPropriExtendedDTO.
	 *
	 * @param pagamento PagamentoExtendedDTO
	 * @return la somma dell'importo versato
	 */
	private BigDecimal calculateTotalImportoVersatoPagNonPro(PagamentoExtendedDTO pagamento) {
		if(Utils.isNotEmpty(pagamento.getPagNonPropri()))
			return pagamento.getPagNonPropri().stream().map(PagNonPropriExtendedDTO::getImportoVersato)
					.filter(Objects::nonNull)
					.map(value -> {
						return BigDecimal.ZERO; // Converti i valori nulli in BigDecimal
					}) 
					.reduce(BigDecimal.ZERO, BigDecimal::add)
					.setScale(2, RoundingMode.HALF_UP);
		return BigDecimal.ZERO;
	}

	/**
	 * Calcola la somma dell'importo versato per una lista di StatoDebitorioExtendedDTO.
	 *
	 * @param statiDebitori la lista di StatoDebitorioExtendedDTO
	 * @return la somma dell'importo versato
	 */
	private BigDecimal calculateTotalImportoVersato(List<StatoDebitorioExtendedDTO> statiDebitori) {
		if(Utils.isNotEmpty(statiDebitori))
			return statiDebitori.stream()
					.map(StatoDebitorioExtendedDTO::getImportoVersato)
					.filter(Objects::nonNull)
					.map(value -> {
						return BigDecimal.ZERO; // Converti i valori nulli in BigDecimal
					})
					.reduce(BigDecimal.ZERO, BigDecimal::add)
					.setScale(2, RoundingMode.HALF_UP);
		return BigDecimal.ZERO;
	}

	/**
	 * Gestisce l'eccezione e lancia BusinessException con il codice dell'errore.
	 *
	 * @param errorCode il codice dell'errore
	 * @throws BusinessException se si verifica un errore aziendale
	 * @throws Exception se si verifica un errore generico
	 */
	private void handleException(String errorCode) throws BusinessException, Exception {
		MessaggiDTO messaggiDTO = null;
		try {
			messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(errorCode);
		} catch (SQLException e) {
			LOG.error("[BusinessLogicManager : verifyAssegnaPagamenti] ERROR", e);
			throw new Exception("Errore durante verify assegnazione dei pagamenti.", e);
		}
		throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
	}


	@Override
	public List<TipoModalitaPagDTO> loadAllTipiModalitaPagamenti() {
		return tipoModalitaPagDAO.loadAllTipiModalitaPagamenti();
	}

	@Override
	public List<AvvisoPagamentoDTO> loadAvvisoPagamentoWorkingByIdSpedizione(Long idSpedizione) {
		return avvisoPagamentoDAO.loadAvvisoPagamentoWorkingByIdSpedizione(idSpedizione);
	}

	@Override
	public void copyWorkingForAvvisoPagamento(AvvisoPagamentoDTO avvisoPagamento, Long idElabora, Long idSpedizione,
			String attore, boolean bollettazioneOrdinaria) throws DAOException {
		LOG.debug("[BusinessLogicManager : copyWorkingForAvvisoPagamento] BEGIN");
		// Copiare le righe dalle tavole di working a quelle effettive
		try {
			avvisoPagamentoDAO.copyAvvisoPagamentoFromWorkingByNap(avvisoPagamento.getNap(), idSpedizione, attore);
			if (bollettazioneOrdinaria) {
				statoDebitorioDAO.copyStatoDebitorioFromWorkingByNap(avvisoPagamento.getNap(), attore);
			}
			avvisoDatiTitolareDAO.copyAvvisoDatiTitolareFromWorkingByNap(avvisoPagamento.getNap(), attore);
			avvisoDatiAmminDAO.copyAvvisoDatiAmminFromWorkingByNap(avvisoPagamento.getNap(), attore);
			avvisoAnnualitaDAO.copyAvvisoAnnualitaFromWorkingByNap(avvisoPagamento.getNap(), attore);
			avvisoUsoDAO.copyAvvisoUsoFromWorkingByNap(avvisoPagamento.getNap(), attore);

			List<StatoDebitorioDTO> statiDebitoriWork = statoDebitorioDAO
					.loadStatoDebitorioWorkingByNap(avvisoPagamento.getNap());
			for (StatoDebitorioDTO statoDebitorioW : statiDebitoriWork) {
				if (bollettazioneOrdinaria) {
					rataSdDAO.copyRataSdFromWorkingByStatoDebitorio(statoDebitorioW.getIdStatoDebitorio(), attore);
					annualitaSdDAO.copyAnnualitaSdFromWorkingByStatoDebitorio(statoDebitorioW.getIdStatoDebitorio(),
							attore);
					List<AnnualitaSdDTO> listaAnnualitaSd = annualitaSdDAO
							.loadAnnualitaSdWorking(statoDebitorioW.getIdStatoDebitorio());
					for (AnnualitaSdDTO annualitaSd : listaAnnualitaSd) {
						List<AnnualitaUsoSdDTO> listaAnnualitaUsoSd = annualitaUsoSdDAO
								.copyAnnualitaUsoSdFromWorkingByAnnualitaSd(annualitaSd.getIdAnnualitaSd(), attore);
						for (AnnualitaUsoSdDTO annualitaUsoSd : listaAnnualitaUsoSd) {
							annualitaUsoSdRaDAO.copyAnnualitaUsoSdRaFromWorkingByAnnualitaUsoSd(
									annualitaUsoSd.getIdAnnualitaUsoSd(), attore);
						}
					}
				} else {
					statoDebitorioDAO.updateStatoDebitorioFromStatoDebitorioWorking(statoDebitorioW, attore);
					rataSdDAO.updateRataSdFromRataSdWorking(statoDebitorioW.getIdStatoDebitorio(), attore);
				}

				rimborsoSdUtilizzatoDAO.insertRimborsoSdUtilizzatoFromWorking(statoDebitorioW.getIdStatoDebitorio(),
						attore, idElabora);
			}
			LOG.debug("[BusinessLogicManager : copyWorkingForAvvisoPagamento] END");
		} catch (DAOException | SQLException e) {
			LOG.error("[BusinessLogicManager : copyWorkingForAvvisoPagamento] ERROR", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		}
	}

	@Override
	public List<StatoDebitorioExtendedDTO> loadAllStatoDebitorio(Integer offset, Integer limit, String sort, Boolean isNotturnoTurnOn)
			throws DAOException, DatiInputErratiException {
		List<StatoDebitorioExtendedDTO> list = new ArrayList<StatoDebitorioExtendedDTO>();
		LOG.debug("[BusinessLogicManager : loadAllStatoDebitorio ] ");
		if(isNotturnoTurnOn != null && isNotturnoTurnOn) {
			list = statoDebitorioDAO.loadStatiDebitoriModificatiOggi(offset,limit, sort);
		}else {
			list = statoDebitorioDAO.loadAllStatoDebitorio(offset,limit, sort);
		}
		return list;
	}

	@Override
	public int aggiornaStatoContribuzione(List<StatoDebitorioExtendedDTO> listStatoDebitorio, Long idAmbito, String fruitore, Long idElabora)
			throws DAOException, DatiInputErratiException, SystemException, ParseException, DataAccessException, SQLException, Exception {
		LOG.debug("[BusinessLogicManager : aggiornaStatoContribuzione ] ");
		int stepStatus = 0;
		Long idStatoDebitorio = null;
		try {
			//ELIMINAZIONE RECORD > 60 GIORNI SU RISCA_R_IUV_DA_INVIARE
			iuvDaInviareDAO.deleteIuvDaInviare(idAmbito);
			
			// 1 step PULIZIA INTERESSI MATURATI SU risca_r_dettaglio_pag e risca_r_rata_sd 
			//setto tutti gli idStatiDebitori all'interno di un array da passare al dao
			List<Long> listIdStatoDebitorio =  new ArrayList<Long>();

			List<Long> listIdStatoDebitorioConFlagAnnullato =  new ArrayList<Long>();
			List<Long> listIdStatoDebitorioSenzaFlagAnnullato =  new ArrayList<Long>();

			Long idStatoContribuzione = null;
			for (int j = 0; j < listStatoDebitorio.size(); j++) {
				listIdStatoDebitorio.add(listStatoDebitorio.get(j).getIdStatoDebitorio()) ; 
				if(listStatoDebitorio.get(j).getFlgAnnullato()== Constants.FLAG_ANNULLATO_TRUE || listStatoDebitorio.get(j).getFlgAddebitoAnnoSuccessivo() == Constants.FLAG_ADDEBITO_ANNO_SUCCESSIVO_TRUE) {
					listIdStatoDebitorioConFlagAnnullato.add(listStatoDebitorio.get(j).getIdStatoDebitorio());
					LOG.debug("[BusinessLogicManager : LIST ANNULLATI idStatoDebitorio  --"+listStatoDebitorio.get(j).getIdStatoDebitorio());
				}else {
					listIdStatoDebitorioSenzaFlagAnnullato.add(listStatoDebitorio.get(j).getIdStatoDebitorio());
					LOG.debug("[BusinessLogicManager : LIST NON ANNULLATI idStatoDebitorio  --"+listStatoDebitorio.get(j).getIdStatoDebitorio());
				}  

			}	

			//se flag annullato = 1 RAMO SENZA INTERESSI
			if(listIdStatoDebitorioConFlagAnnullato != null && !listIdStatoDebitorioConFlagAnnullato.isEmpty()) {
				//pulizia interessi maturati 
				dettaglioPagDAO.updateDettaglioPagByListIdStatoDebSetInteressiMaturati(listIdStatoDebitorioConFlagAnnullato, null, fruitore);
				rataSdDAO.updateRataSdSetInteressiMaturati(listIdStatoDebitorioConFlagAnnullato, fruitore);

				//elenco pagamenti e rimborsi con totale versato e totale restituito
				List<RataSdExtendedDTO> listRateConFlagAnnullato = rataSdDAO.findRateConTotaleVersatoRestituito(listIdStatoDebitorioConFlagAnnullato);

				for (int i = 0; i < listRateConFlagAnnullato.size(); i++) {

					RataSdExtendedDTO rataConFlagAnnullato = listRateConFlagAnnullato.get(i);
					idStatoDebitorio = rataConFlagAnnullato.getIdStatoDebitorio();
					//1.3
					if(rataConFlagAnnullato.getTotVersato() == null || rataConFlagAnnullato.getTotVersato().intValue() == 0) {
						//nessun versamento/ imposto=NULL
						idStatoContribuzione = null;					
					}else {
						BigDecimal totVersato = rataConFlagAnnullato.getTotVersato();
						BigDecimal totRestituito = rataConFlagAnnullato.getTotaleRestituito();	
						if(totVersato.doubleValue() > totRestituito.doubleValue()) {
							// esistono versamenti, superiori ai rimborsi (ECCEDENTE) 4
							idStatoContribuzione = Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.ECCEDENTE.longValue();	
						}else {
							//esistono versamenti, minori <=  rimborsi (REGOLARIZZATO) 3
							idStatoContribuzione = Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.REGOLARIZZATO.longValue();
						}
					}
					//update stato contribuzione in stato debitorio 1.13
					if(idStatoContribuzione != rataConFlagAnnullato.getIdStatoContribuzione()) {
						statoDebitorioDAO.updateStatoContribuzioneForStatoDebitorio(idStatoContribuzione, rataConFlagAnnullato.getIdStatoDebitorio(), fruitore);			
					}

					//1.14 se fruitore BATCH_STATO_CONT aggiornamento IUV per comunicazione a PAGOPA
					if(fruitore != null && fruitore.equals(Constants.BATCH_STATO_CONT)) {
						//inizializzo gli interessi a null
						interessi = null;
						interesRitPag = null;	
						LOG.debug("[BusinessLogicManager : ----idStatoDebitorio---- ] "+rataConFlagAnnullato.getIdStatoDebitorio());
						aggiornamentoIuvPerComunicazionePagopa(idElabora, rataConFlagAnnullato.getNap());				
					}
				}			
			}

			//se flag annullato = 0 RAMO CON INTERESSI
			if(listIdStatoDebitorioSenzaFlagAnnullato != null && !listIdStatoDebitorioSenzaFlagAnnullato.isEmpty()) {

				//elenco pagamenti e rimborsi con totale versato e totale restituito
				List<RataSdExtendedDTO> listRateSenzaFlagAnnullato = rataSdDAO.findRateConTotaleVersatoRestituito(listIdStatoDebitorioSenzaFlagAnnullato);

				String dataScadenzaPagamento = null;

				for (int i = 0; i < listRateSenzaFlagAnnullato.size(); i++) {
					idStatoDebitorio = listRateSenzaFlagAnnullato.get(i).getIdStatoDebitorio();
					//richiamo la query per tirare su la data_scadenza_pagamento con dilazione (se non ritorna nulla prendero' la data senza dilazione) 
					dataScadenzaPagamento = rataSdDAO.findDataScadenzaPagamento(listRateSenzaFlagAnnullato.get(i).getIdStatoDebitorio());
					
					//INIZIALIZZO LE VARIABILI INTERESSI
					interessi = null;
					interesRitPag = null;
					
					if(dataScadenzaPagamento!=null && (Utils.isDateAfter(Utils.getCurrentDate(), dataScadenzaPagamento))) {
						//GESTIONE CALCOLO INTERESSI se DATA ODIERNA > DATA SCADENZA 1.8
						BigDecimal sommatoriaInteressi = this.gestioneCalcoloInteressi(listRateSenzaFlagAnnullato.get(i),dataScadenzaPagamento, idAmbito, fruitore);
						//1.9 Ricalcolo del totale dovuto = interessi maturati + canone dovuto + spese notifica
						BigDecimal importoTotaleDovutoDopoCalcoloInteressi  = listRateSenzaFlagAnnullato.get(i).getCanoneDovuto().add(sommatoriaInteressi).add(listRateSenzaFlagAnnullato.get(i).getImpSpeseNotifica());
						LOG.debug("[BusinessLogicManager : ---- id stato deb ---- ] "+listRateSenzaFlagAnnullato.get(i).getIdStatoDebitorio());
						LOG.debug("[BusinessLogicManager : ---- interessi dopo calcolo ---- ] "+interessi);
						LOG.debug("[BusinessLogicManager : ---- interesRitPag dopo calcolo ---- ] "+interesRitPag);
						LOG.debug("[BusinessLogicManager : ---- canone dovuto ---- ] "+listRateSenzaFlagAnnullato.get(i).getCanoneDovuto());
						LOG.debug("[BusinessLogicManager : ---- sommatoriaInteressi ---- ] "+sommatoriaInteressi);
						this.verificaEAggiornaStatoContribuzione(listRateSenzaFlagAnnullato.get(i),importoTotaleDovutoDopoCalcoloInteressi, fruitore);

					}else {						
						//AGGIORNAMENTO STATO CONTRIBUZIONE SENZA CALCOLO INTERESSI 1.10
						//controllare se esistono pagamenti (in caso affermativo)
						LOG.debug("[BusinessLogicManager : ----idStatoDebitorio---- ] "+listRateSenzaFlagAnnullato.get(i).getIdStatoDebitorio());
						if(listRateSenzaFlagAnnullato.get(i).getTotVersato()!=null && listRateSenzaFlagAnnullato.get(i).getTotVersato().intValue() != 0) {
							this.verificaEAggiornaStatoContribuzione(listRateSenzaFlagAnnullato.get(i), null, fruitore);						
						}else {
							//se non esistono pagamenti 1.7.1
							idStatoContribuzione = null;
							statoDebitorioDAO.updateStatoContribuzioneForStatoDebitorio(idStatoContribuzione, listRateSenzaFlagAnnullato.get(i).getIdStatoDebitorio(), fruitore);
						}
					}
					//[DP] issue 45 spostato controllo
					//1.14 se fruitore BATCH_STATO_CONT aggiornamento IUV per comunicazione a PAGOPA
					if(fruitore != null && fruitore.equals(Constants.BATCH_STATO_CONT)) {
						LOG.debug("[BusinessLogicManager : ----idStatoDebitorio---- ] "+listRateSenzaFlagAnnullato.get(i).getIdStatoDebitorio());
						LOG.debug("[BusinessLogicManager : ---- interessi prima di aggiornamento iuv ---- ] "+interessi);
						LOG.debug("[BusinessLogicManager : ---- interesRitPag prima di aggiornamento iuv ---- ] "+interesRitPag);
						aggiornamentoIuvPerComunicazionePagopa(idElabora, listRateSenzaFlagAnnullato.get(i).getNap());
					}
				}
			}
			
			/**
			 * Commento patch su interessi
			 * 
			 * if (fruitore != null && fruitore.equals(Constants.BATCH_STATO_CONT)) {
				// 1.14Bis richiamo aggiornamento r_iuv_da_inviare per settare gli interessi a 0 quando necessario
				Integer interessiAnnullati = iuvDaInviareDAO.annullaInteressiByIdElabora(idElabora, fruitore);
				if (interessiAnnullati > 0) {
					logStep(idElabora, Constants.DB.PASSO_ELABORA.COD_PASSO_ELABORA.UPD_STATO_CONTRIB, 0,
							"Ripristino importo interessi = 0 per posizioni debitorie non scadute: n "
									+ interessiAnnullati,
							Constants.DB.FASE_ELABORA.COD_FASE_ELABORA.AGGIORNA_STATO_CONTRIBUZIONE, fruitore);
				}
			}**/
			
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("[BusinessLogicManager : aggiornaStatoContribuzione] ERROR idStatoDebitorio: "+idStatoDebitorio, e);
			//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			stepStatus = 1;			
			throw new Exception(e.getMessage() + " idStatoDebitorio: "+idStatoDebitorio);
		}
		return stepStatus;

	}

	private void aggiornamentoIuvPerComunicazionePagopa(Long idElabora,String nap) throws DAOException, SystemException {
		if(nap!= null && !nap.equals("")) {
			List<IuvExtendedDTO> listIuvExtended =  iuvDAO.getSumCanoneSpeseAndTotVersatoByNap(nap);
			if(listIuvExtended != null && !listIuvExtended.isEmpty()) {
				for (int i2 = 0; i2 < listIuvExtended.size(); i2++) {
					//verifico se e' gia presente in risca_r_iuv_da_inviare (se non e' presente insert, altrimenti update) //aggiungere id elabora alla count
					Integer countIuvDaInviare =  iuvDaInviareDAO.countIuvDaInviareByIdIuv(listIuvExtended.get(i2).getIdIuv(), idElabora);
					LOG.debug("[BusinessLogicManager : ----nap---- ] "+nap);
					LOG.debug("[BusinessLogicManager : ----idIuv---- ] "+listIuvExtended.get(i2).getIdIuv());
					if(countIuvDaInviare == 0) {
						//insert
						IuvDaInviareDTO iuvDaInviare = new IuvDaInviareDTO();
						iuvDaInviare.setIdIuv(listIuvExtended.get(i2).getIdIuv());
						iuvDaInviare.setFlgDaInviare(0);
						iuvDaInviare.setIdElabora(idElabora);
						iuvDaInviare.setCanoneDovuto(listIuvExtended.get(i2).getDovutoIuv());
						iuvDaInviare.setImpVersato(listIuvExtended.get(i2).getVersatoIuv());
						iuvDaInviare.setTotSpeseNotifPerNap(listIuvExtended.get(i2).getSpeseIuv());
						iuvDaInviare.setDataScadPag(listIuvExtended.get(i2).getDataScadPag());
						iuvDaInviare.setFlgSdAnnullato(listIuvExtended.get(i2).getFlgAnnullato());					    	
						iuvDaInviare.setImportoNew(BigDecimal.ZERO);

						if(interessi != null) {
							iuvDaInviare.setInteressi(interessi);	
						}else {
							iuvDaInviare.setInteressi(BigDecimal.valueOf(0));
						}

						if(interesRitPag != null) {
							iuvDaInviare.setInteresRitPag(interesRitPag);
						}else {
							iuvDaInviare.setInteresRitPag(BigDecimal.valueOf(0));
						}

						iuvDaInviare.setGestAttoreIns(Constants.GEST_ATTORE_STATO_CONTRIBUZIONE);
						iuvDaInviare.setGestAttoreUpd(Constants.GEST_ATTORE_STATO_CONTRIBUZIONE);	
						LOG.debug("[BusinessLogicManager : ----interessi insert---- ] "+interessi);
						LOG.debug("[BusinessLogicManager : ----interesRitPag insert---- ] "+interesRitPag);

						iuvDaInviareDAO.saveIuvDaInviare(iuvDaInviare);
					}else {
						//update
						IuvDaInviareDTO iuvDaInviare = new IuvDaInviareDTO();
						iuvDaInviare.setIdIuv(listIuvExtended.get(i2).getIdIuv());
						iuvDaInviare.setIdElabora(idElabora);
						iuvDaInviare.setCanoneDovuto(listIuvExtended.get(i2).getDovutoIuv());
						iuvDaInviare.setImpVersato(listIuvExtended.get(i2).getVersatoIuv());
						iuvDaInviare.setTotSpeseNotifPerNap(listIuvExtended.get(i2).getSpeseIuv());
						iuvDaInviare.setFlgSdAnnullato(listIuvExtended.get(i2).getFlgAnnullato());
						iuvDaInviare.setImportoNew(BigDecimal.ZERO);

						if(interessi != null) {
							iuvDaInviare.setInteressi(interessi);	
						}else {
							iuvDaInviare.setInteressi(BigDecimal.valueOf(0));
						}

						if(interesRitPag != null) {
							iuvDaInviare.setInteresRitPag(interesRitPag);
						}else {
							iuvDaInviare.setInteresRitPag(BigDecimal.valueOf(0));
						}

						iuvDaInviare.setGestAttoreUpd(Constants.GEST_ATTORE_STATO_CONTRIBUZIONE);		
						LOG.debug("[BusinessLogicManager : ----interessi update---- ] "+interessi);
						LOG.debug("[BusinessLogicManager : ----interesRitPag update---- ] "+interesRitPag);

						iuvDaInviareDAO.updateIuvDaInviare(iuvDaInviare);


					}
				}
			}


		}
	}

	private void verificaEAggiornaStatoContribuzione(RataSdExtendedDTO listRate, BigDecimal importoTotaleDovutoDopoCalcoloInteressi, String fruitore) throws DAOException, SystemException {
		Long idStatoContribuzione;
		//verificare i rimborsi (imp_rimborso o imp_restituito) 1.10
		RataSdExtendedDTO rataSenzaFlagAnnullato = listRate;
		if(rataSenzaFlagAnnullato.getTotVersato()==null) {
			idStatoContribuzione = null; 
		}else if(rataSenzaFlagAnnullato.getIdAttivitaStatoDebitorio() != null && rataSenzaFlagAnnullato.getIdAttivitaStatoDebitorio() == Constants.DB.ATTIVITA_STATO_DEB.ID_ATTIVITA_STATO_DEB.CONCORDATO.intValue()) {
			idStatoContribuzione = Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.INESIGIBILE.longValue();
		} else {
			BigDecimal totVersato = rataSenzaFlagAnnullato.getTotVersato();
			BigDecimal totRestituito = rataSenzaFlagAnnullato.getTotaleRestituito();						
			BigDecimal differenzaTotale = totVersato.subtract(totRestituito);		   

			BigDecimal totDovuto ;
			if(importoTotaleDovutoDopoCalcoloInteressi != null && importoTotaleDovutoDopoCalcoloInteressi.longValue() > 0) {
				totDovuto = importoTotaleDovutoDopoCalcoloInteressi;
			}else {
				totDovuto = rataSenzaFlagAnnullato.getCanoneDovuto();
			}
//ISSUE 89
             if(totDovuto.compareTo(differenzaTotale) == 0 ){
				/*if v_imp_rimb <> 0  se c'e' un rimborso      
			       or v_flg_attiv_accertamento = 'S'  oppure un accertamento (anche senza spese di notifica) deve essere REGOLARIZZATO 				    	   
			       p_id_stato_contribuzione  = 3;     REGOLARIZZATO */
				if(totRestituito.doubleValue() > 0 || rataSenzaFlagAnnullato.getFlgAccertamento() == Constants.FLAG_ACCERTAMENTO_TRUE ){
					idStatoContribuzione = Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.REGOLARIZZATO.longValue();	
				}  else {
					idStatoContribuzione = Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.REGOLARE.longValue();				
				}                          
			}else {
				if(totDovuto.doubleValue() > differenzaTotale.doubleValue()){
					idStatoContribuzione = Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.INSUFFICIENTE.longValue();				
				}else {
					idStatoContribuzione = Constants.DB.STATO_CONTRIBUZIONE.ID_STATO_CONTRIBUZIONE.ECCEDENTE.longValue();				
				}
			}

			//aggiunto controllo in analisi 26/02/24 se presente dilazione la data scadenza pagamento se maggiore della current date l'idStatoContribuzione viene settato a null
			if(listRate.getFlgDilazione()==Constants.FLAG_DILAZIONE_TRUE) {
				String dataScadenzaPagamentoPerDilazione = rataSdDAO.findDataScadenzaPagamentoPerDilazione(listRate.getIdStatoDebitorio());

				if(dataScadenzaPagamentoPerDilazione!=null && (Utils.isDateAfter( dataScadenzaPagamentoPerDilazione,Utils.getCurrentDate()))) {
					idStatoContribuzione = null; 
				}
			}

		}


		if(idStatoContribuzione != rataSenzaFlagAnnullato.getIdStatoContribuzione()) {
			//update stato contribuzione in stato debitorio 1.13
			statoDebitorioDAO.updateStatoContribuzioneForStatoDebitorio(idStatoContribuzione, rataSenzaFlagAnnullato.getIdStatoDebitorio(), fruitore);			
		}
	}

	private BigDecimal gestioneCalcoloInteressi(RataSdExtendedDTO rataSdExtendedDTO, String dataScadenzaPagamento, Long idAmbito, String fruitore) throws  DAOException, SystemException, ParseException, DataAccessException, SQLException {

		BigDecimal sommatoriaInteressi = new BigDecimal(0);
		BigDecimal sommatoriaInteressiSenzaPagamenti = new BigDecimal(0);
		if(rataSdExtendedDTO.getFlgDilazione()==Constants.FLAG_DILAZIONE_FALSE) {
			//(no dilazione) RAMO SENZA DILAZIONE 1.8.1
			Double interessi_rit_pag_iuv = (double)0;
			BigDecimal canoneDovutoRicalcolato = rataSdExtendedDTO.getCanoneDovuto();
			List<PagamentoExtendedDTO> listPagamento = pagamentoDAO.getPagamentiPerRiscCoattivaEFallimByIdRataSdAndIdStatoDeb(rataSdExtendedDTO.getIdRataSd(), rataSdExtendedDTO.getIdStatoDebitorio());
			if(listPagamento != null && !listPagamento.isEmpty())	{

				for (int i = 0; i < listPagamento.size(); i++) {
					//POTREBBERO ESSERCI VALORI SETTATI ERRONEAMENTE SUL DB (IMPOSTO A 0)
					if(listPagamento.get(i).getImportoVersato()==null) {
						listPagamento.get(i).setImportoVersato(BigDecimal.ZERO);
					}
					//calcolo giorni di ritardo  data_op_val  meno r.data_scadenza_pagamento 1.8.3
					if(listPagamento.get(i).getDataOpVal()!=null && dataScadenzaPagamento != null) {

						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						Date dataScadenzaPagamentoDate = formatter.parse(dataScadenzaPagamento);
						long diffGDataScadenzaDataVersamento = Utils.diffGiorniTraDueDate(dataScadenzaPagamentoDate, listPagamento.get(i).getDataOpVal()); 

						if(diffGDataScadenzaDataVersamento <= 0) {
							//NON ESISTONO GIORNI di RITARDO: interessi non calcolati
							//ANALISI DA RIVEDERE: SOTTRARRE IL PAGATO CANONE DOVUTO - VERSATO
							canoneDovutoRicalcolato = canoneDovutoRicalcolato.subtract(listPagamento.get(i).getImportoVersato());
						}else {
							//ESISTONO GIORNI DI RITARDO SI CALCOLANO GLI INTERESSI 1.8.4 /1.8.6
							BigDecimal importoCalc = BigDecimal.ZERO;
							if(canoneDovutoRicalcolato != null && canoneDovutoRicalcolato.doubleValue() > 0) {

								if(listPagamento.get(i).getImportoVersato()!=null) {
									if(listPagamento.get(i).getImportoVersato().doubleValue() <= canoneDovutoRicalcolato.doubleValue() ) {
										importoCalc = listPagamento.get(i).getImportoVersato();
									}else {
										importoCalc = canoneDovutoRicalcolato;
									}
								}
								// servizio calcolo interessi 1.8.7				    		   
								BigDecimal interessiTotaliCalcolati = calcoloInteresseDAO.calcoloInteressi(idAmbito, importoCalc, dataScadenzaPagamento,listPagamento.get(i).getDataOpVal().toString() );

								//update interessi sulla tabella di dettaglio pagamenti 1.8.8
								dettaglioPagDAO.updateDettaglioPagSetInteressiMaturatiByIdPagamentoAndIdRataStatoDeb(interessiTotaliCalcolati, listPagamento.get(i).getIdPagamento(), rataSdExtendedDTO.getIdRataSd(), fruitore);


								//RICALCOLO del DOVUTO e SOMMATORIA INTERESSI 1.8.9
								//				    		   if(canoneDovutoRicalcolato == null) {
								//					    		   canoneDovutoRicalcolato = rataSdExtendedDTO.getCanoneDovuto().longValue() - listPagamento.get(i).getImportoVersato().longValue();
								//				    		   }else {
								canoneDovutoRicalcolato = canoneDovutoRicalcolato.subtract(listPagamento.get(i).getImportoVersato());
								//				    		   }

								if(sommatoriaInteressi.equals(BigDecimal.valueOf(0))) {
									sommatoriaInteressi = (interessiTotaliCalcolati);
								}else {
									sommatoriaInteressi = sommatoriaInteressi.add(interessiTotaliCalcolati);
								}
								// COMMENTATA IF in seguito di approfondimenti lato analisi con Stefano Tiziana 20/02/24
								//if ((listPagamento.get(i).getIdTipoModalitaPag() == null ? 0 : listPagamento.get(i).getIdTipoModalitaPag().intValue())  != Constants.DB.TIPO_MODALITA_PAG.ID_TIPO_MODALITA_PAG.PAGOPA.intValue() ) {
								interessi_rit_pag_iuv =  interessi_rit_pag_iuv.doubleValue() + interessiTotaliCalcolati.doubleValue();
								//}				
							}else {
								//casistica rivista per problema in produzione sul secondo pagamento
								if(canoneDovutoRicalcolato != null && canoneDovutoRicalcolato.doubleValue() == 0) {
									if( sommatoriaInteressi != null) {
										if(listPagamento.get(i).getImportoVersato()!=null) {
											importoCalc = listPagamento.get(i).getImportoVersato();

											// servizio calcolo interessi 1.8.7				    		   
											BigDecimal interessiTotaliCalcolati = calcoloInteresseDAO.calcoloInteressi(idAmbito, importoCalc, dataScadenzaPagamento,listPagamento.get(i).getDataOpVal().toString() );

											//update interessi sulla tabella di dettaglio pagamenti 1.8.8
											dettaglioPagDAO.updateDettaglioPagSetInteressiMaturatiByIdPagamentoAndIdRataStatoDeb(interessiTotaliCalcolati, listPagamento.get(i).getIdPagamento(), rataSdExtendedDTO.getIdRataSd(), fruitore);

											canoneDovutoRicalcolato = canoneDovutoRicalcolato.subtract(listPagamento.get(i).getImportoVersato());

										}
									}
								}
							}
						}
					}
				}
			}
			//end for pagamenti		
			interesRitPag = BigDecimal.valueOf(interessi_rit_pag_iuv);

			String dataProtocollo = accertamentoDAO.findMinDataProtocolloByIdStatoDebitorio(rataSdExtendedDTO.getIdStatoDebitorio());
			if((!dataProtocollo.equals(Constants.DATA_SCONOSCIUTA) && (canoneDovutoRicalcolato != null && canoneDovutoRicalcolato.doubleValue()>0))) {		  
				//SE PRESENTE ACCERTAMENTO : se presente una data protocollo e canoneDovutoRicalcolato > 0 richiamo il calcolo interessi 1.8.10			  
				BigDecimal interessiTotaliCalcolati = calcoloInteresseDAO.calcoloInteressi(idAmbito, canoneDovutoRicalcolato, dataScadenzaPagamento,dataProtocollo );
				Double interessi_iuv = (double)0;
				interessi_iuv =  interessiTotaliCalcolati.doubleValue();
				interessi = BigDecimal.valueOf(interessi_iuv);

				//sommatoria interessi 1.8.11 				
				sommatoriaInteressiSenzaPagamenti = sommatoriaInteressiSenzaPagamenti.add(interessiTotaliCalcolati);
				if(sommatoriaInteressiSenzaPagamenti != null && sommatoriaInteressiSenzaPagamenti.intValue()>0) {
					sommatoriaInteressi = sommatoriaInteressi.add(sommatoriaInteressiSenzaPagamenti);
				}

				//Aggiornamento rata su tabella risca_r_rata_sd 1.8.12
				rataSdDAO.updateRataSdSetInteressiMaturatiByIdRata(sommatoriaInteressi, rataSdExtendedDTO.getIdRataSd(), fruitore);				

			}else {
				//se non c e stato accertamento, potrebbe esserci qualcosa ancora da pagare  1.8.10
				//se non esistono pagamenti (tot_versato <= 0) (significa che deve essere pagato tutto il canone dovuto
				// e quindi calcoliamo gli interessi sul canone_dovuto)
				if(rataSdExtendedDTO.getTotVersato() == null ||rataSdExtendedDTO.getTotVersato().doubleValue() <= 0) {
					//richiamare il servizio Calcolo Interessi	

					BigDecimal interessiTotaliCalcolati = calcoloInteresseDAO.calcoloInteressi(idAmbito,  rataSdExtendedDTO.getCanoneDovuto(), dataScadenzaPagamento,Utils.getCurrentDate());

					Double interessi_iuv = (double)0;
					interessi_iuv =  interessiTotaliCalcolati.doubleValue();
					interessi = BigDecimal.valueOf(interessi_iuv);

					//non bisogna aggiornare il db fare le operazioni riguardanti  il ricalcolo per lo IUV
					//Rivista analisi:In questa casistica occorre aggiornare il db mettendo a 0 gli interessi maturati sulla rata( quando non ci sono pagamenti(totVersato) e il canone dovuto e' totalmente da pagare)
					//Aggiornamento rata su tabella risca_r_rata_sd
					rataSdDAO.updateRataSdSetInteressiMaturatiByIdRata(new BigDecimal(0), rataSdExtendedDTO.getIdRataSd(), fruitore);	

				}else {
					//se esistono pagamenti tot_versato > 0  1.8.10
					//  (significa che ci sono stati pagamenti e li ho gestiti in precedenza, nel ciclo    apposito, quindi non devo fare nulla, in questo caso, e procedere con il passo successivo)
					// richiamare il servizio Calcolo Interessi passando come importo il cannone_dovuto - tot_versato
					if (canoneDovutoRicalcolato.compareTo(BigDecimal.ZERO) > 0) {						 
						if(rataSdExtendedDTO.getCanoneDovuto().doubleValue() > 0) {
							BigDecimal importoDaCalcolare = rataSdExtendedDTO.getCanoneDovuto().subtract(rataSdExtendedDTO.getTotVersato());
							if(importoDaCalcolare.compareTo(BigDecimal.ZERO) > 0) {
								BigDecimal interessiTotaliCalcolati = calcoloInteresseDAO.calcoloInteressi(idAmbito, importoDaCalcolare, dataScadenzaPagamento,Utils.getCurrentDate());

								Double interessi_iuv = (double)0;
								interessi_iuv =  interessiTotaliCalcolati.doubleValue();
								interessi = BigDecimal.valueOf(interessi_iuv);

								//Sommatoria Interessi 1.8.11
								//La sommatoria interessi senza pagamenti viene aggiunta alla sommatoria interessi SOLO nel caso in cui NON ci siano pagamenti
								if(listPagamento == null || listPagamento.isEmpty()) {
									sommatoriaInteressiSenzaPagamenti = sommatoriaInteressiSenzaPagamenti.add(interessiTotaliCalcolati);
									if(sommatoriaInteressiSenzaPagamenti != null && sommatoriaInteressiSenzaPagamenti.intValue()>0) {
										sommatoriaInteressi = sommatoriaInteressi.add(sommatoriaInteressiSenzaPagamenti);
									}
								}
								//Aggiornamento rata su tabella risca_r_rata_sd 1.8.12
								rataSdDAO.updateRataSdSetInteressiMaturatiByIdRata(sommatoriaInteressi, rataSdExtendedDTO.getIdRataSd(), fruitore);
							}
						}
					}else {
						//ultima else del punto 1.8.10 bisogna richiamare l'update sulla rata 1.8.12
						rataSdDAO.updateRataSdSetInteressiMaturatiByIdRata(sommatoriaInteressi, rataSdExtendedDTO.getIdRataSd(), fruitore);
					}
				}
			}
		}else {
			// RAMO CON DILAZIONE par. 3.5.3.1.2.1
			List<PagamentoExtendedDTO>	listPagamentiForDilazione = pagamentoDAO.getPagamentiByIdStatoDebForDilazione(rataSdExtendedDTO.getIdStatoDebitorio());
			List<RataSdExtendedDTO> listRateForDilazione =	rataSdDAO.findRateByIdStatoDebitorioForDilazione(rataSdExtendedDTO.getIdStatoDebitorio());
			List<PagamentoRataSdExtendedDTO> v_matr_pagam_rate = new ArrayList<PagamentoRataSdExtendedDTO>();
			Double v_r1_parte_dovuto_pagato = (double) 0;
			Double v_r2_parte_dovuto_pagato = (double) 0;

			Double interessiMaturatiPrimaRata = (double)0;
			Double interessiMaturatiSecondaRata = (double)0;
			Double interessiMaturati = (double)0;

			if(listPagamentiForDilazione != null && !listPagamentiForDilazione.isEmpty()) {
				//D4
				for (int i = 0; i < listPagamentiForDilazione.size(); i++) {
					PagamentoRataSdExtendedDTO pagamentoRata = new PagamentoRataSdExtendedDTO();
					pagamentoRata.setIdPagamento(listPagamentiForDilazione.get(i).getIdPagamento());
					pagamentoRata.setDataOpVal(listPagamentiForDilazione.get(i).getDataOpVal());
					pagamentoRata.setIdTipoModalitaPag(listPagamentiForDilazione.get(i).getIdTipoModalitaPag());
					if(listRateForDilazione != null && !listRateForDilazione.isEmpty()) {
						if(listRateForDilazione.size()>2 || listRateForDilazione.size()<2) break;

						//PRIMA RATA
						if(v_r1_parte_dovuto_pagato < listRateForDilazione.get(0).getCanoneDovuto().doubleValue()) {

							pagamentoRata.setProgrRata(listRateForDilazione.get(0).getProgrRata());
							pagamentoRata.setDataScadenzaPagamento(listRateForDilazione.get(0).getDataScadenzaPagamento());

							if ((listRateForDilazione.get(0).getCanoneDovuto().doubleValue() - v_r1_parte_dovuto_pagato) >= listPagamentiForDilazione.get(i).getImportoVersato().doubleValue()) {
								pagamentoRata.setImportoPagamPerRata(listPagamentiForDilazione.get(i).getImportoVersato());				             
								v_r1_parte_dovuto_pagato = v_r1_parte_dovuto_pagato + listPagamentiForDilazione.get(i).getImportoVersato().doubleValue();
								pagamentoRata.setIdRataSd(listRateForDilazione.get(0).getIdRataSd());
								v_matr_pagam_rate.add(pagamentoRata);
							}else {									
								pagamentoRata.setImportoPagamPerRata(BigDecimal.valueOf(listRateForDilazione.get(0).getCanoneDovuto().doubleValue() - v_r1_parte_dovuto_pagato));
								pagamentoRata.setIdRataSd(listRateForDilazione.get(0).getIdRataSd());

								//v_r2_parte_dovuto_pagato =LEAST(v_r2_canone_dovuto, v_matr_pagam(i).imp_versato - (v_r1_canone_dovuto - v_r1_parte_dovuto_pagato));
								if(listRateForDilazione.get(1).getCanoneDovuto().doubleValue() > (listPagamentiForDilazione.get(i).getImportoVersato().doubleValue()-(listRateForDilazione.get(0).getCanoneDovuto().doubleValue() - v_r1_parte_dovuto_pagato))) {
									v_r2_parte_dovuto_pagato = (listPagamentiForDilazione.get(i).getImportoVersato().doubleValue()-(listRateForDilazione.get(0).getCanoneDovuto().doubleValue() - v_r1_parte_dovuto_pagato));
								}else {
									v_r2_parte_dovuto_pagato = listRateForDilazione.get(1).getCanoneDovuto().doubleValue();
								}
								v_r1_parte_dovuto_pagato = listRateForDilazione.get(0).getCanoneDovuto().doubleValue();
								v_matr_pagam_rate.add(pagamentoRata);	

								//SECONDA RATA   
								pagamentoRata = new PagamentoRataSdExtendedDTO();
								pagamentoRata.setIdPagamento(listPagamentiForDilazione.get(i).getIdPagamento());
								pagamentoRata.setDataOpVal(listPagamentiForDilazione.get(i).getDataOpVal());
								pagamentoRata.setProgrRata(listRateForDilazione.get(1).getProgrRata());
								pagamentoRata.setDataScadenzaPagamento(listRateForDilazione.get(1).getDataScadenzaPagamento());
								pagamentoRata.setImportoPagamPerRata(BigDecimal.valueOf(v_r2_parte_dovuto_pagato));
								pagamentoRata.setIdRataSd(listRateForDilazione.get(1).getIdRataSd());
								pagamentoRata.setIdTipoModalitaPag(listPagamentiForDilazione.get(i).getIdTipoModalitaPag());

								v_matr_pagam_rate.add(pagamentoRata);								
							}																
						}else if(v_r2_parte_dovuto_pagato.doubleValue() < listRateForDilazione.get(1).getCanoneDovuto().doubleValue()) {						
							//NB ELSE DA VERIFICARE SE LASCIARE SOLO LA IF AGGIUNGENDO UNA CONDIZIONE
							pagamentoRata.setProgrRata(listRateForDilazione.get(1).getProgrRata());
							pagamentoRata.setDataScadenzaPagamento(listRateForDilazione.get(1).getDataScadenzaPagamento());
							pagamentoRata.setIdRataSd(listRateForDilazione.get(1).getIdRataSd());

							if ((listRateForDilazione.get(1).getCanoneDovuto().doubleValue() - v_r2_parte_dovuto_pagato.doubleValue()) >= listPagamentiForDilazione.get(i).getImportoVersato().doubleValue()) {
								pagamentoRata.setImportoPagamPerRata(listPagamentiForDilazione.get(i).getImportoVersato());
								v_r2_parte_dovuto_pagato = v_r2_parte_dovuto_pagato + listPagamentiForDilazione.get(i).getImportoVersato().doubleValue();
							}else {
								pagamentoRata.setImportoPagamPerRata(BigDecimal.valueOf(listRateForDilazione.get(1).getCanoneDovuto().doubleValue() - v_r2_parte_dovuto_pagato.doubleValue()));
								v_r2_parte_dovuto_pagato = listRateForDilazione.get(1).getCanoneDovuto().doubleValue();
							}

							v_matr_pagam_rate.add(pagamentoRata);
						}					   
					}

				}
				Double interessi_rit_pag_iuv = (double)0;
				Long idPagamento = null;

				if(v_matr_pagam_rate != null && !v_matr_pagam_rate.isEmpty()) {
					//D5
					for (int j = 0; j < v_matr_pagam_rate.size(); j++) {				
						if (Utils.isDateAfter(v_matr_pagam_rate.get(j).getDataOpVal().toString(),v_matr_pagam_rate.get(j).getDataScadenzaPagamento())) {
							/**
							 * richiamare la funzione Calcolo Interessi (par.: 3.5.3.1.3) con i seguenti parametri di input:
                              	v_matr_pagam_rate(i).importo_pagam_per_rata
                              	v_matr_pagam_rate(i).data_scad_pag, 
                                v_matr_pagam_rate(i).data_op_val; 
							 */
							//D6
							BigDecimal interessiMaturatiPerRata = calcoloInteresseDAO.calcoloInteressi(idAmbito, v_matr_pagam_rate.get(j).getImportoPagamPerRata(), 
									v_matr_pagam_rate.get(j).getDataScadenzaPagamento(), v_matr_pagam_rate.get(j).getDataOpVal().toString());

							v_matr_pagam_rate.get(j).setInteressiPagamPerRata(interessiMaturatiPerRata);
						}else {
							v_matr_pagam_rate.get(j).setInteressiPagamPerRata(BigDecimal.valueOf(0));
						}

						//D7 D8
						if(v_matr_pagam_rate.get(j).getProgrRata()==1) {
							//update risca_r_rata_sd
							interessiMaturatiPrimaRata += ( (rataSdExtendedDTO.getInteressiMaturati() == null ? 0 : rataSdExtendedDTO.getInteressiMaturati().doubleValue()) + v_matr_pagam_rate.get(j).getInteressiPagamPerRata().doubleValue());
							rataSdDAO.updateRataSdSetInteressiMaturatiByIdRata(BigDecimal.valueOf(interessiMaturatiPrimaRata), v_matr_pagam_rate.get(j).getIdRataSd(), fruitore);
						}else if (v_matr_pagam_rate.get(j).getProgrRata()==2) {					
							//update risca_r_rata_sd
							interessiMaturatiSecondaRata += (( rataSdExtendedDTO.getInteressiMaturati() == null ? 0 : rataSdExtendedDTO.getInteressiMaturati().doubleValue()) + v_matr_pagam_rate.get(j).getInteressiPagamPerRata().doubleValue());
							rataSdDAO.updateRataSdSetInteressiMaturatiByIdRata(BigDecimal.valueOf(interessiMaturatiSecondaRata), v_matr_pagam_rate.get(j).getIdRataSd(), fruitore);
						}
						// COMMENTATA IF in seguito di approfondimenti lato analisi con Stefano Tiziana 20/02/24
						//if ((v_matr_pagam_rate.get(j).getIdTipoModalitaPag() == null ? 0 : v_matr_pagam_rate.get(j).getIdTipoModalitaPag().intValue())  != Constants.DB.TIPO_MODALITA_PAG.ID_TIPO_MODALITA_PAG.PAGOPA.intValue() ) {
						interessi_rit_pag_iuv =  interessi_rit_pag_iuv.doubleValue() + v_matr_pagam_rate.get(j).getInteressiPagamPerRata().doubleValue();
						//}
						if(idPagamento == null || !idPagamento.equals(v_matr_pagam_rate.get(j).getIdPagamento())) {
							//aggiungere condizione settaggio a 0 solo se il pagamento sia diverso dall'importo della rata da pagare
							// se l'importo pagato del pagamento e' diverso dall'importo dovuto dalla rata allora azzeriamo gli interessi						
							if(!v_r1_parte_dovuto_pagato.equals(v_matr_pagam_rate.get(j).getImportoPagamPerRata().doubleValue())) {
								interessiMaturati = (double)0;
							}
						}						                     

						//update risca_r_dettaglio_pagamento				
						interessiMaturati += ((rataSdExtendedDTO.getInteressiMaturati() == null ? 0 : rataSdExtendedDTO.getInteressiMaturati().doubleValue()) + (v_matr_pagam_rate.get(j).getInteressiPagamPerRata() == null ? 0 : v_matr_pagam_rate.get(j).getInteressiPagamPerRata().doubleValue()));
						idPagamento = v_matr_pagam_rate.get(j).getIdPagamento();

						dettaglioPagDAO.updateDettaglioPagSetInteressiMaturatiByIdPagamentoAndIdRataStatoDeb(BigDecimal.valueOf(interessiMaturati), v_matr_pagam_rate.get(j).getIdPagamento(), listRateForDilazione.get(0).getIdRataSdPadre(), fruitore);	
					}		
					interesRitPag = BigDecimal.valueOf(interessi_rit_pag_iuv);

				}
			}
			//fine pagamenti


			//D9
			String dataProtocollo = accertamentoDAO.findMinDataProtocolloByIdStatoDebitorio(rataSdExtendedDTO.getIdStatoDebitorio());
			BigDecimal interessiRata1PerRiscossioneCoattiva = BigDecimal.valueOf(0);
			BigDecimal interessiRata2PerRiscossioneCoattiva = BigDecimal.valueOf(0);
			//se esiste ancora un accertamento di tipo 3 o 7
			if(!dataProtocollo.equals(Constants.DATA_SCONOSCIUTA)) {		 
				//D10
				Double importoRata1 = (listRateForDilazione.get(0).getCanoneDovuto().doubleValue() - v_r1_parte_dovuto_pagato);
				if (importoRata1 > 0 ) {
					//richiamo calcolo interessi
					interessiRata1PerRiscossioneCoattiva = calcoloInteresseDAO.calcoloInteressi(idAmbito, BigDecimal.valueOf(importoRata1), 
							listRateForDilazione.get(0).getDataScadenzaPagamento(), dataProtocollo);

					//update risca_r_rata_sd
					interessiMaturatiPrimaRata += ( (rataSdExtendedDTO.getInteressiMaturati() == null ? 0 : rataSdExtendedDTO.getInteressiMaturati().doubleValue()) + (interessiRata1PerRiscossioneCoattiva == null ? 0 : interessiRata1PerRiscossioneCoattiva.doubleValue()));
					rataSdDAO.updateRataSdSetInteressiMaturatiByIdRata(BigDecimal.valueOf(interessiMaturatiPrimaRata), listRateForDilazione.get(0).getIdRataSd(), fruitore);
				}

				Double importoRata2 = (listRateForDilazione.get(1).getCanoneDovuto().doubleValue() - v_r2_parte_dovuto_pagato);
				if (importoRata2 > 0 ) {
					//richiamo calcolo interessi
					interessiRata2PerRiscossioneCoattiva = calcoloInteresseDAO.calcoloInteressi(idAmbito, BigDecimal.valueOf(importoRata2), 
							listRateForDilazione.get(1).getDataScadenzaPagamento(), dataProtocollo);

					//update risca_r_rata_sd
					interessiMaturatiSecondaRata += ( (rataSdExtendedDTO.getInteressiMaturati() == null ? 0 : rataSdExtendedDTO.getInteressiMaturati().doubleValue()) + (interessiRata2PerRiscossioneCoattiva == null ? 0 : interessiRata2PerRiscossioneCoattiva.doubleValue()));
					rataSdDAO.updateRataSdSetInteressiMaturatiByIdRata(BigDecimal.valueOf(interessiMaturatiSecondaRata), listRateForDilazione.get(1).getIdRataSd(), fruitore);
				}

				//D11
				Double interessi_iuv = ((interessiRata1PerRiscossioneCoattiva == null ? 0 : interessiRata1PerRiscossioneCoattiva.doubleValue()) + (interessiRata2PerRiscossioneCoattiva == null ? 0 : interessiRata2PerRiscossioneCoattiva.doubleValue()));
				interessi = BigDecimal.valueOf(interessi_iuv);

				//D14
				BigDecimal sommaInteressiMaturati =  rataSdDAO.findSumInteressiMaturatiByIdRataPadreAndIdStatoDebitorio(listRateForDilazione.get(0).getIdRataSdPadre(), rataSdExtendedDTO.getIdStatoDebitorio());						
				//update rata sd
				//modificata l'update con id rata sd l'id rata sd padre
				rataSdDAO.updateRataSdSetInteressiMaturatiByIdRata(sommaInteressiMaturati, listRateForDilazione.get(0).getIdRataSdPadre(), fruitore);
				sommatoriaInteressi = sommaInteressiMaturati.add(BigDecimal.valueOf(interessi_iuv));

			}else {
				//D12
				Double importoRata1 = (listRateForDilazione.get(0).getCanoneDovuto().doubleValue() - v_r1_parte_dovuto_pagato);
				BigDecimal interessiPerIuvRata1= BigDecimal.valueOf(0);
				BigDecimal interessiPerIuvRata2= BigDecimal.valueOf(0);
				if (importoRata1 > 0 ) {
					//funzione Calcolo Interessi

					interessiPerIuvRata1 = calcoloInteresseDAO.calcoloInteressi(idAmbito, BigDecimal.valueOf(importoRata1), 
							listRateForDilazione.get(0).getDataScadenzaPagamento(),Utils.getCurrentDate() );
				}

				Double importoRata2 = (listRateForDilazione.get(1).getCanoneDovuto().doubleValue() - v_r2_parte_dovuto_pagato);
				if (importoRata2 > 0 ) {
					//funzione Calcolo Interessi


					interessiPerIuvRata2 = calcoloInteresseDAO.calcoloInteressi(idAmbito, BigDecimal.valueOf(importoRata2), 
							listRateForDilazione.get(1).getDataScadenzaPagamento(),Utils.getCurrentDate() );
				}

				//D13
				Double interessi_iuv  = (interessiPerIuvRata1.doubleValue() + interessiPerIuvRata2.doubleValue());
				interessi = BigDecimal.valueOf(interessi_iuv);
				//e' stato spostato il calcolo della sommatoria interessi dopo il D14 in quanto viene calcolato dal db stesso dopo gli aggiornamenti delle singole rate
				//	sommatoriaInteressi = BigDecimal.valueOf(interessi_iuv).add(BigDecimal.valueOf(interessiMaturati));				

				//D14
				BigDecimal sommaInteressiMaturati =  rataSdDAO.findSumInteressiMaturatiByIdRataPadreAndIdStatoDebitorio(listRateForDilazione.get(0).getIdRataSdPadre(), rataSdExtendedDTO.getIdStatoDebitorio());						
				//update rata sd
				rataSdDAO.updateRataSdSetInteressiMaturatiByIdRata(sommaInteressiMaturati, listRateForDilazione.get(0).getIdRataSdPadre(), fruitore);							
				sommatoriaInteressi = BigDecimal.valueOf(interessi_iuv).add(sommaInteressiMaturati);

			}				   			
		}
		return sommatoriaInteressi;
	}


	public List<SoggettoDelegaExtendedDTO> loadSoggettiDelegaByIdDelegato(Long idDelegato)
			throws  Exception {
		LOG.debug("[BusinessLogicManager : loadSoggettiDelegaByIdDelega] BEGIN");
		try {
			List<SoggettoDelegaExtendedDTO> listSoggettoDelega  = soggettoDelegaDAO.loadSoggettiDelegaByIdDelegato(idDelegato);
			for (SoggettoDelegaExtendedDTO soggettoDelegaDTO : listSoggettoDelega) {
				soggettoDelegaDTO.setSoggetto(soggettoDAO.getSoggettoById(soggettoDelegaDTO.getIdSoggetto()));
			}
			return listSoggettoDelega;
		} catch (DAOException | SQLException e) {
			LOG.error("[BusinessLogicManager : loadSoggettiDelegaByIdDelega] ERROR", e);
			throw new Exception(e);
		}
	}

	@Override
	public List<GruppoDelegaExtendedDTO> loadGruppiDelegaByIdDelegato(Long idDelegato)
			throws BusinessException, Exception {
		LOG.debug("[BusinessLogicManager : loadGruppiDelegaByIdDelegato] BEGIN");
		try {
			List<GruppoDelegaExtendedDTO> listGruppoDelega  = gruppoDelegaDAO.loadGruppiDelegaByIdDelegato(idDelegato);
			for (GruppoDelegaExtendedDTO GruppoDelega : listGruppoDelega) {
				GruppoDelega.setGruppo(gruppiDAO.loadGruppiById(String.valueOf(GruppoDelega.getIdGruppo()), null));
			}
			return listGruppoDelega;
		} catch (DAOException | SQLException e) {
			LOG.error("[BusinessLogicManager : loadGruppiDelegaByIdDelegato] ERROR", e);
			throw new Exception(e);
		}
	}

	@Override
	public List<AmbitoFonteExtendedDTO> loadAmbitiFonteByCodFonte(String codFonte) throws BusinessException, Exception {
		LOG.debug("[BusinessLogicManager : loadAmbitiFonteByCodFonte] BEGIN");
		try {
			return ambitoFonteDao.loadAmbitiFonteByCodFonte(codFonte);

		} catch (Exception e) {
			LOG.error("[BusinessLogicManager : loadAmbitiFonteByCodFonte] ERROR", e);
			throw e;
		}
	}


	/**
	 * Esegue la validazione del DTO e, se fornito, del DTO JSON.
	 * 
	 * @param dto Il DTO da validare.
	 * @param dtoJson Il DTO JSON da validare.
	 * @param jsonString La rappresentazione JSON del DTO.
	 * @throws BusinessException Se si verifica un errore di business durante la validazione.
	 * @throws Exception Se si verifica un errore generico durante la validazione.
	 */
	@Override
	public <T> void validatorDTO(T dto, Class<T>  dtoClass, String jsonString) throws BusinessException,ValidationException, Exception {
		LOG.debug("[BusinessLogicManager : validatorDTO] BEGIN");
		try {
			if(dto != null) {
				DTOValidator<T> validatorDTO = new DTOValidator<>();
				validatorDTO.validateDTO(dto);
			}	    		    	
			if (jsonString != null ) {
				if (Utils.isValidJson(jsonString)) {
					if (Utils.containsUnwantedCharacters(jsonString)) {
						throw new ValidationException("Il JSON contiene caratteri non desiderati.");
					}
				}else {
					throw new ValidationException("Il JSON non e' valido."); 
				}
			}
			if (jsonString != null && dtoClass != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				T myDto = objectMapper.readValue(jsonString, dtoClass);
				validateJsonDTO(myDto);
			}


		}catch (ValidationException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			handleIllegalArgumentException(e);

		} catch (Exception e) {
			handleException(e);
		} finally {
			LOG.debug("[BusinessLogicManager : validatorDTO] END");
		}
	}




	/**
	 * Esegue la validazione di un oggetto DTO JSON utilizzando il validatore di bean.
	 * 
	 * @param myDto L'oggetto DTO JSON da validare.
	 * @throws IllegalArgumentException Se l'oggetto DTO JSON non e' valido.
	 */
	private <T> void validateJsonDTO(T myDto) throws  IllegalArgumentException{
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(myDto);
		if (!violations.isEmpty()) {
			for (ConstraintViolation<T> violation : violations) {
				LOG.warn("Validation Error: " + violation.getPropertyPath() + " " + violation.getMessage());
			}
			throw new IllegalArgumentException("Il JSON NON E' VALIDO!!.");
		}
	}

	@Override
	public List<LottoDTO> loadLottiByIdElabora(Long idElabora) throws SystemException, DAOException, BusinessException {
		List<LottoDTO> list;
		LOG.debug("[BusinessLogicManager : loadLottiByIdElabora ] ");
		list = lottoDAO.findLottiAttesi(idElabora);
		return list;
	}

	@Override
	public DelegatoExtendedDTO createDelegato(DelegatoExtendedDTO delegato, String origCf)
			throws Exception {
		LOG.debug("[BusinessLogicManager : createDelegato] param delegato = " + delegato);
		LOG.debug("[BusinessLogicManager : createDelegato] param origCf = " + origCf);

		if(delegato.getSoggettoDelega()!=null) {
			LOG.debug("[BusinessLogicManager : createDelegato] param dataAbilitazione = " + delegato.getSoggettoDelega().getDataAbilitazione());


			//Recuperare all'interno di RISCA_T_SOGGETTO tramite orig_cf l'id del soggetto e inserirlo all'interno di RISCA_R_SOGGETTO_DELEGA				
			if(origCf != null)
			{
				long countSoggetto = soggettoDAO.countSoggettoByCfOrPI(origCf);
				if (countSoggetto > 0) {
					DelegatoExtendedDTO delegatoInserito = delegatoDAO.saveDelegato(delegato);

					List<GruppiSoggettoDTO> listSoggettoRicercato = soggettoGruppoDAO.getGruppoSoggettoByCf(origCf);
					if(listSoggettoRicercato != null && !listSoggettoRicercato.isEmpty()) {
						for (int i = 0; i < listSoggettoRicercato.size(); i++) {												
							//inserisco all'interno di risca_r_soggetto_delega
							SoggettoDelegaDTO soggettoDelega = new SoggettoDelegaDTO();
							soggettoDelega.setIdSoggetto(listSoggettoRicercato.get(i).getIdSoggetto());
							soggettoDelega.setIdDelegato(delegatoInserito.getIdDelegato());
							soggettoDelega.setDataAbilitazione(delegato.getSoggettoDelega().getDataAbilitazione());
							soggettoDelega.setGestAttoreIns(delegato.getGestAttoreIns());
							soggettoDelega.setGestAttoreUpd(delegato.getGestAttoreUpd());
							soggettoDelegaDAO.createSoggettoDelega(soggettoDelega);

							if(listSoggettoRicercato.get(i).getIdGruppoSoggetto() != null && listSoggettoRicercato.get(i).getIdGruppoSoggetto().longValue() != 0) {
								//inserisco in risca_r_gruppo_delega
								GruppoDelegaDTO gruppoDelega = new GruppoDelegaDTO();
								gruppoDelega.setIdGruppo(listSoggettoRicercato.get(i).getIdGruppoSoggetto());
								gruppoDelega.setIdDelegato(delegatoInserito.getIdDelegato());
								gruppoDelega.setDataAbilitazione(delegato.getSoggettoDelega().getDataAbilitazione());
								gruppoDelega.setGestAttoreIns(delegato.getGestAttoreIns());
								gruppoDelega.setGestAttoreUpd(delegato.getGestAttoreUpd());
								gruppoDelegaDAO.createGruppoDelega(gruppoDelega);
							}
						}//chiusura for
					}else {
						LOG.debug("[BusinessLogicManager::createDelegato] soggetto non presente " );
						ErrorObjectDTO error = new ErrorObjectDTO();
						error.setCode(ErrorMessages.CODE_2_SOGGETTO_NON_PRESENTE);
						error.setTitle(ErrorMessages.MESSAGE_E2_SOGGETTO_NON_PRESENTE);
						throw new DatiInputErratiException(error) ;
					}

				}else if(countSoggetto == 0){
					LOG.debug("[BusinessLogicManager::createDelegato] soggetto non presente " );
					ErrorObjectDTO error = new ErrorObjectDTO();
					error.setCode(ErrorMessages.CODE_2_SOGGETTO_NON_PRESENTE);
					error.setTitle(ErrorMessages.MESSAGE_E2_SOGGETTO_NON_PRESENTE);
					throw new DatiInputErratiException(error) ;
				}						
			}
		}else {
			LOG.debug("[BusinessLogicManager::createDelegato] soggetto non presente " );
			ErrorObjectDTO error = new ErrorObjectDTO();
			error.setCode(ErrorMessages.CODE_2_SOGGETTO_NON_PRESENTE);
			error.setTitle(ErrorMessages.MESSAGE_E2_SOGGETTO_NON_PRESENTE);
			throw new DatiInputErratiException(error) ;
		}

		return delegato;
	}


	/**
	 * Gestisce le eccezioni derivanti da argomenti non validi durante la validazione.
	 * 
	 * @param e L'eccezione di argomento non valido.
	 * @throws BusinessException Se si verifica un errore di business durante la gestione dell'eccezione.
	 * @throws SQLException Se si verifica un errore SQL durante la gestione dell'eccezione.
	 */
	private void handleIllegalArgumentException(IllegalArgumentException e) throws BusinessException, SQLException {
		MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E003);
		throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
	}

	/**
	 * Gestisce le eccezioni generiche durante la validazione.
	 * 
	 * @param e L'eccezione generica.
	 * @throws Exception Se si verifica un errore generico durante la gestione dell'eccezione.
	 */
	private void handleException(Exception e) throws Exception {
		LOG.error("[BusinessLogicManager : validatorDTO] ERROR", e);
		throw e;
	}


	@Override
	public boolean isNotturnoTurnOn() throws SystemException, DAOException, BusinessException, Exception {
		long countElaboraNotturno =  elaboraDAO.countElaboraNotturno();

		if(countElaboraNotturno > 0) {
			return true;
		}else {
			return false;
		}
	}	

	@Override
	public ReportResultDTO creaRuoloRicercaMorosita(ElaboraDTO elabora, String tipoRicercaMorosita, Integer anno,
			Integer flgRest, Integer flgAnn, String lim, AmbitoDTO ambito, String attore, List<Long> listIdStatiDebitoriSelezionati)
					throws SystemException, DAOException, BusinessException, Exception {
		LOG.debug("[BusinessLogicManager : creaRuoloRicercaMorosita] param elabora = " + elabora);
		LOG.debug("[BusinessLogicManager : creaRuoloRicercaMorosita] param ambito = " + ambito);

		ReportResultDTO reportResult = new ReportResultDTO();
		try {

			// recuperare dati ricerca morosita
			List<StatoDebitorioExtendedDTO> listRicercaMorositaSearchResult = tipoRicercaMorositaDAO.ricercaMorosita(tipoRicercaMorosita,
					ambito.getIdAmbito(), anno, flgRest, flgAnn, lim, null, null, null);
			List<StatoDebitorioExtendedDTO> listRicercaMorosita = new ArrayList<StatoDebitorioExtendedDTO>();

			if (listRicercaMorositaSearchResult.isEmpty()) {
				return null;
			}

			if(listIdStatiDebitoriSelezionati != null && !listIdStatiDebitoriSelezionati.isEmpty()) {
				for (int i = 0; i < listRicercaMorositaSearchResult.size(); i++) {
					for (int j = 0; j < listIdStatiDebitoriSelezionati.size(); j++) {
						if( listRicercaMorositaSearchResult.get(i).getIdStatoDebitorio().equals(listIdStatiDebitoriSelezionati.get(j))){
							listRicercaMorosita.add(listRicercaMorositaSearchResult.get(i));
						}
					}

				}
			}else {
				listRicercaMorosita = listRicercaMorositaSearchResult;
			}

			// Creazione nuova elabora
			elabora = insertElabora(ambito.getIdAmbito(), attore, Constants.DB.TIPO_ELABORA.COD_TIPO_ELABORA.SO, Constants.DB.STATO_ELABORA.COD_STATO_ELABORA.FILE450_ON);

			String fileNameTxt = attore+"_"+Constants.NOME_FILE_450+System.currentTimeMillis()+ ".txt";
			String relativeFilePath = createRuoloFile450(listRicercaMorosita, elabora, ambito.getCodAmbito(), attore, ambito.getIdAmbito(), fileNameTxt);
			if (relativeFilePath != null) {
				String reportUrl = downloadManager.copyFileToDownloadArea(
						elabora.getIdElabora() + "_" + elabora.getTipoElabora().getCodTipoElabora(),
						relativeFilePath);

				reportResult.setReportUrl(reportUrl);
				// UPDATE STATO
				updateElabora(elabora, Constants.DB.STATO_ELABORA.COD_STATO_ELABORA.FILE450_OK,  relativeFilePath);
				logStep(elabora.getIdElabora(), Constants.DB.PASSO_ELABORA.COD_PASSO_ELABORA.CREA_FILE_450,0,Constants.NOTA_ELABORA_FILE_450_OK+listRicercaMorosita.size() , Constants.DB.FASE_ELABORA.COD_FASE_ELABORA.CREA_RUOLO, attore);

				reportResult.setElabora(elabora);

				//insert into risca_t_file_450 
				File450DTO file450DTO = new File450DTO();

				Date date = new Date();
				file450DTO.setNomeFile(fileNameTxt);
				file450DTO.setDataCreazione(date);
				file450DTO.setDataConferma(date);
				file450DTO.setGestAttoreIns(attore);
				file450DTO.setGestAttoreUpd(attore);
				file450DTO = file450DAO.saveFile450(file450DTO);
				// ciclare su tutti gli stati debitori (x ogni stato debitorio insert e update)
				long numeroPartita = 0;
				for (int i = 0; i < listRicercaMorosita.size(); i++) {
					if (listRicercaMorosita.get(i).getIdStatoDebitorio() != null) {
						AccertamentoExtendedDTO accertamento = new AccertamentoExtendedDTO();
						accertamento.setIdStatoDebitorio(listRicercaMorosita.get(i).getIdStatoDebitorio());
						TipiAccertamentoDTO tipoAccertamento = new TipiAccertamentoDTO();
						tipoAccertamento.setIdTipoAccertamento(Constants.DB.TIPO_ACCERTAMENTO.ID_TIPO_ACCERTAMENTO.RISCOSSIONE_COATTIVA.longValue());
						accertamento.setTipoAccertamento(tipoAccertamento);
						accertamento.setDataProtocollo(file450DTO.getDataCreazione());
						accertamento.setIdFile450(file450DTO.getIdFile450());
						accertamento.setGestAttoreIns(attore);
						accertamento.setGestAttoreUpd(attore);
						accertamento =  accertamentoDAO.saveAccertamenti(accertamento);


						/**
						 * INSERT SU risca_r_ruolo  
						 */

						RuoloDTO ruoloDTO = new RuoloDTO();		
						numeroPartita = numeroPartita +1;						
						settaRuoloDTO(listRicercaMorosita.get(i), elabora.getIdElabora(), ambito, attore, numeroPartita,
								accertamento.getIdAccertamento(), ruoloDTO); 

						ruoloDAO.saveRuolo(ruoloDTO, ambito.getIdAmbito());

						statoDebitorioDAO.updateAttivitaStatoDebitorio(listRicercaMorosita.get(i).getIdStatoDebitorio());
					}
				}
			}

			return reportResult;

		} catch (BusinessException e) {
			LOG.error("[BusinessLogicManager : creaRuoloRicercaMorosita] ERROR", e);
			updateElabora(elabora, Constants.DB.STATO_ELABORA.COD_STATO_ELABORA.FILE450_KO,null);
			logStep(elabora.getIdElabora(), Constants.DB.PASSO_ELABORA.COD_PASSO_ELABORA.CREA_FILE_450,1,Constants.NOTA_ELABORA_FILE_450_KO+elabora.getIdElabora(), Constants.DB.FASE_ELABORA.COD_FASE_ELABORA.CREA_RUOLO ,attore );
			// invio email
			inviaEmailErrore(elabora.getIdElabora(),ambito,attore, idStatoDebitorioEmail, codiceUtenzaEmail, e.getMessage());
			throw e;

		} catch (SQLException e) {
			LOG.error("[BusinessLogicManager : creaRuoloRicercaMorosita] ERROR", e);
			updateElabora(elabora, Constants.DB.STATO_ELABORA.COD_STATO_ELABORA.FILE450_KO,null);
			logStep(elabora.getIdElabora(), Constants.DB.PASSO_ELABORA.COD_PASSO_ELABORA.CREA_FILE_450,1,Constants.NOTA_ELABORA_FILE_450_KO+elabora.getIdElabora() , Constants.DB.FASE_ELABORA.COD_FASE_ELABORA.CREA_RUOLO , attore);
			// invio email
			inviaEmailErrore(elabora.getIdElabora(),ambito,attore, idStatoDebitorioEmail, codiceUtenzaEmail, e.getMessage());
			throw e;

		} catch (Exception e) {
			LOG.error("[BusinessLogicManager : creaRuoloRicercaMorosita] ERROR", e);
			updateElabora(elabora, Constants.DB.STATO_ELABORA.COD_STATO_ELABORA.FILE450_KO,null);
			logStep(elabora.getIdElabora(), Constants.DB.PASSO_ELABORA.COD_PASSO_ELABORA.CREA_FILE_450,1,Constants.NOTA_ELABORA_FILE_450_KO+elabora.getIdElabora(), Constants.DB.FASE_ELABORA.COD_FASE_ELABORA.CREA_RUOLO , attore );
			
			// invio email
			inviaEmailErrore(elabora.getIdElabora(),ambito,attore, idStatoDebitorioEmail, codiceUtenzaEmail, e.getMessage());
			throw e;

		}finally {
			LOG.debug("[BusinessLogicManager : creaRuoloRicercaMorosita] END ");

		}
	}




	private void settaRuoloDTO(StatoDebitorioExtendedDTO statoDebitorioExtendedDTO, Long idElabora,
			AmbitoDTO ambito, String attore, long numeroPartita, Long idAccertamento,
			RuoloDTO ruoloDTO) throws SQLException, DAOException, SystemException, Exception {
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		ruoloDTO.setIdAccertamento(idAccertamento);
		ruoloDTO.setDataCreazioneRuolo(now);

		//P_IDENT_PRENOT_RUOLO
		String progressivoPartita = "001";
		String codiceTipoAtto ="";
		List<AmbitoConfigDTO> ambitoConfig = new ArrayList<AmbitoConfigDTO>();
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_TIPO_ATTO);
		if (ambitoConfig.size() > 0) {
			codiceTipoAtto = Utils.padString (ambitoConfig.get(0).getValore(),2,Boolean.TRUE);
		}

		//data atto as (data protocollo) 
		String dataProtocollo = "";
		String numProtocollo = "";
		AccertamentoDTO accertamentoDTO = accertamentoDAO.findMaxDataAndNumProtocolloByIdStatoDebitorio(statoDebitorioExtendedDTO.getIdStatoDebitorio());		
		if(accertamentoDTO != null) {
			final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");

			if(accertamentoDTO.getDataProtocollo()!=null) {
				dataProtocollo = sdf.format(accertamentoDTO.getDataProtocollo());			
			}

			dataProtocollo = Utils.padString (dataProtocollo,8,Boolean.TRUE);

			//Estremi atto as (numero protocollo)
			numProtocollo = Utils.padString (accertamentoDTO.getNumProtocollo(),12,Boolean.TRUE);
		}
		//numeroPartita deve esser lunga 9 con zeri iniziali
		String pIdentPrenotRuolo = String.format("%09d", (numeroPartita))
				.concat(progressivoPartita)
				.concat(codiceTipoAtto)
				.concat(dataProtocollo)
				.concat(numProtocollo);

		ruoloDTO.setpIdentPrenotRuolo(pIdentPrenotRuolo);

		//
		//IMP CANONE MANCANTE AS (importo articolo ruolo 1R96 M50) 

		if(statoDebitorioExtendedDTO.getAccImportoDiCanoneMancante()!=null && statoDebitorioExtendedDTO.getAccImportoDiCanoneMancante().compareTo(BigDecimal.ZERO) > 0 ) {
			BigDecimal importoArticoloRuolo = statoDebitorioExtendedDTO.getAccImportoDiCanoneMancante().setScale(2, RoundingMode.HALF_UP);
			ruoloDTO.setImportoCanoneMancante(importoArticoloRuolo);
		}else {
			ruoloDTO.setImportoCanoneMancante(new BigDecimal(0));
		}


		//NUM BIL ACC CANONE AS (numero accertamento bilancio 1R96 M50) id_accerta_bilancio = 1
		String numBilAccCanone = "";

		if(statoDebitorioExtendedDTO.getAccImportoDiCanoneMancante()!=null && statoDebitorioExtendedDTO.getAccImportoDiCanoneMancante().compareTo(BigDecimal.ZERO) > 0 ) {
			BigDecimal importoArticoloRuolo = statoDebitorioExtendedDTO.getAccImportoDiCanoneMancante().setScale(2, RoundingMode.HALF_UP);
			if(importoArticoloRuolo.compareTo(BigDecimal.ZERO) > 0 ) {
				BilAccDTO bilAccDTO = bilAccDAO.getNumeroAccBilancioByDataScadenzaPag(statoDebitorioExtendedDTO.getAccDataScadenzaPag(), 1);
				numBilAccCanone = bilAccDTO.getNumero_acc_bilancio()==null ? "" : bilAccDTO.getNumero_acc_bilancio();			
			}
		}
		ruoloDTO.setNumBilAccCanone(numBilAccCanone);

		//imp_interessi_mancanti as( IMPORTO ARTICOLO DI RUOLO 1R97 se non esiste 0)
		StatoDebitorioExtendedDTO statoDebitorioDTO = statoDebitorioDAO.findStatoDebitorioForM50ByIdStatoDebitorio(statoDebitorioExtendedDTO.getIdStatoDebitorio());	
		BigDecimal interessiAncoraDovuti = new BigDecimal(0);
		/**
		 * Da nuova specifica:
		 *  importoArticoloRuolo = sommando DUE componenti:
		 *  1 interessi sul ritardato pagamento: ACC_INTERESSI_MANCANTI - risca_t_stato_debitorio.imp_spese_notifica
		 *  2 Se Imp. di canone mancante > 0, calcolare l'interesse su questo importo: Calcolo Interessi
		 */
		// servizio calcolo interessi			    		   
		try {
			interessiAncoraDovuti = calcoloInteresseDAO.calcoloInteressi(ambito.getIdAmbito(), statoDebitorioExtendedDTO.getAccImportoDiCanoneMancante(), statoDebitorioExtendedDTO.getAccDataScadenzaPag(),Utils.getCurrentDate() );
		}catch (BusinessException e) {
			LOG.error("[BusinessLogicManager : createRuoloFile450] Errore create RUOLO.", e);
			//tracciare errore in RISCA_R_REGISTRO_ELABORA
			logStep(idElabora, Constants.DB.PASSO_ELABORA.COD_PASSO_ELABORA.CREA_FILE_450,1,Constants.NOTA_ELABORA_FILE_450_KO+idElabora  , Constants.DB.FASE_ELABORA.COD_FASE_ELABORA.CREA_RUOLO,attore );
		}

		BigDecimal interessiSuRitardatoPagamento = new BigDecimal(0);
		if(statoDebitorioDTO != null) {
			interessiSuRitardatoPagamento = statoDebitorioExtendedDTO.getAccInteressiMancanti().subtract(statoDebitorioDTO.getImpSpeseNotifica() != null ?  statoDebitorioDTO.getImpSpeseNotifica() : BigDecimal.ZERO);
		}
		BigDecimal importoArticoloRuolo = interessiAncoraDovuti.add(interessiSuRitardatoPagamento);

		if(importoArticoloRuolo.compareTo(BigDecimal.ZERO) > 0 ) {
			importoArticoloRuolo = importoArticoloRuolo.setScale(2, RoundingMode.HALF_UP);

		}else {
			importoArticoloRuolo = new BigDecimal(0);
		}
		ruoloDTO.setImportoInteressiMancanti(importoArticoloRuolo);

		BilAccDTO bilAccDTO = new BilAccDTO();
		//num_bil_acc_interessi as(NUMERO ACCERTAMENTO BILANCIO 1R97 M50) id_accerta_bilancio = 10
		String numBilAccInteressi = "";
		if(importoArticoloRuolo!=null && importoArticoloRuolo.compareTo(BigDecimal.ZERO) > 0 ) {
			bilAccDTO = bilAccDAO.getNumeroAccBilancioByDataScadenzaPag(Utils.getCurrentDate(), 10);
			numBilAccInteressi = bilAccDTO.getNumero_acc_bilancio()==null ? "" : bilAccDTO.getNumero_acc_bilancio();			
		}

		ruoloDTO.setNumBilAccInteressi(numBilAccInteressi);

		//imp_spese_mancanti as (IMPORTO ARTICOLO DI RUOLO 1R98)		
		BigDecimal importoArticoloRuolo1R98 = new BigDecimal(0);
		if(statoDebitorioDTO != null) {
			if(statoDebitorioDTO.getImpSpeseNotifica() != null && statoDebitorioDTO.getImpSpeseNotifica().compareTo(BigDecimal.ZERO) > 0) {

				importoArticoloRuolo1R98 = statoDebitorioDTO.getImpSpeseNotifica().setScale(2, RoundingMode.HALF_UP) ;
			}
		}
		ruoloDTO.setImportoSpeseMancanti(importoArticoloRuolo1R98);

		//num_bil_acc_spese as NUMERO ACCERTAMENTO BILANCIO R98 id_accerta_bilancio = 20
		String numBilAccSpese = "";
		if(importoArticoloRuolo1R98!=null && importoArticoloRuolo1R98.compareTo(BigDecimal.ZERO) > 0 ) {
			bilAccDTO = bilAccDAO.getNumeroAccBilancioByDataScadenzaPag(Utils.getCurrentDate(), 20);
			numBilAccSpese = bilAccDTO.getNumero_acc_bilancio()==null ? "" : bilAccDTO.getNumero_acc_bilancio();			
		}
		ruoloDTO.setNumBilAccSpese(numBilAccSpese);


		//codice_ente_creditore 
		String codiceEnteCreditore = "";
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_ENTE_CREDITORE);
		if (ambitoConfig.size() > 0) {
			codiceEnteCreditore = Utils.padString (ambitoConfig.get(0).getValore(),5,Boolean.TRUE); 
		}
		ruoloDTO.setCodiceEnteCreditore(codiceEnteCreditore);

		//tipo_ufficio 
		String tipoUfficio = "";
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_TIPO_UFFICIO);
		if (ambitoConfig.size() > 0) {
			tipoUfficio = Utils.padString (ambitoConfig.get(0).getValore(),1,Boolean.TRUE);
		}
		ruoloDTO.setTipoUfficio(tipoUfficio);

		//codice_ufficio
		String codiceUfficio = "";
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_CODICE_UFFICIO);
		if (ambitoConfig.size() > 0) {
			codiceUfficio = Utils.padString (ambitoConfig.get(0).getValore(),6,Boolean.TRUE);
		}
		ruoloDTO.setCodiceUfficio(codiceUfficio);

		//anno_scadenza as anno M20
		Long anno = rataSdDAO.findAnnoDataScadPagamentoByIdStatoDebitorio(statoDebitorioExtendedDTO.getIdStatoDebitorio());
		ruoloDTO.setAnnoScadenza(anno == null ? 0 : anno.intValue());

		//identif_tipologia_atto as  DENTIFICATIVO TIPOLOGIA ATTO M20 
		//come deve essere settato? e' un intero
		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
				Constants.DB.AMBITO_CONFIG.CHIAVE.MINUTA_RUOLO_IDENTIF_TIPOLOGIA_ATTO);

		ruoloDTO.setIdentifTipologiaAtto(Integer.parseInt(ambitoConfig.get(0).getValore()));

		//numero_partita as NUMERO PARTITA M20 
		ruoloDTO.setNumeroPartita(Long.valueOf(numeroPartita).intValue());

		//progressivo_partita M20
		ruoloDTO.setProgressivoPartita(Integer.valueOf(progressivoPartita));

		//codiceTipoAtto M20	
		ruoloDTO.setCodiceTipoAtto(codiceTipoAtto);

		//motivazione_iscrizione M20
		String motivazioneIscrizione = "";
		if (accertamentoDTO.getDataNotifica() != null) {
			motivazioneIscrizione = Utils.padString ("",28,Boolean.TRUE);
		}else {
			motivazioneIscrizione = Utils.padString (Constants.MOTIVAZIONE_ISCRIZIONE_COMPIUTA_GIACENZA,28,Boolean.TRUE);
		}
		ruoloDTO.setMotivazioneIscrizione(motivazioneIscrizione);
		ruoloDTO.setGestAttoreIns(attore);
		ruoloDTO.setGestAttoreUpd(attore);
	}	


	private String createRuoloFile450(List<StatoDebitorioExtendedDTO> listStatoDebitorioMorosita,ElaboraDTO elabora, String codAmbito,  String attore, Long idAmbito, String fileNameTxt) throws BusinessException, Exception {


		String pathRelativoTxt = File.separator + codAmbito + File.separator
				+ elabora.getTipoElabora().getCodTipoElabora() + File.separator + fileNameTxt;
		Path p = Paths.get(downloadManager.getDownloadPath());
		Path parent = p.getParent();

		String destFileNameTxt = parent.toString() + pathRelativoTxt;
		String outDirStr = parent.toString() + File.separator + codAmbito + File.separator
				+ elabora.getTipoElabora().getCodTipoElabora();
		//try {
		File outDir = new File(outDirStr);		
		if (!outDir.exists()) {
			outDir.mkdirs();
			Utils.setFilePermissions(outDirStr);
		}

		String filePath = outDirStr	+ File.separator + fileNameTxt;
		FileWriter fw = new FileWriter(filePath);
		long progressivo = 1 ;
		String presenzaCointestati = "";

		/**
		 *  calcolo di totaleImportoArticoloRuolo
		 */
		BigDecimal totaleImportoArticoloRuolo = new BigDecimal(0);
		for (int i = 0; i < listStatoDebitorioMorosita.size(); i++) {
			idStatoDebitorioEmail = listStatoDebitorioMorosita.get(i).getIdStatoDebitorio();
			codiceUtenzaEmail = listStatoDebitorioMorosita.get(i).getCodUtenza();
			if(listStatoDebitorioMorosita.get(i).getAccImportoDiCanoneMancante()!=null && listStatoDebitorioMorosita.get(i).getAccImportoDiCanoneMancante().compareTo(BigDecimal.ZERO) > 0 ) {					
				BigDecimal importoArticoloRuolo = listStatoDebitorioMorosita.get(i).getAccImportoDiCanoneMancante().setScale(2, RoundingMode.HALF_UP);
				totaleImportoArticoloRuolo = totaleImportoArticoloRuolo.add(importoArticoloRuolo);
			}
			StatoDebitorioExtendedDTO statoDebitorioDTO = statoDebitorioDAO.findStatoDebitorioForM50ByIdStatoDebitorio(listStatoDebitorioMorosita.get(i).getIdStatoDebitorio());

			if(listStatoDebitorioMorosita.get(i).getAccImportoDiCanoneMancante()!=null && listStatoDebitorioMorosita.get(i).getAccImportoDiCanoneMancante().compareTo(BigDecimal.ZERO) > 0 ) {					

				BigDecimal interessiAncoraDovuti = new BigDecimal(0);
				/**
				 * Da nuova specifica:
				 *  importoArticoloRuolo = sommando DUE componenti:
				 *  1 interessi sul ritardato pagamento: ACC_INTERESSI_MANCANTI - risca_t_stato_debitorio.imp_spese_notifica
				 *  2 Se Imp. di canone mancante > 0, calcolare l'interesse su questo importo: Calcolo Interessi
				 */
				// servizio calcolo interessi			    		   
				try {
					interessiAncoraDovuti = calcoloInteresseDAO.calcoloInteressi(idAmbito, listStatoDebitorioMorosita.get(i).getAccImportoDiCanoneMancante(), listStatoDebitorioMorosita.get(i).getAccDataScadenzaPag(),Utils.getCurrentDate() );
				}catch (BusinessException e) {
					LOG.error("[BusinessLogicManager : createRuoloFile450] Errore create RUOLO.", e);
					//tracciare errore in RISCA_R_REGISTRO_ELABORA
					logStep(elabora.getIdElabora(), Constants.DB.PASSO_ELABORA.COD_PASSO_ELABORA.CREA_FILE_450,1,Constants.NOTA_ELABORA_FILE_450_KO+elabora.getIdElabora()  , Constants.DB.FASE_ELABORA.COD_FASE_ELABORA.CREA_RUOLO,attore );
				}

				BigDecimal interessiSuRitardatoPagamento = new BigDecimal(0);
				if(statoDebitorioDTO != null) {
					interessiSuRitardatoPagamento = listStatoDebitorioMorosita.get(i).getAccInteressiMancanti().setScale(2, RoundingMode.HALF_UP).subtract(statoDebitorioDTO.getImpSpeseNotifica() != null ?  statoDebitorioDTO.getImpSpeseNotifica().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
				}
				BigDecimal importoArticoloRuolo = interessiAncoraDovuti.add(interessiSuRitardatoPagamento);	
				totaleImportoArticoloRuolo = totaleImportoArticoloRuolo.add(importoArticoloRuolo);
			}else {					
				BigDecimal importoArticoloRuolo = new BigDecimal(0);
				if(statoDebitorioDTO != null) {
					importoArticoloRuolo = listStatoDebitorioMorosita.get(i).getAccInteressiMancanti().setScale(2, RoundingMode.HALF_UP).subtract(statoDebitorioDTO.getImpSpeseNotifica() != null ?  statoDebitorioDTO.getImpSpeseNotifica().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);					
					totaleImportoArticoloRuolo = totaleImportoArticoloRuolo.add(importoArticoloRuolo);
				}
			}

			if(statoDebitorioDTO != null) {
				if(statoDebitorioDTO.getImpSpeseNotifica() != null && statoDebitorioDTO.getImpSpeseNotifica().compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal importoArticoloRuolo = statoDebitorioDTO.getImpSpeseNotifica().setScale(2, RoundingMode.HALF_UP) ;
					totaleImportoArticoloRuolo = totaleImportoArticoloRuolo.add(importoArticoloRuolo);
				}
			}
		}	
		/**
		 * fine  totaleImportoArticoloRuolo
		 */

		String M00 = businessLogicFile450.creaM00(codAmbito, totaleImportoArticoloRuolo);
		fw.write(M00 + "\n");
		String M10 = businessLogicFile450.creaM10(codAmbito, progressivo);	
		fw.write(M10 + "\n");
		long totM10 = 1;
		long totM20 = 0;
		long totM30 = 0;
		long totM50 = 0;
		long numeroPartita = 0;
		for (int i = 0; i < listStatoDebitorioMorosita.size(); i++) {
			idStatoDebitorioEmail = listStatoDebitorioMorosita.get(i).getIdStatoDebitorio();
			codiceUtenzaEmail = listStatoDebitorioMorosita.get(i).getCodUtenza();
			numeroPartita = numeroPartita +1;
			String M20 = "";
			progressivo = progressivo + 1;
			presenzaCointestati = Constants.ASSENZA_COMPONENTE_GRUPPO;
			M20 = businessLogicFile450.creaM20(codAmbito,listStatoDebitorioMorosita.get(i),progressivo, numeroPartita, presenzaCointestati);
			totM20 = totM20 +1;
			fw.write(M20 + "\n");
			progressivo = progressivo + 1;
			String M30 = businessLogicFile450.creaM30(codAmbito,listStatoDebitorioMorosita.get(i),progressivo,numeroPartita);
			totM30 = totM30 +1;
			fw.write(M30 + "\n");

			/** Creo M50 
			 * Per il canone, il codice entrata e: 1R96; se "acc_importo_di_canone_mancante e > 0
			 *  Per gli interessi, il codice entrata e: 1R97; se acc_interessi_mancanti > 0 
			 *  Per le spese di notifica, il codice entrata e: 1R98   cercare sul db imp_spese_notifica >0 
			 */
			String M50 = "";
			String codiceEntrata= "";
			long progressivoM50 = 0 ;
			String tipoCodiceEntrata = "";
			String numeroAccertamentoBilancio = "";

			if(listStatoDebitorioMorosita.get(i).getAccImportoDiCanoneMancante()!=null && listStatoDebitorioMorosita.get(i).getAccImportoDiCanoneMancante().compareTo(BigDecimal.ZERO) > 0 ) {
				codiceEntrata = Constants.CODICE_ENTRATA_1R96;
				tipoCodiceEntrata = Constants.TIPO_CODICE_ENTRATA_I;
				BigDecimal importoArticoloRuolo = listStatoDebitorioMorosita.get(i).getAccImportoDiCanoneMancante().setScale(2, RoundingMode.HALF_UP);
				if(importoArticoloRuolo.compareTo(BigDecimal.ZERO) > 0 ) {
					progressivo = progressivo + 1;
					progressivoM50 = progressivoM50 + 1;
					BilAccDTO bilAccDTO = bilAccDAO.getNumeroAccBilancioByDataScadenzaPag(listStatoDebitorioMorosita.get(i).getAccDataScadenzaPag(), 1);
					numeroAccertamentoBilancio = bilAccDTO.getNumero_acc_bilancio()==null ? "" : bilAccDTO.getNumero_acc_bilancio();
					M50 = businessLogicFile450.creaM50(codAmbito,listStatoDebitorioMorosita.get(i),progressivo,numeroPartita, progressivoM50, codiceEntrata, tipoCodiceEntrata, importoArticoloRuolo, numeroAccertamentoBilancio);
					totM50 = totM50 +1;
					fw.write(M50 + "\n");
				}
			}
			StatoDebitorioExtendedDTO statoDebitorioDTO = statoDebitorioDAO.findStatoDebitorioForM50ByIdStatoDebitorio(listStatoDebitorioMorosita.get(i).getIdStatoDebitorio());

			if(listStatoDebitorioMorosita.get(i).getAccImportoDiCanoneMancante()!=null && listStatoDebitorioMorosita.get(i).getAccImportoDiCanoneMancante().compareTo(BigDecimal.ZERO) > 0 ) {
				codiceEntrata = Constants.CODICE_ENTRATA_1R97;
				tipoCodiceEntrata = Constants.TIPO_CODICE_ENTRATA_T;

				BigDecimal interessiAncoraDovuti = new BigDecimal(0);
				/**
				 * Da nuova specifica:
				 *  importoArticoloRuolo = sommando DUE componenti:
				 *  1 interessi sul ritardato pagamento: ACC_INTERESSI_MANCANTI - risca_t_stato_debitorio.imp_spese_notifica
				 *  2 Se Imp. di canone mancante > 0, calcolare l'interesse su questo importo: Calcolo Interessi
				 */
				// servizio calcolo interessi			    		   
				try {
					interessiAncoraDovuti = calcoloInteresseDAO.calcoloInteressi(idAmbito, listStatoDebitorioMorosita.get(i).getAccImportoDiCanoneMancante(), listStatoDebitorioMorosita.get(i).getAccDataScadenzaPag(),Utils.getCurrentDate() );
				}catch (BusinessException e) {
					LOG.error("[BusinessLogicManager : createRuoloFile450] Errore create RUOLO.", e);
					//tracciare errore in RISCA_R_REGISTRO_ELABORA
					logStep(elabora.getIdElabora(), Constants.DB.PASSO_ELABORA.COD_PASSO_ELABORA.CREA_FILE_450,1,Constants.NOTA_ELABORA_FILE_450_KO+elabora.getIdElabora()  , Constants.DB.FASE_ELABORA.COD_FASE_ELABORA.CREA_RUOLO,attore );
				}

				BigDecimal interessiSuRitardatoPagamento = new BigDecimal(0);
				if(statoDebitorioDTO != null) {
					interessiSuRitardatoPagamento = listStatoDebitorioMorosita.get(i).getAccInteressiMancanti().subtract(statoDebitorioDTO.getImpSpeseNotifica() != null ?  statoDebitorioDTO.getImpSpeseNotifica() : BigDecimal.ZERO);
				}
				BigDecimal importoArticoloRuolo = interessiAncoraDovuti.add(interessiSuRitardatoPagamento);
				if(importoArticoloRuolo.compareTo(BigDecimal.ZERO) > 0 ) {
					progressivo = progressivo + 1;
					progressivoM50 = progressivoM50 + 1;
					numeroAccertamentoBilancio = "";
					M50 = businessLogicFile450.creaM50(codAmbito,listStatoDebitorioMorosita.get(i),progressivo,numeroPartita, progressivoM50, codiceEntrata, tipoCodiceEntrata, importoArticoloRuolo.setScale(2, RoundingMode.HALF_UP), numeroAccertamentoBilancio);
					totM50 = totM50 +1;
					fw.write(M50 + "\n");
				}
			}else {
				codiceEntrata = Constants.CODICE_ENTRATA_1R97;
				tipoCodiceEntrata = Constants.TIPO_CODICE_ENTRATA_T;

				BigDecimal importoArticoloRuolo = new BigDecimal(0);
				if(statoDebitorioDTO != null) {
					importoArticoloRuolo = listStatoDebitorioMorosita.get(i).getAccInteressiMancanti().subtract(statoDebitorioDTO.getImpSpeseNotifica() != null ?  statoDebitorioDTO.getImpSpeseNotifica() : BigDecimal.ZERO);
					if(importoArticoloRuolo.compareTo(BigDecimal.ZERO) > 0 ) {
						progressivo = progressivo + 1;
						progressivoM50 = progressivoM50 + 1;
						numeroAccertamentoBilancio = "";
						M50 = businessLogicFile450.creaM50(codAmbito,listStatoDebitorioMorosita.get(i),progressivo,numeroPartita, progressivoM50, codiceEntrata, tipoCodiceEntrata, importoArticoloRuolo.setScale(2, RoundingMode.HALF_UP), numeroAccertamentoBilancio);
						totM50 = totM50 +1;
						fw.write(M50 + "\n");
					}
				}
			}

			if(statoDebitorioDTO != null) {
				if(statoDebitorioDTO.getImpSpeseNotifica() != null && statoDebitorioDTO.getImpSpeseNotifica().compareTo(BigDecimal.ZERO) > 0) {
					codiceEntrata = Constants.CODICE_ENTRATA_1R98;
					tipoCodiceEntrata = Constants.TIPO_CODICE_ENTRATA_A;

					BigDecimal importoArticoloRuolo = statoDebitorioDTO.getImpSpeseNotifica().setScale(2, RoundingMode.HALF_UP) ;
					if(importoArticoloRuolo.compareTo(BigDecimal.ZERO) > 0 ) {
						progressivo = progressivo + 1;
						progressivoM50 = progressivoM50 + 1;
						numeroAccertamentoBilancio = "";
						M50 = businessLogicFile450.creaM50(codAmbito,listStatoDebitorioMorosita.get(i),progressivo,numeroPartita, progressivoM50, codiceEntrata, tipoCodiceEntrata, importoArticoloRuolo, numeroAccertamentoBilancio);
						totM50 = totM50 +1;
						fw.write(M50 + "\n");
					}
				}
			}
		}
		String M99 = businessLogicFile450.creaM99(codAmbito, totM10, totM20, totM30, totM50, totaleImportoArticoloRuolo);
		fw.write(M99 + "\n");


		fw.close();

		updateElabora(elabora, Constants.DB.STATO_ELABORA.COD_STATO_ELABORA.FILE450_OK,null);
		logStep(elabora.getIdElabora(), Constants.DB.PASSO_ELABORA.COD_PASSO_ELABORA.CREA_FILE_450,0,Constants.NOTA_ELABORA_FILE_450_OK+listStatoDebitorioMorosita.size() , Constants.DB.FASE_ELABORA.COD_FASE_ELABORA.CREA_RUOLO,attore );

		return pathRelativoTxt;
	}



	private void logStep(Long idElabora, String codPasso, int esito, String note, String codFase, String attore) {
		RegistroElaboraDTO registroElabora = new RegistroElaboraDTO();
		registroElabora.setIdElabora(idElabora);
		registroElabora.setCodPassoElabora(codPasso);
		registroElabora.setFlgEsitoElabora(esito);
		registroElabora.setNotaElabora(note);
		registroElabora.setGestAttoreIns(attore);
		registroElabora.setGestAttoreUpd(attore);
		registroElabora.setCodFaseElabora(codFase);
		try {
			registroElaboraDAO.saveRegistroElabora(registroElabora);
		} catch (Exception e) {
			LOG.error("[BusinessLogicManager::logStep] (Exception) ", e);
		}
		LOG.debug(note);
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
			LOG.error("[BusinessLogicManager] Errore inserimento elaborazione.", e);
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
			LOG.error("[BusinessLogicManager] Errore durante l aggiornamento elaborazione.", e);
		}
		return elabora;
	}

	public int inviaEmailErrore(Long idElabora, AmbitoDTO ambito, String attore, Long idStatoDebitorio, String codiceUtenza, String messaggioErrore) throws MailException {
		int stepStatus = 0;
		ElaboraDTO elabora = new ElaboraDTO();
		elabora.setIdElabora(idElabora);
		elabora.setDataRichiesta(new Date());
		try {

			String mailDest = null;
			String mailMitt = null;
			String user = null;
			String password = null;
			List<AmbitoConfigDTO> listAmbitoConfig = null;

			listAmbitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
					Constants.AMBITO_CONFIG_CHIAVE_MAIL_DEST);

			if (listAmbitoConfig.size() > 0) {
				mailDest = listAmbitoConfig.get(0).getValore();

				listAmbitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(), Constants.AMBITO_CONFIG_CHIAVE_MAIL_MITT);
				if (listAmbitoConfig.size() > 0) {
					mailMitt = listAmbitoConfig.get(0).getValore();
					LOG.debug("[BusinessLogicManager::inviaEmailErrore] mailDest = " + mailDest);
					LOG.debug("[BusinessLogicManager::inviaEmailErrore] mailMitt = " + mailMitt);
					user = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(), Constants.AMBITO_CONFIG_CHIAVE_MAIL_USERNAME).get(0)
							.getValore();
					password = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(), Constants.AMBITO_CONFIG_CHIAVE_MAIL_PASSWORD).get(0)
							.getValore();

				}
			} 


			listAmbitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(), Constants.AMBITO_CONFIG_CHIAVE_NOME_ALLEGATO);

			String oggetto = getOggettoMail(elabora, Constants.DB.EMAIL_SERVIZIO.COD_EMAIL_SERVIZIO.E_CREA_RUOLO);

			StringBuilder testo = new StringBuilder();
			testo.append("Non e' stato possibile creare il report.  \n\n");
			if(messaggioErrore != null) {
				testo.append("Errore: "+messaggioErrore+" \n");
			}
			if(codiceUtenza != null && idStatoDebitorio != null) {
				testo.append("CODICE UTENZA: "+codiceUtenza+" id stato debitorio: " + idStatoDebitorio+" \n\n");
			}
			testo.append("Elaborazione : "+idElabora+" \n");

			testo.append("\n");


			try {
				listAmbitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
						Constants.AMBITO_CONFIG_CHIAVE_NOME_ALLEGATO);
			} catch (SQLException e) {
				LOG.error(e);
			}
			String nomeAllegato = listAmbitoConfig != null ? listAmbitoConfig.get(0).getValore()
					: "RISCA-LogElaborazione-{dataElaborazione}.txt";
			List<RegistroElaboraExtendedDTO> registroElaborazioni = null;
			try {

				registroElaborazioni = registroElaboraDAO.loadRegistroElaboraByElaboraAndAmbito("" + elabora.getIdElabora(),
						"" + ambito.getIdAmbito(), null, Constants.DB.FASE_ELABORA.COD_FASE_ELABORA.CREA_RUOLO);
			} catch (Exception e1) {
				LOG.error("[BusinessLogicManager::inviaEmailErrore] ", e1);
			}
			MailInfo mailInfo = prepareMail(oggetto, testo.toString(), registroElaborazioni, mailDest, mailMitt,
					nomeAllegato, elabora.getDataRichiesta(), user, password);

			mailManager.sendMail(mailInfo);
		}catch (Exception e) {
			logStep(elabora.getIdElabora(), Constants.DB.PASSO_ELABORA.COD_PASSO_ELABORA.INVIO_MAIL_SERVIZIO,1,Constants.DB.EMAIL_SERVIZIO.COD_EMAIL_SERVIZIO.E_CREA_RUOLO+ " - InvioMail - Errore:  " + e.getMessage() , Constants.DB.EMAIL_SERVIZIO.COD_EMAIL_SERVIZIO.E_CREA_RUOLO,attore );			
			throw new MailException(e.getMessage());
		}


		return stepStatus;

	}

	private MailInfo prepareMail(String oggetto, String testo, List<RegistroElaboraExtendedDTO> registroElaborazioni,
			String mailDest, String mailMitt, String nomeAllegato, Date dataRichiesta, String user, String password) {
		MailInfo mailInfo = new MailInfo();
		mailInfo.setDestinatario(mailDest);
		mailInfo.setMittente(mailMitt);
		mailInfo.setHost(mailManager.getMailHost());
		mailInfo.setPort(mailManager.getMailPort());
		mailInfo.setUsername(user);
		mailInfo.setPassword(password);
		mailInfo.setOggetto(oggetto);
		mailInfo.setTesto(testo);

		StringBuffer txtElencoElab = new StringBuffer();
		for (RegistroElaboraExtendedDTO elab : registroElaborazioni) {
			txtElencoElab.append(elab.getDesPassoElabora());
			txtElencoElab.append(" - Esito: ");
			txtElencoElab.append(elab.getFlgEsitoElabora() == 0 ? "OK" : "Errore");
			txtElencoElab.append(" - ");
			txtElencoElab.append(elab.getNotaElabora());
			txtElencoElab.append("\n");
		}

		AttachmentData attachment = new AttachmentData();
		SimpleDateFormat df2 = new SimpleDateFormat(Constants.DATE_FORMAT_MAIL);
		String filename = nomeAllegato.replace("{dataElaborazione}", df2.format(dataRichiesta));
		attachment.setFilename(filename);
		attachment.setData(txtElencoElab.toString().getBytes());
		attachment.setMimeType("text/plain");

		mailInfo.setAttachments(new AttachmentData[] { attachment });

		return mailInfo;
	}

	private String getOggettoMail(ElaboraDTO elabora, String code)  {

		SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT_MAIL);
		SimpleDateFormat df2 = new SimpleDateFormat(Constants.HOUR_FORMAT_MAIL);
		Date now = new Date();

		EmailServizioDTO emailDto = emailServizioDAO.loadEmailServizioByCodEmail(code);

		String oggetto = emailDto.getOggettoEmailServizio();

		oggetto = StringUtils.replace(oggetto, "[TIPO_ELABORA]", Constants.DB.TIPO_ELABORA.DES_TIPO_ELABORA.SO);
		oggetto = StringUtils.replace(oggetto, "[DATA_RICHIESTA]", "" + df.format(now));
		oggetto = StringUtils.replace(oggetto, "[ORA_RICHIESTA]", "" + df2.format(now));
		oggetto = StringUtils.replace(oggetto, "[ID_ELABORA]", "" + elabora.getIdElabora());
		return oggetto;
	}

	@Override
	public Integer deleteWorkingSoris() throws DAOException {
		LOG.debug("[BusinessLogicManager : deleteWorkingSoris] BEGIN");
		Integer result = null;
	
			soris00CDAO.delete();
			sorisFr0DAO.delete();
			sorisFr1DAO.delete();
			sorisFr3DAO.delete();
			sorisFr7DAO.delete();
			result = soris99CDAO.delete();
			System.out.println("result--> "+result);	
		
		LOG.debug("[BusinessLogicManager : deleteWorkingSoris] END");
		return result;
	}





}
