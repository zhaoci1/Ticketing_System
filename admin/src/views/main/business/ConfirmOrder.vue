<template>
  <div>
    <p>
      <a-space>
        <a-button type="primary" @click="handleQuery()">刷新</a-button>
      </a-space>
    </p>
    <a-table
      :dataSource="confirmOrders"
      :columns="columns"
      :pagination="pagination"
      @change="handleTableChange"
      :loading="loading"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'operation'"> </template>
        <template v-else-if="column.dataIndex === 'type'">
          <span v-for="item in CONFIRM_ORDER_STATUS_ARRAY" :key="item.code">
            <span v-if="item.code === record.status"> {{ item.desc }}</span>
          </span>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script>
import Axios from "@/api/confirmOrderApi";
import { message } from "ant-design-vue";
import { defineComponent, ref, onMounted } from "vue";

export default defineComponent({
  name: "confirm-order-view",
  setup() {
    const CONFIRM_ORDER_STATUS_ARRAY = window.CONFIRM_ORDER_STATUS_ARRAY;
    const visible = ref(false);
    let confirmOrder = ref({
      id: undefined,
      memberId: undefined,
      date: undefined,
      trainCode: undefined,
      start: undefined,
      end: undefined,
      dailyTrainTicketId: undefined,
      tickets: undefined,
      status: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    let loading = ref(false);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 2,
    });
    const confirmOrders = ref([]);

    const columns = [
      {
        title: "会员id",
        dataIndex: "memberId",
        key: "memberId",
      },
      {
        title: "日期",
        dataIndex: "date",
        key: "date",
      },
      {
        title: "车次编号",
        dataIndex: "trainCode",
        key: "trainCode",
      },
      {
        title: "出发站",
        dataIndex: "start",
        key: "start",
      },
      {
        title: "到达站",
        dataIndex: "end",
        key: "end",
      },
      {
        title: "余票ID",
        dataIndex: "dailyTrainTicketId",
        key: "dailyTrainTicketId",
      },
      {
        title: "车票",
        dataIndex: "tickets",
        key: "tickets",
      },
      {
        title: "订单状态",
        dataIndex: "status",
        key: "status",
      },
    ];

    const handleTableChange = (pagination) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize,
      });
    };

    const handleQuery = (page) => {
      if (!page) {
        page = {
          page: 1,
          size: pagination.value.pageSize,
        };
      }
      loading.value = true;
      Axios.pageList(page).then((res) => {
        loading.value = false;
        if (res.code == 200) {
          confirmOrders.value = res.data.list;
          pagination.value.current = page.page;
          pagination.value.total = res.data.total;
        } else {
          message.error("查询失败");
        }
      });
    };
    onMounted(() => {
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize,
      });
    });
    return {
      CONFIRM_ORDER_STATUS_ARRAY,
      confirmOrder,
      visible,
      confirmOrders,
      handleTableChange,
      columns,
      onMounted,
      handleQuery,
      pagination,
      loading,
    };
  },
});
</script>
