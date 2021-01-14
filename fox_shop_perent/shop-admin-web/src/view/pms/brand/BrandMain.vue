<template>
    <div>
        <div>
            <div class="crumbs">
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item>
                        <i class="el-icon-lx-cascades"></i> 品牌列表
                    </el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <div class="container">
                <div class="handle-box">
                    <el-button
                            type="primary"
                            icon="el-icon-circle-plus"
                            class="handle-del mr10"
                            @click="addBrand"
                    >品牌新增
                    </el-button>
                    <el-input v-model="query.name" placeholder="品牌名称" class="handle-input mr10"></el-input>
                    <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
                </div>
                <el-table
                        :data="tableData"
                        border
                        class="table"
                        ref="multipleTable"
                        header-cell-class-name="table-header"
                >
                    <el-table-column type="selection" width="55" align="center"></el-table-column>
                    <el-table-column prop="id" label="编号" width="55" align="center"></el-table-column>
                    <el-table-column prop="name" label="品牌名称" align="center"></el-table-column>
                    <el-table-column prop="firstLetter" label="首字母" align="center"></el-table-column>
                    <el-table-column prop="sort" label="排序" align="center"></el-table-column>
                    <el-table-column label="头像(查看大图)" align="center">
                        <template slot-scope="scope">
                            <el-image
                                    class="table-td-thumb"
                                    :src="scope.row.logo"
                                    :preview-src-list="[scope.row.logo]"
                            ></el-image>
                        </template>
                    </el-table-column>
                    <el-table-column label="品牌制造商" width="100" align="center">
                        <template slot-scope="scope">
                            <el-switch
                                    @change="handleFactoryStatusChange(scope.row)"
                                    :active-value="1"
                                    :inactive-value="0"
                                    v-model="scope.row.factoryStatus">
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column label="是否显示" width="100" align="center">
                        <template slot-scope="scope">
                            <el-switch
                                    @change="handleShowStatusChange(scope.row)"
                                    :active-value="1"
                                    :inactive-value="0"
                                    v-model="scope.row.showStatus">
                            </el-switch>
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
            <el-dialog :title="titleName" :visible.sync="editVisible" width="40%" v-if="editVisible">
                <el-form ref="brandForm" :model="brandForm" label-width="120px">
                    <el-form-item label="品牌名称" prop="name">
                        <el-input v-model="brandForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="品牌首字母" prop="firstLetter">
                        <el-input v-model="brandForm.firstLetter"></el-input>
                    </el-form-item>
                    <el-form-item label="品牌LOGO" prop="logo">
                        <single-upload v-model="brandForm.logo"></single-upload>
                    </el-form-item>
                    <el-form-item label="品牌专区大图" prop="bigPic">
                        <single-upload v-model="brandForm.bigPic"></single-upload>
                    </el-form-item>
                    <el-form-item label="品牌故事" prop="brandStory">
                        <el-input
                                type="textarea"
                                :rows="2"
                                placeholder="请输入品牌故事"
                                v-model="brandForm.brandStory">
                        </el-input>
                    </el-form-item>
                    <el-form-item label="排序" prop="sort">
                        <el-input v-model="brandForm.sort"></el-input>
                    </el-form-item>
                    <el-form-item label="是否显示" prop="showStatus">
                        <el-radio-group v-model="brandForm.showStatus">
                            <el-radio :label="1">是</el-radio>
                            <el-radio :label="0">否</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="品牌制造商" prop="factoryStatus">
                        <el-radio-group v-model="brandForm.factoryStatus">
                            <el-radio :label="1">是</el-radio>
                            <el-radio :label="0">否</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="closeDialog">取 消</el-button>
                <el-button type="primary" @click="saveBrand">确 定</el-button>
            </span>
            </el-dialog>
        </div>
    </div>
</template>

<script>
    import {fetchBrandData,saveBrand,updateFactoryStatus,updateShowStatus,getBrandById} from '../../../api/pms/brand'
    import SingleUpload from '@/components/SingleUpload'
    export default {
        name: "BrandMain",
        components: {SingleUpload},
        data() {
            return {
                query: {
                    name: '',
                    size: 5,
                    current: 1
                },
                tableData: [],
                pageSizes: [5, 10, 15, 20],
                editVisible: false,
                pageTotal: 0,
                titleName:"",
                brandForm: {
                    name: "",
                    firstLetter: "",
                    bigPic: "",
                    logo: "",
                    brandStory: "",
                    showStatus: 1,
                    factoryStatus: 1,
                    sort: null,
                },
                idx: -1,
                id: -1
            }
        },
        methods: {
            // 获取 easy-mock 的模拟数据
            getData() {
                fetchBrandData(this.query).then(res => {
                    this.tableData = res.data.records;
                    this.pageTotal = res.data.total;
                });
            },
            // 触发搜索按钮
            handleSearch() {
                this.$set(this.query, 'pageIndex', 1);
                this.getData();
            },
            //新增
            addBrand() {
                this.editVisible = true;
                this.titleName="品牌新增";
                this.refreshForm();
            },
            // 编辑操作
            handleEdit(row) {
                let id=row.id;
                getBrandById(id).then(result=>{
                    if(result.code === 200){
                        this.brandForm =result.data;
                        this.titleName="品牌修改";
                        this.editVisible = true;
                    }else{
                        this.$message.error("数据查询失败");
                    }
                })
            },
            //新增+修改
            saveBrand() {
                saveBrand(this.brandForm).then(result => {
                    if (result.code === 200) {
                        this.$message.success("保存品牌成功");
                        this.$set(this.query, 'current', 1);
                        this.refreshForm();
                        this.editVisible = false;
                        this.getData();
                    } else {
                        this.$message.error("保存品牌失败");
                    }
                })
            },
            // 分页导航
            handlePageChange(val) {
                this.$set(this.query, 'current', val);
                this.getData();
            },
            handleSizeChange(val) {
                this.$set(this.query, 'current', 1);
                this.$set(this.query, 'size', val);
                this.getData();
            },

            handleFactoryStatusChange(row) {
                let status = row.factoryStatus;
                let params = {
                    id: row.id,
                    factoryStatus:status
                }
                updateFactoryStatus(params).then(result=>{
                    if(result.code === 200){
                        this.$message.success("修改是否品牌制造商成功");
                    }else{
                        this.$message.error("修改是否品牌制造商失败");
                    }
                })
            },
            handleShowStatusChange(row) {
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
            //关闭dialog清空表单
            closeDialog() {
                this.refreshForm();
                this.editVisible = false;
            },
            refreshForm() {
                for(let key in this.brandForm){
                    this.brandForm[key]=""
                }
            }
        },
        mounted() {
            this.getData();
        }
    }
</script>

<style>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }

    .table {
        width: 100%;
        font-size: 14px;
    }

    .red {
        color: #ff0000;
    }

    .mr10 {
        margin-right: 10px;
    }

    .table-td-thumb {
        display: block;
        margin: auto;
        width: 40px;
        height: 40px;
    }

</style>
