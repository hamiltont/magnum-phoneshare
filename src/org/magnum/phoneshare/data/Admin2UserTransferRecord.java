package org.magnum.phoneshare.data;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Admin2UserTransferRecord extends TransferRecord {
	
	@Persistent
	String mAdminGoogleId;
	
	@Persistent
	Date mToSignedDate;
	
	@Override
	public boolean isTransferComplete() {
		return mToSignedDate != null;
	}

}
