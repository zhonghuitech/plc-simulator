import { BasicColumn, FormSchema } from "@nanta/ui";

export const searchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: 'Name',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'gender',
    label: 'Gender',
    component: 'Select',
    componentProps: {
      options: [
        { label: 'male', value: 1 },
        { label: 'female', value: 2 },
      ],
    },
    colProps: { span: 8 },
  },
];

export const editModalSchema: FormSchema[] = [
  {
    field: 'area',
    label: 'Area',
    component: 'Select',
    colProps: { span: 24 },
    defaultValue: 'QNA3E',
    componentProps: {
      options: [
        {
          label: 'MELSEC QNA3E',
          value: 'QNA3E',
          key: 'QNA3E',
        },
        {
          label: 'MODBUS',
          value: 'MODBUS',
          key: 'MODBUS',
        },
        {
          label: 'SIEMENS S7',
          value: 'S7WORKER',
          key: 'S7WORKER',
        },
        {
          label: 'OMRON',
          value: 'OMRON',
          key: 'OMRON',
        },
      ],
    },
  }, 
  {
    field: 'address',
    label: 'Address',
    component: 'Input', 
    colProps: { span: 24 },
  }
]

export const columns: BasicColumn[] = [
  {
    title: "Address",
    dataIndex: "address",
    key: "address",
  },
  {
    title: "Value",
    dataIndex: "value",
    key: "value",
  },  
];