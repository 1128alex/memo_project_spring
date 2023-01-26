package com.memo.post.bo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	// private Logger logger = LoggerFactory.getLogger(PostBO.class);
	private Logger logger = LoggerFactory.getLogger(this.getClass());

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

	public void updatePost(int userId, String userLoginId, int postId, String subject, String content,
			MultipartFile file) {

		// 기존 글을 가져온다. (이미지가 교체될 때 기존 이미지 제거를 위해)
		Post post = getPostByPostIdUserId(postId, userId);
		if (post == null) {
			logger.warn("[update post] 수정할 메모가 존재하지 않습니다. postId:{}, userId{}", postId, userId);
			return;
		}

		// 멀티파일이 비어있지 않다면 업로드 후 imagePath
		String imagePath = null;
		if (file != null) {
			imagePath = fileManagerService.saveFile(userLoginId, file);

			if (imagePath != null && post.getImagePath() != null) {
				fileManagerService.deleteFile(post.getImagePath());
			}

		}

		// db 업데이트
		postDAO.updatePostByPostIdUserId(postId, userId, subject, content, imagePath);
	}

	public int deletePostByPostIdUserId(int postId, int userId) {
		// 기존글 가져오기
		Post post = getPostByPostIdUserId(postId, userId);
		if (post == null) {
			logger.warn("[글 삭제] post is null. postId:{}, userId:{}", postId, userId);
			return 0;
		}

		// 업로드 되었던 이미지가 있으면 파일 삭제
		if (post.getImagePath() != null) {
			fileManagerService.deleteFile(post.getImagePath());
		}

		// DB delete
		return postDAO.deletePostByPostIdUserId(postId, userId);
	}
}
