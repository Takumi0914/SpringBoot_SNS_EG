package com.example.eg_sns.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.eg_sns.dto.RequestComment;
import com.example.eg_sns.dto.RequestShare;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.service.CommentsService;
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
	private CommentsService commentsService;
	
	@Autowired
	private PostsService postsService;
	
	
	

	
	@GetMapping(path = {"", "/"})
	public String index(Model model) {
		
		log.info("ホーム画面が呼ばれました。");
				
		List<Posts> postsList = postsService.findAllPosts();
		model.addAttribute("postsList", postsList);
		
		log.info("投稿をリフレッシュしました。");
		
//		List<Comments> comments = commentsService.findAllComentss();
//		log.info("aaa。：commentsList={}", comments);
//		model.addAttribute("commentsList", comments);
		
		log.info("コメントをリフレッシュしました。");
		
		model.addAttribute("requestComment", new RequestComment());
		log.info("コメントをリフレッシュしました。");
		
		
				return "/home/index";
	}
	
	@PostMapping("/post")
	public String post(@Validated @ModelAttribute RequestShare requestShare,
										  @RequestParam("file") MultipartFile file){
		
		log.info("投稿内容を受け取りました。：requestShare={} file={}", requestShare, file);
		
		Long usersId = getUsersId();
		
		
		//postsService.save(requestShare,usersId,file);
		postsService.save(requestShare, usersId, file);
		
		log.info("投稿内容を保存しました。");
		
		return "redirect:/home";
		
		
	}
	
	@PostMapping("/comment")
	public String comment(@Validated @ModelAttribute RequestComment requestComment) {
			//,@ModelAttribute("postId") Long postId) {
		
		log.info("コメントを受け取りました。：requestComment={}", requestComment);
		
		requestComment.setUsersId(getUsersId());
		//Long usersId = getUsersId();
				
		commentsService.save(requestComment);
		
		log.info("コメントを保存しました。");
		
		return "redirect:/home";
		
		
	}
	
	@GetMapping("/comment/delete/{postsId}/{commentsId}")
	public String commentDelete(@PathVariable Long postsId, @PathVariable Long commentsId) {

		log.info("コメント削除処理のアクションが呼ばれました。：postsId={}, commentsId={}", postsId, commentsId);

		// ログインユーザー情報取得（※自分が投稿したコメント以外を削除しない為の制御。）
		Long usersId = getUsersId();

		// コメント削除処理
		commentsService.delete(commentsId, usersId, postsId);

		// 入力画面へリダイレクト。
		return "redirect:/home";
	}
	
	@GetMapping("/post/delete/{id}/{usersId}")
	public String  postDelete(@PathVariable Long id , @PathVariable Long usersId) {

		log.info("投稿削除処理のアクションが呼ばれました。：id={} usersId={}", id, usersId);

		// ログインユーザー情報取得（※自分が投稿したコメント以外を削除しない為の制御。）
		// コメント削除処理
		postsService.delete(usersId, id);

		// 入力画面へリダイレクト。
		return "redirect:/home";
	}
	
}
