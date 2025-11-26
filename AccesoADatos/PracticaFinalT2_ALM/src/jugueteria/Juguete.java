package jugueteria;

public class Juguete {
	int idJuguete,stock;
	String nombre, descripcion;
	float precio;
	enum categoria{
		Pelotas,
		Muñecas,
		Vehiculos
	}
	categoria categorias;
	
	public Juguete(int idJuguete, int stock, String nombre, String descripcion, float precio, categoria categorias) {
		super();
		this.idJuguete = idJuguete;
		this.stock = stock;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.categorias = categorias;
	}

	public int getIdJuguete() {
		return idJuguete;
	}

	public void setIdJuguete(int idJuguete) {
		this.idJuguete = idJuguete;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
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

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public categoria getCategorias() {
		return categorias;
	}

	public void setCategorias(categoria categorias) {
		this.categorias = categorias;
	}

	@Override
	public String toString() {
		return "Juguete [idJuguete=" + idJuguete + ", stock=" + stock + ", nombre=" + nombre + ", descripcion="
				+ descripcion + ", precio=" + precio + ", categorias=" + categorias + "]";
	}
	
	
}
