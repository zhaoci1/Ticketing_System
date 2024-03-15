import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("business/admin/train-carriage/save", passenger);
  },
  pageList(page) {
    return myAxios.get("business/admin/train-carriage/queryList", {
      params: {
        page: page.page,
        size: page.size,
      },
    });
  },
  delete(id) {
    return myAxios.delete("business/admin/train-carriage/delete/"+id);
  },
};

export default Axios;
