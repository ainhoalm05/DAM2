package PracticaFinalT1_ALM;

import java.io.Serializable;

public class Empleado implements Serializable{
	private static final long serialVersionUID = 9144443773906433590L;
	int identificacion;
	String nombre;
	String password;
	String cargo;
	public Empleado(int id, String nombre, String passwd, String cargo) {
		super();
		this.identificacion = id;
		this.nombre = nombre;
		this.password = passwd;
		this.cargo = cargo;
	}
	@Override
	public String toString() {
		return "Empleado [id=" + identificacion + ", nombre=" + nombre + ", passwd=" + password + ", cargo=" + cargo + "]";
	}
	public int getId() {
		return identificacion;
	}
	public void setId(int id) {
		this.identificacion = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String passwd) {
		this.password = passwd;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
}