import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("member/ticket/save", passenger);
  },
  pageList(page) {
    return myAxios.get("member/ticket/queryList", {
      params: {
        page: page.page,
        size: page.size,
        trainCode: page.trainCode,
      },
    });
  },
  delete(id) {
    return myAxios.delete("member/ticket/delete/" + id);
  },
};

export default Axios;
