<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 角色列表
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="app-container">
            <el-card class="filter-container" shadow="never">
                <div>
                    <i class="el-icon-search"></i>
                    <span>筛选搜索</span>
                    <el-button
                            style="float:right"
                            type="primary"
                            @click="handleSearchList()"
                            size="small">
                        查询搜索
                    </el-button>
                    <el-button
                            style="float:right;margin-right: 15px"
                            @click="handleResetSearch()"
                            size="small">
                        重置
                    </el-button>
                </div>
                <div style="margin-top: 15px">
                    <el-form :inline="true" :model="query" size="small" label-width="140px">
                        <el-form-item label="输入搜索：">
                            <el-input v-model="query.name" class="input-width" placeholder="角色名称" clearable></el-input>
                        </el-form-item>
                    </el-form>
                </div>
            </el-card>
            <el-card class="operate-container" shadow="never">
                <i class="el-icon-tickets"></i>
                <span>数据列表</span>
                <el-button size="mini" class="btn-add" @click="handleAdd()" style="margin-left: 20px">添加</el-button>
            </el-card>
            <div class="table-container">
                <el-table ref="roleTable"
                          :data="tableData"
                          style="width: 100%;"
                          v-loading="listLoading" border>
                    <el-table-column label="编号" width="100" align="center">
                        <template slot-scope="scope">{{scope.row.id}}</template>
                    </el-table-column>
                    <el-table-column label="角色名称" align="center">
                        <template slot-scope="scope">{{scope.row.name}}</template>
                    </el-table-column>
                    <el-table-column label="描述" align="center">
                        <template slot-scope="scope">{{scope.row.description}}</template>
                    </el-table-column>
                    <el-table-column label="用户数" width="100" align="center">
                        <template slot-scope="scope">{{scope.row.adminCount}}</template>
                    </el-table-column>
                    <el-table-column label="添加时间" width="160" align="center">
                        <template slot-scope="scope">{{scope.row.createTime | formatDateTime}}</template>
                    </el-table-column>
                    <el-table-column label="是否启用" width="140" align="center">
                        <template slot-scope="scope">
                            <el-switch
                                    @change="handleStatusChange(scope.row)"
                                    :active-value="1"
                                    :inactive-value="0"
                                    v-model="scope.row.status">
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="160" align="center">
                        <template slot-scope="scope">
                            <el-row>
                                <el-button size="mini"
                                           type="text"
                                           @click="handleSelectMenu(scope.row)">分配菜单
                                </el-button>
                                <el-button size="mini"
                                           type="text"
                                           @click="handleSelectResource(scope.row)">分配资源
                                </el-button>
                            </el-row>
                            <el-row>
                                <el-button size="mini"
                                           type="text"
                                           @click="handleUpdate(scope.row)">编辑
                                </el-button>
                                <el-button size="mini"
                                           type="text"
                                           @click="handleDelete(scope.row)">删除
                                </el-button>
                            </el-row>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pagination-container">
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
        </div>
        <!-- 编辑弹出框 -->
        <el-dialog :title="titleName" :visible.sync="editVisible" width="40%">
            <el-form :model="roleForm"
                     ref="roleForm"
                     label-width="150px" size="small">
                <el-form-item label="角色名称：">
                    <el-input v-model="roleForm.name" style="width: 250px"></el-input>
                </el-form-item>
                <el-form-item label="描述：">
                    <el-input v-model="roleForm.description"
                              type="textarea"
                              :rows="5"
                              style="width: 250px"></el-input>
                </el-form-item>
                <el-form-item label="是否启用：">
                    <el-radio-group v-model="roleForm.status">
                        <el-radio :label="1">是</el-radio>
                        <el-radio :label="0">否</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveOrUpdateRole">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import {fetchRoleData,getRoleById,saveOrUpdateRole,deleteRole} from "../../../api/ums/role";

    //新增+修改弹框的重置
    const defaultRole = {
        id: null,
        name: null,
        description: null,
        adminCount: 0,
        status: 1
    };
    //查询的重置
    const defaultQuery = {
        name: '',
        size: 5,
        current: 1
    };

    export default {
        name: "role",
        data() {
            return {
                query: Object.assign({}, defaultQuery),
                roleForm: Object.assign({}, defaultRole),
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
                fetchRoleData(this.query).then(result => {
                    this.tableData = result.data.records;
                    this.pageTotal = result.data.total;
                })
            },
            //查询
            handleSearchList() {
                this.$set(this.query, 'current', 1);
                this.getData();
            },
            //查询的重置
            handleResetSearch() {
                this.query = Object.assign({}, defaultQuery);
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
            handleAdd() {
                this.editVisible = true;
                this.titleName = "角色新增";
                this.roleForm = Object.assign({}, defaultRole);
            },
            //修改弹框
            handleUpdate(row) {
                this.titleName = "角色修改";
                let id = row.id;
                getRoleById(id).then(result => {
                    this.roleForm = result.data;
                    this.editVisible = true;
                })
            },
            //新增+修改
            saveOrUpdateRole() {
                saveOrUpdateRole(this.roleForm).then(result => {
                    if (result.code == 200) {
                        this.$message.success("保存成功");
                        this.editVisible = false;
                        if (this.titleName === "角色新增") {
                            this.$set(this.query, 'current', 1);//新增返回第一页，修改不用
                        }
                        this.getData();
                    } else {
                        this.$message.error("保存失败");
                    }
                })
            },
            //删除
            handleDelete(row) {
                this.$confirm('是否要删除该角色?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteRole(row.id).then(result => {
                        if (result.code == 200) {
                            this.$message({
                                type: 'success',
                                message: '删除成功!'
                            });
                            this.getData();
                        }
                    });
                });
            },


        },
        mounted() {
            this.getData();
        }
    }
</script>

<style scoped>

</style>