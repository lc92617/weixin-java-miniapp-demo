package com.github.binarywang.demo.wx.miniapp.model.req;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@Valid
public class WeChatLoginReq {

    @NotEmpty
    String code;

//    @NotEmpty
//    String password;

}
