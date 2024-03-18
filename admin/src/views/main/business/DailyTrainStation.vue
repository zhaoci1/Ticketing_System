<template>
  <div>
    <p>
      <a-space>
        <a-date-picker
          v-model:value="param.date"
          valueFormat="YYYY-MM-DD"
          placeholder="请选择日期"
        />
        <the-select v-model="param.trainCode"></the-select>
        <a-button type="primary" @click="handleQuery()">查询</a-button>
        <a-button type="primary" @click="onAdd">新增</a-button>
      </a-space>
    </p>
    <a-table
      :dataSource="dailyTrainStations"
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
      title="每日车站"
      @ok="handleOk"
      ok-text="确认"
      cancel-text="取消"
    >
      <a-form
        :model="dailyTrainStation"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 20 }"
      >
        <a-form-item label="日期">
          <a-date-picker
            v-model:value="dailyTrainStation.date"
            valueFormat="YYYY-MM-DD"
            placeholder="请选择日期"
          />
        </a-form-item>
        <a-form-item label="车次编号">
          <the-select v-model="dailyTrainStation.trainCode"></the-select>
        </a-form-item>
        <a-form-item label="站序">
          <a-input v-model:value="dailyTrainStation.index" />
        </a-form-item>
        <a-form-item label="站名">
          <station-select v-model="dailyTrainStation.name"></station-select>
        </a-form-item>
        <a-form-item label="站名拼音">
          <a-input v-model:value="dailyTrainStation.namePinyin" disabled />
        </a-form-item>
        <a-form-item label="进站时间">
          <a-time-picker
            v-model:value="dailyTrainStation.inTime"
            valueFormat="HH:mm:ss"
            placeholder="请选择时间"
          />
        </a-form-item>
        <a-form-item label="出站时间">
          <a-time-picker
            v-model:value="dailyTrainStation.outTime"
            valueFormat="HH:mm:ss"
            placeholder="请选择时间"
          />
        </a-form-item>
        <a-form-item label="停站时长">
          <a-time-picker
            v-model:value="dailyTrainStation.stopTime"
            valueFormat="HH:mm:ss"
            placeholder="请选择时间"
            disabled
          />
        </a-form-item>
        <a-form-item label="里程（公里）">
          <a-input v-model:value="dailyTrainStation.km" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import Axios from "@/api/dailyTrainStationApi";
import StationSelect from "@/components/station-select.vue";
import theSelect from "@/components/the-select.vue";
import { message } from "ant-design-vue";
import { pinyin } from "pinyin-pro";
import { defineComponent, ref, onMounted, watch } from "vue";
import dayjs from "dayjs";
export default defineComponent({
  components: { theSelect, StationSelect },
  name: "daily-train-station-view",
  setup() {
    const visible = ref(false);
    let dailyTrainStation = ref({
      id: undefined,
      date: undefined,
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
      pageSize: 2,
    });
    let param = ref({
      trainCode: null,
    });
    const dailyTrainStations = ref([]);

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
    const onAdd = () => {
      dailyTrainStation.value = {};
      visible.value = true;
    };
    const onEdit = (record) => {
      dailyTrainStation.value = { ...record };
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
      Axios.save(dailyTrainStation.value).then((res) => {
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
      page.code = param.value.trainCode;
      page.date = param.value.date;
      Axios.pageList(page).then((res) => {
        loading.value = false;
        if (res.code == 200) {
          dailyTrainStations.value = res.data.list;
          pagination.value.current = page.page;
          pagination.value.total = res.data.total;
        } else {
          message.error("查询失败");
        }
      });
    };
    watch(
      () => dailyTrainStation.value.name,
      () => {
        if (dailyTrainStation.value.name != null) {
          dailyTrainStation.value.namePinyin = pinyin(
            dailyTrainStation.value.name,
            {
              tonType: "none",
            }
          ).replaceAll(" ", "");
        } else {
          dailyTrainStation.value.namePinyin = "";
        }
      },
      { immediate: true }
    );
    watch(
      () => dailyTrainStation.value.inTime,
      () => {
        let diff = dayjs(dailyTrainStation.value.outTime, "HH:mm:ss").diff(
          dayjs(dailyTrainStation.value.inTime, "HH:mm:ss"),
          "seconds"
        );
        dailyTrainStation.value.stopTime = dayjs("00:00:00", "HH:mm:ss")
          .second(diff)
          .format("HH:mm:ss");
      },
      { immediate: true }
    );

    // 自动计算停车时长
    watch(
      () => dailyTrainStation.value.outTime,
      () => {
        let diff = dayjs(dailyTrainStation.value.outTime, "HH:mm:ss").diff(
          dayjs(dailyTrainStation.value.inTime, "HH:mm:ss"),
          "seconds"
        );
        dailyTrainStation.value.stopTime = dayjs("00:00:00", "HH:mm:ss")
          .second(diff)
          .format("HH:mm:ss");
      },
      { immediate: true }
    );
    onMounted(() => {
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize,
      });
    });
    return {
      dailyTrainStation,
      visible,
      dailyTrainStations,
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
