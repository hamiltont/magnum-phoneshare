package org.magnum.phoneshare.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.magnum.phoneshare.data.transfer.TransferRecord;

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
	};

	@Persistent
	List<TransferRecord> mTransferHistory;

	@Persistent
	Status mStatus;

	@Persistent
	MagnumUser mCurrentUser;

	@Persistent
	String mModel;

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
		mModel = model;
		mTransferHistory = new ArrayList<TransferRecord>(0);
	}

	public String getModel() {
		return mModel;
	}

	public String getLengthOfCheckOut() {
		return "Some time";
	}

	public String getSerial() {
		return mSerial;
	}

	public MagnumUser getCurrentUser() {
		if (mCurrentUser == null)
			return new MagnumUser("", "", "Admin", "");
		return mCurrentUser;
	}

	public Integer getStatus() {
		return mStatus.ordinal();
	}
	
	public String getStatusString() {
		switch (mStatus) {
		case DEVICE_CHECKED_IN:
			return "Checked In";
		case DEVICE_CHECKED_OUT:
			return "Checked Out";
		default:
			return "Error!";
		}
	}

	public void addTransferRecord(TransferRecord record) {
		if (mTransferHistory.size() == 0)
			mTransferHistory.add(record);

		if (mTransferHistory.get(mTransferHistory.size() - 1)
				.isTransferComplete())
			mTransferHistory.add(record);

		throw new IllegalStateException(
				"Attempting to add a new transfer record when the prior one is not complete");
	}

	public void persist() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(this);
	}

	// TODO add in the ability for admin's to return phones to a specific room
	// e.g. durham 357
	public String getLocation() {
		if (mTransferHistory.size() == 0)
			return "Checked In";
		TransferRecord tr = mTransferHistory.get(mTransferHistory.size() - 1);
		if (tr.isTransferComplete() == false) {
			return tr.getFrom().getName() + " -> " + tr.getTo().getName();
		}

		return tr.getTo().getName();
	}

	/**
	 * Returns the amount of time the phone has been in it's current location in
	 * a human-readable string format
	 * 
	 * @return
	 */
	public String getHowLong() {
		if (mTransferHistory.size() == 0)
			return "Always";

		TransferRecord tr = mTransferHistory.get(mTransferHistory.size() - 1);
		if (tr.isTransferComplete() == false) {
			TransferRecord prev = mTransferHistory
					.get(mTransferHistory.size() - 1);
			if (prev == null)
				return "Always";
			else
				return Integer.toString(daysBetween(prev.getFinalizedDate(),
						new GregorianCalendar().getTime()))
						+ " days";
		} else
			return Integer.toString(daysBetween(tr.getFinalizedDate(),
					new GregorianCalendar().getTime()))
					+ " days";

	}

	public int daysBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	public List<TransferRecord> getTransferRecords() {
		return mTransferHistory;
	}

}
