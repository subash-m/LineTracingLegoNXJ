package com.temp.test;

import java.util.Scanner;

/**
 * @author Subash
 *
 */
public class Test {
	public static void main(String[] args) {
		int white=47, black = 35;
		int threshold=0, color=0;
		int kp = 3, ki = 1, kd = 0;
		int lastError = 0, error =0, integral = 0, der = 0 ;
		int power=22, temp=0, correct = 0;
		int cTurn=0, bTurn=0;
		
		Scanner sin  = new Scanner(System.in);

		while(color != -1){
			threshold = (white + black)/2;
			System.out.println("Threshold = "+threshold);
			color = sin.nextInt();
			correct = 44*(threshold - color)/(44 - 38);
			
			error = threshold - color;
			integral = error + integral;
			der = error - lastError;
			
			//correct = kp * error + ki * integral + kd * der;
			
			//System.out.println("Temp = "+temp);
			cTurn = power - correct;
			bTurn = power+correct;
			lastError = error;
			System.out.println("Left Turn = "+cTurn);
			System.out.println("Right Turn = "+bTurn);
		}
	}

}
