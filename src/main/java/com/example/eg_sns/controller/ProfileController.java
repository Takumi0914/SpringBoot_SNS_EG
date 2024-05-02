package com.example.eg_sns.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eg_sns.dto.EditPassword;
import com.example.eg_sns.dto.EditProfile;
import com.example.eg_sns.dto.RequestComment;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.entity.Users;
import com.example.eg_sns.service.CommentsService;
import com.example.eg_sns.service.EditService;
import com.example.eg_sns.service.FriendsService;
import com.example.eg_sns.service.PostsService;
import com.example.eg_sns.service.StorageService;
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
	private EditService editService;
	
	@Autowired
	private PostsService postsService;
	
	@Autowired
	private CommentsService commentsService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private FriendsService friendsService;
	
	@Autowired
	private StorageService storageService;

	@GetMapping(path = {"", "/","/{usersId}"})
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
		
		
		Users users = new Users();
		if(usersId != null) {
		users = usersService.findUsers(usersId);
		}else {
		users = usersService.findUsers(loginId);
		}
		
	    model.addAttribute("loginId", loginId);
        model.addAttribute("usersId",usersId);
		
		String usersName = users.getName();
		model.addAttribute("usersName", usersName);
		
        log.info("投稿をリフレッシュしました。");
		
		model.addAttribute("requestComment", new RequestComment());
		
		log.info("コメントをリフレッシュしました。");
		
		return "profile/index";
	}

	@PostMapping("/password")
	public String edit(@Validated @ModelAttribute EditPassword editPassword,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		
		log.info("プロフィールを変更処理を呼び出しました。: editPassword= {}", editPassword );
		
		Users user = getUsers();
		
		String loginId = user.getLoginId();
		
		String userPassword = user.getPassword();
		
		List<String> errorList = new ArrayList<String>();
		
		// 現在のパスワードチェック。
				if (!userPassword.equals(editPassword.getPassword())) {
					log.warn("現在のパスワードが違います。：editPassword.getPassword()={}", editPassword.getPassword());
					
					
					errorList.add("現在のパスワードが違います。");
					redirectAttributes.addFlashAttribute("passwordValidationError", errorList);
					
					return "redirect:/profile";
					
				}else if (result.hasErrors()) {
					
					for(ObjectError error : result.getAllErrors()) {
						errorList.add(error.getDefaultMessage());
					}
					redirectAttributes.addFlashAttribute("passwordValidationError", errorList);
					
					log.info("エラーメッセージを受け取りました。：passwordValidationError={} ", errorList);
					
					return "redirect:/profile";
				}
		
		Users users = editService.findUsers(loginId, userPassword);
		
		editService.update(editPassword ,users);
		
		log.info("パスワードを保存しました。: editPassword= {}", editPassword.getPassword() );
		 
		 
		
		return "redirect:/profile";
		
	}
	
	//プロフィールページでのコメントボタン押下処理
	@PostMapping("/comment")
	public String comment(@Validated @ModelAttribute RequestComment requestComment,BindingResult result,
			RedirectAttributes redirectAttributes) {
		
		log.info("コメントを受け取りました。：requestComment={}", requestComment);
		
		  Long postsId = requestComment.getPostId();

		
		// バリデーション。
		if(result.hasErrors()) {
			Map<Long, BindingResult> errorMap = new HashMap<Long, BindingResult>();
			errorMap.put(postsId, result);
			
			
			redirectAttributes.addFlashAttribute("commentsValidationError", errorMap);
			
			log.info("エラーメッセージを受け取りました。：commentsValidationError={} ", errorMap);
			
			return "redirect:/profile/"+ "#comment_" + postsId;
		}
		
		requestComment.setUsersId(getUsersId());

		commentsService.save(requestComment);
		
		log.info("コメントを保存しました。");
		
		return "redirect:/profile";
		
		
	}
	
	//プロフィールページでのコメント削除ボタン押下処理
	@GetMapping("/comment/delete/{postsId}/{commentsId}")
	public String commentDelete(@PathVariable Long postsId, @PathVariable Long commentsId) {

		log.info("コメント削除処理のアクションが呼ばれました。：postsId={}, commentsId={}", postsId, commentsId);

		Long usersId = getUsersId();

		commentsService.delete(commentsId, usersId, postsId);

		return "redirect:/profile";
	}
	
	//プロフィールページでの投稿削除ボラン押下処理
	@GetMapping("/post/delete/{id}/{usersId}")
	public String  postDelete(@PathVariable Long id , @PathVariable Long usersId) {

		log.info("投稿削除処理のアクションが呼ばれました。：id={} usersId={}", id, usersId);
		
		postsService.delete(usersId, id);

		return "redirect:/profile";
	}
	
	//プロフィール編集内容保存処理
	@PostMapping("/edit")
	public String edit(@Validated @ModelAttribute EditProfile editprofile,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			Model model,
			@RequestParam("file") MultipartFile file) {
		
		log.info("プロフィールを変更処理を呼び出しました。: editProfile= {}", editprofile );
		
		// バリデーション。
		if(result.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
			for(ObjectError error : result.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			redirectAttributes.addFlashAttribute("validationError", errorList);
			
			log.info("エラーメッセージを受け取りました。：ValidationError={} ", errorList);
			
			return "redirect:/profile";
		}
			
		String imgUri = storageService.store(file);
		
		Users users = getUsers();
		
		log.info("プロフィール変更が完了しました。");
		
		editService.update(editprofile, users, imgUri);
		
		return  "redirect:/profile";
	        
	}
	
	
}
