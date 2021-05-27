package pe.auna.siteds.mapping;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sap.aii.mapping.api.AbstractTransformation;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;

import pe.auna.siteds.beans.GetConsultaEntVinculadaRequest;
import pe.gob.susalud.jr.transaccion.susalud.bean.InConEntVinc278;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.ConEntVinc278ServiceImpl;

/**
 * @author Bryan Vargas
 * @version 2.1
 */
public class ConsultaEntVinculadaRequest extends AbstractTransformation {

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

    @Override
    public void transform(TransformationInput in, TransformationOutput out) throws StreamTransformationException {
        this.execute(in.getInputPayload().getInputStream(), out.getOutputPayload().getOutputStream());
    }

    public void execute(InputStream in, OutputStream out) throws StreamTransformationException {
    	this.addInfoMessage("Iniciando Proceso de Mapeo: ConsultaEntVinculadaRequest...");

    	try {
    		Document documentIn = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
    		GetConsultaEntVinculadaRequest getConsultaEntVinculadaRequest = this.getConsultaEntVinculada(documentIn);
    		Document documentOut = this.buildRequestMessage(getConsultaEntVinculadaRequest);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        	this.addInfoMessage("Finalizando Proceso de Mapeo: ConsultaEntVinculadaRequest...");
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }
    
    public GetConsultaEntVinculadaRequest getConsultaEntVinculada(Document documentIn) {
    	String codigoExcepcion = this.getElementText(documentIn, "coExcepcion");
    	GetConsultaEntVinculadaRequest getConsultaEntVinculadaRequest = new GetConsultaEntVinculadaRequest();
    	getConsultaEntVinculadaRequest.setTxNombre(this.getElementText(documentIn, "txNombre"));
    	getConsultaEntVinculadaRequest.setCoIafa(this.getElementText(documentIn, "coIafa"));
    	getConsultaEntVinculadaRequest.setTxPeticion(codigoExcepcion.equals("0000") ? this.getElementText(documentIn, "txPeticion") : ""); 
    	getConsultaEntVinculadaRequest.setCoExcepcion(this.getElementText(documentIn, "coExcepcion"));
		return getConsultaEntVinculadaRequest;
    }

    private Document buildRequestMessage(GetConsultaEntVinculadaRequest request) throws ParserConfigurationException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    	String docNS = "http://auna.com/X12N/IntegracionAutorizaciones";
        Element rootElement = documentOut.createElementNS(docNS, "ConsultaEntVinculadaRequest");
    	rootElement.setPrefix("ns0");
    	documentOut.appendChild(rootElement);
    	rootElement.appendChild(this.createTextElement(documentOut, "TxNombre", request.getTxNombre()));
    	rootElement.appendChild(this.createTextElement(documentOut, "CoIafa", request.getCoIafa()));
    	rootElement.appendChild(this.buildPeticion(documentOut, request.getTxPeticion()));
    	rootElement.appendChild(this.createTextElement(documentOut, "CoExcepcion", request.getCoExcepcion()));
    	return documentOut;
    }
    
    private Element buildPeticion(Document documentOut, String peticion) {
    	Element txPeticion = documentOut.createElement("TxPeticion");
        ConEntVinc278ServiceImpl dataIn = new ConEntVinc278ServiceImpl();
        InConEntVinc278 beanIn = dataIn.x12NToBean(peticion);
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoTransaccion", beanIn.getNoTransaccion()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdRemitente", beanIn.getIdRemitente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdReceptor", beanIn.getIdReceptor()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "FeTransaccion", beanIn.getFeTransaccion()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "HoTransaccion", beanIn.getHoTransaccion()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdCorrelativo", beanIn.getIdCorrelativo()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdTransaccion", beanIn.getIdTransaccion()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "TiFinalidad", beanIn.getTiFinalidad()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CaIPRESS", beanIn.getCaIPRESS()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoIPRESS", beanIn.getNoIPRESS()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "TiDoIPRESS", beanIn.getNoIPRESS()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuRucIPRESS", beanIn.getNuRucIPRESS()));
    	return txPeticion;
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
     * @param msg Mesaje a loggear Envia los mensajes a la consola o el trace de
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