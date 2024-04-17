package com.example.eg_sns.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eg_sns.dto.EditProfile;
import com.example.eg_sns.dto.RequestComment;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.entity.Users;
import com.example.eg_sns.service.CommentsService;
import com.example.eg_sns.service.EditService;
import com.example.eg_sns.service.PostsService;
import com.example.eg_sns.service.UsersService;

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
	
	@Autowired
	private PostsService postsService;
	
	@Autowired
	private CommentsService commentsService;
	
	@Autowired
	private UsersService usersService;

	@GetMapping(path = {"", "/","{usersId}"})
	public String index(Model model,@PathVariable(required = false) Long usersId) {
		
		log.info("リンク先のプロフィールを呼び出しました。 :usersId={}", usersId);
		
		Long loginId = getUsersId();
		List<Posts> postsList = new ArrayList<>();
		
		if(usersId != null) {
		postsList = postsService.findByUsersId(usersId);
		}else {
		postsList = postsService.findByUsersId(loginId);
		}
		model.addAttribute("postsList", postsList);
		
		
		
		
//		Users users = new Users();
//		
//		if(usersId != null) {
//		users = usersService.findUsers(usersId);
//		}
//		
//		String usersName = users.getName();
//		
//		model.addAttribute("usersName", usersName);
		
		
		
		
		
		
        log.info("投稿をリフレッシュしました。");
		
		
		
		model.addAttribute("requestComment", new RequestComment());
		log.info("コメントをリフレッシュしました。");
		
		
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
	
	@PostMapping("/comment")
	public String comment(@Validated @ModelAttribute RequestComment requestComment) {
			//,@ModelAttribute("postId") Long postId) {
		
		log.info("コメントを受け取りました。：requestComment={}", requestComment);
		
		requestComment.setUsersId(getUsersId());
		//Long usersId = getUsersId();
				
		commentsService.save(requestComment);
		
		log.info("コメントを保存しました。");
		
		return "redirect:/profile";
		
		
	}
	
	@GetMapping("/comment/delete/{postsId}/{commentsId}")
	public String commentDelete(@PathVariable Long postsId, @PathVariable Long commentsId) {

		log.info("コメント削除処理のアクションが呼ばれました。：postsId={}, commentsId={}", postsId, commentsId);

		// ログインユーザー情報取得（※自分が投稿したコメント以外を削除しない為の制御。）
		Long usersId = getUsersId();

		// コメント削除処理
		commentsService.delete(commentsId, usersId, postsId);

		// 入力画面へリダイレクト。
		return "redirect:/profile";
	}

	
}
