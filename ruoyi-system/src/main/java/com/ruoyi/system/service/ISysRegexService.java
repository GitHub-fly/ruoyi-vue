package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysRegex;

/**
 * 正则达Service接口
 * 
 * @author ruoyi
 * @date 2023-03-01
 */
public interface ISysRegexService 
{
    /**
     * 查询正则达
     * 
     * @param regexId 正则达主键
     * @return 正则达
     */
    public SysRegex selectSysRegexByRegexId(Long regexId);

    /**
     * 查询正则达列表
     * 
     * @param sysRegex 正则达
     * @return 正则达集合
     */
    public List<SysRegex> selectSysRegexList(SysRegex sysRegex);

    /**
     * 新增正则达
     * 
     * @param sysRegex 正则达
     * @return 结果
     */
    public int insertSysRegex(SysRegex sysRegex);

    /**
     * 修改正则达
     * 
     * @param sysRegex 正则达
     * @return 结果
     */
    public int updateSysRegex(SysRegex sysRegex);

    /**
     * 批量删除正则达
     * 
     * @param regexIds 需要删除的正则达主键集合
     * @return 结果
     */
    public int deleteSysRegexByRegexIds(Long[] regexIds);

    /**
     * 删除正则达信息
     * 
     * @param regexId 正则达主键
     * @return 结果
     */
    public int deleteSysRegexByRegexId(Long regexId);
}
