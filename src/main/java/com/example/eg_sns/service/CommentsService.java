package com.example.eg_sns.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eg_sns.dto.RequestComment;
import com.example.eg_sns.entity.Comments;
import com.example.eg_sns.repository.CommentsRepository;

import lombok.extern.log4j.Log4j2;


@Log4j2
@Service
public class CommentsService {

	/** リポジトリインターフェース。 */
	@Autowired
	private CommentsRepository repository;

	/**
	 * コメント登録処理を行う。
	 *
	 * @param requestTopicComment コメントDTO
	 * @param usersId ユーザーID
	 * @param topicsId トピックID
	 */
	public void save(RequestComment requestComment) {
		
		Comments comments= new Comments();
		comments.setComment(requestComment.getComment());
		comments.setUsersId(requestComment.getUsersId());
		comments.setPostsId(requestComment.getPostId());
		
		log.info("コメントを保存しました。：requestComment={}", requestComment);

		// 投稿データの登録及び、取得。
	     Comments regComments = repository.save(comments);
	     Long commentsId = regComments.getId();
		
	}

	/**
	 * コメントの削除処理を行う。
	 *
	 * @param id コメントID
	 * @param usersId ユーザーID
	 * @param postsId トピックID
	 */
	public void delete(Long id, Long usersId, Long postsId) {
		log.info("コメントを削除します。：id={}, usersId={}, commentsId={}", id, usersId, postsId);

		repository.deleteByIdAndUsersIdAndPostsId(id, usersId, postsId);
	}

	/**
	 * コメントの削除処理を行う。
	 *
	 * @param commentsList コメントリスト
	 */
	public void deleteall(List<Comments> commentsList) {
		repository.deleteAll(commentsList);
	}
	
	public List<Comments> findAllComentss() {
		return (List<Comments>) repository.findByOrderByIdDesc();
		
	}
	
	public List<Comments> findBypostsId(Long postsId) {
		return (List<Comments>) repository.findByUsersIdOrderByIdDesc(postsId);
	}
}
