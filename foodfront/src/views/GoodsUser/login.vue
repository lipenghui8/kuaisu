<template>
  <el-dialog title="登录" :visible.sync="loginVisible" width="500px" top="30vh" :lock-scroll="false">
    <!-- Form 组件提供了表单验证的功能，只需要通过 rules 属性传入约定的验证规则，
    并将 Form-Item 的 prop 属性设置为需校验的字段名即可 -->
    <el-form
      ref="loginForm"
      :model="loginForm"
      title="login"
      style="width: 300px; margin: 0 auto"
      :rules="loginRules"
    >
      <el-form-item prop="account">
        <el-input
          v-model="loginForm.account"
          placeholder="账号"
          maxlength="20"
          title="账号"
          @keyup.enter.native="loginAction('loginForm')"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          placeholder="密码"
          maxlength="20"
          title="密码"
          @keyup.enter.native="loginAction('loginForm')"
        ></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input
          v-model="loginForm.code"
          placeholder="验证码"
          style="width: 150px"
          maxlength="6"
          title="验证码"
          @keyup.enter.native="loginAction('loginForm')"
        ></el-input>
        <div
          class="codeImgContent"
          title="看不清？点击更改验证码"
          style="
            width: 90px;
            height: 40px;
            border: solid 1px gray;
            float: right;
            cursor: pointer;
            border-radius: 3px;
          "
          @click="changeCode"
        >
          <img
            :src="codeUrl"
            alt="加载中..."
            style="width: inherit; height: inherit"
          />
        </div>
      </el-form-item>
      <el-link type="primary" style="float: right">忘记密码？</el-link>
      <el-button
        type="primary"
        @click="loginAction('loginForm')"
        style="width: 300px; margin-top: 10px"
        >登录</el-button
      >
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-link type="primary" @click="goToRegister()">立即注册</el-link>
    </span>
  </el-dialog>
</template>
<script>
// import axios from "axios";
import Qs from "qs";
import {login} from "@/api/user";

export default {
  inject: ["reload"],
  name: "loginView",
  data() {
    return {
      codeUrl: "", //验证码url
      //用于限制发送的时间间隔
      codeState: false,
      loginForm: {
        account: "",
        password: "",
        code: "",
      },
      // //login子组件的login_ed属性，watch监听到变化时会传递给父组件
      // login_ed_comp: false,
      //登录页面可见性
      loginVisible: false,
      //验证码的图片base64字符串
      codeImageBase: "",
      loginRules: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            min: 5,
            max: 20,
            message: "长度在 5 到 20 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            max: 20,
            message: "长度在 5 到 20 个字符",
            trigger: "blur",
          },
        ],
        code: [
          {
            required: true,
            message: "请输入验证码",
            trigger: "blur",
          },
        ],
      },
    };
  },

  methods: {
    //点击改变验证码
    changeCode() {

      if (this.codeState == false) {
        //设置不可用状态，间隔1000ms
        let that = this;
        that.codeState = true;
        this.codeUrl = "";
        setTimeout(() => {
          //请求数据。。。
          //请求成功后在返回的状态里面加上下面这句话
          that.codeState = false;
          //RequestUrl是全局变量
          this.codeUrl =
            this.backApi + "/user/getImage?" + new Date().valueOf();
        }, 1000);
      } else {
        this.$message.error("过于频繁！");
        return;
      }
    },
    goToRegister() {
      this.$router.push("/GoodsUser/register");
      this.loginVisible = false;
    },
    loginAction(formName) {
      //按钮前验证，需要给form设置ref，给button的click调用设置参数为表格数据
      //在refs列表中找到ref名为formName的
      let axiosThis = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let data = Qs.stringify({
            account: this.loginForm.account,
            password: this.loginForm.password,
            code: this.loginForm.code,
          });
          login(data).then((res) => {
                //请求成功
                // JSON.s;
                let responseData = res;
                if (responseData.state == "登录成功") {
                  axiosThis.$message.success("登录成功！");
                  // 用全局变量代替了
                  axiosThis.loginVisible = false;
                  axiosThis.$root.setUserData(
                    responseData.id,
                    responseData.account,
                    responseData.username,
                    responseData.avator,
                    true,
                    responseData.worktype,
                    responseData.location,
                  );
                  // axiosThis.$root.userData.login_ed=true;
                  //重载页面
                  axiosThis.reload();
                } else {
                  //请求失败
                  axiosThis.$message.error(responseData.state+"请联系管理员");
                  axiosThis.changeCode();
                  return;
                }
              }).catch((err) => {
            this.$message.error("请求失败，无法登录");
          });
        } else {
          //valid
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
  watch: {

    loginVisible(newVal) {
      if (newVal == true) {
        this.changeCode();
      }
    },
  },
  // created:function(){
  //   this.changeCode();
  // },
  mounted: function () {
    // this.changeCode();
  },
};
</script>

