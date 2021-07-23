package pe.auna;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import pe.auna.siteds.mapping.CondicionMedicaRequest;
import pe.auna.siteds.mapping.CondicionMedicaResponse;
import pe.auna.siteds.mapping.ConsultaAsegCodRequest;
import pe.auna.siteds.mapping.ConsultaAsegNomRequest;
import pe.auna.siteds.mapping.ConsultaDatosAdiRequest;
import pe.auna.siteds.mapping.ConsultaDatosAdiResponse;
import pe.auna.siteds.mapping.ConsultaDerivaRequest;
import pe.auna.siteds.mapping.ConsultaEntVinculadaRequest;
import pe.auna.siteds.mapping.ConsultaObservacionRequest;
import pe.auna.siteds.mapping.ConsultaProcRequest;
import pe.auna.siteds.mapping.ConsultaxCartaGarantiaRequest;
import pe.auna.siteds.mapping.ConsultaxCartaGarantiaResponse;
import pe.auna.siteds.mapping.ConsultaxCartaGarantiaResponseBorrar;
import pe.auna.siteds.mapping.NumAutorizacionRequest;

/**
 *
 * @author Pablo Paladino / Bryan Vargas
 */
public class MainMap {

	public static void main(String[] args) {
		/***** RESPONSE ****/
//    	testGetCondicionMedicaResponse();
    	testGetConsultaxCartaGarantiaResponse();
		/***** REQUEST *****/
//    	testGetCondicionMedicaRequest();
//    	testGetConsultaAsegCodRequest();
//    	testGetConsultaAsegNomRequest();
//    	testGetConsultaEntVinculadaRequest();
//    	testGetConsultaDerivaRequest();
    	//testGetConsultaxCartaGarantiaRequest();
//    	testGetConsultaObservacionRequest();
//    	testGetConsultaProcRequest();
//    	testGetConsultaDatosAdiRequest();
//		testGetConsultaDatosAdiResponse();
//    	testGetNumAutorizacionRequest();
	}

	private static void testGetConsultaDatosAdiResponse() {
		try {
			boolean debug = true;
			InputStream responseIn = new FileInputStream(new File("src/resources/getConsultaDatosAdiResponse_in.xml"));
			OutputStream responseOut = new FileOutputStream(
					new File("src/resources/getConsultaDatosAdiResponse_out.xml"));
			ConsultaDatosAdiResponse responseMapping = new ConsultaDatosAdiResponse();
			responseMapping.setDebug(debug);
			responseMapping.execute(responseIn, responseOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testGetCondicionMedicaResponse() {
		try {
			boolean debug = true;
			InputStream responseIn = new FileInputStream(new File("src/resources/getCondicionMedicaResponse_in.xml"));
			OutputStream responseOut = new FileOutputStream(new File("src/resources/getCondicionMedicaResponse_out.xml"));
			CondicionMedicaResponse responseMapping = new CondicionMedicaResponse();
			responseMapping.setDebug(debug);
			responseMapping.execute(responseIn, responseOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testGetConsultaAsegCodRequest() {
		try {
			boolean debug = true;
			InputStream requestIn = new FileInputStream(
					new File("src/resources/getConsultaAsegCodRequest_in_error.xml"));
			OutputStream requestOut = new FileOutputStream(new File("src/resources/getConsultaAsegCodRequest_out.xml"));
			ConsultaAsegCodRequest requestMapping = new ConsultaAsegCodRequest();
			requestMapping.setDebug(debug);
			requestMapping.execute(requestIn, requestOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testGetConsultaAsegNomRequest() {
		try {
			boolean debug = true;
			InputStream requestIn = new FileInputStream(new File("src/resources/getConsultaAsegNomRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(new File("src/resources/getConsultaAsegNomRequest_out.xml"));
			ConsultaAsegNomRequest requestMapping = new ConsultaAsegNomRequest();
			requestMapping.setDebug(debug);
			requestMapping.execute(requestIn, requestOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testGetConsultaEntVinculadaRequest() {
		try {
			boolean debug = true;
			InputStream requestIn = new FileInputStream(new File("src/resources/getConsultaEntVinculadaRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(
					new File("src/resources/getConsultaEntVinculadaRequest_out.xml"));
			ConsultaEntVinculadaRequest requestMapping = new ConsultaEntVinculadaRequest();
			requestMapping.setDebug(debug);
			requestMapping.execute(requestIn, requestOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testGetConsultaDerivaRequest() {
		try {
			boolean debug = true;
			InputStream requestIn = new FileInputStream(new File("src/resources/getConsultaDerivaRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(new File("src/resources/getConsultaDerivaRequest_out.xml"));
			ConsultaDerivaRequest requestMapping = new ConsultaDerivaRequest();
			requestMapping.setDebug(debug);
			requestMapping.execute(requestIn, requestOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testGetConsultaxCartaGarantiaRequest() {
		try {
			boolean debug = true;
			InputStream requestIn = new FileInputStream(
					new File("src/resources/getConsultaxCartaGarantiaRequest_in2.xml"));
			OutputStream requestOut = new FileOutputStream(
					new File("src/resources/getConsultaxCartaGarantiaRequest_out.xml"));
			ConsultaxCartaGarantiaRequest requestMapping = new ConsultaxCartaGarantiaRequest();
			requestMapping.setDebug(debug);
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("USER", "lp$laprotectora2020");
			parameters.put("PASSWORD", "lp@UN@$U$@LUD");
			requestMapping.execute(requestIn, requestOut, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testGetConsultaxCartaGarantiaResponse() {
		try {
			boolean debug = true;
			InputStream responseIn = new FileInputStream(
					new File("src/resources/getConsultaxCartaGarantiaResponse_in2.xml"));
			OutputStream responseOut = new FileOutputStream(
					new File("src/resources/getConsultaxCartaGarantiaResponse_out.xml"));
			ConsultaxCartaGarantiaResponseBorrar responseMapping = new ConsultaxCartaGarantiaResponseBorrar();
			responseMapping.setDebug(debug);
			responseMapping.execute(responseIn, responseOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testGetCondicionMedicaRequest() {
		try {
			boolean debug = true;
			InputStream requestIn = new FileInputStream(new File("src/resources/getCondicionMedicaRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(new File("src/resources/getCondicionMedicaRequest_out.xml"));
			CondicionMedicaRequest requestMapping = new CondicionMedicaRequest();
			requestMapping.setDebug(debug);
			requestMapping.execute(requestIn, requestOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testGetConsultaObservacionRequest() {
		try {
			boolean debug = true;
			InputStream requestIn = new FileInputStream(new File("src/resources/getConsultaObservacionRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(new File("src/resources/getConsultaObservacionRequest_out.xml"));
			ConsultaObservacionRequest requestMapping = new ConsultaObservacionRequest();
			requestMapping.setDebug(debug);
			requestMapping.execute(requestIn, requestOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testGetConsultaProcRequest() {
		try {
			boolean debug = true;
			InputStream requestIn = new FileInputStream(new File("src/resources/getConsultaProcRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(new File("src/resources/getConsultaProcRequest_out.xml"));
			ConsultaProcRequest requestMapping = new ConsultaProcRequest();
			requestMapping.setDebug(debug);
			requestMapping.execute(requestIn, requestOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testGetConsultaDatosAdiRequest() {
		try {
			boolean debug = true;
			InputStream requestIn = new FileInputStream(new File("src/resources/getConsultaDatosAdiRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(new File("src/resources/getConsultaDatosAdiRequest_out.xml"));
			ConsultaDatosAdiRequest requestMapping = new ConsultaDatosAdiRequest();
			requestMapping.setDebug(debug);
			requestMapping.execute(requestIn, requestOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testGetNumAutorizacionRequest() {
		try {
			boolean debug = true;
			InputStream requestIn = new FileInputStream(new File("src/resources/getNumAutorizacionRequest_in2.xml"));
			OutputStream requestOut = new FileOutputStream(new File("src/resources/getNumAutorizacionRequest_out.xml"));
			NumAutorizacionRequest requestMapping = new NumAutorizacionRequest();
			requestMapping.setDebug(debug);
			requestMapping.execute(requestIn, requestOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}