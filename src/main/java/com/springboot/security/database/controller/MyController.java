package com.springboot.security.database.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.springboot.security.database.model.User;
import com.springboot.security.database.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class MyController {


	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		return "login";
	}
	
	@GetMapping("/welcome")
	public String getWelcome(Model model, Principal principal) {
	    if (principal != null) {
	        String username = principal.getName();
	        String picture = null;

	        if (principal instanceof Authentication) {
	            Authentication authentication = (Authentication) principal;

	            if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
	                DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
	                username = oAuth2User.getAttribute("name");

	                String clientRegistrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
	                
	                if ("google".equals(clientRegistrationId)) {
	                    picture = oAuth2User.getAttribute("picture"); // For Google, profile picture is stored under "picture"
	                } else if ("github".equals(clientRegistrationId)) {
	                    picture = oAuth2User.getAttribute("avatar_url"); // For GitHub, profile picture is stored under "avatar_url"
	                }
	            } else if (authentication.getPrincipal() instanceof UserDetails) {
	                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	                username = userDetails.getUsername();
	                Optional<User> userOptional = userRepository.findByUsernameOrEmail(username, username);
	                User user = userOptional.orElse(null);
	                if (user != null) {
	                    picture = user.getPicture();
	                }
	            }
	        }

	        model.addAttribute("username", username);
	        model.addAttribute("picture", picture);
	    }
	    return "welcome";
	}
 


	@GetMapping("/user/sample")
	public String getSapmle() {
		return "sample";
	}

	@GetMapping("/admin/dashboard")
	public String getAdminDashboard() {
		return "dashboard";
	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
			@RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirectAttributes,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "register";
		}

		if (!user.getPassword().equals(confirmPassword)) {
			model.addAttribute("MailerrorMessage", "Passwords do not match.");
			return "register";
		}

		Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
		if (existingUser.isPresent()) {
			model.addAttribute("errorMessage", "Email already exists. Please use a different email.");
			return "register";

		} else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			return "redirect:/welcome";
		}

	}

}
