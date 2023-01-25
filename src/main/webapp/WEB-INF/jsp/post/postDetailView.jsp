<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Core -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글 상세/수정</h1>
		<input type="text" id="subject" class="form-control"
			placeholder="제목을 입력하세요" value="${post.subject}">
		<textarea class="form-control" id="content" rows="15" cols=""
			placeholder="내용을 입력하세요">${post.content}</textarea>
		<%-- 이미지가 있을 때만 이미지 영역 추가 --%>
		<c:if test="${not empty post.imagePath}">
			<div class="mt-3">
				<a></a> <img alt="업로드 이미지" src="${post.imagePath}" width="300">
			</div>
		</c:if>

		<div class="d-flex justify-content-end my-4">
			<input type="file" id="file" accept=".jpg,.jpeg,.png,.gif"> <a
				href="#" id="fileUploadBtn"><img width="34" src=""></a>
		</div>
		<div class="d-flex justify-content-between">
			<button type="button" id="postDeleteBtn" class="btn btn-secondary">삭제</button>

			<div>
				<a id="postListBtn" class="btn btn-dark" href="/post/post_list_view">목록으로</a>
				<button type="button" id="postUpdateBtn" class="btn btn-info"
					data-post-id="${post.id}">수정</button>
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function() {
		$('#postUpdateBtn').on('click', function() {
			let subject = $('#subject').val().trim();
			if (subject == '') {
				alert("제목을 입력하세요");
				return;
			}

			let content = $('#content').val();
			console.log(content);

			let file = $('#file').val();
			console.log(file);

			// 파일이 업로드 된 경우 확장자 체크
			if (file != '') {
				let ext = file.split(".").pop().toLowerCase();
				if ($.inArray(ext, [ 'jpg', 'jpeg', 'png', 'gif' ]) == -1) {
					alert("이미지 파일만 업로드 할 수 있습니다.");
					$('#file').val('');
					return;
				}
			}
			let postId = $(this).data('post-id');

			let formData = new FormData();
			formData.append("postId", postId);
			formData.append("subject", subject);
			formData.append("content", content);
			formData.append("file", $('#file')[0].files[0]);

			$.ajax({
				type : "PUT",
				url : "/post/update",
				data : formData,
				enctype : "multipart/form-data",
				processData : false,
				contentType : false,
				success : function(data) {
					if (data.code == 1) {
						alert("메모가 수정되었습니다.");
						location.reload(true);
					} else {
						alert("에러.");
					}
				},
				error : function(e) {
					alert(e);
				}
			});
		});
	});
</script>
