package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;

import life.majiang.community.dto.GiteeUser;
import life.majiang.community.provider.GiteeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state){
        AccessTokenDTO accessTokenDto = new AccessTokenDTO();
        accessTokenDto.setClient_id("c36af9184110141bbaff882a83d35b6d7aaaad90b228799574a44b40d9221948");
        accessTokenDto.setClient_secret("cff1bfb77b599b5a642062a2714022abef3cd591850b597e6fd01734ca730b5b");
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri("http://localhost:8887/callback");
        accessTokenDto.setState(state);
        String accessToken = giteeProvider.getAccessToken(accessTokenDto);
        System.out.println(accessToken);
        GiteeUser user = giteeProvider.getUser(accessToken);
        System.out.println(user.getName());
        System.out.println(user);

        return "index";
    }
}
