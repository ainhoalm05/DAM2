package ejemploXLM;

import org.w3c.dom.*;
import java.io.File;
import javax.xml.parsers.*;

public class Ejemplo1 {

	public static void main(String[] args) {
		try {
			File ficheroXML = new File ("persona.xml");
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docB = dbf.newDocumentBuilder();
			
			Document doc = docB.parse(ficheroXML);
			
			//Normaliza el documento eliminando los espacios y los saltos de linea
			doc.getDocumentElement().normalize();
			
			NodeList lista = doc.getElementsByTagName("persona");
			
			int cantidad = lista.getLength();
			
			for(int i=0;i<cantidad;i++) {
				Node nodo=lista.item(i);
				
				if(nodo.getNodeType() == Node.ELEMENT_NODE) {
					Element persona = (Element)nodo;
					String nombre = persona.getElementsByTagName("nombre").item(0).getTextContent();
					String edad = persona.getElementsByTagName("edad").item(0).getTextContent();
					String ciudad = persona.getElementsByTagName("ciudad").item(0).getTextContent();
					System.out.println("La persona es: "+nombre+" La edad: "+edad+" La ciudad: "+ciudad);
				}
			}
		}catch(Exception e) {
			
		}

	}

}
