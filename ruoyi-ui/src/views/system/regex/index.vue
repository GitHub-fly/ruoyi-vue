<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="正则的应用对象" prop="regexTarget">
        <el-input v-model="queryParams.regexTarget" placeholder="请输入正则的应用对象" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="正则表达式" prop="regex">
        <el-input v-model="queryParams.regex" placeholder="请输入正则表达式" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="正则优先级最高级为1" prop="regexOrder">
        <el-input v-model="queryParams.regexOrder" placeholder="请输入正则优先级最高级为1" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="删除标志" prop="delFlg">
        <el-input v-model="queryParams.delFlg" placeholder="请输入删除标志" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:regex:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['system:regex:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:regex:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:regex:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="regexList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="正则数据id" width="100" align="center" prop="regexId" />
      <el-table-column label="正则的应用对象" align="center" prop="regexTarget" />
      <el-table-column label="正则表达式" width="350" align="center" prop="regex" />
      <el-table-column label="正则优先级最高级为1" align="center" prop="regexOrder" />
      <el-table-column label="删除标志" align="center" prop="delFlg" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:regex:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:regex:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改正则达对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="正则的应用对象" prop="regexTarget">
          <el-input v-model="form.regexTarget" placeholder="请输入正则的应用对象" />
        </el-form-item>
        <el-form-item label="正则表达式" prop="regex">
          <el-input v-model="form.regex" placeholder="请输入正则表达式" />
        </el-form-item>
        <el-form-item label="正则优先级最高级为1" prop="regexOrder">
          <el-input v-model="form.regexOrder" placeholder="请输入正则优先级最高级为1" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRegex, getRegex, delRegex, addRegex, updateRegex } from "@/api/system/regex"

export default {
  name: "Regex",
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
      // 正则达表格数据
      regexList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        regexTarget: null,
        regex: null,
        regexOrder: null,
        delFlg: null,
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
    /** 查询正则达列表 */
    getList() {
      this.loading = true
      listRegex(this.queryParams).then(response => {
        this.regexList = response.rows
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
        regexId: null,
        regexTarget: null,
        regex: null,
        regexOrder: null,
        delFlg: null,
        createTime: null,
        updateTime: null
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
      this.ids = selection.map(item => item.regexId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加正则达"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const regexId = row.regexId || this.ids
      getRegex(regexId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改正则达"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.regexId != null) {
            updateRegex(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRegex(this.form).then(response => {
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
      const regexIds = row.regexId || this.ids
      this.$modal.confirm('是否确认删除正则达编号为"' + regexIds + '"的数据项？').then(function () {
        return delRegex(regexIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => { })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/regex/export', {
        ...this.queryParams
      }, `regex_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
