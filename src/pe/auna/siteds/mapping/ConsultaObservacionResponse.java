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
import pe.auna.siteds.beans.GetConsultaObservacionResponse;
import pe.gob.susalud.jr.transaccion.susalud.bean.In271ConObs;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.In271ConObsServiceImpl;

public class ConsultaObservacionResponse extends AbstractTransformation {

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
        	GetConsultaObservacionResponse rsp = this.getConsultaObservacion(documentIn);
        	Document documentOut = this.buildReponseMessage(rsp);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }

    private GetConsultaObservacionResponse getConsultaObservacion(Document documentIn) {
    	String codigoError = this.getElementText(documentIn, "CoError");
    	String rptObs = this.getElementText(documentIn, "RptObs");
    	GetConsultaObservacionResponse rsp;    	
    	
    	if (codigoError.equals("0000")) {
    		In271ConObs in271ConObs = null;
    		NodeList conObs271 = documentIn.getElementsByTagName("TxRespuesta");

    		if (conObs271.getLength() > 0 && conObs271.item(0).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) conObs271.item(0);
				in271ConObs = this.getIn271ConObs(eElement);
    		}
    		
    		In271ConObsServiceImpl dataOut = new In271ConObsServiceImpl();
            String msgXMLx12n = dataOut.beanToX12N(in271ConObs);
            rsp = this.getConsultaObservacionResponse(codigoError, msgXMLx12n, rptObs);
    	} else {
    		rsp = this.getConsultaObservacionResponse(codigoError, null, rptObs);
    	}
    	return rsp;
    }
    
    private In271ConObs getIn271ConObs(Element eElement) {
    	In271ConObs in271ConObs = new In271ConObs();
    	in271ConObs.setNoTransaccion(this.getElementText(eElement, "NoTransaccion"));
    	in271ConObs.setIdRemitente(this.getElementText(eElement, "IdRemitente"));
    	in271ConObs.setIdReceptor(this.getElementText(eElement, "IdReceptor")); 
        in271ConObs.setFeTransaccion(this.getElementText(eElement, "FeTransaccion"));
        in271ConObs.setHoTransaccion(this.getElementText(eElement, "HoTransaccion")); 
        in271ConObs.setIdCorrelativo(this.getElementText(eElement, "IdCorrelativo")); 
        in271ConObs.setIdTransaccion(this.getElementText(eElement, "IdTransaccion")); 
        in271ConObs.setTiFinalidad(this.getElementText(eElement, "TiFinalidad"));  
        in271ConObs.setCaRemitente(this.getElementText(eElement, "CaRemitente"));
        in271ConObs.setCaReceptor(this.getElementText(eElement, "CaReceptor")); 
        in271ConObs.setNuRucReceptor(this.getElementText(eElement, "NuRucReceptor"));        
        in271ConObs.setCaPaciente(this.getElementText(eElement, "CaPaciente")); 
        in271ConObs.setApPaternoPaciente(this.getElementText(eElement, "ApPaternoPaciente"));         
        in271ConObs.setNoPaciente(this.getElementText(eElement, "NoPaciente"));    
        in271ConObs.setCoAfPaciente(this.getElementText(eElement, "CoAfPaciente"));        
        in271ConObs.setApMaternoPaciente(this.getElementText(eElement, "ApMaternoPaciente"));        
        in271ConObs.setCoSubCobertura(this.getElementText(eElement, "CoSubCobertura"));        
        in271ConObs.setTeMsgLibre1(this.getElementText(eElement, "TeMsgLibre1"));         
        in271ConObs.setTeMsgLibre2(this.getElementText(eElement, "TeMsgLibre2"));
        
    	return in271ConObs;
    }
    
    
    private Document buildReponseMessage(GetConsultaObservacionResponse response) throws ParserConfigurationException, MalformedURLException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    	
    	String docNS = "http://www.susalud.gob.pe/ws/siteds/schemas";
        Element rootElement = documentOut.createElementNS(docNS, "getConsultaObservacionResponse");
        rootElement.setPrefix(null);
    	documentOut.appendChild(rootElement);
    	rootElement.appendChild(this.createTextElement(documentOut, "coError", response.getCoError()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txNombre", response.getTxNombre()));
    	rootElement.appendChild(this.createTextElement(documentOut, "coIafa", response.getCoIafa()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txRespuesta", response.getTxRespuesta()));
    	return documentOut;
    }

    private GetConsultaObservacionResponse getConsultaObservacionResponse(String coError, String txRespuesta, String rptObs) {
    	GetConsultaObservacionResponse x = new GetConsultaObservacionResponse();
        x.setCoError(coError);
        x.setCoIafa("20006");
        x.setTxNombre("271_CON_OBS");
        x.setTxRespuesta(txRespuesta);
        x.setRptObs(rptObs);
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