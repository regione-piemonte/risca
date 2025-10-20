/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.identitadigitale;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.iride2.iridefed.entity.Ruolo;
import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;
import it.csi.iride2.policy.exceptions.MalformedIdTokenException;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitoFonteDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.FonteDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ProfilazioneDAO;
import it.csi.risca.riscabesrv.dto.FonteDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.ProfilazioneDTO;
import it.csi.risca.riscabesrv.dto.ProfiloOggAppDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;


public class IdentitaDigitaleManager  implements IdentitaDigitale{
	
	protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	
    @Autowired
    private IrideServiceHelper serviceHelper;
    
	@Autowired
    private ProfilazioneDAO profilazioneDAO;
	
    @Autowired
    private FonteDAO fonteDAO;
    
    @Autowired
    private AmbitoFonteDAO ambitoFonteDAO;
    
    @Autowired
    private MessaggiDAO messaggiDAO;

	@Override
	public void verificaIdentitaDigitale(String fruitore, Long idAmbitoInput, HttpHeaders httpHeaders, String codOggettoProfilo ) throws BusinessException, SQLException, SystemException {
		LOGGER.debug("[IdentitaDigitaleManager::verificaIdentitaDigitale] BEGIN");
        Utils utils = new Utils();
    	if(!utils.isLocalMod()){
			Boolean verifyIdentitaDigitale = true;
			String keyHeader = getKeyFromHeader(httpHeaders);
			if(keyHeader == null) {
				LOGGER.error("[IdentitaDigitaleManager::verificaIdentitaDigitale] keyHeader non e' valorizzato " + keyHeader);
				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.U001);
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
			}
			if(fruitore != null) {
				verifyIdentitaDigitale = isFruitoreBatch(fruitore, keyHeader);
			}
			if(!verifyIdentitaDigitale){
				Identita identita = getIdentitaDigitale(keyHeader, null);
				verifyIdentitaDigitale = isIdentitaAutentica(fruitore, identita);
				if(!verifyIdentitaDigitale){
					verifyIdentitaDigitale = isIdentitaDigitale(identita,fruitore, idAmbitoInput, codOggettoProfilo);
				}
			}
			LOGGER.debug("[IdentitaDigitaleManager::verificaIdentitaDigitale] END ");
			if(!verifyIdentitaDigitale) {	
				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.U001);
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
			}
    	}
		
	}
	
	 public static Identita getIdentitaDigitale(String keyHeader, String fruitore) {
		LOGGER.debug("[IdentitaDigitaleManager::getIdentitaDigitale] BEGIN");
		if(fruitore != null) 
			return null;
		Identita identita = null;
		try {
			if(keyHeader != null)
			   identita = new Identita(normalizeToken(keyHeader));
			} catch (MalformedIdTokenException e) {
				LOGGER.info("[IdentitaDigitaleManager::getIdentitaFromHeader] Malformed token " + e);
			}
		LOGGER.debug("[IdentitaDigitaleManager::getIdentitaDigitale] END");
		return identita;
	}
	 public String getCompetenzaTerritoriale(Identita identita) {
			LOGGER.info("[IdentitaDigitaleManager::getCompetenzaTerritoriale] BEGIN");
			String CompetenzaTerritoriale = null;
		    Utils utils = new Utils();
		    if(utils.isLocalMod()){
		    	 // Ambiente solo per test
//		    	return "PROV|H" ; 
//		    	return "COMU|H" ; 
	    	return null;
		    }

			if(identita != null) {
				Application cod = new Application();
				cod.setId("RISCA");
				UseCase use = new UseCase();
				use.setAppId(cod);
				use.setId("UC_SIPRA");
				CompetenzaTerritoriale = serviceHelper.getCompetenzaTerritoriale(identita, use);
			}
			LOGGER.info("[IdentitaDigitaleManager::getCompetenzaTerritoriale] CompetenzaTerritoriale :" + CompetenzaTerritoriale);
			return CompetenzaTerritoriale;
		}
	
	public <E> Long getAmbitoByIdentitaOrFonte(Identita identita, String fruitore, E elemento) {
		LOGGER.debug("[IdentitaDigitaleManager::getAmbitoByIdentitaOrFonte] BEGIN");
		Long idAmbito = null;
	    Utils utils = new Utils();
	    if(utils.isLocalMod()){
	    	return idAmbito = 1l ; // Ambiente 
	    }
	    if(elemento != null && fruitore != null) {
             Class<?> objectClass = elemento.getClass();
              Method getIdAmbito;
			try {
				getIdAmbito = objectClass.getMethod( "getIdAmbito");
	            Object idAmbitoValue = getIdAmbito.invoke(elemento) != null ? getIdAmbito.invoke(elemento)  : null;
	            if(idAmbitoValue != null)
	                return  (Long) idAmbitoValue;
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
	            LOGGER.error("[IdentitaDigitaleManager::getAmbitoByIdentitaOrFonte:: ERROR ]: "+e);
	        }
	    }
		if(identita != null) {
			Application cod = new Application();
			cod.setId("RISCA");
			UseCase use = new UseCase();
			use.setAppId(cod);
			use.setId("UC_SIPRA");
			idAmbito = serviceHelper.getInfoPersonaInUseCase(identita, use);
			LOGGER.debug("[IdentitaDigitaleManager::getAmbitoByIdentitaOrFonte] idAmbito:" + idAmbito);
		} else if(fruitore != null) {
			LOGGER.debug("[IdentitaDigitaleManager::getAmbitoByIdentitaOrFonte] get ambito by " +fruitore);
			 FonteDTO fonteDTO = fonteDAO.loadFonteByCodFonte(fruitore);
			 try {
				idAmbito = ambitoFonteDAO.findIdAmbitoByIdFonte(fonteDTO.getIdFonte());
				LOGGER.debug("[IdentitaDigitaleManager::getAmbitoByIdentitaOrFonte] ID AMBITO PER LA FONTE:"+fruitore+" : " + idAmbito);
			} catch (DAOException e) {
				LOGGER.info("[IdentitaDigitaleManager::getAmbitoByIdentitaOrFonte] NON ESISTE UN ID AMBITO PER LA FONTE "+ fruitore);
				return null;
			}
			
		}
		LOGGER.debug("[IdentitaDigitaleManager::getAmbitoByIdentitaOrFonte] END idAmbito:" + idAmbito);
		return idAmbito;
	}
	
	@Override
	public Boolean isIdentitaDigitale(Identita identita,String fruitore ,Long idAmbitoInput, String codOggettoProfilo) {
		LOGGER.debug("[IdentitaDigitaleManager::isIdentitaDigitale] BEGIN");
	  	if(identita == null) {
	  		return false;
	  	}
		Boolean isIdentitaDigitale = true;
		String ruolo = "";
		Long idAmbito = getAmbitoByIdentitaOrFonte(identita, fruitore,null);
		if(idAmbito != null) {
			if(idAmbitoInput != null) {
				if (!idAmbito.equals(idAmbitoInput)) {
					LOGGER.error("[IdentitaDigitaleManager::isIdentitaDigitale] idAmbito: "+idAmbito+" DIVERSO DA idAmbitoInput: "+idAmbitoInput);
					return isIdentitaDigitale = false;
				}
			}
		}else {
			LOGGER.error("[IdentitaDigitaleManager::isIdentitaDigitale] idAmbito non e' valorizzato: "+idAmbito);
			return isIdentitaDigitale = false;
		}
		Application cod = new Application();
		cod.setId("RISCA");
		Ruolo[] ruoli = serviceHelper.getRuoli(identita, cod);
		if (ruoli != null) {
			LOGGER.debug("[IdentitaDigitaleManager::isIdentitaDigitale] ruoli trovati: " + ruoli.length);
			LOGGER.debug("[IdentitaDigitaleManager::isIdentitaDigitale] ruolo: " + ruoli[0].getCodiceRuolo());
			ruolo = ruoli[0].getCodiceRuolo();
			ProfilazioneDTO profilazione = null;
			try {
				profilazione = profilazioneDAO.loadProfilazione(ruolo);
				List<ProfiloOggAppDTO> listProfiloOggettoApp = profilazione.getProfiloOggettoApp().stream().filter(
						p -> p.getFlgAttivo() == true && p.getOggettoApp().getCodOggettoApp().equals(codOggettoProfilo))
						.collect(Collectors.toList());

				if (listProfiloOggettoApp.isEmpty()) {
					LOGGER.error("[IdentitaDigitaleManager::isIdentitaDigitale] NON ESISTE UN PROFILO PER IL RUOLO SELEZIONATO");
					return isIdentitaDigitale = false;
				}
			} catch (Exception e) {
				LOGGER.error("[IdentitaDigitaleManager::isIdentitaDigitale] Errore nell'accesso ai dati", e);
			}
			
		}else {
			LOGGER.error("[IdentitaDigitaleManager::isIdentitaDigitale] NON ESISTE RUOLO PER IL PROFILO ");
		 return	isIdentitaDigitale = false;
		}
		
	     LOGGER.debug("[IdentitaDigitaleManager::isIdentitaDigitale] END");
		return 	isIdentitaDigitale;

	}
	@Override
	public Boolean isFruitoreBatch(String fruitore, String key) {
	     LOGGER.debug("[IdentitaDigitaleManager::isFruitoreBatch] BEGIN");
	 	Boolean isFruitoreBatch = true;
		FonteDTO fonteDTO = fonteDAO.loadFonteByChiaveFonte(fruitore, key);
		if (fonteDTO == null) {
		     LOGGER.error("[IdentitaDigitaleManager::isFruitoreBatch] NON ESISTE UN FONTE PER LA KEY PASSATA ");
			isFruitoreBatch = false;
		}
	     LOGGER.debug("[IdentitaDigitaleManager::isFruitoreBatch] END");
	 	return 	isFruitoreBatch;
	}
	@Override
	public Boolean isIdentitaAutentica(String fruitore, Identita identita) {
		LOGGER.debug("[IdentitaDigitaleManager::isIdentitaAutentica] BEGIN");
	  	if(identita == null) {
	  		return false;
	  	}
	  	Boolean isIdentitaAutentica = true;
		if (serviceHelper.isIdentitaAutentica(identita)) {
	    	 FonteDTO fonteDTO = fonteDAO.loadFonteByCodFonte(fruitore);
				if (fonteDTO == null) {
					LOGGER.error("[IdentitaDigitaleManager::isIdentitaAutentica] fonte non esistente ");
					isIdentitaAutentica = false;
				}
	     }else {
			LOGGER.error("[IdentitaDigitaleManager::isIdentitaAutentica] isIdentitaAutentica fallita ");
	    	isIdentitaAutentica = false;
		 }
	     LOGGER.debug("[IdentitaDigitaleManager::isIdentitaAutentica] END");
		 return isIdentitaAutentica;
	}
	
	 public static String getKeyFromHeader(HttpHeaders httpHeaders) {
			LOGGER.debug("[IdentitaDigitaleManager::getKeyFromHeader] BEGIN");
			MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
			Iterator<Map.Entry<String, List<String>>> s = headers.entrySet().iterator();
			String marker = null;
			while (s.hasNext()) {
				Map.Entry<String, List<String>> obj = s.next();
				String key = obj.getKey();
				if (key.equalsIgnoreCase(Constants.AUTH_ID_MARKER)) {
					 marker = obj.getValue().get(0);
				}
			}
		     LOGGER.debug("[IdentitaDigitaleManager::getKeyFromHeader] END");
			return  marker;
		}
	 
	 public static String normalizeToken(String token) {
			return token;
		}

}
