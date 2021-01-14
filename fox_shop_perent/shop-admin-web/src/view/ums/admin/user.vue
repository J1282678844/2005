<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 用户列表
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
                            <el-input v-model="query.username" class="input-width" placeholder="帐号/姓名"
                                      clearable>
                            </el-input>
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
                <el-table ref="adminTable"
                          :data="tableData"
                          style="width: 100%;"
                          v-loading="listLoading"
                          border>
                    <el-table-column label="编号" width="100" align="center">
                        <template slot-scope="scope">{{scope.row.id}}</template>
                    </el-table-column>
                    <el-table-column label="帐号" align="center">
                        <template slot-scope="scope">{{scope.row.username}}</template>
                    </el-table-column>
                    <el-table-column label="姓名" align="center">
                        <template slot-scope="scope">{{scope.row.nickName}}</template>
                    </el-table-column>
                    <el-table-column label="邮箱" align="center">
                        <template slot-scope="scope">{{scope.row.email}}</template>
                    </el-table-column>
                    <el-table-column label="添加时间" width="160" align="center">
                        <template slot-scope="scope">{{scope.row.createTime | formatDateTime}}</template>
                    </el-table-column>
                    <el-table-column label="最后登录" width="160" align="center">
                        <template slot-scope="scope">{{scope.row.loginTime | formatDateTime}}</template>
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
                    <el-table-column label="操作" width="180" align="center">
                        <template slot-scope="scope">
                            <el-button size="mini"
                                       type="text"
                                       @click="handleSelectRole(scope.row)">分配角色
                            </el-button>
                            <el-button size="mini"
                                       type="text"
                                       @click="handleUpdate(scope.row)">编辑
                            </el-button>
                            <el-button size="mini"
                                       type="text"
                                       @click="handleDelete(scope.row)">删除
                            </el-button>
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
            <el-form :model="userForm"
                     ref="userForm"
                     label-width="150px" size="small">
                <el-form-item label="帐号：">
                    <el-input v-model="userForm.username" style="width: 250px"></el-input>
                </el-form-item>
                <el-form-item label="姓名：">
                    <el-input v-model="userForm.nickName" style="width: 250px"></el-input>
                </el-form-item>
                <el-form-item label="邮箱：">
                    <el-input v-model="userForm.email" style="width: 250px"></el-input>
                </el-form-item>
                <el-form-item label="备注：">
                    <el-input v-model="userForm.note"
                              type="textarea"
                              :rows="5"
                              style="width: 250px"></el-input>
                </el-form-item>
                <el-form-item label="是否启用：">
                    <el-radio-group v-model="userForm.status">
                        <el-radio :label="1">是</el-radio>
                        <el-radio :label="0">否</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveOrUpdateUser">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import {fetchUserData, getUserById, saveOrUpdateUser} from "../../../api/ums/user";

    //新增+修改弹框的重置
    const defaultUser = {
        id: null,
        username: null,
        nickName: null,
        email: null,
        note: null,
        status: 1
    };
    //查询的重置
    const defaultQuery = {
        username: '',
        size: 5,
        current: 1
    };

    export default {
        name: "user",
        data() {
            return {
                query: Object.assign({}, defaultQuery),
                userForm: Object.assign({}, defaultUser),
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
                fetchUserData(this.query).then(result => {
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
                this.titleName = "用户新增";
                this.userForm = Object.assign({}, defaultUser);
            },
            //修改弹框
            handleUpdate(row) {
                this.titleName = "用户修改";
                let id = row.id;
                getUserById(id).then(result => {
                    this.userForm = result.data;
                    this.editVisible = true;
                })
            },
            //新增+修改
            saveOrUpdateUser() {
                saveOrUpdateUser(this.userForm).then(result => {
                    if (result.code == 200) {
                        this.$message.success("保存成功");
                        this.editVisible = false;
                        if (this.titleName === "用户新增") {
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
                this.$confirm('是否要删除该用户?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteUser(row.id).then(result => {
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