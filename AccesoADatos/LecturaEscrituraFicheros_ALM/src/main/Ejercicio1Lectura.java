package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Ejercicio1Lectura {

	public static void main(String[] args) {
		File fichero = new File("FicheroEjercicio1.txt");
		if(!fichero.exists()) {
						
			//Crear el fichero
			try {
				fichero.createNewFile();
			}catch(IOException io){
				io.printStackTrace();
			}
	}
	try {
			
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
				System.out.print(linea);
			}
			lector.close();
			
		} catch (IOException e) {
			
			e.getMessage();
		}
	}

}
