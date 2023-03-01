package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysRegex;
import com.ruoyi.system.service.ISysRegexService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 正则达Controller
 * 
 * @author ruoyi
 * @date 2023-03-01
 */
@RestController
@RequestMapping("/system/regex")
public class SysRegexController extends BaseController
{
    @Autowired
    private ISysRegexService sysRegexService;

    /**
     * 查询正则达列表
     */
    @PreAuthorize("@ss.hasPermi('system:regex:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysRegex sysRegex)
    {
        startPage();
        List<SysRegex> list = sysRegexService.selectSysRegexList(sysRegex);
        return getDataTable(list);
    }

    /**
     * 导出正则达列表
     */
    @PreAuthorize("@ss.hasPermi('system:regex:export')")
    @Log(title = "正则达", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRegex sysRegex)
    {
        List<SysRegex> list = sysRegexService.selectSysRegexList(sysRegex);
        ExcelUtil<SysRegex> util = new ExcelUtil<SysRegex>(SysRegex.class);
        util.exportExcel(response, list, "正则达数据");
    }

    /**
     * 获取正则达详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:regex:query')")
    @GetMapping(value = "/{regexId}")
    public AjaxResult getInfo(@PathVariable("regexId") Long regexId)
    {
        return success(sysRegexService.selectSysRegexByRegexId(regexId));
    }

    /**
     * 新增正则达
     */
    @PreAuthorize("@ss.hasPermi('system:regex:add')")
    @Log(title = "正则达", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysRegex sysRegex)
    {
        return toAjax(sysRegexService.insertSysRegex(sysRegex));
    }

    /**
     * 修改正则达
     */
    @PreAuthorize("@ss.hasPermi('system:regex:edit')")
    @Log(title = "正则达", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysRegex sysRegex)
    {
        return toAjax(sysRegexService.updateSysRegex(sysRegex));
    }

    /**
     * 删除正则达
     */
    @PreAuthorize("@ss.hasPermi('system:regex:remove')")
    @Log(title = "正则达", businessType = BusinessType.DELETE)
	@DeleteMapping("/{regexIds}")
    public AjaxResult remove(@PathVariable Long[] regexIds)
    {
        return toAjax(sysRegexService.deleteSysRegexByRegexIds(regexIds));
    }
}
