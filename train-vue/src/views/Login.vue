<template>
  <div>
    <a-row class="login">
      <a-col :span="8" :offset="8" class="login-main">
        <h1 style="text-align: center">
          <rocket-two-tone />&nbsp;甲蛙12306售票系统
        </h1>
        <a-form
          :model="loginForm"
          name="basic"
          autocomplete="off"
          @finish="onFinish"
          @finishFailed="onFinishFailed"
        >
          <a-form-item
            label=""
            name="mobile"
            :rules="[{ required: true, message: '请输入手机号!' }]"
          >
            <a-input v-model:value="loginForm.mobile" placeholder="手机号" />
          </a-form-item>

          <a-form-item
            label=""
            name="code"
            :rules="[{ required: true, message: '请输入验证码!' }]"
          >
            <a-input v-model:value="loginForm.code">
              <template #addonAfter>
                <a @click="sendCode">获取验证码</a>
              </template>
            </a-input>
          </a-form-item>

          <a-form-item>
            <a-button type="primary" block @click="login">登录</a-button>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import { defineComponent, reactive } from "vue";
import { message } from "ant-design-vue";
import Axios from "@/api/loginApi";
import { useRouter } from "vue-router";
export default defineComponent({
  name: "login",
  setup() {
    const router = useRouter();
    const loginForm = reactive({
      mobile: "12345678909",
      code: "",
    });
    const onFinish = (values) => {
      console.log("Success:", values);
    };
    const onFinishFailed = (errorInfo) => {
      console.log("Failed:", errorInfo);
    };
    const sendCode = () => {
      Axios.sendCode(loginForm).then((res) => {
        if (res.code == 200) {
          message.success("验证码已发送");
          loginForm.code = res.data;
        } else {
          message.error("验证码获取失败!");
        }
      });
    };
    const login = () => {
      Axios.login(loginForm).then((res) => {
        if (res.code == 200) {
          message.success("登录成功");
          router.push("/main");
        } else {
          message.error("登录失败!");
        }
      });
    };
    return {
      login,
      loginForm,
      onFinish,
      onFinishFailed,
      sendCode,
      router,
    };
  },
});
</script>
<style>
.login-main h1 {
  font-size: 25px;
  font-weight: bold;
}
.login-main {
  margin-top: 100px;
  padding: 30px 30px 20px;
  border: 2px solid grey;
  border-radius: 10px;
  background-color: #fcfcfc;
}
</style>
