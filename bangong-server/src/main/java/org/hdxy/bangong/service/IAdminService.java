package org.hdxy.bangong.service;

import org.hdxy.bangong.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hdxy.bangong.pojo.RespBean;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LK
 * @since 2021-04-22
 */
public interface IAdminService extends IService<Admin> {

    RespBean login(String username, String password, HttpServletRequest request);

    Admin getAdminByUserName(String username);
}
