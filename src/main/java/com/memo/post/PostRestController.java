package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;
import com.memo.user.bo.UserBO;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@RestController
public class PostRestController {
	@Autowired
	private PostBO postBO;

	@PostMapping("/create")
	public Map<String, Object> create(@RequestParam("subject") String subject,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) {

		int userId = (int) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");

		int rowCount = postBO.addPost(userId, userLoginId, subject, content, file);

		Map<String, Object> result = new HashMap<>();

		if (rowCount > 0) {
			result.put("code", 1);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("errorMessage", "메모 저장에 실패했습니다.");
		}

		return result;
	}

	@PutMapping("/update")
	public Map<String, Object> update(@RequestParam("postId") int postId, @RequestParam("subject") String subject,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) {

		int userId = (int) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");

		postBO.updatePost(userId, userLoginId, postId, subject, content, file);
		// update db

		Map<String, Object> result = new HashMap<>();
		result.put("code", 1);
		result.put("result", "성공");

		return result;
	}

	@DeleteMapping("/delete")
	public Map<String, Object> delete(@RequestParam("postId") int postId, HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		// postBO
		int rowCount = postBO.deletePostByPostIdUserId(postId, userId);
		Map<String, Object> result = new HashMap<>();
		if (rowCount > 0) {
			result.put("code", 1);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("errorMessage", "에러가 났습니다. 관리자에게 문의해주세요.");
		}
		return result;
	}
}
