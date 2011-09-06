package org.magnum.phoneshare.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.io.protocol.proto.ProtocolDescriptor.DeclaredType;

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
	};

	@Persistent
	Status mStatus;

	/**
	 * The Google ID of the current user. Only valid if Device#mStatus ==
	 * Status.DEVICE_CHECKED_OUT
	 */
	@Persistent
	String mCurrentUserId = "";

	@Persistent
	String mDisplayName;
	
	@Persistent
	String mDescription;
	
	@Persistent
	@PrimaryKey
	String mSerial;
	
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	Key mKey;
	
	public Device(String model, String serial) {
		mStatus = Status.DEVICE_CHECKED_IN;
		mSerial = serial;
		mDisplayName = model;
	}
	
	public String getDisplayName() {
		return mDisplayName;
	}
	
	public String getLengthOfCheckOut() {
		return "Some time";
	}
	
	public String getSerial() {
		return mSerial;
	}
	
	public String getCurrentUserGoogleId() {
		return mCurrentUserId;
	}
	
	public Integer getStatus() {
		return mStatus.ordinal();
	}

}
