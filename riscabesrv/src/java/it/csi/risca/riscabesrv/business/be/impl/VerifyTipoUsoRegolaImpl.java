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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import it.csi.risca.riscabesrv.business.be.VerifyTipoUsoRegola;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoRegolaDAO;
import it.csi.risca.riscabesrv.dto.JsonRangeDTO;
import it.csi.risca.riscabesrv.dto.JsonRegolaDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

@Component 
public class VerifyTipoUsoRegolaImpl extends BaseApiServiceImpl implements VerifyTipoUsoRegola {

	private final String className = this.getClass().getSimpleName();


	private static final String  REGEX_7_INTERI_2_DECIMAL = "^\\d{1,7}(\\.\\d{1,2})?$";
    private static final String REGEX_5_INTERI_4_DECIMAL = "^\\d{1,5}(\\.\\d{1,4})?$";

	
	@Autowired
	private TipoUsoRegolaDAO tipoUsoRegolaDAO;

	@Autowired
	private MessaggiDAO messaggiDAO;

	@Override
	public void checkTipoUsoRegola(TipoUsoRegolaExtendedDTO tipoUsoRegolaNew) throws BusinessException, Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			TipoUsoRegolaExtendedDTO tipoUsoRegolaOld = tipoUsoRegolaDAO
					.loadTipoUsoRegola(tipoUsoRegolaNew.getIdTipoUsoRegola());
			
			
			if (tipoUsoRegolaOld != null) {
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
						false);
				mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
				JsonRegolaDTO jsonRegola = mapper.readValue(tipoUsoRegolaOld.getJsonRegola(), JsonRegolaDTO.class);
				tipoUsoRegolaOld.setJsonRegolaDTO(jsonRegola);
			}
			
//			primo controllo:	Canone_unitario o canone_percentuale.
			BigDecimal canoneUnitario = tipoUsoRegolaNew.getJsonRegolaDTO().getCanoneUnitario();
			BigDecimal canonePercentuale = tipoUsoRegolaNew.getJsonRegolaDTO().getCanonePercentuale();
			
			if(canoneUnitario != null) {
				if(!verificaCanone(canoneUnitario, REGEX_7_INTERI_2_DECIMAL)) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E109);
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
							Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}
			}
			if(canonePercentuale != null) {
				if(!verificaCanone(canonePercentuale, REGEX_7_INTERI_2_DECIMAL)) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E109);
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
							Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}
			}
			
			if ((canoneUnitario != null && canonePercentuale != null)
					|| (canoneUnitario == null && canonePercentuale == null)) {
				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E103);
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
						Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
			} else {
				if (canoneUnitario != null && tipoUsoRegolaOld.getJsonRegolaDTO().getCanoneUnitario() == null) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E103);
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
							Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}
				if (canonePercentuale != null && tipoUsoRegolaOld.getJsonRegolaDTO().getCanonePercentuale() == null) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E103);
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
							Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}
				
			}
			BigDecimal canoneMinimo = tipoUsoRegolaNew.getJsonRegolaDTO().getCanoneMinimo();
//			Terzo controllo:	canoneMinimo , soglia , ranges
			if(canoneMinimo != null) {
				if(!verificaCanone(canoneMinimo, REGEX_7_INTERI_2_DECIMAL)) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E109);
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
							Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}
			}
//			secondo controllo:	canoneMinimo , soglia , ranges
			BigDecimal soglia = tipoUsoRegolaNew.getJsonRegolaDTO().getSoglia();
			List<JsonRangeDTO> ranges = Utils.isNotEmpty(tipoUsoRegolaNew.getJsonRegolaDTO().getJsonRanges() ) ?
					tipoUsoRegolaNew.getJsonRegolaDTO().getJsonRanges()
									.stream().sorted(Comparator.comparing(JsonRangeDTO::getSogliaMin)) // crescente
									.collect(Collectors.toList()) 
					: null ;
			
			// Verifica se canone_unitario e presente
			boolean isCanoneUnitarioPresent = canoneUnitario != null;

			// Verifica se uno dei seguenti attributi e presente: canone_minimo, soglia o ranges
			boolean isAnyOtherAttributePresent = (canoneMinimo != null || soglia != null || Utils.isNotEmpty(ranges));

			// Controlla se canone_unitario e presente da solo o insieme a uno dei seguenti attributi: canone_minimo, soglia o ranges
			if (!isCanoneUnitarioPresent || 
					(isCanoneUnitarioPresent && !isAnyOtherAttributePresent && (canoneMinimo != null || soglia != null || Utils.isNotEmpty(ranges)))) {
			    MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E103);
			    throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
			            Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
			}
			else {
				// Ottieni i vecchi dati per confrontarli
				JsonRegolaDTO oldRegolaDTO = tipoUsoRegolaOld.getJsonRegolaDTO();

				// Controlli combinati tra i nuovi dati e i vecchi dati
				boolean canoneUnitarioInvalid = canoneUnitario != null && oldRegolaDTO.getCanoneUnitario() == null;
				boolean canoneMinimoInvalid = canoneMinimo != null && oldRegolaDTO.getCanoneMinimo() == null;
				boolean sogliaInvalid = soglia != null && oldRegolaDTO.getSoglia() == null;
				boolean rangesInvalid = Utils.isNotEmpty(ranges) && !Utils.isNotEmpty(oldRegolaDTO.getJsonRanges());

				// Lancia un'eccezione se uno dei controlli e valido
				if (canoneUnitarioInvalid || canoneMinimoInvalid || sogliaInvalid || rangesInvalid) {
					// Carica il messaggio di errore una volta per evitare ripetizioni
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E103);
					String erroreMessaggio = Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio());
				    throw new BusinessException(400, messaggiDTO.getCodMessaggio(), erroreMessaggio);
				}
			}
			
//			Quarto controllo:	4.	Soglia
			if(soglia != null) {
				BigDecimal canoneMinimoSogliaInf = tipoUsoRegolaNew.getJsonRegolaDTO().getCanoneMinimoSogliaInf();
				BigDecimal canoneMinimoSogliaSup = tipoUsoRegolaNew.getJsonRegolaDTO().getCanoneMinimoSogliaSup();
				if (canoneMinimoSogliaInf == null && canoneMinimoSogliaSup == null) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E001);
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
							Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}
				if(!verificaCanone(soglia, REGEX_5_INTERI_4_DECIMAL)) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E108);
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
							Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}
				if(!verificaCanone(canoneMinimoSogliaInf, REGEX_7_INTERI_2_DECIMAL)) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E109);
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
							Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}
				if(!verificaCanone(canoneMinimoSogliaSup, REGEX_7_INTERI_2_DECIMAL)) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E109);
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
							Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}
			}
//			Quinto controllo:	5.	ranges		
			if(Utils.isNotEmpty(ranges)) {
				if(ranges.size() < 2) {
					MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E104);
					throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
							Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
				}
					for (int i = 0; i < ranges.size(); i++) {
						
				    if (ranges.get(i).getCanoneMinimoRange() == null || (ranges.get(i).getSogliaMax() == null && ranges.get(i).getSogliaMin() == null)) {
						MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E001);
						throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
								Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
					}
				    
					if(!verificaCanone(ranges.get(i).getCanoneMinimoRange(), REGEX_7_INTERI_2_DECIMAL)) {
						MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E109);
						throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
								Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
					}
					if(!verificaCanone(ranges.get(i).getSogliaMax(), REGEX_5_INTERI_4_DECIMAL)) {
						MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E108);
						throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
								Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
					}
					if(!verificaCanone(ranges.get(i).getSogliaMin(), REGEX_5_INTERI_4_DECIMAL)) {
						MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E108);
						throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
								Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
					}
					
					
					BigDecimal sogliaMin = ranges.get(i).getSogliaMin();
					BigDecimal sogliaMax = ranges.get(i).getSogliaMax();
					if (sogliaMin != null && sogliaMax != null) {
					    if (sogliaMin.compareTo(sogliaMax) > 0 || 
					            (sogliaMin.compareTo(BigDecimal.ZERO) != 0 && sogliaMax.compareTo(BigDecimal.ZERO) != 0) &&
					            sogliaMin.compareTo(sogliaMax) == 0) {
					        // Caso di errore E114
					        MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E114);
					        throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
					                Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
					    }
					}
					
				}
				for (int i = 0; i < ranges.size() - 1; i++) {
					BigDecimal sogliaMin = ranges.get(i).getSogliaMin();
					BigDecimal sogliaMax = ranges.get(i).getSogliaMax();
					BigDecimal sogliaMinP = ranges.get(i + 1).getSogliaMin();
					BigDecimal sogliaMaxP = ranges.get(i + 1).getSogliaMax();

					if (sogliaMin != null && sogliaMax != null && sogliaMinP != null && sogliaMaxP != null) {
						// Verifica la sovrapposizione tra i due range
						if (sogliaMax.compareTo(sogliaMinP) >= 0 && sogliaMin.compareTo(sogliaMaxP) <= 0) {
							// Sovrapposizione trovata, gestisci il caso di errore
							MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E105);
							throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
									Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
						}
					}
					


					
				}

			}
		} catch (BusinessException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		} catch (Exception e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}


	@Override
	public void verificaAnno(Integer idAmbito, Integer anno) throws BusinessException, Exception {
		if(!String.valueOf(anno).matches("^\\d{4}$")) {
	        MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E106);
	        throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
	                Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
		}
		if(tipoUsoRegolaDAO.checkAnnoExistence(idAmbito, anno)) {
	        MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E112);
	        throw new BusinessException(400, messaggiDTO.getCodMessaggio(),
	                Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
		}
		
	}

	@Override
	public boolean verificaCanone(BigDecimal canone, String pattern) {
		if( canone != null) {
			String canoneString = canone.toString();
			return canoneString.matches(pattern);	
		}
		return true;

	}
}
