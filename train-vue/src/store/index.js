import { createStore } from "vuex";

export default createStore({
  state: {
    member: {},
  },
  getters: {},
  // 同步方法
  mutations: {
    setMember(state, member) {
      state.member = member;
    },
  },
  // 异步任务
  actions: {},
  modules: {},
});
