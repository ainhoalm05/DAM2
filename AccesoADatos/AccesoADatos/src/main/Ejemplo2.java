package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

public class Ejemplo2 {

	public static void main(String[] args) {
		//Dentro de new File se pone la ruta del archivo o fichero
		File fichero = new File("FicheroEjemplo.txt");
		
		if(!fichero.exists()) {
			
				
			
				//Crear el fichero
				try {
					fichero.createNewFile();
				}catch(IOException io){
					io.printStackTrace();
				}
		}
		try {
				
				System.out.println("Nombre fichereo: "+fichero.getName());
				System.out.println("Ruta relativa: "+fichero.getPath());
				System.out.println("Ruta absoluta: "+fichero.getAbsolutePath());
				System.out.println("Tamaño: "+fichero.length());
				System.out.println("Lectura: "+fichero.canRead());
				System.out.println("Escritura: "+fichero.canWrite());
				System.out.println("Ejecucion: "+fichero.canExecute());
				
				//FileWriter escritor=new FileWriter(fichero);	
				BufferedWriter pw = new BufferedWriter(new FileWriter (fichero,true));
				
				for(int i = 0;i<10;i++) {
					pw.write("Linea: "+i);
					pw.newLine();
				}
				pw.close();
				//Devuelve caracter a caracter
				FileReader lector = new FileReader(fichero);
				//Lleno el buffer de los caracteres y leo lineas
				BufferedReader buffer= new BufferedReader(lector);
				String linea;
				
				while((linea=buffer.readLine())!=null) {
					System.out.println(linea);
				}
				lector.close();
				
			} catch (IOException e) {
				
				e.getMessage();
			}finally {
				System.out.println("Adios");
			}
			
			
	}

}
