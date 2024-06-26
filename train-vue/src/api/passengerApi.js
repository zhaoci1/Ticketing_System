import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    console.log(passenger);
    return myAxios.post("member/passenger/save", passenger);
  },
  pageList(page) {
    return myAxios.get("member/passenger/queryList", {
      params: {
        page: page.page,
        size: page.size,
      },
    });
  },
  delete(id) {
    return myAxios.delete("member/passenger/delete/" + id);
  },
  queryMine() {
    return myAxios.get("member/passenger/query-mine");
  },
  doConfirm(data) {
    return myAxios.post("business/confirm-order/do", data);
  },
  queryLineCount(id) {
    return myAxios.get("business/confirm-order/query-line-count/" + id);
  },
  cancel(id) {
    return myAxios.get("business/confirm-order/cancel/" + id);
  },

};

export default Axios;
