package com.snh48.picq.repository.weibo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.snh48.picq.entity.weibo.WeiboUser;

/**
 * @ClassName: UserRepository
 * @Description: 微博用户表DAO组件
 * @author JuFF_白羽
 * @date 2018年8月1日 上午10:45:19
 */
@Repository
public interface WeiboUserRepository extends JpaRepository<WeiboUser, Long> {

	/**
	 * @Description: 根据粉丝数降序获取微博用户列表
	 * @author JuFF_白羽
	 * @return List<WeiboUser> 微博用户集合
	 */
	@Query("from WeiboUser t order by t.followersCount desc")
	List<WeiboUser> findOrderByFollowersCountDesc();

}
