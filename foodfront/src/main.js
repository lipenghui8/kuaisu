import Vue from 'vue'
import Cookies from 'js-cookie'
import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import BaiduMap from 'vue-baidu-map'
import VueJsonp from 'vue-jsonp'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import enLang from 'element-ui/lib/locale/lang/en' // lang i18n
//import i18n from './lang'
import axios from 'axios'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control

import qs from 'qs'
import goodsBlock from './utils/goodsBlock'
/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
// if (process.env.NODE_ENV === 'production') {
//     const { mockXHR } = require('../mock')
//     mockXHR()
// }

Vue.use(ElementUI, {
    size: Cookies.get('size') || 'medium', // set element-ui default size
    //i18n: (key, value) => i18n.t(key, value)
    locale: enLang
})
Vue.use(BaiduMap, {
  // ak 是在百度地图开发者平台申请的密钥 详见 http://lbsyun.baidu.com/apiconsole/key */
  ak: 'koWrGRuQVOnimgV7OqoT8rUpmcDpsoZo'
})

Vue.config.productionTip = false
Vue.prototype.$qs = qs
Vue.prototype.backApi = "http://localhost:8080"
Vue.prototype.goodsBlock = goodsBlock
// Vue.prototype.dangerArea = ['上海','北京']
// Vue.prototype.$axios = axios
// axios.defaults.baseURL = process.env.VUE_APP_BASE_API

new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App),
  data(){
      return{
        userData: {
          login_ed: false,
          userId: 1,
          account: "1912080054",
          username: "123456",
          avator: "",
          worktype:'',
          location:'',
        },
        dangerArea:['上海','北京',],
        dangerGoods:[],
      }
  },
  methods:{
    setUserData(userId, account, username, avator, login_ed,worktype,location) {
      console.log("setUserData")
      this.userData.userId = userId;
      this.userData.account = account;
      this.userData.username = username;
      this.userData.avator = avator;
      this.userData.login_ed = login_ed;
      this.userData.worktype = worktype;
      this.userData.location = location;
    },
    cleanUserData() {
      this.userData.account = "";
      this.userData.username = "";
      this.userData.avator = "";
      this.userData.login_ed = false;
      this.userData.worktype = '';
      this.userData.location = '';
    },
  }

})
