import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("business/admin/sk-token/save", passenger);
  },
  pageList(page) {
    return myAxios.get("business/admin/sk-token/queryList", {
      params: {
        page: page.page,
        size: page.size,
        trainCode: page.trainCode,
      },
    });
  },
  delete(id) {
    return myAxios.delete("business/admin/sk-token/delete/" + id);
  },
  queryAll() {
    return myAxios.get("business/admin/train/queryAll");
  },
};

export default Axios;
