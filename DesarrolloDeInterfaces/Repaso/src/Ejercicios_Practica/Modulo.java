package Ejercicios_Practica;

public class Modulo {
	String Nombre="";
	int Horas = 0;
	Profesor Profesor;
	boolean Convalidable = true;
	
	public Modulo(String nombre, int horas, Profesor string, boolean convalidable) {
		super();
		Nombre = nombre;
		Horas = horas;
		Profesor = string;
		Convalidable = convalidable;
	}
	
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public int getHoras() {
		return Horas;
	}
	public void setHoras(int horas) {
		Horas = horas;
	}
	public Profesor getProfesor() {
		return Profesor;
	}
	public void setProfesor(Profesor profesor) {
		Profesor = profesor;
	}
	public boolean isConvalidable() {
		return Convalidable;
	}
	public void setConvalidable(boolean convalidable) {
		Convalidable = convalidable;
	}

	@Override
	public String toString() {
		return "Modulo Nombre = " + Nombre + ", Horas = " + Horas + ", Profesor = " + Profesor + ", Convalidable = "
				+ Convalidable;
	}
	
}
