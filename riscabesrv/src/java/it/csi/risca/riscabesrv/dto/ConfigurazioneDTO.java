/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Configurazione dto.
 *
 * @author CSI PIEMONTE
 */
public class ConfigurazioneDTO extends GestAttoreDTO {

	@JsonProperty("chiave")
    private String chiave;

	@JsonProperty("valore")
    private String valore;

	@JsonProperty("note")
    private String note;

    /**
     * Gets chiave.
     *
     * @return the chiave
     */
    public String getChiave() {
        return chiave;
    }

    /**
     * Sets chiave.
     *
     * @param chiave the chiave
     */
    public void setChiave(String chiave) {
        this.chiave = chiave;
    }

    /**
     * Gets valore.
     *
     * @return the valore
     */
    public String getValore() {
        return valore;
    }

    /**
     * Sets valore.
     *
     * @param valore the valore
     */
    public void setValore(String valore) {
        this.valore = valore;
    }

    /**
     * Gets note.
     *
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets note.
     *
     * @param note the note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(chiave, note, valore);
    }

    /**
     * @param obj Object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConfigurazioneDTO other = (ConfigurazioneDTO) obj;
        return Objects.equals(chiave, other.chiave) && Objects.equals(note, other.note) && Objects.equals(valore, other.valore);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ConfigurazioneDTO [chiave=").append(chiave).append("\n  valore=").append(valore).append("\n  note=").append(note).append("]");
        return builder.toString();
    }

}