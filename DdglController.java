package com.application.action.gysxt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.application.bo.gysxt.IDdglBO;
import com.application.bo.gysxt.IDdpsfkBO;
import com.application.model.Order;
import com.application.model.OrderDetail;
import com.application.model.SubOrderDetail;
import com.common.core.tag.ui.page.Pager;
import com.common.core.web.ApplicationController;
/**
 * @Description TODO
 * @author http://www.bwbroad.com 吴强
 * @date 2013-7-3 上午9:16:26
 * @Copyrigth 首自信
 */
@Controller
@RequestMapping("/gysxt/ddgl")
public class DdglController extends ApplicationController {
	public static final String VIEW_PATH = "/application/gysxt/ddgl";
	@Autowired
	private IDdglBO ddglBO;
	@Autowired
	private IDdpsfkBO ddpsfkBO;
	/**
	 * @Description 返回分页列表 
	 * @author  http://www.bwbroad.com   吴强
	 * @param model   mvc model
	 * @param pager   分页参数信息
	 * @param params  其他参数
	 * @return        转向的页面
	 */
	@RequestMapping(value = "/list")
    public String list(Model model,Pager<Order> pager,@RequestParam Map<String, Object> params) {
		ddglBO.queryByPage(pager,params);
		model.addAttribute("pager", pager);
		return VIEW_PATH + "/list";
	}
	
	/**
	 * @Description 供应商对应的订单信息
	 * @author  http://www.bwbroad.com   吴强
	 * @param model   mvc model
	 * @param pager   分页参数信息
	 * @param params  其他参数
	 * @return        转向的页面
	 */
	@RequestMapping(value = "/gysList")
    public String gysList(Model model,Pager<Order> pager,@RequestParam Map<String, Object> params) {
		ddglBO.gysQueryByPage(pager,params);
		model.addAttribute("pager", pager);
		return VIEW_PATH + "/gysList";
	}
	
	/**
	 * @Description 根据类型跳转页面
	 * @author  http://www.bwbroad.com   吴强
	 * @param model
	 * @param id    id 订单ID
	 * @param type  add,view,edit
	 * @return
	 */
	@RequestMapping(value = "/goOperatePage")
    public String goOperatePage(Model model,String id,String type) {
		model.addAttribute("orderId", id);
		if("add".equals(type)){
			return VIEW_PATH+"/add";
		}else if("edit".equals(type)){
			//根据配送ID查询配送信息
			Order order = ddglBO.queryById(id);
			//订单信息返回
			model.addAttribute("order", order);
			return VIEW_PATH+"/edit";
		}else{
			return VIEW_PATH+"/detail";
		}
	}

	/**
	 * @Description 详情页面查询
	 * @author http://www.bwbroad.com 吴强
	 * @param id id 
	 * @return
	 */
	@RequestMapping(value = "/queryOrder")
	@ResponseBody
	public Map<String, List<Order>> queryOrder(String id) {
		// 查询当前配送单信息
		List<Order> orderList = new ArrayList<Order>();
		Order order = ddglBO.queryById(id);
		orderList.add(order);
		// 定义返回的数据格式
		Map<String, List<Order>> map = new HashMap<String, List<Order>>();
		map.put("Rows", orderList);
		return map;
	}
	
	/**
	 * @Description 详情页面订单详情查询
	 * @author http://www.bwbroad.com 吴强
	 * @param id id
	 * @return
	 */
	@RequestMapping(value = "/queryOrderDetail")
	@ResponseBody
	public Map<String, List<OrderDetail>> queryOrderDetail(String id) {
		// 查询当前配送单信息
		List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
		orderDetailList = ddglBO.queryDetailById(id);
		// 定义返回的数据格式
		Map<String, List<OrderDetail>> map = new HashMap<String, List<OrderDetail>>();
		map.put("Rows", orderDetailList);
		return map;
	}
	
	/**
	 * @Description 弹出对话框显示信息,分组查询采购订单信息
	 * @author http://www.bwbroad.com  吴强 
	 * @param model   mvc model
	 * @param pager   分页参数信息
	 * @param params  其他参数
	 * @return
	 */
	@RequestMapping(value = "/listForDialog")
    public String listForDialog(Model model,Pager<Order> pager,@RequestParam Map<String, Object> params) {
		pager.setPageSize(5);
		ddglBO.queryByPage(pager,params);
		model.addAttribute("pager", pager);
		return VIEW_PATH + "/listForDialog";
	}
	
	/**
	 * @Description 插入和修改
	 * @author http://www.bwbroad.com 吴强
	 * @param 订单信息
	 */
	@RequestMapping(value = "/insertAndUpdate", method = RequestMethod.POST)
	@ResponseBody
	public void insertAndUpdate( @RequestParam Map<String, Object> params) {
		String jsobject = (String) params.get("order");// 这个是jsp页面传过来的对象字符串 
		JSONObject jsObj = JSONObject.fromObject(jsobject);// 根据字符串转换对象 
		Map<String,Object> classMap = new HashMap<String, Object>();
		classMap.put("orderDetail", OrderDetail.class);
		Order order = (Order) JSONObject.toBean(jsObj, Order.class, classMap) ;// 把值绑定成相应的值对象 
		ddglBO.saveOrUpdate(order);
	}
	/**
	 * @Description 配送单编辑需要的订单查询
	 * @author http://www.bwbroad.com 吴强
	 * @param sendId 配送单ID
	 * @return
	 */
	@RequestMapping(value = "/queryOrderBySendId")
	@ResponseBody
	public Map<String, List<Order>> queryOrderBySendId(String sendId) {
		// 查询当前配送单信息
		List<Order> orderList = new ArrayList<Order>();
		orderList = ddglBO.queryOrderBySendId(sendId);
		// 定义返回的数据格式
		Map<String, List<Order>> map = new HashMap<String, List<Order>>();
		map.put("Rows", orderList);
		return map;
	}
	
	/**
	 * @Description 根据订单ID与物料信息查询配送与入库信息
	 * @author http://www.bwbroad.com 吴强
	 * @param orderId 订单ID
	 * @param goodsCode 物料编码
	 * @return
	 */
	@RequestMapping(value = "/querySubOrderDetailByCode")
	@ResponseBody
	public Map<String, List<SubOrderDetail>> querySubOrderDetailByCode(String orderId, String goodsCode) {
		//根据订单ID与物料信息查询配送与入库信息
		List<SubOrderDetail> subOrderDetail = new ArrayList<SubOrderDetail>();
		subOrderDetail = ddpsfkBO.querySubOrderDetailByCode(orderId, goodsCode);
		// 定义返回的数据格式
		Map<String, List<SubOrderDetail>> map = new HashMap<String, List<SubOrderDetail>>();
		map.put("Rows", subOrderDetail);
		return map;
	}
	
	/**
	 * @Description 订单下发
	 * @author http://www.bwbroad.com 吴强
	 * @param id id 
	 * @return
	 */
	@RequestMapping(value = "/issuedOrder")
	@ResponseBody
	public Map<String,Object> updateOrder(@RequestParam("id") String[] id) {
		Map<String,Object> map = new HashMap<String, Object>();
		String result = ddglBO.updateOrder(id);
		map.put("msg", result);
		return map;
	}
}
