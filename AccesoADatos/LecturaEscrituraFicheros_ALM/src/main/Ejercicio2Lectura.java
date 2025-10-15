package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Ejercicio2Lectura {

	public static void main(String[] args) {
		File fichero = new File("FicheroEjercicio2.txt");
		
	try {
			
			FileReader lector = new FileReader(fichero);
			//FileWriter escritor=new FileWriter(fichero);	
			BufferedReader buffer = new BufferedReader(lector);
			String linea;
			
			
			int contador = 0;
			int contadorVocales = 0;
			
			while((linea=buffer.readLine())!=null) {
				for(int i = 0; i<linea.length();i++) {
					if (linea.charAt(i) == 'a' || linea.charAt(i) == 'e' || linea.charAt(i) == 'i' || linea.charAt(i) == 'o' || linea.charAt(i) == 'u') {
					contadorVocales++;
					}
				}
				for(int i = 0; i<linea.length();i++) {
					if(linea.charAt(i)!= ' ') {
						contador++;
					}
					
				}
			}
			System.out.println("Vocales: "+contadorVocales);
			System.out.println("Letras: "+(contador-1));

			lector.close();
			
		} catch (IOException e) {
			
			e.getMessage();
		}
	}

	}


