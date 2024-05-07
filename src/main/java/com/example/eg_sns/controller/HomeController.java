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

import com.example.eg_sns.dto.RequestComment;
import com.example.eg_sns.dto.RequestShare;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.service.CommentsService;
import com.example.eg_sns.service.PostsService;
import com.example.eg_sns.service.StorageService;

import lombok.extern.log4j.Log4j2;

/**
 * ※TODO 適宜実装を入れてください。
 */
@Log4j2
@Controller
@RequestMapping("/home")
public class HomeController extends AppController {
	
	@Autowired
	private CommentsService commentsService;
	
	@Autowired
	private PostsService postsService;
	
	@Autowired
	private StorageService storageService;
	

	
	@GetMapping(path = {"", "/"})
	public String index(Model model) {
		
		log.info("ホーム画面が呼ばれました。");
				
		List<Posts> postsList = postsService.findAllPosts();
		model.addAttribute("postsList", postsList);
		
		log.info("投稿をリフレッシュしました。");

		log.info("コメントをリフレッシュしました。");
		
		model.addAttribute("requestComment", new RequestComment());
		log.info("コメントをリフレッシュしました。");
		
		
				return "/home/index";
	}
	
	@PostMapping("/post")
	public String post(@Validated @ModelAttribute RequestShare requestShare,
											BindingResult result,
											RedirectAttributes redirectAttributes,
											Model model,
										  @RequestParam("file") MultipartFile file){
		
		log.info("投稿内容を受け取りました。：requestShare={} file={}", requestShare, file);

		// バリデーション。
		if(result.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
			for(ObjectError error : result.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			redirectAttributes.addFlashAttribute("validationError", errorList);
			
			log.info("エラーメッセージを受け取りました。：ValidationError={} ", errorList);
			
			return "redirect:/home";
		}
		
			Long usersId = getUsersId();

			String imgUri = storageService.store(file);
			
			 postsService.save(requestShare, usersId, imgUri);

		
		log.info("投稿内容を保存しました。");
		
		return "redirect:/home";
		
		
	}
	
	@PostMapping("/comment")
	public String comment(@Validated @ModelAttribute RequestComment requestComment,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
			//,@ModelAttribute("postId") Long postId) {
		
		log.info("コメントを受け取りました。：requestComment={}", requestComment);
		
	  Long postsId = requestComment.getPostId();
	  
		// バリデーション。
		if(result.hasErrors()) {
			Map<Long, BindingResult> errorMap = new HashMap<Long, BindingResult>();
			errorMap.put(postsId, result);
			
			
			redirectAttributes.addFlashAttribute("commentsValidationError", errorMap);
			
			log.info("エラーメッセージを受け取りました。：commentsValidationError={} ", errorMap);
			
			return "redirect:/home/"+ "#comment_" + postsId;
		}
		
		requestComment.setUsersId(getUsersId());
				
		commentsService.save(requestComment);
		
		log.info("コメントを保存しました。");
		
		return "redirect:/home";
		
		
	}
	
	@GetMapping("/comment/delete/{postsId}/{commentsId}")
	public String commentDelete(@PathVariable Long postsId, @PathVariable Long commentsId) {

		log.info("コメント削除処理のアクションが呼ばれました。：postsId={}, commentsId={}", postsId, commentsId);

		Long usersId = getUsersId();

		commentsService.delete(commentsId, usersId, postsId);

		return "redirect:/home";
	}
	
	@GetMapping("/post/delete/{id}/{usersId}")
	public String  postDelete(@PathVariable Long id , @PathVariable Long usersId) {

		log.info("投稿削除処理のアクションが呼ばれました。：id={} usersId={}", id, usersId);

		postsService.delete(usersId, id);
		commentsService.delete(id);

		return "redirect:/home";
	}
	
}
