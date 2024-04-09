import myAxios from "@/utils/request";

let Axios = {
  pageList(page) {
    return myAxios.get("business/daily-train-ticket/queryList", {
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
  queryTrain(obj) {
    return myAxios.get(
      "business/daily_train_station_member/query-by-train-code",
      {
        params:obj
      }
    );
  },
};

export default Axios;
