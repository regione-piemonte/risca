/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.dao.DataAccessException;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.AmbitoFonteExtendedDTO;
import it.csi.risca.riscabesrv.dto.AssegnaPagamentoDTO;
import it.csi.risca.riscabesrv.dto.AvvisoPagamentoDTO;
import it.csi.risca.riscabesrv.dto.DelegatoExtendedDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagListDTO;
import it.csi.risca.riscabesrv.dto.ElaboraDTO;
import it.csi.risca.riscabesrv.dto.File450DTO;
import it.csi.risca.riscabesrv.dto.FonteDTO;
import it.csi.risca.riscabesrv.dto.GruppoDelegaExtendedDTO;
import it.csi.risca.riscabesrv.dto.IuvDTO;
import it.csi.risca.riscabesrv.dto.LottoDTO;
import it.csi.risca.riscabesrv.dto.PagNonPropriExtendedDTO;
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.PagopaScompRichIuvDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.ReportResultDTO;
import it.csi.risca.riscabesrv.dto.RimborsoSdUtilizzatoDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.SoggettoDelegaExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoModalitaPagDTO;

public interface BusinessLogic 
{

	public RecapitiExtendedDTO readRecapitoByPk(Long idRecapito)throws DAOException, SystemException;

	public RecapitiExtendedDTO createRecapito(RecapitiExtendedDTO recapito)throws DAOException, SystemException, DatiInputErratiException;
	
	public RecapitiExtendedDTO updateRecapito(RecapitiExtendedDTO recapito,SoggettiExtendedDTO soggettiExtendedOLD, SoggettiExtendedDTO soggettiExtended, Long indModManuale)throws DAOException, SystemException, DatiInputErratiException;

	public AvvisoPagamentoDTO createAvvisoPagamento(AvvisoPagamentoDTO avvisoPagamento)throws DAOException, SystemException, DatiInputErratiException;

	public AvvisoPagamentoDTO readAvvisoPagamentoByNap(String nap)throws DAOException, SystemException;

	public RataSdDTO createRataSd(RataSdDTO rataSd)throws DAOException, SystemException, DatiInputErratiException;

	public TipoModalitaPagDTO readTipoModalitaPagByCodTipoModalitaPag(String codTipoModalitaPag)throws DAOException, SystemException;

	public Long getIdAmbitoByIdFonte(Long idFonte) throws DAOException;
	
	public FonteDTO verificaFruitoreBatch(String fruitore);

	public File450DTO createFile450(File450DTO file450)throws DAOException, SystemException, DatiInputErratiException;

	public IuvDTO createIuv(IuvDTO iuv)throws Exception;

	public LottoDTO createLotto(LottoDTO lotto)throws Exception;

	public PagopaScompRichIuvDTO createPagopaScompRichIuv(PagopaScompRichIuvDTO pagopaScompRichIuv)throws Exception;

	public RimborsoSdUtilizzatoDTO createRimborsoSdUtilizzato(RimborsoSdUtilizzatoDTO rimborsoSdUtilizzato)throws Exception;
	
	public List<RecapitiExtendedDTO> insertUpdateAllRecapiti(List<RecapitiExtendedDTO> recapito,SoggettiExtendedDTO soggettiExtendedOLD, SoggettiExtendedDTO soggettiExtended, Long indModManuale)throws DAOException, SystemException, DatiInputErratiException;

	public List<RecapitiExtendedDTO> readRecapitiByIdSoggetto(Long idSoggetto)throws DAOException;
	
	public void deleteRecapitiByIdSoggetto(long idSoggetto) throws DAOException;
	
	public void deleteRecapitoAlternativoByIdRecapito(long idRecapito) throws DAOException;
	
	public List<DettaglioPagDTO> saveDettaglioPagList(DettaglioPagListDTO DettaglioPagList,  String fruitore,HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception;
	
	public void assegnaPagamentiPost(String fruitore, AssegnaPagamentoDTO assegnaPagamento,Long idAmbito, HttpHeaders httpHeaders, HttpServletRequest httpRequest)  throws Exception;
	
	public void verifyAssegnaPagamenti(AssegnaPagamentoDTO assegnaPagamento)  throws BusinessException,  Exception;

	public List<TipoModalitaPagDTO> loadAllTipiModalitaPagamenti();

	public List<AvvisoPagamentoDTO> loadAvvisoPagamentoWorkingByIdSpedizione(Long idSpedizione);
	
	public void copyWorkingForAvvisoPagamento(AvvisoPagamentoDTO avvisoPagamento, Long idElabora, Long idSpedizione, String attore, boolean bollettazioneOrdinaria) throws DAOException;

	public List<StatoDebitorioExtendedDTO> loadAllStatoDebitorio(Integer offset, Integer limit, String sort, Boolean isNotturnoTurnOn)throws DAOException, DatiInputErratiException;

	public int aggiornaStatoContribuzione(List<StatoDebitorioExtendedDTO> listStatoDebitorio, Long idAmbito, String fruitore, Long idElabora)throws DAOException, DatiInputErratiException, SystemException, ParseException,DataAccessException, SQLException, Exception;

	public List<SoggettoDelegaExtendedDTO> loadSoggettiDelegaByIdDelegato(Long idDelegato)throws BusinessException,  Exception;

	public List<GruppoDelegaExtendedDTO> loadGruppiDelegaByIdDelegato(Long idDelegato) throws BusinessException,  Exception;

	public List<AmbitoFonteExtendedDTO> loadAmbitiFonteByCodFonte(String codFonte)throws BusinessException,  Exception;
	
//	public void validatorDTO(Object dto,String tableName) throws BusinessException,  Exception;

	
	<T> void validatorDTO(T dto, Class<T>  dtoJson, String jsonString) throws BusinessException, Exception;

	public List<LottoDTO> loadLottiByIdElabora(Long idElabora)throws SystemException,  DAOException, BusinessException;

	public DelegatoExtendedDTO createDelegato(DelegatoExtendedDTO delegato, String origCf)throws SystemException,  DAOException, BusinessException, Exception;

	public boolean isNotturnoTurnOn() throws SystemException,  DAOException, BusinessException, Exception;

	public void updatePagamentiNonPropri(List<PagNonPropriExtendedDTO> pagNonPropriList, PagamentoExtendedDTO pagamento, HttpHeaders httpHeaders, HttpServletRequest httpRequest, String fruitore, Identita identita) throws ValidationException, Exception;

	public ReportResultDTO creaRuoloRicercaMorosita(ElaboraDTO elabora, String tipoRicercaMorosita, Integer anno,
			Integer flgRest, Integer flgAnn, String lim, AmbitoDTO ambito, String attore,List<Long> listIdStatiDebitoriSelezionati)throws SystemException,  DAOException, BusinessException, Exception;

	public Integer deleteWorkingSoris()throws DAOException;

}
