package org.magnum.phoneshare.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnum.phoneshare.data.Device;
import org.magnum.phoneshare.data.MagnumUser;
import org.magnum.phoneshare.data.PMF;
import org.magnum.phoneshare.data.Device.Status;
import org.magnum.phoneshare.data.jspmodels.AvailablePhonesResult;
import org.magnum.phoneshare.data.jspmodels.CheckedOutPhonesResult;
import org.magnum.phoneshare.data.jspmodels.PhoneDBResult;
import org.magnum.phoneshare.data.jspmodels.UserPhoneResult;
import org.magnum.phoneshare.data.transfer.Admin2UserTransferRecord;
import org.magnum.phoneshare.data.transfer.TransferRecord;
import org.magnum.phoneshare.data.transfer.User2AdminTransferRecord;
import org.magnum.phoneshare.data.transfer.User2UserTransferRecord;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("unchecked")
@Controller
public class WebController {

	// TODO - If I'm clever I can simply return the List<Device> and process it
	// properly inside the JSP instead of copying everything
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
		modview.addObject("isAdminUser", user.getIsAdmin()
				|| userService.isUserAdmin());

		// Query all the phones this user has
		Query userPhones = pm.newQuery(Device.class);
		userPhones.setFilter("mStatus == status && mCurrentUser == user");
		userPhones
				.declareImports("import org.magnum.phoneshare.data.MagnumUser;");
		userPhones.declareParameters("Integer status, MagnumUser user");
		List<Device> phones = (List<Device>) userPhones.executeWithArray(
				Status.DEVICE_CHECKED_OUT, user);
		ArrayList<UserPhoneResult> userPhoneResult = new ArrayList<UserPhoneResult>();
		for (Device d : phones) {
			UserPhoneResult up = new UserPhoneResult();
			up.model = d.getModel();
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
		Query availPhones = pm.newQuery(Device.class);
		availPhones.setFilter("mStatus == stat");
		availPhones.declareParameters("Integer stat");
		phones = (List<Device>) availPhones.execute(Status.DEVICE_CHECKED_IN);

		ArrayList<AvailablePhonesResult> availPhoneResult = new ArrayList<AvailablePhonesResult>();
		for (Device d : phones) {
			AvailablePhonesResult ap = new AvailablePhonesResult();
			ap.location = "Foo";
			ap.model = d.getModel();
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
		ArrayList<CheckedOutPhonesResult> outPhoneResult = new ArrayList<CheckedOutPhonesResult>();
		for (Device d : phones) {
			CheckedOutPhonesResult op = new CheckedOutPhonesResult();
			op.model = d.getModel();
			op.to = d.getCurrentUser().getName();
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
				.getUserId(), phone, name, userService.getCurrentUser()
				.getEmail());

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(user);
		} finally {
			pm.close();
		}

		return "registration_result";
	}

	// TODO why can I see this page as an admin when I'm not logged in
	@RequestMapping(value = "/phonedb", method = RequestMethod.GET)
	public ModelAndView phoneDatabase(Model m) {
		if (isPhoneShareAdmin() == false)
			return new ModelAndView("error");

		ModelAndView mv = new ModelAndView("phonedb");
		mv.addObject("isAdminUser", true);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query phoneQuery = pm.newQuery(Device.class);
		List<Device> phones = (List<Device>) phoneQuery.execute();
		List<PhoneDBResult> outResult = new ArrayList<PhoneDBResult>();
		for (Device d : phones) {
			PhoneDBResult pd = new PhoneDBResult();
			pd.location = d.getLocation();
			pd.model = d.getModel();
			pd.time = d.getHowLong();
			pd.serial = d.getSerial();
			outResult.add(pd);
		}
		mv.addObject("phone", outResult);

		return mv;
	}

	@RequestMapping(value = "/register_phone", method = RequestMethod.GET)
	public String showRegisterPhoneForm(
			@RequestParam(value = "message", required = false) String message) {
		if (message != null)
			System.out.println("MEssage is " + message);

		return "register_phone";
	}

	@RequestMapping(value = "/register_phone", method = RequestMethod.POST)
	public String handleRegisterPhoneForm(@RequestParam("model") String model,
			@RequestParam("serial") String serial) {

		// Ensure no phone with that serial exists
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query search = pm.newQuery(Device.class);
		search.setFilter("mSerial == serial");
		search.declareParameters("String serial");
		List<Device> devices = (List<Device>) search.execute(serial.trim());
		if (devices.size() != 0)
			return "error";

		Device d = new Device(model, serial);
		pm.makePersistent(d);

		return "redirect:/register_phone?message=Success";
	}

	/**
	 * Checks if the current user is a phone-share admin
	 * 
	 * @return true if the user is logged in and is a phone-share admin, false
	 *         otherwise
	 */
	private boolean isPhoneShareAdmin() {
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
		if (user == null)
			return false;

		if (us.isUserAdmin())
			return true;

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query findUser = pm.newQuery(MagnumUser.class);
		findUser.setFilter("googleID == gid");
		findUser.declareParameters("String gid");
		List<MagnumUser> results = (List<MagnumUser>) findUser.execute(user
				.getUserId());
		if (results.size() != 1)
			return false;

		return true;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ModelAndView getUsers() {
		if (!isPhoneShareAdmin())
			return new ModelAndView("error");

		ModelAndView mv = new ModelAndView("users");
		mv.addObject("isAdminUser", true);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query userQuery = pm.newQuery(MagnumUser.class);
		mv.addObject("users", (List<MagnumUser>) userQuery.execute());

		return mv;
	}

	@RequestMapping(value = "/device/{serial}", method = RequestMethod.GET)
	public ModelAndView viewDeviceInfo(@PathVariable("serial") String serial) {

		if (false == isPhoneShareAdmin())
			return new ModelAndView("error");

		// Query / add device info
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query deviceQuery = pm.newQuery(Device.class);
		deviceQuery.setFilter("mSerial == serial");
		deviceQuery.declareParameters("String serial");
		List<Device> deviceResult = (List<Device>) deviceQuery.execute(serial);

		if (deviceResult.size() != 1)
			return new ModelAndView("error");

		Device phone = deviceResult.get(0);

		ModelAndView mv = new ModelAndView("device");
		mv.addObject("isAdminUser", true);
		mv.addObject("phone", phone);

		// Query / add current user info
		UserService us = UserServiceFactory.getUserService();
		Query cuQuery = pm.newQuery(MagnumUser.class);
		cuQuery.setFilter("googleID == gid");
		cuQuery.declareParameters("String gid");
		MagnumUser user = ((List<MagnumUser>) cuQuery.execute(us
				.getCurrentUser().getUserId())).get(0);
		mv.addObject("user", user);

		// Query / add all user info
		Query uQuery = pm.newQuery(MagnumUser.class);
		List<MagnumUser> users = (List<MagnumUser>) uQuery.execute();
		mv.addObject("users", users);

		return mv;
	}

	@RequestMapping(value = "/transfer")
	public ModelAndView initiateOrUpdatePhoneTransfer(
			@RequestParam("from") String fromId,
			@RequestParam("to") String toId, @RequestParam("type") String type, 
			@RequestParam("serial") String serial) {

		// Ensure that both from and to are phoneshare users
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query uq = pm.newQuery(MagnumUser.class);
		uq.setFilter("googleID == user || googleID == other");
		uq.declareParameters("String user, String other");
		List<MagnumUser> users = (List<MagnumUser>) uq.execute(fromId, toId);
		if (users.size() != 2 || (users.size() == 1 && fromId.equals(toId)))
			return new ModelAndView("error");
		MagnumUser from, to;
		if (users.get(0).getGoogleId().equals(fromId)) {
			from = users.get(0);
			to = users.get(1);
		} else {
			from = users.get(1);
			to = users.get(1);
		}

		// Build the transfer
		TransferRecord tr = null;
		if (type.equals("admin2user"))
			tr = new Admin2UserTransferRecord();
		else if (type.equals("user2user"))
			tr = new User2UserTransferRecord();
		else if (type.equals("user2admin"))
			tr = new User2AdminTransferRecord();
		else
			return new ModelAndView("error");
		tr.setTo(to);
		tr.setFrom(from);

		// TODO add in email option
		
		// TODO ensure a device exists with that serial
		
		// TODO The currently logged in user should be detected to limit the
		// update options
		pm.makePersistent(tr);
		

		return new ModelAndView("transfer");
	}

}
