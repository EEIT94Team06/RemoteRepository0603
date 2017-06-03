package controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import model.ProductBean;
import model.ProductService;
import model.spring.PrimitiveNumberEditor;

@Controller
@RequestMapping("/pages/product.controller")
public class ProductController {
	@InitBinder
	public void init(WebDataBinder webDataBinder) {
		CustomDateEditor customerDateEditor = new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true);
		webDataBinder.registerCustomEditor(java.util.Date.class, customerDateEditor);

		PrimitiveNumberEditor primitiveNumberEditor = new PrimitiveNumberEditor(Integer.class, true);
		webDataBinder.registerCustomEditor(int.class, primitiveNumberEditor);
		webDataBinder.registerCustomEditor(
				double.class, "price", new PrimitiveNumberEditor(Double.class, true));
	}
	@Autowired
	private ProductService productService;
	@RequestMapping(
			method={RequestMethod.GET, RequestMethod.POST}
	)
	public String method(String prodaction,	Model model,
			ProductBean bean, BindingResult bindingResult) {
//接收資料
		Map<String, String> errors = new HashMap<String, String>();
		model.addAttribute("errors", errors);
				
//轉換資料
		if(bindingResult!=null && bindingResult.hasErrors()) {
    		if(bindingResult.getFieldError("id")!=null) {
    			errors.put("id", "Id欄位請輸入整數 (FBB)");
    		}
    		if(bindingResult.getFieldError("price")!=null) {
    			errors.put("price", "Price欄位請輸入整數 (FBB)");
    		}
    		if(bindingResult.getFieldError("make")!=null) {
    			errors.put("make", "Make必須是擁有YYYY-MM-DD格式的日期 (FBB)");
    		}
    		if(bindingResult.getFieldError("expire")!=null) {
    			errors.put("expire", "Expire欄位請輸入整數 (FBB)");
    		}
		}
		
//驗證資料		
		if("Insert".equals(prodaction) || "Update".equals(prodaction) || "Delete".equals(prodaction)) {
			if(bean!=null && bean.getId()==0) {
				errors.put("id", "請輸入Id以便執行"+prodaction);
			}
		}

		if(errors!=null && !errors.isEmpty()) {
			return "product.error";
		}
		
//呼叫Model, 根據Model執行結果呼叫View
		if("Select".equals(prodaction)) {
			List<ProductBean> result = productService.select(bean);
			model.addAttribute("select", result);
			return "product.select";
		} else if("Insert".equals(prodaction)) {
			ProductBean result = productService.insert(bean);
			if(result==null) {
				errors.put("action", "Insert失敗");
			} else {
				model.addAttribute("insert", result);
			}
			return "product.error";
		} else if("Update".equals(prodaction)) {
			ProductBean result = productService.update(bean);
			if(result==null) {
				errors.put("action", "Update失敗");
			} else {
				model.addAttribute("update", result);
			}
			return "product.error";
		} else if("Delete".equals(prodaction)) {
			boolean result = productService.delete(bean);
			if(result) {
				model.addAttribute("delete", 1);
			} else {
				model.addAttribute("delete", 0);
			}
			return "product.error";
		} else {
			errors.put("action", "Unknown Action:"+prodaction);
			return "product.error";
		}
	}
}
