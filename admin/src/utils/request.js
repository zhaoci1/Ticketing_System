import axios from "axios";

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
    return response.data;
  },
  function (error) {
    return Promise.reject(error);
  }
);

export default myAxios;
