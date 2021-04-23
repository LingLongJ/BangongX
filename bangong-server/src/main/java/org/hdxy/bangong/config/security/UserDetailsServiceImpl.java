package org.hdxy.bangong.config.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hdxy.bangong.pojo.Admin;
import org.hdxy.bangong.pojo.AdminRole;
import org.hdxy.bangong.pojo.Role;
import org.hdxy.bangong.service.IAdminRoleService;
import org.hdxy.bangong.service.IAdminService;
import org.hdxy.bangong.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IAdminService iAdminService;
    @Autowired
    private IAdminRoleService iAdminRoleService;
    @Autowired
    private IRoleService iRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//      根据用户名进行查找
        Admin admin = iAdminService.getAdminByUserName(username);
        //获取它的全部关联权限
        List<AdminRole> adminRoles = iAdminRoleService.list(new QueryWrapper<AdminRole>().eq("adminId", admin.getId()));
        String roles = "";
        //遍历获取全部权限并且组合成一个字符串以逗号分割 ROLE_admin,ROLE_test
        for (AdminRole adminRole : adminRoles) {
            Role role = iRoleService.getOne(new QueryWrapper<Role>().eq("id", adminRole.getRid()));
            roles = roles + role.getName() + ",";
        }
        roles = roles.substring(0, roles.length() - 1);
        if (admin != null && roles!=null) {
            //将用户名 密码 和权限信息组合成一个User对象
            return new User(
                    admin.getUsername(),
                    admin.getPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList(roles));
        }

        return null;
    }

}
