import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import Layout2 from '@/layout/index2'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */


/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
//都看得见的路由
export const constantRoutes = [{
        path: '/redirect',
        component: Layout,
        hidden: true,
        children: [{
            path: '/redirect/:path(.*)',
            component: () =>
                import ('@/views/redirect/index')
        }]
    },
    // {
    //     path: '/login',
    //     component: () =>
    //         import ('@/views/login/index'),
    //     hidden: true
    // },
    {
        path: '/404',
        component: () =>
            import ('@/views/404'),
        hidden: true
    },

  /**
   * 主页
   */
  {
    path: '/',
    component: Layout,
    // redirect: '/dashboard',
    children: [{
      path: 'home',
      name: 'home',
      component: () =>
        import ('@/views/Home/home'),
      meta: { title: '系统主页', icon: 'home' }
    }]
  },

    /**
     * 数据图表
     */
    {
        path: '/dashboard',
        component: Layout,
        redirect: '/dashboard',
        children: [{
            path: 'dashboard',
            name: 'Dashboard',
            component: () =>
                import ('@/views/dashboard/index'),
            meta: { title: '后台数据', icon: 'dashboard' }
        }]
    },



    // /**
    //  * 数据上链（原产地）
    //  */
    // {
    //     path: '/GoodsUpdate',
    //     component: Layout,
    //     meta:{title:'商品管理',icon:'manage'},
    //     children: [
    //       {
    //         path: 'goodsAdd',
    //         name: 'goodsAdd',
    //         component: () =>
    //             import ('@/views/GoodsUpdate/goodsAdd'),
    //         meta: { title: '商品上链', icon: 'chain'}
    //     },
    //       {
    //         path: 'goodsTransport',
    //         name: 'goodsTransport',
    //         component: () =>
    //           import ('@/views/GoodsUpdate/goodsTransport'),
    //         meta: { title: '商品上链', icon: 'chain'}
    //       },
    //       {
    //         path: 'goodsProcess',
    //         name: 'goodsProcess',
    //         component: () =>
    //           import ('@/views/GoodsUpdate/goodsProcess'),
    //         meta: { title: '商品上链', icon: 'chain'}
    //       },
    //       {
    //         path: 'goodsSale',
    //         name: 'goodsSale',
    //         component: () =>
    //           import ('@/views/GoodsUpdate/goodsSale'),
    //         meta: { title: '商品上链', icon: 'chain'}
    //       },
    //     ]
    // },


  /**
   * 数据查询
   */
  {
    path: '/GoodsQuery',
    component: Layout,
    children: [{
      path: 'goodsQuery',
      name: 'goodsQuery',
      component: () =>
        import ('@/views/GoodsQuery/goodsQuery'),
      meta: { title: '商品查询', icon: 'query' }
    }]
  },

  /**
   * 查询记录（需要登录)
   */
  {
    path: '/GoodsQueryHistory',
    component: Layout,
    children: [{
      path: 'goodsQueryHistory',
      name: 'goodsQueryHistory',
      component: () =>
        import ('@/views/GoodsQueryHistory/goodsQueryHistory'),
      meta: { title: '查询记录', icon: 'history' }
    }]
  },

  /**
   *注册界面
   */
  {
    path:'/GoodsUser',
    component: Layout2,
    children: [{
      path: 'register',
      name: 'register',
      component: () =>
        import ('@/views/GoodsUser/register'),
    }]
  },

  /**
   * 溯源地图
   */
  {
    path:'/GoodsMap',
    component: Layout,
    children: [{
      path: 'goodsMap',
      name: 'goodsMap',
      component: () =>
        import ('@/views/GoodsMap/goodsMap'),
      meta: { title: '溯源地图', icon: 'map' }
    }]

  },

    // 404 page must be placed at the end !!!
    { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
    const newRouter = createRouter()
    router.matcher = newRouter.matcher // reset router
}

export default router
