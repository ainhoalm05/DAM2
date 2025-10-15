/*package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Ejercicio4Escritura {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner (System.in);
		System.out.println("Introduce el nombre del fichero 1");
		String fichero1= sc.nextLine();
		System.out.println("Introduce el nombre del fichero 2");
		String fichero2= sc.nextLine();
		System.out.println("Introduce el nombre de la ruta");
		String ruta= sc.nextLine();
				
		File directorio = new File(ruta);
	
		if(directorio.isDirectory() && directorio.exists()) {
			File fichero1prog=new File(directorio,fichero1);
			File fichero2prog=new File(directorio,fichero2);
			String nombreNuevo=fichero1+" "+fichero2;
			File ficheroNuevo=new File(directorio,nombreNuevo);

			if(fichero1prog.exists() && fichero1prog.isFile() && fichero2prog.exists() && fichero2prog.isFile()) {
				try {
					if(!ficheroNuevo.exists()) {
						ficheroNuevo.createNewFile();
					}
					try {
					FileReader Lecturaf1 = new FileReader(fichero1prog);
					BufferedReader bufferLectura = new BufferedReader(Lecturaf1);
					
					String linea;
					
					while((linea=bufferLectura.readLine()!=null)) {
						System.out.println(linea);
					}
					}catch(IOException e) {
						e.printStackTrace();
					}
				}catch(IOException e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("Los ficheros no existen o son directorios");
			}
		
		}else {
			System.out.println("El directorio no existe");
		}
}
}*/
