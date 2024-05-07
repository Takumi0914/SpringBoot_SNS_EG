package com.example.eg_sns.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class EditPassword extends DtoBase {
	
	/** パスワード */
	@NotBlank(message = "現在のパスワードを入力してください。")
	@Size(max = 32, message = "パスワードは最大32文字です。")
	private String password;
	/** パスワード */
	@NotBlank(message = "新しいパスワードを入力してください。")
	@Size(max = 32, message = "パスワードは最大32文字です。")
	private String newpassword1;
	
	@NotBlank(message = "新しいパスワードを再度入力してください。")
	@Size(max = 32, message = "パスワードは最大32文字です。")
	private String newpassword2;
	
	@AssertTrue(message = "新しいパスワードは同一にしてください")
	public boolean isPasswordValid() {
		if(newpassword1 == null || newpassword1.isEmpty()) {
			return true;
		}
		return newpassword1.equals(newpassword2) ;
	}
}