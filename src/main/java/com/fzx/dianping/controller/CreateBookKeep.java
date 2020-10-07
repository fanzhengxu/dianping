package com.fzx.dianping.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 新增账单.
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 22:53 2020/4/13
 */
@Controller("/createBookKeep")
@RequestMapping("/createBookKeep")
public class CreateBookKeep {

    @RequestMapping("/create")
    @ResponseBody
    public JSONObject test(HttpServletRequest request) {
        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        System.out.println(jsonObject.getString("payType"));
        System.out.println(jsonObject.getString("purpose"));
        String amountStr = jsonObject.getString("amount");
        Double amount;
        jsonObject = new JSONObject();
        try {
            amount = Double.valueOf(amountStr);
        } catch (Exception e) {
            jsonObject.put("status", 500);
            jsonObject.put("msg", "输入的金额\"" + amountStr +"\"不为数字");
            return jsonObject;
        }
        System.out.println(jsonObject.getString("msg"));
        jsonObject.put("status", 200);
        jsonObject.put("msg", "新增成功");
        return jsonObject;
    }
}
