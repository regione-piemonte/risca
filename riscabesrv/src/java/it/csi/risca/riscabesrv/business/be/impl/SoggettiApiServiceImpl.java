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

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.SoggettiApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.GenericException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.helper.scriva.ScrivaServiceHelper;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ComuneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.NazioneDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ProvinciaExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.SoggettoExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.TipoNaturaGiuridicaDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.TipoSoggettoDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.mapper.SoggettoExtendedMapper;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiConfigDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettiDAO;
import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.FonteDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.NazioniDTO;
import it.csi.risca.riscabesrv.dto.PaginationHeaderDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedResponseDTO;
import it.csi.risca.riscabesrv.dto.TipiNaturaGiuridicaDTO;
import it.csi.risca.riscabesrv.dto.TipiRecapitoDTO;
import it.csi.risca.riscabesrv.dto.TipiSedeDTO;
import it.csi.risca.riscabesrv.dto.TipiSoggettoDTO;
import it.csi.risca.riscabesrv.dto.WarningDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type soggetto api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class SoggettiApiServiceImpl extends BaseApiServiceImpl implements SoggettiApi {

    private final String className = this.getClass().getSimpleName();
    
	private static final String IDENTITY = "identity";

	@Autowired
	private SoggettiDAO soggettoDAO;

	@Autowired
	private ScrivaServiceHelper scrivaServiceHelper;

	@Autowired
	private AmbitiConfigDAO ambitiConfigDao;

    @Autowired
    private TracciamentoManager tracciamentoManager;

	@Autowired
	private IrideServiceHelper serviceHelper;

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;

	@Autowired
	private MessaggiDAO messaggiDAO;
	
    @Autowired
    private BusinessLogic businessLogic;
    
	@Override
	@Transactional
	public Response loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg(Long idAmbito, Long idTipoSoggetto, String cfSoggetto,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
		LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettiByIdAmbAndTipoSoggAndCfSogg] BEGIN");
		LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettiByIdAmbAndTipoSoggAndCfSogg] Parametri in input idAmbito ["
				+ idAmbito + "] e idTipoSoggetto [" + idTipoSoggetto + "] ");
		Identita identita = IdentitaDigitaleManager
				.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), null);

		Application cod = new Application();
		cod.setId("RISCA");
		UseCase use = new UseCase();
		use.setAppId(cod);
		use.setId("UC_SIPRA");
		Long idAmbitoSess = null;
		if (identita != null) {
			idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
			LOGGER.debug(
					"[SoggettiApiServiceImpl::loadSoggettiByIdAmbAndTipoSoggAndCfSogg] idAmbitoSess:" + idAmbitoSess);
			if (idAmbito != null && idAmbitoSess != null) {
				LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettiByIdAmbAndTipoSoggAndCfSogg] idAmbito:" + idAmbito);
				if (!idAmbito.equals(idAmbitoSess)) {
					ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
					return Response.serverError().entity(err).status(401).build();
				}
			}
		}
		SoggettiExtendedResponseDTO response = new SoggettiExtendedResponseDTO();

		SoggettoExtendedDTO dtoFonte = null;
		String ambito = String.valueOf(idAmbito);

		String fontiAmbito = "";
		StringBuffer listaCampiAggiornati = new StringBuffer();
		boolean isAbilitato = false;
		boolean isGestioneAbilitata = false;
		boolean isFonteAbilitataInLettura = false;
		boolean isFonteAbilitataInScrittura = false;
		try {
			for (AmbitoConfigDTO ambitiConfig : CollectionUtils
					.emptyIfNull(ambitiConfigDao.loadAmbitiConfigByIdOrCodAmbito(ambito))) {
				if (ambitiConfig.getChiave().equals("FonteAmbito"))
					fontiAmbito += "," + ambitiConfig.getValore();
				else if (ambitiConfig.getChiave().equals("SOGGETTO.isGestioneAbilitata"))
					isGestioneAbilitata = ambitiConfig.getValore().equals("S");
				else if (ambitiConfig.getChiave().equals("SOGGETTO.isFonteAbilitataInLettura"))
					isFonteAbilitataInLettura = ambitiConfig.getValore().equals("S");
				else if (ambitiConfig.getChiave().equals("SOGGETTO.isFonteAbilitataInScrittura"))
					isFonteAbilitataInScrittura = ambitiConfig.getValore().equals("S");
				else if (ambitiConfig.getChiave().equals("GRUPPO.isAbilitato"))
					isAbilitato = ambitiConfig.getValore().equals("S");
			}
		} catch (SQLException e1) {
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

		}

		SoggettiExtendedDTO soggetto = soggettoDAO.loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg(idAmbito, idTipoSoggetto,
				cfSoggetto);

		String codTipoSoggetto = idTipoSoggetto == 1 ? "PF" : (idTipoSoggetto == 3 ? "PB" : "PG");
		if (isFonteAbilitataInLettura) {
			if (fontiAmbito.indexOf("SCRIVA") > -1) {
				// fixme: Nell'"Algoritmo 2 Ricerca Soggetto" non viene fatto riferimento a
				// quale gestione fare in caso non si riesca a contattare il servizio!
				// the exception is raised to prevent inconsistent information
				List<SoggettoExtendedDTO> dtoFonti = scrivaServiceHelper.getSoggettoFromScriva(null, cfSoggetto,
						codTipoSoggetto, null, /* fixme: where to get "tipoAdempimento"? */
						null /* fixme: where to get "codiceFiscaleImpresa"? */);

				dtoFonte = dtoFonti == null || dtoFonti.size() == 0 ? null : dtoFonti.get(0);
			}
		}

		// fixme: "soggetto.getdataAggiornamento()" is a string passed from the DTO that
		// can be NULL. getGestDataXXX() methods are used instead
		Date data = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (soggetto != null) {
			if (soggetto.getDataAggiornamento() != null) {
				try {
					data = formatter.parse(soggetto.getDataAggiornamento());
				} catch (ParseException e) {

				}
			}
		}
		long dataAggRISCAInMillis = soggetto == null ? 0l : (data != null ? data.getTime() : 0l);
		// (soggetto.getGestDataUpd() != null ? soggetto.getGestDataUpd() :
		// soggetto.getGestDataIns()).getTime();
		// fixme: sometimes SCRIVA returns NULL for getDataAggiornamento() (i.e. for
		// AAAAAA00A11D000L)! Thus, it would be better to use getGestDataXXX() methods
		long dataAggFonteInMillis = dtoFonte == null ? 0l
				: (dtoFonte.getDataAggiornamento() != null ? dtoFonte.getDataAggiornamento().getTime() : 0l);

		if (dataAggRISCAInMillis < dataAggFonteInMillis) {
			final SoggettiExtendedDTO soggettoDst = soggetto != null ? soggetto : new SoggettiExtendedDTO();
			final SoggettoExtendedDTO fonteSCRIVA = dtoFonte;

			TipoSoggettoDTO tipoSoggettoFonte = fonteSCRIVA != null ? fonteSCRIVA.getTipoSoggetto() : null;
			if (tipoSoggettoFonte == null)
				soggettoDst.setTipoSoggetto(null);
			else {
				Long idTipoSoggettoFonte = soggettoDAO.decodeId("risca_d_tipo_soggetto",
						"cod_tipo_soggetto='" + tipoSoggettoFonte.getCodTipoSoggetto() + "'");
				checkIfFieldHasBeenModified(listaCampiAggiornati, "id_tipo_soggetto", soggettoDst.getIdTipoSoggetto(),
						idTipoSoggettoFonte);
				TipiSoggettoDTO tipiSoggettoDTO = new TipiSoggettoDTO();
				tipiSoggettoDTO.setIdTipoSoggetto(idTipoSoggettoFonte);
				tipiSoggettoDTO.setCodTipoSoggetto(tipoSoggettoFonte.getCodTipoSoggetto());
				tipiSoggettoDTO.setDesTipoSoggetto(tipoSoggettoFonte.getDesTipoSoggetto());
				// tipiSoggettoDTO.setOrdinaTipoSoggetto(tipoSoggettoFonte.getXXXX()); // fixme:
				// how to get it?

				soggettoDst.setTipoSoggetto(tipiSoggettoDTO);

			}

			String CodFiscaleAttore = identita != null ? identita.getCodFiscale()
					: soggetto != null ? soggetto.getGestAttoreIns() != null ? soggetto.getGestAttoreIns() : "" : "";

			soggettoDst.setGestAttoreIns(CodFiscaleAttore);
			soggettoDst.setGestAttoreUpd(CodFiscaleAttore);

			soggettoDst.setIdFonte(soggettoDAO.decodeId("risca_d_fonte", "cod_fonte='SCRIVA'"));
			soggettoDst.setIdFonteOrigine(soggettoDst.getIdFonte());
			if (fonteSCRIVA != null) {
				checkIfFieldHasBeenModified(listaCampiAggiornati, "cf_soggetto", soggettoDst.getCfSoggetto(),
						 fonteSCRIVA.getCfSoggetto() );

				checkIfFieldHasBeenModified(listaCampiAggiornati, "nome", soggettoDst.getNome(),
						 fonteSCRIVA.getNome() );

				checkIfFieldHasBeenModified(listaCampiAggiornati, "cognome", soggettoDst.getCognome(),
						fonteSCRIVA.getCognome() );

				checkIfFieldHasBeenModified(listaCampiAggiornati, "citta_estera_nascita",
						soggettoDst.getCittaEsteraNascita(),
						 fonteSCRIVA.getCittaEsteraNascita() );

				checkIfFieldHasBeenModified(listaCampiAggiornati, "den_soggetto", soggettoDst.getDenSoggetto(),
						fonteSCRIVA.getDenSoggetto() );

				checkIfFieldHasBeenModified(listaCampiAggiornati, "partita_iva_soggetto",
						soggettoDst.getPartitaIvaSoggetto(),
						 fonteSCRIVA.getPartitaIvaSoggetto() );

				soggettoDst.setCfSoggetto(fonteSCRIVA.getCfSoggetto());
				soggettoDst.setNome(fonteSCRIVA.getNome());
				soggettoDst.setCognome(fonteSCRIVA.getCognome());
				soggettoDst.setCittaEsteraNascita(fonteSCRIVA.getCittaEsteraNascita());
				soggettoDst.setDenSoggetto(fonteSCRIVA.getDenSoggetto());
				soggettoDst.setPartitaIvaSoggetto(fonteSCRIVA.getPartitaIvaSoggetto());

				checkIfFieldHasBeenModified(listaCampiAggiornati, "data_nascita_soggetto",
						soggettoDst.getDataNascitaSoggetto(),
						fonteSCRIVA.getDataNascitaSoggetto() != null
								? new SimpleDateFormat("yyyy-MM-dd").format(fonteSCRIVA.getDataNascitaSoggetto())
								: null);
				soggettoDst.setDataNascitaSoggetto(
						fonteSCRIVA.getDataNascitaSoggetto() != null ? new SimpleDateFormat("yyyy-MM-dd")
								.format(new Date(fonteSCRIVA.getDataNascitaSoggetto().getTime())) : null);
				boolean isPF = idTipoSoggetto == 1;
				ComuneExtendedDTO comuneNascitaFonte = fonteSCRIVA.getComuneNascita();
				if (comuneNascitaFonte != null) {
					soggettoDst.setComuneNascita(null);

					Long idComuneFonte = soggettoDAO.decodeId("risca_d_comune",
							"cod_istat_comune='" + comuneNascitaFonte.getCodIstatComune() + "'");
					soggettoDst.setIdComuneNascita(idComuneFonte.intValue());
					ComuneExtendedDTO comuneNascita = new ComuneExtendedDTO();
					comuneNascita.setIdComune(Long.valueOf(soggettoDst.getIdComuneNascita()));

					soggettoDst.setComuneNascita(comuneNascita);

					Long idNazioneFonte = soggettoDAO.decodeId("risca_d_nazione", "cod_istat_nazione='"
							+ comuneNascitaFonte.getProvincia().getRegione().getNazione().getCodIstatNazione() + "'");
					soggettoDst.setIdStatoNascita(idNazioneFonte.intValue());
					NazioneDTO nazioneNascita = new NazioneDTO();
					nazioneNascita.setIdNazione(Long.valueOf(soggettoDst.getIdStatoNascita()));

					soggettoDst.setNazioneNascita(nazioneNascita);

				} else {
//				setto la nazione estera per la citta estera
					if (fonteSCRIVA.getCittaEsteraNascita() != null) {
						Long idNazioneFonte = soggettoDAO.decodeId("risca_d_nazione",
								"cod_istat_nazione='" + fonteSCRIVA.getNazioneNascita().getCodIstatNazione() + "'");
						soggettoDst.setIdStatoNascita(idNazioneFonte.intValue());
						NazioneDTO nazioneNascita = new NazioneDTO();
						nazioneNascita.setIdNazione(Long.valueOf(soggettoDst.getIdStatoNascita()));

						soggettoDst.setNazioneNascita(nazioneNascita);
					} else {
						if (fonteSCRIVA != null && fonteSCRIVA.getComuneSedeLegale() != null
								&& fonteSCRIVA.getComuneSedeLegale().getProvincia() != null
								&& fonteSCRIVA.getComuneSedeLegale().getProvincia().getRegione() != null
								&& fonteSCRIVA.getComuneSedeLegale().getProvincia().getRegione().getNazione() != null) {
							Long idNazioneFonte = fonteSCRIVA.getComuneSedeLegale().getProvincia().getRegione()
									.getNazione().getIdNazione();
							if (idNazioneFonte != null) {
								soggettoDst.setIdStatoNascita(idNazioneFonte.intValue());
								NazioneDTO nazioneNascita = new NazioneDTO();
								nazioneNascita.setIdNazione(idNazioneFonte);
								soggettoDst.setNazioneNascita(nazioneNascita);
								fonteSCRIVA.setNazioneSedeLegale(
										fonteSCRIVA.getComuneSedeLegale().getProvincia().getRegione().getNazione());
							}
						}

					}

				}
				TipoNaturaGiuridicaDTO tipoNaturaGiuridicaFonte = fonteSCRIVA.getTipoNaturaGiuridica();
				if (tipoNaturaGiuridicaFonte != null) {
					soggettoDst.setTipiNaturaGiuridica(null);

					Long idTipoNaturaGiuridicaFonte = fonteSCRIVA.getTipoSoggetto() == null ? null
							: soggettoDAO.decodeId("risca_d_tipo_natura_giuridica", "id_tipo_natura_giuridica='"
									+ tipoNaturaGiuridicaFonte.getIdTipoNaturaGiuridica() + "'");
					if (idTipoNaturaGiuridicaFonte != null) {
						soggettoDst.setIdTipoNaturaGiuridica(idTipoNaturaGiuridicaFonte);
						TipiNaturaGiuridicaDTO tipiNaturaGiuridica = new TipiNaturaGiuridicaDTO();
						tipiNaturaGiuridica.setIdTipoNaturaGiuridica(soggettoDst.getIdTipoNaturaGiuridica());

						soggettoDst.setTipiNaturaGiuridica(tipiNaturaGiuridica);

					}
				}

				soggettoDst.setIdAmbito(idAmbito);
				soggettoDst.setIdTipoSoggetto(idTipoSoggetto);

				// search for existing "recapitoPrincipale"
				RecapitiExtendedDTO recapitoPrincipaleSoggetto = new RecapitiExtendedDTO();
				for (RecapitiExtendedDTO recapito : CollectionUtils.emptyIfNull(soggettoDst.getRecapiti()))
					if ("P".equals(recapito.getTipoRecapito().getCodTipoRecapito()))
						recapitoPrincipaleSoggetto = recapito;

				final RecapitiExtendedDTO recapitoPrincipale = recapitoPrincipaleSoggetto;

				recapitoPrincipale
						.setIdTipoRecapito(soggettoDAO.decodeId("risca_d_tipo_recapito", "cod_tipo_recapito='P'"));
				TipiRecapitoDTO tipoRecapito = new TipiRecapitoDTO();
				tipoRecapito.setIdTipoRecapito(recapitoPrincipale.getIdTipoRecapito());

				recapitoPrincipale.setTipoRecapito(tipoRecapito);

				recapitoPrincipale.setIdTipoSede(
						soggettoDAO.decodeId("risca_d_tipo_sede", "ind_default = '" + codTipoSoggetto + "'"));
				TipiSedeDTO tipoSede = new TipiSedeDTO();
				tipoSede.setIdTipoSede(recapitoPrincipale.getIdTipoSede());

				recapitoPrincipale.setTipoSede(tipoSede);

				recapitoPrincipale.setIdFonte(soggettoDAO.decodeId("risca_d_fonte", "cod_fonte='SCRIVA'"));
				FonteDTO fonte =new FonteDTO();
				fonte.setIdFonte(recapitoPrincipale.getIdFonte());
				recapitoPrincipale.setFonte(fonte);
				recapitoPrincipale.setIdFonteOrigine(soggettoDst.getIdFonte());

				// Fixmi
//			if(recapitoPrincipale.getIdTipoInvio() == null)
//				recapitoPrincipale.setIdTipoInvio(3L /* CARTACEO */);
//
//			recapitoPrincipale.setTipoInvio(new TipiInvioDTO() {{ setIdTipoInvio(recapitoPrincipale.getIdTipoInvio()); }});

				if (recapitoPrincipale.getCodRecapito() == null)
					try {
						recapitoPrincipale
								.setCodRecapito("RRI" + soggettoDAO.findNextSequenceValue("seq_risca_r_recapito"));
					} catch (Exception e) {
						LOGGER.error(
								"[SoggettiApiServiceImpl::loadSoggettiByIdAmbAndTipoSoggAndCfSogg] ERROR : idAmbito ["
										+ idAmbito + "] e idTipoSoggetto [" + idTipoSoggetto + "] " + e);
						return null; // TODO: handle proper exception
					}

				Long idComuneRecapitoFonte = soggettoDAO.decodeId("risca_d_comune",
						"cod_istat_comune='" + (isPF
								? fonteSCRIVA.getComuneResidenza() == null ? null
										: fonteSCRIVA.getComuneResidenza().getCodIstatComune()
								: fonteSCRIVA.getComuneSedeLegale() == null ? null
										: fonteSCRIVA.getComuneSedeLegale().getCodIstatComune())
								+ "'");
				checkIfFieldHasBeenModified(listaCampiAggiornati, "id_comune_recapito",
						recapitoPrincipale.getIdComuneRecapito(), idComuneRecapitoFonte);
				recapitoPrincipale.setIdComuneRecapito(idComuneRecapitoFonte);
				ComuneExtendedDTO comuneRecapito = new ComuneExtendedDTO();
				comuneRecapito.setIdComune(recapitoPrincipale.getIdComuneRecapito());

				ProvinciaExtendedDTO provinciaRecapito = new ProvinciaExtendedDTO();
				if (isPF) {
					ComuneExtendedDTO comuneResidenza = fonteSCRIVA.getComuneResidenza();

					provinciaRecapito.setIdProvincia(
							comuneResidenza == null ? null : comuneResidenza.getProvincia().getIdProvincia());
					provinciaRecapito.setSiglaProvincia(
							comuneResidenza == null ? null : comuneResidenza.getProvincia().getSiglaProvincia());

					comuneRecapito.setDenomComune(comuneResidenza == null ? null : comuneResidenza.getDenomComune());
				} else {
					ComuneExtendedDTO comuneSedeLegale = fonteSCRIVA.getComuneSedeLegale();

					provinciaRecapito.setIdProvincia(
							comuneSedeLegale == null ? null : comuneSedeLegale.getProvincia().getIdProvincia());
					provinciaRecapito.setSiglaProvincia(
							comuneSedeLegale == null ? null : comuneSedeLegale.getProvincia().getSiglaProvincia());

					comuneRecapito.setDenomComune(comuneSedeLegale == null ? null : comuneSedeLegale.getDenomComune());
				}

				comuneRecapito.setProvincia(provinciaRecapito);
				recapitoPrincipale.setComuneRecapito(comuneRecapito);

				Long idNazioneRecapitoFonte = soggettoDAO.decodeId("risca_d_nazione",
						"id_nazione='" + (isPF
								? fonteSCRIVA.getNazioneResidenza() == null ? null
										: fonteSCRIVA.getNazioneResidenza().getIdNazione()
								: fonteSCRIVA.getNazioneSedeLegale() == null ? null
										: fonteSCRIVA.getNazioneSedeLegale().getIdNazione())
								+ "'");
				if (idNazioneRecapitoFonte != null) {
					checkIfFieldHasBeenModified(listaCampiAggiornati, "id_nazione_recapito",
							recapitoPrincipale.getIdNazioneRecapito(), idNazioneRecapitoFonte);
					recapitoPrincipale.setIdNazioneRecapito(idNazioneRecapitoFonte);
					NazioniDTO nazioneRecapito = new NazioniDTO();
					nazioneRecapito.setIdNazione(idNazioneRecapitoFonte);
					if (isPF) {
						nazioneRecapito.setDenomNazione(fonteSCRIVA.getNazioneResidenza().getDenomNazione());
					} else {
						nazioneRecapito.setDenomNazione(fonteSCRIVA.getNazioneSedeLegale().getDenomNazione());
					}

					recapitoPrincipale.setNazioneRecapito(nazioneRecapito);

				}

				checkIfFieldHasBeenModified(listaCampiAggiornati, "indirizzo", recapitoPrincipale.getIndirizzo(),
						fonteSCRIVA.getIndirizzoSoggetto());
				recapitoPrincipale.setIndirizzo(fonteSCRIVA.getIndirizzoSoggetto());
				checkIfFieldHasBeenModified(listaCampiAggiornati, "num_civico", recapitoPrincipale.getNumCivico(),
						fonteSCRIVA.getNumCivicoIndirizzo());
				recapitoPrincipale.setNumCivico(fonteSCRIVA.getNumCivicoIndirizzo());
				checkIfFieldHasBeenModified(listaCampiAggiornati, "email", recapitoPrincipale.getEmail(),
						fonteSCRIVA.getDesEmail());
				recapitoPrincipale.setEmail(fonteSCRIVA.getDesEmail());
				checkIfFieldHasBeenModified(listaCampiAggiornati, "pec", recapitoPrincipale.getPec(),
						fonteSCRIVA.getDesPec());
				recapitoPrincipale.setPec(fonteSCRIVA.getDesPec());
				checkIfFieldHasBeenModified(listaCampiAggiornati, "telefono", recapitoPrincipale.getTelefono(),
						fonteSCRIVA.getNumTelefono());
				recapitoPrincipale.setTelefono(fonteSCRIVA.getNumTelefono());
				checkIfFieldHasBeenModified(listaCampiAggiornati, "cellulare", recapitoPrincipale.getCellulare(),
						fonteSCRIVA.getNumCellulare());
				recapitoPrincipale.setCellulare(fonteSCRIVA.getNumCellulare());
				checkIfFieldHasBeenModified(listaCampiAggiornati, "des_loaclita", recapitoPrincipale.getDesLocalita(),
						fonteSCRIVA.getDesLocalita());
				recapitoPrincipale.setDesLocalita(fonteSCRIVA.getDesLocalita());

				String cittaEsteraRecapitoFonte = isPF ? fonteSCRIVA.getCittaEsteraResidenza()
						: fonteSCRIVA.getCittaEsteraSedeLegale();
				checkIfFieldHasBeenModified(listaCampiAggiornati, "citta_estera_recapito",
						recapitoPrincipale.getCittaEsteraRecapito(), cittaEsteraRecapitoFonte);
				recapitoPrincipale.setCittaEsteraRecapito(cittaEsteraRecapitoFonte);

				String capRecapitoFonte = isPF ? fonteSCRIVA.getCapResidenza() : fonteSCRIVA.getCapSedeLegale();
				checkIfFieldHasBeenModified(listaCampiAggiornati, "cap_recapito", recapitoPrincipale.getCapRecapito(),
						capRecapitoFonte);
				recapitoPrincipale.setCapRecapito(capRecapitoFonte);

				recapitoPrincipale.setGestAttoreIns(CodFiscaleAttore);
				recapitoPrincipale.setGestAttoreUpd(CodFiscaleAttore);

				List<RecapitiExtendedDTO> recapitiList = new ArrayList<>();
				recapitiList.add(recapitoPrincipale);

				soggettoDst.setRecapiti(recapitiList);

			}
			Long idSoggetto;
			try {
				if (soggettoDst.getIdSoggetto() != null)
					idSoggetto = soggettoDAO.updateSoggetto(soggettoDst, null, null); // aggiorna il nuovo soggetto
																						// preso dalla fonte e anche
																						// indirizzo di spedizione
				else
					soggettoDst.setIdSoggetto(idSoggetto = soggettoDAO.saveSoggetto(soggettoDst, null)); // inserisce il
																											// nuovo
																											// soggetto
																											// preso
																											// dalla
																											// fonte

				soggetto = soggettoDAO.loadSoggettoById(idSoggetto);
			} catch (BusinessException be) {
				throw be;
			} catch (GenericException e) {
				LOGGER.error(
						"[SoggettiApiServiceImpl::loadSoggettiByIdAmbAndTipoSoggAndCfSogg] ERROR GenericException: idAmbito ["
								+ idAmbito + "] e idTipoSoggetto [" + idTipoSoggetto + "]");
				if (e.getErroObjectDTO() != null)
					return Response.serverError().entity(e.getErroObjectDTO())
							.status(Integer.parseInt(e.getErroObjectDTO().getStatus())).build();
			} catch (Exception e) {
				LOGGER.error(
						"[SoggettiApiServiceImpl::loadSoggettiByIdAmbAndTipoSoggAndCfSogg] ERROR idSoggetto null: idAmbito ["
								+ idAmbito + "] e idTipoSoggetto [" + idTipoSoggetto + "]");
				return buildErrorRepsonse(404, "E073", "Impossibile salvare il soggetto", null);
			}

			if (listaCampiAggiornati.length() > 0)
				response.setCodiceMessaggio("I012");
		}

		if (soggetto == null || soggetto.getIdSoggetto() == null) {
			LOGGER.error(
					"[SoggettiApiServiceImpl::loadSoggettiByIdAmbAndTipoSoggAndCfSogg] ERROR idSoggetto = null : idAmbito ["
							+ idAmbito + "] e idTipoSoggetto [" + idTipoSoggetto + "]");
			return buildErrorRepsonse(404, isGestioneAbilitata ? "A008" : "I001", isGestioneAbilitata
					? "Attenzione: il SOGGETTO che hai cercato non e' presente. Vuoi inserirlo?"
					: "La ricerca non ha restituito alcun risultato. Modificare i criteri di ricerca e riprovare.",
					null);
		}

		response.setSoggetto(soggetto);
		response.setFontiAmbito(fontiAmbito.length() == 0 ? "" : fontiAmbito.substring(1));
		response.setIsAbilitato(isAbilitato ? "S" : "N");
		response.setIsGestioneAbilitata(isGestioneAbilitata ? "S" : "N");
		response.setIsFonteAbilitataInLettura(isFonteAbilitataInLettura ? "S" : "N");
		response.setIsFonteAbilitataInScrittura(isFonteAbilitataInScrittura ? "S" : "N");
		response.setListaCampiAggiornati(listaCampiAggiornati.length() == 0 ? "" : listaCampiAggiornati.substring(1));

		response.setFonteRicerca(soggetto.getFonte() != null ? soggetto.getFonte().getCodFonte() : "");

		LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettiByIdAmbAndTipoSoggAndCfSogg] END");
		return Response.ok(response).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private StringBuffer checkIfFieldHasBeenModified(StringBuffer sb, String fieldName, Object fieldSrc,
			Object fieldFonte) {
		if (fieldSrc == null && fieldFonte != null)
			return sb;

		if (((fieldFonte == null && fieldSrc != null))
				|| (fieldSrc instanceof String && !StringUtils.equals((String) fieldSrc, (String) fieldFonte))
				|| (fieldSrc instanceof Number && ((Number) fieldSrc).longValue() != ((Number) fieldFonte).longValue())
				|| (fieldSrc instanceof Date && ((Date) fieldSrc).getTime() != ((Date) fieldFonte).getTime())
				|| (fieldSrc instanceof Boolean
						&& ((Boolean) fieldSrc).booleanValue() != ((Boolean) fieldFonte).booleanValue()))
			return sb.append("," + fieldName);

		return sb;
	}

	@Override
	public Response loadSoggettoById(Long idSoggetto, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettoById] BEGIN");
		LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettoById] Parametri in input idSoggetto [" + idSoggetto + "] ");
		SoggettiExtendedDTO soggetto = null;
		try {
			LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettoById] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_SOGG);
			LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettoById] verificaIdentitaDigitale END");

			long start = System.currentTimeMillis();
			soggetto = soggettoDAO.loadSoggettoById(idSoggetto);
			long stop = System.currentTimeMillis();
			LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettoById] QueryExecutionTime: " + (stop - start));

		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

		}

		LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettoById] END");
		return Response.ok(soggetto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	@Transactional
	public Response saveSoggetto(SoggettiExtendedDTO soggetto, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[SoggettiApiServiceImpl::saveSoggetto] BEGIN");
		LOGGER.debug("[SoggettiApiServiceImpl::saveSoggetto] Parametro in input soggetto :\n " + soggetto);

		Long id = null;
		WarningDTO warningDTO = null;
		try {
			setGestAttoreInsUpd(soggetto, fruitore, httpRequest, httpHeaders);
			for (RecapitiExtendedDTO recapito : soggetto.getRecapiti()) {
				setGestAttoreInsUpd(recapito, fruitore, httpRequest, httpHeaders);
			}
		    businessLogic.validatorDTO(soggetto, null, null);
			LOGGER.debug("[SoggettiApiServiceImpl::saveSoggetto] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, soggetto.getIdAmbito(), httpHeaders,
					Constants.POST_PUT_DEL_SOGG);
			LOGGER.debug("[SoggettiApiServiceImpl::saveSoggetto] verificaIdentitaDigitale END");

			id = soggettoDAO.saveSoggetto(soggetto, fruitore);
			soggetto.setIdSoggetto(id);
		}  catch (ValidationException  ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:"+ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003,ve.getMessage(), null, null);
			   return Response.status(Response.Status.BAD_REQUEST)
	                   .entity(err)
	                   .build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (GenericException e) {

			if (e.getErroObjectDTO() != null) {
				LOGGER.error("[SoggettiApiServiceImpl::saveSoggetto] ERROR salvataggio soggetto: "
						+ e.getErroObjectDTO().getStatus());
				LOGGER.debug("[SoggettiApiServiceImpl::saveSoggetto] END");
				return Response.serverError().entity(e.getErroObjectDTO())
						.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
			} else
				return Response.serverError().entity(e.getError()).status(Integer.parseInt(e.getError().getStatus()))
						.build();

		} catch (Exception e) {
			LOGGER.debug("[SoggettiApiServiceImpl::saveSoggetto] END");
			return buildErrorRepsonse(404, "E073", "Impossibile salvare il soggetto", null);

		}

		if (id != null) {
			LOGGER.debug("[SoggettiApiServiceImpl::saveSoggetto] BEGIN Integrazione con scriva");
			SoggettoExtendedDTO dtoFonte = null;
			String ambito = String.valueOf(soggetto.getIdAmbito());

			String fontiAmbito = "";
			boolean isFonteAbilitataInLettura = false;
			boolean isFonteAbilitataInScrittura = false;
			try {
				for (AmbitoConfigDTO ambitiConfig : CollectionUtils
						.emptyIfNull(ambitiConfigDao.loadAmbitiConfigByIdOrCodAmbito(ambito))) {
					if (ambitiConfig.getChiave().equals("FonteAmbito"))
						fontiAmbito += "," + ambitiConfig.getValore();
					else if (ambitiConfig.getChiave().equals("SOGGETTO.isFonteAbilitataInLettura"))
						isFonteAbilitataInLettura = ambitiConfig.getValore().equals("S");
					else if (ambitiConfig.getChiave().equals("SOGGETTO.isFonteAbilitataInScrittura"))
						isFonteAbilitataInScrittura = ambitiConfig.getValore().equals("S");
				}
			} catch (Exception e1) {
				ErrorDTO err = new ErrorDTO("500", "E005",
						"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

			}

			String codTipoSoggetto = soggetto.getTipoSoggetto().getIdTipoSoggetto() == 1 ? "PF"
					: (soggetto.getTipoSoggetto().getIdTipoSoggetto() == 3 ? "PB" : "PG");
			if (isFonteAbilitataInLettura) {
				if (fontiAmbito.indexOf("SCRIVA") > -1) {
					List<SoggettoExtendedDTO> dtoFonti = scrivaServiceHelper.getSoggettoFromScriva(null, soggetto
							.getCfSoggetto(), codTipoSoggetto, null, /* fixme: where to get "tipoAdempimento"? */
							null /* fixme: where to get "codiceFiscaleImpresa"? */);

					dtoFonte = dtoFonti == null || dtoFonti.size() == 0 ? null : dtoFonti.get(0);
				}
			}

			LOGGER.debug("[SoggettiApiServiceImpl::saveSoggetto] isFonteAbilitataInScrittura ="
					+ isFonteAbilitataInScrittura);
			if (isFonteAbilitataInScrittura) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dataAggFonte = dtoFonte == null ? null
						: (dtoFonte.getDataAggiornamento() != null ? dtoFonte.getDataAggiornamento() : null);

				SoggettoExtendedDTO soggettoScriva = null;
				SoggettoExtendedMapper soggettoMapper = new SoggettoExtendedMapper(dtoFonte, soggetto);
				try {
					soggettoScriva = soggettoMapper.mapSoggettoRiscaToSoggettoScriva();
					if (dtoFonte == null) {
						if (!Utils.isValidateCFPIva(soggettoScriva.getPartitaIvaSoggetto(),
								soggettoScriva.getCfSoggetto())) {
							MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.I025);
							warningDTO = new WarningDTO(Constants.I025,
									Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
						}
						if (warningDTO == null)
							scrivaServiceHelper.saveSoggettoInScriva(soggettoScriva);
					} else {
						String dataAggiornamento = soggetto.getDataAggiornamento().replace("T", " ").replace("Z", "");
						Date dataRisca = formatter.parse(dataAggiornamento);
						if (dataRisca.getTime() > dataAggFonte.getTime()) {
							if (!Utils.isValidateCFPIva(soggettoScriva.getPartitaIvaSoggetto(),
									soggettoScriva.getCfSoggetto())) {
								MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.I025);
								warningDTO = new WarningDTO(Constants.I025,
										Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
							}
							if (warningDTO == null)
								scrivaServiceHelper.updateSoggettoInScriva(soggettoScriva);
						}
					}

				} catch (ParseException e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					LOGGER.error("[SoggettiApiServiceImpl::saveSoggetto:: operazione saveSoggettoInScriva]: " + e);
					return buildErrorRepsonse(404, "E073", "Impossibile salvare il soggetto", null);
				} catch (BusinessException e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					LOGGER.error("[SoggettiApiServiceImpl::saveSoggetto:: operazione saveSoggettoInScriva]: " + e);
					return buildErrorRepsonse(404, "E073", "Impossibile salvare il soggetto", null);

				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					LOGGER.error("[SoggettiApiServiceImpl::saveSoggetto:: operazione saveSoggettoInScriva]: " + e);
					return buildErrorRepsonse(404, "E073", "Impossibile salvare il soggetto", null);
				}
			}

			LOGGER.debug("[SoggettiApiServiceImpl::saveSoggetto] END Integrazione con scriva");

			try {
				LOGGER.debug("[SoggettiApiServiceImpl::saveSoggetto] BEGIN save tracciamento");
				Identita identita = IdentitaDigitaleManager
						.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);

				tracciamentoManager.saveTracciamento(fruitore, soggetto, identita, null, "JSON SOGGETTO",
						soggetto.getIdSoggetto() != null ? soggetto.getIdSoggetto().toString() : null, "RISCA_T_SOGGETTO",
						Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, true, httpRequest);
				
				tracciamentoManager.saveTracciamento(fruitore, soggetto, identita, null, "JSON SOGGETTO",
						soggetto.getIdSoggetto() != null ? soggetto.getIdSoggetto().toString() : null, "RISCA_R_RECAPITO",
						Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, false, httpRequest);
				
				LOGGER.debug("[SoggettiApiServiceImpl::saveSoggetto] END save tracciamento");

			} catch (Exception e) {
				LOGGER.error("[SoggettiApiServiceImpl::saveSoggetto:: operazione insertLogAudit]: " + e);
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
						.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
			}
			LOGGER.debug("[SoggettiApiServiceImpl::saveSoggetto] END");
		}
		String jsonWarning = null;
		if (warningDTO != null) {
			JSONObject jsonWarningHeader = new JSONObject(warningDTO);
			jsonWarning = jsonWarningHeader.toString();
		}
		return Response.ok(soggetto).header("jsonWarning", jsonWarning).header(HttpHeaders.CONTENT_ENCODING, IDENTITY)
				.build();
	}

	@Override
	@Transactional
	public Response updateSoggetto(SoggettiExtendedDTO soggetto, String fruitore, Long indModManuale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[SoggettiApiServiceImpl::updateSoggetto] BEGIN");
		LOGGER.debug("[SoggettiApiServiceImpl::updateSoggetto] Parametro in input soggetto :\n " + soggetto);

		Long id = null;
		WarningDTO warningDTO = null;
		try {
			setGestAttoreInsUpd(soggetto, fruitore, httpRequest, httpHeaders);
			for (RecapitiExtendedDTO recapito : soggetto.getRecapiti()) {
				setGestAttoreInsUpd(recapito, fruitore, httpRequest, httpHeaders);
			}
			
		    businessLogic.validatorDTO(soggetto, null, null);
			LOGGER.debug("[SoggettiApiServiceImpl::updateSoggetto] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, soggetto.getIdAmbito(), httpHeaders,
					Constants.POST_PUT_DEL_SOGG);
			LOGGER.debug("[SoggettiApiServiceImpl::updateSoggetto] verificaIdentitaDigitale END");

			id = soggettoDAO.updateSoggetto(soggetto, fruitore, indModManuale);
		}  catch (ValidationException  ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:"+ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003,ve.getMessage(), null, null);
			   return Response.status(Response.Status.BAD_REQUEST)
	                   .entity(err)
	                   .build();
		
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (GenericException e) {
			if (e.getErroObjectDTO() != null) {
				LOGGER.error("[SoggettiApiServiceImpl::updateSoggetto] ERROR salvataggio soggetto: "
						+ e.getErroObjectDTO().getStatus());
				LOGGER.debug("[SoggettiApiServiceImpl::updateSoggetto] END");
				return Response.serverError().entity(e.getErroObjectDTO())
						.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
			} else
				return Response.serverError().entity(e.getError()).status(Integer.parseInt(e.getError().getStatus()))
						.build();
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

		}

		if (id != null) {
			LOGGER.debug("[SoggettiApiServiceImpl::updateSoggetto] BEGIN Integrazione con scriva");
			SoggettoExtendedDTO dtoFonte = null;
			String ambito = String.valueOf(soggetto.getIdAmbito());

			String fontiAmbito = "";
			boolean isFonteAbilitataInLettura = false;
			boolean isFonteAbilitataInScrittura = false;
			try {
				for (AmbitoConfigDTO ambitiConfig : CollectionUtils
						.emptyIfNull(ambitiConfigDao.loadAmbitiConfigByIdOrCodAmbito(ambito))) {
					if (ambitiConfig.getChiave().equals("FonteAmbito"))
						fontiAmbito += "," + ambitiConfig.getValore();
					else if (ambitiConfig.getChiave().equals("SOGGETTO.isFonteAbilitataInLettura"))
						isFonteAbilitataInLettura = ambitiConfig.getValore().equals("S");
					else if (ambitiConfig.getChiave().equals("SOGGETTO.isFonteAbilitataInScrittura"))
						isFonteAbilitataInScrittura = ambitiConfig.getValore().equals("S");
				}
			} catch (Exception e1) {
				ErrorDTO err = new ErrorDTO("500", "E005",
						"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

			}

			String codTipoSoggetto = soggetto.getTipoSoggetto().getIdTipoSoggetto() == 1 ? "PF"
					: (soggetto.getTipoSoggetto().getIdTipoSoggetto() == 3 ? "PB" : "PG");
			if (isFonteAbilitataInLettura) {
				if (fontiAmbito.indexOf("SCRIVA") > -1) {
					List<SoggettoExtendedDTO> dtoFonti = scrivaServiceHelper.getSoggettoFromScriva(null, soggetto
							.getCfSoggetto(), codTipoSoggetto, null, /* fixme: where to get "tipoAdempimento"? */
							null /* fixme: where to get "codiceFiscaleImpresa"? */);

					dtoFonte = dtoFonti == null || dtoFonti.size() == 0 ? null : dtoFonti.get(0);
				}
			}

			LOGGER.debug("[SoggettiApiServiceImpl::updateSoggetto] isFonteAbilitataInScrittura ="
					+ isFonteAbilitataInScrittura);
			if (isFonteAbilitataInScrittura) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dataAggFonte = dtoFonte == null ? null
						: (dtoFonte.getDataAggiornamento() != null ? dtoFonte.getDataAggiornamento() : null);

				SoggettoExtendedDTO soggettoScriva = null;
				SoggettoExtendedMapper soggettoMapper = new SoggettoExtendedMapper(dtoFonte, soggetto);
				try {
					soggettoScriva = soggettoMapper.mapSoggettoRiscaToSoggettoScriva();
					if (dtoFonte == null) {
						if (!Utils.isValidateCFPIva(soggettoScriva.getPartitaIvaSoggetto(),
								soggettoScriva.getCfSoggetto())) {
							MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.I025);
							warningDTO = new WarningDTO(Constants.I025,
									Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
						}
						if (warningDTO == null)
							scrivaServiceHelper.saveSoggettoInScriva(soggettoScriva);
					} else {
						String dataAggiornamento = soggetto.getDataAggiornamento().replace("T", " ").replace("Z", "");
						Date dataRisca = formatter.parse(dataAggiornamento);

						if (dataRisca.getTime() > dataAggFonte.getTime()) {
							if (!Utils.isValidateCFPIva(soggettoScriva.getPartitaIvaSoggetto(),
									soggettoScriva.getCfSoggetto())) {
								MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.I025);
								warningDTO = new WarningDTO(Constants.I025,
										Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
							}
							if (warningDTO == null)
								scrivaServiceHelper.updateSoggettoInScriva(soggettoScriva);
						}
					}

				} catch (ParseException e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					LOGGER.error("[SoggettiApiServiceImpl::updateSoggetto:: operazione saveSoggettoInScriva]: " , e);
					return buildErrorRepsonse(404, "E073", "Impossibile salvare il soggetto", null);
				} catch (BusinessException e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					LOGGER.error("[SoggettiApiServiceImpl::updateSoggetto:: operazione saveSoggettoInScriva]: " , e);
					return buildErrorRepsonse(404, "E073", "Impossibile salvare il soggetto", null);

				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					LOGGER.error("[SoggettiApiServiceImpl::updateSoggetto:: operazione saveSoggettoInScriva]: " , e);
					return buildErrorRepsonse(404, "E073", "Impossibile salvare il soggetto", null);
				}
			}

			LOGGER.debug("[SoggettiApiServiceImpl::updateSoggetto] END Integrazione con scriva");
			try {
				LOGGER.debug("[SoggettiApiServiceImpl::updateSoggetto] BEGIN save tracciamento");

				Identita identita = IdentitaDigitaleManager
						.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);


				tracciamentoManager.saveTracciamento(fruitore, soggetto, identita, null, "JSON SOGGETTO",
						soggetto.getIdSoggetto() != null ? soggetto.getIdSoggetto().toString() : null, "RISCA_T_SOGGETTO",
						Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, true, httpRequest);
				
				tracciamentoManager.saveTracciamento(fruitore, soggetto, identita, null, "JSON SOGGETTO",
						soggetto.getIdSoggetto() != null ? soggetto.getIdSoggetto().toString() : null, "RISCA_R_RECAPITO",
						Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, false, httpRequest);
				
				LOGGER.debug("[SoggettiApiServiceImpl::updateSoggetto] END save tracciamento");
			} catch (Exception e) {
				LOGGER.error("[SoggettiApiServiceImpl::updateSoggetto:: operazione updateLogAudit]: " , e);
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
						.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
			}

		}
		String jsonWarning = null;
		if (warningDTO != null) {
			JSONObject jsonWarningHeader = new JSONObject(warningDTO);
			jsonWarning = jsonWarningHeader.toString();
		}
		return Response.ok(soggetto).header("jsonWarning", jsonWarning).header(HttpHeaders.CONTENT_ENCODING, IDENTITY)
				.build();
	}

	@Override
	@Transactional
	public Response deleteSoggetto(Long idSoggetto, String fruitore, Long idRecapito, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[SoggettiApiServiceImpl::deleteSoggetto] BEGIN");
		LOGGER.debug("[SoggettiApiServiceImpl::deleteSoggetto] Parametro in input idSoggetto :" + idSoggetto
				+ "idRecapito:" + idRecapito);
		SoggettiExtendedDTO soggetto = null;
		SoggettiExtendedDTO soggettoExtendedDTO = null;
		try {
			LOGGER.debug("[SoggettiApiServiceImpl::deleteSoggetto] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_SOGG);			
			LOGGER.debug("[SoggettiApiServiceImpl::deleteSoggetto] verificaIdentitaDigitale BEGIN");
			soggettoExtendedDTO =soggettoDAO.loadSoggettoById(idSoggetto);
			soggetto = soggettoDAO.deleteSoggetto(idSoggetto, idRecapito);
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		try {
			LOGGER.debug("[SoggettiApiServiceImpl::deleteSoggetto] BEGIN save tracciamento");

			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);

			
			tracciamentoManager.saveTracciamento(fruitore, soggettoExtendedDTO, identita, null, "JSON SOGGETTO",
					idSoggetto != null ? idSoggetto.toString() : null, "RISCA_T_SOGGETTO",
					Constants.FLG_OPERAZIONE_DELETE, Constants.OPERAZIONE_DELETE, true, true, httpRequest);
			tracciamentoManager.saveTracciamento(fruitore, soggettoExtendedDTO, identita, null, "JSON SOGGETTO",
					idSoggetto != null ? idSoggetto.toString() : null, "RISCA_R_RECAPITO",
					Constants.FLG_OPERAZIONE_DELETE, Constants.OPERAZIONE_DELETE, false, true, httpRequest);
			
			LOGGER.debug("[SoggettiApiServiceImpl::deleteSoggetto] END save tracciamento");

		} catch (Exception e) {
			LOGGER.error("[SoggettiApiServiceImpl::deleteSoggetto:: operazione deleteLogAudit]: " ,e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}

		return Response.ok(soggetto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadSoggettiByCampoRicerca(Long idAmbito, String campoRicerca, Integer offset, Integer limit,
			String sort, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettiByCampoRicerca] BEGIN");
		LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettiByCampoRicerca] Parametri in input idAmbito [" + idAmbito
				+ "] e campoRicerca [" + campoRicerca + "] ");
		Identita identita = IdentitaDigitaleManager
				.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), null);

		Application cod = new Application();
		cod.setId("RISCA");
		UseCase use = new UseCase();
		use.setAppId(cod);
		use.setId("UC_SIPRA");
		Long idAmbitoSess = null;
		if (identita != null) {

			idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
			LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettiByCampoRicerca] idAmbitoSess:" + idAmbitoSess);
			if (idAmbito != null && idAmbitoSess != null) {
				LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettiByCampoRicerca] idAmbito:" + idAmbito);
				if (!idAmbito.equals(idAmbitoSess)) {
					ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
					return Response.serverError().entity(err).status(401).build();
				}
			}
		}
		List<SoggettiExtendedDTO> soggetto;
		Integer numberAllSoggetti;
		try {
			soggetto = soggettoDAO.loadSoggettiByCampoRicerca(idAmbito, campoRicerca, offset, limit, sort);
			numberAllSoggetti = soggettoDAO.countSoggettiByCampoRicerca(idAmbito, campoRicerca);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

		}
		PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
		paginationHeader.setTotalElements(numberAllSoggetti);
		paginationHeader.setTotalPages((numberAllSoggetti / limit) + ((numberAllSoggetti % limit) == 0 ? 0 : 1));
		paginationHeader.setPage(offset);
		paginationHeader.setPageSize(limit);
		paginationHeader.setSort(sort);
		JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
		String jsonString = jsonPaginationHeader.toString();

		LOGGER.debug("[SoggettiApiServiceImpl::loadSoggettiByCampoRicerca] END");
		return Response.ok(soggetto).header("PaginationInfo", jsonString).header(HttpHeaders.CONTENT_ENCODING, IDENTITY)
				.build();
	}

	@Override
	public Response loadSoggettoExtByIdSoggetto(Long idSoggetto, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		SoggettiExtendedDTO soggetto = null;
		try {
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_SOGG);
			soggetto = soggettoDAO.getSoggettoById(idSoggetto);
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005","Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}finally {
		    LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(soggetto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY)
				.build();
	}

}
