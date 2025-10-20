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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatiElaborazioneDAO;
import it.csi.risca.riscabesrv.dto.StatoElaborazioneDTO;

public class StatiElaborazioneDAOImpl extends RiscaBeSrvGenericDAO<StatoElaborazioneDTO> implements StatiElaborazioneDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	private static final String ID_AMBITO = "idAmbito";
	
	private static final String ID_FUNZIONALITA = "idFunzionalita";
	
	private static final String  QUERY_STATO_ELABORAZIONE="	select distinct rdse.* from risca_r_funzionalita_stato rrfs   "
			+ "	inner join risca_d_stato_elabora rdse on   rrfs.id_stato_elabora = rdse.id_stato_elabora   "
			+ "	inner join risca_d_funzionalita rdf on rrfs.id_funzionalita = rdf.id_funzionalita  "
			+ "	left join risca_d_tipo_elabora rdte on rrfs.id_funzionalita = rdte.id_funzionalita  "
			+ "	inner join risca_d_ambito rda on rdte.id_ambito = rda.id_ambito ";
	
	private static final String  QUERY_STATO_ELABORAZIONE_BY_ID_AMBITO_AND_ID_FUNZIONALITA = QUERY_STATO_ELABORAZIONE+
			"where  rdte.id_ambito = :idAmbito and  rrfs.id_funzionalita  = :idFunzionalita";
	
	
	@Override
	public List<StatoElaborazioneDTO> loadStatiElaborazioneByIdAmbitoAndIdFunzionalita(Long idAmbito,
			Long idFunzionalita) {
        LOGGER.debug("[StatiElaborazioneDAOImpl::loadStatiElaborazioneByIdAmbitoAndIdFunzionalita] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        Map<String, Object> map = new HashMap<>();

        map.put(ID_AMBITO, idAmbito);
        map.put(ID_FUNZIONALITA, idFunzionalita);
        try {
        	LOGGER.debug("[StatiElaborazioneDAOImpl::loadStatiElaborazioneByIdAmbitoAndIdFunzionalita] BEGIN");
        	return findSimpleDTOListByQuery(CLASSNAME, methodName, QUERY_STATO_ELABORAZIONE_BY_ID_AMBITO_AND_ID_FUNZIONALITA, map);
			
        } catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[StatiElaborazioneDAOImpl::loadStatiElaborazioneByIdAmbitoAndIdFunzionalita] No record found in database for Tipo Elabora ", e);
		  return Collections.emptyList();
		}  catch (DataAccessException e) {
            LOGGER.error("[StatiElaborazioneDAOImpl::loadStatiElaborazioneByIdAmbitoAndIdFunzionalita] Errore nell'accesso ai dati", e);
            throw e;
        } 
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<StatoElaborazioneDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new StatoElaborazioneRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
     * The type stati Elaborazione row mapper.
     */
    public static class StatoElaborazioneRowMapper implements RowMapper<StatoElaborazioneDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public StatoElaborazioneRowMapper() throws SQLException {
            // Instantiate class
        }

        /**
         * Implementations must implement this method to map each row of data
         * in the ResultSet. This method should not call {@code next()} on
         * the ResultSet; it is only supposed to map values of the current row.
         *
         * @param rs     the ResultSet to map (pre-initialized for the current row)
         * @param rowNum the number of the current row
         * @return the result object for the current row (may be {@code null})
         * @throws SQLException if a SQLException is encountered getting
         *                      column values (that is, there's no need to catch SQLException)
         */
        @Override
        public StatoElaborazioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	StatoElaborazioneDTO bean = new StatoElaborazioneDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, StatoElaborazioneDTO bean) throws SQLException {
        	
            bean.setIdStatoElabora(rs.getLong("id_stato_elabora"));
            bean.setCodStatoElabora(rs.getString("cod_stato_elabora"));
            bean.setDesStatoElabora(rs.getString("des_stato_elabora"));
        }
    }

}
