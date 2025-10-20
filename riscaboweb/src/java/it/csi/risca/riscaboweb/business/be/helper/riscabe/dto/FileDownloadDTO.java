package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;
/**
 * The File Download DTO.
 *
 * @author CSI PIEMONTE
 */

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileDownloadDTO {

	@JsonProperty("file_name")
	private String fileName = null;

	@JsonProperty("mime_type")
	private String mimeType = null;

	@JsonProperty("stream")
	private byte[] stream = null;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public byte[] getStream() {
		return stream;
	}

	public void setStream(byte[] stream) {
		this.stream = stream;
	}
		
}
