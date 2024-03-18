<template>
  <div class="job">
    <p>
      <a-button type="primary" @click="handleAdd()"> 新增 </a-button>&nbsp;
      <a-button type="primary" @click="handleQuery()"> 刷新 </a-button>
    </p>
    <a-table :dataSource="jobs" :columns="columns" :loading="loading">
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'operation'">
          <a-space>
            <a-popconfirm
              title="手动执行会立即执行一次，确定执行？"
              ok-text="是"
              cancel-text="否"
              @confirm="handleRun(record)"
            >
              <a-button type="primary" size="small"> 手动执行 </a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定重启？"
              ok-text="是"
              cancel-text="否"
              @confirm="handleResume(record)"
            >
              <a-button
                v-show="record.state === 'PAUSED' || record.state === 'ERROR'"
                type="primary"
                size="small"
              >
                重启
              </a-button>
            </a-popconfirm>
            <a-popconfirm
              title="确定暂停？"
              ok-text="是"
              cancel-text="否"
              @confirm="handlePause(record)"
            >
              <a-button
                v-show="record.state === 'NORMAL' || record.state === 'BLOCKED'"
                type="primary"
                size="small"
              >
                暂停
              </a-button>
            </a-popconfirm>
            <a-button type="primary" @click="handleEdit(record)" size="small">
              编辑
            </a-button>
            <a-popconfirm
              title="删除后不可恢复，确认删除?"
              ok-text="是"
              cancel-text="否"
              @confirm="handleDelete(record)"
            >
              <a-button type="danger" size="small"> 删除 </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-modal
      title="用户"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
    >
      <a-form :model="job" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="类名">
          <a-input v-model:value="job.name" />
        </a-form-item>
        <a-form-item label="描述">
          <a-input v-model:value="job.description" />
        </a-form-item>
        <a-form-item label="分组">
          <a-input v-model:value="job.group" :disabled="!!job.state" />
        </a-form-item>
        <a-form-item label="表达式">
          <a-input v-model:value="job.cronExpression" />
          <div class="ant-alert ant-alert-success">
            每5秒执行一次：0/5 * * * * ?
            <br />
            每5分钟执行一次：* 0/5 * * * ?
          </div>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import { onMounted, ref } from "vue";
import Axios from "@/api/batchApi";
import { notification } from "ant-design-vue";

export default {
  setup() {
    const jobs = ref();
    const loading = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const job = ref();
    job.value = {};

    const columns = [
      {
        title: "分组",
        dataIndex: "group",
      },
      {
        title: "类名",
        dataIndex: "name",
      },
      {
        title: "描述",
        dataIndex: "description",
      },
      {
        title: "状态",
        dataIndex: "state",
      },
      {
        title: "表达式",
        dataIndex: "cronExpression",
      },
      {
        title: "上次时间",
        dataIndex: "preFireTime",
      },
      {
        title: "下次时间",
        dataIndex: "nextFireTime",
      },
      {
        title: "操作",
        dataIndex: "operation",
      },
    ];

    const handleModalOk = () => {
      modalLoading.value = true;
      Axios.save(job.value).then((res) => {
        if (res.data) {
          modalVisible.value = false;
          modalLoading.value = false;
          notification.success({ description: "保存成功！" });
          handleQuery();
        } else {
          notification.success({ description: res.message });
        }
      });
    };
    /**
     * 添加任务
     */
    const handleAdd = () => {
      modalVisible.value = true;
      job.value = {
        group: "DEFAULT",
      };
    };

    /**
     * 编辑任务
     */
    const handleEdit = (record) => {
      modalVisible.value = true;
      job.value = { ...record };
    };
    /**
     * 刷新任务
     */
    const handleQuery = () => {
      Axios.query().then((res) => {
        jobs.value = res.data
        console.log(res.data);
      });
    };
    /**
     * 手动执行一遍定时任务
     */
    const handleRun = (record) => {};

    /**
     * 重启任务
     */
    const handleResume = (record) => {
      Axios.resume(record).then((res) => {
        if (res.data) {
          modalVisible.value = false;
          notification.success({ description: "重启成功" });
          handleQuery();
        } else {
          notification.success({ description: res.message });
        }
      });
    };

    /**
     * 暂停方法
     */
    const handlePause = (record) => {
      Axios.pause(record).then((res) => {
        if (res.data) {
          notification.success({ description: "暂停成功！" });
          handleQuery();
        } else {
          notification.success({ description: res.message });
        }
      });
    };

    /**
     * 删除任务
     */
    const handleDelete = (record) => {
      Axios.delete(record).then((res) => {
        if (res.data) {
          notification.success({ description: "删除成功！" });
          handleQuery();
        } else {
          notification.success({ description: res.message });
        }
      });
    };
    onMounted(() => {
      handleQuery();
    });
    return {
      jobs,
      job,
      loading,
      columns,
      handleModalOk,
      handleRun,
      handleAdd,
      handleResume,
      handleQuery,
      handleEdit,
      handlePause,
      handleDelete,
      modalVisible,
      modalLoading,
    };
  },
};
</script>
