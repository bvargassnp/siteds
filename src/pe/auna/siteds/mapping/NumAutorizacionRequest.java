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

import pe.auna.siteds.beans.GetNumAutorizacionRequest;
import pe.gob.susalud.jr.transaccion.susalud.bean.InSolAut271;
import pe.gob.susalud.jr.transaccion.susalud.service.imp.SolAut271ServiceImpl;

/**
 * @author Bryan Vargas
 * @version 2.1
 */
public class NumAutorizacionRequest extends AbstractTransformation {

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
    	this.addInfoMessage("Iniciando Proceso de Mapeo: NumAutorizacionRequest...");

    	try {
    		Document documentIn = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
    		GetNumAutorizacionRequest getNumAutorizacionRequest = this.getNumAutorizacionRequest(documentIn);
    		Document documentOut = this.buildRequestMessage(getNumAutorizacionRequest);
        	TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentOut), new StreamResult(out));
        	this.addInfoMessage("Finalizando Proceso de Mapeo: NumAutorizacionRequest...");
        } catch (Exception e) {
            this.addDebugMessage(e.toString());
            throw new StreamTransformationException(e.toString());
        }
    }

    public GetNumAutorizacionRequest getNumAutorizacionRequest(Document documentIn) {
    	String codigoExcepcion = this.getElementText(documentIn, "coExcepcion");
    	GetNumAutorizacionRequest getNumAutorizacionRequest = new GetNumAutorizacionRequest();
    	getNumAutorizacionRequest.setTxNombre(this.getElementText(documentIn, "txNombre")); 
    	getNumAutorizacionRequest.setCoIafa(this.getElementText(documentIn, "coIafa"));
    	getNumAutorizacionRequest.setTxPeticion(codigoExcepcion.equals("0000") ? this.getElementText(documentIn, "txPeticion") : ""); 
    	getNumAutorizacionRequest.setCoExcepcion(this.getElementText(documentIn, "coExcepcion")); 
		return getNumAutorizacionRequest;
    }
    
    private Document buildRequestMessage(GetNumAutorizacionRequest request) throws ParserConfigurationException {
    	Document documentOut = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    	String docNS = "http://auna.com/X12N/IntegracionAutorizaciones";
        Element rootElement = documentOut.createElementNS(docNS, "NumAutorizacionRequest");
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
        SolAut271ServiceImpl dataIn = new SolAut271ServiceImpl();
        InSolAut271 beanIn = dataIn.x12NToBean(peticion);
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
    	txPeticion.appendChild(this.createTextElement(documentOut, "CaReceptor", beanIn.getCaReceptor()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoAdmisionista", beanIn.getCoAdmisionista()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CaPaciente", beanIn.getCaPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "ApPaternoPaciente", beanIn.getApPaternoPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoPaciente", beanIn.getNoPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoAfPaciente", beanIn.getCoAfPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "ApMaternoPaciente", beanIn.getApMaternoPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoEsPaciente", beanIn.getCoEsPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "TiDoPaciente", beanIn.getTiDoPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuDoPaciente", beanIn.getNuDoPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuIdenEmpleador", beanIn.getNuIdenEmpleador()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuContratoPaciente", beanIn.getNuContratoPaciente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuPoliza", beanIn.getNuPoliza()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuCertificado", beanIn.getNuCertificado()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoTiPolizaAfiliacion", beanIn.getCoTiPolizaAfiliacion()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoProducto", beanIn.getCoProducto()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "DeProducto", beanIn.getDeProducto()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuPlan", beanIn.getNuPlan()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "TiPlanSalud", beanIn.getTiPlanSalud()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoMoneda", beanIn.getCoMoneda()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoParentesco", beanIn.getCoParentesco()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "SoBeneficio", beanIn.getSoBeneficio()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuSoBeneficio", beanIn.getNuSoBeneficio()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoEspecialidad", beanIn.getCoEspecialidad()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "FeNacimiento", beanIn.getFeNacimiento()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "Genero", beanIn.getGenero()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "EsMarital", beanIn.getEsMarital()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "FeIniVigencia", beanIn.getFeIniVigencia()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "FeFinVigencia", beanIn.getFeFinVigencia()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "EsCobertura", beanIn.getEsCobertura()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuDecAccidente", beanIn.getNuDecAccidente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdInfAccidente", beanIn.getIdInfAccidente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "DeTiAccidente", beanIn.getDeTiAccidente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "FeAfiliacion", beanIn.getFeAfiliacion()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "FeOcuAccidente", beanIn.getFeOcuAccidente()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuAtencion", beanIn.getNuAtencion()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdDerFarmacia", beanIn.getIdDerFarmacia()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "TiProducto", beanIn.getTiProducto()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "DeProductoDeFarmacia", beanIn.getDeProductoDeFarmacia()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "FeAtencion", beanIn.getFeAtencion()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuCobertura", beanIn.getNuCobertura()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "ObsCobertura", beanIn.getObsCobertura()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "MsgObs", beanIn.getMsgObs()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "MsgConEspeciales", beanIn.getMsgConEspeciales()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CaContratante", beanIn.getCaContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoPaContratante", beanIn.getNoPaContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoContratante", beanIn.getNoContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoMaContratante", beanIn.getNoMaContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "TiDoContratante", beanIn.getTiDoContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdReContratante", beanIn.getIdReContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoReContratante", beanIn.getCoReContratante()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CaTitular", beanIn.getCaTitular()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoPaTitular", beanIn.getNoPaTitular()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoTitular", beanIn.getNoTitular()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoAfTitular", beanIn.getCoAfTitular()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NoMaTitular", beanIn.getNoMaTitular()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "TiDoTitular", beanIn.getTiDoTitular()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdReTitular", beanIn.getIdReTitular()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuDoTitular", beanIn.getNuDoTitular()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "FeIncTitular", beanIn.getFeIncTitular()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "NuCobPreExistencia", beanIn.getNuCobPreExistencia()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "BeMaxInicial", beanIn.getBeMaxInicial()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CanServicio", beanIn.getCanServicio()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "IdDeProducto", beanIn.getIdDeProducto()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoTiCobertura", beanIn.getCoTiCobertura()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoSubTiCobertura", beanIn.getCoSubTiCobertura()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "MsgObsPre", beanIn.getMsgObsPre()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoTiMoneda", beanIn.getCoTiMoneda()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoPagoFijo", beanIn.getCoPagoFijo()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoCalServicio", beanIn.getCoCalServicio()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CanCalServicio", beanIn.getCanCalServicio()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "CoPagoVariable", beanIn.getCoPagoVariable()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "FlagCG", beanIn.getFlagCG()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "DeflagCG", beanIn.getDeflagCG()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "FeFinCarencia", beanIn.getFeFinCarencia()));
    	txPeticion.appendChild(this.createTextElement(documentOut, "FeFinEspera", beanIn.getFeFinEspera()));
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