package com.snh48.picq.vo;

import java.io.Serializable;

import com.snh48.picq.entity.modian.MoDianComment;
import com.snh48.picq.entity.modian.MoDianPoolProject;

import lombok.Data;

/**
 * @ClassName: MoDianCommentVO
 * @Description: 摩点评论VO类
 *               <p>
 *               包含摩点评论和摩点集资项目的所有字段。
 * @author JuFF_白羽
 * @date 2018年8月9日 上午11:13:42
 */
@Data
public class MoDianCommentVO implements Serializable {

	private static final long serialVersionUID = -3602745252875992437L;

	/**
	 * 摩点评论表
	 */
	private MoDianComment moDianComment;

	/**
	 * 摩点集资项目表
	 */
	private MoDianPoolProject moDianPoolProject;

	public MoDianCommentVO() {
	}

	public MoDianCommentVO(MoDianComment moDianComment) {
		MoDianPoolProject moDianPoolProject = new MoDianPoolProject();
		this.moDianComment = moDianComment;
		this.moDianPoolProject = moDianPoolProject;
	}

	public MoDianCommentVO(MoDianPoolProject moDianPoolProject) {
		MoDianComment moDianComment = new MoDianComment();
		this.moDianComment = moDianComment;
		this.moDianPoolProject = moDianPoolProject;
	}

	public MoDianCommentVO(MoDianComment moDianComment, MoDianPoolProject moDianPoolProject) {
		this.moDianComment = moDianComment;
		this.moDianPoolProject = moDianPoolProject;
	}

}
