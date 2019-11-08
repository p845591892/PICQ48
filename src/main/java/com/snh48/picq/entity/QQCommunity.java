package com.snh48.picq.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.snh48.picq.core.QQType;

import lombok.Data;

/**
 * @ClassName: Community
 * @Description: （yyh）QQ群信息
 * @author JuFF_白羽
 * @date 2018年7月6日 下午4:42:37
 */
@Data
@Entity
@Table(name = "T_QQ_COMMUNITY")
public class QQCommunity implements Serializable {

	private static final long serialVersionUID = -8245053442842866062L;

	/**
	 * ID(QQ群/QQ号)
	 */
	@Id
	@Column(name = "ID")
	private Long id;

	/**
	 * QQ群/QQ名
	 */
	@Column(name = "COMMUNITY_NAME", length = 225)
	private String communityName;

	/**
	 * QQ类型（group：QQ群 ， friend：QQ好友）
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "QQ_TYPE", length = 20)
	private QQType qqType;

}
