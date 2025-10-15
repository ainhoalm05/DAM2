
package lanzarProceso4;

public class Principal {

	public static void main(String[] args) {
		String ruta = "ping" + " google.es";
		GeneradorProceso4 lanzador=new GeneradorProceso4();
		lanzador.ejecutar(ruta);
		System.out.println("Proceso ejecutado");
		
	}

}
