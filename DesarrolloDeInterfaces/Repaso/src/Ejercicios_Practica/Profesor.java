package Ejercicios_Practica;

public class Profesor extends Persona{
	int salario = 0;
	int asignaturas = 0;
	boolean tutor = false;

	public Profesor(String dNI, String nombre, String apellidos, int salario, int asignaturas, boolean tutor) {
		super(dNI, nombre, apellidos);
		this.salario = salario;
		this.asignaturas = asignaturas;
		this.tutor = tutor;
	}
	public double getSalario() {
		return salario;
	}
	public void setSalario(int salario) {
		this.salario = salario;
	}

	public int getAsignaturas() {
		return asignaturas;
	}
	public void setAsignaturas(int asignaturas) {
		this.asignaturas = asignaturas;
	}
	public boolean isTutor() {
		return tutor;
	}
	public void setTutor(boolean tutor) {
		this.tutor = tutor;
	}

	@Override
	public String toString() {
		return "\n Profesor de "+super.toString()+" salario = "+salario+" asignaturas = " + asignaturas + ", tutor = " + tutor ;
	}


}
