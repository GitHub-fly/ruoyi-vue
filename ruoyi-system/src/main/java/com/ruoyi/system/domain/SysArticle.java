package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 文章对象 sys_article
 * 
 * @author ruoyi
 * @date 2023-02-28
 */
public class SysArticle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文章id */
    private Long articleId;

    /** 文章标题 */
    @Excel(name = "文章标题")
    private String articleTitle;

    /** 文章内容 */
    @Excel(name = "文章内容")
    private String articleContent;

    /** 文章作者 */
    @Excel(name = "文章作者")
    private String articleAuthor;

    /** 文章封面 */
    @Excel(name = "文章封面")
    private String articleImage;

    /** 发表时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发表时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publishTime;

    /** 删除标志（0代表存在 2代表删除） */
    private Integer delFlag;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String companyName;

    /** 公司法定代表人 */
    @Excel(name = "公司法定代表人")
    private String companyLegalPeople;

    /** 事务所名称 */
    @Excel(name = "事务所名称")
    private String companyOfficeName;

    public void setArticleId(Long articleId) 
    {
        this.articleId = articleId;
    }

    public Long getArticleId() 
    {
        return articleId;
    }
    public void setArticleTitle(String articleTitle) 
    {
        this.articleTitle = articleTitle;
    }

    public String getArticleTitle() 
    {
        return articleTitle;
    }
    public void setArticleContent(String articleContent) 
    {
        this.articleContent = articleContent;
    }

    public String getArticleContent() 
    {
        return articleContent;
    }
    public void setArticleAuthor(String articleAuthor) 
    {
        this.articleAuthor = articleAuthor;
    }

    public String getArticleAuthor() 
    {
        return articleAuthor;
    }
    public void setArticleImage(String articleImage) 
    {
        this.articleImage = articleImage;
    }

    public String getArticleImage() 
    {
        return articleImage;
    }
    public void setPublishTime(Date publishTime) 
    {
        this.publishTime = publishTime;
    }

    public Date getPublishTime() 
    {
        return publishTime;
    }
    public void setDelFlag(Integer delFlag) 
    {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag() 
    {
        return delFlag;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setCompanyName(String companyName) 
    {
        this.companyName = companyName;
    }

    public String getCompanyName() 
    {
        return companyName;
    }
    public void setCompanyLegalPeople(String companyLegalPeople) 
    {
        this.companyLegalPeople = companyLegalPeople;
    }

    public String getCompanyLegalPeople() 
    {
        return companyLegalPeople;
    }
    public void setCompanyOfficeName(String companyOfficeName) 
    {
        this.companyOfficeName = companyOfficeName;
    }

    public String getCompanyOfficeName() 
    {
        return companyOfficeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("articleId", getArticleId())
            .append("articleTitle", getArticleTitle())
            .append("articleContent", getArticleContent())
            .append("articleAuthor", getArticleAuthor())
            .append("articleImage", getArticleImage())
            .append("publishTime", getPublishTime())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("fileName", getFileName())
            .append("companyName", getCompanyName())
            .append("companyLegalPeople", getCompanyLegalPeople())
            .append("companyOfficeName", getCompanyOfficeName())
            .toString();
    }
}
