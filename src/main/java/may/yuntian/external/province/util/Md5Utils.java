package may.yuntian.external.province.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5对请求头headSign用户认证密钥
 * @author: liyongqiang
 * @create: 2023-04-04 09:28
 */
public class Md5Utils {

    /**
     * md5加密 32位转小写
     * @param str
     * @return
     */
    public static String toMD5Way (String str) {
        StringBuffer hexValue = new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(str.getBytes());

            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 默认小写，在toString后加toUpperCase()即为大写加密
        return hexValue.toString();
    }

}
