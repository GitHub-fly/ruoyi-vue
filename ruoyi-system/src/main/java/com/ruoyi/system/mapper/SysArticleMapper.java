package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysArticle;

/**
 * 文章Mapper接口
 * 
 * @author ruoyi
 * @date 2023-02-28
 */
public interface SysArticleMapper 
{
    /**
     * 查询文章
     * 
     * @param articleId 文章主键
     * @return 文章
     */
    public SysArticle selectSysArticleByArticleId(Long articleId);

    /**
     * 查询文章列表
     * 
     * @param sysArticle 文章
     * @return 文章集合
     */
    public List<SysArticle> selectSysArticleList(SysArticle sysArticle);

    /**
     * 新增文章
     * 
     * @param sysArticle 文章
     * @return 结果
     */
    public int insertSysArticle(SysArticle sysArticle);

    /**
     * 修改文章
     * 
     * @param sysArticle 文章
     * @return 结果
     */
    public int updateSysArticle(SysArticle sysArticle);

    /**
     * 删除文章
     * 
     * @param articleId 文章主键
     * @return 结果
     */
    public int deleteSysArticleByArticleId(Long articleId);

    /**
     * 批量删除文章
     * 
     * @param articleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysArticleByArticleIds(Long[] articleIds);
}
