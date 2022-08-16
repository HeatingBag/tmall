
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
	
<div class="searchProducts">
<!-- 	1. 遍历ps，把每个产品的图片，价格，标题等信息显示出来 -->
	<c:forEach items="${ps}" var="p">
		<div class="productUnit" price="${p.promotePrice}">
			<a href="foreproduct?pid=${p.id}">
				<img class="productImage" src="img/productSingle/${p.firstProductImage.id}.jpg">
			</a>
			<span class="productPrice">¥<fmt:formatNumber type="number" value="${p.promotePrice}" minFractionDigits="2"/></span>
			<a class="productLink" href="foreproduct?pid=${p.id}">
			 ${fn:substring(p.name, 0, 50)}
			</a>
			
			<a class="tmallLink" href="foreproduct?pid=${p.id}">天猫专卖</a>

			<div class="show1 productInfo">
				<span class="monthDeal ">月成交 <span class="productDealNumber">${p.saleCount}笔</span></span>
				<span class="productReview">评价<span class="productReviewNumber">${p.reviewCount}</span></span>
				<span class="wangwang"><img src="img/site/wangwang.png"></span>
			</div>
			
		</div>
	</c:forEach>
<!-- 	2. 如果ps为空，则显示 "没有满足条件的产品" -->
	<c:if test="${empty ps}">
		<div class="noMatch">没有满足条件的产品<div>
	</c:if>
	
		<div style="clear:both"></div>
</div>