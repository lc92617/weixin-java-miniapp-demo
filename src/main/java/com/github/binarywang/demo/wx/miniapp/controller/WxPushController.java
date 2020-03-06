package com.github.binarywang.demo.wx.miniapp.controller;

import com.github.binarywang.demo.wx.miniapp.config.WxMaConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * Created by qcl on 2019-05-20
 * 微信：2501902696
 * desc: 微信小程序模版推送实现
 */
@RestController
@RequestMapping("/wx/push/{appid}")
public class WxPushController {

    @GetMapping("/push")
    public String push(@PathVariable String appid, @RequestParam String openid, @RequestParam String formid) {
        //1,配置小程序信息
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        //2,设置模版信息（keyword1：类型，keyword2：内容）
        List<WxMaTemplateData> templateDataList = new ArrayList<>(2);
        WxMaTemplateData data1 = new WxMaTemplateData("keyword1", "获取老师微信");
        WxMaTemplateData data2 = new WxMaTemplateData("keyword2", "2501902696");
        templateDataList.add(data1);
        templateDataList.add(data2);

        //3，设置推送消息
        WxMaTemplateMessage templateMessage = WxMaTemplateMessage.builder()
            .toUser(openid)//要推送的用户openid
            .formId(formid)//收集到的formid
            .templateId("eDZCu__qIz64Xx19dAoKg0Taf5AAoDmhUHprF6CAd4A")//推送的模版id（在小程序后台设置）
            .data(templateDataList)//模版信息
            .page("pages/index/index")//要跳转到小程序那个页面
            .build();
        //4，发起推送
        try {
            wxService.getMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            System.out.println("推送失败：" + e.getMessage());
            return e.getMessage();
        }
        return "推送成功";
    }
}
