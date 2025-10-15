package main;
import java.io.*;
import java.util.Scanner;

public class Ejercicio1 {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce la ruta del directorio: ");
        String rutaDirectorio = scanner.nextLine();
        
 	   File directorio = new File(rutaDirectorio);
       if (directorio.exists() && directorio.isDirectory()) {
       	
       	String [] directoriotest = directorio.list();
       	
       	for(int i=0;i<directoriotest.length;i++) {
       		System.out.println(directoriotest[i]);
       	}
       }else {
       	System.out.println("No es un directorio");
       }
	}
	}


