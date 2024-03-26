import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("business/admin/confirm-order/save", passenger);
  },
  pageList(page) {
    return myAxios.get("business/admin/confirm-order/queryList", {
      params: {
        page: page.page,
        size: page.size,
        trainCode: page.trainCode,
      },
    });
  },
  delete(id) {
    return myAxios.delete("business/admin/confirm-order/delete/" + id);
  },
};

export default Axios;
