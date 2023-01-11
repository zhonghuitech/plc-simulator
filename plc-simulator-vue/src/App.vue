<template>
  <div>
    <ConfigProvider>
      <AppProvider>
        <DefaultLayout>
          <RouterView />
        </DefaultLayout>
      </AppProvider>
    </ConfigProvider>
  </div>
</template>

<script lang="ts" setup>
import { ConfigProvider } from 'ant-design-vue';
import { AppProvider } from '/@/components/Application';
import DefaultLayout from './layouts/default.vue';
</script>
