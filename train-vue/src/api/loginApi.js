import myAxios from "@/utils/request";

let Axios = {
    sendCode(loginForm) {
      console.log(loginForm.mobile);
    return myAxios.post("member/member/sendCode", {
      mobile: loginForm.mobile,
    });
  },
};

export default Axios;
