package may.yuntian.modules.sys.oauth2;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017-10-20
 */
public class OAuth2Token implements AuthenticationToken {
    private String token;

    public OAuth2Token(String token){
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
