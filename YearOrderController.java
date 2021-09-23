package com.application.action.gysxt;

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

import com.application.bo.gysxt.IGysxxBO;
import com.application.bo.gysxt.YearOrderBO;
import com.application.bo.gysxt.YearOrderWuLiaoRelationBO;
import com.application.model.DeptOrder;
import com.application.model.DeptOrderDetail;
import com.application.model.InventoryInfo;
import com.application.model.Supplier;
import com.application.model.TMcode;
import com.application.model.YearOrder;
import com.application.model.YearOrderWuLiaoRelation;
import com.common.core.tag.ui.page.Pager;
import com.common.core.web.ApplicationController;
import com.common.utils.StringUtil;

/**
 * @author 刘潇
 * @date 2013-7-3
 * @Copyrigth 首自信
 */
@Controller
@RequestMapping("/gysxt/yearOrder")
public class YearOrderController extends ApplicationController {
	public static final String VIEW_PATH = "/application/gysxt/yearOrder";
	@Autowired
	private YearOrderBO yearOrderBO;
	@Autowired
	private IGysxxBO iGysxxBo;
	@Autowired
	private YearOrderWuLiaoRelationBO yearOrderWuLiaoRelationBO;

	/**
	 * @Description 返回分页列表
	 * @author 刘潇
	 * @param model
	 *            mvc model
	 * @param pager
	 *            分页参数信息
	 * @param params
	 *            其他参数
	 * @return 转向的页面
	 */
	@RequestMapping(value = "/list")
	public String list(Model model, Pager<YearOrder> pager,
			@RequestParam Map<String, Object> params) {
		yearOrderBO.queryByPage(pager, params);
		model.addAttribute("pager", pager);

		return VIEW_PATH + "/list";
	}


	@RequestMapping(value = "/materialList")
	@ResponseBody
	public Map<String, Object> materialList(Model model,
			Pager<YearOrderWuLiaoRelation> pager,
			@RequestParam Map<String, Object> params,String orderId,HttpServletRequest request) {
		//获取ligerUI设置的页面信息
		params.put("orderId",orderId);
		int page=Integer.parseInt(request.getParameter("page"));
		int pagesize=Integer.parseInt(request.getParameter("pagesize"));
		//设置pager参数
		pager.setCurrentPage(page);
		pager.setPageSize(pagesize);
		// 查询物料信息
		List<YearOrderWuLiaoRelation> mCodeList = yearOrderWuLiaoRelationBO.orderQueryByPage(pager, params);
		// 定义返回的数据格式
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Rows", mCodeList);
		map.put("total", pager.getCount());//返回总页数给ligerUI
		return map;
	}
	/**
	 * @Description 插入和修改
	 * @author 刘潇
	 * @param user
	 */
	@RequestMapping(value = "/insertAndUpdate", method = RequestMethod.POST)
	public void insertAndUpdate(YearOrder yearOrder) {
		Supplier supplier=iGysxxBo.queryByUserId(getUserInfo().getUsers().getId().toString());
		yearOrder.setGongysId(Integer.parseInt(supplier.getSupplierCode()));
		yearOrder.setGongysName(supplier.getSuppilerName());
		yearOrderBO.saveOrUpdate(yearOrder);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public void save(Model model, @RequestParam Map<String, Object> params) {
		String jsobject = (String) params.get("yearOrder");// 这个是jsp页面传过来的对象字符串 
		JSONObject jsObj = JSONObject.fromObject(jsobject);// 根据字符串转换对象 
		Map<String,Object> classMap = new HashMap<String, Object>();
		classMap.put("yearOrderWuLiaoRelations", YearOrderWuLiaoRelation.class);
		YearOrder yearOrder = (YearOrder) JSONObject.toBean(jsObj, YearOrder.class, classMap) ;// 把值绑定成相应的值对象 
		yearOrderBO.saveOrUpdate(yearOrder);
		long orderId=yearOrderBO.getOrderId();
		for(YearOrderWuLiaoRelation details:yearOrder.getYearOrderWuLiaoRelations()){
			details.setOrderId(orderId);
			yearOrderWuLiaoRelationBO.saveOrUpdate(details);
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public void update(Model model, @RequestParam Map<String, Object> params) {
		String jsobject = (String) params.get("yearOrder");// 这个是jsp页面传过来的对象字符串 
		JSONObject jsObj = JSONObject.fromObject(jsobject);// 根据字符串转换对象 
		Map<String,Object> classMap = new HashMap<String, Object>();
		classMap.put("yearOrderWuLiaoRelations", YearOrderWuLiaoRelation.class);
		YearOrder yearOrder = (YearOrder) JSONObject.toBean(jsObj, YearOrder.class, classMap) ;// 把值绑定成相应的值对象 
		yearOrderBO.saveOrUpdate(yearOrder);
		long orderId=yearOrderBO.getOrderId();
		for(YearOrderWuLiaoRelation details:yearOrder.getYearOrderWuLiaoRelations()){
			details.setOrderId(orderId);
			yearOrderWuLiaoRelationBO.saveOrUpdate(details);
		}
	}

	
	
	@RequestMapping(value = "/materInsertAndUpdate", method = RequestMethod.POST)
	public void materInsertAndUpdate(YearOrderWuLiaoRelation wuLiaoRelation) {
		yearOrderWuLiaoRelationBO.saveOrUpdate(wuLiaoRelation);
	}
	/**
	 * @Description 删除
	 * @author 刘潇
	 * @param id
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public void delete(@RequestParam("id") String[] id) {
		yearOrderBO.deleteByIds(id);
		yearOrderWuLiaoRelationBO.deleteByOrderId(id);
	}

	/**
	 * @Description 显示插入和修改页面
	 * @author 刘潇
	 * @param model
	 * @param id
	 *            id
	 * @param type
	 *            edit or add or view
	 * @return
	 */
	//主表操作跳转
	@RequestMapping(value = "/goInsertAndUpdate")
	public String goInsertAndUpdate(Model model, String id, String type) {
		Supplier supplier=iGysxxBo.queryByUserId(getUserInfo().getUsers().getId().toString());
	    model.addAttribute("supplierCode",supplier.getSupplierCode());
	    model.addAttribute("supplierName",supplier.getSuppilerName());
		if (StringUtil.isNotEmpty(id)) {
			model.addAttribute("yearOrder", yearOrderBO.queryById(id));
		}
		model.addAttribute("type", type);
		if(type.equals("add")){
			return VIEW_PATH + "/add";
		}
		return VIEW_PATH + "/insertAndUpdate";
	}
    //物料关系子表跳转
	@RequestMapping(value = "/goMaterialInsertAndUpdate")
	public String goMaterialInsertAndUpdate(Model model, String id, String type,String orderId) { 
		if (StringUtil.isNotEmpty(id)) {
			model.addAttribute("material", yearOrderWuLiaoRelationBO.queryById(id));
		}
		model.addAttribute("type", type);
		model.addAttribute("orderId",orderId);
		return VIEW_PATH + "/materialInsertAndUpdate";
	}
	
	@RequestMapping(value = "/listForDialog")
	public String listForDialog(Model model, Pager<YearOrder> pager,
			@RequestParam Map<String, Object> params) {
		pager.setPageSize(5);
		yearOrderBO.queryByPage(pager, params);
		model.addAttribute("pager", pager);
		return VIEW_PATH + "/listForDialog";
	}
}
