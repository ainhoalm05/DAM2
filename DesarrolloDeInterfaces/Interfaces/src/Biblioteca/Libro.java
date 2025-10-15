package Biblioteca;

public class Libro extends Biblioteca{
	boolean prestar;

	public Libro(int codigo, String titulo, int anoPublicacion, boolean prestar) {
		super(codigo, titulo, anoPublicacion);
		prestar = false;
	}

	public boolean isPrestar() {
		return prestar;
	}

	public void setPrestar(boolean prestar) {
		this.prestar = prestar;
	}

	public void prestar(Libro libroPrestar) {
		prestar=true;
		
		
	}
	
	public void devolver(Libro libroDevolver) {
		prestar=false;
		//if(codigo==libroDevolver.getCodigo() && libroDevolver.prestar==true) {
		//	libroDevolver.setPrestar(false);
		//}else {
		//	System.out.println("Te has equivocado de libro");;
		//}
	}
	
	public boolean prestado(Libro libroPrestado) {
	
	return prestar;
	}
	
	@Override
	public String toString() {
		return "\nLibro prestar=" + prestar ;
	}
	
	
}
