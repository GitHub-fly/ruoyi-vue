package com.ruoyi.system;

import com.ruoyi.system.domain.SysArticle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author zhj
 * @Date 2023/3/1
 * @Version 1.0.0
 * @Description
 */
public class Test {

    public static void main(String[] args) throws Exception {
        new Test().testOne();
//       new Test().testTwo();

    }

    void test() throws Exception {
        String path = "/Users/xunmi/coding/上市公司年报";
        File dir = new File(path);
        File[] files = dir.listFiles();
        int i = 0;
        for (File file : files) {
            System.out.println(i);
            System.out.println(file);
            System.out.println(file.getName());
            String pdfText = getPdfText(file, true, 1, 10);
            SysArticle article = getCompanyData(pdfText);
            article.setFileName(getFileName(file));
            article.setArticleImage("/profile/upload/default.png");
//            sysArticleMapper.insertSysArticle(article);
            i++;
        }
    }

    void testOne() throws Exception {
        String fileName = "长运股份2001年年度报告-2002-04-03-563299";
        File file = new File("/Users/xunmi/coding/上市公司年报/" + fileName + ".PDF");
        String pdfText = getPdfText(file, true, 1, 10);
        System.out.println(pdfText);
        SysArticle article = getCompanyData(pdfText);
        article.setFileName(getFileName(file));
        article.setArticleImage("/profile/upload/default.png");
    }

    void testTwo() {
//        String regex = "(?<=其他有关资料 \\n).*(?<=(）\\x20\\n))";
        String regex = "(?<=(其他[相有]关资料 \\n名称 )).*?(?=\\n)";
        String text = " \n" +
                "七、其他有关资料 \n" +
                "名称 瑞华会计师事务所（特殊普通合伙） \n" +
                "公司聘请的会计师事务所 北京市东城区永定门西滨河路 8 号院 7 号楼中海\n" +
                "办公地址 \n" +
                "（境内） 地产广场西塔 5-11 层 \n" +
                "签字会计师姓名 李玉、问璐 \n" +
                "名    称 广发证券股份有限公司 \n" +
                "办公地址 广州市天河北路 183-187 号大都会广场 43 楼 \n" +
                "报告期内履行持续督导职责的\n" +
                "签字的保荐 \n" +
                "保荐机构 成燕  邵丰 ";
        Pattern compile = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = compile.matcher(text);
        String officeName = "";
        if (matcher.find()) {
            officeName = matcher.group().replaceAll(" ", "")
                    .replaceAll("\\n", "")
                    .replaceAll("名称", "");
            ;
        }
        System.out.println("事务所===========");
        System.out.println(officeName);
    }


    String getFileName(File file) {
        String name = file.getName();
        int pointIndex = name.lastIndexOf(".");
        return name.substring(0, pointIndex);
    }

    String getPdfText(File file, boolean sort, int startPage, int endPage) throws Exception {
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
            throw e;
        } finally {
            if (document != null) {
                document.close();
            }
        }
        return text;
    }

    SysArticle getCompanyData(String text) {

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
            System.out.println("法人1====================");
            legalName = matcher.group().replaceAll(" ", "");
        } else {
            regex = "(?<=(((法 定 代 表 人 )|(法 定 表 人 ))[:：])).*?(?=\\n)";
            compile = Pattern.compile(regex);
            matcher = compile.matcher(text);
            if (matcher.find()) {
                System.out.println("法人2====================");
                legalName = matcher.group().replaceAll(" ", "");
            } else {
                regex = "(?<=((法定代表人)|(法人代表)[:：\\x20]\\b?)).*?(?=\\n)";
                compile = Pattern.compile(regex);
                matcher = compile.matcher(text);
                if (matcher.find()) {
                    System.out.println("法人3====================");
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
            System.out.println("1");
            officeName = matcher.group().replaceAll(" ", "");
        } else {
            regex = "(?<=((事务所|事务所名称)[:：\\x20]\\b)).*?(?=\\n)";
            compile = Pattern.compile(regex);
            matcher = compile.matcher(text);
            if (matcher.find()) {
                System.out.println("2");
                officeName = matcher.group().replaceAll(" ", "");
            } else {
                regex = "(?<=其他有关资料 \\n).*(?<=[伙]）\\x20\\n)";
                compile = Pattern.compile(regex, Pattern.DOTALL);
                matcher = compile.matcher(text);
                if (matcher.find()) {
                    System.out.println("3");
                    officeName = matcher.group().replaceAll(" ", "")
                            .replaceAll("\\n", "")
                            .replaceAll("名称", "");
                } else {
                    regex = "(?<=公司聘请).*(?=为公司审计单位)";
                    compile = Pattern.compile(regex, Pattern.DOTALL);
                    matcher = compile.matcher(text);
                    if (matcher.find()) {
                        System.out.println("4");
                        officeName = matcher.group().replaceAll(" ", "")
                                .replaceAll("\\n", "");
                    } else {
                        regex = "(?<=事务所名称、办公地址：).*(?=\\x20{2}青岛市)";
                        compile = Pattern.compile(regex, Pattern.DOTALL);
                        matcher = compile.matcher(text);
                        if (matcher.find()) {
                            System.out.println("5");
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
}
