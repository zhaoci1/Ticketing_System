import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("business/station/save", passenger);
  },
  pageList(page) {
    return myAxios.get("business/station/queryList", {
      params: {
        page: page.page,
        size: page.size,
      },
    });
  },
  delete(id) {
    return myAxios.delete("business/station/delete/"+id);
  },
  queryAll() {
    return myAxios.get("business/station/queryAll");
  },
};

export default Axios;
