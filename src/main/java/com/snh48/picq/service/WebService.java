package com.snh48.picq.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.snh48.picq.vo.MtboxVO;

/**
 * @Description: 页面跳转请求服务类
 * @author: JuFF_白羽
 * @date: 2018年9月23日 下午9:27:25
 */
public interface WebService {

	/**
	 * .mtbox中的数据
	 * <p>
	 * 共5组
	 * 
	 * @return
	 */
	public Map<String, MtboxVO> getMtboxData();

	/**
	 * .ds中的数据
	 * <P>
	 * 共10组
	 * 
	 * @return
	 * @throws ParseException
	 */
	public Map<String, MtboxVO> getDsData() throws ParseException;

	/**
	 * @Description: 获取摩点项目的名称
	 * @author JuFF_白羽
	 * @param projectIds 摩点项目ID，可用英文的“,”连接多个ID
	 * @return String 返回类型
	 */
	public String getProjectNames(String projectIds);

	/**
	 * @Description: 获取摩点项目详情列表
	 * @author JuFF_白羽
	 * @param projectIds 摩点项目ID，可用英文的“,”连接多个ID
	 * @return List<MtboxVO> 返回包含用户名称，打卡次数，集资总额三个字段的对象集合。
	 */
	public List<MtboxVO> getModianDetailsTable(String projectIds);

	/**
	 * 获取摩点项目的名称
	 * 
	 * @param detailIds 项目ID，多个ID用逗号间隔
	 * @return 可视化页面的总标题
	 */
	public String getTaobaDetailNames(String detailIds);

	/**
	 * 获取桃叭项目集资详细列表
	 * 
	 * @param detailIds 项目ID，多个ID用逗号间隔
	 * @return 可视化页面的集资详细表格
	 */
	public List<MtboxVO> getTaobaJoinTable(String detailIds);

	/**
	 * .firwin中的数据
	 * <p>
	 * 共3组
	 */
	public List<MtboxVO> getFirwinData();

	/**
	 * .custom-bar-chart中的数据
	 * <p>
	 * y：y轴数据 x：x轴数据
	 */
	public Map<String, Object> getBarData();

}
