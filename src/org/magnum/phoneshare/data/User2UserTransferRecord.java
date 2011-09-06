package org.magnum.phoneshare.data;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * A transfer between two users.
 * 
 * @author hamiltont
 * 
 */
@PersistenceCapable
public class User2UserTransferRecord extends TransferRecord {

	@Persistent
	Date mFromSignedDate;

	@Persistent
	Date mToSignedDate;

	@Override
	public boolean isTransferComplete() {
		return mToSignedDate != null;
	}
}
