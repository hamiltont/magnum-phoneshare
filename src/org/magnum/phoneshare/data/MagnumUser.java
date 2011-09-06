package org.magnum.phoneshare.data;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Some extra user information on top of the default Google user info. This
 * extra info is useful for contacting users if they have a phone out too long
 * and for controlling their permissions on the magnum website
 * 
 * @author hamiltont
 * 
 */
@PersistenceCapable
public class MagnumUser {

	@Persistent
	@PrimaryKey
	String googleID;

	@Persistent
	String phoneNum;

	@Persistent
	String fullname;

	@Persistent
	boolean isAdmin = false;

	public MagnumUser(String googleId, String phone, String name) {
		googleID = googleId;
		phoneNum = phone;
		fullname = name;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}
	
	public String getGoogleId() {
		return googleID;
	}
}
