package com.github.binarywang.demo.wx.miniapp.controller;

import com.github.binarywang.demo.wx.miniapp.model.common.UserAccount;
import com.github.binarywang.demo.wx.miniapp.model.req.WeChatLoginReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.github.binarywang.demo.wx.miniapp.config.WxMaConfiguration;
import com.github.binarywang.demo.wx.miniapp.utils.JsonUtils;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.Map;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https:f:wq:wq
 * :QZQ//github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class WxUserController extends BaseController{

    /**
     * 登陆接口
     */
    @PostMapping("/login")
    public String login(@RequestHeader String appid, @RequestBody WeChatLoginReq weChatLoginReq) {
        String code = weChatLoginReq.getCode();
        if (StringUtils.isBlank(code)) {
            return null;
        }

        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            log.info(session.getSessionKey());
            log.info(session.getOpenid());
            //TODO 可以增加自己的逻辑，关联业务相关数据
            UserAccount userAccount = new UserAccount();
            userAccount.setWxOpenId(session.getOpenid());
            userAccount.setSessionKey(session.getSessionKey());
            String token = getToken(userAccount);
//            userAccount.setPassword(weChatLoginReq.getPassword());
            return token;
//            return JsonUtils.toJson(session);
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @PostMapping("/info")
    public String info(@RequestHeader String appid, @RequestBody Map<String, String> body) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        String sessionKey = body.get("sessionKey");
        String signature = body.get("signature");
        String rawData = body.get("rawData");
        String encryptedData = body.get("encryptedData");
        String iv = body.get("iv");

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(userInfo);
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    public String phone(@PathVariable String appid, String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(phoneNoInfo);
    }

}
