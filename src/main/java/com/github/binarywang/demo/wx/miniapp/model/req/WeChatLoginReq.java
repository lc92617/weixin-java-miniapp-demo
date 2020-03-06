package com.github.binarywang.demo.wx.miniapp.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WeChatLoginReq {

    @NotEmpty
    String code;

}
