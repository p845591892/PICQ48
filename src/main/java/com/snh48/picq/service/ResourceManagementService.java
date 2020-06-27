package com.snh48.picq.service;

import org.springframework.data.domain.Page;

import com.github.pagehelper.PageInfo;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.entity.snh48.RoomMessage;
import com.snh48.picq.vo.MemberVO;
import com.snh48.picq.vo.RoomMessageVO;

public interface ResourceManagementService {

	/**
	 * @Description: 获取成员房间的监控信息列表的HTML
	 * @author JuFF_白羽
	 * @param roomId 房间ID
	 * @return String Table的DetailView中的HTML内容
	 */
	public String getRoomMonitorTableHtml(Long roomId);

	/**
	 * @Description: 获取成员房间监控的新增弹窗内容
	 * @author JuFF_白羽
	 * @param roomId 房间ID
	 * @return String 新增弹窗的HTML
	 */
	public String getMeberAddMonitorLayerHtml(Long roomId);

	/**
	 * @Description: 获取摩点项目的监控信息列表
	 * @author JuFF_白羽
	 * @param projectId 摩点项目ID
	 * @return String 摩点集资记录监控列表的HTML
	 */
	public String getCommentMonitorTableHtml(Long projectId);

	/**
	 * @Description: 获取摩点项目监控的新增弹窗内容
	 * @author JuFF_白羽
	 * @param projectId 摩点项目ID
	 * @return String 新增弹窗的HTML
	 */
	public String getModianAddMonitorLayerHtml(Long projectId);

	/**
	 * @Description: 获取微博动态监控列表
	 * @author JuFF_白羽
	 * @param userId 微博用户ID
	 * @return String 微博动态监控列表的HTML
	 */
	public String getDynamicMonitorTableHtml(Long userId);

	/**
	 * 获取微博动态监控的新增弹窗内容
	 * 
	 * @param userId 微博用户ID
	 * @return 新增弹窗的HTML
	 */
	public String getWeiboAddMonitorLayerHtml(Long userId);

	/**
	 * @Description: 分页获取口袋房间消息
	 * @author JuFF_白羽
	 * @param pageNumber 当前页数
	 * @param pageSize   每页数据条数
	 * @return PageInfo<RoomMessage> 返回PageInfo的口袋房间消息表
	 */
	public PageInfo<RoomMessageVO> getRoomMessage(Integer pageNumber, Integer pageSize);

	/**
	 * 分页获取成员列表
	 * 
	 * @param pageNumber 当前页数
	 * @param pageSize   每页条数
	 * @param vo         匹配参数对象
	 * @return Page<Member> 返回Page的成员列表
	 */
	public Page<Member> getMembers(Integer pageNumber, Integer pageSize, MemberVO vo);

	/**
	 * 分页获取口袋房间消息列表
	 * 
	 * @param pageNumber 当前页数
	 * @param pageSize   每页条数
	 * @param vo         房间消息参数对象
	 * @return Page<RoomMessage> 返回Page的房间消息列表
	 */
	public Page<RoomMessage> getRoomMessages(Integer pageNumber, Integer pageSize, RoomMessageVO vo);

	/**
	 * 获取桃叭项目监控配置列表的HTML
	 * 
	 * @param detailId 项目ID
	 * @return Table的DetailView中的HTML内容
	 */
	public String getTaobaMonitorHtml(Long detailId);

	/**
	 * 获取桃叭项目监控的新增弹窗内容
	 * @param detailId 项目ID
	 * @return 弹窗的HTML
	 */
	public String getTaobaMonitorLayerHtml(Long detailId);
}
