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

import pe.auna.siteds.beans.GetConsultaProcRequest;
import pe.gob.susalud.jr.transaccion.susalud.bean.InConAse270;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.ConAse270ServiceImpl;


/**
 * @author Bryan Vargas
 * @version 2.1 
 *
 */
public class ConsultaProcRequest extends AbstractTransformation {

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
    	this.addInfoMessage("Iniciando Proceso de Mapeo: ConsultaProcRequest...");

    	try {
    		Document documentIn = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
    		GetConsultaProcRequest getConsultaProcRequest = this.getConsultaProcRequest(documentIn);
    		Document documentOut = this.buildRequestMessage(getConsultaProcRequest);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        	this.addInfoMessage("Finalizando Proceso de Mapeo: ConsultaProcRequest...");
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }

    public GetConsultaProcRequest getConsultaProcRequest(Document documentIn) {
    	String codigoExcepcion = this.getElementText(documentIn, "coExcepcion");
    	GetConsultaProcRequest getConsultaProcRequest = new GetConsultaProcRequest();
    	getConsultaProcRequest.setTxNombre(this.getElementText(documentIn, "txNombre")); 
    	getConsultaProcRequest.setCoIafa(this.getElementText(documentIn, "coIafa"));
    	getConsultaProcRequest.setTxPeticion(codigoExcepcion.equals("0000") ? this.getElementText(documentIn, "txPeticion") : "");
    	getConsultaProcRequest.setCoExcepcion(this.getElementText(documentIn, "coExcepcion"));
		return getConsultaProcRequest;
    }
    
    private Document buildRequestMessage(GetConsultaProcRequest request) throws ParserConfigurationException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    	String docNS = "http://auna.com/X12N/IntegracionAutorizaciones";
        Element rootElement = documentOut.createElementNS(docNS, "ConsultaProcRequest");
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
        ConAse270ServiceImpl dataIn = new ConAse270ServiceImpl();
        InConAse270 beanIn = dataIn.x12NToBean(peticion);
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoTransaccion", beanIn.getNoTransaccion()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdRemitente", beanIn.getIdRemitente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdReceptor", beanIn.getIdReceptor()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "FeTransaccion", beanIn.getFeTransaccion()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "HoTransaccion", beanIn.getHoTransaccion()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdCorrelativo", beanIn.getIdCorrelativo()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdTransaccion", beanIn.getIdTransaccion()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "TiFinalidad", beanIn.getTiFinalidad()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CaRemitente", beanIn.getCaRemitente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuRucRemitente", beanIn.getNuRucRemitente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "TxRequest", beanIn.getTxRequest()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CaReceptor", beanIn.getCaReceptor()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CaPaciente", beanIn.getCaPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "ApPaternoPaciente", beanIn.getApPaternoPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoPaciente", beanIn.getNoPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoAfPaciente", beanIn.getCoAfPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "ApMaternoPaciente", beanIn.getApMaternoPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "TiDocumento", beanIn.getTiDocumento()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuDocumento", beanIn.getNuDocumento()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoProducto", beanIn.getCoProducto()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "DeProducto", beanIn.getDeProducto()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoInProducto", beanIn.getCoInProducto()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuCobertura", beanIn.getNuCobertura()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "DeCobertura", beanIn.getDeCobertura()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CaServicio", beanIn.getCaServicio()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoCalservicio", beanIn.getCoCalservicio()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "BeMaxInicial", beanIn.getBeMaxInicial()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoTiCobertura", beanIn.getCoTiCobertura()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoSuTiCobertura", beanIn.getCoSuTiCobertura()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoAplicativoTx", beanIn.getCoAplicativoTx()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoEspecialidad", beanIn.getCoEspecialidad()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoParentesco", beanIn.getCoParentesco()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuPlan", beanIn.getNuPlan()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuAutOrigen", beanIn.getNuAutOrigen()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "TiCaContratante", beanIn.getTiCaContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoPaContratante", beanIn.getNoPaContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoContratante", beanIn.getNoContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoMaContratante", beanIn.getNoMaContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "TiDoContratante", beanIn.getTiDoContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdReContratante", beanIn.getIdReContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoReContratante", beanIn.getCoReContratante()));
    	
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