package com.example.eg_sns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ショルダー確認用コントローラー。
 * ※ショルダー（ヘッダー、フッター、その他ショルダー）の見栄えを確認するためのコントローラー。
 *
 * @author tomo-sato
 */
@Controller
@RequestMapping("/shoulder")
public class ShoulderController  extends AppController{

	@GetMapping(path = {"", "/", "usersID"})
	public String index(Model model,@PathVariable(required = false) Long usersId) {
		
		Long loginId = getUsersId();
		
		model.addAttribute("loginId", loginId);
        model.addAttribute("usersId",usersId);
		
		return "common/shoulder_fragment";
	}
}
