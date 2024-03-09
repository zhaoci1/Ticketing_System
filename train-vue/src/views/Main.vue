<template>
  <a-layout id="components-layout-demo-top-side-2">
    <TheHeaderView></TheHeaderView>
    <a-layout>
      <the-sider-view></the-sider-view>
     <a-layout-content :style="{
      background:'#fff',padding:'20px'
     }">
     <router-view></router-view>
     </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script>
import {
  UserOutlined,
  LaptopOutlined,
  NotificationOutlined,
} from "@ant-design/icons-vue";
import { defineComponent, ref } from "vue";
import TheHeaderView from "@/components/the-header";
import TheSiderView from "@/components/the-sider.vue";
import store from "@/store";
import Axios from "@/api/loginApi";

export default defineComponent({
  components: {
    TheSiderView,
    TheHeaderView,
  },
  setup() {
    const count = ref();
    Axios.getCount().then((res) => {
      if (res.code == 200) {
        count.value = res.data
      } else {
        message.error("登录失败!");
      }
    });
    return {
      count
    };
  },
  mounted() {
    console.log(store.state.member.token);
  },
});
</script>
<style>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>
