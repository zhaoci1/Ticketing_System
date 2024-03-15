import { createRouter, createWebHistory } from "vue-router";
import store from "@/store";
import { notification } from "ant-design-vue";

const routes = [
  {
    path: "/",
    component: () => import("../views/Main.vue"),
    children: [
      {
        path: "welcome",
        component: () => import("../views/main/Welcome.vue"),
      },
      {
        path: "about",
        component: () => import("../views/main/About.vue"),
      },
      {
        path: "station",
        component: () => import("../views/main/Station.vue"),
      },
      {
        path: "train",
        component: () => import("../views/main/Train.vue"),
      },
      {
        path: "train-station",
        component: () => import("../views/main/TrainStation.vue"),
      },
      {
        path: "train-carriage",
        component: () => import("../views/main/TrainCarriage.vue"),
      },
      {
        path: "train-seat",
        component: () => import("../views/main/TrainSeat.vue"),
      },
    ],
  },
  {
    path: "",
    redirect: "/welcome",
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
