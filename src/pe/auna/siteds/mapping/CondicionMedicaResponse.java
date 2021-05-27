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

import pe.auna.siteds.beans.GetCondicionMedicaResponse;
import pe.gob.susalud.jr.transaccion.susalud.bean.InConMed271;
import pe.gob.susalud.jr.transaccion.susalud.bean.InConMed271Detalle;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.ConMed271ServiceImpl;
/**
 * @author Bryan Vargas
 * @version 2.1 
 *
 */
public class CondicionMedicaResponse extends AbstractTransformation {


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
        	this.addInfoMessage("Iniciando Proceso de Mapeo: CondicionMedicaResponse...");
        	Document documentIn = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        	GetCondicionMedicaResponse rsp = this.getCondicionMedica(documentIn);
        	Document documentOut = this.buildReponseMessage(rsp);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        	this.addInfoMessage("Finalizando Proceso de Mapeo: CondicionMedicaResponse...");
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }
    
    private GetCondicionMedicaResponse getCondicionMedica(Document documentIn) {
    	String codigoError = this.getElementText(documentIn, "CoError");
    	GetCondicionMedicaResponse rsp;
    	
    	if (codigoError.equals("0000")) {
    		InConMed271 inConMed271 = null;
    		NodeList conMed271 = documentIn.getElementsByTagName("TxRespuesta");

    		if (conMed271.getLength() > 0 && conMed271.item(0).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) conMed271.item(0);
				inConMed271 = this.getInConMed271(eElement);
    			NodeList conMed271Detalle = eElement.getElementsByTagName("InConMed271Detalle");
    			for (int i = 0; i < conMed271Detalle.getLength(); i++) {
    				inConMed271.addDetalle(this.getInConMed271Detalle(conMed271Detalle.item(i)));
    			}
    		}
    		
    		ConMed271ServiceImpl dataOut = new ConMed271ServiceImpl();
            String msgXMLx12n = dataOut.beanToX12N(inConMed271);
            rsp = this.getCondicionMedicaResponse(codigoError, msgXMLx12n);
    	} else {
    		rsp = this.getCondicionMedicaResponse(codigoError, null);
    	}
    	return rsp;
    }
    
    private InConMed271 getInConMed271(Element eElement) {
    	InConMed271 inConMed271 = new InConMed271();
		inConMed271.setNoTransaccion(this.getElementText(eElement, "NoTransaccion"));       
        inConMed271.setIdRemitente(this.getElementText(eElement, "IdRemitente"));
        inConMed271.setIdReceptor(this.getElementText(eElement, "IdReceptor")); 
        inConMed271.setFeTransaccion(this.getElementText(eElement, "FeTransaccion"));
        inConMed271.setHoTransaccion(this.getElementText(eElement, "HoTransaccion"));
        inConMed271.setIdCorrelativo(this.getElementText(eElement, "IdCorrelativo"));
        inConMed271.setIdTransaccion(this.getElementText(eElement, "IdTransaccion"));
        inConMed271.setTiFinalidad(this.getElementText(eElement, "TiFinalidad"));  
        inConMed271.setCaRemitente(this.getElementText(eElement, "CaRemitente"));         
        inConMed271.setCaReceptor(this.getElementText(eElement, "CaReceptor"));        
        inConMed271.setNuRucReceptor(this.getElementText(eElement, "NuRucReceptor"));        
        inConMed271.setCaPaciente(this.getElementText(eElement, "CaPaciente"));         
        inConMed271.setApPaternoPaciente(this.getElementText(eElement, "ApPaternoPaciente"));         
        inConMed271.setNoPaciente(this.getElementText(eElement, "NoPaciente"));
        inConMed271.setCoPaciente(this.getElementText(eElement, "CoPaciente"));
        inConMed271.setApPaternoPaciente(this.getElementText(eElement, "ApMaternoPaciente"));        
    	return inConMed271;
    }
    
    public InConMed271Detalle getInConMed271Detalle(Node consultaAsegCod) {
    	InConMed271Detalle inConMed271Detalle = new InConMed271Detalle();
		if (consultaAsegCod.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) consultaAsegCod;
			inConMed271Detalle.setCoSeCIE10(this.getElementText(eElement, "CoSeCIE10"));			
			inConMed271Detalle.setCoRestriccion(this.getElementText(eElement, "CoRestriccion")); 	        
			inConMed271Detalle.setDePreexistencia(this.getElementText(eElement, "DePreexistencia"));			
			inConMed271Detalle.setMsgObsPr(this.getElementText(eElement, "MsgObsPr"));
			inConMed271Detalle.setIdFuenteRE(this.getElementText(eElement, "IdFuenteRE"));	        
			inConMed271Detalle.setEsCobertura(this.getElementText(eElement, "EsCobertura"));	        
			inConMed271Detalle.setMoDiagnostico(this.getElementText(eElement, "MoDiagnostico"));
			inConMed271Detalle.setTiEspera(this.getElementText(eElement, "TiEspera"));       
		}
		return inConMed271Detalle;
    }

    private Document buildReponseMessage(GetCondicionMedicaResponse response) throws ParserConfigurationException, MalformedURLException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    	String docNS = "http://www.susalud.gob.pe/ws/siteds/schemas";
        Element rootElement = documentOut.createElementNS(docNS, "getCondicionMedicaResponse");
        rootElement.setPrefix(null);
    	documentOut.appendChild(rootElement);   	
    	rootElement.appendChild(this.createTextElement(documentOut, "coError", response.getCoError()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txNombre", response.getTxNombre()));    	
    	rootElement.appendChild(this.createTextElement(documentOut, "coIafa", response.getCoIafa()));    	
    	rootElement.appendChild(this.createTextElement(documentOut, "txRespuesta", response.getTxRespuesta()));
    	return documentOut;
    }

    private GetCondicionMedicaResponse getCondicionMedicaResponse(String coError, String txRespuesta) {
    	GetCondicionMedicaResponse x = new GetCondicionMedicaResponse();
        x.setCoError(coError);
        x.setCoIafa("20006");
        x.setTxNombre("271_CON_MED");
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