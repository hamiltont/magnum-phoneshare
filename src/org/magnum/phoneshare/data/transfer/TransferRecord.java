package org.magnum.phoneshare.data.transfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.magnum.phoneshare.data.MagnumUser;

import com.google.appengine.api.datastore.Key;

/**
 * Format for all records of phones changing hands. Must be a subclass to be
 * used, this just provides all the cross-subclass data members and a common
 * supertype
 * 
 * @author hamiltont
 * 
 */
@PersistenceCapable
public abstract class TransferRecord {

	public enum Accessories {
		NONE, CHARGER, TRANSFER_CABLE, BOTH
	}

	@Persistent
	Key mPhone;

	@Persistent
	MagnumUser mFrom;

	@Persistent
	MagnumUser mTo;

	@Persistent
	Date mDateCreated;

	@Persistent
	Date mDateFinalized;

	@Persistent
	List<Accessories> mAccessoriesIncluded = new ArrayList<Accessories>();

	/**
	 * Signals if the transfer is in an incomplete state, which will typically
	 * cause the nightly cron to inspect this transfer and see if and action
	 * such as mEmail is required.
	 * 
	 * @return true if this transfer is complete, false if this transfer is
	 *         incomplete (perhaps only one of the two parties has agreed that
	 *         this transfer occurred)
	 */
	public abstract boolean isTransferComplete();

	public MagnumUser getFrom() {
		return mFrom;
	}

	public MagnumUser getTo() {
		return mTo;
	}

	public Date getFinalizedDate() {
		return mDateFinalized;
	}

	public String getFinalizedDateString() {
		return mDateFinalized.toLocaleString();
	}

	public boolean hasAccessories() {
		return mAccessoriesIncluded.size() > 0;
	}
	
	public void setTo(MagnumUser toUser) {
		mTo = toUser;
	}
	
	public void setFrom(MagnumUser fromUser) {
		mFrom = fromUser;
	}
}
