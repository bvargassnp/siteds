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

import pe.auna.siteds.beans.GetNumAutorizacionResponse;
import pe.gob.susalud.jr.transaccion.susalud.bean.In997ResAut;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.In997ResAutServiceImpl;

public class NumAutorizacionResponse extends AbstractTransformation {

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
     * @throws StreamTransformationException Procesa el request del servicio
     * generando un request http post
     */
    public void execute(InputStream in, OutputStream out) throws StreamTransformationException {
        try {
            this.addInfoMessage("... NumAutorizacion Response ...  \n Iniciando Proceso de Mapeo ...");
        	Document documentIn = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        	GetNumAutorizacionResponse rsp = this.getNumAutorizacionResponse(documentIn);
        	Document documentOut = this.buildReponseMessage(rsp);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }

    private GetNumAutorizacionResponse getNumAutorizacionResponse(Document documentIn) {
    	String codigoError = this.getElementText(documentIn, "CoError");
    	GetNumAutorizacionResponse rsp;
    	if (codigoError.equals("0000")) {
    		NodeList autorizaciones = documentIn.getElementsByTagName("TxRespuesta");
    		In997ResAut in997ResAut = null;
    		
			for (int i = 0; i < autorizaciones.getLength(); i++) {
				in997ResAut = this.getIn997ResAut(autorizaciones.item(i));
    		}
			
            In997ResAutServiceImpl dataOut = new In997ResAutServiceImpl();
            String msgXMLx12n = dataOut.beanToX12N(in997ResAut);
            rsp = this.getNumAutorizacionResponse("0000", msgXMLx12n);
    	} else {
    		rsp = this.getNumAutorizacionResponse(codigoError, null);
    	}
    	return rsp;
    }
    
    private In997ResAut getIn997ResAut(Node autorizacion) {
    	In997ResAut in997ResAut = new In997ResAut();
		if (autorizacion.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) autorizacion;
	    	in997ResAut.setNoTransaccion(this.getElementText(eElement, "NoTransaccion"));
	    	in997ResAut.setIdRemitente(this.getElementText(eElement, "IdRemitente"));
	    	in997ResAut.setIdReceptor(this.getElementText(eElement, "IdReceptor")); 
	    	in997ResAut.setFeTransaccion(this.getElementText(eElement, "FeTransaccion")); 
	    	in997ResAut.setHoTransaccion(this.getElementText(eElement, "HoTransaccion")); 
	    	in997ResAut.setIdCorrelativo(this.getElementText(eElement, "IdCorrelativo")); 
	        in997ResAut.setIdTransaccion(this.getElementText(eElement, "IdTransaccion")); 
	        in997ResAut.setNuAutorizacion(this.getElementText(eElement, "NuAutorizacion")); 
	        in997ResAut.setCoSeguridad(this.getElementText(eElement, "CoSeguridad")); 
	        in997ResAut.setCoError(this.getElementText(eElement, "CoError"));
		}
    	return in997ResAut;
    }
    
    private Document buildReponseMessage(GetNumAutorizacionResponse response) throws ParserConfigurationException, MalformedURLException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    	String docNS = "http://www.susalud.gob.pe/ws/siteds/schemas";
        Element rootElement = documentOut.createElementNS(docNS, "getNumAutorizacionResponse");
        rootElement.setPrefix(null);
    	documentOut.appendChild(rootElement);
    	rootElement.appendChild(this.createTextElement(documentOut, "coError", response.getCoError()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txNombre", response.getTxNombre()));
    	rootElement.appendChild(this.createTextElement(documentOut, "coIafa", response.getCoIafa()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txRespuesta", response.getTxRespuesta()));
    	return documentOut;
    }

    private GetNumAutorizacionResponse getNumAutorizacionResponse(String coError, String txRespuesta) {
        GetNumAutorizacionResponse x = new GetNumAutorizacionResponse();
        x.setCoError(coError);
        x.setCoIafa("20006");
        x.setTxNombre("997_RES_AUT");
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
