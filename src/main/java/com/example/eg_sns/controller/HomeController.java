package com.example.eg_sns.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eg_sns.dto.RequestShare;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.service.PostsService;

import lombok.extern.log4j.Log4j2;

/**
 * ※TODO 適宜実装を入れてください。
 */
@Log4j2
@Controller
@RequestMapping("/home")
public class HomeController extends AppController {
	
	@Autowired
	private PostsService postsService;
	

	
	@GetMapping(path = {"", "/"})
	public String index(Model model) {
		
		log.info("ホーム画面が呼ばれました。");
				
		List<Posts> postsList = postsService.findAllPosts();
		model.addAttribute("postsList", postsList);
		
		log.info("投稿をリフレッシュしました。");
		
		
				return "/home/index";
	}
	
	@PostMapping("/post")
	public String post(@Validated @ModelAttribute RequestShare requestShare,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		
		log.info("投稿内容を受け取りました。：requestTopic={}", requestShare);
		
		Long usersId = getUsersId();
		
		postsService.save(requestShare,usersId,null);
		
		log.info("投稿内容を保存しました。");
		
		return "redirect:/home";
		
		
	}
}
