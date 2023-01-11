
import appProvider from './src/AppProvider.vue';
import type { App, Plugin } from 'vue';
import { Component } from 'vue';

export const withInstall = <T>(component: T, alias?: string) => {
    const comp = component as any;
    comp.install = (app: App) => {
        app.component(comp.name || comp.displayName, component as Component);
        if (alias) {
            app.config.globalProperties[alias] = component;
        }
    };
    return component as T & Plugin;
};

export const AppProvider = withInstall(appProvider);