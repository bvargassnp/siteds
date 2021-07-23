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

import pe.auna.siteds.beans.GetConsultaAsegCodResponse;
import pe.gob.susalud.jr.transaccion.susalud.bean.InConCod271;
import pe.gob.susalud.jr.transaccion.susalud.bean.InConCod271Detalle;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.ConCod271ServiceImpl;

public class ConsultaAsegCodResponse extends AbstractTransformation {

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
        	GetConsultaAsegCodResponse rsp = this.getConsultaAsegCod(documentIn);
        	Document documentOut = this.buildReponseMessage(rsp);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }
    
    private GetConsultaAsegCodResponse getConsultaAsegCod(Document documentIn) {
    	String codigoError = this.getElementText(documentIn, "CoError");
    	GetConsultaAsegCodResponse rsp;
    	
    	if (codigoError.equals("0000")) {
    		InConCod271 inConCod271 = null;
    		NodeList conCod271 = documentIn.getElementsByTagName("TxRespuesta");

    		if (conCod271.getLength() > 0 && conCod271.item(0).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) conCod271.item(0);
    			inConCod271 = this.getInConCod271(eElement);
    			NodeList conCod271Detalle = eElement.getElementsByTagName("InConCod271Detalle");

    			for (int i = 0; i < conCod271Detalle.getLength(); i++) {
    				inConCod271.addDetalle(this.getInConCod271Detalle(conCod271Detalle.item(i)));
    			}
    		}
    			
            ConCod271ServiceImpl dataOut = new ConCod271ServiceImpl();
            String msgXMLx12n = dataOut.beanToX12N(inConCod271);
            rsp = this.getConsultaAsegCodResponse(codigoError, msgXMLx12n);
    	} else {
    		rsp = this.getConsultaAsegCodResponse(codigoError, null);
    	}
    	return rsp;
    }
    
    private InConCod271 getInConCod271(Element eElement) {
    	InConCod271 inConCod271 = new InConCod271();
		inConCod271.setNoTransaccion(this.getElementText(eElement, "NoTransaccion")); //1s
        inConCod271.setIdRemitente(this.getElementText(eElement, "IdRemitente")); //2s
        inConCod271.setIdReceptor(this.getElementText(eElement, "IdReceptor")); //3s
        inConCod271.setFeTransaccion(this.getElementText(eElement, "FeTransaccion")); //4s
        inConCod271.setHoTransaccion(this.getElementText(eElement, "HoTransaccion")); //5s
        inConCod271.setIdCorrelativo(this.getElementText(eElement, "IdCorrelativo")); //6s
        inConCod271.setIdTransaccion(this.getElementText(eElement, "IdTransaccion")); //7s
        inConCod271.setTiFinalidad(this.getElementText(eElement, "TiFinalidad")); //8s not in the response. 
        inConCod271.setCaRemitente(this.getElementText(eElement, "CaRemitente")); //9s
        inConCod271.setUserRemitente(this.getElementText(eElement, "UserRemitente")); //10s
        inConCod271.setPassRemitente(this.getElementText(eElement, "PassRemitente")); //10s
        inConCod271.setFeUpFoto(this.getElementText(eElement, "FeUpFoto")); //10s
        inConCod271.setCaReceptor(this.getElementText(eElement, "CaReceptor")); //10s
        inConCod271.setNuRucReceptor(this.getElementText(eElement, "NuRucReceptor")); //10s
        inConCod271.setCaPaciente(this.getElementText(eElement, "CaPaciente")); //10s
        inConCod271.setApPaternoPaciente(this.getElementText(eElement, "ApPaternoPaciente")); //10s
        inConCod271.setNoPaciente(this.getElementText(eElement, "NoPaciente")); //10s
        inConCod271.setCoAfPaciente(this.getElementText(eElement, "CoAfPaciente")); //10s
        inConCod271.setApMaternoPaciente(this.getElementText(eElement, "ApMaternoPaciente")); //10s
        inConCod271.setCoEsPaciente(this.getElementText(eElement, "CoEsPaciente")); //10s
        inConCod271.setTiDoPaciente(this.getElementText(eElement, "TiDoPaciente")); //10s
        inConCod271.setNuDoPaciente(this.getElementText(eElement, "NuDoPaciente")); //10s
        inConCod271.setNuIdenPaciente(this.getElementText(eElement, "NuIdenPaciente")); //10s
        inConCod271.setNuContratoPaciente(this.getElementText(eElement, "NuContratoPaciente")); //10s
        inConCod271.setNuPoliza(this.getElementText(eElement, "NuPoliza")); //10s
        inConCod271.setNuCertificado(this.getElementText(eElement, "NuCertificado")); //10s
        inConCod271.setCoTiPoliza(this.getElementText(eElement, "CoTiPoliza")); //10s
        inConCod271.setCoProducto(this.getElementText(eElement, "CoProducto")); //10s
        inConCod271.setDeProducto(this.getElementText(eElement, "DeProducto")); //10s
        inConCod271.setNuPlan(this.getElementText(eElement, "NuPlan")); //10s
        inConCod271.setTiPlanSalud(this.getElementText(eElement, "TiPlanSalud")); //10s
        inConCod271.setCoMoneda(this.getElementText(eElement, "CoMoneda")); //10s
        inConCod271.setCoParentesco(this.getElementText(eElement, "CoParentesco")); //10s
        inConCod271.setSoBeneficio(this.getElementText(eElement, "SoBeneficio")); //10s
        inConCod271.setNuSoBeneficio(this.getElementText(eElement, "NuSoBeneficio")); //10s
        inConCod271.setFeNacimiento(this.getElementText(eElement, "FeNacimiento")); //10s
        inConCod271.setGenero(this.getElementText(eElement, "Genero")); //10s
        inConCod271.setEsMarital(this.getElementText(eElement, "EsMarital")); //10s
        inConCod271.setFeIniVigencia(this.getElementText(eElement, "FeIniVigencia")); //10s
        inConCod271.setFeFinVigencia(this.getElementText(eElement, "FeFinVigencia")); //10s
        inConCod271.setTiCaContratante(this.getElementText(eElement, "TiCaContratante")); //10s
        inConCod271.setNoPaContratante(this.getElementText(eElement, "NoPaContratante")); //10s
        inConCod271.setNoContratante(this.getElementText(eElement, "NoContratante")); //10s
        inConCod271.setNoMaContratante(this.getElementText(eElement, "NoMaContratante")); //10s
        inConCod271.setTiDoContratante(this.getElementText(eElement, "TiDoContratante")); //10s
        inConCod271.setIdReContratante(this.getElementText(eElement, "IdReContratante")); //10s
        inConCod271.setCoReContratante(this.getElementText(eElement, "CoReContratante")); //10s
        inConCod271.setCaTitular(this.getElementText(eElement, "CaTitular")); //10s
        inConCod271.setNoPaTitular(this.getElementText(eElement, "NoPaTitular")); //10s
        inConCod271.setNoTitular(this.getElementText(eElement, "NoTitular")); //10s
        inConCod271.setCoAfTitular(this.getElementText(eElement, "CoAfTitular")); //10s
        inConCod271.setNoMaTitular(this.getElementText(eElement, "NoMaTitular")); //10s
        inConCod271.setTiDoTitular(this.getElementText(eElement, "TiDoTitular")); //10s
        inConCod271.setNuDoTitular(this.getElementText(eElement, "NuDoTitular")); //10s
        inConCod271.setFeInsTitular(this.getElementText(eElement, "FeInsTitular")); //10s
    	return inConCod271;
    }
    
    public InConCod271Detalle getInConCod271Detalle(Node consultaAsegCod) {
    	InConCod271Detalle inConCod271Detalle = new InConCod271Detalle();
		if (consultaAsegCod.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) consultaAsegCod;
			inConCod271Detalle.setInfBeneficio(this.getElementText(eElement, "InfBeneficio")); //1s
	        inConCod271Detalle.setNuCobertura(this.getElementText(eElement, "NuCobertura")); //2s
	        inConCod271Detalle.setBeMaxInicial(this.getElementText(eElement, "BeMaxInicial")); //3s
	        inConCod271Detalle.setMoCobertura(this.getElementText(eElement, "MoCobertura")); //4s
	        inConCod271Detalle.setCoInRestriccion(this.getElementText(eElement, "CoInRestriccion")); //5s
	        inConCod271Detalle.setCanServicio(this.getElementText(eElement, "CanServicio")); //6s
	        inConCod271Detalle.setIdProducto(this.getElementText(eElement, "IdProducto")); //7s
	        inConCod271Detalle.setCoTiCobertura(this.getElementText(eElement, "CoTiCobertura")); //8s not in the response. 
	        inConCod271Detalle.setCoSubTiCobertura(this.getElementText(eElement, "CoSubTiCobertura")); //9s
	        inConCod271Detalle.setMsgConEspeciales(this.getElementText(eElement, "MsgConEspeciales")); //10s
	        inConCod271Detalle.setCoTiMoneda(this.getElementText(eElement, "CoTiMoneda")); //10s
	        inConCod271Detalle.setCoPagoFijo(this.getElementText(eElement, "CoPagoFijo")); //10s
	        inConCod271Detalle.setCoCalServicio(this.getElementText(eElement, "CoCalServicio")); //10s
	        inConCod271Detalle.setCanCalServicio(this.getElementText(eElement, "CanCalServicio")); //10s
	        inConCod271Detalle.setCoPagoVariable(this.getElementText(eElement, "CoPagoVariable")); //10s
	        inConCod271Detalle.setFlagCaGarantia(this.getElementText(eElement, "FlagCaGarantia")); //10s
	        inConCod271Detalle.setDeflagCaGarantia(this.getElementText(eElement, "DeflagCaGarantia")); //10s
	        inConCod271Detalle.setFeFinCarencia(this.getElementText(eElement, "FeFinCarencia")); //10s
	        inConCod271Detalle.setFeFinEspera(this.getElementText(eElement, "FeFinEspera")); //10s
		}
		return inConCod271Detalle;
    }

    private Document buildReponseMessage(GetConsultaAsegCodResponse response) throws ParserConfigurationException, MalformedURLException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    	
    	String docNS = "http://www.susalud.gob.pe/ws/siteds/schemas";
        Element rootElement = documentOut.createElementNS(docNS, "getConsultaAsegCodResponse");
        rootElement.setPrefix(null);
    	documentOut.appendChild(rootElement);
    	rootElement.appendChild(this.createTextElement(documentOut, "coError", response.getCoError()));
    	rootElement.appendChild(this.createTextElement(documentOut, "txNombre", response.getTxNombre()));    	
    	rootElement.appendChild(this.createTextElement(documentOut, "coIafa", response.getCoIafa()));    	
    	rootElement.appendChild(this.createTextElement(documentOut, "txRespuesta", response.getTxRespuesta()));
    	return documentOut;
    }

    private GetConsultaAsegCodResponse getConsultaAsegCodResponse(String coError, String txRespuesta) {
        GetConsultaAsegCodResponse x = new GetConsultaAsegCodResponse();
        x.setCoError(coError);
        x.setCoIafa("20006");
        x.setTxNombre("271_CON_COD");
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