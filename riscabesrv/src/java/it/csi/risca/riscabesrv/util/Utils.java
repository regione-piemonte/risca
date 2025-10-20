/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.risca.riscabesrv.dto.ambiente.DatiTecniciAmbienteDTO;
import it.csi.risca.riscabesrv.dto.enumeration.ValidationResultEnum;
import it.csi.risca.riscabesrv.util.validation.ValidationUtil;

public class Utils {

	protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd"; // Imposta il formato data predefinito
	public static final String DATE_FORMAT_DD_MM_YYYY= "dd/MM/yyyy"; // Imposta il formato data predefinito
    public static String getCurrentDateFormatted() {
        return getCurrentDateFormatted(DEFAULT_DATE_FORMAT);
    }

    public static String getCurrentDateFormatted(String dateFormat) {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(now);
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
		LOGGER.debug("[Utils::isLocalMod] BEGIN");

		Properties p = new Properties();
		try {
			InputStream is = getClass().getResourceAsStream("/application.properties");
			LOGGER.debug("[Utils::isLocalMod] Input Stream ");
			if (is == null) {
				return false;
			}
			p.load(is);
			LOGGER.debug("[Utils::isLocalMod] dev.mode " + p.getProperty("dev.mode"));
		} catch (IOException e) {
			LOGGER.debug("[Utils::isLocalMod] false ");
			return false;

		}
		String devModeString = p.getProperty("dev.mode") != null ? p.getProperty("dev.mode") : "";
		if ("true".equalsIgnoreCase(devModeString)) {
			LOGGER.debug("[Utils::isLocalMod]: " + true);
			LOGGER.debug("[Utils::isLocalMod] END ");
			return true;
		} else {
			LOGGER.debug("[Utils::isLocalMod]: " + false);
			LOGGER.debug("[Utils::isLocalMod] END ");
			return false;
		}

	}

	public static long daysInYear(int year) {
		TimeUnit time = TimeUnit.DAYS;
		return time.convert(new Date(year, 12, 31).getTime() - new Date(year, 01, 01).getTime(), TimeUnit.MILLISECONDS)
				+ 1;
	}


	/* public static long diffGiorniTraDueDate(Date inizio, Date dataFine) {
		TimeUnit time = TimeUnit.DAYS;
		long diffGiorni = Math.abs(dataFine.getTime() - inizio.getTime());
		return TimeUnit.DAYS.convert(diffGiorni, TimeUnit.MILLISECONDS) + 1;// add 1 per includere ultimo giorno
	} */

	
	public static long diffGiorniTraDueDate(Date inizio, Date dataFine) {
		DateTime ini = new DateTime(inizio.getTime());
		DateTime fin = new DateTime(dataFine.getTime());
		return Days.daysBetween(ini, fin).getDays() + 1; // add 1 per includere ultimo giorno
	}

	public static Date addDaysInDate(Date data, int giorniDaAggiungere) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.DATE, giorniDaAggiungere);
		Date date = c.getTime();
		return date;
	}

	public static Boolean isValidateCFPIva(String pIva, String cf) {
		ValidationResultEnum result = null;
		if (StringUtils.isNotBlank(pIva))
			result = ValidationUtil.isValidPIva(pIva);
		else
			result = ValidationUtil.validateCF(cf);

		if (result == ValidationResultEnum.VALID) {
			return true;
		}
		return false;
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
			LOGGER.error(e);
			return null;
		}
	}
	public static DatiTecniciAmbienteDTO creaDatiTecniciFromJsonDt(String jsonDt, String campoDaLeggere) {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
		final JSONObject obj = new JSONObject(jsonDt);
		final JSONObject geodata = obj.getJSONObject("riscossione");
		String campo = "";
		DatiTecniciAmbienteDTO datiTecniciAmbiente = null;
		for (String key : obj.getJSONObject("riscossione").keySet()) {
			if (key.equals(campoDaLeggere))
				campo = geodata.getJSONObject(campoDaLeggere).toString();
		}
		try {
			datiTecniciAmbiente = mapper.readValue(campo, DatiTecniciAmbienteDTO.class);
		} catch (IOException e) {
			LOGGER.error("[Utils::isLocalMod] Errore nella lettura dati json dt", e);
		}

		return datiTecniciAmbiente;
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
	 * Formatta un array di stringhe in una singola stringa separata da un separatore specifico.
	 *
	 * @param separator      Il separatore da utilizzare per separare le stringhe nell'array.
	 * @param arrayToFormat  L'array di stringhe da formattare.
	 * @return Una stringa contenente le stringhe dell'array separate dal separatore specificato.
	 */
	public static String formatArraytoStringWithSeparator(String separator, String[] arrayToFormat) {
	    StringBuilder stringBuilder = new StringBuilder();
	    int lunghezza = arrayToFormat != null ? arrayToFormat.length : 0;
	    for (int i = 0; i < lunghezza; i++) {
	        stringBuilder.append(arrayToFormat[i]);
	        if (i < lunghezza - 1) {
	            stringBuilder.append(separator);
	        }
	    }
	    return stringBuilder.toString();
	}

	/**
	 * Converte una stringa contenente numeri separati da un separatore in una lista di oggetti di tipo numerico specificato.
	 *
	 * @param inputString La stringa contenente i numeri da convertire.
	 * @param separatore Il separatore utilizzato per dividere i numeri nella stringa.
	 * @param numberType Il tipo numerico desiderato (ad esempio Integer.class, Double.class, Long.class).
	 * @param <T> Il tipo generico che estende Number.
	 * @return Una lista di oggetti numerici del tipo specificato.
	 */
	public static <T extends Number> List<T> convertStringToList(String inputString, String separatore, Class<T> numberType) {
	    String[] stringNumbers = inputString.split(separatore);
	    List<T> numbers = new ArrayList<>();

	    for (String stringNumber : stringNumbers) {
	        try {
	            if (numberType == Integer.class) {
	                numbers.add(numberType.cast(Integer.parseInt(stringNumber)));
	            } else if (numberType == Double.class) {
	                numbers.add(numberType.cast(Double.parseDouble(stringNumber)));
	            } else if (numberType == Long.class) {
	                numbers.add(numberType.cast(Long.parseLong(stringNumber)));
	            } else {
	                throw new IllegalArgumentException("Tipo numerico non supportato: " + numberType.getSimpleName());
	            }
	        } catch (NumberFormatException e) {
	            LOGGER.debug("[Utils::convertStringToList] ERROR ", e);
	        }
	    }

	    return numbers;
	}

	/**
	 * Verifica se una lista e' non nulla e non vuota.
	 *
	 * @param list La lista da verificare.
	 * @return true se la lista e' non nulla e non vuota, altrimenti false.
	 */
	public static boolean isNotEmpty(List<?> list) {
	    return list != null && !list.isEmpty();
	}
	
	/**
	 * Converte una stringa rappresentante una data in un oggetto Date utilizzando il formato specificato.
	 *
	 * @param dateString La stringa contenente la data da convertire.
	 * @param format Il formato della data nella stringa, ad esempio "yyyy-MM-dd".
	 * @return Un oggetto Date rappresentante la data convertita, oppure null se si verifica un'eccezione durante la conversione.
	 */
	public static Date convertiStringInData(String dateString, String format) {
	        SimpleDateFormat sdf = new SimpleDateFormat(format);
	        try {
	            return sdf.parse(dateString);
	        } catch (ParseException e) {
	            LOGGER.debug("[Utils::convertiStringInData] ERROR ", e);
	            return null;
	        }
	    }
	/**
	 * Formatta un importo numerico aggiungendo il separatore delle migliaia e sostituendo il punto finale con la virgola.
	 *
	 * @param importo L'importo numerico da formattare.
	 * @return Una stringa rappresentante l'importo formattato con il separatore delle migliaia e la virgola come separatore decimale.
	 *         Ad esempio, 3831.90 diventa "3.831,00".
	 */
	public static String formattaImporto(BigDecimal importo) {
	    // Crea un oggetto DecimalFormat per formattare l'importo
	    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.ITALY));

	    // Formatta l'importo
	    String importoFormattato = decimalFormat.format(importo);


	    return importoFormattato;
	}
	public static String truncateImporto(String importoFormattato) {
        int posizioneVirgola = importoFormattato.indexOf(",");

        // Sostituisci dopo la virgola con ",00"
        importoFormattato = importoFormattato.substring(0, posizioneVirgola + 1) + "00";


	    return importoFormattato;
	}
	
    /**
     * Copia valori non nulli dall'oggetto sorgente all'oggetto destinazione utilizzando la riflessione.
     *
     * @param source      L'oggetto sorgente da cui copiare i valori.
     * @param destination L'oggetto destinazione a cui verranno copiati i valori.
     * @throws IllegalArgumentException Se l'oggetto sorgente o destinazione e' nullo.
     */
    public static void copyNonNullValues(Object source, Object destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination objects must not be null.");
        }

        Class<?> sourceClass = source.getClass();
        Class<?> destinationClass = destination.getClass();

        Method[] sourceMethods = sourceClass.getMethods();

        for (Method sourceMethod : sourceMethods) {
            String methodName = sourceMethod.getName();

            // Controlla se il metodo e' un getter
            if (methodName.startsWith("get") && !methodName.equals("getClass")) {
                try {
                    // Ottieni il valore dal metodo getter
                    Object value = sourceMethod.invoke(source);

                    // Se il valore e' non nullo, procedi con la copia
                    if (value != null) {
                        // Costruisci il nome del metodo setter corrispondente
                        String setterName = "set" + methodName.substring(3);

                        // Trova il metodo setter corrispondente nella classe di destinazione
                        Method destinationSetter = findMethod(destinationClass, setterName, sourceMethod.getReturnType());

                        // Se il metodo setter esiste, invocalo per impostare il valore nella classe di destinazione
                        if (destinationSetter != null) {
                            destinationSetter.invoke(destination, value);
                        }
                    }
                } catch (Exception e) {
    	            LOGGER.debug("[Utils::copyNonNullValues] ERROR ", e);
                }
            }
        }
    }
    /**
     * Metodo di supporto per trovare un metodo con il nome e il tipo di ritorno specificati in una classe.
     *
     * @param clazz        La classe in cui cercare il metodo.
     * @param methodName   Il nome del metodo da cercare.
     * @param returnType   Il tipo di ritorno del metodo.
     * @return L'oggetto Method se trovato, o null se non trovato.
     */
    private static Method findMethod(Class<?> clazz, String methodName, Class<?> returnType) {
        try {
            return clazz.getMethod(methodName, returnType);
        } catch (NoSuchMethodException e) {
            // Il metodo non esiste nella classe, ignora e continua
            return null;
        }
    }
    
	public static Date getStartDateForYear(int year, Calendar calendar) {
		Calendar startCalendar = (Calendar) calendar.clone();
		startCalendar.set(Calendar.YEAR, year);
		startCalendar.set(Calendar.MONTH, Calendar.JANUARY);
		startCalendar.set(Calendar.DAY_OF_MONTH, 1);
		resetTimeToMidnight(startCalendar);
		return startCalendar.getTime();
	}

	public static Date getEndDateForYear(int year, Calendar calendar) {
		Calendar endCalendar = (Calendar) calendar.clone();
		endCalendar.set(Calendar.YEAR, year);
		endCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
		endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		resetTimeToMidnight(endCalendar);
		return endCalendar.getTime();
	}

	public static void resetTimeToMidnight(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}
	
	public static Date setHour(Date date, int hour, int min) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}



	public static boolean isDateAfter(String firstDate, String secondDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date dFirstDate = sdf.parse(firstDate);
			Date dSecondDate = sdf.parse(secondDate);

			return dFirstDate.after(dSecondDate);
		}
		catch (ParseException e)
		{
			return false;
		}
	}
	
	public static Long checkLongNull(Long number)
	{
		if (number == null || number.longValue() == 0) return null;
		
		return number;
	}
	
	public static String getCurrentDate() {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return sdf.format(date);
	}



	/**
	 * Verifica se la stringa JSON contiene caratteri non desiderati.
	 *
	 * @param jsonString La stringa JSON da verificare.
	 * @return true se la stringa contiene caratteri non desiderati, false altrimenti.
	 */
    public static boolean containsUnwantedCharacters(String jsonString) {
        return jsonString.contains("%");
    }
    
    /**
     * Verifica se la stringa fornita rappresenta un JSON valido.
     *
     * @param jsonString La stringa da verificare come JSON.
     * @return true se la stringa e' un JSON valido, false altrimenti.
     */
    
    public static boolean isValidJson(String jsonOrigin) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(jsonOrigin);
            if(!jsonOrigin.endsWith("}")) {
                return false;
            }
            // Convalida anche con la libreria JSONObject se necessario
            new JSONObject(jsonOrigin);

            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Trunca una stringa e restituisce solo i primi 4 caratteri.
     * 
     * @param input La stringa da troncare.
     * @return Una stringa contenente solo i primi 4 caratteri dell'input, oppure l'intera stringa se e' piu' corta di 4 caratteri.
     */
    public static String truncateToFourCharacters(String input) {
        // Verifica se la lunghezza dell'input e' inferiore a 4
        if (input == null) {
            return ""; // Restituisce una stringa vuota se l'input e' nullo
        }
        
        if (input.length() <= 4) {
            return input; // Restituisce l'input cosi com'e se e' piu' corto o uguale a 4 caratteri
        }

        // Restituisce solo i primi 4 caratteri dell'input
        return input.substring(0, 4);
    }
    

	public static String padString(String str, int len, boolean alignLeft) {
		String allign = alignLeft?"-":"";
		return String.format("%1$"+allign+len+"s", str, null);
		
	}
}
