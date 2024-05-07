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

	
	 public  Friends findUsers(Long usersId){
		 Friends friends = repository.findByUsersId(usersId) ;
		return friends;
		 
	 }
	 
	 public  List<Friends> findAlllFriends(){
			return (List<Friends>) repository.findAll() ;
			 
		 }
	 
	 //フレンド検索をします
	 //update叩きたいフレンドを一件取得
	 public Friends findFriends(Long friendId, Long usersId) {
			log.info("フレンドを検索します。：friendId={}, usersId={}", friendId, usersId);

			Friends friend = repository.findByFriendsIdAndUsersId(friendId, usersId);
			log.info("ユーザー検索結果。：friend={}", friend);

			return friend;
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
	
	
	public void delete(Long friendId, Long usersId) {
		log.info("フレンドを削除します。: usersId={}, friendId={}", friendId, usersId);

		repository.deleteByFriendIdAndUsersId(friendId, usersId);
	}

	
	
	
	//regist=フレンド追加/承認（statusを３にアップデートする
	public void updateStatus(Friends friend) {
		
		repository.save(friend);
		
	}
	
}
