package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Ejercicio3 {

	public static void main(String[] args) {
		Scanner entrada = new Scanner (System.in);
		
		System.out.println("Introduce el nombre del directorio a buscar");
		String dir = entrada.nextLine();
		File directorio = new File(dir);
		          
		if (directorio.exists() && directorio.isDirectory()) {
		       	
		System.out.println("Introduce el nombre del fichero a crear");
		String fich =entrada.nextLine();
		File fichero2 = new File(fich);
		       	
			try {
				System.out.println("Se ha creado el fichero");
				fichero2.createNewFile();
						
			}catch(IOException io){
				io.printStackTrace();
			}
		}else {
		       	System.out.println("No es un directorio");
		}


	}

}
