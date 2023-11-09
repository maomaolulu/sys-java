package may.yuntian.untils;

import com.github.pagehelper.PageHelper;

public class pageUtil2 {
    public static void startPage(){

         Integer pageIndex = ServletUtils.getParameterToInt("pageIndex");
         Integer pageSize = ServletUtils.getParameterToInt("pageSize");
         String orderByColumn = ServletUtils.getParameter("sort");

        PageHelper.startPage(pageIndex,pageSize,orderByColumn );
    }

}
