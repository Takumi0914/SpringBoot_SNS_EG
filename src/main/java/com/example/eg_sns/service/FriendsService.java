package com.example.eg_sns.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eg_sns.dto.RequestFriend;
import com.example.eg_sns.entity.Friends;
import com.example.eg_sns.repository.FriendsRepository;

import lombok.extern.log4j.Log4j2;

/**
 * ユーザー関連サービスクラス。
 *
 * @author tomo-sato
 */
@Log4j2
@Service
public class FriendsService {

	/** リポジトリインターフェース。 */
	@Autowired
	private FriendsRepository repository;

	/**
	 * ユーザー検索を行う。
	 * ログインIDを指定し、ユーザーを検索する。
	 *
	 * @param loginId ログインID
	 * @return ユーザー情報を返す。
	 */

	
	 public  List<Friends> findUsers(Long usersId){
		return (List<Friends>) repository.findByUsersId(usersId) ;
		 
	 }
	 
	 public  List<Friends> findAlllFriends(){
			return (List<Friends>) repository.findAll() ;
			 
		 }


	/**
	 * ユーザー登録処理を行う。
	 *
	 * @param requestAccount ユーザーDTO
	 */
	public void save(RequestFriend requestFriend) {
		Friends friend = new Friends();
		friend.setStatus(requestFriend.getStatus());
		friend.setFriendId(requestFriend.getFriendId());
		friend.setUsersId(requestFriend.getUsersId());
		
		repository.save(friend);
	}

	/**
	 * ユーザー登録処理を行う。
	 *
	 * @param requestAccount ユーザーDTO
	 */
	
}
