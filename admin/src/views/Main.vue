<template>
  <a-layout id="components-layout-demo-top-side-2">
    <TheHeaderView></TheHeaderView>
    <a-layout>
      <the-sider-view></the-sider-view>
      <a-layout-content
        :style="{
          background: '#fff',
          padding: '20px',
        }"
      >
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
  setup() {},
});
</script>
<style>
</style>
