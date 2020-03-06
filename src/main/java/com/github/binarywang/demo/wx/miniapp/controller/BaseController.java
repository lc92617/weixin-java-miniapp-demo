package com.github.binarywang.demo.wx.miniapp.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.binarywang.demo.wx.miniapp.model.common.UserAccount;

import java.util.Date;

public abstract class BaseController {

    protected String getToken(UserAccount user) {
        String token = "";
        token = JWT.create()
            .withKeyId(user.getWxOpenId())
            .withIssuer("www.taijiazhenren.club")
            .withIssuedAt(new Date())
            .withClaim("session_key", user.getSessionKey())
            .withAudience(user.getWxOpenId())
            .sign(Algorithm.HMAC256(user.getSessionKey()));
        return token;
    }
}

