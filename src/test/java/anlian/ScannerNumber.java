package anlian;

import may.yuntian.anlian.utils.Number2Money;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

public class ScannerNumber {
	String s = "do<br>a38<br><br>";
	public static void main(String[] args) {
        
        String a = "10000.01";
        String format = Number2Money.format(a);
        System.out.println("format = " + format);

//		SimpleDateFormat format = newCommission SimpleDateFormat("yyyy-MM-dd");
//		List<String> codeList = newCommission ArrayList<String>();
//		String sampleCodeStr = "2017-KP-053-T001-01";//ZJ-210402-8888008-[01K,02K,03K]
//		String string = sampleCodeStr.substring(sampleCodeStr.length()-6, sampleCodeStr.length());
//		System.err.println(string);
//		String[] strings = sampleCodeStr.split("-");
//		for(String s:strings) {
//			System.err.println(s);
//		}
//		String laString = strings[strings.length-1];
//		String nextString = strings[strings.length-2];
//		System.err.println(laString+"===="+nextString);
//		String num = nextString.substring(nextString.length()-3, nextString.length());
//		System.err.print(num+laString);
//		try {
//			System.err.println(sampleCodeStr.getBytes("gbk"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		String sampleCodeStr2 = sampleCodeStr.replace("[", "@");
//		
//		System.out.println(sampleCodeStr2);
//		String sampleCodeStr3 = sampleCodeStr2.replace("]", "");
//		System.out.println(sampleCodeStr3);
//		
//		String[] sampleCode = sampleCodeStr3.split("@");
//		System.out.println(sampleCode[0]);
//		String[] codes = sampleCode[1].split(",");
//		for(String code:codes) {
//			String samplecodenum = sampleCode[0]+code;
//			System.out.println(samplecodenum);
//			codeList.add(samplecodenum);
//		}
//		System.out.println(codeList.get(0));
		
		
		
//		String aString = "y=0.1646x+0.0022";
//		String a1String = aString.replace("+", "@");
//		String[] aString2Str = a1String.split("@");
//		String b = aString2Str[1];
//		String a2String = aString2Str[0];
//		String a2 = a2String.replace("x", "");
//		String a3 = a2.replace("=", "@");
//		String[] a4 = a3.split("@");
//		String a = a4[1];
//		String ax = "xvalue";
//		String by = "yvalue";
		
//		String string = Calculation.curveEquation(ax,by,aString);
		
//		String newString = string.replace("x", ax).replace("y", by);
		
//		System.err.println(a2String);
//		System.err.println(b);
//		System.err.println(a2);
//		System.err.println(a);
//		System.err.println("x="+"("+"y"+"-"+b+")"+"+"+a);
//		System.err.println(string);
//		System.err.println(newString);
		
		
		
//		Calendar cale = Calendar.getInstance();
//	    cale.add(Calendar.WEEK_OF_MONTH, 0);
////	    cale.set(Calendar.DAY_OF_WEEK, 2);
//	    cale.setFirstDayOfWeek(Calendar.MONDAY);
//	    cale.set(Calendar.DAY_OF_WEEK, cale.getFirstDayOfWeek());
//	    Date date = cale.getTime();
//		
//		System.out.println(date);
		
		
//		String str="27.5℃ 101.1kPa";
//		String stringCode = str.replace(" ", "@");
//		System.out.println(stringCode);
//		String[] stringCodes = stringCode.split("@");
//		System.out.println(stringCodes[0]+"====="+stringCodes[1]);
//		System.err.println(format.format(dateTimeMonthStart()));
//		System.err.println(format.format(dateTimeMonthEnd()));
//		System.err.println(format.format(getYearFirst(Integer.valueOf(DateUtils.getNowYear()))));
//		System.err.println(format.format(getYearLast(Integer.valueOf(DateUtils.getNowYear()))));
//		String uuid = UUID.randomUUID().toString().replaceAll("-","");
//		 String text = "http://coa.anliantest.com/#/previw_file？uuid="+uuid;
////		 String imgPath = "";
//		 String destPath = "C:/Users/ThinkPad/Desktop/原始记录表/"+uuid+".jpg";
//		 try {
//			QRCodeUtil.encode(text, destPath);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		 
//		System.err.println(QrCodeUtil.generateAsBase64(text, newCommission QrConfig(), "png"));
		
//		public class subString {
//		    public static void main(String[] args) {
		        
//		        s= trimheadAndEndChar(s,"<br>");
//		        System.err.println(s);
//		        int index = s.lastIndexOf("<br>");
//		        System.err.println(index);
//		        s = s.substring(index, s.length());
//		        System.err.println(s.substring(index, s.length()));
//		        if (s.equals("<br>")) {
//		        	s= trimheadAndEndChar(s,"<br>");
//					main();
//				}
//		       System.err.println(s);
		       
//		       String msg = "Client data";
//		         
//		        try {
//		            //创建一个Socket，跟服务器的8080端口链接
//		            Socket socket = newCommission Socket("127.0.0.1",8080);
//		            //使用PrintWriter和BufferedReader进行读写数据
//		            PrintWriter pw = newCommission PrintWriter(socket.getOutputStream());
//		            BufferedReader is = newCommission BufferedReader(newCommission InputStreamReader(socket.getInputStream()));
//		            //发送数据
//		            pw.println(msg);
//		            pw.flush();
//		            //接收数据
//		            String line = is.readLine();
//		            System.out.println("received from server" + line);
//		            //关闭资源
//		            pw.close();
//		            is.close();
//		            socket.close();
//		        } catch (UnknownHostException e) {
//		            // TODO Auto-generated catch block
//		            e.printStackTrace();
//		        } catch (IOException e) {
//		            // TODO Auto-generated catch block
//		            e.printStackTrace();
//		        }
//		        
		
//		java.text.NumberFormat  formater  =  java.text.DecimalFormat.getInstance();
//		formater.setMaximumFractionDigits(2);
//		formater.setMinimumFractionDigits(2);
//		System.out.println(formater.format(3.1415927));
//        String laeqString = "75.5";
//        String timeStr = "5";
//
//        Double res =Double.valueOf(laeqString) + 10 * Math.log10(Double.valueOf(timeStr) / 8);

//        BigDecimal max = new BigDecimal("0.11");
//        System.out.println(max);
		
//		List<String> codeList = newCommission ArrayList<String>(Arrays.asList("09","10","11"));
//		String cString = codeList.toString();
//		System.out.println(cString);
//		String substanceIds = StringUtils.join(codeList,",");
//		System.out.println(substanceIds);


//        public class GetServerIP {
//            public static void main(String[] args) throws UnknownHostException {
//                InetAddress ip = InetAddress.getLocalHost();
//                System.out.println(ip.getHostAddress());
//            }
//        }
		
	}
	
//	public static String trimheadAndEndChar(String content, String spliter) {
//		if(content.isEmpty() || spliter.isEmpty()) {
//			return content;
//		}
//		//要配替换正则表达式的特殊字符需要在前面加 进行转义
//		if(spliter.equals("*")
//				||spliter.equals("\\")
//				||spliter.equals("^")
//				||spliter.equals("$")
//				||spliter.equals("(")
//				||spliter.equals(")")
//				||spliter.equals("+")
//				||spliter.equals(".")
//				||spliter.equals("[")
//				||spliter.equals("?")
//				||spliter.equals("{")
//				||spliter.equals("|")){
//				spliter ="\\"+ spliter;
//		}
//		String rex = "^" + spliter + "*|"+ spliter + "*$";
////		System.out.println(rex);
//		return content.replaceAll(rex,"");
//
//	}
	
    /**
     * 获取当前月份第一天
     */
    public static Date dateTimeMonthStart(){
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        Date date = cale.getTime();
        return date;
    }
    
    /**
     * 获取当月最后一天
     */
    public static Date dateTimeMonthEnd(){
    	Calendar ca = Calendar.getInstance();    
    	ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date date = ca.getTime();
        return date;
    }
	
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR,year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
     
    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR,year);
        calendar.roll(Calendar.DAY_OF_YEAR,-1);
        Date currYearLast = calendar.getTime();
         
        return currYearLast;
    }
	
}
