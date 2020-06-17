package com.snh48.picq.repository.snh48;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.snh48.Member;

/**
 * @ClassName: MemberRepository
 * @Description: 成员表DAO组件
 * @author JuFF_白羽
 * @date 2018年7月11日 上午10:21:53
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

	/**
	 * @Description: 根据房间监控状态获取成员信息
	 * @author JuFF_白羽
	 * @param roomMonitor 成员口袋房间监控状态
	 *                    <p>
	 *                    参数：1开启，2关闭
	 * @return List<Member> 符合监控状态条件的成员信息集合
	 */
	List<Member> findByRoomMonitor(Integer roomMonitor);

	/**
	 * @Description: 根据成员ID修改房间监控状态
	 * @author JuFF_白羽
	 * @param id          成员ID
	 * @param roomMonitor 监控状态码
	 * @return int 受影响的行数
	 */
	@Transactional
	@Modifying
	@Query(value = "update Member t set t.roomMonitor = ?2 where t.id = ?1")
	int updateRoomMonitorById(Long id, int roomMonitor);

	/**
	 * @Description: 根据监控状态获取成员数量
	 * @author JuFF_白羽
	 * @param roomMonitor 监控状态码
	 * @return Integer 成员数量（个）
	 */
	@Query("select count(t) from Member t where roomMonitor = ?1")
	Integer countByRoomMonitor(int roomMonitor);

	/**
	 * @Description: 获取非该监控状态的成员数量
	 * @author JuFF_白羽
	 * @param roomMonitor 监控状态码
	 * @return Integer 成员数量（个）
	 */
	@Query("select count(t) from Member t where roomMonitor != ?1")
	Integer countByNotRoomMonitor(int roomMonitor);

	/**
	 * 获取成员信息
	 * 
	 * @param roomId 口袋房间ID
	 * @return {@link Menber}
	 */
	Member findByRoomId(Long roomId);

	/**
	 * @Description: 根据房间监控状态获取成员信息
	 * @author JuFF_白羽
	 * @param roomMonitor 排除的监控状态
	 *                    <p>
	 *                    参数：1开启，2关闭，404未开启
	 * @return {@link List<Menber>}
	 */
	List<Member> findByRoomMonitorNot(int roomMonitor);

}
