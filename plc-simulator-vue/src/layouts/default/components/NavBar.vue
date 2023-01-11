<template>
  <a-layout-header class="header">
    <div class="logo" style="font-size: x-large; color: azure">
      <router-link to="/" aria-label="Back home">
        <img src="/favicon.ico" class="nanta-logo" /> Nanta
      </router-link>
    </div>

    <a-row>
      <a-col :span="12">
        <a-menu :selectedKeys="selectedKeys" theme="dark" mode="horizontal" @select="onSelect"
          :style="{ lineHeight: '64px', fontSize: 'large' }">
          <a-menu-item v-for="item in navItems" :key="item.key">{{ item.name }}</a-menu-item>
        </a-menu>
      </a-col>
      <a-col :span="12">
        <div class="badge-items">         
          <span class="badge-item">
            <a href="https://github.com/SunlentIoT/plc-simulator" target="_blank">
              <NantaIcon icon="mdi:github" size="36"></NantaIcon>
            </a>
          </span>
        </div>
      </a-col>
    </a-row>
  </a-layout-header>
</template>

<script lang="ts" setup>
import { Nav } from "../../types/type";
import { PropType, Icon as NantaIcon } from "@nanta/ui";

const props = defineProps({
  navItems: Array as PropType<Array<Nav>>,
  selectedKeys: Array as PropType<Array<string>>,
});

const emits = defineEmits(["selectd"])

const onSelect = ({ item, key, selectedKeys }) => {
  console.log("selectd");
  console.log(item, key);
  emits("selectd", item, key);
};
</script>

<style scoped>
.logo {
  float: left;
  width: 188px;
  height: 31px;
  /*background: rgba(255, 255, 255, 0.3);*/
}

.site-layout-background {
  background: #fff;
}

.ant-layout-header {
  padding: 0 12px;
}

.nanta-logo {
  height: 60px;
  margin-bottom: 10px;
}

.badge-items {
  display: flex;
  justify-content: right;
  align-items: center;
}

.badge-item {
  margin-right: 1.5rem;
}
</style>
