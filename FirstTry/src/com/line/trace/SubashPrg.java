package com.line.trace;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;

/**
 * @author Subash
 *
 */
/*class Light{
	
	final int maxSamples = 10;
	int white, black;
	int currentColor;
	LightSensor light;
	
	public Light(SensorPort port){
		light = new LightSensor(port);
		currentColor = 0;
	}
	
	public int getWhite() {
		return white;
	}
	public int getBlack() {
		return black;
	}
	public void setWhite(){
		int result = 0;
		for(int i=0; i<maxSamples; i++){
			result += light.getLightValue();
		}
		result /= maxSamples;
		LCD.drawInt(result, 0, 1);
		white = result;
	}
	public void setBlack(){
		int result = 0;
		for(int i=0; i<maxSamples; i++){
			result += light.getLightValue();
		}
		result /= maxSamples;
		LCD.drawInt(result, 0, 1);
		black = result;
	}
	public int getColorInput(){
		currentColor = light.getLightValue();
		return currentColor;
	}
}

class Engine{
	NXTRegulatedMotor leftMotor, rightMotor;
	Light light;
	
	public Engine(NXTRegulatedMotor m1, NXTRegulatedMotor m2, Light light){
		leftMotor = m1;
		rightMotor = m2;
		this.light = light;
	}
	
	public void setLeftSpeed(int speed){
		leftMotor.setSpeed(speed);
	}
	public void setRightSpeed(int speed){
		rightMotor.setSpeed(speed);
	}
	
	public void drive(){
		LCD.clear();
		LCD.drawString("Cruiser Running", 0, 0);
		while(!Button.ESCAPE.isDown()){
			light.getColorInput();
			//if(light.currentColor)
		}
	}
	public void turnLeft(){
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(180);
	}
	public void turnRight(){
		leftMotor.setSpeed(180);
		rightMotor.setSpeed(0);
	}
}*/
public class SubashPrg {

	public static void main(String[] args) {
		/*Light light = new Light(SensorPort.S1);
		LCD.clear();
		LCD.drawString("Show White", 0, 0);
		Button.waitForAnyPress();
		light.setWhite();
		Button.waitForAnyPress();
		
		LCD.clear();
		LCD.drawString("Show Black", 0, 0);
		Button.waitForAnyPress();
		light.setBlack();
		Button.waitForAnyPress();
		
		Engine engine = new Engine(Motor.A, Motor.B, light);
		engine.setLeftSpeed(360);
		engine.setRightSpeed(360);
		engine.drive();*/
		
		NXTMotor mB = new NXTMotor(MotorPort.B);
		NXTMotor mC = new NXTMotor(MotorPort.A);	
		int color;
		int cTurn;
		int bTurn;
		int power = 22;
		int threshold;
		
		int kp = 1, ki = 0, kd = 0;
		int lastError = 0, error =0, integral = 0, der = 0 ;
		int correct = 0;
		
		try {
			LineHelper line = new LineHelper();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while (!Button.ESCAPE.isDown()) {
			threshold = (LineHelper.lineValue.getBlack() + LineHelper.lineValue.getWhite())/2;
			color = LineUtil.getAvgColorValue();
			
			error = threshold - color;
			integral = error + integral;
			der = error - lastError;
			//correct = kp * error + ki * integral + kd * der;
			
			correct = 44 * (threshold-color)/(44-38);
			cTurn = power - correct;
			mC.setPower(Math.abs(cTurn));
		    //Motor.A.setSpeed(500);
			if(cTurn<0){
				mC.stop();
				mC.backward();
			}else{
				mC.forward();
				//Button.waitForAnyPress();
			}

			bTurn = power + correct;
			mB.setPower(Math.abs(bTurn));
			//Motor.B.setSpeed(500);
			if(bTurn>0)
				mB.forward();
			else
				mB.backward();
		}
	}
}