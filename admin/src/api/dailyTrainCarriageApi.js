import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("business/admin/daily-train-carriage/save", passenger);
  },
  pageList(page) {
    return myAxios.get("business/admin/daily-train-carriage/queryList", {
      params: {
        page: page.page,
        size: page.size,
        trainCode: page.code,
        date: page.date,
      },
    });
  },
  delete(id) {
    return myAxios.delete("business/admin/daily-train-carriage/delete/" + id);
  },
};

export default Axios;
