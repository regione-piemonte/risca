/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class Utils {

	protected static Logger lOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public static StringBuffer checkIfFieldHasBeenModified(StringBuffer sb, String fieldName, Object fieldSrc,
			Object fieldFonte) {

		// set null when field is string empty
		if (fieldSrc instanceof String || fieldFonte instanceof String) {
			if (StringUtils.isBlank((String) fieldSrc))
				fieldSrc = null;
			if (StringUtils.isBlank((String) fieldFonte))
				fieldFonte = null;
		}
		if (fieldSrc == null && fieldFonte != null)
			return sb.append("," + fieldName);

		if (fieldSrc != null && fieldFonte == null)
			return sb.append("," + fieldName);

		if (((fieldFonte != null && fieldSrc != null))
				&& ((fieldSrc instanceof String && !StringUtils.equals((String) fieldSrc, (String) fieldFonte))
						|| (fieldSrc instanceof Number
								&& ((Number) fieldSrc).longValue() != ((Number) fieldFonte).longValue())
						|| (fieldSrc instanceof Date && ((Date) fieldSrc).getTime() != ((Date) fieldFonte).getTime())
						|| (fieldSrc instanceof Boolean
								&& ((Boolean) fieldSrc).booleanValue() != ((Boolean) fieldFonte).booleanValue())))
			return sb.append("," + fieldName);

		return sb;
	}

	public boolean isLocalMod() {
		lOGGER.debug("[Utils::isLocalMod] BEGIN");

		Properties p = new Properties();
		try {
			InputStream is = getClass().getResourceAsStream("/application.properties");
			lOGGER.debug("[Utils::isLocalMod] Input Stream ");
			if (is == null) {
				return false;
			}
			p.load(is);
			lOGGER.debug("[Utils::isLocalMod] dev.mode " + p.getProperty("dev.mode"));
		} catch (IOException e) {
			lOGGER.debug("[Utils::isLocalMod] false ");
			return false;

		}
		String devModeString = p.getProperty("dev.mode") != null ? p.getProperty("dev.mode") : "";
		if ("true".equalsIgnoreCase(devModeString)) {
			lOGGER.debug("[Utils::isLocalMod]: " + true);
			lOGGER.debug("[Utils::isLocalMod] END ");
			return true;
		} else {
			lOGGER.debug("[Utils::isLocalMod]: " + false);
			lOGGER.debug("[Utils::isLocalMod] END ");
			return false;
		}

	}

	public static long daysInYear(int year) {
		TimeUnit time = TimeUnit.DAYS;
		return time.convert(new Date(year, 12, 31).getTime() - new Date(year, 01, 01).getTime(), TimeUnit.MILLISECONDS)
				+ 1;
	}

	public static long diffGiorniTraDueDate(Date inizio, Date dataFine) {
		TimeUnit time = TimeUnit.DAYS;
		long diffGiorni = dataFine.getTime() - inizio.getTime();
		return time.convert(diffGiorni, TimeUnit.MILLISECONDS) + 1;// add 1 per includere ultimo giorno
	}

	public static Date addDaysInDate(Date data, int giorniDaAggiungere) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.DATE, giorniDaAggiungere);
		Date date = c.getTime();
		return date;
	}

	public static String removeAccentedCharacters(String input) {
		String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
		String pattern = "\\p{InCombiningDiacriticalMarks}+";
		return normalized.replaceAll(pattern, "'");
	}

	public static boolean isPropertyExists(Object object, String propertyName) {
		Class<?> objectClass = object.getClass();
		Field[] fields = objectClass.getDeclaredFields();

		for (Field field : fields) {
			if (field.getName().equals(propertyName)) {
				return true;
			}
		}

		return false;
	}

	public static String formatDate(String inputDateStr, SimpleDateFormat inputFormatter,
			SimpleDateFormat outputFormatter) {
		try {
			Date date = inputFormatter.parse(inputDateStr);
			return outputFormatter.format(date);
		} catch (ParseException e) {
			lOGGER.debug("[Utils::formatDate] ERROR ",e);
			return null;
		}
	}

	public static void setFilePermissions(String path) throws IOException {
		Path filePath = Paths.get(path);
		Set<String> supportedAttr = filePath.getFileSystem().supportedFileAttributeViews();

		if (supportedAttr.contains("posix")) {
			Files.setPosixFilePermissions(filePath, PosixFilePermissions.fromString("rwxrwx---"));
		} else if (supportedAttr.contains("acl")) {
			UserPrincipal owner = Files.getOwner(filePath);
			AclFileAttributeView view = Files.getFileAttributeView(filePath, AclFileAttributeView.class);
			AclEntry entry = AclEntry.newBuilder().setType(AclEntryType.ALLOW).setPrincipal(owner)
					.setPermissions(AclEntryPermission.READ_DATA, AclEntryPermission.WRITE_DATA,
							AclEntryPermission.APPEND_DATA, AclEntryPermission.READ_NAMED_ATTRS,
							AclEntryPermission.WRITE_NAMED_ATTRS, AclEntryPermission.EXECUTE,
							AclEntryPermission.READ_ATTRIBUTES, AclEntryPermission.WRITE_ATTRIBUTES,
							AclEntryPermission.DELETE, AclEntryPermission.READ_ACL, AclEntryPermission.SYNCHRONIZE)
					.build();

			List<AclEntry> acl = view.getAcl();
			acl.add(0, entry);
			view.setAcl(acl);
		}
	}
	/**
	 * Costruisce un URL di destinazione aggiungendo parametri a una stringa di base.
	 *
	 * @param path    La stringa di base dell'URL.
	 * @param params  Un array di parametri alternati chiave-valore da aggiungere all'URL.
	 *                Le chiavi sono agli indici pari e i valori agli indici dispari.
	 * @return Una stringa contenente l'URL di destinazione con i parametri.
	 */
	public static String buildTargetUrl(String path, Object... params) {
	    StringBuilder targetUrl = new StringBuilder(path);

	    for (int i = 0; i < params.length; i += 2) {
	        Object key = params[i];
	        Object value = (i + 1 < params.length) ? params[i + 1] : null;

	        if (value != null) {
	        	if (targetUrl.indexOf("?") != -1) {
	        		targetUrl.append("&").append(key);
	            } else {
	            	targetUrl.append("?").append(key);
	            }

	            if (value != null) {
	                targetUrl.append("=").append(value);
	            }
	        }
	    }

	    return targetUrl.toString();
	}
	


}
