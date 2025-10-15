
package lanzarProceso4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GeneradorProceso4 {
	public void ejecutar (String ruta) {
		Runtime rt = Runtime.getRuntime();//Es lo mismo que el ProcessBuilder
		Process proceso = null;
		String line;
		
		try {
			proceso = rt.exec(ruta);//Hijo del proceso
			BufferedReader br = 
					new BufferedReader(new InputStreamReader(proceso.getInputStream()));
			while((line=br.readLine())!=null) {
				System.out.println(line);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		if(proceso!=null) {
			proceso.destroy();
		}
		try {
			proceso.waitFor();
		}catch(InterruptedException e) {
			System.exit(-2);
		}
		System.exit(0);
		
	}
}
