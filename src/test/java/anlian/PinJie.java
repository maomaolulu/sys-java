package anlian;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import org.junit.jupiter.api.Test;


import may.yuntian.anlian.utils.DateUtils;

public class PinJie {
	
//	@Test
	public void main() {

		String sampleCodeStr = "ZJ-210402-8888008-[01,02,03]";//ZJ-210402-8888008-[01K,02K,03K]

		String sampleCodeStr2 = sampleCodeStr.replace("[", "@");
		
		System.out.println(sampleCodeStr2);
		String sampleCodeStr3 = sampleCodeStr2.replace("]", "");
		System.out.println(sampleCodeStr3);
		
		String kb = "";
		String[] sampleCode = sampleCodeStr3.split("@");
		System.out.println(sampleCode[0]);
		String[] codes = sampleCode[1].split(",");
		
		for(String code:codes) {
			kb+=code+"K,";
		}
		kb="["+kb.substring(0,kb.length()-1)+"]";
		String sampleKbCode = sampleCode[0]+kb;
		
		
		
		System.out.println(sampleKbCode);
		
		String string = "ZJ210901-09";
		string = string+"K";
		System.out.println(string);
	}
	

	
	
}
