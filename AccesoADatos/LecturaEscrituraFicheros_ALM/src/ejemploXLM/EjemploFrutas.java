package ejemploXLM;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EjemploFrutas {

	public static void main(String[] args) {
		 ArrayList<fruta> frutas = new ArrayList<>();

	        try {
	            // Cargar el archivo XML
	            File ficheroXML = new File("frutas.xml");
	           

	            // Crear el parser XML
	            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder docB = dbf.newDocumentBuilder();
	            Document doc = docB.parse(ficheroXML);

	            // Normalizar el documento
	            doc.getDocumentElement().normalize();

	            // Obtener la lista de elementos <fruta>
	            NodeList listaFrutas = doc.getElementsByTagName("fruta");

	            // Recorrer las frutas
	            for (int i = 0; i < listaFrutas.getLength(); i++) {
	                Node nodoFruta = listaFrutas.item(i);

	                if (nodoFruta.getNodeType() == Node.ELEMENT_NODE) {
	                    Element frutaElem = (Element) nodoFruta;

	                    // Obtener datos básicos
	                    String nombre=frutaElem.getElementsByTagName("nombre").item(0).getTextContent().trim();
	                    String tipo=frutaElem.getElementsByTagName("tipo").item(0).getTextContent().trim();
	                    String color=frutaElem.getElementsByTagName("color").item(0).getTextContent().trim();
	                    String origen=frutaElem.getElementsByTagName("origen").item(0).getTextContent().trim();
	                    String precio=frutaElem.getElementsByTagName("precio").item(0).getTextContent().trim();
	                    String temporada=frutaElem.getElementsByTagName("temporada").item(0).getTextContent().trim();
	                    
	                    // Crear objeto Fruta
	                    fruta fruta = new fruta(nombre, tipo, color, origen, precio, temporada);

	                    // Obtener nutrientes dentro de la fruta
	                    NodeList listaNutrientes = frutaElem.getElementsByTagName("nutriente");
	                    for (int j = 0; j < listaNutrientes.getLength(); j++) {
	                        Node nodoNutriente = listaNutrientes.item(j);
	                        if (nodoNutriente.getNodeType() == Node.ELEMENT_NODE) {
	                            String nutriente = nodoNutriente.getTextContent().trim();
	                            fruta.agregarNutriente(nutriente);
	                        }
	                    }

	                    // Añadir a la lista
	                    frutas.add(fruta);
	                }
	            }

	            // Mostrar todas las frutas
	            for (fruta f : frutas) {
	                System.out.println(f);
	                System.out.println("-----------------------------------");
	            }

	        } catch (Exception e) {
	            System.out.println("Error al procesar el XML:");
	            e.printStackTrace();
	        }
	    }

	

	}


