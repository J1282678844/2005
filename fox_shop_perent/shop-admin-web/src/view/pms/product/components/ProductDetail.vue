<template>
  <el-card class="form-container" shadow="never">
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="填写商品信息"></el-step>
      <el-step title="填写商品促销"></el-step>
      <el-step title="填写商品属性"></el-step>
      <el-step title="选择商品关联"></el-step>
    </el-steps>
    <!--基本信息-->
    <product-info-detail
      v-show="showStatus[0]"
      :is-edit="isEdit"
      v-model="productForm"
      @nextStep="nextStep"></product-info-detail>
    <!--促销信息-->
    <product-sale-detail
      v-show="showStatus[1]"
      :is-edit="isEdit"
      v-model="productForm"
      @prevStep="prevStep"
      @nextStep="nextStep">
    </product-sale-detail>
    <!--属性信息-->
    <product-attr-detail
      v-show="showStatus[2]"
      :is-edit="isEdit"
      v-model="productForm"
      @prevStep="prevStep"
      @nextStep="nextStep">
    </product-attr-detail>
    <!--关联信息-->
    <product-relation-detail
      v-show="showStatus[3]"
      :is-edit="isEdit"
      v-model="productForm"
      @prevStep="prevStep"
      @finishCommit="finishCommit">
    </product-relation-detail>
  </el-card>
</template>

<script>
  import ProductInfoDetail from './ProductInfoDetail';
  import ProductAttrDetail from './ProductAttrDetail';
  import ProductRelationDetail from './ProductRelationDetail';
  import ProductSaleDetail from './ProductSaleDetail';
  import {saveOrUpdateProductData,getProductById} from "../../../../api/pms/product";

  const defaultProductParam={
    albumPics: '',
    brandId: null,
    brandName: '',
    deleteStatus: 0,
    description: '',
    detailDesc: '',
    detailHtml: '',
    detailMobileHtml: '',
    detailTitle: '',
    feightTemplateId: 0,
    flashPromotionCount: 0,
    flashPromotionId: 0,
    flashPromotionPrice: 0,
    flashPromotionSort: 0,
    giftPoint: 0,
    giftGrowth: 0,
    keywords: '',
    lowStock: 0,
    name: '',
    newStatus: 0,
    note: '',
    originalPrice: 0,
    pic: '',
    previewStatus: 0,
    price: 0,
    productAttributeCategoryId: null,
    cateParentId:null,
    productCategoryId: null,
    productCategoryName: '',
    productSn: '',
    promotionEndTime: '',
    promotionPerLimit: 0,
    promotionPrice: null,
    promotionStartTime: '',
    promotionType: 0,
    publishStatus: 0,
    recommandStatus: 0,
    sale: 0,
    serviceIds: '',
    sort: 0,
    stock: 0,
    subTitle: '',
    unit: '',
    usePointLimit: 0,
    verifyStatus: 0,
    weight: 0,
    //会员价格集合
    memberPriceList:[],
    //阶梯价格维护
    productLadderList:[{count: 0,discount: 0,price: 0}],
    //满减价格
    productFullReductionList:[{fullPrice: 0, reducePrice: 0}],
    //SKU维护
    skuStockList: [],
    //商品手动输入的  属性+参数  列表
    productAttributeValueList: []
  }
    export default {
        name: "ProductDetail",
      components:{ProductInfoDetail,ProductAttrDetail,ProductRelationDetail,ProductSaleDetail},
      props: {
        isEdit: {
          type: Boolean,
          default: false
        }
      },
        data() {
            return {
              showStatus: [true, false, false, false],
              active:0,
              productForm:Object.assign({},defaultProductParam)
            }
        },
        methods: {
          hideAll(){
            for(let i=0;i<this.showStatus.length;i++){
              this.showStatus[i]=false;
            }
          },
          //选择下页
          nextStep(){
            if(this.active<this.showStatus.length-1){
              this.hideAll();
              this.active++;
              this.showStatus[this.active]=true;
            }
          },
          //选择上页
          prevStep(){
            this.hideAll();
            this.active--;
            this.showStatus[this.active]=true;
          },
          //保存商品数据
          finishCommit(){
            saveOrUpdateProductData(this.productForm).then(result=>{
              if(result.code === 200){
                this.$message.success("保存商品的数据成功");
                this.$router.push("/Product");
              }else{
                this.$message.success("保存商品的数据失败");
              }
            })
          }

        },
        mounted() {
          if(this.isEdit){
            getProductById(this.$route.query.productId).then(result=>{
              this.productForm=result.data;
            })
          }
        }
    }
</script>

<style scoped>

</style>
