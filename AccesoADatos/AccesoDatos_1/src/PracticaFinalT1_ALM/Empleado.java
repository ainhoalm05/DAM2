package PracticaFinalT1_ALM;

import java.io.Serializable;

public class Empleado implements Serializable{
	private int id;
	private String nombre;
	private String password;
	private String cargo;	 
	
	public Empleado(int id, String nombre, String password, String cargo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.password = password;
		this.cargo = cargo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	@Override
	public String toString() {
		return "Empleado [identificacion=" + id + ", nombre=" + nombre + ", password=" + password
				+ ", cargo=" + cargo + "]";
	}
}
