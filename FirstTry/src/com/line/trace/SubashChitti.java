package com.line.trace;

import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.RotateMoveController;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.PilotProps;

/**
 * @author SUBASH
 *
 */
public class SubashChitti {
	
	static LineHelper line;
	int color;
	static int white, whiteMean;
	static int black, blackMean;
	static int threshold = 0;
	static int TURN_POWER = 22;
	static int DRIVE_POWER = 25;
	int leftTurn;
	int rightTurn;
	
	public void setColor(int color) {
		this.color = color;
	}

	public void setLeftTurn(int leftTurn) {
		this.leftTurn = leftTurn;
	}

	public void setRightTurn(int rightTurn) {
		this.rightTurn = rightTurn;
	}
	
	public int getColor() {
		color = LineUtil.getAvgColorValue();
		return color;
	}
	
	public int getLeftTurn() {
		return leftTurn;
	}

	public int getRightTurn() {
		return rightTurn;
	}

	public static LineHelper getColorValue(){
		try 
		{
			line = new LineHelper();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return line;
	}

	public int getCorrection(int power){
		color = LineUtil.getAvgColorValue();
		return 4 * power * (threshold-color)/(whiteMean - blackMean);
	}

	public static void main(String[] args) {
		
		/*PilotProps pp = new PilotProps();
    	try {
			pp.loadPersistentValues();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	float wheelDiameter = Float.parseFloat(pp.getProperty(PilotProps.KEY_WHEELDIAMETER, "5.6"));
    	float trackWidth = Float.parseFloat(pp.getProperty(PilotProps.KEY_TRACKWIDTH, "14.0"));
    	RegulatedMotor leftMotr = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, "A"));
    	RegulatedMotor rightMotr = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, "B"));
    	boolean reverse = Boolean.parseBoolean(pp.getProperty(PilotProps.KEY_REVERSE,"false"));
		
    	final DifferentialPilot pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotr, rightMotr, reverse);
    	pilot.setRotateSpeed(80);*/
		
		NXTMotor leftMotor = new NXTMotor(MotorPort.A);
		NXTMotor rightMotor = new NXTMotor(MotorPort.B);
				
		/*Get the Color Values of the background and Line color*/
		line = getColorValue();
		white = line.lineValue.getWhite();
		black = line.lineValue.getBlack();
		
		threshold = (white + black) / 2;
		whiteMean = (white + threshold) / 2;
		blackMean = (black + threshold) / 2;
		
		SubashChitti instance = new SubashChitti();
		
		Behavior InLine = new Behavior() {
			private boolean suppress = false;
			
			public boolean takeControl(){
				return instance.getColor() >= blackMean && instance.getColor() <= whiteMean ;
			}
			public void suppress(){
				suppress = true;
			}
			public void action(){
				while (!suppress && instance.getColor() >= blackMean && instance.getColor() <= whiteMean) {
					int error = instance.getCorrection(DRIVE_POWER);
					LCD.clear();
					instance.setLeftTurn(DRIVE_POWER - error);
					leftMotor.setPower(Math.abs(instance.getLeftTurn()));
					leftMotor.forward();
					
					instance.setRightTurn(DRIVE_POWER + error);
					rightMotor.setPower(Math.abs(instance.getRightTurn()));
					rightMotor.forward();
					Thread.yield();
				}
				suppress = false;
			}
		};
		
		Behavior OffLine = new Behavior() {
			
			private boolean suppress = false;
			
			public boolean takeControl(){
				return instance.getColor() > whiteMean;
			}
			public void suppress(){
				suppress = true;
			}
			public void action(){
		
				while (!suppress && instance.getColor() > whiteMean) {
					int error = instance.getCorrection(TURN_POWER);
					instance.setLeftTurn(TURN_POWER - error);
					leftMotor.setPower(Math.abs(instance.getLeftTurn()/3));
					if(instance.getLeftTurn() > 0)
						leftMotor.forward();
					else
						leftMotor.backward();
					
					instance.setRightTurn(TURN_POWER + error);
					rightMotor.setPower(Math.abs(instance.getRightTurn()/2));
					if(instance.getRightTurn() > 0)
						rightMotor.forward();
					else
						rightMotor.backward();
					Thread.yield();
				}
				suppress = false;
			}
		};
		
		Behavior Overshoot = new Behavior(){
			
			private boolean suppress = false;
			
			public boolean takeControl(){
				return instance.getColor() < blackMean ;
			}
			public void action(){
				
				while(!suppress){
					/*pilot.rotate(-360, true);*/
					while(!suppress && instance.getColor() < blackMean){
					int error = instance.getCorrection(TURN_POWER);
					instance.setLeftTurn(TURN_POWER - error);
					leftMotor.setPower(Math.abs(instance.getLeftTurn()/2));
					if(instance.getLeftTurn() > 0)
						leftMotor.forward();
					else
						leftMotor.backward();
					
					instance.setRightTurn(TURN_POWER + error);
					rightMotor.setPower(Math.abs(instance.getRightTurn()/3));
					if(instance.getRightTurn() > 0)
						rightMotor.forward();
					else
						rightMotor.backward();
					Thread.yield();
					}
				}
				/*pilot.stop();*/
				suppress = false;
			}
			public void suppress(){
				suppress = true;
			}
		};
		
		Behavior Stop = new Behavior(){
			
			private boolean suppress = false;
			
			public boolean takeControl(){
				return Button.ESCAPE.isDown();
			}
			public void action(){
				suppress=false;
				System.exit(0);
			}
			public void suppress(){
				suppress = true;
			}
		};
		
		Behavior[] behaviors = {Overshoot, OffLine, InLine, Stop};
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.start();

		/*LCD.drawString("HAI", 0, 0);
		leftMotor.forward();
		rightMotor.forward();
		Button.waitForAnyPress();*/
		
	}
}
