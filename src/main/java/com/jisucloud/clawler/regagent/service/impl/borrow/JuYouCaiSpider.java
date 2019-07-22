package com.jisucloud.clawler.regagent.service.impl.borrow;

import com.jisucloud.clawler.regagent.i.PapaSpider;
import com.jisucloud.clawler.regagent.i.UsePapaSpider;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@UsePapaSpider
public class JuYouCaiSpider extends PapaSpider {

	private OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();

	@Override
	public String message() {
		return "聚优财是浙江聚有财金融服务外包有限公司旗下运营的理财APP，原为“聚有财”升级；旨在宣传优生活，优选择；公司致力于成为一个针对中国中产阶级的专业互联网资产管理平台.关注互联网金融，投资理财，资产配置方面，坚持设计清晰、透明的理财产品；关注用户个性化、定制化需求。";
	}

	@Override
	public String platform() {
		return "jyc99";
	}

	@Override
	public String home() {
		return "jyc99.com";
	}

	@Override
	public String platformName() {
		return "聚优财";
	}

	@Override
	public String[] tags() {
		return new String[] {"P2P", "消费分期" , "借贷"};
	}
	
	@Override
	public Set<String> getTestTelephones() {
		return Sets.newHashSet("13910002045", "18210538513");
	}

	@Override
	public boolean checkTelephone(String account) {
		try {
			String url = "https://www.jyc99.com/api/user/loginreg/dologin";
			FormBody formBody = new FormBody
	                .Builder()
	                .add("userPassword", "casda0sdjj1231")
	                .add("userMobile", account)
	                .add("type", "1")
	                .add("terminalType", "1")
	                .add("macAddress", UUID.randomUUID().toString())
	                .build();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
					.addHeader("Host", "www.jyc99.com")
					.addHeader("contentType", "application/json; charset=utf-8")
					.addHeader("X-Requested-With", "XMLHttpRequest")
					.addHeader("Referer", "https://www.jyc99.com/userreg/login?returnUrl=/product")
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String res = response.body().string();
			if (res.contains("帐号或密码") || res.contains("帐号已锁定")) {
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