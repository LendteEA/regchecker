package com.jisucloud.clawler.regagent.service.impl.photo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;



@Slf4j
@PapaSpiderConfig(
		home = "shijue.me", 
		message = "视觉中国是中国最具活力的视觉图片分享社区及创意设计产品社会化电商平台。依托独特的创意生态理论，为原创者和消费者提供一个互动沟通的原创社区，发现原创、发现美丽，收获并分享美好的创意体。", 
		platform = "shijue", 
		platformName = "视觉中国", 
		tags = { "原创" , "设计" }, 
		testTelephones = { "18515290000", "13811085745" })
public class ShiJueZhongGuoSpider extends PapaSpider {

	

	public boolean checkTelephone(String account) {
		try {
			String url = "http://www.shijue.me/user/v2/userIsExist";
			FormBody formBody = new FormBody
	                .Builder()
	                .add("userName", account)
	                .add("countryCode", "86")
	                .build();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
					.addHeader("Referer", "http://www.shijue.me/user/registerMe?redirect=http%3A%2F%2Fwww.shijue.me%2Fcommunity%2Findex.html")
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String res = response.body().string();
			JSONObject result = JSON.parseObject(res);
			return result.getBooleanValue("isExist");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean checkEmail(String account) {
		return false;
	}

	@Override
	public Map<String, String> getFields() {
		return null;
	}

}
