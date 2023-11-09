package may.yuntian.external.express.sf.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import may.yuntian.external.express.sf.service.IServiceCodeStandard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: liyongqiang
 * @create: 2023-05-22 15:52
 */
public class CallExpressServiceTools {
    private static Logger logger = LoggerFactory.getLogger(CallExpressServiceTools.class);
    private static CallExpressServiceTools tools = new CallExpressServiceTools();

    private CallExpressServiceTools() {
    }

    public static CallExpressServiceTools getInstance() {
        Class var0 = CallExpressServiceTools.class;
        synchronized(CallExpressServiceTools.class) {
            if (tools == null) {
                tools = new CallExpressServiceTools();
            }
        }

        return tools;
    }

    public static String callSfExpressServiceByCSIM(String reqURL, String reqXML, String clientCode, String checkword) {
        String result = null;
        String verifyCode = VerifyCodeUtil.md5EncryptAndBase64(reqXML + checkword);
        result = querySFAPIservice(reqURL, reqXML, verifyCode);
        return result;
    }

    public static String querySFAPIservice(String url, String xml, String verifyCode) {
        HttpClientUtil httpclient = new HttpClientUtil();
        if (url == null) {
            url = "http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";
        }

        String result = null;

        try {
            result = httpclient.postSFAPI(url, xml, verifyCode);
            return result;
        } catch (Exception var6) {
            logger.warn(" " + var6);
            return null;
        }
    }

    public static String packageMsgData(IServiceCodeStandard serviceCode) {
        String jsonString = "";

        try {
            InputStream is = new FileInputStream(serviceCode.getPath());
            byte[] bs = new byte[is.available()];
            is.read(bs);
            jsonString = new String(bs);
        } catch (Exception var4) {
        }

        return jsonString;
    }

    public static String getMsgDigest(String msgData, String timeStamp, String md5Key) throws UnsupportedEncodingException {
        String msgDigest = VerifyCodeUtil.md5EncryptAndBase64(URLEncoder.encode(msgData + timeStamp + md5Key, "UTF-8"));
        return msgDigest;
    }
}
