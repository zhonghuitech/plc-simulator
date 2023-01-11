<template>
  <a-layout>
    <NavBar :navItems="navItems" :selectedKeys="selectedNavKeys" @selectd="onNavSelected" />
    <a-layout>
      <SideBar :sideMenus="sideMenus" @menu-selected="onSideSelected" :selectedKeys="selectedKeys"
        :openKeys="openKeys" />
      <a-layout style="padding: 0 24px 24px">
        <a-breadcrumb style="margin: 16px 0">
          <a-breadcrumb-item v-for="item in breadcrumbList" :key="item">{{
              item
          }}</a-breadcrumb-item>
        </a-breadcrumb>
        <a-layout-content>
          <slot />
        </a-layout-content>
      </a-layout>
    </a-layout>
    <Footer />
  </a-layout>
</template>

<script lang="ts" >
import { Menu, Nav } from "./types/type";
import NavBar from "./default/components/NavBar.vue";
import SideBar from "./default/components/SideBar.vue";
import Footer from "./default/components/Footer.vue";
import { makeid } from "./default/index";
import { ref, computed, onMounted, defineComponent } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getMenus, iteratorMenu, getMenuNode, findItemByPath, navItems } from "./menu";

export default defineComponent({
  components: { NavBar, SideBar, Footer },
  setup(props, { slots }) {
    const selectedKeys = ref<string[]>([]);
    const selectedNavKeys = ref<string[]>([]);
    const openKeys = ref<string[]>([]);

    const breadcrumbList = ref<string[]>([]);
    const sideMenus = getMenus();
    const route = useRoute();

    const menuNodes = getMenuNode(getMenus());
    menuNodes.forEach((item: Menu) => {
      item.key && openKeys.value.push(item.key);
    })

    const getKeyAttributeMenuMap = (() => {
      const res = {};

      // make sure each menu has key attribute else random makeid!
      sideMenus.forEach((item: Menu) => {
        iteratorMenu(
          item,
          (item, keyPath) => {
            if (!item.key || item.key.length == 0) {
              item.key = makeid(5);
            }
            item.keyPath = [];
            if (keyPath && keyPath.length > 0) {
              keyPath.forEach((i) => {
                item.keyPath && item.keyPath.push(i);
              });
            }
            item.keyPath.push(item.key);
          },
          []
        );
      });

      console.log(sideMenus);

      sideMenus.forEach((item: Menu) => {
        iteratorMenu(item, (item) => {
          const { name, path } = item;
          if (item.key) {
            res[item.key] = { name, path };
          }
        });
      });

      return res;
    })();

    function findKeyPath(path: string) {
      let res: string[] | undefined;
      sideMenus.forEach((item) => {
        iteratorMenu(item, (item) => {
          if (item.path === path) {
            res = item.keyPath;
          }
        });
      });
      return res;
    }

    function initBreadcrumbList(keyPath: string[] | undefined) {
      if (!keyPath) {
        return;
      }
      // init it!
      breadcrumbList.value = [];
      if (keyPath && keyPath.length > 0) {
        for (let i = 0; i < keyPath.length; i++) {
          if (getKeyAttributeMenuMap[keyPath[i]]) {
            breadcrumbList.value.push(getKeyAttributeMenuMap[keyPath[i]].name);
          }
        }
      }
    }

    const router = useRouter()

    const onSideSelected = (item: any) => {
      initBreadcrumbList(item.keyPath);
      const activeSide = activeSideBar(null, item.key);
      selectedNavKeys.value.pop()
      if (activeSide && activeSide.group) {
        activeNavbar(activeSide.group);
      }
    }

    const onNavSelected = (item: Nav, key: string) => {
      console.log('selectd key', key)
      const nav = navItems.find(item => item.key === key);
      if (nav && nav.path) {
        router.push(nav.path)
        activeNavbar(nav.group)
      }
    }

    function activeNavbar(group?: string) {
      const activeNav = navItems.find(item => item.group === group);
      if (activeNav && activeNav.key) {
        selectedNavKeys.value.pop()
        selectedNavKeys.value.push(activeNav.key);
      }
    }

    function activeSideBar(path: string | null, key?: string): Menu | undefined {
      const activeSide = findItemByPath(sideMenus, path, key);
      if (activeSide && activeSide.key) {
        selectedKeys.value.pop();
        selectedKeys.value.push(activeSide.key);
        return activeSide;
      }
    }

    function initActiveNavAndSider(path: string) {
      const activeSide = activeSideBar(path);

      initBreadcrumbList(findKeyPath(path));
      if (activeSide && activeSide.group) {
        activeNavbar(activeSide.group)
      }
    }

    onMounted(async () => {
      // await router.isReady()
      // initActiveNavAndSider(route.path)
    })

    return {
      navItems,
      selectedNavKeys,
      onNavSelected,
      onSideSelected,
      selectedKeys,
      breadcrumbList,
      openKeys,
      sideMenus,
      initActiveNavAndSider
    }
  },
  watch: {
    $route(to, from) {
      console.log('change route from:' + from.path + ' -> to:' + to.path)
      this.initActiveNavAndSider(to.path)
    },
  },
})
</script>

<style>
.site-layout-background {
  background: #fff;
}
</style>
