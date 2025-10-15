package ejemplo3Hilos;

import java.util.ArrayList;

public class Principal {
	private static final int NUM_HILOS=10;
	private static final int CUENTA_TOTAL=100000;

	public static void main(String[] args) {
		Contador contCompartido = new Contador();
		Thread [] hilos = new Thread[NUM_HILOS];//Array de hilos
		
		for(int i = 0; i<NUM_HILOS;i++) {
			Thread th=new Thread(new Hilo(i, CUENTA_TOTAL/NUM_HILOS, contCompartido));//Creamos el hilo
			th.start();//Lanzamos el hilo
			hilos[i]=th;//Lo guardamos en nuestro array
		}
		for(Thread h : hilos) {
			try {
				h.join();
			}catch(InterruptedException e) {
				System.out.println("Exception por interrupcion");
			}
		}
		System.out.printf("Cuenta global: %s\n", contCompartido.getCuenta());
	}

}


// 