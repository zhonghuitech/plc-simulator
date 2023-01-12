<template>
    <NantaTable @register="registerTable"
        :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }"
        :clickToRowSelect="false">
        <template #headerTop>
            <div style="margin-bottom: 10px;">
                <NantaButton type="primary" @click="handleCreate" :disabled="!operation.createEnabled" class="button-s"
                    preIcon="ic:baseline-plus">Add Reg</NantaButton>
                <NantaButton type="primary" danger @click="handleMultiDelete" :disabled="!operation.deleteEnabled"
                    class="button-s" preIcon="ic:baseline-delete">Delete</NantaButton>
            </div>
            <a-alert type="info" show-icon>
                <template #message>
                    <template v-if="checkedKeys.length > 0">
                        <span>Selected {{ checkedKeys.length }} items (pagination support.)</span>
                        <a-button type="link" @click="checkedKeys = []" size="small">清空</a-button>
                    </template>
                    <template v-else>
                        <span>No items selected.</span>
                    </template>
                </template>
            </a-alert>
        </template>
        <template #toolbar>
            <div>
                <NantaButton type="primary" class="button-s">
                    Show details
                </NantaButton>
            </div>
        </template>
        <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'action'">
                <NantaTableAction :actions="getAction(record)" />
            </template>
            <template v-else-if="column.key === 'tags'">
                <span>
                    <a-tag v-for="tag in record.tags" :key="tag"
                        :color="tag === 'loser' ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'">
                        {{ tag.toUpperCase() }}
                    </a-tag>
                </span>
            </template>
        </template>
    </NantaTable>
    <NantaFormModal @register="registerModal" v-bind="mProps" @ok="handleOK" @cancel="handleCancel" />
    <NantaFormModal @register="registerModal2" v-bind="mProps2" @ok="handleOK2" @cancel="handleCancel2" />
</template>

<script lang="ts" setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import { NantaTable, NantaTableAction, useTable, ActionItem, NantaFormModal, ModalInnerRecord, NantaFormModalProps, NantaButton, useModal, Recordable } from "@nanta/ui";
import { columns, searchFormSchema, editModalSchema, editModalSchema2 } from "./data"
import { ActionType } from './type'
import { createAxiosFetch } from '/@/utils/http/axiosFetch';
const url = 'http://127.0.0.1:8090/api/v1/read';

const checkedKeys = ref<Array<string | number>>([]);
const operation = ref({ copyEnabled: false, createEnabled: true, modifyEnabled: false, deleteEnabled: false });

function getAction(record: Recordable): ActionItem[] {
    const ifShow = (action: ActionItem) => {
        const value = (record.gender && (record.gender === 1 || record.gender === 2));
        if (!value && action.label === 'Delete') {
            return false;
        }
        return true;
    };

    const actions: ActionItem[] = [
        {
            icon: 'clarity:note-edit-line',
            label: 'Edit',
            onClick: handleEdit.bind(null, record),
        },
        {
            icon: 'ant-design:delete-outlined',
            color: 'error',
            label: 'Delete',
            onClick: handleDelete.bind(null, record),
        },
    ]
    actions.forEach(item => { item.ifShow = ifShow })

    return actions;
}

interface RegData {
    address: string;
    value: string;
}

function transfer(params: RegData[]) {
    const tData = params.map((item) => {
        return {
            address: item.address,
            value: item.value,
        }
    })
    return tData;
}

const fetchSetting = {
    pageField: 'page',
    sizeField: 'pageSize',
    listField: 'content',
    totalField: 'totalElements',
};

const [registerTable, { updateTableDataRecord, deleteTableDataRecord, findTableDataRecord, reload }] = useTable({
    title: 'PLC Simulator Example.',
    columns,
    api: createAxiosFetch(url),
    afterFetch: transfer,
    fetchSetting,
    actionColumn: {
        title: 'Actions',
        dataIndex: 'action',
        // slots: { customRender: 'action' },
        fixed: undefined,
    },
    useSearchForm: false,
    searchFormConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
        autoSubmitOnEnter: true,
        submitButtonOptions: { text: 'search' }
    },
})

const mProps: NantaFormModalProps = {
    schemas: editModalSchema,
    colon: true,
    modalProps: {
        okText: "I'm sure.",
        cancelText: 'Reject',
    }
}

const [registerModal, { openModal, closeModal, setModalProps, changeLoading, changeOkLoading }] = useModal();

const mProps2: NantaFormModalProps = {
    schemas: editModalSchema2,
    colon: true
}

const handleOK = async (newRecord: Recordable, oldRecord: Recordable) => {
    console.log('handleOK in outer event callback', newRecord, oldRecord)
    updateTableDataRecord(oldRecord.key, newRecord)
    changeLoading(true);
    changeOkLoading(true);
    // closeModal()
    changeOkLoading(false)
    changeLoading(false)
}

const handleCancel = (newRecord: Recordable, oldRecord: Recordable) => {
    console.log('handle cancel in outer event callback', newRecord, oldRecord);
}

const [registerModal2, { openModal: openModal2, closeModal: closeModal2 }] = useModal();

const handleOK2 = (newRecord: Recordable, oldRecord: Recordable) => {
    console.log('handleOK2 in outer event callback', newRecord, oldRecord)
    updateTableDataRecord(oldRecord.key, newRecord)
    closeModal()
}

const handleCancel2 = (newRecord: Recordable, oldRecord: Recordable) => {
    console.log('handle cancel in outer event callback', newRecord, oldRecord);
}

function handleEdit(record: Recordable) {
    console.log('edit clicked!');
    console.log(record);
    const innerRecord: ModalInnerRecord = {
        title: "Edit",
        record
    }

    openModal(true, innerRecord)
}

function handleDelete(record: Recordable) {
    console.log('delete action clicked!');
    console.log(record);
}

function handleCopyCreate() {
    console.log('copycreate');
    if (checkedKeys.value.length > 0) {
        const key = checkedKeys.value[0];
        const record = findTableDataRecord(key)
        doModifyAction(key, ActionType.COPY_CREATE, record as Recordable);
    }
}

function handleCreate() {
    doModifyAction(0, ActionType.CREATE);
}

function handleModify() {
    console.log('modify');
    if (checkedKeys.value.length > 0) {
        const key = checkedKeys.value[0];
        const record = findTableDataRecord(key)
        doModifyAction(key, ActionType.MODIFY, record as Recordable);
    }
}

function handleMultiDelete() {
    console.log('delete', checkedKeys);
}

const doModifyAction = (id: string | number, type: ActionType, record?: Recordable) => {
    console.log('id', id, 'type', type);
    let title: string = "Create"
    if (type == ActionType.CREATE) {
        title = "Create"
    } else if (type == ActionType.COPY_CREATE) {
        title = "Copy create"
    } else if (type == ActionType.MODIFY) {
        title = "Modify"
    } else {
        throw new Error('illegal type.');
    }

    const innerRecord: ModalInnerRecord = {
        title,
        record: record || {}
    }

    if (type == ActionType.CREATE) {
        openModal2(true, innerRecord);
    } else {
        openModal(true, innerRecord)
    }
};

function onSelectChange(selectedRowKeys: (string | number)[]) {
    console.log(selectedRowKeys);
    checkedKeys.value = selectedRowKeys;
    const size = selectedRowKeys.length;
    if (size <= 0) {
        operation.value = { copyEnabled: false, createEnabled: true, modifyEnabled: false, deleteEnabled: false };
    } else if (size == 1) {
        operation.value = { copyEnabled: true, createEnabled: true, modifyEnabled: true, deleteEnabled: true };
    } else {
        operation.value = { copyEnabled: false, createEnabled: true, modifyEnabled: false, deleteEnabled: true };
    }
}

let timer: any = null;
onMounted(() => {
    timer = setInterval(reload, 1000);
})

onBeforeUnmount(() => {
    if (timer) {
        clearInterval(timer);
    }
})
</script>

<style scoped>
.button-s {
    margin-right: 4px;
}

.header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: large;
    font-weight: 500;
}
</style>