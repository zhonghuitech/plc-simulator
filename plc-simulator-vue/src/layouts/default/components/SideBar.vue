<template>
  <a-layout-sider width="200" style="background: #fff">
    <!-- eslint-disable -->
    <a-menu :selectedKeys="selectedKeys" :openKeys="openKeys" mode="inline" @select="onSelect"
      :style="{ height: '100%', borderRight: 0 }">
      <basic-sub-menu v-for="menu in sideMenus" :item="menu" :key="menu.key" />
    </a-menu>
  </a-layout-sider>
</template>

<script lang="ts" setup>
import { Menu } from '../../types/type';
import BasicSubMenu from './BasicSubMenu.vue';
import { PropType } from "@nanta/ui";
const props = defineProps({
  sideMenus: Array as PropType<Array<Menu>>,
  selectedKeys: Array as PropType<Array<string>>,
  openKeys: Array as PropType<Array<string>>,
})

console.log(props.selectedKeys);

const emit = defineEmits(['menu-selected']);
const onSelect = e => {
  const { key, keyPath } = e;
  // console.log(e)
  emit('menu-selected', { key, keyPath })
}
</script>

<style scoped>
.site-layout-background {
  background: #fff;
}
</style>
