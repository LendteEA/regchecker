package com.jisucloud.clawler.regagent.service.impl.health;


import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;

import java.util.HashMap;
import java.util.Map;



@Slf4j
@PapaSpiderConfig(
		home = "haodf.com", 
		message = "好大夫在线-智慧互联网医院， 汇集全国15万+优质医疗权威专家，为患者提供网上看病、挂专家号，在线开药，线上买药，线上复诊，网络预约手术等全方位服务；患者通过智慧互联网医院获得全国优质医疗专家的权威诊治，小病大病网上就诊，看病不再难。", 
		platform = "haodf", 
		platformName = "好大夫在线", 
		tags = { "健康运动", "医疗", "生活应用" , "挂号" , "用药" }, 
		testTelephones = { "13877117175", "18212345678" })
public class HaoDaiFuSpider extends PapaSpider {

	

	public boolean checkTelephone(String account) {
		try {
			String url = "https://passport.haodf.com/user/ajaxuseridcnt4name?username=" + account + "&type=NORMAL&_=" + System.currentTimeMillis();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
					.addHeader("Host", "passport.haodf.com")
					.addHeader("Referer", "https://passport.haodf.com/user/showlogin?fromType=&forward=https%3A%2F%2Fwww.haodf.com%2F")
					.build();
			Response response = okHttpClient.newCall(request).execute();
			if (response.body().string().contains("1")) {
				return true;
			}
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
