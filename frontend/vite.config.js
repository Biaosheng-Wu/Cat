import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'
import basicSsl from '@vitejs/plugin-basic-ssl'
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    uni(),
    basicSsl(),
  ],
  server: {
    https: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
