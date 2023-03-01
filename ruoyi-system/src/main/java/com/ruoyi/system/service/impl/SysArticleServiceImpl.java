package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysArticle;
import com.ruoyi.system.domain.SysRegex;
import com.ruoyi.system.mapper.SysArticleMapper;
import com.ruoyi.system.mapper.SysRegexMapper;
import com.ruoyi.system.service.ISysArticleService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文章Service业务层处理
 *
 * @author ruoyi
 * @date 2023-02-28
 */
@Service
public class SysArticleServiceImpl implements ISysArticleService {

    @Autowired
    private SysArticleMapper sysArticleMapper;
    @Autowired
    private SysRegexMapper sysRegexMapper;

    @Override
    public void parsePDFData() throws Exception {
        // 读取正则表达式
        List<SysRegex> companyNameRegexList = sysRegexMapper.selectSysRegexList(SysRegex.builder()
                .regexTarget(0L)
                .build());

        List<SysRegex> companyLegalNameRegexList = sysRegexMapper.selectSysRegexList(SysRegex.builder()
                .regexTarget(1L)
                .build());

        List<SysRegex> companyOfficeNameRegexList = sysRegexMapper.selectSysRegexList(SysRegex.builder()
                .regexTarget(2L)
                .build());

        String path = "/Users/xunmi/coding/上市公司年报";
        File dir = new File(path);
        File[] files = dir.listFiles();
        assert files != null;
        for (File file : files) {
            String pdfText = getPdfText(file, true, 1, 10);
            SysArticle article = SysArticle.builder()
                    .articleImage("/profile/upload/default.png")
                    .fileName(getFileName(file))
                    .companyName(getTargetData(pdfText, companyNameRegexList, false))
                    .companyLegalPeople(getTargetData(pdfText, companyLegalNameRegexList, false))
                    .companyOfficeName(getTargetData(pdfText, companyOfficeNameRegexList, true)
                            .replaceAll("\\n", "")
                            .replaceAll("名称", ""))
                    .build();
            sysArticleMapper.insertSysArticle(article);
        }
    }

    /**
     * 获取指定文件的文件名（去除文件后缀）
     * @param file 指定文件
     * @return 文件名
     */
    protected String getFileName(File file) {
        String name = file.getName();
        int pointIndex = name.lastIndexOf(".");
        return name.substring(0, pointIndex);
    }

    /**
     * 通过 PDFBox 工具解析指定 PDF 文件的内容
     * @param file 指定 PDF 文件对象
     * @param sort 排序
     * @param startPage 开始页数
     * @param endPage 结束页数
     * @return 返回文件内容字符串
     * @throws Exception 异常
     */
    protected String getPdfText(File file, boolean sort, int startPage, int endPage) throws Exception {
        PDDocument document = null;
        String text = "";
        try {
            document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(sort);
            stripper.setStartPage(startPage);
            stripper.setEndPage(endPage);
            text = stripper.getText(document);
        } catch (Exception e) {
            System.out.println(e);
            throw new Exception();
        } finally {
            if (document != null) {
                document.close();
            }
        }
        return text;
    }


    /**
     * 通过指定的"正则表达式"解析获取指定字段的内容
     * @param textContent 被解析文本字符串
     * @param regexList 指定的正则表达式
     * @param multipleLine 是否开启正则表达式多行匹配
     * @return 目标字段数据
     */
    protected String getTargetData(String textContent, List<SysRegex> regexList, boolean multipleLine) {

        String targetData = "";
        Pattern compile = null;
        Matcher matcher = null;
        for (SysRegex sysRegex : regexList) {
            if (multipleLine) {
                compile = Pattern.compile(sysRegex.getRegex(), Pattern.DOTALL);
            } else {
                compile = Pattern.compile(sysRegex.getRegex());
            }
            matcher = compile.matcher(textContent);
            if (matcher.find()) {
                targetData = matcher.group().replaceAll(" ", "");
                return targetData;
            }
        }
        return targetData;
    }

    /**
     * 查询文章
     *
     * @param articleId 文章主键
     * @return 文章
     */
    @Override
    public SysArticle selectSysArticleByArticleId(Long articleId) {
        return sysArticleMapper.selectSysArticleByArticleId(articleId);
    }

    /**
     * 查询文章列表
     *
     * @param sysArticle 文章
     * @return 文章
     */
    @Override
    public List<SysArticle> selectSysArticleList(SysArticle sysArticle) {
        return sysArticleMapper.selectSysArticleList(sysArticle);
    }

    /**
     * 新增文章
     *
     * @param sysArticle 文章
     * @return 结果
     */
    @Override
    public int insertSysArticle(SysArticle sysArticle) {
        if (sysArticle.getArticleImage() == null) {
            // 上传文件路径
            sysArticle.setArticleImage("/profile/upload/default.png");
        }
        Date nowDate = DateUtils.getNowDate();
        sysArticle.setPublishTime(nowDate);
        sysArticle.setCreateTime(nowDate);
        return sysArticleMapper.insertSysArticle(sysArticle);
    }

    /**
     * 修改文章
     *
     * @param sysArticle 文章
     * @return 结果
     */
    @Override
    public int updateSysArticle(SysArticle sysArticle) {
        sysArticle.setUpdateTime(DateUtils.getNowDate());
        return sysArticleMapper.updateSysArticle(sysArticle);
    }

    /**
     * 批量删除文章
     *
     * @param articleIds 需要删除的文章主键
     * @return 结果
     */
    @Override
    public int deleteSysArticleByArticleIds(Long[] articleIds) {
        return sysArticleMapper.deleteSysArticleByArticleIds(articleIds);
    }

    /**
     * 删除文章信息
     *
     * @param articleId 文章主键
     * @return 结果
     */
    @Override
    public int deleteSysArticleByArticleId(Long articleId) {
        return sysArticleMapper.deleteSysArticleByArticleId(articleId);
    }
}
