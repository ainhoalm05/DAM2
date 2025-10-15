package lanzarProceso3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneradorProceso3 {

	public void ejecutar(String ruta, String[] args) {
		List <String> nombreArgumentos = new ArrayList<>();
		if(ruta == null || ruta.isEmpty()) {
			System.out.println("Falta el nombre del comando");
			System.exit(1);
		}
		nombreArgumentos.add(ruta);
		
		for(int i=0;i<args.length;i++) {
			nombreArgumentos.add(args[i]);
		}
				
		//Clase para poder iniciar un proceso
		ProcessBuilder pb = new ProcessBuilder();
		
		pb.command(nombreArgumentos);

		// Llamada a inheritIO() que hace que el proceso herece la E/S del proceso padre
		// esto finalmente hace que veamos el resultado del comando
		pb.inheritIO();
		
		try {
			
			Process proceso = pb.start();
			int codigoRetorno = proceso.waitFor();//Esperamos a que el proceso termine para que no se solape con el "codigoRetorno" 
			System.out.println("-----------------------------");
			System.out.println("El comando devuelve:" + codigoRetorno);
			System.out.println("-----------------------------");

			if(codigoRetorno == 0) {
				System.out.println("Ejecucion correcta!!");
			}else {
				System.out.println("Ejecucion con errores");
			}
			
		}catch(IOException e) {
			System.out.println("Error durante la ejecucion del comando");
			System.out.println("Informacion adicional:");
			e.printStackTrace();
			System.exit(2);

		}catch(InterruptedException e){
			System.err.println("Proceso interrumpido");
		}
		}
}
