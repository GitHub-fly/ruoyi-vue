package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysRegexMapper;
import com.ruoyi.system.domain.SysRegex;
import com.ruoyi.system.service.ISysRegexService;

/**
 * 正则达Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-03-01
 */
@Service
public class SysRegexServiceImpl implements ISysRegexService 
{
    @Autowired
    private SysRegexMapper sysRegexMapper;

    /**
     * 查询正则达
     * 
     * @param regexId 正则达主键
     * @return 正则达
     */
    @Override
    public SysRegex selectSysRegexByRegexId(Long regexId)
    {
        return sysRegexMapper.selectSysRegexByRegexId(regexId);
    }

    /**
     * 查询正则达列表
     * 
     * @param sysRegex 正则达
     * @return 正则达
     */
    @Override
    public List<SysRegex> selectSysRegexList(SysRegex sysRegex)
    {
        return sysRegexMapper.selectSysRegexList(sysRegex);
    }

    /**
     * 新增正则达
     * 
     * @param sysRegex 正则达
     * @return 结果
     */
    @Override
    public int insertSysRegex(SysRegex sysRegex)
    {
        sysRegex.setCreateTime(DateUtils.getNowDate());
        return sysRegexMapper.insertSysRegex(sysRegex);
    }

    /**
     * 修改正则达
     * 
     * @param sysRegex 正则达
     * @return 结果
     */
    @Override
    public int updateSysRegex(SysRegex sysRegex)
    {
        sysRegex.setUpdateTime(DateUtils.getNowDate());
        return sysRegexMapper.updateSysRegex(sysRegex);
    }

    /**
     * 批量删除正则达
     * 
     * @param regexIds 需要删除的正则达主键
     * @return 结果
     */
    @Override
    public int deleteSysRegexByRegexIds(Long[] regexIds)
    {
        return sysRegexMapper.deleteSysRegexByRegexIds(regexIds);
    }

    /**
     * 删除正则达信息
     * 
     * @param regexId 正则达主键
     * @return 结果
     */
    @Override
    public int deleteSysRegexByRegexId(Long regexId)
    {
        return sysRegexMapper.deleteSysRegexByRegexId(regexId);
    }
}
