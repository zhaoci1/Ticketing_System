<template>
  <div>
    <p>
      <a-space>
        <the-select v-model="param.trainCode"></the-select>
        <a-button type="primary" @click="handleQuery()">查找</a-button>
      </a-space>
    </p>
    <a-table
      :dataSource="dailyTrainSeats"
      :columns="columns"
      :pagination="pagination"
      @change="handleTableChange"
      :loading="loading"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'operation'">
          <a-space>
            <a-popconfirm
              title="删除后不可恢复，确认删除?"
              @confirm="onDelete(record)"
              ok-text="确认"
              cancel-text="取消"
            >
              <a style="color: red">删除</a>
            </a-popconfirm>
            <a @click="onEdit(record)">编辑</a>
          </a-space>
        </template>
        <template v-else-if="column.dataIndex === 'col'">
          <span v-for="item in SEAT_COL_ARRAY" :key="item.code">
            <span
              v-if="item.code === record.col && item.type === record.seatType"
            >
              {{ item.desc }}
            </span>
          </span>
        </template>
        <template v-else-if="column.dataIndex === 'seatType'">
          <span v-for="item in SEAT_TYPE_ARRAY" :key="item.code">
            <span v-if="item.code === record.seatType">
              {{ item.desc }}
            </span>
          </span>
        </template>
      </template>
    </a-table>
    <a-modal
      v-model:visible="visible"
      title="每日座位"
      @ok="handleOk"
      ok-text="确认"
      cancel-text="取消"
    >
      <a-form
        :model="dailyTrainSeat"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 20 }"
      >
        <a-form-item label="日期">
          <a-date-picker
            v-model:value="dailyTrainSeat.date"
            valueFormat="YYYY-MM-DD"
            placeholder="请选择日期"
          />
        </a-form-item>
        <a-form-item label="车次编号">
            <the-select v-model="dailyTrainSeat.trainCode"></the-select>
        </a-form-item>
        <a-form-item label="箱序">
          <a-input v-model:value="dailyTrainSeat.carriageIndex" />
        </a-form-item>
        <a-form-item label="排号">
          <a-input v-model:value="dailyTrainSeat.row" />
        </a-form-item>
        <a-form-item label="列号">
          <a-select v-model:value="dailyTrainSeat.col">
            <a-select-option
              v-for="item in SEAT_COL_ARRAY"
              :key="item.code"
              :value="item.code"
            >
              {{ item.desc }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="座位类型">
          <a-select v-model:value="dailyTrainSeat.seatType">
            <a-select-option
              v-for="item in SEAT_TYPE_ARRAY"
              :key="item.code"
              :value="item.code"
            >
              {{ item.desc }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="同车箱座序">
          <a-input v-model:value="dailyTrainSeat.carriageSeatIndex" />
        </a-form-item>
        <a-form-item label="售卖情况">
          <a-input v-model:value="dailyTrainSeat.sell" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import Axios from "@/api/dailyTrainSeatApi";
import theSelect from "@/components/the-select.vue";
import { message } from "ant-design-vue";
import { defineComponent, ref, onMounted } from "vue";

export default defineComponent({
  components: { theSelect },
  name: "daily-train-seat-view",
  setup() {
    const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
    const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
    const visible = ref(false);
    let dailyTrainSeat = ref({
      id: undefined,
      date: undefined,
      trainCode: undefined,
      carriageIndex: undefined,
      row: undefined,
      col: undefined,
      seatType: undefined,
      carriageSeatIndex: undefined,
      sell: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    let loading = ref(false);
    let param = ref({});
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    const dailyTrainSeats = ref([]);

    const columns = [
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
        title: "箱序",
        dataIndex: "carriageIndex",
        key: "carriageIndex",
      },
      {
        title: "排号",
        dataIndex: "row",
        key: "row",
      },
      {
        title: "列号",
        dataIndex: "col",
        key: "col",
      },
      {
        title: "座位类型",
        dataIndex: "seatType",
        key: "seatType",
      },
      {
        title: "同车箱座序",
        dataIndex: "carriageSeatIndex",
        key: "carriageSeatIndex",
      },
      {
        title: "售卖情况",
        dataIndex: "sell",
        key: "sell",
      },
    ];
    const onEdit = (record) => {
      dailyTrainSeat.value = { ...record };
      visible.value = true;
    };
    const onDelete = (record) => {
      Axios.delete(record.id).then((res) => {
        if (res.data) {
          message.success("删除成功");
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.success("删除失败");
        }
      });
    };
    const handleOk = (e) => {
      Axios.save(dailyTrainSeat.value).then((res) => {
        if (res.data) {
          message.success("保存成功");
          visible.value = false;
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error("保存失败");
        }
        console.log(res);
      });
      visible.value = false;
    };

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
      page.trainCode = param.value.trainCode;
      Axios.pageList(page).then((res) => {
        loading.value = false;
        if (res.code == 200) {
          dailyTrainSeats.value = res.data.list;
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
      SEAT_COL_ARRAY,
      SEAT_TYPE_ARRAY,
      dailyTrainSeat,
      visible,
      dailyTrainSeats,
      handleTableChange,
      columns,
      onMounted,
      handleQuery,
      pagination,
      loading,
      handleOk,
      onEdit,
      onDelete,
      param,
    };
  },
});
</script>
