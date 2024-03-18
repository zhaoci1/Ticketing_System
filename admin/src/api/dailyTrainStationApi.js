import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("business/admin/daily-train-station/save", passenger);
  },
  pageList(page) {
    return myAxios.get("business/admin/daily-train-station/queryList", {
      params: {
        page: page.page,
        size: page.size,
        trainCode: page.code,
        date: page.date,
      },
    });
  },
  delete(id) {
    return myAxios.delete("business/admin/daily-train-station/delete/" + id);
  },
  genSeat(trainCode) {
    return myAxios.get("business/admin/daily-train-station/gen-seat/" + trainCode);
  },
};

export default Axios;
