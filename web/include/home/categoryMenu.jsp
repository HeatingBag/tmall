
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
	
<div class="categoryMenu">
<!-- ForeServlet的request.setAttribute("cs", cs);传递过来的Category集合 -->
		<c:forEach items="${cs}" var="c">
			<div cid="${c.id}" class="eachCategory">
				<span class="glyphicon glyphicon-link"></span>
				<a href="forecategory?cid=${c.id}">
					${c.name}
				</a>
			</div>
		</c:forEach>
	</div>  