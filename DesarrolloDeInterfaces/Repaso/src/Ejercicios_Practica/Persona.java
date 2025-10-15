package Ejercicios_Practica;

public class Persona implements Comparable<Persona>{

	String DNI = "";
	String Nombre = "";
	String Apellidos = "";
	double Salario;
	
	
	public Persona(String dNI, String nombre, String apellidos, double salario) {
		super();
		DNI = dNI;
		Nombre = nombre;
		Apellidos = apellidos;
		Salario = salario;
	}

	public Persona(String dNI, String nombre, String apellidos) {
		super();
		DNI = dNI;
		Nombre = nombre;
		Apellidos = apellidos;
	}
	
	public String getDNI() {
		return DNI;
	}

	public void setDNI(String dNI) {
		DNI = dNI;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public String getApellidos() {
		return Apellidos;
	}

	public void setApellidos(String apellidos) {
		Apellidos = apellidos;
	}

	
	public double getSalario() {
		return Salario;
	}

	public void setSalario(double salario) {
		Salario = salario;
	}

	@Override
	public int compareTo(Persona p) {
		if(Salario<p.getSalario())
			return 1;
		if(Salario>p.getSalario())
			return -1;
		
		return 0;
	}

	@Override
	public String toString() {
		return "DNI = " + DNI + ", Nombre = " + Nombre + ", Apellidos = " + Apellidos  ;
	}
	
}
