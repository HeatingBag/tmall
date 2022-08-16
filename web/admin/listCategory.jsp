<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<script>
	$(function() {

		$("#addForm").submit(function() {
			if (!checkEmpty("name", "分类名称"))
				return false;
			if (!checkEmpty("categoryPic", "分类图片"))
				return false;
			return true;
		});
	});
</script>

<title>分类管理</title>


<div class="workingArea">
	<h1 class="label label-info">分类管理</h1>
	<br> <br>

	<div class="listDataTableDiv">
		<table
			class="table table-striped table-bordered table-hover  table-condensed">
			<thead>
				<tr class="success">
					<th>ID</th>
					<th>图片</th>
					<th>分类名称</th>
					<th>属性管理</th>
					<th>产品管理</th>
					<th>编辑</th>
					<th>删除</th>
				</tr>
			</thead>
			<tbody>
				<!-- 			这里拿到了从CategoryServlet.list()传过来的thecs:request.setAttribute("thecs", cs); -->
				<!-- 			此时thecs就是集合cs -->
				<c:forEach items="${thecs}" var="c">

					<!-- 				取出cs的里Category的id,name，准备后面的那几个按钮(编辑，删除啥的)，把数据显示出来 -->
					<tr>
						<td>${c.id}</td>
						<td><img height="40px" src="img/category/${c.id}.jpg"></td>
						<td>${c.name}</td>
						<!-- 		属性管理超链 -->
						<td><a href="admin_property_list?cid=${c.id}"><span
								class="glyphicon glyphicon-th-list"></span></a></td>
						<!-- 		产品管理超链 -->
						<td><a href="admin_product_list?cid=${c.id}"><span
								class="glyphicon glyphicon-shopping-cart"></span></a></td>
						<!-- 		用于编辑的超链，指向地址admin_category_edit,并且会传递当前分类对象的id过去 -->
						<td><a href="admin_category_edit?id=${c.id}"><span
								class="glyphicon glyphicon-edit"></span></a></td>
						<!-- 		用于删除的超链，指向地址admin_category_delete,并且会传递当前分类对象的id过去。		 -->
						<td><a deleteLink="true"
							href="admin_category_delete?id=${c.id}"><span
								class=" 	glyphicon glyphicon-trash"></span></a></td>

					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<div class="pageDiv">
		<%@include file="../include/admin/adminPage.jsp"%>
	</div>

	<div class="panel panel-warning addDiv">
		<div class="panel-heading">新增分类</div>
		<div class="panel-body">
			<!-- 	  1. form的action="admin_category_add"，会导致访问CategoryServlet的add()方法 -->
			<!-- 2. method="post" 用于保证中文的正确提交  3. 必须有enctype="multipart/form-data"，这样才能上传文件-->
			<form method="post" id="addForm" action="admin_category_add"
				enctype="multipart/form-data">
				<table class="addTable">
					<tr>
						<td>分类名称</td>
						<td><input id="name" name="name" type="text"
							class="form-control"></td>
					</tr>
					<tr>
						<td>分类圖片</td>
						<td>
							<!-- 	    				4. accept="image/*" 这样把上传的文件类型限制在了图片 --> <input
							id="categoryPic" accept="image/*" type="file" name="filepath" />
						</td>
					</tr>
					<tr class="submitTR">
						<td colspan="2" align="center">
							<button type="submit" class="btn btn-success">提 交</button>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>

</div>

<%@include file="../include/admin/adminFooter.jsp"%>