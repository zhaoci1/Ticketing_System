import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("member/admin/ticket/save", passenger);
  },
  pageList(page) {
    return myAxios.get("member/admin/ticket/queryList", {
      params: {
        page: page.page,
        size: page.size,
        trainCode: page.trainCode,
      },
    });
  },
  delete(id) {
    return myAxios.delete("member/admin/ticket/delete/" + id);
  },
};

export default Axios;
