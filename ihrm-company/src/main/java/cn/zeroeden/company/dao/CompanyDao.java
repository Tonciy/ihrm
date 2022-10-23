package cn.zeroeden.company.dao;

import cn.zeroeden.domain.company.Company;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author Zero
 * @Description 描述此类
 * <实体类，主键类型>
 */
@Mapper
public interface CompanyDao extends BaseMapper<Company> {
}
