<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>



<title>编辑分类</title>


<script>
$(function(){
	
	$("#editForm").submit(function(){
		if(!checkEmpty("name","分类名称"))
			return false;

		return true;
	});
});

</script>

<div class="workingArea">

	<ol class="breadcrumb">
	  <li><a href="admin_category_list">所有分类</a></li>
	  <li class="active">编辑分类</li>
	</ol>

	<div class="panel panel-warning editDiv">
	  <div class="panel-heading">编辑分类</div>
	  <div class="panel-body">
<!-- 	  编辑页面提交数据到admin_category_update -->
<!-- 	  1. method="post" 用于提交中文 -->
<!-- 	  2. enctype="multipart/form-data" 用于提交二进制文件 -->
	    	<form method="post" id="editForm" action="admin_category_update"  enctype="multipart/form-data">
	    		<table class="editTable">
	    			<tr>
	    				<td>分类名称</td>
<!-- 	    				在editCategory.jsp页面里，获取由CategoryServlet.edit() 通过request传递过来的Category对象，获取name和id -->
	    				<td><input  id="name" name="name" value="${c.name}" type="text" class="form-control"></td>
	    			</tr>
	    			<tr>
	    				<td>分类圖片</td>
	    				<td>
	    					<input id="categoryPic" accept="image/*" type="file" name="filepath" />
	    				</td>
	    			</tr>	    			
	    			<tr class="submitTR">
	    				<td colspan="2" align="center">
	    					<input type="hidden" name="id" value="${c.id}">
	    					<button type="submit" class="btn btn-success">提 交</button>
	    				</td>
	    			</tr>
	    		</table>
	    	</form>
	  </div>
	</div>	
</div>