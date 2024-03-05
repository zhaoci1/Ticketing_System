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
    // 请求的时候，先执行这里的代码，再发送请求
    //  可以用以设置请求头，跨域之类的
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

// 设置响应拦截器
myAxios.interceptors.response.use(
  function (response) {
    // 获取响应数据的时候，对数据进行处理再返回
    return response.data;
  },
  function (error) {
    return Promise.reject(error);
  }
);

export default myAxios;
