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
	public String postListView(Model model) {
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
