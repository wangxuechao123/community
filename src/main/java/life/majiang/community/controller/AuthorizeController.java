package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;

import life.majiang.community.dto.GiteeUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.provider.GiteeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {
    //github
//    @Autowired
//    private GithubProvider githubProvider;
//    @GetMapping("/callback")
//    public String callback(@RequestParam(name="code") String code,
//                           @RequestParam(name="state") String state){
//        AccessTokenDTO accessTokenDto = new AccessTokenDTO();
//        accessTokenDto.setClient_id("504919e43d2c0f93f34b");
//        accessTokenDto.setClient_secret("4f9774c48a274ecf81aecab05e6d38099a4989e9");
//        accessTokenDto.setCode(code);
//        accessTokenDto.setRedirect_uri("http://localhost:8887/callback");
//        accessTokenDto.setState(state);
//        String accessToken = githubProvider.getAccessToken(accessTokenDto);
//        GithubUser user = githubProvider.getUser(accessToken);
//        System.out.println(user.getName());
//        return "index";
//    }


    @Autowired
    private GiteeProvider giteeProvider;

    @Value("${gitee.client.id}")
    private String clientId;
    @Value("${gitee.client.secret}")
    private String clientSecret;
    @Value("${gitee.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDto = new AccessTokenDTO();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(redirectUri);
        accessTokenDto.setState(state);
        String accessToken = giteeProvider.getAccessToken(accessTokenDto);
        GiteeUser giteeUser = giteeProvider.getUser(accessToken);
        if (giteeUser !=null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(giteeUser.getName());
            user.setAccountId(String.valueOf(giteeUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //登录成功，写cookie和session
            request.getSession().setAttribute("user",giteeUser);
            return "redirect:/";
        }else {
            //登陆失败，重新登陆
            return "redirect:/";
        }
    }
}
