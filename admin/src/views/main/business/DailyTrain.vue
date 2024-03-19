<template>
  <div>
    <p>
      <a-space>
        <a-date-picker
          v-model:value="param.date"
          valueFormat="YYYY-MM-DD"
          placeholder="请选择日期"
        />
        <the-select v-model="param.code"></the-select>
        <a-button type="primary" @click="handleQuery()">查询</a-button>
        <a-button type="primary" @click="onAdd">新增</a-button>
      </a-space>
    </p>
    <a-table
      :dataSource="dailyTrains"
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
          <span v-for="item in TRAIN_TYPE_ARRAY" :key="item.code">
            <span v-if="item.code === record.type"> {{ item.desc }}</span>
          </span>
        </template>
      </template>
    </a-table>
    <a-modal
      v-model:visible="visible"
      title="每日车次"
      @ok="handleOk"
      ok-text="确认"
      cancel-text="取消"
    >
      <a-form
        :model="dailyTrain"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 20 }"
      >
        <a-form-item label="日期">
          <a-date-picker
            v-model:value="dailyTrain.date"
            valueFormat="YYYY-MM-DD"
            placeholder="请选择日期"
          />
        </a-form-item>
        <a-form-item label="车次编号">
          <the-select
            v-model="dailyTrain.trainCode"
            @change="onChangeCode"
          ></the-select>
        </a-form-item>
        <a-form-item label="车次类型">
          <a-select v-model:value="dailyTrain.type">
            <a-select-option
              v-for="item in TRAIN_TYPE_ARRAY"
              :key="item.code"
              :value="item.code"
            >
              {{ item.desc }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="始发站">
          <a-input v-model:value="dailyTrain.start" />
        </a-form-item>
        <a-form-item label="始发站拼音">
          <a-input v-model:value="dailyTrain.startPinyin" />
        </a-form-item>
        <a-form-item label="出发时间">
          <a-time-picker
            v-model:value="dailyTrain.startTime"
            valueFormat="HH:mm:ss"
            placeholder="请选择时间"
          />
        </a-form-item>
        <a-form-item label="终点站">
          <a-input v-model:value="dailyTrain.end" />
        </a-form-item>
        <a-form-item label="终点站拼音">
          <a-input v-model:value="dailyTrain.endPinyin" />
        </a-form-item>
        <a-form-item label="到站时间">
          <a-time-picker
            v-model:value="dailyTrain.endTime"
            valueFormat="HH:mm:ss"
            placeholder="请选择时间"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import Axios from "@/api/dailyTrainApi";
import theSelect from "@/components/the-select.vue";
import TheSelect from "@/components/the-select.vue";
import { message } from "ant-design-vue";
import { defineComponent, ref, onMounted } from "vue";

export default defineComponent({
  components: { theSelect },
  name: "daily-train-view",
  setup() {
    const TRAIN_TYPE_ARRAY = window.TRAIN_TYPE_ARRAY;
    const visible = ref(false);
    let dailyTrain = ref({
      id: undefined,
      date: undefined,
      code: undefined,
      type: undefined,
      start: undefined,
      startPinyin: undefined,
      startTime: undefined,
      end: undefined,
      endPinyin: undefined,
      endTime: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    let loading = ref(false);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    const dailyTrains = ref([]);
    let param = ref({
      code: "",
    });
    const columns = [
      {
        title: "日期",
        dataIndex: "date",
        key: "date",
      },
      {
        title: "车次编号",
        dataIndex: "code",
        key: "code",
      },
      {
        title: "车次类型",
        dataIndex: "type",
        key: "type",
      },
      {
        title: "始发站",
        dataIndex: "start",
        key: "start",
      },
      {
        title: "始发站拼音",
        dataIndex: "startPinyin",
        key: "startPinyin",
      },
      {
        title: "出发时间",
        dataIndex: "startTime",
        key: "startTime",
      },
      {
        title: "终点站",
        dataIndex: "end",
        key: "end",
      },
      {
        title: "终点站拼音",
        dataIndex: "endPinyin",
        key: "endPinyin",
      },
      {
        title: "到站时间",
        dataIndex: "endTime",
        key: "endTime",
      },
      {
        title: "操作",
        dataIndex: "operation",
      },
    ];
    const onAdd = () => {
      dailyTrain.value = {};
      visible.value = true;
    };
    const onEdit = (record) => {
      dailyTrain.value = { ...record };
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
      Axios.save(dailyTrain.value).then((res) => {
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
      page.code = param.value.code;
      page.date = param.value.date;
      Axios.pageList(page).then((res) => {
        loading.value = false;
        if (res.code == 200) {
          dailyTrains.value = res.data.list;
          pagination.value.current = page.page;
          pagination.value.total = res.data.total;
        } else {
          message.error("查询失败");
        }
      });
    };
    const onChangeCode = (train) => {
      console.log(train);
      let t = { ...train };
      delete t.id;
      dailyTrain.value = Object.assign(dailyTrain.value, t);
    };
    onMounted(() => {
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize,
      });
    });
    return {
      param,
      TRAIN_TYPE_ARRAY,
      dailyTrain,
      visible,
      dailyTrains,
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
      onChangeCode,
    };
  },
});
</script>
