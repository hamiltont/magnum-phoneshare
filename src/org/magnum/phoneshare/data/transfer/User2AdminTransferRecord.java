package org.magnum.phoneshare.data.transfer;

import javax.jdo.annotations.PersistenceCapable;

import org.magnum.phoneshare.data.MagnumUser;

/**
 * Represents a device going from a user to an admin. Should only be creatable
 * by an admin, e.g. is always complete
 * 
 * @author hamiltont
 * 
 */
@PersistenceCapable
public class User2AdminTransferRecord extends TransferRecord {

	// TODO make admin have to verify this
	@Override
	public boolean isTransferComplete() {
		return true;
	}
	
	public MagnumUser getAdmin() {
		return mTo;
	}

}
