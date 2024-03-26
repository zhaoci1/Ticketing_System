import { createRouter, createWebHistory } from "vue-router";

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
        path: "base/",
        children: [
          {
            path: "station",
            component: () => import("../views/main/base/Station.vue"),
          },
          {
            path: "train",
            component: () => import("../views/main/base/Train.vue"),
          },
          {
            path: "train-station",
            component: () => import("../views/main/base/TrainStation.vue"),
          },
          {
            path: "train-carriage",
            component: () => import("../views/main/base/TrainCarriage.vue"),
          },
          {
            path: "train-seat",
            component: () => import("../views/main/base/TrainSeat.vue"),
          },
        ],
      },
      {
        path: "batch/",
        children: [
          {
            path: "job",
            component: () => import("../views/main/batch/JobT.vue"),
          },
        ],
      },
      {
        path: "business/",
        children: [
          {
            path: "daily-train",
            component: () => import("../views/main/business/DailyTrain.vue"),
          },
          {
            path: "daily-train_station",
            component: () => import("../views/main/business/DailyTrainStation.vue"),
          },
          {
            path: "daily-train_carriage",
            component: () => import("../views/main/business/DailyTrainCarriage.vue"),
          },
          {
            path: "daily-train_seat",
            component: () => import("../views/main/business/DailyTrainSeat.vue"),
          },
          {
            path: "daily-train_ticket",
            component: () => import("../views/main/business/DailyTrainTicket.vue"),
          },
          {
            path: "confirm_order",
            component: () => import("../views/main/business/ConfirmOrder.vue"),
          },
        ],
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
