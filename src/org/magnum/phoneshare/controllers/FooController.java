package org.magnum.phoneshare.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FooController {

	@RequestMapping(value = "/foo", method = RequestMethod.GET)
	public String getDataCollectionPlan(Model model) {
		return "foo";
	}

}