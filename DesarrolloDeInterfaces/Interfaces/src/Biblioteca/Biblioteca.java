package Biblioteca;

public class Biblioteca {
	
	int codigo;
	String titulo;
	int anoPublicacion;
	
	public Biblioteca(int codigo, String titulo, int anoPublicacion) {
		super();
		this.codigo = codigo;
		this.titulo = titulo;
		this.anoPublicacion = anoPublicacion;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getAñoPublicacion() {
		return anoPublicacion;
	}

	public void setAñoPublicacion(int añoPublicacion) {
		this.anoPublicacion = añoPublicacion;
	}

	@Override
	public String toString() {
		return "Biblioteca [codigo=" + codigo + ", titulo=" + titulo + ", añoPublicacion=" + anoPublicacion + "]";
	}
	
	
}
