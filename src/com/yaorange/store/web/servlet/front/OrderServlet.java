package com.yaorange.store.web.servlet.front;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yaorange.store.entity.Order;
import com.yaorange.store.entity.Page;
import com.yaorange.store.entity.Product;
import com.yaorange.store.entity.User;
import com.yaorange.store.service.OrderService;
import com.yaorange.store.service.impl.OrderServiceImpl;
import com.yaorange.store.utils.CookieUtils;
import com.yaorange.store.utils.PaymentUtil;
import com.yaorange.store.utils.UUIDUtils;
import com.yaorange.store.web.servlet.BaseServlet;

@WebServlet("/order")
public class OrderServlet extends BaseServlet {

	private static final long serialVersionUID = -7889694049564615742L;
	private OrderService orderService = new OrderServiceImpl();
	
	/**
	 * 订单详情
	 */
	public String details(HttpServletRequest req, HttpServletResponse resp) {
		String oid = UUIDUtils.getUUID();
		req.getSession().setAttribute("oid", oid);

		return "order_info.jsp";
	}

	/**
	 * 确认订单
	 */
	public String confirmOrder(HttpServletRequest req, HttpServletResponse resp) {
		// 判断是否登录
		User user = (User) req.getSession().getAttribute("user");
		if (user == null) {
			return "login.jsp";
		}
		// 获取表单
		String address = req.getParameter("address");
		String name = req.getParameter("name");
		String telephone = req.getParameter("telephone");
		// 保存订单(订单,订单项)
		// 订单
		Order order = new Order();
		order.setAddress(address);
		order.setName(name);
		order.setOid((String) req.getSession().getAttribute("oid"));
		order.setOrdertime(new Date());
		order.setState(0);
		order.setTelephone(telephone);
		// order.setTotal(total);
		order.setUid(user.getUid());

		// 订单项
		@SuppressWarnings("unchecked")
		Map<String, Product> cart = (Map<String, Product>) req.getSession().getAttribute("cart");
		orderService.save(order, cart);

		req.getSession().removeAttribute("cart");
		// 发出支付请求
		// 准备请求参数
		String p0_Cmd = "Buy";
		String p1_MerId = "10001126856";// 真实
		String p2_Order = order.getOid();
		String p3_Amt = "0.01";
		String p4_Cur = "CNY";
		String p5_Pid = "1";
		String p6_Pcat = "1";
		String p7_Pdesc = "1";
		String p8_Url = "http://localhost:99/store/order?method=callback";
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = req.getParameter("pd_FrpId");
		String pr_NeedResponse = "1";

		// 秘钥
		String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";

		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
				p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);

		// 拼装一个完成的支付请求url
		StringBuilder sValue = new StringBuilder("https://www.yeepay.com/app-merchant-proxy/node?");
		// 业务类型
		sValue.append("p0_Cmd=").append(p0_Cmd).append("&");
		// 商户编号
		sValue.append("p1_MerId=").append(p1_MerId).append("&");
		// 商户订单号
		sValue.append("p2_Order=").append(p2_Order).append("&");
		// 支付金额
		sValue.append("p3_Amt=").append(p3_Amt).append("&");
		// 交易币种
		sValue.append("p4_Cur=").append(p4_Cur).append("&");
		// 商品名称
		sValue.append("p5_Pid=").append(p5_Pid).append("&");
		// 商品种类
		sValue.append("p6_Pcat=").append(p6_Pcat).append("&");
		// 商品描述
		sValue.append("p7_Pdesc=").append(p7_Pdesc).append("&");
		// 商户接收支付成功数据的地址
		sValue.append("p8_Url=").append(p8_Url).append("&");
		// 送货地址
		sValue.append("p9_SAF=").append(p9_SAF).append("&");
		// 商户扩展信息
		sValue.append("pa_MP=").append(pa_MP).append("&");
		// 银行编码
		sValue.append("pd_FrpId=").append(pd_FrpId).append("&");
		// 应答机制
		sValue.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
		sValue.append("hmac=").append(hmac);

		return "redirect:" + sValue.toString();
	}

	/**
	 * 支付请求回调
	 */
	public String callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 在公司中一定得 验证数据是否被篡改过
		// 验证请求来源和数据有效性
		// 阅读支付结果参数说明
		// System.out.println("==============================================");
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String rb_BankId = request.getParameter("rb_BankId");
		String ro_BankOrderId = request.getParameter("ro_BankOrderId");
		String rp_PayDate = request.getParameter("rp_PayDate");
		String rq_CardNo = request.getParameter("rq_CardNo");
		String ru_Trxtime = request.getParameter("ru_Trxtime");

		// hmac
		String hmac = request.getParameter("hmac");
		// 利用本地密钥和加密算法 加密数据
		String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
		boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid,
				r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
		if (isValid) {
			// 有效
			if (r1_Code != null && !r1_Code.isEmpty() && r1_Code.equals("1") && r9_BType.equals("1")) {
				// 表示支付成功
				// 修改订单状态为 已支付
				// 根据订单号修改状态为1
				orderService.updateState(r6_Order);

				request.setAttribute("msg", "支付已成功!");
			} else if (r9_BType.equals("2")) {
				// 修改订单状态:
				// 服务器点对点，来自于易宝的通知
				System.out.println("收到易宝通知，修改订单状态！");
				orderService.updateState(r6_Order);
				// 回复给易宝success，如果不回复，易宝会一直通知
				response.getWriter().print("success");
				return null;
			} else {
				request.setAttribute("msg", "支付失败，下次再来!");
			}
		} else {
			request.setAttribute("msg", "支付失败，数据被篡改!");
		}
		return "msg.jsp";
	}

	/**
	 * 我的订单功能
	 */
	public String myOrders(HttpServletRequest req, HttpServletResponse resp) {
		String uid = getUser(req).getUid();
		String currPage = req.getParameter("pageNo");
		// 查询出当前用户所有的订单分页列表
		Page page = orderService.findPage(uid, currPage);

		req.setAttribute("page", page);
		return "order_list.jsp";
	}

	
	public String orderDetails(HttpServletRequest req,HttpServletResponse resp)
	{
		String  oid = req.getParameter("oid");
		Order order = orderService.findByOid(oid);
		req.setAttribute("order",order);
		return "order_details.jsp";
	}
}
