package Ejercicios_Practica;

import java.util.Scanner;

public class Ejercicio5 {
	
	public static void main(String[] args) {
		Scanner entrada = new Scanner (System.in);
		int numero = 0;
		System.out.print("Introduce un número positivo: ");
		numero=entrada.nextInt();
		System.out.println(""+numero);
		System.out.println(" "+factorial(numero));
	}

	public static int factorial(int a) {
		if(a == 0) {
			return 1;
		}
		return a*factorial(a-1);
		
	}
}
