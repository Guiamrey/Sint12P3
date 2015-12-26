import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

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
import java.io.PrintWriter;
import java.net.URL;
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
        int i = 0;
        while (listaXML.size() > 0) {
            i++;
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
        PrintWriter out = res.getWriter();
        try {
            if (req.getParameter("etapa") != null) {
                String etapa = req.getParameter("etapa");
                if (etapa.equals("0")) {
                    session = req.getSession(true);
                    session.setMaxInactiveInterval(20);
                    Inicio(out, req, res, session);
                } else {
                    Enumeration param = req.getParameterNames();
                    while (param.hasMoreElements()) {
                        String valor = (String) param.nextElement();
                        session.setAttribute(valor, req.getParameter(valor));
                    }
                    if (etapa.equals("10")) { //Se sale de la primera pantala de eleccion de consulta
                        if (((String) session.getAttribute("consulta")).equals("1")) {
                            etapa11(out, req, res, session);
                        } else {
                            etapa12(out, req, res, session);
                        }
                    } else {
                        if (etapa.charAt(0) == '0') { //sin etapa anterior
                            if (etapa.charAt(2) == '1') { //primera consulta
                                if (etapa.charAt(1) == '1') {  //comprobar etapa
                                    etapa21(out, req, res, session);
                                } else {
                                    resultado1(out, req, res, session);
                                }
                            } else {//segunda consulta
                                switch (etapa.charAt(1)) { //comprobar etapa
                                    case '1':
                                        etapa22(out, req, res, session);
                                        break;
                                    case '2':
                                        etapa32(out, req, res, session);
                                        break;
                                    case '3':
                                        resultado2(out, req, res, session);
                                        break;
                                }
                            }
                        } else { //Boton atras pulsado
                            if (etapa.charAt(2) == '1') { //primera consulta
                                if (etapa.charAt(1) == '1') {  //comprobar etapa
                                    etapa11(out, req, res, session);
                                } else {
                                    etapa21(out, req, res, session);
                                }
                            } else {//segunda consulta
                                switch (etapa.charAt(1)) { //comprobar etapa
                                    case '1':
                                        etapa12(out, req, res, session);
                                        break;
                                    case '2':
                                        etapa22(out, req, res, session);
                                        break;
                                    case '3':
                                        etapa32(out, req, res, session);
                                        break;
                                }
                            }
                        }
                    }
                }
            } else {
                session = req.getSession(true);
                session.setMaxInactiveInterval(20);
                Inicio(out, req, res, session);
            }
        } catch (Throwable e) {
            session = req.getSession(true);
            session.setMaxInactiveInterval(20);
            Inicio(out, req, res, session);
        }
    }

    /*****************************************************************************************************************************************/
    public void Inicio(PrintWriter out, HttpServletRequest req, HttpServletResponse res, HttpSession session) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        imprimirInicio(out);
        out.println("<div class='error'>");
        out.println("<b>Errores encontrados</b>: <br>");
        if (listError.size() == 0) {
            out.println("Ningún error encontrado");
        } else {
            for (int i = 0; i < listError.size(); i++) {
                out.println((i + 1) + ". " + listError.get(i) + "<br>");
            }
        }
        out.println("<br><b>Ficheros erróneos</b>: <br>");
        if (listFichError.size() == 0) {
            out.println("Ningún fichero erróneo encontrado");
        } else {
            for (int i = 0; i < listFichError.size(); i++) {
                out.println((i + 1) + ". " + listFichError.get(i) + "<br>");
            }
        }
        out.println("</div>");
        out.println("<h3>Selecciona la consulta que desea hacer</h3>");
        out.println("<form method='POST' action='?etapa_10' >");
        out.println("<input type='hidden' name='etapa' value='10'>");
        out.println("<input type='radio' value='1' checked='' name='consulta'>Lista de canciones");
        out.println("<br>");
        out.println("<input type='radio' value='2' name='consulta'>Número de canciones");
        out.println("<br>");
        out.println("<p>");
        out.println("<input type='submit' value='Enviar' ");
        out.println("<p>");
        out.println("</form>");
        imprimirFinal(out);
    }

    public void etapa11(PrintWriter out, HttpServletRequest req, HttpServletResponse res, HttpSession session) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        ArrayList<String> list = getCantantes();
        Collections.sort(list);
        imprimirInicio(out);
        out.println("<h4>Seleccione el inteprete deseado:</h4>");
        out.println("<form method='POST' action='?etapa_11&consultainicial_Cantantes' >");
        out.println("<input type='hidden' name='etapa' value='011'>");
        for (int i = 0; i < list.size(); i++) {
            out.println("<input type='radio' checked='' value='" + list.get(i) + "' name='interprete'>" + list.get(i) + "");
            out.println("<br>");
        }
        out.println("<input type='radio' value='Todos' name='interprete'>Todos");
        out.println("<br>");
        out.println("<p>");
        out.println("<input type='submit' value='Enviar' >");
        out.println("<br>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);
    }

    public void etapa21(PrintWriter out, HttpServletRequest req, HttpServletResponse res, HttpSession session) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String interprete = (String) session.getAttribute("interprete");
        ArrayList<String> list = getAlbumCantante(interprete);
        imprimirInicio(out);
        out.println("<h2>Lista de canciones</h2> <h3>Cantante: " + interprete + "</h3>");
        out.println("<h4>Seleccione el álbum deseado:</h4>");
        out.println("<form method='POST  ' action='?etapa_21&consultainicial_Cantantes&Cantante_" + interprete + "' >");
        out.println("<input type='hidden' name='etapa' value='021'>");
        if (list.isEmpty()) {
            out.println("Su consulta no ha generado ningún resultado <br>(El artista seleccionado no tiene ningún álbum)<h4>:(</h4>");
        } else {
            for (int i = 0; i < list.size(); i++) {
                out.println("<input type='radio' checked='' value='" + list.get(i) + "' name='album1'>" + list.get(i) + "");
                out.println("<br>");
            }
            out.println("<input type='radio' value='Todos' name='album1'>Todos ");
            out.println("<br>");
            out.println("<p>");
            out.println("<input type='submit' value='Enviar' >");
        }
        out.println("<br>");
        out.println("<input type='submit' onclick='form.etapa.value=111' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);
    }

    public void resultado1(PrintWriter out, HttpServletRequest req, HttpServletResponse res, HttpSession session) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String interprete = (String) session.getAttribute("interprete");
        String album1 = (String) session.getAttribute("album1");
        ArrayList<String> list = getCancionesCantante(interprete, album1);
        Collections.sort(list);
        imprimirInicio(out);
        out.println("<h2>Lista de canciones</h2><h3>Cantante: " + interprete + "<br>Álbum: " + album1 + "</h3>");
        out.println("<h4>Resultado de su consulta:</h4>");
        out.println("<br>");
        out.println("<ul>");
        if (list.isEmpty()) {
            out.println("Su consulta no ha generado ningún resultado <h4>:(</h4>");
        } else {
            for (int i = 0; i < list.size(); i++) {
                out.println("<il>" + list.get(i) + "</li>");
                out.println("<br>");
            }
        }
        out.println("</ul>");
        out.println("<br>");
        out.println("<form method='POST' action='?etapa_31'>");
        out.println("<input type='hidden' name='etapa' value='031'>");
        out.println("<p>");
        out.println("<input type='submit' onclick='form.etapa.value=121' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);
    }

    /*********************************************************************************************
     * **********************************************************************************************
     ************************************************************************************************/
    public void etapa12(PrintWriter out, HttpServletRequest req, HttpServletResponse res, HttpSession session) throws IOException, ServletException {
        ArrayList<String> list = getAnhoAlbumes();
        Collections.sort(list);
        imprimirInicio(out);
        out.println("<h3>Lista de canciones por estilo</h3>");
        out.println("<h4>Seleccione el año deseado:</h4>");
        out.println("<form method='POST' action='?etapa_12&consultainicial_Canciones' >");
        out.println("<input type='hidden' name='etapa' value='012'>");
        if (list.isEmpty()) {
            out.println("Su consulta no ha generado ningún resultado <h4>:(</h4>");
        } else {
            for (int i = 0; i < list.size(); i++) {
                out.println("<input type='radio' checked='' value='" + list.get(i) + "' name='anhio'>" + list.get(i) + "");
                out.println("<br>");
            }
            out.println("<input type='radio' value='Todos' name='anhio'>Todos");
            out.println("<br>");
            out.println("<p>");
            out.println("<input type='submit' value='Enviar' >");
        }
        out.println("<br>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);
    }

    public void etapa22(PrintWriter out, HttpServletRequest req, HttpServletResponse res, HttpSession session) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String anhio = (String) session.getAttribute("anhio");
        ArrayList<String> list = getAlbumesPorAnho(anhio);
        imprimirInicio(out);
        out.println("<h2>Número de canciones</h2><h3>Año: " + anhio + "</h3>");
        out.println("<h4>Seleccione el álbum deseado:</h4>");
        out.println("<form method='POST' action='?etapa_22&consultainicial_Canciones&anio_" + anhio + "' >");
        out.println("<input type='hidden' name='etapa' value='022'>");
        if (list.isEmpty()) {
            out.println("Su consulta no ha generado ningún resultado <h4>:(</h4>");
        } else {
            for (int i = 0; i < list.size(); i++) {
                out.println("<input type='radio' checked='' value='" + list.get(i) + "' name='album2'>" + list.get(i) + "");
                out.println("<br>");
            }
            out.println("<input type='radio' value='Todos' name='album2'>Todos");
            out.println("<br>");
            out.println("<p>");
            out.println("<input type='submit' value='Enviar' >");
        }
        out.println("<br>");
        out.println("<input type='submit' onclick='form.etapa.value=112' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);
    }

    public void etapa32(PrintWriter out, HttpServletRequest req, HttpServletResponse res, HttpSession session) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String anhio = (String) session.getAttribute("anhio");
        String album2 = (String) session.getAttribute("album2");

        ArrayList<String> list = getEstilo(anhio, album2);
        imprimirInicio(out);
        out.println("<h2>Número de canciones</h2><h3>Año: " + anhio + "<br>Álbum: " + album2 + "</h3>");
        out.println("<h4>Seleccione el estilo deseado:</h4>");
        out.println("<form method='POST' action='?etapa_32&consultainicial_Canciones&anio_" + anhio + "album_" + album2 + "' >");
        out.println("<input type='hidden' name='etapa' value='032'>");
        if (list.isEmpty()) {
            out.println("Su consulta no ha generado ningún resultado <h4>:(</h4>");
        } else {
            for (int i = 0; i < list.size(); i++) {
                out.println("<input type='radio' checked='' value='" + list.get(i) + "' name='estilo'>" + list.get(i) + "");
                out.println("<br>");
            }
            out.println("<input type='radio' value='Todos' name='estilo'>Todos");
            out.println("<br>");
            out.println("<p>");
            out.println("<input type='submit' value='Enviar' >");
        }
        out.println("<br>");
        out.println("<input type='submit' onclick='form.etapa.value=122' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);
    }

    public void resultado2(PrintWriter out, HttpServletRequest req, HttpServletResponse res, HttpSession session) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String anhio = (String) session.getAttribute("anhio");
        String album2 = (String) session.getAttribute("album2");
        String estilo = (String) session.getAttribute("estilo");

        ArrayList<String> list = getCancionesEstilo(anhio, album2, estilo);
        imprimirInicio(out);
        out.println("<h2>Número de canciones</h2><h3>Año: " + anhio + "<br>Álbum: " + album2 + "<br>Estilo: " + estilo + "</h3>");
        out.println("<h4>Resultado de su consulta:</h4>");
        out.println("<br>");
        out.println("El número de canciones es: " + list.size());
        out.println("<form method='POST' action='?etapa_42'>");
        out.println("<input type='hidden' name='etapa' value='042'>");
        out.println("<p>");
        out.println("<input type='submit' onclick='form.etapa.value=132' value='Atrás'>");
        out.println("<input type='submit' onclick='form.etapa.value=0' value='Inicio'>");
        out.println("</form>");
        imprimirFinal(out);
    }

    public void imprimirInicio(PrintWriter out) {
        out.println("<html lang='es'>");
        out.println("<head>");
        out.println("<link rel='stylesheet' href='iml.css'>");
        out.println("<link href='https://fonts.googleapis.com/css?family=Open+Sans+Condensed:300' rel='stylesheet' type='text/css'>");
        out.println("<title>Consulta músical</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<header>");
        out.println("<h1>Servicio web de consulta músical</h1>");
        out.println("</header>");
        out.println("<div>");
    }

    public void imprimirFinal(PrintWriter out) {
        out.println("</div>");
        out.println("<footer>Creado por Ruth Guimarey Docampo<br>Servicios de Internet. Práctica 2</footer>");
        out.println("</body>");
        out.println("</html>");
    }

    /*************************************************************************************************************************************
     * **********************************************************************************************************************************
     *************************************************************************************************************************************/

    public static void processIML(String XML) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true); //<-----------
        dbf.setValidating(true);  //<--------
        dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema"); //<--------
        dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", "iml.xsd"); //<--------
        DocumentBuilder db;
        Document doc;
        XML_DTD_ErrorHandler errorHandler = new XML_DTD_ErrorHandler();
        try {
            db = dbf.newDocumentBuilder();
            db.setErrorHandler(errorHandler);
            // doc = db.parse(new File(XML)); //2 //<--------
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); //<--------
            Schema schema = sf.newSchema(new File("iml.xsd")); //<--------
            Validator validator = schema.newValidator(); //<--------
            // validator.validate(new DOMSource(doc)); //2 //<--------
            validator.validate(new StreamSource(new File(XML))); // 1//<--------
           // System.out.println("DOCUMENTO VALIDO: " + XML);
            doc = db.parse(XML);// 1 //<--------
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
                listError.add("Error: " + e.toString());
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
                                ArrayList<String> descrp = new ArrayList<String>();
                                String nombreC = null;
                                String duracion = null;
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