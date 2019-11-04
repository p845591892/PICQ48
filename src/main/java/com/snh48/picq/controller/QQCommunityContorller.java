package com.snh48.picq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.service.QQCommunityService;
import com.snh48.picq.vo.ResultVO;

/**
 * @ClassName: QQCommunityApi
 * @Description: QQ列表操作控制类
 *               <p>
 * 				提供QQ列表页面的操作请求接口。
 * @author JuFF_白羽
 * @date 2018年8月10日 下午3:19:06
 */
@RestController
@RequestMapping("/qq-community")
public class QQCommunityContorller {
	
	/**
	 * QQ列表操作服务类
	 */
	@Autowired
	private QQCommunityService qqCommunityService;
	
	/**
	 * @Description: 新增/修改QQ信息接口
	 * @author JuFF_白羽
	 */
	@PostMapping("/add")
	public ResultVO addQQCommunity(QQCommunity qqCommunity){
		ResultVO result = new ResultVO();
		int i = qqCommunityService.addQQCommunity(qqCommunity);
		if (i == 1) {
			result.setCause("QQ号不能为空");
		}
		if (i == 2) {
			result.setCause("QQ群名不能为空");
		}
		if (i == 3) {
			result.setCause("QQ号已存在");
		}
		result.setStatus(i);
		return result;
	}
	
	/**
	 * @Description: 修改QQ信息接口
	 * @author JuFF_白羽
	 */
	@PostMapping("/update")
	public ResultVO updateQQCommunity(QQCommunity qqCommunity){
		ResultVO result = new ResultVO();
		int i = qqCommunityService.updateQQCommunity(qqCommunity);
		if (i == 1) {
			result.setCause("QQ号不能为空");
		}
		if (i == 2) {
			result.setCause("QQ群名不能为空");
		}
		result.setStatus(i);
		return result;
	}
	
	/**
	 * @Description: 修改QQ信息接口
	 * @author JuFF_白羽
	 */
	@PostMapping("/delete")
	public ResultVO deleteQQCommunity(String id) {
		ResultVO result = new ResultVO();
		if (id != null) {
			result.setStatus(qqCommunityService.deleteQQCommunity(id));
		} else {
			result.setStatus(400);
			result.setCause("ID不能为空");
		}
		return result;
	}

}
