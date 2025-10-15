package Ejercicios_Practica;

public class Directivo extends Persona{
	int salario = 0;
	boolean Salesiano = true;
	boolean Turno = true;

	
	public Directivo(String dNI, String nombre, String apellidos, int salario, boolean salesiano, boolean turno) {
		super(dNI, nombre, apellidos, salario);
		this.salario = salario;
		Salesiano = salesiano;
		Turno = turno;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(int salario) {
		this.salario = salario;
	}

	public boolean isSalesiano() {
		return Salesiano;
	}
	public void setSalesiano(boolean salesiano) {
		Salesiano = salesiano;
	}
	public boolean isTurno() {
		return Turno;
	}
	public void setTurno(boolean turno) {
		Turno = turno;
	}
	@Override
	public String toString() {
		return "Directivo de "+super.toString()+" salario = "+salario+" es Salesiano = " + Salesiano + ", Turno = " + Turno;
	}
	
}
