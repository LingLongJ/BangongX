package org.hdxy.bangong.config.security;

import org.hdxy.bangong.util.JwtTokenUtil;
import org.hdxy.bangong.util.UntilFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jwt登录授权过滤器
 */
public class JwtAuthencationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //下面的请求直接放行不需要进行身份认证
        if (request.getRequestURI().startsWith("/login")
                || request.getRequestURI().startsWith("/doc.html")
                || request.getRequestURI().startsWith("/swagger-resources")
                || request.getRequestURI().startsWith("/webjars")
                || request.getRequestURI().contains("css")
                || request.getRequestURI().contains("js")
                || request.getRequestURI().contains("jpg")
                || request.getRequestURI().contains("png")
                || request.getRequestURI().contains("gif")
                || request.getRequestURI().contains("/v2/api-docs")
        ) {
            System.out.println("JwtAuthencationTokenFilter中不进行过滤的uri----" + request.getRequestURI());
            filterChain.doFilter(request, response);//放行
            return;
        }
        System.out.println("JwtAuthencationTokenFilter中进行过滤的uri----" + request.getRequestURI());
        //获取请求头Authorization的值
        String authToken = request.getHeader(UntilFinal.TokenHandler);
        //判断authToken不为空 且 不是空字符串 且必须以Bearer开头
        if (authToken != null || !authToken.equals("") || authToken.startsWith(UntilFinal.TOKENHEADER)) {
            //去除掉Bearer获取真正的token
            String token = authToken.substring(UntilFinal.TOKENHEADER.length());
            //通过token获取用户名
            String username = JwtTokenUtil.getUserNameFromToken(token);
            //token存在 但没有登录
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //UserDetails获取这个用户
                UserDetails userDetails = detailsService.loadUserByUsername(username);
                //判断token有效期
                if (JwtTokenUtil.validateToken(token, userDetails)) {
                    //包装下获取到的信息 否则Security不认得
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
//                     authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //注入到Security全局中
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
        return;
    }
}
