<template>
  <div>
    <p>
      <a-space>
        <a-button type="primary" @click="handleQuery()">刷新</a-button>
        <a-button type="primary" @click="showModal">新增</a-button>
      </a-space>
    </p>
    <a-modal
      v-model:visible="visible"
      title="乘车人"
      @ok="handleOk"
      ok-text="确认"
      cancel-text="取消"
    >
      <a-form
        :model="passenger"
        name="basic"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="站名">
          <a-input v-model:value="passenger.name" />
        </a-form-item>

        <a-form-item label="站名拼音">
          <a-input v-model:value="passenger.namePinyin" disabled />
        </a-form-item>
        <a-form-item label="拼音首字母">
          <a-input v-model:value="passenger.namePy" disabled />
        </a-form-item>
      </a-form>
    </a-modal>
    <a-table
      :dataSource="dataSource"
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
  </div>
</template>

<script>
import Axios from "@/api/stationApi";
import { message } from "ant-design-vue";
import { reactive, ref, onMounted, watch } from "vue";
import { pinyin } from "pinyin-pro";

export default {
  setup() {
    const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE_ARRAY;
    const visible = ref(false);
    let dataSource = ref([]);
    let loading = ref(false);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 2,
    });
    const columns = [
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
        title: "站名拼音首字母",
        dataIndex: "namePy",
        key: "namePy",
      },
      {
        title: "操作",
        dataIndex: "operation",
      },
    ];
    const passenger = ref({
      id: undefined,
      name: undefined,
      namePinyin: undefined,
      namePy: undefined,
      memberId: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    watch(
      () => passenger.value.name,
      () => {
        if (passenger.value.name != null) {
          passenger.value.namePinyin = pinyin(passenger.value.name, {
            tonType: "none",
          }).replaceAll(" ", "");
          passenger.value.namePy = pinyin(passenger.value.name, {
            pattern: "first",
            tonType: "none",
          }).replaceAll(" ", "");
        } else {
          passenger.value.namePinyin = "";
          passenger.value.namePy = "";
        }
      },
      { immediate: true }
    );
    const showModal = () => {
      passenger.value = {};
      visible.value = true;
    };
    const onEdit = (record) => {
      passenger.value = { ...record };
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
      Axios.save(passenger.value).then((res) => {
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
      Axios.pageList(page).then((res) => {
        loading.value = false;
        if (res.code == 200) {
          dataSource.value = res.data.list;
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
      PASSENGER_TYPE_ARRAY,
      onDelete,
      onEdit,
      handleTableChange,
      passenger,
      visible,
      showModal,
      handleOk,
      dataSource,
      columns,
      onMounted,
      handleQuery,
      pagination,
      loading,
    };
  },
};
</script>
@/api/businessApi
