import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    port: 8010,
    proxy: {
      "^/api/v1": {
        target: `http://localhost:8090`,
        ws: true,
        changeOrigin: true,
      }      
    },
  },
  plugins: [vue()],
  resolve: {
    alias: {
      "/@": path.resolve(__dirname, "./src"),
    }
  },
})