package pe.auna.siteds.mapping;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sap.aii.mapping.api.AbstractTransformation;
import com.sap.aii.mapping.api.DynamicConfiguration;
import com.sap.aii.mapping.api.DynamicConfigurationKey;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;

import pe.auna.siteds.beans.GetConsultaxCartaGarantiaRequest;
import pe.auna.siteds.beans.GetConsultaxCartaGarantiaResponse;
import pe.gob.susalud.jr.transaccion.susalud.bean.In278ResCG;
import pe.gob.susalud.jr.transaccion.susalud.bean.In278ResCGDetalle;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.In278ResCGServiceImpl;


public class ConsultaxCartaGarantiaResponseBorrar extends AbstractTransformation {

	// ATRIBUTOS
	private boolean debug = false;

	// SETTER
	/**
	 * @param debug Parametro que define el ambiente de ejecucion. Setea el modo
	 *              debug para cuando se ejecuta desde un entorno local.
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	// PUBLIC METHODS
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sap.aii.mapping.api.AbstractTransformation#transform(com.sap.aii.mapping.
	 * api.TransformationInput, com.sap.aii.mapping.api.TransformationOutput)
	 * 
	 * @param in
	 * 
	 * @param out
	 * 
	 * @throws StreamTransformationException Metodo de entrada para inciar el java
	 * mapping
	 */
	@Override
	public void transform(TransformationInput in, TransformationOutput out) throws StreamTransformationException {

		this.execute(in.getInputPayload().getInputStream(), out.getOutputPayload().getOutputStream());
	}

	/**
	 * @param in  InputStream con el xml de entrada del servicio
	 * @param out OutputSteam donde se envia el request http.
	 * @throws StreamTransformationException Procesa el request del servicio
	 *                                       generando un request http post
	 */
	public void execute(InputStream in, OutputStream out) throws StreamTransformationException {
		this.addInfoMessage("Iniciando Proceso de Mapeo: ConsultaxCartaGarantiaResponse...");
		try {
			GetConsultaxCartaGarantiaResponse rsp = null;
			Document documentIn = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			rsp = this.getConsultaxCartaGarantia(documentIn);
			Document documentOut = this.buildReponseMessage(rsp);
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut),
					new StreamResult(out));
			this.addInfoMessage("Finalizando Proceso de Mapeo: ConsultaxCartaGarantiaResponse...");
		} catch (Exception e) {
			this.addDebugMessage(e.toString());
			throw new StreamTransformationException(e.toString());
		}
	}

	private GetConsultaxCartaGarantiaResponse getConsultaxCartaGarantia(Document documentIn) {
		GetConsultaxCartaGarantiaResponse rsp;
		GetConsultaxCartaGarantiaRequest getConsultaxCartaGarantiaRequest = this.getConsultaxCartaGarantiaRequest();
		String valida = this.validarGetConsultaxCartaGarantiaRequest(getConsultaxCartaGarantiaRequest);
		if (valida == null) {
//			if (!dynamicConfiguration.get(NO_TRANSACCION).equals(" ")) {
			In278ResCG in278ResCG = null;
			NodeList resCG278 = documentIn.getElementsByTagName("InformacionAfiliadoResult");
			if (resCG278 != null && resCG278.getLength() > 0 && resCG278.getLength() <= 50
					&& resCG278.item(0).getNodeType() == Node.ELEMENT_NODE) {
				valida = this.validarGetConsultaxCartaGarantiaResponse(resCG278);
				if (valida == null) {
					in278ResCG = this.getIn278ResCG((Element) resCG278.item(0));
					for (int x = 0; x < resCG278.item(0).getChildNodes().getLength(); x++) {
						if (resCG278.item(0).getChildNodes().item(x).getNodeType() == Node.ELEMENT_NODE) {
							in278ResCG.addDetalle(this.getIn278ResCGDetalle(resCG278.item(0).getChildNodes().item(x)));
						}
					}
					In278ResCGServiceImpl dataOut = new In278ResCGServiceImpl();
					String msgXMLx12n = dataOut.beanToX12N(in278ResCG);
					rsp = this.getConsultaxCartaGarantiaResponse("0000", msgXMLx12n);
				} else {
					rsp = this.getConsultaxCartaGarantiaResponse(valida, "laprotectora");
				}

			} else if (resCG278 != null && resCG278.getLength() > 50) {
				rsp = this.getConsultaxCartaGarantiaResponse("1200", "laprotectora");
			} else {
				rsp = this.getConsultaxCartaGarantiaResponse("1300", "laprotectora");
			}
//			} else {
//				rsp = this.getConsultaxCartaGarantiaResponse("9999", "Error al traducir peticion");
//			}
		} else {
			rsp = this.getConsultaxCartaGarantiaResponse(valida, null);
		}
		return rsp;
	}

	private String validarGetConsultaxCartaGarantiaResponse(NodeList resCG278) {
		String res = null;
		Element eElement = null;
		NodeList nodeList = null;
		for (int i = 0; i < resCG278.getLength(); i++) {
			eElement = (Element) resCG278.item(i);
			nodeList = (NodeList) eElement.getElementsByTagName("Afiliado").item(0);
			res = "1300";
			if (nodeList != null) {
				for (int j = 0; j < nodeList.getLength(); j++) {
					if (nodeList.item(j).getNodeType() == Node.ELEMENT_NODE) {
						if (!nodeList.item(j).getTextContent().equals("")) {
							res = null;
							break;
						}
					}
				}
				if (res != null)
					break;

			} else {
				return res;
			}
		}
		return res;
	}

	private In278ResCG getIn278ResCG(Node consultaxCartaGarantia) {
		In278ResCG in278ResCG = new In278ResCG();
		if (consultaxCartaGarantia.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) consultaxCartaGarantia;
			in278ResCG.setNoTransaccion(this.getElementText(eElement, "transaccion"));
			in278ResCG.setIdRemitente("20006");
//			in278ResCG.setIdReceptor(dynamicConfiguration.get(ID_REMITENTE));
			in278ResCG.setFeTransaccion(this.generarFechaEnFormato("yyyyMMdd"));
			in278ResCG.setHoTransaccion(this.generarFechaEnFormato("hhmmss"));
			in278ResCG.setIdCorrelativo(this.getElementText(eElement, "correlativo"));
			in278ResCG.setIdTransaccion(this.getElementText(eElement, "iden_uni_Transac"));
			in278ResCG.setTiFinalidad("11");
			in278ResCG.setCaRemitente(this.getElementText(eElement, "calif_remi"));
			in278ResCG.setCaReceptor(this.getElementText(eElement, "calif_recep"));
			in278ResCG.setNuRucReceptor(this.getElementText(eElement, "RUC_recep"));
		}
		return in278ResCG;
	}

	public In278ResCGDetalle getIn278ResCGDetalle(Node consultaxCartaGarantiaDetalle) {
		In278ResCGDetalle in278ResCGDetalle = new In278ResCGDetalle();
		if (consultaxCartaGarantiaDetalle.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) consultaxCartaGarantiaDetalle;
			in278ResCGDetalle.setCaPaciente(this.getElementText(eElement, "calif_afi"));
			in278ResCGDetalle.setApPaternoPaciente(this.getElementText(eElement, "apepat_afi"));
			in278ResCGDetalle.setNoPaciente(this.getElementText(eElement, "nom_afi"));
			in278ResCGDetalle.setCoAfPaciente(this.getElementText(eElement, "cod_afi"));
			in278ResCGDetalle.setApMaternoPaciente(this.getElementText(eElement, "apemat_afi"));
			in278ResCGDetalle.setCoTiDoPaciente(this.getElementText(eElement, "cod_tip_doc_afi"));
			in278ResCGDetalle.setNuDoPaciente(this.getElementText(eElement, "doc_afi"));
			in278ResCGDetalle.setMonPago(this.getElementText(eElement, "monto_pagado"));
			in278ResCGDetalle.setTiCaContratante(this.getElementText(eElement, "tip_calif_contrat"));
			in278ResCGDetalle.setNoPaContratante(this.getElementText(eElement, "ape_pat_nom_contrat"));
			in278ResCGDetalle.setNoContratante(this.getElementText(eElement, "nom_contrat"));
			in278ResCGDetalle.setNoMaContratante(this.getElementText(eElement, "ape_mat_contrat"));
			in278ResCGDetalle.setTiDoContratante(this.getElementText(eElement, "tip_doc_contrat"));
			in278ResCGDetalle.setIdCaReContratante(this.getElementText(eElement, "ident_calif_ref_contrat"));
			in278ResCGDetalle.setNuCaReContratante(this.getElementText(eElement, "ref_indentif_contrat"));
			in278ResCGDetalle.setDeCarGarantia(this.getElementText(eElement, "descrip_carta_garant"));
			in278ResCGDetalle.setNuSoCarGarantia(this.getElementText(eElement, "nro_soli_carta_garant"));
			in278ResCGDetalle.setNuCarGarantia(this.getElementText(eElement, "nro_carta_garant"));
			in278ResCGDetalle.setVeCarGarantia(this.getElementText(eElement, "vers_carta_garant"));
			in278ResCGDetalle.setEsCarGarantia(this.getElementText(eElement, "est_carta_garant"));
			in278ResCGDetalle.setCoProducto(this.getElementText(eElement, "cod_prod"));
			in278ResCGDetalle.setCoProcedimiento(this.getElementText(eElement, "cod_proced"));
			in278ResCGDetalle.setDeProcedimiento(this.getElementText(eElement, "descrip_proced"));
			in278ResCGDetalle.setNuPlan(this.getElementText(eElement, "nro_plan"));
			in278ResCGDetalle.setTiPlanSalud(this.getElementText(eElement, "tip_plansalud"));
			in278ResCGDetalle.setCoMoneda(this.getElementText(eElement, "cod_moneda"));
			in278ResCGDetalle.setFeCarGarantia(this.getElementText(eElement, "fch_carta_garant"));
		}
		return in278ResCGDetalle;
	}

	private Document buildReponseMessage(GetConsultaxCartaGarantiaResponse response)
			throws ParserConfigurationException, MalformedURLException {
		Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		String docNS = "http://www.susalud.gob.pe/ws/siteds/schemas";
		Element rootElement = documentOut.createElementNS(docNS, "getConsultaxCartaGarantiaResponse");
		rootElement.setPrefix(null);
		documentOut.appendChild(rootElement);
		rootElement.appendChild(this.createTextElement(documentOut, "coError", response.getCoError()));
		rootElement.appendChild(this.createTextElement(documentOut, "txNombre", response.getTxNombre()));
		rootElement.appendChild(this.createTextElement(documentOut, "coIafa", response.getCoIafa()));
		rootElement.appendChild(this.createTextElement(documentOut, "txRespuesta", response.getTxRespuesta()));
		return documentOut;
	}

	private GetConsultaxCartaGarantiaResponse getConsultaxCartaGarantiaResponse(String coError, String txRespuesta) {
		GetConsultaxCartaGarantiaResponse x = new GetConsultaxCartaGarantiaResponse();
		x.setCoError(coError);
		x.setCoIafa("20006");
		x.setTxNombre("278_RES_CG");
		x.setTxRespuesta(txRespuesta);
		return x;
	}

	private String getElementText(Element eElement, String property) {

		NodeList propertyNode = eElement.getElementsByTagName(property);
		return propertyNode.getLength() > 0 ? propertyNode.item(0).getTextContent() : "";
	}

	private Element createTextElement(Document document, String tag, String value) {
		Element element = document.createElement(tag);
		element.appendChild(document.createTextNode((value != null) ? value : ""));
		return element;
	}

	public GetConsultaxCartaGarantiaRequest getConsultaxCartaGarantiaRequest() {
		GetConsultaxCartaGarantiaRequest getConsultaxCartaGarantiaRequest = new GetConsultaxCartaGarantiaRequest();
//		getConsultaxCartaGarantiaRequest.setTxNombre(dynamicConfiguration.get(TX_NOMBRE));
//		this.getTrace().addInfo("TX_NOMBRE: " + dynamicConfiguration.get(TX_NOMBRE));
//		getConsultaxCartaGarantiaRequest.setCoIafa(dynamicConfiguration.get(CO_IAFA));
//		this.getTrace().addInfo("CO_IAFA: " + dynamicConfiguration.get(CO_IAFA));
//		getConsultaxCartaGarantiaRequest.setTxPeticion(dynamicConfiguration.get(TX_PETICION));
//		this.getTrace().addInfo("TX_PETICION: " + dynamicConfiguration.get(TX_PETICION));
//		getConsultaxCartaGarantiaRequest.setCoExcepcion(dynamicConfiguration.get(CO_EXCEPCION));
//		this.getTrace().addInfo("CO_EXCEPCION: " + dynamicConfiguration.get(CO_EXCEPCION));
		return getConsultaxCartaGarantiaRequest;

	}

	private String validarGetConsultaxCartaGarantiaRequest(GetConsultaxCartaGarantiaRequest request) {
		String rsp = null;
		if (request == null) {
			rsp = "0010";
		} else {
//			if (request.getTxNombre().isEmpty()) {
//				rsp = "0300";
//			if (!request.getTxNombre().equalsIgnoreCase("278_SOL_CG")) {
//				rsp = "0310";
//			} else if (request.getCoIafa().isEmpty()) {
//				rsp = "0400";
//			} else if (!request.getCoIafa().equalsIgnoreCase("20006")) {
//				rsp = "0410";
//			} else if (request.getTxPeticion().isEmpty()) {
//				rsp = "0500";
//			} else if (request.getCoExcepcion().isEmpty() || !request.getCoExcepcion().equalsIgnoreCase("0000")) {
//				rsp = "1004";
//			}
		}
		return rsp;
	}

	private String generarFechaEnFormato(String formato) {
		String fechaConFormato = "";
		// Create an instance of SimpleDateFormat used for formatting
		// the string representation of date (month/day/year)
		DateFormat df = new SimpleDateFormat(formato);

		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();
		// Using DateFormat format method we can create a string
		// representation of a date with the defined format.
		fechaConFormato = df.format(today);
		return fechaConFormato;
	}

	/**
	 * @param msg Mesaje a loggear Envia los mensajes a la consola o el trace de PI
	 *            dependiendo si se encuentra en entorno de pruebas o no.
	 */
	private void addInfoMessage(String msg) {
		if (this.debug) {
			System.out.println(msg);
		} else {
			this.getTrace().addInfo(msg);
		}
	}

	/**
	 * @param msg Mesaje a loggear Envia los mensajes a la consola o el trace de PI
	 *            dependiendo si se encuentra en entorno de pruebas o no.
	 */
	private void addDebugMessage(String msg) {
		if (this.debug) {
			System.out.println(msg);
		} else {
			this.getTrace().addDebugMessage(msg);
		}
	}

}