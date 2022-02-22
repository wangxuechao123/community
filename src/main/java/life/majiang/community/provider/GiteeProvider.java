package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GiteeUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GiteeProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        // 这里需要把类转换成json字符串，用到fastjson API
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        String url = "https://gitee.com/oauth/token?grant_type=authorization_code&code="+accessTokenDTO.getCode()+"&client_id="+accessTokenDTO.getClient_id()+"&redirect_uri="+accessTokenDTO.getRedirect_uri()+"&client_secret="+accessTokenDTO.getClient_secret();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            JSONObject jsonObject = JSON.parseObject(string);
            return jsonObject.getString("access_token");
        } catch (Exception e) {
            log.error("getAccessToken error,{}", accessTokenDTO, e);
        }
        return null;
    }

    public GiteeUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://gitee.com/api/v5/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GiteeUser giteeUser = JSON.parseObject(string, GiteeUser.class);
            return giteeUser;
        } catch (Exception e) {
            log.error("getUser error,{}", accessToken, e);
        }
        return null;
    }
}
