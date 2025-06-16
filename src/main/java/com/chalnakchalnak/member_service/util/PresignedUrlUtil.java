package com.chalnakchalnak.member_service.util;

import com.chalnakchalnak.member_service.common.entity.BaseResponseStatus;
import com.chalnakchalnak.member_service.common.exception.BaseException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class PresignedUrlUtil {

    private static final String algorithm = "HmacSHA256";
    private static final String data = "aws4_request";

    public static String generateDoc(String bucket,
                                     String algorithm,
                                     String credential,
                                     String dateTime,
                                     String contentType) {
        // Policy 문서 생성
        return  """
            {
              "expiration": "%s",
              "conditions": [
                {"bucket": "%s"},
                ["starts-with", "$key", ""],
                {"x-amz-algorithm": "%s"},
                {"x-amz-credential": "%s"},
                {"x-amz-date": "%s"},
                ["content-length-range", 0, 10485760],
                {"Content-Type": "%s"}
              ]
            }
            """.formatted(
                new Date(System.currentTimeMillis() + 5 * 60 * 1000).toInstant().toString(),
                bucket,
                algorithm,
                credential,
                dateTime,
                contentType
        );
    }

    public static byte[] hmacSha256(byte[] key, String data) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(key, algorithm));
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.UNABLE_TO_CALCULATE_HMAC);
        }
    }

    public static byte[] getSignatureKey(String secretKey, String date, String region, String service) {
        byte[] kDate = hmacSha256(("AWS4" + secretKey).getBytes(StandardCharsets.UTF_8), date);
        byte[] kRegion = hmacSha256(kDate, region);
        byte[] kService = hmacSha256(kRegion, service);
        return hmacSha256(kService, data);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
