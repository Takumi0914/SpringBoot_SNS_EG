package com.example.eg_sns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.eg_sns.core.annotation.LoginCheck;
import com.example.eg_sns.service.TopicsService;

import lombok.extern.log4j.Log4j2;

/**
 * ホームコントローラー。
 *
 * @author tomo-sato
 */
@LoginCheck
@Log4j2
@Controller
@RequestMapping("/home")
public class HomeController {

	/** トピック関連サービスクラス。 */
	@Autowired
	private TopicsService topicsService;

	/**
	 * [GET]ホーム画面のアクション。
	 *
	 * @param model 画面にデータを送るためのオブジェクト
	 */
	@GetMapping(path = {"", "/"})
	public String index(Model model) {
		log.info("ホーム画面のアクションが呼ばれました。");

//		// トピック全件取得する。
//		List<Topics> topicsList = topicsService.findAllTopics();
//		model.addAttribute("topicsList", topicsList);

		return "home/index";
	}
}
