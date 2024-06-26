package com.example.eg_sns.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eg_sns.dto.EditPassword;
import com.example.eg_sns.dto.EditProfile;
import com.example.eg_sns.entity.Users;
import com.example.eg_sns.repository.UsersRepository;

import lombok.extern.log4j.Log4j2;

/**
 * ユーザー関連サービスクラス。
 *
 * @author tomo-sato
 */
@Log4j2
@Service
public class EditService {

	/** リポジトリインターフェース。 */
	@Autowired
	private UsersRepository repository;

	/**
	 * ユーザー検索を行う。
	 * ログインIDを指定し、ユーザーを検索する。
	 *
	 * @param loginId ログインID
	 * @return ユーザー情報を返す。
	 */
	public Users findUsers(String loginId) {
		log.info("ユーザーを検索します。：loginId={}", loginId);

		Users users = repository.findByLoginId(loginId);
		log.info("ユーザー検索結果。：loginId={}, users={}", loginId, users);

		return users;
	}

	public Users findUsers(String loginId, String password) {
		log.info("ユーザーを検索します。：loginId={}, password={}", loginId, password);

		Users users = repository.findByLoginIdAndPassword(loginId, password);
		log.info("ユーザー検索結果。：loginId={}, password={}, users={}", loginId, password, users);

		return users;
	}
	
	public void update(EditProfile editprofile, Users users) {
		
		users.setProfile(editprofile.getProfile());
		users.setName(editprofile.getName());
		users.setEmail(editprofile.getEmail());
		users.setLoginId(editprofile.getLoginId());
		
		repository.save(users);
		
	}

	public void update(EditPassword editPassword, Users users) {
		
		users.setPassword(editPassword.getNewpassword1());
		
		repository.save(users);
		
	}

	public void update(EditProfile editProfile,Users user, String file) {
		
		user.setLoginId(editProfile.getLoginId());
		user.setName(editProfile.getName());
		user.setEmail(editProfile.getEmail());
		user.setProfile(editProfile.getProfile());
		user.setIconUri(file);
		
		repository.save(user);
		
	}

}
