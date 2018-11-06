package com.apap.tutorial6.controller;
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

import com.apap.tutorial6.service.UserRoleService;
import com.apap.tutorial6.model.PasswordModel;
import com.apap.tutorial6.model.UserRoleModel;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;
	
	@RequestMapping(value= "/addUser", method = RequestMethod.POST)
	private String addUserSubmit(@ModelAttribute UserRoleModel user) {
		userService.addUser(user);
		return "home";
	}
	
	@RequestMapping("/updatePassword")
	public String updatePassword() {
		return "update-password";}
	
	@RequestMapping(value="/passwordSubmit", method=RequestMethod.POST)
	public ModelAndView updatePassowrdSubmit(@ModelAttribute PasswordModel pass, Model model, RedirectAttributes redir) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		UserRoleModel user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		String message = "";
		
		if (pass.getConPassword().equals(pass.getNewPassword())) {
					
					if (passwordEncoder.matches(pass.getOldPassword(), user.getPassword())) {
						
						userService.changePassword(user, pass.getNewPassword());
						message = "password changed success";
					}
					else {
						message = "wrong password";
					}
					
		}
		else {
			message = "new password invalid";
		}
		
		ModelAndView modelAndView = new ModelAndView("redirect:/user/updatePassword");
		redir.addFlashAttribute("msg",message);
		return modelAndView;
	}

}
