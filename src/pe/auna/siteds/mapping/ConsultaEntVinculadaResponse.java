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

import pe.auna.siteds.beans.GetConsultaEntVinculadaResponse;
import pe.gob.susalud.jr.transaccion.susalud.bean.InResEntVinc278;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.ResEntVinc278ServiceImpl;

public class ConsultaEntVinculadaResponse extends AbstractTransformation {

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
        	GetConsultaEntVinculadaResponse rsp = this.getConsultaEntVinculada(documentIn);
        	Document documentOut = this.buildReponseMessage(rsp);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }

    private GetConsultaEntVinculadaResponse getConsultaEntVinculada(Document documentIn) {
    	String codigoError = this.getElementText(documentIn, "CoError");
    	GetConsultaEntVinculadaResponse rsp;
    	
    	if (codigoError.equals("0000")) {
    		NodeList entidadesVinculadas = documentIn.getElementsByTagName("TxRespuesta");
    		InResEntVinc278 inResEntVinc278 = null;
    		
			for (int i = 0; i < entidadesVinculadas.getLength(); i++) {
				inResEntVinc278 = this.getConsultaEntVinculada(entidadesVinculadas.item(i));
    		}
			
            ResEntVinc278ServiceImpl dataOut = new ResEntVinc278ServiceImpl();
            String msgXMLx12n = dataOut.beanToX12N(inResEntVinc278);
            rsp = this.getConsultaEntVinculadaResponse("0000", msgXMLx12n);
    	} else {
    		rsp = this.getConsultaEntVinculadaResponse(codigoError, null);
    	}
    	return rsp;
    }

    public InResEntVinc278 getConsultaEntVinculada(Node entidadVinculada) {
    	InResEntVinc278 inResEntVinc278 = new InResEntVinc278();
		if (entidadVinculada.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) entidadVinculada;
	        inResEntVinc278.setNoTransaccion(this.getElementText(eElement, "NoTransaccion")); //1s
	        inResEntVinc278.setIdRemitente(this.getElementText(eElement, "IdRemitente")); //2s
	        inResEntVinc278.setIdReceptor(this.getElementText(eElement, "IdReceptor")); //3s
	        inResEntVinc278.setFeTransaccion(this.getElementText(eElement, "FeTransaccion")); //4s
	        inResEntVinc278.setHoTransaccion(this.getElementText(eElement, "HoTransaccion")); //5s
	        inResEntVinc278.setIdCorrelativo(this.getElementText(eElement, "IdCorrelativo")); //6s
	        inResEntVinc278.setIdTransaccion(this.getElementText(eElement, "IdTransaccion")); //7s
	        inResEntVinc278.setTiFinalidad(this.getElementText(eElement, "TiFinalidad")); //8s not in the response. 
	        inResEntVinc278.setRespuesta(this.getElementText(eElement, "Respuesta")); //9s
	        inResEntVinc278.setMsgRespuesta(this.getElementText(eElement, "MsgRespuesta")); //10s
		}
		return inResEntVinc278;
    }

    private Document buildReponseMessage(GetConsultaEntVinculadaResponse response) throws ParserConfigurationException, MalformedURLException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    	
    	String docNS = "http://www.susalud.gob.pe/ws/siteds/schemas";
        Element rootElement = documentOut.createElementNS(docNS, "getConsultaEntVinculadaResponse");
        rootElement.setPrefix(null);
    	documentOut.appendChild(rootElement);
    	rootElement.appendChild(this.createTextElement(documentOut, "coError", response.getCoError()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txNombre", response.getTxNombre()));
    	rootElement.appendChild(this.createTextElement(documentOut, "coIafa", response.getCoIafa()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txRespuesta", response.getTxRespuesta()));
    	return documentOut;
    }

    private GetConsultaEntVinculadaResponse getConsultaEntVinculadaResponse(String coError, String txRespuesta) {
        GetConsultaEntVinculadaResponse x = new GetConsultaEntVinculadaResponse();
        x.setCoError(coError);
        x.setCoIafa("20006");
        x.setTxNombre("278_RES_ENT_VINC");
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
