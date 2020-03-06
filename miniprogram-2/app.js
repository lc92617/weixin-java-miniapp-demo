//app.js
App({
  globalData: {
    userInfo: null,
    appId: 'wx45c426052a6f559a',
    baseUrl: 'http://localhost:10086'
    // baseUrl: 'https://www.taijiazhenren.club/wx'
  },
  onLaunch: function() {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        wx.request({
          url: this.globalData.baseUrl + '/user/login',
          method: 'POST',
          header: {
            'content-type': 'application/json', // 默认值
            'appid': this.globalData.appId
          },
          data: {
            'code': res.code
          },
          success: function(res) {
            console.log(res)
            wx.setStorageSync("sessionId", res)
          }
        })
      }
    })
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo
              console.log(res)
              wx.request({
                url: this.globalData.baseUrl + '/user/info',
                method: 'POST',
                header: {
                  'content-type': 'application/json', // 默认值
                  'appid': this.globalData.appId
                },
                data: {
                  'content-type': 'application/json', // 默认值
                  'sessionKey': wx.getStorageSync("sessionId"),
                  'signature': res.signature,
                  'rawData': res.rawData,
                  'encryptedData': res.encryptedData,
                  'iv': res.iv
                },
                success: function(res) {
                  console.log(res) //打印到控制台
                }
              })

              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        }
      }
    })
  }
})