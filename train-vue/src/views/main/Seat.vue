<template>
  <div>
    <table>
      <tr>
        <td style="width: 25px; background: #ff9900"></td>
        <td>：已被购买</td>
        <td style="width: 20px"></td>
        <td style="width: 25px; background: #999999"></td>
        <td>：未被购买</td>
      </tr>
    </table>
    <div v-for="(seatObj, carriage) in train" :key="carriage"
         style="border: 3px solid #99CCFF;
                 margin-bottom: 30px;
                 padding: 5px;
                 border-radius: 4px">
      <div style="display:block;
                  width:50px;
                  height:10px;
                  position:relative;
                  top:-15px;
                  text-align: center;
                  background: white;">
        {{carriage}}
      </div>
      <table>
        <tr>
          <!-- 遍历排数 -->
          <td v-for="(sell, index) in Object.values(seatObj)[0]" :key="index"
              style="text-align: center">
            {{index + 1}}
          </td>
        </tr>
        <!-- 遍历座位数，并对数据进行判断 -->
        <tr v-for="(sellList, col) in seatObj" :key="col">
          <td v-for="(sell, index) in sellList" :key="index"
              style="text-align: center;
                      border: 2px solid white;
                      background: grey;
                      padding: 0 4px;
                      color: white;
                      "
              :style="{background: (sell > 0 ? '#FF9900' : '#999999')}">{{col}}</td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import Axios from "@/api/seatApi";
import { defineComponent, onMounted, ref } from "vue";
import { useRoute } from "vue-router";

export default defineComponent({
  setup() {
    const route = useRoute();
    const param = ref({});
    param.value = route.query;

    const list = ref();

    const train = ref({});

    const querySeat = () => {
      Axios.query(param.value).then((res) => {
        list.value = res.data;
        format();
      });
    };
    const format = () => {
      let _train ={};
      for (let i = 0; i < list.value.length; i++) {
        let item = list.value[i];
        // 计算当前区间是否还有票

        let sellDB = item.sell;
        // 假设有6站：start=1,end=3,售卖情况为11111，截取1-3的售卖情况1-11(1-3的售卖情况)-111
        // 转为int类型，如果大于0，就表示这个位置是不可选的
        let sell = sellDB.substr(
          param.value.startIndex,
          param.value.endIndex - param.value.startIndex
        );

        // 将sell放入火车数据中
        if (!_train["车箱" + item.carriageIndex]) {
          _train["车箱" + item.carriageIndex] = {};
        }
        if (!_train["车箱" + item.carriageIndex][item.col]) {
          _train["车箱" + item.carriageIndex][item.col] = [];
        }
        _train["车箱" + item.carriageIndex][item.col].push(parseInt(sell));
      }
      train.value = _train;
      console.log(train.value);
    };
    onMounted(() => {
      querySeat();
    });
    return {
      param,
      train,
    };
  },
});
</script>
