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
				<a></a>
				<img alt="업로드 이미지" src="${post.imagePath}" width="300">
			</div>
		</c:if>

		<div class="d-flex justify-content-end my-4">
			<input type="file" id="file" accept=".jpg,.jpeg,.png,.gif"> <a
				href="#" id="fileUploadBtn"><img width="34" src=""></a>
		</div>
		<div class="d-flex justify-content-between mb-4">
			<button type="button" id="postListBtn" class="btn btn-dark">목록</button>
			<div>
				<button type="button" id="clearBtn" class="btn btn-secondary">모두
					지우기</button>
				<button type="button" id="postCreateBtn" class="btn btn-info">저장</button>
			</div>
		</div>
	</div>
</div>