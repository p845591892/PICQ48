package com.snh48.picq.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	 * 序列(QQ群号)
	 */
	@Id
	@Column(name = "ID")
	private Long id;

	/**
	 * QQ群名(窗口名)
	 */
	@Column(name = "COMMUNITY_NAME", length = 500)
	private String communityName;

}
