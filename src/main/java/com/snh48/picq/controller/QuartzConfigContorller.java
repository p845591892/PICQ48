package com.snh48.picq.controller;

import javax.net.ssl.HttpsURLConnection;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.annotation.Log;
import com.snh48.picq.annotation.OperationType;
import com.snh48.picq.entity.QuartzConfig;
import com.snh48.picq.quartz.QuartzManage;
import com.snh48.picq.service.QuartzConfigService;
import com.snh48.picq.utils.StringUtil;
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
	 * 新增一条定时任务
	 * 
	 * @param quartzConfig 任务实例
	 */
	@Log(desc = "新增定时任务", type = OperationType.ADD)
	@PostMapping("/add")
	public ResultVO addQuartzConfig(QuartzConfig quartzConfig) {
		ResultVO result = new ResultVO();
		try {
			quartzConfigService.saveQuartzConfig(quartzConfig);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		result.setStatus(HttpsURLConnection.HTTP_OK);
		return result;
	}

	/**
	 * @Description: 修改定时任务配置
	 * @author JuFF_白羽
	 * @throws SchedulerException
	 */
	@Log(desc = "修改定时任务", type = OperationType.UPDATE)
	@PostMapping("/update")
	public ResultVO updateQuartzConfig(QuartzConfig quartzConfig) throws SchedulerException {
		ResultVO result = new ResultVO();
		if (quartzManage.checkJob(quartzConfig)) {
			result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
			result.setCause("任务进行中，无法修改。");
		} else {
			if (quartzConfig.getId() == null) {
				result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
				result.setCause("任务ID不能为空");
			} else if (StringUtil.isEmpty(quartzConfig.getJobName())) {
				result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
				result.setCause("任务名不能为空");
			} else if (StringUtil.isEmpty(quartzConfig.getClassPath())) {
				result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
				result.setCause("定时任务类不能为空");
			} else if (StringUtil.isEmpty(quartzConfig.getCron())) {
				result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
				result.setCause("定时公式不能为空");
			} else {
				quartzConfigService.saveQuartzConfig(quartzConfig);
				result.setStatus(HttpsURLConnection.HTTP_OK);
			}
		}
		return result;
	}

	/**
	 * 启动定时任务
	 * 
	 * @param id 任务ID
	 */
	@Log(desc = "启动定时任务", type = OperationType.UPDATE)
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
	@Log(desc = "关闭定时任务", type = OperationType.UPDATE)
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

	/**
	 * 根据ID删除一条任务实例
	 * 
	 * @param id 任务ID
	 */
	@Log(desc = "删除定时任务", type = OperationType.DEL)
	@PostMapping("/delete")
	public ResultVO deleteQuartzConfig(Long id) {
		ResultVO result = new ResultVO();
		if (quartzManage.checkJob(id)) {
			result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
			result.setCause("任务进行中，无法删除！");
		} else {
			quartzConfigService.delete(id);
			result.setStatus(HttpsURLConnection.HTTP_OK);
		}
		return result;
	}

}
