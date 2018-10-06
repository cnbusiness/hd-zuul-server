package com.hd.cloud.zuul.filter;

import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.hd.cloud.service.AuthService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreRequestLogFilter extends ZuulFilter {

	@Inject
	private AuthService authService;

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String requestUri = request.getRequestURI();
		log.info("zuul server##########requestUri:{}", requestUri);
		boolean bool = true;
		// 对于不需要token校验的 不执行
		if (requestUri.indexOf("nofilter") >= 0 || requestUri.indexOf("swagger") >= 0 || requestUri.indexOf("auth") >= 0
				|| requestUri.indexOf("logout") >= 0 || requestUri.indexOf("sso") >= 0 || requestUri.indexOf("forget") >= 0 
				|| requestUri.indexOf("wxpay") >= 0 || requestUri.indexOf("pos") >= 0
				|| requestUri.indexOf("hd-cloud-hospital") >= 0 || requestUri.indexOf("hd-cloud-thirdparty") >= 0) {
			bool = false;
		}
		return bool;
	}

	@Override
	public Object run() {
		log.info("zuul server##########run=====");
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		// 进行token校验
		String id = request.getHeader("userId");
		String token = request.getHeader("token");
		log.info("zuul server##########id:{},token:{}", id, token);
		Pattern pattern = Pattern.compile("^-?(?:\\d+|\\d{1,3}(?:,\\d{3})+)(?:\\.\\d+)?$");
		if (id == null || token == null) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			return null;
		} else {
			boolean userBool = pattern.matcher(id).matches();
			if (userBool) {
				long userId = Long.parseLong(id);
				boolean result = authService.isValidTokenPare(userId, 1, token);
				log.info("zuul server##########run:{}=====",result);
				if (!result) {
					// 验证不通过 返回401
					ctx.setSendZuulResponse(false);
					ctx.setResponseStatusCode(401);
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
