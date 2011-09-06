package org.magnum.phoneshare.data;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Represents a device going from a user to an admin. Should only be creatable
 * by an admin, e.g. is always complete
 * 
 * @author hamiltont
 * 
 */
@PersistenceCapable
public class User2AdminTransferRecord extends TransferRecord {

	@Persistent
	String mAdminGoogleId;

	@Override
	public boolean isTransferComplete() {
		return true;
	}

}
