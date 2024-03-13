import myAxios from "@/utils/request";

let Axios = {
  save(passenger) {
    return myAxios.post("member/passenger/save", passenger);
  },
  pageList(page) {
    return myAxios.get("member/passenger/queryList", {
      params: {
        page: page.page,
        size: page.size,
      },
    });
  },
  delete(id) {
    return myAxios.delete("member/passenger/delete/"+id);
  },
};

export default Axios;
