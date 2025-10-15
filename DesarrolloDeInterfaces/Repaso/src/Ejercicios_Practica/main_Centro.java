package Ejercicios_Practica;

import java.util.ArrayList;
import java.util.List;

public class main_Centro {

	public static void main(String[] args) {
		
		List <Profesor> profesores =new ArrayList();
		List <Administracion> administradores =new ArrayList();
		List <Directivo> directivos =new ArrayList();
		List <Alumno> alumnos =new ArrayList();


		
		Profesor profesor1 = new Profesor("10923y","Paco","Rodriguez",2000,4,true);
		profesores.add(profesor1);
		Profesor profesor2 = new Profesor("10923435t","Maria","Caridad",2500,5,false);
		profesores.add(profesor2);

		Administracion administrador1 = new Administracion("8932474g","Manuel"," Marezcutti",3000,"ADE",10);
		administradores.add(administrador1);
		Directivo directivo1 = new Directivo("932840h","Jose","Ramos", 3500,true,false);
		directivos.add(directivo1);
		Modulo[] modulos = {
				 new Modulo(null, 0, profesor2, false),
				 new Modulo(null, 0, profesor1, false)			
		};
		Alumno alumno1 = new Alumno("1234545r", "Carlos", "Ruiz", "10/06/2007", "Masculino", false, modulos );
		alumnos.add(alumno1);
		Alumno alumno2 = new Alumno("1234545r", "Laura", "Muñoz", "20/09/2007", "Femenino", false, modulos );
		alumnos.add(alumno2);

		profesores.sort(null);//Oredena de menor a mayor el ArrayList de los profesores
		System.out.println(profesores);
		System.out.println("La persona que mas cobra: "+profesores.get(profesores.size()-1));//Cpgemos la ultima posicion del array ya que sabemos que el ultimo es el que mas cobra
		//System.out.println(alumno1);
		//System.out.println(alumno2);
		
		
	}

}
