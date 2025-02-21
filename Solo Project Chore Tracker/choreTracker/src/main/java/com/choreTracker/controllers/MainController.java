package com.choreTracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.choreTracker.models.LoginUser;
import com.choreTracker.models.User;
import com.choreTracker.services.JobService;
import com.choreTracker.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/")
public class MainController {

	// Add once service is implemented:
	@Autowired
	private UserService userServ;
	@Autowired
	private JobService jobService;

	@GetMapping("/")
	public String index(Model model) {

		// Bind empty User and LoginUser objects to the JSP
		// to capture the form input :
		model.addAttribute("newUser", new User());
		model.addAttribute("newLogin", new LoginUser());

		return "index.jsp";
	}

	// Register Route
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("newUser") User newUser, BindingResult result, Model model,
			HttpSession session) {

		// call a register method in the service
		// to do some extra validations and create a new user!
		userServ.register(newUser, result);

		if (result.hasErrors()) {
			// Be sure to send in the empty LoginUser before
			// re-rendering the page.
			model.addAttribute("newLogin", new LoginUser());
			return "index.jsp";
		}

		// No errors! :
		// Store their ID from the DB in session,
		// in other words, log them in.
		session.setAttribute("userId", newUser.getId());

		return "redirect:/dashboard";
	}

	// Login Route
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, BindingResult result, Model model,
			HttpSession session) {

		// Add once service is implemented:
		User user = userServ.login(newLogin, result);

		if (result.hasErrors()) {
			model.addAttribute("newUser", new User());
			return "index.jsp";
		}

		// No errors!
		// Store their ID from the DB in session,
		// in other words, log them in.
		session.setAttribute("userId", user.getId());

		return "redirect:/dashboard";
	}

	// Logout Route
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.setAttribute("userId", null);
		return "redirect:/";
	}
// Logout Route (M2)
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//    	session.invalidate();
//    	return "redirect:/";
//    }

	// Dashboard Route
//	@GetMapping("/books")
//	public String books(Model model, HttpSession session) {
//		Long userId = (Long) session.getAttribute("userId");
//		if (userId == null) {
//			return "redirect:/";
//		}
//		User user = userServ.findById(userId);
//		model.addAttribute("user", user);
//		// Get all Books:
//		model.addAttribute("books", bookService.allBooks());
//
//		return "books.jsp";
	//}
    @GetMapping("/dashboard")
    public String home(Model model, HttpSession session) {
    	
    	if(session.getAttribute("userId") == null) {
    		return "redirect:/";
    	}
    	
    	model.addAttribute("jobs", jobService.allJobs());
    	model.addAttribute("user", userServ.findById((Long)session.getAttribute("userId")));
    	return "jobs.jsp";
    }
    

}