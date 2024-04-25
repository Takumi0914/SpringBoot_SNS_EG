package com.example.eg_sns.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.eg_sns.entity.Friends;

import jakarta.transaction.Transactional;


/**
 * ユーザー関連リポジトリインターフェース。
 *
 * @author tomo-sato
 */
public interface FriendsRepository extends PagingAndSortingRepository<Friends, Long>, CrudRepository<Friends, Long> {

	/**
	 * ユーザー検索を行う。
	 * ログインIDを指定し、ユーザーを検索する。
	 *
	 * @param loginId ログインID
	 * @return ユーザー情報を返す。
	 */

	
	Friends   findByUsersId(Long usersId );
	



//	void deleteByUsersIdAndFriendId(Long usersId, Long friendId);




//	List<Friends> findByFriendId(Long friendId);




	Friends findByFriendsIdAndUsersId(Long friendId, Long usersId);



	@Transactional
	void deleteByFriendIdAndUsersId(Long friendId, Long usersId);

	
}
