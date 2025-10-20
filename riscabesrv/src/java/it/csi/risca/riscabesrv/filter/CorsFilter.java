/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;

import it.csi.risca.riscabesrv.util.Constants;


/**
 * CorsFilter class
 *
 * @author CSI PIEMONTE
 */
public class CorsFilter implements Filter {
    protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME + ".business");
    private static final String ENABLECORS_INIT_PARAM = "enablecors";
    private boolean enableCors = false;
    /**
     * doFilter
     *
     * @param servletRequest servletRequest
     * @param servletResponse servletResponse
     * @param chain chain
     * @throws IOException IOException
     * @throws ServletException ServletException
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
    	if (enableCors) {
            HttpServletResponse res = (HttpServletResponse) servletResponse;
            res.setHeader("Access-Control-Allow-Origin", "*");
            res.setHeader("Content-Security-Policy", "default-src 'self'");
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        }
        chain.doFilter(servletRequest, servletResponse);
    }


    /**
     * init
     *
     * @param filterConfig filterConfig
     * @throws ServletException ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String sEnableCors = filterConfig.getInitParameter(ENABLECORS_INIT_PARAM);
        enableCors = "true".equals(sEnableCors);
        
    }

    /**
     * destroy
     *
     */
    @Override
    public void destroy() {
    }

}