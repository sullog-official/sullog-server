package sullog.backend.oauth.kakao.service;

import org.junit.jupiter.api.Test;
import sullog.backend.oauth.kakao.dto.UserInfoResponse;

import static org.assertj.core.api.Assertions.assertThat;

class KakaoOAuthTest {

    private final KakaoOAuth kakaoOAuth = new KakaoOAuth();

    @Test
    void 클라이언트에서_전달받은_인가코드로_accessToken을_조회한다() {
        // 인가코드는 smaple이 없어서 테스트코드 작성 스킵
    }

    @Test
    void accessToken으로_사용자정보를_조회한다() {
        //given
        String token = "7Bi9U6AEEIVlPVjXgjetV0eOfR_M2G5i7Z_AjBdRCiolEAAAAYZFLUCi";

        //when
        UserInfoResponse userInfo = kakaoOAuth.getUserInfo(token);

        //then
        assertThat(userInfo.getKakaoAccount().getNickName()).isEqualTo("최승연");
    }

}