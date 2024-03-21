<template>
  <div class="order-train">
    <span class="order-train-main">{{ dailyTrainTicket.date }}</span
    >&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.trainCode }}</span
    >次&nbsp; <span class="order-train-main">{{ dailyTrainTicket.start }}</span
    >站
    <span class="order-train-main">({{ dailyTrainTicket.startTime }})</span
    >&nbsp; <span class="order-train-main">——</span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.end }}</span
    >站
    <span class="order-train-main">({{ dailyTrainTicket.endTime }})</span>&nbsp;

    <div>
      <span v-for="item in seatTypes" :key="item.type">
        <span>{{ item.desc }}</span>
        <span>{{ item.price }}￥</span>&nbsp; <span>{{ item.count }}</span
        >&nbsp;张票&nbsp;&nbsp;
      </span>
    </div>
  </div>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  setup() {
    const dailyTrainTicket = SessionStorage.get("dailyTrainTicket") || {};
    const SEAT_TYPE = window.SEAT_TYPE;

    const seatTypes = [];

    // 遍历类型数组
    for (let KEY in SEAT_TYPE) {
      // 将当前遍历的对象转为小写
      let key = KEY.toLowerCase();
      console.log(key);
      // 获取key，该key表示当前车次的类型数据
      if (dailyTrainTicket[key] >= 0) {
        seatTypes.push({
          type: KEY,
          code: SEAT_TYPE[KEY]["code"],
          desc: SEAT_TYPE[KEY]["desc"],
          count: dailyTrainTicket[key],
          price: dailyTrainTicket[key + "Price"],
        });
      }
    }
    console.log(seatTypes);
    return {
      dailyTrainTicket,
      seatTypes,
    };
  },
});
</script>

<style>
.order-train .order-train-main {
  font-size: 18px;
  font-weight: bold;
}
</style>
