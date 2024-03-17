<template>
  <div>
    <p>
      <a-space>
        <the-select v-model="param.trainCode" width="200px"></the-select>
        <a-button type="primary" @click="handleQuery()">查找</a-button>
        <a-button type="primary" @click="onAdd">新增</a-button>
      </a-space>
    </p>
    <a-table
      :dataSource="trainCarriages"
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
      </template>
    </a-table>
    <a-modal
      v-model:visible="visible"
      title="火车车厢"
      @ok="handleOk"
      ok-text="确认"
      cancel-text="取消"
    >
      <a-form
        :model="trainCarriage"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 20 }"
      >
        <a-form-item label="车次编号">
          <the-select v-model="trainCarriage.trainCode"></the-select>
        </a-form-item>
        <a-form-item label="厢号">
          <a-input v-model:value="trainCarriage.index" />
        </a-form-item>
        <a-form-item label="座位类型">
          <a-select v-model:value="trainCarriage.seatType">
            <a-select-option
              v-for="item in SEAT_TYPE_ARRAY"
              :key="item.code"
              :value="item.code"
            >
              {{ item.desc }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="排数">
          <a-input v-model:value="trainCarriage.rowCount" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import Axios from "@/api/trainCarriageApi";
import theSelect from "@/components/the-select.vue";
import { message } from "ant-design-vue";
import { defineComponent, ref, onMounted } from "vue";

export default defineComponent({
  components: { theSelect },
  name: "train-carriage-view",
  setup() {
    const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
    const visible = ref(false);
    let trainCarriage = ref({
      id: undefined,
      trainCode: undefined,
      index: undefined,
      seatType: undefined,
      seatCount: undefined,
      rowCount: undefined,
      colCount: undefined,
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
    const trainCarriages = ref([]);

    const columns = [
      {
        title: "车次编号",
        dataIndex: "trainCode",
        key: "trainCode",
      },
      {
        title: "厢号",
        dataIndex: "index",
        key: "index",
      },
      {
        title: "座位类型",
        dataIndex: "seatType",
        key: "seatType",
      },
      {
        title: "座位数",
        dataIndex: "seatCount",
        key: "seatCount",
      },
      {
        title: "排数",
        dataIndex: "rowCount",
        key: "rowCount",
      },
      {
        title: "列数",
        dataIndex: "colCount",
        key: "colCount",
      },
      {
        title: "操作",
        dataIndex: "operation",
      },
    ];
    const onAdd = () => {
      trainCarriage.value = {};
      visible.value = true;
    };
    const onEdit = (record) => {
      trainCarriage.value = { ...record };
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
      Axios.save(trainCarriage.value).then((res) => {
        if (res.data) {
          message.success("保存成功");
          visible.value = false;
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(res.message);
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
          trainCarriages.value = res.data.list;
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
      SEAT_TYPE_ARRAY,
      trainCarriage,
      visible,
      trainCarriages,
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
