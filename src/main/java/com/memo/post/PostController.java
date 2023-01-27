package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {

	@Autowired
	private PostBO postBO;

	@GetMapping("/post_list_view")
	public String postListView(@RequestParam(value = "prevId", required = false) Integer prevIdParam,
			@RequestParam(value = "nextId", required = false) Integer nextIdParam, Model model, HttpSession session) {

		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/sign_in_view";
		}

		int prevId = 0;
		int nextId = 0;
		List<Post> postList = postBO.getPostListByUserId(userId, prevIdParam, nextIdParam);
		if (postList.isEmpty() == false) { // postList가 비었을 때 에러 방지
			prevId = postList.get(0).getId();
			nextId = postList.get(postList.size() - 1).getId();

			// 이전 방향의 끝인가? postList의 0 index 값과 post 테이블의 가장 큰 값이 같으면 마지막 페이지
			if (postBO.isPrevLastPage(prevId, nextId)) { // 마지막 페이지일 때
				prevId = 0;
			}
			// 다음 방향의 끝인가?
			if (postBO.isNextLastPage(prevId, nextId)) { // 마지막 페이지일 때
				nextId = 0;
			}
		}

		model.addAttribute("prevId", prevId); // 가져온 리스트 중 가장 앞쪽(큰 id)
		model.addAttribute("nextId", nextId); // 가져온 리스트 중 가장 뒤쪽(작은 id)

		List<Post> posts = postBO.getPostList();
		model.addAttribute("posts", posts);
		model.addAttribute("viewName", "post/postList");
		return "template/layout";
	}

	@GetMapping("/post_create_view")
	public String postCreateView(Model model) {
		model.addAttribute("viewName", "post/postCreate");
		return "template/layout";
	}

	@GetMapping("/post_detail_view")
	public String postDetailView(@RequestParam("postId") int postId, HttpSession session, Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/sign_in_view";
		}

		// DB select by - userId, postId
		Post post = postBO.getPostByPostIdUserId(postId, userId);

		model.addAttribute("post", post);
		model.addAttribute("viewName", "post/postDetailView");

		return "template/layout";
	}
}
