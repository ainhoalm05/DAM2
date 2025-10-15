package Proceso;

import java.util.ArrayList;
import java.util.List;

public class GenerardorProceso {
	public void ejecutar() {
		List <String> nombreArgumentos = new ArrayList<>();
		nombreArgumentos.add("C:/MyCode/Sumar2Numeros.exe");
		nombreArgumentos.add("18");
		nombreArgumentos.add("20");
		
		//Clase para poder iniciar un proceso
		ProcessBuilder pb = new ProcessBuilder();
		try {
			Process proceso = pb.start();
			//pb.start();
		}catch(Exception e) {
			//Saca la pila del error
			e.printStackTrace();
		}
	}
}
