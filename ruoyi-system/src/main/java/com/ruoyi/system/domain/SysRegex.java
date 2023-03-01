package com.ruoyi.system.domain;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 正则达对象 sys_regex
 * 
 * @author ruoyi
 * @date 2023-03-01
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRegex extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 正则数据id */
    private Long regexId;

    /** 正则的应用对象（0=公司名1=法人2=事务所） */
    @Excel(name = "正则的应用对象", readConverterExp = "0==公司名1=法人2=事务所")
    private Long regexTarget;

    /** 正则表达式 */
    @Excel(name = "正则表达式")
    private String regex;

    /** 正则优先级最高级为1 */
    @Excel(name = "正则优先级最高级为1")
    private Long regexOrder;

    /** 删除标志（0代表存在 2代表删除） */
    @Excel(name = "删除标志", readConverterExp = "0=代表存在,2=代表删除")
    private Long delFlg;

    public void setRegexId(Long regexId) 
    {
        this.regexId = regexId;
    }

    public Long getRegexId() 
    {
        return regexId;
    }
    public void setRegexTarget(Long regexTarget) 
    {
        this.regexTarget = regexTarget;
    }

    public Long getRegexTarget() 
    {
        return regexTarget;
    }
    public void setRegex(String regex) 
    {
        this.regex = regex;
    }

    public String getRegex() 
    {
        return regex;
    }
    public void setRegexOrder(Long regexOrder) 
    {
        this.regexOrder = regexOrder;
    }

    public Long getRegexOrder() 
    {
        return regexOrder;
    }
    public void setDelFlg(Long delFlg) 
    {
        this.delFlg = delFlg;
    }

    public Long getDelFlg() 
    {
        return delFlg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("regexId", getRegexId())
            .append("regexTarget", getRegexTarget())
            .append("regex", getRegex())
            .append("regexOrder", getRegexOrder())
            .append("delFlg", getDelFlg())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
