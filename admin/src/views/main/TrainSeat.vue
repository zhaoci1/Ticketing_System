<template>
  <div>
    <p>
      <a-space>
        <the-select v-model="param.trainCode"></the-select>
        <a-button type="primary" @click="handleQuery()">查找</a-button>
        <a-button type="primary" @click="onAdd">新增</a-button>
      </a-space>
    </p>
    <a-table
      :dataSource="trainSeats"
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
        <template v-else-if="column.dataIndex === 'type'">
          <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.key">
            <span v-if="item.key === record.type">{{ item.value }}</span>
          </span>
        </template>
        <template v-else-if="column.dataIndex === 'type'">
          <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.key">
            <span v-if="item.key === record.type">{{ item.value }}</span>
          </span>
        </template>
      </template>
    </a-table>
    <a-modal
      v-model:visible="visible"
      title="座位"
      @ok="handleOk"
      ok-text="确认"
      cancel-text="取消"
    >
      <a-form
        :model="trainSeat"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 20 }"
      >
        <a-form-item label="车次编号">
          <the-select v-model="trainSeat.trainCode"></the-select>
        </a-form-item>
        <a-form-item label="厢序">
          <a-input v-model:value="trainSeat.carriageIndex" />
        </a-form-item>
        <a-form-item label="排号">
          <a-input v-model:value="trainSeat.row" />
        </a-form-item>
        <a-form-item label="列号">
          <a-select v-model:value="trainSeat.col">
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
          <a-select v-model:value="trainSeat.seatType">
            <a-select-option
              v-for="item in SEAT_TYPE_ARRAY"
              :key="item.code"
              :value="item.code"
            >
              {{ item.desc }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="同车厢座序">
          <a-input v-model:value="trainSeat.carriageSeatIndex" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import Axios from "@/api/trainSeatApi";
import theSelect from "@/components/the-select.vue";
import { message } from "ant-design-vue";
import { defineComponent, ref, onMounted } from "vue";

export default defineComponent({
  components: { theSelect },
  name: "train-seat-view",
  setup() {
    const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
    const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
    const visible = ref(false);
    let trainSeat = ref({
      id: undefined,
      trainCode: undefined,
      carriageIndex: undefined,
      row: undefined,
      col: undefined,
      seatType: undefined,
      carriageSeatIndex: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    let loading = ref(false);
    let param = ref({
      trainCode: "",
    });
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 2,
    });
    const trainSeats = ref([]);

    const columns = [
      {
        title: "车次编号",
        dataIndex: "trainCode",
        key: "trainCode",
      },
      {
        title: "厢序",
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
        title: "同车厢座序",
        dataIndex: "carriageSeatIndex",
        key: "carriageSeatIndex",
      },
      {
        title: "操作",
        dataIndex: "operation",
      },
    ];
    const onAdd = () => {
      trainSeat.value = {};
      visible.value = true;
    };
    const onEdit = (record) => {
      trainSeat.value = { ...record };
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
      Axios.save(trainSeat.value).then((res) => {
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
          trainSeats.value = res.data.list;
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
      trainSeat,
      visible,
      trainSeats,
      handleTableChange,
      columns,
      onMounted,
      handleQuery,
      pagination,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete,
      param,
    };
  },
});
</script>
