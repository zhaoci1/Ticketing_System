import axios from "axios";
import store from "@/store";
import { notification } from "ant-design-vue";
import router from "@/router";

let myAxios = axios.create({
  // 设置统一请求路径前缀
  baseURL: process.env.VUE_APP_SERVER,
  timeout: 3000,
  headers: { "X-Custom-Header": "foobar" },
  ContentType: "application/json",
});

// 设置请求拦截器
myAxios.interceptors.request.use(
  function (config) {
    // 请求的时候，先执行这里的代码，再发送请求
    //  可以用以设置请求头，跨域之类的
    const token = store.state.member.token;
    if (token) {
      config.headers.token = token;
      console.log("请求头添加了token");
    }
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

// 设置响应拦截器
myAxios.interceptors.response.use(
  function (response) {
    console.log("返回结果", response);
    // 获取响应数据的时候，对数据进行处理再返回
    return response.data;
  },
  function (error) {
    console.log("返回错误", error);
    const res = error.response;
    const status = res.status;
    if (status === 401) {
      console.log("未登录或者登录超时，正在跳回登录页");
      store.commit("setMember", {});
      notification.error({ description: "未登录或者登录超时" });
      router.push("/login");
    }
    return Promise.reject(error);
  }
);

export default myAxios;
