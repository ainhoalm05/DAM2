package ejemplo2Hilos;

import java.util.Random;

public class Hilo implements Runnable{
	private final String nombre;
	Hilo (String nombre){
		this.nombre=nombre;
	}
	public void run() {
		System.out.printf("Hola soy el hilo %s. \n", nombre );
		for(int i=0;i<5;i++) {
			Random r = new Random();
			int pausa = 20 + r.nextInt(500-20);
			System.out.printf("Hilo %s hace pausa %d ms. \n", this.nombre,pausa);//this.nombre se utiliza cuando hay ambigüedad
			
			try {
				Thread.sleep(pausa);
			}catch(InterruptedException ie){
				System.out.printf("Hilo interrumpido %s",nombre);
			}
		}
	}
}
