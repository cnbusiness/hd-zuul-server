package com.hd.cloud.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hd.cloud.bo.Auth;
import com.hd.cloud.service.AuthService;
import com.hd.cloud.util.ConstantUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
  * @ClassName: AuthServiceImpl
  * @Description: token验证
  * @author ShengHao shenghaohao@hadoop-tech.com
  * @Company hadoop-tech 
  * @date 2017年12月29日 下午2:10:38
  *
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	/**
	 * 
	 * @Title: isValidTokenPare
	 * @param:
	 * @Description: 传入userId与token，进行token有效性验证
	 */
	@Override
	public boolean isValidTokenPare(long userId, int type, String token) {
		String redisKey = ConstantUtil.USER_TOKEN_REDIS;
		final Auth auth = read(redisKey, userId);
		log.debug("auth :{}", auth);
		boolean isValid = false;
		if (auth == null || token == null || !token.equals(auth.getToken())) {
			log.info("UserId = {} token validation failure!", userId);
			return isValid;
		}
		// 当token验证成功是，auth对象的过期时间刷新
		refreshAuthExpire(redisKey, userId);
		log.info("UserId = {} token validation success!", userId);
		return true;
	}

	private Auth read(String redisKey, final long userId) {
		String key = redisKey + userId;
		Auth auth = (Auth) redisTemplate.opsForValue().get(key);
		return auth;
	}

	private void refreshAuthExpire(String key, long userId) {
		String redisKey = key + userId;
		redisTemplate.expire(redisKey, 24, TimeUnit.DAYS);
	}

}