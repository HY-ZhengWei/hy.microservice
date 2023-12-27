package org.hy.microservice.common.openapi;

import org.hy.microservice.common.BaseViewMode;





/**
 * 系统操作日志
 * 
 * @author      ZhengWei(HY)
 * @createDate  2023-12-27
 * @version     v1.0
 */
public class OpenApiConfig extends BaseViewMode 
{

	private static final long serialVersionUID = -1820383376053890059L;
	
	
	/** URL地址 */
	private String  url;
	
	/** 每分钟最大允许的请求量。等于0 表示不限制访问量； 小与0 表示取默认值 */
	private Long    maxCountMinute;
	
	/** 每10分钟最大允许的请求量。等于0 表示不限制访问量； 小与0 表示取默认值 */
	private Long    maxCountMinute10;
	
	/** 保存简单信息的解析对象的名称。当本属性有值时，表示保存简单信息 */
	private String  SimpleClassName;

	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getMaxCountMinute() {
		return maxCountMinute;
	}

	public void setMaxCountMinute(Long maxCountMinute) {
		this.maxCountMinute = maxCountMinute;
	}

	public Long getMaxCountMinute10() {
		return maxCountMinute10;
	}

	public void setMaxCountMinute10(Long maxCountMinute10) {
		this.maxCountMinute10 = maxCountMinute10;
	}

	public String getSimpleClassName() {
		return SimpleClassName;
	}

	public void setSimpleClassName(String simpleClassName) {
		SimpleClassName = simpleClassName;
	}
	
}
