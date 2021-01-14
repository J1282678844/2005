<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 商品分类
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button
                        type="primary"
                        icon="el-icon-circle-plus"
                        class="handle-del mr10"
                        @click="addCategroy"
                >新增分类
                </el-button>
                <el-button
                        type="primary"
                        icon="el-icon-delete"
                        class="handle-del mr10"
                        @click="delAllSelection"
                >批量删除
                </el-button>
                <el-input v-model="query.name" placeholder="分类名称" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
            </div>
            <el-table
                    :data="tableData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >

                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="id" label="编号" width="55" align="center"></el-table-column>
                <el-table-column prop="name" label="分类名称" align="center"></el-table-column>
                <el-table-column prop="level" label="级别" align="center">
                    <template slot-scope="scope">
                        <span v-if="scope.row.level == 0">一级</span>
                        <span v-else>二级</span>
                    </template>
                </el-table-column>

                <el-table-column label="分类图标" align="center">
                    <template slot-scope="scope">
                        <el-image v-if="scope.row.icon != null"
                                  class="table-td-thumb"
                                  :src="scope.row.icon"
                                  :preview-src-list="[scope.row.icon]"
                        ></el-image>
                    </template>
                </el-table-column>
                <el-table-column prop="productCount" label="商品数量" align="center"></el-table-column>
                <el-table-column prop="productUnit" label="数量单位" align="center"></el-table-column>
                <el-table-column label="导航栏" width="100" align="center">
                    <template slot-scope="scope">
                        <el-switch
                                @change="handleNavStatusChange(scope.$index, scope.row)"
                                :active-value="1"
                                :inactive-value="0"
                                v-model="scope.row.navStatus">
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="是否显示" width="100" align="center">
                    <template slot-scope="scope">
                        <el-switch
                                @change="handleShowStatusChange(scope.$index, scope.row)"
                                :active-value="1"
                                :inactive-value="0"
                                v-model="scope.row.showStatus">
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column prop="sort" label="排序"></el-table-column>
                <el-table-column label="设置" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                                v-if="scope.row.level == 0"
                                size="mini"
                                icon="el-icon-caret-bottom"
                                @click="handleChildren( scope.row)">查看下级
                        </el-button>
                        <el-button
                                size="mini"
                                v-else
                                icon="el-icon-caret-top"
                                @click="handleParent( scope.row)">返回上级
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                @click="handleEdit(scope.row)"
                        >编辑
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        background
                        layout="total, sizes, prev, pager, next, jumper"
                        :current-page="query.current"
                        :page-size="query.size"
                        :page-sizes="pageSizes"
                        :total="pageTotal"
                        @current-change="handlePageChange"
                        @size-change="handleSizeChange"
                ></el-pagination>
            </div>
        </div>
        <!-- 编辑弹出框 -->
        <el-dialog :title="titleName" :visible.sync="editVisible" width="40%">
            <el-form ref="categoryForm" :model="categoryForm" label-width="120px">
                <el-form-item label="分类名称" prop="name">
                    <el-input v-model="categoryForm.name"></el-input>
                </el-form-item>
                <el-form-item label="上级分类" prop="parentId">
                    <el-select v-model="categoryForm.parentId" placeholder="请选择上级分类">
                        <el-option label="无上级分类" :value="0"></el-option>
                        <el-option v-for="category in parentCategoryList"
                                   :label="category.name"
                                   :key="category.id"
                                   :value="category.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="数量单位" prop="productUnit">
                    <el-input v-model="categoryForm.productUnit"></el-input>
                </el-form-item>
                <el-form-item label="排序" prop="sort">
                    <el-input v-model="categoryForm.sort"></el-input>
                </el-form-item>
                <el-form-item label="是否显示" prop="showStatus">
                    <el-radio-group v-model="categoryForm.showStatus">
                        <el-radio :label="1">是</el-radio>
                        <el-radio :label="0">否</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="显示在导航栏" prop="navStatus">
                    <el-radio-group v-model="categoryForm.navStatus">
                        <el-radio :label="1">是</el-radio>
                        <el-radio :label="0">否</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="分类图标" prop="icon">
                    <single-upload v-model="categoryForm.icon"></single-upload>
                </el-form-item>
                <el-form-item label="关键词" prop="keywords">
                    <el-input v-model="categoryForm.keywords"></el-input>
                </el-form-item>
                <el-form-item label="分类描述" prop="description">
                    <el-input
                            type="textarea"
                            :rows="2"
                            placeholder="请输入分类描述"
                            v-model="categoryForm.description">
                    </el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import {
        fetchCategoryData,
        fetchParentCategoryData,
        saveOrUpdateCategroy,
        queryCategoryById,
        updateNavStatus,
        updateShowStatus
    } from "../../../api/pms/category";
    import SingleUpload from '@/components/SingleUpload'

    export default {
        name: "CategoryMain",
        components: {SingleUpload},
        data() {
            return {
                query: {
                    name: '',
                    size: 5,
                    current: 1,
                    level: 0,
                    pid: null
                },
                categoryForm: {
                    name: "",
                    parentId: 0,
                    icon: "",
                    showStatus: 1,
                    navStatus: 1,
                    keywords: "",
                    description: ""
                },
                titleName: "",
                tableData: [],
                multipleSelection: [],
                parentCategoryList: [],
                delList: [],
                pageSizes: [5, 10, 15, 20],
                editVisible: false,
                pageTotal: 0,
                form: {},
                idx: -1,
                id: -1
            }
        },
        methods: {
            // 获取 easy-mock 的模拟数据
            getData() {
                fetchCategoryData(this.query).then(res => {
                    this.tableData = res.data.records;
                    this.pageTotal = res.data.total;
                });
            },
            //查询
            handleSearch() {
                this.$set(this.query, 'current', 1);
                this.getData();
            },
            //查看下级
            handleChildren(row) {
                this.$set(this.query, 'pid', row.id);
                this.$set(this.query, 'level', null);
                this.$set(this.query, 'current', 1);
                this.getData();
            },
            //返回上级
            handleParent(row) {
                this.$set(this.query, 'pid', null);
                this.$set(this.query, 'level', 0);
                this.$set(this.query, 'current', 1);
                this.getData();
            },
            //上下页
            handlePageChange(val) {
                this.$set(this.query, 'current', val);
                this.getData();
            },
            //条数显示
            handleSizeChange(val) {
                this.$set(this.query, 'current', 1);
                this.$set(this.query, 'size', val);
                this.getData();
            },
            //新增弹框
            addCategroy() {
                this.titleName = "新增商品分类";
                this.refreshForm();
                this.queryParentCategoryList();
                this.editVisible = true;
            },
            //查询上下级分类
            queryParentCategoryList() {
                fetchParentCategoryData().then(result => {
                    this.parentCategoryList = result.data;
                })
            },
            //修改
            handleEdit(row) {
                this.titleName = "修改商品分类";
                this.editVisible = true;
                queryCategoryById(row.id).then(result => {
                    if (result.code == 200) {
                        this.categoryForm = result.data;
                        this.queryParentCategoryList();
                    }
                })
            },
            //新增+修改
            saveEdit() {
                saveOrUpdateCategroy(this.categoryForm).then(result => {
                    if (result.code == 200) {
                        this.$message.success("保存成功");
                        this.editVisible = false;
                        if (this.titleName === "新增商品分类"){
                            this.$set(this.query, 'current', 1);
                            this.$set(this.query, 'pid', null);
                            this.$set(this.query, 'level', 0);
                        }
                        this.getData();
                    } else {
                        this.$message.error("保存失败");
                    }
                })
            },
            handleNavStatusChange(row){
                let status = row.navStatus;
                let params = {
                    id: row.id,
                    navStatus:status
                }
                updateNavStatus(params).then(result=>{
                    if(result.code === 200){
                        this.$message.success("修改是否在导航栏显示成功");
                    }else{
                        this.$message.error("修改是否在导航栏显示失败");
                    }

                })
            },
            handleNavStatusChange(row){
                let status = row.showStatus;
                let params = {
                    id: row.id,
                    showStatus:status
                }
                updateShowStatus(params).then(result=>{
                    if(result.code === 200){
                        this.$message.success("修改是否显示成功");
                    }else{
                        this.$message.error("修改是否显示失败");
                    }

                })
            },
            refreshForm() {
                if (this.$refs.categoryForm !== undefined) {
                    this.$refs.categoryForm.resetFields();
                }
            }


        },
        mounted() {
            this.getData();
        }
    }
</script>

<style>

</style>
