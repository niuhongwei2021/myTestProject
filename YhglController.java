package com.application.action.jcsjgl;

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

import com.application.bo.jcsjgl.IJsglBO;
import com.application.bo.jcsjgl.IYhglBO;
import com.common.core.tag.ui.page.Pager;
import com.common.core.web.ApplicationController;
import com.common.security.model.Role;
import com.common.security.model.Users;
import com.common.utils.StringUtil;
/**
 * 
 * @Description 基础信息管理->用户管理
 * @author  北京八维博大科技   薛建新
 * @date 2013-5-12  下午6:45:57
 * @Copyright 首自信
 */
@Controller
@RequestMapping("/jcsjgl/yhgl")
public class YhglController extends ApplicationController {
	public static final String VIEW_PATH="/application/jcsjgl/yhgl";
	@Autowired
	private IYhglBO yhglBO;
	@Autowired
	private IJsglBO jsglBO;
	
	/**
	 * @Description 返回分页列表
	 * @author  http://www.bwbroad.com   xuejx
	 * @param model   mvc model
	 * @param pager   分页参数信息
	 * @param params  其他参数
	 * @return        转向的页面
	 */
	@RequestMapping(value = "/list")
    public String list(Model model,Pager<Users> pager,@RequestParam Map<String, Object> params) {
		yhglBO.queryByPage(pager,params);
		model.addAttribute("pager", pager);
		return VIEW_PATH+"/list";
	}
	
	@RequestMapping(value = "/listForDialog")
    public String listForDialog(Model model,Pager<Users> pager,@RequestParam Map<String, Object> params) {
		pager.setPageSize(5);
		this.list(model, pager, params);
		return VIEW_PATH+"/listForDialog";
	}
	
	
	/**
	 * @Description 插入和修改
	 * @author  http://www.bwbroad.com   xuejx
	 * @param user
	 */
	@RequestMapping(value = "/insertAndUpdate" , method = RequestMethod.POST)
	@ResponseBody
    public void insertAndUpdate(  Users user) {
		yhglBO.saveOrUpdate(user);
	}
	
	
	/**
	 * @Description 删除
	 * @author  http://www.bwbroad.com   xuejx
	 * @param id
	 */
	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	@ResponseBody
    public void delete( @RequestParam("id") String[] id) {
	   yhglBO.deleteByIds(id);
	}
	
	/**
	 * @Description 显示插入和修改页面
	 * @author  http://www.bwbroad.com   xuejx
	 * @param model
	 * @param id    id
	 * @param type  edit or add or view
	 * @return
	 */
	@RequestMapping(value = "/goInsertAndUpdate")
    public String goInsertAndUpdate(Model model,String id,String type) {
		if(StringUtil.isNotEmpty(id)){
			model.addAttribute("u", yhglBO.queryById(id));
		}
		model.addAttribute("type", type);
		return VIEW_PATH + "/insertAndUpdate";
	}
	
	/**
	 * @Description 显示插入和修改页面
	 * @author  http://www.bwbroad.com   吴强
	 * @param model
	 * @param id    id
	 * @return
	 */
	@RequestMapping(value = "/goUpdateRoles")
    public String goUpdateRoles(Model model,String id) {
		Map<Long, Role> oldRoleMap = new HashMap<Long, Role>();
		List<Role> allRoleList = new ArrayList<Role>();
		if(StringUtil.isNotEmpty(id)){
			//根据ID查询已分配的角色信息
			oldRoleMap = jsglBO.queryMapRoleById(id);
			model.addAttribute("oldRoleMap", oldRoleMap);
			//查询所有的角色信息
			allRoleList = jsglBO.queryRoleAll();
			model.addAttribute("allRoleList", allRoleList);
		}
		model.addAttribute("userId", id);
		return VIEW_PATH+"/updateRoles";
	}
	
	/**
	 * @Description 显示插入和修改页面
	 * @author  http://www.bwbroad.com   吴强
	 * @param id    选中角色的所有ID
	 * @param userId  用户ID
	 * @return
	 */
	@RequestMapping(value = "/updateRoles")
	@ResponseBody
    public void updateRoles(String[] id,String userId) {
		//根据userI的，删除用户现有的所有角色
		yhglBO.delRolesByUserId(userId);
		//根据角色id，批量插入用户与角色的关联数据到表中，
		yhglBO.insertUserRoles(userId, id);
	}
	
	
   /**
	@RequestBody 将HTTP请求正文转换为适合的HttpMessageConverter对象。 mappingJacksonHttpMessageConverter
    @ResponseBody 将内容或对象作为 HTTP 响应正文返回，并调用适合HttpMessageConverter的Adapter转换对象，写入输出流。
    
	@RequestMapping(value = "/list0", method = RequestMethod.GET)
    @ResponseBody
    public Map<String ,Object> testPostJson() {
		Map<String ,Object> m=new HashMap<String ,Object>();
		m.put("a", "a");
		m.put("b","b");
		return m;
	}
	
	
	@RequestMapping(value = "/list1")
    public String list1(HttpServletRequest request) {
		Map<String ,Object> m=new HashMap<String ,Object>();
		m.put("a", "a");
		m.put("b","b");
		request.setAttribute("m", m);
		return "/test";
	}
	
	
     
    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    protected void exception(HttpServletResponse response,RuntimeException e) throws IOException {  
       response.setContentType("text/html; charset=utf-8");
       PrintWriter   w=response.getWriter();
       StringBuffer sb=new StringBuffer();
       sb.append("<html><head> ")
       .append(" <script type='text/javascript'>")
       .append( "alert('")
       .append(e.getMessage())
       .append("');")
       .append(" </script> </head><body></body></html>");
       w.write(sb.toString());
       w.flush();
    }  
    
	
	*/

}
