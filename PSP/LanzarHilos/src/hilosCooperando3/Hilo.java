
package hilosCooperando3;

public class Hilo implements Runnable {

	int voto;
	int partido;
	int zona;
	int numeroHilos = 0;
	Encuesta cont;
	int miCuenta;

	public Hilo(int zona, int numeroHilos, Encuesta cont) {
		super();
		this.voto = ((int) Math.random() * 2); // 0 vota, 1 no vota
		this.partido = ((int) Math.random() * 4); // 4 partidos 0 pp, 1 psoe, 2 vox, 3 sumar
		this.zona = zona;
	}

	public int getMiCuenta() {
		return miCuenta;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		if (voto == 1) {
			cont.incrementoNoNulos();
			if (partido == 0) {
				cont.incrementoPP();
			} else if (partido == 1) {
				cont.incrementoPsoe();
			} else if (partido == 2) {
				cont.incrementoVox();
			} else if (partido == 3) {
				cont.incrementoSumar();
			}
		} 

		System.out.println("Zona: " + zona );

	}

}
