package com.example.eg_sns.controller;

import java.util.ArrayList;
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

import com.example.eg_sns.dto.RequestFriend;
import com.example.eg_sns.entity.Friends;
import com.example.eg_sns.entity.Users;
import com.example.eg_sns.service.FriendsService;
import com.example.eg_sns.service.UsersService;

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
	
	@Autowired
	private UsersService usersService;

	@GetMapping("/list")
	public String list(Model model) {
		
		List<Users> usersList = usersService.findAllUsers();
		
		Long loginId = getUsersId();
		
//		//ユーザーからログインユーザを除いたリストを作成
//     	List<Users> friendsList = new ArrayList<Users>();
//		for(Users friend : usersList) {
//			if(loginId != friend.getId()) {
//				friendsList.add(friend);
//			}
//		}
		
		Iterable<Users> usersList2 = usersService.findAllUsers();
		List<Users> usersList3 = new ArrayList<Users>();
		
		List<Friends> tmpFriendsList = null;
		for (Users users : usersList2) {
			if (loginId.equals(users.getId().longValue())) {
				tmpFriendsList = users.getFriendsList();
			} else {
				usersList3.add(users);
			}
		}
		
		for (Friends friends : tmpFriendsList) {
			Long friendUsersId = friends.getFriendId();

			int size = usersList3.size();
			for (int i = 0; i < size; i++) {
				Users users = usersList3.get(i);
				if (friendUsersId.equals(users.getId())) {
					users.setFriendsInfo(friends);
					usersList3.set(i, users);
					break;
				}
			}
		}
		
		
		model.addAttribute("usersList3", usersList3);
		log.info("フレンドリストを受け取りました。：usersList3={}", usersList3);

		
		
		
		
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
