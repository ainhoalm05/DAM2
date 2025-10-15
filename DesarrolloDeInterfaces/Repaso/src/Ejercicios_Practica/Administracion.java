package Ejercicios_Practica;

public class Administracion extends Persona{
	int salario = 0;
	String Estudios = "";
	int Antiguedad = 0;

	public Administracion(String dNI, String nombre, String apellidos, int salario, String estudios, int antiguedad) {
		super(dNI, nombre, apellidos, salario);
		this.salario = salario;
		Estudios = estudios;
		Antiguedad = antiguedad;
	}
	public double getSalario() {
		return salario;
	}
	public void setSalario(int salario) {
		this.salario = salario;
	}
	public String getEstudios() {
		return Estudios;
	}
	public void setEstudios(String estudios) {
		Estudios = estudios;
	}
	public int getAntiguedad() {
		return Antiguedad;
	}
	public void setAntiguedad(int antiguedad) {
		Antiguedad = antiguedad;
	}
	@Override
	public String toString() {
		return "Administracion de "+super.toString()+" salario = "+salario+" Estudios = " + Estudios + ", Antiguedad = " + Antiguedad ;
	}


}
