package com.example.eg_sns.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.eg_sns.entity.Comments;


/**
 * コメント関連リポジトリインターフェース。
 *
 * @author tomo-sato
 */
public interface CommentsRepository extends PagingAndSortingRepository<Comments, Long>, CrudRepository<Comments, Long> {

	
	List<Comments> findByOrderByIdDesc();
	/**
	 * コメントの削除処理を行う。
	 * ※物理削除（データが完全に消える。）
	 *
	 * @param id コメントID
	 * @param usersId ユーザーID
	 * @param postsId トピックID
	 */
	@Transactional
	void deleteByIdAndUsersIdAndPostsId(Long id, Long usersId, Long postsId);
	List<Comments> findByUsersIdOrderByIdDesc(Long postsId);
}
