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
      :dataSource="trainStations"
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
      </template>
    </a-table>
    <a-modal
      v-model:visible="visible"
      title="火车车站"
      @ok="handleOk"
      ok-text="确认"
      cancel-text="取消"
    >
      <a-form
        :model="trainStation"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 20 }"
      >
        <a-form-item label="车次编号">
          <the-select v-model="trainStation.trainCode"></the-select>
        </a-form-item>
        <a-form-item label="站序">
          <a-input v-model:value="trainStation.index" />
        </a-form-item>
        <a-form-item label="站名">
          <station-select v-model="trainStation.name"></station-select>
        </a-form-item>
        <a-form-item label="站名拼音">
          <a-input v-model:value="trainStation.namePinyin" disabled />
        </a-form-item>
        <a-form-item label="进站时间">
          <a-time-picker
            v-model:value="trainStation.inTime"
            valueFormat="HH:mm:ss"
            placeholder="请选择时间"
          />
        </a-form-item>
        <a-form-item label="出站时间">
          <a-time-picker
            v-model:value="trainStation.outTime"
            valueFormat="HH:mm:ss"
            placeholder="请选择时间"
          />
        </a-form-item>
        <a-form-item label="停站时长">
          <a-time-picker
            v-model:value="trainStation.stopTime"
            valueFormat="HH:mm:ss"
            placeholder="请选择时间"
            disabled
          />
        </a-form-item>
        <a-form-item label="里程（公里）">
          <a-input v-model:value="trainStation.km" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import Axios from "@/api/trainStationApi";
import { message } from "ant-design-vue";
import { defineComponent, ref, onMounted, watch } from "vue";
import { pinyin } from "pinyin-pro";
import theSelect from "@/components/the-select.vue";
import StationSelect from "@/components/station-select.vue";
import dayjs from "dayjs";

export default defineComponent({
  components: { theSelect, StationSelect },
  name: "train-station-view",
  setup() {
    const visible = ref(false);
    let param = ref({
      trainCode: "",
    });
    let trainStation = ref({
      id: undefined,
      trainCode: undefined,
      index: undefined,
      name: undefined,
      namePinyin: undefined,
      inTime: undefined,
      outTime: undefined,
      stopTime: undefined,
      km: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    let loading = ref(false);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    const trainStations = ref([]);

    const columns = [
      {
        title: "车次编号",
        dataIndex: "trainCode",
        key: "trainCode",
      },
      {
        title: "站序",
        dataIndex: "index",
        key: "index",
      },
      {
        title: "站名",
        dataIndex: "name",
        key: "name",
      },
      {
        title: "站名拼音",
        dataIndex: "namePinyin",
        key: "namePinyin",
      },
      {
        title: "进站时间",
        dataIndex: "inTime",
        key: "inTime",
      },
      {
        title: "出站时间",
        dataIndex: "outTime",
        key: "outTime",
      },
      {
        title: "停站时长",
        dataIndex: "stopTime",
        key: "stopTime",
      },
      {
        title: "里程（公里）",
        dataIndex: "km",
        key: "km",
      },
      {
        title: "操作",
        dataIndex: "operation",
      },
    ];

    watch(
      () => trainStation.value.name,
      () => {
        if (trainStation.value.name != null) {
          trainStation.value.namePinyin = pinyin(trainStation.value.name, {
            tonType: "none",
          }).replaceAll(" ", "");
        } else {
          trainStation.value.namePinyin = "";
        }
      },
      { immediate: true }
    );
    watch(
      () => trainStation.value.inTime,
      () => {
        let diff = dayjs(trainStation.value.outTime, "HH:mm:ss").diff(
          dayjs(trainStation.value.inTime, "HH:mm:ss"),
          "seconds"
        );
        trainStation.value.stopTime = dayjs("00:00:00", "HH:mm:ss")
          .second(diff)
          .format("HH:mm:ss");
      },
      { immediate: true }
    );
    watch(
      () => trainStation.value.outTime,
      () => {
        let diff = dayjs(trainStation.value.outTime, "HH:mm:ss").diff(
          dayjs(trainStation.value.inTime, "HH:mm:ss"),
          "seconds"
        );
        trainStation.value.stopTime = dayjs("00:00:00", "HH:mm:ss")
          .second(diff)
          .format("HH:mm:ss");
      },
      { immediate: true }
    );
    // 下拉框筛选方法
    const onAdd = () => {
      trainStation.value = {};
      visible.value = true;
    };
    const onEdit = (record) => {
      trainStation.value = { ...record };
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
      Axios.save(trainStation.value).then((res) => {
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
          trainStations.value = res.data.list;
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
      trainStation,
      visible,
      trainStations,
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
<style>
the-select {
  width: 200px;
}
</style>
