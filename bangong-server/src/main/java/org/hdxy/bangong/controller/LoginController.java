package org.hdxy.bangong.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hdxy.bangong.pojo.Admin;
import org.hdxy.bangong.pojo.RespBean;
import org.hdxy.bangong.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登录
 */
@Api(tags = "loginController")
@RestController
public class LoginController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation("登录之后返回token")
    @PostMapping("/login")
    public RespBean login(String username, String password, HttpServletRequest request) {
        return adminService.login(username, password, request);
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal) {
        if (principal == null) {
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        admin.setPassword(null);
        return admin;
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public RespBean logout() {
        return RespBean.success("退出成功");
    }


    @GetMapping("hello")
    public String a(){
        return "aaa";
    }
}
