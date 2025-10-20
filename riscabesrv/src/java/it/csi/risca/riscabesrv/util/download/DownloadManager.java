/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.util.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class DownloadManager {

	private static final Logger log = Logger.getLogger("risca.download");

	private String downloadPath;
	private String downloadUrl;

	public DownloadManager(String downloadPath, String downloadUrl) {
		this.downloadPath = downloadPath;
		this.downloadUrl = downloadUrl;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String copyFileToDownloadArea(String fileId, String fileRelativePath) throws DownloadException {
		log.debug("DownloadManager - copyFileToDownloadArea - " + downloadPath);
		String tempFilePath = "";
		String fileUrl = "";
		try {
			Path p = Paths.get(downloadPath);
			Path parent = p.getParent();
			String exportFilesArea = parent.toString();
			File srcFile = new File(exportFilesArea + fileRelativePath);
			String fileName = srcFile.getName();
			tempFilePath = downloadPath + File.separator  + fileName;
			File destFile = new File(tempFilePath);
			FileUtils.copyFile(srcFile, destFile);
			fileUrl = downloadUrl + "/" + fileName;
			
		} catch (IOException e) {
			log.debug("DownloadManager - copyFileToDownloadArea - ERROR " + e.getMessage());
			throw new DownloadException(e.getMessage());
		} finally {
			log.debug("DownloadManager - copyFileToDownloadArea - file copied to " + tempFilePath);
		}

		return fileUrl;
	}

}
