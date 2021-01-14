<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 菜单列表
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="app-container">
            <el-card class="operate-container" shadow="never">
                <i class="el-icon-tickets" style="margin-top: 5px"></i>
                <span style="margin-top: 5px">数据列表</span>
                <el-button class="btn-add" @click="handleAdd()" size="mini">添加</el-button>
            </el-card>
            <div class="table-container">
                <el-table ref="menuTable"
                          style="width: 100%"
                          :data="tableData"
                          v-loading="listLoading" border>
                    <el-table-column label="编号" width="100" align="center">
                        <template slot-scope="scope">{{scope.row.id}}</template>
                    </el-table-column>
                    <el-table-column label="菜单名称" align="center">
                        <template slot-scope="scope">{{scope.row.title}}</template>
                    </el-table-column>
                    <el-table-column label="菜单级数" width="100" align="center">
                        <template slot-scope="scope">
                            <span v-if="scope.row.level == 0">一级</span>
                            <span v-else>二级</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="前端名称" align="center">
                        <template slot-scope="scope">{{scope.row.name}}</template>
                    </el-table-column>
                    <el-table-column label="前端图标" width="100" align="center">
                        <template slot-scope="scope"><svg-icon :icon-class="scope.row.icon"></svg-icon></template>
                    </el-table-column>
                    <el-table-column label="是否显示" width="100" align="center">
                        <template slot-scope="scope">
                            <el-switch
                                    @change="handleHiddenChange(scope.row)"
                                    :active-value="0"
                                    :inactive-value="1"
                                    v-model="scope.row.hidden">
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column label="排序" width="100" align="center">
                        <template slot-scope="scope">{{scope.row.sort }}</template>
                    </el-table-column>
                    <el-table-column label="设置" width="120" align="center">
                        <template slot-scope="scope">
                            <el-button
                                    v-if="scope.row.level == 0"
                                    size="mini"
                                    icon="el-icon-caret-bottom"
                                    @click="handleChildren(scope.row)">查看下级
                            </el-button>
                            <el-button
                                    size="mini"
                                    v-else
                                    icon="el-icon-caret-top"
                                    @click="handleParent(scope.row)">返回上级
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="200" align="center">
                        <template slot-scope="scope">
                            <el-button
                                    size="mini"
                                    type="text"
                                    @click="handleUpdate(scope.row)">编辑
                            </el-button>
                            <el-button
                                    size="mini"
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
            <el-form :model="menuForm"
                     ref="menuForm"
                     label-width="150px">
                <el-form-item label="菜单名称：" prop="title">
                    <el-input v-model="menuForm.title"></el-input>
                </el-form-item>
                <el-form-item label="上级菜单：">
                    <el-select v-model="menuForm.parentId"
                               placeholder="请选择菜单">
                        <el-option
                                v-for="item in selectMenuList"
                                :key="item.id"
                                :label="item.title"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="前端名称：" prop="name">
                    <el-input v-model="menuForm.name"></el-input>
                </el-form-item>
                <el-form-item label="前端图标：" prop="icon">
                    <el-input v-model="menuForm.icon" style="width: 80%"></el-input>
                    <svg-icon style="margin-left: 8px" :icon-class="menuForm.icon"></svg-icon>
                </el-form-item>
                <el-form-item label="是否显示：">
                    <el-radio-group v-model="menuForm.hidden">
                        <el-radio :label="0">是</el-radio>
                        <el-radio :label="1">否</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="排序：">
                    <el-input v-model="menuForm.sort"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveOrUpdateMenu">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import {fetchMenuData,getMenuById,saveOrUpdateMenu,deleteMenu} from "../../../api/ums/menu";

    //新增+修改弹框的重置
    const defaultMenu = {
        title: '',
        parentId: 0,
        name: '',
        icon: '',
        hidden: 0,
        sort: 0
    };

    export default {
        name: "menu",
        data() {
            return {
                menuForm: Object.assign({}, defaultMenu),
                query: {
                    size: 5,
                    current: 1,
                    level: 0,
                    pid: null
                },
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
                fetchMenuData(this.query).then(result => {
                    this.tableData = result.data.records;
                    this.pageTotal = result.data.total;
                })
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
            //新增弹框
            handleAdd() {
                this.editVisible = true;
                this.titleName = "菜单新增";
                this.menuForm = Object.assign({}, defaultMenu);
            },
            //修改弹框
            handleUpdate(row) {
                this.titleName = "菜单修改";
                let id = row.id;
                getMenuById(id).then(result => {
                    this.menuForm = result.data;
                    this.editVisible = true;
                })
            },
            //新增+修改
            saveOrUpdateMenu() {
                saveOrUpdateMenu(this.menuForm).then(result => {
                    if (result.code == 200) {
                        this.$message.success("保存成功");
                        this.editVisible = false;
                        if (this.titleName === "菜单新增") {
                            this.$set(this.query, 'current', 1);//新增返回第一页，修改不用
                            this.$set(this.query, 'pid', null);
                            this.$set(this.query, 'level', 0);
                        }
                        this.getData();
                    } else {
                        this.$message.error("保存失败");
                    }
                })
            },
            //删除
            handleDelete(row) {
                this.$confirm('是否要删除该菜单?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteMenu(row.id).then(result => {
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