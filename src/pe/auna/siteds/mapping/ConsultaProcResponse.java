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

import pe.auna.siteds.beans.GetConsultaProcResponse;
import pe.gob.susalud.jr.transaccion.susalud.bean.InConProc271;
import pe.gob.susalud.jr.transaccion.susalud.bean.InConProc271Detalle;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.In271ConProcServiceImpl;


public class ConsultaProcResponse extends AbstractTransformation {

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
        	GetConsultaProcResponse rsp = this.getConsultaProc(documentIn);
        	Document documentOut = this.buildReponseMessage(rsp);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }

    private GetConsultaProcResponse getConsultaProc(Document documentIn) {
    	String codigoError = this.getElementText(documentIn, "CoError");
    	GetConsultaProcResponse rsp;
    	
    	if (codigoError.equals("0000")) {
    		InConProc271 inConProc271  = null;
    		NodeList conProc271 = documentIn.getElementsByTagName("TxRespuesta");

    		if (conProc271.getLength() > 0 && conProc271.item(0).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) conProc271.item(0);
				inConProc271 = this.getInConProc271(eElement);
    			NodeList conProc271Detalle = eElement.getElementsByTagName("InConProc271Detalle");

    			for (int i = 0; i < conProc271Detalle.getLength(); i++) {
    				inConProc271.addDetalle(this.getInConProc271Detalle(conProc271Detalle.item(i)));
    			}
    		}
    		
    		In271ConProcServiceImpl dataOut = new In271ConProcServiceImpl();
            String msgXMLx12n = dataOut.beanToX12N(inConProc271);
            rsp = this.getConsultaProcResponse(codigoError, msgXMLx12n);
    	} else {
    		rsp = this.getConsultaProcResponse(codigoError, null);
    	}
    	return rsp;
    }
    
    private InConProc271 getInConProc271(Element eElement) {
    	InConProc271 inConProc271 = new InConProc271();
    	inConProc271.setNoTransaccion(this.getElementText(eElement, "NoTransaccion"));
    	inConProc271.setIdRemitente(this.getElementText(eElement, "IdRemitente"));
    	inConProc271.setIdReceptor(this.getElementText(eElement, "IdReceptor")); 
    	inConProc271.setFeTransaccion(this.getElementText(eElement, "FeTransaccion")); 
    	inConProc271.setHoTransaccion(this.getElementText(eElement, "HoTransaccion")); 
    	inConProc271.setIdCorrelativo(this.getElementText(eElement, "IdCorrelativo")); 
    	inConProc271.setIdTransaccion(this.getElementText(eElement, "IdTransaccion")); 
    	inConProc271.setTiFinalidad(this.getElementText(eElement, "TiFinalidad")); 
    	inConProc271.setCaRemitente(this.getElementText(eElement, "CaRemitente"));
    	inConProc271.setCaReceptor(this.getElementText(eElement, "CaReceptor")); 
    	inConProc271.setNuRucReceptor(this.getElementText(eElement, "NuRucReceptor"));    	
    	inConProc271.setCaPaciente(this.getElementText(eElement, "CaPaciente"));
    	inConProc271.setApPaternoPaciente(this.getElementText(eElement, "ApPaternoPaciente"));
    	inConProc271.setNoPaciente(this.getElementText(eElement, "NoPaciente"));
    	inConProc271.setCoAfPaciente(this.getElementText(eElement, "CoAfPaciente"));
    	inConProc271.setApMaternoPaciente(this.getElementText(eElement, "ApMaternoPaciente"));
    	
    	return inConProc271;
    }
    
    public InConProc271Detalle getInConProc271Detalle(Node consultaAsegCod) {
    	InConProc271Detalle inConProc271Detalle = new InConProc271Detalle();
		if (consultaAsegCod.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) consultaAsegCod;
			inConProc271Detalle.setCoInProcedimiento(this.getElementText(eElement, "CoInProcedimiento"));
			inConProc271Detalle.setCoInRestriccion(this.getElementText(eElement, "CoInRestriccion")); 
			inConProc271Detalle.setCoProcedimiento(this.getElementText(eElement, "CoProcedimiento")); 
			inConProc271Detalle.setImDeducible(this.getElementText(eElement, "ImDeducible")); 
			inConProc271Detalle.setPoCuExDecimal(this.getElementText(eElement, "PoCuExDecimal")); 
			inConProc271Detalle.setNuFrecuencia(this.getElementText(eElement, "NuFrecuencia"));
			inConProc271Detalle.setCoSexo(this.getElementText(eElement, "CoSexo"));
			inConProc271Detalle.setTiNuDias(this.getElementText(eElement, "TiNuDias"));
			inConProc271Detalle.setTeMsgObservacion(this.getElementText(eElement, "TeMsgObservacion"));
			inConProc271Detalle.setIdFuentePE(this.getElementText(eElement, "IdFuentePE"));
			inConProc271Detalle.setCoTiEspera(this.getElementText(eElement, "CoTiEspera"));
			inConProc271Detalle.setDeTiEspera(this.getElementText(eElement, "DeTiEspera"));
			inConProc271Detalle.setFeFinVigencia(this.getElementText(eElement, "FeFinVigencia"));			
			inConProc271Detalle.setTeMsgTiEspera(this.getElementText(eElement, "TeMsgTiEspera"));			
			inConProc271Detalle.setIdFuenteTE(this.getElementText(eElement, "IdFuenteTE"));			
			inConProc271Detalle.setCoExCarencia(this.getElementText(eElement, "CoExCarencia"));
			inConProc271Detalle.setDeExCarencia(this.getElementText(eElement, "DeExCarencia"));
			inConProc271Detalle.setTeMsgExCarencia(this.getElementText(eElement, "TeMsgExCarencia"));
			inConProc271Detalle.setIdFuenteEC(this.getElementText(eElement, "IdFuenteEC"));
		}
		return inConProc271Detalle;
    }
    
    private Document buildReponseMessage(GetConsultaProcResponse response) throws ParserConfigurationException, MalformedURLException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    	
    	String docNS = "http://www.susalud.gob.pe/ws/siteds/schemas";
        Element rootElement = documentOut.createElementNS(docNS, "getConsultaProcResponse");
        rootElement.setPrefix(null);
    	documentOut.appendChild(rootElement);
    	rootElement.appendChild(this.createTextElement(documentOut, "coError", response.getCoError()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txNombre", response.getTxNombre()));
    	rootElement.appendChild(this.createTextElement(documentOut, "coIafa", response.getCoIafa()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txRespuesta", response.getTxRespuesta()));
    	return documentOut;
    }

    private GetConsultaProcResponse getConsultaProcResponse(String coError, String txRespuesta) {
    	GetConsultaProcResponse x = new GetConsultaProcResponse();
        x.setCoError(coError);
        x.setCoIafa("20006");
        x.setTxNombre("271_CON_PROC");
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