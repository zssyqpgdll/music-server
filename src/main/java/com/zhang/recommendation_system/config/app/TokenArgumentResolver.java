
package com.zhang.recommendation_system.config.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhang.recommendation_system.dao.UserMapper;
import com.zhang.recommendation_system.dao.UserTokenDao;
import com.zhang.recommendation_system.pojo.User;
import com.zhang.recommendation_system.pojo.UserToken;
import com.zhang.recommendation_system.util.SysConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserMapper userDao;
    @Autowired
    private UserTokenDao userTokenDao;

    public TokenArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(TokenToUser.class)) {
            return true;
        }
        return false;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getParameterAnnotation(TokenToUser.class) instanceof TokenToUser) {
            User loginMallUser = null;
            String token = webRequest.getHeader("token");
            if (null != token && !"".equals(token) && token.length() == SysConstant.TOKEN_LENGTH) {
                UserToken userToken = userTokenDao.selectByToken(token);
                if (userToken == null || userToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
                    GHException.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
                }
//                loginMallUser = userDao.selectById(userToken.getUserId());
                // 这块也需要针对音乐app 进行修改
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                wrapper.eq("uid", userToken.getUserId());
                loginMallUser = userDao.selectOne(wrapper);

                if (loginMallUser == null) {
                    GHException.fail(ServiceResultEnum.USER_NULL_ERROR.getResult());
                }
                //账户锁定
//                if (user.getLockedFlag().intValue() == 1) {
//                    NewBeeMallException.fail(ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult());
//                }
                return loginMallUser;
            } else {
                GHException.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            }
        }
        return null;
    }

    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

}
