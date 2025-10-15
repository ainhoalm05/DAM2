package ejemplo3Hilos;

public class Hilo implements Runnable{
	int numHilo, miParte, miCuenta = 0;
	private final Contador cont;
	Hilo ( int numHilo, int miParte, Contador c){
		this.numHilo=numHilo;
		this.miParte=miParte;
		this.cont=c;
	}
	synchronized public int getCuenta() {
		return miCuenta;
	}
	
	@Override
	public void run() {
		for(int i=0;i<miParte;i++) {
			this.cont.incrementar();//Incrementar el contador compartido (este no se tiene porque ejecutar 100 veces)
			miCuenta++;//Este va a ser 100 de verdad(se ejecuta 100 veces)
		}
		System.out.printf("Hilo %d lo damos por terminado, cuenta: %d\n",numHilo,getCuenta());
	}
}
