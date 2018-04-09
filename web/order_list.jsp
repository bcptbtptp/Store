<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>

	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>会员登录</title>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
		<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="js/bootstrap.min.js" type="text/javascript"></script>
		<!-- 引入自定义css文件 style.css -->
		<link rel="stylesheet" href="css/style.css" type="text/css" />

		<style>
			body {
				margin-top: 20px;
				margin: 0 auto;
			}
			
			.carousel-inner .item img {
				width: 100%;
				height: 300px;
			}
		</style>
	</head>

	<body>

			<jsp:include page="include/header.jsp"></jsp:include>

		<div class="container">
			<div class="row">

				<div style="margin:0 auto; margin-top:10px;width:950px;">
					<strong>我的订单</strong>
					<table class="table table-bordered">
						<c:forEach var="o" items="${page.list}">
						<tbody>
							<tr class="success">
								<th colspan="5">订单编号:<a href="order?method=orderDetails&oid=${o.oid }">${o.oid }</a> </th>
							</tr>
							<tr class="warning">
								<th>图片</th>
								<th>商品</th>
								<th>价格</th>
								<th>数量</th>
								<th>小计</th>
							</tr>
							<c:forEach var="oi" items="${o.orderItems}">
							<tr class="active">
								<td width="60" width="40%">
									<input type="hidden" name="id" value="22">
									<img src=" ${oi.product.pimage }" width="70" height="60">
								</td>
								<td width="30%">
									<a target="_blank"> ${oi.product.pname }</a>
								</td>
								<td width="20%">
									￥ ${oi.product.shop_price }
								</td>
								<td width="10%">
									${oi.count }
								</td>
								<td width="15%">
									<span class="subtotal">￥ ${oi.subtotal }</span>
								</td>
							</tr>
							</c:forEach>
						</tbody>
						</c:forEach>
						
					</table>
				</div>
			</div>
			<!--分页 -->
		<div style="width:380px;margin:0 auto;margin-top:50px;">
			<ul class="pagination" style="text-align:center; margin-top:10px;">
				<li class="disabled"><a href="order?method=myOrders&pageNo=${page.currPage - 1}" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
				<c:forEach begin="1" end="${page.totalPage }" varStatus="i">
					<li <c:if test="${page.currPage==i.index }">class="active"</c:if>><a href="order?method=myOrders&pageNo=${i.index}">${i.index}</a></li>
				</c:forEach>
				<li>
					<a href="order?method=myOrders&pageNo=${page.currPage + 1}" aria-label="Next">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
			</ul>
		</div>
		<!-- 分页结束=======================        -->
		</div>

			<jsp:include page="include/footer.jsp"/>
	</body>

</html>