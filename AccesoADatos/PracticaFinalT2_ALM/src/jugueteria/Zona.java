package jugueteria;

public class Zona {
	int idZona;
	String nombre, descripcion;
	
	public Zona(int idZona, String nombre, String descripcion) {
		super();
		this.idZona = idZona;
		this.nombre = nombre;
		this.descripcion = descripcion;
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
		return "Zona [idZona=" + idZona + ", nombre=" + nombre + ", descripcion=" + descripcion + "]";
	}
	
		
}
