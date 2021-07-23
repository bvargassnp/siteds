package pe.auna.siteds.mapping;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

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
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;

import pe.auna.siteds.beans.GetConsultaDatosAdiResponse;
import pe.gob.susalud.jr.transaccion.susalud.bean.In271ConDtad;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.In271ConDtadServiceImpl;


public class ConsultaDatosAdiResponse  extends AbstractTransformation {

    // ATRIBUTOS	
    private boolean debug = false;

    // SETTER
    /**
     * @param debug	Parametro que define el ambiente de ejecucion. Setea el modo
     * debug para cuando se ejecuta desde un entorno local.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    // PUBLIC METHODS
    /* (non-Javadoc)
	 * @see com.sap.aii.mapping.api.AbstractTransformation#transform(com.sap.aii.mapping.api.TransformationInput, com.sap.aii.mapping.api.TransformationOutput)
	 * @param in
	 * @param out
	 * @throws StreamTransformationException
	 * Metodo de entrada para inciar el java mapping
     */
    @Override
    public void transform(TransformationInput in, TransformationOutput out) throws StreamTransformationException {
        this.execute(in.getInputPayload().getInputStream(), out.getOutputPayload().getOutputStream());
    }

    /**
     * @param in	InputStream con el xml de entrada del servicio
     * @param out	OutputSteam donde se envia el request http.
     * @throws StreamTransformationException
     *
     * Procesa el request del servicio generando un request http post
     */
    public void execute(InputStream in, OutputStream out) throws StreamTransformationException {
        try {
        	this.addInfoMessage("Iniciando Proceso de Mapeo...");
        	Document documentIn = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        	GetConsultaDatosAdiResponse rsp = this.getConsultaDatosAdi(documentIn);
        	Document documentOut = this.buildReponseMessage(rsp);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }

    private GetConsultaDatosAdiResponse getConsultaDatosAdi(Document documentIn) {
    	String codigoError = this.getElementText(documentIn, "CoError");
    	
    	GetConsultaDatosAdiResponse rsp;
    	
    	if (codigoError.equals("0000")) {
    		In271ConDtad in271ConDtad = null;
    		NodeList conDtad271 = documentIn.getElementsByTagName("TxRespuesta");

    		if (conDtad271.getLength() > 0 && conDtad271.item(0).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) conDtad271.item(0);
				in271ConDtad = this.getIn271ConDtad(eElement);
    		}
    		
    		In271ConDtadServiceImpl dataOut = new In271ConDtadServiceImpl();
            String msgXMLx12n = dataOut.beanToX12N(in271ConDtad);
            rsp = this.getConsultaAsegNomResponse(codigoError, msgXMLx12n);
    	} else {
    		rsp = this.getConsultaAsegNomResponse(codigoError, null);
    	}
    	return rsp;
    }
    
    private In271ConDtad getIn271ConDtad(Element eElement) {
    	In271ConDtad in271ConDtad = new In271ConDtad();
    	in271ConDtad.setNoTransaccion(this.getElementText(eElement, "NoTransaccion")); 
    	in271ConDtad.setIdRemitente(this.getElementText(eElement, "IdRemitente")); 
    	in271ConDtad.setIdReceptor(this.getElementText(eElement, "IdReceptor")); 
    	in271ConDtad.setFeTransaccion(this.getElementText(eElement, "FeTransaccion")); 
    	in271ConDtad.setHoTransaccion(this.getElementText(eElement, "HoTransaccion")); 
    	in271ConDtad.setIdCorrelativo(this.getElementText(eElement, "IdCorrelativo")); 
    	in271ConDtad.setIdTransaccion(this.getElementText(eElement, "IdTransaccion")); 
    	in271ConDtad.setTiFinalidad(this.getElementText(eElement, "TiFinalidad"));  
    	in271ConDtad.setCaRemitente(this.getElementText(eElement, "CaRemitente"));
    	in271ConDtad.setCaReceptor(this.getElementText(eElement, "CaReceptor")); 
    	in271ConDtad.setNuRucReceptor(this.getElementText(eElement, "NuRucReceptor"));    	
    	in271ConDtad.setCaPaciente(this.getElementText(eElement, "CaPaciente"));
    	in271ConDtad.setApPaternoPaciente(this.getElementText(eElement, "ApPaternoPaciente"));
    	in271ConDtad.setNoPaciente(this.getElementText(eElement, "NoPaciente"));
    	in271ConDtad.setCoAfPaciente(this.getElementText(eElement, "CoAfPaciente"));
    	in271ConDtad.setApMaternoPaciente(this.getElementText(eElement, "ApMaternoPaciente"));
    	in271ConDtad.setDeDirPaciente1(this.getElementText(eElement, "DeDirPaciente1"));
    	in271ConDtad.setDeDirPaciente2(this.getElementText(eElement, "DeDirPaciente2"));
    	in271ConDtad.setCoUbigeoPaciente(this.getElementText(eElement, "CoUbigeoPaciente"));
    	in271ConDtad.setNoContacto(this.getElementText(eElement, "NoContacto"));
    	in271ConDtad.setEmContacto(this.getElementText(eElement, "EmContacto"));
    	in271ConDtad.setNuTeContacto(this.getElementText(eElement, "NuTeContacto"));
    	in271ConDtad.setTiCaCalificador(this.getElementText(eElement, "TiCaCalificador"));
    	in271ConDtad.setApPaNoEmCalificador(this.getElementText(eElement, "ApPaNoEmCalificador"));
    	in271ConDtad.setNoEmCalificador(this.getElementText(eElement, "NoEmCalificador"));
    	in271ConDtad.setApMaCalificador(this.getElementText(eElement, "ApMaCalificador"));
    	in271ConDtad.setNuControl(this.getElementText(eElement, "NuControl"));
    	in271ConDtad.setNuControlST(this.getElementText(eElement, "NuControlST"));
    	return in271ConDtad;
    }
    
  
    
    private Document buildReponseMessage(GetConsultaDatosAdiResponse response) throws ParserConfigurationException, MalformedURLException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    	
    	String docNS = "http://www.susalud.gob.pe/ws/siteds/schemas";
        Element rootElement = documentOut.createElementNS(docNS, "getConsultaDatosAdiResponse");
        rootElement.setPrefix(null);
    	documentOut.appendChild(rootElement);
    	rootElement.appendChild(this.createTextElement(documentOut, "coError", response.getCoError()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txNombre", response.getTxNombre()));
    	rootElement.appendChild(this.createTextElement(documentOut, "coIafa", response.getCoIafa()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txRespuesta", response.getTxRespuesta()));
    	return documentOut;
    }

    private GetConsultaDatosAdiResponse getConsultaAsegNomResponse(String coError, String txRespuesta) {
    	GetConsultaDatosAdiResponse x = new GetConsultaDatosAdiResponse();
        x.setCoError(coError);
        x.setCoIafa("20006");
        x.setTxNombre("271_CON_DTAD");
        x.setTxRespuesta(txRespuesta);
        return x;
    }

    private String getElementText(Element eElement, String property) {
    	NodeList propertyNode = eElement.getElementsByTagName(property);
    	return propertyNode.getLength() > 0 ?  propertyNode.item(0).getTextContent() : "";
    }

    private String getElementText(Document document, String property) {
    	NodeList propertyNode = document.getElementsByTagName(property);
    	return propertyNode.getLength() > 0 ?  propertyNode.item(0).getTextContent() : "";
    }

    private Element createTextElement(Document document, String tag, String value) {
    	Element element = document.createElement(tag);
    	element.appendChild(document.createTextNode((value != null) ? value : ""));
    	return element;
    }

    /**
     * @param msg	Mesaje a loggear Envia los mensajes a la consola o el trace de
     * PI dependiendo si se encuentra en entorno de pruebas o no.
     */
    private void addInfoMessage(String msg) {
        if (this.debug) {
            System.out.println(msg);
        } else {
            getTrace().addInfo(msg);
        }
    }

    /**
     * @param msg	Mesaje a loggear Envia los mensajes a la consola o el trace de
     * PI dependiendo si se encuentra en entorno de pruebas o no.
     */
    private void addDebugMessage(String msg) {
        if (this.debug) {
            System.out.println(msg);
        } else {
            getTrace().addDebugMessage(msg);
        }
    }

}
