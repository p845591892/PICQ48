package com.snh48.picq.vo;

import java.io.Serializable;

import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.entity.modian.CommentMonitor;

import lombok.Data;

/**
 * @ClassName: CommentMonitorVO
 * @Description: 摩点评论监控配置VO类
 *               <p>
 *               包含了摩点评论监控和QQ表字段。
 * @author JuFF_白羽
 * @date 2018年8月9日 上午9:41:28
 */
@Data
public class CommentMonitorVO implements Serializable {

	private static final long serialVersionUID = 4521408853435630479L;

	/**
	 * 摩点项目评论监控配置
	 */
	private CommentMonitor commentMonitor;

	/**
	 * （yyh）QQ群信息
	 */
	private QQCommunity qqCommunity;

	public CommentMonitorVO() {
	}

	public CommentMonitorVO(CommentMonitor commentMonitor) {
		QQCommunity qqCommunity = new QQCommunity();
		this.commentMonitor = commentMonitor;
		this.qqCommunity = qqCommunity;
	}

	public CommentMonitorVO(QQCommunity qqCommunity) {
		CommentMonitor commentMonitor = new CommentMonitor();
		this.commentMonitor = commentMonitor;
		this.qqCommunity = qqCommunity;
	}

	public CommentMonitorVO(CommentMonitor commentMonitor, QQCommunity qqCommunity) {
		this.commentMonitor = commentMonitor;
		this.qqCommunity = qqCommunity;
	}

}
