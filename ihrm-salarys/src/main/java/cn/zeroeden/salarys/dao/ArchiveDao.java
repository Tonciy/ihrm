package cn.zeroeden.salarys.dao;

import cn.zeroeden.domain.salarys.SalaryArchive;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ArchiveDao extends BaseMapper<SalaryArchive> {
}
