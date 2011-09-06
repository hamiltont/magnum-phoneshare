package org.magnum.phoneshare.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.magnum.phoneshare.data.Device;
import org.magnum.phoneshare.data.MagnumUser;
import org.magnum.phoneshare.data.PMF;
import org.magnum.phoneshare.data.Device.Status;
import org.magnum.phoneshare.data.jspmodels.AvailablePhonesResult;
import org.magnum.phoneshare.data.jspmodels.CheckedOutPhonesResult;
import org.magnum.phoneshare.data.jspmodels.UserPhoneResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Controller
public class WebController {

	// TODO - If I'm clever I can simply return the List<Device> and process it
	// properly inside the JSP instead of copying everything
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView getHome(Model model) {
		// Ensure logged in
		UserService userService = UserServiceFactory.getUserService();
		if (false == userService.isUserLoggedIn())
			return new ModelAndView("error");

		// If user is not in the magnum users DB, redirect them
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query findUser = pm.newQuery(MagnumUser.class);
		findUser.setFilter("googleID == gid");
		findUser.declareParameters("String gid");
		List<MagnumUser> results = (List<MagnumUser>) findUser
				.execute(userService.getCurrentUser().getUserId());
		if (results.size() == 0)
			return new ModelAndView("redirect:register");
		else if (results.size() != 1)
			return new ModelAndView("error");

		// Set the isUserAdmin flag
		MagnumUser user = results.get(0);
		ModelAndView modview = new ModelAndView("home");
		modview.addObject("isUserAdmin", user.getIsAdmin()
				|| userService.isUserAdmin());

		// Query all the phones this user has
		Query userPhones = pm.newQuery(Device.class);
		userPhones.setFilter("mStatus == status && mCurrentUserId == gid");
		userPhones.declareParameters("Integer status, String gid");
		List<Device> phones = (List<Device>) userPhones.executeWithArray(
				Status.DEVICE_CHECKED_OUT, user.getGoogleId());
		ArrayList<UserPhoneResult> userPhoneResult = new ArrayList<UserPhoneResult>(
				phones.size());
		for (Device d : phones) {
			UserPhoneResult up = new UserPhoneResult();
			up.model = d.getDisplayName();
			up.checked_out = d.getLengthOfCheckOut();
			up.serial = d.getSerial();
			userPhoneResult.add(up);
		}
		modview.addObject("userphones", userPhoneResult);

		// Query all available phones
		// TODO make the location work for admins, fix query count / location
		// Query availPhones = pm.newQuery(Device.class);
		// availPhones.setFilter("status == stat");
		// availPhones.declareParameters("Integer stat");
		// availPhones.setResult("count(displayName), displayName");
		// availPhones.setGrouping("displayName");

		// phones = (List<Device>)
		// availPhones.execute(Status.DEVICE_CHECKED_IN);
		phones = new ArrayList<Device>();
		ArrayList<AvailablePhonesResult> availPhoneResult = new ArrayList<AvailablePhonesResult>(
				phones.size());
		for (Device d : phones) {
			AvailablePhonesResult ap = new AvailablePhonesResult();
			ap.location = "Foo";
			ap.model = d.getDisplayName();
			ap.quantity = "5";
			availPhoneResult.add(ap);
		}
		modview.addObject("availphones", availPhoneResult);

		// Query all non-current-user checked out phones
		Query outPhones = pm.newQuery(Device.class);
		phones = null;
		if (user.getIsAdmin()) {
			outPhones.setFilter("mStatus == stat");
			outPhones.declareParameters("Integer stat");
			phones = (List<Device>) outPhones
					.execute(Status.DEVICE_CHECKED_OUT);
		} else {
			outPhones.setFilter("mStatus == stat && mCurrentUserId != gid");
			outPhones.declareParameters("Integer stat, String gid");
			phones = (List<Device>) outPhones.execute(
					Status.DEVICE_CHECKED_OUT, user.getGoogleId());
		}
		ArrayList<CheckedOutPhonesResult> outPhoneResult = new ArrayList<CheckedOutPhonesResult>(
				phones.size());
		for (Device d : phones) {
			CheckedOutPhonesResult op = new CheckedOutPhonesResult();
			op.model = d.getDisplayName();
			op.to = d.getCurrentUserGoogleId();
			op.when = "last week?";
			outPhoneResult.add(op);
		}
		modview.addObject("outphones", outPhoneResult);

		return modview;
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerNewUser(Model m) {
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerNewUser(@RequestParam("name") String name,
			@RequestParam("phone") String phone, Model m) {

		UserService userService = UserServiceFactory.getUserService();

		// Ensure user is logged in
		if (userService.isUserLoggedIn() == false)
			return "error";

		MagnumUser user = new MagnumUser(userService.getCurrentUser()
				.getUserId(), phone, name);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(user);
		} finally {
			pm.close();
		}

		return "registration_result";
	}
}
