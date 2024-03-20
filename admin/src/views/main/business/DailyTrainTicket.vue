<template>
  <div>
    <p>
      <a-space>
        <the-select v-model="param.trainCode"></the-select>
        <a-date-picker
          v-model:value="param.date"
          valueFormat="YYYY-MM-DD"
          placeholder="请选择日期"
        ></a-date-picker>
        <station-select v-model="param.start"></station-select>
        <station-select v-model="param.end"></station-select>
        <a-button type="primary" @click="handleQuery()">查询</a-button>
      </a-space>
    </p>
    <a-table
      :dataSource="dailyTrainTickets"
      :columns="columns"
      :pagination="pagination"
      @change="handleTableChange"
      :loading="loading"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'operation'"> </template>
        <template v-else-if="column.dataIndex === 'station'">
          {{ record.start }}<br />
          {{ record.end }}
        </template>
        <template v-else-if="column.dataIndex === 'time'">
          {{ record.startTime }}<br />
          {{ record.endTime }}
        </template>
        <template v-else-if="column.dataIndex === 'duration'">
          {{ calDuration(record.startTime, record.endTime) }}<br />
          <div
            v-if="
              record.startTime.replaceAll(':', '') >=
              record.endTime.replaceAll(':', '')
            "
          >
            次日到达
          </div>
          <div v-else>当日到达</div>
        </template>
        <template v-else-if="column.dataIndex === 'ydz'">
          <div v-if="record.ydz >= 0">
            {{ record.ydz }}<br />
            {{ record.ydzPrice }}￥
          </div>
          <div v-else>--</div>
        </template>
        <template v-else-if="column.dataIndex === 'edz'">
          <div v-if="record.edz >= 0">
            {{ record.edz }}<br />
            {{ record.edzPrice }}￥
          </div>
          <div v-else>--</div>
        </template>
        <template v-else-if="column.dataIndex === 'rw'">
          <div v-if="record.rw >= 0">
            {{ record.rw }}<br />
            {{ record.rwPrice }}￥
          </div>
          <div v-else>--</div>
        </template>
        <template v-else-if="column.dataIndex === 'yw'">
          <div v-if="record.yw >= 0">
            {{ record.yw }}<br />
            {{ record.ywPrice }}￥
          </div>
          <div v-else>--</div>
        </template>
      </template>
    </a-table>
    <a-modal
      v-model:visible="visible"
      title="余票信息"
      @ok="handleOk"
      ok-text="确认"
      cancel-text="取消"
    >
      <a-form
        :model="dailyTrainTicket"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 20 }"
      >
        <a-form-item label="日期">
          <a-date-picker
            v-model:value="dailyTrainTicket.date"
            valueFormat="YYYY-MM-DD"
            placeholder="请选择日期"
          />
        </a-form-item>
        <a-form-item label="车次编号">
          <a-input v-model:value="dailyTrainTicket.trainCode" />
        </a-form-item>
        <a-form-item label="出发站">
          <a-input v-model:value="dailyTrainTicket.start" />
        </a-form-item>
        <a-form-item label="出发站拼音">
          <a-input v-model:value="dailyTrainTicket.startPinyin" />
        </a-form-item>
        <a-form-item label="出发时间">
          <a-time-picker
            v-model:value="dailyTrainTicket.startTime"
            valueFormat="HH:mm:ss"
            placeholder="请选择时间"
          />
        </a-form-item>
        <a-form-item label="出发站序">
          <a-input v-model:value="dailyTrainTicket.startIndex" />
        </a-form-item>
        <a-form-item label="到达站">
          <a-input v-model:value="dailyTrainTicket.end" />
        </a-form-item>
        <a-form-item label="到达站拼音">
          <a-input v-model:value="dailyTrainTicket.endPinyin" />
        </a-form-item>
        <a-form-item label="到站时间">
          <a-time-picker
            v-model:value="dailyTrainTicket.endTime"
            valueFormat="HH:mm:ss"
            placeholder="请选择时间"
          />
        </a-form-item>
        <a-form-item label="到站站序">
          <a-input v-model:value="dailyTrainTicket.endIndex" />
        </a-form-item>
        <a-form-item label="一等座余票">
          <a-input v-model:value="dailyTrainTicket.ydz" />
        </a-form-item>
        <a-form-item label="一等座票价">
          <a-input v-model:value="dailyTrainTicket.ydzPrice" />
        </a-form-item>
        <a-form-item label="二等座余票">
          <a-input v-model:value="dailyTrainTicket.edz" />
        </a-form-item>
        <a-form-item label="二等座票价">
          <a-input v-model:value="dailyTrainTicket.edzPrice" />
        </a-form-item>
        <a-form-item label="软卧余票">
          <a-input v-model:value="dailyTrainTicket.rw" />
        </a-form-item>
        <a-form-item label="软卧票价">
          <a-input v-model:value="dailyTrainTicket.rwPrice" />
        </a-form-item>
        <a-form-item label="硬卧余票">
          <a-input v-model:value="dailyTrainTicket.yw" />
        </a-form-item>
        <a-form-item label="硬卧票价">
          <a-input v-model:value="dailyTrainTicket.ywPrice" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import Axios from "@/api/dailyTrainTicketApi";
import StationSelect from "@/components/station-select.vue";
import theSelect from "@/components/the-select.vue";
import { message } from "ant-design-vue";
import { defineComponent, ref, onMounted } from "vue";
import dayjs from "dayjs";

export default defineComponent({
  components: { theSelect, StationSelect },
  name: "daily-train-ticket-view",
  setup() {
    const visible = ref(false);
    let dailyTrainTicket = ref({
      id: undefined,
      date: undefined,
      trainCode: undefined,
      start: undefined,
      startPinyin: undefined,
      startTime: undefined,
      startIndex: undefined,
      end: undefined,
      endPinyin: undefined,
      endTime: undefined,
      endIndex: undefined,
      ydz: undefined,
      ydzPrice: undefined,
      edz: undefined,
      edzPrice: undefined,
      rw: undefined,
      rwPrice: undefined,
      yw: undefined,
      ywPrice: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    let loading = ref(false);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    const dailyTrainTickets = ref([]);

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
        title: "车站",
        dataIndex: "station",
      },
      {
        title: "时间",
        dataIndex: "time",
      },
      {
        title: "历时",
        dataIndex: "duration",
      },
      {
        title: "一等座余票",
        dataIndex: "ydz",
        key: "ydz",
      },
      {
        title: "二等座余票",
        dataIndex: "edz",
        key: "edz",
      },
      {
        title: "软卧",
        dataIndex: "rw",
        key: "rw",
      },
      {
        title: "硬卧",
        dataIndex: "yw",
        key: "yw",
      },
    ];
    let param = ref({});
    const onEdit = (record) => {
      dailyTrainTicket.value = { ...record };
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
    const calDuration = (startTime,endTime) => {
      let diff = dayjs(endTime, "HH:mm:ss").diff(
        dayjs(startTime, "HH:mm:ss"),
        "seconds"
      );
      return dayjs("00:00:00", "HH:mm:ss").second(diff).format("HH:mm:ss");
    };
    const handleOk = (e) => {
      Axios.save(dailyTrainTicket.value).then((res) => {
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
      page.date = param.value.date;
      page.start = param.value.start;
      page.end = param.value.end;
      Axios.pageList(page).then((res) => {
        loading.value = false;
        if (res.code == 200) {
          dailyTrainTickets.value = res.data.list;
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
      dailyTrainTicket,
      visible,
      dailyTrainTickets,
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
      calDuration,
    };
  },
});
</script>
