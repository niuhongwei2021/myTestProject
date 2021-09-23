package com.application.action.gysxt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.application.bo.gysxt.ICgglBO;
import com.application.model.Dept;
import com.application.model.DeptOrder;
import com.application.model.DeptOrderDetail;
import com.application.model.TMcode;
import com.common.core.tag.ui.page.Pager;
import com.common.core.web.ApplicationController;

@Controller
@RequestMapping("/gysxt/cgsqgl")
public class CgglController extends ApplicationController {
	public static final String VIEW_PATH="/application/gysxt/cgsqgl";
	@Autowired
	private ICgglBO cgglBO;
	/**
	 * @Description 返回分页列表
	 * @author  http://www.bwbroad.com   吴强
	 * @param model   mvc model
	 * @param pager   分页参数信息
	 * @param params  其他参数
	 * @return        转向的页面
	 */
	@RequestMapping(value = "/list")
    public String list(Model model,Pager<DeptOrder> pager,@RequestParam Map<String, Object> params) {
		cgglBO.queryByPage(pager,params);
		model.addAttribute("pager", pager);
		return VIEW_PATH+"/list";
	}
	
	/**
	 * @Description 返回分页列表
	 * @author http://www.bwbroad.com 吴强
	 * @param model
	 *            mvc model
	 * @param pager
	 *            分页参数信息
	 * @param params
	 *            其他参数
	 * @return 转向的页面
	 */
	@RequestMapping(value = "/mCodeList")
	@ResponseBody
	public Map<String, Object> mCodeList(Model model, Pager<TMcode> pager,
			@RequestParam Map<String, Object> params,HttpServletRequest request) {
		//获取ligerUI设置的页面信息
		int page=Integer.parseInt(request.getParameter("page"));
		int pagesize=Integer.parseInt(request.getParameter("pagesize"));
		//设置pager参数
		pager.setCurrentPage(page);
		pager.setPageSize(pagesize);
		// 查询物料信息
		List<TMcode> mCodeList = cgglBO.queryMCodeByPage(pager, params);
		// 定义返回的数据格式
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Rows", mCodeList);
		map.put("total", pager.getCount());//返回总页数给ligerUI
		return map;
	}
	
	/**
	 * @Description 插入
	 * @author http://www.bwbroad.com 吴强
	 * @param 采购计划信息
	 */
	@RequestMapping(value = "/insertDeptOrder", method = RequestMethod.POST)
	public void insertDeptOrder(Model model,@RequestParam Map<String, Object> params) {
		String jsobject = (String) params.get("deptOrder");// 这个是jsp页面传过来的对象字符串 
		JSONObject jsObj = JSONObject.fromObject(jsobject);// 根据字符串转换对象 
		Map<String,Object> classMap = new HashMap<String, Object>();
		classMap.put("deptOrderDetail", DeptOrderDetail.class);
		DeptOrder deptOrder = (DeptOrder) JSONObject.toBean(jsObj, DeptOrder.class, classMap) ;// 把值绑定成相应的值对象 
		//cgglBO.saveDeptOrder(deptOrder);
		//取采购计划ID
		long orderId = cgglBO.getDeptOrderId();
		deptOrder.setId(orderId);
		//获得登录用户对应的部门信息
		Dept dept = cgglBO.queryDeptByUser();
		model.addAttribute("deptOrder", deptOrder);
		model.addAttribute("dept", dept);
	}
	
	/**
	 * @Description 修改
	 * @author http://www.bwbroad.com 吴强
	 * @param 采购计划信息
	 */
	@RequestMapping(value = "/updateDeptOrder", method = RequestMethod.POST)
	@ResponseBody
	public void updateDeptOrder(Model model, @RequestParam Map<String, Object> params) {
		String jsobject = (String) params.get("deptOrder");// 这个是jsp页面传过来的对象字符串 
		JSONObject jsObj = JSONObject.fromObject(jsobject);// 根据字符串转换对象 
		Map<String,Object> classMap = new HashMap<String, Object>();
		classMap.put("deptOrderDetail", DeptOrderDetail.class);
		DeptOrder deptOrder = (DeptOrder) JSONObject.toBean(jsObj, DeptOrder.class, classMap) ;// 把值绑定成相应的值对象 
		cgglBO.updateDeptOrder(deptOrder);
	}
	
	/**
	 * @Description 提交
	 * @author http://www.bwbroad.com 吴强
	 * @param 采购计划信息
	 */
	@RequestMapping(value = "/updateCommit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateCommit(@RequestParam("id") String[] id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", cgglBO.updateCommit(id));
		return map;
	}
	
	/**
	 * @Description 根据类型跳转页面
	 * @author  http://www.bwbroad.com   吴强
	 * @param model
	 * @param id    id 计划ID
	 * @param type  add,view,edit
	 * @return
	 */
	@RequestMapping(value = "/goOperatePage")
    public String goOperatePage(Model model,String id,String type) {
		model.addAttribute("orderPlanId", id);
		return VIEW_PATH+"/detail";
	}
	@RequestMapping(value = "/goDetailPage")
    public String goDetailPage(Model model,String piid,String type) {
		model.addAttribute("piid", piid);
		return VIEW_PATH+"/flowDetail";
	}
	
	/**
	 * @Description 采购申请跳转
	 * @author  http://www.bwbroad.com   吴强
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/goApply")
    public String goApply(Model model) {
		Dept dept = cgglBO.queryDeptByUser();
		model.addAttribute("dept",dept);
		return VIEW_PATH+"/add";
	}
	
	/**
	 * @Description 详情页面查询
	 * @author http://www.bwbroad.com 吴强
	 * @param id id 
	 * @return
	 */
	@RequestMapping(value = "/queryDeptOrder")
	@ResponseBody
	public Map<String, List<DeptOrder>> queryDeptOrder(String id) {
		// 查询当前计划信息
		List<DeptOrder> deptOrderList = new ArrayList<DeptOrder>();
		DeptOrder deptOrder = cgglBO.queryById(id);
		deptOrderList.add(deptOrder);
		// 定义返回的数据格式
		Map<String, List<DeptOrder>> map = new HashMap<String, List<DeptOrder>>();
		map.put("Rows", deptOrderList);
		return map;
	}
	@RequestMapping(value = "/queryDeptOrderByPiid")
	@ResponseBody
	public Map<String, List<DeptOrder>> queryDeptOrderByPiid(String piid) {
		// 查询当前计划信息
		List<DeptOrder> deptOrderList = new ArrayList<DeptOrder>();
		DeptOrder deptOrder = cgglBO.queryByPiid(piid);
		deptOrderList.add(deptOrder);
		// 定义返回的数据格式
		Map<String, List<DeptOrder>> map = new HashMap<String, List<DeptOrder>>();
		map.put("Rows", deptOrderList);
		return map;
	}
	
	/**
	 * @Description 详情页面订单详情查询
	 * @author http://www.bwbroad.com 吴强
	 * @param id id
	 * @return
	 */
	@RequestMapping(value = "/queryDeptOrderDetail")
	@ResponseBody
	public Map<String, List<DeptOrderDetail>> queryDeptOrderDetail(String id) {
		// 查询当前配送单信息
		List<DeptOrderDetail> detailList = new ArrayList<DeptOrderDetail>();
		detailList = cgglBO.queryDetailById(id);
		// 定义返回的数据格式
		Map<String, List<DeptOrderDetail>> map = new HashMap<String, List<DeptOrderDetail>>();
		map.put("Rows", detailList);
		return map;
	}
	
	/**
	 * @Description 采购计划编辑查询
	 * @author http://www.bwbroad.com 吴强
	 * @param id 采购计划ID
	 * @return
	 */
	@RequestMapping(value = "/queryDeptOrderById")
	@ResponseBody
	public Map<String, List<DeptOrder>> queryDeptOrderByID(String id) {
		// 查询当前采购计划信息
		List<DeptOrder> orderList = new ArrayList<DeptOrder>();
		orderList = cgglBO.queryDeptOrderById(id);
		// 定义返回的数据格式
		Map<String, List<DeptOrder>> map = new HashMap<String, List<DeptOrder>>();
		map.put("Rows", orderList);
		return map;
	}
	
	/**
	 * @Description 删除
	 * @author  http://www.bwbroad.com   吴强
	 * @param id
	 */
	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	@ResponseBody
    public void delete( @RequestParam("id") String[] id) {
		cgglBO.deleteByIds(id);
	}
}
