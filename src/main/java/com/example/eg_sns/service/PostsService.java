package com.example.eg_sns.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eg_sns.dto.RequestShare;
import com.example.eg_sns.entity.PostImages;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.repository.PostImagesRepository;
import com.example.eg_sns.repository.PostsRepository;

import lombok.extern.log4j.Log4j2;

/**
 * 投稿関連サービスクラス。
 */
@Log4j2
@Service
public class PostsService {

	/** リポジトリインターフェース。 */
	@Autowired
	private PostsRepository repository;

	/** 投稿関連リポジトリインターフェース。 */
	@Autowired
	private PostImagesRepository postsImagesRepository;

	/**
	 * 投稿処理を行う。
	 *
	 * @param requestShare      コメント投稿DTO
	 * @param usersId           ユーザーID
	 * @param postImagesFileUri 投稿画像URI
	 */
	public void save(RequestShare requestShare, Long usersId, String imgUri) {
		log.info("投稿処理を行います。：requestShare={}, usersId={}, postImagesFileUri={}", requestShare, usersId,
				imgUri);

		Posts posts = new Posts();
		posts.setUsersId(usersId);
		posts.setTitle(requestShare.getTitle());
		posts.setBody(requestShare.getBody());

		// 投稿データの登録及び、取得。
		Posts regPosts = repository.save(posts);
		Long postsId = regPosts.getId();

		if (imgUri != null) {
			PostImages postImages = new PostImages();

			postImages.setPostsId(postsId);
			postImages.setUsersId(usersId);
			postImages.setImageUri(imgUri);

			log.info("画像を保存します。： postImages={}", postImages);

			postsImagesRepository.save(postImages);
		}

	}

	public void delete(Long usersId, Long id) {
		log.info("コメントを削除します。： usersId={}, commentsId={}", usersId, id);

		repository.deleteByUsersIdAndId(usersId, id);
	}

	/**
	 * 投稿一覧を取得する。
	 * 投稿IDの降順。
	 * 
	 * @return 投稿一覧を返す。
	 */
	public List<Posts> findAllPosts() {
		return (List<Posts>) repository.findByOrderByIdDesc();
	}

	/**
	 * 投稿一覧を取得する。
	 * 投稿IDの降順。
	 * 
	 * @param usersId ユーザーID
	 * @return 投稿一覧を返す。
	 */
	public List<Posts> findByUsersId(Long usersId) {
		return (List<Posts>) repository.findByUsersIdOrderByIdDesc(usersId);
	}

	public List<Posts> findTop5(Long id) {
		return (List<Posts>) repository.findTop5ByIdGreaterThanEqual(id);
	}

	public List<Posts> findTop5Besides(Long id) {
		return (List<Posts>) repository.findTop5OrderByIdGreaterThan(id);
	}

	public List<Posts> findFirst1ByOrderById() {
		return (List<Posts>) repository.findFirst1ByOrderById();
	}

}
