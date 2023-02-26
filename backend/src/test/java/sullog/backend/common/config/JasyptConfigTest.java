package sullog.backend.common.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {

    @Test
    void jasypt() {
        String todoStr = "변환하고할 문자열"; // (1)

        // 여기에 나온 결과를 ENC()로 감싸 yml에 넣으면 됩니다
        System.out.println("todoStr = " + jasyptEncoding(todoStr)); // (3)
    }

    public String jasyptEncoding(String value) {

        String key = "전달드린 JASYPT PASSWORD(JASYPT_PASSWORD= 이후 부분만 넣으면 됩니다)"; // (2)
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }

}