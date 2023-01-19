package com.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {

	@Autowired
	private PostDAO postDAO;

	@Autowired
	private FileManagerService fileManagerService;

	public List<Post> getPostList() {
		return postDAO.selectPostList();
	}

	public int addPost(int userId, String userLoginId, String subject, String content, MultipartFile file) {
		String imagePath = null;

		if (file != null) {
			imagePath = fileManagerService.saveFile(userLoginId, file);
		}

		// 파일 업로드 => 경로
		return postDAO.insertPost(userId, subject, content, imagePath);
		// dao insert
	}

	public Post getPostByPostIdUserId(int postId, int userId) {
		return postDAO.getPostByPostIdUserId(postId, userId);

	}

}
