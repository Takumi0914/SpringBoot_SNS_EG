package com.example.eg_sns.entity;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 投稿Entityクラス。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "friends")
public class Friends extends EntityBase {

	/** ID　プライマリキー　オートインクリメント */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** ユーザーID */
	@Column(name = "users_id", nullable = false)
	private Long usersId;

	/** フレンドのユーザーID */
	@Column(name = "friend_users_id", nullable = false)
	private Long friendId;

	/** 承認ステータス */
	@Column(name = "approval_status", nullable = false)
	private String status;

	/** 投稿コメント情報の紐づけ */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "posts_id", referencedColumnName = "id", insertable = false, updatable = false)
	private List<Comments> postCommentsList;

	/** ユーザー情報の紐づけ */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Users users;
}
