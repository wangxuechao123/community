package life.majiang.community.provider;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
@Slf4j
@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDto){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        // 这里需要把类转换成json字符串，用到fastjson API
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));// 把类对象转换成json字符串
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            //System.out.println(string);
            //access_token=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&scope=user&token_type=bearer
            String[] split = string.split("&");
            String token = split[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getAccessToken error,{}", accessTokenDto, e);
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);// 可以吧string(json字符串)自动转成java类对象
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            log.error("getUser error,{}", accessToken, e);
        }
        return null;
    }
}
