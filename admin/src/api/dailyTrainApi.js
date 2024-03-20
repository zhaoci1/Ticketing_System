import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("business/admin/daily-train/save", passenger);
  },
  pageList(page) {
    return myAxios.get("business/admin/daily-train/queryList", {
      params: {
        page: page.page,
        size: page.size,
        code: page.code,
        date: page.date,
      },
    });
  },
  delete(id) {
    return myAxios.delete("business/admin/daily-train/delete/" + id);
  },
  genSeat(trainCode) {
    return myAxios.get("business/admin/daily-train/gen-seat/" + trainCode);
  },
  genDaily(date) {
    return myAxios.get("business/admin/daily-train/gen-daily/" + date);
  },
};

export default Axios;
