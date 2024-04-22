package com.example.eg_sns.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.eg_sns.entity.Friends;


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

	
	List<Friends>   findByUsersId(Long usersId );



	
		
	
	
}
