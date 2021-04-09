package com.ee.hive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	@RequestMapping(value = "/emailInsert", method = RequestMethod.GET)
	public String emailInsert(Model model) {

		return "join/email_insert";
	}

	@RequestMapping(value = "/workInsert", method = RequestMethod.GET)
	public String workInsert(Model model) {

		return "join/work_insert";
	}

	@RequestMapping(value = "/channelInsert", method = RequestMethod.GET)
	public String channelInsert(Model model) {

		return "join/channel_insert";
	}

	@RequestMapping(value = "/memberInvite", method = RequestMethod.GET)
	public String memberInvite(Model model) {

		return "join/member_invite";
	}

}