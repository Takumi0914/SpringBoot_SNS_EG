package com.example.eg_sns.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ユーザーEntityクラス。
 *
 * @author tomo-sato
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class Users extends EntityBase {

	/** ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** ログインID */
	@Column(name = "login_id", nullable = false)
	private String loginId;

	/** パスワード */
	@Column(name = "password", nullable = false)
	private String password;

	/** 名前 */
	@Column(name = "name", nullable = false)
	private String name;

	/** プロフィールアイコンURI */
	@Column(name = "icon_uri", nullable = false)
	private String iconUri;
	
	@Column(name = "email_address", nullable = false)
	private String email;

	@Column(name = "profile", nullable = false)
	private String profile;
	
	/** ユーザー情報の紐づけ */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", referencedColumnName = "id", insertable = false, updatable = false)
	private List<Friends> friendsList;
	
	@Transient
	private Friends friendsInfo;

}
