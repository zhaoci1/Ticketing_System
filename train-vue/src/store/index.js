import { createStore } from "vuex";
const MEMBER = "MEMBER";
export default createStore({
  state: {
    member: window.SessionStorage.get(MEMBER) || {},
  },
  getters: {},
  // 同步方法
  mutations: {
    setMember(state, member) {
      state.member = member;
      window.SessionStorage.set(MEMBER, member);
    },
  },
  // 异步任务
  actions: {},
  modules: {},
});
