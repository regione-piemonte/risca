/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.scriva.dto;

import java.io.Serializable;
import java.util.Objects;

import it.csi.iride2.policy.entity.Identita;

/**
 * The type Attore scriva.
 *
 * @author CSI PIEMONTE
 */
public class XRequestAuth implements Serializable {

    private String componenteApplicativa;

    private String ruolo;

    private String username;

    private String password;

    private Identita identita;

    /**
     * Gets componente applicativa.
     *
     * @return the componente applicativa
     */
    public String getComponenteApplicativa() {
        return componenteApplicativa;
    }

    /**
     * Sets componente applicativa.
     *
     * @param componenteApplicativa the componente applicativa
     */
    public void setComponenteApplicativa(String componenteApplicativa) {
        this.componenteApplicativa = componenteApplicativa;
    }

    /**
     * Gets ruolo.
     *
     * @return the ruolo
     */
    public String getRuolo() {
        return ruolo;
    }

    /**
     * Sets ruolo.
     *
     * @param ruolo the ruolo
     */
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets identita.
     *
     * @return the identita
     */
    public Identita getIdentita() {
        return identita;
    }

    /**
     * Sets identita.
     *
     * @param identita the identita
     */
    public void setIdentita(Identita identita) {
        this.identita = identita;
    }

    /**
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XRequestAuth that = (XRequestAuth) o;
        return Objects.equals(componenteApplicativa, that.componenteApplicativa) && Objects.equals(ruolo, that.ruolo) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(identita, that.identita);
    }

    /**
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(componenteApplicativa, ruolo, username, password, identita);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        return "XRequestAuth {\n" +
                "         componenteApplicativa:'" + componenteApplicativa + "'" +
                ",\n         ruolo:'" + ruolo + "'" +
                ",\n         username:'" + username + "'" +
                ",\n         password:'" + password + "'" +
                ",\n         identita:" + identita +
                "}\n";
    }
}