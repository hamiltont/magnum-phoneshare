package org.magnum.phoneshare.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;

/**
 * Info about a device that can be checked out, such as a smartphone
 * 
 * @author hamiltont
 * 
 */
@PersistenceCapable
public class Device {

	public enum Status {
		DEVICE_CHECKED_IN, DEVICE_CHECKED_OUT
	}

	@Persistent
	Status mStatus;

	/**
	 * The Google ID of the current user. Only valid if Device#mStatus ==
	 * Status.DEVICE_CHECKED_OUT
	 */
	@Persistent
	String mCurrentUserId;

	@Persistent
	String mDisplayName;
	
	@Persistent
	String mDescription;
	
	@Persistent
	String mSerial;
	
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Key mKey;

}
