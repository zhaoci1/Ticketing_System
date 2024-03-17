<template>
  <div>
    <p>
      <a-space>
        <a-button type="primary" @click="handleQuery()">刷新</a-button>
        <a-button type="primary" @click="onAdd">新增</a-button>
      </a-space>
    </p>
    <a-table
      :dataSource="trains"
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
            <a-popconfirm
              title="删除生成座位将删除已有记录，确认生成座位?"
              @confirm="genSeat(record)"
              ok-text="确认"
              cancel-text="取消"
            >
              <a>生成座位</a>
            </a-popconfirm>
          </a-space>
        </template>
        <template v-else-if="column.dataIndex === 'type'">
          <span v-for="item in TRAIN_TYPE_ARRAY" :key="item.code">
            <span v-if="item.code === record.code">{{ item.value }}</span>
          </span>
        </template>
      </template>
    </a-table>
    <a-modal
      v-model:visible="visible"
      title="车次"
      @ok="handleOk"
      ok-text="确认"
      cancel-text="取消"
    >
      <a-form
        :model="train"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 20 }"
      >
        <a-form-item label="车次编号">
          <a-input v-model:value="train.code" :disabled="!!train.id" />
        </a-form-item>
        <a-form-item label="车次类型">
          <a-select v-model:value="train.type">
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
          <station-select v-model="train.start"></station-select>
        </a-form-item>
        <a-form-item label="始发站拼音">
          <a-input v-model:value="train.startPinyin" disabled />
        </a-form-item>
        <a-form-item label="出发时间">
          <a-time-picker
            v-model:value="train.startTime"
            valueFormat="HH:mm:ss"
            placeholder="请选择时间"
          />
        </a-form-item>
        <a-form-item label="终点站">
          <station-select v-model="train.end"></station-select>
        </a-form-item>
        <a-form-item label="终点站拼音">
          <a-input v-model:value="train.endPinyin" disabled />
        </a-form-item>
        <a-form-item label="到站时间">
          <a-time-picker
            v-model:value="train.endTime"
            valueFormat="HH:mm:ss"
            placeholder="请选择时间"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import Axios from "@/api/trainApi";
import { message } from "ant-design-vue";
import { defineComponent, ref, onMounted, watch } from "vue";
import { pinyin } from "pinyin-pro";
import stationSelect from "@/components/station-select.vue";
import TheSelect from "@/components/the-select.vue";

export default defineComponent({
  components: { stationSelect, TheSelect },
  name: "train-view",
  setup() {
    const TRAIN_TYPE_ARRAY = window.TRAIN_TYPE_ARRAY;
    const visible = ref(false);
    let train = ref({
      id: undefined,
      code: undefined,
      type: undefined,
      start: "12312312",
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
      pageSize: 2,
    });
    const trains = ref([]);

    const columns = [
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
    watch(
      () => train.value.start,
      () => {
        if (train.value.start != null) {
          train.value.startPinyin = pinyin(train.value.start, {
            tonType: "none",
          }).replaceAll(" ", "");
        } else {
          train.value.startPinyin = "";
        }
      },
      { immediate: true }
    );
    watch(
      () => train.value.end,
      () => {
        if (train.value.end != null) {
          train.value.endPinyin = pinyin(train.value.end, {
            tonType: "none",
          }).replaceAll(" ", "");
        } else {
          train.value.endPinyin = "";
        }
      },
      { immediate: true }
    );
    const onAdd = () => {
      train.value = {};
      visible.value = true;
    };
    const onEdit = (record) => {
      train.value = { ...record };
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
      Axios.save(train.value).then((res) => {
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
      Axios.pageList(page).then((res) => {
        loading.value = false;
        if (res.code == 200) {
          trains.value = res.data.list;
          pagination.value.current = page.page;
          pagination.value.total = res.data.total;
        } else {
          message.error("查询失败");
        }
      });
    };
    const genSeat = (record) => {
      Axios.genSeat(record.code).then((res) => {
        message.success("生成成功");
      });
    };
    onMounted(() => {
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize,
      });
    });
    return {
      TRAIN_TYPE_ARRAY,
      train,
      visible,
      trains,
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
      genSeat,
    };
  },
});
</script>
