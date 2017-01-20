package com.line.trace;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;


/**
 * @author SUBASH
 *
 */
public class LineHelper {
	static LineValue lineValue = new LineValue();
	static LightSensor lightSensor = new LightSensor(SensorPort.S1);
	
	public LineHelper() throws InterruptedException {
		waitForUser("Show Line Bg Color");
		lineValue.setWhite(getThreshold());
		//waitForUser(null);

		waitForUser("Show Line Color");
		lineValue.setBlack(getThreshold());
		//waitForUser(null);
	}
	
	private synchronized void waitForUser(String message)
			throws InterruptedException {
		if (message != null) {
			LCD.drawString(message, 0, 2, false);
		}
		Sound.twoBeeps();
		Button.ESCAPE.waitForPressAndRelease();
	}

	private int getThreshold() {
		LineUtil util = new LineUtil();
		int value = util.getAvgColorValue();
		LCD.drawString("Value = "+value, 0, 3);
		return value;
	}

	/*private void initialize() {
		Thread cruiser = new Thread(new SubashPrg());
		cruiser.start();
	}*/
}