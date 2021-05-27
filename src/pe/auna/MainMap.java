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
 * @author swe jorge.delucia
 */
public class MainMap {

	public static void main(String[] args) {
//		String caca = "";
//		caca.replace(",", "");
//		System.out.println(String.format("%15s", caca.replace(',', ' ')).replace(' ', '0'));
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
			InputStream responseIn = new FileInputStream(new File("resources/getCondicionMedicaResponse_in.xml"));
			OutputStream responseOut = new FileOutputStream(new File("resources/getCondicionMedicaResponse_out.xml"));
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
			InputStream requestIn = new FileInputStream(new File("resources/getConsultaAsegNomRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(new File("resources/getConsultaAsegNomRequest_out.xml"));
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
			InputStream requestIn = new FileInputStream(new File("resources/getConsultaEntVinculadaRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(
					new File("resources/getConsultaEntVinculadaRequest_out.xml"));
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
			InputStream requestIn = new FileInputStream(new File("resources/getConsultaDerivaRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(new File("resources/getConsultaDerivaRequest_out.xml"));
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
			InputStream requestIn = new FileInputStream(new File("resources/getCondicionMedicaRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(new File("resources/getCondicionMedicaRequest_out.xml"));
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
			InputStream requestIn = new FileInputStream(new File("resources/getConsultaObservacionRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(new File("resources/getConsultaObservacionRequest_out.xml"));
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
			InputStream requestIn = new FileInputStream(new File("resources/getConsultaProcRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(new File("resources/getConsultaProcRequest_out.xml"));
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
			InputStream requestIn = new FileInputStream(new File("resources/getConsultaDatosAdiRequest_in.xml"));
			OutputStream requestOut = new FileOutputStream(new File("resources/getConsultaDatosAdiRequest_out.xml"));
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
			InputStream requestIn = new FileInputStream(new File("resources/getNumAutorizacionRequest_in2.xml"));
			OutputStream requestOut = new FileOutputStream(new File("resources/getNumAutorizacionRequest_out.xml"));
			NumAutorizacionRequest requestMapping = new NumAutorizacionRequest();
			requestMapping.setDebug(debug);
			requestMapping.execute(requestIn, requestOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String algo(String var1) {

		Map<String, String> mapDet = new HashMap<String, String>();
		Document doc = null;
		Element eElement = null;
		String strx = "";

		try {
			InputStream in = new ByteArrayInputStream(var1.getBytes("UTF-8"));
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

		NodeList items = doc.getDocumentElement().getChildNodes();

		if (items.getLength() > 0) {
			for (int i = 0; i < items.getLength(); i++) {
				if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
					if (items.item(i).getNodeName().equals("STCD1")    || //E1KNA1M-STCD1
						items.item(i).getNodeName().equals("KUNNR")	   || //E1KNA1M-KUNNR
						items.item(i).getNodeName().equals("SPERR")	   || //E1KNA1M-SPERR
						items.item(i).getNodeName().equals("STRAS")    || //E1KNA1M-STRAS
						items.item(i).getNodeName().equals("TELF1")    || //E1KNA1M-TELF1
						items.item(i).getNodeName().equals("ORT01")    || //E1KNA1M-ORT01
						items.item(i).getNodeName().equals("ORT02")    ||
						items.item(i).getNodeName().equals("NAME1")    ||
						items.item(i).getNodeName().equals("NAME2")    ||
						items.item(i).getNodeName().equals("NAME3")    ||
						items.item(i).getNodeName().equals("NAME4")) {    //E1KNA1M-ORT02

						mapDet.put(items.item(i).getNodeName(), items.item(i).getTextContent().trim());

					}
				}
			} 
			
			NodeList e1kna11 = doc.getElementsByTagName("E1KNA11");
			eElement = (Element) e1kna11.item(0);
			NodeList e1kna11Childs = eElement.getChildNodes();

			for (int j = 0; j < e1kna11Childs.getLength(); j++) {
				Node e1kna11Child = (Node) e1kna11Childs.item(j);
				if (e1kna11Child.getNodeType() == Node.ELEMENT_NODE) {
					if (e1kna11Child.getNodeName().equals("KNURL")) {
						mapDet.put(e1kna11Child.getNodeName(), e1kna11Child.getTextContent().trim());
					}
				}
			}		
			
			NodeList e1mdmaa = doc.getElementsByTagName("E1MDMAA");
			eElement = (Element) e1mdmaa.item(0);
			NodeList e1mdmaaChilds = eElement.getChildNodes();

			for (int j = 0; j < e1mdmaaChilds.getLength(); j++) {
				Node e1mdmaaChild = (Node) e1mdmaaChilds.item(j);
				if (e1mdmaaChild.getNodeType() == Node.ELEMENT_NODE) {
					if (e1mdmaaChild.getNodeName().equals("USERNAME")) {
						mapDet.put(e1mdmaaChild.getNodeName(), 
								  (e1mdmaaChild.getTextContent().equals("") ? 
								   "INICIALIZA" : e1mdmaaChild.getTextContent().trim()));
					}
//					container.getTrace().addInfo(hijo.getNodeName().toString());
				}
			}
			
			NodeList e1knvvm = doc.getElementsByTagName("E1KNVVM");
			eElement = (Element) e1knvvm.item(0);
			NodeList e1knvvmChilds = eElement.getChildNodes();

			for (int j = 0; j < e1knvvmChilds.getLength(); j++) {
				Node e1knvvmChild = (Node)  e1knvvmChilds.item(j);
				if (e1knvvmChild.getNodeType() == Node.ELEMENT_NODE) {
					if (e1knvvmChild.getNodeName().equals("KVGR1") ||
						e1knvvmChild.getNodeName().equals("ZTERM") ||
						e1knvvmChild.getNodeName().equals("KVGR2") ||
						e1knvvmChild.getNodeName().equals("KVGR3") ) {
						mapDet.put(e1knvvmChild.getNodeName(), e1knvvmChild.getTextContent().trim());
					}
				}
			}

			strx = ((mapDet.containsKey("STCD1") == true) ? mapDet.get("STCD1") : "") + "|" + 
				   ((mapDet.containsKey("KUNNR") == true) ? mapDet.get("KUNNR") : "") + "|" + 
			       ((mapDet.containsKey("SPERR") == true) ? mapDet.get("SPERR") : "") + "|" +
			       ((mapDet.containsKey("USERNAME") == true) ? mapDet.get("USERNAME") : "") + "|" + 
				   
				   (((mapDet.containsKey("NAME1") == true) ? mapDet.get("NAME1") : "") + " " + 
				    ((mapDet.containsKey("NAME2") == true) ? mapDet.get("NAME2") : "")).trim() + "|" + 
				   (((mapDet.containsKey("NAME3") == true) ? mapDet.get("NAME3") : "") + " " +
				    ((mapDet.containsKey("NAME4") == true) ? mapDet.get("NAME4") : "")).trim() + "|" +
				    
			       ((mapDet.containsKey("STRAS") == true) ? mapDet.get("STRAS") : "") + "|" + 
			       ((mapDet.containsKey("TELF1") == true) ? mapDet.get("TELF1") : "") + "|" + 
			       ((mapDet.containsKey("ORT01") == true) ? mapDet.get("ORT01") : "") + "|" + 
			       ((mapDet.containsKey("KNURL") == true) ? mapDet.get("KNURL") : "") + "|" + 
			       ((mapDet.containsKey("KVGR1") == true) ? mapDet.get("KVGR1") : "") + "|" + 
			       ((mapDet.containsKey("ZTERM") == true) ? mapDet.get("ZTERM") : "") + "|" + 
			       ((mapDet.containsKey("KVGR2") == true) ? mapDet.get("KVGR2") : "") + "|" + 
			       ((mapDet.containsKey("KVGR3") == true) ? mapDet.get("KVGR3") : "") + "|" + 
			       ((mapDet.containsKey("ORT02") == true) ? mapDet.get("ORT02") : "") + "|";

		}

		return strx;

	}
	
	
//	private static String algo(String var1) {
//
//		Map<String, String> mapDet = new HashMap<String, String>();
//		Document doc = null;
//		Element eElement = null;
//		String strx = "";
//
//		try {
//			InputStream in = new ByteArrayInputStream(var1.getBytes("UTF-8"));
//			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage());
//		}
//
////		NodeList e1kna1m = doc.getElementsByTagName("E1KNA1M");
////		NodeList e1mdmaa = doc.getElementsByTagName("E1MDMAA");
////		NodeList e1kna11 = doc.getElementsByTagName("E1KNA11");
////		NodeList e1knvvm = doc.getElementsByTagName("E1KNVVM");
//
//		// eElement = (Element) e1kna11.item(0);
//		// container.getTrace().addInfo(eElement.toString());
//
//		NodeList items = doc.getDocumentElement().getChildNodes();
//
//		if (items.getLength() > 0) {
//			for (int i = 0; i < items.getLength(); i++) {
//				if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
//					if (items.item(i).getNodeName().equals("STCD1")    || //E1KNA1M-STCD1
//						items.item(i).getNodeName().equals("KUNNR")	   || //E1KNA1M-KUNNR
//						items.item(i).getNodeName().equals("SPERR")	   || //E1KNA1M-SPERR
//						items.item(i).getNodeName().equals("USERNAME") || //
////						items.item(i).getNodeName().equals("NAME1")    ||
////					    items.item(i).getNodeName().equals("NAME2")    ||
////						items.item(i).getNodeName().equals("NAME3")    ||
//						items.item(i).getNodeName().equals("STRAS")    || //E1KNA1M-STRAS
//						items.item(i).getNodeName().equals("TELF1")    || //E1KNA1M-TELF1
//						items.item(i).getNodeName().equals("ORT01")    || //E1KNA1M-ORT01
////						items.item(i).getNodeName().equals("KNURL")    || //E1KNA11-KNURL
////						items.item(i).getNodeName().equals("KVGR1")    || //E1KNVVM-KVGR1
////						items.item(i).getNodeName().equals("ZTERM")    || //E1KNVVM-ZTERM
////						items.item(i).getNodeName().equals("KVGR2")    || //E1KNVVM-KVGR2
////						items.item(i).getNodeName().equals("KVGR3")    || //E1KNVVM-KVGR3
//						items.item(i).getNodeName().equals("ORT02")) {    //E1KNA1M-ORT02
//
////						if (e1kna11.item(0).getNodeName().equals("KNURL")) {
////							e1kna11.item(0).getTextContent();
////						}
//
//						mapDet.put(items.item(i).getNodeName(), items.item(i).getTextContent().trim());
//
//					}
//				}
//
////				if (e1kna11.getLength() > 0) {
////					for (int j = 0; j < e1kna11.getLength(); j++) {
////						if (e1kna11.item(j).getNodeType() == Node.ELEMENT_NODE) {
////							if (e1kna11.item(j).getNodeName().equals("KNURL")) {
////								mapDet.put(e1kna11.item(j).getNodeName(), e1kna11.item(j).getTextContent().trim());
////							}
////						}
////					}
////				}
//
//			} // eElement = (Element) conDtad271.item(0);
//			
//			NodeList e1kna11 = doc.getElementsByTagName("E1KNA11");
//			eElement = (Element) e1kna11.item(0);
//			NodeList e1kna11Childs = eElement.getChildNodes();
//
//			for (int j = 0; j < e1kna11Childs.getLength(); j++) {
//				// Obtengo al hijo actual
//				Node e1kna11Child = (Node) e1kna11Childs.item(j);
//				// Compruebo si es un nodo
//				if (e1kna11Child.getNodeType() == Node.ELEMENT_NODE) {
//					// Muestro el contenido
////                System.out.println("Propiedad: " + hijo.getNodeName() + ", Valor: " + hijo.getTextContent());
//					if (e1kna11Child.getNodeName().equals("KNURL")) {
//						mapDet.put(e1kna11Child.getNodeName(), e1kna11Child.getTextContent().trim());
//					}
////					container.getTrace().addInfo(hijo.getNodeName().toString());
//				}
//
//			}
//			
//			
//			
//			NodeList e1mdmaa = doc.getElementsByTagName("E1MDMAA");
//			eElement = (Element) e1mdmaa.item(0);
//			NodeList e1mdmaaChilds = eElement.getChildNodes();
//
//			for (int j = 0; j < e1mdmaaChilds.getLength(); j++) {
//				// Obtengo al hijo actual
//				Node e1mdmaaChild = (Node) e1mdmaaChilds.item(j);
//				// Compruebo si es un nodo
//				if (e1mdmaaChild.getNodeType() == Node.ELEMENT_NODE) {
//					// Muestro el contenido
////                System.out.println("Propiedad: " + hijo.getNodeName() + ", Valor: " + hijo.getTextContent());
//					if (e1mdmaaChild.getNodeName().equals("USERNAME")) {
//						mapDet.put(e1mdmaaChild.getNodeName(), e1mdmaaChild.getTextContent().trim());
//					}
////					container.getTrace().addInfo(hijo.getNodeName().toString());
//				}
//
//			}
//
//			
//			NodeList e1knvvm = doc.getElementsByTagName("E1KNVVM");
//			eElement = (Element) e1knvvm.item(0);
//			NodeList e1knvvmChilds = eElement.getChildNodes();
//
//			for (int j = 0; j < e1knvvmChilds.getLength(); j++) {
//				// Obtengo al hijo actual
//				Node e1knvvmChild = (Node) e1kna11Childs.item(j);
//				// Compruebo si es un nodo
//				if (e1knvvmChild.getNodeType() == Node.ELEMENT_NODE) {
//					// Muestro el contenido
////                System.out.println("Propiedad: " + hijo.getNodeName() + ", Valor: " + hijo.getTextContent());
//					if (e1knvvmChild.getNodeName().equals("KVGR1") ||
//						e1knvvmChild.getNodeName().equals("ZTERM") ||
//						e1knvvmChild.getNodeName().equals("KVGR2") ||
//						e1knvvmChild.getNodeName().equals("KVGR3") ) {
//						mapDet.put(e1knvvmChild.getNodeName(), e1knvvmChild.getTextContent().trim());
//					}
////					container.getTrace().addInfo(hijo.getNodeName().toString());
//				}
//
//			}
//
//			// for (Map.Entry<String, String> det : mapDet.entrySet()) {
//			// System.out.println(entry.getKey() + " = " + entry.getValue());
//			strx = mapDet.get("STCD1") + "|" + mapDet.get("KUNNR") + "|" + mapDet.get("SPERR") + "|"
//					+ mapDet.get("USERNAME") + "|" + mapDet.get("NAME1") + "|" + mapDet.get("NAME2") + "|"
//					+ mapDet.get("NAME3") + "|" + mapDet.get("STRAS") + "|" + mapDet.get("TELF1") + "|"
//					+ mapDet.get("ORT01") + "|" + mapDet.get("KNURL") + "|" + mapDet.get("KVGR1") + "|"
//					+ mapDet.get("ZTERM") + "|" + mapDet.get("KVGR2") + "|" + mapDet.get("KVGR3") + "|"
//					+ mapDet.get("ORT02") + "|";
//
//		}
//
//		return strx;
//
////		Map<String, String> mapDet = new HashMap<String, String>();
////		Document doc = null;
////		String strx = "";
////
////		try {
////			InputStream in = new ByteArrayInputStream(var1.getBytes("UTF-8"));
//////    		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
////		} catch (Exception e) {
////			throw new RuntimeException(e.getMessage());
////		}
////
////		NodeList e1kna11 = doc.getElementsByTagName("E1KNA11");
////		// Element eElement = (Element) uno.item(0);
////		// container.getTrace().addInfo(eElement.toString());
////
////		NodeList items = doc.getDocumentElement().getChildNodes();
////
////		if (items.getLength() > 0) {
////			for (int i = 0; i < items.getLength(); i++) {
////				if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
////					if (items.item(i).getNodeName().equals("STCD1") || items.item(i).getNodeName().equals("KUNNR")
////							|| items.item(i).getNodeName().equals("SPERR")
////							|| items.item(i).getNodeName().equals("USERNAME")
////							|| items.item(i).getNodeName().equals("NAME1")
////							|| items.item(i).getNodeName().equals("NAME2")
////							|| items.item(i).getNodeName().equals("NAME3")
////							|| items.item(i).getNodeName().equals("STRAS")
////							|| items.item(i).getNodeName().equals("TELF1")
////							|| items.item(i).getNodeName().equals("ORT01")
////							|| items.item(i).getNodeName().equals("KNURL")
////							|| items.item(i).getNodeName().equals("KVGR1")
////							|| items.item(i).getNodeName().equals("ZTERM")
////							|| items.item(i).getNodeName().equals("KVGR2")
////							|| items.item(i).getNodeName().equals("KVGR3")
////							|| items.item(i).getNodeName().equals("ORT02")) {
////
////						mapDet.put(items.item(i).getNodeName(), items.item(i).getTextContent().trim());
////
////					}
////				}
////			}
////
////			// for (Map.Entry<String, String> det : mapDet.entrySet()) {
////			// System.out.println(entry.getKey() + " = " + entry.getValue());
////			strx = mapDet.get("STCD1") + "|" + mapDet.get("KUNNR") + "|" + mapDet.get("SPERR") + "|"
////					+ mapDet.get("USERNAME") + "|" + mapDet.get("NAME1") + "|" + mapDet.get("NAME2") + "|"
////					+ mapDet.get("NAME3") + "|" + mapDet.get("STRAS") + "|" + mapDet.get("TELF1") + "|"
////					+ mapDet.get("ORT01") + "|" + mapDet.get("KNURL") + "|" + mapDet.get("KVGR1") + "|"
////					+ mapDet.get("ZTERM") + "|" + mapDet.get("KVGR2") + "|" + mapDet.get("KVGR3") + "|"
////					+ mapDet.get("ORT02") + "|";
////
////		}
////
////		return strx;
//
//	}
}