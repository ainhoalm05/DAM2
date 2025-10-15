package hilosCooperando3;

public class Main {

		private static final int NUM_HILOS = 4;
		private static final int ZONAS = 10;

		public static void main(String[] args) {

			Encuesta cont = new Encuesta();
			Thread[] hilos = new Thread[NUM_HILOS];

			for (int i = 0; i < NUM_HILOS; i++) {
				Thread th = new Thread(new Hilo(i + 1, ZONAS / NUM_HILOS, cont));
				th.start();

				hilos[i] = th; // almacena la referencia de ese hilo en el array en la posición i
			}
			for (Thread h : hilos) {
				try {
					h.join(); // Bloquea el hilo principal hasta que h termine, es decir el main
				} catch (InterruptedException e) {
					System.out.println("Interrupcion del hilo");
					e.printStackTrace();
				}
			}

			cont.mostrarResultados();


	}

}
