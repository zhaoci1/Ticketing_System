import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("business/admin/train-seat/save", passenger);
  },
  pageList(page) {
    return myAxios.get("business/admin/train-seat/queryList", {
      params: {
        page: page.page,
        size: page.size,
        trainCode: page.trainCode,
      },
    });
  },
  delete(id) {
    return myAxios.delete("business/admin/train-seat/delete/"+id);
  },
};

export default Axios;
