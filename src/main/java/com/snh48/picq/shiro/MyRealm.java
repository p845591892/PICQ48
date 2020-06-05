package com.snh48.picq.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.snh48.picq.entity.system.Permission;
import com.snh48.picq.entity.system.Role;
import com.snh48.picq.entity.system.User;
import com.snh48.picq.exception.ActivationAccountException;
import com.snh48.picq.repository.system.UserRepository;

import lombok.extern.log4j.Log4j2;

/**
 * @Description: shiro自定义Realm
 *               <p>
 *               1、检查提交的进行认证的令牌信息。
 *               <p>
 *               2、根据令牌信息从数据源(通常为数据库)中获取用户信息。
 *               <p>
 *               3、对用户信息进行匹配验证。
 *               <p>
 *               4、验证通过将返回一个封装了用户信息的AuthenticationInfo实例。
 *               <p>
 *               5、验证失败则抛出异常信息。
 * @author JuFF_白羽
 * @date 2018年11月1日 上午11:27:34
 */
@Log4j2
public class MyRealm extends AuthorizingRealm {

	@Autowired
	private UserRepository userRepository;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		log.info("----->> shiro-获取身份权限");
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		User user = (User) principals.getPrimaryPrincipal();
		for (Role role : user.getRoles()) {
			authorizationInfo.addRole(role.getRole());
			for (Permission p : role.getPermissions()) {
				authorizationInfo.addStringPermission(p.getPermission());
			}
		}
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		log.info("----->> shiro-获取身份证明");
		
		// 获取用户的输入的账号.
		String username = (String) token.getPrincipal();
		// 通过username从数据库中查找 User对象，如果找到，没找到.
		// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
		User user = null;
		try {
			user = userRepository.findByUsername(username);
		} catch (Exception e) {
			log.error("查询用户异常：{}", e.getMessage());
		}
		
		log.info("认证用户：{}", username);
		
		if (user == null) {
			// 用户不存在
			throw new UnknownAccountException();
		} else if (user.getState() == 2) {
			// 用户被锁定
			throw new LockedAccountException();
		} else if (user.getState() == 0) {
			// 用户未激活
			throw new ActivationAccountException();
		} else {
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, // 用户名
					user.getPassword(), // 密码
					getName() // realm name
			);
			return authenticationInfo;
		}
	}

}
