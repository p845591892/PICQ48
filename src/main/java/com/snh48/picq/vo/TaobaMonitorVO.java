package com.snh48.picq.vo;

import java.io.Serializable;

import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.entity.taoba.TaobaMonitor;

import lombok.Data;

/**
 * 桃叭监控QQ配置的VO类
 * 
 * @author shiro
 *
 */
@Data
public class TaobaMonitorVO implements Serializable {

	private static final long serialVersionUID = 3096097225392184049L;

	/**
	 * 桃叭监控配置表
	 */
	private TaobaMonitor taobaMonitor;

	/**
	 * （yyh）QQ群信息
	 */
	private QQCommunity qqCommunity;

	public TaobaMonitorVO() {
	}

	public TaobaMonitorVO(TaobaMonitor taobaMonitor) {
		QQCommunity qqCommunity = new QQCommunity();
		this.taobaMonitor = taobaMonitor;
		this.qqCommunity = qqCommunity;
	}

	public TaobaMonitorVO(QQCommunity qqCommunity) {
		TaobaMonitor taobaMonitor = new TaobaMonitor();
		this.taobaMonitor = taobaMonitor;
		this.qqCommunity = qqCommunity;
	}

	public TaobaMonitorVO(TaobaMonitor taobaMonitor, QQCommunity qqCommunity) {
		this.taobaMonitor = taobaMonitor;
		this.qqCommunity = qqCommunity;
	}

}
