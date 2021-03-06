package lejos.hardware.sensor;

import lejos.hardware.port.LegacySensorPort;
import lejos.hardware.port.Port;
import lejos.robotics.*;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 *  This class is used to obtain readings from a legacy RCX light sensor, using an adapter cable
 *  to connect it to the NXT brick. The light sensor can be calibrated to low and high values.
 *  Note: The RCX light sensor is not very sensitive when the floodlight is turned off: 
 *  Dark is around 523, sunlight is around 634. When the floodlight is on, dark readings are around
 *  155 and sunlight is around 713.
 * 
 */
public class RCXLightSensor extends AnalogSensor implements SensorConstants, LampController {
	
	private int _zero = 1023;
	private int _hundred = 0;
	private static final int SWITCH_DELAY = 10;
	
	private boolean floodlight = false;
	
	/**
	 * Create an RCX light sensor object attached to the specified port.
	 * The sensor will be activated, i.e. the LED will be turned on.
	 * @param port port, e.g. Port.S1
	 */
	public RCXLightSensor(Port pt)
	{
		super(pt);
		port.setTypeAndMode(TYPE_REFLECTION, MODE_RAW);
		setFloodlight(true); // Light sensitivity poor when off.
		
	}
	
	/**
	  * Activates an RCX light sensor. This method should be called
	  * if you want to get accurate values from an RCX
	  * sensor. In the case of RCX light sensors, you should see
	  * the LED go on when you call this method.
	  * @deprecated Use {@link #setFloodlight(boolean)} with true instead 
	  */
    @Deprecated
	public void activate()
	{
		setFloodlight(true);
	}
	
	/**
     * Passivates an RCX light sensor.
     * @deprecated Use {@link #setFloodlight(boolean)} with false instead 
	 */
    @Deprecated
	public void passivate()
	{
		setFloodlight(false);
	}
	
	/**
	 * Read the current sensor value.
	 * @return Value as a percentage.
	 * @deprecated Use {@link #getLightValue()} instead 
	 */
    @Deprecated
	public int readValue()
	{
		return getLightValue();
	}

	public int getFloodlight() {
		if(this.floodlight == true)
			return Color.RED;
		else
			return Color.NONE;
	}

	public boolean isFloodlightOn() {
		return floodlight;
	}

	public void setFloodlight(boolean floodlight) {
		this.floodlight = floodlight;
		if(floodlight == true)
			switchType(TYPE_REFLECTION, SWITCH_DELAY);
		else
	         switchType(TYPE_CUSTOM, SWITCH_DELAY);
;
	}

	public boolean setFloodlight(int color) {
		if(color == Color.RED) {
			setFloodlight(true);
			return true;
		} else if (color == Color.NONE) {
			setFloodlight(false);
			return true;
		} else return false;
	}
	
	public int getLightValue() {
		return ((1023 - NXTRawIntValue(port.getPin1())) * 100/ 1023); 
	}

	public int getNormalizedLightValue() {
		return 1023 - NXTRawIntValue(port.getPin1());
	}
	
	/**
	 * call this method when the light sensor is reading the low value - used by readValue
	 **/
		public void calibrateLow()
		{
			_zero = NXTRawIntValue(port.getPin1());
		}
	/** 
	 *call this method when the light sensor is reading the high value - used by readValue
	 */	
		public void calibrateHigh()
		{
			_hundred = NXTRawIntValue(port.getPin1());
		}
		/** 
		 * set the normalized value corresponding to readValue() = 0
		 * @param low the low value
		 */
		public void setLow(int low) { _zero = 1023 - low;}
		  /** 
	     * set the normalized value corresponding to  readValue() = 100;
	     * @param high the high value
	     */
	    public void setHigh(int high) { _hundred = 1023 - high;}
	   
	    public int getLow() { return 1023 - _zero;}
	    
	    public int  getHigh() {return 1023 - _hundred;}

}
