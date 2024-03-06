import { createRouter, createWebHistory } from "vue-router";
import store from "@/store";
import { notification } from "ant-design-vue";

const routes = [
  {
    path: "/login",
    name: "login",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/Login.vue"),
  },
  {
    path: "/",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/Main.vue"),
    // 自定义变量meta
    meta: {
      loginRequire: true,
    },
  },
  {
    path: "/main",
    name: "main",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/Main.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});
router.beforeEach((to, from, next) => {
  if (
    // 对自定义变量进行校验，login因为有变量，所以返回值为true，其他没有这个变量的，默认都是false，也就是说都会被拦截
    to.matched.some(function (item) {
      console.log(item, "是否需要登录校验：", item.meta.loginRequire || false);
      return item.meta.loginRequire;
    })
  ) {
    const member = store.state.member;
    console.log("页面登录校验开始：", member);
    // token不存在则表示未登录或者超时
    if (!member.token) {
      console.log("用户未登录或者登录超时！");
      notification.error({ description: "未登录或登录超时" });
      next("/login");
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
