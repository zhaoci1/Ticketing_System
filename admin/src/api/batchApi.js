import myAxios from "@/utils/request";

let Axios = {
  save(batch) {
    return myAxios.post("batch/admin/job/add", batch);
  },
  query() {
    return myAxios.get("batch/admin/job/query");
  },
  delete(req) {
    console.log(req);
    return myAxios.post("batch/admin/job/delete", {
      name: req.name,
      group: req.group,
    });
  },
  pause(req) {
    return myAxios.post("batch/admin/job/pause", {
      name: req.name,
      group: req.group,
    });
  },
  reschedule(req) {
    return myAxios.post("batch/admin/job/reschedule", {
      name: req.name,
      group: req.group,
    });
  },
  resume(req) {
    return myAxios.post("batch/admin/job/resume", {
      name: req.name,
      group: req.group,
    });
  },
  run(req) {
    return myAxios.post("batch/admin/job/run", {
      name: req.name,
      group: req.group,
    });
  },
};

export default Axios;
