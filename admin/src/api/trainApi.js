import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("business/admin/train/save", passenger);
  },
  pageList(page) {
    return myAxios.get("business/admin/train/queryList", {
      params: {
        page: page.page,
        size: page.size,
      },
    });
  },
  delete(id) {
    return myAxios.delete("business/admin/train/delete/"+id);
  },
  genSeat(trainCode) {
    return myAxios.get("business/admin/train/gen-seat/"+trainCode);
  },
};

export default Axios;
