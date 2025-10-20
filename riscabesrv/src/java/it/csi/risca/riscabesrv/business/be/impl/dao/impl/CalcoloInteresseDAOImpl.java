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
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitoInteresseDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.CalcoloInteresseDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AmbitoInteresseDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * Calcolo Interesse DAO Impl
 *
 * @author CSI PIEMONTE
 */
public class CalcoloInteresseDAOImpl extends RiscaBeSrvGenericDAO<BigDecimal> implements CalcoloInteresseDAO {

	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	private static final String TIPO_INTERESSE_LEGALI = "L";
	private static final String TIPO_INTERESSE_DI_MORA = "M";
	@Autowired
	private AmbitoInteresseDAO ambitoInteresseDAO;
	@Autowired
	private MessaggiDAO messaggiDAO;

	@SuppressWarnings("deprecation")
	@Override
	public BigDecimal calcoloInteressi(Long idAmbito, BigDecimal importo, String dataScadenza, String dataVersamento)
			throws BusinessException, DataAccessException, SQLException, SystemException {
		LOGGER.debug("[CalcoloInteresseDAOImpl::calcoloInteressi] BEGIN");
		if (importo == null || dataScadenza == null || dataVersamento == null)
			throw new BusinessException(400, "E001", "Attenzione: compila tutti i campi obbligatori");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dataIni = null;
		Date dataFin = null;
		try {
			dataIni = formatter.parse(dataScadenza);
			// Per il calcolo dei giorni imposto le ore 12 sulla data inizio come faceva
			// GERICA altrimenti in alcuni casi il calcolo dei giorni viene impreciso
			dataIni = Utils.setHour(dataIni, 12, 0);
			dataFin = formatter.parse(dataVersamento);

		} catch (ParseException e1) {
			LOGGER.error(" [CalcoloInteresseDAOImpl::calcoloInteressi] Errore nel parse ai dati", e1);
			return null;
		}
		
		long diffGDataScadenzaDataVersamento = Utils.diffGiorniTraDueDate(dataIni, dataFin);
		long totGiorniDiMora = 0;
		BigDecimal importoCalcoloInteresse = BigDecimal.ZERO;
		try {
			messaggiDAO.loadMessaggiByCodMessaggio("E088");
			if (dataIni.after(dataFin) || (dataIni.compareTo(dataFin) == 0)) {
				return importoCalcoloInteresse;
			}

			List<AmbitoInteresseDTO> listByDataFine = ambitoInteresseDAO.getAmbitoInteresseByDateFineAndTipo(idAmbito,
					TIPO_INTERESSE_LEGALI, dataFin);
			if (listByDataFine.isEmpty()) {
				throw new BusinessException(400, "E088",
						"Attenzione: non sono presenti tutti gli intervalli di date per il calcolo delle percentuali!");
			}
			List<AmbitoInteresseDTO> listByDataInizio = ambitoInteresseDAO
					.getAmbitoInteresseByDateInizioAndTipo(idAmbito, TIPO_INTERESSE_LEGALI, dataIni);
			if (listByDataInizio.isEmpty()) {
				throw new BusinessException(400, "E088",
						"Attenzione: non sono presenti tutti gli intervalli di date per il calcolo delle percentuali!");
			}

			List<AmbitoInteresseDTO> listGL = ambitoInteresseDAO.getAmbitoInteresseByDateAndTipo(idAmbito,
					TIPO_INTERESSE_LEGALI, dataIni, dataFin);
			if (listGL.isEmpty()) {
				throw new BusinessException(400, "E088",
						"Attenzione: non sono presenti tutti gli intervalli di date per il calcolo delle percentuali!");
			}
			long giorniLegaliAmbitoInteresse = listGL.get(0).getGiorniLegali();
			Date dataFinePerGL = Utils.addDaysInDate(dataIni, listGL.get(0).getGiorniLegali());
			listGL = listGL.stream().filter(l -> l.getDataInizio().before(dataFinePerGL)).collect(Collectors.toList());

			listGL = dividiPeriodoInteressePerAnni(dataIni, dataFin, listGL);

			for (int i = 0; i < listGL.size(); i++) {
				long daysInYear = Utils.daysInYear(listGL.get(i).getDataFine().getYear());
				verifyIntervalloDelleDate(listGL, i);

				long diffGiorniAmbitoInteresseL = 0l;
				
				if(listGL.size()>1) {	
					if (i == 0) {
						diffGiorniAmbitoInteresseL = Utils.diffGiorniTraDueDate(dataIni, listGL.get(i).getDataFine());
					} else if (i == (listGL.size() - 1)) {
	
						diffGiorniAmbitoInteresseL = Utils.diffGiorniTraDueDate(listGL.get(i).getDataInizio(), dataFin) - 1;
					} else {
						diffGiorniAmbitoInteresseL = Utils.diffGiorniTraDueDate(listGL.get(i).getDataInizio(),
								listGL.get(i).getDataFine());
					}
				} else {
					if(diffGDataScadenzaDataVersamento<=listGL.get(i).getGiorniLegali()) {
						diffGiorniAmbitoInteresseL = diffGDataScadenzaDataVersamento;
					} else {
						diffGiorniAmbitoInteresseL = listGL.get(i).getGiorniLegali();
					}
				}

				if (diffGiorniAmbitoInteresseL <= giorniLegaliAmbitoInteresse) {
					importoCalcoloInteresse = FormulaCalcoloInteressi(importo, importoCalcoloInteresse,
							listGL.get(i).getPercentuale(), daysInYear, diffGiorniAmbitoInteresseL);
					giorniLegaliAmbitoInteresse = giorniLegaliAmbitoInteresse - diffGiorniAmbitoInteresseL;

				} else {
					importoCalcoloInteresse = FormulaCalcoloInteressi(importo, importoCalcoloInteresse,
							listGL.get(i).getPercentuale(), daysInYear, giorniLegaliAmbitoInteresse);
					break;
				}
				if (giorniLegaliAmbitoInteresse == 0) {
					break;
				}
			}
			totGiorniDiMora = diffGDataScadenzaDataVersamento - listGL.get(0).getGiorniLegali();
			if (totGiorniDiMora > 0) {
				LOGGER.debug(
						"[CalcoloInteresseDAOImpl::FormulaCalcoloInteressi] modifica data inizio aggiungendo i giorni legali");
				Date dataInizioAmbitoInteresseM = Utils.addDaysInDate(dataIni, listGL.get(0).getGiorniLegali());
				LOGGER.debug("[CalcoloInteresseDAOImpl::FormulaCalcoloInteressi] get listGM");
				List<AmbitoInteresseDTO> listGM = ambitoInteresseDAO.getAmbitoInteresseByDateAndTipo(idAmbito,
						TIPO_INTERESSE_DI_MORA, dataInizioAmbitoInteresseM, dataFin);
				if (listGM.isEmpty()) {
					throw new BusinessException(400, "E088",
							"Attenzione: non sono presenti tutti gli intervalli di date per il calcolo delle percentuali!");
				}

				listGM = dividiPeriodoInteressePerAnni(dataInizioAmbitoInteresseM, dataFin, listGM);

				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				for (int i = 0; i < listGM.size(); i++) {
					long daysInYear = Utils.daysInYear(listGM.get(i).getDataFine().getYear());
					verifyIntervalloDelleDate(listGM, i);
					if (i == 0) {
						Date dataFine = dataFin.after(listGM.get(i).getDataFine()) ? listGM.get(i).getDataFine()
								: dataFin;
						long diffGiorniAmbitoInteresseM = Utils.diffGiorniTraDueDate(dataInizioAmbitoInteresseM,
								dataFine);
						LOGGER.debug("[CalcoloInteresseDAOImpl::calcoloInteressi] 1 - "
								+ df.format(dataInizioAmbitoInteresseM) + " - " + df.format(dataFine) + ": "
								+ diffGiorniAmbitoInteresseM);
						importoCalcoloInteresse = FormulaCalcoloInteressi(importo, importoCalcoloInteresse,
								listGM.get(i).getPercentuale(), daysInYear, diffGiorniAmbitoInteresseM);
					} else if (i == (listGM.size() - 1)) {
						long diffGiorniAmbitoInteresseM = Utils.diffGiorniTraDueDate(listGM.get(i).getDataInizio(),
								dataFin);
						LOGGER.debug("[CalcoloInteresseDAOImpl::calcoloInteressi] 2 - "
								+ df.format(listGM.get(i).getDataInizio()) + " - " + df.format(dataFin) + ": "
								+ diffGiorniAmbitoInteresseM);
						importoCalcoloInteresse = FormulaCalcoloInteressi(importo, importoCalcoloInteresse,
								listGM.get(i).getPercentuale(), daysInYear, diffGiorniAmbitoInteresseM);
					} else {
						long diffGiorniAmbitoInteresseM = Utils.diffGiorniTraDueDate(listGM.get(i).getDataInizio(),
								listGM.get(i).getDataFine());
						LOGGER.debug("[CalcoloInteresseDAOImpl::calcoloInteressi] 3 - "
								+ df.format(listGM.get(i).getDataInizio()) + " - "
								+ df.format(listGM.get(i).getDataFine()) + ": " + diffGiorniAmbitoInteresseM);
						importoCalcoloInteresse = FormulaCalcoloInteressi(importo, importoCalcoloInteresse,
								listGM.get(i).getPercentuale(), daysInYear, diffGiorniAmbitoInteresseM);
					}

				}
			}

		} catch (DataAccessException e3) {
			LOGGER.error(" [CalcoloInteresseDAOImpl::getAmbitoInteresseByDateAndTipo] Errore nell'accesso ai dati", e3);
			throw e3;
		} catch (SQLException e4) {
			LOGGER.error(" [CalcoloInteresseDAOImpl::getAmbitoInteresseByDateAndTipo] Errore nella query ", e4);
			throw e4;
		}

		return importoCalcoloInteresse.setScale(2, RoundingMode.HALF_UP);

	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<BigDecimal> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	private void verifyIntervalloDelleDate(List<AmbitoInteresseDTO> list, int i) {
		LOGGER.debug("[CalcoloInteresseDAOImpl::verifyIntervalloDelleDate] BEGIN");
		Date dataFineAmbitoInteresse = Utils.addDaysInDate(list.get(i).getDataFine(), 1);
		if (1 < list.size() && i < (list.size() - 1)) {
			List<AmbitoInteresseDTO> listDataFine = list.stream()
					.filter(p -> p.getDataInizio().equals(dataFineAmbitoInteresse)).collect(Collectors.toList());
			if (listDataFine.isEmpty()) {
				throw new BusinessException(400, "E088",
						"Attenzione: non sono presenti tutti gli intervalli di date per il calcolo delle percentuali!");
			}
		}
		LOGGER.debug("[CalcoloInteresseDAOImpl::verifyIntervalloDelleDate] End");
	}

	private BigDecimal FormulaCalcoloInteressi(BigDecimal importo, BigDecimal importoCalcoloInteresse,
			Double percentuale, long daysInYear, long diffGiorniAmbitoInteresse) {
		LOGGER.debug("[CalcoloInteresseDAOImpl::FormulaCalcoloInteressi] BEGIN percentuale " + percentuale + " giorni "
				+ diffGiorniAmbitoInteresse);
		BigDecimal importoInteresse = importo.multiply(BigDecimal.valueOf(percentuale), MathContext.DECIMAL64)
				.divide(BigDecimal.valueOf(100), 20, RoundingMode.HALF_UP)
				.divide(BigDecimal.valueOf(daysInYear), 20, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(diffGiorniAmbitoInteresse), MathContext.DECIMAL64);

		importoCalcoloInteresse = importoCalcoloInteresse.add(importoInteresse);
		LOGGER.debug("[CalcoloInteresseDAOImpl::FormulaCalcoloInteressi] BEGIN importoInteresse :" + importoInteresse);
		LOGGER.debug("[CalcoloInteresseDAOImpl::FormulaCalcoloInteressi] BEGIN importoCalcoloInteresse :"
				+ importoCalcoloInteresse);
		return importoCalcoloInteresse;

	}

	private List<AmbitoInteresseDTO> dividiPeriodoInteressePerAnni(Date dataInizio, Date dataFine,
			List<AmbitoInteresseDTO> listGM) {
		List<AmbitoInteresseDTO> newListGM = new ArrayList<AmbitoInteresseDTO>();
		for (int i = 0; i < listGM.size(); i++) {
			newListGM.addAll(dividiPeriodoInteresse(listGM.get(i)));
		}
		// Devo togliere gli eventuali intervalli iniziali che vengono prima di
		// dataInizioAmbitoInteresseM
		listGM = newListGM.stream().filter(gm -> gm.getDataFine().after(dataInizio)).collect(Collectors.toList());

		// Devo togliere gli eventuali intervalli finali che vengono dopo la dataFine
		listGM = listGM.stream().filter(gm -> gm.getDataInizio().before(dataFine)).collect(Collectors.toList());

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		for (AmbitoInteresseDTO ai : listGM) {
			LOGGER.debug(df.format(ai.getDataInizio()) + " - " + df.format(ai.getDataFine()));
		}

		return listGM;
	}

	private List<AmbitoInteresseDTO> dividiPeriodoInteresse(AmbitoInteresseDTO aiDto) {
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(aiDto.getDataInizio());

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(aiDto.getDataFine());

		int startYear = startCalendar.get(Calendar.YEAR);
		int endYear = endCalendar.get(Calendar.YEAR);

		Date start = aiDto.getDataInizio();
		Date end = aiDto.getDataFine();
		List<AmbitoInteresseDTO> ranges = new ArrayList<AmbitoInteresseDTO>();

		for (int year = startYear; year <= endYear; year++) {

			Date yearStartDate = Utils.getStartDateForYear(year, startCalendar);
			Date yearEndDate = Utils.getEndDateForYear(year, endCalendar);

			AmbitoInteresseDTO dto = new AmbitoInteresseDTO();
			dto.setPercentuale(aiDto.getPercentuale());
			dto.setGiorniLegali(aiDto.getGiorniLegali());
			if (start.after(yearStartDate) && end.before(yearEndDate)) {
				dto.setDataInizio(start);
				dto.setDataFine(end);
			} else if (start.after(yearStartDate)) {
				dto.setDataInizio(start);
				dto.setDataFine(yearEndDate);
			} else if (end.before(yearEndDate)) {
				dto.setDataInizio(yearStartDate);
				dto.setDataFine(end);
			} else {
				dto.setDataInizio(yearStartDate);
				dto.setDataFine(yearEndDate);
			}
			ranges.add(dto);
		}

		return ranges;
	}
}
