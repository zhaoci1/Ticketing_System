<template>
  <div>
    <a-select
      v-model:value="name"
      show-search
      allowClear
      :filterOption="filterNameOption"
      @change="onChange"
      placeholder="请选择站名"
      
    >
      <a-select-option
        v-for="item in stationAll"
        :key="item.name"
        :value="item.name"
        :label="item.name + item.namePinyin + item.namePy"
      >
        {{ item.name }}  {{ item.namePinyin }} ~ {{ item.namePy }}
      </a-select-option>
    </a-select>
  </div>
</template>

<script>
import { defineComponent, onMounted, ref, watch } from "vue";
import Axios from "@/api/stationApi";

export default defineComponent({
  // 父子组件传值
  props: ["modelValue"],
  //   定义一个事件change
  emits: ["update:modelValue", "change"],
  setup(props, {emit}) {
    let stationAll = ref([]);
    let name = ref();
    const filterNameOption = (input, option) => {
      return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    };
    const queryStationAll = () => {
      Axios.queryAll().then((res) => {
        stationAll.value = res.data;
        console.log(res);
      });
    };
    /**
     * 将选中的车次信息返回给父组件
     */
    const onChange = (value) => {
      emit("update:modelValue", value);
      let station = stationAll.value.filter((item) => item.name === value)[0];
      if (station == null) {
        station = {};
      }
      emit("change", station);
    };
    onMounted(() => {
      queryStationAll();
    });
    // 监控父组件的值
    watch(
      () => props.modelValue,
      () => {
        name.value = props.modelValue;
      },
      { immediate: true }
    );
    return {
      stationAll,
      queryStationAll,
      filterNameOption,
      onChange,
      name
    };
  },
});
</script>
