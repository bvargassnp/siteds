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

import pe.auna.siteds.beans.GetConsultaDerivaResponse;
import pe.gob.susalud.jr.transaccion.susalud.bean.In271ResDeriva;
import pe.gob.susalud.jr.transaccion.susalud.bean.In271ResDerivaDetalle;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.In271ResDerivaServiceImpl;

/**
 * @author Bryan Vargas
 * @version 2.1
 */
public class ConsultaDerivaResponse extends AbstractTransformation {

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
        	GetConsultaDerivaResponse rsp = this.getConsultaDeriva(documentIn);
        	Document documentOut = this.buildReponseMessage(rsp);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }

    private GetConsultaDerivaResponse getConsultaDeriva(Document documentIn) {
    	String codigoError = this.getElementText(documentIn, "CoError");
    	GetConsultaDerivaResponse rsp;
    	
    	if (codigoError.equals("0000")) {
    		In271ResDeriva in271ResDeriva = null;
    		NodeList resDeriva271 = documentIn.getElementsByTagName("TxRespuesta");

    		if (resDeriva271.getLength() > 0 && resDeriva271.item(0).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) resDeriva271.item(0);
				in271ResDeriva = this.getIn271ResDeriva(eElement);
    			NodeList resDeriva271Detalle = eElement.getElementsByTagName("In271ResDerivaDetalle");

    			for (int i = 0; i < resDeriva271Detalle.getLength(); i++) {
    				in271ResDeriva.addDetalle(this.getIn271ResDerivaDetalle(resDeriva271Detalle.item(i)));
    			}
    		}
    		
    		In271ResDerivaServiceImpl dataOut = new In271ResDerivaServiceImpl();
            String msgXMLx12n = dataOut.beanToX12N(in271ResDeriva);
            rsp = this.getConsultaDerivaResponse(codigoError, msgXMLx12n);
    	} else {
    		rsp = this.getConsultaDerivaResponse(codigoError, null);
    	}
    	return rsp;
    }
    
    private In271ResDeriva getIn271ResDeriva(Element eElement) {
    	In271ResDeriva in271ResDeriva = new In271ResDeriva();
    	in271ResDeriva.setNoTransaccion(this.getElementText(eElement, "NoTransaccion"));
    	in271ResDeriva.setIdRemitente(this.getElementText(eElement, "IdRemitente"));
    	in271ResDeriva.setIdReceptor(this.getElementText(eElement, "IdReceptor"));
    	in271ResDeriva.setFeTransaccion(this.getElementText(eElement, "FeTransaccion")); 
    	in271ResDeriva.setHoTransaccion(this.getElementText(eElement, "HoTransaccion")); 
    	in271ResDeriva.setIdCorrelativo(this.getElementText(eElement, "IdCorrelativo")); 
    	in271ResDeriva.setIdTransaccion(this.getElementText(eElement, "IdTransaccion")); 
    	in271ResDeriva.setTiFinalidad(this.getElementText(eElement, "TiFinalidad"));  
    	in271ResDeriva.setCaRemitente(this.getElementText(eElement, "CaRemitente"));
    	in271ResDeriva.setCaReceptor(this.getElementText(eElement, "CaReceptor")); 
    	in271ResDeriva.setNuRucReceptor(this.getElementText(eElement, "NuRucReceptor"));
    	return in271ResDeriva;
    }
    
    public In271ResDerivaDetalle getIn271ResDerivaDetalle(Node consultaAsegCod) {
    	In271ResDerivaDetalle in271ResDerivaDetalle = new In271ResDerivaDetalle();
		if (consultaAsegCod.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) consultaAsegCod;
			in271ResDerivaDetalle.setCaPaciente(this.getElementText(eElement, "CaPaciente"));
			in271ResDerivaDetalle.setApPaternoPaciente(this.getElementText(eElement, "ApPaternoPaciente"));
			in271ResDerivaDetalle.setNoPaciente(this.getElementText(eElement, "NoPaciente")); 
			in271ResDerivaDetalle.setCoAfPaciente(this.getElementText(eElement, "CoAfPaciente")); 
			in271ResDerivaDetalle.setApMaternoPaciente(this.getElementText(eElement, "ApMaternoPaciente")); 
			in271ResDerivaDetalle.setCoTiDoPaciente(this.getElementText(eElement, "CoTiDoPaciente")); 
			in271ResDerivaDetalle.setNuDoPaciente(this.getElementText(eElement, "NuDoPaciente")); 
			in271ResDerivaDetalle.setCoParentesco(this.getElementText(eElement, "CoParentesco")); 
			in271ResDerivaDetalle.setTiDoTrabajoAfiliado(this.getElementText(eElement, "TiDoTrabajoAfiliado"));
			in271ResDerivaDetalle.setNuDoTrabajoAfiliado(this.getElementText(eElement, "NuDoTrabajoAfiliado"));
			in271ResDerivaDetalle.setNuAtencion(this.getElementText(eElement, "NuAtencion")); 
			in271ResDerivaDetalle.setTeMsgLibre1(this.getElementText(eElement, "TeMsgLibre1"));
			in271ResDerivaDetalle.setCoTiProducto(this.getElementText(eElement, "CoTiProducto"));
			in271ResDerivaDetalle.setDeProducto(this.getElementText(eElement, "DeProducto"));			
			in271ResDerivaDetalle.setCoTiCobertura(this.getElementText(eElement, "CoTiCobertura"));			
			in271ResDerivaDetalle.setCoSubTiCobertura(this.getElementText(eElement, "CoSubTiCobertura"));			
			in271ResDerivaDetalle.setFeAtSalud(this.getElementText(eElement, "FeAtSalud")); 	
			in271ResDerivaDetalle.setNoLuAtencion(this.getElementText(eElement, "NoLuAtencion"));			
			in271ResDerivaDetalle.setCoLuAtencion(this.getElementText(eElement, "CoLuAtencion"));			
			in271ResDerivaDetalle.setTiDoContratante(this.getElementText(eElement, "TiDoContratante")); 			
			in271ResDerivaDetalle.setIdCaReferencia(this.getElementText(eElement, "IdCaReferencia"));			
			in271ResDerivaDetalle.setReIdContratante(this.getElementText(eElement, "ReIdContratante"));
		}
		return in271ResDerivaDetalle;
    }
    
    private Document buildReponseMessage(GetConsultaDerivaResponse response) throws ParserConfigurationException, MalformedURLException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    	
    	String docNS = "http://www.susalud.gob.pe/ws/siteds/schemas";
        Element rootElement = documentOut.createElementNS(docNS, "getConsultaDerivaResponse");
        rootElement.setPrefix(null);
    	documentOut.appendChild(rootElement);
    	rootElement.appendChild(this.createTextElement(documentOut, "coError", response.getCoError()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txNombre", response.getTxNombre()));
    	rootElement.appendChild(this.createTextElement(documentOut, "coIafa", response.getCoIafa()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txRespuesta", response.getTxRespuesta()));
    	return documentOut;
    }

    private GetConsultaDerivaResponse getConsultaDerivaResponse(String coError, String txRespuesta) {
    	GetConsultaDerivaResponse x = new GetConsultaDerivaResponse();
        x.setCoError(coError);
        x.setCoIafa("20006");
        x.setTxNombre("271_RES_DERIVA");
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