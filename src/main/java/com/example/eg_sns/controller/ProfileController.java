package com.example.eg_sns.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.example.eg_sns.dto.RequestFriend;
import com.example.eg_sns.entity.Friends;
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
		
		//usersにはログインユーザーならログインユーザーの情報を、
		//他ユーザーならその情報を取得している
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
		
		
		Users friendUsers = new Users();
		Friends friendInfo = new Friends();
		//他ユーザーを表示しているとき、他ユーザーの
		if(usersId != null) {
			friendUsers = usersService.findUsers(loginId);
			friendInfo = friendsService.findFriends(loginId, usersId);
		}
	//	if(friendInfo != null) {
			friendUsers.setFriendsInfo(friendInfo);
//		//}else {
//			friendInfo = 
//		}
		  log.info("フレンド情報をセットしました。:friendUsers={}, friendInfo={}", friendUsers, friendInfo);
		  
		  model.addAttribute("friendUsers", friendUsers);
		

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
			
			log.info("エラーメッセージを受け取りました。：validationError={} ", errorList);
			
			return "redirect:/profile";
		}
			
		String imgUri = storageService.store(file);
		
		Users users = getUsers();
		
		if(StringUtils.isEmpty(imgUri) && !StringUtils.isEmpty(editprofile.getFileHidden())) {
			imgUri = editprofile.getFileHidden();
			
		}
		
		log.info("プロフィール変更が完了しました。");
		
		editService.update(editprofile, users, imgUri);
		
		return  "redirect:/profile";
	        
	}
	
	
	//ここから友達申請等の処理
	
	
	//友達申請ボタン押下時アクション
	@PostMapping("/add/{friendId}")
	public String post(@Validated @ModelAttribute RequestFriend requestFriend,@PathVariable(required = false) Long friendId) {
		
		log.info("友達申請を受け取りました。：requestFriend={}", requestFriend);
		
		/**
		 * status 
		 * friendId
		 * usersId
		 */
		
		Long loginId = getUsersId();

		
		RequestFriend friend = new RequestFriend();
		friend.setStatus(requestFriend.getStatus());
		friend.setFriendId(requestFriend.getFriendId());
		friend.setUsersId(loginId);
		
		log.info("友達申請を送信しました。：friend={}", friend);
		friendsService.save(friend);
		
		RequestFriend friend2 = new RequestFriend();
		friend2.setStatus("2");
		friend2.setFriendId(friend.getUsersId());
		friend2.setUsersId(friend.getFriendId());
		
		log.info("友達申請を受信しました。：friend2={}", friend2);
		
		friendsService.save(friend2);
		
		
		return "redirect:/profile/" + Long.toString(friendId);
		
		
	}
			
			@PostMapping("/delete/{friendId}")
			public String delete(@Validated @ModelAttribute RequestFriend requestFriend,@PathVariable(required = false) Long friendId) {

				log.info("フレンド却下を受け取りました。：friendId={}", friendId);

				// ログインユーザー情報取得（※自分が投稿したコメント以外を削除しない為の制御。）
				Long usersId = getUsersId();
				
				
				//ユーザー検索（テーブル検索）
				//Friends friend = friendsService.findFriends(friendId, usersId);

				// コメント削除処理
//				friendsService.delete();
				
				friendsService.delete(friendId, usersId);
				
				Long friendId2 = usersId;
				Long usersId2 = friendId;
				
				friendsService.delete(friendId2, usersId2);

				// 入力画面へリダイレクト。
				return "redirect:/profile/" + Long.toString(friendId);	
				
			}
			
			
			
			@PostMapping("/regist/{friendId}")
			public String regist(@Validated @ModelAttribute RequestFriend requestFriend,@PathVariable(required = false) Long friendId) {

				log.info("フレンド追加を受け取りました。： friendId={}", friendId);

				// ログインユーザー情報取得
				Long usersId = getUsersId();
				
				Friends friend = friendsService.findFriends(friendId, usersId);

				
				//Friends friend =new Friends();
				
				// フレンド登録にステータスを変更（３にアップデート）
				friend.setFriendId(friendId);
				friend.setUsersId(usersId);
				friend.setStatus("3");
				log.info("フレンド追加を送信しました。：friend={} ", friend);

				friendsService.updateStatus(friend);
				
				
				//Idを入れ替えてテーブル両方のテーブルを変更したい
				Long usersId2 = friendId;
				Long friendId2 = usersId;
//				//二段めのテーブルのステータスを変更
//				Friends friend2 = new Friends();
//				friend2.setFriendId(friendId2);
//				friend2.setUsersId(usersId2);
//				friend2.setStatus("3");
				
				Friends friend2 = friendsService.findFriends(friendId2, usersId2);
				friend2.setFriendId(friendId2);
				friend2.setUsersId(usersId2);
				friend2.setStatus("3");
				log.info("フレンド追加を送信しました。：friend={} ", friend2);
				
				friendsService.updateStatus(friend2);

				// 入力画面へリダイレクト。
				return "redirect:/profile/" + Long.toString(friendId);
				
			}
	
	
}
