package com.jisucloud.clawler.regagent.service.impl.borrow;

import com.jisucloud.clawler.regagent.i.PapaSpider;
import com.jisucloud.clawler.regagent.i.UsePapaSpider;
import com.jisucloud.clawler.regagent.util.OCRDecode;

import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import com.deep077.spiderbase.selenium.mitm.AjaxHook;
import com.deep077.spiderbase.selenium.mitm.ChromeAjaxHookDriver;
import com.deep077.spiderbase.selenium.mitm.HookTracker;
import com.google.common.collect.Sets;
import org.openqa.selenium.WebElement;

import java.util.Map;
import java.util.Set;

@Slf4j
@UsePapaSpider
public class PengJinSuoSpider extends PapaSpider {

	private ChromeAjaxHookDriver chromeDriver;
	private boolean checkTel = false;
	private boolean vcodeSuc = false;//验证码是否正确

	@Override
	public String message() {
		return "鹏金所,全称深圳市鹏金所互联网金融服务有限公司,是万科领衔多家上市公司联袂打造的互联网金融平台。鹏金所是深圳市鹏鼎创盈金融信息服务股份有限公司。";
	}

	@Override
	public String platform() {
		return "penging";
	}

	@Override
	public String home() {
		return "penging.com";
	}

	@Override
	public String platformName() {
		return "鹏金所";
	}

	@Override
	public String[] tags() {
		return new String[] {"P2P", "借贷"};
	}
	
	@Override
	public Set<String> getTestTelephones() {
		return Sets.newHashSet("15900068904", "18210538513");
	}

	private String getImgCode() {
		for (int i = 0 ; i < 3; i++) {
			try {
				WebElement img = chromeDriver.findElementByCssSelector("#imgObj");
				chromeDriver.mouseClick(img);
				byte[] body = chromeDriver.screenshot(img);
				return OCRDecode.decodeImageCode(body);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	String code;

	@Override
	public boolean checkTelephone(String account) {
		HookTracker hookTracker = HookTracker.builder()
				.addUrl("login/phoneCheckFindPwd")
				.isPost().build();
		try {
			chromeDriver = ChromeAjaxHookDriver.newChromeInstance(false, true);
			chromeDriver.get("http://www.penging.com/findPwd.do");
			chromeDriver.addAjaxHook(new AjaxHook() {
				
				@Override
				public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
					if (!contents.getTextContents().contains("验证码错误")) {
						vcodeSuc = true;
						checkTel = contents.getTextContents().equals("true");
					}
				}
				
				@Override
				public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
					if (contents.getTextContents().contains("SMSSendType")) {//要发短信了
						HttpResponse httpResponse = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.NO_CONTENT);
						//System.out.println("打断发短信");
						return httpResponse;
					}
					return null;
				}

				@Override
				public HookTracker getHookTracker() {
					return hookTracker;
				}
			});
			chromeDriver.findElementById("MB_PHN").sendKeys(account);
			for (int i = 0; i < 5; i++) {
				code = getImgCode();
				WebElement validate = chromeDriver.findElementById("CFY_NO");
				validate.clear();
				validate.sendKeys(code);
				chromeDriver.mouseClick(chromeDriver.findElementById("showTxt"));smartSleep(3000);
				if (vcodeSuc) {
					break;
				}
				if (chromeDriver.checkElement("a[class='xubox_yes xubox_botton1']")) {
					System.out.println("点击确定");
					chromeDriver.mouseClick(chromeDriver.findElementByCssSelector("a[class='xubox_yes xubox_botton1']"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (chromeDriver != null) {
				chromeDriver.quit();
			}
		}
		return checkTel;
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