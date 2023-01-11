import { BasicColumn, CellFormat, FormSchema } from "@nanta/ui";

const GENDER = {
  1: 'male',
  2: 'female'
};

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
    field: 'id',
    label: 'id',
    component: 'Input',
    ifShow: false,
    colProps: { span: 24 },
  }, 
  {
    field: 'name',
    label: 'Name',
    component: 'Input',
    colProps: { span: 24 },
  }, 
  {
    field: 'age',
    label: 'Age',
    component: 'InputNumber', 
    colProps: { span: 24 },
  },
  {
    field: 'email',
    label: 'Email',
    component: 'Input', 
    colProps: { span: 24 },
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
    colProps: { span: 12 },
  },
  {
    field: 'address',
    label: 'address',
    component: 'InputTextArea',
    colProps: { span: 24 },
  }, 
  {
    field: 'tags',
    label: 'Tags',
    component: 'Select',
    colProps: { span: 24 },
    slot: 'selectTag',
    placeholder: 'Please input your tag and then press return.'
  }, 
]

export const editModalSchema2: FormSchema[] = [
  {
    field: 'name2',
    label: 'Name2',
    component: 'Input',
    colProps: { span: 24 },
  }, 
  {
    field: 'age2',
    label: 'Age2',
    component: 'InputNumber', 
    colProps: { span: 24 },
  }
]

export const columns: BasicColumn[] = [
  {
    title: "Address",
    dataIndex: "name",
    key: "name",
  },
  {
    title: "Value",
    dataIndex: "age",
    key: "age",
  },  
];

export const data = [
  {
    key: "1",
    name: "John Brown",
    age: 32,
    email: "john.brown@gmail.com",
    address: "New York No. 1 Lake Park",
    tags: ["nice", "developer"],
    gender: 1,
  },
  {
    key: "2",
    name: "Jim Green",
    email: "jim.green@gmail.com",
    age: 42,
    address: "London No. 1 Lake Park",
    tags: ["loser"],
    gender: 1,
  },
  {
    key: "3",
    name: "Joe Black",
    age: 32,
    email: "joe.black@gmail.com",
    address: "Sidney No. 1 Lake Park",
    tags: ["cool", "teacher"],
    gender: 2,
  },
  {
    key: "4",
    name: "Aborn Jiang",
    age: 32,
    email: "aborn.jiang@gmail.com",
    address: "Shanghai China",
    tags: ["coder", "engineer"],
  },
  {
    key: "5",
    name: "Jack Gre",
    age: 32,
    email: "jack.gre@gmail.com",
    address: "Hangzhou China",
    tags: ["coder", "engineer"],
  },
  {
    key: "6",
    name: "Kitty Hello",
    age: 32,
    email: "he@gmail.com",
    address: "Berlin No. 991 Geek Park",
    tags: ["coder", "engineer"],
    gender: 3,
  },
  {
    key: "7",
    name: "Mike",
    age: 32,
    email: "mike@gmail.com",
    address: "Paris No. 1 Lake Park",
    tags: ["coder", "engineer"],
  },
  {
    key: "8",
    name: "Cook",
    age: 67,
    email: "cook@gmail.com",
    address: "Taipei No. 101 Tower",
    tags: ["coder", "engineer"],
  },
  {
    key: "9",
    name: "Jack Ma",
    age: 42,
    email: "jack.ma@gmail.com",
    address: "Paris No. 1 Lake Park",
    tags: ["coder", "engineer"],
  },
  {
    key: "10",
    name: "Xu",
    age: 10,
    email: "xu@gmail.com",
    address: "Nanjing No. 1 Golden Park",
    tags: ["coder", "engineer"],
  },
  {
    key: "11",
    name: "Google",
    age: 33,
    email: "google@gmail.com",
    address: "Osaka No. 1 Lake Park",
    tags: ["coder", "engineer"],
  },
  {
    key: "12",
    name: "Orode100",
    age: 100,
    email: "100@gmail.com",
    address: "London No. 1 Center Park",
    tags: ["coder", "engineer"],
  },
];