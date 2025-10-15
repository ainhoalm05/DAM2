package hilosCooperando3;

public class Encuesta {

	int incrementaNoNulos = 0, incrementaNulos = 0;
	int votoPP = 0, votoPSOE = 0, votoVox = 0, votoSumar = 0;

	synchronized public void incrementoNoNulos() {
		incrementaNoNulos++;
	}

	synchronized public void incrementoNulos() {
		incrementaNulos++;
	}

	synchronized public void incrementoPP() {
		votoPP++;
	}

	synchronized public void incrementoPsoe() {
		votoPSOE++;
	}

	synchronized public void incrementoVox() {
		votoVox++;
	}

	synchronized public void incrementoSumar() {
		votoSumar++;
	}

	public void mostrarResultados() {

		System.out.println(" - Votaciones no nulas: " + incrementaNoNulos);
		System.out.println(" - Votaciones  nulas: " + incrementaNulos);
		System.out.println(" - Votaciones PP: " + votoPP);
		System.out.println(" - Votaciones PSOE: " + votoPSOE);
		System.out.println(" - Votaciones Vox: " + votoVox);
		System.out.println(" - Votaciones Sumar: " + votoSumar);

	}

}



