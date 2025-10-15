package ejemploLanzarHilo;

public class LanzaHilos {

		public static void main(String[] args) {
		
			//Hilo mihilo1 = new Hilo("H1");
			//Thread h1 = new Thread(mihilo1);
			
			//Creamos los hilos
			Thread h1 = new Thread(new Hilo("H1"));
			
			Hilo mihilo2 = new Hilo("H2");
			Thread h2 = new Thread(mihilo2);
			
			
			System.out.println("Hola");
			//Lanzamos los hilos
			h1.start();
			h2.start();
			
			//Mensaje del hilo principal
			System.out.println("Hilo principal terminado");
		}
		}

	


