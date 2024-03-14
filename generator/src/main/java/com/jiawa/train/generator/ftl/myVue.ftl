<template>
    <div>
        <p>
            <a-space>
                <a-button type="primary" @click="handleQuery()">刷新</a-button>
                <a-button type="primary" @click="onAdd">新增</a-button>
            </a-space>
        </p>
        <a-table
                :dataSource="${domain}s"
                :columns="columns"
                :pagination="pagination"
                @change="handleTableChange"
                :loading="loading"
        >
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'operation'">
                    <#if !readOnly>
                        <a-space>
                            <a-popconfirm
                                    title="删除后不可恢复，确认删除?"
                                    @confirm="onDelete(record)"
                                    ok-text="确认"
                                    cancel-text="取消">
                                <a style="color:red">删除</a>

                            </a-popconfirm>
                            <a @click="onEdit(record)">编辑</a>
                        </a-space>
                    </#if>
                </template>
                <#list fieldList as field>
                    <#if field.enums>
                        <template v-else-if="column.dataIndex ==='type'">
          <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.key">
            <span v-if="item.key===record.type">{{ item.value }}</span>
          </span>
                        </template>
                    </#if>
                </#list>
            </template>
        </a-table>
        <#if !readOnly>
            <a-modal v-model:visible="visible" title="${tableNameCn}" @ok="handleOk"
                     ok-text="确认" cancel-text="取消">
                <a-form :model="${domain}" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
                    <#list fieldList as field>
                        <#if field.name!="id" && field.nameHump!="createTime" && field.nameHump!="updateTime">
                            <a-form-item label="${field.nameCn}">
                                <#if field.enums>
                                    <a-select v-model:value="${domain}.${field.nameHump}">
                                        <a-select-option v-for="item in ${field.enumsConst}_ARRAY" :key="item.code"
                                                         :value="item.code">
                                            {{item.desc}}
                                        </a-select-option>
                                    </a-select>
                                <#elseif field.javaType=='Date'>
                                    <#if field.type=='time'>
                                        <a-time-picker v-model:value="${domain}.${field.nameHump}"
                                                       valueFormat="HH:mm:ss" placeholder="请选择时间"/>
                                    <#elseif field.type=='date'>
                                        <a-date-picker v-model:value="${domain}.${field.nameHump}"
                                                       valueFormat="YYYY-MM-DD" placeholder="请选择日期"/>
                                    <#else>
                                        <a-date-picker v-model:value="${domain}.${field.nameHump}"
                                                       valueFormat="YYYY-MM-DD HH:mm:ss" show-time
                                                       placeholder="请选择日期"/>
                                    </#if>
                                <#else>
                                    <a-input v-model:value="${domain}.${field.nameHump}"/>
                                </#if>
                            </a-form-item>
                        </#if>
                    </#list>
                </a-form>
            </a-modal>
        </#if>
    </div>
</template>

<script>
    import Axios from "@/api/${domain}Api";
    import {message} from "ant-design-vue";
    import {defineComponent, ref, onMounted} from "vue";

    export default defineComponent({
        name: "${do_main}-view",
        setup() {
            <#list fieldList as field>
            <#if field.enums>
            const ${field.enumsConst}_ARRAY = window.${field.enumsConst}_ARRAY;
            </#if>
            </#list>
            const visible = ref(false);
            let ${domain} = ref({
                <#list fieldList as field>
                ${field.nameHump}: undefined,
                </#list>
            });
            let loading = ref(false);
            const pagination = ref({
                total: 0,
                current: 1,
                pageSize: 2,
            });
            const ${domain}s = ref([]);

            const columns = [
                <#list fieldList as field>
                <#if field.name!="id" && field.nameHump!="createTime" && field.nameHump!="updateTime">
                {
                    title: '${field.nameCn}',
                    dataIndex: '${field.nameHump}',
                    key: '${field.nameHump}',
                },
                </#if>
                </#list>
                <#if !readOnly>
                {
                    title: '操作',
                    dataIndex: 'operation'
                }
                </#if>
            ];
            <#if !readOnly>
            const onAdd = () => {
                ${domain}.value = {};
                visible.value = true;
            };
            const onEdit = (record) => {
                ${domain}.value = {...record};
                visible.value = true;
            };
            const onDelete = (record) => {
                Axios.delete(record.id).then(res => {
                    if (res.data) {
                        message.success("删除成功");
                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize,
                        });
                    } else {
                        message.success("删除失败");
                    }
                })
            }
            const handleOk = (e) => {
                Axios.save(${domain}.value).then((res) => {
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
            </#if>

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
                        ${domain}s.value = res.data.list;
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
                <#list fieldList as field>
                <#if field.enums>
                ${field.enumsConst}_ARRAY,
                </#if>
                </#list>
                ${domain},
                visible,
                ${domain}s,
                handleTableChange,
                columns,
                onMounted,
                handleQuery,
                pagination,
                loading,
                <#if !readOnly>
                onAdd,
                handleOk,
                onEdit,
                onDelete,
                </#if>
            };
        },
    });
</script>
