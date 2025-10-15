package main;

import java.io.File;
import java.util.Scanner;

public class Ejercicio4 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
	       System.out.print("Introduce la ruta del directorio: ");
	       String rutaDirectorio = scanner.nextLine();
	      
			 File directorio = new File("Ejercicio1");
			
			 if (directorio.exists() && directorio.isDirectory()) {
			       	
			       	String [] listaDirectorio= directorio.list();
			       	
			       	for(int i=0;i<listaDirectorio.length;i++) {
			       		System.out.println(listaDirectorio[i]);
			       	}
			       }else {
			       	System.out.println("No es un directorio");
			       }

	}

}
