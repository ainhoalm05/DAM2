package ficherosBinarios;

import java.io.Serializable;

public class Ejemplo1Persona implements Serializable{
	private String nombre;
	private int edad;
	
	public Ejemplo1Persona(String nombre, int edad) {
		super();
		this.nombre = nombre;
		this.edad = edad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	@Override
	public String toString() {
		return "Ejemplo1Persona [nombre=" + nombre + ", edad=" + edad + "]";
	}
	
	
}
