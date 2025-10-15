package Biblioteca;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class mainBiblioteca {

	public static void main(String[] args) {
		
		Scanner entrada=new Scanner(System.in);
		
		List <Libro> libros =new ArrayList();
		List <Revista> revistas =new ArrayList();

		Libro libro1=new Libro(12354,"El ratoncito Perez",1945,false);
		Libro libro2=new Libro(86761,"Cervantes",1921,false);
		
		Revista revista1=new Revista(32,"HOLA",2023,8);
		Revista revista2=new Revista(65,"EL MUNDO",2025,6);

		System.out.println(libros);
		System.out.println(revistas);
	}

	public static void prestarLibro() {
		Scanner entrada = new Scanner(System.in);
		
		System.out.println("Introduce el codigo del libro que quieres coger prestado: ");
	}
}
