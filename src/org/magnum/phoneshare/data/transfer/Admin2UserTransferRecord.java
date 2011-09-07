package org.magnum.phoneshare.data.transfer;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.magnum.phoneshare.data.MagnumUser;

@PersistenceCapable
public class Admin2UserTransferRecord extends TransferRecord {
	
	@Persistent
	Date mToSignedDate;
	
	@Override
	public boolean isTransferComplete() {
		return mToSignedDate != null;
	}
	
	public MagnumUser getAdmin() {
		return mFrom;
	}
	
	/**
	 * Allows an admin user to manually set the acceptance date
	 * 
	 * @param s
	 */
	public void manuallySetSignedDate(String s) {
		try {
			mToSignedDate = DateFormat.getInstance().parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
