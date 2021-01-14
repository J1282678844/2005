import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/dashboard'
        },
        {
            path: '/',
            component: () => import(/* webpackChunkName: "home" */ '../components/common/Home.vue'),
            meta: {title: '自述文件'},
            children: [
                {
                    path: '/dashboard',
                    name: "dashboard",
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
                    meta: {title: '系统首页'}
                },
                {
                    path: '/product',
                    name: "product",
                    component: () => import(/* webpackChunkName: "table" */ '../view/pms/product/ProductMain'),
                    meta: {title: '商品列表', icon: 'product-list'}
                },
                {
                    path: '/productAdd',
                    name: "productAdd",
                    component: () => import(/* webpackChunkName: "table" */ '../view/pms/product/Add'),
                    meta: {title: '商品新增', icon: 'product-add'}
                },
                {
                    path: '/productUpdate',
                    name: "productUpdate",
                    component: () => import(/* webpackChunkName: "table" */ '../view/pms/product/Update'),
                    meta: {title: '商品修改', icon: 'product-add'}
                },
                {
                    path: '/brand',
                    name: "brand",
                    component: () => import(/* webpackChunkName: "table" */ '../view/pms/brand/BrandMain'),
                    meta: {title: '品牌管理', icon: 'product-brand'}
                },
                {
                    path: '/Category',
                    name: "Category",
                    component: () => import(/* webpackChunkName: "table" */ '../view/pms/category/CategoryMain'),
                    meta: {title: '商品分类', icon: 'product-cate'}
                },
                {
                    path: '/Attr',
                    name: "Attr",
                    component: () => import(/* webpackChunkName: "table" */ '../view/pms/attr/ProductAttr'),
                    meta: {title: '商品类型', icon: 'product-attr'}
                },
                {
                    path: '/AttrList',
                    name: "AttrList",
                    component: () => import(/* webpackChunkName: "table" */ '../view/pms/attr/AttrList'),
                    meta: {title: '属性列表'}
                },
                {
                    path: '/ParamList',
                    name: "ParamList",
                    component: () => import(/* webpackChunkName: "table" */ '../view/pms/attr/ParamList'),
                    meta: {title: '参数列表'}
                },
                {
                    path: '/user',
                    name: "user",
                    component: () => import(/* webpackChunkName: "table" */ '../view/ums/admin/user'),
                    meta: {title: '用户列表', icon: 'el-icon-user'}
                },
                {
                    path: '/role',
                    name: "role",
                    component: () => import(/* webpackChunkName: "table" */ '../view/ums/admin/role'),
                    meta: {title: '角色列表', icon: 'el-icon-s-custom'}
                },
                {
                    path: '/menu',
                    name: "menu",
                    component: () => import(/* webpackChunkName: "table" */ '../view/ums/admin/menu'),
                    meta: {title: '菜单列表', icon: 'el-icon-menu'}
                },
                {
                    path: '/resource',
                    name: "resource",
                    component: () => import(/* webpackChunkName: "table" */ '../view/ums/admin/resource'),
                    meta: {title: '资源列表', icon: 'el-icon-s-management'}
                },




                {
                    path: '/icon',
                    component: () => import(/* webpackChunkName: "icon" */ '../components/page/Icon.vue'),
                    meta: {title: '自定义图标'}
                },
                {
                    path: '/table',
                    component: () => import(/* webpackChunkName: "table" */ '../components/page/BaseTable.vue'),
                    meta: {title: '基础表格'}
                },
                {
                    path: '/tabs',
                    component: () => import(/* webpackChunkName: "tabs" */ '../components/page/Tabs.vue'),
                    meta: {title: 'tab选项卡'}
                },
                {
                    path: '/form',
                    component: () => import(/* webpackChunkName: "form" */ '../components/page/BaseForm.vue'),
                    meta: {title: '基本表单'}
                },
                {
                    // 富文本编辑器组件
                    path: '/editor',
                    component: () => import(/* webpackChunkName: "editor" */ '../components/page/VueEditor.vue'),
                    meta: {title: '富文本编辑器'}
                },
                {
                    // markdown组件
                    path: '/markdown',
                    component: () => import(/* webpackChunkName: "markdown" */ '../components/page/Markdown.vue'),
                    meta: {title: 'markdown编辑器'}
                },
                {
                    // 图片上传组件
                    path: '/upload',
                    component: () => import(/* webpackChunkName: "upload" */ '../components/page/Upload.vue'),
                    meta: {title: '文件上传'}
                },
                {
                    // vue-schart组件
                    path: '/charts',
                    component: () => import(/* webpackChunkName: "chart" */ '../components/page/BaseCharts.vue'),
                    meta: {title: 'schart图表'}
                },
                {
                    // 拖拽列表组件
                    path: '/drag',
                    component: () => import(/* webpackChunkName: "drag" */ '../components/page/DragList.vue'),
                    meta: {title: '拖拽列表'}
                },
                {
                    // 拖拽Dialog组件
                    path: '/dialog',
                    component: () => import(/* webpackChunkName: "dragdialog" */ '../components/page/DragDialog.vue'),
                    meta: {title: '拖拽弹框'}
                },
                {
                    // 国际化组件
                    path: '/i18n',
                    component: () => import(/* webpackChunkName: "i18n" */ '../components/page/I18n.vue'),
                    meta: {title: '国际化'}
                },
                {
                    // 权限页面
                    path: '/permission',
                    component: () => import(/* webpackChunkName: "permission" */ '../components/page/Permission.vue'),
                    meta: {title: '权限测试', permission: true}
                },
                {
                    path: '/404',
                    component: () => import(/* webpackChunkName: "404" */ '../components/page/404.vue'),
                    meta: {title: '404'}
                },
                {
                    path: '/403',
                    component: () => import(/* webpackChunkName: "403" */ '../components/page/403.vue'),
                    meta: {title: '403'}
                },
                {
                    path: '/donate',
                    component: () => import(/* webpackChunkName: "donate" */ '../components/page/Donate.vue'),
                    meta: {title: '支持作者'}
                }
            ]
        },
        {
            path: '/login',
            component: () => import(/* webpackChunkName: "login" */ '../view/Login'),
            meta: {title: '登录'}
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
});
