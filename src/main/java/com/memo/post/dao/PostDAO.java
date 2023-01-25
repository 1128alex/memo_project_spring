package com.memo.post.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.memo.post.model.Post;

@Repository
public interface PostDAO {
	public List<Map<String, Object>> selectPostListTest();

	public List<Post> selectPostList();

	public int insertPost(@Param("userId") int userId, @Param("subject") String subject,
			@Param("content") String content, @Param("imagePath") String imagePath);

	public Post getPostByPostIdUserId(@Param("postId") int postId, @Param("userId") int userId);

	public void updatePostByPostIdUserId(@Param("postId") int postId, @Param("userId") int userId, @Param("subject") String subject,
			@Param("content") String content, @Param("imagePath") String imagePath);

}
