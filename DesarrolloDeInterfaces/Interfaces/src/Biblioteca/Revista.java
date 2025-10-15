package Biblioteca;

public class Revista extends Biblioteca{
	int numero;

	public Revista(int codigo, String titulo, int anoPublicacion, int numero) {
		super(codigo, titulo, anoPublicacion);
		this.numero = numero;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		return "Revista [numero=" + numero + "]";
	}

	
	
	
}
