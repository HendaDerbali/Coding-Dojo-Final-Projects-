package com.choreTracker.controllers;

import java.awt.print.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.choreTracker.models.Job;
import com.choreTracker.models.User;
import com.choreTracker.services.JobService;
import com.choreTracker.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/")
public class JobController {
	// First Step Usually add this to link this controller with services :
	@Autowired
	UserService userService;
	@Autowired
	JobService jobService;

	// Second Step : Routes :

	// ? Create : ( Form Many to one with multi select List on One )

	@RequestMapping("addJob")
	public String newJob(@ModelAttribute("job") Job job, Model model, HttpSession session) {
		// 3 session lines : to make bring the user in the session
		User user = userService.findById((Long) session.getAttribute("userId"));
		model.addAttribute("user", user);
		System.out.println(user);

		return "newJob.jsp";
	}

	@PostMapping("/addJob")
	public String create(@Valid @ModelAttribute("job") Job job, BindingResult result, Model model) {
		if (result.hasErrors()) {

			return "newJob.jsp";
		} else {
			jobService.createJob(job);
			return "redirect:/dashboard";
		}
	}

	// ? Edit :

	@RequestMapping("edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model, HttpSession session) {
		// Session
		if (session.getAttribute("userId") == null) {
			return "redirect:/";
		}
		// 3 session lines : to make bring the user in the session
		User user = userService.findById((Long) session.getAttribute("userId"));
		model.addAttribute("user", user);
		System.out.println(user);

		Job job = jobService.findJob(id);
		model.addAttribute("job", job);
		System.out.println(job);

		return "editJob.jsp";
	}

	@RequestMapping(value = "/editJob/{id}", method = RequestMethod.PUT)
	public String update(@Valid @ModelAttribute("job") Job job, BindingResult result, Model model,
			HttpSession session) {
		// Session
		if (session.getAttribute("userId") == null) {
			return "redirect:/";
		}
		if (result.hasErrors()) {
			model.addAttribute("job", job);
			return "editJob.jsp";
		} else {
			jobService.updateJob(job);
			return "redirect:/dashboard";
		}
	}

	// ? Delete Job :
	@RequestMapping(value = "/deleteJob/{id}", method = RequestMethod.DELETE)
	public String destroy(@PathVariable("id") Long id) {
		jobService.deleteJob(id);
		return "redirect:/dashboard";
	}

	// ? Get One By id

	@GetMapping("/view/{id}")
	public String showPage(Model model, @PathVariable("id") Long id, HttpSession session) {
		// session lines : to make bring the user in the session
		if (session.getAttribute("userId") == null) {
			return "redirect:/";
		}
		// 3 session lines : to make bring the user in the session
		User user = userService.findById((Long) session.getAttribute("userId"));
		model.addAttribute("user", user);
		System.out.println(user);

		Job job = jobService.findJob(id);
		model.addAttribute("job", job);
		System.out.println(job);

		return "showOneJob.jsp";
	}
	
	// 2 nd One To Many : Add Job To My List :
		@GetMapping("/add/{id}")
		public String addJobToMyList(Model model, @PathVariable("id") Long id, HttpSession session) {
			// Session
			if (session.getAttribute("userId") == null) {
				return "redirect:/";
			}
			// 3 session lines : to make bring the user in the session
			User user = userService.findById((Long) session.getAttribute("userId"));
			model.addAttribute("user", user);
			System.out.println(user);

			// set Job User WhoAdd : UserWhoAdd is set to user logged in in session (user):
			Job job = jobService.findJob(id);
			job.setUserWhoAdd(user);
			jobService.updateJob(job);

			return "redirect:/dashboard";
		}
}
