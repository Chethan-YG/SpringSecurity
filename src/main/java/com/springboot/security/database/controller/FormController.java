package com.springboot.security.database.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.security.database.Service.MailService;
import com.springboot.security.database.model.MailStructure;
import com.springboot.security.database.model.User;
import com.springboot.security.database.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class FormController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MailService mailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/forgot-password")
	public String showForgotPasswordForm() {
		return "forgot_password";
	}

	
	@PostMapping("/forgot-password")
	public String processForgotPassWordForm(@RequestParam("email") String email, Model model, HttpSession session) {

		Optional<User> existingUser = userRepository.findByEmail(email);
		if (!existingUser.isPresent()) {
			model.addAttribute("errorMessage", "Email does not exists. Please enter correct email.");
			return "forgot_password";

		} else {
			String otp = MailService.generateOTP();

			session.setAttribute("userEmail", email);
			MailStructure mailStructure = new MailStructure();
			mailStructure.setSubject("Forgot Password OTP");
			mailStructure.setMessage("Your OTP for password reset is: " + otp);
			mailService.sendMail(email, mailStructure);

			session.setAttribute("otp", otp);
			model.addAttribute("otpSent", true);
			return "forgot_password";

		}

	}
	
	
	
	@PostMapping("/submit-otp")
	public String submitOTP(@RequestParam("otp") String enteredOTP, Model model, HttpSession session) {
		String storedOTP = (String) session.getAttribute("otp");

		if (enteredOTP.equals(storedOTP)) {
			session.removeAttribute("otp");
			return "redirect:/reset-password";
		} else {
			model.addAttribute("incorrectOtp", "Invalid OTP. Please try again.");
			return "forgot_password";
		}
	}
	
	
	@GetMapping("/reset-password")
	public String showResetForm(Model model, HttpSession session) {
		String email = (String) session.getAttribute("userEmail");
		if (email != null) {
			Optional<User> userOptional = userRepository.findByEmail(email);
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				model.addAttribute("user", user);
				return "reset_password";
			} else {
				return "Error"; 
			}
		} else {
			return "Error"; 
		}
	}



	@PostMapping("/update-password")
	public String updatePassword(@RequestParam("password") String newPassword, HttpSession session,
		RedirectAttributes redirectAttributes) {
		
		String userEmail = (String) session.getAttribute("userEmail");
		if (userEmail == null) {
			redirectAttributes.addFlashAttribute("error", "Email not found in session.");
			return "redirect:/reset-password";
		}

		User user = userRepository.findByEmail(userEmail).orElse(null);
		if (user != null) {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
		} else {
			redirectAttributes.addFlashAttribute("error", "User not found.");
			return "redirect:/reset-password";
		}

		return "redirect:/login";
	}

}
