<template>
    <div>
        <div>
            <div class="crumbs">
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>
                        <i class="el-icon-lx-cascades"></i> 商品类型->参数列表
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <div class="container">
                <div class="handle-box">
                    <el-button
                            type="primary"
                            icon="el-icon-circle-plus"
                            class="handle-del mr10"
                            @click="addAttrCategory"
                    >类型新增
                    </el-button>
                    <el-input v-model="query.name" placeholder="类型名称" class="handle-input mr10"></el-input>
                    <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
                </div>
                <el-table
                        :data="tableData"
                        border
                        class="table"
                        ref="multipleTable"
                        header-cell-class-name="table-header">
                    <el-table-column prop="id" label="编号" width="55" align="center"></el-table-column>
                    <el-table-column prop="name" label="类型名称"></el-table-column>
                    <el-table-column prop="attributeCount" label="属性数量"></el-table-column>
                    <el-table-column prop="paramCount" label="参数数量"></el-table-column>
                    <el-table-column label="设置">
                        <el-button
                                size="mini"
                                icon="el-icon-setting"
                                @click="handleAttrs( scope.row)"
                        >属性设置
                        </el-button>
                        <el-button
                                size="mini"
                                icon="el-icon-setting"
                                @click="handleParams( scope.row)"
                        >参数设置
                        </el-button>
                    </el-table-column>
                    <el-table-column label="操作" width="180" align="center">
                        <template slot-scope="scope">
                            <el-button
                                    type="text"
                                    icon="el-icon-edit"
                                    @click="UpdateAttrCategory(scope.row)"
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
            <el-dialog :title="titleName" :visible.sync="editVisible" width="30%">
                <el-form ref="attrCategoryForm" :model="attrCategoryForm" label-width="70px">
                    <el-form-item label="类型名称">
                        <el-input v-model="attrCategoryForm.name"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveOrUpdateAttrCategory">确 定</el-button>
            </span>
            </el-dialog>
        </div>
    </div>
</template>

<script>
    import {fetchAttrCategoryData, getAttrCategoryById,saveOrUpdateAttrCategory} from "../../../api/pms/attrCategory";

    const defaultAttrCategoryForm = {
        name: ""
    }
    export default {
        name: "ParamList",
        data() {
            return {
                query: {
                    name: '',
                    size: 5,
                    current: 1
                },
                attrCategoryForm: Object.assign({}, defaultAttrCategoryForm),
                tableData: [],
                pageSizes: [5, 10, 15, 20],
                editVisible: false,
                pageTotal: 0,
                titleName: "",
                idx: -1,
                id: -1
            }
        },
        methods: {
            getData() {
                fetchAttrCategoryData(this.query).then(result => {
                    this.tableData = result.data.records;
                    this.pageTotal = result.data.total;
                })
            },
            //查询
            handleSearch() {
                this.$set(this.query, 'current', 1);
                this.getData();
            },
            //新增弹框
            addAttrCategory() {
                this.editVisible = true;
                this.titleName = "类型新增";
                this.attrCategoryForm = Object.assign({}, defaultAttrCategoryForm);
            },
            //修改弹框
            UpdateAttrCategory(row) {
                this.titleName = "类型修改";
                let id = row.id;
                getAttrCategoryById(id).then(result => {
                    this.attrCategoryForm = result.data;
                    this.editVisible = true;
                })
            },
            //新增+修改
            saveOrUpdateAttrCategory() {
                saveOrUpdateAttrCategory(this.attrCategoryForm).then(result=>{
                    if (result.code == 200){
                        this.$message.success("保存成功");
                        this.editVisible = false;
                        if (this.titleName === "类型新增"){
                            this.$set(this.query, 'current', 1);//新增返回第一页，修改不用
                        }
                        this.getData();
                    }else {
                        this.$message.error("保存失败");
                    }
                })
            },
            handleAttrs(row) {
            },
            handleParams(row) {
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
            }
        },
        mounted() {
            this.getData();
        }
    }
</script>

<style scoped>

</style>