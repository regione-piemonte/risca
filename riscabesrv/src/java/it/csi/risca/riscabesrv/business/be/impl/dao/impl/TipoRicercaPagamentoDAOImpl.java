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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoRicercaPagamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.PagamentoDAOImpl.PagNonPropriRowMapper;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.PagamentoDAOImpl.PagamentoExtendedRowMapper;
import it.csi.risca.riscabesrv.dto.PagNonPropriExtendedDTO;
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RicercaPagamentoDTO;
import it.csi.risca.riscabesrv.dto.TipoRicercaPagamentoDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class TipoRicercaPagamentoDAOImpl extends RiscaBeSrvGenericDAO<TipoRicercaPagamentoDTO> implements TipoRicercaPagamentoDAO {

	private final String className = this.getClass().getSimpleName();
	
	public static final String QUERY_SELECT_ALL_TIPO_RICERCA_PAGAMENTO = "select * from risca_d_tipo_ricerca_pagamento";
	
	public static final String QUERY_SELECT_RICERCA_PAGAMENTO ="SELECT * FROM risca_t_pagamento p ,"
			+ " risca_d_tipo_modalita_pag mod , " 
			+ " risca_d_ambito rda";
	
	public static final String DA_CONFERMARE = QUERY_SELECT_RICERCA_PAGAMENTO+ " WHERE p.id_tipo_modalita_pag = mod.id_tipo_modalita_pag "
			+" AND p.id_ambito = rda.id_ambito "
			+" AND imp_da_assegnare > 0 ";
	
	public static final String DA_RIMBORSARE = QUERY_SELECT_RICERCA_PAGAMENTO+" ,risca_r_pag_non_propri np"
			+ " WHERE p.id_tipo_modalita_pag = mod.id_tipo_modalita_pag"
			+" AND p.id_ambito = rda.id_ambito "
			+ " AND np.ID_PAGAMENTO = p.ID_PAGAMENTO"
			+ " AND np.ID_TIPO_IMP_NON_PROPRI  = 3 ";
	
	public static final String DA_VISIONARE = " SELECT * FROM risca_t_pagamento p "
			+ " INNER JOIN risca_d_tipo_modalita_pag mod ON  p.id_tipo_modalita_pag = mod.id_tipo_modalita_pag "
			+ " INNER JOIN risca_d_ambito mod ON  p.id_ambito = rda.id_ambito "
			+ " left join (SELECT dpa.id_pagamentoFROM risca_r_dettaglio_pag dpa) as det on p.id_pagamento = det.id_pagamento"
			+ " left join (SELECT pnp.id_pagamento FROM risca_r_pag_non_propri pnp ) as pno on p.id_pagamento = pno.id_pagamento"
			+ " where det.id_pagamento is null"
			+ " and pno.id_pagamento is null";
	
	public static final String NON_DI_COMPETENZA =QUERY_SELECT_RICERCA_PAGAMENTO+" ,risca_r_pag_non_propri np"
			+ " WHERE p.id_tipo_modalita_pag = mod.id_tipo_modalita_pag"
			+" AND p.id_ambito = rda.id_ambito "
			+ " AND np.ID_PAGAMENTO          = p.ID_PAGAMENTO"
			+ " AND np.ID_TIPO_IMP_NON_PROPRI  = 2";
	
	public static final String NON_IDENTIFICATO = QUERY_SELECT_RICERCA_PAGAMENTO+"  ,risca_r_pag_non_propri np"
			+ " WHERE p.id_tipo_modalita_pag = mod.id_tipo_modalita_pag"
			+" AND p.id_ambito = rda.id_ambito "
			+ " AND np.ID_PAGAMENTO          = p.ID_PAGAMENTO"
			+ " AND np.ID_TIPO_IMP_NON_PROPRI  = 1 ";
	
	public static final String RIMBORSATO =QUERY_SELECT_RICERCA_PAGAMENTO+" WHERE p.id_tipo_modalita_pag = mod.id_tipo_modalita_pag"
			+" AND p.id_ambito = rda.id_ambito "
			+ " and coalesce(flg_rimborsato, 0) <> 0 ";
	
	public static final String COLLEGATO = QUERY_SELECT_RICERCA_PAGAMENTO+" WHERE p.id_tipo_modalita_pag = mod.id_tipo_modalita_pag"
			+" AND p.id_ambito = rda.id_ambito "
			+ " AND EXISTS"
			+ " (SELECT risca_r_dettaglio_pag.id_pagamento"
			+ " FROM risca_r_dettaglio_pag"
			+ " WHERE id_pagamento=p.id_pagamento)";
	
	public static final String ID_TIPO_MOD_PAG ="p.id_tipo_modalita_pag = :idTipoModPag";
	public static final String DATA_OP_VAL_DA ="p.data_op_val >= :dataOpValDa";
	public static final String DATA_OP_VAL_A ="p.data_op_val <= :dataOpValA";
	public static final String QUINTO_CAMPO ="p.quinto_campo = :quintoCampo";
	public static final String CODICE_AVVISO ="p.codice_avviso = :codiceAvviso";
	public static final String CRO ="p.cro = :cro";
	public static final String IMPORTO_DA ="p.importo_versato >= :importoDa";
	public static final String IMPORTO_A ="p.importo_versato <= :importoA";
	public static final String SOGGETTO_VERSAMENTO ="p.soggetto_versamento ilike '%'||:soggettoVersamento||'%'";
	public static final String ORDER_BY_DATA= " ORDER BY data_op_val DESC";
	
	@Override
	public List<TipoRicercaPagamentoDTO> loadAllTipiRicercaPagamenti() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<TipoRicercaPagamentoDTO> res = new ArrayList<>();
		try {
			res = findSimpleDTOListByQuery(className, methodName, QUERY_SELECT_ALL_TIPO_RICERCA_PAGAMENTO, null);
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
			throw  e;
		} finally {
	        LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return res;
	}
	
	@Override
	public List<PagamentoExtendedDTO> ricercaPagamenti(RicercaPagamentoDTO ricercaPagamentoDTO, Integer offset,
	        Integer limit, String sort) throws Exception {
	    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	    try {
	        String finalQuery = buildQuery(ricercaPagamentoDTO, sort);
	        Map<String, Object> queryParams = buildQueryParams(ricercaPagamentoDTO);
	        SqlParameterSource params = new MapSqlParameterSource(queryParams);
	        List<PagamentoExtendedDTO> resultRicercaPagamenti = executeQuery(finalQuery, params, offset, limit);
	        
	    	if(resultRicercaPagamenti != null && resultRicercaPagamenti.size() > 0) {
				for (PagamentoExtendedDTO pagamento : resultRicercaPagamenti) {
					
					queryParams.put("idPagamento", pagamento.getIdPagamento());
					params = getParameterValue(queryParams);
					
					List<PagNonPropriExtendedDTO> pagNonPropriList  = template.query(getQuery(PagamentoDAOImpl.QUERY_PAG_NON_PROPRI_LIST_BY_PAGAMENTO, null, null), params,
							new PagNonPropriRowMapper());
					
					if(pagNonPropriList != null && pagNonPropriList.size() > 0)
						pagamento.setPagNonPropri(pagNonPropriList);
				}

			}
	        
	        
	        LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        return resultRicercaPagamenti;
	    } catch (EmptyResultDataAccessException e) {
	        LOGGER.debug(getClassFunctionDebugString(className, methodName, " Data not found"));
	        return Collections.emptyList();
	    } catch (SQLException e) {
	        LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
	        throw new Exception(e);
	    } catch (Exception e) {
	        LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
	        throw new Exception(e);
	    }
	}
	
	@Override
	public Integer countPagamenti(RicercaPagamentoDTO ricercaPagamentoDTO) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	    try {
	        String buildQueryPagamento = buildQuery(ricercaPagamentoDTO, null);
	        Map<String, Object> queryParams = buildQueryParams(ricercaPagamentoDTO);
	        SqlParameterSource params = new MapSqlParameterSource(queryParams);
	        String finalQuery = "SELECT COUNT(*) FROM (" + buildQueryPagamento.toString() + ") as tot_pagamenti";
	        return template.queryForObject(finalQuery, params, Integer.class);
	    } catch (Exception e) {
	        LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
	        throw e;
	    } finally {
	        LOGGER.debug(getClassFunctionEndInfo(className, methodName));

	    }
	}
	
	private String buildQuery(RicercaPagamentoDTO ricercaPagamentoDTO, String sort) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	    StringBuilder queryBuilder = new StringBuilder();
	    if (ricercaPagamentoDTO.getStatoPagamento() != null) {
	        Map<String, String> tipoRicercaClauses = createStatoPagamentoClauses();
	        if (tipoRicercaClauses.containsKey(ricercaPagamentoDTO.getStatoPagamento().getDesTipoRicercaPagamento())) {
	            queryBuilder.append(tipoRicercaClauses.get(ricercaPagamentoDTO.getStatoPagamento().getDesTipoRicercaPagamento()));
	        }
	    } else {
	        queryBuilder.append(QUERY_SELECT_RICERCA_PAGAMENTO).append(" WHERE p.id_tipo_modalita_pag = mod.id_tipo_modalita_pag ").append(" AND p.id_ambito = rda.id_ambito ");
	    }

	    if (ricercaPagamentoDTO.getTipoModalitaPag() != null) {
	        queryBuilder.append(" AND ");
	        queryBuilder.append(ID_TIPO_MOD_PAG);
	    }

	    if (ricercaPagamentoDTO.getDataOpDa() != null  && StringUtils.isNotBlank(ricercaPagamentoDTO.getDataOpDa())) {
	        queryBuilder.append(" AND ");
	        queryBuilder.append(DATA_OP_VAL_DA);
	    }

	    if (ricercaPagamentoDTO.getDataOpA() != null && StringUtils.isNotBlank(ricercaPagamentoDTO.getDataOpA())  ) {
	        queryBuilder.append(" AND ");
	        queryBuilder.append(DATA_OP_VAL_A);
	    }

	    if (ricercaPagamentoDTO.getQuintoCampo() != null  && StringUtils.isNotBlank(ricercaPagamentoDTO.getQuintoCampo())) {
	        queryBuilder.append(" AND ");
	        queryBuilder.append(QUINTO_CAMPO);
	    }
	    if (ricercaPagamentoDTO.getCodiceAvviso() != null && StringUtils.isNotBlank(ricercaPagamentoDTO.getCodiceAvviso())  ) {
	        queryBuilder.append(" AND ");
	        queryBuilder.append(CODICE_AVVISO);
	    }
	    if (ricercaPagamentoDTO.getCro() != null && StringUtils.isNotBlank(ricercaPagamentoDTO.getCro())  ) {
	        queryBuilder.append(" AND ");
	        queryBuilder.append(CRO);
	    }
	    if (ricercaPagamentoDTO.getImportoDa() != null) {
	        queryBuilder.append(" AND ");
	        queryBuilder.append(IMPORTO_DA);
	    }
	    if (ricercaPagamentoDTO.getImportoA() != null) {
	        queryBuilder.append(" AND ");
	        queryBuilder.append(IMPORTO_A);
	    }
	    if (ricercaPagamentoDTO.getSoggettoVersamento() != null  &&  StringUtils.isNotBlank(ricercaPagamentoDTO.getSoggettoVersamento())) {
	        queryBuilder.append(" AND ");
	        queryBuilder.append(SOGGETTO_VERSAMENTO);
	    }

	    if (sort != null && StringUtils.isNotBlank(sort)) {
	        String dynamicOrderByCondition = mapSortConCampiDB(sort);
	        if (dynamicOrderByCondition != null) {
	            dynamicOrderByCondition = dynamicOrderByCondition.concat(",").concat(ORDER_BY_DATA.replace("ORDER BY", ""));
	        }
	        queryBuilder.append(ORDER_BY_DATA);
	    } else {
	        queryBuilder.append(ORDER_BY_DATA);
	    }
	    LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	    return queryBuilder.toString();
	}

	private Map<String, Object> buildQueryParams(RicercaPagamentoDTO ricercaPagamentoDTO) throws ParseException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	    Map<String, Object> queryParams = new HashMap<>();
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    if (ricercaPagamentoDTO.getTipoModalitaPag() != null) {
	        queryParams.put("idTipoModPag", ricercaPagamentoDTO.getTipoModalitaPag().getIdTipoModalitaPag());
	    }

	    if ( ricercaPagamentoDTO.getDataOpDa() != null && StringUtils.isNotBlank(ricercaPagamentoDTO.getDataOpDa()) ) {
	        queryParams.put("dataOpValDa", formatter.parse(ricercaPagamentoDTO.getDataOpDa()));
	    }

	    if (ricercaPagamentoDTO.getDataOpA() != null &&  StringUtils.isNotBlank(ricercaPagamentoDTO.getDataOpA()) ) {
	        queryParams.put("dataOpValA", formatter.parse(ricercaPagamentoDTO.getDataOpA()));
	    }

	    if (ricercaPagamentoDTO.getQuintoCampo() != null &&  StringUtils.isNotBlank(ricercaPagamentoDTO.getQuintoCampo()) ) {
	        queryParams.put("quintoCampo", ricercaPagamentoDTO.getQuintoCampo());
	    }

	    if (ricercaPagamentoDTO.getCodiceAvviso() != null &&  StringUtils.isNotBlank(ricercaPagamentoDTO.getCodiceAvviso()) ) {
	        queryParams.put("codiceAvviso", ricercaPagamentoDTO.getCodiceAvviso());
	    }

	    if (ricercaPagamentoDTO.getCro() != null &&  StringUtils.isNotBlank(ricercaPagamentoDTO.getCro()) ) {
	        queryParams.put("cro", ricercaPagamentoDTO.getCro());
	    }

	    if (ricercaPagamentoDTO.getImportoDa() != null) {
	        queryParams.put("importoDa", ricercaPagamentoDTO.getImportoDa());
	    }

	    if (ricercaPagamentoDTO.getImportoA() != null) {
	        queryParams.put("importoA", ricercaPagamentoDTO.getImportoA());
	    }

	    if (StringUtils.isNotBlank(ricercaPagamentoDTO.getSoggettoVersamento()) && ricercaPagamentoDTO.getSoggettoVersamento() != null) {
	        queryParams.put("soggettoVersamento", ricercaPagamentoDTO.getSoggettoVersamento());
	    }

	    LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	    return queryParams;
	}


	private List<PagamentoExtendedDTO> executeQuery(String finalQuery, SqlParameterSource params, Integer offset, Integer limit) throws DataAccessException, SQLException {
	    String query = getQuery(finalQuery, offset != null ? offset.toString() : null, limit != null ? limit.toString() : null);
	    return template.query(query, params, getPagamentoExtendedRowMapper());
	}


	private String mapSortConCampiDB(String sort) {
		// forse da fare
		return null;
	}

	private Map<String, String> createStatoPagamentoClauses() {
	    Map<String, String> statoPagamentoClauses = new HashMap<>();
		statoPagamentoClauses.put(Constants.DA_CONFERMARE, DA_CONFERMARE);
		statoPagamentoClauses.put(Constants.DA_RIMBORSARE, DA_RIMBORSARE);
		statoPagamentoClauses.put(Constants.DA_VISIONARE, DA_VISIONARE);
		statoPagamentoClauses.put(Constants.NON_DI_COMPETENZA, NON_DI_COMPETENZA);
		statoPagamentoClauses.put(Constants.NON_IDENTIFICATO, NON_IDENTIFICATO);
		statoPagamentoClauses.put(Constants.RIMBORSATO, RIMBORSATO);
		statoPagamentoClauses.put(Constants.COLLEGATO, COLLEGATO);
	    return statoPagamentoClauses;
	}

	
	
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public RowMapper<PagamentoExtendedDTO> getPagamentoExtendedRowMapper() throws SQLException {
		return new PagamentoExtendedRowMapper();
	}
	@Override
	public RowMapper<TipoRicercaPagamentoDTO> getRowMapper() throws SQLException {
		return new TipoRicercaPagamentoRowMapper();
	}
	/**
	 * The type TipoRicercaPagamentoRowMapper.
	 */
	public static class TipoRicercaPagamentoRowMapper implements RowMapper<TipoRicercaPagamentoDTO> {
		
		/**
		 * Instantiates a new TipoRicercaPagamentoRowMapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public TipoRicercaPagamentoRowMapper() throws SQLException {
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
		public TipoRicercaPagamentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			TipoRicercaPagamentoDTO bean = new TipoRicercaPagamentoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, TipoRicercaPagamentoDTO bean) throws SQLException {
			bean.setIdTipoRicercaPagamento(rs.getLong("id_tipo_ricerca_pagamento"));
			bean.setCodTipoRicercaPagamento(rs.getString("cod_tipo_ricerca_pagamento"));
			bean.setDesTipoRicercaPagamento(rs.getString("des_tipo_ricerca_pagamento"));

		}

	}
	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	


}
