package com.snh48.picq.controller;

import javax.net.ssl.HttpsURLConnection;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.entity.QuartzConfig;
import com.snh48.picq.quartz.QuartzManage;
import com.snh48.picq.service.QuartzConfigService;
import com.snh48.picq.vo.ResultVO;

/**
 * @Description: 定时任务配置列表操作控制类
 *               <p>
 *               提供轮询配置列表页面的操作请求接口。
 * @author JuFF_白羽
 * @date 2018年11月26日 下午4:17:36
 */
@RestController
@RequestMapping("/quartz-config")
public class QuartzConfigContorller {

	/**
	 * 定时任务配置列表操作服务类
	 */
	@Autowired
	private QuartzConfigService quartzConfigService;

	@Autowired
	private QuartzManage quartzManage;

	/**
	 * @Description: 修改定时任务配置
	 * @author JuFF_白羽
	 * @throws SchedulerException
	 */
	@PostMapping("/update")
	public ResultVO updateQuartzConfig(QuartzConfig quartzConfig) throws SchedulerException {
		ResultVO result = new ResultVO();
		if (quartzManage.checkJob(quartzConfig)) {
			result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
			result.setCause("任务进行中，无法修改。");
		} else {
			int i = quartzConfigService.updateQuartzConfig(quartzConfig);
			if (i == 0) {
				result.setCause("轮询配置ID不能为空");
			} else if (i == 1) {
				result.setCause("轮询任务名不能为空");
			} else if (i == 2) {
				result.setCause("定时任务触发器名不能为空");
			} else if (i == 3) {
				result.setCause("定时公式不能为空");
			} else if (i == 4) {
				result.setCause("定时任务不在调度工厂中");
			}
			result.setStatus(i);
		}
		return result;
	}

	/**
	 * 启动定时任务
	 * 
	 * @param id 任务ID
	 */
	@PostMapping("/start")
	public ResultVO startQuartzJob(Long id) {
		int i = quartzConfigService.startQuartzJob(id);
		ResultVO result = new ResultVO();
		if (i == 0) {
			result.setStatus(HttpsURLConnection.HTTP_NOT_FOUND);
			result.setCause("任务不存在。");
		} else if (i == 1) {
			result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
			result.setCause("任务已启动。");
		} else if (i == 2) {
			result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
			result.setCause("任务无法启动。");
		}
		result.setStatus(i);
		result.setCause("success");
		return result;
	}

	/**
	 * 关闭定时任务
	 * 
	 * @param id 任务ID
	 */
	@PostMapping("/shutdown")
	public ResultVO shutdownQuartzJob(Long id) {
		int i = quartzConfigService.shutdownQuartzJob(id);
		ResultVO result = new ResultVO();
		if (i == 0) {
			result.setStatus(HttpsURLConnection.HTTP_NOT_FOUND);
			result.setCause("任务不存在。");
		} else if (i == 1) {
			result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
			result.setCause("任务未启动。");
		} else if (i == 2) {
			result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
			result.setCause("任务无法关闭。");
		}
		result.setStatus(i);
		result.setCause("success");
		return result;
	}

}
