package lanzarProceso6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;



public class GeneradorProcesos6 {
	public void ejecutar (String ruta,  String[] args) {
		List <String> nombreArgumentos = new ArrayList<>();
		Process proceso = null;
		
		nombreArgumentos.add(ruta);
		
		for(int i=0;i<args.length;i++) {
			nombreArgumentos.add(args[i]);
		}

		ProcessBuilder pb = new ProcessBuilder(ruta);
		
		pb.command(nombreArgumentos);

		pb.inheritIO();
		
		try {
			proceso = pb.start();
			System.out.println("Se ha lanzado el proceso");
			System.out.println("El proceso padre esperara a que el hijo termine su ejecucion");
			
			
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		try {
			proceso.waitFor(5000, TimeUnit.MILLISECONDS);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		// Sacar el valor de retorno del proceso
		// Destruir manualmente con destroy
		/*/
		try {
			System.out.println(proceso.exitValue());
		
		}catch(IllegalThreadStateException e) {
			System.out.println(e);
		}*/
		if (proceso!=null) {
			proceso.destroy();
			System.out.println("El proceso hijo se destruye");
		}
	}
}
