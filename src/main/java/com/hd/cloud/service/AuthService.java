package com.hd.cloud.service;

/**
 * 
  * @ClassName: AuthService
  * @Description: token验证
  * @author ShengHao shenghaohao@hadoop-tech.com
  * @Company hadoop-tech 
  * @date 2017年12月29日 下午2:10:23
  *
 */
public interface AuthService {

	/**
	 * 
	 * @Title: isValidTokenPare
	 * @param:
	 * @Description: 验证UserId与token
	 * @return boolean
	 */
	boolean isValidTokenPare(long userId,int type, String token);

}