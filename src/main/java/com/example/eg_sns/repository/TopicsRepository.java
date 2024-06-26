package com.example.eg_sns.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.eg_sns.entity.Topics;

/**
 * トピック関連リポジトリインターフェース。
 *
 * @author tomo-sato
 */
public interface TopicsRepository extends PagingAndSortingRepository<Topics, Long>, CrudRepository<Topics, Long> {

	/**
	 * トピック検索を行う。
	 * トピックIDを指定し、トピックを検索する。
	 *
	 * @param id トピックID
	 * @return トピック情報を返す。
	 */
	Optional<Topics> findById(Long id);

	/**
	 * トピック検索を行う。
	 * トピックID、ユーザーIDを指定し、トピックを検索する。
	 *
	 * @param id トピックID
	 * @param usersId ユーザーID
	 * @return トピック情報を返す。
	 */
	Optional<Topics> findByIdAndUsersId(Long id, Long usersId);

	/**
	 * トピック一覧を取得する。
	 * トピックIDの降順。
	 * @return トピック一覧を返す。
	 */
	List<Topics> findByOrderByIdDesc();
}
