package Ejercicios_Practica;

import java.util.Arrays;

public class Alumno extends Persona{
	String Fnacimiento = "";
	String sexo = "";
	boolean Repetidor = false;
	Modulo[] modulo;
		
	public Alumno(String dNI, String nombre, String apellidos, String fnacimiento, String sexo, boolean repetidor,
			Modulo[] modulo) {
		super(dNI, nombre, apellidos);
		Fnacimiento = fnacimiento;
		this.sexo = sexo;
		Repetidor = repetidor;
		this.modulo = modulo;
	}

	public Modulo[] getModulo() {
		return modulo;
	}
	public void setModulo(Modulo[] modulo) {
		this.modulo = modulo;
	}
	public String getFnacimiento() {
		return Fnacimiento;
	}
	public void setFnacimiento(String fnacimiento) {
		Fnacimiento = fnacimiento;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public boolean isRepetidor() {
		return Repetidor;
	}
	public void setRepetidor(boolean repetidor) {
		Repetidor = repetidor;
	}
	
	@Override
	public String toString() {
		return "Alumnode de "+super.toString()+" Fnacimiento = " + Fnacimiento + ", sexo = " + sexo + ", Repetidor = " + Repetidor + ", modulo = "
				+ Arrays.toString(modulo);
	}
	
	
}
