import myAxios from "@/utils/request";

let Axios = {
  sendCode(loginForm) {
    return myAxios.post("member/member/sendCode", {
      mobile: loginForm.mobile,
    });
  },
  login(loginForm) {
    console.log(loginForm);
    return myAxios.post("member/member/login", loginForm);
  },
};

export default Axios;
