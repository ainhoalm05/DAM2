package LazarProceso2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeneradorProceso2 {
		
		public void ejecutar(String rutaDirectorio, String nombreEjecutable) {
			List <String> nombreArgumentos = new ArrayList<>();
			nombreArgumentos.add(nombreEjecutable);
			File directorio = new File(rutaDirectorio);
			
			//Clase para poder iniciar un proceso
			ProcessBuilder pb = new ProcessBuilder(nombreArgumentos);
			
			pb.command(nombreEjecutable);
			pb.directory(directorio);
			try {
				pb.start();
			}catch(Exception e) {
				//Saca la pila del error
				e.printStackTrace();
			}
		
			}
		}
