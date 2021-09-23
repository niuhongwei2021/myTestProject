package com.application.action.gysxt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.application.bo.gysxt.ISjddBO;
import com.application.model.TOrder;
import com.common.core.tag.ui.page.Pager;
import com.common.core.web.ApplicationController;
import com.common.utils.StringUtil;
/**
 * @Description TODO
 * @author http://www.bwbroad.com 吴强
 * @date 2013-6-17 下午3:21:01
 * @Copyrigth 首自信
 */
@Controller
@RequestMapping("/gysxt/sjdd")
public class SjddController extends ApplicationController {
	public static final String VIEW_PATH = "/application/gysxt/sjdd";
	@Autowired
	private ISjddBO sjddBO;
	/**
	 * @Description 返回分页列表 
	 * @author  http://www.bwbroad.com   吴强
	 * @param model   mvc model
	 * @param pager   分页参数信息
	 * @param params  其他参数
	 * @return        转向的页面
	 */
	@RequestMapping(value = "/list")
    public String list(Model model,Pager<TOrder> pager,@RequestParam Map<String, Object> params) {
		sjddBO.queryByPage(pager,params);
		model.addAttribute("pager", pager);
		return VIEW_PATH + "/list";
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
    public String listForDialog(Model model,Pager<TOrder> pager,@RequestParam Map<String, Object> params) {
		pager.setPageSize(5);
		sjddBO.queryByPageNew(pager,params);
		model.addAttribute("pager", pager);
		return VIEW_PATH + "/listForDialog";
	}
	
	/**
	 * @Description 插入和修改
	 * @author  http://www.bwbroad.com   吴强
	 * @param user
	 */
	@RequestMapping(value = "/insertAndUpdate" , method = RequestMethod.POST)
    public void insertAndUpdate(TOrder order) {
		sjddBO.saveOrUpdate(order);
	}
	
	
	/**
	 * @Description 删除
	 * @author  http://www.bwbroad.com   吴强
	 * @param id
	 */
	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public void delete( @RequestParam("id") String[] id) {
		sjddBO.deleteByIds(id);
	}
	
	/**
	 * @Description 显示插入和修改页面
	 * @author  http://www.bwbroad.com   吴强
	 * @param model
	 * @param id    id
	 * @param type  edit or add or view
	 * @return
	 */
	@RequestMapping(value = "/goInsertAndUpdate")
    public String goInsertAndUpdate(Model model,String id,String type) {
		if (StringUtil.isNotEmpty(id)) {
			model.addAttribute("order", sjddBO.queryById(id));
		}
		model.addAttribute("type", type);
		return VIEW_PATH + "/insertAndUpdate";
	}
	
	/**
	 * @Description 详情页面查询
	 * @author http://www.bwbroad.com 吴强
	 * @param id id
	 * @return
	 */
	@RequestMapping(value = "/queryOrder")
	@ResponseBody
	public Map<String, List<TOrder>> queryOrder(String id) {
		// 查询当前配送单信息
		List<TOrder> orderList = new ArrayList<TOrder>();
		orderList = sjddBO.queryByEbelnId(id);
		// 定义返回的数据格式
		Map<String, List<TOrder>> map = new HashMap<String, List<TOrder>>();
		map.put("Rows", orderList);
		return map;
	}
	
	/**
	 * @Description 配送单编辑需要的订单查询
	 * @author http://www.bwbroad.com 吴强
	 * @param sendId 配送单ID
	 * @return
	 */
	@RequestMapping(value = "/queryOrderBySendId")
	@ResponseBody
	public Map<String, List<TOrder>> queryOrderBySendId(String sendId) {
		// 查询当前配送单信息
		List<TOrder> orderList = new ArrayList<TOrder>();
		orderList = sjddBO.queryOrderBySendId(sendId);
		// 定义返回的数据格式
		Map<String, List<TOrder>> map = new HashMap<String, List<TOrder>>();
		map.put("Rows", orderList);
		return map;
	}
}
