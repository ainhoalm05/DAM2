package JavaNIO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

public class Ejemplo1 {
	public static void leerFiles() {
		//Creamos la ruta
		Path ruta = Paths.get("FicheroEjercicio1.txt");
		try{
			//Leer todo el contenido
			String contenido = Files.readString(ruta);
			System.out.println(contenido);
			
			//Leer linea a linea
			List <String> listaContenido = Files.readAllLines(ruta);
			for(String linea:listaContenido) {
				System.out.println(linea);
			}
			
			
		}catch(IOException e) {
			e.getMessage();
		}
	}
	//Escribir fichero
	public static void escrituraFiles() {
		Path ruta = Paths.get("FicheroEscritura.txt");
		try {
			String contenido = "Hola esta es mi primera escritura";
			Files.write(ruta, contenido.getBytes(StandardCharsets.UTF_8),StandardOpenOption.CREATE,StandardOpenOption.WRITE);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	//Copiar un archivo a otro
	public static void copiarFiles() {
		Path rutaOrigen = Paths.get("FicheroEscritura.txt");
		Path rutaDestino = Paths.get("FicheroCopia.txt");
		
		try {
			Files.copy(rutaOrigen, rutaDestino,StandardCopyOption.REPLACE_EXISTING );
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//Listar los contenidos de un directorio
	public static void listarFiles() {
		//Path.of == Paths.get
		Path directorio = Path.of(".");
		try {
			Stream <Path> flujo = Files.list(directorio);
			flujo.forEach(archivo -> System.out.println(archivo.getFileName()));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//Leer propiedades de un archivo
	public static void leerPropiedadesFiles() {
		Path ruta = Path.of("pirmos.txt");
		System.out.println("Fichero existe: "+ Files.exists(ruta));
		System.out.println("Fichero es directorio: "+ Files.isDirectory(ruta));
	}
	
	//Borrar archivo
	public static void borrarFiles() {
		//Path.of == Paths.get
		Path ruta = Path.of("primos.txt");
		try {
			Files.deleteIfExists(ruta);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		leerFiles();
		escrituraFiles();
		copiarFiles();
		listarFiles();
	}

}
