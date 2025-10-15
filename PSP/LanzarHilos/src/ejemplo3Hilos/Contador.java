package ejemplo3Hilos;

public class Contador {
	public int cuenta = 0;
	
	public int getCuenta() {
		return cuenta;
	}
	
	synchronized public int incrementar() {
		this.cuenta++;
		return cuenta;
	}
}
