import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class Sint12P3 extends HttpServlet {

    public static ArrayList<String> listaXML = new ArrayList<String>();
    public static ArrayList<String> listaXMLleidos = new ArrayList<String>();
    public static ArrayList<Document> listDoc = new ArrayList<Document>();
    public static ArrayList<String> listError = new ArrayList<String>();
    public static ArrayList<String> listFichError = new ArrayList<String>();
    public static HttpSession session;

    public void init() {
        String URL = "sabina.xml";
        listaXML.add(URL);
        while (listaXML.size() > 0) {
            String url = listaXML.get(0);
            processIML(url);
            listaXML.remove(0);
            listaXMLleidos.add(url);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        String JSP = null;
        String Caducado = "";
        try {
            if (req.getParameter("etapa") != null) {
                String etapa = req.getParameter("etapa");
                if (etapa.equals("0")) {
                    session = req.getSession(true);
                    session.setMaxInactiveInterval(20);
                    Error error = new Error();
                    error.setErrores(listError);
                    error.setFichError(listFichError);
                    JSP = "/Inicio.jsp";
                    req.setAttribute("errorBean", error);
                    session.setAttribute("Caducado", Caducado);
                    ControltoJSP(req, res, JSP);
                } else {
                    Enumeration param = req.getParameterNames();
                    while (param.hasMoreElements()) {
                        String valor = (String) param.nextElement();
                        session.setAttribute(valor, req.getParameter(valor));
                    }

                    ResultBean result = new ResultBean();
                    if (etapa.equals("10")) { //Se sale de la primera pantala de eleccion de consulta
                        if ((session.getAttribute("consulta")).equals("1")) {
                            result.setData(getCantantes());
                            JSP = "/etapa11.jsp";
                        } else {
                            result.setData(getAnhoAlbumes());
                            JSP = "/etapa12.jsp";
                        }
                    } else {
                        if (etapa.charAt(0) == '0') { //sin etapa anterior
                            if (etapa.charAt(2) == '1') { //primera consulta
                                if (etapa.charAt(1) == '1') {  //comprobar etapa
                                    String interprete = (String) session.getAttribute("interprete");
                                    result.setData(getAlbumCantante(interprete));
                                    JSP = "/etapa21.jsp";
                                } else {
                                    String interprete = (String) session.getAttribute("interprete");
                                    String album1 = (String) session.getAttribute("album1");
                                    result.setData(getCancionesCantante(interprete, album1));
                                    JSP = "/resultado1.jsp";
                                }
                            } else {//segunda consulta
                                String anhio = (String) session.getAttribute("anhio");
                                String album2 = (String) session.getAttribute("album2");
                                String estilo = (String) session.getAttribute("estilo");
                                switch (etapa.charAt(1)) { //comprobar etapa
                                    case '1':
                                        result.setData(getAlbumesPorAnho(anhio));
                                        JSP = "/etapa22.jsp";
                                        break;
                                    case '2':
                                        result.setData(getEstilo(anhio, album2));
                                        JSP = "/etapa32.jsp";
                                        break;
                                    case '3':
                                        result.setData(getCancionesEstilo(anhio, album2, estilo));
                                        JSP = "/resultado2.jsp";
                                        break;
                                }
                            }
                        } else { //Boton atras pulsado
                            if (etapa.charAt(2) == '1') { //primera consulta
                                if (etapa.charAt(1) == '1') {  //comprobar etapa
                                    result.setData(getCantantes());
                                    JSP = "/etapa11.jsp";
                                } else {
                                    String interprete = (String) session.getAttribute("interprete");
                                    result.setData(getAlbumCantante(interprete));
                                    JSP = "/etapa21.jsp";
                                }
                            } else {//segunda consulta
                                String anhio = (String) session.getAttribute("anhio");
                                String album2 = (String) session.getAttribute("album2");
                                switch (etapa.charAt(1)) { //comprobar etapa
                                    case '1':
                                        result.setData(getAnhoAlbumes());
                                        JSP = "/etapa12.jsp";
                                        break;
                                    case '2':
                                        result.setData(getAlbumesPorAnho(anhio));
                                        JSP = "/etapa22.jsp";
                                        break;
                                    case '3':
                                        result.setData(getEstilo(anhio, album2));
                                        JSP = "/etapa32.jsp";
                                        break;
                                }
                            }
                        }
                    }
                    req.setAttribute("resultBean", result);
                    ControltoJSP(req, res, JSP);
                }
            } else {
                session = req.getSession(true);
                session.setMaxInactiveInterval(20);
                Error error = new Error();
                error.setErrores(listError);
                error.setFichError(listFichError);
                JSP = "/Inicio.jsp";
                req.setAttribute("errorBean", error);
                session.setAttribute("Caducado", Caducado);
                ControltoJSP(req, res, JSP);
            }
        } catch (Throwable e) {
            session = req.getSession(true);
            session.setMaxInactiveInterval(20);
            Error error = new Error();
            error.setErrores(listError);
            error.setFichError(listFichError);
            JSP = "/Inicio.jsp";
            req.setAttribute("errorBean", error);
            Caducado = "La sesión ha caducado";
            session.setAttribute("Caducado", Caducado);
            ControltoJSP(req, res, JSP);
        }
    }

    public void ControltoJSP(HttpServletRequest req, HttpServletResponse res, String jsp) throws ServletException, IOException {
        ServletContext cont = getServletContext();
        RequestDispatcher reqdis = cont.getRequestDispatcher(jsp);
        reqdis.forward(req, res);
    }

    /*************************************************************************************************************************************
     * **********************************************************************************************************************************
     *************************************************************************************************************************************/

    public static void processIML(String XML) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(true);
        dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", "iml.xsd");
        DocumentBuilder db;
        Document doc;
        XML_DTD_ErrorHandler errorHandler = new XML_DTD_ErrorHandler();
        try {
            db = dbf.newDocumentBuilder();
            db.setErrorHandler(errorHandler);
            // doc = db.parse(new File(XML)); //2
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("iml.xsd"));
            Validator validator = schema.newValidator();
            // validator.validate(new DOMSource(doc)); //2
            validator.validate(new StreamSource(new File(XML))); // 1
            doc = db.parse(XML);// 1
            listDoc.add(doc);
            NodeList iml = doc.getElementsByTagName("IML");
            for (int i = 0; i < iml.getLength(); i++) {
                String IML = iml.item(i).getTextContent().trim();
                if ((!listaXML.contains(IML) && !IML.equals("")) && !listaXMLleidos.contains(IML)) {
                    listaXML.add(IML);
                }
            }
        } catch (ParserConfigurationException e) {
            if (errorHandler.hasError() || errorHandler.hasWarning() || errorHandler.hasFatalError()) {
                listFichError.add("Fichero erróneo: " + XML);
                listError.add(errorHandler.getMessage());
                errorHandler.clear();
            } else {
                listError.add("Error: " + e.toString());
                listFichError.add("Fichero erróneo: " + XML);
            }
        } catch (SAXException e) {
            if (errorHandler.hasError() || errorHandler.hasWarning() || errorHandler.hasFatalError()) {
                listFichError.add("Fichero erróneo: " + XML);
                listError.add(errorHandler.getMessage());
                errorHandler.clear();
            } else {
                listError.add("Error: " + e.getLocalizedMessage());
                listFichError.add("Fichero erróneo: " + XML);
            }
        } catch (IOException e) {
            if (errorHandler.hasError() || errorHandler.hasWarning() || errorHandler.hasFatalError()) {
                listFichError.add("Fichero erróneo: " + XML);
                listError.add(errorHandler.getMessage());
                errorHandler.clear();
            } else {
                listError.add("Error: " + e.toString());
                listFichError.add("Fichero erróneo: " + XML);
            }
        }
    }

    /***
     * CONSULTA 1
     *********/
    public static ArrayList<String> getCantantes() {
        ArrayList<String> lista = new ArrayList<String>();
        for (Document doc : listDoc) {
            Element element = doc.getDocumentElement(); //Element Interprete
            Node firstChild = element.getFirstChild(); //Primer hijo (#text) del elemento Interprete
            while (!firstChild.getNodeName().equals("Nombre")) {
                firstChild = firstChild.getNextSibling();
            }
            Node nombre = firstChild.getChildNodes().item(0);
            while (nombre.getNodeName().equals("#text")) {
                nombre = nombre.getNextSibling();
            }
            lista.add(nombre.getTextContent());
        }
        return lista;
    }

    public static ArrayList<String> getAlbumCantante(String cantante) {
        ArrayList<String> lista = new ArrayList<String>();
        ArrayList<String> list = new ArrayList<String>();
        for (Document doc : listDoc) {
            Element element = doc.getDocumentElement();
            Node name = element.getFirstChild();
            while (name.getNodeName().equals("#text")) {
                name = name.getNextSibling();
            }
            if (name.getNodeName().equals("Nombre")) {
                name = name.getFirstChild();
                while (name.getNodeName().equals("#text")) {
                    name = name.getNextSibling();
                }
            }
            String nombre = name.getTextContent();
            if (nombre.equals(cantante) || cantante.equalsIgnoreCase("todos")) {
                NodeList nombreA = doc.getElementsByTagName("NombreA");
                for (int i = 0; i < nombreA.getLength(); i++) {
                    String albumes = nombreA.item(i).getTextContent();
                    Node an = nombreA.item(i);
                    while (!an.getNodeName().equals("Año")) {
                        an = an.getNextSibling();
                    }
                    String anho = an.getTextContent();
                    list.add(anho + "----" + albumes);
                }
            }
        }
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            String aux[] = list.get(i).split("----");
            lista.add(aux[1]);
        }
        return lista;
    }

    public static ArrayList<String> getCancionesCantante(String cantante, String album) {
        ArrayList<String> lista = new ArrayList<String>();
        for (Document doc : listDoc) {
            Element element = doc.getDocumentElement();
            Node firstChild = element.getFirstChild(); //Primer hijo (#text) del elemento Interprete
            while (!firstChild.getNodeName().equals("Nombre")) {
                firstChild = firstChild.getNextSibling();
            }
            Node name = firstChild.getChildNodes().item(0);
            while (name.getNodeName().equals("#text")) {
                name = name.getNextSibling();
            }
            String nombre = name.getTextContent();
            if (nombre.equalsIgnoreCase(cantante) || cantante.equalsIgnoreCase("todos")) {
                NodeList canciones = doc.getElementsByTagName("Cancion");
                for (int j = 0; j < canciones.getLength(); j++) {
                    Node auxiliar = canciones.item(j).getParentNode().getFirstChild();
                    while (!auxiliar.getNodeName().equals("NombreA")) {
                        auxiliar = auxiliar.getNextSibling();
                    }
                    String nombreA = auxiliar.getTextContent();
                    if (nombreA.equals(album) || album.equalsIgnoreCase("todos")) {
                        NodeList childNodes = canciones.item(j).getChildNodes(); //NODO CANCION QUE COINCIDE CON LOS PARAMETROS SELECCIONADOS
                        ArrayList<String> descrp = new ArrayList<String>();
                        String nombreC = null;
                        String duracion = null;
                        String descrip = "";
                        for (int i = 0; i < childNodes.getLength(); i++) {
                            if (childNodes.item(i).getNodeName().equals("#text")) { //SELECCIONAR LOS COMENTARIOS DENTRO DEL ELEMENTO CANCION
                                String aux = childNodes.item(i).getTextContent();
                                aux = aux.replaceAll("\n", "").trim();
                                if (!aux.equals("")) descrp.add(aux);
                            } else if (childNodes.item(i).getNodeName().equals("NombreT")) { //SACAR EL NOMBRE DE LA CANCION
                                nombreC = childNodes.item(i).getTextContent();
                            } else if (childNodes.item(i).getNodeName().equals("Duracion")) { //SACAR LA DURACION DE LA CANCION
                                duracion = childNodes.item(i).getTextContent();
                            }
                        }
                        for (String cad : descrp) {
                            descrip = descrip + cad + " ";
                        }
                        String cancion = nombreC + " (" + descrip + "; " + duracion + ")";
                        lista.add(cancion);
                    }
                }
            }
        }
        return lista;
    }

    /*******
     * CONSULTA 2
     *******/
    public static ArrayList<String> getAnhoAlbumes() {
        ArrayList<String> lista = new ArrayList<String>();
        for (Document doc : listDoc) {
            NodeList listanho = doc.getElementsByTagName("Año"); //Element Año
            for (int i = 0; i < listanho.getLength(); i++) {
                String anho = listanho.item(i).getTextContent();
                if (!lista.contains(anho))
                    lista.add(anho);
            }
        }
        return lista;
    }

    public static ArrayList<String> getAlbumesPorAnho(String anho) {
        ArrayList<String> lista = new ArrayList<String>();
        ArrayList<String> list = new ArrayList<String>();
        for (Document doc : listDoc) {
            NodeList albumes = doc.getElementsByTagName("Album");
            for (int i = 0; i < albumes.getLength(); i++) {
                Node aux = albumes.item(i).getFirstChild();
                while (!aux.getNodeName().equals("Año")) {
                    aux = aux.getNextSibling();
                }
                String Anho = aux.getTextContent();
                if (Anho.equals(anho) || anho.equalsIgnoreCase("todos")) {
                    Node child = albumes.item(i).getFirstChild();
                    while (!child.getNodeName().equals("NombreA")) {
                        child = child.getNextSibling();
                    }
                    String album = child.getTextContent();
                    list.add(Anho + "----" + album);
                }
            }
        }
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            String aux[] = list.get(i).split("----");
            lista.add(aux[1]);
        }
        return lista;
    }

    public static ArrayList<String> getEstilo(String anho, String album) {
        ArrayList<String> lista = new ArrayList<String>();
        for (Document doc : listDoc) {
            NodeList albumes = doc.getElementsByTagName("Album");
            for (int i = 0; i < albumes.getLength(); i++) {
                Node aux = albumes.item(i).getFirstChild();
                while (!aux.getNodeName().equals("Año")) {
                    aux = aux.getNextSibling();
                }
                String Anho = aux.getTextContent();
                Node auxi = albumes.item(i).getFirstChild();
                while (!auxi.getNodeName().equals("NombreA")) {
                    auxi = auxi.getNextSibling();
                }
                String nombreA = auxi.getTextContent();
                if ((Anho.equals(anho) || anho.equalsIgnoreCase("todos")) && (nombreA.equals(album) || album.equalsIgnoreCase("todos"))) {
                    Element disco = (Element) albumes.item(i);
                    NodeList canciones = disco.getElementsByTagName("Cancion");
                    for (int a = 0; a < canciones.getLength(); a++) {
                        Node cancion = canciones.item(a);
                        while (!cancion.getNodeName().equals("Cancion")) {
                            cancion = cancion.getNextSibling();
                        }
                        NamedNodeMap attributes = cancion.getAttributes();
                        for (int j = 0; j < attributes.getLength(); j++) {
                            String estilo = attributes.item(j).getTextContent();
                            if (!lista.contains(estilo))
                                lista.add(estilo);
                        }
                    }
                }
            }
        }
        return lista;
    }

    public static ArrayList<String> getCancionesEstilo(String anho, String album, String estilo) {
        ArrayList<String> lista = new ArrayList<String>();
        for (Document doc : listDoc) {
            NodeList albumes = doc.getElementsByTagName("Album");
            for (int i = 0; i < albumes.getLength(); i++) {
                Node aux = albumes.item(i).getFirstChild();
                while (!aux.getNodeName().equals("Año")) {
                    aux = aux.getNextSibling();
                }
                String Anho = aux.getTextContent();
                Node auxi = albumes.item(i).getFirstChild();
                while (!auxi.getNodeName().equals("NombreA")) {
                    auxi = auxi.getNextSibling();
                }
                String nombreA = auxi.getTextContent();
                if ((Anho.equals(anho) || anho.equalsIgnoreCase("todos")) && (nombreA.equals(album) || album.equalsIgnoreCase("todos"))) {
                    Element disco = (Element) albumes.item(i);
                    NodeList canciones = disco.getElementsByTagName("Cancion"); //TODAS LAS CANCIONES DEL ALBUM SELECIONADO
                    for (int a = 0; a < canciones.getLength(); a++) {
                        NamedNodeMap attributes = canciones.item(a).getAttributes();
                        for (int j = 0; j < attributes.getLength(); j++) {
                            String Estilo = attributes.item(j).getTextContent();
                            if (Estilo.equals(estilo) || estilo.equalsIgnoreCase("todos")) {
                                NodeList childNodes = canciones.item(a).getChildNodes(); //ELEMENTOS DE LAS CANCIONES QUE COINCIDEN CON LOS PARAMETROS SELECCIONADOS
                                String nombreC = null;
                                for (int k = 0; k < childNodes.getLength(); k++) {
                                    if (childNodes.item(k).getNodeName().equals("NombreT")) { //SACAR EL NOMBRE DE LA CANCION
                                        nombreC = childNodes.item(k).getTextContent();
                                    }
                                }
                                String song = nombreC;
                                lista.add(song);
                            }
                        }
                    }
                }
            }
        }
        return lista;
    }
}

class XML_DTD_ErrorHandler extends DefaultHandler {

    public boolean error;
    public boolean warning;
    public boolean fatalerror;
    public String message;

    public XML_DTD_ErrorHandler() {
        error = false;
        warning = false;
        fatalerror = false;
        message = null;
    }

    public void warning(SAXParseException spe) throws SAXException {
        warning = true;
        message = "Warning: " + spe.getMessage();
        throw new SAXException();
    }

    public void error(SAXParseException spe) throws SAXException {
        error = true;
        message = "Error: " + spe.getMessage();
        throw new SAXException();
    }

    public void fatalerror(SAXParseException spe) throws SAXException {
        fatalerror = true;
        message = "Fatal Error: " + spe.getMessage();
        throw new SAXException();
    }

    public boolean hasWarning() {
        return warning;
    }

    public boolean hasError() {
        return error;
    }

    public boolean hasFatalError() {
        return fatalerror;
    }

    public String getMessage() {
        return message;
    }

    public void clear() {
        warning = false;
        error = false;
        fatalerror = false;
        message = null;
    }
}