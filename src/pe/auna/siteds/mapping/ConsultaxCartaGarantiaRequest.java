package pe.auna.siteds.mapping;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sap.aii.mapping.api.AbstractTransformation;
import com.sap.aii.mapping.api.DynamicConfiguration;
import com.sap.aii.mapping.api.DynamicConfigurationKey;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;

import pe.auna.siteds.beans.GetConsultaxCartaGarantiaRequest;
import pe.gob.susalud.jr.transaccion.susalud.bean.In278SolCG;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.In278SolCGServiceImpl;

/**
 * @author Bryan Vargas
 * @version 2.1 
 *
 */
public class ConsultaxCartaGarantiaRequest extends AbstractTransformation {

    // ATRIBUTOS	
    private boolean debug = false;
    DynamicConfiguration dynamicConfiguration;
    // Dynamic Configuration Key for the target IdRemitente which will be used in response message
    private static final DynamicConfigurationKey ID_REMITENTE = DynamicConfigurationKey.create("http://sap.com/xi/XI/System/IdRemitente", "IdRemitente");
    // Dynamic Configuration Key for the target NoTransaccion name which will be used in response message validation
    private static final DynamicConfigurationKey NO_TRANSACCION = DynamicConfigurationKey.create("http://sap.com/xi/XI/System/NoTransaccion", "NoTransaccion");
    // Dynamic Configuration Key for the target TxNombre which will be used in response message validation
    private static final DynamicConfigurationKey TX_NOMBRE = DynamicConfigurationKey.create("http://sap.com/xi/XI/System/TxNombre", "TxNombre");
    // Dynamic Configuration Key for the target CoIafa name which will be used in response message validation
    private static final DynamicConfigurationKey CO_IAFA = DynamicConfigurationKey.create("http://sap.com/xi/XI/System/CoIafa", "CoIafa");
    // Dynamic Configuration Key for the target TxPeticion which will be used in response message validation
    private static final DynamicConfigurationKey TX_PETICION = DynamicConfigurationKey.create("http://sap.com/xi/XI/System/TxPeticion", "TxPeticion");
    // Dynamic Configuration Key for the target CoExcepcion name which will be used in response message validation
    private static final DynamicConfigurationKey CO_EXCEPCION = DynamicConfigurationKey.create("http://sap.com/xi/XI/System/CoExcepcion", "CoExcepcion");

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
    	dynamicConfiguration = in.getDynamicConfiguration();
    	Map<String, String> parameters = new HashMap<String, String>();
    	parameters.put("USER", in.getInputParameters().getString("USER"));
    	parameters.put("PASSWORD", in.getInputParameters().getString("PASSWORD"));
        this.execute(in.getInputPayload().getInputStream(), out.getOutputPayload().getOutputStream(), parameters);
    }

    public void execute(InputStream in, OutputStream out, Map<String, String> parameters) throws StreamTransformationException {
    	this.addInfoMessage("Iniciando Proceso de Mapeo: ConsultaxCartaGarantiaRequest...");

    	try {
    		Document documentIn = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
    		GetConsultaxCartaGarantiaRequest getConsultaxCartaGarantiaRequest = this.getConsultaxCartaGarantiaRequest(documentIn);
    		Document documentOut = this.buildRequestMessage(getConsultaxCartaGarantiaRequest, parameters);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        	this.addInfoMessage("Finalizando Proceso de Mapeo: ConsultaxCartaGarantiaRequest...");
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }

    public GetConsultaxCartaGarantiaRequest getConsultaxCartaGarantiaRequest(Document documentIn) {
    	GetConsultaxCartaGarantiaRequest getConsultaxCartaGarantiaRequest = new GetConsultaxCartaGarantiaRequest();
    	String txNombre = this.getElementText(documentIn, "txNombre");
    	getConsultaxCartaGarantiaRequest.setTxNombre(txNombre);
    	String coIafa = this.getElementText(documentIn, "coIafa");
    	getConsultaxCartaGarantiaRequest.setCoIafa(coIafa);
    	String txPeticion = this.getElementText(documentIn, "txPeticion");
    	getConsultaxCartaGarantiaRequest.setTxPeticion(txPeticion);
    	String coExcepcion = this.getElementText(documentIn, "coExcepcion");
    	getConsultaxCartaGarantiaRequest.setCoExcepcion(coExcepcion);
    	
    	dynamicConfiguration.put(TX_NOMBRE, txNombre);
    	dynamicConfiguration.put(CO_IAFA, coIafa);
    	dynamicConfiguration.put(TX_PETICION, txPeticion);
    	dynamicConfiguration.put(CO_EXCEPCION, coExcepcion);
		return getConsultaxCartaGarantiaRequest;
    }
    
    private Document buildRequestMessage(GetConsultaxCartaGarantiaRequest request, Map<String, String> parameters) throws ParserConfigurationException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

    	String docNS = "http://www.laprotectora.com.pe/";
        Element rootElement = documentOut.createElementNS(docNS, "InformacionAfiliado");
    	rootElement.setPrefix("lap");
    	In278SolCGServiceImpl dataIn = new In278SolCGServiceImpl();
    	In278SolCG beanIn = dataIn.x12NToBean(request.getTxPeticion());
    	rootElement.appendChild(this.createTextElement(documentOut, "user", parameters.get("USER")));   // 1 	
    	rootElement.appendChild(this.createTextElement(documentOut, "pass", parameters.get("PASSWORD"))); // 2    	
    	rootElement.appendChild(this.createTextElement(documentOut, "numeroCarta", beanIn.getNuCarGarantia()));   // 1
    	rootElement.appendChild(this.createTextElement(documentOut, "paternoPaciente", beanIn.getApPaternoPaciente()));   // 2
    	rootElement.appendChild(this.createTextElement(documentOut, "maternoPaciente", beanIn.getApMaternoPaciente()));   // 3
    	rootElement.appendChild(this.createTextElement(documentOut, "nombrePaciente", beanIn.getNoPaciente()));   // 4
    	rootElement.appendChild(this.createTextElement(documentOut, "codigoIPRESS", beanIn.getIdRemitente()));   // 5
    	rootElement.appendChild(this.createTextElement(documentOut, "codigoIAFAS", beanIn.getIdReceptor()));   // 6
    	rootElement.appendChild(this.createTextElement(documentOut, "DNI", beanIn.getNuDoPaciente()));   // 7
    	    	
    	documentOut.appendChild(rootElement);
    	dynamicConfiguration.put(ID_REMITENTE, beanIn.getIdRemitente());
    	dynamicConfiguration.put(NO_TRANSACCION, beanIn.getNoTransaccion());
    	return documentOut;
    }

    private String getElementText(Document document, String property) {
    	NodeList propertyNode = document.getElementsByTagName(property);
    	return propertyNode.getLength() > 0 ?  propertyNode.item(0).getTextContent() : "";
    }

    private Element createTextElement(Document document, String tag, String value) {
    	Element element = document.createElement("lap:" + tag);
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