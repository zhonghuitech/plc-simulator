import { createApp } from 'vue'
import App from './App.vue'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import { setupRouter } from './router';
import { setupNanta } from '@nanta/ui';
import '@nanta/ui/dist/style.css';

const app = createApp(App);
setupRouter(app);
setupNanta(app);
app.use(Antd).mount('#app')
