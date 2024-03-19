import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("business/admin/daily-train-seat/save", passenger);
  },
  pageList(page) {
    return myAxios.get("business/admin/daily-train-seat/queryList", {
      params: {
        page: page.page,
        size: page.size,
        trainCode: page.trainCode,
        date: page.date,
      },
    });
  },
  delete(id) {
    return myAxios.delete("business/admin/daily-train-seat/delete/" + id);
  },
  genSeat(trainCode) {
    return myAxios.get("business/admin/daily-train-seat/gen-seat/" + trainCode);
  },
};

export default Axios;
