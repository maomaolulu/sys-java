package may.yuntian.external.express.sf.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author: liyongqiang
 * @create: 2023-05-22 15:52
 */
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public HttpClientUtil() {
    }

    public String post(String url, StringEntity entity) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = this.postForm(url, entity);
        String body = "";
        body = invoke(httpClient, post);

        try {
            httpClient.close();
        } catch (IOException var7) {
            logger.error("HttpClientService post error", var7);
        }

        return body;
    }

    public String post(String url, String name, String value) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> parameters = new ArrayList();
        parameters.add(new BasicNameValuePair("content", value));
        HttpPost post = this.postForm(url, new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8")));
        String body = "";
        body = invoke(httpClient, post);

        try {
            httpClient.close();
        } catch (IOException var9) {
            logger.error("HttpClientService post error", var9);
        }

        return body;
    }

    public String postSFAPI(String url, String xml, String verifyCode) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> parameters = new ArrayList();
        parameters.add(new BasicNameValuePair("xml", xml));
        parameters.add(new BasicNameValuePair("verifyCode", verifyCode));
        HttpPost post = this.postForm(url, new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8")));
        String body = "";
        body = invoke(httpClient, post);

        try {
            httpClient.close();
        } catch (IOException var9) {
            logger.error("HttpClientService post error", var9);
        }

        return body;
    }

    public String get(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        String body = "";
        body = invoke(httpClient, get);

        try {
            httpClient.close();
        } catch (IOException var6) {
            logger.error("HttpClientService get error", var6);
        }

        return body;
    }

    public String delete(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete delete = new HttpDelete(url);
        String body = "";
        body = invoke(httpClient, delete);

        try {
            httpClient.close();
        } catch (IOException var6) {
            logger.error("HttpClientService get error", var6);
        }

        return body;
    }

    public static String invoke(CloseableHttpClient httpclient, HttpUriRequest httpost) {
        HttpResponse response = sendRequest(httpclient, httpost);
        String body = "";
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            body = parseResponse(response);
        }

        return body;
    }

    private static String parseResponse(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        String body = "";

        try {
            if (entity != null) {
                body = EntityUtils.toString(entity);
            }
        } catch (ParseException var4) {
            logger.error("HttpClientService paseResponse error", var4);
        } catch (IOException var5) {
            logger.error("HttpClientService paseResponse error", var5);
        }

        return body;
    }

    private static HttpResponse sendRequest(CloseableHttpClient httpclient, HttpUriRequest httpost) {
        CloseableHttpResponse response = null;

        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException var4) {
            logger.error("HttpClientService sendRequest error", var4);
        } catch (IOException var5) {
            logger.error("HttpClientService sendRequest error", var5);
        }

        return response;
    }

    public HttpPost postForm(String url, StringEntity entity) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        return httpPost;
    }

    public static String post(String url, Map<String, String> params) throws UnsupportedEncodingException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(5000).setSocketTimeout(60000).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("appCode", (String)params.get("partnerID"));
        httpPost.addHeader("timestamp", getFormatTimeString());
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        List<NameValuePair> paramsList = new ArrayList();
        Iterator var7 = params.entrySet().iterator();

        while(var7.hasNext()) {
            Entry<String, String> entry = (Entry)var7.next();
            paramsList.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
        }

        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(paramsList, "UTF-8");
        httpPost.setEntity(urlEncodedFormEntity);
        String body = invoke(httpClient, httpPost);

        try {
            httpClient.close();
        } catch (IOException var9) {
            logger.error("HttpClientService post error", var9);
        }

        return body;
    }

    public static String getFormatTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String startString = sdf.format(new Date());
        return startString;
    }
}
