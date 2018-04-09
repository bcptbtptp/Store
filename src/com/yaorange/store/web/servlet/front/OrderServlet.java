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
	 * ��������
	 */
	public String details(HttpServletRequest req, HttpServletResponse resp) {
		String oid = UUIDUtils.getUUID();
		req.getSession().setAttribute("oid", oid);

		return "order_info.jsp";
	}

	/**
	 * ȷ�϶���
	 */
	public String confirmOrder(HttpServletRequest req, HttpServletResponse resp) {
		// �ж��Ƿ��¼
		User user = (User) req.getSession().getAttribute("user");
		if (user == null) {
			return "login.jsp";
		}
		// ��ȡ��
		String address = req.getParameter("address");
		String name = req.getParameter("name");
		String telephone = req.getParameter("telephone");
		// ���涩��(����,������)
		// ����
		Order order = new Order();
		order.setAddress(address);
		order.setName(name);
		order.setOid((String) req.getSession().getAttribute("oid"));
		order.setOrdertime(new Date());
		order.setState(0);
		order.setTelephone(telephone);
		// order.setTotal(total);
		order.setUid(user.getUid());

		// ������
		@SuppressWarnings("unchecked")
		Map<String, Product> cart = (Map<String, Product>) req.getSession().getAttribute("cart");
		orderService.save(order, cart);

		req.getSession().removeAttribute("cart");
		// ����֧������
		// ׼���������
		String p0_Cmd = "Buy";
		String p1_MerId = "10001126856";// ��ʵ
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

		// ��Կ
		String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";

		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
				p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);

		// ƴװһ����ɵ�֧������url
		StringBuilder sValue = new StringBuilder("https://www.yeepay.com/app-merchant-proxy/node?");
		// ҵ������
		sValue.append("p0_Cmd=").append(p0_Cmd).append("&");
		// �̻����
		sValue.append("p1_MerId=").append(p1_MerId).append("&");
		// �̻�������
		sValue.append("p2_Order=").append(p2_Order).append("&");
		// ֧�����
		sValue.append("p3_Amt=").append(p3_Amt).append("&");
		// ���ױ���
		sValue.append("p4_Cur=").append(p4_Cur).append("&");
		// ��Ʒ����
		sValue.append("p5_Pid=").append(p5_Pid).append("&");
		// ��Ʒ����
		sValue.append("p6_Pcat=").append(p6_Pcat).append("&");
		// ��Ʒ����
		sValue.append("p7_Pdesc=").append(p7_Pdesc).append("&");
		// �̻�����֧���ɹ����ݵĵ�ַ
		sValue.append("p8_Url=").append(p8_Url).append("&");
		// �ͻ���ַ
		sValue.append("p9_SAF=").append(p9_SAF).append("&");
		// �̻���չ��Ϣ
		sValue.append("pa_MP=").append(pa_MP).append("&");
		// ���б���
		sValue.append("pd_FrpId=").append(pd_FrpId).append("&");
		// Ӧ�����
		sValue.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
		sValue.append("hmac=").append(hmac);

		return "redirect:" + sValue.toString();
	}

	/**
	 * ֧������ص�
	 */
	public String callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// �ڹ�˾��һ���� ��֤�����Ƿ񱻴۸Ĺ�
		// ��֤������Դ��������Ч��
		// �Ķ�֧���������˵��
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
		// ���ñ�����Կ�ͼ����㷨 ��������
		String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
		boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid,
				r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
		if (isValid) {
			// ��Ч
			if (r1_Code != null && !r1_Code.isEmpty() && r1_Code.equals("1") && r9_BType.equals("1")) {
				// ��ʾ֧���ɹ�
				// �޸Ķ���״̬Ϊ ��֧��
				// ���ݶ������޸�״̬Ϊ1
				orderService.updateState(r6_Order);

				request.setAttribute("msg", "֧���ѳɹ�!");
			} else if (r9_BType.equals("2")) {
				// �޸Ķ���״̬:
				// ��������Ե㣬�������ױ���֪ͨ
				System.out.println("�յ��ױ�֪ͨ���޸Ķ���״̬��");
				orderService.updateState(r6_Order);
				// �ظ����ױ�success��������ظ����ױ���һֱ֪ͨ
				response.getWriter().print("success");
				return null;
			} else {
				request.setAttribute("msg", "֧��ʧ�ܣ��´�����!");
			}
		} else {
			request.setAttribute("msg", "֧��ʧ�ܣ����ݱ��۸�!");
		}
		return "msg.jsp";
	}

	/**
	 * �ҵĶ�������
	 */
	public String myOrders(HttpServletRequest req, HttpServletResponse resp) {
		String uid = getUser(req).getUid();
		String currPage = req.getParameter("pageNo");
		// ��ѯ����ǰ�û����еĶ�����ҳ�б�
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
