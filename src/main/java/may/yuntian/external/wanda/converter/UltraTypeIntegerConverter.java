package may.yuntian.external.wanda.converter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import may.yuntian.common.exception.RRException;

/**
 * 紫外光谱分类数值转换器
 * @author: liyongqiang
 * @create: 2023-07-07 16:56
 */
public class UltraTypeIntegerConverter implements Converter<Integer> {

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
            throw new RRException("紫外光谱分类不能为空！");
        }
        if ("中波紫外线".equals(cellData.getStringValue())) {
            return 1;
        }
        if ("短波紫外线".equals(cellData.getStringValue())) {
            return 2;
        }
        return 3;
    }
}
