package Ejercicios_Practica;

import java.util.Scanner;

public class Ejercicio4 {

	public static void main(String[] args) {
			Scanner scan = new Scanner (System.in);
			int matX = 0;
			int matY = 0;
			System.out.print("Introduce el tamaño de la matriz en el eje X: ");
			matX=scan.nextInt();
			System.out.print("Introduce el tamaño de la matriz en el eje Y: ");
			matY=scan.nextInt();
			for(int i = 0; i < matX; i++) {
				System.out.print(" "+Math.floor(Math.random()*9));
				for(int j = 1; j < matY; j++) {
					System.out.print(" "+Math.floor(Math.random()*9));
				}
				System.out.println("");
			}
		}
	}


