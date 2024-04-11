import myAxios from "@/utils/request";

let Axios = {
  query(obj) {
    return myAxios.get("business/seat_sell/query", {
      params: obj,
    });
  },
};

export default Axios;
