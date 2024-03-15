import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("business/admin/train-station/save", passenger);
  },
  pageList(page) {
    return myAxios.get("business/admin/train-station/queryList", {
      params: {
        page: page.page,
        size: page.size,
      },
    });
  },
  delete(id) {
    return myAxios.delete("business/admin/train-station/delete/"+id);
  },
};

export default Axios;
