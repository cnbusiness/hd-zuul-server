package com.hd.cloud.bo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: Auth
 * @Description: 认证
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年12月29日 下午2:10:09
 *
 */
@SuppressWarnings("deprecation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auth implements Serializable {
	/**
	 * id主键
	 */
	private long id;

	/**
	 * 用户id
	 */
	private long userId;

	/**
	 * token令牌
	 */
	private String token;

	/**
	 * token的生成时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date authTime;
}
