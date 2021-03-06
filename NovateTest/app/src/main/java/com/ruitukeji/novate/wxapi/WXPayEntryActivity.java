/*
 * Copyright (C) 2015 tony<wuhaiyang1213@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ruitukeji.novate.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.common.cklibrary.common.BaseActivity;
import com.common.cklibrary.common.ViewInject;
import com.ruitukeji.novate.BuildConfig;
import com.ruitukeji.novate.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void setRootView(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, BuildConfig.WEIXIN_APPKEY);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d("pay", "errStr=" + resp.errStr);
        Log.d("pay", "errCode=" + resp.errCode);
        Log.d("pay", "getType=" + resp.getType());
        Log.d("pay", "toString=" + resp.toString());
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //支付成功
                    ViewInject.toast(getString(R.string.payment_succeed));
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    //支付错误
                    ViewInject.toast(getString(R.string.pay_error));
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    //取消支付
                    ViewInject.toast(getString(R.string.pay_cancel));
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    //认证被否决
                    ViewInject.toast(getString(R.string.authentication_rejected));
                    break;
                case BaseResp.ErrCode.ERR_SENT_FAILED:
                    //发送失败
                    ViewInject.toast(getString(R.string.send_failure));
                    break;
                case BaseResp.ErrCode.ERR_UNSUPPORT:
                    //不支持错误
                    ViewInject.toast(getString(R.string.unsupported_error));
                    break;
                default:
                    ViewInject.toast(getString(R.string.operation_error));
                    break;
            }
            finish();
        }
    }


}