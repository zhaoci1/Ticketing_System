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
};

export default Axios;
