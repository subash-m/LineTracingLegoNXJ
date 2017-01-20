package com.line.trace;

/**
 * @author SUBASH
 *
 */
public class LineUtil {
	private final static int NUMBER_OF_SAMPLES = 10;
	
	public static int getAvgColorValue(){
		int sum = 0;
		
		for(int i=0; i<NUMBER_OF_SAMPLES; i++){
			sum += LineHelper.lightSensor.getLightValue();
		}
		return sum/NUMBER_OF_SAMPLES;
	}
}