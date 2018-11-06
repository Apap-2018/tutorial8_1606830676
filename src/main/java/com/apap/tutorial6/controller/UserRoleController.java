package com.apap.tutorial6.controller;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import com.apap.tutorial6.service.UserRoleService;
import com.apap.tutorial6.model.PasswordModel;
import com.apap.tutorial6.model.UserRoleModel;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;
	
	@RequestMapping(value="/addUser", method=RequestMethod.POST)
	private ModelAndView addUser(@ModelAttribute UserRoleModel user, RedirectAttributes redir) {
	    String pattern = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,}";
	    String message = "";
	    System.out.println(user.getPassword().matches(pattern) + " ENTER POST METHOD : " + user.getPassword());
		if(user.getPassword().matches(pattern)){
			userService.addUser(user);
			message = null;
			System.out.println("PASSWORD VALID");
		}
		else {
			message = "password must contain number and alphabet min 8 char";
		}
		System.out.println("LAST POST");
		ModelAndView modelAndView = new ModelAndView("redirect:/");
		redir.addFlashAttribute("msg",message);
		return modelAndView;
	}
	
	@RequestMapping("/updatePassword")
	public String updatePassword() {
		return "update-password";}
	
	public boolean validatePassword(String password) {
		if (password.length()>=8 && Pattern.compile("[0-9]").matcher(password).find() &&  Pattern.compile("[a-zA-Z]").matcher(password).find())  {
			return true;
		}
		else {
			return false;
		}
	}
	
	@RequestMapping(value="/passwordSubmit",method=RequestMethod.POST)
	public ModelAndView updatePasswordSubmit(@ModelAttribute PasswordModel pass, Model model,RedirectAttributes redir) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		UserRoleModel user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		String message = "";
	    String pattern = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,}";
		if (pass.getConPassword().equals(pass.getNewPassword())) {
			
			if (passwordEncoder.matches(pass.getOldPassword(), user.getPassword())) {
				if(pass.getNewPassword().matches(pattern)) {
					userService.changePassword(user, pass.getNewPassword());
					message = "Success changed password";
				}
				else {
					message = "password must contain number and alphabet min 8 char";
				}
			}
			else {
				message = "wrong old password";
			}
			
		}
		else {
			message = "wrong new password";
		} 
		
		ModelAndView modelAndView = new ModelAndView("redirect:/user/updatePassword");
		redir.addFlashAttribute("msg",message);
		return modelAndView;
	}
}
