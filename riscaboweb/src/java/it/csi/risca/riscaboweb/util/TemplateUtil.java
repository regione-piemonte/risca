package it.csi.risca.riscaboweb.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

/**
 * The type Template util.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TemplateUtil {

    public static final Logger LOGGER = Logger.getLogger(Constants.COMPONENT_NAME + ".service");
	 /**
     * Get compiled template pdf byte [ ].
     *
     * @param json         json
     * @param templatePath templatePath
     * @return byte[] byte [ ]
     * @throws Exception Exception
     */
    public static byte[] getCompiledTemplatePDF(String json, String templatePath) throws Exception {
		LOGGER.debug("[TemplateUtil::getCompiledTemplatePDF] BEGIN");
        try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			IXDocReport report;
			InputStream in =TemplateUtil.class.getClassLoader().getResourceAsStream(templatePath);
			report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
			Map<String, Object> mappings = new HashMap<>();
			JSONObject obj = new JSONObject(json);
			mappings.put("obj", obj);
			IContext context = report.createContext();
			context.put("d", mappings);
			Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
			report.convert(context, options, out);
			LOGGER.debug("[TemplateUtil::getCompiledTemplatePDF] END");
			return out.toByteArray();
		} catch (FileNotFoundException e) {
			LOGGER.error("[TemplateUtil::getCompiledTemplatePDF] ERRORE :", e);
			throw new Exception(e);
		} catch (JSONException e) {
			LOGGER.error("[TemplateUtil::getCompiledTemplatePDF] ERRORE :", e);
			throw new Exception(e);
		} catch (XDocConverterException e) {
			LOGGER.error("[TemplateUtil::getCompiledTemplatePDF] ERRORE :", e);
			throw new Exception(e);
		} catch (IOException e) {
			LOGGER.error("[TemplateUtil::getCompiledTemplatePDF] ERRORE :", e);
			throw new Exception(e);
		} catch (XDocReportException e) {
			LOGGER.error("[TemplateUtil::getCompiledTemplatePDF] ERRORE :", e);
			throw new Exception(e);
		}
    }
}
