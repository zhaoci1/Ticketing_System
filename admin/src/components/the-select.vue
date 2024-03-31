<template>
  <div>
    <a-select
      v-model:value="trainCode"
      show-search
      allowClear
      :filterOption="filterTrainCodeOption"
      @change="onChange"
      placeholder="请选择车次"
    >
      <a-select-option
        v-for="item in trainAll"
        :key="item.code"
        :value="item.code"
        :label="item.code + item.start + item.end"
      >
        {{ item.code }} | {{ item.start }} ~ {{ item.end }}
      </a-select-option>
    </a-select>
  </div>
</template>

<script>
import { defineComponent, onMounted, ref, watch } from "vue";
import Axios from "@/api/trainStationApi";

export default defineComponent({
  // 父子组件传值
  props: ["modelValue"],
  //   定义一个事件change
  emits: ["update:modelValue", "change"],
  setup(props, { emit }) {
    let trainAll = ref([]);
    let trainCode = ref();

    const filterTrainCodeOption = (input, option) => {
      return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    };
    const queryTrainAll = () => {
      let list = window.sessionStorage.getItem("SESSION_ALL_TRAIN");
      if (list != null) {
        trainAll.value = list;
      } else {
        Axios.queryAll().then((res) => {
          if (res.code == 200) {
            trainAll.value = res.data;
            console.log("保存缓存");
            window.sessionStorage.setItem("SESSION_ALL_TRAIN", JSON.stringify(trainAll.value));
          }
        });
      }
    };
    /**
     * 将选中的车次信息返回给父组件
     */
    const onChange = (value) => {
      emit("update:modelValue", value);
      let train = trainAll.value.filter((item) => item.code === value)[0];
      if (train == null) {
        train = {};
      }
      emit("change", train);
    };
    onMounted(() => {
      queryTrainAll();
    });
    // 监控父组件的值
    watch(
      () => props.modelValue,
      () => {
        trainCode.value = props.modelValue;
      },
      { immediate: true }
    );
    return {
      trainAll,
      queryTrainAll,
      filterTrainCodeOption,
      onChange,
      trainCode,
    };
  },
});
</script>
