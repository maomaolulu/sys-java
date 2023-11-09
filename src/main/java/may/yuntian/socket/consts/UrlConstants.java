package may.yuntian.socket.consts;

/**
 * @Author yrb
 * @Date 2023/4/11 19:16
 * @Version 1.0
 * @Description 地址
 */
public class UrlConstants {
    // 测试环境 保存留言
    public static final String TEST_SAVE_NOTE_URL = "http://192.168.0.203:81/proxyAnlianSysJava/anlian/wecommessage/save";
    // 正式环境 保存留言
    public static final String ONLINE_SAVE_NOTE_URL="http://47.111.249.220:81/proxyAnlianSysJava/anlian/wecommessage/save";

    public static String getStatusStr(Integer status){
        String statusStr = " ";
        switch (status){
            case 1:
                statusStr = "项目录入";
                break;
            case 2:
                statusStr = "下发";
                break;
            case 3:
                statusStr = "排单";
                break;
            case 4:
                statusStr = "现场调查";
                break;
            case 5:
                statusStr = "采样";
                break;
            case 10:
                statusStr = "送样";
                break;
            case 20:
                statusStr = "检测报告";
                break;
            case 22:
                statusStr = "检测报告发送";
                break;
            case 35:
                statusStr = "报告编制";
                break;
            case 36:
                statusStr = "审核";
                break;
            case 37:
                statusStr = "专家评审";
                break;
            case 38:
                statusStr = "出版前校核";
                break;
            case 40:
                statusStr = "质控签发";
                break;
            case 50:
                statusStr = "报告寄送";
                break;
            case 60:
                statusStr = "项目归档";
                break;
            case 70:
                statusStr = "项目完结";
                break;
            case 90:
                statusStr = "项目挂起";
                break;
            case 98:
                statusStr = "项目挂起";
                break;
            case 99:
                statusStr = "项目终止";
                break;
            default:
                break;
        }
        return statusStr;
    }

}
