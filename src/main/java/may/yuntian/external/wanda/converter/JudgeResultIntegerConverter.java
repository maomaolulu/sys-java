package may.yuntian.external.wanda.converter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import may.yuntian.common.exception.RRException;

/**
 * 结果判定数值转换器
 * @author: liyongqiang
 * @create: 2023-07-07 16:28
 */
public class JudgeResultIntegerConverter implements Converter<Integer> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // cellData是当前单元格的数据
        if (StrUtil.isBlank(cellData.getStringValue())) {
            throw new RRException("结果判定不能为空！");
        }
        if ("不判定".equals(cellData.getStringValue())) {
            return 1;
        }
        if ("符合".equals(cellData.getStringValue())) {
            return 2;
        }
        return 3;
    }

}
