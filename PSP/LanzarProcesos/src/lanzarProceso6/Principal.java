package lanzarProceso6;

import lanzarProceso6.GeneradorProcesos6;

public class Principal {

	public static void main(String[] args) {
		String ruta = "notepad";
		String [] parametros= {"C:\\Users\\DAM\\eclipse-workspace\\PSP\\LanzarProcesos\\src\\lanzarProceso6\\ejemplo.txt"};
		/*String ruta = "cmd";
		String [] parametros= {"/c", "dir", "/p"};*/
		GeneradorProcesos6 lanzador = new GeneradorProcesos6();
		lanzador.ejecutar(ruta, parametros);
		System.out.println("Proceso Ejectado!!!");

	}

}
