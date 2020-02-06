import Vue from 'vue'
import App from './App.vue'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.css'

Vue.use(Vuetify)
Vue.use(require('vue-moment'));

new Vue({
  el: '#app',
  render: h => h(App)
})
