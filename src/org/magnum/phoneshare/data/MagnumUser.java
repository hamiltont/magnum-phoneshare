package org.magnum.phoneshare.data;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

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
	String googleID;
	
	@Persistent
	String phoneNum;
	
	@Persistent
	boolean isAdmin;
}
