import { EXCEPTION_COMPONENT, PAGE_NOT_FOUND_NAME, E404_COMPONENT } from '/@/router/constant';

export const basicRouteItems = [
    { path: '/', name: 'Home', component: () => import('/@/pages/simulator/index.vue') },    
    
    { path: '/simulator/index', name: 'Simulator', group: 'components', component: () => import('/@/pages/simulator/index.vue') },
    
    { path: '/404', name: '404Page', component: E404_COMPONENT },
    { path: '/:path(.*)*', name: PAGE_NOT_FOUND_NAME, component: EXCEPTION_COMPONENT, }
]