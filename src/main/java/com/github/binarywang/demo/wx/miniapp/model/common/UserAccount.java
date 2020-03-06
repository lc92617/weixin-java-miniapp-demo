package com.github.binarywang.demo.wx.miniapp.model.common;

import lombok.Data;

@Data
public class UserAccount {

    private String id;

    private String password;

    private String wxOpenId;

    private String sessionKey;
}
