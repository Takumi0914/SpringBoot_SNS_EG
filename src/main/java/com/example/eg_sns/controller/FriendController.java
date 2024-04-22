package com.example.eg_sns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.eg_sns.dto.RequestFriend;
import com.example.eg_sns.service.FriendsService;

import lombok.extern.log4j.Log4j2;

/**
 * ※TODO 適宜実装を入れてください。
 */
@Log4j2
@Controller
@RequestMapping("/friend")
public class FriendController extends AppController {
	
	@Autowired
	private FriendsService friendsService;

	@GetMapping("/list")
	public String list() {
		return "friend/list";
	}
	
	
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
	
	
}
