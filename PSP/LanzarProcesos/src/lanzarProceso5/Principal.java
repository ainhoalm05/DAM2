package lanzarProceso5;

import lanzarProceso5.GeneradorProceso5;

public class Principal {

	public static void main(String[] args) {
		String ruta = "notepad";
		GeneradorProceso5 lanzador=new GeneradorProceso5();
		lanzador.ejecutaryDestruir(ruta);
		System.out.println("Proceso ejecutado");
		
	}

}