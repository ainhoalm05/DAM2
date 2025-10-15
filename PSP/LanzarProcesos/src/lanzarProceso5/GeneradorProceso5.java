package lanzarProceso5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class GeneradorProceso5 {
	public void ejecutaryDestruir (String ruta) {
		Process proceso = null;
		ProcessBuilder pb = new ProcessBuilder(ruta);
		
		
		try {
			proceso = pb.start();
			System.out.println("Se ha lanzado el proceso");
			System.out.println("El proceso padre esperara a que el hijo termine su ejecucion");
			
			
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		try {
			proceso.waitFor();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		// Sacar el valor de retorno del proceso
		// Destruir manualmente con destroy
		try {
			System.out.println(proceso.exitValue());
		
		}catch(IllegalThreadStateException e) {
			System.out.println(e);
		}
		if (proceso!=null) {
			proceso.destroy();
			System.out.println("El proceso hijo se destruye");
		}
	}
}
