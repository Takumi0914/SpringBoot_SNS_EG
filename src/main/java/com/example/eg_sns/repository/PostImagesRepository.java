package com.example.eg_sns.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.eg_sns.entity.PostImages;



/**
 * 投稿画像関連リポジトリインターフェース。
 */
public interface PostImagesRepository
		extends PagingAndSortingRepository<PostImages, Long>, CrudRepository<PostImages, Long> {
	
	
	
}
