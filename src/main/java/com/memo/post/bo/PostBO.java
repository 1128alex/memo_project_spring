package com.memo.post.bo;

import java.util.Collections;
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

	private static final int POST_MAX_SIZE = 3;

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

	public List<Post> getPostListByUserId(int userId, Integer prevId, Integer nextId) {
		// 게시글 번호: 10 9 8 | 7 6 5 | 4 3 2 | 1
		// 만약 4 3 2 페이지에 있을 때
		// 1) 이전: 정방향ASC 4보다 큰 3개 (5 6 7) => List reverse(7 6 5)
		// 2) 다음: 2보다 작은 3개 DESC
		// 3) 첫페이지(이전, 다음 없음) DESC 3개
		String direction = null; // 방향
		Integer standardId = null; // 기준 postId

		if (prevId != null) {
			direction = "prev";
			standardId = prevId;

			List<Post> postList = postDAO.selectPostListByUserId(userId, direction, standardId, userId);
			Collections.reverse(postList);
			return postList;
		} else if (nextId != null) {
			direction = "next";
			standardId = nextId;
		}
		// 펏페이지일 때(페이징 X) standardId, direction이 null
		//
		return postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
	}

	public boolean isPrevLastPage(int prevId, int userId) {
		int maxPostId = postDAO.selectPostIdByUserIdSort(userId, "DESC");
		return maxPostId == prevId ? true : false;
	}

	public boolean isNextLastPage(int nextId, int userId) {
		int minPostId = postDAO.selectPostIdByUserIdSort(userId, "ASC");
		return minPostId == nextId ? true : false;
	}
}
