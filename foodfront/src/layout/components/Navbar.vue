<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />
    <div class="flogin-container">
    <el-button
      v-if="!this.$root.userData.login_ed"
      type="primary"
      round
      size="mini"
      plain
      @click="changeLoginView()"
    >登录
    </el-button>
    </div>
    <template v-if="this.$root.userData.login_ed">
      <!-- fit的默认值是cover -->
      <el-popover
        placement="bottom"
        :title="this.$root.userData.worktype | typeFilter "
        width="200"
        trigger="hover"
        content="这是一段内容,这是一段内容,这是一段内容,这是一段内容。"
        class="login-container"
      >
        <p>您好，{{ this.$root.userData.username }}！</p>
        <div style="text-align: right; margin: 0">
          <el-button size="mini" type="text" @click="visible = false"
          >个人中心</el-button
          >
          <el-button type="primary" size="mini" @click="log_out()"
          >退出</el-button
          >
        </div>
        <!-- reference确定触发hover的对象 -->
        <el-avatar
          src=""
          icon="el-icon-user-solid"
          :size="30"
          slot="reference"
          style=""
        >
        </el-avatar>
      </el-popover>
    </template>
    <loginView
      :login_ed.sync="this.$root.userData.login_ed"
      ref="loginView"
    ></loginView>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import loginView from '@/views/GoodsUser/login'

export default {
  inject: ["reload"],
  filters:{
    typeFilter(status){
      const statusMap = {
        1: '生产商',
        2: '加工商',
        3: '运输商',
        4: '销售商',
        5: '监管人员',
      }
      return statusMap[status]
    },
  },
  components: {
    Breadcrumb,
    Hamburger,
    loginView,
  },
  computed: {
    ...mapGetters([
      'sidebar'
    ])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    changeLoginView() {
      this.$refs.loginView.loginVisible = true; // 修改子组件数据
    },
    log_out() {
      this.$root.cleanUserData();
      this.reload();
    },
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }
  .login-container{
    line-height: 26px;
    display: flex;
    height: 100%;
    float: right;
    margin-right: 4%;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;
    justify-content: center;
    align-items: center
  }

  .flogin-container{
    line-height: 26px;
    display: flex;
    height: 100%;
    float: right;
    margin-right: 4%;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;
    justify-content: center;
    align-items: center
  }

  .breadcrumb-container {
    float: left;
  }


}
</style>
