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

import pe.auna.siteds.beans.GetConsultaAsegNomResponse;
import pe.gob.susalud.jr.transaccion.susalud.bean.InConNom271;
import pe.gob.susalud.jr.transaccion.susalud.bean.InConNom271Detalle;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.ConNom271ServiceImpl;

/**
 * @author Bryan Vargas
 * @version 2.1
 */
public class ConsultaAsegNomResponse extends AbstractTransformation {

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
        	GetConsultaAsegNomResponse rsp = this.getConsultaAsegCod(documentIn);
        	Document documentOut = this.buildReponseMessage(rsp);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }

    private GetConsultaAsegNomResponse getConsultaAsegCod(Document documentIn) {
    	String codigoError = this.getElementText(documentIn, "CoError");
    	GetConsultaAsegNomResponse rsp;
    	
    	if (codigoError.equals("0000")) {
    		InConNom271 inConNom271 = null;
    		NodeList conCod271 = documentIn.getElementsByTagName("TxRespuesta");

    		if (conCod271.getLength() > 0 && conCod271.item(0).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) conCod271.item(0);
    			inConNom271 = this.getInConNom271(eElement);
    			NodeList conNom271Detalle = eElement.getElementsByTagName("InConNom271Detalle");

    			for (int i = 0; i < conNom271Detalle.getLength(); i++) {
    				inConNom271.addDetalle(this.getInConNom271Detalle(conNom271Detalle.item(i)));
    			}
    		}
    		
            ConNom271ServiceImpl dataOut = new ConNom271ServiceImpl();
            String msgXMLx12n = dataOut.beanToX12N(inConNom271);
            rsp = this.getConsultaAsegNomResponse(codigoError, msgXMLx12n);
    	} else {
    		rsp = this.getConsultaAsegNomResponse(codigoError, null);
    	}
    	return rsp;
    }
    
    private InConNom271 getInConNom271(Element eElement) {
    	InConNom271 inConNom271 = new InConNom271();
		inConNom271.setNoTransaccion(this.getElementText(eElement, "NoTransaccion"));
        inConNom271.setIdRemitente(this.getElementText(eElement, "IdRemitente"));
        inConNom271.setIdReceptor(this.getElementText(eElement, "IdReceptor"));
        inConNom271.setFeTransaccion(this.getElementText(eElement, "FeTransaccion"));
        inConNom271.setHoTransaccion(this.getElementText(eElement, "HoTransaccion"));
        inConNom271.setIdCorrelativo(this.getElementText(eElement, "IdCorrelativo"));
        inConNom271.setIdTransaccion(this.getElementText(eElement, "IdTransaccion")); 
        inConNom271.setTiFinalidad(this.getElementText(eElement, "TiFinalidad")); 
        inConNom271.setCaRemitente(this.getElementText(eElement, "CaRemitente"));
        inConNom271.setCaReceptor(this.getElementText(eElement, "CaReceptor"));
        inConNom271.setNuRucReceptor(this.getElementText(eElement, "NuRucReceptor"));
    	return inConNom271;
    }
    
    public InConNom271Detalle getInConNom271Detalle(Node consultaAsegCod) {
    	InConNom271Detalle inConNom271Detalle = new InConNom271Detalle();
		if (consultaAsegCod.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) consultaAsegCod;
	        inConNom271Detalle.setCaPaciente(this.getElementText(eElement, "CaPaciente"));
	        inConNom271Detalle.setApPaternoPaciente(this.getElementText(eElement, "ApPaternoPaciente"));
	        inConNom271Detalle.setNoPaciente(this.getElementText(eElement, "NoPaciente"));
	        inConNom271Detalle.setCoAfPaciente(this.getElementText(eElement, "CoAfPaciente"));
	        inConNom271Detalle.setApMaternoPaciente(this.getElementText(eElement, "ApMaternoPaciente"));
	        inConNom271Detalle.setCoEsPaciente(this.getElementText(eElement, "CoEsPaciente"));
	        inConNom271Detalle.setTiDoPaciente(this.getElementText(eElement, "TiDoPaciente"));
	        inConNom271Detalle.setNuDoPaciente(this.getElementText(eElement, "NuDoPaciente"));
	        inConNom271Detalle.setCoProducto(this.getElementText(eElement, "CoProducto"));
	        inConNom271Detalle.setCoParentesco(this.getElementText(eElement, "CoParentesco"));
	        inConNom271Detalle.setFeNacimiento(this.getElementText(eElement, "FeNacimiento"));
	        inConNom271Detalle.setTiDoContratante(this.getElementText(eElement, "TiDoContratante"));
	        inConNom271Detalle.setGenero(this.getElementText(eElement, "Genero")); //10s
		}
		return inConNom271Detalle;
    }
    
    private Document buildReponseMessage(GetConsultaAsegNomResponse response) throws ParserConfigurationException, MalformedURLException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();    	
    	String docNS = "http://www.susalud.gob.pe/ws/siteds/schemas";
        Element rootElement = documentOut.createElementNS(docNS, "getConsultaAsegNomResponse");
        rootElement.setPrefix(null);
    	documentOut.appendChild(rootElement);
    	rootElement.appendChild(this.createTextElement(documentOut, "coError", response.getCoError()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txNombre", response.getTxNombre()));
    	rootElement.appendChild(this.createTextElement(documentOut, "coIafa", response.getCoIafa()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txRespuesta", response.getTxRespuesta()));
    	return documentOut;
    }

    private GetConsultaAsegNomResponse getConsultaAsegNomResponse(String coError, String txRespuesta) {
        GetConsultaAsegNomResponse x = new GetConsultaAsegNomResponse();
        x.setCoError(coError);
        x.setCoIafa("20006");
        x.setTxNombre("271_CON_NOM");
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