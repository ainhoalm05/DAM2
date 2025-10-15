package Vehiculos_Inteligentes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		
		List <CocheAutonomo> cochesA = new ArrayList<>();
		List <DroneAereo> dron = new ArrayList<>();
		List <RobotRepartidor> robot = new ArrayList<>();

		
		
		Scanner entrada = new Scanner(System.in);
		
		System.out.println("Cantidad de coches: ");
		int n = entrada.nextInt();
		
		for(int i=0;i<n;i++) {

			System.out.println();
			
			System.out.println();
			
			System.out.println();
			
			System.out.println();
			
			System.out.println();
			
			System.out.println();
			
			System.out.println();
			
			CocheAutonomo cocheAutonomo = new CocheAutonomo();
			cochesA.add(cocheAutonomo);
			
		}

	}

}
