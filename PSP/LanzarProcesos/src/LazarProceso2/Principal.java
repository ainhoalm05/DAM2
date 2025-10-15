package LazarProceso2;


public class Principal {

	public static void main(String[] args) {
		String ruta = "C:/windows/system32";
		String nombre = "cmd.exe";
		GeneradorProceso2 lanzador = new GeneradorProceso2();
		lanzador.ejecutar(ruta, nombre);
		System.out.println("Proceso Ejectado!!!");

	}

}
