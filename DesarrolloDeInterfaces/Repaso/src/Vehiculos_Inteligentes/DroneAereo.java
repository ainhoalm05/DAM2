package Vehiculos_Inteligentes;

public class DroneAereo extends vehiculoInteligente{
	double altitudMaxima;
	int numeroHelices;
	double pesoCargaMax;
	boolean gpsIntegrado;
	String fabricante;
	
	
	public DroneAereo(int id, String modelo, boolean sensoresActivos, double bateriaNivel, String fechaActivacion,
			double altitudMaxima, int numeroHelices, double pesoCargaMax, boolean gpsIntegrado, String fabricante) {
		super(id, modelo, sensoresActivos, bateriaNivel, fechaActivacion);
		this.altitudMaxima = altitudMaxima;
		this.numeroHelices = numeroHelices;
		this.pesoCargaMax = pesoCargaMax;
		this.gpsIntegrado = gpsIntegrado;
		this.fabricante = fabricante;
	}


	public double getAltitudMaxima() {
		return altitudMaxima;
	}


	public void setAltitudMaxima(double altitudMaxima) {
		this.altitudMaxima = altitudMaxima;
	}


	public int getNumeroHelices() {
		return numeroHelices;
	}


	public void setNumeroHelices(int numeroHelices) {
		this.numeroHelices = numeroHelices;
	}


	public double getPesoCargaMax() {
		return pesoCargaMax;
	}


	public void setPesoCargaMax(double pesoCargaMax) {
		this.pesoCargaMax = pesoCargaMax;
	}


	public boolean isGpsIntegrado() {
		return gpsIntegrado;
	}


	public void setGpsIntegrado(boolean gpsIntegrado) {
		this.gpsIntegrado = gpsIntegrado;
	}


	public String getFabricante() {
		return fabricante;
	}


	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}


	@Override
	public String toString() {
		return "DroneAereo [altitudMaxima=" + altitudMaxima + ", numeroHelices=" + numeroHelices + ", pesoCargaMax="
				+ pesoCargaMax + ", gpsIntegrado=" + gpsIntegrado + ", fabricante=" + fabricante + "]";
	}
	
	
	
	
}
