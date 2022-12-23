package cn.zeroeden.salarys.dao;

import cn.zeroeden.domain.salarys.SalaryArchiveDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ArchiveDetailDao extends BaseMapper<SalaryArchiveDetail> {
}
