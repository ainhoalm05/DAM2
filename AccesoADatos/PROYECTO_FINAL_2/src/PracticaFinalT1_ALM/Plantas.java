package PracticaFinalT1_ALM;

public class Plantas {
	String nombre,foto,descripcion;
	int codigo,stock;
	float precio;
	
	public Plantas(int codigo,String nombre, String foto, String descripcion,  float precio, int stock) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.foto = foto;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		
	}
	
	
	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getFoto() {
		return foto;
	}


	public void setFoto(String foto) {
		this.foto = foto;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}


	@Override
	public String toString() {
		return "plantasClass nombre=" + nombre + ", foto=" + foto + ", descripcion=" + descripcion + ", codigo="
				+ codigo + ", stock=" + stock + ", precio=" + precio;
	}

}
