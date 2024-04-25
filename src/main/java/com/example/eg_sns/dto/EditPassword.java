package com.example.eg_sns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class EditPassword extends DtoBase {
	
	/** パスワード */
	@NotBlank(message = "パスワードを入力してください。")
	@Size(max = 32, message = "パスワードは最大32文字です。")
	private String password;
	/** パスワード */
	@NotBlank(message = "パスワードを入力してください。")
	@Size(max = 32, message = "パスワードは最大32文字です。")
	private String newpassword;
}