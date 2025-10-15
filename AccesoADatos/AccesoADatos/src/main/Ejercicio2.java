package main;

import java.io.File;
import java.util.Scanner;

public class Ejercicio2 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce la ruta del fichero: ");
       
        
        File fichero = new File(scanner.nextLine());
		
    	if(fichero.exists() && fichero.isFile()) {
    				
    		System.out.println("Fichero existe, Borrando Fichero…");
    		fichero.delete();
    				
    	}else {
    		
    		System.out.println("Fichero no existe");
    			
    	}

	}

}
