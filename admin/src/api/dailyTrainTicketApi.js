import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("business/admin/daily-train-ticket/save", passenger);
  },
  pageList(page) {
    return myAxios.get("business/admin/daily-train-ticket/queryList", {
      params: {
        page: page.page,
        size: page.size,
        trainCode: page.trainCode,
        date: page.date,
        start: page.start,
        end: page.end,
      },
    });
  },
  delete(id) {
    return myAxios.delete("business/admin/daily-train-ticket/delete/" + id);
  },
};

export default Axios;
