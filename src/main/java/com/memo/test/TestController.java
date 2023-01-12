package com.memo.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.memo.post.dao.PostDAO;

@Controller
public class TestController {
	@GetMapping("/test1")
	@ResponseBody
	public String test1() {
		return "Hello world!";
	}

	@GetMapping("/test2")
	@ResponseBody
	public Map<String, Object> test2() {
		Map<String, Object> map = new HashMap<>();
		map.put("1", 1);
		map.put("2", 2);
		map.put("3", 3);

		return map;
	}

	@GetMapping("/test3")
	public String TestView() {
		return "test/test";
	}

	@Autowired
	private PostDAO postDAO;

	@GetMapping("/test4")
	@ResponseBody
	public List<Map<String, Object>> test4() {
		return postDAO.selectPostListTest();
	}
}
