package jugueteria;

public class Stand {
	int idStand, idZona;
	String nombre, descripcion;
	
	public Stand(int idStand, int idZona, String nombre, String descripcion) {
		super();
		this.idStand = idStand;
		this.idZona = idZona;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	public int getIdStand() {
		return idStand;
	}

	public void setIdStand(int idStand) {
		this.idStand = idStand;
	}

	public int getIdZona() {
		return idZona;
	}

	public void setIdZona(int idZona) {
		this.idZona = idZona;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "Stand [idStand=" + idStand + ", idZona=" + idZona + ", nombre=" + nombre + ", descripcion="
				+ descripcion + "]";
	}
	
	
}
