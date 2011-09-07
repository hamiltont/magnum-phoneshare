package org.magnum.phoneshare.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

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

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY) 
	Key mKey;
	
	@Persistent
	String googleID;

	@Persistent
	String phoneNum;

	@Persistent
	String fullname;
	
	@Persistent
	String mEmail;

	@Persistent
	boolean isAdmin = false;

	public MagnumUser(String googleId, String phone, String name, String email) {
		googleID = googleId;
		phoneNum = phone;
		fullname = name;
		mEmail = email;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}
	
	public String getGoogleId() {
		return googleID;
	}
	
	public String getName() {
		return fullname;
	}
	
	public String getEmail() {
		return mEmail;
	}
	
	public String getPhoneNumber() {
		return phoneNum;
	}
}
