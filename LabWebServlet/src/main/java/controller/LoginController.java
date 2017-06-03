package controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import model.CustomerBean;
import model.CustomerService;

@Controller
@RequestMapping("/secure/login.controller")
@SessionAttributes(names={"user"})
public class LoginController {
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(
			method={RequestMethod.GET, RequestMethod.POST}
	)
	public String method(String username, String password, Model model) {
		System.out.println("LoginController.method()");
//接收資料
//驗證資料
//轉換資料
		Map<String, String> errors = new HashMap<String, String>();
		model.addAttribute("errors", errors);
		
		if(username==null || username.length()==0) {
			errors.put("xxx", "請輸入帳號");
		}
		if(password==null || password.length()==0) {
			errors.put("ooo", "請輸入密碼");
		}
		
		if(errors!=null && !errors.isEmpty()) {
			return "login.error";
		}
//呼叫Model
		CustomerBean bean = customerService.login(username, password);

//根據Model執行結果呼叫View
		if(bean==null) {
			errors.put("ooo", "登入失敗，請再次輸入帳號密碼");
			return "login.error";
		} else {
			model.addAttribute("user", bean);
			return "login.success";
		}
	}
}
