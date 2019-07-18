window.LandaJS = window.LandaJS || {
 
 
  ready: function (cb) {
    var _this = this;
 
    var checkIfLoaded = function () {
      if (typeof(window.__LandaJS) == 'object') {
        cb && cb.call();
      } else {
        if (!_this.clent) {
          _this.clent = _this._getQueryString('client');
 
        }
        if (_this.clent) {
          if (!_this.accessToken && !_this.corpId && !_this.userInfo && !_this.corpInfo) {
            window.apiready = function () {
 
              _this.accessToken = api.pageParam.accessToken;
              _this.corpId = api.pageParam.corpId;
              _this.userInfo = api.pageParam.userInfo;
              _this.corpInfo = api.pageParam.corpInfo;
              cb && cb.call();
 
            }
 
          } else {
            cb && cb.call();
 
          }
 
 
        } else {
          setTimeout(function () {
            checkIfLoaded();
          }, 200);
        }
 
      }
    }
    // 引入水印组件
    var k2 = window.document.createElement('script');
    k2.src = 'https://landa-1251886366.cos.ap-shanghai.myqcloud.com/waterMark.js'; // 此处更换云存储地址
    window.document.body.insertBefore(k2, null);
 
    var arr = window.location.search.slice(1).split('=');
    var index = arr.indexOf('token');
    if (arr.indexOf('token') > -1) { // 来自pc的web端
      window.localStorage.setItem('lan_token', token); // 将token存入localStorage
      window.onunload = function () {                 // 卸载时清空lan_token
        window.localStorage.removeItem('lan_token');
      };
 
      var k = window.document.createElement('script');
      k.src = 'https://landa-1251886366.cos.ap-shanghai.myqcloud.com/landa-debug.js';
      window.document.body.insertBefore(k, null);
 
      checkIfLoaded();
    } else {
      checkIfLoaded();
    }
 
  },
  getUserInfo: function () {
    if (this.clent) {
      return this.userInfo;
    }
 
    return window.__LandaJS.getUserInfo();
  },
 
  _getQueryString: function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
  },
 
  getAccessToken: function () {
    if (this.clent) {
      return this.accessToken
    }
    return window.__LandaJS.getAccessToken();
  },
 
  getCorpInfo: function () {
    if (this.clent) {
      return this.corpInfo;
    }
    return window.__LandaJS.getCorpInfo();
  },
 
  getDeviceInfo: function () {
 
    return window.__LandaJS.getDeviceInfo();
  },
 
  uuid: function () {
    var CHARS = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var chars = CHARS, uuid = new Array(36), rnd = 0, r;
    for (var i = 0; i < 36; i++) {
      if (i == 8 || i == 13 || i == 18 || i == 23) {
        uuid[i] = '-';
      } else if (i == 14) {
        uuid[i] = '4';
      } else {
        if (rnd <= 0x02) rnd = 0x2000000 + (Math.random() * 0x1000000) | 0;
        r = rnd & 0xf;
        rnd = rnd >> 4;
        uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
      }
    }
    return uuid.join('');
  },
 
  _timer: null,
  _listeners: [],
 
  _startListen: function () {
    var _this = this;
 
    if (!_this._timer) {
      _this._timer = setInterval(function () {
        try {
          for (var i = 0; i < _this._listeners.length; i++) {
            for (var k = 0; k < __LandaJS.result.length; k++) {
              if (_this._listeners[i].id == __LandaJS.result[k].id) {
                var cb = _this._listeners[i].cb, data = __LandaJS.result[k].data;
 
                _this._listeners.splice(i, 1);
                __LandaJS.result.splice(k, 1);
 
                cb.call(_this, data);
 
                return;
              }
            }
          }
        } catch (err) {
          alert(err);
        }
      }, 200);
    }
  },
 
  _pushRequest: function (req, cb, param) {
    var id = this.uuid(), data = {id: id, request: req};
 
    if (param) {
      for (var k in param) {
        if (k !== 'id' && k !== 'request') {
          data[k] = param[k];
        }
      }
    }
 
    window.postMessage(JSON.stringify(data));
    this._listeners.push({id: id, cb: cb});
 
    this._startListen();
  },
 
 
  requestCamera: function (cb, param) {
    var _this = this;
    if (this.clent) {
      _this._passMessage('camera', param)
      _this._passBackMessage(_this, cb)
    }
    else {
      this._pushRequest('camera', function (data) {
        if (!window._WaterMark) {
          throw Error('加载水印组件失败')
        } else {
          window._WaterMark.takeWatermark(data, function (afterPhoto) {
            cb.call(_this, {data: afterPhoto.slice(22)});
          })
        }
      }, param);
    }
 
  },
 
 
  requestImagePicker: function (cb, param) {  // param => {pickType: 0|1|2 } 0 => 打开选择框 1=> 打开相册 2=> 打开相机
    var _this = this;
 
    if (this.clent) {
      _this._passMessage('pickImage', param)
      _this._passBackMessage(_this, cb)
    } else {
      this._pushRequest('pickImage', function (data) {
        cb.call(_this, data);
      }, param);
    }
 
  },
  /**
   * 向app发送消息
   */
  _passMessage: function (type, param) {
 
    api.sendEvent({
      name: 'request',
      extra: {
        type: type,
        param: param,
 
      }
    });
  }
  ,
  /**
   *获取app回传的信息
   * @param _this
   * @param cb
   * @private
   */
  _passBackMessage: function (_this, cb) {
    api.addEventListener({
      name: 'requestBack'
    }, function (ret, err) {
      if (ret) {
        var type = ret.value.type;
        if (type == 'pickImage') {
          var data = {data: ret.value.data, didCancel: ret.value.didCancel}
          cb.call(_this, data)
        } else if (type == 'camera') {
 
 
          if (!window._WaterMark) {
            throw Error('加载水印组件失败')
          } else {
            try {
              window._WaterMark.takeWatermark(ret.value.data, function (afterPhoto) {
                cb.call(_this, {data: afterPhoto.slice(22)});
              })
            } catch (e) {
            }
          }
        } else if (type == 'geo') {
          var data = ret.value.data
          if (!data.address) { // 没有具体位置信息
            if (AMap && AMap.Geocoder) { // 如果存在逆编码组件
              // 生成全局唯一实例
              if (!window.__geocoder) {
                window.__geocoder = new AMap.Geocoder({
                  radius: 50
                });
              }
              // 获取具体位置消息
              window.__geocoder.getAddress([data.longitude, data.latitude], function (status, result) {
                if (status === 'complete' && result.info === 'OK') {
                  // 将得到的位置信息赋给data
                  data.address = result.regeocode.formattedAddress;
                  data.poiName = result.regeocode.addressComponent.building;
                  data.province = result.regeocode.addressComponent.province;
                  data.city = result.regeocode.addressComponent.city;
                  data.cityCode = result.regeocode.addressComponent.cityCode;
                  data.street = result.regeocode.addressComponent.street;
                  data.streetNumber = result.regeocode.addressComponent.streetNumber;
                  data.adCode = result.regeocode.addressComponent.adcode;
                  // 将data传出回调
                  cb.call(_this, data);
                } else {
                  throw Error('获取详细信息失败', result.info)
                }
              })
 
            } else { // 没有你编码组件提示开发者添加
              throw Error('缺少高德逆编码组件---请引入AMap.Geocoder')
            }
          } else { // 已有位置信息
            cb.call(_this, data);
          }
        }
      }
 
 
    })
 
  },
 
 
  requestGeo: function (cb) {
    var _this = this;
    if (this.clent) {
      _this._passMessage('geo', '')
      _this._passBackMessage(_this, cb)
 
 
    } else {
 
      this._pushRequest('geo', function (data) {
        if (!data.address) { // 没有具体位置信息
          if (AMap && AMap.Geocoder) { // 如果存在逆编码组件
            // 生成全局唯一实例
            if (!window.__geocoder) {
              window.__geocoder = new AMap.Geocoder({
                radius: 50
              });
            }
            // 获取具体位置消息
            window.__geocoder.getAddress([data.longitude, data.latitude], function (status, result) {
              if (status === 'complete' && result.info === 'OK') {
                // 将得到的位置信息赋给data
                data.address = result.regeocode.formattedAddress;
                data.poiName = result.regeocode.addressComponent.building;
                data.province = result.regeocode.addressComponent.province;
                data.city = result.regeocode.addressComponent.city;
                data.cityCode = result.regeocode.addressComponent.cityCode;
                data.street = result.regeocode.addressComponent.street;
                data.streetNumber = result.regeocode.addressComponent.streetNumber;
                data.adCode = result.regeocode.addressComponent.adcode;
                // 将data传出回调
                cb.call(_this, data);
              } else {
                throw Error('获取详细信息失败', result.info)
              }
            })
 
          } else { // 没有你编码组件提示开发者添加
            throw Error('缺少高德逆编码组件---请引入AMap.Geocoder')
          }
        } else { // 已有位置信息
          cb.call(_this, data);
        }
      });
    }
 
  },
 
 
  requestAppBack: function (errCallback) {
    if (this.clent) {
      //在名为winName的window中执行jsfun脚本
      var jsback = 'requestAppBack();';
      api.execScript({
        name: 'mappboxscreen',
        script: jsback
      });
 
      return;
    }
    this._pushRequest('appback', errCallback);
  }
};