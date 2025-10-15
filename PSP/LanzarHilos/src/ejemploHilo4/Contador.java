package ejemploHilo4;

public class Contador {
	private long cont1 = 0;
	private long cont2 = 0;
	
	private final Object lock1 = new Object();
	private final Object lock2 = new Object();
	
	public void incremetar1() {
		synchronized(lock1) {
			cont1++;
		}
	}
	
	public void incremetar2() {
		synchronized(lock2) {
			cont2++;
		}
	}

	public long getContador1() {
		synchronized(lock1) {
			return cont1;
		}
	}
	public long getContador2() {
		synchronized(lock2) {
			return cont2;
		}
	}

}
