package org.magnum.phoneshare.data;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

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
	String mFromGoogleId;

	@Persistent
	String mToGoogleId;

	@Persistent
	Date mDateCreated;

	@Persistent
	Accessories mAccessoriesIncluded;

	/**
	 * Signals if the transfer is in an incomplete state, which will typically
	 * cause the nightly cron to inspect this transfer and see if and action
	 * such as email is required.
	 * 
	 * @return true if this transfer is complete, false if this transfer is
	 *         incomplete (perhaps only one of the two parties has agreed that
	 *         this transfer occurred)
	 */
	public abstract boolean isTransferComplete();
}
