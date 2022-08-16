
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<title>模仿天猫官网-${c.name}</title>
<div id="category">
	<div class="categoryPageDiv">
		<!-- 	1. 显示当前分类图片 -->
		<!-- 	2. 排序条 sortBar.jsp -->
		<!-- 	3. 产品列表 productsByCategory.jsp -->
		<img src="img/category/${c.id}.jpg">
		<%@include file="sortBar.jsp"%>
		<%@include file="productsByCategory.jsp"%>
	</div>

</div>