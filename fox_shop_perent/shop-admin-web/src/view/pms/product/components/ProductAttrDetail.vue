<template><!--商品属性-->
    <div style="margin-top: 50px">
        <el-form :model="value" label-width="120px" style="width: 900px;margin: 0 auto;" size="small">
            <el-form-item label="属性类型：">
                <el-select v-model="value.productAttributeCategoryId"
                           placeholder="请选择属性类型"
                           @change="handleProductAttrChange">
                    <el-option
                            v-for="item in productAttributeCategoryOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="商品规格：">
                <el-card shadow="never" class="cardBg">
                    <div v-for="(productAttr,idx) in selectProductAttr">
                        {{productAttr.name}}：
                        <el-checkbox-group v-if="productAttr.handAddStatus===0" v-model="selectProductAttr[idx].values">
                            <el-checkbox v-for="item in getInputListArr(productAttr.inputList)" :label="item"
                                         :key="item"
                                         class="littleMarginLeft"></el-checkbox>
                        </el-checkbox-group>
                        <div v-else>
                            <el-checkbox-group v-model="selectProductAttr[idx].values">
                                <div v-for="(item,index) in selectProductAttr[idx].options"
                                     style="display: inline-block"
                                     class="littleMarginLeft">
                                    <el-checkbox :label="item" :key="item"></el-checkbox>
                                    <el-button type="text" class="littleMarginLeft"
                                               @click="handleRemoveProductAttrValue(idx,index)">删除
                                    </el-button>
                                </div>
                                idx,
                            </el-checkbox-group>
                            <el-input v-model.trim="addProductAttrValue " style="width: 160px;margin-left: 10px"
                                      clearable></el-input>
                            <el-button class="littleMarginLeft" @click="handleAddProductAttrValue(idx)">增加</el-button>
                        </div>
                    </div>
                </el-card>
                <el-table style="width: 100%;margin-top: 20px"
                          :data="value.skuStockList"
                          border>
                    <el-table-column
                            v-for="(item,index) in selectProductAttr"
                            :label="item.name"
                            :key="item.id"
                            align="center">
                        <template slot-scope="scope">
                            {{getProductSkuSp(scope.row,index)}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="销售价格"
                            width="80"
                            align="center">
                        <template slot-scope="scope">
                            <el-input v-model="scope.row.price"></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="商品库存"
                            width="80"
                            align="center">
                        <template slot-scope="scope">
                            <el-input v-model="scope.row.stock"></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="库存预警值"
                            width="80"
                            align="center">
                        <template slot-scope="scope">
                            <el-input v-model="scope.row.lowStock"></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="SKU编号"
                            align="center">
                        <template slot-scope="scope">
                            <el-input v-model="scope.row.skuCode" readonly></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column label="SKU图片" align="center">
                        <template slot-scope="scope">
                            <single-upload v-model="scope.row.pic"></single-upload>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            width="80"
                            align="center">
                        <template slot-scope="scope">
                            <el-button
                                    type="text"
                                    @click="handleRemoveProductSku(scope.$index, scope.row)">删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-button
                        type="primary"
                        style="margin-top: 20px"
                        @click="handleRefreshProductSkuList">刷新列表
                </el-button>
                <el-button
                        type="primary"
                        style="margin-top: 20px"
                        @click="handleSyncProductSkuPrice">同步价格
                </el-button>
                <el-button
                        type="primary"
                        style="margin-top: 20px"
                        @click="handleSyncProductSkuStock">同步库存
                </el-button>
            </el-form-item>
            <el-form-item label="商品参数：">
                <el-card shadow="never" class="cardBg">
                    <div v-for="(item,index) in selectProductParam" :class="{littleMarginTop:index!==0}">
                        <div class="paramInputLabel">{{item.name}}:</div>
                        <el-select v-if="item.inputType===1" class="paramInput"
                                   v-model="selectProductParam[index].value">
                            <el-option
                                    v-for="item in getParamInputList(item.inputList)"
                                    :key="item"
                                    :label="item"
                                    :value="item">
                            </el-option>
                        </el-select>
                        <el-input v-else class="paramInput" v-model="selectProductParam[index].value"></el-input>
                    </div>
                </el-card>
            </el-form-item>
            <el-form-item label="商品相册：">
                <multi-upload v-model="selectProductPics"></multi-upload>
            </el-form-item>
            <el-form-item label="规格参数：">
                <el-tabs v-model="activeHtmlName" type="card">
                    <el-tab-pane label="电脑端详情" name="pc">
                        <tinymce :width="595" :height="300" v-model="value.detailHtml"></tinymce>
                    </el-tab-pane>
                    <el-tab-pane label="移动端详情" name="mobile">
                        <tinymce :width="595" :height="300" v-model="value.detailMobileHtml"></tinymce>
                    </el-tab-pane>
                </el-tabs>
            </el-form-item>
            <el-form-item style="text-align: center">
                <el-button size="medium" @click="handlePrev">上一步，填写商品促销</el-button>
                <el-button type="primary" size="medium" @click="handleNext">下一步，选择商品关联</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import {fetchAttrCategoryAll} from "../../../../api/pms/attrCategory";
    import {fetchAttrListData} from "../../../../api/pms/attrList";
    import multiUpload from "../../../../components/multiUpload";
    import SingleUpload from "../../../../components/SingleUpload";

    export default {
        name: "ProductInfoDetail",
        components: {multiUpload, SingleUpload},
        props: {
            value: Object,
            isEdit: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
                //商品属性分类下拉选项
                productAttributeCategoryOptions: [],
                //选中的属性数组
                selectProductAttr: [],
                //可手动添加的商品属性
                addProductAttrValue: null,
                //相册数组
                selectProductPics: [],
                //选中的参数数组
                selectProductParam: [],
                //商品富文本详情激活类型
                activeHtmlName: 'pc',
                hasEditCreated: false
            }
        },
        computed: {
            //商品的编号
            productId() {
                return this.value.id;
            }
        },
        watch: {
            productId: function (newValue) {
                if (!this.isEdit) return;
                if (this.hasEditCreated) return;
                if (newValue === undefined || newValue == null || newValue === 0) return;
                this.handleProductAttrChange(this.value.productAttributeCategoryId);
                this.handleProductPics(this.value.albumPics);
                this.hasEditCreated = true;
            },
        },
        methods: {
            handlePrev() {
                this.$emit("prevStep");
            },
            //相册数组回显
            handleProductPics(photos) {
                this.selectProductPics = photos.split(",");
            },
            handleNext() {
                //商品相册的处理
                this.selectProductPhotos();
                //合并商品的属性和参数进行保存
                this.mergeProductAttrValue();
                this.$emit("nextStep");
            },
            //合并商品的属性和参数进行保存
            mergeProductAttrValue() {
                this.value.productAttributeValueList = [];

                //合并属性
                for (let i = 0; i < this.selectProductAttr.length; i++) {
                    let attr = this.selectProductAttr[i];
                    if (attr.handAddStatus === 1 && attr.options != null && attr.options.length > 0) {
                        this.value.productAttributeValueList.push({
                            productAttributeId: attr.id,
                            value: this.getOptionstr(attr.options)
                        })
                    }
                }
                //合并参数
                for (let i = 0; i < this.selectProductParam.length; i++) {
                    let attr = this.selectProductParam[i];
                    this.value.productAttributeValueList.push({
                        productAttributeId: attr.id,
                        value: attr.value
                    })
                }
            },
            //拼接选中的属性
            getOptionstr(options) {
                let value = "";
                for (let i = 0; i < options.length; i++) {
                    if (i === options.length - 1) {
                        value += options[i];
                    } else {
                        value += options[i] + ",";
                    }
                }
                return value;
            },
            //商品相册的处理
            selectProductPhotos() {
                let list = this.selectProductPics;
                if (list != null && list.length > 0) {
                    let photos = "";
                    for (let i = 0; i < list.length; i++) {
                        photos += list[i] + ",";
                    }
                    if (photos !== "" && photos.endsWith(",")) {
                        photos = photos.substr(0, photos.length - 1);
                    }
                    this.value.pic = this.value.skuStockList[0].pic;//将第一个SKU的图片设置为主图片
                    this.value.albumPics = photos;
                }
            },
            //查询属性分类数据
            queryProductAttributeCategory() {
                fetchAttrCategoryAll().then(result => {
                    let list = result.data;
                    this.productAttributeCategoryOptions = [];
                    for (let i = 0; i < list.length; i++) {
                        this.productAttributeCategoryOptions.push({label: list[i].name, value: list[i].id});
                    }
                })
            },
            //参数数组
            getParamInputList(val) {
                return val.split(",");
            },
            //属性数组
            getInputListArr(inputList) {
                return inputList.split(",");
            },
            //属性分类改变事件
            handleProductAttrChange(val) {
                this.getAttrList(val);
                this.getParamsList(val);
            },
            //查询属性列表
            getAttrList(val) {
                let query = {
                    cid: val,
                    type: 0,
                    size: 20,
                    current: 1
                };
                fetchAttrListData(query).then(result => {
                    this.selectProductAttr = [];
                    let list = result.data.records;
                    for (let i = 0; i < list.length; i++) {
                        let values = [];
                        let options = [];
                        //判断是否为   修改状态
                        if (this.isEdit) {
                            if (list[i].handAddStatus === 1) {
                                options = this.getProductAttrValues(list[i].id);//回显手动输入的属性
                            }
                            values = this.getAttrValues(list[i].name);//回显设定的属性
                        }
                        this.selectProductAttr.push({
                            values: values,
                            options: options,
                            handAddStatus: list[i].handAddStatus,
                            id: list[i].id,
                            name: list[i].name,
                            inputList: list[i].inputList
                        })
                    }
                })
            },
            //查询参数列表
            getParamsList(val) {
                let query = {
                    cid: val,
                    type: 1,
                    size: 20,
                    current: 1
                };
                fetchAttrListData(query).then(result => {
                    this.selectProductParam = [];
                    let list = result.data.records;
                    for (let i = 0; i < list.length; i++) {
                        let value = "";
                        //判断是否为   修改状态
                        if (this.isEdit){
                            value = this.getProductParams(list[i].id);//回显参数
                        }
                        this.selectProductParam.push({
                            inputType: list[i].inputType,
                            id: list[i].id,
                            name: list[i].name,
                            value: value,
                            inputList: list[i].inputList
                        })
                    }
                })
            },
            //回显参数
            getProductParams(paramsId){
                let attrList = this.value.productAttributeValueList;
                let value = "";
                for (let i = 0; i < attrList.length; i++) {
                    if (paramsId === attrList[i].id){
                        value = attrList[i].value;
                        break;
                    }
                }
                return value;
            },
            //回显手动输入的属性
            getProductAttrValues(attrId) {
                let attrList = this.value.productAttributeValueList;
                let options = [];
                for (let i = 0; i < attrList.length; i++) {
                    if (attrId === attrList[i].productAttributeId) {
                        let attrValues = attrList[i].value;
                        let optionsList = attrValues.split(",");
                        for (let j = 0; j < optionsList.length; j++) {
                            options.push(optionsList[j]);
                        }
                    }
                }
                return options;
            },
            //回显设定的属性
            getAttrValues() {
                let valuesSet = new Set();//自动去重
                let skuList = this.value.skuStockList;
                for (let i = 0; i < skuList.length; i++) {
                    let spData = skuList[i].spData;
                    let dataList = JSON.parse(spData);
                    for (let j = 0; j < dataList.length; j++) {
                        let key = dataList[j].key;
                        if (key === name) {
                            valuesSet.add(dataList[j].value);
                            break;
                        }
                    }
                }
                return Array.from(valuesSet);
            },
            //手动添加属性
            handleAddProductAttrValue(idx) {
                let options = this.selectProductAttr[idx].options;
                if (this.addProductAttrValue === "" || this.addProductAttrValue === null) {
                    this.$message({
                        message: '属性值不能为空',
                        type: 'warning',
                        duration: 1000
                    });
                    this.addProductAttrValue = null;
                    return
                }
                if (options.indexOf(this.addProductAttrValue) !== -1) {
                    this.$message({
                        message: '属性值不能重复',
                        type: 'warning',
                        duration: 1000
                    });
                    this.addProductAttrValue = null;
                    return
                }
                this.selectProductAttr[idx].options.push(this.addProductAttrValue);
                this.addProductAttrValue = null;
            },
            //删除手动添加的属性
            handleRemoveProductAttrValue(idx, index) {
                this.selectProductAttr[idx].options.splice(index, 1);
            },
            //提示刷新列表
            handleRefreshProductSkuList() {
                this.$confirm('刷新列表将导致sku信息重新生成，是否要刷新', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.refreshProductSkuList();
                });
            },
            //设置列头对应的数据
            getProductSkuSp(row, index) {
                let spData = JSON.parse(row.spData);
                if (spData != null && index < spData.length) {
                    return spData[index].value;
                }
                return null;
            },
            //删除SKU数据
            handleRemoveProductSku(index, row) {
                let skuList = this.value.skuStockList;
                if (skuList.length === 1) {
                    skuList.pop();
                } else {
                    skuList.splice(index, 1);
                }

            },
            //刷新列表
            refreshProductSkuList() {
                let attrLength = this.selectProductAttr.length;
                this.value.skuStockList = [];
                let skuList = this.value.skuStockList;
                if (attrLength === 1) {
                    let data = this.selectProductAttr[0];
                    for (let i = 0; i < data.values.length; i++) {
                        skuList.push({
                            spData: JSON.stringify([{key: data.name, value: data.values[i]}])
                        })

                    }
                } else if (attrLength === 2) {
                    let attr0 = this.selectProductAttr[0];
                    let attr1 = this.selectProductAttr[1];
                    if (attr0.values.length === 0 || attr1.values.length === 0) {
                        this.$message({
                            message: '属性数据不完整。',
                            type: 'warning',
                            duration: 1000
                        });
                        return;
                    }
                    for (let i = 0; i < attr0.values.length; i++) {
                        for (let j = 0; j < attr1.values.length; j++) {
                            let spData = [];
                            spData.push({key: attr0.name, value: attr0.values[i]});
                            spData.push({key: attr1.name, value: attr1.values[j]});
                            skuList.push({
                                spData: JSON.stringify(spData)
                            })
                        }
                    }
                } else if (attrLength === 3) {
                    let attr0 = this.selectProductAttr[0];
                    let attr1 = this.selectProductAttr[1];
                    let attr2 = this.selectProductAttr[2];
                    if (attr0.values.length === 0 || attr1.values.length === 0 || attr2.values.length === 0) {
                        this.$message({
                            message: '属性数据不完整。',
                            type: 'warning',
                            duration: 1000
                        });
                        return;
                    }
                    for (let i = 0; i < attr0.values.length; i++) {
                        for (let j = 0; j < attr1.values.length; j++) {
                            for (let k = 0; k < attr2.values.length; k++) {
                                let spData = [];
                                spData.push({key: attr0.name, value: attr0.values[i]});
                                spData.push({key: attr1.name, value: attr1.values[j]});
                                spData.push({key: attr2.name, value: attr2.values[k]});
                                skuList.push({
                                    spData: JSON.stringify(spData)
                                })
                            }
                        }
                    }
                }
            },
            //同步价格
            handleSyncProductSkuPrice() {
                this.$confirm('该操作会将第一个sku的价格同步到其他的sku中', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let skuList = this.value.skuStockList;
                    if (skuList.length === 0) {
                        this.$message({
                            message: '没有SKU的价格需要同步。',
                            type: 'warning',
                            duration: 1000
                        });
                        return;
                    }
                    let templateList = [];
                    templateList = templateList.concat(templateList, skuList);
                    let price = this.value.skuStockList[0].price;
                    for (let i = 0; i < templateList.length; i++) {
                        templateList[i].price = price;
                    }
                    this.value.skuStockList = [];
                    this.value.skuStockList = this.value.skuStockList.concat(this.value.skuStockList, templateList);
                });
            },
            //同步库存和预警库存
            handleSyncProductSkuStock() {
                this.$confirm('该操作会将第一个sku的库存和预警库存同步到其他的sku中', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let skuList = this.value.skuStockList;
                    if (skuList.length === 0) {
                        this.$message({
                            message: '没有SKU的库存需要同步。',
                            type: 'warning',
                            duration: 1000
                        });
                        return;
                    }
                    let templateList = [];
                    templateList = templateList.concat(templateList, skuList);
                    let stock = this.value.skuStockList[0].stock;
                    let lowStock = this.value.skuStockList[0].lowStock;
                    for (let i = 0; i < templateList.length; i++) {
                        templateList[i].stock = stock;
                        templateList[i].lowStock = lowStock;
                    }
                    this.value.skuStockList = [];
                    this.value.skuStockList = this.value.skuStockList.concat(this.value.skuStockList, templateList);
                });
            }

        },
        mounted() {
            this.queryProductAttributeCategory();
        }
    }
</script>

<style scoped>

</style>
