package org.hdxy.bangong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hdxy.bangong.pojo.Admin;
import org.hdxy.bangong.mapper.AdminMapper;
import org.hdxy.bangong.pojo.RespBean;
import org.hdxy.bangong.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hdxy.bangong.util.JwtTokenUtil;
import org.hdxy.bangong.util.UntilFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LK
 * @since 2021-04-22
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService detailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminMapper adminMapper;

    /**
     * 登录之后返回token
     *
     * @param username 用户名
     * @param password 密码
     * @param request  请求对象
     * @return
     */
    @Override
    public RespBean login(String username, String password, HttpServletRequest request) {
        //获取当前用户
        UserDetails userDetails = detailsService.loadUserByUsername(username);
        //判断用户是否存在 密码是个正确
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名密码不正确");
        }
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用");
        }
        System.out.println(userDetails.getAuthorities());
        //将用户信息构建Security认识的信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        //加入到全局中
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //根据用户名生成一个Token
        String token = JwtTokenUtil.generateToken(userDetails);
        //存入map中
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHeader", UntilFinal.TokenHandler);
        //构建成ResoBean对象返回出去
        return RespBean.success("登录成功", tokenMap);
    }

    /**
     * 根据用户名获取当前用户信息
     * @param username 用户名
     * @return  全部用户信息
     */
    @Override
    public Admin getAdminByUserName(String username) {
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username).eq("enabled",1));
        return admin;
    }
}
