<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文章标题" prop="articleTitle">
        <el-input v-model="queryParams.articleTitle" placeholder="请输入文章标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="文章作者" prop="articleAuthor">
        <el-input v-model="queryParams.articleAuthor" placeholder="请输入文章作者" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="发表时间" prop="publishTime">
        <el-date-picker clearable v-model="queryParams.publishTime" type="date" value-format="yyyy-MM-dd"
          placeholder="请选择发表时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="文件名称" prop="fileName">
        <el-input v-model="queryParams.fileName" placeholder="请输入文件名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="公司名称" prop="companyName">
        <el-input v-model="queryParams.companyName" placeholder="请输入公司名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="公司法定代表人" prop="companyLegalPeople">
        <el-input v-model="queryParams.companyLegalPeople" placeholder="请输入公司法定代表人" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="事务所名称" prop="companyOfficeName">
        <el-input v-model="queryParams.companyOfficeName" placeholder="请输入事务所名称" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:article:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['system:article:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:article:remove']">删除</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport"
          v-hasPermi="['system:article:import']">自动导入PDF文件</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:article:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文章编号" align="center" prop="articleId" />
      <el-table-column label="文章标题" align="center" prop="articleTitle" />
      <el-table-column label="文章内容" align="center" prop="articleContent">
        <template slot-scope="scope">
          <p v-html="scope.row.articleContent" />
        </template>
      </el-table-column>

      <el-table-column label="文章作者" align="center" prop="articleAuthor" />
      <el-table-column label="文章封面" align="center" prop="articleImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.articleImage" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column label="发表时间" align="center" prop="publishTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.publishTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="文件名称" align="center" prop="fileName" />
      <el-table-column label="公司名称" align="center" prop="companyName" />
      <el-table-column label="公司法定代表人" align="center" prop="companyLegalPeople" />
      <el-table-column label="事务所名称" align="center" prop="companyOfficeName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:article:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:article:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改文章对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文章标题" prop="articleTitle">
          <el-input v-model="form.articleTitle" placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="文章内容">
          <editor v-model="form.articleContent" :min-height="192" />
        </el-form-item>
        <el-form-item label="文章作者" prop="articleAuthor">
          <el-input v-model="form.articleAuthor" placeholder="请输入文章作者" />
        </el-form-item>
        <el-form-item label="文章封面" prop="articleImage">
          <image-upload :limit="1" v-model="form.articleImage" />
        </el-form-item>
        <el-form-item label="文件名称" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入文件名称" />
        </el-form-item>
        <el-form-item label="公司名称" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入公司名称" />
        </el-form-item>
        <el-form-item label="公司法定代表人" prop="companyLegalPeople">
          <el-input v-model="form.companyLegalPeople" placeholder="请输入公司法定代表人" />
        </el-form-item>
        <el-form-item label="事务所名称" prop="companyOfficeName">
          <el-input v-model="form.companyOfficeName" placeholder="请输入事务所名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入 pdf 对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="120" multiple accept=".pdf" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入 pdf 格式文件。</span>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listArticle, getArticle, delArticle, addArticle, updateArticle, importData } from "@/api/system/article"
import { getToken } from "@/utils/auth"
export default {
  name: "Article",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 文章表格数据
      articleList: [],
      // 弹出层标题
      title: "",
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/article/importData"
      },
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        articleTitle: null,
        articleContent: null,
        articleAuthor: null,
        articleImage: null,
        publishTime: null,
        fileName: null,
        companyName: null,
        companyLegalPeople: null,
        companyOfficeName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询文章列表 */
    getList() {
      this.loading = true
      listArticle(this.queryParams).then(response => {
        this.articleList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        articleId: null,
        articleTitle: null,
        articleContent: null,
        articleAuthor: null,
        articleImage: null,
        publishTime: null,
        delFlag: null,
        createTime: null,
        updateTime: null,
        fileName: null,
        companyName: null,
        companyLegalPeople: null,
        companyOfficeName: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.articleId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加文章"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const articleId = row.articleId || this.ids
      getArticle(articleId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改文章"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.articleId != null) {
            updateArticle(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addArticle(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const articleIds = row.articleId || this.ids
      this.$modal.confirm('是否确认删除文章编号为"' + articleIds + '"的数据项？').then(function () {
        return delArticle(articleIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => { })
    },

    /** 导入按钮操作 */
    handleImport() {
      importData().then(() => {
        this.getList()
      })
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('system/article/export', {
        ...this.queryParams
      }, `article_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
      this.getList()
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit()
    }
  }
}
</script>
