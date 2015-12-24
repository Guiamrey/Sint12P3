import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class XML_DTD {

    public static ArrayList<String> listaXML = new ArrayList<>();
    public static ArrayList<String> listaXMLleidos = new ArrayList<>();
    public static ArrayList<Document> listDoc = new ArrayList<>();
    public static ArrayList<String> listError = new ArrayList<>();
    public static ArrayList<String> listFichError = new ArrayList<>();

    public static void main(String[] args) {
        String URL = "sabina.xml";
        processIML(URL);
        listaXMLleidos.add(URL);
        while (listaXML.size() > 0) {
            String url = listaXML.get(0);
            processIML(url);
            listaXML.remove(0);
            listaXMLleidos.add(url);
        }

        for (int i = 0; i < listError.size(); i++) {
            System.out.println(listError.get(i));
        }
        for (int i = 0; i < listFichError.size(); i++) {
            System.out.println(listFichError.get(i));
        }
/*** consulta 1 ***/
        // ArrayList list = getCantantes();
        // ArrayList list = getAlbumCantante("Pene");
          //ArrayList list = getCancionesCantante("Todos", "Todos");
/***consulta 2****/
        //ArrayList list = getAnhoAlbumes();
       // ArrayList list = getAlbumesPorAnho("todos");
       // ArrayList list = getEstilo("Todos", "todos");
       // ArrayList list = getCancionesEstilo("Todos", "Todos", "Todos");
     /*   System.out.println("\n\n");
        if (list.isEmpty()) {
            System.out.println("Lista vacia");
            return;
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
        }*/

    }

    public static void processIML(String XML) {
      //  System.out.println(XML);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); //
        dbf.setValidating(true); //
        DocumentBuilder db; //
        Document doc; //
        XML_DTD_ErrorHandler errorHandler = new XML_DTD_ErrorHandler();
        try {
            db = dbf.newDocumentBuilder(); //
            db.setErrorHandler(errorHandler); //
            doc = db.parse(XML);

            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("iml.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(doc));

            System.out.println("DOCUMENTO VALIDO: "+XML);

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
            System.out.println("DOCUMENTO INVALIDO: "+XML);

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
                listError.add("Error: " + e.getMessage());
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
                    Node an = nombreA.item(i).getNextSibling();
                    while (an.getNodeName().equals("#text")) {
                        an = an.getNextSibling();
                    }
                    String anho = an.getTextContent();
                    list.add(anho + "--" + albumes);
                }
            }
        }
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            String aux[] = list.get(i).split("--");
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
                        NodeList childNodes = canciones.item(j).getChildNodes(); //NODOS CANCION QUE COINCIDEN CON LOS PARAMETROS SELECCIONADOS
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
                    list.add(Anho + "--" + album);
                }
            }
        }
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            String aux[] = list.get(i).split("--");
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
