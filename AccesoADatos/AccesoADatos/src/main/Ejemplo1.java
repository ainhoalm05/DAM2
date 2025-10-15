package main;
import java.io.*;

public class Ejemplo1 {

	public static void main(String[] args) {
		//Dentro de new File se pone la ruta del archivo o fichero
		File fichero = new File("FicheroEjemplo.txt");
		
		if(fichero.exists()) {
			
			System.out.println("Nombre fichereo: "+fichero.getName());
			System.out.println("Ruta relativa: "+fichero.getPath());
			System.out.println("Ruta absoluta: "+fichero.getAbsolutePath());
			System.out.println("Tamaño: "+fichero.length());
			System.out.println("Lectura: "+fichero.canRead());
			System.out.println("Escritura: "+fichero.canWrite());
			System.out.println("Ejecucion: "+fichero.canExecute());

			try {
				FileWriter escritor=new FileWriter(fichero);	
			} catch(IOException w){
				
			}
			
			
			try {
				//Devuelve caracter a caracter
				FileReader lector = new FileReader(fichero);
				//Lleno el buffer de los caracteres y leo lineas
				BufferedReader buffer= new BufferedReader(lector);
				String linea;
				
				while((linea=buffer.readLine())!=null) {
					System.out.println(linea);
				}
				
			} catch (IOException e) {
				
				e.getMessage();
			}
			
			
			//fichero.close();
		}else {
			System.out.println("No existe el fichero");
			//Crear el fichero
			try {
				fichero.createNewFile();
			}catch(IOException io){
				io.printStackTrace();
			}
		}
	}

}
