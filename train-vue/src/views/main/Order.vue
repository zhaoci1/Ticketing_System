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
    <a-divider></a-divider>
    <b>勾选要购票的乘客：</b>
    <a-checkbox-group
      v-model:value="passengerChecks"
      :options="passengerOptions"
    ></a-checkbox-group>
    <br />
    <a-table :dataSource="passengerChecks" :columns="columns">
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'seatTypeCode'">
          <a-select v-model:value="record.seatTypeCode">
            <a-select-option
              v-for="item in PASSENGER_TYPE_ARRAY"
              :key="item.code"
              :value="item.code"
            >
              {{ item.desc }}
            </a-select-option>
          </a-select>
        </template>
        <template v-else-if="column.dataIndex === 'passengerType'">
          <a-select v-model:value="record.passengerType">
            <a-select-option
              v-for="item in SEAT_TYPE_ARRAY"
              :key="item.code"
              :value="item.code"
            >
              {{ item.desc }}
            </a-select-option>
          </a-select>
        </template>
      </template>
    </a-table>

    <a-button @click="finishCheckPassenger">提交订单</a-button>
    <a-modal v-model:visible="visible" title="Basic Modal" @ok="handleOk">
      <a-table :dataSource="passengerChecks" :columns="columns">
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'passengerCode'">
            <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code">
              <span v-if="item.code === record.passengerCode">
                {{ item.desc }}
              </span>
            </span>
          </template>
          <template v-else-if="column.dataIndex === 'passengerType'">
            <span v-for="item in SEAT_TYPE_ARRAY" :key="item.code">
              <span v-if="item.code === record.passengerType">
                {{ item.desc }}
              </span>
            </span>
          </template>
        </template>
      </a-table>
      <div v-if="chooseSeatType === 0" style="color: red">
        购买的车票不支持选座(只有座位类型一致，且余票大于20时才能选座)
      </div>
      <div v-else style="text-align: center">
        <!-- 只买一张票时，显示一排座位，多个座位时，显示两排座位 -->
        <a-switch
          class="choose-seat-item"
          v-for="item in SEAT_COL_ARRAY"
          :key="item.code"
          v-model:checked="chooseSeatObj[item.code + '1']"
          :checked-children="item.desc"
          :un-checked-children="item.desc"
        />
        <div v-if="passengerChecks.length > 1">
          <a-switch
            class="choose-seat-item"
            v-for="item in SEAT_COL_ARRAY"
            :key="item.code"
            v-model:checked="chooseSeatObj[item.code + '2']"
            :checked-children="item.desc"
            :un-checked-children="item.desc"
          />
        </div>
        <div style="color: #999999">
          提示：你可以选择{{ passengerChecks.length }}个座位
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { computed, defineComponent, onMounted, ref, watch } from "vue";
import Axios from "@/api/passengerApi";
import { message } from "ant-design-vue";

export default defineComponent({
  setup() {
    const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE_ARRAY;
    const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
    const passengerList = ref([]);
    // 展示所有的乘客
    const passengerOptions = ref([]);
    // 获取被勾选的乘客
    const passengerChecks = ref([]);
    // 用来存储乘客的购买信息
    const tickets = ref([]);
    const dailyTrainTicket = SessionStorage.get("dailyTrainTicket") || {};
    const SEAT_TYPE = window.SEAT_TYPE;

    const seatTypes = [];

    // 0：不支持选座，1：一等座，2：二等座
    const chooseSeatType = ref(0);
    // 根据选择的座位类型，筛选出对应的列
    const SEAT_COL_ARRAY = computed(() => {
      return window.SEAT_COL_ARRAY.filter(
        (item) => item.type === chooseSeatType.value
      );
    });

    // 支持选座的时候该对象才会有数据
    const chooseSeatObj = ref({});

    // 初始化座位
    watch(
      () => SEAT_COL_ARRAY.value,
      () => {
        chooseSeatObj.value = {};
        for (let i = 1; i <= 2; i++) {
          SEAT_COL_ARRAY.value.forEach((item) => {
            chooseSeatObj.value[item.code + i] = false;
          });
        }
      },
      { immediate: true }
    );

    let visible = ref(false);

    const columns = [
      {
        title: "姓乘客名",
        dataIndex: "name",
        key: "name",
      },
      {
        title: "身份证",
        dataIndex: "idCard",
        key: "idCard",
      },
      {
        title: "票种",
        dataIndex: "seatTypeCode",
        key: "seatTypeCode",
      },
      {
        title: "座位类型",
        dataIndex: "passengerType",
        key: "passengerType",
      },
    ];

    // 遍历类型数组
    for (let KEY in SEAT_TYPE) {
      // 将当前遍历的对象转为小写
      let key = KEY.toLowerCase();
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
    const handleQueryPassenger = () => {
      Axios.queryMine().then((res) => {
        passengerList.value = res.data;
        // 将数据填充到选项里面去
        passengerList.value.forEach((item) =>
          passengerOptions.value.push({
            label: item.name,
            value: item,
          })
        );
      });
    };
    const handleOk = () => {
      // 设置每张票的座位,先清空座位
      for (let i = 0; i < passengerChecks.value.length; i++) {
        passengerChecks.value[i].seat = null;
      }
      let i = -1;
      for (const key in chooseSeatObj.value) {
        if (chooseSeatObj.value[key]) {
          i++;
          if (i > passengerChecks.value.length - 1) {
            message.error("选座数大于购票数");
            return;
          }
          passengerChecks.value[i].seat = key;
        }
      }
      if (i > -1 && i < passengerChecks.value.length - 1) {
        message.error("选座数小于购票数");
        return;
      }
      console.log("passengerChecks", passengerChecks.value);
      console.log("tickets", tickets.value);
      let ticketList = [];
      passengerChecks.value.forEach((item) => {
        ticketList.push({
          passengerId: item.id,
          passengerName: item.name,
          passengerIdCard: item.idCard,
          passengerType: item.passengerType,
          seat: item.seat,
          seatTypeCode: item.seatTypeCode,
        });
      });
      Axios.doConfirm({
        dailyTrainTicketId: dailyTrainTicket.id,
        date: dailyTrainTicket.date,
        trainCode: dailyTrainTicket.trainCode,
        start: dailyTrainTicket.start,
        end: dailyTrainTicket.end,
        tickets: ticketList,
      }).then(res=>{
        if(res.code!=200){
          message.error(res.message)
        }else{
          message.success(res.message)
        }
      });
    };
    const finishCheckPassenger = () => {
      let seatTypesTemp = JSON.parse(JSON.stringify(seatTypes));
      for (let i = 0; i < passengerChecks.value.length; i++) {
        let ticket = passengerChecks.value[i];
        for (let j = 0; j < seatTypesTemp.length; j++) {
          let seatType = seatTypesTemp[j];
          if (ticket.passengerType === seatType.code) {
            seatType.count--;
            if (seatType.count < 0) {
              message.error("余票不足");
              return;
            }
          }
        }
      }
      console.log("校验通过");
      // 判断是否支持选座
      let ticketSeatTypeCodes = [];
      for (let i = 0; i < passengerChecks.value.length; i++) {
        let ticket = passengerChecks.value[i];
        ticketSeatTypeCodes.push(ticket.passengerType);
      }
      // 为购票列表中的所有座位类型去重
      const ticketSeatTypeCodesSet = Array.from(new Set(ticketSeatTypeCodes));
      // 选择了多个种类的座位，就不支持选座了
      if (ticketSeatTypeCodesSet.length !== 1) {
        chooseSeatType.value = 0;
      } else {
        // 表示乘客统一选择了一种座位
        if (ticketSeatTypeCodesSet[0] === SEAT_TYPE.YDZ.code) {
          console.log("一等座选座");
          chooseSeatType.value = SEAT_TYPE.YDZ.code;
        } else if (ticketSeatTypeCodesSet[0] === SEAT_TYPE.EDZ.code) {
          console.log("二等座选座");
          chooseSeatType.value = SEAT_TYPE.EDZ.code;
        } else {
          console.log("不支持选座");
          chooseSeatType.value = 0;
        }
      }

      // 当余票小于20时，不允许选座，不然的话选座成功率不高，还影响出票
      if (chooseSeatType.value != 0) {
        for (let i = 0; i < seatTypes.length; i++) {
          let seatType = seatTypes[i];
          if (ticketSeatTypeCodes[0] === seatType.code) {
            if (seatType.count < 20) {
              console.log("余票小于20！不支持选座！");
              chooseSeatType.value = 0;
              break;
            }
          }
        }
      }

      visible.value = true;
    };
    watch(
      () => passengerChecks.value,
      (newVal, oldVal) => {
        // 每次有变化时，把购票列表情况，然后重新构造列表
        tickets.value = [];
        passengerChecks.value.forEach((item) =>
          tickets.value.push({
            passengerId: item.id,
            passengerType: item.type,
            // 默认选中一等座
            seatTypeCode: seatTypes[0].code,
            passengerName: item.name,
            passengerIdCard: item.idCard,
          })
        );
        for (let i = 0; i < tickets.value.length; i++) {
          passengerChecks.value[i].seatTypeCode = tickets.value[i].seatTypeCode;

          passengerChecks.value[i].passengerType =
            tickets.value[i].passengerType;
        }
      },
      { immediate: true }
    );
    onMounted(() => {
      handleQueryPassenger();
    });
    return {
      dailyTrainTicket,
      seatTypes,
      passengerList,
      passengerOptions,
      passengerChecks,
      tickets,
      columns,
      PASSENGER_TYPE_ARRAY,
      SEAT_TYPE_ARRAY,
      visible,
      finishCheckPassenger,
      handleOk,
      chooseSeatType,
      chooseSeatObj,
      SEAT_COL_ARRAY,
    };
  },
});
</script>

<style>
.order-train .order-train-main {
  font-size: 18px;
  font-weight: bold;
}

.choose-seat-item {
  margin: 5px 5px;
}
</style>
