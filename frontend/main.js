import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App'
import { gsap } from "gsap";
import showdown from "showdown";


// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
Vue.use(showdown);
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  app.use(ElementPlus)
  return {
    app
  }
}
// #endif