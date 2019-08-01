package com.example.springbootshirojwt.util;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 所有的请求都会先经过Filter，所以我们继承官方的BasicHttpAuthenticationFilter，并且重写鉴权的方法。
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {
    // 已登录认证标识,前端放置在 headers 头文件中的已登录认证，如果用户发起的请求是需要登录认证才能正常返回的，那么头文件中就必须存在该标识并携带有效值
    private static String LOGIN_SIGN = "Authorization";
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    /**
     * 检测用户是否已经登录
     * 检测header里面是否包含Authorization字段即可
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(LOGIN_SIGN);
        return authorization != null;
    }

    /**
     * 触发ShiroRealm 自身的登录控制（在realm中判断token的合法性，并给予认证,详情见realm）
     * 始终返回 true 的原因是因为具体的是否登录成功的判断，需要在 Realm 中手动实现，此处不做统一判断
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response)throws AuthenticationException {
        System.out.println("JWTFilter.executeLogin()");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        JWTToken jwtToken = new JWTToken(authorization);
        // 提交给realm进行登入，如果错误,登录失败他会抛出异常并被捕获(见下方isAccessAllowed()方法),且会将非法请求重定向，统一处理
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }
    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("JWTFilter.preHandle()");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials","true");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue){
        System.out.println("JWTFilter.isAccessAllowed()");
        if (isLoginAttempt(request, response)) {
            try{
                //此处抛出的错误在没有被catch的情况下也不能被@RestControllerAdvice捕捉,因为其只捕捉Controller层抛出的错误
                executeLogin(request, response);
            }catch(Exception e){
                try{
                HttpServletResponse httpServletResponse=(HttpServletResponse)response;
                HttpServletRequest httpServletRequest=(HttpServletRequest)request;
                //此处不能重定向,因为401接口为anno，所以不会走jwtFilter，不能跨域，而转发因为已经跨过域了，刚好满足需求。
                request.getRequestDispatcher("/401").forward(httpServletRequest,httpServletResponse);
                }catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
        return true;
    }
}
