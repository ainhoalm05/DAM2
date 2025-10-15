package lanzarProceso3;


public class Principal {
	public static void main(String[] args) {
		String ruta = "ipconfig";
		String [] parametros= {"/all"};
		GeneradorProceso3 lanzador = new GeneradorProceso3();
		lanzador.ejecutar(ruta, parametros);
		System.out.println("Proceso Ejectado!!!");

	}
}