package com.application.action.gysxt;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.application.bo.gysxt.IGysxxBO;
import com.application.model.Supplier;
import com.common.core.tag.ui.page.Pager;
import com.common.core.web.ApplicationController;
import com.common.utils.StringUtil;

@Controller
@RequestMapping("/gysxt/gysxx")
public class GysxxController extends ApplicationController {
	public static final String VIEW_PATH="/application/gysxt/gysxx";
	@Autowired
	private IGysxxBO gysxxBO;
	
	/**
	 * @Description 返回分页列表
	 * @author  http://www.bwbroad.com   吴强
	 * @param model   mvc model
	 * @param pager   分页参数信息
	 * @param params  其他参数
	 * @return        转向的页面
	 */
	@RequestMapping(value = "/list")
    public String list(Model model,Pager<Supplier> pager,@RequestParam Map<String, Object> params) {
		gysxxBO.queryByPage(pager,params);
		model.addAttribute("pager", pager);
		return VIEW_PATH+"/list";
	}
	
	/**
	 * @Description 弹出对话框显示信息
	 * @author http://www.bwbroad.com  吴强 
	 * @param model   mvc model
	 * @param pager   分页参数信息
	 * @param params  其他参数
	 * @return
	 */
	@RequestMapping(value = "/listForDialog")
    public String listForDialog(Model model,Pager<Supplier> pager,@RequestParam Map<String, Object> params) {
		pager.setPageSize(5);
		this.list(model,pager,params);
		return VIEW_PATH+"/listForDialog";
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
		if(StringUtil.isNotEmpty(id)){
			model.addAttribute("gysxx", gysxxBO.queryById(id));
		}
		model.addAttribute("type", type);
		return VIEW_PATH+"/insertAndUpdate";
	}
  

	/**
	 * @Description 插入和修改
	 * @author  http://www.bwbroad.com   吴强
	 * @param user
	 */
	@RequestMapping(value = "/insertAndUpdate" , method = RequestMethod.POST)
	@ResponseBody
    public void insertAndUpdate(Supplier gysxx) {
		gysxxBO.saveOrUpdate(gysxx);
	}
	
	/**
	 * @Description 删除
	 * @author  http://www.bwbroad.com   吴强
	 * @param id
	 */
	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	@ResponseBody
    public void delete( @RequestParam("id") String[] id) {
		gysxxBO.deleteByIds(id);
	}
}
