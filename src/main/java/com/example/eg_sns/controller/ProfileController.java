package com.example.eg_sns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eg_sns.dto.EditProfile;
import com.example.eg_sns.entity.Users;
import com.example.eg_sns.service.EditService;

import lombok.extern.log4j.Log4j2;

	

/**
 * ※TODO 適宜実装を入れてください。
 */
@Log4j2
@Controller
@RequestMapping("/profile")
public class ProfileController extends AppController {
	
	@Autowired
	private EditService editservice;

	@GetMapping(path = {"", "/"})
	public String index() {
		return "profile/index";
	}
	
	@PostMapping("/edit")
	public String edit(@Validated @ModelAttribute EditProfile editprofile,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		
		log.info("プロフィールを変更処理を呼び出しました。: editProfile= {}", editprofile );
		
		Users users = getUsers();
		
		log.info("プロフィール変更が完了しました。");
		
		editservice.update(editprofile ,users);
		
		return "profile/index";
	}

	
}
