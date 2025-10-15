package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Ejercicio1Escritura {

	public static void main(String[] args) {
		String rutaFichero="primos.txt";
		File fichero = new File(rutaFichero);
		
		try {
			if (!fichero.exists()) {
				fichero.createNewFile();
			}
			//Abre el fichero de escritura
			FileWriter aperturaEscritura = new FileWriter(fichero);
			//Para hacer operaciones en el fichero
			BufferedWriter ficheroEscritura = new BufferedWriter(aperturaEscritura);
			
			for(int i = 2; i <= 500;i++) {
				if (esPrimo(i)) {
				ficheroEscritura.write(i);
				ficheroEscritura.newLine();
				}
			}
			ficheroEscritura.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static boolean esPrimo(int numero) {
		boolean primo=true;
		if(numero>2) {
			for (int i=2;i<=Math.sqrt(numero);i++) {
				if(numero % i==0) {
					primo = false;
				}
			}
		}
		return primo;
	}

}
