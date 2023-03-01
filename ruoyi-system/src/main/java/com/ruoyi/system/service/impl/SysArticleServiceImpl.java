package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysArticle;
import com.ruoyi.system.mapper.SysArticleMapper;
import com.ruoyi.system.service.ISysArticleService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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

    @Override
    public void parsePDFData() throws Exception {
        String path = "/Users/xunmi/coding/上市公司年报";
        File dir = new File(path);
        File[] files = dir.listFiles();
        int i = 0;
        for (File file : files) {
            String pdfText = getPdfText(file, true, 1, 10);
            SysArticle article = getCompanyData(pdfText);
            article.setFileName(getFileName(file));
            article.setArticleImage("/profile/upload/default.png");
            System.out.println(article);
            sysArticleMapper.insertSysArticle(article);
        }
    }

    protected String getFileName(File file) {
        String name = file.getName();
        int pointIndex = name.lastIndexOf(".");
        return name.substring(0, pointIndex);
    }

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

    protected SysArticle getCompanyData(String text) {
        SysArticle article = new SysArticle();
        String regex = "";
        Pattern compile = null;
        Matcher matcher = null;
        /*
         匹配公司名
         (^\b)? : 以零个或一个空格、换行开头
         [\u4E00-\u9FA5]+ : 一个或多个汉字
         (有限公司) ：包含"有限公司"字符串
         \b?    : 零个或一个空格、换行符等
         */
        regex = "(^\b)?[\\u4E00-\\u9FA5]+(\\()?.+(\\))?(有限公司)\b?";
        compile = Pattern.compile(regex);
        matcher = compile.matcher(text);
        String companyName = "";
        if (matcher.find()) {
            companyName = matcher.group().replaceAll(" ", "");
        }
        System.out.println("公司名==========");
        System.out.println(companyName);

        regex = "(?<=(法定代表人[:：\\x20]\\b?)).*?(?=\\n)";
        compile = Pattern.compile(regex);
        matcher = compile.matcher(text);
        String legalName = "";
        if (matcher.find()) {
            legalName = matcher.group().replaceAll(" ", "");
        } else {
            regex = "(?<=(((法 定 代 表 人 )|(法 定 表 人 ))[:：])).*?(?=\\n)";
            compile = Pattern.compile(regex);
            matcher = compile.matcher(text);
            if (matcher.find()) {
                legalName = matcher.group().replaceAll(" ", "");
            } else {
                regex = "(?<=((法定代表人)|(法人代表)[:：\\x20]\\b?)).*?(?=\\n)";
                compile = Pattern.compile(regex);
                matcher = compile.matcher(text);
                if (matcher.find()) {
                    legalName = matcher.group().replaceAll(" ", "");
                }
            }
        }
        System.out.println("法定人===========");
        System.out.println(legalName);

        regex = "(?<=(其他[相有]关资料 \\n名称 )).*?(?=\\n)";
        compile = Pattern.compile(regex);
        matcher = compile.matcher(text);
        String officeName = "";
        if (matcher.find()) {
            officeName = matcher.group().replaceAll(" ", "");
        } else {
            regex = "(?<=((事务所|事务所名称)[:：\\x20]\\b)).*?(?=\\n)";
            compile = Pattern.compile(regex);
            matcher = compile.matcher(text);
            if (matcher.find()) {
                officeName = matcher.group().replaceAll(" ", "");
            } else {
                regex = "(?<=其他有关资料 \\n).*(?<=[伙]）\\x20\\n)";
                ;
                compile = Pattern.compile(regex, Pattern.DOTALL);
                matcher = compile.matcher(text);
                if (matcher.find()) {
                    officeName = matcher.group().replaceAll(" ", "")
                            .replaceAll("\\n", "")
                            .replaceAll("名称", "");
                } else {
                    regex = "(?<=公司聘请).*(?=为公司审计单位)";
                    compile = Pattern.compile(regex, Pattern.DOTALL);
                    matcher = compile.matcher(text);
                    if (matcher.find()) {
                        officeName = matcher.group().replaceAll(" ", "")
                                .replaceAll("\\n", "")
                                .replaceAll("名称", "");
                    } else {
                        regex = "(?<=事务所名称、办公地址：).*(?=\\x20{2}青岛市)";
                        compile = Pattern.compile(regex, Pattern.DOTALL);
                        matcher = compile.matcher(text);
                        if (matcher.find()) {
                            officeName = matcher.group().replaceAll(" ", "")
                                    .replaceAll("\\n", "");
                        }
                    }
                }

            }
        }

        System.out.println("事务所===========");
        System.out.println(officeName);
        article.setCompanyName(companyName);
        article.setCompanyLegalPeople(legalName);
        article.setCompanyOfficeName(officeName);
        return article;
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
