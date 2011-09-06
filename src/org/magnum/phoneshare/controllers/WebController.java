package org.magnum.phoneshare.controllers;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.magnum.phoneshare.data.MagnumUser;
import org.magnum.phoneshare.data.PMF;
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView getHome(Model model) {
		// If user is not logged in, redirect to login
		UserService userService = UserServiceFactory.getUserService();
		if (false == userService.isUserLoggedIn())
			// TODO create login URL and send to JSP
			return new ModelAndView("please_login");

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
			return "redirect:" + userService.createLoginURL("/register");

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
